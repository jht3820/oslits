package kr.opensoftlab.oslops.prj.prj3000.prj3000.web;

import java.util.List;
import java.util.Map;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

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
import kr.opensoftlab.oslops.com.fms.web.service.FileMngService;
import kr.opensoftlab.oslops.com.vo.LoginVO;
import kr.opensoftlab.oslops.prj.prj3000.prj3000.service.Prj3000Service;
import kr.opensoftlab.sdf.util.RequestConvertor;

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
public class Prj3000Controller {

	/** 로그4j 로거 로딩 */
	private final Logger Log = Logger.getLogger(this.getClass());

	/** Prj3000Service DI */
	@Resource(name = "prj3000Service")
	private Prj3000Service prj3000Service;

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
	 * Prj3000 화면 - 산출물 업로드 화면
	 * @param 
	 * @return 
	 * @exception Exception
	 */
	@SuppressWarnings({ "unchecked", "rawtypes" })
	@RequestMapping(value="/prj/prj3000/prj3000/selectPrj3000View.do")
	public String selectPrj3000View(HttpServletRequest request, HttpServletResponse response, ModelMap model ) throws Exception {

		try{
			//리퀘스트에서 넘어온 파라미터를 맵으로 세팅
			Map paramMap = RequestConvertor.requestParamToMapAddSelInfo(request, true);
			
			//로그인VO 가져오기
    		HttpSession ss = request.getSession();
    		LoginVO loginVO = (LoginVO) ss.getAttribute("loginVO");
    		
    		paramMap.put("prjId", ss.getAttribute("selPrjId"));
        	paramMap.put("authGrpId", ss.getAttribute("selAuthGrpId"));
        	paramMap.put("usrId", loginVO.getUsrId());
        	paramMap.put("licGrpId", loginVO.getLicGrpId());
        	
        	//표준 산출물 조회
        	paramMap.put("docCommonCd", "01");
        	paramMap.put("docNm", egovMessageSource.getMessage("prj3000.success.docTitle"));
        	
        	//라이선스 그룹에 할당된 메뉴목록 가져오기
        	List<Map> baseDocList = (List) prj3000Service.selectPrj3000BaseMenuList(paramMap);
           	
        	model.addAttribute("baseDocList", baseDocList);

			return "/prj/prj3000/prj3000/prj3000";
		}
		catch(Exception ex){
			Log.error("selectPrj3000View()", ex);
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
	@RequestMapping(value="/prj/prj3000/prj3000/selectPrj3000MenuListAjax.do")
    public ModelAndView selectPrj3000MenuListAjax(HttpServletRequest request, HttpServletResponse response, ModelMap model ) throws Exception {
    	
    	try{
        	
    		//리퀘스트에서 넘어온 파라미터를 맵으로 세팅
    		Map<String, String> paramMap = RequestConvertor.requestParamToMapAddSelInfo(request, true);
        	
    		String prjId = (String) paramMap.get("prjId");
        	
        	//로그인VO 가져오기
    		HttpSession ss = request.getSession();
    		
    		//prjId없는경우 세션에서 가져와서 넣기
    		if(prjId == null || "".equals(prjId)){
    			paramMap.put("prjId", (String)ss.getAttribute("selPrjId"));
    		}else{
    			paramMap.put("prjId", prjId);
    		}
        	
    		List<Map> baseDocList = null;
    		
    		//rootsystem 기본 산출물 메뉴 가져오는 경우
    		if(prjId != null && "ROOTSYSTEM_PRJ".equals(prjId)){
    			//라이선스 그룹에 할당된 메뉴목록 가져오기
    			baseDocList = (List) prj3000Service.selectPrj3000RootMenuList(paramMap);
    		}else{
    			//라이선스 그룹에 할당된 메뉴목록 가져오기
    			baseDocList = (List) prj3000Service.selectPrj3000BaseMenuList(paramMap);
    		}
        	
        	model.addAttribute("baseDocList", baseDocList);
        	
        	//조회성공메시지 세팅
        	model.addAttribute("message", egovMessageSource.getMessage("success.common.select"));
        	
        	return new ModelAndView("jsonView", paramMap);
    	}
    	catch(Exception ex){
    		Log.error("selectAdm1000MenuInfo()", ex);
    		
    		//조회실패 메시지 세팅
    		model.addAttribute("message", egovMessageSource.getMessage("fail.common.select"));
    		return new ModelAndView("jsonView");
    	}
    }
    /**
	 * Prj3000 산출물 메뉴정보 조회(단건) AJAX
	 * @param 
	 * @return 
	 * @exception Exception
	 */
    @SuppressWarnings({ "rawtypes", "unchecked" })
	@RequestMapping(value="/prj/prj3000/prj3000/selectPrj3000MenuInfoAjax.do")
    public ModelAndView selectPrj3000MenuInfoAjax(HttpServletRequest request, HttpServletResponse response, ModelMap model ) throws Exception {
    	
    	try{
        	
    		// request 파라미터를 map으로 변환
        	Map<String, String> paramMap = RequestConvertor.requestParamToMap(request, true);
        	String prjId = (String) paramMap.get("prjId");
        	
        	//로그인VO 가져오기
    		HttpSession ss = request.getSession();
    		
    		//prjId없는경우 세션에서 가져와서 넣기
    		if(prjId == null || "".equals(prjId)){
    			paramMap.put("prjId", (String)ss.getAttribute("selPrjId"));
    		}else{
    			paramMap.put("prjId", prjId);
    		}
        	//산출물 메뉴정보조회
        	Map<String, String> docInfoMap = null;
        	
        	//rootsystem의 경우 기본 산출물 정보 조회
        	if("ROOTSYSTEM_PRJ".equals(prjId)){
        		docInfoMap = (Map) prj3000Service.selectPrj3000WizardMenuInfo(paramMap);
        	}else{
        		docInfoMap = (Map) prj3000Service.selectPrj3000MenuInfo(paramMap);
        	}
        	
        	//파일 Sn 초기화
        	int _fileSn = 0;
        	
        	//첨부파일 리스트
        	String atchFormFileId = docInfoMap.get("docFormFileId");
        	if(!"".equals(atchFormFileId) && atchFormFileId != null){
					
    				//atchFileId값을 넘기기 위한 FileVO
    	        	FileVO fileVO = new FileVO();
    	        	fileVO.setAtchFileId(atchFormFileId);
    	        	
    	        	//파일 리스트 조회
    				List<FileVO> fileList = fileMngService.fileDownList(fileVO);
    				model.addAttribute("fileList",fileList);
    				
    				//파일 Sn 조회
    	        	//FileSn 불러오기
    	           	_fileSn = fileMngService.getFileSN(fileVO);
    	           	
        	}
        	
        	//파일 Sn 전송
        	model.addAttribute("fileSn", _fileSn);
        	
        	//조회성공메시지 세팅
        	model.addAttribute("message", egovMessageSource.getMessage("success.common.select"));
        	
        	return new ModelAndView("jsonView", docInfoMap);
    	}
    	catch(Exception ex){
    		Log.error("selectPrj3000MenuInfoAjax()", ex);

    		//조회실패 메시지 세팅
    		model.addAttribute("message", egovMessageSource.getMessage("fail.common.select"));
    		return new ModelAndView("jsonView");
    	}
    }
    /**
   	 * Prj3000 메뉴정보 등록(단건) AJAX
   	 * 산출물 메뉴정보 등록후 등록 정보 결과 및 정보를 화면으로 리턴한다.
   	 * @param 
   	 * @return 
   	 * @exception Exception
   	 */
       @SuppressWarnings({ "rawtypes", "unchecked" })
   	@RequestMapping(value="/prj/prj3000/prj3000/insertPrj3000MenuInfoAjax.do")
       public ModelAndView insertPrj3000MenuInfoAjax(HttpServletRequest request, HttpServletResponse response, ModelMap model ) throws Exception {
       	
       	try{
           	
       		// request 파라미터를 map으로 변환
           	Map<String, String> paramMap = RequestConvertor.requestParamToMap(request, true);
           	
           	HttpSession ss = request.getSession();
        	paramMap.put("prjId", (String)ss.getAttribute("selPrjId"));
        	
        	//양식 파일 ID 생성
        	String fileFormId = idgenService.getNextStringId();
        	paramMap.put("fileFormId", fileFormId);
        	
        	//파일 ID 생성
        	String fileAtchId = idgenService.getNextStringId();
        	paramMap.put("fileAtchId", fileAtchId);

           	// 메뉴 등록
           	Map<String, String> docInfoMap = (Map) prj3000Service.insertPrj3000MenuInfo(paramMap);
           	
           	//등록 성공 메시지 세팅
           	model.addAttribute("message", egovMessageSource.getMessage("success.common.insert"));
           	
           	
           	return new ModelAndView("jsonView", docInfoMap);
       	}
       	catch(Exception ex){
       		Log.error("selectPrj3000MenuInfoAjax()", ex);

       		//조회실패 메시지 세팅 및 저장 성공여부 세팅
       		model.addAttribute("saveYN", "N");
       		model.addAttribute("message", egovMessageSource.getMessage("fail.common.insert"));
       		return new ModelAndView("jsonView");
       	}
    }
       /**
      	 * Prj3000 메뉴정보 수정(단건) AJAX
      	 * 메뉴정보 수정 처리
      	 * @param 
      	 * @return 
      	 * @exception Exception
      	 */
      	@RequestMapping(value="/prj/prj3000/prj3000/updatePrj3000MenuInfoAjax.do")
          public ModelAndView updatePrj3000MenuInfoAjax(HttpServletRequest request, HttpServletResponse response, ModelMap model ) throws Exception {
          	
          	try{
              	
          		// request 파라미터를 map으로 변환
              	Map<String, String> paramMap = RequestConvertor.requestParamToMap(request, true);
              	
              	HttpSession ss = request.getSession();
              	paramMap.put("prjId", (String)ss.getAttribute("selPrjId"));
           	
           	
              	// 메뉴 수정
              	prj3000Service.updatePrj3000MenuInfo(paramMap);
              	
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
       	 * Prj3000 산출물 양식 확정 AJAX
       	 * 산출물 파일 확정 처리
       	 * @param 
       	 * @return 
       	 * @exception Exception
       	 */
       	@RequestMapping(value="/prj/prj3000/prj3000/updatePrj3000FileSnAjax.do")
           public ModelAndView updatePrj3000FileSnAjax(HttpServletRequest request, HttpServletResponse response, ModelMap model ) throws Exception {
           	
           	try{
               	
           		// request 파라미터를 map으로 변환
               	Map<String, String> paramMap = RequestConvertor.requestParamToMap(request, true);
               	
               	HttpSession ss = request.getSession();
               	paramMap.put("prjId", (String)ss.getAttribute("selPrjId"));
               //	paramMap.put("docFileSn", "0");

               	
               	// 산출물 확정 처리
            	prj3000Service.updatePrj3000FileSnSelect(paramMap);
               	
               	//등록 성공 메시지 세팅
            	model.addAttribute("saveYN", "Y");
               	model.addAttribute("message", egovMessageSource.getMessage("success.common.update"));
               	
               	return new ModelAndView("jsonView");
           	}
           	catch(Exception ex){
           		Log.error("updatePrj3000FileSnAjax()", ex);

           		//수정 실패 메시지 세팅 및 저장 성공여부 세팅
           		model.addAttribute("saveYN", "N");
           		model.addAttribute("message", egovMessageSource.getMessage("fail.common.update"));
           		return new ModelAndView("jsonView");
           	}
       }
   	/**
	 * Prj3000 개발문서 삭제 AJAX
	 * 선택한 개발문서 및 하위 개발문서 삭제 처리
	 * @param 
	 * @return 
	 * @exception Exception
	 */
	@RequestMapping(value="/prj/prj3000/prj3000/deletePrj3000AuthGrpInfoAjax.do")
    public ModelAndView deletePrj3000AuthGrpInfoAjax(HttpServletRequest request, HttpServletResponse response, ModelMap model ) throws Exception {
    	
    	try{
        	
    		// request 파라미터를 map으로 변환
        	Map<String, Object> paramMap = RequestConvertor.requestParamToMapAddSelInfoList(request, true,"docId");
    		
        	HttpSession ss = request.getSession();
           	paramMap.put("prjId", (String)ss.getAttribute("selPrjId"));
           	
        	// 개발문서 삭제
           	prj3000Service.deletePrj3000MenuInfo(paramMap);
        	
        	//삭제 성공 메시지 세팅
        	model.addAttribute("message", egovMessageSource.getMessage("success.common.delete"));
        	
        	return new ModelAndView("jsonView");
    	}
    	catch(Exception ex){
    		Log.error("deleteAdm1000MenuInfoAjax()", ex);

    		//삭제실패 메시지 세팅 및 저장 성공여부 세팅
    		model.addAttribute("saveYN", "N");
    		model.addAttribute("message", egovMessageSource.getMessage("fail.common.delete"));
    		return new ModelAndView("jsonView");
    	}
    }
	/**
	 * Prj3000 산출물 파일 업로드 AJAX
	 * 산출물 파일 업로드
	 * @param 
	 * @return 
	 * @exception Exception
	 */
	@RequestMapping(value="/prj/prj3000/prj3000/insertPrj3000FileUploadAjax.do")
    public ModelAndView insertPrj3000FileUploadAjax(HttpServletRequest request, HttpServletResponse response, ModelMap model ) throws Exception {
    	
    	try{
    		//System.out.println("############");
    		MultipartHttpServletRequest mptRequest = (MultipartHttpServletRequest)request;
    		//System.out.println("############");
    		
    		// request 파라미터를 map으로 변환
        	Map<String, String> paramMap = RequestConvertor.requestParamToMapAddSelInfo(request, true);
        	
        	HttpSession ss = request.getSession();
           	paramMap.put("prjId", (String)ss.getAttribute("selPrjId"));
        	
           	String _atchFileId = paramMap.get("docFormFileId");
           	
           	//int _fileSn = 0;
           	
           	//atchFileId이 없을 경우, 새로 추가
			if("".equals(_atchFileId) || _atchFileId == null || "NULL".equals(_atchFileId)){
				//파일 추가 후 결과 값 (디렉토리 = Doc/PrjId
				List<FileVO> _result = fileUtil.fileUploadInsert(mptRequest,"",0,"Doc"+"/"+paramMap.get("prjId"));
				
				//추가된 파일이 존재하지 않을 경우 파일업로드 처리를 하지 않는다.
				if(_result != null){
					//첨부된 파일 DB등록
					_atchFileId = fileMngService.insertFileInfs(_result);  //DB에 생성된 첨부파일 ID를 리턴한다.
					paramMap.put("docFormFileId",_atchFileId);
					paramMap.put("docFormFileSn",null);
					
		        	
		           	// 메뉴 수정
		        	prj3000Service.updatePrj3000MenuInfo(paramMap);
		        	
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
				if(_result != null && !(_result.isEmpty())){
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
    		Log.error("insertPrj3000FileUploadAjax()", ex);

    		//업로드 실패 메시지 세팅 및 저장 성공여부 세팅
    		model.addAttribute("saveYN", "N");
    		model.addAttribute("message", egovMessageSource.getMessage("fail.request.msg"));
    		return new ModelAndView("jsonView");
    	}
    }
	
	/**
	 * Prj3000 확정 산출물 전체 다운로드
	 * 산출물 파일 업로드
	 * @param 
	 * @return 
	 * @exception Exception
	 */
	@SuppressWarnings("rawtypes")
	@RequestMapping(value="/prj/prj3000/prj3000/selectPrj3000MenuTreeZipDownload.do")
	public ModelAndView selectPrj3000MenuTreeZipDownload(HttpServletRequest request, HttpServletResponse response, ModelMap model ) throws Exception {
		
		try{
			// request 파라미터를 map으로 변환
        	Map<String, String> paramMap = RequestConvertor.requestParamToMapAddSelInfo(request, true);
        	
        	HttpSession ss = request.getSession();
           	paramMap.put("prjId", (String)ss.getAttribute("selPrjId"));
        	
           	List<Map> docMenuList = prj3000Service.selectPrj3000MenuTree(paramMap);
           	
           	model.addAttribute("docMenuList", docMenuList);
        	
        	//등록 성공 메시지 세팅
        	model.addAttribute("message", egovMessageSource.getMessage("success.common.select"));
        	
        	return new ModelAndView("jsonView");
		}
		catch(Exception ex){
			Log.error("selectPrj3000MenuTreeZipDownload()", ex);
			
			//업로드 실패 메시지 세팅 및 저장 성공여부 세팅
			model.addAttribute("saveYN", "N");
			model.addAttribute("message", egovMessageSource.getMessage("fail.common.select"));
			return new ModelAndView("jsonView");
		}
	}
	
}
