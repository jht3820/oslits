package kr.opensoftlab.oslits.req.req4600.req4600.web;

 import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import kr.opensoftlab.oslits.adm.adm6000.adm6000.service.Adm6000Service;
import kr.opensoftlab.oslits.com.vo.LoginVO;
import kr.opensoftlab.oslits.prj.prj1000.prj1000.service.Prj1000Service;

import kr.opensoftlab.oslits.req.req1000.req1000.vo.Req1000VO;
import kr.opensoftlab.oslits.req.req4000.req4100.service.Req4100Service;
import kr.opensoftlab.oslits.req.req4600.req4600.service.Req4600Service;
import kr.opensoftlab.oslits.req.req4600.req4600.vo.Req4600VO;
import kr.opensoftlab.sdf.util.RequestConvertor;

import org.apache.log4j.Logger;
import org.json.simple.JSONArray;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.multipart.MultipartHttpServletRequest;
import org.springframework.web.servlet.ModelAndView;

import egovframework.com.cmm.EgovMessageSource;
import egovframework.com.cmm.service.FileVO;
import egovframework.rte.fdl.cmmn.trace.LeaveaTrace;
import egovframework.rte.fdl.property.EgovPropertyService;

/**
 * @Class Name : Req4600Controller.java
 * @Description : Req4600Controller Controller class
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
public class Req4600Controller {

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
	
	@Resource(name = "req4600Service")
	private Req4600Service req4600Service;
	
	
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
	
	
	/**
	 * Req4600 화면 이동
	 * @param 
	 * @return 
	 * @exception Exception
	 */
	@RequestMapping(value="/req/req4000/req4600/selectReq4600View.do")
	public String selectReq4600ListView(HttpServletRequest request, HttpServletResponse response, ModelMap model ) throws Exception {
		try{
			//	request 파라미터를 map으로 변환
			Map<String, String> paramMap = RequestConvertor.requestParamToMapAddSelInfo(request, true);
			paramMap.put("prjId", request.getSession().getAttribute("selPrjId").toString());

			return "/req/req4000/req4600/req4600";
		}catch(Exception ex){
    		Log.error("selectReq4600View()", ex);
    		throw new Exception(ex.getMessage());
		}
	}
	
	/**
	 * Req4600 화면 이동 (이동시 요구사항 목록 조회)
	 * @param 
	 * @return 
	 * @exception Exception
	 */
	@RequestMapping(value="/req/req4000/req4600/selectReq4600ListAjaxView.do")
	public ModelAndView selectReq4600ListAjaxView(HttpServletRequest request, HttpServletResponse response, ModelMap model ) throws Exception {

		try{
			// request 파라미터를 map으로 변환
			Map<String, String> paramMap = RequestConvertor.requestParamToMapAddSelInfo(request, true);
			
			List<Req1000VO> reqList = null;
			List<Map> admList = null;
			
			//요구사항 목록 조회
			reqList = req4600Service.selectReq4600ReqWbsListAjax(paramMap);
					
			model.addAttribute("list", reqList);
		
			
			return new ModelAndView("jsonView");
		}
		catch(Exception ex){
			Log.error("selectReq4600ListView()", ex);
			model.addAttribute("message", egovMessageSource.getMessage("fail.common.select"));
			throw new Exception(ex.getMessage());
		}
	}

	@SuppressWarnings("rawtypes")
	@RequestMapping(value="/req/req4000/req4600/updateReq4600ProgresInfo.do")
	public ModelAndView updateReq4600ProgresInfo(HttpServletRequest request, ModelMap model ) throws Exception {

		Map<String, String> paramMap = RequestConvertor.requestParamToMap(request, true);
    	
    	HttpSession ss = request.getSession();
		LoginVO loginVO = (LoginVO) ss.getAttribute("loginVO");
		
		String reqIds[] = request.getParameterValues("reqId");
		String prjIds[] = request.getParameterValues("prjId");
		String progresss[] = request.getParameterValues("progress");
		Req4600VO req4600VO  = null;
		List<Req4600VO> list = new ArrayList<Req4600VO>();
		try{
			
			// 요구사항 단건정보 조회
			if(reqIds!=null){
				for (int i = 0; i < reqIds.length; i++) {
					req4600VO = new Req4600VO();
					
					req4600VO.setReqId(reqIds[i]);
					req4600VO.setPrjId(prjIds[i]);
					req4600VO.setProgress(progresss[i]);
					
					req4600VO.setRegUsrId(paramMap.get("regUsrId"));
					req4600VO.setRegUsrIp(paramMap.get("regUsrIp"));
	        		
					req4600VO.setLicGrpId(loginVO.getLicGrpId());
					list.add(req4600VO);
					
				}			

				req4600Service.updateReq4600ProgresInfo(list);
			}
												
			//등록 성공 메시지 세팅
			model.addAttribute("message", egovMessageSource.getMessage("success.common.update"));

			return new ModelAndView("jsonView");
		}
		catch(Exception ex){
			Log.error("updateReq4600ProgresInfo()", ex);

			//조회실패 메시지 세팅 및 저장 성공여부 세팅
			model.addAttribute("saveYN", "N");
			model.addAttribute("message", egovMessageSource.getMessage("fail.common.update"));
			return new ModelAndView("jsonView");
		}
	}

}
