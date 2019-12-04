package kr.opensoftlab.oslops.cmm.cmm1000.cmm1500.web;

import java.util.List;
import java.util.Map;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import kr.opensoftlab.oslops.adm.adm7000.adm7000.service.Adm7000Service;
import kr.opensoftlab.oslops.com.vo.LoginVO;
import kr.opensoftlab.sdf.util.RequestConvertor;

import org.apache.log4j.Logger;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestMapping;

import egovframework.com.cmm.EgovMessageSource;
import egovframework.rte.fdl.property.EgovPropertyService;


/**
 * @Class Name : Cmm1500Controller.java
 * @Description : 요구사항 분류 공통팝업 컨트롤러 클래스
 * @Modification Information
 * <pre>
 *   수정일      수정자          수정내용
 *  -------    --------    ---------------------------
 *  2018.09.27  공대영         최초 생성
 *  
 * </pre>
 *  @author 공대영
 *  @version 1.0
 *  @see 
 *  
 *  Copyright (C) Open Soft Lab Corp. All Rights Reserved
 */
@Controller
public class Cmm1500Controller {
	
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
	

	/**
	 * 요구사항 분류 선택 공통 팝업
	 * @param 
	 * @return 
	 * @exception Exception
	 */
	@RequestMapping(value="/cmm/cmm1000/cmm1500/selectCmm1500View.do")
	public String selectCmm1400View( HttpServletRequest request, HttpServletResponse response, ModelMap model ) throws Exception {
		return "/cmm/cmm1000/cmm1500/cmm1500";
	}
	
	

}
