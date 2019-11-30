package kr.opensoftlab.oslops.prs.prs4000.prs4000.web;

import java.util.List;
import java.util.Map;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import kr.opensoftlab.oslops.com.vo.LoginVO;
import kr.opensoftlab.oslops.prs.prs4000.prs4000.service.Prs4000Service;
import kr.opensoftlab.sdf.util.RequestConvertor;

import org.apache.log4j.Logger;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;

import egovframework.com.cmm.EgovMessageSource;

/**
 * @Class Name : Prs4000Controller.java
 * @Description : Prs4000Controller Controller class
 * @Modification Information
 *
 * @author 김정환
 * @since 2016.03.11
 * @version 1.0
 * @see
 *  
 *  Copyright (C)  All right reserved.
 */

@Controller
public class Prs4000Controller {
	/** 로그4j 로거 로딩 */
	protected Logger Log = Logger.getLogger(this.getClass());
	
	/** Dpl2000Service DI */
    @Resource(name = "prs4000Service")
    private Prs4000Service prs4000Service;
    
    /** EgovMessageSource */
	@Resource(name = "egovMessageSource")
	EgovMessageSource egovMessageSource;


	/**
	 * 일정관리 화면이동
	 * @return
	 * @throws Exception
	 */
    @RequestMapping(value="/prs/prs4000/prs4000/selectPrs4000View.do")
    public String selectPrs4000View() throws Exception {
    	return "/prs/prs4000/prs4000/prs4000";
    }
    
    
	/**
	 * 일정관리 내역을 조회한다.
	 * @param request
	 * @param response
	 * @param model
	 * @return
	 * @throws Exception
	 */
	@SuppressWarnings({ "rawtypes", "unchecked" })
	@RequestMapping(value="/prs/prs4000/prs4000/selectPrs4000ListAjax.do")
    public ModelAndView selectPrs4000ListAjax(HttpServletRequest request, HttpServletResponse response, ModelMap model ) throws Exception {
    	
    	try{
        	
    		//리퀘스트에서 넘어온 파라미터를 맵으로 세팅
        	Map paramMap = RequestConvertor.requestParamToMap(request);
        	
    		//로그인VO 가져오기
			HttpSession session = request.getSession();
			LoginVO loginVO = (LoginVO) session.getAttribute("loginVO");

        	paramMap.put("usrId", loginVO.getUsrId()); 			// 사용자 ID
        	
        	//라이선스 그룹에 할당된 메뉴목록 가져오기
        	List<Map> schdList = (List) prs4000Service.selectPrs4000List(paramMap);
        	
        	paramMap.put("schdList", schdList);
        	
        	//조회성공메시지 세팅
        	model.addAttribute("message", egovMessageSource.getMessage("success.common.select"));
        	
        	return new ModelAndView("jsonView", paramMap);
    	}
    	catch(Exception ex){
    		Log.error("selectPrs4000ListAjax()", ex);
    		
    		//조회실패 메시지 세팅
    		model.addAttribute("message", egovMessageSource.getMessage("fail.common.select"));
    		return new ModelAndView("jsonView");
    	}
    }
	
	/**
	 * 일정관리 등록 Ajax
	 * @param request
	 * @param response
	 * @param model
	 * @return
	 * @throws Exception
	 */
	@RequestMapping(value="/prs/prs4000/prs4000/insertPrs4000SchdInfoAjax.do")
    public ModelAndView insertPrs4000SchdInfoAjax(HttpServletRequest request, HttpServletResponse response, ModelMap model ) throws Exception {
    	
		try{
			// request 파라미터를 map으로 변환
        	Map<String, String> paramMap = RequestConvertor.requestParamToMap(request, true);
        	
    		// 로그인VO 가져오기
			HttpSession ss = request.getSession();
			LoginVO loginVO = (LoginVO) ss.getAttribute("loginVO");
			
        	paramMap.put("usrId", loginVO.getUsrId()); 			// 사용자 ID
        	paramMap.put("usrIp", request.getRemoteAddr()); // 사용자 IP
        	
        	String schdSeq = prs4000Service.insertPrs4000SchdInfo(paramMap);
        	
        	//등록 성공 메시지 세팅
        	model.addAttribute("schdSeq", schdSeq);
        	model.addAttribute("message", egovMessageSource.getMessage("success.common.insert"));
        	
        	return new ModelAndView("jsonView");
        	
		}catch(Exception e){
			Log.error("saveAdm2000UsrInfo()", e);
			
    		model.addAttribute("successYn", "N");
    		model.addAttribute("message", egovMessageSource.getMessage("fail.common.insert"));
    		
    		return new ModelAndView("jsonView");
		}
    }
	
	/**
	 * 일정관리 수정
	 * @param request
	 * @param response
	 * @param model
	 * @return
	 * @throws Exception
	 */
	@RequestMapping(value="/prs/prs4000/prs4000/updatePrs4000SchdInfoAjax.do")
    public ModelAndView updatePrs4000SchdInfoAjax(HttpServletRequest request, HttpServletResponse response, ModelMap model ) throws Exception {
    	
    	try{
        	
    		// request 파라미터를 map으로 변환
        	Map<String, String> paramMap = RequestConvertor.requestParamToMap(request, true);
        	
    		// 로그인VO 가져오기
			HttpSession ss = request.getSession();
			LoginVO loginVO = (LoginVO) ss.getAttribute("loginVO");
			
        	paramMap.put("usrId", loginVO.getUsrId()); 			// 사용자 ID
        	paramMap.put("usrIp", request.getRemoteAddr()); 	// 사용자 IP
			
        	// 메뉴 수정
        	prs4000Service.updatePrs4000SchdInfo(paramMap);
        	
        	//등록 성공 메시지 세팅
        	model.addAttribute("message", egovMessageSource.getMessage("success.common.update"));
        	
        	return new ModelAndView("jsonView");
    	}
    	catch(Exception ex){
    		Log.error("updatePrs4000SchdInfoAjax()", ex);

    		//수정 실패 메시지 세팅 및 저장 성공여부 세팅
    		model.addAttribute("message", egovMessageSource.getMessage("fail.common.update"));
    		return new ModelAndView("jsonView");
    	}
    }
	
	/**
	 * 일정관리를 삭제한다
	 * @param request
	 * @param response
	 * @param model
	 * @return
	 * @throws Exception
	 */
	@RequestMapping(value="/prs/prs4000/prs4000/deletePrs4000SchdInfoAjax.do")
    public ModelAndView deletePrs4000SchdInfoAjax(HttpServletRequest request, HttpServletResponse response, ModelMap model ) throws Exception {
    	try{
    		// request 파라미터를 map으로 변환
        	Map<String, String> paramMap = RequestConvertor.requestParamToMap(request, true);
        	
    		// 로그인VO 가져오기
			HttpSession ss = request.getSession();
			LoginVO loginVO = (LoginVO) ss.getAttribute("loginVO");
			
			paramMap.put("usrId", loginVO.getUsrId());
			
        	// 일정정관리 삭제
        	prs4000Service.deletePrs4000SchdInfo(paramMap);
        	model.addAttribute("message", egovMessageSource.getMessage("success.common.insert"));
        	
        	return new ModelAndView("jsonView");
    	}
    	catch(Exception ex){
    		Log.error("deletePrs4000SchdInfoAjax()", ex);
    		
    		model.addAttribute("message", egovMessageSource.getMessage("fail.common.delete"));
    		return new ModelAndView("jsonView");
    	}
    }
	
	

}
