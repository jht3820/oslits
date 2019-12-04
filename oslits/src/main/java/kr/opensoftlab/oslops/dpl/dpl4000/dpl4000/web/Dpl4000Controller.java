package kr.opensoftlab.oslops.dpl.dpl4000.dpl4000.web;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import kr.opensoftlab.oslops.com.fms.web.service.FileMngService;
import kr.opensoftlab.oslops.dpl.dpl1000.dpl1000.service.Dpl1000Service;
import kr.opensoftlab.oslops.stm.stm3000.stm3000.service.Stm3000Service;

import org.apache.log4j.Logger;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestMapping;

import egovframework.com.cmm.EgovMessageSource;
import egovframework.rte.fdl.cmmn.trace.LeaveaTrace;
import egovframework.rte.fdl.property.EgovPropertyService;

/**
 * @Class Name : Dpl4000Controller.java
 * @Description : Dpl4000Controller Controller class
 * @Modification Information
 *
 * @author 강재민
 * @since 2016.01.30
 * @version 1.0
 * @see
 *   수정일     		 수정자           수정내용
 *  ------------------------------------
 *   2018.10.08  공대영          배포관련 기능 추가
 *   2019.03.14  배용진          기능개선
 * 
 *  Copyright (C)  All right reserved.
 */

@Controller
public class Dpl4000Controller {
	
	/** 로그4j 로거 로딩 */
	protected Logger Log = Logger.getLogger(this.getClass());

	/** EgovMessageSource */
	@Resource(name = "egovMessageSource")
	EgovMessageSource egovMessageSource;

	/** EgovPropertyService */
	@Resource(name = "propertiesService")
	protected EgovPropertyService propertiesService;

	/** TRACE */
	@Resource(name = "leaveaTrace")
	LeaveaTrace leaveaTrace;

	
	/**
	 * Dpl4000 배포 계획 통합정보 화면으로 이동한다.
	 * @param request
	 * @param response
	 * @param model
	 * @return
	 * @throws Exception
	 */
	@RequestMapping(value="/dpl/dpl4000/dpl4000/selectDpl4000View.do")
	public String selectDpl4000View( HttpServletRequest request, HttpServletResponse response, ModelMap model ) throws Exception {
		return "/dpl/dpl4000/dpl4000/dpl4000";
	}
}
