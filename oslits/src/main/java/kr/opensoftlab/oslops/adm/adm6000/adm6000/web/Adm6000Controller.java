package kr.opensoftlab.oslops.adm.adm6000.adm6000.web;

import java.util.List;
import java.util.Map;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import kr.opensoftlab.oslops.com.vo.LoginVO;
import kr.opensoftlab.oslops.adm.adm6000.adm6000.service.Adm6000Service;
import kr.opensoftlab.sdf.util.RequestConvertor;

import org.apache.log4j.Logger;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;

import egovframework.com.cmm.EgovMessageSource;

/**
 * @Class Name : Adm6000Controller.java
 * @Description : Adm6000Controller Controller class
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
public class Adm6000Controller {
	/** 로그4j 로거 로딩 */
	protected Logger Log = Logger.getLogger(this.getClass());
	
	/** Dpl2000Service DI */
    @Resource(name = "adm6000Service")
    private Adm6000Service adm6000Service;
    
    /** EgovMessageSource */
	@Resource(name = "egovMessageSource")
	EgovMessageSource egovMessageSource;



    
	/**
	 * 휴일관리 내역을 조회한다.
	 * @param request
	 * @param response
	 * @param model
	 * @return
	 * @throws Exception
	 */
	@SuppressWarnings({ "rawtypes", "unchecked" })
	@RequestMapping(value="/adm/adm6000/adm6000/selectAdm6000ListAjax.do")
    public ModelAndView selectAdm6000ListAjax(HttpServletRequest request, HttpServletResponse response, ModelMap model ) throws Exception {
    	
    	try{
        	
    		//리퀘스트에서 넘어온 파라미터를 맵으로 세팅
        	Map paramMap = RequestConvertor.requestParamToMap(request);
        	
    		//로그인VO 가져오기
			HttpSession session = request.getSession();
			LoginVO loginVO = (LoginVO) session.getAttribute("loginVO");
			
        	paramMap.put("usrId", loginVO.getUsrId()); 			// 사용자 ID
        	paramMap.put("licGrpId", loginVO.getLicGrpId());
        	
        	
        	// 라이선스 그룹에 할당된 휴일 내역 가져온다.
        	List<Map> holiList = (List) adm6000Service.selectAdm6000List(paramMap);
        	
        	
        	paramMap.put("holiList", holiList);
        	
        	//조회성공메시지 세팅
        	model.addAttribute("message", egovMessageSource.getMessage("success.common.select"));
        	
        	return new ModelAndView("jsonView", paramMap);
    	}
    	catch(Exception ex){
    		Log.error("selectAdm6000ListAjax()", ex);
    		
    		//조회실패 메시지 세팅
    		model.addAttribute("message", egovMessageSource.getMessage("fail.common.select"));
    		return new ModelAndView("jsonView");
    	}
    }
	
	/**
	 * 휴일관리 단건 등록 Ajax
	 * @param request
	 * @param response
	 * @param model
	 * @return
	 * @throws Exception
	 */
	@SuppressWarnings({ "unchecked", "unused", "rawtypes" })
	@RequestMapping(value="/adm/adm6000/adm6000/insertAdm6000HoliInfoAjax.do")
    public ModelAndView insertAdm6000HoliInfoAjax(HttpServletRequest request, HttpServletResponse response, ModelMap model ) throws Exception {
    	
		try{
			// request 파라미터를 map으로 변환
        	Map<String, String> paramMap = RequestConvertor.requestParamToMap(request, true);
        	
    		// 로그인VO 가져오기
			HttpSession ss = request.getSession();
			LoginVO loginVO = (LoginVO) ss.getAttribute("loginVO");
			
        	paramMap.put("usrId", loginVO.getUsrId()); 			// 사용자 ID
        	paramMap.put("usrIp", request.getRemoteAddr());	// 사용자 IP
        	paramMap.put("licGrpId", loginVO.getLicGrpId());	// 라이센서 ID
        	
        	String holiSeq = adm6000Service.insertAdm6000HoliInfo(paramMap);
        	
        	//등록 성공 메시지 세팅
        	model.addAttribute("message", egovMessageSource.getMessage("success.common.insert"));
        	
        	return new ModelAndView("jsonView");
        	
		}catch(Exception e){
			Log.error("insertAdm6000HoliInfoAjax()", e);
			
    		model.addAttribute("successYn", "N");
    		model.addAttribute("message", egovMessageSource.getMessage("fail.common.insert"));
    		
    		return new ModelAndView("jsonView");
		}
    }
	
	
	/**
	 * 휴일관리 일괄 등록 Ajax
	 * @param request
	 * @param response
	 * @param model
	 * @return
	 * @throws Exception
	 */
	@RequestMapping(value="/adm/adm6000/adm6000/insertAdm6000HoliListAjax.do")
    public ModelAndView insertAdm6000HoliListAjax(HttpServletRequest request, HttpServletResponse response, ModelMap model ) throws Exception {
    	
		try{
			// request 파라미터를 map으로 변환
        	Map<String, String> paramMap = RequestConvertor.requestParamToMap(request, true);
        	
    		// 로그인VO 가져오기
			HttpSession ss = request.getSession();
			LoginVO loginVO = (LoginVO) ss.getAttribute("loginVO");
			
        	paramMap.put("usrId", loginVO.getUsrId()); 			// 사용자 ID
        	paramMap.put("usrIp", request.getRemoteAddr()); // 사용자 IP
        	
        	adm6000Service.insertAdm6000HoliList(paramMap);
        	
        	//등록 성공 메시지 세팅
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
	 * 휴일관리 수정
	 * @param request
	 * @param response
	 * @param model
	 * @return
	 * @throws Exception
	 */
	@RequestMapping(value="/adm/adm6000/adm6000/updateAdm6000HoliInfoAjax.do")
    public ModelAndView updateAdm6000HoliInfoAjax(HttpServletRequest request, HttpServletResponse response, ModelMap model ) throws Exception {
    	
    	try{
        	
    		// request 파라미터를 map으로 변환
        	Map<String, String> paramMap = RequestConvertor.requestParamToMap(request, true);
        	
    		// 로그인VO 가져오기
			HttpSession ss = request.getSession();
			LoginVO loginVO = (LoginVO) ss.getAttribute("loginVO");
			
        	paramMap.put("usrId", loginVO.getUsrId()); 			// 사용자 ID
        	paramMap.put("usrIp", request.getRemoteAddr()); 	// 사용자 IP
        	paramMap.put("licGrpId", loginVO.getLicGrpId());	// 라이센서 ID
        	
        	// 메뉴 수정
        	adm6000Service.updateAdm6000HoliInfo(paramMap);
        	
        	//등록 성공 메시지 세팅
        	model.addAttribute("message", egovMessageSource.getMessage("success.common.update"));
        	
        	return new ModelAndView("jsonView");
    	}
    	catch(Exception ex){
    		Log.error("updateAdm6000HoliInfoAjax()", ex);

    		//수정 실패 메시지 세팅 및 저장 성공여부 세팅
    		model.addAttribute("message", egovMessageSource.getMessage("fail.common.update"));
    		return new ModelAndView("jsonView");
    	}
    }
	
	/**
	 * 휴일관리를 삭제한다 ( 단건 )
	 * @param request
	 * @param response
	 * @param model
	 * @return
	 * @throws Exception
	 */
	@RequestMapping(value="/adm/adm6000/adm6000/deleteAdm6000HoliInfoAjax.do")
    public ModelAndView deleteAdm6000HoliInfoAjax(HttpServletRequest request, HttpServletResponse response, ModelMap model ) throws Exception {
    	try{
    		// request 파라미터를 map으로 변환
        	Map<String, String> paramMap = RequestConvertor.requestParamToMap(request, true);
        	
    		// 로그인VO 가져오기
			HttpSession ss = request.getSession();
			LoginVO loginVO = (LoginVO) ss.getAttribute("loginVO");
			
			paramMap.put("usrId", loginVO.getUsrId());
			
        	// 휴일정관리 삭제
        	adm6000Service.deleteAdm6000HoliInfo(paramMap);
        	model.addAttribute("message", egovMessageSource.getMessage("success.common.insert"));
        	
        	return new ModelAndView("jsonView");
    	}
    	catch(Exception ex){
    		Log.error("deleteAdm6000HoliInfoAjax()", ex);
    		
    		model.addAttribute("message", egovMessageSource.getMessage("fail.common.delete"));
    		return new ModelAndView("jsonView");
    	}
    }
	/**
	 * 할당 API 토큰 목록 화면으로 이동
	 * @param request
	 * @param response
	 * @param model
	 * @return
	 * @throws Exception
	 */
	@RequestMapping(value="/adm/adm6000/adm6000/selectAdm6000View.do")
	public String selectAdm6000View( HttpServletRequest request, HttpServletResponse response, ModelMap model ) throws Exception {
		return "/adm/adm6000/adm6000/adm6000";
	}
	

}
