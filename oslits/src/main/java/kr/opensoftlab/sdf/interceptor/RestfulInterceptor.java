package kr.opensoftlab.sdf.interceptor;

import java.util.Enumeration;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import kr.opensoftlab.oslits.stm.stm1000.stm1000.service.Stm1000Service;

import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.ModelAndViewDefiningException;
import org.springframework.web.servlet.handler.HandlerInterceptorAdapter;

/**
 * RestfulInterceptor.java 클래스
 * 
 * @author 공대영
 * @since 2018. 09. 13.
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

public class RestfulInterceptor extends HandlerInterceptorAdapter {
	
	@Resource(name = "stm1000Service")
	private Stm1000Service stm1000Service;
	
	
	@SuppressWarnings("unchecked")
	@Override
	public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
		
		String tokenKey = request.getParameter("tokenKey");
		String cmd = request.getRequestURI();
		Map paramMap =new HashMap();
		Map resultMap =new HashMap();
		Map statusMap =new HashMap();
		
		paramMap.put("apiTok", tokenKey);
		
		List<Map<String, String>> list = stm1000Service.selectStm1000ApiUrlList(paramMap);
		
		if(list.size()==0){
			statusMap.put("code", "100");
			statusMap.put("codename", "토큰 인증 실패");
			resultMap.put("status", statusMap);
			
			ModelAndView mv = new ModelAndView("jsonView",resultMap);
			
			throw new ModelAndViewDefiningException(mv);
		}
		boolean validUrl = false;
		String prjId ="";
		String licGrpId ="";
		for (Map<String, String> apiMap : list ) {
			String apiUrl = apiMap.get("apiUrl");
			prjId = apiMap.get("prjId");	
			licGrpId = apiMap.get("licGrpId");	
			
			validUrl=  validUrl || compareRegisterRestfulURL(apiUrl, cmd );
		}
		
		if(! validUrl ){
			statusMap.put("code", "101");
			statusMap.put("codename", "유효하지 않는 URL");
			resultMap.put("status", statusMap);
			
			ModelAndView mv = new ModelAndView("jsonView",resultMap);
			
			throw new ModelAndViewDefiningException(mv);
		}
		
		request.setAttribute("licGrpId", licGrpId);
		request.setAttribute("prjId", prjId);
		
		return true;
		
	}
	
	/**
	 * 
	 * DB에 등록된 URL과 서버에 URL이 유효한지 체크한다.
	 * 
	 * @param dbRegUrl
	 * @param serverUrl
	 * @return
	 */
	public boolean compareRegisterRestfulURL(String dbRegUrl, String serverUrl){
		
		String[] dbRegUrls=dbRegUrl.split("/");
		String[] serverUrls=serverUrl.split("/");
		
		if(dbRegUrls.length != serverUrls.length ){
			return false;
		}
		
		for (int i = 0; i < dbRegUrls.length; i++) {
			if( ! ( dbRegUrls[i].indexOf("{") > -1 && dbRegUrls[i].indexOf("}") > -1 ) ){
				if(! dbRegUrls[i].equals( serverUrls[i] ) ){
					return false;
				}
			}
		}

		return true;
	}

	@Override
	public void postHandle(HttpServletRequest request, HttpServletResponse response, 
			Object handler,	ModelAndView modelAndView) throws Exception {
		
	}

	@Override
	public void afterCompletion(HttpServletRequest request,
			HttpServletResponse response, Object handler, Exception ex)	throws Exception {

	
	}
}

