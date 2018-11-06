package kr.opensoftlab.sdf.interceptor;


import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import kr.opensoftlab.oslits.adm.adm5000.adm5100.service.Adm5100Service;
import kr.opensoftlab.oslits.adm.adm5000.adm5100.vo.Adm5100VO;
import kr.opensoftlab.oslits.cmm.cmm4000.cmm4000.web.Cmm4000Controller;
import kr.opensoftlab.oslits.com.vo.LoginVO;

import org.apache.log4j.Logger;
import org.springframework.web.servlet.handler.HandlerInterceptorAdapter;

import egovframework.com.cmm.EgovMessageSource;
import egovframework.com.cmm.service.EgovProperties;

/**
 * 사용자의 시스템 사용이력 정보 저장
 * select, insert, delete, update로 시작하는 페이지 이름만 이력 저장
 * @author 진주영
 * @since 2016.10.28
 * @see
 *
 * <pre>
 * << 개정이력(Modification Information) >>
 *
 *   수정일      		수정자          	수정내용
 *  -------    --------    ---------------------------
 *  </pre>
 */


public class UserUseControllerLog extends HandlerInterceptorAdapter {

	/** 로그4j 로거 로딩 */
	private static final Logger Log = Logger.getLogger(Cmm4000Controller.class);
	
	/** EgovMessageSource */
	@Resource(name = "egovMessageSource")
	EgovMessageSource egovMessageSource;
	
	/** Adm5100Service DI */
	@Resource(name = "adm5100Service")
	private Adm5100Service adm5100Service;
	/**
	 * 	1. 시스템 사용이력 정보 저장
	 *	  -  ADM5100에 레코드 삽입
	 */
	@Override
	public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
		
		String systemLogYn = EgovProperties.getProperty("Globals.oslits.systemLog");
		
		//만약 UserUseControllerLog 사용 안한다는 설정이면 아래 사용이력 로그저장을 실행하지 않음
		if("N".equals(systemLogYn)){
			return true;
		}
		try{
			//세션 불러오기
			HttpSession ss = request.getSession();
			
			//로그인VO
			LoginVO loginVO = (LoginVO) ss.getAttribute("loginVO");
			
			//로그인 상태에만 시스템 기록
			if(loginVO != null){
				
				//adm5100vo생성
				Adm5100VO adm5100vo = new Adm5100VO(); 
				
				adm5100vo.setLicGrpId(loginVO.getLicGrpId());
		    	adm5100vo.setLogUsrId(loginVO.getUsrId());
		    	String requestURI = request.getRequestURI();
				adm5100vo.setLogUrl(requestURI);
		    	adm5100vo.setLogIp(request.getRemoteAddr());
		    	adm5100vo.setMenuId((String)ss.getAttribute("selMenuId"));
		    	adm5100vo.setMenuNm((String)ss.getAttribute("selMenuNm"));
				adm5100vo.setMenuUrl((String)ss.getAttribute("selMenuUrl"));
				adm5100vo.setMenuCd(menuTypeSearching(requestURI));
		    	
		    	
		    	//로그 저장
		    	adm5100Service.insertAdm5100AuthLoginLog(adm5100vo);
			}
		}catch(Exception e){
			Log.debug("UserUseControllerLog preHandle()",e);
			return true;
		}
		
		return true;
	}
	
	/**
	 * 
	 * @desc 메뉴 URL 코드값 매칭 함수 
	 * @param 메뉴 URL 
	 * @return URL 코드 값
	 * @exception Exception
	 */
	public String menuTypeSearching(String menuUrl){
		if(menuUrl.indexOf("select") >= 0){
			return "01";
		}else if(menuUrl.indexOf("insert") >= 0){
			return "02";
		}else if(menuUrl.indexOf("update") >= 0){
			return "03";
		}else if(menuUrl.indexOf("delete") >= 0){
			return "04";
		}else if(menuUrl.indexOf("save") >= 0){
			return "05";
		}
		return "09";
	}
}