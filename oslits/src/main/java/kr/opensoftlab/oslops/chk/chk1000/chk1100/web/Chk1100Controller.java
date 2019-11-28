package kr.opensoftlab.oslops.chk.chk1000.chk1100.web;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import kr.opensoftlab.oslops.chk.chk1000.chk1100.service.Chk1100Service;
import kr.opensoftlab.oslops.chk.chk1000.chk1100.vo.Chk1100VO;
import kr.opensoftlab.oslops.com.fms.web.service.FileMngService;
import kr.opensoftlab.oslops.com.vo.LicVO;
import kr.opensoftlab.oslops.com.vo.LoginVO;
import kr.opensoftlab.oslops.prj.prj1000.prj1100.service.Prj1100Service;
import kr.opensoftlab.oslops.req.req4000.req4100.service.Req4100Service;
import kr.opensoftlab.sdf.excel.BigDataSheetWriter;
import kr.opensoftlab.sdf.excel.ExcelDataListResultHandler;
import kr.opensoftlab.sdf.excel.Metadata;
import kr.opensoftlab.sdf.excel.SheetHeader;
import kr.opensoftlab.sdf.util.OslAgileConstant;
import kr.opensoftlab.sdf.util.PagingUtil;
import kr.opensoftlab.sdf.util.ProjectOptionInfoUtil;
import kr.opensoftlab.sdf.util.ReqHistoryMngUtil;
import kr.opensoftlab.sdf.util.RequestConvertor;

import org.apache.commons.lang.builder.ToStringBuilder;
import org.apache.commons.lang.builder.ToStringStyle;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;

import com.google.gson.GsonBuilder;

import egovframework.com.cmm.EgovMessageSource;
import egovframework.com.cmm.service.EgovProperties;
import egovframework.com.cmm.service.FileVO;
import egovframework.rte.fdl.property.EgovPropertyService;
import egovframework.rte.ptl.mvc.tags.ui.pagination.PaginationInfo;

@Controller
public class Chk1100Controller {
	
	/**
     * Logging 을 위한 선언
     * Log는 반드시 가이드에 맞춰서 작성한다. 개발용으로는 debug 카테고리를 사용한다.
     */
	protected Logger Log = Logger.getLogger(this.getClass());
	
	/** System Property 를 사용하기 위한 Bean Injection */
	@Resource(name = "propertiesService")
	protected EgovPropertyService propertiesService;
	
	/** EgovMessageSource */
	@Resource(name = "egovMessageSource")
	EgovMessageSource egovMessageSource;
	
	/** Chk1000Service DI */
    @Resource(name = "chk1100Service")
    private Chk1100Service chk1100Service;
    
    /** Prj1100Service DI */
    @Resource(name = "prj1100Service")
    private Prj1100Service prj1100Service;

    /** Req4100Service DI */
	@Resource(name = "req4100Service")
	private Req4100Service req4100Service;
	
    /** FileMngService */
   	@Resource(name="fileMngService")
   	private FileMngService fileMngService;
   	

	@Value("${Globals.fileStorePath}")
	private String tempPath;
	

	@Resource(name = "historyMng")
	private ReqHistoryMngUtil historyMng;
	
   	
	/**
     * Chk1100 화면로딩
     * @desc  	1. 화면로딩시 최초 정보를 조회한다.
     * @param 	request
     * @param 	response
     * @throws 	Exception
     */
	@SuppressWarnings({ "rawtypes", "unchecked" })
	@RequestMapping(value="/chk/chk1000/chk1100/selectChk1100View.do")
	public String selectChk1100View(@ModelAttribute("chk1100VO") Chk1100VO chk1100VO, HttpServletRequest request, HttpServletResponse response, ModelMap model )	throws Exception {
		// request 파라미터를 map으로 변환
		Map<String, String> paramMap = RequestConvertor.requestParamToMapAddSelInfo(request, true);
		
		//세션에서 프로젝트 아이디값 가져오기
		HttpSession ss = request.getSession();
		paramMap.put("prjId", (String) ss.getAttribute("selPrjId"));

		// 세션에서 해당 사용자의 라이선스 그룹 아이디 가져오기
		LicVO licVo = (LicVO)ss.getAttribute("licVO");
		paramMap.put("licGrpId", licVo.getLicGrpId());
		return "/chk/chk1000/chk1100/chk1100";
	}
    /**
     * Chk1100 요청 검수 목록 AJAX 조회
     * @param 	request
     * @param 	response
     * @throws 	Exception
     */
	@SuppressWarnings({ "rawtypes", "unchecked" })
	@RequestMapping(value="/chk/chk1000/chk1100/selectChk1100AjaxView.do")
	public ModelAndView selectChk1100AjaxView(@ModelAttribute("chk1100VO") Chk1100VO chk1100VO, HttpServletRequest request, HttpServletResponse response, ModelMap model )	throws Exception {
    	
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
			chk1100VO.setPageIndex(_pageNo);
			chk1100VO.setPageSize(_pageSize);
			chk1100VO.setPageUnit(_pageSize);
    					
    		PaginationInfo paginationInfo = PagingUtil.getPaginationInfo(chk1100VO); /** paging - 신규방식 */
    		
    		List<Chk1100VO> chk1100List = null;
			
			HttpSession ss = request.getSession();
			String prjId	= (String) ss.getAttribute("selPrjId");
			String licGrpId = ((LoginVO) ss.getAttribute("loginVO")).getLicGrpId();
			
			chk1100VO.setPrjId(prjId);
			chk1100VO.setLicGrpId(licGrpId);
			
			int totCnt = 0;
			// 결재목록
		    chk1100List = chk1100Service.selectChk1100List(chk1100VO);
		
		    /** 총 데이터의 건수 를 가져온다. */
		    totCnt = chk1100Service.selectChk1100ListCnt(chk1100VO);
		    paginationInfo.setTotalRecordCount(totCnt);

			//페이지 정보 보내기
			Map<String, Integer> pageMap = new HashMap<String, Integer>();
			pageMap.put("pageNo",chk1100VO.getPageIndex());
			pageMap.put("listCount", chk1100List.size());
			pageMap.put("totalPages", paginationInfo.getTotalPageCount());
			pageMap.put("totalElements", totCnt);
			pageMap.put("pageSize", _pageSize);

			model.addAttribute("page", pageMap);
			//조회성공 메시지 세팅
    		model.addAttribute("errorYn", "N");
			model.addAttribute("list", chk1100List); 			/** 조회 목록 List 형태로 화면에 Return 한다. */
			return new ModelAndView("jsonView");
        	
    	}catch(Exception ex){
    		Log.error("selectChk1100AjaxView()", ex);

			//조회실패 메시지 세팅
    		model.addAttribute("errorYn", "Y");
			model.addAttribute("message", egovMessageSource.getMessage("fail.common.select"));
			return new ModelAndView("jsonView");
    	}
    }

	/**
	  * 선택된 요구사항 다음 다음 작업흐름 ID를 가진 작업흐름 정보를 가져온다
	  * @param request
	  * @param response
	  * @throws Exception
	  */
	@SuppressWarnings("rawtypes")
	@RequestMapping(value="/chk/chk1000/chk1100/selectChk1100FlowNextNextIdInfoAjax.do")
	public ModelAndView selectChk1100FlowNextNextIdInfoAjax(HttpServletRequest request, HttpServletResponse response, ModelMap model )	throws Exception {
	    	
	    try{
	    	// request 파라미터를 map으로 변환
	        Map<String, String> paramMap = RequestConvertor.requestParamToMapAddSelInfo(request, true);
	        	
			Map flowInfoMap = chk1100Service.selectFlw1100FlowList(paramMap);
				
			model.addAttribute("nextFlowInfo", flowInfoMap);
			model.addAttribute("errorYN", "N");
				
	        //조회 성공 메시지 세팅
	        model.addAttribute("message", egovMessageSource.getMessage("success.common.select"));
	        	
	        return new ModelAndView("jsonView");
	        	
	    }catch(Exception ex){
	    	Log.error("selectChk1100ChkInfoAjax()", ex);
	    		
	    	model.addAttribute("errorYN", "Y");
	    	model.addAttribute("message", egovMessageSource.getMessage("fail.common.select"));
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
    @SuppressWarnings({ "rawtypes", "unchecked" })
	@RequestMapping(value="/chk/chk1000/chk1100/selectChk1100ChkInfoAjax.do")
	public ModelAndView selectChk1100ChkInfoAjax(HttpServletRequest request, HttpServletResponse response, ModelMap model )	throws Exception {
    	
    	try{
    		// request 파라미터를 map으로 변환
        	Map<String, String> paramMap = RequestConvertor.requestParamToMapAddSelInfo(request, true);
        	
			HttpSession ss = request.getSession();
			String prjId	= (String) ss.getAttribute("selPrjId");
			String licGrpId = ((LoginVO) ss.getAttribute("loginVO")).getLicGrpId();
        	
			paramMap.put("prjId", prjId);
			paramMap.put("licGrpId", licGrpId);
			
        	// 요구사항 단건정보 조회
        	Map chkInfoMap = (Map) chk1100Service.selectChk1100ChkInfoAjax(paramMap);        	
        	model.addAttribute("chkInfoMap", chkInfoMap);

			//atchFileId값을 넘기기 위한 FileVO
        	FileVO fileVO = new FileVO();
        	fileVO.setAtchFileId((String)chkInfoMap.get("atchFileId"));
        	
        	//파일 리스트 조회
			List<FileVO> fileList = fileMngService.fileDownList(fileVO);
			model.addAttribute("fileList",fileList);
			
			//검수 거부 목록 조회
			List<Map> rejectList = chk1100Service.selectChk1100ReqChkRejectList(paramMap);
			model.addAttribute("rejectList", rejectList);
			
        	//등록 성공 메시지 세팅
        	model.addAttribute("message", egovMessageSource.getMessage("success.common.select"));
        	
        	return new ModelAndView("jsonView");
        	
    	}catch(Exception ex){
    		Log.error("selectChk1100ChkInfoAjax()", ex);
    		throw new Exception(ex.getMessage());
    	}
    }
    
    /**
     * 요구사항 검수 요청 엑셀 목록을 조회한다.
     * @param request
     * @param response
     * @throws Exception
     */
	@RequestMapping(value="/chk/chk1000/chk1100/selectChk1100ExcelList.do")
	public ModelAndView selectChk1100ExcelList(@ModelAttribute("chk1100VO") Chk1100VO chk1100VO, HttpServletRequest request, HttpServletResponse response, ModelMap model )	throws Exception {

		
		HttpSession ss = request.getSession();
		String prjId	= (String) ss.getAttribute("selPrjId");
		String licGrpId = ((LoginVO) ss.getAttribute("loginVO")).getLicGrpId();
		
		chk1100VO.setPrjId(prjId);
		chk1100VO.setLicGrpId(licGrpId);
    			
		//엑셀 다운로드 양식의 헤더명 선언
		SheetHeader header = new SheetHeader(new String[]{
				egovMessageSource.getMessage("excel.accptNm")
				,egovMessageSource.getMessage("excel.reqChargerNm")
				,egovMessageSource.getMessage("excel.reqClsNm")
				,egovMessageSource.getMessage("excel.reqNm")
				,egovMessageSource.getMessage("excel.reqReqDtm")
				,egovMessageSource.getMessage("excel.reqChgDtm")
				,egovMessageSource.getMessage("excel.reqImprtNm")
				,egovMessageSource.getMessage("excel.reqDevWkTm")
				});
		/* 조회되는 데이터와 포멧 지정 
		 * ex 1. 수치형 new Metadata("property", XSSFCellStyle.ALIGN_RIGHT, "#,##0")
		 * ex 2. 사용자 지정 날짜 변환 new Metadata("property", ,"00-00-00") YYMMDD -> YY-MM-DD
		 * */
		@SuppressWarnings("serial")
		List<Metadata> metadatas = new ArrayList<Metadata>(); 
		metadatas.add(new Metadata("accptNm"));
		metadatas.add(new Metadata("reqChargerNm"));
		metadatas.add(new Metadata("reqClsNm"));
		metadatas.add(new Metadata("reqNm"));
		metadatas.add(new Metadata("reqReqDtm"));		        
		metadatas.add(new Metadata("reqChgDtm"));
		metadatas.add(new Metadata("reqImprtNm"));		        
		metadatas.add(new Metadata("reqDevWkTm"));

		BigDataSheetWriter writer = new BigDataSheetWriter(
				egovMessageSource.getMessage("excel.chk1100.sheetNm")
				, tempPath
				,egovMessageSource.getMessage("excel.chk1100.sheetNm")
				, metadatas);

		writer.beginSheet();

		try{

			ExcelDataListResultHandler  resultHandler = new ExcelDataListResultHandler(writer.getXMLSheetWriter(), writer.getStyleMap(), header, metadatas);

			chk1100Service.selectChk1100ExcelList(chk1100VO,resultHandler);

		}
		catch(Exception ex){
			Log.error("selectAdm5000ExcelList()", ex);
			throw new Exception(ex.getMessage());
		}finally{
			writer.endSheet();
		}

		return writer.getModelAndView();
	}
}
