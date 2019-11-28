package kr.opensoftlab.oslops.prj.prj1000.prj1000.web;

import java.util.List;
import java.util.Map;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import kr.opensoftlab.oslops.cmm.cmm4000.cmm4000.service.Cmm4000Service;
import kr.opensoftlab.oslops.com.vo.LoginVO;
import kr.opensoftlab.oslops.prj.prj1000.prj1000.service.Prj1000Service;
import kr.opensoftlab.oslops.prj.prj2000.prj2000.service.Prj2000Service;
import kr.opensoftlab.sdf.util.ModuleUseCheck;
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
 * @Class Name : Prj1000Controller.java
 * @Description : Prj1000Controller Controller class
 * @Modification Information
 *
 * @author 강재민
 * @since 2016.01.23.
 * @version 1.0
 * @see
 *  
 *  Copyright (C) OpenSoftLab Corp All right reserved.
 */
@Controller
public class Prj1000Controller {
	/** 로그4j 로거 로딩 */
	private static final Logger Log = Logger.getLogger(Prj1000Controller.class);
	
	/** Prj1000Service DI */
    @Resource(name = "prj1000Service")
    private Prj1000Service prj1000Service;
    
    /** Cmm4000Service DI */
    @Resource(name = "cmm4000Service")
    private Cmm4000Service cmm4000Service;
    
    /** Prj2000Service DI */
    @Resource(name = "prj2000Service")
    private Prj2000Service prj2000Service;
    
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
	 * Prj1000 프로젝트 생성관리 목록 조회
	 * @param 
	 * @return 
	 * @exception Exception
	 */
	@RequestMapping(value="/prj/prj1000/prj1000/selectPrj1000View.do")
    public String selectPrj1000View(HttpServletRequest request, HttpServletResponse response, ModelMap model ) throws Exception {
        	return "/prj/prj1000/prj1000/prj1000";
    }
    
	/**
	 * Prj1004 프로젝트 생성 마법사 팝업
	 * @param 
	 * @return 
	 * @exception Exception
	 */
	@SuppressWarnings({ "unchecked", "rawtypes" })
	@RequestMapping(value="/prj/prj1000/prj1000/selectPrj1004View.do")
    public String selectPrj1004View(HttpServletRequest request, HttpServletResponse response, ModelMap model ) throws Exception {
		//리퀘스트에서 넘어온 파라미터를 맵으로 세팅
		Map<String, String> paramMap = RequestConvertor.requestParamToMap(request, true);
				
		//로그인VO 가져오기
		HttpSession ss = request.getSession();
		LoginVO loginVO = (LoginVO) ss.getAttribute("loginVO");
		paramMap.put("usrId", loginVO.getUsrId());
		paramMap.put("prjGrpCd", "01");

		// 전체프로젝트 생성관리인지 프로젝트 생성관리인지 구분하는 값
		String pageType = paramMap.get("pageType");
			
		List<Map> prjList = null;
		
		// 전체 프로젝트 생성관리에서 프로젝트 생성 마법사 팝업 호출시
		if(pageType != null && "systemAdmin".equals(pageType)){
			// 라이선스 그룹의 모든 프로젝트 그룹을 불러온다
			prjList = prj1000Service.selectPrj1000ProjectGroupListAjax(paramMap);
		// 프로젝트 생성관리에서 프로젝트 마법사 팝업 호출시	
		}else {
			//프로젝트 목록 불러오기
			prjList = (List)prj1000Service.selectPrj1000View(paramMap);
		}
				
		if(prjList != null && prjList.size() > 0){
			model.addAttribute("prjList",prjList);
		}
				
        return "/prj/prj1000/prj1000/prj1004";
    }
    
    /**
	 * Prj1000 조회버튼 클릭시 프로젝트 생성관리 조회 AJAX
	 * @param 
	 * @return 
	 * @exception Exception
	 */
    @SuppressWarnings({ "rawtypes", "unchecked" })
	@RequestMapping(value="/prj/prj1000/prj1000/selectPrj1000ViewAjax.do")
    public ModelAndView selectPrj1000ViewAjax(HttpServletRequest request, HttpServletResponse response, ModelMap model ) throws Exception {
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
        	List<Map> selectPrj1000List = (List) prj1000Service.selectPrj1000View(paramMap);

        	model.put("list", selectPrj1000List);
        	
        	//조회성공메시지 세팅
        	model.addAttribute("message", egovMessageSource.getMessage("success.common.select"));
        	
        	

        	return new ModelAndView("jsonView", model);
    	}
    	catch(Exception ex){
    		Log.error("selectPrj1000ViewAjax()", ex);
    		model.addAttribute("message", egovMessageSource.getMessage("fail.common.select"));
    		throw new Exception(ex.getMessage());
    	}
    }
    
    

    /**
	 * prj1000 선택 프로젝트 정보 불러오기 Ajax
	 * 
	 * @param 
	 * @return 
	 * @exception Exception
	 */
    @SuppressWarnings({ "rawtypes"})
	@RequestMapping(value="/prj/prj1000/prj1000/selectPrj1000SelPrjInfoAjax.do")
    public ModelAndView selectPrj1000SelPrjInfoAjax(HttpServletRequest request, HttpServletResponse response, ModelMap model ) throws Exception {
    	try{
    		//리퀘스트에서 넘어온 파라미터를 맵으로 세팅
    		Map<String, String> paramMap = RequestConvertor.requestParamToMap(request, true);

        	//프로젝트 생성관리 목록 가져오기
        	Map prjInfo = prj1000Service.selectPrj1000Info(paramMap);
        	model.put("prjInfo", prjInfo);
        	
        	//성공 메시지 세팅
			model.addAttribute("errorYN", "N");
			model.addAttribute("message", egovMessageSource.getMessage("success.common.select"));
        	
        	return new ModelAndView("jsonView", model);
    	}
    	catch(Exception ex){
    		Log.error("selectPrj1000SelPrjInfoAjax()", ex);
    		model.addAttribute("errorYN", "Y");
    		model.addAttribute("message", egovMessageSource.getMessage("fail.common.select"));
    		throw new Exception(ex.getMessage());
    	}
    }
    
    
    
    
	/**
	 * Prj1001 프로젝트 생성 팝업 호출
	 * @param 
	 * @return 
	 * @exception Exception
	 */
	@RequestMapping(value="/prj/prj1000/prj1000/selectPrj1001View.do")
    public String selectPrj1001View(HttpServletRequest request, HttpServletResponse response, ModelMap model ) throws Exception {
    	try{
    		//리퀘스트에서 넘어온 파라미터를 맵으로 세팅
    		Map<String, String> paramMap = RequestConvertor.requestParamToMap(request, true);
    		
    		//세션 가져오기
    		HttpSession ss = request.getSession();
    		LoginVO loginVO = (LoginVO) ss.getAttribute("loginVO");
			paramMap.put("usrId", loginVO.getUsrId());
    		
			String type = paramMap.get("type");
			String groupStr = "";
			//프로젝트 그룹 추가인 경우
			if(type != null && "group".equals(type)){
				groupStr = "그룹";
			}else if("project".equals(type)){
				model.addAttribute("prjGrpNm", paramMap.get("prjGrpNm"));
				model.addAttribute("startDt", paramMap.get("startDt"));
				model.addAttribute("endDt", paramMap.get("endDt"));
			}
			model.addAttribute("groupStr", groupStr);
			model.addAttribute("type", type);
			model.addAttribute("prjGrpId", paramMap.get("prjGrpId"));
    		
        	return "/prj/prj1000/prj1000/prj1001";
    	}
    	catch(Exception ex){
    		Log.error("selectPrj1000View()", ex);
    		model.addAttribute("message", egovMessageSource.getMessage("fail.common.select"));
    		throw new Exception(ex.getMessage());
    	}
    }
    
    /**
	 * Prj1003 최초 프로젝트 생성 레이어 팝업 조회
	 * @param 
	 * @return 
	 * @exception Exception
	 */
	@SuppressWarnings({ "unchecked", "rawtypes" })
	@RequestMapping(value="/prj/prj1000/prj1000/selectPrj1003View.do")
    public String selectPrj1003View(HttpServletRequest request, HttpServletResponse response, ModelMap model ) throws Exception {
    	try{
    		//리퀘스트에서 넘어온 파라미터를 맵으로 세팅
    		Map<String, String> paramMap = RequestConvertor.requestParamToMap(request, true);
    		
    		List<Map> prjGrpList = prj1000Service.selectPrj1000PrjGrpExistCheck(paramMap);
    		String groupStr = "";
    		String type = paramMap.get("type");
    		
    		//프로젝트 그룹이 존재하는 경우
    		if(prjGrpList != null && prjGrpList.size() > 0){
    			Map prjGrpFirstInfo = prjGrpList.get(0);
        		
    			//그룹 정보 전달
    			model.addAttribute("prjGrpId", prjGrpFirstInfo.get("prjId"));
        		model.addAttribute("prjGrpNm", prjGrpFirstInfo.get("prjNm"));
				model.addAttribute("startDt", prjGrpFirstInfo.get("startDt"));
				model.addAttribute("endDt", prjGrpFirstInfo.get("endDt"));
    		}else{
    			groupStr = "그룹";
    			model.addAttribute("prjGrpId", null);
    		}
    		
    		model.addAttribute("groupStr", groupStr);
    		model.addAttribute("type", type);
        	
    		return "/prj/prj1000/prj1000/prj1003";
    	}
    	catch(Exception ex){
    		Log.error("selectPrj1003View()", ex);
    		model.addAttribute("message", egovMessageSource.getMessage("fail.common.select"));
    		throw new Exception(ex.getMessage());
    	}
    }
    /**
	 * Prj1000 프로젝트 생성관리 등록(insert) AJAX
	 * @param Map
	 * @return 프로젝트 ID
	 * @exception Exception
	 */
	@SuppressWarnings({ "rawtypes", "unchecked" })
	@RequestMapping(value="/prj/prj1000/prj1000/insertPrj1001Ajax.do")
    public ModelAndView insertPrj1001Ajax(HttpServletRequest request, HttpServletResponse response, ModelMap model ) throws Exception {
    	try{
    		// request 파라미터를 map으로 변환
        	Map<String, String> paramMap = RequestConvertor.requestParamToMap(request, true);
        	
        	//세션 및 로그인VO가져오기
			HttpSession ss = request.getSession();
			LoginVO loginVO = (LoginVO) ss.getAttribute("loginVO");
			
			//userId
			String usrId = (String)loginVO.getUsrId();
			paramMap.put("usrId", usrId);
			
        	//생성 타입
        	String type = (String) paramMap.get("type");
        	
        	int count = 0; 
        	
        	//그룹 생성인경우
        	if("group".equals(type)){
        		String prjId = prj1000Service.insertPrj1000PrjGrpAjax(paramMap);
        		model.put("prjId", prjId);
        	}
        	//단위 프로젝트 생성인경우
        	else{
        		
        		Object arcmObj = paramMap.get("prjAcrm");
        		
        		if(arcmObj != null){
        			// 프로젝트 약어
            		String prjAcrmStr = (String)arcmObj;
            		paramMap.put("prjAcrm", prjAcrmStr.toUpperCase());
        		}
        		
        		count = prj1000Service.selectPrj1000ProjectAcronymCount(paramMap);
        		
        		if(count == 0){
        			// 프로젝트 생성관리 등록
	            	String prjId = prj1000Service.insertPrj1001Ajax(paramMap);
	            	model.put("prjId", prjId);
        		}
        	}
        	
        	if(count == 0){
        		
        		// 세션 재세팅을 위한 프로젝트 목록 불러올 때 프로젝트 그룹값 Map에서 제거한다.
    			// 그렇지 않으면 단위 프로젝트만 조회되어 세션에 세팅된다.
    			paramMap.remove("prjGrpCd");
        		
	        	//세션 재 세팅 - prjList
	        	//프로젝트 목록 불러오기
	    		List<Map> prjList = (List)prj1000Service.selectPrj1000View(paramMap);
	    		
	    		//프로젝트 목록 세션 제거
	    		ss.removeAttribute("prjList");
	    		
	    		//세션 재 등록
	    		ss.setAttribute("prjList", prjList);
	    		//성공 메시지 세팅
				model.addAttribute("errorYN", "N");
	        	model.addAttribute("message", egovMessageSource.getMessage("success.common.insert"));
        	}else{
        		model.addAttribute("errorYN", "Y");
        		model.addAttribute("duplicateYN", "Y");
        	}
        	
        	return new ModelAndView("jsonView", model);
    	}
    	catch(Exception ex){
    		Log.error("insertPrj1001Ajax()", ex);
    		
    		//실패 세팅
    		model.addAttribute("errorYN", "Y");
    		model.addAttribute("message", egovMessageSource.getMessage("fail.common.insert"));
    		return new ModelAndView("jsonView");
    	}
    }
    
    
    
    /**
	 * Prj1000 프로젝트 생성관리 수정(update) AJAX
	 * @param Map
	 * @return update row, 프로젝트ID
	 * @exception Exception
	 */
	@RequestMapping(value="/prj/prj1000/prj1000/updatePrj1000Ajax.do")
    public ModelAndView updatePrj1000Ajax(HttpServletRequest request, HttpServletResponse response, ModelMap model ) throws Exception {
    	try{
    		// request 파라미터를 map으로 변환
        	Map<String, String> paramMap = RequestConvertor.requestParamToMap(request, true);
        	
    		Object arcmObj = paramMap.get("prjAcrm");
    		
    		if(arcmObj != null){
    			// 프로젝트 약어
        		String prjAcrmStr = (String)arcmObj;
        		paramMap.put("prjAcrm", prjAcrmStr.toUpperCase());
    		}
        	
        	// 프로젝트 생성관리 수정
        	int updateCnt = prj1000Service.updatePrj1000Ajax(paramMap);
        	
        	model.put("prjId", paramMap.get("prjId"));
        	model.put("updateCnt", updateCnt);
        	model.addAttribute("message", egovMessageSource.getMessage("success.common.update"));
        	
        	//세션 정보 가져오기
			HttpSession ss = request.getSession();
			LoginVO loginVO = (LoginVO) ss.getAttribute("loginVO");
			
			//userId
			String usrId = (String)loginVO.getUsrId();
			paramMap.put("usrId", usrId);
			
			// 세션 재세팅을 위한 프로젝트 목록 불러올 때 프로젝트 그룹값 Map에서 제거한다.
			// 그렇지 않으면 단위 프로젝트만 조회되어 세션에 세팅된다.
			paramMap.remove("prjGrpCd");
			//세션 재 세팅 - prjList
        	//프로젝트 목록 불러오기
    		List<Map> prjList = (List)prj1000Service.selectPrj1000View(paramMap);
    		
    		//프로젝트 목록 세션 제거
    		ss.removeAttribute("prjList");
    		
    		//세션 재 등록
    		ss.setAttribute("prjList", prjList);
    		
        	return new ModelAndView("jsonView", model);
    	}
    	catch(Exception ex){
    		Log.error("updatePrj1000Ajax()", ex);
    		model.addAttribute("message", egovMessageSource.getMessage("fail.common.update"));
    		return new ModelAndView("jsonView");
    	}
    }
    
    
    
    /**
	 * Prj1000 프로젝트 생성관리 삭제(delete) AJAX
	 * @param Map
	 * @return delete row
	 * @exception Exception
	 */
	@SuppressWarnings({ "unchecked", "rawtypes" })
	@RequestMapping(value="/prj/prj1000/prj1000/deletePrj1001Ajax.do")
    public ModelAndView deletePrj1001Ajax(HttpServletRequest request, HttpServletResponse response, ModelMap model ) throws Exception {
    	try{
    		// request 파라미터를 map으로 변환
        	Map<String, String> paramMap = RequestConvertor.requestParamToMap(request, true);
        	
        	//세션 정보 가져오기
			HttpSession ss = request.getSession();
			LoginVO loginVO = (LoginVO) ss.getAttribute("loginVO");
			
			//userId
			String usrId = (String)loginVO.getUsrId();
			paramMap.put("usrId", usrId);
			
        	String prjGrpCd = (String)paramMap.get("prjGrpCd");
        	
        	//프로젝트 그룹 제거인경우 다른 프로젝트가 존재하는지 체크
        	if("01".equals(prjGrpCd)){
        		paramMap.put("prjGrpId",(String)paramMap.get("prjId"));
        		List<Map> prjGrpIdList = prj1000Service.selectPrj1000PrjGrpIdExistCheck(paramMap);
        		
        		//프로젝트가 존재한다면 제거 실패
        		if(prjGrpIdList.size() > 0){
        			model.addAttribute("delYN", "N");
        			model.addAttribute("message", "권한을 부여받지 못한 프로젝트가 존재합니다.<br>프로젝트 그룹 제거에 실패했습니다.");
        			return new ModelAndView("jsonView");
        		}
        		//프로젝트 그룹 제거
        		else{
        			prj1000Service.deletePrj1000PrjGrpAjax(paramMap);
                	model.addAttribute("message", egovMessageSource.getMessage("success.common.delete"));
                	
                	//세션 재 세팅 - prjList
                	//프로젝트 목록 불러오기
            		List<Map> prjList = (List)prj1000Service.selectPrj1000View(paramMap);
            		
            		//프로젝트 목록 세션 제거
            		ss.removeAttribute("prjList");
            		
            		//세션 재 등록
            		ss.setAttribute("prjList", prjList);
            		
                	return new ModelAndView("jsonView");
        		}
        	}
        	
        	
        	// 프로젝트 제거
        	prj1000Service.deletePrj1001Ajax(paramMap);

        	String strErrCode = (String)paramMap.get("ERR_CODE");
        	String strErrMsg = (String)paramMap.get("ERR_MSG");
        	
        	//삭제 실패시 삭제 구분 N로 세팅하고 메시지 세팅하여 리턴
        	if("-1".equals(strErrCode)){
        		model.addAttribute("delYN", "N");
        		model.addAttribute("message", strErrMsg);
        		return new ModelAndView("jsonView");
        	}
        	
        	//세션 재 세팅 - prjList
        	//전체 조회 위해서 prjGrpCd 제거
        	paramMap.remove("prjGrpCd");
        	//프로젝트 목록 불러오기
    		List<Map> prjList = (List)prj1000Service.selectPrj1000View(paramMap);
    		
    		//프로젝트 목록 세션 제거
    		ss.removeAttribute("prjList");
    		
    		//세션 재 등록
    		ss.setAttribute("prjList", prjList);
    		
        	model.addAttribute("prjId", paramMap.get("prjId"));
        	model.addAttribute("message", egovMessageSource.getMessage("success.common.delete"));
        	
        	return new ModelAndView("jsonView");
    	}
    	catch(Exception ex){
    		Log.error("updatePrj1001Ajax()", ex);
    		model.addAttribute("delYN", "N");
    		model.addAttribute("message", egovMessageSource.getMessage("fail.common.delete"));
    		return new ModelAndView("jsonView");
    	}
    }

	

	@SuppressWarnings({ "unchecked", "rawtypes" })
	@RequestMapping(value="/prj/prj1000/prj1000/selectPrj1000ProjectGroupListAjax.do")
    public ModelAndView selectPrj1000ProjectGroupListAjax(HttpServletRequest request, HttpServletResponse response, ModelMap model ) throws Exception {
    	try{
    		// request 파라미터를 map으로 변환
        	Map<String, String> paramMap = RequestConvertor.requestParamToMap(request, true);
        	
 
    		//로그인VO 가져오기
    		HttpSession ss = request.getSession();
    		LoginVO loginVO = (LoginVO) ss.getAttribute("loginVO");
    		String selPrjGrpId = (String)ss.getAttribute("selPrjGrpId");
    		
    		paramMap.put("licGrpId", loginVO.getLicGrpId());
    		paramMap.put("prjGrpId", selPrjGrpId);
    		List<Map> prjList = prj1000Service.selectPrj1000ProjectGroupListAjax(paramMap);


    		model.addAttribute("prjList", prjList);
    		model.addAttribute("errorYn", 'N');
        	model.addAttribute("message", egovMessageSource.getMessage("success.common.select"));
        	
        	return new ModelAndView("jsonView", model);
    	}
    	catch(Exception ex){
    		Log.error("selectPrj1000ProjectGroupListAjax()", ex);
    		
    		model.addAttribute("errorYn", 'Y');
    		model.addAttribute("message", egovMessageSource.getMessage("fail.common.select"));
    		return new ModelAndView("jsonView");
    	}
    }
	
	/**
	 * 관리 권한 있는 프로젝트 목록 검색
	 * @param 
	 * @return 
	 * @exception Exception
	 */
	@SuppressWarnings({"rawtypes", "unchecked" })
	@RequestMapping(value="/prj/prj1000/prj1000/selectPrj1000AdminPrjList.do")
    public ModelAndView selectPrj1000AdminPrjList(HttpServletRequest request, HttpServletResponse response, ModelMap model ) throws Exception {
    	try{
    		// request 파라미터를 map으로 변환
        	Map<String, String> paramMap = RequestConvertor.requestParamToMap(request, true);
        	
 
    		//로그인VO 가져오기
    		HttpSession ss = request.getSession();
    		LoginVO loginVO = (LoginVO) ss.getAttribute("loginVO");
    		paramMap.put("usrId", loginVO.getUsrId());
    		
    		List<Map> adminPrjList = prj1000Service.selectPrj1000AdminPrjList(paramMap);


    		model.addAttribute("adminPrjList", adminPrjList);
    		model.addAttribute("errorYn", 'N');
        	model.addAttribute("message", egovMessageSource.getMessage("success.common.select"));
        	
        	return new ModelAndView("jsonView", model);
    	}
    	catch(Exception ex){
    		Log.error("selectPrj1000ProjectGroupListAjax()", ex);
    		
    		model.addAttribute("errorYn", 'Y');
    		model.addAttribute("message", egovMessageSource.getMessage("fail.common.select"));
    		return new ModelAndView("jsonView");
    	}
    }
	
	/**
	 * 프로젝트 생성 마법사 - 프로젝트 생성
	 * @param 
	 * @return 
	 * @exception Exception
	 */
	@SuppressWarnings({"rawtypes", "unchecked" })
	@RequestMapping(value="/prj/prj1000/prj1000/insertPrj1000WizardPrjInfo.do")
    public ModelAndView insertPrj1000WizardPrjInfo(HttpServletRequest request, HttpServletResponse response, ModelMap model ) throws Exception {
    	try{
    		// request 파라미터를 map으로 변환
        	Map<String, String> paramMap = RequestConvertor.requestParamToMap(request, true);
        	
 
    		//로그인VO 가져오기
    		HttpSession ss = request.getSession();
    		LoginVO loginVO = (LoginVO) ss.getAttribute("loginVO");
    		paramMap.put("usrId", loginVO.getUsrId());

    		//프로젝트 마법사 생성
    		String newPrjId = (String) prj1000Service.insertPrj1000WizardProject(paramMap);
    		
    		paramMap.remove("selPrjId");
    		paramMap.put("selPrjId",newPrjId);
    		
    		//프로젝트 단건 조회
    		Map prjInfo = (Map) prj1000Service.selectPrj1000Info(paramMap);
    		
    		//세션 재 세팅 - prjList
        	//프로젝트 목록 불러오기
    		List<Map> prjList = (List)prj1000Service.selectPrj1000View(paramMap);
    		
    		//프로젝트 목록 세션 제거
    		ss.removeAttribute("prjList");
    		
    		//세션 재 등록
    		ss.setAttribute("prjList", prjList);
    		
    		model.addAttribute("prjInfo", prjInfo);
    		model.addAttribute("errorYn", 'N');
        	model.addAttribute("message", egovMessageSource.getMessage("success.common.insert"));
        	
        	return new ModelAndView("jsonView", model);
    	}
    	catch(Exception ex){
    		Log.error("insertPrj1000WizardPrjInfo()", ex);
    		
    		model.addAttribute("errorYn", 'Y');
    		model.addAttribute("message", egovMessageSource.getMessage("fail.common.insert"));
    		return new ModelAndView("jsonView");
    	}
    }
}
