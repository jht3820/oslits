package kr.opensoftlab.oslops.adm.adm3000.adm3000.web;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.log4j.Logger;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestMapping;

import egovframework.com.cmm.EgovMessageSource;
import egovframework.rte.fdl.cmmn.trace.LeaveaTrace;
import egovframework.rte.fdl.property.EgovPropertyService;

/**
 * @Class Name : Adm3000Controller.java
 * @Description : Adm3000Controller Controller class
 * @Modification Information
 *
 * @author 
 * @since 2017.04.16
 * @version 1.0
 * @see
 *  
 *  Copyright (C)  All right reserved.
 */

@Controller
public class Adm3000Controller {
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
	 * Adm3000 라이선스 관리
	 * @param 
	 * @return 
	 * @exception Exception
	 */
	@RequestMapping(value="/adm/adm3000/adm3000/selectAdm3000View.do")
    public String selectAdm3000View(HttpServletRequest request, HttpServletResponse response, ModelMap model ) throws Exception {
    	try{

        	return "/adm/adm3000/adm3000/adm3000";
    	}
    	catch(Exception ex){
    		Log.error("selectAdm3000View()", ex);
    		model.addAttribute("message", egovMessageSource.getMessage("fail.common.select"));
    		throw new Exception(ex.getMessage());
    	}
    }
}
