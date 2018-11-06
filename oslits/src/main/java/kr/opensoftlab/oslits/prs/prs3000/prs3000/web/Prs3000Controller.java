package kr.opensoftlab.oslits.prs.prs3000.prs3000.web;


import java.io.File;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import kr.opensoftlab.oslits.com.fms.web.service.FileMngService;
import kr.opensoftlab.oslits.com.vo.LoginVO;
import kr.opensoftlab.oslits.prs.prs3000.prs3000.service.Prs3000Service;
import kr.opensoftlab.sdf.util.RequestConvertor;

import org.apache.log4j.Logger;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.multipart.MultipartHttpServletRequest;
import org.springframework.web.servlet.ModelAndView;

import egovframework.com.cmm.EgovMessageSource;
import egovframework.com.cmm.service.EgovFileMngUtil;
import egovframework.com.cmm.service.FileVO;
import egovframework.com.utl.fcc.service.EgovStringUtil;
import egovframework.rte.fdl.cmmn.trace.LeaveaTrace;
import egovframework.rte.fdl.property.EgovPropertyService;

/**
 * @Class Name : Prs3000Controller.java
 * @Description : Prs3000Controller Controller class
 * @Modification Information
 *
 * @author 강재민
 * @since 2016.01.31
 * @version 1.0
 * @see
 * <pre>
 * << 개정이력(Modification Information) >>
 *   
 *   수정일     		 		수정자            		수정내용
 *  ---------------    -------------   ------------------------------
 *   2016. 02. 17.   		진주영        		updatePrs3000 개인정보 -> 사진 업로드 기능 추가
 * </pre>
 *  Copyright (C)  All right reserved.
 */

@Controller
public class Prs3000Controller {
	/** 로그4j 로거 로딩 */
	protected Logger Log = Logger.getLogger(this.getClass());
	
	/** Dpl2000Service DI */
    @Resource(name = "prs3000Service")
    private Prs3000Service prs3000Service;
    
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
	
	/** TRACE */
	@Resource(name = "leaveaTrace")
	LeaveaTrace leaveaTrace;
	
	
	
	/**
	 * Prs3000 개인정보 수정 화면 조회
	 * @param 
	 * @return 
	 * @exception Exception
	 */
	@RequestMapping(value="/prs/prs3000/prs3000/selectPrs3000View.do")
    public String selectPrs3000View(HttpServletRequest request, HttpServletResponse response, ModelMap model ) throws Exception {
    	try{
    		/**리퀘스트에서 넘어온 파라미터를 맵으로 세팅*/
    		Map<String, String> paramMap = RequestConvertor.requestParamToMap(request, true);

    		model.addAttribute("loginUsrId", paramMap.get("regUsrId"));
        	model.addAttribute("message", egovMessageSource.getMessage("success.common.select"));
        	
        	return "/prs/prs3000/prs3000/prs3000";
    	}
    	catch(Exception ex){
    		Log.error("selectPrs3000View()", ex);
    		model.addAttribute("message", egovMessageSource.getMessage("fail.common.select"));
    		throw new Exception(ex.getMessage());
    	}
    }
    
	
	/**
	 * Prs3000 개인정보  조회
	 * @param 
	 * @return 
	 * @exception Exception
	 */
	@RequestMapping(value="/prs/prs3000/prs3000/selectPrs3000UsrInfoAjxa.do")
    public ModelAndView selectPrs3000UsrInfoAjxa(HttpServletRequest request, HttpServletResponse response, ModelMap model ) throws Exception {
    	try{
    		/**리퀘스트에서 넘어온 파라미터를 맵으로 세팅*/
    		Map<String, String> paramMap = RequestConvertor.requestParamToMap(request, true);
    		Map usrInfo = prs3000Service.selectPrs3000(paramMap);
    		
        	model.addAttribute("usrInfo", usrInfo);

        	model.addAttribute("message", egovMessageSource.getMessage("success.common.select"));
        	
        	return new ModelAndView("jsonView", model);
    	}
    	catch(Exception ex){
    		Log.error("selectPrs3000UsrInfoAjxa()", ex);
    		model.addAttribute("message", egovMessageSource.getMessage("fail.common.select"));
    		throw new Exception(ex.getMessage());
    	}
    }
    
    
    /**
	 * Prs3000 개인정보 수정 화면(이메일 체크) AJAX
	 * @param Map
	 * @return int
	 * @exception Exception
	 */
	@RequestMapping(value="/prs/prs3000/prs3000/selectPrs3000emailChRepAjax.do")
    public ModelAndView selectPrs3000emailChRepAjax(HttpServletRequest request, HttpServletResponse response, ModelMap model ) throws Exception {
    	try{
    		/**리퀘스트에서 넘어온 파라미터를 맵으로 세팅*/
    		Map<String, String> paramMap = RequestConvertor.requestParamToMap(request, true);

    		String email = paramMap.get("email");
    		
			// 이메일 입력값이 없을 경우 retrun
			if( "".equals(EgovStringUtil.isNullToString(email)) ) {
				
				model.addAttribute("errorChk", "Y");
				model.addAttribute("message", egovMessageSource.getMessage("fail.required.email"));
				
				return new ModelAndView("jsonView", model);
			}
    		
	    	/**개인정보 수정 화면 이메일 체크*/
	    	int emailCheckCnt = prs3000Service.selectPrs3000emailChRepAjax(paramMap);
    			
	    	//이메일 중복
	    	if(emailCheckCnt > 0){
	    		model.addAttribute("errorChk", "Y");
	    		model.addAttribute("message", egovMessageSource.getMessage("fail.common.email"));
	    	}else{
	    		//중복 이메일이 없는 경우
	    		model.addAttribute("errorChk", "N");
		        model.addAttribute("message", egovMessageSource.getMessage("success.common.email"));
	    	}
        	return new ModelAndView("jsonView", model);
    	}
    	catch(Exception ex){
    		Log.error("selectPrs3000emailChRepAjax()", ex);
    		model.addAttribute("message", egovMessageSource.getMessage("fail.common.select"));
    		throw new Exception(ex.getMessage());
    	}
    }
    
    
    
    /**
	 * Prs3000 개인정보 수정(UPDATE) AJAX
	 * @param Map
	 * @return
	 * @exception Exception
	 */
	@SuppressWarnings("unused")
	@RequestMapping(value="/prs/prs3000/prs3000/updatePrs3000.do")
    public ModelAndView updatePrs3000(HttpServletRequest request, HttpServletResponse response, ModelMap model ) throws Exception {
    	try{
    		MultipartHttpServletRequest mptRequest = (MultipartHttpServletRequest)request;
    		
    		/**리퀘스트에서 넘어온 파라미터를 맵으로 세팅*/
    		Map<String, String> paramMap = RequestConvertor.requestParamToMap(request, true);
    		
    		//사용자 UsrImgId값
    		String inUsrImgId = paramMap.get("usrImgId");
    		
    		//파일이 첨부되었는지 확인
    		String insertFileCnt = paramMap.get("insertFileCnt");
    		
    		//파일이 첨부되었으면 값이 2
    			//USR_IMG_ID가 없을 경우
    			if("".equals(inUsrImgId) || inUsrImgId == null){
    				//파일 추가 후 결과 값
    				List<FileVO> _result = fileUtil.fileUploadInsert(mptRequest,"",0,"UsrImg");
    				
    				//추가된 파일이 존재하지 않을 경우 파일업로드 처리를 하지 않는다.
    				if(_result != null){
    					//첨부된 파일 DB등록
    					inUsrImgId = fileMngService.insertFileInfs(_result);  //DB에 생성된 첨부파일 ID를 리턴한다.
    				}else{
    					//첨부된 파일도 없을 경우 공백
    					inUsrImgId = "";
    				}
    			}else{
    				/*USR_IMG_ID가 있을 경우 원본 파일 삭제 후 업로드 */
    				
    				//FileVO 세팅
    				FileVO fileVo = new FileVO();
    				fileVo.setAtchFileId(inUsrImgId);
    				fileVo.setFileSn("0");
    				
    				//파일의 단일 정보 불러오기
    				fileVo = fileMngService.selectFileInf(fileVo);
    				
    				//atchFileId가 없을 경우
    				if(fileVo == null || "".equals(fileVo.getAtchFileId()) || fileVo.getAtchFileId() == null){
    					//파일 추가 후 결과 값
        				List<FileVO> _result = fileUtil.fileUploadInsert(mptRequest,"",0,"UsrImg");
        				
        				//추가된 파일이 존재하지 않을 경우 파일업로드 처리를 하지 않는다.
        				if(_result != null){
        					//첨부된 파일 DB등록
        					inUsrImgId = fileMngService.insertFileInfs(_result);  //DB에 생성된 첨부파일 ID를 리턴한다.
        				}else{
        					//첨부된 파일도 없을 경우 공백
        					inUsrImgId = "";
        				}	
    				}else{
    					if(mptRequest.getFileMap().size() > 0){
	    				//DB에서 삭제
	    				fileMngService.deleteFileInf(fileVo);
	    				
	    				//파일 물리적 삭제
	    				String fileDeletePath  = fileVo.getFileStreCours()+fileVo.getStreFileNm();
	    				
	    				//파일 존재 체크
	    				File file = new File(fileDeletePath);
	    				boolean isExists = file.exists();
	    				
	    				if(isExists){
	    					String delResult       = EgovFileMngUtil.deleteFile(fileDeletePath);
	    			    
		    			    if("".equals(delResult) || delResult == null){
		    					//실패 메시지 세팅 및 성공여부 세팅
		    					model.addAttribute("fileError", "Y");
		    					model.addAttribute("message", egovMessageSource.getMessage("fail.common.delete"));
		    					return new ModelAndView("jsonView");
		    			    }
	    				}
	    			    //atchFileId는 있으나, fileSn이 없을 경우 fileSn을 0부터 세팅
	    				//파일 추가 후 결과 값
	    				List<FileVO> _result = fileUtil.fileUploadInsert(mptRequest,inUsrImgId,0,"UsrImg");
	    				
	    				//추가된 파일이 존재하지 않을 경우 파일업로드 처리를 하지 않는다.
	    				if(_result != null){
	    					//첨부된 파일 DB등록 (AtchFileId는 보존하고, FileSn만 추가 - 파일 상세정보)
	    					inUsrImgId = fileMngService.insertFileDetail(_result);  //DB에 생성된 첨부파일 ID를 리턴한다.
	    				}
    					}
    				}
    			}
    			model.addAttribute("fileUpload","Y");
    		paramMap.put("usrImgId",inUsrImgId);
    		
    		/**개인정보 수정 */
    		int updateCnt = prs3000Service.updatePrs3000(paramMap);
    		
    		if( updateCnt > 0 ){
    			/**개인정보를 조회한다.*/
    			Map usrInfo = prs3000Service.selectPrs3000(paramMap);
    			request.getSession().setAttribute("usrInfo", usrInfo);
    		}
    		model.addAttribute("fileError", "N");
        	model.addAttribute("message", egovMessageSource.getMessage("success.common.update"));
        	
        	return new ModelAndView("jsonView", model);
    	}
    	catch(Exception ex){
    		Log.error("updatePrs3000()", ex);
    		model.addAttribute("message", egovMessageSource.getMessage("fail.common.update"));
    		throw new Exception(ex.getMessage());
    	}
    }
	
	/**
	 * Prs3000 수정 전 비밀번호 조회
	 * @param 
	 * @return 
	 * @exception Exception
	 */
	@RequestMapping(value="/prs/prs3000/prs3000/selectPrs3000PwCheckAjxa.do")
    public ModelAndView selectPrs3000PwCheckAjxa(HttpServletRequest request, HttpServletResponse response, ModelMap model ) throws Exception {
    	try{
    		/**리퀘스트에서 넘어온 파라미터를 맵으로 세팅*/
    		Map<String, String> paramMap = RequestConvertor.requestParamToMap(request, true);
    		String beforePw = prs3000Service.selectPrs3000PwCheck(paramMap);
    		
        	model.addAttribute("beforePw", beforePw);
        	model.addAttribute("errorYN", "N");
        	model.addAttribute("message", egovMessageSource.getMessage("success.common.select"));
        	
        	return new ModelAndView("jsonView", model);
    	}
    	catch(Exception ex){
    		Log.error("selectPrs3000PwCheckAjxa()", ex);
    		model.addAttribute("errorYN", "Y");
    		model.addAttribute("message", egovMessageSource.getMessage("fail.common.select"));
    		throw new Exception(ex.getMessage());
    	}
    }
	
}
