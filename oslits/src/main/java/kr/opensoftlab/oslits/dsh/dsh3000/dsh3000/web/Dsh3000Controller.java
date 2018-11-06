package kr.opensoftlab.oslits.dsh.dsh3000.dsh3000.web;

import java.util.List;
import java.util.Map;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import kr.opensoftlab.oslits.com.fms.web.service.FileMngService;
import kr.opensoftlab.oslits.com.vo.LoginVO;
import kr.opensoftlab.oslits.dsh.dsh1000.dsh1000.service.Dsh1000Service;
import kr.opensoftlab.oslits.prj.prj1000.prj1000.service.Prj1000Service;
import kr.opensoftlab.oslits.prj.prj1000.prj1100.service.Prj1100Service;

import kr.opensoftlab.oslits.req.req1000.req1000.service.Req1000Service;
import kr.opensoftlab.oslits.req.req1000.req1000.vo.Req1000VO;
import kr.opensoftlab.oslits.req.req4000.req4100.service.Req4100Service;
import kr.opensoftlab.oslits.req.req4000.req4400.service.Req4400Service;
import kr.opensoftlab.sdf.util.RequestConvertor;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;

import com.google.gson.GsonBuilder;

import egovframework.com.cmm.EgovMessageSource;
import egovframework.rte.fdl.cmmn.trace.LeaveaTrace;
import egovframework.rte.fdl.property.EgovPropertyService;

/**
 * @Class Name : Dsh3000Controller.java
 * @Description : Dsh3000Controller Controller class
 * @Modification Information
 *
 * @author 진주영
 * @since 2018.09.19.
 * @version 1.0
 * @see
 *  
 *  Copyright (C) OpenSoftLab Corp All right reserved.
 */

@Controller
public class Dsh3000Controller {

	/**
     * Logging 을 위한 선언
     * Log는 반드시 가이드에 맞춰서 작성한다. 개발용으로는 debug 카테고리를 사용한다.
     */
	protected Logger Log = Logger.getLogger(this.getClass());
	
	/** Dsh1000Service DI */
    @Resource(name = "dsh1000Service")
    private Dsh1000Service dsh1000Service;

    /** Req1000Service DI */
	@Resource(name = "req1000Service")
	private Req1000Service req1000Service;
	
    /** Req4100Service DI */
	@Resource(name = "req4100Service")
	private Req4100Service req4100Service;
	
	/** Req4400Service DI */
	@Resource(name = "req4400Service")
	private Req4400Service req4400Service;
	
	/** Prj1000Service DI */
    @Resource(name = "prj1000Service")
    private Prj1000Service prj1000Service;
    


	/** Prj1100Service DI */
    @Resource(name = "prj1100Service")
    private Prj1100Service prj1100Service;
    
    /** EgovMessageSource */
	@Resource(name = "egovMessageSource")
	EgovMessageSource egovMessageSource;

	/** EgovPropertyService */
	@Resource(name = "propertiesService")
	protected EgovPropertyService propertiesService;

	/** FileMngService */
   	@Resource(name="fileMngService")
   	private FileMngService fileMngService;
	
	@Value("${Globals.fileStorePath}")
	private String tempPath;
	
	/** TRACE */
	@Resource(name = "leaveaTrace")
	LeaveaTrace leaveaTrace;
	/**
	 * Dsh3000 화면 이동
	 * @param 
	 * @return 
	 * @exception Exception
	 */
	@SuppressWarnings({ "rawtypes", "unchecked" })
	@RequestMapping(value="/dsh/dsh3000/dsh3000/selectDsh3000View.do")
	public String selectDsh3000View(HttpServletRequest request, HttpServletResponse response, ModelMap model ) throws Exception {
		
		try{
			// request 파라미터를 map으로 변환
			Map<String, String> paramMap = RequestConvertor.requestParamToMapAddSelInfo(request, true);
			//세션
			HttpSession ss = request.getSession();
			
			//로그인VO 가져오기
    		LoginVO loginVO = (LoginVO) ss.getAttribute("loginVO");
    		paramMap.put("usrId", loginVO.getUsrId());
    		
			List<Map> prjList = (List)prj1000Service.selectPrj1000View(paramMap);
			String prjListJson	= (new GsonBuilder().serializeNulls().create()).toJsonTree(prjList).toString();
			
			
			model.addAttribute("prjList", prjListJson);
			return "/dsh/dsh3000/dsh3000/dsh3000";
		}
		catch(Exception ex){
			Log.error("selectDsh3000View()", ex);
			throw new Exception(ex.getMessage());
		}
	}
	
	/**
	 * Dsh3000 대시보드 데이터 조회 Ajax
	 * @param 
	 * @return 
	 * @exception Exception
	 */
	@SuppressWarnings({ "rawtypes", "unchecked" })
	@RequestMapping(value="/dsh/dsh3000/dsh3000/selectDsh3000DashBoardDataAjax.do")
	public ModelAndView selectDsh3000DashBoardDataAjax(HttpServletRequest request, HttpServletResponse response, ModelMap model ) throws Exception {

		try{

			// request 파라미터를 map으로 변환
			Map<String, String> paramMap = RequestConvertor.requestParamToMapAddSelInfo(request, true);

			//프로젝트 ID 가져오기
    		HttpSession ss = request.getSession();
    		
    		String prjId = (String) paramMap.get("prjId");
    		
    		
    		LoginVO loginVO = (LoginVO) ss.getAttribute("loginVO");
    		
    		String usrId = (String) loginVO.getUsrId();
    		paramMap.put("usrId", usrId);
    		
    		//[차트1] 프로세스별 총갯수 + 최종 완료 갯수
    		List<Map> processReqCnt = dsh1000Service.selectDsh1000ProcessReqCntList(paramMap);
    		
    		//[차트2] 월별 프로세스별 요구사항 갯수
    		List<Map> monthProcessReqCnt = dsh1000Service.selectDsh1000MonthProcessReqCntList(paramMap);
    		
    		//프로세스 목록
			List<Map> processList = prj1100Service.selectFlw1000ProcessList(paramMap);
			
			//작업흐름 목록
			List<Map> flowList = prj1100Service.selectFlw1100FlowList(paramMap);
			
			Req1000VO req1000Vo = new Req1000VO();
			req1000Vo.setPrjId(prjId);
			req1000Vo.setLoginUsrId(usrId);
			req1000Vo.setReqProType("01"); //접수 요청중 요구사항만
			
			//해당 프로젝트 접수 대기 목록
			List<Req1000VO> newReqList = req1000Service.selectReq1000AllList(req1000Vo);
			
			model.addAttribute("processReqCnt", processReqCnt);
			model.addAttribute("monthProcessReqCnt", monthProcessReqCnt);
			model.addAttribute("processList", processList);
			model.addAttribute("flowList", flowList);
			model.addAttribute("newReqList", newReqList);
			
			//등록 성공 메시지 세팅
			model.addAttribute("errorYN", "N");
			model.addAttribute("message", egovMessageSource.getMessage("success.common.select"));

			return new ModelAndView("jsonView");
		}
		catch(Exception ex){
			Log.error("selectDsh3000DashBoardDataAjax()", ex);

			//조회실패 메시지 세팅 및 저장 성공여부 세팅
			model.addAttribute("errorYN", "Y");
			model.addAttribute("message", egovMessageSource.getMessage("fail.common.select"));
			return new ModelAndView("jsonView");
		}
	}
	
	/**
	 * Dsh3000 대시보드 부분 데이터 조회 Ajax
	 * @param 
	 * @return 
	 * @exception Exception
	 */
	@SuppressWarnings({ "rawtypes", "unchecked" })
	@RequestMapping(value="/dsh/dsh3000/dsh3000/selectDsh3000DashBoardSubDataAjax.do")
	public ModelAndView selectDsh3000DashBoardSubDataAjax(HttpServletRequest request, HttpServletResponse response, ModelMap model ) throws Exception {

		try{

			// request 파라미터를 map으로 변환
			Map<String, String> paramMap = RequestConvertor.requestParamToMapAddSelInfo(request, true);

			//프로젝트 ID 가져오기
    		HttpSession ss = request.getSession();
    		
    		String prjId = (String) paramMap.get("prjId");
    		LoginVO loginVO = (LoginVO) ss.getAttribute("loginVO");
    		
    		String usrId = (String) loginVO.getUsrId();
    		paramMap.put("usrId", usrId);

    		//부분 타입
    		String redoId = (String) paramMap.get("redoId");
    		
    		if(redoId == null){
    			//조회실패 메시지 세팅 및 저장 성공여부 세팅
    			model.addAttribute("errorYN", "Y");
    			model.addAttribute("message", egovMessageSource.getMessage("fail.common.select"));
    			return new ModelAndView("jsonView");
    		}
    		
    		//차트
    		if("chart".equals(redoId)){
    			//[차트1] 프로세스별 총갯수 + 최종 완료 갯수
        		List<Map> processReqCnt = dsh1000Service.selectDsh1000ProcessReqCntList(paramMap);
        		
        		//[차트2] 월별 프로세스별 요구사항 갯수
        		List<Map> monthProcessReqCnt = dsh1000Service.selectDsh1000MonthProcessReqCntList(paramMap);
        		
        		model.addAttribute("processReqCnt", processReqCnt);
        		model.addAttribute("monthProcessReqCnt", monthProcessReqCnt);
    		}
    		//접수
    		else if("request".equals(redoId)){
    			Req1000VO req1000Vo = new Req1000VO();
    			req1000Vo.setPrjId(prjId);
    			req1000Vo.setLoginUsrId(usrId);
    			req1000Vo.setReqProType("01"); //접수 요청중 요구사항만
    			
    			//해당 프로젝트 접수 대기 목록
    			List<Req1000VO> newReqList = req1000Service.selectReq1000AllList(req1000Vo);
    			
    			model.addAttribute("newReqList", newReqList);
    		}
    		//프로세스
    		else if("process".equals(redoId)){
    			//작업흐름 목록
    			List<Map> flowList = prj1100Service.selectFlw1100FlowList(paramMap);
    			
    			model.addAttribute("flowList", flowList);
    		}

			//등록 성공 메시지 세팅
			model.addAttribute("errorYN", "N");
			model.addAttribute("message", egovMessageSource.getMessage("success.common.select"));

			return new ModelAndView("jsonView");
		}
		catch(Exception ex){
			Log.error("selectDsh3000DashBoardSubDataAjax()", ex);

			//조회실패 메시지 세팅 및 저장 성공여부 세팅
			model.addAttribute("errorYN", "Y");
			model.addAttribute("message", egovMessageSource.getMessage("fail.common.select"));
			return new ModelAndView("jsonView");
		}
	}
	
	/**
	 * Dsh3000 프로세스, 작업흐름 별 요구사항 목록
	 * @param 
	 * @return 
	 * @exception Exception
	 */
	@SuppressWarnings({ "rawtypes", "unchecked" })
	@RequestMapping(value="/dsh/dsh3000/dsh3000/selectDsh3000ProFlowRequestAjax.do")
	public ModelAndView selectDsh3000ProFlowRequestAjax(HttpServletRequest request, HttpServletResponse response, ModelMap model ) throws Exception {

		try{

			// request 파라미터를 map으로 변환
			Map<String, String> paramMap = RequestConvertor.requestParamToMapAddSelInfo(request, true);

			//프로젝트 ID 가져오기
    		HttpSession ss = request.getSession();
    		
    		String prjId = (String) paramMap.get("prjId");
    		LoginVO loginVO = (LoginVO) ss.getAttribute("loginVO");
    		
    		String usrId = (String) loginVO.getUsrId();
    		paramMap.put("usrId", usrId);
    		
    		List<Map> proFlowReqList = req4100Service.selectReq4100ProcessFlowReqList(paramMap);
    		
			model.addAttribute("proFlowReqList", proFlowReqList);
			//등록 성공 메시지 세팅
			model.addAttribute("errorYN", "N");
			model.addAttribute("message", egovMessageSource.getMessage("success.common.select"));

			return new ModelAndView("jsonView");
		}
		catch(Exception ex){
			Log.error("selectDsh3000ProFlowRequestAjax()", ex);

			//조회실패 메시지 세팅 및 저장 성공여부 세팅
			model.addAttribute("errorYN", "Y");
			model.addAttribute("message", egovMessageSource.getMessage("fail.common.select"));
			return new ModelAndView("jsonView");
		}
	}
}
