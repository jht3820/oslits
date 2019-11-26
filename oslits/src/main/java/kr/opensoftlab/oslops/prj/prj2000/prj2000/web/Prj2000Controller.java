package kr.opensoftlab.oslits.prj.prj2000.prj2000.web;

import java.util.ArrayList;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import kr.opensoftlab.oslits.adm.adm1000.adm1000.web.Adm1000Controller;
import kr.opensoftlab.oslits.com.vo.LoginVO;
import kr.opensoftlab.oslits.prj.prj2000.prj2000.service.Prj2000Service;
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
 * @Class Name : Prj2000Controller.java
 * @Description : Prj2000Controller Controller class
 * @Modification Information
 *
 * @author 정형택
 * @since 2016.01.26.
 * @version 1.0
 * @see
 *  
 *  Copyright (C) OpenSoftLab Corp All right reserved.
 */

@Controller
public class Prj2000Controller {

	/** 로그4j 로거 로딩 */
	private final Logger Log = Logger.getLogger(this.getClass());
	
	/** Prj2000Service DI */
    @Resource(name = "prj2000Service")
    private Prj2000Service prj2000Service;
    
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
	 * Prj2000 화면 이동(이동시 프로젝트에 속한 권한그룹 조회)
	 * @param 
	 * @return 
	 * @exception Exception
	 */
    @SuppressWarnings({ "rawtypes", "unchecked" })
	@RequestMapping(value="/prj/prj2000/prj2000/selectPrj2000View.do")
    public String selectPrj2000View(HttpServletRequest request, HttpServletResponse response, ModelMap model ) throws Exception {
    	
    	try{
    		//로그인VO 가져오기
    		HttpSession ss = request.getSession();
    		LoginVO loginVO = (LoginVO) ss.getAttribute("loginVO");
        	
        	Map paramMap = new HashMap<String, String>();
        	
        	paramMap.put("prjId", ss.getAttribute("selPrjId"));
        	paramMap.put("authGrpId", ss.getAttribute("selAuthGrpId"));
        	paramMap.put("usrId", loginVO.getUsrId());
        	paramMap.put("licGrpId", loginVO.getLicGrpId());
        	
        	//프로젝트에 생성되어 있는 권한그룹 목록 가져오기
        	List<Map> prjAuthGrpList = (List) prj2000Service.selectPrj2000PrjAuthGrpList(paramMap);
        	
        	model.addAttribute("prjAuthGrpList", prjAuthGrpList);
        	
        	return "/prj/prj2000/prj2000/prj2000";
    	}
    	catch(Exception ex){
    		Log.error("selectPrj2000View()", ex);
    		throw new Exception(ex.getMessage());
    	}
    }
    
    
    /**
   	 * Prj2100 화면 이동 (사용자 배정 조회 )
   	 * @param 
   	 * @return 
   	 * @exception Exception
   	 */
    @SuppressWarnings({ "rawtypes", "unchecked" })
   	@RequestMapping(value="/prj/prj2000/prj2100/selectPrj2100View.do")
       public String selectPrj2100View(HttpServletRequest request, HttpServletResponse response, ModelMap model ) throws Exception {
       	
       	try{
       		//로그인VO 가져오기
       		HttpSession ss = request.getSession();
       		LoginVO loginVO = (LoginVO) ss.getAttribute("loginVO");
           	
           	Map paramMap = new HashMap<String, String>();
           	
           	paramMap.put("prjId", ss.getAttribute("selPrjId"));
           	paramMap.put("authGrpId", ss.getAttribute("selAuthGrpId"));
           	paramMap.put("usrId", loginVO.getUsrId());
           	paramMap.put("licGrpId", loginVO.getLicGrpId());
           	
           	//프로젝트에 생성되어 있는 권한그룹 목록 가져오기
           	List<Map> prjAuthGrpList = (List) prj2000Service.selectPrj2000PrjAuthGrpList(paramMap);
           	
           	model.addAttribute("prjAuthGrpList", prjAuthGrpList);
           	
           	return "/prj/prj2000/prj2100/prj2100";
       	}
       	catch(Exception ex){
       		Log.error("selectPrj2100View()", ex);
       		throw new Exception(ex.getMessage());
       	}
       }
    
    /**
	 * Prj2000 권한롤 선택시 해당 롤의 메뉴권한 정보를 가져온다.(대메뉴 정보 및 소메뉴 정보)
	 * @param 
	 * @return 
	 * @exception Exception
	 */
    @SuppressWarnings({ "rawtypes", "unchecked" })
	@RequestMapping(value="/prj/prj2000/prj2000/selectPrj2000AuthGrpMenuListAjax.do")
    public ModelAndView selectAdm1000AuthGrpMenuListAjax(HttpServletRequest request, HttpServletResponse response, ModelMap model ) throws Exception {
    	
    	try{
        	
    		//리퀘스트에서 넘어온 파라미터를 맵으로 세팅
        	Map paramMap = RequestConvertor.requestParamToMap(request, true);
        	
        	HttpSession ss = request.getSession();
       		LoginVO loginVO = (LoginVO) ss.getAttribute("loginVO");
       		paramMap.put("adminYn", loginVO.getAdmYn());
       		paramMap.put("prjId", ss.getAttribute("selPrjId"));
        	//소분류 메뉴 정보 목록 조회
        	List<Map> authGrpSmallMenuList = (List) prj2000Service.selectPrj2000AuthGrpSmallMenuList(paramMap);
        	
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
	 * Prj2001 프로젝트 권한 추가 레이어 팝업 화면 이동
	 * @param 
	 * @return 
	 * @exception Exception
	 */
	@RequestMapping(value="/prj/prj2000/prj2000/selectPrj2001View.do")
    public String selectPrj2001View(HttpServletRequest request, HttpServletResponse response, ModelMap model ) throws Exception {
    	
    	try{
        	
        	return "/prj/prj2000/prj2000/prj2001";
    	}
    	catch(Exception ex){
    		Log.error("selectPrj2000View()", ex);
    		throw new Exception(ex.getMessage());
    	}
    }
	
	/**
	 * Prj2000 신규권한 등록 (단건) AJAX
	 * 신규권한 등록 처리
	 * @param 
	 * @return 
	 * @exception Exception
	 */
	@RequestMapping(value="/prj/prj2000/prj2000/insertPrj2000AuthGrpInfoAjax.do")
    public ModelAndView insertPrj2000AuthGrpInfoAjax(HttpServletRequest request, HttpServletResponse response, ModelMap model ) throws Exception {
    	
    	try{
        	
    		// request 파라미터를 map으로 변환
        	Map<String, String> paramMap = RequestConvertor.requestParamToMapAddSelInfo(request, true);
        	
        	if("admin".equals(paramMap.get("type") ) ){
        		paramMap.put("selPrjId", Adm1000Controller.ROOTSYSTEM_PRJ);
        	}
        	HttpSession ss = request.getSession();
       		LoginVO loginVO = (LoginVO) ss.getAttribute("loginVO");
           	paramMap.put("licGrpId", loginVO.getLicGrpId());
        	        	
        	// 신규 권한 등록
        	String newAuthGrpId = prj2000Service.insertPrj2000AuthGrpInfoAjax(paramMap);
        	model.addAttribute("newAuthGrpId", newAuthGrpId);
        	
        	//등록 성공 메시지 세팅
        	model.addAttribute("message", egovMessageSource.getMessage("success.common.insert"));
        	
        	return new ModelAndView("jsonView");
    	}
    	catch(Exception ex){
    		Log.error("insertAdm1000AuthGrpInfoAjax()", ex);

    		//등록 실패 메시지 세팅 및 저장 성공여부 세팅
    		model.addAttribute("saveYN", "N");
    		model.addAttribute("message", egovMessageSource.getMessage("fail.common.insert"));
    		return new ModelAndView("jsonView");
    	}
    }	
	
	/**
	 * Prj2000 권한정보 삭제(단건) AJAX
	 * 권한정보 삭제 처리
	 * @param 
	 * @return 
	 * @exception Exception
	 */
	@RequestMapping(value="/prj/prj2000/prj2000/deletePrj2000AuthGrpInfoAjax.do")
    public ModelAndView deletePrj2000AuthGrpInfoAjax(HttpServletRequest request, HttpServletResponse response, ModelMap model ) throws Exception {
    	
    	try{
        	
    		// request 파라미터를 map으로 변환
        	Map<String, String> paramMap = RequestConvertor.requestParamToMapAddSelInfo(request, true);
        	HttpSession ss = request.getSession();
       		LoginVO loginVO = (LoginVO) ss.getAttribute("loginVO");
           	paramMap.put("licGrpId", loginVO.getLicGrpId());
           	
        	// 메뉴 삭제
        	prj2000Service.deletePrj2000AuthGrpInfoAjax(paramMap);
        	
        	//등록 성공 메시지 세팅
        	model.addAttribute("message", egovMessageSource.getMessage("success.common.delete"));
        	
        	return new ModelAndView("jsonView");
    	}
    	catch(Exception ex){
    		Log.error("deletePrj2000AuthGrpInfoAjax()", ex);

    		//삭제실패 메시지 세팅 및 저장 성공여부 세팅
    		model.addAttribute("saveYN", "N");
    		model.addAttribute("message", egovMessageSource.getMessage("fail.common.delete"));
    		return new ModelAndView("jsonView");
    	}
    }
	
	/**
	 * Prj2000 권한롤 배정 메뉴권한을 저장한다.
	 * @param 
	 * @return 
	 * @exception Exception
	 */
    @SuppressWarnings({ "rawtypes", "unchecked" })
	@RequestMapping(value="/prj/prj2000/prj2000/savePrj2000AuthGrpMenuAuthListAjax.do")
    public ModelAndView savePrj2000AuthGrpMenuAuthListAjax(HttpServletRequest request, HttpServletResponse response, ModelMap model ) throws Exception {
    	try{
    		/**
    		 * 	hidden + menuId + 컬럼명 으로 넘어오는 인풋 정보를 메뉴ID 별로 컬럼값 세팅하여
    		 * 	리스트로 담은 후 저장 처리함.
    		 */
    		List<Map> list = new ArrayList<Map>();
        	Enumeration enu = request.getParameterNames();
        	String authGrpId = request.getParameter("menuAuthGrpId");
        	String mainMenuId = request.getParameter("mainMenuId");
                	
        	String tempMenuId = "";
        	String strStatusMenuId = "";
    		String strStatus = "";
    		
        	Map chkMap = new HashMap();
        	
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
        		}
        		
        		//hidden으로 담아온 체크박스 체크여부 값을 key value로 세팅해 list에 담는다.
        		if( "hidden".equals(strKeys.substring(0,6)) ){
        			menuId = strKeys.substring(6,18);
        			colNm = strKeys.substring(18);
        			
        			//이전 저장된 menuId와 지금 꺼낸 메뉴 ID가 같으면 한맵에 컬럼 정보로 담는다.
        			if(tempMenuId.equals(menuId)){
        				chkMap.put(colNm, request.getParameter("hidden" + menuId + colNm));
        				
        				//컬럼명이 printYn 이면 마지막 컬럼 이므로 맵을 리스트에 저장한다.
        				if(colNm.equals("printYn")){
        					chkMap.put("authGrpId", authGrpId);
        					chkMap.put("mainMenuId", mainMenuId);
        					list.add(chkMap);
        					System.out.println(list);
        				}
        			}
        			//이전 저장된 menuId와 다르면 새로운 메뉴정보 이므로 새로이 맵을 만들어서 menuId를 맵에 넣는다.
        			else{
        				chkMap = new HashMap();
        				
        				//기본정보 생성된 맵에 붙이기
        				RequestConvertor.mapAddCommonInfo(request, chkMap);
        				
        				if(menuId.equals(strStatusMenuId)){
        					chkMap.put("status", strStatus);
        				}
        				
        				//메뉴정보 세팅
        				chkMap.put("menuId", menuId);
        				chkMap.put(colNm, request.getParameter("hidden" + menuId + colNm));
        			}
        			tempMenuId = menuId;
        		}
        	}

        	//메뉴권한정보 저장 처리
        	prj2000Service.savePrj2000AuthGrpMenuAuthListAjax(list);
        	
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
	 * Prj2000 권한롤 선택시 해당 롤의 배정된 사용자 및 전체 사용자 목록을 가져온다.
	 * @param 
	 * @return 
	 * @exception Exception
	 */
    @SuppressWarnings({ "rawtypes" })
	@RequestMapping(value="/prj/prj2000/prj2000/selectPrj2000UsrAddListAjax.do")
    public ModelAndView selectPrj2000UsrAddListAjax(HttpServletRequest request, HttpServletResponse response, ModelMap model ) throws Exception {
    	
    	try{
        	
    		//리퀘스트에서 넘어온 파라미터를 맵으로 세팅
        	Map paramMap = RequestConvertor.requestParamToMap(request, true);
        	
        	//배정 사용자 및 전체 사용자 목록 조회
        	List prjUsrAddListMap = (List) prj2000Service.selectPrj2000UsrAddListAjax(paramMap);
        	List prjUsrAllListMap = (List) prj2000Service.selectPrj2000UsrAllListAjax(paramMap);
        	
        	model.addAttribute("prjUsrAddListMap", prjUsrAddListMap);
        	model.addAttribute("prjUsrAllListMap", prjUsrAllListMap);
        	
        	//조회성공메시지 세팅
        	model.addAttribute("message", egovMessageSource.getMessage("success.common.select"));
        	
        	return new ModelAndView("jsonView");
    	}
    	catch(Exception ex){
    		Log.error("selectPrj2000UsrAddListAjax()", ex);
    		
    		//조회실패 메시지 세팅
    		model.addAttribute("message", egovMessageSource.getMessage("fail.common.select"));
    		return new ModelAndView("jsonView");
    	}
    }
    
    /**
	 * Prj2000 권한롤 저장시 해당 롤의 배정된 사용자 및 전체 사용자 목록을 가져온다.
	 * @param 
	 * @return 
	 * @exception Exception
	 */
    @SuppressWarnings({ "rawtypes" })
	@RequestMapping(value="/prj/prj2000/prj2000/savePrj2000PrjUsrAuthListAjax.do")
    public ModelAndView savePrj2000PrjUsrAuthListAjax(HttpServletRequest request, HttpServletResponse response, ModelMap model ) throws Exception {
    	
    	try{
        	
    		//리퀘스트에서 넘어온 파라미터를 맵으로 세팅
        	Map paramMap = RequestConvertor.requestParamToMap(request, true);
        	
        	String status = (String) paramMap.get("status");
        	
        	//삭제 처리일때만
        	if("D".equals(status)){
	         	//현재 프로젝트에 권한그룹으로 등록된 사용자가 1명일경우 삭제 불가능
	           	int authUsrCnt = prj2000Service.selectPrj2000UsrCntAjax(paramMap);
	           	
	           	if(authUsrCnt == 1){
	           	//삭제실패 메시지 세팅 및 저장 성공여부 세팅
	        		model.addAttribute("saveYN", "N");
	        		model.addAttribute("message", egovMessageSource.getMessage("fail.common.prjAuthGrpUsrChk"));
	        		return new ModelAndView("jsonView");
	           	}
           	}
        	//추가, 삭제 처리
        	prj2000Service.savePrj2000PrjUsrAuthListAjax(paramMap);
        	
    		//정상적으로 추가, 삭제 처리가 되었다면 아래 목록을 조회하여 뿌린다.
        	//배정 사용자 및 전체 사용자 목록 조회
        	List prjUsrAddListMap = (List) prj2000Service.selectPrj2000UsrAddListAjax(paramMap);
        	List prjUsrAllListMap = (List) prj2000Service.selectPrj2000UsrAllListAjax(paramMap);

        	model.addAttribute("prjUsrAddListMap", prjUsrAddListMap);
        	model.addAttribute("prjUsrAllListMap", prjUsrAllListMap);

        	String searchSelect = (String)paramMap.get("searchSelect");
        	String searchTxt = (String)paramMap.get("searchTxt");
        	
        	// 배정 삭제시 사용자의 정보를 조회한다.
        	if(status != null && searchSelect != null && searchTxt != null){
        		if("D".equals(status) && !"rn".equals(searchSelect) && !"".equals(searchTxt)){
        			List prjDelUsrListMap = (List) prj2000Service.selectPrj2000DelUsrInfoListAjax(paramMap);
                	model.addAttribute("prjDelUsrListMap", prjDelUsrListMap);
            	}
        	}
        	
        	//성공메시지 세팅
        	model.addAttribute("saveYN", "Y");
        	model.addAttribute("message", egovMessageSource.getMessage("success.common.save"));
        	
        	return new ModelAndView("jsonView");
    	}
    	catch(Exception ex){
    		Log.error("savePrj2000PrjUsrAuthListAjax()", ex);
    		
    		//저장실패 메시지 세팅
    		model.addAttribute("saveYN", "N");
    		model.addAttribute("message", egovMessageSource.getMessage("fail.common.save"));
    		return new ModelAndView("jsonView");
    	}
    }
    /**
     * 권한 그룹 상세 조회
     * @param request
     * @param response
     * @param model
     * @return
     * @throws Exception
     */
    @SuppressWarnings({ "rawtypes", "unchecked" })
   	@RequestMapping(value="/prj/prj2000/prj2100/selectPrj2000AuthGrpInfoAjax.do")
       public ModelAndView selectPrj2000AuthGrpInfoAjax(HttpServletRequest request, HttpServletResponse response, ModelMap model ) throws Exception {
       	
       	try{
       		//로그인VO 가져오기
       		HttpSession ss = request.getSession();
       		LoginVO loginVO = (LoginVO) ss.getAttribute("loginVO");
           	
       		Map paramMap = RequestConvertor.requestParamToMap(request, true);
       		if("admin".equals(paramMap.get("type") ) ){
        		paramMap.put("prjId", Adm1000Controller.ROOTSYSTEM_PRJ);
        	}else{
        		paramMap.put("prjId", ss.getAttribute("selPrjId"));
        	}
           	paramMap.put("usrId", loginVO.getUsrId());
           	paramMap.put("licGrpId", loginVO.getLicGrpId());
           	
           	//프로젝트에 생성되어 있는 권한그룹 목록 가져오기
           	Map prjAuthGrpInfo = (Map) prj2000Service.selectPrj2000AuthGrpInfoAjax(paramMap);
           	
           	model.addAttribute("prjAuthGrpInfo", prjAuthGrpInfo);
           	
          //조회성공메시지 세팅
        	model.addAttribute("message", egovMessageSource.getMessage("success.common.select"));
        	
        	return new ModelAndView("jsonView");
    	}
    	catch(Exception ex){
    		Log.error("selectPrj2000UsrAddListAjax()", ex);
    		
    		//조회실패 메시지 세팅
    		model.addAttribute("message", egovMessageSource.getMessage("fail.common.select"));
    		return new ModelAndView("jsonView");
    	}
    }
    /**
     * 권한 그룹 상세 수정
     * @param request
     * @param response
     * @param model
     * @return
     * @throws Exception
     */
    @RequestMapping(value="/prj/prj2000/prj2000/updatePrj2000AuthGrpInfoAjax.do")
    public ModelAndView updatePrj2000AuthGrpInfoAjax(HttpServletRequest request, HttpServletResponse response, ModelMap model ) throws Exception {
    	
    	try{
        	
    		// request 파라미터를 map으로 변환
    		HttpSession ss = request.getSession();
       		LoginVO loginVO = (LoginVO) ss.getAttribute("loginVO");
           	
       		Map paramMap = RequestConvertor.requestParamToMap(request, true);
           	
       		if("admin".equals(paramMap.get("type") ) ){
        		paramMap.put("prjId", Adm1000Controller.ROOTSYSTEM_PRJ);
        	}else{
        		paramMap.put("prjId", ss.getAttribute("selPrjId"));
        	}
           	
           	paramMap.put("usrId", loginVO.getUsrId());
           	paramMap.put("licGrpId", loginVO.getLicGrpId());
        	
        	prj2000Service.updatePrj2000AuthGrpInfoAjax(paramMap);
        	
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
}
