package kr.opensoftlab.oslops.req.req1000.req1000.web;

import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.HashMap;
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
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.multipart.MultipartHttpServletRequest;
import org.springframework.web.servlet.ModelAndView;

import com.google.gson.GsonBuilder;

import egovframework.com.cmm.EgovMessageSource;
import egovframework.com.cmm.service.EgovFileMngUtil;
import egovframework.com.cmm.service.EgovProperties;
import egovframework.com.cmm.service.FileVO;
import egovframework.rte.fdl.cmmn.trace.LeaveaTrace;
import egovframework.rte.fdl.idgnr.EgovIdGnrService;
import egovframework.rte.fdl.property.EgovPropertyService;
import egovframework.rte.ptl.mvc.tags.ui.pagination.PaginationInfo;
import kr.opensoftlab.oslops.adm.adm6000.adm6000.service.Adm6000Service;
import kr.opensoftlab.oslops.com.fms.web.service.FileMngService;
import kr.opensoftlab.oslops.com.vo.LoginVO;
import kr.opensoftlab.oslops.prj.prj1000.prj1000.service.Prj1000Service;
import kr.opensoftlab.oslops.req.req1000.req1000.service.Req1000Service;
import kr.opensoftlab.oslops.req.req1000.req1000.vo.Req1000VO;
import kr.opensoftlab.sdf.excel.BigDataSheetWriter;
import kr.opensoftlab.sdf.excel.ExcelDataListResultHandler;
import kr.opensoftlab.sdf.excel.Metadata;
import kr.opensoftlab.sdf.excel.SheetHeader;
import kr.opensoftlab.sdf.util.OslAgileConstant;
import kr.opensoftlab.sdf.util.PagingUtil;
import kr.opensoftlab.sdf.util.ReqHistoryMngUtil;
import kr.opensoftlab.sdf.util.RequestConvertor;

/**
 * @Class Name : Req1000Controller.java
 * @Description : Req1000Controller Controller class
 * @Modification Information
 *
 * @author 정형택
 * @since 2016.01.23.
 * @version 1.0
 * @see
 *  
 *  Copyright (C) OpenSoftLab Corp All right reserved.
 */

@Controller
public class Req1000Controller {

	/**
	 * Logging 을 위한 선언
	 * Log는 반드시 가이드에 맞춰서 작성한다. 개발용으로는 debug 카테고리를 사용한다.
	 */
	protected Logger Log = Logger.getLogger(this.getClass());

	/** Adm6000Service DI */
	@Resource(name = "adm6000Service")
	private Adm6000Service adm6000Service;
	
	/** Req1000Service DI */
	@Resource(name = "req1000Service")
	private Req1000Service req1000Service;
	
	/** Prj1000Service DI */
    @Resource(name = "prj1000Service")
    private Prj1000Service prj1000Service;
	
	/** EgovMessageSource */
	@Resource(name = "egovMessageSource")
	EgovMessageSource egovMessageSource;

	/** EgovPropertyService */
	@Resource(name = "propertiesService")
	protected EgovPropertyService propertiesService;

	/** TRACE */
	@Resource(name = "leaveaTrace")
	LeaveaTrace leaveaTrace;
	
	/** FileMngService */
   	@Resource(name="fileMngService")
   	private FileMngService fileMngService;
		
	@Value("${Globals.fileStorePath}")
	private String tempPath;

	/** EgovFileMngUtil - 파일 업로드 Util */
	@Resource(name="EgovFileMngUtil")
	private EgovFileMngUtil fileUtil;
	
	@Resource(name = "egovFileIdGnrService")
	private EgovIdGnrService idgenService;
	
	@Resource(name = "historyMng")
	private ReqHistoryMngUtil historyMng;
	
	/**
	 * Req1000 화면 이동(요구사항 요청화면)
	 * @param 
	 * @return 
	 * @exception Exception
	 */
	@SuppressWarnings({ "rawtypes" })
	@RequestMapping(value="/req/req1000/req1000/selectReq1000View.do")
	public String selectReq1000ListView(HttpServletRequest request, HttpServletResponse response, ModelMap model ) throws Exception {
		// request 파라미터를 map으로 변환
		Map<String, String> paramMap = RequestConvertor.requestParamToMapAddSelInfo(request, true);
		paramMap.put("prjId", request.getSession().getAttribute("selPrjId").toString());
		
		//프로젝트 단건 조회.
		Map prjInfo = (Map)prj1000Service.selectPrj1000Info(paramMap);
		model.addAttribute("currPrjInfo",prjInfo);
		
		return "/req/req1000/req1000/req1000";
	}
	
	/**
	 * Req1000 신규 요구사항 목록조회
	 * @param 
	 * @return 
	 * @exception Exception
	 */
	@RequestMapping(value="/req/req1000/req1000/selectReq1000ListAjaxView.do")
	public ModelAndView selectReq1000ListAjaxView(@ModelAttribute("req1000VO") Req1000VO req1000VO, HttpServletRequest request, HttpServletResponse response, ModelMap model ) throws Exception {

		try{
			// request 파라미터를 map으로 변환
			Map<String, String> paramMap = RequestConvertor.requestParamToMapAddSelInfo(request, true);
			//현재 페이지 값, 보여지는 개체 수
			String _pageNo_str = paramMap.get("pageNo");
			String _pageSize_str = paramMap.get("pageSize");
			
			int _pageNo = 1;
			int _pageSize = OslAgileConstant.pageSize;
			
			if(_pageNo_str != null && !"".equals(_pageNo_str)){
				_pageNo = Integer.parseInt(_pageNo_str)+1;  
			}
			if(_pageSize_str != null && !"".equals(_pageSize_str)){
				_pageSize = Integer.parseInt(_pageSize_str);  
			}
			
			//페이지 사이즈
			req1000VO.setPageIndex(_pageNo);
			req1000VO.setPageSize(_pageSize);
			req1000VO.setPageUnit(_pageSize);
			
			PaginationInfo paginationInfo = PagingUtil.getPaginationInfo(req1000VO);  /** paging - 신규방식 */

			List<Req1000VO> req1000List = null;

			HttpSession ss = request.getSession();
			LoginVO loginVO = (LoginVO) ss.getAttribute("loginVO");

			req1000VO.setReqUsrId(loginVO.getUsrId());
			req1000VO.setLoginUsrId(loginVO.getUsrId());
			req1000VO.setLicGrpId(loginVO.getLicGrpId());
			req1000VO.setPrjId((String) ss.getAttribute("selPrjId"));

			/**
			 * 목록 조회
			 */
			int totCnt = 0;
			req1000List = req1000Service.selectReq1000List(req1000VO);

			
			/** 총 데이터의 건수 를 가져온다. */
			totCnt = req1000Service.selectReq1000ListCnt(req1000VO);
			paginationInfo.setTotalRecordCount(totCnt);
			
			model.addAttribute("list", req1000List);
			
			//페이지 정보 보내기
			Map<String, Integer> pageMap = new HashMap<String, Integer>();
			pageMap.put("pageNo",req1000VO.getPageIndex());
			pageMap.put("listCount", req1000List.size());
			pageMap.put("totalPages", paginationInfo.getTotalPageCount());
			pageMap.put("totalElements", totCnt);
			pageMap.put("pageSize", _pageSize);
			
			//조회성공메시지 세팅
			model.addAttribute("errorYn", "N");
			model.addAttribute("page", pageMap);
			
			return new ModelAndView("jsonView");
		}
		catch(Exception ex){
			Log.error("selectReq1000ListView()", ex);
			//조회 실패메시지 세팅
			model.addAttribute("errorYn", "Y");
			throw new Exception(ex.getMessage());
		}
	}


    /**
	 * 선택된 요구사항의 세부정보를 가져온다. (단건조회)
     * @desc 1. 세션의 프로젝트 ID, 라이센스 그룹 ID를 가져온다.
     *       2. Map 에 1번 을 셋팅하고 쿼리를 호출한다.
     * @param request
     * @param response
     * @throws Exception
     */
    @SuppressWarnings({ "rawtypes" })
	@RequestMapping(value="/req/req1000/req1000/selectReq1000ReqInfoAjax.do")
	public ModelAndView selectReq1000ReqInfoAjax(HttpServletRequest request, HttpServletResponse response, ModelMap model )	throws Exception {
    	
    	try{
    		// request 파라미터를 map으로 변환
        	Map<String, String> paramMap = RequestConvertor.requestParamToMapAddSelInfo(request, true);
        	
			HttpSession ss = request.getSession();
			String prjId	= (String) ss.getAttribute("selPrjId");
			
			//paramMap에 넘어온 prjId가 있는지 확인(dsh3000에서 쓰임)
        	String paramPrjId = paramMap.get("prjId");
        	if(paramPrjId != null && !"".equals(paramPrjId)){
        		prjId = paramPrjId;
        	}
        	
			String licGrpId = ((LoginVO) ss.getAttribute("loginVO")).getLicGrpId();
        	
			paramMap.put("prjId", prjId);
			paramMap.put("licGrpId", licGrpId);
			
        	// 요구사항 단건정보 조회
        	Map reqInfoMap = (Map) req1000Service.selectReq1000ReqInfo(paramMap);        	
        	model.addAttribute("reqInfoMap", reqInfoMap);
        	List<FileVO> fileList = null;
        	int fileCnt = 0;
        	
        	if(reqInfoMap != null){
        		//atchFileId값을 넘기기 위한 FileVO
            	FileVO fileVO = new FileVO();
	        	fileVO.setAtchFileId((String)reqInfoMap.get("atchFileId"));
	        	
	        	//파일 리스트 조회
				fileList = fileMngService.fileDownList(fileVO);
				
				/*	마지막 FileSn구하기 
				 * 	<FileSn이 겹치지 않기 위해, 가장 큰 FileSn 값을 가져온다.>
				 */
				
				for(FileVO temp : fileList){
					if(fileCnt < Integer.parseInt(temp.getFileSn())){
						fileCnt = Integer.parseInt(temp.getFileSn());
					}
				}
        	}
			model.addAttribute("fileList",fileList);
			model.addAttribute("fileListCnt",fileCnt);
			
        	//조회 성공 메시지 세팅
        	model.addAttribute("message", egovMessageSource.getMessage("success.common.select"));
        	
        	return new ModelAndView("jsonView");
        	
    	}catch(Exception ex){
    		Log.error("selectReq1000ChkInfoAjax()", ex);
    		throw new Exception(ex.getMessage());
    	}
    }
    
	/**
	 * Req1001 요구사항 등록/수정 화면 호출
	 * @param 
	 * @return 
	 * @exception Exception
	 */
    @SuppressWarnings({ "rawtypes" })
	@RequestMapping(value="/req/req1000/req1000/selectReq1001View.do")
	public String selectReq1001View(HttpServletRequest request, HttpServletResponse response, ModelMap model ) throws Exception {
		try{
			HttpSession ss = request.getSession();
        	
			// request 파라미터를 map으로 변환
        	Map<String, String> paramMap = RequestConvertor.requestParamToMapAddSelInfo(request, true);
        	Map reqUsrInfoMap = null;
        	
        	LoginVO loginVO = (LoginVO) ss.getAttribute("loginVO");
        	String licGrpId = loginVO.getLicGrpId();
        	String pageType = paramMap.get("type");
        	String continueInsert = paramMap.get("continueInsert");

        	//세션
        	String  prjId = "";
        	// 세션의 선택된 프로젝트 아이디 가져오기
			if(paramMap.get("popupPrjId") != null){
				prjId = paramMap.get("popupPrjId");
			}
			else if(paramMap.get("prjId") != null){
				prjId = paramMap.get("prjId");
			}
			else{
				prjId = (String) ss.getAttribute("selPrjId");
			}
        	
        	//연속 등록 호출인경우
        	if(continueInsert != null && "Y".equals(continueInsert)){
        		Map<String, String> newMap = new HashMap<String, String>();
        		
        		newMap.put("licGrpId", licGrpId);
        		newMap.put("prjId", paramMap.get("ph_prjId"));
        		newMap.put("reqId", paramMap.get("ph_reqId"));
    			
            	// 요구사항 단건정보 조회
            	Map ph_reqInfoMap = (Map) req1000Service.selectReq1000ReqInfo(newMap);        	
            	model.addAttribute("ph_reqInfoMap", ph_reqInfoMap);
        	}
        	
        	// 요청자 id, 프로젝트 id 세팅
        	paramMap.put("reqUsrId", loginVO.getUsrId());
        	paramMap.put("prjId", prjId);
        	
        	// 등록일 경우  사용자 정보 조회
    		if( "insert".equals(pageType) ){
    			reqUsrInfoMap = (Map)req1000Service.selectReq1000ReqUserInfo(paramMap);
    			model.addAttribute("reqUsrInfoMap",reqUsrInfoMap);
    		}
    		

        	//파일 업로드 사이즈 구하기
			String fileInfoMaxSize = EgovProperties.getProperty("Globals.lunaops.fileInfoMaxSize");
			String fileSumMaxSize = EgovProperties.getProperty("Globals.lunaops.fileSumMaxSize");
			model.addAttribute("fileInfoMaxSize",fileInfoMaxSize);
			model.addAttribute("fileSumMaxSize",fileSumMaxSize);
			
    		// 팝업 페이지 정보전달 Map
    		model.addAttribute("pageType", pageType);
    		model.addAttribute("continueInsert", continueInsert);
    		
			return "/req/req1000/req1000/req1001";
		}
    	catch(Exception ex){
    		Log.error("selectReq1001View()", ex);
    		throw new Exception(ex.getMessage());
    	}
	
	}
    
    
	/**
	 * Req1000 요구사항 단건 등록 AJAX
	 * 요구사항 정보를 저장한다.
	 * @param 
	 * @return 
	 * @exception Exception
	 */
	@SuppressWarnings("rawtypes")
	@RequestMapping(value="/req/req1000/req1000/insertReq1001ReqInfoAjax.do")
	public ModelAndView insertReq1001ReqInfoAjax(HttpServletRequest request, HttpServletResponse response, ModelMap model ) throws Exception {

		try{
			MultipartHttpServletRequest mptRequest = (MultipartHttpServletRequest)request;
			// request 파라미터를 map으로 변환
			Map<String, String> paramMap = RequestConvertor.requestParamToMapAddSelInfo(request, true);
			HttpSession ss = request.getSession();

			//LoginVO에서 로그인 사용자명 가져오기
			LoginVO loginVO = (LoginVO) ss.getAttribute("loginVO");
			paramMap.put("whkRegUsrNm", loginVO.getUsrNm());
			
			//추가된 파일 고유 ID
			String _atchFileId = "NULL";
			
			// 파일 추가후 결과값
			List<FileVO> _result = fileUtil.fileUploadInsert(mptRequest,"",0,"Req");

			//추가된 파일이 존재하지 않을 경우 파일업로드 처리를 하지 않는다.
			if(_result != null){
				//첨부된 파일 DB등록
				_atchFileId = fileMngService.insertFileInfs(_result);  //DB에 생성된 첨부파일 ID를 리턴한다.
				paramMap.put("atchFileId",_atchFileId);
			}else{
				//추가된 파일이 없는 경우 atch_file_id 강제 생성
				String atchFileIdString = idgenService.getNextStringId();
				fileMngService.insertFileMasterInfo(atchFileIdString);
				paramMap.put("atchFileId",atchFileIdString);
			}

			// 프로젝트 정보 조회
        	Map prjInfo = req1000Service.selectReq1000ReqPrjInfo(paramMap);
        	
        	//프로젝트 약어 조회
        	String prjAcrm = (String) prjInfo.get("prjAcrm");
        	paramMap.put("prjAcrm", prjAcrm);
        	
			// 요구사항 등록
			Object insertKey = req1000Service.saveReq1000ReqInfo(paramMap);
			
			paramMap.put("reqId", (String)insertKey);
				
			// 요구사항 단건정보 조회
	        Map reqInfoMap = (Map) req1000Service.selectReq1000ReqInfo(paramMap);
			
			//그리드에 등록하기 위한 요구사항 정보
			model.addAttribute("reqInfo",reqInfoMap);

			//등록 성공 메시지 세팅
			model.addAttribute("message", egovMessageSource.getMessage("success.common.insert"));

			return new ModelAndView("jsonView");
		}
		catch(Exception ex){
			Log.error("insertReq1001ReqInfoAjax()", ex);

			//조회실패 메시지 세팅 및 저장 성공여부 세팅
			model.addAttribute("saveYN", "N");
			model.addAttribute("message", egovMessageSource.getMessage("fail.common.insert"));
			return new ModelAndView("jsonView");
		}
	}
    
	/**
	 * Req1000 요구사항 단건 수정 AJAX
	 * 요구사항 정보를 저장한다.
	 * @param 
	 * @return 
	 * @exception Exception
	 */
	@SuppressWarnings("rawtypes")
	@RequestMapping(value="/req/req1000/req1000/updateReq1001ReqInfoAjax.do")
	public ModelAndView updateReq1001ReqInfoAjax(HttpServletRequest request, HttpServletResponse response, ModelMap model ) throws Exception {

		try{
			MultipartHttpServletRequest mptRequest = (MultipartHttpServletRequest)request;
			
			// request 파라미터를 map으로 변환
			Map<String, String> paramMap = RequestConvertor.requestParamToMapAddSelInfo(request, true);
			Map<String, Object> paramFiles = new HashMap<String, Object>();
			HttpSession ss = request.getSession();
			
			//LoginVO에서 로그인 사용자명 가져오기
			LoginVO loginVO = (LoginVO) ss.getAttribute("loginVO");
			paramMap.put("whkRegUsrNm", loginVO.getUsrNm());
			
			List<FileVO> _result = new ArrayList<FileVO>();

			//AtchFileId, FileSn 가져오기
			String _atchFileId = paramMap.get("atchFileId");
			FileVO fVo = new FileVO();
			fVo.setAtchFileId(_atchFileId);
			
			int _FileSn = -1;
			_FileSn = fileMngService.getMaxFileSN(fVo);

			//atchFileId이 없을 경우, 새로 추가
			if("".equals(_atchFileId) || _atchFileId == null || "NULL".equals(_atchFileId)){
				//파일 추가 후 결과 값
				_result = fileUtil.fileUploadInsert(mptRequest,"",0,"Req");

				//추가된 파일이 존재하지 않을 경우 파일업로드 처리를 하지 않는다.
				if(_result != null){
					//첨부된 파일 DB등록
					_atchFileId = fileMngService.insertFileInfs(_result);  //DB에 생성된 첨부파일 ID를 리턴한다.
					paramMap.put("atchFileId",_atchFileId);
				}else{
					//추가된 파일이 없는 경우 atch_file_id 강제 생성
					String atchFileIdString = idgenService.getNextStringId();
					fileMngService.insertFileMasterInfo(atchFileIdString);
					paramMap.put("atchFileId",atchFileIdString);
				}
			}else if(_FileSn >= 0){
				//atchFileId는 있으나, fileSn이 없을 경우 fileSn을 0부터 세팅
				//파일 추가 후 결과 값
				_result = fileUtil.fileUploadInsert(mptRequest,_atchFileId,_FileSn,"Req");
				
				//추가된 파일이 존재하지 않을 경우 파일업로드 처리를 하지 않는다.
				if(_result != null){
					//첨부된 파일 DB등록 (AtchFileId는 보존하고, FileSn만 추가 - 파일 상세정보)
					_atchFileId = fileMngService.insertFileDetail(_result);  //DB에 생성된 첨부파일 ID를 리턴한다.
				}
			}else{
				paramMap.put("atchFileId",_atchFileId);
			}
/*
			String prjId = (String) ss.getAttribute("selPrjId");
			paramMap.put("prjId", prjId);*/
			paramFiles.put("fileList", _result);
			paramFiles.putAll(paramMap);
			
			//첨부파일 추가로 전달
			paramFiles.put("fileActionType", "add");
			
			// TODO 요구사항 정보 수정이력관리에 등록
			//historyMng.insertReqHistory(paramFiles , OslAgileConstant.EVENT_MODIFY);
			
			// 프로젝트 정보 조회
        	Map prjInfo = req1000Service.selectReq1000ReqPrjInfo(paramMap);
        	
        	//프로젝트 약어 조회
        	String prjAcrm = (String) prjInfo.get("prjAcrm");
        	paramMap.put("prjAcrm", prjAcrm);
        	
			// 요구사항 수정
			req1000Service.saveReq1000ReqInfo(paramMap);

			// 요구사항 단건정보 조회
			Map reqInfoMap = (Map) req1000Service.selectReq1000ReqInfo(paramMap);			
			
			//그리드에 등록하기 위한 요구사항 정보
			model.addAttribute("reqInfo",reqInfoMap);
			
			//수정 성공 메시지 세팅
			model.addAttribute("message", egovMessageSource.getMessage("success.common.update"));

			return new ModelAndView("jsonView");
		}
		catch(Exception ex){
			Log.error("updateReq1001ReqInfoAjax()", ex);

			//조회실패 메시지 세팅 및 저장 성공여부 세팅
			model.addAttribute("saveYN", "N");
			model.addAttribute("message", egovMessageSource.getMessage("fail.common.update"));
			return new ModelAndView("jsonView");
		}
	}
	
	/**
	 * Req1000 요구사항 상세보기 화면 호출
	 * @param request
	 * @param response
	 * @param model
	 * @return
	 * @throws Exception
	 */
	@SuppressWarnings("rawtypes")
	@RequestMapping(value="/req/req1000/req1000/selectReq1002View.do")
	public String selectReq1002View(HttpServletRequest request, HttpServletResponse response, ModelMap model ) throws Exception {
		try{
			// request 파라미터를 map으로 변환
        	Map<String, String> paramMap = RequestConvertor.requestParamToMapAddSelInfo(request, true);
        	
        	//세션
			HttpSession ss = request.getSession();
        	String  prjId = "";
        	// 세션의 선택된 프로젝트 아이디 가져오기
			if(paramMap.get("popupPrjId") != null){
				prjId = paramMap.get("popupPrjId");
			}
			else if(paramMap.get("prjId") != null){
				prjId = paramMap.get("prjId");
			}
			else{
				prjId = (String) ss.getAttribute("selPrjId");
			}
        	
        	paramMap.put("prjId", prjId);
        	
        	// 프로젝트 정보 조회
        	Map prjInfo = req1000Service.selectReq1000ReqPrjInfo(paramMap);
        	
        	String prjName = "";

        	if( prjInfo != null ){
        		prjName = (String)prjInfo.get("prjName");
        	}
        	
        	model.addAttribute("prjName",prjName);
        	model.addAttribute("prjId",prjId);
			
			
			return "/req/req1000/req1000/req1002";
		}
    	catch(Exception ex){
    		Log.error("selectReq1002View()", ex);
    		throw new Exception(ex.getMessage());
    	}
	}


	/**
	 * Req1000 신규 요구사항 요청관리 엑셀 조회
	 * @param 
	 * @return 
	 * @exception Exception
	 */
	@RequestMapping(value="/req/req1000/req1000/selectReq1000ExcelList.do")
	public ModelAndView selectReq1000ExcelList(@ModelAttribute("req1000VO") Req1000VO req1000VO,HttpServletRequest request, HttpServletResponse response, ModelMap model ) throws Exception {

		// 엑셀 다운로드 양식의 헤더명 선언
		String strPrjId 		= egovMessageSource.getMessage("excel.prjId");			// 프로젝트 ID
		String strPrjNm 		= egovMessageSource.getMessage("excel.prjNm");			// 프로젝트 명
		String strReqOrd 		= egovMessageSource.getMessage("excel.reqOrd");			// 요구사항 순번
		String strReqId 		= egovMessageSource.getMessage("excel.reqId");			// 요구사항ID
		String strReqProTypeNm 	= egovMessageSource.getMessage("excel.reqProTypeNm");	// 처리유형
		String strReqNewTypeNm 	= egovMessageSource.getMessage("excel.reqNewTypeNm");	// 접수유형
		String strReqNm 		= egovMessageSource.getMessage("excel.reqNm");			// 요구사항 명
		String strReqDesc 		= egovMessageSource.getMessage("excel.reqDesc");		// 요구사항 설명
		String strReqUsrNm 		= egovMessageSource.getMessage("excel.reqUsrNm");		// 요청자 명
		String strReqDtm 		= egovMessageSource.getMessage("excel.reqDtm");			// 요청일
		String strReUsrDeptNm 	= egovMessageSource.getMessage("excel.reqUsrDeptNm");	// 요청자 소속
		String strReqUsrPositionNm 	= egovMessageSource.getMessage("excel.reqUsrPositionNm");	// 요청자 직급
		String strReqUsrDutyNm 	= egovMessageSource.getMessage("excel.reqUsrDutyNm");	// 요청자 직책
		String strReqUsrEmail 	= egovMessageSource.getMessage("excel.reqUsrEmail");	// 요청자 이메일
		String strReqUsrNum 	= egovMessageSource.getMessage("excel.reqUsrNum");		// 요청자 연락처
		String strReqStDtm 		= egovMessageSource.getMessage("excel.reqStDtm");		// 작업시작일자
		String strReqEdDtm 		= egovMessageSource.getMessage("excel.reqEdDtm");		// 작업종료일자
		String strReqStDuDtm	= egovMessageSource.getMessage("excel.reqStDuDtm");		// 작업시작 예정일자
		String strReqEdDuDtm 	= egovMessageSource.getMessage("excel.reqEdDuDtm");		// 작업종료 예정일자
		String strRegDtm 		= egovMessageSource.getMessage("excel.regDtm");			// 최초등록일시
		String strRegUsrId 		= egovMessageSource.getMessage("excel.regUsrId");		// 최초등록자 ID
		String strRegUsrIp		= egovMessageSource.getMessage("excel.regUsrIp");		// 최초등록자 IP
		String strModifyDtm 	= egovMessageSource.getMessage("excel.modifyDtm");		// 최종수정일
		String strModifyUsrId 	= egovMessageSource.getMessage("excel.modifyUsrId");	// 최종수정자 ID
		String strModifyUsrIp 	= egovMessageSource.getMessage("excel.modifyUsrIp");	// 최종수정자 IP
				
		SheetHeader header = new SheetHeader(new String[]{strPrjId, strPrjNm, strReqOrd, strReqId, strReqProTypeNm, strReqNewTypeNm,
														strReqNm, strReqDesc, strReqUsrNm, strReqDtm, strReUsrDeptNm, strReqUsrPositionNm, strReqUsrDutyNm,
														strReqUsrEmail, strReqUsrNum, strReqStDuDtm, strReqEdDuDtm, strReqStDtm,
														strReqEdDtm, strRegDtm, strRegUsrId, strRegUsrIp, strModifyDtm,
														strModifyUsrId, strModifyUsrIp});
				
		/* 조회되는 데이터와 포멧 지정 
		 * ex 1. 수치형 new Metadata("property", XSSFCellStyle.ALIGN_RIGHT, "#,##0")
		 * ex 2. 사용자 지정 날짜 변환 new Metadata("property", ,"00-00-00") YYMMDD -> YY-MM-DD
		**/
		List<Metadata> metadatas = new ArrayList<Metadata>();
		metadatas.add(new Metadata("prjId"));
		metadatas.add(new Metadata("prjNm"));
		metadatas.add(new Metadata("reqOrd"));
		metadatas.add(new Metadata("reqId"));
		metadatas.add(new Metadata("reqProTypeNm"));
		metadatas.add(new Metadata("reqNewTypeNm"));		        
		metadatas.add(new Metadata("reqNm"));
		metadatas.add(new Metadata("reqDesc"));
		metadatas.add(new Metadata("reqUsrNm"));
		metadatas.add(new Metadata("reqDtm"));
		metadatas.add(new Metadata("reqUsrDeptNm"));
		metadatas.add(new Metadata("reqUsrPositionNm"));
		metadatas.add(new Metadata("reqUsrDutyNm"));
		metadatas.add(new Metadata("reqUsrEmail"));
		metadatas.add(new Metadata("reqUsrNum"));
		metadatas.add(new Metadata("reqStDuDtm"));
		metadatas.add(new Metadata("reqEdDuDtm"));
		metadatas.add(new Metadata("reqStDtm"));
		metadatas.add(new Metadata("reqEdDtm"));
		metadatas.add(new Metadata("regDtm"));
		metadatas.add(new Metadata("regUsrId"));
		metadatas.add(new Metadata("regUsrIp"));
		metadatas.add(new Metadata("modifyDtm"));
		metadatas.add(new Metadata("modifyUsrId"));
		metadatas.add(new Metadata("modifyUsrIp"));

		
		BigDataSheetWriter writer = new BigDataSheetWriter(URLEncoder.encode("사용자 요구사항 소요등록", "UTF-8"), tempPath, URLEncoder.encode("사용자 요구사항 소요등록", "UTF-8"), metadatas);

		writer.beginSheet();

		try{
			HttpSession ss = request.getSession();

			LoginVO loginVO = (LoginVO) ss.getAttribute("loginVO");

			req1000VO.setLoginUsrId(loginVO.getUsrId());
			req1000VO.setLicGrpId(loginVO.getLicGrpId());
			req1000VO.setPrjId((String) ss.getAttribute("selPrjId"));
			req1000VO.setReqUsrId(loginVO.getUsrId());

			ExcelDataListResultHandler  resultHandler = new ExcelDataListResultHandler(writer.getXMLSheetWriter(), writer.getStyleMap(), header, metadatas);

			req1000Service.selectReq1000ExcelList(req1000VO,resultHandler);
		}
		catch(Exception ex){
			Log.error("selectReq1000ExcelList()", ex);
			throw new Exception(ex.getMessage());
		}finally{
			writer.endSheet();
		}

		return writer.getModelAndView();
	}
	

	/**
	 * Req1000 개발공수, 담당자 AJAX
	 * 요구사항 정보를 수정한다.
	 * @param 
	 * @return 
	 * @exception Exception
	 */
	@SuppressWarnings("rawtypes")
	@RequestMapping(value="/req/req1000/req1000/updateReq1001ReqSubInfo.do")
	public ModelAndView updateReq1001ReqSubInfo(HttpServletRequest request, HttpServletResponse response, ModelMap model ) throws Exception {

		try{
			MultipartHttpServletRequest mptRequest = (MultipartHttpServletRequest)request;
			
			// request 파라미터를 map으로 변환
			Map<String, String> paramMap = RequestConvertor.requestParamToMapAddSelInfo(request, true);
			Map<String, Object> paramFiles = new HashMap<String, Object>();
			List<FileVO> _result = new ArrayList<FileVO>();
			req1000Service.updateReq1001ReqSubInfo(paramMap);

			HttpSession ss = request.getSession();
			LoginVO loginVO = (LoginVO) ss.getAttribute("loginVO");
			paramMap.put("prjId", (String) ss.getAttribute("selPrjId"));
			paramMap.put("licGrpId", (String) loginVO.getLicGrpId());
			
			//개발주기 요구사항 목록 불러오기
			List reqList = null;
			//AtchFileId, FileSn 가져오기
			String _atchFileId = paramMap.get("atchFileId");
			FileVO fVo = new FileVO();
			fVo.setAtchFileId(_atchFileId);
			
			int _FileSn = -1;
			_FileSn = fileMngService.getMaxFileSN(fVo);

			//atchFileId이 없을 경우, 새로 추가
			if("".equals(_atchFileId) || _atchFileId == null || "NULL".equals(_atchFileId)){
				//파일 추가 후 결과 값
				_result = fileUtil.fileUploadInsert(mptRequest,"",0,"Req");

				//추가된 파일이 존재하지 않을 경우 파일업로드 처리를 하지 않는다.
				if(_result != null){
					//첨부된 파일 DB등록
					_atchFileId = fileMngService.insertFileInfs(_result);  //DB에 생성된 첨부파일 ID를 리턴한다.
					paramMap.put("atchFileId",_atchFileId);
				}else{
					//추가된 파일이 없는 경우 atch_file_id 강제 생성
					String atchFileIdString = idgenService.getNextStringId();
					fileMngService.insertFileMasterInfo(atchFileIdString);
					paramMap.put("atchFileId",atchFileIdString);
				}
			}else if(_FileSn >= 0){
				//atchFileId는 있으나, fileSn이 없을 경우 fileSn을 0부터 세팅
				//파일 추가 후 결과 값
				_result = fileUtil.fileUploadInsert(mptRequest,_atchFileId,_FileSn,"Req");
				
				//추가된 파일이 존재하지 않을 경우 파일업로드 처리를 하지 않는다.
				if(_result != null){
					//첨부된 파일 DB등록 (AtchFileId는 보존하고, FileSn만 추가 - 파일 상세정보)
					_atchFileId = fileMngService.insertFileDetail(_result);  //DB에 생성된 첨부파일 ID를 리턴한다.
				}
			}else{
				paramMap.put("atchFileId",_atchFileId);
			}

			String reqListJson = (new GsonBuilder().serializeNulls().create()).toJsonTree(reqList).toString();
			paramFiles.put("fileList", _result);
			paramFiles.putAll(paramMap);
			
			model.addAttribute("reqList",reqList);
			model.addAttribute("reqListJson",reqListJson.replaceAll("<", "&lt"));

			//등록 성공 메시지 세팅
			model.addAttribute("saveYN", "Y");
			model.addAttribute("message", egovMessageSource.getMessage("success.common.save"));

			return new ModelAndView("jsonView");
		}
		catch(Exception ex){
			Log.error("updateReq1001ReqSubInfo()", ex);

			//조회실패 메시지 세팅 및 저장 성공여부 세팅
			model.addAttribute("saveYN", "N");
			model.addAttribute("message", egovMessageSource.getMessage("fail.common.save"));
			return new ModelAndView("jsonView");
		}
	}
	
	/**
	 * 요구사항 일괄삭제
	 * @param request
	 * @param response
	 * @param model
	 * @return
	 * @throws Exception
	 */
	@RequestMapping(value="/req/req1000/req1000/deleteReq1001ReqInfoAjax.do")
	public ModelAndView deleteReq1001ReqInfoAjax(HttpServletRequest request, HttpServletResponse response, ModelMap model ) throws Exception {
		try{
			// request 파라미터를 map으로 변환
			Map<String, Object> paramMap = RequestConvertor.requestParamToMapAddSelInfoList(request, true,"reqId");

			HttpSession ss = request.getSession();
			
			//LoginVO에서 로그인 사용자명 가져오기
			LoginVO loginVO = (LoginVO) ss.getAttribute("loginVO");
			paramMap.put("whkRegUsrNm", loginVO.getUsrNm());
			//삭제일경우
			paramMap.put("prjId", (String)ss.getAttribute("selPrjId"));
			
			// 요구사항 일괄삭제
			req1000Service.deleteReq1000ReqInfo(paramMap);

			//삭제 성공 메시지 세팅
			model.addAttribute("successYn", "Y");
			model.addAttribute("message", egovMessageSource.getMessage("success.common.delete"));

			return new ModelAndView("jsonView");

		}catch(Exception e){
			Log.error("deleteReq1001ReqInfoAjax()", e);

			model.addAttribute("successYn", "N");
			model.addAttribute("message", egovMessageSource.getMessage("fail.common.delete"));

			return new ModelAndView("jsonView");
		}
	}
	
	/**
	 * 하위 요구사항 추가 화면 이동
	 * @param request
	 * @param response
	 * @param model
	 * @return
	 * @throws Exception
	 */
	@SuppressWarnings("rawtypes")
	@RequestMapping(value="/req/req1000/req1000/selectReq1003View.do")
	public String selectReq1003View(HttpServletRequest request, HttpServletResponse response, ModelMap model ) throws Exception {
		try{
			// request 파라미터를 map으로 변환
        	Map<String, String> paramMap = RequestConvertor.requestParamToMapAddSelInfo(request, true);
        	String reqId = paramMap.get("selReqId");
        	String upperReqId = paramMap.get("upperReqId");
        	
        	//하위 요구사항인경우 요구사항 그룹 정보를 조회
        	if(!"top".equals(upperReqId)){
        		reqId = upperReqId;
        	}
        	paramMap.put("reqId", reqId);
        	
			// 요구사항 단건정보 조회
			Map reqInfoMap = (Map) req1000Service.selectReq1000ReqInfo(paramMap);
			model.addAttribute("reqInfoMap",reqInfoMap);
			
			return "/req/req1000/req1000/req1003";
		}
    	catch(Exception ex){
    		Log.error("selectReq1003View()", ex);
    		throw new Exception(ex.getMessage());
    	}
	}
}
