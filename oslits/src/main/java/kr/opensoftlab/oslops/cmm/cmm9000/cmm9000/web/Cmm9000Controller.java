package kr.opensoftlab.oslops.cmm.cmm9000.cmm9000.web;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import kr.opensoftlab.oslops.arm.arm1000.arm1000.service.Arm1000Service;
import kr.opensoftlab.oslops.cmm.cmm4000.cmm4000.service.Cmm4000Service;
import kr.opensoftlab.oslops.cmm.cmm9000.cmm9000.service.Cmm9000Service;
import kr.opensoftlab.oslops.com.util.AuthMainPageConvertor;
import kr.opensoftlab.oslops.com.vo.LoginVO;
import kr.opensoftlab.oslops.prj.prj1000.prj1000.service.Prj1000Service;
import kr.opensoftlab.sdf.jenkins.JenkinsClient;
import kr.opensoftlab.sdf.util.ModuleUseCheck;
import kr.opensoftlab.sdf.util.RequestConvertor;

import org.apache.log4j.Logger;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;

import egovframework.com.cmm.EgovMessageSource;
import egovframework.com.utl.fcc.service.EgovStringUtil;
import egovframework.rte.fdl.property.EgovPropertyService;

/**
 * @Class Name : Cmm9000Controller.java
 * @Description : Cmm9000Controller Controller class
 * @Modification Information
 *
 * @author 정형택
 * @since 2016.01.20.
 * @version 1.0
 * @see
 *  
 *  Copyright (C)  All right reserved.
 */

@Controller
public class Cmm9000Controller {

	/** 로그4j 로거 로딩 */
	private static final Logger Log = Logger.getLogger(Cmm9000Controller.class);
	
	/** Cmm4000Service DI */
    @Resource(name = "cmm4000Service")
    private Cmm4000Service cmm4000Service;
    
	/** Cmm9000Service DI */
    @Resource(name = "cmm9000Service")
    private Cmm9000Service cmm9000Service;  
    
    /** Prj1000Service DI */
    @Resource(name = "prj1000Service")
    private Prj1000Service prj1000Service;
    
    /** arm1000Service DI */
    @Resource(name = "arm1000Service")
    private Arm1000Service arm1000Service;

	/** ModuleUseCheck DI */
	@Resource(name = "moduleUseCheck")
	private ModuleUseCheck moduleUseCheck;
	
    /** EgovMessageSource */
	@Resource(name = "egovMessageSource")
	EgovMessageSource egovMessageSource;

	/** EgovPropertyService */
	@Resource(name = "propertiesService")
	protected EgovPropertyService propertiesService;

	/**
	 * Cmm9000 메인화면 이동 (권한 별 페이지 이동)
	 * @param 
	 * @return 
	 * @exception Exception
	 */
	@RequestMapping(value="/cmm/cmm9000/cmm9000/selectCmm9000MainMove.do")
    public String selectCmm9000MainMove(ModelMap model, HttpServletRequest request, HttpServletResponse response) throws Exception {
    	try{
    		
    		//결과 URL
    		String strRsltUrl = "/index";
    		
    		//세션 가져오기
    		HttpSession ss = request.getSession();

    		//main Url로 이동
    		strRsltUrl = "redirect:"+ss.getAttribute("selMainUrl");
    		
    		//ss.setAttribute("selMainUrl", strRsltUrl);
    		
        	return strRsltUrl;
        	
    	}catch(Exception ex){
    		Log.error("selectCmm9000MainMove()", ex);
    		throw new Exception(ex.getMessage());
    	}
    }
    
    /**
	 * Cmm9000 선택한 프로젝트로 변경
	 * @param 
	 * @return 
	 * @exception Exception
	 */
    @SuppressWarnings({ "unchecked", "rawtypes" })
	@RequestMapping(value="/cmm/cmm9000/cmm9000/selectCmm9000PrjChgView.do")
    public String selectCmm9000PrjChgView(ModelMap model, HttpServletRequest request, HttpServletResponse response) throws Exception {
    	try{
    		
    		//결과 URL
    		String strRsltUrl = "/index";
    		
    		//로그인VO 가져오기
    		HttpSession ss = request.getSession();
    		LoginVO loginVO = (LoginVO) ss.getAttribute("loginVO");
    		
    		Map prjMap = new HashMap();
    		
    		String prjId = request.getParameter("prjId");
    		
    		if(prjId != null && "prjInsert".equals(prjId)){
    			return "/prj/prj1000/prj1000/prj1000";
    		}
    		
    		prjMap.put("usrId", loginVO.getUsrId());
    		prjMap.put("prjId", prjId);
    		
    		//선택한 프로젝트의 PRJ_ID로 해당 프로젝트의 권한목록을 가져온다.
    		List<Map> authList = (List) cmm4000Service.selectCmm4000UsrPrjAuthList(prjMap);
    		if(authList != null){
    			ss.setAttribute("usrTyp", authList.get(0).get("usrTyp"));
    		}
    		
    		//우선순위가 가장 높은 프로젝트와 권한롤의 PRJ_ID, AUTH_GRP_ID, LIC_GRP_ID 값 세팅하여 해당 프로젝트권한의 메뉴권한정보를 가져온다.
    		Map paramMap = new HashMap<String, String>();
    		paramMap.put("usrId", loginVO.getUsrId());
    		paramMap.put("prjId", authList.get(0).get("prjId"));
    		paramMap.put("authGrpId", authList.get(0).get("authGrpId"));
    		paramMap.put("licGrpId", loginVO.getLicGrpId());
    		
    		paramMap.put("adminYn", loginVO.getAdmYn());
    		
    		List<Map> menuList = (List) cmm4000Service.selectCmm4000MenuList(paramMap);

        	//모듈 사용 체크
    		menuList = moduleUseCheck.moduleUseMenuList(menuList,request);

    		//프로젝트 단건 조회
    		paramMap.put("selPrjId", request.getParameter("prjId"));
    		Map selPrjInfo = prj1000Service.selectPrj1000Info(paramMap);
    		ss.setAttribute("selPrjGrpId", selPrjInfo.get("prjGrpId"));
    		
    		//선택한 프로젝트의 권한롤 및 메뉴정보 세션 저장
    		ss.setAttribute("authList", authList);
    		ss.setAttribute("menuList", menuList);
    		
    		//선택한 프로젝트 및 권한 id 저장
    		ss.setAttribute("selPrjId", request.getParameter("prjId"));
    		
    		//프로젝트 정보 조회
    		paramMap.put("selPrjId", prjId);
    		Map prjInfo = prj1000Service.selectPrj1000Info(paramMap);
    		ss.setAttribute("selPrjTaskTypeCd", prjInfo.get("prjTaskTypeCd"));
    		ss.setAttribute("selPrjTaskTypeNm", prjInfo.get("prjTaskTypeNm"));
    		
    		ss.setAttribute("selAuthGrpId", authList.get(0).get("authGrpId"));
    		ss.setAttribute("selMainAuthGrpId", authList.get(0).get("mainAuthGrpId"));
    		boolean isMain = false;
    		for (Map menuMap : menuList) {
    			if("Y".equals(  menuMap.get("mainYn") ) ){
    				ss.setAttribute("selMenuId", menuMap.get("menuId"));
    	    		ss.setAttribute("selMenuNm", menuMap.get("menuNm")); 
    	    		ss.setAttribute("selMenuUrl", menuMap.get("menuUrl"));
    	    		ss.setAttribute("selMainUrl", menuMap.get("menuUrl"));
    	    		
    	    		// 프로젝트 변경 시 mainYn 값에 따른 메인화면 변경시
    	    		// 메인화면으로 img 클릭시  왼쪽 title에 사용하기 위해 세션에 저장
    	    		ss.setAttribute("firstMenuNm", menuMap.get("menuNm"));

    	    		ss.setAttribute("selAcceptUseCd", menuMap.get("acceptUseCd"));
    	    		
    	    		isMain = true;
    			}
				
			}
    		if(!isMain){
    			
    			List mainMenuList = cmm4000Service.selectCmm4000LoginMainMenuList(paramMap);
    			String strMenuNm = "";
    			String strMenuId = "";
    			String strMenuUrl = "";
    			
    			if(mainMenuList.size()>1){
    				strMenuNm=(String) ((Map)mainMenuList.get(1)).get("menuNm");
    				strMenuId=(String) ((Map)mainMenuList.get(1)).get("menuId");
    				strMenuUrl=(String) ((Map)mainMenuList.get(1)).get("menuUrl");
    				
    				// 권한별 최초 시작메뉴, 메인화면으로 img 클릭 왼쪽 title에 사용하기 위해 세션에 저장
    				ss.setAttribute("selMenuId", strMenuId);
    	    		ss.setAttribute("selMenuNm", strMenuNm); 
    	    		ss.setAttribute("selMenuUrl", strMenuUrl);
    	    		ss.setAttribute("selMainUrl", strMenuUrl);
    	    		ss.setAttribute("firstMenuNm", strMenuNm);	
    	    		
    	    		ss.setAttribute("selAcceptUseCd", ((Map)mainMenuList.get(1)).get("acceptUseCd"));
    			}else{
    				strMenuNm=(String) ((Map)mainMenuList.get(0)).get("menuNm");
    				strMenuId=(String) ((Map)mainMenuList.get(0)).get("menuId");
    				strMenuUrl=(String) ((Map)mainMenuList.get(0)).get("menuUrl");
    				
    				// 권한별 최초 시작메뉴, 메인화면으로 img 클릭 왼쪽 title에 사용하기 위해 세션에 저장
    				ss.setAttribute("selMenuId", strMenuId);
    	    		ss.setAttribute("selMenuNm", strMenuNm); 
    	    		ss.setAttribute("selMenuUrl", strMenuUrl);
    	    		ss.setAttribute("selMainUrl", strMenuUrl);
    				// 권한별 최초 시작메뉴, 메인화면으로 img 클릭 왼쪽 title에 사용하기 위해 세션에 저장
    	    		ss.setAttribute("firstMenuNm", strMenuNm);
    	    		
    	    		ss.setAttribute("selAcceptUseCd", ((Map)mainMenuList.get(0)).get("acceptUseCd"));
    			}
    		}
    		
    		/*
    		//권한에 따라 메인화면 지정
    		strRsltUrl = AuthMainPageConvertor.authMainPageConvert((String) ss.getAttribute("selMainAuthGrpId"));
    		*/
    		strRsltUrl = "redirect:"+ss.getAttribute("selMainUrl");
        	return strRsltUrl;
        	
    	}catch(Exception ex){
    		Log.error("selectCmm9000PrjChgView()", ex);
    		throw new Exception(ex.getMessage());
    	}
    }
	/**
	 * Cmm9000 선택한 권한으로 변경
	 * @param 
	 * @return 
	 * @exception Exception
	 */
    @SuppressWarnings({ "unchecked", "rawtypes" })
	@RequestMapping(value="/cmm/cmm9000/cmm9000/selectCmm9000AuthGrpChgView.do")
    public String selectCmm9000AuthGrpChgView(ModelMap model, HttpServletRequest request, HttpServletResponse response) throws Exception {
    	try{
    		
    		//결과 URL
    		String strRsltUrl = "/index";
    		
    		//로그인VO 가져오기
    		HttpSession ss = request.getSession();
    		LoginVO loginVO = (LoginVO) ss.getAttribute("loginVO");
    		
    		//우선순위가 가장 높은 프로젝트와 권한롤의 PRJ_ID, AUTH_GRP_ID, LIC_GRP_ID 값 세팅하여 해당 프로젝트권한의 메뉴권한정보를 가져온다.
    		Map paramMap = new HashMap<String, String>();
    		paramMap.put("usrId", loginVO.getUsrId());
    		paramMap.put("prjId", ss.getAttribute("selPrjId"));
    		paramMap.put("authGrpId", request.getParameter("authGrpId"));
    		paramMap.put("licGrpId", loginVO.getLicGrpId());
    		paramMap.put("adminYn", loginVO.getAdmYn()) ;
    		
    		List<Map> menuList = (List) cmm4000Service.selectCmm4000MenuList(paramMap);
    		
    		//선택한 프로젝트의 권한롤 및 메뉴정보 세션 저장
    		ss.setAttribute("menuList", menuList);
    		
    		//선택한 프로젝트 및 권한 id 저장
    		String selAuthGrpId = (String)request.getParameter("authGrpId");
    		ss.setAttribute("selAuthGrpId", selAuthGrpId);
    		
    		//권한그룹 타입 변경
    		List<Map> authList = (List)ss.getAttribute("authList");
    		if(authList != null){
    			for(Map authInfo : authList){
    				if(selAuthGrpId.equals(authInfo.get("authGrpId"))){
    					ss.setAttribute("usrTyp", authInfo.get("usrTyp"));
    					break;
    				}
    			}
    		}
    		
    		boolean isMain = false;
    		for (Map menuMap : menuList) {
    			if("Y".equals(  menuMap.get("mainYn") ) ){
    				ss.setAttribute("selMenuId", menuMap.get("menuId"));
    	    		ss.setAttribute("selMenuNm", menuMap.get("menuNm"));
    	    		ss.setAttribute("selMenuUrl", menuMap.get("menuUrl"));
    	    		ss.setAttribute("selMainUrl", menuMap.get("menuUrl"));
    	    		
    	    		// 우측에서 권한 변경 시 mainYn 값에 따른 메인화면 변경시
    	    		// 메인화면으로 img 클릭시  왼쪽 title에 사용하기 위해 세션에 저장
    	    		ss.setAttribute("firstMenuNm", menuMap.get("menuNm"));
    	    		
    	    		ss.setAttribute("selAcceptUseCd", menuMap.get("acceptUseCd"));
    	    		
    	    		isMain = true;
    			}
				
			}
    		if(!isMain){
    			
    			List mainMenuList = cmm4000Service.selectCmm4000LoginMainMenuList(paramMap);
    			String strMenuNm = "";
    			String strMenuId = "";
    			String strMenuUrl = "";
    			
    			if(mainMenuList.size()>1){
    				strMenuNm=(String) ((Map)mainMenuList.get(1)).get("menuNm");
    				strMenuId=(String) ((Map)mainMenuList.get(1)).get("menuId");
    				strMenuUrl=(String) ((Map)mainMenuList.get(1)).get("menuUrl");
    				
    				// 권한별 최초 시작메뉴, 메인화면으로 img 클릭 왼쪽 title에 사용하기 위해 세션에 저장
    				ss.setAttribute("selMenuId", strMenuId);
    	    		ss.setAttribute("selMenuNm", strMenuNm); 
    	    		ss.setAttribute("selMenuUrl", strMenuUrl);
    	    		ss.setAttribute("selMainUrl", strMenuUrl);
    				// 권한별 최초 시작메뉴, 메인화면으로 img 클릭 왼쪽 title에 사용하기 위해 세션에 저장
    	    		ss.setAttribute("firstMenuNm", strMenuNm);	
    	    		
    	    		ss.setAttribute("selAcceptUseCd", ((Map)mainMenuList.get(1)).get("acceptUseCd"));
    			}else{
    				strMenuNm=(String) ((Map)mainMenuList.get(0)).get("menuNm");
    				strMenuId=(String) ((Map)mainMenuList.get(0)).get("menuId");
    				strMenuUrl=(String) ((Map)mainMenuList.get(0)).get("menuUrl");
    				
    				// 권한별 최초 시작메뉴, 메인화면으로 img 클릭 왼쪽 title에 사용하기 위해 세션에 저장
    				ss.setAttribute("selMenuId", strMenuId);
    	    		ss.setAttribute("selMenuNm", strMenuNm); 
    	    		ss.setAttribute("selMenuUrl", strMenuUrl);
    	    		ss.setAttribute("selMainUrl", strMenuUrl);
    				// 권한별 최초 시작메뉴, 메인화면으로 img 클릭 왼쪽 title에 사용하기 위해 세션에 저장
    	    		ss.setAttribute("firstMenuNm", strMenuNm);
    	    		
    	    		ss.setAttribute("selAcceptUseCd", ((Map)mainMenuList.get(0)).get("acceptUseCd"));
    			}
    		}
    		/*
    		//권한에 따라 메인화면 지정
    		strRsltUrl = AuthMainPageConvertor.authMainPageConvert((String) ss.getAttribute("selMainAuthGrpId"));
    		*/
    		strRsltUrl = "redirect:"+ss.getAttribute("selMainUrl");
    		//ss.setAttribute("selMainUrl", strRsltUrl);
    		
        	return strRsltUrl;
        	
    	}catch(Exception ex){
    		Log.error("selectCmm9000AuthGrpChgView()", ex);
    		throw new Exception(ex.getMessage());
    	}
    }
    
    /**
	 * Cmm9000 선택한 메뉴로 변경
	 * @param 
	 * @return 
	 * @exception Exception
	 */
    @SuppressWarnings({ "rawtypes", "unchecked" })
	@RequestMapping(value="/cmm/cmm9000/cmm9000/selectCmm9000MenuChgView.do")
    public String selectCmm9000MenuChgView(ModelMap model, HttpServletRequest request, HttpServletResponse response) throws Exception {
    	try{
    		
    		//결과 URL
    		String strRsltUrl = request.getParameter("menuUrl");
    		
    		//메뉴 URL이 비어 있으면 최초 페이지로 보낸다.
    		if(strRsltUrl == null){
    			return "forward:/cmm/cmm4000/cmm4000/selectCmm4000LoginAfter.do";
    		}
    		
    		//세션에 선택한 메뉴 정보 담기
    		HttpSession ss = request.getSession();
    		ss.setAttribute("selMenuId", request.getParameter("menuId"));
    		ss.setAttribute("selMenuNm", request.getParameter("menuNm"));
    		ss.setAttribute("selMenuUrl", request.getParameter("menuUrl"));
    		
    		List<Map> menuList = (List) ss.getAttribute("menuList");
    		String selMenuId = EgovStringUtil.nullConvert((String) ss.getAttribute("selMenuId"));
    		
    		//메뉴 권한이 있는지 체크
    		boolean menuAuthChk = false;
    		
    		//menuList가 있으면 선택한 메뉴의 버튼 권한을 세팅한다.
			if(menuList != null){
				for(Map menuInfo : menuList){
					String menuId = (String) menuInfo.get("menuId");
					if(selMenuId.equals(menuId)){
						menuAuthChk = true;

						// 선택한 메뉴의 이름을 세팅한다.
						ss.setAttribute("selMenuNm", menuInfo.get("menuNm"));
						ss.setAttribute("selBtnAuthSearchYn", menuInfo.get("selectYn"));
						ss.setAttribute("selBtnAuthInsertYn", menuInfo.get("regYn"));
						ss.setAttribute("selBtnAuthUpdateYn", menuInfo.get("modifyYn"));
						ss.setAttribute("selBtnAuthDeleteYn", menuInfo.get("delYn"));
						ss.setAttribute("selBtnAuthExcelYn", menuInfo.get("excelYn"));
						ss.setAttribute("selBtnAuthPrintYn", menuInfo.get("printYn"));
					}
				}
			}
    		//menuList에서 해당 메뉴 권한을 찾지 못한 경우 최초 페이지로 보낸다.
			if(!menuAuthChk){
				return "forward:/cmm/cmm4000/cmm4000/selectCmm4000LoginAfter.do";
			}
        	return "forward:" + strRsltUrl;
        	
    	}catch(Exception ex){
    		Log.error("selectCmm9000MenuChgView()", ex);
    		throw new Exception(ex.getMessage());
    	}
    }
    
    /**
	 * Cmm9000 왼쪽 메뉴 영역에 표시할 각종 정보 건수를 조회(알림, 담당요구사항, 전체요구사항, 개발주기별 요구사항)
	 * @param 
	 * @return 
	 * @exception Exception
	 */
    @SuppressWarnings({ "rawtypes"})
	@RequestMapping(value="/cmm/cmm9000/cmm9000/selectCmm9000LeftMenuInfos.do")
    public ModelAndView selectCmm9000LeftMenuInfos(HttpServletRequest request, HttpServletResponse response, ModelMap model) throws Exception {
    	
    	Map rtnMap = new HashMap();
    	
    	try{
    		// request 파라미터를 map으로 변환
        	Map<String, String> paramMap = RequestConvertor.requestParamToMapAddSelInfo(request, true);
        	
    		//왼쪽 메뉴 영역에 표시할 각종 정보 건수를 조회(알림, 담당요구사항, 전체요구사항, 개발주기별 요구사항)
    		rtnMap = cmm9000Service.selectCmm9000LeftMenuInfos(paramMap);
    		
    		//세션에 선택한 메뉴 정보 담기
    		HttpSession ss = request.getSession();
    		LoginVO loginVo = (LoginVO)ss.getAttribute("loginVO");
    		
    		//사용자 Id 가져오기
    		String usrId = loginVo.getUsrId();
    		paramMap.put("usrId", usrId);
    		
    		Map alarmCnt = arm1000Service.selectArm1000AlarmCnt(paramMap);
    		
    		model.addAttribute("rtnMap", rtnMap);
    		model.addAttribute("alarmCnt", alarmCnt);
    		return new ModelAndView("jsonView");
    		
    	}catch(Exception ex){
    		Log.error("selectCmm9000LeftMenuInfos()", ex);
    		model.addAttribute("errorYn", "Y");
			model.addAttribute("message", egovMessageSource.getMessage("fail.common.select"));
    		return new ModelAndView("jsonView");
    	}
    }

}
