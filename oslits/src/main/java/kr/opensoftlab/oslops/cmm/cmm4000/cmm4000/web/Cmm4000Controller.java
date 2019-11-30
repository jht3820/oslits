package kr.opensoftlab.oslops.cmm.cmm4000.cmm4000.web;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Random;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.apache.log4j.Logger;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.servlet.ModelAndView;

import egovframework.com.cmm.EgovMessageSource;
import egovframework.com.cmm.listener.SesssionEventListener;
import egovframework.com.cmm.service.EgovProperties;
import egovframework.com.cop.ems.service.EgovSndngMailService;
import egovframework.com.cop.ems.service.SndngMailVO;
import egovframework.com.utl.sim.service.EgovFileScrty;
import egovframework.rte.fdl.cmmn.trace.LeaveaTrace;
import egovframework.rte.fdl.property.EgovPropertyService;
import kr.opensoftlab.oslops.adm.adm5000.adm5000.service.Adm5000Service;
import kr.opensoftlab.oslops.adm.adm5000.adm5000.vo.Adm5000VO;
import kr.opensoftlab.oslops.adm.adm5000.adm5200.service.Adm5200Service;
import kr.opensoftlab.oslops.cmm.cmm4000.cmm4000.service.Cmm4000Service;
import kr.opensoftlab.oslops.cmm.cmm4000.cmm4000.service.impl.Cmm4000ServiceImpl;
import kr.opensoftlab.oslops.com.vo.LicVO;
import kr.opensoftlab.oslops.com.vo.LoginVO;
import kr.opensoftlab.oslops.prj.prj1000.prj1000.service.Prj1000Service;
import kr.opensoftlab.sdf.util.ModuleUseCheck;
import kr.opensoftlab.sdf.util.RequestConvertor;

/**
 * @Class Name : Cmm4000Controller.java
 * @Description : Cmm4000Controller Controller class
 * @Modification Information
 *
 * @author 정형택
 * @since 2013.11.06.
 * @version 1.0
 * @see
 *  
 *  Copyright (C)  All right reserved.
 */

@Controller
public class Cmm4000Controller {

	/** 로그4j 로거 로딩 */
	private static final Logger Log = Logger.getLogger(Cmm4000Controller.class);
	
	/** Cmm4000Service DI */
    @Resource(name = "cmm4000Service")
    private Cmm4000Service cmm4000Service;
    
    /** Adm5000Service DI */
    @Resource(name = "adm5000Service")
    private Adm5000Service adm5000Service;
    
    /** Adm5200Service DI */
    @Resource(name = "adm5200Service")
    private Adm5200Service adm5200Service;
    
    /** Prj1000Service DI */
    @Resource(name = "prj1000Service")
    private Prj1000Service prj1000Service;
    
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
	
	/** EgovSndngMailService */
	@Resource(name = "egovSndngMailService")
	private EgovSndngMailService egovSndngMailService;
	
	/** 이메일 옵션 properties */
	//인증번호 재 발송 대기시간
	private static final int ReSendTime = Integer.parseInt(EgovProperties.getProperty("Globals.mail.reSend"));
	//인증번호 세션 만료 시간
	private static final int SessionTime = Integer.parseInt(EgovProperties.getProperty("Globals.mail.sessionTime"));
	
	private final static String SUCCESS = "SUCCESS";// 로그인 성공
	
	private final static String FAIL  = "FAIL";// 로그인 실패
	
    /**
	 * Cmm4000 화면 이동
	 * @param 
	 * @return 
	 * @exception Exception
	 */
    @RequestMapping(value="/cmm/cmm4000/cmm4000/selectCmm4000View.do")
    public String selectCmm4000View(Model model) throws Exception {
		model.addAttribute("joinCheck", EgovProperties.getProperty("Globals.lunaops.userJoin"));
    	return "/cmm/cmm4000/cmm4000/cmm4000";
    }
    
   
    	
	/**
     * Cmm4000 로그아웃 처리 
     * @desc 1. 로그아웃 id, pw 체크
     *  	 2. 로그아웃 성공시 loginVO 세션에 세팅
     *  		- /cmm/cmm4000/cmm4000/selectCmm4000LoginAfter.do 후처리 실행
     *  	 3. 로그아웃 실패시 로그인 페이지로 보냄
     * 
     * @param request
     * @param response
     * @throws Exception
     */
	@RequestMapping(value="/cmm/cmm4000/cmm4000/selectCmm4000Logout.do")
	public String selectCmm4000Logout(HttpServletRequest request, HttpServletResponse response, ModelMap model)	throws Exception {
		try{
			// request 파라미터를 map으로 변환
        	//Map<String, String> param = RequestConvertor.requestParamToMap(request, true);
        	
			
			LoginVO loginVO = (LoginVO) request.getSession().getAttribute("loginVO");
			
			
			//로그아웃 시간 업데이트 로직 삽입 			
        	Adm5000VO adm5000vo = new Adm5000VO();
        	adm5000vo.setLicGrpId(loginVO.getLicGrpId());
        	adm5000vo.setLoginUsrId(loginVO.getUsrId());
        	
        	adm5000Service.updateAdm5000AuthLogoutLog(adm5000vo);
        	
        	//로그아웃 처리 세션 해제후 로그인 페이지로 강제이동
			request.getSession().invalidate();

			/* 로그아웃 여부 세팅 */
    		model.addAttribute("logoutYn", "Y");
			model.addAttribute("message", egovMessageSource.getMessage("cmm4000.success.logout"));
			model.addAttribute("joinCheck", EgovProperties.getProperty("Globals.lunaops.userJoin"));
        	return "/cmm/cmm4000/cmm4000/cmm4000";
        	
    	}catch(Exception ex){
    		Log.error("selectCmm4000Logout()", ex);
    		throw new Exception(ex.getMessage());
    	}
    }
    
    /**
     * Cmm4000 로그인 처리 
     * @desc 1. 로그인 id, pw 체크
     *  	 2. 로그인 성공시 loginVO 세션에 세팅
     *  		- /cmm/cmm4000/cmm4000/selectCmm4000LoginAfter.do 후처리 실행
     *  	 3. 로그인 실패시 로그인 페이지로 보냄
     * 
     * @param request
     * @param response
     * @throws Exception
     */
	@RequestMapping(method=RequestMethod.POST, value="/cmm/cmm4000/cmm4000/selectCmm4000LoginAction.do")
	public String selectCmm4000LoginAction(@ModelAttribute("loginVO") LoginVO loginVO, HttpServletRequest request, HttpServletResponse response, ModelMap model)	throws Exception {
		
    	try{
    		// request 파라미터를 map으로 변환
        	Map<String, String> param = RequestConvertor.requestParamToMap(request, true);
        	
        	/* 파라미터 로그찍기 */    
    		if (Log.isDebugEnabled()) {
    			Log.debug("\n=====> parameter map : " + param);
    		}

    		//로그인 사용자 정보 조회
    		String strUsrIp = request.getRemoteAddr();
    		loginVO.setModifyUsrIp(strUsrIp);
    		
    		
    		// 비밀번호 초기화 시 새로 입력한 비밀번호 세팅
    		if(param.get("initPassYn") != null 
    				&& param.get("initPassYn").equals( "Y" )   ){
    			cmm4000Service.updateCmm4000AccountInit(param);
    			
    		}
    		
    		LoginVO rtnLoginVO = cmm4000Service.selectCmm4000LoginAction(loginVO);
    		
    		/* 중복로그인 확인후 로그인 진행  */
    		String duplicationLoginOption = EgovProperties.getProperty("Globals.lunaops.duplicationLoginOption");
    		
    		if("Y".equals(duplicationLoginOption)){
    			if(param.get("loginSessionYn") != null && "Y".equals(param.get("loginSessionYn"))){
        			if(rtnLoginVO.getLoginStatus()==Cmm4000ServiceImpl.LOGIN_SUCCESS ){
        				rtnLoginVO.setLoginStatus(Cmm4000ServiceImpl.DUPULICATE_USER_LOGIN);
        			}
        		}
    		}
    		
    		String returnPage="";
    		switch(rtnLoginVO.getLoginStatus()){
    			case Cmm4000ServiceImpl.LOGIN_SUCCESS :
    				request.getSession().setAttribute("loginVO", rtnLoginVO);
    				boolean isDuplicate = false;
    				SesssionEventListener listener = null;
    				String sessionId ="";
    				/* 중복로그인 확인후 로그인 진행  */
    				if("Y".equals(duplicationLoginOption)){
    					listener = SesssionEventListener.getInstance();
    					sessionId = request.getSession().getId();
        	    		isDuplicate= listener.isDuplicateLogin(sessionId, loginVO.getUsrId());
    				}
    				
    	    		if(isDuplicate){
    	    			model.addAttribute("loginSessionYn", "Y");
    	    			model.addAttribute("usrId", param.get("usrId"));    	    			
    	    			model.addAttribute("usrPw", param.get("usrPw"));
                		model.addAttribute("message", egovMessageSource.getMessage("common.sessionclose.msg"));
    	    			returnPage ="/cmm/cmm4000/cmm4000/cmm4000";
    	    		}else{
    	    			//로그인 로그 삽입
    	    			returnPage = insertAdm5000AuthLoginLog(rtnLoginVO, request, SUCCESS);
                    	
    	    			rtnLoginVO.setModifyUsrId(rtnLoginVO.getUsrId());
    					rtnLoginVO.setModifyUsrIp(loginVO.getModifyUsrIp());		
    	    		}
                	
    				break;
    			case Cmm4000ServiceImpl.NO_SEARCH_ID :
    				model.addAttribute("loginYn", "N");
    				model.addAttribute("message", egovMessageSource.getMessage("fail.common.login"));
    				returnPage ="/cmm/cmm4000/cmm4000/cmm4000";
    				break;
    			case Cmm4000ServiceImpl.ACCOUNT_LOCK :
    				model.addAttribute("loginYn", "N");
            		model.addAttribute("message", egovMessageSource.getMessage("fail.common.accountLock"));
            		insertAdm5000AuthLoginLog(rtnLoginVO, request, FAIL);
    				returnPage ="/cmm/cmm4000/cmm4000/cmm4000";
    				break;
    			case Cmm4000ServiceImpl.WRONG_PASSWORD :
    				model.addAttribute("loginYn", "N");
    				
    				Object[] params = new Object[2];
    				params[0] = rtnLoginVO.getPwFailCnt();
    				params[1] = Cmm4000ServiceImpl.MAX_WRONG_PASSWORD_LIMIT_COUNT;
    				model.addAttribute("message", egovMessageSource.getMessage("fail.common.login"));
            		insertAdm5000AuthLoginLog(rtnLoginVO, request, FAIL);
    				returnPage ="/cmm/cmm4000/cmm4000/cmm4000";
    				break;
    			case Cmm4000ServiceImpl.NO_USE_USER :
    				
    				model.addAttribute("loginYn", "N");
            		model.addAttribute("message", egovMessageSource.getMessage("fail.common.usecd"));
            		insertAdm5000AuthLoginLog(rtnLoginVO, request, FAIL);
    				returnPage ="/cmm/cmm4000/cmm4000/cmm4000";
    				break; 
    			case Cmm4000ServiceImpl.NOT_USED_3_MONTHS :
    				
    				model.addAttribute("loginYn", "N");
            		model.addAttribute("message", egovMessageSource.getMessage("fail.common.notUsed3Month"));
            		insertAdm5000AuthLoginLog(rtnLoginVO, request, FAIL);
    				returnPage ="/cmm/cmm4000/cmm4000/cmm4000";
    				break;
    			case Cmm4000ServiceImpl.PW_EXPORED :
    				
    				model.addAttribute("loginYn", "N");
    				model.addAttribute("usrId", rtnLoginVO.getUsrId());
    				model.addAttribute("licGrpId", rtnLoginVO.getLicGrpId());
    				model.addAttribute("exprYn", "Y");
            		model.addAttribute("message", egovMessageSource.getMessage("fail.common.pwExpired"));
            		insertAdm5000AuthLoginLog(rtnLoginVO, request, FAIL);
    				returnPage ="/cmm/cmm4000/cmm4000/cmm4000";
    				break; 		
    			case Cmm4000ServiceImpl.DUPULICATE_USER_LOGIN:
    				listener = SesssionEventListener.getInstance();
    	    		sessionId = request.getSession().getId();
    	    		isDuplicate= listener.closeDuplicateSession(sessionId, loginVO.getUsrId());
    				returnPage = insertAdm5000AuthLoginLog(rtnLoginVO, request, SUCCESS);
    				break;
    			case Cmm4000ServiceImpl.INITIAL_ACCOUNT_WAIT_MINUTE_AFTER:
    				model.addAttribute("loginYn", "N");
    				Object[] initparams = new Object[1];
    				initparams[0] = (Cmm4000ServiceImpl.INITIAL_ACCOUNT_WAIT_MINUTE/60);
            		model.addAttribute("message", egovMessageSource.getMessage("fail.common.initimeblock",initparams));
            		insertAdm5000AuthLoginLog(rtnLoginVO, request, FAIL);
            		returnPage ="/cmm/cmm4000/cmm4000/cmm4000";
    				break;
    			case Cmm4000ServiceImpl.INITIAL_PASSWORD_CHANGE :
    				model.addAttribute("usrId", rtnLoginVO.getUsrId());
    				model.addAttribute("licGrpId", rtnLoginVO.getLicGrpId());
    				model.addAttribute("iniYn", "Y");
    				returnPage ="/cmm/cmm4000/cmm4000/cmm4000";
    				break;
    			default :
    				
    				break;
    		}
    		
    		return returnPage;
    	}catch(Exception ex){
    		Log.error("selectCmm4000LoginAction()", ex);
    		throw new Exception(ex.getMessage());
    	}
	}
	
	/**
	 * 로그인 이력을 기록한다.
	 * @param loginVO
	 * @param request
	 * @return
	 * @throws Exception
	 */
	public String insertAdm5000AuthLoginLog( LoginVO loginVO ,HttpServletRequest request ,String loginStatus) throws Exception{
		
		Adm5000VO adm5000vo = new Adm5000VO();
    	adm5000vo.setLicGrpId(loginVO.getLicGrpId());
    	adm5000vo.setLoginUsrId(loginVO.getUsrId());
    	adm5000vo.setLoginIp(request.getRemoteAddr());
    	
    	adm5000vo.setLoginStatus(loginStatus);
    	
    	adm5000Service.insertAdm5000AuthLoginLog(adm5000vo);
    	return "forward:/cmm/cmm4000/cmm4000/selectCmm4000LoginAfter.do";
	}
	
	/**
     * Cmm4000 로그인 성공후 처리 
     * @Desc 로그인 성공이후 액션 사용자의 권한 및 메뉴, 메인화면 선택 등의 후처리.
     * 		 1. 사용자 그룹 라이선스 체크(라이선스가 만료라면 라이선스 결제 페이지 강제 이동)
     * 		 2. 프로젝트 존재 여부 체크
     * 			프로젝트가 없거나 만료면 프로젝트 생성 페이지로 강제 이동 
     * 			- usrId + '_GRP' == licGrpId 같은 사람만
     * 			- 나머지는 '프로덕트 오너에게 프로젝트 생성 문의하세요.' 메시지 뿌림
     *       3. 사용자 롤에 따른 메뉴 권한체크 및 세팅
     *       4. 사용자 롤에 따른 메인화면 세팅
     *         
     * @param request
     * @param response
     * @throws Exception
     */
	@SuppressWarnings({ "rawtypes", "unchecked" })
	@RequestMapping(value="/cmm/cmm4000/cmm4000/selectCmm4000LoginAfter.do")
	public String selectCmm4000LoginAfter(ModelMap model, HttpServletRequest request, HttpServletResponse response)	throws Exception {
		
    	try{
    		//활성화 상태인 라이선스 존재 여부
    		boolean isActLic = false;
    		
    		//프로젝트 존재 여부
    		boolean isPrj = false;
    		
    		    		
    		HttpSession ss = request.getSession();
    		LoginVO loginVO = (LoginVO) ss.getAttribute("loginVO");
   	
   
    		//활성화 상태인 라이선스 존재 여부 체크
    		isActLic = cmm4000LicChk(request, response, loginVO);
    		
    		//프로젝트 존재 여부 체크
    		isPrj = cmm4000PrjChk(request, response, loginVO);
    		
    		//라이선스 활성화일 경우 프로젝트가 생성되지 않았다면 최초등록자만 프로젝트 강제 생성 페이지로 이동
    		//아닌경우는 로그인화면으로 이동시키고 얼럿 메시지 띄움.
    		if(isActLic){
    			if(!isPrj){
    				//프로젝트 존재 여부 N으로 세팅 및 메시지 세팅
        			model.addAttribute("isPrjYn", "N");
        			
        			//로그인한 사용자가 라이선스 최초 등록자이면 
        			//프로젝트 생성 페이지로 강제 이동 시킴. usrId + '_GRP' == licGrpId 같은 사람이 최초등록자.
        			if( (loginVO.getUsrId() + "_GRP").equals(loginVO.getLicGrpId()) ) {
        				//프로젝트 그룹 존재 체크
        				Map<String,String> param = new HashMap<String,String>();
        				param.put("licGrpId",(String)loginVO.getLicGrpId());
        				List<Map> prjGrpList = prj1000Service.selectPrj1000PrjGrpExistCheck(param);
        				
        				String type = "group";
        				//프로젝트 그룹이 존재하는 경우 프로젝트 생성 높이
        				if(prjGrpList != null && prjGrpList.size() > 0){
        					type = "project";
        				}
        				
        				model.addAttribute("message", egovMessageSource.getMessage("fail.common.noprjpo"));
        				model.addAttribute("type", type);
        				return "/prj/prj1000/prj1000/prj1002";
        			}
        			//라이선스 최초 등록자가 아니라면
        			//로그인 페이지로 이동 및 메시지 세팅, 로그인 세션 파괴 처리함.
        			else{
        				ss.invalidate();
        				model.addAttribute("message", egovMessageSource.getMessage("fail.common.noprj"));
        				return "/cmm/cmm4000/cmm4000/cmm4000";
        			}
    			}
    		}else{
    			//라이선스 활성화 여부 N으로 세팅 및 메시지 세팅
    			model.addAttribute("isActLicYn", "N");
    			model.addAttribute("message", egovMessageSource.getMessage("fail.common.licend"));
    			
    			//라이선스 발급 페이지 강제 이동
    			return "/adm/adm3000/adm3000/adm3000";
    		}
    		
    		//usrId, 라이센스 ID 세팅
    		Map paramMap = new HashMap<String, String>();
    		paramMap.put("usrId", loginVO.getUsrId());
    		paramMap.put("licGrpId", loginVO.getLicGrpId());
    		
    		// 사용자의 가장 최근 로그인 이력을 조회
    		String recentLogin = cmm4000Service.selectCmm4000RecentLoginDate(loginVO);
    		
    		// 사용자의 이전 접속IP 주소와 현재 접속IP주소 조회
    		List<Map> userAccessIpInfoList = cmm4000Service.selectCmm4000AccessIpInfo(loginVO);
    		
    		// 로그인 이력 조회 후 기존 로그인 이력 갱신
    		cmm4000Service.updateCmm4000RecentLoginDate(loginVO);
    		
    		// 로그인한 사용자 정보 조회
    		Map loginUsrInfo = cmm4000Service.selectCmm4000LoginUsrInfo(loginVO);
    		
    		// 비밀번호 만료일 조회
    		int limitDay = adm5200Service.selectAdm5200PwChangeDateCheck(paramMap);
    		
    		//2018-08-09 - 프로젝트 그룹 제거하고 프로젝트 계층 목록(+그룹)을 세션으로 띄움 		:진주영
    		//프로젝트 목록 불러오기
    		List<Map> prjList = (List)prj1000Service.selectPrj1000View(paramMap);

    		//프로젝트 목록 있는지 검사
    		boolean prjChk = false;
    		for(Map prjInfo : prjList){
    			if("0".equals(String.valueOf(prjInfo.get("leaf")))){
    				prjChk = true;
    				break;
    			}
    		}
    		
    		//배정된 프로젝트 목록이 없으면
    		if(!prjChk){
    			ss.invalidate();
    			
    			//프로젝트 존재 여부 N으로 세팅 및 메시지 세팅
    			model.addAttribute("isPrjYn", "N");
				model.addAttribute("message", egovMessageSource.getMessage("fail.common.noaddprj"));
				return "/cmm/cmm4000/cmm4000/cmm4000";
    		}
    		
    		List<Map> authList = getAuthGroupList( loginVO,  prjList ,  ss) ;
    		
    		//우선순위가 가장 높은 프로젝트와 권한롤의 PRJ_ID, AUTH_GRP_ID, LIC_GRP_ID 값 세팅하여 해당 프로젝트권한의 메뉴권한정보를 가져온다.
    		paramMap.put("prjId", authList.get(0).get("prjId"));
    		paramMap.put("authGrpId", authList.get(0).get("authGrpId"));
    	
    		
    		paramMap.put("adminYn", loginVO.getAdmYn());
    		
    		List<Map> menuList = (List) cmm4000Service.selectCmm4000MenuList(paramMap);

        	//모듈 사용 체크
    		menuList = moduleUseCheck.moduleUseMenuList(menuList, request);
    		
    		//세션에 정보 저장
    		ss.setAttribute("prjList", prjList);
    		ss.setAttribute("authList", authList);
    		ss.setAttribute("menuList", menuList);
    		
    		
    		ss.setAttribute("selAuthGrpId", authList.get(0).get("authGrpId"));
    		//사용자 구분 세션에 저장
    		ss.setAttribute("usrTyp", authList.get(0).get("usrTyp"));
    		
    		// 가장 최근 로그인 정보 저장
    		ss.setAttribute("recentLogin", recentLogin);
    		
    		// 로그인한 사용자 정보 저장
    		ss.setAttribute("loginUsrInfo", loginUsrInfo);
    		
    		// 만료일이 만료일 정보 저장
    		ss.setAttribute("limitDay", limitDay);

    		// 사용자의 이전,현재 접속IP정보 저장
    		ss.setAttribute("userAccessIpInfoList", userAccessIpInfoList);
    		
    		String strRsltUrl = getLoginAfterMainURL( paramMap ,  ss) ;
    		//strRsltUrl = "/dsh/dsh1000/dsh1000/selectDsh1000View.do";
    		
        	ss.setAttribute("selMainUrl", strRsltUrl);
    		String returnPage ="redirect:"+strRsltUrl;
    		   
        	return returnPage;
        	
    	}catch(Exception ex){
    		Log.error("selectCmm4000LoginAction()", ex);
    		throw new Exception(ex.getMessage());
    	}
	}
	
	/**
	 * 
	 * 로그인 후 권한 지정되있는 경로를 조회하여 최초 로그인되도록 한다.
	 * 
	 * @param paramMap
	 * @param ss
	 * @return
	 * @throws Exception
	 */
	@SuppressWarnings("rawtypes")
	public String getLoginAfterMainURL(Map paramMap , HttpSession ss) throws Exception{
		List mainMenuList = cmm4000Service.selectCmm4000LoginMainMenuList(paramMap);
		String strRsltUrl = "";
		String strMenuId = "";
		String strMenuNm = "";
		if(mainMenuList.size()>1){
			strRsltUrl=(String) ((Map)mainMenuList.get(1)).get("menuUrl");
			strMenuId=(String) ((Map)mainMenuList.get(1)).get("menuId");
			strMenuNm=(String) ((Map)mainMenuList.get(1)).get("menuNm");
			ss.setAttribute("selMenuId",strMenuId);
    		ss.setAttribute("selMenuNm", strMenuNm);
    		ss.setAttribute("selMenuUrl", strRsltUrl);
    		
    		// 권한별 최초 시작메뉴, 메인화면으로 img 클릭 왼쪽 title에 사용하기 위해 세션에 저장
    		ss.setAttribute("firstMenuNm", strMenuNm);
    		
    		ss.setAttribute("selBtnAuthSearchYn", ((Map)mainMenuList.get(1)).get("selectYn"));
			ss.setAttribute("selBtnAuthInsertYn", ((Map)mainMenuList.get(1)).get("regYn"));
			ss.setAttribute("selBtnAuthUpdateYn", ((Map)mainMenuList.get(1)).get("modifyYn"));
			ss.setAttribute("selBtnAuthDeleteYn", ((Map)mainMenuList.get(1)).get("delYn"));
			ss.setAttribute("selBtnAuthExcelYn", ((Map)mainMenuList.get(1)).get("excelYn"));
			ss.setAttribute("selBtnAuthPrintYn", ((Map)mainMenuList.get(1)).get("printYn"));

			ss.setAttribute("selAcceptUseCd", ((Map)mainMenuList.get(1)).get("acceptUseCd"));
		}else{
			strRsltUrl=(String) ((Map)mainMenuList.get(0)).get("menuUrl");
			strMenuId=(String) ((Map)mainMenuList.get(0)).get("menuId");
			strMenuNm=(String) ((Map)mainMenuList.get(0)).get("menuNm");
			ss.setAttribute("selMenuId",strMenuId);
	    	ss.setAttribute("selMenuNm", strMenuNm);	
	    	ss.setAttribute("selMenuUrl", strRsltUrl);
	    		
	    	// 권한별 최초 시작메뉴, 메인화면으로 img 클릭 왼쪽 title에 사용하기 위해 세션에 저장
	    	ss.setAttribute("firstMenuNm", strMenuNm);
	    	
	    	ss.setAttribute("selBtnAuthSearchYn", ((Map)mainMenuList.get(0)).get("selectYn"));
			ss.setAttribute("selBtnAuthInsertYn", ((Map)mainMenuList.get(0)).get("regYn"));
			ss.setAttribute("selBtnAuthUpdateYn", ((Map)mainMenuList.get(0)).get("modifyYn"));
			ss.setAttribute("selBtnAuthDeleteYn", ((Map)mainMenuList.get(0)).get("delYn"));
			ss.setAttribute("selBtnAuthExcelYn", ((Map)mainMenuList.get(0)).get("excelYn"));
			ss.setAttribute("selBtnAuthPrintYn", ((Map)mainMenuList.get(0)).get("printYn"));
			ss.setAttribute("selAcceptUseCd", ((Map)mainMenuList.get(0)).get("acceptUseCd"));
		}
		return strRsltUrl;
	}
	
	/**
	 * 
	 * 권한 그룹 목록 조회
	 * 
	 * @param loginVO
	 * @param prjList
	 * @param ss
	 * @return
	 * @throws Exception
	 */
	@SuppressWarnings({ "unchecked", "rawtypes" })
	public List<Map> getAuthGroupList(LoginVO loginVO, List<Map> prjList , HttpSession ss) throws Exception{

		//우선순위 가장 높은 프로젝트 정보 맵 가져오기
		Map fstPrjMap = null;
		
		//최 상위 프로젝트를 가져온다.
		for(Map<String,String> prjInfo : prjList){
			//그룹 코드 (01 - 그룹, 02 - 프로젝트)
			String prjGrpCd = prjInfo.get("prjGrpCd");
			String prjId = prjInfo.get("prjId");
			if( loginVO.getPrjId() == null || loginVO.getPrjId().equals("") ){
				if(prjGrpCd != null && "02".equals(prjGrpCd)){
    				fstPrjMap = prjInfo;
    				break;
    			}
			}else{
				if(loginVO.getPrjId().equals(prjId)){
					fstPrjMap = prjInfo;
    				break;
				}
			}		
				
			//입력된 ord순에 따라 제일 먼저 조회되는 프로젝트를 최 상위 프로젝트로 선택
			
		}
		
		//권한 가져오기
		//우선순위가 가장 높은 프로젝트의 PRJ_ID로 해당 프로젝트의 권한목록을 가져온다.
		List<Map> authList = (List) cmm4000Service.selectCmm4000UsrPrjAuthList(fstPrjMap);
		//선택한 프로젝트 및 권한 id 저장
		ss.setAttribute("selPrjId", fstPrjMap.get("prjId"));
		ss.setAttribute("selPrjGrpId", fstPrjMap.get("prjGrpId"));
		ss.setAttribute("selPrjTaskTypeCd", fstPrjMap.get("prjTaskTypeCd"));
		ss.setAttribute("selPrjTaskTypeNm", fstPrjMap.get("prjTaskTypeNm"));
		return authList;
		
	}
	
	/**
     * Cmm4000 활성화 상태 라이선스 존재 여부 체크 
     * @Desc - 활성화 상태인 라이선스가 존재하면 licVO 세션에 저장하고 true 리턴
     * 		 - 활성화 상태인 라이선스가 존재하지 않으면 isActLicYn과 message model에 세팅하고 false 리턴
     * @param request, response
     * @return boolean
     * @throws Exception
     */
	public boolean cmm4000LicChk(HttpServletRequest request, HttpServletResponse response, LoginVO loginVO)	throws Exception {
		
    	try{
    		
    		//라이선스 정보 취득
    		LicVO licVO = cmm4000Service.selectCmm4000LicInfo(loginVO);
    		
    		//활성화 라이선스가 존재하면
    		if(licVO != null){
    			request.getSession().setAttribute("licVO", licVO);
    			return true;
    		}
    		//활성화 라이선스가 존재하지 않으면
    		else{
    			return false;
    		}
        	
    	}catch(Exception ex){
    		Log.error("cmm4000LicChk()", ex);
    		throw new Exception(ex.getMessage());
    	}
	}
	
	/**
     * Cmm4000 프로젝트 존재 여부 체크 
     * @Desc - 생성된 프로젝트가 존재하면 true 리턴
     * 		 - 생성된 프로젝트가 존재하지 않으면 isPrjYn과 message를 model에 세팅하고 false 리턴
     * @param request, response
     * @return boolean
     * @throws Exception
     */
	@SuppressWarnings("rawtypes")
	public boolean cmm4000PrjChk(HttpServletRequest request, HttpServletResponse response, LoginVO loginVO)	throws Exception {
		
    	try{
    		//프로젝트 존재여부 조회
    		Map prjMap = cmm4000Service.selectCmm4000PrjChk(loginVO);
    		
    		//프로젝트가 존재한다면
    		if("Y".equals(prjMap.get("isPrjYn"))){
    			return true;
    		}
    		else{
    			return false;
    		}
    		
    	}catch(Exception ex){
    		Log.error("cmm4000PrjChk()", ex);
    		throw new Exception(ex.getMessage());
    	}
	}
	
	/**
	 * Cmm4001 화면 이동
	 * @param 
	 * @return 
	 * @exception Exception
	 */
	@RequestMapping(value="/cmm/cmm4000/cmm4001/selectCmm4001View.do")
	public String selectCmm4001View() throws Exception {
		//return "forward:/cmm/cmm4001/cmm4001/selectCmm4001ChkAction.do";
		return "/cmm/cmm4000/cmm4000/cmm4001";
	}
    /**
     * Cmm4001 인증번호 발송 처리 [아이디 찾기]
     * @desc 1. 사용자 입력 정보 이름, 이메일 체크
     *  	 2. 사용자 정보 체크 후 랜덤 문자(10자리), 메일 발송
     *  	 3. 랜덤 문자(서버 session 저장, SessionTime분)
     * @map  nmChk:			이름 입력 확인
     * 		 emailChk:		이메일 입력 확인
     * 		 dbChk:			이름, 이메일 DB 조회 결과
     * 		 emailSend:		이메일 전송 결과
     * 		 emailSendTime:	이메일 전송 시간(재 전송 ReSendTime초)
     * @param request
     * @throws Exception
     */
	@RequestMapping(value = "/cmm/cmm4000/cmm4001/selectCmm4001MailSend.do", produces="text/plain;charset=UTF-8")
	  public ModelAndView selectCmm4001MailSend (HttpServletRequest request, ModelMap model) throws Exception   {

		// 값을 넘기기 위한 HasMap
		HashMap<String, String> map = new HashMap<String, String>();
		try{
			// request 파라미터를 map으로 변환
			Map<String, String> param = RequestConvertor.requestParamToMap(request, true);

			/* 파라미터 로그찍기 */    
			if (Log.isDebugEnabled()) {
				Log.debug("\n=====> parameter map : " + param);
			}
			
			// 사용자 입력 정보 할당
			String UsrInNm = param.get("InNm");
			String UsrInEmail = param.get("InEmail");
			
			// 사용자 입력 정보 중 '이름' 입력 값이 없을 경우(null)
			if(UsrInNm == null) {
				map.put("nmChk", "N");
				
				return new ModelAndView("jsonView", map);
			}
			// 사용자 입력 정보 중 '이메일' 입력 값이 없을 경우(null)
			else if(UsrInEmail == null) {
				map.put("emailChk", "N");
				
				return new ModelAndView("jsonView", map);
			}
			
			// 사용자 입력 정보 paramMap 맵핑
			Map<String, String> paramMap = new HashMap<String, String>();
			paramMap.put("usrNm", UsrInNm);
			paramMap.put("email", UsrInEmail);

			// DB 사용자 정보 조회
			String returnId = (String) cmm4000Service.selectCmm4000NameEmailChk(paramMap);
			
			// DB 사용자 정보 조회 결과 확인
			if(returnId == null) {
				// 사용자 정보 DB 조회 결과 없음
				map.put("dbChk", "N");
				
				return new ModelAndView("jsonView", map);
			}			
			// 이름, 이메일 체크 완료
			map.put("nmChk", "Y");
			map.put("emailChk", "Y");

			// 이메일 전송 문구
			map.put("emailSend","");
			map.put("emailSendTime", "N");

			/* 이메일 재사용시간 구하기 */
			if(request.getSession() != null && request.getSession().getAttribute("EmailAuthTime") != null) {
				// 이전 이메일 전송 시간을 세션에서 가져오기 (unixtime)
				Long unixtimeTemp = (Long)request.getSession().getAttribute("EmailAuthTime");

				// 현재 시간의 unixtime
				Long systemTimeTemp = System.currentTimeMillis() / 1000;

				// 현재 시간이 이전 전송 시간을 지나지 않았다면 경고 문구 
				if(unixtimeTemp != null && ((unixtimeTemp+ReSendTime) > systemTimeTemp)) {
					// 이메일 재 전송시간 몇 초 남았는지 전달
					map.put("emailSendTime",""+((unixtimeTemp+ReSendTime)-systemTimeTemp));
					
					return new ModelAndView("jsonView", map);
				}
			}

			/* 인증 코드 생성 */
			StringBuffer buf =new StringBuffer();
			// 인증코드 10자리
			buf = randomAuthNumber(10);
			System.out.println(buf);
			//인증 코드 암호화 SHA-256
			String enUsrCode = EgovFileScrty.encryptPassword(buf.toString(), "Search");
			
			/* 메일 전송 로직 */
			SndngMailVO mailVO = new SndngMailVO();
			mailVO.setDsptchPerson("admin@opensoftlab.kr");		// 발신자
			mailVO.setRecptnPerson(UsrInEmail);					// 수신자
			mailVO.setSj(egovMessageSource.getMessage("cmm4000.success.emailTitleId.send"));	// 제목
			mailVO.setEmailCn(
					egovMessageSource.getMessage("cmm4000.success.emailContentsId.send")
					+buf
					);
			
			// 이메일 전송 객체, 성공 = true / 실패 = false
			boolean sendingMailResult = egovSndngMailService.sndngMail(mailVO);
			if(sendingMailResult){
				// 메일 전송 성공
				map.put("emailSend", egovMessageSource.getMessage("cmm3200.success.emailAuthNum.send"));
			}else{
				// 메일 전송 실패
				map.put("emailSend", egovMessageSource.getMessage("cmm3200.fail.emailAuthNum.send"));
			}
			 
			/* 세션 데이터 등록 */
			// 인증 코드
			request.getSession().setAttribute("EmailAuthValue", enUsrCode);
			
			// 이메일 인증 코드, 재 발급 대기 시간
			request.getSession().setAttribute("EmailAuthTime", System.currentTimeMillis() / 1000);
		
			// 전체 세션 만료 시간
			request.getSession().setMaxInactiveInterval(SessionTime*60);
			
		}catch(Exception e){
			Log.error("selectCmm4001MailSend", e);
			return new ModelAndView("jsonView", map);
		}
		
		return new ModelAndView("jsonView", map);
    }
	
	/**
     * Cmm4001 인증번호 확인 처리 [아이디 찾기]
     * @desc 1. 사용자 입력 정보 이름, 이메일 체크
     *  	 2. 사용자 정보 체크
     *  	 3. 세션에 인증정보 확인
     *  	 4. 사용자가 입력한 인증번호와, 세션의 인증번호 확인
     *  	 5. 인증번호 맞을 경우, 아이디 값 전달
     * @map  nmChk:			이름 입력 확인
     * 		 emailChk:		이메일 입력 확인
     * 		 dbChk:			이름, 이메일 DB 조회 결과
     * 		 authNum:		세션 혹은 입력된 인증번호 오류 확인
     * @param request
     * @throws Exception
     */
	@RequestMapping(value = "/cmm/cmm4000/cmm4001/selectCmm4001ChkAction.do", produces="text/plain;charset=UTF-8")
	  public ModelAndView selectCmm4001ChkAction (HttpServletRequest request, ModelMap model) throws Exception   {

		// 값을 넘기기 위한 HasMap
		HashMap<String, String> map = new HashMap<String, String>();
		try{

			// request 파라미터를 map으로 변환
			Map<String, String> param = RequestConvertor.requestParamToMap(request, true);

			/* 파라미터 로그찍기 */    
			if (Log.isDebugEnabled()) {
				Log.debug("\n=====> parameter map : " + param);
			}

			// 사용자 입력 정보 할당
			String UsrInNm = param.get("InNm");
			String UsrInEmail = param.get("InEmail");
			String UsrInAuthNum = param.get("idAuthSucc");
			
			// 사용자 입력 정보 중 '이름' 입력 값이 없을 경우(null)
			if(UsrInNm == null) {
				map.put("nmChk", "N");

				return new ModelAndView("jsonView", map);
			}
			// 사용자 입력 정보 중 '이메일' 입력 값이 없을 경우(null)
			else if(UsrInEmail == null) {
				map.put("emailChk", "N");

				return new ModelAndView("jsonView", map);
			}
			// 사용자 입력 정보 중 '인증번호' 입력 값이 없을 경우(null)
			else if( UsrInAuthNum == null) {
				map.put("authNum", "N");

				return new ModelAndView("jsonView", map);
			}

			//사용자 입력 정보 paramMap 맵핑
			Map<String, String> paramMap = new HashMap<String, String>();
			paramMap.put("usrNm", UsrInNm);
			paramMap.put("email", UsrInEmail);

			// DB 사용자 정보 조회
			String returnId = (String) cmm4000Service.selectCmm4000NameEmailChk(paramMap);
			
			// DB 사용자 정보 조회 결과 확인
			if(returnId == null) {
				// 사용자 정보 DB 조회 결과 없음
				map.put("dbChk", "N");
				
				return new ModelAndView("jsonView", map);
			}
			/* 세션에 암호화된 인증번호 존재하는지 체크 */
			if(request.getSession() != null && request.getSession().getAttribute("EmailAuthValue") != null){
				map.put("authNum", "Y");

				//사용자가 입력한 인증 번호 암호화
				String enUsrInAN = EgovFileScrty.encryptPassword(UsrInAuthNum.toString(), "Search");

				//세션에 저장된 인증번호
				String enSessionAuthNum = (String)request.getSession().getAttribute("EmailAuthValue");
				if(enSessionAuthNum != null){
					
					//입력한 인증 값과 세션의 인증 값이 같을 경우
					if(enUsrInAN.equals(enSessionAuthNum)){
						//인증 성공 했을 경우, 아이디 반환
						map.put("succId", returnId);
						
						//생성된 세션 초기화
						request.getSession().removeAttribute("EmailAuthValue");
						request.getSession().removeAttribute("EmailAuthTime");
					}else{
						//입력된 인증번호가 잘못된 경우
						map.put("authNum", "N");
						
						return new ModelAndView("jsonView", map);
					}
				}
			}else{
				//세션에 인증번호 없을 경우 에러
				map.put("authNum", "N");
				
				return new ModelAndView("jsonView", map);
			}
		}catch(Exception e){
			Log.error("selectCmm4001ChkAction", e);
			return new ModelAndView("jsonView", map);
		}
		return new ModelAndView("jsonView", map);
	}
	
	/**
     * Cmm4001 인증번호 발송 처리 [비밀번호 찾기]
     * @desc 1. 사용자 입력 정보 아이디, 이메일 체크
     *  	 2. 사용자 정보 체크 후 랜덤 문자(12자리), 메일 발송
     *  	 3. 랜덤 문자(서버 session 저장, 3분)
     * @map  idChk:			아이디 입력 확인
     * 		 emailChk:		이메일 입력 확인
     * 		 dbChk:			아이디, 이메일 DB 조회 결과
     * 		 pwEmailSend:		이메일 전송 결과
     * 		 pwEmailSendTime:	이메일 전송 시간
     * @param request
     * @throws Exception
     */
	@RequestMapping(value = "/cmm/cmm4000/cmm4001/selectCmm4001PwMailSend.do", produces="text/plain;charset=UTF-8")
	  public ModelAndView selectCmm4001PwMailSend (HttpServletRequest request, ModelMap model) throws Exception   {

		// 값을 넘기기 위한 HasMap
		HashMap<String, String> map = new HashMap<String, String>();
		try{
			// request 파라미터를 map으로 변환
			Map<String, String> param = RequestConvertor.requestParamToMap(request, true);

			/* 파라미터 로그찍기 */    
			if (Log.isDebugEnabled()) {
				Log.debug("\n=====> parameter map : " + param);
			}
			
			// 사용자 입력 정보 할당
			String UsrInId = param.get("InId");
			String UsrInEmail = param.get("InEmail");
			
			// 사용자 입력 정보 중 '이름' 입력 값이 없을 경우(null)
			if(UsrInId == null) {
				map.put("idChk", "N");
				
				return new ModelAndView("jsonView", map);
			}
			// 사용자 입력 정보 중 '이메일' 입력 값이 없을 경우(null)
			else if(UsrInEmail == null) {
				map.put("emailChk", "N");
				
				return new ModelAndView("jsonView", map);
			}
			
			// 사용자 입력 정보 paramMap 맵핑
			Map<String, String> paramMap = new HashMap<String, String>();
			paramMap.put("usrId", UsrInId);
			paramMap.put("email", UsrInEmail);

			// DB 사용자 정보 조회 (사용 유무, 사용 = '01')
			String returnUse = (String) cmm4000Service.selectCmm4000IdEmailChk(paramMap);
			
			// DB 사용자 정보 조회 결과 확인
			if(!"01".equals(returnUse)) {
				// 사용자 정보 DB 조회 결과 없음 또는 사용자 사용 정지 상태
				map.put("dbChk", "N");
				
				return new ModelAndView("jsonView", map);
			}

			// 아이디, 이메일 체크 완료
			map.put("idChk", "Y");
			map.put("emailChk", "Y");


			// 이메일 전송 문구
			map.put("pwEmailSend","");
			map.put("pwEmailSendTime", "N");

			/* 이메일 재사용시간 구하기 */
			if(request.getSession() != null && request.getSession().getAttribute("PwEmailAuthTime") != null) {
				// 이전 이메일 전송 시간을 세션에서 가져오기 (unixtime)
				Long unixtimeTemp = (Long)request.getSession().getAttribute("PwEmailAuthTime");

				// 현재 시간의 unixtime
				Long systemTimeTemp = System.currentTimeMillis() / 1000;

				// 현재 시간이 이전 전송 시간을 지나지 않았다면 경고 문구 
				if(unixtimeTemp != null && ((unixtimeTemp+ReSendTime) > systemTimeTemp)) {
					// 이메일 재 전송시간 몇 초 남았는지 전달
					map.put("pwEmailSendTime",""+((unixtimeTemp+ReSendTime)-systemTimeTemp));
					
					return new ModelAndView("jsonView", map);
				}
			}
			/* 인증 코드 생성 */
			StringBuffer buf =new StringBuffer();
			// 인증 코드 12자리
			buf = randomAuthNumber(12);
			System.out.println(buf);
			//인증 코드 암호화 SHA-256
			String enUsrCode = EgovFileScrty.encryptPassword(buf.toString(), "PwSearch");
			
			/* 메일 전송 로직 */
			SndngMailVO mailVO = new SndngMailVO();
			mailVO.setDsptchPerson("admin@opensoftlab.kr");		// 발신자
			mailVO.setRecptnPerson(UsrInEmail);					// 수신자
			mailVO.setSj(egovMessageSource.getMessage("cmm4000.success.emailTitlePw.send"));	// 제목
			mailVO.setEmailCn(
					egovMessageSource.getMessage("cmm4000.success.emailContentsPw.send")
					+buf
					);
			
			// 이메일 전송 객체, 성공 = true / 실패 = false
			boolean sendingMailResult = egovSndngMailService.sndngMail(mailVO);
			if(sendingMailResult){
				// 메일 전송 성공
				map.put("pwEmailSend", egovMessageSource.getMessage("cmm3200.success.emailAuthNum.send"));
			}else{
				// 메일 전송 실패
				map.put("pwEmailSend", egovMessageSource.getMessage("cmm3200.fail.emailAuthNum.send"));
			}		

			/* 세션 데이터 등록 */
			// 인증 코드
			request.getSession().setAttribute("PwEmailAuthValue", enUsrCode);
			
			// 이메일 인증 코드, 재 발급 대기 시간
			request.getSession().setAttribute("PwEmailAuthTime", System.currentTimeMillis() / 1000);
		
			// 전체 세션 만료 시간
			request.getSession().setMaxInactiveInterval(SessionTime*60);
			
		}catch(Exception e){
			Log.error("selectCmm4001PwMailSend", e);
			return new ModelAndView("jsonView", map);
		}
		
		return new ModelAndView("jsonView", map);
    }
	
	/**
     * Cmm4001 인증번호 확인 처리 [비밀번호 찾기]
     * @desc 1. 사용자 입력 정보 아이디, 이메일 체크
     *  	 2. 사용자 정보 체크
     *  	 3. 세션에 인증정보 확인
     *  	 4. 사용자가 입력한 인증번호와, 세션의 인증번호 확인
     *  	 5. 인증번호 맞을 경우, 비밀번호 재 설정
     * @map  idChk:			아이디 입력 확인
     * 		 emailChk:		이메일 입력 확인
     * 		 dbChk:			아이디, 이메일 DB 조회 결과
     * 		 emailSend:		이메일 전송 결과
     * 		 emailSendTime:	이메일 전송 시간
     * 		 authNum:		세션 혹은 입력된 인증번호 오류 확인
     * @param request
     * @throws Exception
     */
	@RequestMapping(value = "/cmm/cmm4000/cmm4001/selectCmm4001PwChkAction.do", produces="text/plain;charset=UTF-8")
	  public ModelAndView selectCmm4001PwChkAction (HttpServletRequest request, ModelMap model) throws Exception   {

		// 값을 넘기기 위한 HasMap
		HashMap<String, String> map = new HashMap<String, String>();
		try{

			// request 파라미터를 map으로 변환
			Map<String, String> param = RequestConvertor.requestParamToMap(request, true);

			/* 파라미터 로그찍기 */    
			if (Log.isDebugEnabled()) {
				Log.debug("\n=====> parameter map : " + param);
			}

			// 사용자 입력 정보 할당
			String UsrInId = param.get("InId");
			String UsrInEmail = param.get("InEmail");
			String UsrInAuthNum = param.get("idAuthSucc");
			
			// 사용자 입력 정보 중 '아이디' 입력 값이 없을 경우(null)
			if(UsrInId == null) {
				map.put("idChk", "N");

				return new ModelAndView("jsonView", map);
			}
			// 사용자 입력 정보 중 '이메일' 입력 값이 없을 경우(null)
			else if(UsrInEmail == null) {
				map.put("emailChk", "N");

				return new ModelAndView("jsonView", map);
			}
			// 사용자 입력 정보 중 '인증번호' 입력 값이 없을 경우(null)
			else if( UsrInAuthNum == null) {
				map.put("authNum", "N");

				return new ModelAndView("jsonView", map);
			}

			//사용자 입력 정보 paramMap 맵핑
			Map<String, String> paramMap = new HashMap<String, String>();
			paramMap.put("usrId", UsrInId);
			paramMap.put("email", UsrInEmail);

			// DB 사용자 정보 조회 (사용 유무, 사용 = '01')
			String returnUse = (String) cmm4000Service.selectCmm4000IdEmailChk(paramMap);
			
			// DB 사용자 정보 조회 결과 확인
			if(!"01".equals(returnUse)) {
				// 사용자 정보 DB 조회 결과 없음 또는 사용자 사용 정지 상태
				map.put("dbChk", "N");
				
				return new ModelAndView("jsonView", map);
			}

			//아이디, 이메일 체크 완료
			map.put("idChk", "Y");
			map.put("emailChk", "Y");

			/* 세션에 암호화된 인증번호 존재하는지 체크 */
			if(request.getSession() != null && request.getSession().getAttribute("PwEmailAuthValue") != null){
				map.put("authNum", "Y");

				//사용자가 입력한 인증 번호 암호화
				String enUsrInAN = EgovFileScrty.encryptPassword(UsrInAuthNum.toString(), "PwSearch");

				//세션에 저장된 인증번호
				String enSessionAuthNum = (String)request.getSession().getAttribute("PwEmailAuthValue");
				if(enSessionAuthNum != null){
					
					/* 입력한 인증 값과 세션의 인증 값이 같을 경우
					 * 1. 인증 코드 생성 (8자리)
					 * 2. 인증 코드 암호화
					 * 3. 인증 코드 세션 등록
					 * 4. 인증 코드 서버 재 전달 (hidden box에 저장)
					 * -. 비밀번호 재 설정 시 hidden box에 저장된 값과 세션 값을 확인 (보안)
					 */
					if(enUsrInAN.equals(enSessionAuthNum)){
						
						/* 인증 코드 생성 */
						StringBuffer buf =new StringBuffer();
						// 인증 코드 8자리
						buf = randomAuthNumber(8);
						// 인증 코드 암호화
						String enSuccCode = EgovFileScrty.encryptPassword(buf.toString(), "SuccCode");
						
						//인증 성공 했을 경우, 임의 값 반환 (비교를 위해 반환)
						map.put("succCode", enSuccCode);
						
						//세션 등록 - 인증 코드
						request.getSession().setAttribute("PwSuccCode", enSuccCode);
						//세션 등록 - 아이디 값 (비밀번호 재 설정에 필요)
						request.getSession().setAttribute("PwSuccId", UsrInId);
						
						//생성된 세션 초기화
						request.getSession().removeAttribute("PwEmailAuthValue");
						request.getSession().removeAttribute("PwEmailAuthTime");
					}else{
						//입력된 인증번호가 잘못된 경우
						map.put("authNum", "N");
						
						return new ModelAndView("jsonView", map);
					}
				}
			}else{
				//세션에 인증번호 없을 경우 에러
				map.put("authNum", "N");
				
				return new ModelAndView("jsonView", map);
			}
		}catch(Exception e){
			Log.error("selectCmm4001PwChkAction", e);
			return new ModelAndView("jsonView", map);
		}
		return new ModelAndView("jsonView", map);
	}
	/**
     * Cmm4001 비밀번호 재 설정
     * @desc 1. 사용자 입력 새 비밀번호, 새 비밀번호 확인 체크
     *  	 2. hidden box 인증코드와 세션 인증코드 확인
     *  	 3. 인증번호 맞을 경우, 비밀번호 재 설정
     * @map  pwChk:			비밀번호 입력 
     * 		 pw2Chk:		비밀번호 확인 입력 
     * 		 codeChk:		인증코드 비교 확인
     * 		 authNum:		세션 혹은 입력된 인증번호 오류 확인
     * @param request
     * @throws Exception
     */
	@RequestMapping(value = "/cmm/cmm4000/cmm4001/selectCmm4001PwNewAction.do", produces="text/plain;charset=UTF-8")
	public ModelAndView selectCmm4001PwNewAction (HttpServletRequest request, ModelMap model) throws Exception   {

		// 값을 넘기기 위한 HasMap
		HashMap<String, String> map = new HashMap<String, String>();
		try{

			// request 파라미터를 map으로 변환
			Map<String, String> param = RequestConvertor.requestParamToMap(request, true);

			/* 파라미터 로그찍기 */    
			if (Log.isDebugEnabled()) {
				Log.debug("\n=====> parameter map : " + param);
			}

			// 사용자 입력 정보 할당
			String UsrInAuthCode = param.get("InAuthCode");	//hidden box 인증 코드
			String UsrInNewPw = param.get("InNewPw");		// 새 비밀번호 값
			String UsrInNewPw2 = param.get("InNewPw2");		// 새 비밀번호 확인 값
			
			// hidden box '인증 코드' 값이 없을 경우(null)
			if(UsrInAuthCode == null) {
				map.put("codeChk", "N");

				return new ModelAndView("jsonView", map);
			}
			// 1. 사용자 입력 정보 중 '새 비밀번호','새 비밀번호 확인' 입력 값이 다를 경우
			else if(!UsrInNewPw.equals(UsrInNewPw2)) {
				map.put("bothPwChk", "N");

				return new ModelAndView("jsonView", map);
			}
			//세션에 저장된 인증코드
			String enSessionAuthCode = (String)request.getSession().getAttribute("PwSuccCode");
			//세션에 저장된 아이디 값
			String PwSuccId = (String)request.getSession().getAttribute("PwSuccId");
			
			// 2. hidden box 코드와 세션의 인증 코드 비교
			if(!UsrInAuthCode.equals(enSessionAuthCode)){
				map.put("bothCodeChk", "N");
				
				return new ModelAndView("jsonView", map);
			}
			
			/* 3. 비밀번호 재 설정 */
			// 비밀번호 암호화
			String enUsrPw = EgovFileScrty.encryptPassword(UsrInNewPw, PwSuccId);
			
			//사용자 입력 정보 paramMap 맵핑
			Map<String, String> paramMap = new HashMap<String, String>();
			paramMap.put("newPwIn", enUsrPw);
			paramMap.put("usrId", PwSuccId);
			
			// 사용자 DB정보 업데이트 - 비밀번호 재 설정
			cmm4000Service.updateCmm4000NewPwe(paramMap);
			
			map.put("allChk", "Y");
			
			//생성된 세션 초기화
			request.getSession().removeAttribute("PwSuccCode");
			request.getSession().removeAttribute("PwSuccId");
			
		}catch(Exception e){
			Log.error("selectCmm4001PwNewAction", e);
			map.put("allChk", "N");
			
			return new ModelAndView("jsonView", map);
		}
		return new ModelAndView("jsonView", map);
	}
	/**
	 * 인증 코드 생성 로직
	 * @param MaxNum : 인증코드 최대 갯수 (default = 10, Max = 30)
	 * @return
	 */
	private StringBuffer randomAuthNumber(int MaxNum) throws Exception   {
		/* 인증 코드 생성 로직 
		 * rand: 랜덤 객체
		 * buf : 인증 코드를 저장하려는 StringBuffer
		 * 인증 코드 MaxNum자리 생성 (숫자+문자)
		 * 숫자 0~9
		 * 문자 a~z 
		 */
		if(MaxNum < 0 && MaxNum > 30){
			MaxNum = 10;
		}
		Random rand =new Random();
		StringBuffer buf =new StringBuffer();
		
		//인증 코드는 MaxNum자리
		for(int i=0;i<MaxNum;i++){
			// boolean 랜덤 생성 (랜덤으로 true or false 생성)
			if(rand.nextBoolean()){
				// 생성된 값이 true인 경우
				// 문자열 랜덤 생성 a~z
				buf.append((char)((int)(rand.nextInt(26))+97));
			}else{
				// 생성된 값이 false인 경우
				// 숫자 랜덤 생성 0~9
				buf.append((rand.nextInt(10))); 
			}
		}
		return buf;
	}
	
	/**
     * Cmm4000 프로젝트 그룹, 프로젝트 조회
     * @desc 1. 프로젝트 그룹 목록 조회 > 0
     * 		 2. 세션에 저장된 선택그룹아이디가 목록에 없는 경우, 첫 번째 그룹 아이디로 지정
     * 		 3. 프로젝트 목록 조회
     * 		 4. 세션에 저장된 선택프로젝트아이디가 목록에 없는 경우, 첫 번째 프로젝트 아이디로 지정
     * 		 4. 세션에 저장되어있는 정보 갱신
     *  	
     * 
     * @param request
     * @param response
     * @throws Exception
     *//*
	@SuppressWarnings({ "unchecked", "rawtypes" })
	@RequestMapping(value=" /cmm/cmm4000/cmm4000/selectCmm4000prjGrpSet.do")
	public ModelAndView selectCmm4000prjGrpSet(HttpServletRequest request, HttpServletResponse response, ModelMap model)	throws Exception {
		try{
			// request 파라미터를 map으로 변환
			Map<String, String> paramMap = RequestConvertor.requestParamToMap(request, true);
			
			//세션 및 로그인VO가져오기
			HttpSession ss = request.getSession();
			LoginVO loginVO = (LoginVO) ss.getAttribute("loginVO");
			
			//현재 선택 그룹,프로젝트 아이디를 가져온다.
			String selPrjGrpId = (String)ss.getAttribute("selPrjGrpId");
			String selPrjId = (String)ss.getAttribute("selPrjId");
			
			//그룹,프로젝트 아이디가 조회된 데이터 안에 있는지 확인
			boolean selPrjGrpChk = false;
			boolean selPrjChk = false;
			
			//usrId, 라이센스 ID 세팅
    		paramMap.put("usrId", loginVO.getUsrId());
    		paramMap.put("licGrpId", loginVO.getLicGrpId());
    		
    		//프로젝트 그룹 목록을 가져온다.
    		List<Map> prjGrpList = (List) prj1200Service.selectPrj1200GrpList(paramMap);
    		
    		//프로젝트 그룹 목록중에 선택된 그룹이 있는지 확인
    		for(Map prjGrpInfo : prjGrpList){
    			if(selPrjGrpId.equals(prjGrpInfo.get("prjGrpId"))){
    				selPrjGrpChk = true;
    				break;
    			}
    		}
    		//그룹안에 선택 그룹 아이디가 존재하지 않으면 첫 번째 그룹 선택
    		if(!selPrjGrpChk){
    			selPrjGrpId = (String)prjGrpList.get(0).get("prjGrpId");
    		}
    		
    		//프로젝트 목록 조회시 필요한 그룹아이디 세팅
    		paramMap.put("selPrjGrpId", selPrjGrpId);
    		
    		//여기에 권한롤 및 메뉴권한, 프로젝트 권한, 메인화면 등 세팅
    		
    		//우선순위 가장 높은 프로젝트 정보 맵 가져오기
    		Map fstPrjMap = prjList.get(0); 
    		
    		//프로젝트 목록중에 선택된 프로젝트가 있는지 확인
    		for(Map prjInfo : prjList){
    			if(selPrjId.equals(prjInfo.get("prjId"))){
    				selPrjChk = true;
    				fstPrjMap = prjInfo;
    				break;
    			}
    		}
    		//그룹안에 선택 그룹 아이디가 존재하지 않으면 첫 번째 그룹 선택
    		if(!selPrjChk){
    			selPrjId = (String)prjList.get(0).get("prjId");
    		}
    		
    		//권한 가져오기
    		//우선순위가 가장 높은 프로젝트의 PRJ_ID로 해당 프로젝트의 권한목록을 가져온다.
    		List<Map> authList = (List) cmm4000Service.selectCmm4000UsrPrjAuthList(fstPrjMap);
    		
    		//세션 정보 제거
    		ss.removeAttribute("prjGrpList");
    		ss.removeAttribute("prjList");
    		ss.removeAttribute("authList");
    		ss.removeAttribute("selAuthGrpId");
    		
    		//프로젝트 그룹, 프로젝트 아이디 재 선택의 경우, 세션 정보 다시 세팅
    		if(!selPrjGrpChk){
    			ss.removeAttribute("selPrjGrpId");
    			ss.setAttribute("selPrjGrpId", selPrjGrpId);
    		}
    		if(!selPrjGrpChk){
    			ss.removeAttribute("selPrjId");
    			ss.setAttribute("selPrjId", selPrjId);
    		}
    		
    		//세션에 정보 저장
    		ss.setAttribute("prjGrpList", prjGrpList);
    		ss.setAttribute("prjList", prjList);
    		ss.setAttribute("authList", authList);
    		
    		//권한 id 저장
    		ss.setAttribute("selAuthGrpId", authList.get(0).get("authGrpId"));
    		
    		model.addAttribute("prjGrpList", prjGrpList);
    		model.addAttribute("prjList", prjList);
    		model.addAttribute("selPrjGrpId", selPrjGrpId);
    		model.addAttribute("selPrjId", selPrjId);
			model.addAttribute("errorYN", "N");
			return new ModelAndView("jsonView", model);
    	}
    	catch(Exception ex){
    		Log.error("selectCmm4000prjGrpSet()", ex);
    		
    		//실패 세팅
    		model.addAttribute("errorYN", "Y");
    		return new ModelAndView("jsonView");
    	}
    }
	*/
	
	/**
	 * Cmm4001 화면 이동
	 * @param 
	 * @return 
	 * @exception Exception
	 */
	@RequestMapping(value="/cmm/cmm4000/cmm4002/selectCmm4002View.do")
	public String selectCmm4002View() throws Exception {
		//return "forward:/cmm/cmm4001/cmm4001/selectCmm4001ChkAction.do";
		return "/cmm/cmm4000/cmm4000/cmm4002";
	}
	
	/**
     * Cmm4000 1년이내 사용한 비밀번호 체크
     * @param request
     * @param response
     * @throws Exception
     */
	@RequestMapping(value="/cmm/cmm4000/cmm4000/selectCmm4000BeforeUsedPwChkAjax.do")
	public ModelAndView selectCmm4000BeforeUsedPwChkAjax(HttpServletRequest request, HttpServletResponse response, ModelMap model)	throws Exception {
		try{
			// request 파라미터를 map으로 변환
        	Map<String, String> paramMap = RequestConvertor.requestParamToMap(request, true);	
        	
        	// 사용자 1년이내 사용한 비밀번호 체크 
        	String isUsedPw = adm5200Service.selectAdm5200BeforeUsedPwCheck(paramMap);
        	
        	// 비밀번호 체크결과 세팅
        	model.addAttribute("isUsedPw", isUsedPw);
        	
        	// 조회 메시지 세팅
        	model.addAttribute("successYn", "Y");
        	model.addAttribute("message", egovMessageSource.getMessage("success.common.select"));
        	
        	return new ModelAndView("jsonView");
        	
		}catch(Exception e){
			Log.error("selectCmm4000BeforeUsedPwChkAjax()", e);
			
    		model.addAttribute("successYn", "N");
    		model.addAttribute("message", egovMessageSource.getMessage("fail.common.select"));
    		
    		return new ModelAndView("jsonView");
		}
    }
	
	/**
     * Cmm4000 비밀번호 만료된 사용자의 비밀번호 변경
     * @param request
     * @param response
     * @throws Exception
     */
	@RequestMapping(value="/cmm/cmm4000/cmm4000/selectCmm4000ExprPwReset.do")
	public ModelAndView selectCmm4000ExprPwReset(HttpServletRequest request, HttpServletResponse response, ModelMap model)	throws Exception {
		try{
			// request 파라미터를 map으로 변환
			Map<String, String> param = RequestConvertor.requestParamToMap(request, true);
			
			String pwChkVal = "Y";
			
			// 사용자의 현재 비밀번호 체크
			int chkVal = cmm4000Service.selectCmm4000CurtPwChk(param);

			// 입력한 비밀번호가 없을 경우 => 비밀번호 잘못입력
			if(chkVal < 1){
				pwChkVal = "N";
	    		model.addAttribute("pwChkVal", pwChkVal);
	    		return new ModelAndView("jsonView", model);
			}
    			
			// 현재 사용자 비밀번호를 정상적으로 입력했을 경우 비밀번호 수정
    		cmm4000Service.updateCmm4000PasswordExprInit(param);
    		
    		model.addAttribute("pwChkVal", pwChkVal);
			return new ModelAndView("jsonView", model);
        	
    	}catch(Exception ex){
    		Log.error("selectCmm4000ExprPwReset()", ex);
    		throw new Exception(ex.getMessage());
    	}
    }
}
