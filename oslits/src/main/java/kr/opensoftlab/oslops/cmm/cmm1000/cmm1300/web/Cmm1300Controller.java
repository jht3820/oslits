package kr.opensoftlab.oslops.cmm.cmm1000.cmm1300.web;

import java.util.List;
import java.util.Map;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import kr.opensoftlab.oslops.adm.adm7000.adm7000.service.Adm7000Service;
import kr.opensoftlab.oslops.cmm.cmm1000.cmm1300.service.Cmm1300Service;
import kr.opensoftlab.sdf.util.RequestConvertor;

import org.apache.log4j.Logger;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;

import egovframework.com.cmm.EgovMessageSource;
import egovframework.rte.fdl.property.EgovPropertyService;


/**
 * @Class Name : Cmm1300Controller.java
 * @Description : 공통 코드 컨트롤러 클래스
 * @Modification Information
 * <pre>
 *   수정일      수정자          수정내용
 *  -------    --------    ---------------------------
 *  2018.08.01  공대영          최초 생성
 *  
 * </pre>
 *  @author 공대영
 *  @version 1.0
 *  @see 
 *  
 *  Copyright (C) Open Soft Lab Corp. All Rights Reserved
 */
@Controller
public class Cmm1300Controller {

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

	/** EgovMessageSource */
	@Resource(name = "cmm1300Service")
	Cmm1300Service cmm1300Service;
	


	/**
	 * Adm4000 공통코드 마스터 페이지 
	 * @param 
	 * @return 
	 * @exception Exception
	 */
	@RequestMapping(value="/cmm/cmm1000/cmm1300/selectCmm1300View.do")
	public String selectCmm1300View(HttpServletRequest request, HttpServletResponse response, ModelMap model ) throws Exception {
		return "/cmm/cmm1000/cmm1300/cmm1300";
	}	

	/**
	 * cmm1000 공통코드 마스터 정보를 가져온다.
	 * @param 
	 * @return 
	 * @exception Exception
	 */
	@SuppressWarnings({ "rawtypes", "unchecked" })
	@RequestMapping(value="/cmm/cmm1000/cmm1300/selectCmm1300ListAjax.do")
	public ModelAndView selectCmm1300ListAjax(HttpServletRequest request, HttpServletResponse response, ModelMap model ) throws Exception {
		try{
			//리퀘스트에서 넘어온 파라미터를 맵으로 세팅
			Map<String, String> paramMap = RequestConvertor.requestParamToMapAddSelInfo(request, true);

			//공통코드 마스터 정보 조회
			List<Map> list = (List) cmm1300Service.selectCmm1300List(paramMap);

			model.addAttribute("list",list);

			model.addAttribute("message", egovMessageSource.getMessage("success.common.select"));

			return new ModelAndView("jsonView");
		}
		catch(Exception ex){
			Log.error("selectCmm1300ListAjax()", ex);

			//조회실패 메시지 세팅 및 저장 성공여부 세팅
			model.addAttribute("errorYN", "Y");
			model.addAttribute("message", egovMessageSource.getMessage("fail.common.select"));
			return new ModelAndView("jsonView");
		}
	}


}
