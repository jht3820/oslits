package kr.opensoftlab.sdf.interceptor;


import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import kr.opensoftlab.oslits.com.vo.LoginVO;

import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.ModelAndViewDefiningException;
import org.springframework.web.servlet.handler.HandlerInterceptorAdapter;

import egovframework.com.cmm.EgovMessageSource;
import egovframework.com.cmm.service.EgovProperties;

/**
 * 로그인 여부 및 유효 라이선스 체크 인터셉터
 * @author 정형택
 * @since 2016.09.13
 * @see
 *
 * <pre>
 * << 개정이력(Modification Information) >>
 *
 *   수정일      		수정자          	수정내용
 *  -------    --------    ---------------------------
 *  20161027	진주영		로그인, 로그아웃 이력 삽입
 *  </pre>
 */


public class AuthenticInterceptor extends HandlerInterceptorAdapter {

	/** EgovMessageSource */
	@Resource(name = "egovMessageSource")
	EgovMessageSource egovMessageSource;
	
	/**
	 * 	1. 라이선스가 유효 여부 체크
	 *    - 라이선스 키파일을 읽어들여 라이선스가 유효하다면 통과, 유효하지 않다면 라이선스 만료 페이지로 이동한다.
	 * 	2. 세션에 계정정보(LoginVO)가 있는지 여부로 인증 여부를 체크한다.
	 *	  - 로그인정보 없고 요청이 AJAX 요청일 경우 999(사용자정의 세션 만료 오류코드) 에러 발생시킨다.
	 * 	  - 로그인정보 있는지 판단하여 있으면 통과, 로그인정보(LoginVO)가 없다면 로그인 페이지로 이동한다.
	 * 	3. insert, update, delete, select 요청에 대하여 해당 권한 유효 체크
	 *    - 요청 명령을 체크하여 해당권한이 있는지 체크하여 있다면 통과, 없다면 권한없음 페이지로 이동한다. 
	 */
	@Override
	public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {

		boolean isPermittedURL = false;

		String cmd = request.getRequestURI();
		String resURI = cmd.substring(0, cmd.indexOf(".do")+3);
		String authYn = EgovProperties.getProperty("Globals.oslits.auth");
		
		//만약 Authentic 사용 안한다는 설정이면 아래 Auth 체크하지 않고 패스 
		if("N".equals(authYn)){
			return true;
		}
		
		//허가된 URL 목록
		String[] permitURLs = {	"/cmm/cmm4000/cmm4000/selectCmm4000View.do", "/cmm/cmm4000/cmm4000/selectCmm4000LoginAction.do"
							,	"/cmm/cmm4000/cmm4000/selectCmm4000LoginAfter.do", "/cmm/cmm4000/cmm4001/selectCmm4001MailSend.do"
							,	"/cmm/cmm4000/cmm4001/selectCmm4001ChkAction.do", "/cmm/cmm4000/cmm4001/selectCmm4001View.do"
							,	"/cmm/cmm4000/cmm4001/selectCmm4001PwMailSend.do", "/cmm/cmm4000/cmm4001/selectCmm4001PwChkAction.do"
							,	"/cmm/cmm4000/cmm4001/selectCmm4001PwNewAction.do"
							, 	"/cmm/cmm3000/cmm3000/selectCmm3000View.do",	"/cmm/cmm3000/cmm3100/selectCmm3100View.do"
							, 	"/cmm/cmm3000/cmm3200/selectCmm3200View.do",	"/cmm/cmm3000/cmm3200/selectCmm3200IdCheck.do"
							, 	"/cmm/cmm3000/cmm3200/selectCmm3200MailSend.do",	"/cmm/cmm3000/cmm3200/selectCmm3200SendValCheck.do"
							, 	"/cmm/cmm3000/cmm3200/insertCmm3200JoinIng.do"
							,   "/cmm/cmm4000/cmm4002/selectCmm4002View.do" // 비밀번호 변경 페이지
		};

		//허가된 URL에 포함되어 있다면 isPermittedURL true로 변경하고 반복문 종료
		for(String url : permitURLs){
			if(url.equals(resURI)){
				isPermittedURL = true;
				break;
			}
		}
		
		LoginVO loginVO = (LoginVO) request.getSession().getAttribute("loginVO");

		if(loginVO != null){
			//허가된 페이지 외에 동작하는 경우 시스템 사용이력
			//만약 로그인 되어 있는 상태에서 로그인 페이지로 이동할려고 하면 강제로 로그인 이후 로직을 태운다.
			if("/cmm/cmm4000/cmm4000/selectCmm4000View.do".equals(resURI)){
				ModelAndView mv = new ModelAndView("forward:/cmm/cmm4000/cmm4000/selectCmm4000LoginAfter.do");
				throw new ModelAndViewDefiningException(mv);
			}
			else{
				return true;
			}
		}
		else{
			//AJAX 요청일 경우 999(사용자 정의 세션만료 에러 코드) 에러 발생시킴.
			//+허가된 페이지가 아닐 경우
			if(!isPermittedURL && (request.getHeader("x-requested-with") != null) && ("XMLHttpRequest".equals(request.getHeader("x-requested-with")))){
				//response.sendError(HttpServletResponse.SC_FORBIDDEN);
				response.sendError(999);
				return false;
			}
			else if(!isPermittedURL){
				//로그인이 안되어 있거나 로그인 없이 사용가능한 페이지가 아닌경우 로그인 페이지로 리다이렉트
				ModelAndView mv = new ModelAndView("forward:/cmm/cmm4000/cmm4000/selectCmm4000View.do");
				mv.addObject("sessionYn", "N");
				mv.addObject("message", egovMessageSource.getMessage("fail.common.session"));
				throw new ModelAndViewDefiningException(mv);
			}else{
				//정상통과
				return true;
			}	
		}
	}
}