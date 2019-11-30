package kr.opensoftlab.oslops.stm.stm1000.stm1000.web;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import kr.opensoftlab.oslops.com.fms.web.service.FileMngService;
import kr.opensoftlab.oslops.com.vo.LoginVO;
import kr.opensoftlab.oslops.stm.stm1000.stm1000.service.Stm1000Service;
import kr.opensoftlab.oslops.stm.stm1000.stm1000.vo.Stm1000VO;
import kr.opensoftlab.sdf.util.OslAgileConstant;
import kr.opensoftlab.sdf.util.PagingUtil;
import kr.opensoftlab.sdf.util.ReqHistoryMngUtil;
import kr.opensoftlab.sdf.util.RequestConvertor;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.ApplicationContext;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.multipart.MultipartHttpServletRequest;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.mvc.annotation.DefaultAnnotationHandlerMapping;

import egovframework.com.cmm.EgovMessageSource;
import egovframework.com.cmm.service.EgovFileMngUtil;
import egovframework.com.cmm.service.EgovProperties;
import egovframework.com.cmm.service.FileVO;
import egovframework.rte.fdl.idgnr.EgovIdGnrService;
import egovframework.rte.fdl.property.EgovPropertyService;
import egovframework.rte.ptl.mvc.tags.ui.pagination.PaginationInfo;

/**
 * @Class Name : Stm1000Controller.java
 * @Description : Stm1000Controller Controller class
 * @Modification Information
 *
 * @author 공대영
 *  * @since 2018.07.23.
 * @version 1.0
 * @see
 *  
 *  Copyright (C)  All right reserved.
 */

@Controller
public class Stm1000Controller {

	/** 로그4j 로거 로딩 */
	private static final Logger Log = Logger.getLogger(Stm1000Controller.class);

	/** EgovMessageSource */
	@Resource(name = "egovMessageSource")
	EgovMessageSource egovMessageSource;

	/** EgovPropertyService */
	@Resource(name = "propertiesService")
	protected EgovPropertyService propertiesService;


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

	/** Stm1000Service DI */
	@Resource(name = "stm1000Service")
	private Stm1000Service stm1000Service;
	
    @Autowired
    private ApplicationContext ac;

    /**
	 * Stm1000 API 관리 화면으로 이동한다.
	 * @param request
	 * @param response
	 * @param model
	 * @return
	 * @throws Exception
	 */
	@RequestMapping(value="/stm/stm1000/stm1000/selectStm1000View.do")
	public String selectStm1000View( HttpServletRequest request, HttpServletResponse response, ModelMap model ) throws Exception {
		return "/stm/stm1000/stm1000/stm1000";
	}
	
	/**
	 * Stm1000 API 관리 등록/수정 팝업으로 이동한다.
	 * @param request
	 * @param response
	 * @param model
	 * @return
	 * @throws Exception
	 */
	@RequestMapping(value="/stm/stm1000/stm1000/selectStm1001View.do")
	public String selectStm1001View(HttpServletRequest request, HttpServletResponse response, ModelMap model ) throws Exception {
		try{
			// request 파라미터를 map으로 변환
        	Map<String, String> paramMap = RequestConvertor.requestParamToMapAddSelInfo(request, true);
        	
        	HttpSession ss = request.getSession();
    		LoginVO loginVO = (LoginVO) ss.getAttribute("loginVO");   		
    		model.addAttribute("regUsrId",loginVO.getUsrId());

        	//파일 업로드 사이즈 구하기
    		String fileInfoMaxSize = EgovProperties.getProperty("Globals.lunaops.fileInfoMaxSize");
    		String fileSumMaxSize = EgovProperties.getProperty("Globals.lunaops.fileSumMaxSize");
    		model.addAttribute("fileInfoMaxSize",fileInfoMaxSize);
    		model.addAttribute("fileSumMaxSize",fileSumMaxSize);
    		
			return "/stm/stm1000/stm1000/stm1001";
		}
    	catch(Exception ex){
    		Log.error("selectStm1001View()", ex);
    		throw new Exception(ex.getMessage());
    	}
	}

	
	/**
	 * Stm1000 API 목록을 조회한다.
	 * @param 
	 * @return 
	 * @exception Exception
	 */
	@RequestMapping(value="/stm/stm1000/stm1000/selectStm1000ListAjaxView.do")
	public ModelAndView selectStm1000ListAjaxView(@ModelAttribute("stm1000DAO") Stm1000VO stm1000VO, HttpServletRequest request, HttpServletResponse response, ModelMap model ) throws Exception {

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
			stm1000VO.setPageIndex(_pageNo);
			stm1000VO.setPageSize(_pageSize);
			stm1000VO.setPageUnit(_pageSize);
			
			
			PaginationInfo paginationInfo = PagingUtil.getPaginationInfo(stm1000VO);  /** paging - 신규방식 */

			List<Stm1000VO> stm1000List = null;

			HttpSession ss = request.getSession();
			LoginVO loginVO = (LoginVO) ss.getAttribute("loginVO");

			stm1000VO.setLoginUsrId(loginVO.getUsrId());
			stm1000VO.setLicGrpId(loginVO.getLicGrpId());
			stm1000VO.setPrjId((String) ss.getAttribute("selPrjId"));

			/**
			 * 목록 조회
			 */
			int totCnt = 0;
			stm1000List =  stm1000Service.selectStm1000List(stm1000VO);

			
			/** 총 데이터의 건수 를 가져온다. */
			totCnt =stm1000Service.selectStm1000ListCnt(stm1000VO);
			paginationInfo.setTotalRecordCount(totCnt);
			
			model.addAttribute("list", stm1000List);
			
			//페이지 정보 보내기
			Map<String, Integer> pageMap = new HashMap<String, Integer>();
			pageMap.put("pageNo",stm1000VO.getPageIndex());
			pageMap.put("listCount", stm1000List.size());
			pageMap.put("totalPages", paginationInfo.getTotalPageCount());
			pageMap.put("totalElements", totCnt);
			pageMap.put("pageSize", _pageSize);
			
			model.addAttribute("page", pageMap);
			
			// 조회성공 여부 및 조회 성공메시지 세팅
			model.addAttribute("errorYn", "N");
			model.addAttribute("message", egovMessageSource.getMessage("success.common.select"));
			
			return new ModelAndView("jsonView");
		}
		catch(Exception ex){
			Log.error("selectStm1000ListAjaxView()", ex);
			// 조회 실패여부 및 실패메시지 세팅
			model.addAttribute("errorYn", "Y");
			model.addAttribute("message", egovMessageSource.getMessage("fail.common.select"));
			return new ModelAndView("jsonView");
		}
	}
	
	/**
	 * Stm1000 API 단건 조회한다.
	 * @param request
	 * @param response
	 * @param model
	 * @return
	 * @throws Exception
	 */
	@SuppressWarnings("rawtypes")
	@RequestMapping(value="/stm/stm1000/stm1000/selectStm1001InfoAjax.do")
	public ModelAndView selectStm1001InfoAjax( HttpServletRequest request, HttpServletResponse response, ModelMap model ) throws Exception {

		try{
			// request 파라미터를 map으로 변환
			Map<String, String> paramMap = RequestConvertor.requestParamToMapAddSelInfo(request, true);
			//현재 페이지 값, 보여지는 개체 수
			
			HttpSession ss = request.getSession();
    		LoginVO loginVO = (LoginVO) ss.getAttribute("loginVO");

    		
        	paramMap.put("licGrpId", loginVO.getLicGrpId());
			
			paramMap.put("prjGrpId", (String)ss.getAttribute("selPrjGrpId"));
			
			Map apiInfoMap =  stm1000Service.selectStm1000Info(paramMap);
			
			
			model.addAttribute("apiInfoMap", apiInfoMap);
			
			
			List<FileVO> fileList = null;
        	int fileCnt = 0;
        	
        	if(apiInfoMap != null){
        		//atchFileId값을 넘기기 위한 FileVO
            	FileVO fileVO = new FileVO();
	        	fileVO.setAtchFileId((String)apiInfoMap.get("atchFileId"));
	        	
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
			
			
			return new ModelAndView("jsonView");
		}
		catch(Exception ex){
			Log.error("selectStm1001InfoAjax()", ex);
			throw new Exception(ex.getMessage());
		}
	}

	/**
	 * Stm1000 API 정보를 등록/수정한다.
	 * @param request
	 * @param response
	 * @param model
	 * @return
	 * @throws Exception
	 */
	@RequestMapping(value="/stm/stm1000/stm1000/saveStm1001InfoAjax.do")
	public ModelAndView saveApi1001InfoAjax(HttpServletRequest request, HttpServletResponse response, ModelMap model ) throws Exception {

		try{
			// request 파라미터를 map으로 변환
			Map<String, String> paramMap = RequestConvertor.requestParamToMapAddSelInfo(request, true);
			
			saveFile(paramMap,request);

			// API 정보 등록/수정
			Object insertKey = stm1000Service.saveStm1000Info(paramMap);
	
			//등록 성공 메시지 세팅
			model.addAttribute("message", egovMessageSource.getMessage("success.common.save"));

			return new ModelAndView("jsonView");
		}
		catch(Exception ex){
			Log.error("saveApi1001InfoAjax()", ex);

			// 등록 실패여부 및 실패메시지 세팅
			model.addAttribute("saveYN", "N");
			model.addAttribute("message", egovMessageSource.getMessage("fail.common.save"));
			return new ModelAndView("jsonView");
		}
	}
	
	/**
	 * Stm1000 첨부파일을 저장한다.
	 * @param request
	 * @param response
	 * @param model
	 * @return
	 * @throws Exception
	 */
	public void saveFile(Map<String, String> paramMap,HttpServletRequest request) throws Exception{
		MultipartHttpServletRequest mptRequest = (MultipartHttpServletRequest)request;
		
		Map<String, Object> paramFiles = new HashMap<String, Object>();
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
	}

	/**
	 * Stm1000 API를 삭제한다.
	 * @param request
	 * @param response
	 * @param model
	 * @return
	 * @throws Exception
	 */
	@RequestMapping(value="/stm/stm1000/stm1000/deleteStm1000InfoAjax.do")
	public ModelAndView deleteStm1000InfoAjax(HttpServletRequest request, HttpServletResponse response, ModelMap model ) throws Exception {

		try{
			// request 파라미터를 map으로 변환
			Map<String, String> paramMap = RequestConvertor.requestParamToMapAddSelInfo(request, true);
			
			int useCount = stm1000Service.selectStm1000UseCountInfo(paramMap);
			if(useCount==0){
				stm1000Service.deleteStm1000Info(paramMap);
				// 삭제 성공 메시지 세팅
				model.addAttribute("message", egovMessageSource.getMessage("success.common.delete"));
			}else{
				// 삭제 실패여부 및 사용중인 API는 삭제할수 없다는 메시지 세팅
				model.addAttribute("saveYN", "N");
				model.addAttribute("message", egovMessageSource.getMessage("fail.common.existInfo"));
			}
			
			return new ModelAndView("jsonView");
		}
		catch(Exception ex){
			Log.error("deleteStm1000InfoAjax()", ex);

			// 삭제 실패여부 및 실패메시지 세팅
			model.addAttribute("saveYN", "N");
			model.addAttribute("message", egovMessageSource.getMessage("fail.common.delete"));
			return new ModelAndView("jsonView");
		}
	}
	
	/**
	 * Stm1000 유효한 Rest Url 선택 팝업 화면 이동한다.
	 * @param request
	 * @param response
	 * @param model
	 * @return
	 * @throws Exception
	 */
	@RequestMapping(value="/stm/stm1000/stm1000/selectStm1002View.do")
	public String selectStm1002View( HttpServletRequest request, HttpServletResponse response, ModelMap model ) throws Exception {
		return "/stm/stm1000/stm1000/stm1002";
	}
	
	
	/**
	 * Stm1000 유효한 RestUrl 목록을 조회한다.
	 * @param request
	 * @param response
	 * @param model
	 * @return
	 * @throws Exception
	 */
	@SuppressWarnings({ "rawtypes", "unchecked" })
	@RequestMapping(value="/stm/stm1000/stm1000/selectStm1000ValidRestfulApiListAjax.do")
	public ModelAndView selectStm1000ValidRestfulApiListAjax(HttpServletRequest request, HttpServletResponse response, ModelMap model ) throws Exception {

		try{
			
			// request 파라미터를 map으로 변환
			Map<String, String> paramMap = RequestConvertor.requestParamToMapAddSelInfo(request, true);
			HttpSession ss = request.getSession();
			
			DefaultAnnotationHandlerMapping mapping = ac.getBean(DefaultAnnotationHandlerMapping.class);
			
			Map map = mapping.getHandlerMap();
			
			List<Map> urlList = new ArrayList<Map>();
			
			Set<String> keySet=map.keySet();
			
			Map urlMap = null;
			
			for( String  key   : keySet) {
				if(key.indexOf("/restapi")>-1){
					if(! "/".equals(key.substring(key.length()-1)) ){
						if(! ".*".equals(key.substring(key.length()-2)) ){
							urlMap = new HashMap();
							urlMap.put("url", key);
							urlList.add(urlMap);	
						}
					}
				}
			}
									
			model.addAttribute("urlList", urlList);			
			// 조회 성공메시지 세팅
			model.addAttribute("message", egovMessageSource.getMessage("success.common.select"));
			return new ModelAndView("jsonView");
		}
		catch(Exception ex){
			Log.error("selectStm1000ValidRestfulApiListAjax()", ex);
			// 조회 실패 메시지 세팅
			model.addAttribute("message", egovMessageSource.getMessage("fail.common.select"));
			return new ModelAndView("jsonView");
		}
	}

}
