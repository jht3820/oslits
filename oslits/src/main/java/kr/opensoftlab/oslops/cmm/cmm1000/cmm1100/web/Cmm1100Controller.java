package kr.opensoftlab.oslops.cmm.cmm1000.cmm1100.web;

import java.util.List;
import java.util.Map;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import kr.opensoftlab.oslops.cmm.cmm1000.cmm1100.service.Cmm1100Service;
import kr.opensoftlab.oslops.com.vo.LoginVO;
import kr.opensoftlab.sdf.util.RequestConvertor;

import org.apache.log4j.Logger;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;

import egovframework.com.cmm.EgovMessageSource;
import egovframework.rte.fdl.property.EgovPropertyService;


/**
 * @Class Name : Cmm1100Controller.java
 * @Description : 공통 - 프로젝트 컨트롤러 클래스
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
public class Cmm1100Controller {

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
	
	@Resource(name = "cmm1100Service")
	Cmm1100Service cmm1100Service;
	


	/**
	 * 프로젝트 선택 팝업 이동
	 * @param 
	 * @return 
	 * @exception Exception
	 */
	@RequestMapping(value="/cmm/cmm1000/cmm1100/selectCmm1100View.do")
	public String selectCmm1100View(HttpServletRequest request, HttpServletResponse response, ModelMap model ) throws Exception {
		return "/cmm/cmm1000/cmm1100/cmm1100";
	}

	 
    /**
	 * Prj1000 조회버튼 클릭시 프로젝트 생성관리 조회 AJAX
	 * @param 
	 * @return 
	 * @exception Exception
	 */
    @SuppressWarnings({ "rawtypes", "unchecked" })
	@RequestMapping(value="/cmm/cmm1000/cmm1100/selectCmm1100ViewAjax.do")
    public ModelAndView selectCmm1100ViewAjax(HttpServletRequest request, HttpServletResponse response, ModelMap model ) throws Exception {
    	try{
    		

    		//리퀘스트에서 넘어온 파라미터를 맵으로 세팅
    		Map<String, String> paramMap = RequestConvertor.requestParamToMap(request, true);
    		
    		//로그인VO 가져오기
    		HttpSession ss = request.getSession();
    		LoginVO loginVO = (LoginVO) ss.getAttribute("loginVO");

    		if(paramMap.get("selectSearch") == null){
    			paramMap.put("selectSearch", "");
    		}
    		
    		paramMap.put("usrId", loginVO.getUsrId());
    		
        	//프로젝트 생성관리 목록 가져오기
        	List<Map> selectCmm1100List = (List) cmm1100Service.selectCmm1100View(paramMap);

        	model.put("list", selectCmm1100List);
        	
        	//조회성공메시지 세팅
        	model.addAttribute("message", egovMessageSource.getMessage("success.common.select"));
        	
        	

        	return new ModelAndView("jsonView", model);
    	}
    	catch(Exception ex){
    		Log.error("selectCmm1100ViewAjax()", ex);
    		model.addAttribute("message", egovMessageSource.getMessage("fail.common.select"));
    		throw new Exception(ex.getMessage());
    	}
    }
	

}
