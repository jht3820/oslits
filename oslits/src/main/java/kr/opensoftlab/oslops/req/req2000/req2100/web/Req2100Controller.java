package kr.opensoftlab.oslops.req.req2000.req2100.web;

import java.util.List;
import java.util.Map;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import kr.opensoftlab.oslops.adm.adm4000.adm4000.service.Adm4000Service;
import kr.opensoftlab.oslops.com.fms.web.service.FileMngService;
import kr.opensoftlab.oslops.prj.prj1000.prj1100.service.Prj1100Service;
import kr.opensoftlab.oslops.req.req2000.req2100.service.Req2100Service;
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
 * @Class Name : Req2100Controller.java
 * @Description : Req2100Controller Controller class
 * @Modification Information
 *
 * @author 배용진
 * @since 2018.11.07.
 * @version 1.0
 * @see
 *  
 *  Copyright (C) OpenSoftLab Corp All right reserved.
 */

@Controller
public class Req2100Controller {

	/**
	 * Logging 을 위한 선언
	 * Log는 반드시 가이드에 맞춰서 작성한다. 개발용으로는 debug 카테고리를 사용한다.
	 */
	protected Logger Log = Logger.getLogger(this.getClass());
	
	/** Req2100Service DI*/
	@Resource(name = "req2100Service")
	private Req2100Service req2100Service;
	
	/** EgovMessageSource */
	@Resource(name = "egovMessageSource")
	EgovMessageSource egovMessageSource;

	/** EgovPropertyService */
	@Resource(name = "propertiesService")
	protected EgovPropertyService propertiesService;
	
	@Value("${Globals.fileStorePath}")
	private String tempPath;

	/** TRACE */
	@Resource(name = "leaveaTrace")
	LeaveaTrace leaveaTrace;

	
	/**
	 * Req2100 요구사항 통계 현황 화면이동
	 * @param 
	 * @return 
	 * @exception Exception
	 */
	@RequestMapping(value="/req/req2000/req2100/selectReq2100View.do")
	public String selectReq2100ListView(HttpServletRequest request, HttpServletResponse response, ModelMap model ) throws Exception {
    	
		return "/req/req2000/req2100/req2100";
	}
	
	
	/**
	 * Req2100 요구사항 통계 현황 데이터 조회
	 * @param 
	 * @return 
	 * @exception Exception
	 */
	@SuppressWarnings({"unchecked","rawtypes"})
	@RequestMapping(value="/req/req2000/req2100/selectReq2100StatisticDataAjax.do")
	public ModelAndView selectReq2100StatisticDataAjax(HttpServletRequest request, HttpServletResponse response, ModelMap model ) throws Exception {
    	
		try{
			// request 파라미터를 map으로 변환
			Map paramMap = RequestConvertor.requestParamToMapAddSelInfo(request, true);
			paramMap.put("prjId", (String)paramMap.get("selPrjId"));
			
			// 프로세스 별 요구사항 처리완료 건
			List<Map> processReqCompCntList = req2100Service.selectReq2100ProcessReqEndCntAjax(paramMap);

			model.addAttribute("processReqCompCntList", processReqCompCntList);
			
			//조회 성공 결과 값, 메시지
			model.addAttribute("errorYn", "N");
			model.addAttribute("message", egovMessageSource.getMessage("success.common.select"));
			
			return new ModelAndView("jsonView");
		}
		catch(Exception ex){
			Log.error("selectReq2100StatisticDataAjax()", ex);
			//조회실패 결과 값, 메시지
			model.addAttribute("errorYn", "Y");
			model.addAttribute("message", egovMessageSource.getMessage("fail.common.select"));
			
			return new ModelAndView("jsonView");
		}
	}

}
