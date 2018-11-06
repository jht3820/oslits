package kr.opensoftlab.sdf.interceptor;

import java.util.Enumeration;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.handler.HandlerInterceptorAdapter;

/**
 * RequestParamCheckInterceptor.java 클래스
 * 
 * @author 정형택
 * @since 2013. 11. 06.
 * @version 1.0
 * @see
 *
 * <pre>
 * << 개정이력(Modification Information) >>
 *   
 *   수정일     		 		수정자            수정내용
 *  ---------------    -------------   ------------------------------
 *   2013. 11. 06.   		정형택        	최초 생성
 * </pre>
 */

public class RequestParamCheckInterceptor extends HandlerInterceptorAdapter {
	
	@SuppressWarnings("unchecked")
	@Override
	public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
		/**
		 * 	시스템 디버깅 Console Session Attribute, Request Parameter, Command URI 확인 Interceptor
		 *  개발종료시 해당 인터셉터 기능 off 할것
		 */
		HttpSession session = request.getSession();
		System.out.println("====================================Command URI Start=============================================");
		System.out.println("command : " + request.getRequestURI());
		System.out.println("====================================Command URI End===============================================");
		System.out.println("");
		System.out.println("====================================Session Attribute Start=======================================");
		
		Enumeration<String> enu = request.getParameterNames();
		Enumeration<String> enuSs = session.getAttributeNames();
		
		while(enuSs.hasMoreElements()){
			String paramName = enuSs.nextElement(); 
			Object paramValue = session.getAttribute(paramName); 
			System.out.println("Session [ parameterName : " + paramName + " / parameterValue : " + paramValue.toString() + "]");
		}
		System.out.println("====================================Session Attribute End=======================================");
		System.out.println("");
		System.out.println("====================================Request Parameter Start=======================================");
		
		while(enu.hasMoreElements()){
			String paramName = enu.nextElement(); 
			String paramValue = request.getParameter(paramName); 
			System.out.println("Request [ parameterName : " + paramName + " / parameterValue : " + paramValue + "]");
		}
		System.out.println("====================================Request Parameter End=======================================");
		System.out.println("");

		return true;
		
	}

	@Override
	public void postHandle(HttpServletRequest request, HttpServletResponse response, 
			Object handler,	ModelAndView modelAndView) throws Exception {
		
		/**
		 * 	postHandle Intercepter에서 
		 *  필수 기능.
		 * 	기존 세션이 로그인 세션으로 존재하지 않을 경우 DuplicationLoginException을 발생시켜 로그인 페이지로 유도한다.
		 */
		//String str = request.getRequestURI();
		//String resURI = str.substring(0, str.indexOf(".do"));
    	//System.out.println("responseURI = " + resURI);
    	
    	//SessionVO sessionVO = (SessionVO) request.getSession().getAttribute("userInfo");
    	/*	중복로그인 체크
    	if(resURI.equals("/egmis/login") || resURI.equals("/egmis/loginaction") || resURI.equals("/egmis/logout")){
    		//System.out.println("로그인 로그인액션");
    	}else{
    		boolean rtn = LoginSessionListener.getInstance().checkDupSession(request.getSession().getId());
    		if(!rtn){
    			throw new DuplicationLoginException("세션 만료 또는 다른곳에서 로그인하여 현재 세션 종료");
    		}
    	}*/
	}

	@Override
	public void afterCompletion(HttpServletRequest request,
			HttpServletResponse response, Object handler, Exception ex)	throws Exception {

		if( ex != null ){		
			ex.printStackTrace();
			throw new Exception(ex.getMessage());
		}
	}
}

