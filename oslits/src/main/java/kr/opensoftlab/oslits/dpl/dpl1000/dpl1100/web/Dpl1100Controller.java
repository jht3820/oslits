package kr.opensoftlab.oslits.dpl.dpl1000.dpl1100.web;

import java.util.List;
import java.util.Map;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import kr.opensoftlab.oslits.com.vo.LoginVO;
import kr.opensoftlab.oslits.dpl.dpl1000.dpl1100.service.Dpl1100Service;
import kr.opensoftlab.oslits.dpl.dpl1000.dpl1100.vo.Dpl1100VO;
import kr.opensoftlab.oslits.dpl.dpl2000.dpl2000.service.Dpl2000Service;
import kr.opensoftlab.sdf.util.OslAgileConstant;
import kr.opensoftlab.sdf.util.RequestConvertor;

import org.apache.log4j.Logger;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.servlet.ModelAndView;

import com.google.gson.GsonBuilder;

import egovframework.com.cmm.EgovMessageSource;
import egovframework.rte.fdl.property.EgovPropertyService;

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

	// ** Dpl1100Service DI *//*
	@Resource(name = "dpl1100Service")
	private Dpl1100Service dpl1100Service;

	// ** Dpl2000Service DI *//*
	@Resource(name = "dpl2000Service")
	private Dpl2000Service dpl2000Service;

	/**
	 * Dpl1100 배포 버전 정보 조회
	 * 
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
			List<Map> selectDpl2000List = (List) dpl2000Service.selectDpl2000SelectBox(paramMap);
			String dplListJson = (new GsonBuilder().serializeNulls().create()).toJsonTree(selectDpl2000List).toString();
			//첫 화면 배포버전
			if(selectDpl2000List != null && selectDpl2000List.size() > 0){
				model.addAttribute("srchDplId", selectDpl2000List.get(0).get("dplId"));
			}
			
			model.addAttribute("dplList", dplListJson);
			
			return "/dpl/dpl1000/dpl1100/dpl1100";
		}
    	catch(Exception ex){
    		Log.error("selectSpr1000BView()", ex);
    		throw new Exception(ex.getMessage());
    	}
	}

	/**
	 * Dpl1000 배포자 리스트를 가져온다.
	 * 
	 * @param
	 * @return
	 * @exception Exception
	 */
	@SuppressWarnings({ "unchecked", "rawtypes" })
	@RequestMapping(value="/dpl/dpl1000/dpl1100/selectDpl1000DeployNmListAjax.do")
	public ModelAndView selectDpl1000DeployNmListAjax(HttpServletRequest request, HttpServletResponse response,ModelMap model) throws Exception {
		try {

			// 리퀘스트에서 넘어온 파라미터를 맵으로 세팅
			Map paramMap = RequestConvertor.requestParamToMap(request, true);
			paramMap.put("prjId", request.getSession().getAttribute("selPrjId"));

			// 배포 버전 정보 리스트 조회
			List<Map> selectDpl2000List = (List) dpl2000Service.selectDpl2000SelectBox(paramMap);

			model.addAttribute("list", selectDpl2000List);

			// 조회성공메시지 세팅
			model.addAttribute("message",egovMessageSource.getMessage("success.common.select"));

			return new ModelAndView("jsonView", model);
		} catch (Exception ex) {
			Log.error("selectDpl1000DeployNmListAjax()", ex);

			// 조회실패 메시지 세팅
			model.addAttribute("message",egovMessageSource.getMessage("fail.common.select"));
			return new ModelAndView("jsonView", model);
		}
	}

	/**
	 * 배포계획 요구사항 배정
	 * 
	 * @param
	 * @return
	 * @exception Exception
	 */
	@SuppressWarnings({ "rawtypes", "unchecked" })
	@RequestMapping(value = "/dpl/dpl1000/dpl1100/insertDpl1100Dpl.do")
	public ModelAndView insertDpl1100Dpl(HttpServletRequest request,HttpServletResponse response, ModelMap model) throws Exception {

		try {

			// request 파라미터를 map으로 변환
			Map paramMap = RequestConvertor.requestParamToMapAddSelInfo(request, true);
			HttpSession ss = request.getSession();
			paramMap.put("prjId", ss.getAttribute("selPrjId").toString());

			//배포계획 요구사항 배정
			dpl1100Service.insertDpl1200ReqDplInfo(paramMap);

			model.addAttribute("saveYN", "Y");
			model.addAttribute("message",egovMessageSource.getMessage("success.common.insert"));
			return new ModelAndView("jsonView");
		} catch (Exception ex) {
			Log.error("insertDpl1100Dpl()", ex);

			// 조회실패 메시지 세팅 및 저장 성공여부 세팅
			model.addAttribute("saveYN", "N");
			model.addAttribute("message",egovMessageSource.getMessage("fail.common.insert"));
			return new ModelAndView("jsonView");
		}
	}
	
	/**
	 * 배포계획 요구사항 제외
	 * 
	 * @param
	 * @return
	 * @exception Exception
	 */
	@SuppressWarnings({ "unchecked", "rawtypes" })
	@RequestMapping(value = "/dpl/dpl1000/dpl1100/deleteDpl1100Dpl.do")
	public ModelAndView deleteDpl1100Dpl(HttpServletRequest request,HttpServletResponse response, ModelMap model) throws Exception {
		
		try {
			
			// request 파라미터를 map으로 변환
			Map paramMap = RequestConvertor.requestParamToMapAddSelInfo(request, true);
			HttpSession ss = request.getSession();
			paramMap.put("prjId", ss.getAttribute("selPrjId").toString());
			
			//배포계획 요구사항 배정 제외
			dpl1100Service.deleteDpl1200ReqDplInfo(paramMap);
			
			model.addAttribute("saveYN", "Y");
			model.addAttribute("message",egovMessageSource.getMessage("success.common.delete"));
			return new ModelAndView("jsonView");
		} catch (Exception ex) {
			Log.error("deleteDpl1100Dpl()", ex);
			
			// 조회실패 메시지 세팅 및 저장 성공여부 세팅
			model.addAttribute("saveYN", "N");
			model.addAttribute("message",egovMessageSource.getMessage("fail.common.delete"));
			return new ModelAndView("jsonView");
		}
	}

	/**
	 * Dpl1100 Ajax 배포버전 요구사항 목록 및 미배정 목록 조회
	 * 
	 * @param
	 * @return
	 * @exception Exception
	 */
	@SuppressWarnings({ "rawtypes", "unchecked" })
	@RequestMapping(value = "/dpl/dpl1000/dpl1100/selectDpl1100DplListAjax.do")
	public ModelAndView selectDpl1100ReqListAjax(HttpServletRequest request,HttpServletResponse response, ModelMap model) throws Exception {

		try {

			// 리퀘스트에서 넘어온 파라미터를 맵으로 세팅
			Map paramMap = RequestConvertor.requestParamToMap(request, true);
			List<Dpl1100VO> dplList = null;
			HttpSession ss = request.getSession();
			paramMap.put("prjId", ss.getAttribute("selPrjId"));

			if ("clsAdd".equals(paramMap.get("clsMode"))) {
				// 요구사항 분류 배정 목록 가져오기
				dplList = dpl1100Service.selectDpl1100ExistDplList(paramMap);
			} else if ("clsDel".equals(paramMap.get("clsMode"))) {
				// 요구사항 분류 미배정 목록 가져오기
				dplList = dpl1100Service.selectDpl1100NotExistDplList(paramMap);
			}

			// 요구사항 분류 배정, 미배정 목록 세팅
			model.addAttribute("list", dplList);

			// 조회성공메시지 세팅
			model.addAttribute("message",egovMessageSource.getMessage("success.common.select"));

			return new ModelAndView("jsonView");
		} catch (Exception ex) {
			Log.error("selectReq4200ReqClsAddDelListAjax()", ex);

			// 조회실패 메시지 세팅
			model.addAttribute("message",egovMessageSource.getMessage("fail.common.select"));
			return new ModelAndView("jsonView");
		}
	}
	
	@SuppressWarnings({ "unchecked", "rawtypes" })
	@RequestMapping(value="/dpl/dpl1000/dpl1100/selectDpl1100ExistBuildInfoAjax.do")
	public ModelAndView selectDpl1100ExistBuildInfoAjax(HttpServletRequest request, HttpServletResponse response,ModelMap model) throws Exception {
		try {

			// 리퀘스트에서 넘어온 파라미터를 맵으로 세팅
			Map paramMap = RequestConvertor.requestParamToMap(request, true);
			paramMap.put("prjId", request.getSession().getAttribute("selPrjId"));

			// 배포 버전 정보 리스트 조회
			String buildInfo  = dpl1100Service.selectDpl1100ExistBuildInfo(paramMap);

			model.addAttribute("buildInfo", buildInfo);

			// 조회성공메시지 세팅
			model.addAttribute("message",egovMessageSource.getMessage("success.common.select"));

			return new ModelAndView("jsonView", model);
		} catch (Exception ex) {
			Log.error("selectDpl1100ExistBuildInfoAjax()", ex);

			// 조회실패 메시지 세팅
			model.addAttribute("message",egovMessageSource.getMessage("fail.common.select"));
			return new ModelAndView("jsonView", model);
		}
	}
	

}
