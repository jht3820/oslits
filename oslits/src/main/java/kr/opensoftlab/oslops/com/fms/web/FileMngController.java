package kr.opensoftlab.oslops.com.fms.web;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.PrintWriter;
import java.net.URLEncoder;
import java.nio.charset.Charset;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.zip.ZipEntry;
import java.util.zip.ZipOutputStream;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import kr.opensoftlab.oslops.com.fms.web.service.FileMngService;
import kr.opensoftlab.oslops.com.vo.LoginVO;
import kr.opensoftlab.oslops.req.req4000.req4800.service.Req4800Service;
import kr.opensoftlab.sdf.util.OslAgileConstant;
import kr.opensoftlab.sdf.util.ReqHistoryMngUtil;
import kr.opensoftlab.sdf.util.RequestConvertor;

import org.apache.commons.io.FileUtils;
import org.apache.log4j.Logger;
import org.jasypt.contrib.org.apache.commons.codec_1_3.binary.Base64;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.util.FileCopyUtils;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;

import egovframework.com.cmm.EgovMessageSource;
import egovframework.com.cmm.EgovWebUtil;
import egovframework.com.cmm.service.EgovFileMngService;
import egovframework.com.cmm.service.EgovFileMngUtil;
import egovframework.com.cmm.service.EgovProperties;
import egovframework.com.cmm.service.FileVO;
import egovframework.com.cmm.util.EgovBasicLogger;
import egovframework.com.cmm.util.EgovResourceCloseHelper;
/**
 * @Class Name : FileMngController.java
 * @Description : FileMngController Controller class
 * @Modification Information
 *
 * @author 진주영
 * @since 2016.02.13
 * @version 1.0
 * @see
 *  
 *  Copyright (C) OpenSoftLab Corp All right reserved.
 */

@Controller
public class FileMngController {
	/** 로그4j 로거 로딩 */
	private final Logger Log = Logger.getLogger(this.getClass());
	
	/** EgovMessageSource */
	@Resource(name = "egovMessageSource")
	EgovMessageSource egovMessageSource;
	
	/** FileMngService */
   	@Resource(name="fileMngService")
   	private FileMngService fileMngService;

   	/** Req4800Service DI */
	@Resource(name = "req4800Service")
	private Req4800Service req4800Service;
	
	@Resource(name="EgovFileMngUtil")
	private EgovFileMngUtil fileUtil;
	
	@Resource(name = "historyMng")
	private ReqHistoryMngUtil historyMng;
	
	/** 파일 제어 서비스 */
	@Resource(name = "EgovFileMngService")
	private EgovFileMngService fileService;
	/**
	 * 브라우저 구분 얻기.
	 *
	 * @param request
	 * @return
	 */
	private String getBrowser(HttpServletRequest request) {
		String header = request.getHeader("User-Agent");
		if (header.indexOf("MSIE") > -1) {
			return "MSIE";
		} else if (header.indexOf("Trident") > -1) { // IE11 문자열 깨짐 방지
			return "Trident";
		} else if (header.indexOf("Chrome") > -1) {
			return "Chrome";
		} else if (header.indexOf("Opera") > -1) {
			return "Opera";
		}
		return "Firefox";
	}

	/**
	 * Disposition 지정하기.
	 *
	 * @param filename
	 * @param request
	 * @param response
	 * @throws Exception
	 */
	private void setDisposition(String filename, HttpServletRequest request, HttpServletResponse response) throws Exception {
		String browser = getBrowser(request);

		String dispositionPrefix = "attachment; filename=";
		String encodedFilename = null;

		if (browser.equals("MSIE")) {
			encodedFilename = URLEncoder.encode(filename, "UTF-8").replaceAll("\\+", "%20");
		} else if (browser.equals("Trident")) { // IE11 문자열 깨짐 방지
			encodedFilename = URLEncoder.encode(filename, "UTF-8").replaceAll("\\+", "%20");
		} else if (browser.equals("Firefox")) {
			encodedFilename = "\"" + new String(filename.getBytes("UTF-8"), "8859_1") + "\"";
		} else if (browser.equals("Opera")) {
			encodedFilename = "\"" + new String(filename.getBytes("UTF-8"), "8859_1") + "\"";
		} else if (browser.equals("Chrome")) {
			StringBuffer sb = new StringBuffer();
			for (int i = 0; i < filename.length(); i++) {
				char c = filename.charAt(i);
				if (c > '~') {
					sb.append(URLEncoder.encode("" + c, "UTF-8"));
				} else {
					sb.append(c);
				}
			}
			encodedFilename = sb.toString();
		} else {
			throw new IOException("Not supported browser");
		}

		response.setHeader("Content-Disposition", dispositionPrefix + encodedFilename);

		if ("Opera".equals(browser)) {
			response.setContentType("application/octet-stream;charset=UTF-8");
		}
	}
	
	/**
	 * 첨부파일로 등록된 PDF파일에 대하여 뷰어 페이지를 제공한다.
	 *
	 * @param commandMap
	 * @param response
	 * @throws Exception
	 */
	@RequestMapping(value = "/com/fms/pdfViewerPage.do")
	public String pdfViewerPage(@RequestParam Map<String, Object> commandMap, HttpServletRequest request, HttpServletResponse response, ModelMap model) throws Exception {
		String atchFileId = (String) commandMap.get("downAtchFileId");
		String fileSn = (String) commandMap.get("downFileSn");
		
		
		StringBuffer str = new StringBuffer();
		
		//파일 다운 없이 iframe 호출인 경우 <form>만 생성
		if(!"".equals(atchFileId) && atchFileId != null && !"".equals(fileSn) && fileSn != null){
			//로그인VO 가져오기
			HttpSession ss = request.getSession();
			LoginVO loginVO = (LoginVO) ss.getAttribute("loginVO");
			if(loginVO != null){
				
				//파일 정보 불러오기
				FileVO fileVO = new FileVO();
				fileVO.setAtchFileId(atchFileId);
				fileVO.setFileSn(fileSn);
				FileVO fvo = fileMngService.selectFileInf(fileVO);
				
				//DB에 조회하려는 파일이 존재하지 않는 경우
				if(fvo == null){
					str.append("<script>alert('"+egovMessageSource.getMessage("com.fail.file.select")+"');</script>");
				}else{
					//File 객체 생성 (실제 경로에서 해당 파일을 불러온다.)
					File uFile = new File(fvo.getFileStreCours(), fvo.getStreFileNm());
					long fSize = uFile.length();

					//파일 사이즈가 0 이상인 경우(0 byte이하는 파일이 없는 경우로 처리)
					if (fSize > 0) {
						byte[] pdfByteArray = FileUtils.readFileToByteArray(uFile);
						String pdfData = new String(Base64.encodeBase64(pdfByteArray), Charset.forName("UTF-8"));
						model.addAttribute("pdfData",pdfData);

						model.addAttribute("atchFileId",atchFileId);
						model.addAttribute("fileSn",fileSn);
						model.addAttribute("fileNm",fvo.getOrignlFileNm());
						
						return "/com/fms/pdfViewerPage";
					}else{
						str.append("<script>alert('"+egovMessageSource.getMessage("com.fail.file.select")+"');</script>");
					}
					
				}
				
			}
		}
		//ContentType 설정
		String mimetype = "text/html; charset=UTF-8";
		//response 설정
		response.setContentType(mimetype);
		
		PrintWriter printwriter = response.getWriter();
		printwriter.println("<html><form name='downForm'></form>");
		printwriter.println(str);
		printwriter.println("</html>");
		printwriter.flush();
		printwriter.close();
		return "/err/file";
	}
	
	/**
	 * 첨부파일로 등록된 파일에 대하여 다운로드를 제공한다.
	 *
	 * @param commandMap
	 * @param response
	 * @throws Exception
	 */
	@RequestMapping(value = "/com/fms/FileDown.do")
	public String FileDownload(@RequestParam Map<String, Object> commandMap, HttpServletRequest request, HttpServletResponse response, ModelMap model) throws Exception {
		String atchFileId = (String) commandMap.get("downAtchFileId");
		String fileSn = (String) commandMap.get("downFileSn");
		
		//ContentType 설정
		String mimetype = "text/html; charset=UTF-8";
		//response 설정
		response.setContentType(mimetype);
		StringBuffer str = new StringBuffer();
		
		//파일 다운 없이 iframe 호출인 경우 <form>만 생성
		if(!"".equals(atchFileId) && atchFileId != null && !"".equals(fileSn) && fileSn != null){
			//로그인VO 가져오기
			HttpSession ss = request.getSession();
			LoginVO loginVO = (LoginVO) ss.getAttribute("loginVO");
			if(loginVO != null){
				
				//파일 정보 불러오기
				FileVO fileVO = new FileVO();
				fileVO.setAtchFileId(atchFileId);
				fileVO.setFileSn(fileSn);
				FileVO fvo = fileMngService.selectFileInf(fileVO);
				
				//DB에 조회하려는 파일이 존재하지 않는 경우
				if(fvo == null){
					str.append("<script>alert('"+egovMessageSource.getMessage("com.fail.file.select")+"');</script>");
				}else{
					//File 객체 생성 (실제 경로에서 해당 파일을 불러온다.)
					File uFile = new File(fvo.getFileStreCours(), fvo.getStreFileNm());
					long fSize = uFile.length();
					
					//파일 사이즈가 0 이상인 경우(0 byte이하는 파일이 없는 경우로 처리)
					if (fSize > 0) {
						
						//파일 다운 setDisposition 지정 (다운 받아지는 파일의 이름 정의) 
						setDisposition(fvo.getOrignlFileNm(), request, response);
						
						BufferedInputStream in = null;
						BufferedOutputStream out = null;

						try {
							in = new BufferedInputStream(new FileInputStream(uFile));
							out = new BufferedOutputStream(response.getOutputStream());
							FileCopyUtils.copy(in, out);

							//버퍼 비우기
							out.flush();
						} catch (IOException ex) {
							EgovBasicLogger.ignore("IO Exception", ex);
						} finally {
							//파일 읽,쓰기 종료
							EgovResourceCloseHelper.close(in, out);
						}
						return "/err/file";
					}else{
						str.append("<script>alert('"+egovMessageSource.getMessage("com.fail.file.select")+"');</script>");
					}
					
				}
				
			}
		}
		PrintWriter printwriter = response.getWriter();
		printwriter.println("<html><form name='downForm'></form>");
		printwriter.println(str);
		printwriter.println("</html>");
		printwriter.flush();
		printwriter.close();
		return "/err/file";
	}
	
	/**
	 * 첨부파일로 등록된 파일에 대하여 압축 다운로드를 제공한다.
	 *
	 * @param commandMap
	 * @param response
	 * @throws Exception
	 */
	@RequestMapping(value = "/com/fms/ZipFileDown.do")
	public String ZipFileDown(@RequestParam Map<String, Object> commandMap,HttpServletRequest request, HttpServletResponse response, ModelMap model) throws Exception {
		
		String[] downFile = (String[]) request.getParameterValues("downFile");
		
    	
		//ContentType 설정
		String mimetype = "text/html; charset=UTF-8";
		//response 설정
		response.setContentType(mimetype);
		StringBuffer str = new StringBuffer();
		
		//파일 다운 없이 iframe 호출인 경우 <form>만 생성
		if(downFile != null && downFile.length > 0){
			//로그인VO 가져오기
			HttpSession ss = request.getSession();
			LoginVO loginVO = (LoginVO) ss.getAttribute("loginVO");
			if(loginVO != null){
				//파일 버퍼
		    	byte[] buf = new byte[4096];
		    	
				//저장 경로
	        	String storePathString = EgovProperties.getProperty("Globals.fileStorePath");
	        	
	        	//임시 압축파일 저장 경로
	        	String addPath = "tempZip/";
	        	
	        	//압축 파일
	        	String zipFilePath = EgovWebUtil.filePathBlackList(storePathString+addPath);
	        	
	        	//디렉토리 없는경우 생성
	        	File saveFolder = new File(zipFilePath);

	    		if (!saveFolder.exists() || saveFolder.isFile()) {
	    			saveFolder.mkdirs();
	    		}
	    		
	    		Date today = new Date();
	    		DateFormat fm = new SimpleDateFormat("yyyyMMddHHmmss");
	    		
	    		//압축 파일명
	    		String zipFileName = "_OSL"+fm.format(today)+".zip";
	    		
	    		//압축 파일 생성
	    		ZipOutputStream zipOut = null;
	    		
	    		//압축 파일 생성 체크
	    		boolean zipMakeChk = true;
	    		try{
	    			//압축 파일 생성
	    			zipOut = new ZipOutputStream(new FileOutputStream(zipFilePath+zipFileName));
		    		
	    			
	    			//FileSns 루프 돌면서 파일 데이터 가져오기
		        	for(int i=0;i<downFile.length;i++){
		        		String[] split_file = downFile[i].split(";");
		        		String split_atchFileId = split_file[0];
		        		String split_fileSn = split_file[1];
		        		String split_dirNm = split_file[2];
		        		
		        		//디렉토리 치환
		        		split_dirNm = split_dirNm.replaceAll("/", "-");
		        		
		        		FileVO fileVO = new FileVO();
		    			fileVO.setAtchFileId(split_atchFileId);
		    			fileVO.setFileSn(split_fileSn);
		    			FileVO fvo = fileService.selectFileInf(fileVO);
		    			
		    			File uFile = new File(fvo.getFileStreCours(), fvo.getStreFileNm());
		    			
		    			if(!uFile.isFile()){
			    			str.append("<script>alert('"+fvo.getOrignlFileNm()+" 파일이 서버에 존재하지 않습니다.');</script>");
			    			
			    			zipOut.close();
			    			
			    			//압축 실패
			    			zipMakeChk = false;
			    			break;
		    			}
		    			FileInputStream in = new FileInputStream(uFile);
		    	        String fileName = "file_"+i+"_"+fvo.getOrignlFileNm().toString();
		    	                
		    	        ZipEntry ze = new ZipEntry(split_dirNm+"/"+fileName);

		    	        zipOut.putNextEntry(ze);
		    	          
		    	        int len;
		    	        while ((len = in.read(buf)) > 0) {
		    	        	zipOut.write(buf, 0, len);
		    	        }
		    	          
		    	        zipOut.closeEntry();
		    	        in.close();
		        	}
		    		
		    		
		        	zipOut.close();
		    		
	    		}catch (IOException ioe) {
	    			Log.error("selectReq4104FileZipUpload()", ioe);
	    			str.append("<script>alert('"+egovMessageSource.getMessage("com.fail.file.select")+"');</script>");
	    			
	    			zipOut.close();
	    			
	    			//압축 실패
	    			zipMakeChk = false;
	    		}catch(Exception e){
    				Log.error(e);
    				
    				str.append("<script>alert('"+egovMessageSource.getMessage("com.fail.file.select")+"');</script>");
	    			
	    			zipOut.close();
	    			
	    			//압축 실패
	    			zipMakeChk = false;
    			}
	    		
	    		//압축 성공한 경우에 파일 다운로드&압축파일 삭제 진행
	    		if(zipMakeChk){
		    		//File 객체 생성 (실제 경로에서 해당 파일을 불러온다.)
					File uFile = new File(zipFilePath, zipFileName);
					long fSize = uFile.length();
					//파일 사이즈가 0 이상인 경우(0 byte이하는 파일이 없는 경우로 처리)
					if (fSize > 0) {
						
						//파일 다운 setDisposition 지정 (다운 받아지는 파일의 이름 정의) 
						setDisposition(zipFileName, request, response);
						
						BufferedInputStream in = null;
						BufferedOutputStream out = null;
	
						try {
							in = new BufferedInputStream(new FileInputStream(uFile));
							out = new BufferedOutputStream(response.getOutputStream());
							FileCopyUtils.copy(in, out);
	
							//버퍼 비우기
							out.flush();
						} catch (IOException ex) {
							EgovBasicLogger.ignore("IO Exception", ex);
						} finally {
							//파일 읽,쓰기 종료
							EgovResourceCloseHelper.close(in, out);
							
							//파일 물리적 삭제
							String fileDeletePath  = zipFilePath+zipFileName;
						    String deleteChk = EgovFileMngUtil.deleteFile(fileDeletePath);
						    
						    Log.debug("Delete Result"+deleteChk);
						}
						return "/err/file";
					}else{
						
						str.append("<script>alert('"+egovMessageSource.getMessage("com.fail.file.select")+"');</script>");
					}
				}
			}
		}
		PrintWriter printwriter = response.getWriter();
		printwriter.println("<html><form name='downForm'></form>");
		printwriter.println(str);
		printwriter.println("</html>");
		printwriter.flush();
		printwriter.close();
		return "/err/file";
	}
	
	/**
	 * 파일을 삭제한다.
	 * - 삭제하기 전 atchFileId가 1개 존재하는 경우, COMTNFILE의 atchFileId USE_AT값을 N으로 변경한다.
	 * - DB 제거와 함께 물리적 경로에서도 삭제한다.
	 * @param request
	 * @param response
	 * @param model
	 * @return
	 * @throws Exception
	 */
	@RequestMapping(value="/com/fms/FileDelete.do")
	public ModelAndView FileDelete(HttpServletRequest request, HttpServletResponse response, ModelMap model ) throws Exception {

		try{

			// request 파라미터를 map으로 변환
			Map<String, String> paramMap = RequestConvertor.requestParamToMapAddSelInfo(request, true);
			paramMap.put("prjId", request.getSession().getAttribute("selPrjId").toString());
			
			//삭제 유형 (default = "normal" , 요구사항 첨부파일 삭제 = "request")
			String deleteType = (String)paramMap.get("deleteType");
			
			//FileVO 파라미터 세팅
			FileVO fileVo = new FileVO();
			fileVo.setAtchFileId(paramMap.get("atchFileId"));
			fileVo.setFileSn(paramMap.get("fileSn"));
			
			//파일의 단일 정보 불러오기
			fileVo = fileMngService.selectFileInf(fileVo);
			
			//atchFileId가 없을 경우 에러
			if(fileVo == null || "".equals(fileVo.getAtchFileId()) || fileVo.getAtchFileId() == null){
				model.addAttribute("Success", "N");	
				model.addAttribute("nonFile", "Y");	
				model.addAttribute("message", egovMessageSource.getMessage("fail.common.nonDelete"));
				return new ModelAndView("jsonView");	
			}
			fileMngService.deleteFileInf(fileVo);

			//파일 물리적 삭제
			String fileDeletePath  = fileVo.getFileStreCours()+fileVo.getStreFileNm();
		    String delResult       =  EgovFileMngUtil.deleteFile(fileDeletePath);
		    System.out.println(delResult);
			model.put("Success", "Y");
			
			//요구사항에서 파일 삭제인 경우 변경이력 쌓음
			if("request".equals(deleteType)){
				Map<String, Object> hisLogParamMap = new HashMap<String, Object>(paramMap);
				List<FileVO> files = new ArrayList<>();
				files.add(fileVo);
				hisLogParamMap.put("fileList", files);
				//첨부파일 삭제로 전달
				hisLogParamMap.put("fileActionType", "del");
				//수정이력 정보 생성
				req4800Service.insertReq4800ModifyHistoryList(hisLogParamMap);
			}
			//등록 성공 메시지 세팅
			model.addAttribute("message", egovMessageSource.getMessage("success.common.delete"));
			
			return new ModelAndView("jsonView");
		}
		catch(Exception ex){
			Log.error("FileDelete()", ex);

			//실패 메시지 세팅 및 성공여부 세팅
			model.put("Success", "N");
			model.addAttribute("message", egovMessageSource.getMessage("fail.common.delete"));
			return new ModelAndView("jsonView");
		}
	}
	
	
}
