package kr.opensoftlab.oslops.prj.prj3000.prj3100.web;

import java.util.List;
import java.util.Map;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import kr.opensoftlab.oslops.com.fms.web.service.FileMngService;
import kr.opensoftlab.oslops.com.vo.LoginVO;
import kr.opensoftlab.oslops.prj.prj3000.prj3100.service.Prj3100Service;
import kr.opensoftlab.sdf.util.RequestConvertor;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.multipart.MultipartHttpServletRequest;
import org.springframework.web.servlet.ModelAndView;

import egovframework.com.cmm.EgovMessageSource;
import egovframework.com.cmm.service.EgovFileMngUtil;
import egovframework.com.cmm.service.FileVO;
import egovframework.rte.fdl.cmmn.trace.LeaveaTrace;
import egovframework.rte.fdl.idgnr.EgovIdGnrService;
import egovframework.rte.fdl.property.EgovPropertyService;

/**
 * @Class Name : Prj3000Controller.java
 * @Description : Prj3000Controller Controller class
 * @Modification Information
 *
 * @author 진주영
 * @since 2016.03.22.
 * @version 1.0
 * @see
 *  
 *  Copyright (C) OpenSoftLab Corp All right reserved.
 */

@Controller
public class Prj3100Controller {

	/** 로그4j 로거 로딩 */
	private final Logger Log = Logger.getLogger(this.getClass());

	/** Prj3100Service DI */
	@Resource(name = "prj3100Service")
	private Prj3100Service prj3100Service;

	/** EgovMessageSource */
	@Resource(name = "egovMessageSource")
	EgovMessageSource egovMessageSource;

	/** EgovPropertyService */
	@Resource(name = "propertiesService")
	protected EgovPropertyService propertiesService;
	
	/** FileMngService */
   	@Resource(name="fileMngService")
   	private FileMngService fileMngService;
   	
	/** EgovFileMngUtil - 파일 업로드 Util */
	@Resource(name="EgovFileMngUtil")
	private EgovFileMngUtil fileUtil;
	
	/** EgovIdGnrService Bean id 자동생성 서비스 */
	@Resource(name = "egovFileIdGnrService")
	private EgovIdGnrService idgenService;
	
	/** TRACE */
	@Resource(name = "leaveaTrace")
	LeaveaTrace leaveaTrace;

	@Value("${Globals.fileStorePath}")
	private String tempPath;
	/**
	 * Prj3100 화면 -  산출물 양식 업로드 테스트
	 * @param 
	 * @return 
	 * @exception Exception
	 */
	@SuppressWarnings({ "unchecked", "rawtypes" })
	@RequestMapping(value="/prj/prj3000/prj3100/selectPrj3100View.do")
	public String selectPrj3100View(HttpServletRequest request, HttpServletResponse response, ModelMap model ) throws Exception {

		try{
			//리퀘스트에서 넘어온 파라미터를 맵으로 세팅
			Map paramMap = RequestConvertor.requestParamToMapAddSelInfo(request, true);
			
			//로그인VO 가져오기
    		HttpSession ss = request.getSession();

    		paramMap.put("prjId", ss.getAttribute("selPrjId"));
        	paramMap.put("authGrpId", ss.getAttribute("selAuthGrpId"));
  
        	//표준 산출물 조회
        	paramMap.put("docCommonCd", "01");
        	
        	//라이선스 그룹에 할당된 메뉴목록 가져오기
        	List<Map> baseDocList = (List) prj3100Service.selectPrj3100BaseMenuList(paramMap);

        	model.addAttribute("baseDocList", baseDocList);
			return "/prj/prj3000/prj3100/prj3100";
		}
		catch(Exception ex){
			Log.error("selectPrj3100View()", ex);
			throw new Exception(ex.getMessage());
		}
	}

    /**
	 * PRJ3000 Ajax 메뉴 정보 목록 조회
	 * @param 
	 * @return 
	 * @exception Exception
	 */
    @SuppressWarnings({ "rawtypes", "unchecked" })
	@RequestMapping(value="/prj/prj3000/prj3100/selectPrj3100MenuListAjax.do")
    public ModelAndView selectPrj3100MenuListAjax(HttpServletRequest request, HttpServletResponse response, ModelMap model ) throws Exception {
    	
    	try{
        	
    		//리퀘스트에서 넘어온 파라미터를 맵으로 세팅
        	Map paramMap = RequestConvertor.requestParamToMap(request);
        	
        	//로그인VO 가져오기
    		HttpSession ss = request.getSession();
    		LoginVO loginVO = (LoginVO) ss.getAttribute("loginVO");
    		
        	paramMap.put("prjId", ss.getAttribute("selPrjId"));
        	paramMap.put("authGrpId", ss.getAttribute("selAuthGrpId"));
        	paramMap.put("usrId", loginVO.getUsrId());
        	paramMap.put("licGrpId", loginVO.getLicGrpId());
        	
        	//라이선스 그룹에 할당된 메뉴목록 가져오기
        	List<Map> baseDocList = (List) prj3100Service.selectPrj3100BaseMenuList(paramMap);
        	
        	model.addAttribute("baseDocList", baseDocList);
        	
        	//조회성공메시지 세팅
        	model.addAttribute("message", egovMessageSource.getMessage("success.common.select"));
        	
        	return new ModelAndView("jsonView", paramMap);
    	}
    	catch(Exception ex){
    		Log.error("selectPrj3100MenuListAjax()", ex);
    		
    		//조회실패 메시지 세팅
    		model.addAttribute("message", egovMessageSource.getMessage("fail.common.select"));
    		return new ModelAndView("jsonView");
    	}
    }
    /**
	 * Prj3100 산출물 메뉴정보 조회(단건) AJAX
	 * @param 
	 * @return 
	 * @exception Exception
	 */
    @SuppressWarnings({ "rawtypes", "unchecked" })
	@RequestMapping(value="/prj/prj3000/prj3100/selectPrj3100MenuInfoAjax.do")
    public ModelAndView selectPrj3100MenuInfoAjax(HttpServletRequest request, HttpServletResponse response, ModelMap model ) throws Exception {
    	
    	try{
        	
    		// request 파라미터를 map으로 변환
        	Map<String, String> paramMap = RequestConvertor.requestParamToMap(request, true);
        	
        	HttpSession ss = request.getSession();
        	paramMap.put("prjId", (String)ss.getAttribute("selPrjId"));
        	//산출물 메뉴정보조회
        	Map<String, String> docInfoMap = (Map) prj3100Service.selectPrj3100MenuInfo(paramMap);
        	
        	//파일 Sn 초기화
        	int _fileSn = 0;
        	
        	if(docInfoMap != null){
        	//첨부파일 리스트
        	String atchFileId = docInfoMap.get("docAtchFileId");
	        	if(!"".equals(atchFileId) && atchFileId != null){
						
	    				//atchFileId값을 넘기기 위한 FileVO
	    	        	FileVO fileVO = new FileVO();
	    	        	fileVO.setAtchFileId(atchFileId);
	    	        	
	    	        	//파일 리스트 조회
	    				List<FileVO> fileList = fileMngService.fileDownList(fileVO);
	    				model.addAttribute("fileList",fileList);
	    				
	    				//파일 Sn 조회
	    	        	//FileSn 불러오기
	    	           	_fileSn = fileMngService.getFileSN(fileVO);
	        	}
        	}
        	//파일 Sn 전송
        	model.addAttribute("fileSn", _fileSn);
        	
        	//조회성공메시지 세팅
        	model.addAttribute("message", egovMessageSource.getMessage("success.common.select"));
        	
        	return new ModelAndView("jsonView", docInfoMap);
    	}
    	catch(Exception ex){
    		Log.error("selectPrj3100MenuInfoAjax()", ex);

    		//조회실패 메시지 세팅
    		model.addAttribute("message", egovMessageSource.getMessage("fail.common.select"));
    		return new ModelAndView("jsonView");
    	}
    }
    
    /**
   	 * Prj3100 메뉴정보 수정(단건) AJAX
   	 * 메뉴정보 수정 처리
   	 * @param 
   	 * @return 
   	 * @exception Exception
   	 */
   	@RequestMapping(value="/prj/prj3000/prj3100/updatePrj3100MenuInfoAjax.do")
       public ModelAndView updatePrj3100MenuInfoAjax(HttpServletRequest request, HttpServletResponse response, ModelMap model ) throws Exception {
       	
       	try{
           	
       		// request 파라미터를 map으로 변환
           	Map<String, String> paramMap = RequestConvertor.requestParamToMap(request, true);
           	
           	HttpSession ss = request.getSession();
           	paramMap.put("prjId", (String)ss.getAttribute("selPrjId"));
        	
        	
           	// 메뉴 수정
           	prj3100Service.updatePrj3100MenuInfo(paramMap);
           	
           	//등록 성공 메시지 세팅
           	model.addAttribute("message", egovMessageSource.getMessage("success.common.update"));
           	
           	return new ModelAndView("jsonView");
       	}
       	catch(Exception ex){
       		Log.error("updateAdm1000MenuInfoAjax()", ex);

       		//수정 실패 메시지 세팅 및 저장 성공여부 세팅
       		model.addAttribute("saveYN", "N");
       		model.addAttribute("message", egovMessageSource.getMessage("fail.common.update"));
       		return new ModelAndView("jsonView");
       	}
   }
    
      	
      	/**
       	 * Prj3000 산출물 확정 AJAX
       	 * 산출물 파일 확정 처리
       	 * @param 
       	 * @return 
       	 * @exception Exception
       	 */
       	@RequestMapping(value="/prj/prj3000/prj3100/updatePrj3100FileSnAjax.do")
           public ModelAndView updatePrj3100FileSnAjax(HttpServletRequest request, HttpServletResponse response, ModelMap model ) throws Exception {
           	
           	try{
               	
           		// request 파라미터를 map으로 변환
               	Map<String, String> paramMap = RequestConvertor.requestParamToMap(request, true);
               	
               	HttpSession ss = request.getSession();
               	paramMap.put("prjId", (String)ss.getAttribute("selPrjId"));

               	// 산출물 확정 처리
            	prj3100Service.updatePrj3100FileSnSelect(paramMap);
               	
               	//등록 성공 메시지 세팅
            	model.addAttribute("saveYN", "Y");
               	model.addAttribute("message", egovMessageSource.getMessage("success.common.update"));
               	
               	return new ModelAndView("jsonView");
           	}
           	catch(Exception ex){
           		Log.error("updatePrj3100FileSnAjax()", ex);

           		//수정 실패 메시지 세팅 및 저장 성공여부 세팅
           		model.addAttribute("saveYN", "N");
           		model.addAttribute("message", egovMessageSource.getMessage("fail.common.update"));
           		return new ModelAndView("jsonView");
           	}
       }
	/**
	 * Prj3100 산출물 파일 업로드 AJAX
	 * 산출물 파일 업로드
	 * @param 
	 * @return 
	 * @exception Exception
	 */
	@RequestMapping(value="/prj/prj3000/prj3100/insertPrj3100FileUploadAjax.do")
    public ModelAndView insertPrj3100FileUploadAjax(HttpServletRequest request, HttpServletResponse response, ModelMap model ) throws Exception {
    	
    	try{
    		MultipartHttpServletRequest mptRequest = (MultipartHttpServletRequest)request;
    		
    		// request 파라미터를 map으로 변환
        	Map<String, String> paramMap = RequestConvertor.requestParamToMapAddSelInfo(request, true);
        	
        	HttpSession ss = request.getSession();
           	paramMap.put("prjId", (String)ss.getAttribute("selPrjId"));
        	
           	String _atchFileId = paramMap.get("docAtchFileId");
           	
           	//int _fileSn = 0;
           	
           	//atchFileId이 없을 경우, 새로 추가
			if("".equals(_atchFileId) || _atchFileId == null || "NULL".equals(_atchFileId)){
				//파일 추가 후 결과 값 (디렉토리 = Doc/PrjId
				List<FileVO> _result = fileUtil.fileUploadInsert(mptRequest,"",0,"Doc"+"/"+paramMap.get("prjId"));
				
				//추가된 파일이 존재하지 않을 경우 파일업로드 처리를 하지 않는다.
				if(_result != null){
					//첨부된 파일 DB등록
					_atchFileId = fileMngService.insertFileInfs(_result);  //DB에 생성된 첨부파일 ID를 리턴한다.
					paramMap.put("docAtchFileId",_atchFileId);
					paramMap.put("docFileSn",null);
		        	
		           	// 메뉴 수정
		        	prj3100Service.updatePrj3100MenuInfo(paramMap);
		        	
		        	model.addAttribute("message", egovMessageSource.getMessage("prj3000.success.fileUpload.insert"));
		        	model.addAttribute("saveYN", "Y");
		        	model.addAttribute("firstInsert", "Y");
		        	model.addAttribute("addFileId", _atchFileId);
		        	model.addAttribute("addFileSn", "0");
		        	model.addAttribute("fileVo", _result.get(0)); 
		        	
		        	return new ModelAndView("jsonView");
				}else{
					//업로드 실패 메시지 세팅 및 저장 성공여부 세팅
		    		model.addAttribute("saveYN", "N");
		    		model.addAttribute("message", egovMessageSource.getMessage("fail.request.msg"));
		    		return new ModelAndView("jsonView");
				}
			}
			
           	FileVO fvo = new FileVO();
           	fvo.setAtchFileId(_atchFileId);
            //int _fileSn = fileMngService.getFileSN(fvo);
        	int _fileSn = Integer.parseInt(paramMap.get("uploadFileSn"));
			if(_fileSn >= 1){
				//파일 추가 후 결과 값
				List<FileVO> _result = fileUtil.fileUploadInsert(mptRequest,_atchFileId,_fileSn,"Doc"+"/"+paramMap.get("prjId"));
				
				//추가된 파일이 존재하지 않을 경우 파일업로드 처리를 하지 않는다.
				if(_result != null){
					//첨부된 파일 DB등록 (AtchFileId는 보존하고, FileSn만 추가 - 파일 상세정보)
					_atchFileId = fileMngService.insertFileDetail(_result);  //DB에 생성된 첨부파일 ID를 리턴한다.
					model.addAttribute("fileVo", _result.get(0)); 
				}else{
					//업로드 실패 메시지 세팅 및 저장 성공여부 세팅
		    		model.addAttribute("saveYN", "N");
		    		model.addAttribute("message", egovMessageSource.getMessage("fail.request.msg"));
		    		return new ModelAndView("jsonView");
				}
			}
        	//등록 성공 메시지 세팅
    		model.addAttribute("addFileId", _atchFileId);
        	model.addAttribute("addFileSn", _fileSn);
        	
        	model.addAttribute("message", egovMessageSource.getMessage("prj3000.success.fileUpload.insert"));
        	model.addAttribute("saveYN", "Y");
        	return new ModelAndView("jsonView");
    	}
    	catch(Exception ex){
    		Log.error("insertPrj3100FileUploadAjax()", ex);

    		//업로드 실패 메시지 세팅 및 저장 성공여부 세팅
    		model.addAttribute("saveYN", "N");
    		model.addAttribute("message", egovMessageSource.getMessage("fail.request.msg"));
    		return new ModelAndView("jsonView");
    	}
    }
}
