package kr.opensoftlab.oslops.adm.adm8000.adm8100.web;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import kr.opensoftlab.oslops.adm.adm8000.adm8100.service.Adm8100Service;
import kr.opensoftlab.oslops.adm.adm8000.adm8100.vo.Adm8100VO;
import kr.opensoftlab.oslops.com.vo.LoginVO;
import kr.opensoftlab.oslops.stm.stm1000.stm1200.vo.Stm1200VO;
import kr.opensoftlab.sdf.util.OslAgileConstant;
import kr.opensoftlab.sdf.util.PagingUtil;
import kr.opensoftlab.sdf.util.RequestConvertor;

import org.apache.log4j.Logger;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;

import egovframework.com.cmm.EgovMessageSource;
import egovframework.rte.fdl.property.EgovPropertyService;
import egovframework.rte.ptl.mvc.tags.ui.pagination.PaginationInfo;


/**
 * @Class Name : Adm8100Controller.java
 * @Description : 보고서 관리 컨트롤러 클래스
 * @Modification Information
 * <pre>
 *   수정일      수정자          수정내용
 *  -------    --------    ---------------------------
 *  2018.10.18  공대영 	      최초 생성
 *  
 * </pre>
 *  @author 공대영
 *  @version 1.0
 *  @see 
 *  
 *  Copyright (C) Open Soft Lab Corp. All Rights Reserved
 */
@Controller
public class Adm8100Controller {

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

	/** Adm7000Service DI */
	@Resource(name = "adm8100Service")
	private Adm8100Service adm8100Service;


	/*
	 * 보고서 목록 이동
	 * 
	 * @param 
	 * @return 
	 * @exception Exception
	 */
	@RequestMapping(value="/adm/adm8000/adm8100/selectAdm8100View.do")
	public String selectAdm8100View(HttpServletRequest request, HttpServletResponse response, ModelMap model ) throws Exception {
		return "/adm/adm8000/adm8100/adm8100";
	}
	
	/**
	 * 
	 * 보고서 목록 조회
	 * 
	 * @param adm8100VO
	 * @param request
	 * @param response
	 * @param model
	 * @return
	 * @throws Exception
	 */
	@RequestMapping(value="/adm/adm8000/adm8100/selectAdm8100ReportListAjax.do")
	public ModelAndView selectAdm8100ReportListAjax(@ModelAttribute("ADM8100DAO") Adm8100VO adm8100VO, HttpServletRequest request, HttpServletResponse response, ModelMap model ) throws Exception {

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
			adm8100VO.setPageIndex(_pageNo);
			adm8100VO.setPageSize(_pageSize);
			adm8100VO.setPageUnit(_pageSize);
			
			
			PaginationInfo paginationInfo = PagingUtil.getPaginationInfo(adm8100VO);  /** paging - 신규방식 */

			List<Adm8100VO> adm8100List = null;

			HttpSession ss = request.getSession();
			LoginVO loginVO = (LoginVO) ss.getAttribute("loginVO");
			
			adm8100VO.setLicGrpId(loginVO.getLicGrpId());
			adm8100VO.setPrjId(ss.getAttribute("selPrjId").toString());

			/**
			 * 목록 조회
			 */
			int totCnt = 0;
			adm8100List =  adm8100Service.selectAdm8100ReportList(adm8100VO);

			
			/** 총 데이터의 건수 를 가져온다. */
			totCnt =adm8100Service.selectAdm8100ReportListCnt(adm8100VO);
			paginationInfo.setTotalRecordCount(totCnt);
			
			model.addAttribute("list", adm8100List);
			
			//페이지 정보 보내기
			Map<String, Integer> pageMap = new HashMap<String, Integer>();
			pageMap.put("pageNo",adm8100VO.getPageIndex());
			pageMap.put("listCount", adm8100List.size());
			pageMap.put("totalPages", paginationInfo.getTotalPageCount());
			pageMap.put("totalElements", totCnt);
			pageMap.put("pageSize", _pageSize);
			
			model.addAttribute("page", pageMap);
			
			return new ModelAndView("jsonView");
		}
		catch(Exception ex){
			Log.error("selectAdm8100ReportListAjax()", ex);
			throw new Exception(ex.getMessage());
		}
	}

	
	

	/*
	 * 보고서 생성 화면으로 이동
	 * 
	 * @param 
	 * @return 
	 * @exception Exception
	 */
	@RequestMapping(value="/adm/adm8000/adm8100/selectAdm8101View.do")
	public String selectAdm8101View(HttpServletRequest request, HttpServletResponse response, ModelMap model ) throws Exception {
		return "/adm/adm8000/adm8100/adm8101";
	}
	/**
	 * 
	 * 보고서 생성
	 * 
	 * @param request
	 * @param response
	 * @param model
	 * @return
	 * @throws Exception
	 */
	@SuppressWarnings({ "rawtypes", "unchecked" })
	@RequestMapping(value="/adm/adm8000/adm8000/insertAdm8100ReportInfo.do")
	public ModelAndView insertAdm8100ReportInfo(HttpServletRequest request, HttpServletResponse response, ModelMap model ) throws Exception {

		try{
			//리퀘스트에서 넘어온 파라미터를 맵으로 세팅
			Map paramMap = RequestConvertor.requestParamToMapAddSelInfo(request, true);
			HttpSession ss = request.getSession();

			LoginVO loginVO = (LoginVO) ss.getAttribute("loginVO");
			paramMap.put("prjId", ss.getAttribute("selPrjId").toString());
			paramMap.put("licGrpId", loginVO.getLicGrpId());	
			// 신규 조직 등록
			int iResult =adm8100Service.insertAdm8100ReportInfo(paramMap);

			//등록 성공 메시지 세팅
			model.addAttribute("message", egovMessageSource.getMessage("success.common.insert"));

			return new ModelAndView("jsonView");

		}catch(Exception ex){
			Log.error("insertAdm8100ReportInfo()", ex);

			//등록실패 메시지 세팅
			model.addAttribute("message", egovMessageSource.getMessage("fail.common.insert"));
			return new ModelAndView("jsonView");
		}
	}
	/**
	 * 
	 * 보고서 수정 화면으로 이동
	 * 
	 * @param request
	 * @param response
	 * @param model
	 * @return
	 * @throws Exception
	 */
	@RequestMapping(value="/adm/adm8000/adm8100/selectAdm8102View.do")
	public String selectAdm8102View(HttpServletRequest request, HttpServletResponse response, ModelMap model ) throws Exception {
		return "/adm/adm8000/adm8100/adm8102";
	}
	
	
	
	/**
	 * 
	 * 보고서 상세 조회
	 * 
	 * @param request
	 * @param response
	 * @param model
	 * @return
	 * @throws Exception
	 */
	@RequestMapping(value="/adm/adm8000/adm8100/selectAdm8100ReportInfoAjax.do")
	public ModelAndView selectAdm8100ReportInfoAjax( HttpServletRequest request, HttpServletResponse response, ModelMap model ) throws Exception {
		
		try{
			// request 파라미터를 map으로 변환
			Map<String, String> paramMap = RequestConvertor.requestParamToMapAddSelInfo(request, true);
			//현재 페이지 값, 보여지는 개체 수
		
			List<Map> list = null;
			
			HttpSession ss = request.getSession();
			LoginVO loginVO = (LoginVO) ss.getAttribute("loginVO");
			paramMap.put("licGrpId", loginVO.getLicGrpId());
			paramMap.put("prjId", ss.getAttribute("selPrjId").toString());
		
			list =  adm8100Service.selectAdm8100ReportInfo(paramMap);
			
			Map map = adm8100Service.selectAdm8100ReportMasterInfo(paramMap);
			
			model.addAttribute("list", list);
			model.addAttribute("reportInfo", map);
			
			return new ModelAndView("jsonView");
		}
		catch(Exception ex){
			Log.error("selectAdm8100ReportInfoAjax()", ex);
			throw new Exception(ex.getMessage());
		}
	}
	/**
	 * 
	 * 보고서 저장 및 작성 처리
	 * 
	 * @param request
	 * @param response
	 * @param model
	 * @return
	 * @throws Exception
	 */
	@SuppressWarnings({ "rawtypes", "unchecked" })
	@RequestMapping(value="/adm/adm8000/adm8100/updateAdm8100ReportInfo.do")
	public ModelAndView updateAdm8100ReportInfo(HttpServletRequest request, HttpServletResponse response, ModelMap model ) throws Exception {

		try{
			//리퀘스트에서 넘어온 파라미터를 맵으로 세팅
			Map paramMap = RequestConvertor.requestParamToMapAddSelInfo(request, true);
			HttpSession ss = request.getSession();

			LoginVO loginVO = (LoginVO) ss.getAttribute("loginVO");
			paramMap.put("prjId", ss.getAttribute("selPrjId").toString());
			paramMap.put("licGrpId", loginVO.getLicGrpId());	
			// 신규 조직 등록
			
			String itemCds[] =request.getParameterValues("itemCd");
			String meaVals[] =request.getParameterValues("meaVal");
			String apprVals[] =request.getParameterValues("apprVal");
			String optVals[] =request.getParameterValues("optVal");
			String modifyApprVals[] =request.getParameterValues("modifyApprVal");
			String modifyOptVals[] =request.getParameterValues("modifyOptVal");
			Adm8100VO adm8100VO = null;
			List<Adm8100VO> adm8100VOList = new ArrayList<Adm8100VO>();
			
			if(itemCds!=null){
				for (int i = 0; i < itemCds.length; i++) {
					adm8100VO = new Adm8100VO();
					adm8100VO.setLicGrpId(loginVO.getLicGrpId());
					adm8100VO.setPrjId(ss.getAttribute("selPrjId").toString());
					adm8100VO.setMeaDtm( (String)paramMap.get("meaDtm") );
					adm8100VO.setReportCd( (String)paramMap.get("reportCd") );
					adm8100VO.setItemCd(itemCds[i]);
					
					adm8100VO.setItemCd(itemCds[i]);
					adm8100VO.setMeaVal(meaVals[i]);
					adm8100VO.setApprVal(apprVals[i]);
					adm8100VO.setOptVal(optVals[i]);
					adm8100VO.setModifyApprVal(modifyApprVals[i]);
					adm8100VO.setModifyOptVal(modifyOptVals[i]);
					
					adm8100VO.setModifyUsrId(loginVO.getUsrId());
					adm8100VO.setModifyUsrIp(request.getRemoteAddr());
					
					
					adm8100VOList.add(adm8100VO);
				}
			}
			
			int iResult =adm8100Service.updateAdm8100ReportInfo(paramMap,adm8100VOList);

			//등록 성공 메시지 세팅
			model.addAttribute("message", egovMessageSource.getMessage("success.common.update"));

			return new ModelAndView("jsonView");

		}catch(Exception ex){
			Log.error("updateAdm8100ReportInfo()", ex);

			//등록실패 메시지 세팅
			model.addAttribute("message", egovMessageSource.getMessage("fail.common.update"));
			return new ModelAndView("jsonView");
		}
	}
	/**
	 * 
	 * 보고서 확정 처리
	 * 
	 * @param request
	 * @param response
	 * @param model
	 * @return
	 * @throws Exception
	 */
	@SuppressWarnings({ "rawtypes", "unchecked" })
	@RequestMapping(value="/adm/adm8000/adm8100/updateAdm8100ReporConfirm.do")
	public ModelAndView updateAdm8100ReporConfirm(HttpServletRequest request, HttpServletResponse response, ModelMap model ) throws Exception {

		try{
			//리퀘스트에서 넘어온 파라미터를 맵으로 세팅
			Map paramMap = RequestConvertor.requestParamToMapAddSelInfo(request, true);
			HttpSession ss = request.getSession();

			LoginVO loginVO = (LoginVO) ss.getAttribute("loginVO");
			paramMap.put("prjId", ss.getAttribute("selPrjId").toString());
			paramMap.put("licGrpId", loginVO.getLicGrpId());	
			// 신규 조직 등록
			
			Adm8100VO adm8100VO = null;
			
			adm8100VO = new Adm8100VO();
			adm8100VO.setLicGrpId(loginVO.getLicGrpId());
			adm8100VO.setPrjId(ss.getAttribute("selPrjId").toString());
			adm8100VO.setMeaDtm( (String)paramMap.get("meaDtm") );
			adm8100VO.setReportCd( (String)paramMap.get("reportCd") );
		
			adm8100VO.setModifyUsrId(loginVO.getUsrId());
			adm8100VO.setModifyUsrIp(request.getRemoteAddr());	
			adm8100Service.updateAdm8100ReporConfirm(adm8100VO);

			//등록 성공 메시지 세팅
			model.addAttribute("message", egovMessageSource.getMessage("success.common.update"));

			return new ModelAndView("jsonView");

		}catch(Exception ex){
			Log.error("updateAdm8100ReportInfo()", ex);

			//등록실패 메시지 세팅
			model.addAttribute("message", egovMessageSource.getMessage("fail.common.update"));
			return new ModelAndView("jsonView");
		}
	}
	
	
	/**
	 * 
	 * 보고서 상세 팝업
	 * 
	 * @param request
	 * @param response
	 * @param model
	 * @return
	 * @throws Exception
	 */
	@RequestMapping(value="/adm/adm8000/adm8100/selectAdm8103View.do")
	public String selectAdm8103View(HttpServletRequest request, HttpServletResponse response, ModelMap model ) throws Exception {
		return "/adm/adm8000/adm8100/adm8103";
	}

	/**
	 * 
	 * 보고서 인쇄용 팝업
	 * 
	 * @param request
	 * @param response
	 * @param model
	 * @return
	 * @throws Exception
	 */
	@RequestMapping(value="/adm/adm8000/adm8100/selectAdm8104View.do")
	public String selectAdm8104View(HttpServletRequest request, HttpServletResponse response, ModelMap model ) throws Exception {
		return "/adm/adm8000/adm8100/adm8104";
	}

	
}
