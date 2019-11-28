package kr.opensoftlab.oslops.prj.prj1000.prj1100.web;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.apache.log4j.Logger;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;

import egovframework.com.cmm.EgovMessageSource;
import egovframework.rte.fdl.cmmn.trace.LeaveaTrace;
import egovframework.rte.fdl.property.EgovPropertyService;
import egovframework.rte.ptl.mvc.tags.ui.pagination.PaginationInfo;
import kr.opensoftlab.oslops.adm.adm4000.adm4000.service.Adm4000Service;
import kr.opensoftlab.oslops.com.vo.LoginVO;
import kr.opensoftlab.oslops.prj.prj1000.prj1100.service.Prj1100Service;
import kr.opensoftlab.oslops.prj.prj1000.prj1100.vo.Prj1100VO;
import kr.opensoftlab.sdf.util.ModuleUseCheck;
import kr.opensoftlab.sdf.util.PagingUtil;
import kr.opensoftlab.sdf.util.RequestConvertor;


/**
 * @Class Name : Prj1100Controller.java
 * @Description : Prj1100Controller Controller class
 * @Modification Information
 *
 * @author 진주영
 * @since 2018.07.18.
 * @version 1.0
 * @see
 *  
 *  Copyright (C) OpenSoftLab Corp All right reserved.
 */
@Controller
public class Prj1100Controller {
	/** 로그4j 로거 로딩 */
	private static final Logger Log = Logger.getLogger(Prj1100Controller.class);
    
    /** Adm4000Service DI */
    @Resource(name = "adm4000Service")
    private Adm4000Service adm4000Service;
    
	/** Prj1100Service DI */
    @Resource(name = "prj1100Service")
    private Prj1100Service prj1100Service;
    
    /** EgovMessageSource */
	@Resource(name = "egovMessageSource")
	EgovMessageSource egovMessageSource;

	/** EgovPropertyService */
	@Resource(name = "propertiesService")
	protected EgovPropertyService propertiesService;

	/** ModuleUseCheck DI */
	@Resource(name = "moduleUseCheck")
	private ModuleUseCheck moduleUseCheck;
	
	/** TRACE */
	@Resource(name = "leaveaTrace")
	LeaveaTrace leaveaTrace;
	
	/**
	 * Prj1100 프로세스 및 작업흐름 관리 화면
	 * @param 
	 * @return 
	 * @exception Exception
	 */
	@RequestMapping(value="/prj/prj1000/prj1100/selectPrj1100View.do")
    public String selectPrj1100View(HttpServletRequest request, HttpServletResponse response, ModelMap model ) throws Exception {
		try {
			//배포 모듈 체크
			boolean jenkinsModuleUseChk = moduleUseCheck.isJenkinsModuleUseChk();
			//svnkit 모듈 체크
			boolean svnModuleUseChk = moduleUseCheck.isSvnKitModuleUseChk();
			
			model.addAttribute("jenkinsModuleUseChk",jenkinsModuleUseChk);
			model.addAttribute("svnModuleUseChk",svnModuleUseChk);
		}catch(Exception e) {
			Log.error("selectPrj1100View()", e);

			//조회실패 메시지 세팅 및 저장 성공여부 세팅
			model.addAttribute("errorYN", "Y");
			model.addAttribute("message", egovMessageSource.getMessage("fail.common.select"));
			return "/err/error";
		}
		return "/prj/prj1000/prj1100/prj1100";
    }
	
	/**
	 * Prj1101 작업흐름 추가 항목 관리 팝업
	 * @param 
	 * @return 
	 * @exception Exception
	 */
	@SuppressWarnings("rawtypes")
	@RequestMapping(value="/prj/prj1000/prj1100/selectPrj1101View.do")
    public String selectPrj1101View(HttpServletRequest request, HttpServletResponse response, ModelMap model ) throws Exception {
		try{
			// request 파라미터를 map으로 변환
			Map<String, String> paramMap = RequestConvertor.requestParamToMapAddSelInfo(request, true);
			
			String processId = paramMap.get("processId");
			String flowId = paramMap.get("flowId");

			//프로젝트 ID 가져오기
    		HttpSession ss = request.getSession();
    		paramMap.put("prjId", (String) ss.getAttribute("selPrjId"));
    		
			//해당 프로세스 정보 조회
			Map processInfo = (Map) prj1100Service.selectFlw1000ProcessInfo(paramMap);
			
			String processConfirmCd = (String) processInfo.get("processConfirmCd");
			model.addAttribute("processConfirmCd",processConfirmCd);
			
			model.addAttribute("processId",processId);
			model.addAttribute("flowId",flowId);
		}catch(Exception ex){
			Log.error("selectPrj1101View()", ex);

			//조회실패 메시지 세팅 및 저장 성공여부 세팅
			model.addAttribute("errorYN", "Y");
			model.addAttribute("message", egovMessageSource.getMessage("fail.common.select"));
			return "/err/error";
		}
		return "/prj/prj1000/prj1100/prj1101";
    }
	
	/**
	 * Prj1102 작업흐름 추가&수정 팝업
	 * @param 
	 * @return 
	 * @exception Exception
	 */
	@RequestMapping(value="/prj/prj1000/prj1100/selectPrj1102View.do")
    public String selectPrj1102View(HttpServletRequest request, HttpServletResponse response, ModelMap model ) throws Exception {
		try{
			// request 파라미터를 map으로 변환
			Map<String, String> paramMap = RequestConvertor.requestParamToMapAddSelInfo(request, true);
			
			String processId = paramMap.get("processId");
			String flowId = paramMap.get("flowId");
			String type = paramMap.get("type");
			
			//배포 모듈 체크
			boolean jenkinsModuleUseChk = moduleUseCheck.isJenkinsModuleUseChk();
			//svnkit 모듈 체크
			boolean svnModuleUseChk = moduleUseCheck.isSvnKitModuleUseChk();
			
			model.addAttribute("processId",processId);
			model.addAttribute("flowId",flowId);
			model.addAttribute("type",type);
			model.addAttribute("jenkinsModuleUseChk",jenkinsModuleUseChk);
			model.addAttribute("svnModuleUseChk",svnModuleUseChk);
			
			
		}catch(Exception ex){
			Log.error("selectPrj1102View()", ex);

			//조회실패 메시지 세팅 및 저장 성공여부 세팅
			model.addAttribute("errorYN", "Y");
			model.addAttribute("message", egovMessageSource.getMessage("fail.common.select"));
			return "/err/error";
		}
		return "/prj/prj1000/prj1100/prj1102";
    }
	
	/**
	 * Prj1103 작업흐름 추가항목 추가&수정 팝업
	 * @param 
	 * @return 
	 * @exception Exception
	 */
	@RequestMapping(value="/prj/prj1000/prj1100/selectPrj1103View.do")
    public String selectPrj1103View(HttpServletRequest request, HttpServletResponse response, ModelMap model ) throws Exception {
		try{
			// request 파라미터를 map으로 변환
			Map<String, String> paramMap = RequestConvertor.requestParamToMapAddSelInfo(request, true);
			
			String processId = paramMap.get("processId");
			String flowId = paramMap.get("flowId");
			String type = paramMap.get("type");
			
			model.addAttribute("processId",processId);
			model.addAttribute("flowId",flowId);
			model.addAttribute("type",type);
		}catch(Exception ex){
			Log.error("selectPrj1103View()", ex);

			//조회실패 메시지 세팅 및 저장 성공여부 세팅
			model.addAttribute("errorYN", "Y");
			model.addAttribute("message", egovMessageSource.getMessage("fail.common.select"));
			return "/err/error";
		}
		return "/prj/prj1000/prj1100/prj1103";
    }
	
	/**
	 * Prj1104 프로세스 복사 팝업
	 * @param 
	 * @return 
	 * @exception Exception
	 */
	@RequestMapping(value="/prj/prj1000/prj1100/selectPrj1104View.do")
	public String selectPrj1104View(HttpServletRequest request, HttpServletResponse response, ModelMap model ) throws Exception {
		return "/prj/prj1000/prj1100/prj1104";
	}
	

	/**
	 * Prj1105 프로세스 추가 팝업
	 * @param 
	 * @return 
	 * @exception Exception
	 */
	@SuppressWarnings("rawtypes")
	@RequestMapping(value="/prj/prj1000/prj1100/selectPrj1105View.do")
	public String selectPrj1105View(HttpServletRequest request, HttpServletResponse response, ModelMap model ) throws Exception {
		try{
			// request 파라미터를 map으로 변환
			Map<String, String> paramMap = RequestConvertor.requestParamToMapAddSelInfo(request, true);
			
			//프로젝트 ID 가져오기
    		HttpSession ss = request.getSession();
    		paramMap.put("prjId", (String) ss.getAttribute("selPrjId"));
    		
			//type
			String popupGb = (String)paramMap.get("popupGb");
			
			//수정인경우 프로세스 정보 조회
			if("update".equals(popupGb)) {
				Map processInfo = (Map)prj1100Service.selectFlw1000ProcessInfo(paramMap);
				model.addAttribute("processInfo", processInfo);
			}
			
		}catch(Exception ex){
			Log.error("selectPrj1105View()", ex);
			
			//조회실패 메시지 세팅 및 저장 성공여부 세팅
			model.addAttribute("errorYN", "Y");
			model.addAttribute("message", egovMessageSource.getMessage("fail.common.select"));
			return "/err/error";
		}
		return "/prj/prj1000/prj1100/prj1105";
	}
	
	/**
	 * [DB]Flw1100 프로세스 목록 조회 Ajax
	 * @param 
	 * @return 
	 * @exception Exception
	 */
	@SuppressWarnings({ "rawtypes", "unchecked" })
	@RequestMapping(value="/prj/prj1000/prj1100/selectPrj1100ProcessListAjax.do")
	public ModelAndView selectPrj1100ProcessListAjax(HttpServletRequest request, HttpServletResponse response, ModelMap model ) throws Exception {

		try{
			// request 파라미터를 map으로 변환
			Map<String, String> paramMap = RequestConvertor.requestParamToMapAddSelInfo(request, true);
			
			//프로젝트 ID 가져오기
    		HttpSession ss = request.getSession();
    		paramMap.put("prjId", (String) ss.getAttribute("selPrjId"));
    		
			List<Map> processList = prj1100Service.selectFlw1000ProcessList(paramMap);
			
			model.addAttribute("processList", processList);
        	
			//성공 메시지 세팅
			model.addAttribute("errorYN", "N");
			model.addAttribute("message", egovMessageSource.getMessage("success.common.select"));

			return new ModelAndView("jsonView");
		}
		catch(Exception ex){
			Log.error("selectPrj1100ProcessListAjax()", ex);

			//조회실패 메시지 세팅 및 저장 성공여부 세팅
			model.addAttribute("errorYN", "Y");
			model.addAttribute("message", egovMessageSource.getMessage("fail.common.select"));
			return new ModelAndView("jsonView");
		}
	}
	
	/**
	 * [DB]Flw1100 프로세스 저장 Ajax
	 * @param 
	 * @return 
	 * @exception Exception
	 */
	@RequestMapping(value="/prj/prj1000/prj1100/insertPrj1100ProcessInfoAjax.do")
	public ModelAndView insertPrj1100ProcessInfoAjax(HttpServletRequest request, HttpServletResponse response, ModelMap model ) throws Exception {

		try{
			// request 파라미터를 map으로 변환
			Map<String, String> paramMap = RequestConvertor.requestParamToMapAddSelInfo(request, true);
			
			//프로젝트 ID 가져오기
    		HttpSession ss = request.getSession();
    		paramMap.put("prjId", (String) ss.getAttribute("selPrjId"));
    		
    		String newProcessId = prj1100Service.insertFlw1000ProcessInfo(paramMap);
			model.addAttribute("newProcessId", newProcessId);
			
			//성공 메시지 세팅
			model.addAttribute("errorYN", "N");
			model.addAttribute("message", egovMessageSource.getMessage("success.common.insert"));

			return new ModelAndView("jsonView");
		}
		catch(Exception ex){
			Log.error("insertPrj1100ProcessInfoAjax()", ex);

			//조회실패 메시지 세팅 및 저장 성공여부 세팅
			model.addAttribute("errorYN", "Y");
			model.addAttribute("message", egovMessageSource.getMessage("fail.common.insert"));
			return new ModelAndView("jsonView");
		}
	}
	
	/**
	 * [DB]Flw1100 프로세스 수정 Ajax
	 * @param 
	 * @return 
	 * @exception Exception
	 */
	@RequestMapping(value="/prj/prj1000/prj1100/updatePrj1100ProcessInfoAjax.do")
	public ModelAndView updatePrj1100ProcessInfoAjax(HttpServletRequest request, HttpServletResponse response, ModelMap model ) throws Exception {

		try{
			// request 파라미터를 map으로 변환
			Map<String, String> paramMap = RequestConvertor.requestParamToMapAddSelInfo(request, true);
			
			//프로젝트 ID 가져오기
    		HttpSession ss = request.getSession();
    		paramMap.put("prjId", (String) ss.getAttribute("selPrjId"));
    		
			prj1100Service.updateFlw1000ProcessInfo(paramMap);
			
			//성공 메시지 세팅
			model.addAttribute("errorYN", "N");
			model.addAttribute("message", egovMessageSource.getMessage("success.common.update"));

			return new ModelAndView("jsonView");
		}
		catch(Exception ex){
			Log.error("updatePrj1100ProcessInfoAjax()", ex);

			//조회실패 메시지 세팅 및 저장 성공여부 세팅
			model.addAttribute("errorYN", "Y");
			model.addAttribute("message", egovMessageSource.getMessage("fail.common.update"));
			return new ModelAndView("jsonView");
		}
	}
	
	/**
	 * [DB]Flw1100 프로세스 확정 Ajax
	 * - 작업흐름 '종료분기' 추가
	 * - 프로세스 수정(confirmCd, jsonData)
	 * @param 
	 * @return 
	 * @exception Exception
	 */
	@RequestMapping(value="/prj/prj1000/prj1100/updatePrj1100ProcessConfirmInfo.do")
	public ModelAndView updatePrj1100ProcessConfirmInfo(HttpServletRequest request, HttpServletResponse response, ModelMap model ) throws Exception {
		
		try{
			// request 파라미터를 map으로 변환
			Map<String, String> paramMap = RequestConvertor.requestParamToMapAddSelInfo(request, true);
			
			//프로젝트 ID 가져오기
			HttpSession ss = request.getSession();
			paramMap.put("prjId", (String) ss.getAttribute("selPrjId"));

			prj1100Service.updateFlw1000ProcessConfirmInfo(paramMap);
			
			//성공 메시지 세팅
			model.addAttribute("errorYN", "N");
			model.addAttribute("message", egovMessageSource.getMessage("success.common.update"));
			
			return new ModelAndView("jsonView");
		}
		catch(Exception ex){
			Log.error("updatePrj1100ProcessConfirmInfo()", ex);
			
			//조회실패 메시지 세팅 및 저장 성공여부 세팅
			model.addAttribute("errorYN", "Y");
			model.addAttribute("message", egovMessageSource.getMessage("fail.common.update"));
			return new ModelAndView("jsonView");
		}
	}
	
	/**
	 * [DB]Flw1100 프로세스 제거 Ajax
	 * @param 
	 * @return 
	 * @exception Exception
	 */
	@RequestMapping(value="/prj/prj1000/prj1100/deletePrj1100ProcessInfoAjax.do")
	public ModelAndView deletePrj1100ProcessInfoAjax(HttpServletRequest request, HttpServletResponse response, ModelMap model ) throws Exception {

		try{
			// request 파라미터를 map으로 변환
			Map<String, String> paramMap = RequestConvertor.requestParamToMapAddSelInfo(request, true);
			
			//프로젝트 ID 가져오기
    		HttpSession ss = request.getSession();
    		paramMap.put("prjId", (String) ss.getAttribute("selPrjId"));
    		
			prj1100Service.deleteFlw1000ProcessInfo(paramMap);
			
			//성공 메시지 세팅
			model.addAttribute("errorYN", "N");
			model.addAttribute("message", egovMessageSource.getMessage("success.common.delete"));

			return new ModelAndView("jsonView");
		}
		catch(Exception ex){
			Log.error("deletePrj1100ProcessInfoAjax()", ex);

			//조회실패 메시지 세팅 및 저장 성공여부 세팅
			model.addAttribute("errorYN", "Y");
			model.addAttribute("message", egovMessageSource.getMessage("fail.common.delete"));
			return new ModelAndView("jsonView");
		}
	}
	
	/**
	 * [DB]Flw1100 프로세스에 배정된 요구사항 수
	 * @param 
	 * @return 
	 * @exception Exception
	 */
	@RequestMapping(value="/prj/prj1000/prj1100/selectPrj1100ProcessReqCntAjax.do")
	public ModelAndView selectPrj1100ProcessReqCntAjax(HttpServletRequest request, HttpServletResponse response, ModelMap model ) throws Exception {
		
		try{
			// request 파라미터를 map으로 변환
			Map<String, String> paramMap = RequestConvertor.requestParamToMapAddSelInfo(request, true);
			
			//프로젝트 ID 가져오기
			HttpSession ss = request.getSession();
			paramMap.put("prjId", (String) ss.getAttribute("selPrjId"));
			
			int proReqCnt = prj1100Service.selectFlw1000ProcessReqCnt(paramMap);
			
			model.addAttribute("proReqCnt", proReqCnt);
			
			//성공 메시지 세팅
			model.addAttribute("errorYN", "N");
			model.addAttribute("message", egovMessageSource.getMessage("success.common.select"));
			
			return new ModelAndView("jsonView");
		}
		catch(Exception ex){
			Log.error("selectPrj1100ProcessReqCntAjax()", ex);
			
			//조회실패 메시지 세팅 및 저장 성공여부 세팅
			model.addAttribute("errorYN", "Y");
			model.addAttribute("message", egovMessageSource.getMessage("fail.common.select"));
			return new ModelAndView("jsonView");
		}
	}
	
	/**
	 * [DB]Flw1100 작업흐름 저장 Ajax
	 * @param 
	 * @return 
	 * @exception Exception
	 */
	@RequestMapping(value="/prj/prj1000/prj1100/insertPrj1100FlowInfoAjax.do")
	public ModelAndView insertPrj1100FlowInfoAjax(HttpServletRequest request, HttpServletResponse response, ModelMap model ) throws Exception {

		try{
			// request 파라미터를 map으로 변환
			Map<String, String> paramMap = RequestConvertor.requestParamToMapAddSelInfo(request, true);
			
			//프로젝트 ID 가져오기
    		HttpSession ss = request.getSession();
    		paramMap.put("prjId", (String) ss.getAttribute("selPrjId"));
    		
			prj1100Service.insertFlw1100FlowInfo(paramMap);
			
			//성공 메시지 세팅
			model.addAttribute("errorYN", "N");
			model.addAttribute("message", egovMessageSource.getMessage("success.common.insert"));

			return new ModelAndView("jsonView");
		}
		catch(Exception ex){
			Log.error("insertPrj1100FlowInfoAjax()", ex);

			//조회실패 메시지 세팅 및 저장 성공여부 세팅
			model.addAttribute("errorYN", "Y");
			model.addAttribute("message", egovMessageSource.getMessage("fail.common.insert"));
			return new ModelAndView("jsonView");
		}
	}
	
	/**
	 * [DB]Flw1100 작업흐름 수정 Ajax
	 * @param 
	 * @return 
	 * @exception Exception
	 */
	@RequestMapping(value="/prj/prj1000/prj1100/updatePrj1100FlowInfoAjax.do")
	public ModelAndView updatePrj1100FlowInfoAjax(HttpServletRequest request, HttpServletResponse response, ModelMap model ) throws Exception {

		try{
			// request 파라미터를 map으로 변환
			Map<String, String> paramMap = RequestConvertor.requestParamToMapAddSelInfo(request, true);
			
			//프로젝트 ID 가져오기
    		HttpSession ss = request.getSession();
    		paramMap.put("prjId", (String) ss.getAttribute("selPrjId"));
    		
			prj1100Service.updateFlw1100FlowInfo(paramMap);
			
			//성공 메시지 세팅
			model.addAttribute("errorYN", "N");
			model.addAttribute("message", egovMessageSource.getMessage("success.common.update"));

			return new ModelAndView("jsonView");
		}
		catch(Exception ex){
			Log.error("updatePrj1100FlowInfoAjax()", ex);

			//조회실패 메시지 세팅 및 저장 성공여부 세팅
			model.addAttribute("errorYN", "Y");
			model.addAttribute("message", egovMessageSource.getMessage("fail.common.update"));
			return new ModelAndView("jsonView");
		}
	}
	
	/**
	 * [DB]Flw1100 작업흐름 삭제 Ajax
	 * @param 
	 * @return 
	 * @exception Exception
	 */
	@RequestMapping(value="/prj/prj1000/prj1100/deletePrj1100FlowInfoAjax.do")
	public ModelAndView deletePrj1100FlowInfoAjax(HttpServletRequest request, HttpServletResponse response, ModelMap model ) throws Exception {

		try{
			// request 파라미터를 map으로 변환
			Map<String, String> paramMap = RequestConvertor.requestParamToMapAddSelInfo(request, true);
			
			//프로젝트 ID 가져오기
    		HttpSession ss = request.getSession();
    		paramMap.put("prjId", (String) ss.getAttribute("selPrjId"));
    		
			prj1100Service.deleteFlw1100FlowInfo(paramMap);
			
			//성공 메시지 세팅
			model.addAttribute("errorYN", "N");
			model.addAttribute("message", egovMessageSource.getMessage("success.common.delete"));

			return new ModelAndView("jsonView");
		}
		catch(Exception ex){
			Log.error("deletePrj1100FlowInfoAjax()", ex);

			//조회실패 메시지 세팅 및 저장 성공여부 세팅
			model.addAttribute("errorYN", "Y");
			model.addAttribute("message", egovMessageSource.getMessage("fail.common.delete"));
			return new ModelAndView("jsonView");
		}
	}
	
	/**
	 * [DB]Flw1200 작업흐름 추가 항목 목록, 공통코드 마스터 목록 조회 Ajax
	 * @param 
	 * @return 
	 * @exception Exception
	 */
	@SuppressWarnings({ "rawtypes", "unchecked" })
	@RequestMapping(value="/prj/prj1000/prj1100/selectPrj1100OptListAjax.do")
	public ModelAndView selectPrj1100OptListAjax(HttpServletRequest request, HttpServletResponse response, ModelMap model ) throws Exception {

		try{
			// request 파라미터를 map으로 변환 (licGrpId 포함)
			Map<String, String> paramMap = RequestConvertor.requestParamToMapAddSelInfo(request, true);
			
			//type Prj1100 - 공통코드 조회X, Prj1101 - 공통코드 조회
			String type = paramMap.get("type");
			
			//프로젝트 ID 가져오기
    		HttpSession ss = request.getSession();
    		if(paramMap.get("prjId") == null){
    			paramMap.put("prjId", (String) ss.getAttribute("selPrjId"));
    		}
    		
    		//추가 항목 목록
			List<Map> optList = prj1100Service.selectFlw1200OptList(paramMap);
			model.addAttribute("optList", optList);
			
			if("prj1101".equals(type)){
				//공통 코드 목록
				List<Map> commonMstList = adm4000Service.selectAdm4000CommonCodeMasterList(paramMap);
				model.addAttribute("commonMstList", commonMstList);
			}else{
				model.addAttribute("commonMstList", null);
			}
			
			
			//성공 메시지 세팅
			model.addAttribute("errorYN", "N");
			model.addAttribute("message", egovMessageSource.getMessage("success.common.select"));

			return new ModelAndView("jsonView");
		}
		catch(Exception ex){
			Log.error("selectPrj1100OptListAjax()", ex);

			//조회실패 메시지 세팅 및 저장 성공여부 세팅
			model.addAttribute("errorYN", "Y");
			model.addAttribute("message", egovMessageSource.getMessage("fail.common.select"));
			return new ModelAndView("jsonView");
		}
	}
	
	/**
	 * [DB]Flw1200 추가 항목 저장 Ajax
	 * @param 
	 * @return 
	 * @exception Exception
	 */
	@RequestMapping(value="/prj/prj1000/prj1100/insertPrj1100OptInfoAjax.do")
	public ModelAndView insertPrj1100OptInfoAjax(HttpServletRequest request, HttpServletResponse response, ModelMap model ) throws Exception {

		try{
			// request 파라미터를 map으로 변환
			Map<String, String> paramMap = RequestConvertor.requestParamToMapAddSelInfo(request, true);
			
			//프로젝트 ID 가져오기
    		HttpSession ss = request.getSession();
    		paramMap.put("prjId", (String) ss.getAttribute("selPrjId"));
    		
			prj1100Service.insertFlw1200OtpInfo(paramMap);
			
			//성공 메시지 세팅
			model.addAttribute("errorYN", "N");
			model.addAttribute("message", egovMessageSource.getMessage("success.common.insert"));

			return new ModelAndView("jsonView");
		}
		catch(Exception ex){
			Log.error("insertPrj1100OptInfoAjax()", ex);

			//조회실패 메시지 세팅 및 저장 성공여부 세팅
			model.addAttribute("errorYN", "Y");
			model.addAttribute("message", egovMessageSource.getMessage("fail.common.insert"));
			return new ModelAndView("jsonView");
		}
	}
	
	/**
	 * [DB]Flw1200 추가 항목 수정 Ajax
	 * @param 
	 * @return 
	 * @exception Exception
	 */
	@RequestMapping(value="/prj/prj1000/prj1100/updatePrj1100OptInfoAjax.do")
	public ModelAndView updatePrj1100OptInfoAjax(HttpServletRequest request, HttpServletResponse response, ModelMap model ) throws Exception {

		try{
			// request 파라미터를 map으로 변환
			Map<String, String> paramMap = RequestConvertor.requestParamToMapAddSelInfo(request, true);
			
			//프로젝트 ID 가져오기
    		HttpSession ss = request.getSession();
    		paramMap.put("prjId", (String) ss.getAttribute("selPrjId"));
    		
			prj1100Service.updateFlw1200OtpInfo(paramMap);
			
			//성공 메시지 세팅
			model.addAttribute("errorYN", "N");
			model.addAttribute("message", egovMessageSource.getMessage("success.common.update"));

			return new ModelAndView("jsonView");
		}
		catch(Exception ex){
			Log.error("updatePrj1100OptInfoAjax()", ex);

			//조회실패 메시지 세팅 및 저장 성공여부 세팅
			model.addAttribute("errorYN", "Y");
			model.addAttribute("message", egovMessageSource.getMessage("fail.common.update"));
			return new ModelAndView("jsonView");
		}
	}
	
	/**
	 * [DB]Flw1200 추가 항목 삭제 Ajax
	 * @param 
	 * @return 
	 * @exception Exception
	 */
	@RequestMapping(value="/prj/prj1000/prj1100/deletePrj1100OptInfoAjax.do")
	public ModelAndView deletePrj1100OptInfoAjax(HttpServletRequest request, HttpServletResponse response, ModelMap model ) throws Exception {

		try{
			// request 파라미터를 map으로 변환
			Map<String, String> paramMap = RequestConvertor.requestParamToMapAddSelInfo(request, true);
			
			//프로젝트 ID 가져오기
    		HttpSession ss = request.getSession();
    		paramMap.put("prjId", (String) ss.getAttribute("selPrjId"));
    		
			prj1100Service.deleteFlw1200OtpInfo(paramMap);
			
			//성공 메시지 세팅
			model.addAttribute("errorYN", "N");
			model.addAttribute("message", egovMessageSource.getMessage("success.common.delete"));

			return new ModelAndView("jsonView");
		}
		catch(Exception ex){
			Log.error("deletePrj1100OptInfoAjax()", ex);

			//조회실패 메시지 세팅 및 저장 성공여부 세팅
			model.addAttribute("errorYN", "Y");
			model.addAttribute("message", egovMessageSource.getMessage("fail.common.delete"));
			return new ModelAndView("jsonView");
		}
	}
	
	/**
	 * [DB]Flw1200 작업흐름 목록 불러오기 Ajax
	 * @param 
	 * @return 
	 * @exception Exception
	 */
	@SuppressWarnings({ "rawtypes", "unchecked" })
	@RequestMapping(value="/prj/prj1000/prj1100/selectPrj1100FlwListAjax.do")
	public ModelAndView selectPrj1100FlwListAjax(HttpServletRequest request, HttpServletResponse response, ModelMap model ) throws Exception {

		try{
			// request 파라미터를 map으로 변환
			Map<String, String> paramMap = RequestConvertor.requestParamToMapAddSelInfo(request, true);
			
			//프로젝트 ID 가져오기
    		HttpSession ss = request.getSession();
    		paramMap.put("prjId", (String) ss.getAttribute("selPrjId"));
    		
			List<Map> flowList = prj1100Service.selectFlw1100FlowList(paramMap);
			
			//작업흐름 Id가져오기 (작업흐름이 0개 일 수 없음)
			String flowId = (String) flowList.get(0).get("flowId");
			paramMap.put("flowId", flowId);
			
			//작업흐름에 해당하는 추가항목 불러오기
			List<Map> optList = prj1100Service.selectFlw1200OptList(paramMap);
			
			model.addAttribute("optList", optList);
			model.addAttribute("flowList", flowList);
			
			//성공 메시지 세팅
			model.addAttribute("errorYN", "N");
			model.addAttribute("message", egovMessageSource.getMessage("success.common.select"));

			return new ModelAndView("jsonView");
		}
		catch(Exception ex){
			Log.error("selectPrj1100FlwListAjax()", ex);

			//조회실패 메시지 세팅 및 저장 성공여부 세팅
			model.addAttribute("errorYN", "Y");
			model.addAttribute("message", egovMessageSource.getMessage("fail.common.select"));
			return new ModelAndView("jsonView");
		}
	}
	
	/**
	 * [DB]Flw1400 요구사항별 리비전 목록 불러오기 Ajax
	 * @param 
	 * @return 
	 * @exception Exception
	 */
	@SuppressWarnings({ "rawtypes", "unchecked" })
	@RequestMapping(value="/prj/prj1000/prj1100/selectPrj1100ReqRepRevisionListAjax.do")
	public ModelAndView selectPrj1100ReqRepRevisionListAjax(@ModelAttribute("prj1100VO") Prj1100VO prj1100VO, HttpServletRequest request, HttpServletResponse response, ModelMap model ) throws Exception {
		
		try{
			// request 파라미터를 map으로 변환
			Map<String, String> paramMap = RequestConvertor.requestParamToMapAddSelInfo(request, true);

			// 로그인 VO를 가져온다.
			HttpSession ss = request.getSession();
			LoginVO loginVO = (LoginVO) ss.getAttribute("loginVO");

			prj1100VO.setLicGrpId(loginVO.getLicGrpId());
			prj1100VO.setPrjId((String)paramMap.get("selPrjId"));
			
			//현재 페이지 값, 보여지는 개체 수
			String _pageNo_str = paramMap.get("pageNo");
			String _pageSize_str = paramMap.get("pageSize");
					
			int _pageNo = 1;
			int _pageSize = 10;//OslAgileConstant.pageSize;
						
			// 넘어온 페이지 정보가 있다면 해당 값으로 세팅
			if(_pageNo_str != null && !"".equals(_pageNo_str)){
				_pageNo = Integer.parseInt(_pageNo_str)+1;  
			}
			if(_pageSize_str != null && !"".equals(_pageSize_str)){
				_pageSize = Integer.parseInt(_pageSize_str);  
			}
			
			// 페이지 번호, 페이지 사이즈 세팅
			prj1100VO.setPageIndex(_pageNo);
			prj1100VO.setPageSize(_pageSize);
			prj1100VO.setPageUnit(_pageSize);
			
			PaginationInfo paginationInfo = PagingUtil.getPaginationInfo(prj1100VO);  /** paging - 신규방식 */
			
			// 총 데이터 건수
			int totCnt = 0;
			
			// 요구사항별 리비전 총 건수 조회
			totCnt = prj1100Service.selectFlw1400ReqRevisionNumListCnt(prj1100VO);
			
			// 총 데이터 건수 세팅
			paginationInfo.setTotalRecordCount(totCnt);
						
			// 요구사항별 리비전 목록 조회
			List<Map> reqRevisionList = prj1100Service.selectFlw1400ReqRevisionNumList(prj1100VO);
			
			//리비전 목록
			model.addAttribute("reqRevisionList", reqRevisionList);
			
			//페이지 정보 보내기
			Map<String, Integer> pageMap = new HashMap<String, Integer>();
			pageMap.put("pageNo",prj1100VO.getPageIndex());
			pageMap.put("listCount", reqRevisionList.size());
			pageMap.put("totalPages", paginationInfo.getTotalPageCount());
			pageMap.put("totalElements", totCnt);
			pageMap.put("pageSize", _pageSize);
			
			// 페이지 정보 세팅
			model.addAttribute("page", pageMap);
			
			//성공 메시지 세팅
			model.addAttribute("errorYN", "N");
			model.addAttribute("message", egovMessageSource.getMessage("success.common.select"));
			
			return new ModelAndView("jsonView");
		}
		catch(Exception ex){
			Log.error("selectPrj1100ReqRepRevisionListAjax()", ex);
			
			//조회실패 메시지 세팅 및 저장 성공여부 세팅
			model.addAttribute("errorYN", "Y");
			model.addAttribute("message", egovMessageSource.getMessage("fail.common.select"));
			return new ModelAndView("jsonView");
		}
	}

	/**
	 * 요구사항 리비전 번호 저장
	 * @param request
	 * @param model
	 * @return
	 * @throws Exception
	 */
	@SuppressWarnings({ "rawtypes", "unchecked" })
	@RequestMapping(value="/prj/prj1000/prj1100/insertPrj1100RevisionNumList.do")
	public ModelAndView insertReq4100RevisionNumInfo(HttpServletRequest request, HttpServletResponse response, ModelMap model )	throws Exception {
		try{
			// request 파라미터를 map으로 변환
			Map paramMap = RequestConvertor.requestParamToMapAddSelInfo(request, true);
			paramMap.put("prjId", paramMap.get("selPrjId"));
			
			//선택 리비전 갯수
			int selRepNumCnt = Integer.parseInt((String)paramMap.get("selRepNumCnt"));
			
			//실패 리비전 갯수 리턴
			int addFailRepNumCnt = prj1100Service.insertFlw1400RevisionNumList(paramMap);
			
			//선택 리비전 갯수와 실패 리비전 갯수가 같은경우 에러
			if(selRepNumCnt == addFailRepNumCnt){
				//실패 갯수 메시지 세팅
				model.addAttribute("errorYn", "Y");
				model.addAttribute("message", "선택된 모든 리비전이 중복됩니다.");
			}
			//실패 리비전 갯수가 0이상인경우 메시지 변경
			else if(addFailRepNumCnt > 0){
				//실패 갯수 메시지 세팅
				model.addAttribute("errorYn", "N");
				model.addAttribute("message", egovMessageSource.getMessage("success.common.insert")+"</br>"+addFailRepNumCnt+"건의 중복 선택 리비전은 제외되었습니다.");
			}else{
				//조회 성공 메시지 세팅
				model.addAttribute("errorYn", "N");
				model.addAttribute("message", egovMessageSource.getMessage("success.common.insert"));
			}
			
			
			
			return new ModelAndView("jsonView");

		}catch(Exception e){
			Log.error("insertReq4100RevisionNumInfo()", e);
			model.addAttribute("errorYn", "Y");
			model.addAttribute("message", egovMessageSource.getMessage("fail.common.insert"));
			return new ModelAndView("jsonView");
		}
	}
	

	/**
	 * 요구사항 리비전 번호 제거
	 * @param request
	 * @param model
	 * @return
	 * @throws Exception
	 */
	@SuppressWarnings({ "rawtypes", "unchecked" })
	@RequestMapping(value="/prj/prj1000/prj1100/deletePrj1100RevisionNumList.do")
	public ModelAndView deletePrj1100RevisionNumList(HttpServletRequest request, HttpServletResponse response, ModelMap model )	throws Exception {
		try{
			// request 파라미터를 map으로 변환
			Map paramMap = RequestConvertor.requestParamToMapAddSelInfo(request, true);
			paramMap.put("prjId", paramMap.get("selPrjId"));
			
			//리비전 제거
			prj1100Service.deleteFlw1400RevisionNumList(paramMap);
			
			//조회 성공 메시지 세팅
			model.addAttribute("errorYn", "N");
			model.addAttribute("message", egovMessageSource.getMessage("success.common.delete"));
			
			return new ModelAndView("jsonView");

		}catch(Exception e){
			Log.error("deletePrj1100RevisionNumList()", e);
			model.addAttribute("errorYn", "Y");
			model.addAttribute("message", egovMessageSource.getMessage("fail.common.delete"));
			return new ModelAndView("jsonView");
		}
	}
	

	/**
	 * [DB]Flw1500 작업흐름별 역할그룹 제한 목록 불러오기 Ajax
	 * @param 
	 * @return 
	 * @exception Exception
	 */
	@SuppressWarnings({ "rawtypes", "unchecked" })
	@RequestMapping(value="/prj/prj1000/prj1100/selectPrj1100FlowAuthGrpListAjax.do")
	public ModelAndView selectPrj1100FlowAuthGrpListAjax(HttpServletRequest request, HttpServletResponse response, ModelMap model ) throws Exception {
		
		try{
			// request 파라미터를 map으로 변환
			Map<String, String> paramMap = RequestConvertor.requestParamToMapAddSelInfo(request, true);
			
			if(paramMap.get("prjId") == null){
    			paramMap.put("prjId", (String) paramMap.get("selPrjId"));
    		}
			
			//역할그룹 목록
			List<Map> flowAuthGrpList = prj1100Service.selectFlw1500FlowAuthGrpList(paramMap);
			model.addAttribute("flowAuthGrpList", flowAuthGrpList);
			
			//성공 메시지 세팅
			model.addAttribute("errorYN", "N");
			model.addAttribute("message", egovMessageSource.getMessage("success.common.select"));
			
			return new ModelAndView("jsonView");
		}
		catch(Exception ex){
			Log.error("selectPrj1100FlowAuthGrpListAjax()", ex);
			
			//조회실패 메시지 세팅 및 저장 성공여부 세팅
			model.addAttribute("errorYN", "Y");
			model.addAttribute("message", egovMessageSource.getMessage("fail.common.select"));
			return new ModelAndView("jsonView");
		}
	}

	/**
	 *  작업흐름별 역할그룹 제한 저장
	 * @param request
	 * @param model
	 * @return
	 * @throws Exception
	 */
	@SuppressWarnings({ "rawtypes", "unchecked" })
	@RequestMapping(value="/prj/prj1000/prj1100/insertPrj1100FlowAuthGrpList.do")
	public ModelAndView insertPrj1100FlowAuthGrpList(HttpServletRequest request, HttpServletResponse response, ModelMap model )	throws Exception {
		try{
			// request 파라미터를 map으로 변환
			Map paramMap = RequestConvertor.requestParamToMapAddSelInfo(request, true);
			paramMap.put("prjId", paramMap.get("selPrjId"));
			
			//선택 역할그룹 갯수
			int selAuthCnt = Integer.parseInt((String)paramMap.get("selAuthCnt"));
			
			//실패 역할그룹 갯수 리턴
			int addFailAuthCnt = prj1100Service.insertFlw1500FlowAuthGrpList(paramMap);
			
			//선택 역할그룹 갯수와 실패 역할그룹 갯수가 같은경우 에러
			if(selAuthCnt == addFailAuthCnt){
				//실패 갯수 메시지 세팅
				model.addAttribute("errorYn", "Y");
				model.addAttribute("message", "선택된 모든 역할그룹이 중복됩니다.");
			}
			//실패 역할그룹 갯수가 0이상인경우 메시지 변경
			else if(addFailAuthCnt > 0){
				//실패 갯수 메시지 세팅
				model.addAttribute("errorYn", "N");
				model.addAttribute("message", egovMessageSource.getMessage("success.common.insert")+"</br>"+addFailAuthCnt+"건의 중복 선택 역할그룹은 제외되었습니다.");
			}else{
				//조회 성공 메시지 세팅
				model.addAttribute("errorYn", "N");
				model.addAttribute("message", egovMessageSource.getMessage("success.common.insert"));
			}
			
			return new ModelAndView("jsonView");

		}catch(Exception e){
			Log.error("insertPrj1100FlowAuthGrpList()", e);
			model.addAttribute("errorYn", "Y");
			model.addAttribute("message", egovMessageSource.getMessage("fail.common.insert"));
			return new ModelAndView("jsonView");
		}
	}
	

	/**
	 *  작업흐름별 역할그룹 제한 제거
	 * @param request
	 * @param model
	 * @return
	 * @throws Exception
	 */
	@SuppressWarnings({ "rawtypes", "unchecked" })
	@RequestMapping(value="/prj/prj1000/prj1100/deletePrj1100FlowAuthGrpList.do")
	public ModelAndView deletePrj1100FlowAuthGrpList(HttpServletRequest request, HttpServletResponse response, ModelMap model )	throws Exception {
		try{
			// request 파라미터를 map으로 변환
			Map paramMap = RequestConvertor.requestParamToMapAddSelInfo(request, true);
			paramMap.put("prjId", paramMap.get("selPrjId"));
			
			//역할그룹 제거
			prj1100Service.deleteFlw1500FlowAuthGrpList(paramMap);
			
			//조회 성공 메시지 세팅
			model.addAttribute("errorYn", "N");
			model.addAttribute("message", egovMessageSource.getMessage("success.common.delete"));
			
			return new ModelAndView("jsonView");

		}catch(Exception e){
			Log.error("deletePrj1100FlowAuthGrpList()", e);
			model.addAttribute("errorYn", "Y");
			model.addAttribute("message", egovMessageSource.getMessage("fail.common.delete"));
			return new ModelAndView("jsonView");
		}
	}
	
	/**
	 * 프로세스 확정 취소
	 * -jsonData 갱신
	 * -최종완료 작업흐름 제거
	 * -프로세스 confirmCd = 01 확정취소
	 * @param request
	 * @param model
	 * @return
	 * @throws Exception
	 */
	@SuppressWarnings({ "rawtypes", "unchecked" })
	@RequestMapping(value="/prj/prj1000/prj1100/updatePrj1100ProcessConfirmCancelAjax.do")
	public ModelAndView updatePrj1100ProcessConfirmCancelAjax(HttpServletRequest request, HttpServletResponse response, ModelMap model )	throws Exception {
		try{
			// request 파라미터를 map으로 변환
			Map paramMap = RequestConvertor.requestParamToMapAddSelInfo(request, true);
			paramMap.put("prjId", paramMap.get("selPrjId"));
			
			prj1100Service.updateFlw1000ProcessConfirmCancle(paramMap);
			
			//조회 성공 메시지 세팅
			model.addAttribute("errorYn", "N");
			model.addAttribute("message", egovMessageSource.getMessage("success.common.update"));
			
			return new ModelAndView("jsonView");
			
		}catch(Exception e){
			Log.error("updatePrj1100ProcessConfirmCancelAjax()", e);
			model.addAttribute("errorYn", "Y");
			model.addAttribute("message", egovMessageSource.getMessage("fail.common.update"));
			return new ModelAndView("jsonView");
		}
	}

	/**
	 * [프로세스 복사] 관리자 권한을 가지고있는 프로젝트의 프로세스 목록 불러오기
	 * @param 
	 * @return 
	 * @exception Exception
	 */
	@SuppressWarnings({ "unchecked", "rawtypes" })
	@RequestMapping(value="/prj/prj1000/prj1100/selectFlw1000ProcessCopyList.do")
	public ModelAndView selectFlw1000ProcessCopyList(HttpServletRequest request, HttpServletResponse response, ModelMap model ) throws Exception {
		
		try{
			// request 파라미터를 map으로 변환
			Map<String, String> paramMap = RequestConvertor.requestParamToMapAddSelInfo(request, true);
			
			//프로젝트 ID 가져오기
			HttpSession ss = request.getSession();
			LoginVO loginVO = (LoginVO) ss.getAttribute("loginVO");
    		paramMap.put("usrId", loginVO.getUsrId());

			List<Map> processCopyList = prj1100Service.selectFlw1000ProcessCopyList(paramMap);
			model.addAttribute("processCopyList", processCopyList);
			
			paramMap.remove("prjId");
			paramMap.put("prjId", "ROOTSYSTEM_PRJ");
			paramMap.put("dshType", "dsh1000");
			
			//ROOTSYSTEM_PRJ 프로세스 목록 불러오기
			List<Map> rootProcessList = prj1100Service.selectFlw1000ProcessList(paramMap);
			model.addAttribute("rootProcessList", rootProcessList);
			
			//성공 메시지 세팅
			model.addAttribute("errorYN", "N");
			model.addAttribute("message", egovMessageSource.getMessage("success.common.select"));
			
			return new ModelAndView("jsonView");
		}
		catch(Exception ex){
			Log.error("selectFlw1000ProcessCopyList()", ex);
			
			//조회실패 메시지 세팅 및 저장 성공여부 세팅
			model.addAttribute("errorYN", "Y");
			model.addAttribute("message", egovMessageSource.getMessage("fail.common.select"));
			return new ModelAndView("jsonView");
		}
	}
	
	/**
	 * [프로세스 복사] prjId, processId로 프로세스 복사 
	 * @param 
	 * @return 
	 * @exception Exception
	 */
	@RequestMapping(value="/prj/prj1000/prj1100/insertPrj1100ProcessCopyInfoAjax.do")
	public ModelAndView insertPrj1100ProcessCopyInfoAjax(HttpServletRequest request, HttpServletResponse response, ModelMap model ) throws Exception {
		
		try{
			// request 파라미터를 map으로 변환
			Map<String, String> paramMap = RequestConvertor.requestParamToMapAddSelInfo(request, true);
			
			prj1100Service.insertPrj1100ProcessCopyInfo(paramMap);
			
			//성공 메시지 세팅
			model.addAttribute("errorYN", "N");
			model.addAttribute("message", egovMessageSource.getMessage("success.common.insert"));
			
			return new ModelAndView("jsonView");
		}
		catch(Exception ex){
			Log.error("insertPrj1100ProcessCopyInfoAjax()", ex);
			
			//조회실패 메시지 세팅 및 저장 성공여부 세팅
			model.addAttribute("errorYN", "Y");
			model.addAttribute("message", egovMessageSource.getMessage("fail.common.insert"));
			return new ModelAndView("jsonView");
		}
	}
}
