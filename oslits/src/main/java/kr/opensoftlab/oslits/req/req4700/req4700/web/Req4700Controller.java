package kr.opensoftlab.oslits.req.req4700.req4700.web;

 import java.util.Calendar;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import kr.opensoftlab.oslits.adm.adm6000.adm6000.service.Adm6000Service;
import kr.opensoftlab.oslits.prj.prj1000.prj1000.service.Prj1000Service;

import kr.opensoftlab.oslits.req.req1000.req1000.vo.Req1000VO;
import kr.opensoftlab.oslits.req.req4000.req4100.service.Req4100Service;
import kr.opensoftlab.sdf.util.RequestConvertor;

import org.apache.log4j.Logger;
import org.json.simple.JSONArray;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;

import egovframework.com.cmm.EgovMessageSource;
import egovframework.rte.fdl.cmmn.trace.LeaveaTrace;
import egovframework.rte.fdl.property.EgovPropertyService;

/**
 * @Class Name : Req4700Controller.java
 * @Description : Req4700Controller Controller class
 * @Modification Information
 *
 * @author 김현종
 * @since 2018.01.15.
 * @version 1.0
 * @see
 *  
 *  Copyright (C) OpenSoftLab Corp All right reserved.
 */

@Controller
public class Req4700Controller {

	/**
	 * Logging 을 위한 선언
	 * Log는 반드시 가이드에 맞춰서 작성한다. 개발용으로는 debug 카테고리를 사용한다.
	 */
	protected Logger Log = Logger.getLogger(this.getClass());
	

	/** Req1000Service DI */
	@Resource(name = "adm6000Service")
	private Adm6000Service adm6000Service;

	/** Req1000Service DI */
	@Resource(name = "req4100Service")
	private Req4100Service req4100Service;
	
	/** Prj6000Service DI */
    @Resource(name = "prj1000Service")
    private Prj1000Service prj1000Service;

	
	/** EgovMessageSource */
	@Resource(name = "egovMessageSource")
	EgovMessageSource egovMessageSource;

	/** EgovPropertyService */
	@Resource(name = "propertiesService")
	protected EgovPropertyService propertiesService;

	/** TRACE */
	@Resource(name = "leaveaTrace")
	LeaveaTrace leaveaTrace;
	
	

	

}
