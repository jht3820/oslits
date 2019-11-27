package kr.opensoftlab.oslits.req.req4000.req4200.web;

import java.util.List;
import java.util.Map;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import kr.opensoftlab.oslits.req.req4000.req4200.service.Req4200Service;
import kr.opensoftlab.sdf.util.RequestConvertor;

import org.apache.log4j.Logger;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;

import egovframework.com.cmm.EgovMessageSource;
import egovframework.rte.fdl.cmmn.trace.LeaveaTrace;
import egovframework.rte.fdl.property.EgovPropertyService;

/**
 * @Class Name : Req4200Controller.java
 * @Description : Req4200Controller Controller class
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
public class Req4200Controller {

	/** 로그4j 로거 로딩 */
	private final Logger Log = Logger.getLogger(this.getClass());
	
	/** Req4200Service DI */
    @Resource(name = "req4200Service")
    private Req4200Service req4200Service;
    
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
	 * Req4200 화면 이동(이동시 요구사항 분류 정보 목록 조회)
	 * @param 
	 * @return 
	 * @exception Exception
	 */
	@SuppressWarnings({ "unchecked", "rawtypes" })
	@RequestMapping(value="/req/req4000/req4200/selectReq4200View.do")
    public String selectReq4200View(HttpServletRequest request, HttpServletResponse response, ModelMap model ) throws Exception {
    	
    	try{
    		//리퀘스트에서 넘어온 파라미터를 맵으로 세팅
        	Map paramMap = RequestConvertor.requestParamToMapAddSelInfo(request, true);
        	
        	//요구사항 분류 목록 가져오기
        	List<Map> reqClsList = (List) req4200Service.selectReq4200ReqClsList(paramMap);
        	
        	model.addAttribute("reqClsList", reqClsList);
        	
        	return "/req/req4000/req4200/req4200";
    	}
    	catch(Exception ex){
    		Log.error("selectReq4200View()", ex);
    		throw new Exception(ex.getMessage());
    	}
    }
    
	/**
	 * Req4200 Ajax 요구사항 분류정보 목록 조회
	 * @param 
	 * @return 
	 * @exception Exception
	 */
    @SuppressWarnings({ "rawtypes", "unchecked" })
	@RequestMapping(value="/req/req4000/req4200/selectReq4200ReqClsListAjax.do")
    public ModelAndView selectReq4200ReqClsListAjax(HttpServletRequest request, HttpServletResponse response, ModelMap model ) throws Exception {
    	
    	try{
        	
    		//리퀘스트에서 넘어온 파라미터를 맵으로 세팅
        	Map paramMap = RequestConvertor.requestParamToMapAddSelInfo(request, true);
        	
        	//요구사항 분류목록 가져오기
        	List<Map> reqClsList = (List) req4200Service.selectReq4200ReqClsList(paramMap);
        	
        	//조회성공메시지 세팅
        	model.addAttribute("reqClsList", reqClsList);
        	model.addAttribute("message", egovMessageSource.getMessage("success.common.select"));
        	
        	return new ModelAndView("jsonView");
    	}
    	catch(Exception ex){
    		Log.error("selectReq4200ReqClsListAjax()", ex);
    		
    		//조회실패 메시지 세팅
    		model.addAttribute("message", egovMessageSource.getMessage("fail.common.select"));
    		return new ModelAndView("jsonView");
    	}
    }
	
    /**
	 * Req4200 Ajax 요구사항 분류 배정 목록 및 미배정 목록 조회
	 * @param 
	 * @return 
	 * @exception Exception
	 */
    @SuppressWarnings({ "rawtypes", "unchecked" })
	@RequestMapping(value="/req/req4000/req4200/selectReq4200ReqClsAddDelListAjax.do")
    public ModelAndView selectReq4200ReqClsAddDelListAjax(HttpServletRequest request, HttpServletResponse response, ModelMap model ) throws Exception {
    	
    	try{
        	
    		//리퀘스트에서 넘어온 파라미터를 맵으로 세팅
        	Map paramMap = RequestConvertor.requestParamToMapAddSelInfo(request, true);
        	List<Map> reqClsList = null;
        	
        	if("clsAdd".equals(paramMap.get("clsMode"))){
	        	//요구사항 분류 배정 목록 가져오기
	        	reqClsList = (List) req4200Service.selectReq4200ReqClsAddListAjax(paramMap);
	        	
        	}else if("clsDel".equals(paramMap.get("clsMode"))){
        		//요구사항 분류 미배정 목록 가져오기
        		reqClsList = (List) req4200Service.selectReq4200ReqClsDelListAjax(paramMap);
            	
        	}

        	//요구사항 분류 배정, 미배정 목록 세팅
        	model.addAttribute("list", reqClsList);
        	
        	//조회성공메시지 세팅
        	model.addAttribute("message", egovMessageSource.getMessage("success.common.select"));
        	
        	return new ModelAndView("jsonView");
    	}
    	catch(Exception ex){
    		Log.error("selectReq4200ReqClsAddDelListAjax()", ex);
    		
    		//조회실패 메시지 세팅
    		model.addAttribute("message", egovMessageSource.getMessage("fail.common.select"));
    		return new ModelAndView("jsonView");
    	}
    }
    
    /**
	 * Req4200 Ajax 요구사항 분류 배정 목록 및 미배정 목록 조회
	 * @param 
	 * @return 
	 * @exception Exception
	 */
    @SuppressWarnings({ "rawtypes" })
	@RequestMapping(value="/req/req4000/req4200/updateReq4200ReqClsAddDelListAjax.do")
    public ModelAndView updateReq4200ReqClsAddDelListAjax(HttpServletRequest request, HttpServletResponse response, ModelMap model ) throws Exception {
    	
    	try{
        	
    		//리퀘스트에서 넘어온 파라미터를 맵으로 세팅
        	Map paramMap = RequestConvertor.requestParamToMapAddSelInfo(request, true);
        	
        	//요구사항 분류에 요구사항 배정 및 삭제 처리
        	req4200Service.updateReq4200ReqClsAddDelListAjax(paramMap);
        	
        	//조회성공메시지 세팅
        	model.addAttribute("message", egovMessageSource.getMessage("success.common.select"));
        	
        	return new ModelAndView("jsonView");
    	}
    	catch(Exception ex){
    		Log.error("updateReq4200ReqClsAddDelListAjax()", ex);
    		
    		//조회실패 메시지 세팅
    		model.addAttribute("saveYn", "N");
    		model.addAttribute("message", egovMessageSource.getMessage("fail.common.select"));
    		return new ModelAndView("jsonView");
    	}
    }
    
}
