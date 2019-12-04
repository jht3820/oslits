package kr.opensoftlab.oslops.req.req2000.req2000.web;

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
import org.springframework.web.servlet.ModelAndView;

import com.google.gson.GsonBuilder;

import egovframework.com.cmm.EgovMessageSource;
import egovframework.rte.fdl.cmmn.trace.LeaveaTrace;
import egovframework.rte.fdl.property.EgovPropertyService;
import egovframework.rte.ptl.mvc.tags.ui.pagination.PaginationInfo;
import kr.opensoftlab.oslops.com.fms.web.service.FileMngService;
import kr.opensoftlab.oslops.com.vo.LicVO;
import kr.opensoftlab.oslops.com.vo.LoginVO;
import kr.opensoftlab.oslops.prj.prj1000.prj1100.service.Prj1100Service;
import kr.opensoftlab.oslops.req.req2000.req2000.service.Req2000Service;
import kr.opensoftlab.oslops.req.req4000.req4100.service.Req4100Service;
import kr.opensoftlab.oslops.req.req4000.req4100.vo.Req4100VO;
import kr.opensoftlab.sdf.excel.BigDataSheetWriter;
import kr.opensoftlab.sdf.excel.ExcelDataListResultHandler;
import kr.opensoftlab.sdf.excel.Metadata;
import kr.opensoftlab.sdf.excel.SheetHeader;
import kr.opensoftlab.sdf.util.OslAgileConstant;
import kr.opensoftlab.sdf.util.PagingUtil;
import kr.opensoftlab.sdf.util.RequestConvertor;

/**
 * @Class Name : Req2000Controller.java
 * @Description : Req2000Controller Controller class
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
public class Req2000Controller {

	/**
	 * Logging 을 위한 선언
	 * Log는 반드시 가이드에 맞춰서 작성한다. 개발용으로는 debug 카테고리를 사용한다.
	 */
	protected Logger Log = Logger.getLogger(this.getClass());

	/** Req2000Service DI*/
	@Resource(name = "req2000Service")
	private Req2000Service req2000Service;
	 
	/** Req4000Service DI */
	@Resource(name = "req4100Service")
	private Req4100Service req4100Service;
	
	/** prj1100Service DI */
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
	
	@Value("${Globals.fileStorePath}")
	private String tempPath;

	/** TRACE */
	@Resource(name = "leaveaTrace")
	LeaveaTrace leaveaTrace;

	/**
	 * Req2000 전체 요구사항 목록 화면이동(이동시 요구사항 목록 조회)
	 * @param 
	 * @return 
	 * @exception Exception
	 */
	@SuppressWarnings({"unchecked","rawtypes"})
	@RequestMapping(value="/req/req2000/req2000/selectReq2000View.do")
	public String selectReq2000ListView(HttpServletRequest request, HttpServletResponse response, ModelMap model ) throws Exception {
		// request 파라미터를 map으로 변환
		Map<String, String> paramMap = RequestConvertor.requestParamToMapAddSelInfo(request, true);
		
		//세션에서 프로젝트 아이디값 가져오기
		HttpSession ss = request.getSession();
		paramMap.put("prjId", (String) ss.getAttribute("selPrjId"));
    	
		// 세션에서 해당 사용자의 라이선스 그룹 아이디 가져오기
		LicVO licVo = (LicVO)ss.getAttribute("licVO");
		paramMap.put("licGrpId", licVo.getLicGrpId());
		
	
		
    	//프로젝트 작업 흐름 목록 가져오기
    	List<Map> selectFlowList = null;
    	String selectFlowListJson = (new GsonBuilder().serializeNulls().create()).toJsonTree(selectFlowList).toString();
    	model.addAttribute("flowList", selectFlowListJson.replaceAll("<", "&lt"));
    	
    	//작업흐름, 개발주기 아이디값 넘어온 경우
    	model.addAttribute("flowId", paramMap.get("flowId"));
    	
		return "/req/req2000/req2000/req2000";
	}
	
	/**
	 * Req2000 요구사항 목록 AJAX 조회
	 * Req2000은 Req4000과 같은 쿼리(req4100_SQL_Oracle.xml)사용
	 * @param 
	 * @return 
	 * @exception Exception
	 */
	@RequestMapping(value="/req/req2000/req2000/selectReq2000ListAjaxView.do")
	public ModelAndView selectReq2000ListAjaxView(@ModelAttribute("req4100VO") Req4100VO req4100VO,HttpServletRequest request, HttpServletResponse response, ModelMap model ) throws Exception {

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

			/**
			 * 상수로 정의된 EVENT_SELECT와 화면에서 세팅해서 보낸 EVENT를 비교해서 분기 처리
			 */
			//req2000VO.setUsrId(loginVO.getUsrId());
			
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
			
			//조회 성공 결과 값
			model.addAttribute("selectYN", "Y");
			return new ModelAndView("jsonView");
		}
		catch(Exception ex){
			Log.error("selectReq2000ListView()", ex);
			//조회 실패 결과 값
			model.addAttribute("selectYN", "N");
			return new ModelAndView("jsonView");
		}
	}

	/**
	 * Req2000 요구사항 코멘트 등록 AJAX
	 * 요구사항 정보를 조회한다.
	 * @param 
	 * @return 
	 * @exception Exception
	 */
	@SuppressWarnings("rawtypes")
	@RequestMapping(value="/req/req2000/req2000/insertReq2000ReqCommentInfoAjax.do")
	public ModelAndView insertReq2000ReqCommentInfoAjax(HttpServletRequest request, HttpServletResponse response, ModelMap model ) throws Exception {

		try{

			// request 파라미터를 map으로 변환
			Map<String, String> paramMap = RequestConvertor.requestParamToMapAddSelInfo(request, true);

			// 요구사항 코멘트 등록
			req2000Service.insertReq2000ReqCommentInfo(paramMap);

			//요구사항 코멘트 목록 조회
			List reqCommentList = (List) req2000Service.selectReq2000ReqCommentListAjax(paramMap);
			model.addAttribute("reqCommentList", reqCommentList);

			//등록 성공 메시지 세팅
			model.addAttribute("message", egovMessageSource.getMessage("success.common.insert"));

			return new ModelAndView("jsonView");
		}
		catch(Exception ex){
			Log.error("insertReq2000ReqCommentInfoAjax()", ex);

			//조회실패 메시지 세팅 및 저장 성공여부 세팅
			model.addAttribute("saveYN", "N");
			model.addAttribute("message", egovMessageSource.getMessage("fail.common.insert"));
			return new ModelAndView("jsonView");
		}
	}

	/**
	 * Req3000 화면 이동(이동시 요구사항 목록 조회) - 전체요구사항 목록과 같은 화면 사용.
	 * @param 
	 * @return 
	 * @exception Exception
	 */					   
	@SuppressWarnings({"unchecked","rawtypes"})
	@RequestMapping(value="/req/req3000/req3000/selectReq3000View.do")
	public String selectReq3000ListView(HttpServletRequest request, HttpServletResponse response, ModelMap model ) throws Exception {
		// request 파라미터를 map으로 변환
		Map<String, String> paramMap = RequestConvertor.requestParamToMapAddSelInfo(request, true);
		
		//세션에서 프로젝트 아이디값 가져오기
		HttpSession ss = request.getSession();
		paramMap.put("prjId", (String) ss.getAttribute("selPrjId"));
    	
		// 세션에서 해당 사용자의 라이선스 그룹 아이디 가져오기
		LicVO licVo = (LicVO)ss.getAttribute("licVO");
		paramMap.put("licGrpId", licVo.getLicGrpId());
		

				
    	//프로젝트 작업 흐름 목록 가져오기
    	List<Map> selectFlowList = null;
    	String selectFlowListJson = (new GsonBuilder().serializeNulls().create()).toJsonTree(selectFlowList).toString();
    	model.addAttribute("flowList", selectFlowListJson.replaceAll("<", "&lt"));
    	
    	//작업흐름, 개발주기 아이디값 넘어온 경우
    	model.addAttribute("flowId", paramMap.get("flowId"));
    	
		model.addAttribute("viewGb", "charge");
		return "/req/req2000/req2000/req2000";
	}
	
	/**
	 * REQ2000 요구사항 엑셀 다운로드
	 * @param 
	 * @return 
	 * @exception Exception
	 */
	@SuppressWarnings({ "rawtypes", "unchecked" })
	@RequestMapping(value="/req/req2000/req2000/selectReq2000ExcelList.do")
	public ModelAndView selectReq2000ExcelList(HttpServletRequest request, HttpServletResponse response, ModelMap model ) throws Exception {
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
		String strReqOrd 		= egovMessageSource.getMessage("excel.reqOrd");			// 요구사항 순번
		String strPrjId 		= egovMessageSource.getMessage("excel.prjId");			// 프로젝트 ID
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
		String strReqUsrPositionNm 	= egovMessageSource.getMessage("excel.reqUsrPositionNm");	// 요청자 직급
		String strReqUsrDutyNm 	= egovMessageSource.getMessage("excel.reqUsrDutyNm");	// 요청자 직책
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
		
		
		SheetHeader header = new SheetHeader(new String[]{strReqOrd, strPrjId, strReqId, strReqProTypeNm, strReqNewTypeNm
														,strReqNo ,strReqNm ,strReqDesc ,strReqDtm , strReqUsrNm, strReUsrDeptNm, strReqUsrPositionNm, strReqUsrDutyNm,  strReqUsrEmail 
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
		metadatas.add(new Metadata("reqOrd"));
		metadatas.add(new Metadata("prjId"));
		metadatas.add(new Metadata("reqId"));
		metadatas.add(new Metadata("reqProTypeNm"));
		metadatas.add(new Metadata("reqNewTypeNm"));
		metadatas.add(new Metadata("reqNo"));
		metadatas.add(new Metadata("reqNm"));
		metadatas.add(new Metadata("reqDesc"));
		metadatas.add(new Metadata("reqDtm", "00-00-00"));
		metadatas.add(new Metadata("reqUsrNm"));
		metadatas.add(new Metadata("reqUsrDeptNm"));
		metadatas.add(new Metadata("reqUsrPositionNm"));
		metadatas.add(new Metadata("reqUsrDutyNm"));
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

		BigDataSheetWriter writer = new BigDataSheetWriter(egovMessageSource.getMessage("excel.req2000.sheetNm"), tempPath, egovMessageSource.getMessage("excel.req2000.sheetNm"), metadatas);

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
			req2000Service.selectReq2000ExcelList(searchParamMap,resultHandler);
		}
		catch(Exception ex){
			Log.error("selectReq2000ExcelList()", ex);
			throw new Exception(ex.getMessage());
		}finally{
			writer.endSheet();
		}

		return writer.getModelAndView();
	}
	
}
