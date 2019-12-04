package kr.opensoftlab.oslops.cmm.cmm1000.cmm1800.web;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import kr.opensoftlab.oslops.cmm.cmm1000.cmm1000.service.Cmm1000Service;
import kr.opensoftlab.oslops.cmm.cmm1000.cmm1000.vo.Cmm1000VO;
import kr.opensoftlab.oslops.cmm.cmm1000.cmm1600.service.Cmm1600Service;
import kr.opensoftlab.oslops.cmm.cmm1000.cmm1600.vo.Cmm1600VO;
import kr.opensoftlab.oslops.cmm.cmm1000.cmm1700.service.Cmm1700Service;
import kr.opensoftlab.oslops.cmm.cmm1000.cmm1700.vo.Cmm1700VO;
import kr.opensoftlab.oslops.cmm.cmm1000.cmm1800.service.Cmm1800Service;
import kr.opensoftlab.oslops.com.vo.LoginVO;
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
 * @Class Name : Cmm1800Controller.java
 * @Description : 프로세스 조회 공통 컨트롤러 클래스
 * @Modification Information
 * <pre>
 *   수정일      수정자          수정내용
 *  -------    --------    ---------------------------
 *  2018.10.16  공대영          최초 생성
 *  
 * </pre>
 *  @author 공대영
 *  @version 1.0
 *  @see 
 *  
 *  Copyright (C) Open Soft Lab Corp. All Rights Reserved
 */
@Controller
public class Cmm1800Controller {
	
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
	
	@Resource(name = "cmm1800Service")
	Cmm1800Service cmm1800Service;
	
	
	
	
	/**
	 *
	 * 프로세스 조회 공통 팝업 화면 이동
	 * 
	 * @param request
	 * @param response
	 * @param model
	 * @return
	 * @throws Exception
	 */
	@RequestMapping(value="/cmm/cmm1000/cmm1800/selectCmm1800View.do")
	public String selectCmm1800View(HttpServletRequest request, HttpServletResponse response, ModelMap model ) throws Exception {
			return "/cmm/cmm1000/cmm1800/cmm1800";
	}	
	

	/**
	 * 
	 * 프로세스 정보 조회 공통 목록 조회
	 * 
	 * @param cmm1800VO
	 * @param request
	 * @param response
	 * @param model
	 * @return
	 * @throws Exception
	 */
	@RequestMapping(value="/cmm/cmm1000/cmm1800/selectCmm1800ProcessListAjax.do")
	public ModelAndView selectCmm1800ProcessListAjax( HttpServletRequest request, HttpServletResponse response, ModelMap model)	throws Exception {
		
    	try{
    		//리퀘스트에서 넘어온 파라미터를 맵으로 세팅
        	Map<String, String> paramMap = RequestConvertor.requestParamToMapAddSelInfo(request, true);
        	HttpSession ss = request.getSession();
			LoginVO loginVO = (LoginVO) ss.getAttribute("loginVO");
        	paramMap.put("prjId", (String) request.getSession().getAttribute("selPrjId") );
        	paramMap.put("licGrpId", loginVO.getLicGrpId() );
        	
        	
    		List<Map> list = null;


    		// 목록 조회  authGrpIds
			list = cmm1800Service.selectCmm1800ProcessList(paramMap);
		    
    		// 총 건수
		    
			model.addAttribute("list", list);
        	
        	//조회성공메시지 세팅
        	model.addAttribute("message", egovMessageSource.getMessage("success.common.select"));
        	
        	return new ModelAndView("jsonView", model);
    	}catch(Exception ex){
    		Log.error("selectCmm1800ProcessListAjax()", ex);
    		
    		//조회실패 메시지 세팅
    		model.addAttribute("message", egovMessageSource.getMessage("fail.common.select"));
    		return new ModelAndView("jsonView", model);
    	}
	}	
	
}
