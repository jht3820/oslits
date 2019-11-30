package kr.opensoftlab.oslops.adm.adm1000.adm1000.web;

import java.util.ArrayList;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import kr.opensoftlab.oslops.adm.adm1000.adm1000.service.Adm1000Service;
import kr.opensoftlab.oslops.com.vo.LoginVO;
import kr.opensoftlab.sdf.jenkins.JenkinsClient;
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
 * @Class Name : Adm1000Controller.java
 * @Description : Adm1000Controller Controller class
 * @Modification Information
 *
 * @author 정형택
 * @since 2016.01.12.
 * @version 1.0
 * @see
 *  
 *  Copyright (C) OpenSoftLab Corp All right reserved.
 */

@Controller
public class Adm1000Controller {

	/** 로그4j 로거 로딩 */
	private static final Logger Log = Logger.getLogger(Adm1000Controller.class);
	
	/** Adm1000Service DI */
    @Resource(name = "adm1000Service")
    private Adm1000Service adm1000Service;
    
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
    
	public final static String ROOTSYSTEM_PRJ = "ROOTSYSTEM_PRJ";
	
    /**
	 * Adm1000 화면 이동(이동시 메뉴 정보 목록 조회)
	 * @param 
	 * @return 
	 * @exception Exception
	 */
    @SuppressWarnings({ "rawtypes", "unchecked" })
	@RequestMapping(value="/adm/adm1000/adm1000/selectAdm1000View.do")
    public String selectAdm1000View(HttpServletRequest request, HttpServletResponse response, ModelMap model ) throws Exception {
    	
    	try{
    		//로그인VO 가져오기
    		HttpSession ss = request.getSession();
    		LoginVO loginVO = (LoginVO) ss.getAttribute("loginVO");
        	
        	Map paramMap = new HashMap<String, String>();
        	
        	paramMap.put("prjId", ROOTSYSTEM_PRJ);
        	paramMap.put("authGrpId", ss.getAttribute("selAuthGrpId"));
        	paramMap.put("usrId", loginVO.getUsrId());
        	paramMap.put("licGrpId", loginVO.getLicGrpId());
        	
        	//라이선스 그룹에 할당된 메뉴목록 가져오기
        	List<Map> baseMenuList = (List) adm1000Service.selectAdm1000BaseMenuList(paramMap);
        	
        	model.addAttribute("baseMenuList", baseMenuList);
        	return "/adm/adm1000/adm1000/adm1000";
    	}
    	catch(Exception ex){
    		Log.error("selectAdm1000View()", ex);
    		throw new Exception(ex.getMessage());
    	}
    }
    
    /**
	 * Adm1001 권한그룹 추가 팝업 화면 이동
	 * @param 
	 * @return 
	 * @exception Exception
	 */
	@SuppressWarnings("rawtypes")
	@RequestMapping(value="/adm/adm1000/adm1000/selectAdm1001View.do")
    public String selectAdm1001View(HttpServletRequest request, HttpServletResponse response, ModelMap model ) throws Exception {
    	
		//리퀘스트에서 넘어온 파라미터를 맵으로 세팅
		Map<String, String> paramMap = RequestConvertor.requestParamToMap(request, true);
		Map selInfo = null;
		
    	try{
    		if( paramMap.get("gb").equals("update") ) {
    			paramMap.put("prjId", paramMap.get("selPrjId"));
    			selInfo = adm1000Service.selectAdm1000AuthGrpInfoAjax(paramMap);
    		}
    		model.put("selInfo", selInfo);
        	return "/adm/adm1000/adm1000/adm1001";
    	}
    	catch(Exception ex){
    		Log.error("selectAdm1001View()", ex);
    		throw new Exception(ex.getMessage());
    	}
    }
    
    /**
	 * Adm1000 메뉴정보 조회(단건) AJAX
	 * @param 
	 * @return 
	 * @exception Exception
	 */
    @SuppressWarnings({ "rawtypes", "unchecked" })
	@RequestMapping(value="/adm/adm1000/adm1000/selectAdm1000MenuInfoAjax.do")
    public ModelAndView selectAdm1000MenuInfoAjax(HttpServletRequest request, HttpServletResponse response, ModelMap model ) throws Exception {
    	
    	try{
        	
    		// request 파라미터를 map으로 변환
        	Map<String, String> paramMap = RequestConvertor.requestParamToMap(request, true);
        	
        	//메뉴정보조회
        	Map<String, String> menuInfoMap = (Map) adm1000Service.selectAdm1000MenuInfo(paramMap);
        	
        	//조회성공메시지 세팅
        	model.addAttribute("message", egovMessageSource.getMessage("success.common.select"));
        	
        	return new ModelAndView("jsonView", menuInfoMap);
    	}
    	catch(Exception ex){
    		Log.error("selectAdm1000MenuInfoAjax()", ex);

    		//조회실패 메시지 세팅
    		model.addAttribute("message", egovMessageSource.getMessage("fail.common.select"));
    		return new ModelAndView("jsonView");
    	}
    }
    
    /**
	 * Adm1000 Ajax 메뉴 정보 목록 조회
	 * @param 
	 * @return 
	 * @exception Exception
	 */
    @SuppressWarnings({ "rawtypes", "unchecked" })
	@RequestMapping(value="/adm/adm1000/adm1000/selectAdm1000MenuListAjax.do")
    public ModelAndView selectAdm1000MenuListAjax(HttpServletRequest request, HttpServletResponse response, ModelMap model ) throws Exception {
    	
    	try{
        	
    		//리퀘스트에서 넘어온 파라미터를 맵으로 세팅
        	Map paramMap = RequestConvertor.requestParamToMap(request);
        	HttpSession ss = request.getSession();
    		LoginVO loginVO = (LoginVO) ss.getAttribute("loginVO");
        	
        	paramMap.put("adminYn", loginVO.getAdmYn());
        	
        	//라이선스 그룹에 할당된 메뉴목록 가져오기
        	List<Map> baseMenuList = (List) adm1000Service.selectAdm1000BaseMenuList(paramMap);

        	//모듈 사용 체크
        	baseMenuList = moduleUseCheck.moduleUseMenuList(baseMenuList, request);
        	
        	paramMap.put("baseMenuList", baseMenuList);
        	
        	//조회성공메시지 세팅
        	model.addAttribute("message", egovMessageSource.getMessage("success.common.select"));
        	
        	return new ModelAndView("jsonView", paramMap);
    	}
    	catch(Exception ex){
    		Log.error("selectAdm1000MenuInfo()", ex);
    		
    		//조회실패 메시지 세팅
    		model.addAttribute("message", egovMessageSource.getMessage("fail.common.select"));
    		return new ModelAndView("jsonView");
    	}
    }
    
    /**
	 * Adm1000 권한롤 선택시 해당 롤의 메뉴권한 정보를 가져온다.(대메뉴 정보 및 소메뉴 정보)
	 * @param 
	 * @return 
	 * @exception Exception
	 */
    @SuppressWarnings({ "rawtypes", "unchecked" })
	@RequestMapping(value="/adm/adm1000/adm1000/selectAdm1000AuthGrpMenuListAjax.do")
    public ModelAndView selectAdm1000AuthGrpMenuListAjax(HttpServletRequest request, HttpServletResponse response, ModelMap model ) throws Exception {
    	
    	try{
        	
    		//리퀘스트에서 넘어온 파라미터를 맵으로 세팅
        	Map paramMap = RequestConvertor.requestParamToMap(request, true);
        	HttpSession ss = request.getSession();
    		LoginVO loginVO = (LoginVO) ss.getAttribute("loginVO");
    		
    		paramMap.put("adminYn", loginVO.getAdmYn());
    		//String prjId	= (String) ss.getAttribute("selPrjId");
			//paramMap.put("prjId", prjId);
    		paramMap.put("prjId", ROOTSYSTEM_PRJ);
        	//소분류 메뉴 정보 목록 조회
        	List<Map> authGrpSmallMenuList = (List) adm1000Service.selectAdm1000AuthGrpSmallMenuList(paramMap);

        	//모듈 사용 체크
        	authGrpSmallMenuList = moduleUseCheck.moduleUseMenuList(authGrpSmallMenuList, request);
        	
        	model.addAttribute("authGrpSmallMenuList", authGrpSmallMenuList);
        	
        	//조회성공메시지 세팅
        	model.addAttribute("message", egovMessageSource.getMessage("success.common.select"));
        	
        	return new ModelAndView("jsonView");
    	}
    	catch(Exception ex){
    		Log.error("selectAdm1000MenuInfo()", ex);
    		
    		//조회실패 메시지 세팅
    		model.addAttribute("message", egovMessageSource.getMessage("fail.common.select"));
    		return new ModelAndView("jsonView");
    	}
    }
    
    /**
	 * Adm1000 권한롤 배정 메뉴권한을 저장한다.
	 * @param 
	 * @return 
	 * @exception Exception
	 */
    @SuppressWarnings({ "rawtypes", "unchecked" })
	@RequestMapping(value="/adm/adm1000/adm1000/saveAdm1000AuthGrpMenuAuthListAjax.do")
    public ModelAndView saveAdm1000AuthGrpMenuAuthListAjax(HttpServletRequest request, HttpServletResponse response, ModelMap model ) throws Exception {
    	try{
    		/**
    		 * 	hidden + menuId + 컬럼명 으로 넘어오는 인풋 정보를 메뉴ID 별로 컬럼값 세팅하여
    		 * 	리스트로 담은 후 저장 처리함.
    		 */
    		List<Map> list = new ArrayList<Map>();
        	Enumeration enu = request.getParameterNames();
        	String authGrpId = request.getParameter("menuAuthGrpId");
        	String tempMenuId = "";
        	String strStatusMenuId = "";
    		String strStatus = "";
    		
    		Map<String, Map> menuIdMap = new HashMap<String, Map>();
    		
        	while(enu.hasMoreElements()){
        		String strKeys = (String) enu.nextElement();
        		String menuId = "";
        		String colNm = "";
        		
        		//파라미터네임이 18자 이하면 다음 파라미터로
        		if(strKeys.length() < 18){
        			continue;
        		}

        		//상태구분값을 가져온다.
        		if("status".equals(strKeys.substring(0,6))){
        			strStatusMenuId = strKeys.substring(6,18);
        			strStatus = request.getParameter(strKeys);
        			
        			//menuId 존재하지 않는 경우
        			if(!menuIdMap.containsKey(strStatusMenuId)) {
        				//menuId 정보 생성
        				Map<String, String> menuDataMap = new HashMap<String, String>();
        				
        				//기본정보 생성된 맵에 붙이기
        				RequestConvertor.mapAddCommonInfo(request, menuDataMap);
        				
        				//관리자이기에 prjId 는 ROOTSYSTEM_PRJ로 세팅한다.
        				menuDataMap.put("prjId", "ROOTSYSTEM_PRJ");
        				
        				//정보 추가 (2depth Map)
        				menuIdMap.put(strStatusMenuId, menuDataMap);
        				
        				//menuId, mainMenuId, authGrpId put
        				menuIdMap.get(strStatusMenuId).put("authGrpId", authGrpId);
        				menuIdMap.get(strStatusMenuId).put("menuId", strStatusMenuId);
        			}
        			
        			//status
        			menuIdMap.get(strStatusMenuId).put("status", strStatus);
        		}
        		
        		
        		//hidden으로 담아온 체크박스 체크여부 값을 key value로 세팅해 list에 담는다.
        		if( "hidden".equals(strKeys.substring(0,6)) ){
        			menuId = strKeys.substring(6,18);
        			colNm = strKeys.substring(18);
        			
        			//menuId 존재하지 않는 경우
        			if(!menuIdMap.containsKey(menuId)) {
        				//menuId 정보 생성
        				Map<String, String> menuDataMap = new HashMap<String, String>();
        				
        				//기본정보 생성된 맵에 붙이기
        				RequestConvertor.mapAddCommonInfo(request, menuDataMap);
        				
        				//관리자이기에 prjId 는 ROOTSYSTEM_PRJ로 세팅한다.
        				menuDataMap.put("prjId", "ROOTSYSTEM_PRJ");
        				
        				//정보 추가 (2depth Map)
        				menuIdMap.put(menuId, menuDataMap);
        				
        				//menuId, mainMenuId, authGrpId put
        				menuIdMap.get(menuId).put("authGrpId", authGrpId);
        				menuIdMap.get(menuId).put("menuId", menuId);
        			}
        			
        			//수정된 값 put
        			menuIdMap.get(menuId).put(colNm, request.getParameter("hidden" + menuId + colNm));
        			
        			/*
        			//이전 저장된 menuId와 지금 꺼낸 메뉴 ID가 같으면 한맵에 컬럼 정보로 담는다.
        			if(tempMenuId.equals(menuId)){
        				chkMap.put(colNm, request.getParameter("hidden" + menuId + colNm));
        				
        				//컬럼명이 printYn 이면 마지막 컬럼 이므로 맵을 리스트에 저장한다.
        				if(colNm.equals("printYn")){
        					chkMap.put("authGrpId", authGrpId);
        					list.add(chkMap);
        					System.out.println(list);
        				}
        			}
        			//이전 저장된 menuId와 다르면 새로운 메뉴정보 이므로 새로이 맵을 만들어서 menuId를 맵에 넣는다.
        			else{
        				chkMap = new HashMap();
        				
        				//기본정보 생성된 맵에 붙이기
        				RequestConvertor.mapAddCommonInfo(request, chkMap);
        				
        				//관리자이기에 prjId 는 ROOTSYSTEM_PRJ로 세팅한다.
        				chkMap.put("prjId", "ROOTSYSTEM_PRJ");
        				
        				if(menuId.equals(strStatusMenuId)){
        					chkMap.put("status", strStatus);
        				}
        				
        				//메뉴정보 세팅
        				chkMap.put("menuId", menuId);
        				chkMap.put(colNm, request.getParameter("hidden" + menuId + colNm));
        			}
        			tempMenuId = menuId;
        			*/
        		}
        	}

        	//Map to List
        	for(Entry<String, Map> mapInfo : menuIdMap.entrySet()) {
        		Map newMap = mapInfo.getValue();
        		list.add(newMap);
        	}
        	
        	//메뉴권한정보 저장 처리
        	adm1000Service.saveAdm1000AuthGrpMenuAuthListAjax(list);
        	
        	//조회성공메시지 세팅
        	model.addAttribute("message", egovMessageSource.getMessage("success.common.save"));
        	
        	return new ModelAndView("jsonView");
    	}
    	catch(Exception ex){
    		Log.error("saveAdm1000AuthGrpMenuAuthListAjax()", ex);
    		
    		//조회실패 메시지 세팅
    		model.addAttribute("message", egovMessageSource.getMessage("fail.common.save"));
    		return new ModelAndView("jsonView");
    	}
    }
    
    /**
	 * Adm1000 메뉴정보 등록(단건) AJAX
	 * 메뉴정보 등록후 등록 정보 결과 및 정보를 화면으로 리턴한다.
	 * @param 
	 * @return 
	 * @exception Exception
	 */
    @SuppressWarnings({ "rawtypes", "unchecked" })
	@RequestMapping(value="/adm/adm1000/adm1000/insertAdm1000MenuInfoAjax.do")
    public ModelAndView insertAdm1000MenuInfoAjax(HttpServletRequest request, HttpServletResponse response, ModelMap model ) throws Exception {
    	
    	try{
        	
    		// request 파라미터를 map으로 변환
        	Map<String, String> paramMap = RequestConvertor.requestParamToMap(request, true);
        	
        	// 메뉴 등록
        	Map<String, String> menuInfoMap = (Map) adm1000Service.insertAdm1000MenuInfo(paramMap);
        	
        	//등록 성공 메시지 세팅
        	model.addAttribute("message", egovMessageSource.getMessage("success.common.insert"));
        	
        	return new ModelAndView("jsonView", menuInfoMap);
    	}
    	catch(Exception ex){
    		Log.error("selectAdm1000MenuInfoAjax()", ex);

    		//조회실패 메시지 세팅 및 저장 성공여부 세팅
    		model.addAttribute("saveYN", "N");
    		model.addAttribute("message", egovMessageSource.getMessage("fail.common.insert"));
    		return new ModelAndView("jsonView");
    	}
    }
    
    /**
	 * Adm1000 메뉴정보 삭제(단건) AJAX
	 * 메뉴정보 삭제 처리
	 * @param 
	 * @return 
	 * @exception Exception
	 */
	@RequestMapping(value="/adm/adm1000/adm1000/deleteAdm1000MenuInfoAjax.do")
    public ModelAndView deleteAdm1000MenuInfoAjax(HttpServletRequest request, HttpServletResponse response, ModelMap model ) throws Exception {
    	
    	try{
        	
    		// request 파라미터를 map으로 변환
        	Map<String, String> paramMap = RequestConvertor.requestParamToMap(request, true);
        	
        	// 메뉴 삭제
        	adm1000Service.deleteAdm1000MenuInfo(paramMap);
        	
        	//등록 성공 메시지 세팅
        	model.addAttribute("message", egovMessageSource.getMessage("success.common.delete"));
        	
        	return new ModelAndView("jsonView");
    	}
    	catch(Exception ex){
    		Log.error("deleteAdm1000MenuInfoAjax()", ex);

    		//삭제실패 메시지 세팅 및 저장 성공여부 세팅
    		model.addAttribute("saveYN", "N");
    		model.addAttribute("message", egovMessageSource.getMessage("fail.common.delete"));
    		return new ModelAndView("jsonView");
    	}
    }
	
	/**
	 * Adm1000 메뉴정보 수정(단건) AJAX
	 * 메뉴정보 수정 처리
	 * @param 
	 * @return 
	 * @exception Exception
	 */
	@RequestMapping(value="/adm/adm1000/adm1000/updateAdm1000MenuInfoAjax.do")
    public ModelAndView updateAdm1000MenuInfoAjax(HttpServletRequest request, HttpServletResponse response, ModelMap model ) throws Exception {
    	
    	try{
        	
    		// request 파라미터를 map으로 변환
        	Map<String, String> paramMap = RequestConvertor.requestParamToMap(request, true);
        	HttpSession ss = request.getSession();
    		//LoginVO loginVO = (LoginVO) ss.getAttribute("loginVO");
    		
    		String prjId	= (String) ss.getAttribute("selPrjId");
			paramMap.put("prjId", prjId);
        	// 메뉴 수정
        	adm1000Service.updateAdm1000MenuInfo(paramMap);
        	
        	//등록 성공 메시지 세팅
        	model.addAttribute("message", egovMessageSource.getMessage("success.common.update"));
        	
        	return new ModelAndView("jsonView");
    	}
    	catch(Exception ex){
    		Log.error("updateAdm1000MenuInfoAjax()", ex);

    		//수정 실패 메시지 세팅 및 저장 성공여부 세팅
    		model.addAttribute("saveYN", "N");
    		model.addAttribute("message", egovMessageSource.getMessage("fail.common.update"));
    		return new ModelAndView("jsonView");
    	}
    }
    	
	/**
	 * Adm1000 신규권한 등록 (단건) AJAX
	 * 신규권한 등록 처리
	 * @param 
	 * @return 
	 * @exception Exception
	 */
	@SuppressWarnings("rawtypes")
	@RequestMapping(value="/adm/adm1000/adm1000/insertAdm1000AuthGrpInfoAjax.do")
    public ModelAndView insertAdm1000AuthGrpInfoAjax(HttpServletRequest request, HttpServletResponse response, ModelMap model ) throws Exception {
    	
    	try{
        	
    		// request 파라미터를 map으로 변환
        	Map<String, String> paramMap = RequestConvertor.requestParamToMapAddSelInfo(request, true);
        	
        	//동일 권한 있는지 체크
        	Map dupChkMap = adm1000Service.selectAdm1000DupChkAuthGrpId(paramMap);
        	
        	//동일 권한롤 있을시 화면으로 보냄.
        	if("Y".equals(dupChkMap.get("dupYn"))){
        		//등록 실패 메시지 세팅 및 저장 성공여부 세팅
        		model.addAttribute("saveYN", "N");
        		model.addAttribute("message", egovMessageSource.getMessage("fail.common.dupid"));
        		return new ModelAndView("jsonView");
        	}
        	
        	// 신규 권한 등록
        	adm1000Service.insertAdm1000AuthGrpInfoAjax(paramMap);
        	
        	//등록 성공 메시지 세팅
        	model.addAttribute("message", egovMessageSource.getMessage("success.common.update"));
        	
        	return new ModelAndView("jsonView");
    	}
    	catch(Exception ex){
    		Log.error("insertAdm1000AuthGrpInfoAjax()", ex);

    		//등록 실패 메시지 세팅 및 저장 성공여부 세팅
    		model.addAttribute("saveYN", "N");
    		model.addAttribute("message", egovMessageSource.getMessage("fail.common.update"));
    		return new ModelAndView("jsonView");
    	}
    }	
	
	/**
	 * Adm1000 권한정보 삭제(단건) AJAX
	 * 메뉴정보 삭제 처리
	 * @param 
	 * @return 
	 * @exception Exception
	 */
	@RequestMapping(value="/adm/adm1000/adm1000/deleteAdm1000AuthGrpInfoAjax.do")
    public ModelAndView deleteAdm1000AuthGrpInfoAjax(HttpServletRequest request, HttpServletResponse response, ModelMap model ) throws Exception {
    	
    	try{
        	
    		// request 파라미터를 map으로 변환
        	Map<String, String> paramMap = RequestConvertor.requestParamToMapAddSelInfo(request, true);
        	HttpSession ss = request.getSession();
    		LoginVO loginVO = (LoginVO) ss.getAttribute("loginVO");
        	
        
        	paramMap.put("licGrpId", loginVO.getLicGrpId());
        	// 권한그룹 삭제
        	adm1000Service.deleteAdm1000AuthGrpInfoAjax(paramMap);
        	
        	//등록 성공 메시지 세팅
        	model.addAttribute("message", egovMessageSource.getMessage("success.common.delete"));
        	
        	return new ModelAndView("jsonView");
    	}
    	catch(Exception ex){
    		Log.error("deleteAdm1000MenuInfoAjax()", ex);

    		//삭제실패 메시지 세팅 및 저장 성공여부 세팅
    		model.addAttribute("saveYN", "N");
    		model.addAttribute("message", egovMessageSource.getMessage("fail.common.delete"));
    		return new ModelAndView("jsonView");
    	}
    }
	
	 /**
		 * 권한 그룹 목록 조회
		 * @param 
		 * @return 
		 * @exception Exception
		 */
	@SuppressWarnings({ "rawtypes", "unchecked" })
	@RequestMapping(value="/adm/adm1000/adm1000/selectAdm1000PrjAuthGrpList.do")
	public ModelAndView selectAdm1000PrjAuthGrpList(HttpServletRequest request, HttpServletResponse response, ModelMap model ) throws Exception {
	   	
		try{
	    	HttpSession ss = request.getSession();
	    	LoginVO loginVO = (LoginVO) ss.getAttribute("loginVO");
	        	
	       	Map paramMap = new HashMap<String, String>();
	        	
	       	paramMap.put("prjId", ROOTSYSTEM_PRJ);
	       	paramMap.put("authGrpId", ss.getAttribute("selAuthGrpId"));
	       	paramMap.put("usrId", loginVO.getUsrId());
	       	paramMap.put("licGrpId", loginVO.getLicGrpId());
	        	
	       	//프로젝트에 생성되어 있는 권한그룹 목록 가져오기
	       	List<Map> prjAuthGrpList = (List) adm1000Service.selectAdm1000PrjAuthGrpList(paramMap);
	        	
	       	model.addAttribute("prjAuthGrpList", prjAuthGrpList);
	        	
	        	
	       	//조회성공메시지 세팅
	       	model.addAttribute("message", egovMessageSource.getMessage("success.common.select"));
	        	
	       	return new ModelAndView("jsonView", paramMap);
	    }
	    catch(Exception ex){
	    	Log.error("selectAdm1000PrjAuthGrpList()", ex);
	    		
	    	//조회실패 메시지 세팅
	    	model.addAttribute("message", egovMessageSource.getMessage("fail.common.select"));
	    	return new ModelAndView("jsonView");
	    }
	}
}
