
package kr.opensoftlab.oslops.req.req4000.req4100.web;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.sql.Timestamp;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;
import java.util.zip.ZipEntry;
import java.util.zip.ZipOutputStream;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.apache.log4j.Logger;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.multipart.MultipartHttpServletRequest;
import org.springframework.web.servlet.ModelAndView;

import com.google.gson.GsonBuilder;

import egovframework.com.cmm.EgovMessageSource;
import egovframework.com.cmm.EgovWebUtil;
import egovframework.com.cmm.service.EgovFileMngService;
import egovframework.com.cmm.service.EgovFileMngUtil;
import egovframework.com.cmm.service.EgovProperties;
import egovframework.com.cmm.service.FileVO;
import egovframework.com.utl.fcc.service.EgovStringUtil;
import egovframework.rte.fdl.cmmn.trace.LeaveaTrace;
import egovframework.rte.fdl.idgnr.EgovIdGnrService;
import egovframework.rte.fdl.property.EgovPropertyService;
import egovframework.rte.ptl.mvc.tags.ui.pagination.PaginationInfo;
import kr.opensoftlab.oslops.adm.adm6000.adm6000.service.Adm6000Service;
import kr.opensoftlab.oslops.cmm.cmm4000.cmm4000.service.Cmm4000Service;
import kr.opensoftlab.oslops.com.exception.UserDefineException;
import kr.opensoftlab.oslops.com.fms.web.service.FileMngService;
import kr.opensoftlab.oslops.com.vo.LoginVO;
import kr.opensoftlab.oslops.dpl.dpl1000.dpl1100.service.Dpl1100Service;
import kr.opensoftlab.oslops.dpl.dpl1000.dpl1100.vo.Dpl1100VO;
import kr.opensoftlab.oslops.prj.prj1000.prj1000.service.Prj1000Service;
import kr.opensoftlab.oslops.prj.prj1000.prj1100.service.Prj1100Service;
import kr.opensoftlab.oslops.req.req1000.req1000.service.Req1000Service;
import kr.opensoftlab.oslops.req.req2000.req2000.service.Req2000Service;
import kr.opensoftlab.oslops.req.req4000.req4100.service.Req4100Service;
import kr.opensoftlab.oslops.req.req4000.req4100.vo.Req4100VO;
import kr.opensoftlab.oslops.req.req4000.req4400.service.Req4400Service;
import kr.opensoftlab.oslops.req.req4000.req4800.service.Req4800Service;
import kr.opensoftlab.oslops.req.req4600.req4600.service.Req4600Service;
import kr.opensoftlab.sdf.excel.BigDataSheetWriter;
import kr.opensoftlab.sdf.excel.ExcelDataListResultHandler;
import kr.opensoftlab.sdf.excel.Metadata;
import kr.opensoftlab.sdf.excel.SheetHeader;
import kr.opensoftlab.sdf.excel.SheetParser;
import kr.opensoftlab.sdf.util.ModuleUseCheck;
import kr.opensoftlab.sdf.util.OslAgileConstant;
import kr.opensoftlab.sdf.util.PagingUtil;
import kr.opensoftlab.sdf.util.ReqHistoryMngUtil;
import kr.opensoftlab.sdf.util.RequestConvertor;

/**
 * @Class Name : Req4100Controller.java
 * @Description : Req4100Controller Controller class
 * @Modification Information
 *
 * @author 공대영
 * @since 2016.02.09.
 * @version 1.0
 * @see
 * 
 * 
 *  Copyright (C) OpenSoftLab Corp All right reserved.
 */

@Controller
public class Req4100Controller {

	/** 로그4j 로거 로딩 */
	private final Logger Log = Logger.getLogger(this.getClass());
	
	/** Adm6000Service DI */
	@Resource(name = "adm6000Service")
	private Adm6000Service adm6000Service;
	
	/** Dpl1100Service DI */
	@Resource(name = "dpl1100Service")
	private Dpl1100Service dpl1100Service;
	
	/** Req1000Service DI */
	@Resource(name = "req1000Service")
	private Req1000Service req1000Service;     
	
	/** Req2000Service DI */
	@Resource(name = "req2000Service")
	private Req2000Service req2000Service;       
		
	/** Cmm4000Service DI */
    @Resource(name = "cmm4000Service")
    private Cmm4000Service cmm4000Service;
	
	/** Req4100Service DI */
	@Resource(name = "req4100Service")
	private Req4100Service req4100Service;
	
	/** Req4400Service DI */
	@Resource(name = "req4400Service")
	private Req4400Service req4400Service;
	
	/** Req4600Service DI */
	@Resource(name = "req4600Service")
	private Req4600Service req4600Service;

	/** Req4800Service DI */
	@Resource(name = "req4800Service")
	private Req4800Service req4800Service;

	/** Prj1000Service DI */
    @Resource(name = "prj1000Service")
    private Prj1000Service prj1000Service;

    /** Prj1100Service DI */
    @Resource(name = "prj1100Service")
    private Prj1100Service prj1100Service;
    
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
	
	/** 파일 제어 서비스 */
	@Resource(name = "EgovFileMngService")
	private EgovFileMngService fileService;
	
	/** TRACE */
	@Resource(name = "leaveaTrace")
	LeaveaTrace leaveaTrace;
	
	@Value("${Globals.fileStorePath}")
	private String tempPath;
	
	@Resource(name = "egovFileIdGnrService")
	private EgovIdGnrService idgenService;
	
	@Resource(name = "historyMng")
	private ReqHistoryMngUtil historyMng;
	
	/** ModuleUseCheck DI */
	@Resource(name = "moduleUseCheck")
	private ModuleUseCheck moduleUseCheck;
	/**
	 * Req4100 화면 이동(이동시 요구사항 분류 정보 목록 조회)
	 * @param 
	 * @return 
	 * @exception Exception
	 */
	@SuppressWarnings({ "rawtypes" })
	@RequestMapping(value="/req/req4000/req4100/selectReq4100View.do")
	public String selectReq4100View(@ModelAttribute("req4100VO") Req4100VO req4100VO, HttpServletRequest request, HttpServletResponse response, ModelMap model ) throws Exception {

		//	request 파라미터를 map으로 변환
		Map<String, String> paramMap = RequestConvertor.requestParamToMapAddSelInfo(request, true);
		paramMap.put("prjId", request.getSession().getAttribute("selPrjId").toString());
		
		//프로젝트 단건 조회
		Map prjInfo = (Map)prj1000Service.selectPrj1000Info(paramMap);
		model.addAttribute("currPrjInfo",prjInfo);
		return "/req/req4000/req4100/req4100";
	}
	
	/**
	 * Req4100 요구사항 목록 조회 AJAX
	 * @param 
	 * @return 
	 * @exception Exception
	 */
	@RequestMapping(value="/req/req4000/req4100/selectReq4100ListAjaxView.do")
	public ModelAndView selectReq4100ListAjaxView(@ModelAttribute("req4100VO") Req4100VO req4100VO, HttpServletRequest request, HttpServletResponse response, ModelMap model ) throws Exception {

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
			req4100VO.setPageIndex(_pageNo);
			req4100VO.setPageSize(_pageSize);
			req4100VO.setPageUnit(_pageSize);
			
			PaginationInfo paginationInfo = PagingUtil.getPaginationInfo(req4100VO);  /** paging - 신규방식 */

			List<Req4100VO> req4100List = null;

			HttpSession ss = request.getSession();
			LoginVO loginVO = (LoginVO) ss.getAttribute("loginVO");

			req4100VO.setLoginUsrId(loginVO.getUsrId());
			req4100VO.setLicGrpId(loginVO.getLicGrpId());
			req4100VO.setPrjId((String) ss.getAttribute("selPrjId"));



			/** 총 데이터의 건수 를 가져온다. */
			int totCnt = req4100Service.selectReq4100ListCnt(req4100VO);
			paginationInfo.setTotalRecordCount(totCnt);
			
			req4100List = req4100Service.selectReq4100List(req4100VO);

			model.addAttribute("list", req4100List);

			//페이지 정보 보내기
			Map<String, Integer> pageMap = new HashMap<String, Integer>();
			pageMap.put("pageNo",req4100VO.getPageIndex());
			pageMap.put("listCount", req4100List.size());
			pageMap.put("totalPages", paginationInfo.getTotalPageCount());
			pageMap.put("totalElements", totCnt);
			pageMap.put("pageSize", _pageSize);

			model.addAttribute("page", pageMap);
			
			//등록 성공 결과 값
			model.addAttribute("selectYN", "Y");
			return new ModelAndView("jsonView");
		}
		catch(Exception ex){
			Log.error("selectReq4100List()", ex);
			//등록 실패 결과 값
			model.addAttribute("selectYN", "N");
			return new ModelAndView("jsonView");
		}
	}

	
	/**
	 * Req4100 요구사항 등록/수정 화면 이동
	 * @param 
	 * @return 
	 * @exception Exception
	 */
	@SuppressWarnings("rawtypes")
	@RequestMapping(value="/req/req4000/req4100/selectReq4101View.do")
	public String selectReq4101View(HttpServletRequest request, HttpServletResponse response, ModelMap model ) throws Exception {
		Map<String, String> paramMap = RequestConvertor.requestParamToMapAddSelInfo(request, true);
		
		try{
		//프로젝트 단건 조회.
		Map prjInfo = (Map)prj1000Service.selectPrj1000Info(paramMap);
		model.addAttribute("currPrjInfo",prjInfo);
		

    	//파일 업로드 사이즈 구하기
		String fileInfoMaxSize = EgovProperties.getProperty("Globals.lunaops.fileInfoMaxSize");
		String fileSumMaxSize = EgovProperties.getProperty("Globals.lunaops.fileSumMaxSize");
		model.addAttribute("fileInfoMaxSize",fileInfoMaxSize);
		model.addAttribute("fileSumMaxSize",fileSumMaxSize);
		
		model.addAttribute("pageType", paramMap.get("type"));
		
		}catch(Exception ex){ 
			Log.error("selectReq4101View()", ex);
			throw new Exception(ex.getMessage());
		}
		return "/req/req4000/req4100/req4101";
	}

	/**
	 * Req4100 요구사항 엑셀 업로드 팝업 이동
	 * @param 
	 * @return 
	 * @exception Exception
	 */
	@RequestMapping(value="/req/req4000/req4100/selectReq4103View.do")
	public String selectReq4103View(HttpServletRequest request, HttpServletResponse response, ModelMap model ) throws Exception {
			return "/req/req4000/req4100/req4103";
	}



	/**
	 * Req4100 요구사항 정보 단건 조회 AJAX
	 * 요구사항 정보를 조회한다.
	 * @param 
	 * @return 
	 * @exception Exception
	 */
	@SuppressWarnings("rawtypes")
	@RequestMapping(value="/req/req4000/req4100/selectReq4102ReqInfoAjax.do")
	public ModelAndView selectReq4102ReqInfoAjax(HttpServletRequest request, HttpServletResponse response, ModelMap model ) throws Exception {

		try{
			// request 파라미터를 map으로 변환
			Map<String, String> paramMap = RequestConvertor.requestParamToMapAddSelInfo(request, true);

			// 요구사항 단건정보 조회
			Map reqInfoMap = (Map) req4100Service.selectReq4102ReqInfoAjax(paramMap);
			model.addAttribute("reqInfoMap", reqInfoMap);
			
			String atchFileId = (String)reqInfoMap.get("atchFileId");
			List<FileVO> fileList = null;
			int fileCnt = 0;
			if(reqInfoMap != null){
					
				//atchFileId값을 넘기기 위한 FileVO
	        	FileVO fileVO = new FileVO();
	        	fileVO.setAtchFileId(atchFileId);
	        	
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
			
			//등록 성공 메시지 세팅
			model.addAttribute("message", egovMessageSource.getMessage("success.common.select"));
			return new ModelAndView("jsonView");
		}
		catch(Exception ex){
			Log.error("selectReq4102ReqInfoAjax()", ex);

			//조회실패 메시지 세팅 및 저장 성공여부 세팅
			model.addAttribute("saveYN", "N");
			model.addAttribute("message", egovMessageSource.getMessage("fail.common.select"));
			return new ModelAndView("jsonView");
		}
	}



	@SuppressWarnings({ "unchecked", "rawtypes" })
	@RequestMapping(value="/req/req4000/req4100/excelUpload.do" ,method = RequestMethod.POST)
	public ModelAndView uploadExcelParseToAjax(final MultipartHttpServletRequest multiRequest,  HttpServletResponse response ) throws Exception {
		List<Object> excelList =null;
		Map jsonMap =new HashMap();

		Map<String, MultipartFile> fileMap = multiRequest.getFileMap();
		try{
			Set<Entry<String,  MultipartFile>> entrySet = fileMap.entrySet();
			Class clz = Req4100VO.class;
			
			for (Entry<String,  MultipartFile> entry : entrySet) {
				SheetParser parser = new SheetParser(entry.getValue().getInputStream(),1);
				excelList = parser.createEntity(clz, 1);
			}
		}
		catch(Exception ex){
			Log.error("selectExcelUploadView()", ex);
			throw new Exception(ex.getMessage());
		}

		jsonMap.put("parseList", excelList);
		return  new ModelAndView("jsonView",jsonMap);
	}

	/**
	 * Req4100 요구사항 단건 저장 AJAX
	 * @param 
	 * @return 
	 * @exception Exception
	 */
	@SuppressWarnings("rawtypes")
	@RequestMapping(value="/req/req4000/req4100/insertReq4100ReqInfoAjax.do")
	public ModelAndView insertReq4100ReqInfoAjax(HttpServletRequest request, HttpServletResponse response, ModelMap model ) throws Exception {

		try{
			MultipartHttpServletRequest mptRequest = (MultipartHttpServletRequest)request;
			// request 파라미터를 map으로 변환
			Map<String, String> paramMap = RequestConvertor.requestParamToMapAddSelInfo(request, true);
			HttpSession ss = request.getSession();

			String prjId = (String) ss.getAttribute("selPrjId");
			paramMap.put("prjId", prjId);
			
			//추가된 파일 고유 ID
			String _atchFileId = "NULL";

			//파일 추가 후 결과 값
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

			// 요구사항 단건 저장
			String insertKey = req4100Service.insertReq4100ReqInfoAjax(paramMap);
			paramMap.put("reqId", insertKey);
			
			// 요구사항 단건정보 조회
			Map reqInfoMap = (Map) req4100Service.selectReq4102ReqInfoAjax(paramMap);
			
			model.addAttribute("reqInfoMap", reqInfoMap);
			
			//그리드에 등록하기 위한 요구사항 정보
			model.addAttribute("reqInfo",reqInfoMap);
			
			//등록 성공 메시지 세팅
			model.addAttribute("message", egovMessageSource.getMessage("success.common.insert"));

			//요구사항 정보 수정이력관리에 등록
			//historyMng.insertReqHistory(new HashMap<String, Object>(paramMap) , OslAgileConstant.EVENT_INSERT);
			
			return new ModelAndView("jsonView");
		}
		catch(Exception ex){
			Log.error("insertReq4100ReqInfoAjax()", ex);

			//조회실패 메시지 세팅 및 저장 성공여부 세팅
			model.addAttribute("saveYN", "N");
			model.addAttribute("message", egovMessageSource.getMessage("fail.common.insert"));
			return new ModelAndView("jsonView");
		}
	}


	/**
	 * Req4100 요구사항 단건 수정 AJAX
	 * @param 
	 * @return 
	 * @exception Exception
	 */
	@SuppressWarnings("rawtypes")
	@RequestMapping(value="/req/req4000/req4100/updateReq4100ReqInfoAjax.do")
	public ModelAndView updateReq4100ReqInfoAjax(HttpServletRequest request, ModelMap model ) throws Exception {

		try{
			MultipartHttpServletRequest mptRequest = (MultipartHttpServletRequest)request;
			
			// request 파라미터를 map으로 변환
			Map<String, String> paramMap = RequestConvertor.requestParamToMapAddSelInfo(request, true);
			Map<String, Object> paramFiles = new HashMap<String, Object>();
			HttpSession ss = request.getSession();
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

			String prjId = (String) ss.getAttribute("selPrjId");
			paramMap.put("prjId", prjId);
			paramFiles.put("fileList", _result);
			paramFiles.putAll(paramMap);

			//첨부파일 추가로 전달
			paramFiles.put("fileActionType", "add");

			// 요구사항 일괄저장
			req4100Service.updateReq4100ReqInfoAjax(paramFiles);
			
			
			// 요구사항 단건정보 조회
			Map reqInfoMap = (Map) req4100Service.selectReq4102ReqInfoAjax(paramMap);
			
			//그리드에 등록하기 위한 요구사항 정보
			model.addAttribute("reqInfo",reqInfoMap);
						
			//등록 성공 메시지 세팅
			model.addAttribute("message", egovMessageSource.getMessage("success.common.update"));

			return new ModelAndView("jsonView");
		}
		catch(Exception ex){
			Log.error("updateReq4100ReqInfoAjax()", ex);

			//조회실패 메시지 세팅 및 저장 성공여부 세팅
			model.addAttribute("saveYN", "N");
			model.addAttribute("message", egovMessageSource.getMessage("fail.common.update"));
			return new ModelAndView("jsonView");
		}
	}



	/**
	 * Req4100 요구사항 일괄저장 AJAX (엑셀 업로드 시 요구사항 저장)
	 * 요구사항 정보를 일괄 저장한다.
	 * @param 
	 * @return 
	 * @exception Exception
	 */
	@SuppressWarnings("rawtypes")
	@RequestMapping(value="/req/req4000/req4100/insertReq4100ReqInfoListAjax.do")
	public ModelAndView insertReq4100ReqInfoListAjax(HttpServletRequest request, HttpServletResponse response, ModelMap model ) throws Exception {

		try{
			// request 파라미터를 map으로 변환
			Map<String, String> paramMap = RequestConvertor.requestParamToMap(request, true);
			HttpSession ss = request.getSession();
			LoginVO loginVO = (LoginVO) ss.getAttribute("loginVO");

			String prjId = (String) ss.getAttribute("selPrjId");
			String licGrpId = loginVO.getLicGrpId();

			paramMap.put("regUsrId", ((LoginVO)ss.getAttribute("loginVO")).getUsrId());
			paramMap.put("licGrpId", licGrpId);
			paramMap.put("selPrjId", prjId);

			// 프로젝트 단건 조회
			Map prjInfo = (Map)prj1000Service.selectPrj1000Info(paramMap);
			
			// 요구사항 일괄저장
			req4100Service.insertReq4100ReqInfoListAjax(paramMap, prjInfo, historyMng);

			//등록 성공 메시지 세팅
			model.addAttribute("message", egovMessageSource.getMessage("success.common.insert"));

			return new ModelAndView("jsonView");
		}
		catch(Exception ex){
			Log.error("insertReq4100ReqCommentInfoAjax()", ex);

			//조회실패 메시지 세팅 및 저장 성공여부 세팅
			model.addAttribute("saveYN", "N");
			model.addAttribute("message", egovMessageSource.getMessage("fail.common.insert"));
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
	@RequestMapping(value="/req/req4000/req4100/deleteReq4100ReqInfoAjax.do")
	public ModelAndView deleteReq4100ReqInfoAjax(HttpServletRequest request, HttpServletResponse response, ModelMap model ) throws Exception {
		try{
			// request 파라미터를 map으로 변환
			Map<String, Object> paramMap = RequestConvertor.requestParamToMapAddSelInfoList(request, true,"reqId");

			HttpSession ss = request.getSession();

			String prjId = (String) ss.getAttribute("selPrjId");


			req4100Service.deleteReq4100ReqInfoAjax(paramMap,prjId);

			// 삭제 성공 메시지 세팅
			model.addAttribute("successYn", "Y");
			model.addAttribute("message", egovMessageSource.getMessage("success.common.delete"));
			
			return new ModelAndView("jsonView");

		}catch(Exception e){
			Log.error("deleteReq4100ReqInfoAjax()", e);

			model.addAttribute("successYn", "N");
			model.addAttribute("message", egovMessageSource.getMessage("fail.common.delete"));

			return new ModelAndView("jsonView");
		}

	}

	/**
	 * Req4104 선택한 요구사항 단건 조회 (+변경 이력)
	 * @param 
	 * @return 
	 * @exception Exception
	 */
	@SuppressWarnings({ "rawtypes", "unchecked" })
	@RequestMapping(value="/req/req4000/req4100/selectReq4104View.do")
    public String selectSpr1004View(HttpServletRequest request, HttpServletResponse response, ModelMap model ) throws Exception {
    	
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
			
			String prjGrpId = (String)ss.getAttribute("selPrjGrpId");
			
			//태그된 요구사항의 프로젝트 그룹 Id
			String tagReqPrjGrpId = paramMap.get("reqPrjGrpId");
			
			//태그된 요구사항과 현재 요구사항이 다를경우
			if(tagReqPrjGrpId != null && !"".equals(tagReqPrjGrpId) && !prjGrpId.equals(tagReqPrjGrpId)){
				prjGrpId = tagReqPrjGrpId;
			}
			
			//프로젝트 그룹 Id세팅
			paramMap.put("prjGrpId", prjGrpId);
			model.addAttribute("prjGrpId",prjGrpId);
			
			//로그인VO 가져오기
    		LoginVO loginVO = (LoginVO) ss.getAttribute("loginVO");
    		
    		paramMap.put("usrId", loginVO.getUsrId());
    		
			List<Map> prjList = (List)prj1000Service.selectPrj1000View(paramMap);

			//프로젝트 그룹명
			String targetprjGrpNm = "";
			
			//프로젝트명
			String targetPrjNm = "";
			
			//프로젝트 타입
			String targetPrjTaskTargetCd = "";
			
			for(Map prjInfo : prjList){
				if(prjGrpId.equals(prjInfo.get("prjId"))){
					targetprjGrpNm = (String)prjInfo.get("prjNm");
				}
				else if(prjId.equals(prjInfo.get("prjId"))){
					targetPrjNm = (String)prjInfo.get("prjNm");
					targetPrjTaskTargetCd = (String)prjInfo.get("prjTaskTypeCd");
				}
			}
			model.addAttribute("targetprjGrpNm",targetprjGrpNm);
			model.addAttribute("targetPrjNm",targetPrjNm);
			model.addAttribute("targetPrjTaskTargetCd",targetPrjTaskTargetCd);
			
			// 요구사항 단건정보 조회
			Map reqInfoMap = (Map) req4100Service.selectReq4102ReqInfoAjax(paramMap);

			List reqCommentList = null;
			if(reqInfoMap != null){
				//요구사항 프로세스 Id
				String processId = (String) reqInfoMap.get("processId");
				paramMap.put("processId",processId);
				
				if(reqInfoMap.get("atchFileId") == null || "".equals(reqInfoMap.get("atchFileId"))){
					//atch_file_id 강제 생성
					String atchFileIdString = idgenService.getNextStringId();
					fileMngService.insertFileMasterInfo(atchFileIdString);
					
					paramMap.put("prjId",prjId);
					paramMap.put("atchFileId",atchFileIdString);
					
					//요구사항 수정
		        	req4100Service.updateReq4100ReqInfoAjax(paramMap);
		        	
		        	//요구사항 조회 데이터에 atchFileId 넣기
		        	reqInfoMap.remove("atchFileId");
					reqInfoMap.put("atchFileId",atchFileIdString);
				}
				//요구사항 코멘트 목록 조회
				if(paramMap.get("popupPrjId") == null) {
					paramMap.put("popupPrjId",prjId);
				}
				reqCommentList = (List) req2000Service.selectReq2000ReqCommentListAjax(paramMap);
				
				paramMap.put("prjId",prjId);
				String atchFileId = (String)reqInfoMap.get("atchFileId");
				
				if(!"".equals(atchFileId) || atchFileId != null){
					//atchFileId값을 넘기기 위한 FileVO
		        	FileVO fileVO = new FileVO();
		        	fileVO.setAtchFileId((String)reqInfoMap.get("atchFileId"));
		        	
		        	//개발주기 종료 시점 넣기
		        	if(paramMap.get("sprStatusCd") != null && "03".equals(paramMap.get("sprStatusCd"))){
		        		fileVO.setCreatDt((String)reqInfoMap.get("modifyDtm"));
		        	}
		        	
		        	//파일 리스트 조회
					List<FileVO> fileList = fileMngService.fileDownList(fileVO);
					String fileListJson			= (new GsonBuilder().serializeNulls().create()).toJsonTree(fileList).toString();
					model.addAttribute("fileList",fileListJson);
					
					//파일SN구하기
					int _fileSn = fileMngService.getMaxFileSN(fileVO);
					model.addAttribute("fileSn",_fileSn);
				}else{
					model.addAttribute("fileList",null);
				}
			}else{
				//요구사항이 없는 경우 에러
				model.addAttribute("popupChk","Y");
				model.addAttribute("exceptionMessage",egovMessageSource.getMessage("req4100.notFoundReq"));

				return "/err/error";

			}
			/********************************
			 * 요구사항 변경 이력 데이터 불러오기
			 * - 요구사항 추가 항목 조회
			 * - 요구사항 작업 내용 조회
			 * - 요구사항 변경 이력 조회
			 * - 요구사항 검수 승인&거부 이력 조회
			 * - 조회된 리스트 데이터를 하나의 리스트로 병합
			 * - 요구사항 클립보드 데이터 조회
			 * - toJson으로 전달
			 ********************************/
			//추가 항목 정보 불러오기
			List<Map> addOptList = (List<Map>) prj1100Service.selectFlw1200ReqOptList(paramMap);
			
			//변경이력 전체 가져오기(담당자 포함)
			paramMap.put("chgAllView", "Y");
			//변경이력
			List<Map> reqChgList = req4100Service.selectReq4700ReqHistoryList(paramMap);
			//결재 정보
			List<Map> reqChkList = req4100Service.selectReq4900ReqSignList(paramMap);
			// 요구사항 수정이력 조회
			List reqChgDetailList = req4800Service.selectReq4800ChgDetailList(paramMap);
			
			// 배포계획 배정된 요구사항 조회를 위한 VO 세팅
			Dpl1100VO dpl1100VO = new Dpl1100VO();
			// 라이선스 그룹 ID 세팅
			dpl1100VO.setLicGrpId(paramMap.get("licGrpId"));
			// 프로젝트 ID 세팅
			dpl1100VO.setPrjId(prjId);
			// 요구사항 ID 세팅
			dpl1100VO.setReqId(paramMap.get("reqId"));
			
			//배포 계획에 배정된 요구사항 조회
			List<Map> reqDplList = dpl1100Service.selectDpl1100ExistDplList(dpl1100VO);
			
			//검수 정보 데이터를 변경 이력 데이터에 추가 (시간 비교해서 추가)
			int chkIndex = 0;
			Date chgDtm;
			Date regDtm;
			Map reqChkInfo;

			if(reqChkList != null && reqChkList.size() > 0){
				//for(Map reqInfo : reqChgList){
				for(int i=0;i<reqChgList.size();i++){
					Map reqInfo = reqChgList.get(i);
					//chkIndex값이 검수 데이터 수보다 큰 경우 루프 종료
					if(chkIndex >= reqChkList.size()){
						break;
					}
					
					//변경 이력 일시
					chgDtm = new Date(((Timestamp)reqInfo.get("chgDtm")).getTime());
					//검수정보 Map 가져오기
					reqChkInfo = reqChkList.get(chkIndex);
					
					//검수 데이터 등록 일시
					regDtm = new Date(((Timestamp)reqChkInfo.get("signDtm")).getTime());
					
					//chgDtm이 regDtm보다 지난 날짜인 경우
					if(chgDtm.compareTo(regDtm) >= 0){
						reqChgList.add(i,reqChkInfo);
						chkIndex++;
					}
				}
				//추가되지 않은 검수 데이터 있는 경우 모두 추가
				for(int i=chkIndex;i<reqChkList.size();i++){
					Map reqInfo = reqChkList.get(i);
					reqChgList.add(reqInfo);
				}
			}
			
			//요구사항 클립보드 데이터
			List<Map> cbContentList = req4600Service.selectReq4600CBContentList(paramMap);
			/*************************************/
			//배포 모듈 체크
			boolean jenkinsModuleUseChk = moduleUseCheck.isJenkinsModuleUseChk();
			//svnkit 모듈 체크
			boolean svnkitModuleUseChk = moduleUseCheck.isSvnKitModuleUseChk();
			
			//쪽지에서 오픈된 상세정보인지 확인(뷰용)
			String viewer = (String)paramMap.get("viewer");
			model.addAttribute("viewer", viewer);
			
			String reqChgDetailListJson 	= (new GsonBuilder().serializeNulls().create()).toJson(reqChgDetailList).toString();
			String reqInfoMapJson 		= (new GsonBuilder().serializeNulls().create()).toJson(reqInfoMap).toString();
        	String reqCommentListJson	= (new GsonBuilder().serializeNulls().create()).toJsonTree(reqCommentList).toString();
        	String reqChgListJson	= (new GsonBuilder().serializeNulls().create()).toJsonTree(reqChgList).toString();
        	String cbContentListJson	= (new GsonBuilder().serializeNulls().create()).toJsonTree(cbContentList).toString();
        	String addOptListJson	= (new GsonBuilder().serializeNulls().create()).toJsonTree(addOptList).toString();
        	String reqDplListJson	= (new GsonBuilder().serializeNulls().create()).toJsonTree(reqDplList).toString();
        	
        	model.addAttribute("reqChgDetailList", reqChgDetailListJson.replaceAll("<", "&lt"));
			model.addAttribute("reqInfoMap", reqInfoMapJson.replaceAll("<", "&lt"));
			model.addAttribute("reqCommentList", reqCommentListJson.replaceAll("<", "&lt"));
			model.addAttribute("reqChgList", reqChgListJson.replaceAll("<", "&lt"));
			model.addAttribute("cbContentList", cbContentListJson.replaceAll("<", "&lt"));
			model.addAttribute("addOptList", addOptListJson.replaceAll("<", "&lt"));
			model.addAttribute("reqDplList", reqDplListJson.replaceAll("<", "&lt"));
			//신규 요구사항인지 여부
			model.addAttribute("newReqChk", paramMap.get("newReqChk"));
			model.addAttribute("mode",paramMap.get("mode"));
			model.addAttribute("jenkinsModuleUseChk",jenkinsModuleUseChk);
			model.addAttribute("svnkitModuleUseChk",svnkitModuleUseChk);
			
			model.addAttribute("prjId", reqInfoMap.get("prjId"));
			model.addAttribute("reqId", reqInfoMap.get("reqId"));
			
			// 어디서 요구사항 상세보기 팝업을 호출했는지 판단하기 위한 값
			// 통합대시보드, 쪽지에서 다른 프로젝트에 있는 요구사항을 상세보기 했을 경우 해당 값을 넘겨준다.
			model.addAttribute("callView", paramMap.get("callView"));
    		
        	return "/req/req4000/req4100/req4104";
    	}
    	catch(Exception ex){
    		Log.error("selectReq4104View()", ex);
    		throw new Exception(ex.getMessage());
    	}
    }
	
	/**
	 * Req4105 요구사항 업무 페이지(req4105.jsp) 업로드 Ajax
	 * @param 
	 * @return 
	 * @exception Exception
	 */
	@SuppressWarnings({ "unused" })
	@RequestMapping(value="/req/req4000/req4100/insertReq4105FileUploadAjax.do")
    public ModelAndView insertReq4105FileUploadAjax(HttpServletRequest request, HttpServletResponse response, ModelMap model ) throws Exception {
    	
    	try{
    		MultipartHttpServletRequest mptRequest = (MultipartHttpServletRequest)request;
    		
    		// request 파라미터를 map으로 변환
        	Map<String, String> paramMap = RequestConvertor.requestParamToMapAddSelInfo(request, true);
        	
        	HttpSession ss = request.getSession();
        	paramMap.put("prjId", (String)paramMap.get("selPrjId"));
           	String _atchFileId = paramMap.get("atchFileId");
			
           	FileVO fvo = new FileVO();
           	fvo.setAtchFileId(_atchFileId);
           	
           	//Max값 세팅
           	int _fileSn = -1;
        	_fileSn = fileMngService.getMaxFileSN(fvo);
           	
			if(_fileSn >= 1){
				//파일 추가 후 결과 값
				List<FileVO> _result = fileUtil.fileUploadInsert(mptRequest,_atchFileId,_fileSn,"Req");
				
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
				Map<String, Object> hisLogParamMap = new HashMap<String, Object>(paramMap);
				hisLogParamMap.put("fileList", _result);
				//첨부파일 추가로 전달
				hisLogParamMap.put("fileActionType", "add");
				req4800Service.insertReq4800ModifyHistoryList(hisLogParamMap);
			}
			
        	//등록 성공 메시지 세팅
    		model.addAttribute("addFileId", _atchFileId);
        	model.addAttribute("addFileSn", _fileSn);
        	
        	model.addAttribute("message", egovMessageSource.getMessage("req4100.success.upload"));
        	model.addAttribute("saveYN", "Y");
        	return new ModelAndView("jsonView");
    	}
    	catch(Exception ex){
    		Log.error("insertReq4105FileUploadAjax()", ex);

    		//업로드 실패 메시지 세팅 및 저장 성공여부 세팅
    		model.addAttribute("saveYN", "N");
    		model.addAttribute("message", egovMessageSource.getMessage("fail.request.msg"));
    		return new ModelAndView("jsonView");
    	}
    }
	
	/**
	 * Req4104 요구사항 상세 페이지 클립보드 붙여넣기
	 * @param 
	 * @return 
	 * @exception Exception
	 */
	@RequestMapping(value="/req/req4000/req4100/insertReq4104CBPaste.do")
    public ModelAndView insertReq4104CBPaste(HttpServletRequest request, HttpServletResponse response, ModelMap model ) throws Exception {
    	
    	try{
    		MultipartHttpServletRequest mptRequest = (MultipartHttpServletRequest)request;
    		
    		// request 파라미터를 map으로 변환
        	Map<String, String> paramMap = RequestConvertor.requestParamToMapAddSelInfo(request, true);
        	
        	//입력내용
        	String notepad_contents = (String)paramMap.get("notepad_contents");
        	
        	// 썸네일 보기 유무
        	String cbThumViewCd = paramMap.get("cbThumViewCd");
        	
        	//파일 값 받음
        	Map<String, MultipartFile> files = mptRequest.getFileMap();
        	
        	//추가된 파일 목록
        	List<FileVO> _result = null;
        	//클립보드 구분 코드 (01 = 파일, 02 = 텍스트)
        	String cbGbCd = "";
        	
        	//시작 파일Sn 저장
        	int start_fileSn = 0;
        	
        	//파일 있는지 체크 (이미지)
        	if(files.size() > 0){
        		//파일
        		cbGbCd = "01";
        		
        		//CB_ATCH_FILE_ID 체크
        		String cbAtchFileId = paramMap.get("cbAtchFileId");
        		
        		//클립보드 파일ID가 없는 경우
        		if(cbAtchFileId == null ||  "".equals(cbAtchFileId)){
        			//파일ID생성 (디렉토리 = ClipBoard)
    				_result = fileUtil.fileUploadInsert(mptRequest,"",0,"ClipBoard");
    				
    				//추가된 파일이 존재하지 않을 경우 파일업로드 처리를 하지 않는다.
    				if(_result != null){
    					//첨부된 파일 DB등록
    					cbAtchFileId = fileMngService.insertFileInfs(_result);  //DB에 생성된 첨부파일 ID를 리턴한다.
    					paramMap.put("cbAtchFileId",cbAtchFileId);
    					paramMap.put("cbFileSn","0");
    					
    		           	//요구사항 cbAtchFileId 수정
    					req4600Service.updateReq4100CbAtchFileId(paramMap);
    		        	
    					
    		        	model.addAttribute("fileList", _result);
    					
    				}else{
    					//업로드 실패 메시지 세팅 및 저장 성공여부 세팅
    		    		model.addAttribute("errorYN", "Y");
    		    		model.addAttribute("message", egovMessageSource.getMessage("fail.request.msg"));
    		    		return new ModelAndView("jsonView");
    				}
        		}else{
        			//파일ID가 있는 경우 파일 업로드 
        			FileVO fvo = new FileVO();
                   	fvo.setAtchFileId(cbAtchFileId);
                   	
                   	//fileSn 구하기
                	int _fileSn = fileMngService.getMaxFileSN(fvo);
                	start_fileSn = _fileSn;
                	
                	if(_fileSn >= 1){
        				//파일 추가 후 결과 값
        				_result = fileUtil.fileUploadInsert(mptRequest,cbAtchFileId,_fileSn,"ClipBoard");

        				//추가된 파일이 존재하지 않을 경우 파일업로드 처리를 하지 않는다.
        				if(_result != null && !(_result.isEmpty())){
        					//첨부된 파일 DB등록 (cbAtchFileId는 보존하고, FileSn만 추가 - 파일 상세정보)
        					cbAtchFileId = fileMngService.insertFileDetail(_result);  //DB에 생성된 첨부파일 ID를 리턴한다.
        					paramMap.put("cbFileSn",String.valueOf(_fileSn));
        				}else{
        					
        					//업로드 실패 메시지 세팅 및 저장 성공여부 세팅
        		    		model.addAttribute("errorYN", "Y");
        		    		model.addAttribute("message", egovMessageSource.getMessage("fail.request.msg"));
        		    		return new ModelAndView("jsonView");
        				}
        			}
        		}
        		
        		//추가된 파일이 있는 경우에만 img 대체 작업 시작
        		if(_result != null){
	            	//HTML로 읽기(img src 대체 구문)
	            	Document doc = (Document) Jsoup.parse(notepad_contents);
	            	Elements eles = doc.select("img[name=paste_img][srcchgchk!=\"Y\"]");
	            	
	            	//실제 이미지 배열 인덱스(fileList)
	            	int imgCnt = 0;
	            	
	            	//찾은 img tag 작업 시작
	            	for(Element e: eles){
	            		String imgObj_atchFileId = _result.get(imgCnt).atchFileId;
	            		String imgObj_atchFileSn = _result.get(imgCnt).fileSn;
	            		e.attr("src","/cmm/fms/getImage.do?fileSn="+imgObj_atchFileSn+"&atchFileId="+imgObj_atchFileId);
	            		e.attr("srcchgchk","Y");
	            		e.attr("atchId",imgObj_atchFileId);
	            		e.attr("fileSn",imgObj_atchFileSn);
	            		imgCnt++;
	            	}
	            	
	            	notepad_contents = doc.body().html();
        		}
        	}else{
        		//텍스트
        		cbGbCd = "02";
        	}
        	
        	
        	
        	paramMap.put("cbGbCd", cbGbCd);
        	paramMap.put("cbContentText", notepad_contents);
        	
        	String type = (String)paramMap.get("type");
        	
        	int updateChk = -1;
        	//파일 또는 텍스트만 저장
        	if("insert".equals(type) && cbGbCd != null && !"".equals(cbGbCd)){
        		int maxReqCbSeq = req4600Service.selectReq4600CBMaxSeq(paramMap);
        		req4600Service.insertReq4600CBPaste(paramMap);
        		model.addAttribute("reqCbSeq", maxReqCbSeq);
        		model.addAttribute("writeUsrId", paramMap.get("regUsrId"));
        		
        	}else if("update".equals(type)){
        		updateChk = req4600Service.updateReq4600CBContentInfo(paramMap);
        		model.addAttribute("writeUsrId", paramMap.get("modifyUsrId"));
        	}
        	
        	
        	model.addAttribute("updateChk", updateChk);
        	model.addAttribute("cbThumViewCd", cbThumViewCd);
        	model.addAttribute("cbGbCd", cbGbCd);
        	model.addAttribute("notepad_contents", notepad_contents);
        	model.addAttribute("start_fileSn", start_fileSn);
        	
        	model.addAttribute("errorYN", "N");
        	return new ModelAndView("jsonView");
    	}
    	catch(Exception ex){
    		Log.error("insertReq4104CBPaste()", ex);
    		System.out.println(ex);

    		//업로드 실패 메시지 세팅 및 저장 성공여부 세팅
    		model.addAttribute("errorYN", "Y");
    		model.addAttribute("message", egovMessageSource.getMessage("fail.request.msg"));
    		return new ModelAndView("jsonView");
    	}
    }
	/**
	 * Req4104 요구사항 클립보드 데이터 단건 조회
	 * @param 
	 * @return 
	 * @exception Exception
	 */
	@SuppressWarnings("rawtypes")
	@RequestMapping(value="/req/req4000/req4100/selectReq4104CBPaste.do")
    public ModelAndView selectReq4104CBPaste(HttpServletRequest request, HttpServletResponse response, ModelMap model ) throws Exception {
    	
    	try{

    		// request 파라미터를 map으로 변환
        	Map<String, String> paramMap = RequestConvertor.requestParamToMapAddSelInfo(request, true);
        	
        	//클립보드 데이터 조회
        	Map cbData = req4600Service.selectReq4600CBContentInfo(paramMap);
        	
        	model.addAttribute("cbData", cbData);
        	
        	model.addAttribute("errorYN", "N");
        	return new ModelAndView("jsonView");
    	}
    	catch(Exception ex){
    		Log.error("selectReq4104CBPaste()", ex);
    		System.out.println(ex);

    		//업로드 실패 메시지 세팅 및 저장 성공여부 세팅
    		model.addAttribute("errorYN", "Y");
    		model.addAttribute("message", egovMessageSource.getMessage("fail.common.select"));
    		return new ModelAndView("jsonView");
    	}
    }
	
	/**
	 * Req4104 요구사항 클립보드 데이터 단건 삭제
	 * @param 
	 * @return 
	 * @exception Exception
	 */
	@RequestMapping(value="/req/req4000/req4100/deleteReq4104CBPaste.do")
    public ModelAndView deleteReq4104CBPaste(HttpServletRequest request, HttpServletResponse response, ModelMap model ) throws Exception {
    	
    	try{

    		// request 파라미터를 map으로 변환
        	Map<String, String> paramMap = RequestConvertor.requestParamToMapAddSelInfo(request, true);
        	
        	//클립보드 데이터 삭제
        	req4600Service.deleteReq4600CBContentInfo(paramMap);
        	
        	model.addAttribute("errorYN", "N");
        	model.addAttribute("message", egovMessageSource.getMessage("success.common.delete"));
        	return new ModelAndView("jsonView");
    	}
    	catch(Exception ex){
    		Log.error("selectReq4104CBPaste()", ex);
    		System.out.println(ex);

    		//업로드 실패 메시지 세팅 및 저장 성공여부 세팅
    		model.addAttribute("errorYN", "Y");
    		model.addAttribute("message", egovMessageSource.getMessage("fail.common.delete"));
    		return new ModelAndView("jsonView");
    	}
    }
	
	/**
	 * - 요구사항 프로세스 변경 화면
	 * - 요구사항 작업흐름 변경
	 * - 작업 관리
	 * - 결재자 지정
	 * @param request
	 * @param response
	 * @param model
	 * @return
	 * @throws Exception
	 */
	@SuppressWarnings({ "rawtypes", "unchecked" })
	@RequestMapping(value="/req/req4000/req4100/selectReq4105View.do")
	public String selectReq4105View(HttpServletRequest request, HttpServletResponse response, ModelMap model ) throws Exception {
		try{
			// request 파라미터를 map으로 변환
        	Map<String, String> paramMap = RequestConvertor.requestParamToMapAddSelInfo(request, true);
        	HttpSession ss = request.getSession();
			
        	//선택 권한그룹 타입
        	String usrTyp = (String)ss.getAttribute("usrTyp");
        	if("01".equals(usrTyp)){
        		response.setStatus(401);
        		return "/req/req4000/req4100/req4105";
        	}
        	
        	//파일 업로드 사이즈 구하기
        	String fileInfoMaxSize = EgovProperties.getProperty("Globals.lunaops.fileInfoMaxSize");
			String fileSumMaxSize = EgovProperties.getProperty("Globals.lunaops.fileSumMaxSize");
			model.addAttribute("fileInfoMaxSize",fileInfoMaxSize);
			model.addAttribute("fileSumMaxSize",fileSumMaxSize);
			
        	//프로젝트 리스트
        	List<Map> prjList = (List<Map>)ss.getAttribute("prjList");
        	
        	//프로젝트 그룹 Id, 프로젝트 Id
        	String prjGrpId = (String)ss.getAttribute("selPrjGrpId");
        	String prjId = (String)ss.getAttribute("selPrjId");
        	
        	//프로젝트 그룹명
			String targetprjGrpNm = "";
			
			//프로젝트명
			String targetPrjNm = "";
			
			//프로젝트 정보
			Map selPrjInfo = null;
			
			for(Map prjInfo : prjList){
				if(prjGrpId.equals(prjInfo.get("prjId"))){
					targetprjGrpNm = (String)prjInfo.get("prjNm");
				}
				else if(prjId.equals(prjInfo.get("prjId"))){
					targetPrjNm = (String)prjInfo.get("prjNm");
					selPrjInfo = prjInfo;
				}
			}
			model.addAttribute("selPrjInfo",selPrjInfo);
			model.addAttribute("targetprjGrpNm",targetprjGrpNm);
			model.addAttribute("targetPrjNm",targetPrjNm);
			model.addAttribute("prjId",prjId);
			model.addAttribute("prjGrpId",prjGrpId);
			
			//요구사항 Id
        	String reqId = paramMap.get("reqId");
        	model.addAttribute("reqId", reqId);

        	//접속자 Id
        	LoginVO loginVO = (LoginVO) ss.getAttribute("loginVO");
        	String usrId = loginVO.getUsrId();
        	model.addAttribute("usrId", usrId);
			return "/req/req4000/req4100/req4105";
		}
    	catch(Exception ex){
    		Log.error("selectReq4105View()", ex);
    		throw new Exception(ex.getMessage());
    	}
	}
	
	/**
	 * - 요구사항 요청 접수 완료
	 * (프로세스 지정)
	 * @param request
	 * @param response
	 * @param model
	 * @return
	 * @throws Exception
	 */
	@SuppressWarnings({ "rawtypes", "unchecked" })
	@RequestMapping(value="/req/req4000/req4100/selectReq4106View.do")
	public String selectReq4106View(HttpServletRequest request, HttpServletResponse response, ModelMap model ) throws Exception {
		try{
			// request 파라미터를 map으로 변환
        	Map<String, String> paramMap = RequestConvertor.requestParamToMapAddSelInfo(request, true);
        	HttpSession ss = request.getSession();
        	
        	//선택 권한그룹 타입
        	String usrTyp = (String)ss.getAttribute("usrTyp");
        	if("01".equals(usrTyp)){
        		response.setStatus(401);
        		return "/req/req4000/req4100/req4105";
        	}
        	
        	//프로젝트 리스트
        	List<Map> prjList = (List<Map>)ss.getAttribute("prjList");
        	
        	//프로젝트 그룹 Id, 프로젝트 Id
        	String prjGrpId = (String)ss.getAttribute("selPrjGrpId");
        	String prjId = (String)ss.getAttribute("selPrjId");
        	
        	//프로젝트 그룹명
			String targetprjGrpNm = "";
			
			//프로젝트명
			String targetPrjNm = "";
			
			//프로젝트 정보
			Map selPrjInfo = null;
			
			for(Map prjInfo : prjList){
				if(prjGrpId.equals(prjInfo.get("prjId"))){
					targetprjGrpNm = (String)prjInfo.get("prjNm");
				}
				else if(prjId.equals(prjInfo.get("prjId"))){
					targetPrjNm = (String)prjInfo.get("prjNm");
					selPrjInfo = prjInfo;
				}
			}
			model.addAttribute("selPrjInfo",selPrjInfo);
			model.addAttribute("targetprjGrpNm",targetprjGrpNm);
			model.addAttribute("targetPrjNm",targetPrjNm);
			
        	String reqId = paramMap.get("reqId");
        	model.addAttribute("reqId", reqId);

			return "/req/req4000/req4100/req4106";
		}
    	catch(Exception ex){
    		Log.error("selectReq4106View()", ex);
    		throw new Exception(ex.getMessage());
    	}
	}
	
	/**
	 * - 요구사항 작업지시 추가&수정
	 * @param request
	 * @param response
	 * @param model
	 * @return
	 * @throws Exception
	 */
	@SuppressWarnings("rawtypes")
	@RequestMapping(value="/req/req4000/req4100/selectReq4107View.do")
	public String selectReq4107View(HttpServletRequest request, HttpServletResponse response, ModelMap model ) throws Exception {
		try{
			// request 파라미터를 map으로 변환
        	Map<String, String> paramMap = RequestConvertor.requestParamToMapAddSelInfo(request, true);
        	
        	String type = (String) paramMap.get("type");
        	
        	//수정인경우
        	if("update".equals(type)){
        		//작업 단건 조회
        		Map workInfo = req4400Service.selectReq4400ReqWorkInfo(paramMap); 
        		model.addAttribute("workInfo",workInfo);
        		model.addAttribute("workId",workInfo.get("workId"));
        	}else{
        		model.addAttribute("workId",null);
        		
        	}
        	model.addAttribute("type",type);
        	
			return "/req/req4000/req4100/req4107";
		}
    	catch(Exception ex){
    		Log.error("selectReq4107View()", ex);
    		throw new Exception(ex.getMessage());
    	}
	}
	
	/**
	 * - 요구사항 작업 종료
	 * @param request
	 * @param response
	 * @param model
	 * @return
	 * @throws Exception
	 */
	@SuppressWarnings({ "rawtypes", "unchecked" })
	@RequestMapping(value="/req/req4000/req4100/selectReq4109View.do")
	public String selectReq4109View(HttpServletRequest request, HttpServletResponse response, ModelMap model ) throws Exception {
		try{
			// request 파라미터를 map으로 변환
        	Map<String, String> paramMap = RequestConvertor.requestParamToMapAddSelInfo(request, true);
        	
    		//작업 단건 조회
    		Map workInfo = req4400Service.selectReq4400ReqWorkInfo(paramMap); 
    		model.addAttribute("workInfo",workInfo);
    		model.addAttribute("workId",workInfo.get("workId"));
    		
    		//데이터 전달
    		model.addAttribute("type",paramMap.get("type"));
    		model.addAttribute("reqId",paramMap.get("reqId"));
    		model.addAttribute("processId",paramMap.get("processId"));
    		model.addAttribute("flowId",paramMap.get("flowId"));
    		
    		//세션
    		HttpSession ss = request.getSession();
        	
        	//프로젝트 리스트
        	List<Map> prjList = (List<Map>)ss.getAttribute("prjList");
    		//프로젝트 그룹 Id, 프로젝트 Id
        	String prjId = (String)ss.getAttribute("selPrjId");
        	
			//프로젝트 정보
			Map selPrjInfo = null;
			
			for(Map prjInfo : prjList){
				if(prjId.equals(prjInfo.get("prjId"))){
					selPrjInfo = prjInfo;
					break;
				}
			}
			model.addAttribute("selPrjInfo",selPrjInfo);
			
			return "/req/req4000/req4100/req4109";
		}
    	catch(Exception ex){
    		Log.error("selectReq4109View()", ex);
    		throw new Exception(ex.getMessage());
    	}
	}
	
	/**
	 * - 요구사항 결재 반려 사유 입력
	 * @param request
	 * @param response
	 * @param model
	 * @return
	 * @throws Exception
	 */
	@RequestMapping(value="/req/req4000/req4100/selectReq4108View.do")
	public String selectReq4108View(HttpServletRequest request, HttpServletResponse response, ModelMap model ) throws Exception {
		return "/req/req4000/req4100/req4108";
	}
	
	/**
	 * Req4104 첨부파일 전체 압축하기
	 * @param 
	 * @return 
	 * @exception Exception
	 */
	@RequestMapping(value="/req/req4000/req4100/selectReq4104FileZipUpload.do")
    public ModelAndView selectReq4104FileZipUpload(HttpServletRequest request, HttpServletResponse response, ModelMap model ) throws Exception {
    	try{
    		// request 파라미터를 map으로 변환
        	Map<String, String> paramMap = RequestConvertor.requestParamToMapAddSelInfo(request, true);

        	//AtchFileId
        	String atchFileId = paramMap.get("atchFileId");
        	
        	//FileSn 배열 구하기
        	String[] FileSns = request.getParameterValues("FileSn");
        	
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
    		
    		try{
	        	ZipOutputStream out = new ZipOutputStream(new FileOutputStream(zipFilePath+zipFileName));
	        	
	        	//FileSns 루프 돌면서 파일 데이터 가져오기
	        	for(int i=0;i<FileSns.length;i++){
	        		FileVO fileVO = new FileVO();
	    			fileVO.setAtchFileId(atchFileId);
	    			fileVO.setFileSn(FileSns[i]);
	    			FileVO fvo = fileService.selectFileInf(fileVO);
	    			
	    			File uFile = new File(fvo.getFileStreCours(), fvo.getStreFileNm());
	    			
	    			FileInputStream in = new FileInputStream(uFile);
	    	        String fileName = fvo.getOrignlFileNm().toString();
	    	                
	    	        ZipEntry ze = new ZipEntry(fileName);
	    	        out.putNextEntry(ze);
	    	          
	    	        int len;
	    	        while ((len = in.read(buf)) > 0) {
	    	            out.write(buf, 0, len);
	    	        }
	    	          
	    	        out.closeEntry();
	    	        in.close();
	        	}
	        	out.close();
	        	
	        	//atchFileId 강제 생성
	        	String atchFileIdString = idgenService.getNextStringId();
	        	
	        	/*#atchFileId#, #fileSn#, #fileStreCours#, #streFileNm#, 
				  #orignlFileNm#, #fileExtsn#, #fileMg#, #fileCn#,SYSDATE*/
	        	
	        	//파일 추가 성공한 경우 FileVO 세팅
	        	FileVO fileVO = new FileVO();
	        	fileVO.setAtchFileId(atchFileIdString);
	        	fileVO.setFileSn("0");
	        	fileVO.setFileStreCours(zipFilePath);
	        	fileVO.setStreFileNm(zipFileName);
	        	fileVO.setOrignlFileNm(zipFileName);
	        	fileVO.setFileExtsn("zip");
	        	fileVO.setFileMg("0");
	        	
	        	List<FileVO> fList = new ArrayList<FileVO>();
	        	fList.add(fileVO);
	        	
	        	String _atchFileId = fileMngService.insertFileInfs(fList);
	        	
	        	model.addAttribute("atchFileId", _atchFileId);
	        	model.addAttribute("errorYN", "N");
	        	model.addAttribute("message", egovMessageSource.getMessage("success.common.select"));
	        	return new ModelAndView("jsonView");
    		}catch (IOException e) {
    			Log.error("selectReq4104FileZipUpload()", e);
    		    
    		    //업로드 실패 메시지 세팅 및 저장 성공여부 세팅
        		model.addAttribute("errorYN", "Y");
        		model.addAttribute("message", egovMessageSource.getMessage("fail.common.select"));
        		return new ModelAndView("jsonView");
    		}
        	
        	
        	
    	}
    	catch(Exception ex){
    		Log.error("selectReq4104FileZipUpload()", ex);

    		//업로드 실패 메시지 세팅 및 저장 성공여부 세팅
    		model.addAttribute("errorYN", "Y");
    		model.addAttribute("message", egovMessageSource.getMessage("fail.common.select"));
    		return new ModelAndView("jsonView");
    	}
    }
	/**
	 * Req4104 첨부파일 전체 압축하기
	 * @param 
	 * @return 
	 * @exception Exception
	 */
	@RequestMapping(value="/req/req4000/req4100/selectReq4104ReqHisInfoList.do")
    public ModelAndView selectReq4104ReqHisInfoList(HttpServletRequest request, HttpServletResponse response, ModelMap model ) throws Exception {
    	try{
    		// request 파라미터를 map으로 변환
			//Map<String, String> paramMap = RequestConvertor.requestParamToMapAddSelInfo(request, true);
			return new ModelAndView("jsonView");
    	}
    	catch(Exception ex){
    		Log.error("selectReq4104ReqHisInfoList()", ex);

    		//업로드 실패 메시지 세팅 및 저장 성공여부 세팅
    		model.addAttribute("errorYN", "Y");
    		model.addAttribute("message", egovMessageSource.getMessage("fail.common.select"));
    		return new ModelAndView("jsonView");
    	}
    }

	/**
	 * Req4105 데이터 불러오기
	 * @param 
	 * @return 
	 * @exception Exception
	 */
	@SuppressWarnings({ "rawtypes", "unchecked" })
	@RequestMapping(value="/req/req4000/req4100/selectReq4105DataLoad.do")
    public ModelAndView selectReq4105DataLoad(HttpServletRequest request, HttpServletResponse response, ModelMap model ) throws Exception {
    	try{
    		// request 파라미터를 map으로 변환
			Map<String, String> paramMap = RequestConvertor.requestParamToMapAddSelInfo(request, true);
			String  prjId = "";
			// 세션의 선택된 프로젝트 아이디 가져오기
			if(paramMap.get("popupPrjId") != null){
				prjId = paramMap.get("popupPrjId");
			}
			else if(paramMap.get("prjId") != null){
				prjId = paramMap.get("prjId");
			}
			else{
				prjId = (String) request.getSession().getAttribute("selPrjId");
			}
			
			paramMap.put("prjId", prjId);
		
			//요구사항 단건 조회
			Map reqInfoMap = (Map) req4100Service.selectReq4102ReqInfoAjax(paramMap);
			
			//요구사항 파일목록 조회
			String atchFileId = (String)reqInfoMap.get("atchFileId");
			
			List<FileVO> fileList = null;
			if(!"".equals(atchFileId) || atchFileId != null){
				//atchFileId값을 넘기기 위한 FileVO
	        	FileVO fileVO = new FileVO();
	        	fileVO.setAtchFileId((String)reqInfoMap.get("atchFileId"));
	        	
	        	//파일 리스트 조회
				fileList = fileMngService.fileDownList(fileVO);
			}
			//처리유형
			String reqProType = (String) reqInfoMap.get("reqProType");
			
			//변경이력
			List<Map> reqChgList = req4100Service.selectReq4700ReqHistoryList(paramMap);
			model.addAttribute("reqChgList", reqChgList);
			
			List<Map> flowList = null;
			
			//프로세스 ID				
			String processId = (String)reqInfoMap.get("processId");
			
			//작업흐름 ID
			String flowId = (String)reqInfoMap.get("flowId");
			
			//프로세스 ID
			paramMap.put("processId", processId);
			model.addAttribute("processId", processId);
			
			//작업흐름 목록
			flowList = prj1100Service.selectFlw1100FlowList(paramMap);
			model.addAttribute("flowList", flowList);
			
			//추가 항목 목록
			paramMap.put("flowId", flowId);
			List<Map> optList = prj1100Service.selectFlw1200ReqOptList(paramMap);
			model.addAttribute("optList", optList);
			
			//추가 항목 목록에서 파일 목록 가져오기
			if(optList != null && optList.size() > 0){
				for(Map optInfo : optList){
					String itemCode = (String) optInfo.get("itemCode");
					
					//분류코드가 첨부파일인경우에만
					if(itemCode != null && "03".equals(itemCode)){
						String optAtchFileId = (String) optInfo.get("itemValue");
						if(!"".equals(optAtchFileId) && optAtchFileId != null){
							//atchFileId값을 넘기기 위한 FileVO
							FileVO fileVO = new FileVO();
							fileVO.setAtchFileId(optAtchFileId);
							
							//파일 리스트 조회
							List<FileVO> flowFileList = fileMngService.fileDownList(fileVO);
							fileList.addAll(flowFileList);
						}
					}
				}
			}

			//결재 목록
			List<Map> reqChkList = req4100Service.selectReq4900ReqSignList(paramMap);
			model.addAttribute("reqChkList", reqChkList);

			//역할그룹 목록
			List<Map> flowAuthGrpList = prj1100Service.selectFlw1500FlowAuthGrpList(paramMap);
			model.addAttribute("flowAuthGrpList", flowAuthGrpList);
			
			//배포 계획 목록
			List<Map> reqDplList = dpl1100Service.selectDpl1100ReqDplList(paramMap);
			model.addAttribute("reqDplList", reqDplList);
			
			//현재 요구사항 프로세스 정보
			Map processInfo = (Map)prj1100Service.selectFlw1000ProcessInfo(paramMap);
			
			
			model.addAttribute("processTypeCd",processInfo.get("processTypeCd"));
			model.addAttribute("fileList",fileList);
			model.addAttribute("processInfo",processInfo);
			model.addAttribute("reqInfoMap", reqInfoMap);
			model.addAttribute("reqProType", reqProType);
			model.addAttribute("errorYN", "N");
    		model.addAttribute("message", egovMessageSource.getMessage("success.common.select"));
			return new ModelAndView("jsonView");
    	}
    	catch(Exception ex){
    		Log.error("selectReq4105DataLoad()", ex);

    		//업로드 실패 메시지 세팅 및 저장 성공여부 세팅
    		model.addAttribute("errorYN", "Y");
    		model.addAttribute("message", egovMessageSource.getMessage("fail.common.select"));
    		return new ModelAndView("jsonView");
    	}
    }

	/**
	 * Req4106 데이터 불러오기
	 * @param 
	 * @return 
	 * @exception Exception
	 */
	@SuppressWarnings({ "rawtypes", "unchecked" })
	@RequestMapping(value="/req/req4000/req4100/selectReq4106DataLoad.do")
    public ModelAndView selectReq4106DataLoad(HttpServletRequest request, HttpServletResponse response, ModelMap model ) throws Exception {
    	try{
    		// request 파라미터를 map으로 변환
			Map<String, String> paramMap = RequestConvertor.requestParamToMapAddSelInfo(request, true);
			paramMap.put("prjId", (String)request.getSession().getAttribute("selPrjId"));
		
			//요구사항 단건 조회
			Map reqInfoMap = (Map) req4100Service.selectReq4102ReqInfoAjax(paramMap);
			
			//요구사항 파일목록 조회
			String atchFileId = (String)reqInfoMap.get("atchFileId");
			
			List<FileVO> fileList = null;
			if(!"".equals(atchFileId) || atchFileId != null){
				//atchFileId값을 넘기기 위한 FileVO
	        	FileVO fileVO = new FileVO();
	        	fileVO.setAtchFileId((String)reqInfoMap.get("atchFileId"));
	        	
	        	//파일 리스트 조회
				fileList = fileMngService.fileDownList(fileVO);
			}
			//처리유형
			String reqProType = (String) reqInfoMap.get("reqProType");

			//접수 페이지에서 reqProType이 01이 아닌경우 이미 접수 완료 처리된 요청사항
			if(reqProType != null && !"01".equals(reqProType)){
				model.addAttribute("errorYN", "Y");
	    		model.addAttribute("message", "이미 접수 처리된 요청사항입니다.");
				return new ModelAndView("jsonView");
			}
			
			
			List<Map> processList = prj1100Service.selectFlw1000ProcessList(paramMap);
			
			model.addAttribute("processList", processList);
			model.addAttribute("fileList",fileList);
			model.addAttribute("reqInfoMap", reqInfoMap);
			model.addAttribute("reqProType", reqProType);
			model.addAttribute("errorYN", "N");
    		model.addAttribute("message", egovMessageSource.getMessage("success.common.select"));
			return new ModelAndView("jsonView");
    	}
    	catch(Exception ex){
    		Log.error("selectReq4106DataLoad()", ex);

    		//업로드 실패 메시지 세팅 및 저장 성공여부 세팅
    		model.addAttribute("errorYN", "Y");
    		model.addAttribute("message", egovMessageSource.getMessage("fail.common.select"));
    		return new ModelAndView("jsonView");
    	}
    }
	
	/**
	 * Req4106 요구사항 접수 완료 AJAX
	 * @param 
	 * @return 
	 * @exception Exception
	 */
	@SuppressWarnings({ "rawtypes", "unchecked" })
	@RequestMapping(value="/req/req4000/req4100/insertReq4106NewReqAcceptInfoAjax.do")
	public ModelAndView insertReq4106NewReqAcceptInfoAjax(HttpServletRequest request, HttpServletResponse response, ModelMap model ) throws Exception {

		try{
			// request 파라미터를 map으로 변환
			Map<String, String> paramMap = RequestConvertor.requestParamToMapAddSelInfo(request, true);
			HttpSession ss = request.getSession();

			String prjId = (String) ss.getAttribute("selPrjId");
			paramMap.put("prjId", prjId);
			
			//로그인VO 가져오기
    		LoginVO loginVO = (LoginVO) ss.getAttribute("loginVO");
    		
    		paramMap.put("usrId", loginVO.getUsrId());
    		
    		//프로젝트 그룹 Id
        	String prjGrpId = (String)ss.getAttribute("selPrjGrpId");
        	paramMap.put("prjGrpId", prjGrpId);
        	
			List<Map> flowList = null;
			
			//작업흐름 목록
			flowList = prj1100Service.selectFlw1100FlowList(paramMap);
			
			//작업흐름 Id가져오기 (작업흐름이 0개 일 수 없음, 접수 반려인경우 flowId없음)
			if(flowList.size() > 0){
				Map flowInfo = (Map) flowList.get(0);
				
				//작업흐름 Id
				String flowId = (String) flowInfo.get("flowId");
				paramMap.put("flowId", flowId);
			}
			
			//접수 처리
			req4100Service.updateReq4106NewReqAccpetInfoAjax(paramMap);
			
			//등록성공값 세팅
			model.addAttribute("errorYN", "N");
			
			//등록 성공 메시지 세팅
			model.addAttribute("message", egovMessageSource.getMessage("success.request.accept"));
			
			return new ModelAndView("jsonView");
		}
		catch(UserDefineException ude) {
			Log.error("insertReq4106NewReqAcceptInfoAjax()", ude);

			//등록실패 메시지 세팅 및 저장 성공여부 세팅
			model.addAttribute("errorYN", "Y");
			model.addAttribute("message", ude.getMessage());
			return new ModelAndView("jsonView");
		}
		catch(Exception ex){
			Log.error("insertReq4106NewReqAcceptInfoAjax()", ex);

			//등록실패 메시지 세팅 및 저장 성공여부 세팅
			model.addAttribute("errorYN", "Y");
			model.addAttribute("message", egovMessageSource.getMessage("fail.request.accept"));
			return new ModelAndView("jsonView");
		}
	}
	
	/**
	 * Req4100 요구사항 작업흐름 변경
	 * @param 
	 * @return 
	 * @exception Exception
	 */
	@SuppressWarnings("rawtypes")
	@RequestMapping(value="/req/req4000/req4100/saveReq4100ReqFlowChgAjax.do")
	public ModelAndView saveReq4100ReqFlowChgAjax(HttpServletRequest request, HttpServletResponse response, ModelMap model ) throws Exception {

		try{
			MultipartHttpServletRequest mptRequest = (MultipartHttpServletRequest)request;
			
			// request 파라미터를 map으로 변환
			Map<String, String> paramMap = RequestConvertor.requestParamToMapAddSelInfo(request, true);
			paramMap.put("prjId", (String)paramMap.get("selPrjId"));
			
			Map<String, Object> paramFiles = new HashMap<String, Object>();
			List<FileVO> _result = new ArrayList<FileVO>();
			
			//세션
			HttpSession ss = request.getSession();
			//로그인VO 가져오기
    		LoginVO loginVO = (LoginVO) ss.getAttribute("loginVO");
    		
    		//프로젝트 그룹 Id
        	String prjGrpId = (String)ss.getAttribute("selPrjGrpId");
        	paramMap.put("prjGrpId", prjGrpId);
        	
    		//로그인 사용자 Id
    		String usrId = loginVO.getUsrId();
    		
    		//현재 요구사항 정보 불러오기
    		Map reqInfo = req4100Service.selectReq4102ReqInfoAjax(paramMap);
			
    		//요구사항 담당자
    		String reqChargerId = (String)reqInfo.get("reqChargerId");
    		
    		//담당자 다른경우 중지
    		if(!reqChargerId.equals(usrId)){
    			//조회실패 메시지 세팅 및 저장 성공여부 세팅
				model.addAttribute("errorYn", "Y");
				model.addAttribute("message", "담당자가 아닌경우 처리가 불가능합니다.");
				return new ModelAndView("jsonView");
    		}
    		
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

			String prjId = (String) ss.getAttribute("selPrjId");
			paramMap.put("prjId", prjId);
			paramFiles.put("fileList", _result);
			paramFiles.putAll(paramMap);

			//첨부파일 추가로 전달
			paramFiles.put("fileActionType", "add");

			//요구사항 작업흐름 변경 시 통합 저장
			boolean rtnVal = req4100Service.saveReq4100ReqFlowChgAjax(paramFiles);
			
			//저장중 오류 발생 시
			if(!rtnVal){
				//조회실패 메시지 세팅 및 저장 성공여부 세팅
				model.addAttribute("errorYn", "Y");
				model.addAttribute("message", egovMessageSource.getMessage("fail.common.save"));
				return new ModelAndView("jsonView");
			}
			
			//등록 성공 메시지 세팅
			model.addAttribute("errorYn", "N");
			model.addAttribute("message", egovMessageSource.getMessage("success.common.save"));

			return new ModelAndView("jsonView");
		}
		catch(Exception ex){
			Log.error("saveReq4100ReqFlowChgAjax()", ex);

			//조회실패 메시지 세팅 및 저장 성공여부 세팅
			model.addAttribute("errorYn", "Y");
			model.addAttribute("message", egovMessageSource.getMessage("fail.common.save"));
			return new ModelAndView("jsonView");
		}
	}
	

	/**
	 * 선택된 요구사항의 세부정보를 가져온다.
	 * @desc 1. 세션의 프로젝트 ID, 라이센스 그룹 ID를 가져온다.
	 *       2. Map 에 1번 을 셋팅하고 쿼리를 호출한다.
	 * @param request
	 * @param response
	 * @throws Exception
	 */
	@SuppressWarnings({ "rawtypes" })
	@RequestMapping(value="/req/req4000/req4100/selectReq4100ReqInfoAjax.do")
	public ModelAndView selectReq4100ReqInfoAjax(HttpServletRequest request, HttpServletResponse response, ModelMap model )	throws Exception {

		try{
			// request 파라미터를 map으로 변환
			Map<String, String> paramMap = RequestConvertor.requestParamToMapAddSelInfo(request, true);

			HttpSession ss = request.getSession();
			String prjId	= (String) ss.getAttribute("selPrjId");
			String licGrpId = ((LoginVO) ss.getAttribute("loginVO")).getLicGrpId();

			paramMap.put("prjId", prjId);
			paramMap.put("licGrpId", licGrpId);

			// 요구사항 단건정보 조회
			//Map reqInfoMap = (Map) req4100Service.selectReq4100ReqInfo(paramMap);  
			Map reqInfoMap = (Map) req4100Service.selectReq4102ReqInfoAjax(paramMap);
			
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

			//요구사항 코멘트 목록 조회
			//List reqCommentList = (List) req2000Service.selectReq2000ReqCommentListAjax(paramMap);

			//model.addAttribute("reqCommentList",reqCommentList);
			//조회 성공 메시지 세팅
			model.addAttribute("message", egovMessageSource.getMessage("success.common.select"));

			return new ModelAndView("jsonView");

		}catch(Exception ex){
			Log.error("selectReq4100ReqInfoAjax()", ex);
			throw new Exception(ex.getMessage());
		}
	}
	
	
	/**
	 * 요구사항 엑셀 업로드시 요청자 ID가 존재하는지 체크
	 * @param request
	 * @param model
	 * @return
	 * @throws Exception
	 */
	@SuppressWarnings({ "rawtypes", "unchecked" })
	@RequestMapping(value="/req/req4000/req4100/selectReq4100ReqUsrChk.do")
	public ModelAndView selectReq4100ReqUsrChk(HttpServletRequest request, HttpServletResponse response, ModelMap model )	throws Exception {

		// 값을 넘기기 위한 HasMap
		HashMap<String, String> chkMap = new HashMap<String, String>();
		try{

			// request 파라미터를 map으로 변환
			Map param = RequestConvertor.requestParamToMap(request, true);

			// 사용자 입력 정보 할당
			String reqUsrInId = (String)param.get("InReqUsrId");

			// 아이디 입력값이 없을 경우 retrun
			if( "".equals(EgovStringUtil.isNullToString(reqUsrInId)) ) {
				
				chkMap.put("idChk", "N");
				return new ModelAndView("jsonView", chkMap);
			}
			

			//요청자 ID 정보 맵핑
			param.put("reqUsrId", reqUsrInId);

			int idChkCnt  = 1;
			
			// DB 요청자 정보 조회
			idChkCnt = req4100Service.selectReq4100ReqUsrChk(param);
			
			// 요청자 정보 조회 결과 확인
			if( idChkCnt == 0) {
				// 요청자 정보 DB 조회 결과 없음
				chkMap.put("chkId", "N");
			}else{
				chkMap.put("chkId", "Y");
			}

			//조회 성공 메시지 세팅
			model.addAttribute("errorYn", "N");
			model.addAttribute("message", egovMessageSource.getMessage("success.common.select"));
			
			return new ModelAndView("jsonView", chkMap);

		}catch(Exception e){
			Log.error("selectReq4100ReqUsrChk()", e);
			model.addAttribute("errorYn", "Y");
			model.addAttribute("message", egovMessageSource.getMessage("fail.common.save"));
			return new ModelAndView("jsonView");
		}
	}
	
	
	/**
	 * 
	 * 요구사항에 반영된 SVN 리비젼 정보를 조회
	 * 
	 * @param request
	 * @param response
	 * @param model
	 * @return
	 * @throws Exception
	 */
	@SuppressWarnings("rawtypes")
	@RequestMapping(value="/req/req4000/req4100/selectReq4100RevisionList.do")
	public ModelAndView selectReq4100RevisionList(@ModelAttribute("req4100VO") Req4100VO req4100VO , HttpServletRequest request, HttpServletResponse response, ModelMap model )	throws Exception {

		try{
			Map<String, String> paramMap = RequestConvertor.requestParamToMapAddSelInfo(request, true);

			HttpSession ss = request.getSession();
			String licGrpId = ((LoginVO) ss.getAttribute("loginVO")).getLicGrpId();

			// 라이선스 그룹 ID 세팅
			req4100VO.setLicGrpId(licGrpId);
			
			String  prjId = "";
			// 넘어온 프로젝트 Id 있을경우 세팅
			if(paramMap.get("popupPrjId") != null){
				prjId = paramMap.get("popupPrjId");
			}
			else if(paramMap.get("prjId") != null){
				prjId = paramMap.get("prjId");
			}
			// 넘어온 프로젝트 Id 없을경우
			else{
				// 세션의 선택된 프로젝트 아이디 가져오기
				prjId = (String) ss.getAttribute("selPrjId");
			}
			
			// 프로젝트 ID 세팅
			req4100VO.setPrjId(prjId);
	
			//현재 페이지 값, 보여지는 개체 수
			String _pageNo_str = paramMap.get("pageNo");
			String _pageSize_str = paramMap.get("pageSize");

			int _pageNo = 1;
			int _pageSize = 15;//OslAgileConstant.pageSize;
						
			// 넘어온 페이지 정보가 있다면 해당 값으로 세팅
			if(_pageNo_str != null && !"".equals(_pageNo_str)){
				_pageNo = Integer.parseInt(_pageNo_str)+1;  
			}
			if(_pageSize_str != null && !"".equals(_pageSize_str)){
				_pageSize = Integer.parseInt(_pageSize_str);  
			}
			
			// 페이지 번호, 페이지 사이즈 세팅
			req4100VO.setPageIndex(_pageNo);
			req4100VO.setPageSize(_pageSize);
			req4100VO.setPageUnit(_pageSize);
			
			PaginationInfo paginationInfo = PagingUtil.getPaginationInfo(req4100VO);  /** paging - 신규방식 */
			
			// 총 데이터 건수
			int totCnt = req4100Service.selectReq4100RevisionListCnt(req4100VO);
			
			// 총 데이터 건수 세팅
			paginationInfo.setTotalRecordCount(totCnt);
			
			// 요구사항에 포함된 리비전 목록 조회
			List<Map> list = req4100Service.selectReq4100RevisionList(req4100VO);
			model.addAttribute("list", list);
			
			//페이지 정보 보내기
			Map<String, Integer> pageMap = new HashMap<String, Integer>();
			pageMap.put("pageNo",req4100VO.getPageIndex());
			pageMap.put("listCount", list.size());
			pageMap.put("totalPages", paginationInfo.getTotalPageCount());
			pageMap.put("totalElements", totCnt);
			pageMap.put("pageSize", _pageSize);
			
			// 페이지 정보 세팅
			model.addAttribute("page", pageMap);
			
			//조회 성공 메시지 세팅
			model.addAttribute("errorYn", "N");
			model.addAttribute("message", egovMessageSource.getMessage("success.common.select"));
			
			return new ModelAndView("jsonView");

		}catch(Exception e){
			Log.error("selectReq4100ReqRevisionList()", e);
			model.addAttribute("errorYn", "Y");
			model.addAttribute("message", egovMessageSource.getMessage("fail.common.save"));
			return new ModelAndView("jsonView");
		}
	}
	

	/**
	 * req1001 요구사항 목록 (자동완성) AJAX
	 * -권한있는 프로젝트 내의 요구사항 목록 가져오기(전체)
	 * @param 
	 * @return 
	 * @exception Exception
	 */
	@SuppressWarnings({"rawtypes" })
	@RequestMapping(value="/req/req1000/req1000/selectReq1001AutoCompleReqAjax.do")
	public ModelAndView selectReq1001AutoCompleReqAjax(HttpServletRequest request, HttpServletResponse response, ModelMap model ) throws Exception {

		try{
			// request 파라미터를 map으로 변환
			Map<String, String> paramMap = RequestConvertor.requestParamToMapAddSelInfo(request, true);
			
			HttpSession ss = request.getSession();
			LoginVO loginVO = (LoginVO) ss.getAttribute("loginVO");
			paramMap.put("usrId", loginVO.getUsrId());
			
			List<Map> reqList = req4100Service.selectReq4100PrjAuthReqList(paramMap);
			model.addAttribute("reqList", reqList);
			
			//등록 성공 결과 값
			model.addAttribute("errorYn", "N");
			model.addAttribute("message", egovMessageSource.getMessage("success.common.select"));
			return new ModelAndView("jsonView");
		}
		catch(Exception ex){
			Log.error("selectReq1001AutoCompleReqAjax()", ex);
			//등록 실패 결과 값
			model.addAttribute("errorYn", "Y");
			model.addAttribute("message", egovMessageSource.getMessage("fail.common.select"));
			return new ModelAndView("jsonView");
		}
	}
	

	/**
	 * req4105 요구사항 담당자 변경  AJAX
	 * @param 
	 * @return 
	 * @exception Exception
	 */
	@RequestMapping(value="/req/req4000/req4000/updateReq4105ReqChargerChgAjax.do")
	public ModelAndView updateReq4105ReqChargerChgAjax(HttpServletRequest request, HttpServletResponse response, ModelMap model ) throws Exception {

		try{
			// request 파라미터를 map으로 변환
			Map<String, String> paramMap = RequestConvertor.requestParamToMapAddSelInfo(request, true);
			HttpSession ss = request.getSession();
			String prjId	= (String) ss.getAttribute("selPrjId");
			String prjGrpId	= (String) ss.getAttribute("selPrjGrpId");
			LoginVO loginVO = (LoginVO) ss.getAttribute("loginVO");
			paramMap.put("usrId", loginVO.getUsrId());
			paramMap.put("prjId", prjId);
			paramMap.put("prjGrpId", prjGrpId);
			
			req4100Service.updateReq4100ReqChargerChgInfoAjax(paramMap);
			
			//등록 성공 결과 값
			model.addAttribute("errorYn", "N");
			model.addAttribute("message", egovMessageSource.getMessage("success.common.update"));
			return new ModelAndView("jsonView");
		}
		catch(Exception ex){
			Log.error("updateReq4105ReqChargerChgAjax()", ex);
			//등록 실패 결과 값
			model.addAttribute("errorYn", "Y");
			model.addAttribute("message", egovMessageSource.getMessage("fail.common.update"));
			return new ModelAndView("jsonView");
		}
	}
	
	/**
	 * REQ4100 요구사항 엑셀 다운로드
	 * @param 
	 * @return 
	 * @exception Exception
	 */
	@SuppressWarnings({ "rawtypes", "unchecked" })
	@RequestMapping(value="/req/req4000/req4100/selectReq4100ExcelList.do")
	public ModelAndView selectReq4100ExcelList(HttpServletRequest request, HttpServletResponse response, ModelMap model ) throws Exception {
		//리퀘스트에서 넘어온 파라미터를 맵으로 세팅
		Map paramMap = RequestConvertor.requestParamToMapAddSelInfo(request, true);
		
		/* 사용법
		 * 
		 * 1. String strReqOrd = egovMessageSource.getMessage("excel.reqOrd"); 
		 *     이렇게 message-common_ko.properties에 선언해둔 헤더명을 불러와 문자열로 만든다.
		 * 2. SheetHeader header = new SheetHeader(new String[]{ 여기에 문자열 추가 }
		 *     new SheetHeader(new String[] 부분에 1에서 만든 문자열을 추가한다.
		 * 3. List<Metadata> metadatas = new ArrayList<Metadata>(); 
		 *    metadatas.add(new Metadata("reqOrd"));
		 *     metadatas List에 new Metadata로 엑셀로 출력할 항목들을 선언한다.
		 * 4. BigDataSheetWriter writer = new BigDataSheetWriter(엑셀 시트명, 파일경로, 엑셀 시트명, metadatas) 선언    
		 * 5. ExcelDataListResultHandler  resultHandler = new ExcelDataListResultHandler(writer.getXMLSheetWriter(), writer.getStyleMap(), header, metadatas) 선언
		 * 6. 서비스에 엑셀 다운로드 메서드를 호출한다.
		 * 
		 * */
		
		// 전체 요구사항 현황 검색어
		String searchSelect = request.getParameter("searchSelect");
		String searchCd = request.getParameter("searchCd");
		String searchTxt = request.getParameter("searchTxt");
		String srchFromDt = request.getParameter("srchFromDt");
		String srchToDt = request.getParameter("srchToDt");

		// 엑셀 다운로드 양식의 헤더명 선언
		// 추가할 것이 있을 경우 주석 해제하고 헤더에 추가하면 됨
		String strPrjId 		= egovMessageSource.getMessage("excel.prjId");			// 프로젝트 ID
		String strPrjNm 		= egovMessageSource.getMessage("excel.prjNm");			// 프로젝트 명
		String strReqOrd 		= egovMessageSource.getMessage("excel.reqOrd");			// 요구사항 순번
		String strReqId 		= egovMessageSource.getMessage("excel.reqId");			// 요구사항ID
		//String strReqClsId 		= egovMessageSource.getMessage("excel.reqClsId");	// 요구사항 분류 ID
		String strReqProTypeNm 	= egovMessageSource.getMessage("excel.reqProTypeNm");	// 처리유형
		String strReqNewTypeNm 	= egovMessageSource.getMessage("excel.reqNewTypeNm");	// 접수유형
		String strReqNo 		= egovMessageSource.getMessage("excel.reqNo");			// 공문번호
		String strReqNm 		= egovMessageSource.getMessage("excel.reqNm");			// 요구사항 명
		String strReqDesc 		= egovMessageSource.getMessage("excel.reqDesc");		// 요구사항 설명
		//String strReqUsrId 		= egovMessageSource.getMessage("excel.reqUsrId");	// 요청자 ID
		String strReqUsrNm 		= egovMessageSource.getMessage("excel.reqUsrNm");		// 요청자 명
		String strReqDtm 		= egovMessageSource.getMessage("excel.reqDtm");			// 요청일
		String strReUsrDeptNm 	= egovMessageSource.getMessage("excel.reqUsrDeptNm");	// 요청자 소속
		String strReqUsrEmail 	= egovMessageSource.getMessage("excel.reqUsrEmail");	// 요청자 이메일
		String strReqUsrNum 	= egovMessageSource.getMessage("excel.reqUsrNum");		// 요청자 연락처
		//String strReqChargerId 	= egovMessageSource.getMessage("excel.reqChargerId");	// 담당자 ID
		String strReqChargerNm 	= egovMessageSource.getMessage("excel.reqChargerNm");	// 담당자 명
		
		String strProcessNm		= egovMessageSource.getMessage("excel.processNm");		// 프로세스 명
		String strFlowNm		= egovMessageSource.getMessage("excel.flowNm");			// 작업흐름 명
		String strReqCompleteRatio = egovMessageSource.getMessage("excel.reqCompleteRatio"); // 요구사항 진척률
		String strReqFp 		= egovMessageSource.getMessage("excel.reqFp");			// 요구사항 FP
		String strReqExFp		= egovMessageSource.getMessage("excel.reqExFp");		// 요구사항 예상 FP
		String strReqStDtm 		= egovMessageSource.getMessage("excel.reqStDtm");		// 작업시작일자
		String strReqEdDtm 		= egovMessageSource.getMessage("excel.reqEdDtm");		// 작업종료일자
		String strReqStDuDtm	= egovMessageSource.getMessage("excel.reqStDuDtm");		// 작업시작 예정일자
		String strReqEdDuDtm 	= egovMessageSource.getMessage("excel.reqEdDuDtm");		// 작업종료 예정일자
		//String strReqAcceptTxt 	= egovMessageSource.getMessage("excel.reqAcceptTxt");	// 접수의견
		String strReqTypeNm		= egovMessageSource.getMessage("excel.reqTypeNm");		// 요구사항 유형
		String strSclNm 		= egovMessageSource.getMessage("excel.sclNm");			// 시스템 구분
		String strPiaNm 		= egovMessageSource.getMessage("excel.piaNm");			// 성능 개선활동 여부
		String strLabInp 		= egovMessageSource.getMessage("excel.labInp");			// 투입인력
		String strOrgReqId 		= egovMessageSource.getMessage("excel.orgReqId");		// 각 체계별 요구사항 ID
		String strRegDtm 		= egovMessageSource.getMessage("excel.regDtm");			// 최초등록일시
		String strRegUsrId 		= egovMessageSource.getMessage("excel.regUsrId");		// 최초등록자 ID
		String strRegUsrIp		= egovMessageSource.getMessage("excel.regUsrIp");		// 최초등록자 IP
		String strModifyDtm 	= egovMessageSource.getMessage("excel.modifyDtm");		// 최종수정일
		String strModifyUsrId 	= egovMessageSource.getMessage("excel.modifyUsrId");	// 최종수정자 ID
		String strModifyUsrIp 	= egovMessageSource.getMessage("excel.modifyUsrIp");	// 최종수정자 IP
		
		
		SheetHeader header = new SheetHeader(new String[]{strPrjId, strPrjNm, strReqOrd, strReqId, strReqProTypeNm, strReqNewTypeNm
														,strReqNo ,strReqNm ,strReqDesc ,strReqDtm , strReqUsrNm, strReUsrDeptNm, strReqUsrEmail 
														,strReqUsrNum ,strReqChargerNm  ,strProcessNm	,strFlowNm ,strReqCompleteRatio
														,strReqFp ,strReqExFp ,strReqStDtm ,strReqEdDtm ,strReqStDuDtm
														,strReqEdDuDtm ,strReqTypeNm ,strSclNm ,strPiaNm ,strLabInp
														,strOrgReqId  ,strRegDtm ,strRegUsrId ,strRegUsrIp ,strModifyDtm 
														,strModifyUsrId ,strModifyUsrIp});
		
		/* 조회되는 데이터와 포멧 지정 
		 * ex 1. 수치형 new Metadata("property", XSSFCellStyle.ALIGN_RIGHT, "#,##0")
		 * ex 2. 사용자 지정 날짜 변환 new Metadata("property", ,"00-00-00") YYMMDD -> YY-MM-DD
		 * */
		List<Metadata> metadatas = new ArrayList<Metadata>(); 
		metadatas.add(new Metadata("prjId"));
		metadatas.add(new Metadata("prjNm"));
		metadatas.add(new Metadata("reqOrd"));
		metadatas.add(new Metadata("reqId"));
		metadatas.add(new Metadata("reqProTypeNm"));
		metadatas.add(new Metadata("reqNewTypeNm"));
		metadatas.add(new Metadata("reqNo"));
		metadatas.add(new Metadata("reqNm"));
		metadatas.add(new Metadata("reqDesc"));
		metadatas.add(new Metadata("reqDtm", "00-00-00"));
		metadatas.add(new Metadata("reqUsrNm"));
		metadatas.add(new Metadata("reqUsrDeptNm"));
		metadatas.add(new Metadata("reqUsrEmail"));
		metadatas.add(new Metadata("reqUsrNum"));
		metadatas.add(new Metadata("reqChargerNm"));
		metadatas.add(new Metadata("processNm"));
		metadatas.add(new Metadata("flowNm"));
		metadatas.add(new Metadata("reqCompleteRatio"));
		metadatas.add(new Metadata("reqFp"));
		metadatas.add(new Metadata("reqExFp"));
		metadatas.add(new Metadata("reqStDtm"));
		metadatas.add(new Metadata("reqEdDtm"));
		metadatas.add(new Metadata("reqStDuDtm", "00-00-00"));
		metadatas.add(new Metadata("reqEdDuDtm", "00-00-00"));
		metadatas.add(new Metadata("reqTypeNm"));
		metadatas.add(new Metadata("sclNm"));
		metadatas.add(new Metadata("piaNm"));
		metadatas.add(new Metadata("labInp"));
		metadatas.add(new Metadata("orgReqId"));
		metadatas.add(new Metadata("regDtm"));
		metadatas.add(new Metadata("regUsrId"));
		metadatas.add(new Metadata("regUsrIp"));
		metadatas.add(new Metadata("modifyDtm"));
		metadatas.add(new Metadata("modifyUsrId"));
		metadatas.add(new Metadata("modifyUsrIp"));

		BigDataSheetWriter writer = new BigDataSheetWriter(egovMessageSource.getMessage("excel.req4100.sheetNm"), tempPath, egovMessageSource.getMessage("excel.req4100.sheetNm"), metadatas);

		writer.beginSheet();

		try{
			Map searchParamMap = new HashMap<String, String>();
			searchParamMap.put("licGrpId", paramMap.get("licGrpId"));
			searchParamMap.put("prjId", paramMap.get("selPrjId"));
			searchParamMap.put("searchSelect", searchSelect);
			searchParamMap.put("searchCd", searchCd);
			searchParamMap.put("searchTxt", searchTxt);
			searchParamMap.put("srchFromDt", srchFromDt);
			searchParamMap.put("srchToDt", srchToDt);
				
			ExcelDataListResultHandler  resultHandler = new ExcelDataListResultHandler(writer.getXMLSheetWriter(), writer.getStyleMap(), header, metadatas);
			req4100Service.selectReq4100ExcelList(searchParamMap,resultHandler);
		}
		catch(Exception ex){
			Log.error("selectReq4100ExcelList()", ex);
			throw new Exception(ex.getMessage());
		}finally{
			writer.endSheet();
		}

		return writer.getModelAndView();
	}
	
	/**
	 * req4110 화면 호출
	 * 외부에서 요구사항 상세보기 화면을 호출한다.
	 * 요구사항 키 암호화시 / 가 포함되므로 RequestMapping URL을 data/req/{reqkey} 에서 /data/req/**로 변경
	 * request.getRequestURL()을 이용하여 url을 가져와 요구사항 키값을 추출한다.
	 * @param 
	 * @return 
	 * @exception Exception
	 */
	@SuppressWarnings({"rawtypes" , "unchecked"})
	@RequestMapping(value="/data/req/**")
	public String selectReq4110View(HttpServletRequest request, HttpServletResponse response, ModelMap model ) throws Exception {
    	
    	try{
    		// 요구사항 키가 포함된 URL을 가져온다.
    		String requestURL = request.getRequestURL().toString();
    		// 요구사항 키를 추출한다.
    		String[] reqKey = requestURL.split("/data/req/");
    		
    		Map<String,String> paramMap = new HashMap<String,String>();
    		// 추출한 키를 map에 세팅
    		paramMap.put("reqKey", reqKey[1]);
		
			// 요구사항 키를 이용하여 요구사항 단건정보 조회
			Map reqInfoMap = (Map) req4100Service.selectReq4110ReqInfo(paramMap);

			if(reqInfoMap != null){
				// 라이선스 그룹 ID
				String licGrpId = (String)reqInfoMap.get("licGrpId");
				// 프로젝트 ID
				String prjId = (String)reqInfoMap.get("prjId");
				//요구사항 프로세스 Id
				String processId = (String) reqInfoMap.get("processId");
				// 요구사항 ID
				String reqId = (String) reqInfoMap.get("reqId");
				
				paramMap.put("licGrpId",licGrpId);
				paramMap.put("prjId",prjId);
				paramMap.put("processId",processId);
				paramMap.put("reqId",reqId);
			}else{
				//요구사항이 없는 경우 에러
				model.addAttribute("reqChk","N");
				model.addAttribute("reqInfoMap", "{}");
				model.addAttribute("reqChgList", "[]");
				model.addAttribute("exceptionMessage",egovMessageSource.getMessage("req4100.notFoundReq"));

				return "/req/req4000/req4100/req4110";
			}
			/********************************
			 * 요구사항 변경 이력 데이터 불러오기
			 * - 요구사항 변경 이력 조회
			 * - 요구사항 검수 승인&거부 이력 조회
			 * - 조회된 리스트 데이터를 하나의 리스트로 병합
			 * - toJson으로 전달
			 ********************************/
			
			//변경이력 전체 가져오기(담당자 포함)
			paramMap.put("chgAllView", "Y");
			//변경이력
			List<Map> reqChgList = req4100Service.selectReq4700ReqHistoryList(paramMap);
			//결재 정보
			List<Map> reqChkList = req4100Service.selectReq4900ReqSignList(paramMap);
			
			//검수 정보 데이터를 변경 이력 데이터에 추가 (시간 비교해서 추가)
			int chkIndex = 0;
			Date chgDtm;
			Date regDtm;
			Map reqChkInfo;

			if(reqChkList != null && reqChkList.size() > 0){
				//for(Map reqInfo : reqChgList){
				for(int i=0;i<reqChgList.size();i++){
					Map reqInfo = reqChgList.get(i);
					//chkIndex값이 검수 데이터 수보다 큰 경우 루프 종료
					if(chkIndex >= reqChkList.size()){
						break;
					}
					
					//변경 이력 일시
					chgDtm = new Date(((Timestamp)reqInfo.get("chgDtm")).getTime());
					//검수정보 Map 가져오기
					reqChkInfo = reqChkList.get(chkIndex);
					
					//검수 데이터 등록 일시
					regDtm = new Date(((Timestamp)reqChkInfo.get("signDtm")).getTime());
					
					//chgDtm이 regDtm보다 지난 날짜인 경우
					if(chgDtm.compareTo(regDtm) >= 0){
						reqChgList.add(i,reqChkInfo);
						chkIndex++;
					}
				}
				//추가되지 않은 검수 데이터 있는 경우 모두 추가
				for(int i=chkIndex;i<reqChkList.size();i++){
					Map reqInfo = reqChkList.get(i);
					reqChgList.add(reqInfo);
				}
			}

			String reqInfoMapJson 	= (new GsonBuilder().serializeNulls().create()).toJson(reqInfoMap).toString();
        	String reqChgListJson	= (new GsonBuilder().serializeNulls().create()).toJsonTree(reqChgList).toString();
        	
        	model.addAttribute("reqInfoMap", reqInfoMapJson);
			model.addAttribute("reqChgList", reqChgListJson);
		
        	return "/req/req4000/req4100/req4110";
    	}
    	catch(Exception ex){
    		Log.error("selectReq4110View()", ex);
    		throw new Exception(ex.getMessage());
    	}
	}
		
}
