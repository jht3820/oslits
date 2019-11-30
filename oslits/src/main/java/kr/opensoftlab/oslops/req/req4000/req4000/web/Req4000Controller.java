package kr.opensoftlab.oslops.req.req4000.req4000.web;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import kr.opensoftlab.oslops.req.req4000.req4000.service.Req4000Service;
import kr.opensoftlab.sdf.excel.BigDataSheetWriter;
import kr.opensoftlab.sdf.excel.ExcelDataListResultHandler;
import kr.opensoftlab.sdf.excel.Metadata;
import kr.opensoftlab.sdf.excel.SheetHeader;
import kr.opensoftlab.sdf.util.RequestConvertor;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;

import egovframework.com.cmm.EgovMessageSource;
import egovframework.rte.fdl.cmmn.trace.LeaveaTrace;
import egovframework.rte.fdl.property.EgovPropertyService;

/**
 * @Class Name : Req4000Controller.java
 * @Description : Req4000Controller Controller class
 * @Modification Information
 *
 * @author 정형택
 * @since 2016.01.30.
 * @version 1.0
 * @see
 *  
 *  Copyright (C) OpenSoftLab Corp All right reserved.
 */

@Controller
public class Req4000Controller {

	/** 로그4j 로거 로딩 */
	private final Logger Log = Logger.getLogger(this.getClass());

	/** Req4000Service DI */
	@Resource(name = "req4000Service")
	private Req4000Service req4000Service;

	/** EgovMessageSource */
	@Resource(name = "egovMessageSource")
	EgovMessageSource egovMessageSource;

	/** EgovPropertyService */
	@Resource(name = "propertiesService")
	protected EgovPropertyService propertiesService;

	/** TRACE */
	@Resource(name = "leaveaTrace")
	LeaveaTrace leaveaTrace;

	@Value("${Globals.fileStorePath}")
	private String tempPath;
	/**
	 * Req4001 화면 -  산출물 업로드 테스트
	 * @param 
	 * @return 
	 * @exception Exception
	 */
	@SuppressWarnings({ "unchecked", "rawtypes" })
	@RequestMapping(value="/req/req4000/req4000/selectReq4001View.do")
	public String selectReq4001View(HttpServletRequest request, HttpServletResponse response, ModelMap model ) throws Exception {

		try{
			//리퀘스트에서 넘어온 파라미터를 맵으로 세팅
			Map paramMap = RequestConvertor.requestParamToMapAddSelInfo(request, true);

			//요구사항 분류 목록 가져오기
			List<Map> reqClsList = (List) req4000Service.selectReq4000ReqClsList(paramMap);

			model.addAttribute("reqClsList", reqClsList);

			return "/req/req4000/req4000/req4001";
		}
		catch(Exception ex){
			Log.error("selectReq4001View()", ex);
			throw new Exception(ex.getMessage());
		}
	}
	
	/**
	 * Req4002 화면 -  산출물 양식 업로드 테스트
	 * @param 
	 * @return 
	 * @exception Exception
	 */
	@SuppressWarnings({ "unchecked", "rawtypes" })
	@RequestMapping(value="/req/req4000/req4000/selectReq4002View.do")
	public String selectReq4002View(HttpServletRequest request, HttpServletResponse response, ModelMap model ) throws Exception {

		try{
			//리퀘스트에서 넘어온 파라미터를 맵으로 세팅
			Map paramMap = RequestConvertor.requestParamToMapAddSelInfo(request, true);

			//요구사항 분류 목록 가져오기
			List<Map> reqClsList = (List) req4000Service.selectReq4000ReqClsList(paramMap);

			model.addAttribute("reqClsList", reqClsList);

			return "/req/req4000/req4000/req4002";
		}
		catch(Exception ex){
			Log.error("selectReq4002View()", ex);
			throw new Exception(ex.getMessage());
		}
	}
	
	/**
	 * Req4000 화면 이동(이동시 요구사항 분류 정보 목록 조회)
	 * @param 
	 * @return 
	 * @exception Exception
	 */
	@SuppressWarnings({ "unchecked", "rawtypes" })
	@RequestMapping(value="/req/req4000/req4000/selectReq4000View.do")
	public String selectReq4000View(HttpServletRequest request, HttpServletResponse response, ModelMap model ) throws Exception {

		try{
			//리퀘스트에서 넘어온 파라미터를 맵으로 세팅
			Map paramMap = RequestConvertor.requestParamToMapAddSelInfo(request, true);

			//요구사항 분류 목록 가져오기
			List<Map> reqClsList = (List) req4000Service.selectReq4000ReqClsList(paramMap);

			model.addAttribute("reqClsList", reqClsList);

			return "/req/req4000/req4000/req4000";
		}
		catch(Exception ex){
			Log.error("selectReq4000View()", ex);
			throw new Exception(ex.getMessage());
		}
	}

	/**
	 * Req4000 Ajax 요구사항 분류정보 목록 조회
	 * @param 
	 * @return 
	 * @exception Exception
	 */
	@SuppressWarnings({ "rawtypes", "unchecked" })
	@RequestMapping(value="/req/req4000/req4000/selectReq4000ReqClsListAjax.do")
	public ModelAndView selectReq4000ReqClsListAjax(HttpServletRequest request, HttpServletResponse response, ModelMap model ) throws Exception {

		try{

			//리퀘스트에서 넘어온 파라미터를 맵으로 세팅
			Map paramMap = RequestConvertor.requestParamToMapAddSelInfo(request, true);

			String prjId = (String) paramMap.get("prjId");
        	
    		//prjId없는경우 세션에서 가져와서 넣기
    		if(prjId != null && !"".equals(prjId)){
    			//selPrjId제거
    			paramMap.remove("selPrjId");
    			
    			//param prjId put
    			paramMap.put("selPrjId", prjId);
    		}
    		
			//요구사항 분류목록 가져오기
			List<Map> reqClsList = (List) req4000Service.selectReq4000ReqClsList(paramMap);

			//조회성공메시지 세팅
			model.addAttribute("reqClsList", reqClsList);
			model.addAttribute("message", egovMessageSource.getMessage("success.common.select"));

			return new ModelAndView("jsonView");
		}
		catch(Exception ex){
			Log.error("selectReq4000ReqClsListAjax()", ex);

			//조회실패 메시지 세팅
			model.addAttribute("message", egovMessageSource.getMessage("fail.common.select"));
			return new ModelAndView("jsonView");
		}
	}

	/**
	 * Req4000 요구사항 분류정보 조회(단건) AJAX
	 * @param 
	 * @return 
	 * @exception Exception
	 */
	@SuppressWarnings({ "rawtypes", "unchecked" })
	@RequestMapping(value="/req/req4000/req4000/selectReq4000ReqClsInfoAjax.do")
	public ModelAndView selectReq4000ReqClsInfoAjax(HttpServletRequest request, HttpServletResponse response, ModelMap model ) throws Exception {

		try{

			// request 파라미터를 map으로 변환
			Map<String, String> paramMap = RequestConvertor.requestParamToMapAddSelInfo(request, true);

			String prjId = (String) paramMap.get("prjId");
        	
    		//prjId없는경우 세션에서 가져와서 넣기
    		if(prjId != null && !"".equals(prjId)){
    			//selPrjId제거
    			paramMap.remove("selPrjId");
    			
    			//param prjId put
    			paramMap.put("selPrjId", prjId);
    		}
    		
			//분류정보조회
			Map<String, String> menuInfoMap = (Map) req4000Service.selectReq4000ReqClsInfo(paramMap);

			//조회성공메시지 세팅
			model.addAttribute("message", egovMessageSource.getMessage("success.common.select"));

			return new ModelAndView("jsonView", menuInfoMap);
		}
		catch(Exception ex){
			Log.error("selectReq4000ReqClsInfoAjax()", ex);

			//조회실패 메시지 세팅
			model.addAttribute("message", egovMessageSource.getMessage("fail.common.select"));
			return new ModelAndView("jsonView");
		}
	}

	/**
	 * Req4000 요구사항 분류정보 등록(단건) AJAX
	 * 분류정보 등록후 등록 정보 결과 및 정보를 화면으로 리턴한다.
	 * @param 
	 * @return 
	 * @exception Exception
	 */
	@SuppressWarnings({ "rawtypes", "unchecked" })
	@RequestMapping(value="/req/req4000/req4000/insertReq4000ReqClsInfoAjax.do")
	public ModelAndView insertAdm1000MenuInfoAjax(HttpServletRequest request, HttpServletResponse response, ModelMap model ) throws Exception {

		try{

			// request 파라미터를 map으로 변환
			Map<String, String> paramMap = RequestConvertor.requestParamToMapAddSelInfo(request, true);

			// 분류 등록
			Map<String, String> reqClsInfoMap = (Map) req4000Service.insertReq4000ReqClsInfo(paramMap);

			//등록 성공 메시지 세팅
			model.addAttribute("message", egovMessageSource.getMessage("success.common.insert"));

			return new ModelAndView("jsonView", reqClsInfoMap);
		}
		catch(Exception ex){
			Log.error("selectAdm1000MenuInfoAjax()", ex);

			//조회실패 메시지 세팅 및 저장 성공여부 세팅
			model.addAttribute("saveYN", "N");
			model.addAttribute("message", egovMessageSource.getMessage("fail.common.insert"));
			return new ModelAndView("jsonView");
		}
	}

	/**
	 * Req4000 요구사항 분류정보 삭제 AJAX
	 * 분류정보 삭제 처리
	 * @param 
	 * @return 
	 * @exception Exception
	 */
	@RequestMapping(value="/req/req4000/req4000/deleteReq4000ReqClsInfoAjax.do")
	public ModelAndView deleteAdm1000MenuInfoAjax(HttpServletRequest request, HttpServletResponse response, ModelMap model ) throws Exception {

		try{
			//request 파라미터를 map으로 변환
			Map<String, Object> paramMap = RequestConvertor.requestParamToMapAddSelInfoList(request, true,"reqClsId");

			// 분류 삭제여부 체크
			List<String> notDelReqClsList = req4000Service.deleteReq4000ReqClsAssignChk(paramMap);

			// 요구사항 배정된 분류 가 1건이라도 있을경우
			if(notDelReqClsList.size() > 0){
				model.addAttribute("errorYn", "Y");
				model.addAttribute("notDelReqClsList", notDelReqClsList);
				model.addAttribute("message", egovMessageSource.getMessage("req4000.reqClsDeleteReason.fail"));
				return new ModelAndView("jsonView");
			}

			// 요구사항 분류 삭제
			req4000Service.deleteReq4000ReqClsInfo(paramMap);
			
			// 삭제성공여부 및 삭제성공 메시지 세팅
			model.addAttribute("errorYn", "N");
			model.addAttribute("message", egovMessageSource.getMessage("success.common.delete"));

			return new ModelAndView("jsonView");
		}
		catch(Exception ex){
			Log.error("deleteAdm1000MenuInfoAjax()", ex);

			// 삭제실패여부 및 삭제실패 메시지 세팅
			model.addAttribute("errorYn", "Y");
			model.addAttribute("message", egovMessageSource.getMessage("fail.common.delete"));
			return new ModelAndView("jsonView");
		}
	}

	/**
	 * Req4000 메뉴정보 수정(단건) AJAX
	 * 분류정보 수정 처리
	 * @param 
	 * @return 
	 * @exception Exception
	 */
	@RequestMapping(value="/req/req4000/req4000/updateReq4000ReqClsInfoAjax.do")
	public ModelAndView updateAdm1000MenuInfoAjax(HttpServletRequest request, HttpServletResponse response, ModelMap model ) throws Exception {

		try{

			//request 파라미터를 map으로 변환
			Map<String, String> paramMap = RequestConvertor.requestParamToMapAddSelInfo(request, true);

			//분류 수정
			req4000Service.updateReq4000ReqClsInfo(paramMap);

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
	 * 요구사항분류 생성관리 엑셀 조회
	 * @param 
	 * @return 
	 * @exception Exception
	 */
	@SuppressWarnings({ "rawtypes" })
	@RequestMapping(value="/req/req4000/req4000/selectReq4000ExcelList.do")
	public ModelAndView selectReq4000ExcelList(HttpServletRequest request, HttpServletResponse response, ModelMap model ) throws Exception {
		//리퀘스트에서 넘어온 파라미터를 맵으로 세팅
		Map paramMap = RequestConvertor.requestParamToMapAddSelInfo(request, true);

		//엑셀 다운로드 양식의 헤더명 선언
		String strPrjId = egovMessageSource.getMessage("excel.prjId");
		String strPrjNm = egovMessageSource.getMessage("excel.prjNm");
		String strReqClsId = egovMessageSource.getMessage("excel.reqClsId");
		String strUpperReqClsId = egovMessageSource.getMessage("excel.upperReqClsId");
		String strReqClsNm = egovMessageSource.getMessage("excel.reqClsNm");
		String strLvl = egovMessageSource.getMessage("excel.lvl");
		String strOrd = egovMessageSource.getMessage("excel.ord");
		SheetHeader header = new SheetHeader(new String[]{strPrjId, strPrjNm, strReqClsId, strUpperReqClsId, strReqClsNm, strLvl, strOrd});
		
		/* 조회되는 데이터와 포멧 지정 
		 * ex 1. 수치형 new Metadata("property", XSSFCellStyle.ALIGN_RIGHT, "#,##0")
		 * ex 2. 사용자 지정 날짜 변환 new Metadata("property", ,"00-00-00") YYMMDD -> YY-MM-DD
		 * */
		List<Metadata> metadatas = new ArrayList<Metadata>(); 
		metadatas.add(new Metadata("prjId"));
		metadatas.add(new Metadata("prjNm"));
		metadatas.add(new Metadata("reqClsId"));
		metadatas.add(new Metadata("upperReqClsId"));
		metadatas.add(new Metadata("reqClsNm"));
		metadatas.add(new Metadata("lvl"));		        
		metadatas.add(new Metadata("ord"));

		BigDataSheetWriter writer = new BigDataSheetWriter(egovMessageSource.getMessage("excel.req4000.sheetNm"), tempPath, egovMessageSource.getMessage("excel.req4000.sheetNm"), metadatas);

		writer.beginSheet();

		try{

			ExcelDataListResultHandler  resultHandler = new ExcelDataListResultHandler(writer.getXMLSheetWriter(), writer.getStyleMap(), header, metadatas);

			req4000Service.selectReq4000ExcelList(paramMap,resultHandler);

		}
		catch(Exception ex){
			Log.error("selectReq4000ExcelList()", ex);
			throw new Exception(ex.getMessage());
		}finally{
			writer.endSheet();
		}

		return writer.getModelAndView();
	}
}
