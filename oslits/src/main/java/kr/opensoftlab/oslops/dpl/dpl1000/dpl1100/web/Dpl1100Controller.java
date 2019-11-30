package kr.opensoftlab.oslops.dpl.dpl1000.dpl1100.web;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import kr.opensoftlab.oslops.com.vo.LoginVO;
import kr.opensoftlab.oslops.dpl.dpl1000.dpl1000.service.Dpl1000Service;
import kr.opensoftlab.oslops.dpl.dpl1000.dpl1100.service.Dpl1100Service;
import kr.opensoftlab.oslops.dpl.dpl1000.dpl1100.vo.Dpl1100VO;
import kr.opensoftlab.sdf.util.OslAgileConstant;
import kr.opensoftlab.sdf.util.PagingUtil;
import kr.opensoftlab.sdf.util.RequestConvertor;

import org.apache.log4j.Logger;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;

import com.google.gson.GsonBuilder;

import egovframework.com.cmm.EgovMessageSource;
import egovframework.rte.fdl.property.EgovPropertyService;
import egovframework.rte.ptl.mvc.tags.ui.pagination.PaginationInfo;

/**
 * @Class Name : Dpl1100Controller.java
 * @Description : Dpl1100Controller Controller class
 * @Modification Information
 *
 * @author 공대영
 * @since 2016.02.08
 * @version 1.0
 * @see Copyright (C) All right reserved.
 */

@Controller
public class Dpl1100Controller {

	/**
	 * Logging 을 위한 선언 Log는 반드시 가이드에 맞춰서 작성한다. 개발용으로는 debug 카테고리를 사용한다.
	 */
	protected Logger Log = Logger.getLogger(this.getClass());

	/** System Property 를 사용하기 위한 Bean Injection */
	@Resource(name = "propertiesService")
	protected EgovPropertyService propertiesService;

	/** EgovMessageSource */
	@Resource(name = "egovMessageSource")
	EgovMessageSource egovMessageSource;

	/** Dpl1000Service DI */
	@Resource(name = "dpl1000Service")
	private Dpl1000Service dpl1000Service;
	
	/** Dpl1100Service DI */
	@Resource(name = "dpl1100Service")
	private Dpl1100Service dpl1100Service;

	/**
	 * Dpl1100 배포 계획 요구사항 배정 화면으로 이동한다.
	 * 화면 이동시 배포 계획 목록을 조회하여 가져온다.
	 * 조회된 배포 계획 목록을 그리드에 세팅
	 * @param
	 * @return
	 * @exception Exception
	 */
	@SuppressWarnings({ "unchecked", "rawtypes" })
	@RequestMapping(value = "/dpl/dpl1000/dpl1100/selectDpl1100View.do")
	public String selectDpl1100View(HttpServletRequest request, HttpServletResponse response,ModelMap model) throws Exception {
		try{
			Map paramMap = RequestConvertor.requestParamToMap(request, true);
			paramMap.put("prjId", request.getSession().getAttribute("selPrjId"));
	
			// 배포 버전 정보 리스트 조회
			List<Map> selectDpl1000List = (List) dpl1000Service.selectDpl1000DeployVerNormalList(paramMap);
			String dplListJson = (new GsonBuilder().serializeNulls().create()).toJsonTree(selectDpl1000List).toString();
			
			//첫 화면 배포버전
			if(selectDpl1000List != null && selectDpl1000List.size() > 0){
				model.addAttribute("srchDplId", selectDpl1000List.get(0).get("dplId"));
			}
			
			model.addAttribute("dplList", dplListJson.replaceAll("<", "&lt"));
			
			// 조회 성공 및 성공메시지 세팅
    		model.addAttribute("errorYn", 'N');
        	model.addAttribute("message", egovMessageSource.getMessage("success.common.select"));
			
			return "/dpl/dpl1000/dpl1100/dpl1100";
		}
    	catch(Exception ex){
    		Log.error("selectSpr1000BView()", ex);
    		
    		// 조회 실패 및 실패메시지 세팅
    		model.addAttribute("errorYn", 'Y');
    		model.addAttribute("message", egovMessageSource.getMessage("fail.common.select"));
    		throw new Exception(ex.getMessage());
    	}
	}

	/**
	 * Dpl1100 배포계획에 요구사항을 배정한다.
	 * @param
	 * @return
	 * @exception Exception
	 */
	@RequestMapping(value = "/dpl/dpl1000/dpl1100/insertDpl1100Dpl.do")
	public ModelAndView insertDpl1100Dpl(HttpServletRequest request,HttpServletResponse response, ModelMap model) throws Exception {

		try {
			// request 파라미터를 map으로 변환
			Map<String, String> paramMap = RequestConvertor.requestParamToMapAddSelInfo(request, true);

			HttpSession ss = request.getSession();
			// 프로젝트 ID를 가져와 Map에 추가한다.
			paramMap.put("prjId", ss.getAttribute("selPrjId").toString());

			//배포계획 요구사항 배정
			dpl1100Service.insertDpl1100ReqDplInfo(paramMap);
			
			// 등록 성공여부 및 등록성공 메시지 세팅
			model.addAttribute("errorYn", "N");
			model.addAttribute("message",egovMessageSource.getMessage("success.common.insert"));
			return new ModelAndView("jsonView");
			
		} catch (Exception ex) {
			Log.error("insertDpl1100Dpl()", ex);

			// 등록 실패여부 및 등록실패 메시지 세팅
			model.addAttribute("errorYn", "Y");
			model.addAttribute("message",egovMessageSource.getMessage("fail.common.insert"));
			return new ModelAndView("jsonView");
		}
	}
	
	/**
	 * Dpl1100 배포계획에서 요구사항을 배정 제외한다.
	 * @param
	 * @return
	 * @exception Exception
	 */
	@RequestMapping(value = "/dpl/dpl1000/dpl1100/deleteDpl1100Dpl.do")
	public ModelAndView deleteDpl1100Dpl(HttpServletRequest request,HttpServletResponse response, ModelMap model) throws Exception {
		
		try {
			// request 파라미터를 map으로 변환
			Map<String, String> paramMap = RequestConvertor.requestParamToMapAddSelInfo(request, true);
			
			HttpSession ss = request.getSession();
			// 프로젝트 ID를 가져와 Map에 추가한다.
			paramMap.put("prjId", ss.getAttribute("selPrjId").toString());
			
			//배포계획 요구사항 배정 제외
			dpl1100Service.deleteDpl1100ReqDplInfo(paramMap);
			
			// 삭제 성공여부 및 삭제성공 메시지 세팅
			model.addAttribute("errorYn", "N");
			model.addAttribute("message",egovMessageSource.getMessage("success.common.delete"));
			return new ModelAndView("jsonView");
			
		} catch (Exception ex) {
			Log.error("deleteDpl1100Dpl()", ex);
			
			// 삭제 실패여부 및 삭제실패 메시지 세팅
			model.addAttribute("errorYn", "Y");
			model.addAttribute("message",egovMessageSource.getMessage("fail.common.delete"));
			return new ModelAndView("jsonView");
		}
	}

	/**
	 * Dpl1100 배포버전 요구사항 목록 및 미배정 목록을 조회 한다.
	 * @param
	 * @return
	 * @exception Exception
	 */
	@SuppressWarnings("rawtypes")
	@RequestMapping(value = "/dpl/dpl1000/dpl1100/selectDpl1100DplListAjax.do")
	public ModelAndView selectDpl1100ReqListAjax(@ModelAttribute("dpl1100VO") Dpl1100VO dpl1100VO, HttpServletRequest request,HttpServletResponse response, ModelMap model) throws Exception {

		try {

			// 리퀘스트에서 넘어온 파라미터를 맵으로 세팅
			Map<String, String> paramMap = RequestConvertor.requestParamToMap(request, true);
			
			// 로그인 VO를 가져온다.
			HttpSession ss = request.getSession();
			LoginVO loginVO = (LoginVO) ss.getAttribute("loginVO");
			
			// 라이선스 그룹 ID
			String licGrpId = loginVO.getLicGrpId();
			// 프로젝트 ID 
			String prjId = (String) paramMap.get("prjId");
			
			// 넘어온 프로젝트 Id가 없을경우 세션의 선택된 프로젝트 Id 가져온다.
			if(prjId == null || "".equals(prjId)) {
				prjId = ss.getAttribute("selPrjId").toString();
			}
			
			//현재 페이지 값, 보여지는 개체 수
			String _pageNo_str = paramMap.get("pageNo");
			String _pageSize_str = paramMap.get("pageSize");
			
			int _pageNo = 1;
			int _pageSize = OslAgileConstant.pageSize;
			
			// 넘어온 페이지 정보가 있다면 해당 값으로 세팅
			if(_pageNo_str != null && !"".equals(_pageNo_str)){
				_pageNo = Integer.parseInt(_pageNo_str)+1;  
			}
			if(_pageSize_str != null && !"".equals(_pageSize_str)){
				_pageSize = Integer.parseInt(_pageSize_str);  
			}
			
			// 라이선스 그룹 ID, 프로젝트 Id 세팅
			dpl1100VO.setLicGrpId(licGrpId);
			dpl1100VO.setPrjId(prjId);
			
			// 페이지 번호, 페이지 사이즈 세팅
			dpl1100VO.setPageIndex(_pageNo);
			dpl1100VO.setPageSize(_pageSize);
			dpl1100VO.setPageUnit(_pageSize);
			
			// 페이징 처리유무 YN 세팅 - 페이징 처리 필요유무 구분값
			// 배포계획에 배정/미배정된 요구사항 조회 시 페이징 처리 필요
			// 요구사항 상세정보 조회시 해당 요구사항이 배포계획에 배정되어있는지 정보를
			// 조회할 떄는 페이징 처리 불필요
			dpl1100VO.setPagingYn("Y");
						
			PaginationInfo paginationInfo = PagingUtil.getPaginationInfo(dpl1100VO);  /** paging - 신규방식 */
			
			// 총 데이터 건수
			int totCnt = 0;
			
			// 배포계획 배정/미배정 요구사항 목록
			List dplAssignReqList = null;
			
			if ("clsAdd".equals(paramMap.get("clsMode"))) {
				
				// 배포계획 배정된 요구사항 총 데이터 건수 조회
				totCnt = dpl1100Service.selectDpl1100ExistDplListCnt(dpl1100VO) ;
				// 배포계획 배정된 요구사항 목록 조회
				dplAssignReqList = dpl1100Service.selectDpl1100ExistDplList(dpl1100VO);

			} else if ("clsDel".equals(paramMap.get("clsMode"))) {
				// 배포계획 미배정된 요구사항 총 데이터 건수 조회
				totCnt = dpl1100Service.selectDpl1100NotExistDplListCnt(dpl1100VO) ;
				// 배포계획 미배정된 요구사항 목록 조회
				dplAssignReqList = dpl1100Service.selectDpl1100NotExistDplList(dpl1100VO);
			}
			
			// 총 데이터 건수 세팅
			paginationInfo.setTotalRecordCount(totCnt);

			// 배포계획에 배정/미배정된 요구사항 목록 세팅
			model.addAttribute("list", dplAssignReqList);

			//페이지 정보 보내기
			Map<String, Integer> pageMap = new HashMap<String, Integer>();
			pageMap.put("pageNo",dpl1100VO.getPageIndex());
			pageMap.put("listCount", dplAssignReqList.size());
			pageMap.put("totalPages", paginationInfo.getTotalPageCount());
			pageMap.put("totalElements", totCnt);
			pageMap.put("pageSize", _pageSize);
			
			// 페이지 정보 세팅
			model.addAttribute("page", pageMap);
			
			// 조회 성공여부 및 조회성공메시지 세팅
			model.addAttribute("errorYn", "N");
			model.addAttribute("message",egovMessageSource.getMessage("success.common.select"));

			return new ModelAndView("jsonView");
		} catch (Exception ex) {
			Log.error("selectDpl1100ReqListAjax()", ex);

			// 조회 실패여부 및 조회실패 메시지 세팅
			model.addAttribute("errorYn", "Y");
			model.addAttribute("message",egovMessageSource.getMessage("fail.common.select"));
			return new ModelAndView("jsonView");
		}
	}
	

}
