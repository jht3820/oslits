package kr.opensoftlab.sdf.util;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import kr.opensoftlab.oslits.com.vo.LoginVO;

import org.springframework.ui.ModelMap;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.reflect.TypeToken;

import egovframework.rte.fdl.string.EgovStringUtil;



/**
 * RequestConvertor.java 클래스
 * 
 * @author 정형택
 * @since 2013. 11. 12.
 * @version 1.0
 * @see
 * @decription UI에서 넘어온 request parameter를 map 형식으로 변환시켜주는 유틸 클래스
 * 
 * <pre>
 * << 개정이력(Modification Information) >>
 *   
 *   수정일     		 		수정자            수정내용
 *  ---------------    -------------   ------------------------------
 *   2013. 11. 12.   		정형택        	최초 생성
 * </pre>
 */

public class RequestConvertor {

	/* parameter 를 map으로 변환 */
	@SuppressWarnings({ "unchecked", "rawtypes" })
	public static Map<String, String> requestParamToMap(HttpServletRequest request) throws Exception{
		
		Enumeration<String> enu = request.getParameterNames();
		Map paramMap = new HashMap<String, String>();
		
		while(enu.hasMoreElements()){
			String paramName = enu.nextElement(); 
			String[] paramValues = request.getParameterValues(paramName);
			
			//값이 1개
			if(paramValues.length == 1){
				paramMap.put(paramName, request.getParameter(paramName).replaceAll("&apos;", "'"));
			}
			//값이 1개 이상
			else{
				List list = new ArrayList();
				for(int i =0; i < paramValues.length; i++){
					list.add(EgovStringUtil.null2string(paramValues[i].replaceAll("&apos;", "'"), " "));
				}
				
				paramMap.put(paramName, list);
			}
			
			System.out.println("맵 세팅 정보 : =============== " + paramName + " : " + paramMap.get(paramName));
		}
		
		return paramMap;
	}			
				
	/**
	 * requestParamToMap
	 * 리퀘스트를 맵에 저장할때 로그인VO를 이용 사용자 정보를 자동으로 세팅한다.
	 * regUsrId, regUsrIp, modifyUsrId, modifyUsrIp
	 * @param request
	 * @param isInputUser
	 * @return
	 */
	@SuppressWarnings({ "unchecked", "rawtypes" })
	public static Map<String, String> requestParamToMap(HttpServletRequest request, boolean isInputUser) throws Exception{
		
		Enumeration<String> enu = request.getParameterNames();
		Map paramMap = new HashMap<String, String>();
		
		/* 입력자, 수정자가 필요할 경우 true로 넘어오면 request paramMap에 입력자와 수정자를 login정보의 usrId와 licGrpId로 세팅한다. */
		if(isInputUser){
			LoginVO loginVO = (LoginVO) request.getSession().getAttribute("loginVO");
			
			String strUsrId = "admin";
			String strUsrIp = "0.0.0.0";
			String strLicGrpId = "";
			
			if( loginVO != null) {
				strUsrId = loginVO.getUsrId(); 
				strUsrIp = request.getRemoteAddr();
				strLicGrpId = loginVO.getLicGrpId();
			}
			
			/* 로그인 정보에서 입력자 수정자, 입력자ip, 수정자ip 강제로 세팅 */
			paramMap.put("regUsrId", strUsrId);
			paramMap.put("modifyUsrId", strUsrId);
			paramMap.put("regUsrIp", strUsrIp);
			paramMap.put("modifyUsrIp", strUsrIp);
			paramMap.put("licGrpId", strLicGrpId);
		}
		
		while(enu.hasMoreElements()){
			String paramName = enu.nextElement(); 
			String[] paramValues = request.getParameterValues(paramName);
			
			//값이 1개
			if(paramValues.length == 1){
				paramMap.put(paramName, request.getParameter(paramName));
			}
			//값이 1개 이상
			else{
				List list = new ArrayList();
				for(int i =0; i < paramValues.length; i++){
					list.add(EgovStringUtil.null2string(paramValues[i], " "));
				}
				
				paramMap.put(paramName, list);
			}
			
			System.out.println("맵 세팅 정보 : =============== " + paramName + " : " + paramMap.get(paramName));
		}
		
		return paramMap;
	}
	
	/**
	 * 파라미터를 모델맵에 세팅
	 * @param model
	 * @param request
	 */
	@SuppressWarnings({ "unchecked", "rawtypes" })
	public static void requestParamToModelMap(ModelMap model, HttpServletRequest request) throws Exception{
    	Enumeration<String> enu = request.getParameterNames();
    	
    	while(enu.hasMoreElements()){
			String paramName = enu.nextElement(); 
			String[] paramValues = request.getParameterValues(paramName);
			
			//값이 1개
			if(paramValues.length == 1){
				model.put(paramName, request.getParameter(paramName));
			}
			//값이 1개 이상
			else{
				List list = new ArrayList();
				for(int i =0; i < paramValues.length; i++){
					list.add(EgovStringUtil.null2string(paramValues[i], " "));
				}
				
				model.put(paramName, list);
			}
			
			System.out.println("모델맵 세팅 정보 : =============== " + paramName + " : " + model.get(paramName));
		}
    }
	
	/**
	 * requestParamToMapAddSelInfo
	 * 리퀘스트를 맵에 저장할때 true 이면 로그인VO를 이용 사용자 정보를 자동으로 세팅한다.
	 * 기본 파라미터를 맵으로 전환할때 선택한 prjId, 선택한 authGrpId를 세팅한다.
	 * regUsrId, regUsrIp, modifyUsrId, modifyUsrIp
	 * @param request
	 * @param isInputUser
	 * @return
	 */
	@SuppressWarnings({ "unchecked", "rawtypes" })
	public static Map<String, String> requestParamToMapAddSelInfo(HttpServletRequest request, boolean isInputUser) throws Exception{
		
		Enumeration<String> enu = request.getParameterNames();
		Map paramMap = new HashMap<String, String>();
		
		/* 입력자, 수정자가 필요할 경우 true로 넘어오면 request paramMap에 입력자와 수정자를 login정보의 usrId와 licGrpId로 세팅한다. */
		if(isInputUser){
			LoginVO loginVO = (LoginVO) request.getSession().getAttribute("loginVO");
			
			String strUsrId = "admin";
			String strUsrIp = "0.0.0.0";
			String strLicGrpId = "";
			
			if( loginVO != null) {
				strUsrId = loginVO.getUsrId(); 
				strUsrIp = request.getRemoteAddr();
				strLicGrpId = loginVO.getLicGrpId();
			}
			
			/* 로그인 정보에서 입력자 수정자, 입력자ip, 수정자ip 강제로 세팅 */
			paramMap.put("regUsrId", strUsrId);
			paramMap.put("modifyUsrId", strUsrId);
			paramMap.put("regUsrIp", strUsrIp);
			paramMap.put("modifyUsrIp", strUsrIp);
			paramMap.put("licGrpId", strLicGrpId);
		}
		
		/*selPrjGrpId, selPrjId, selAuthGrpId 세팅 */
		paramMap.put("selPrjGrpId", request.getSession().getAttribute("selPrjGrpId"));
		paramMap.put("selPrjId", request.getSession().getAttribute("selPrjId"));
		paramMap.put("selAuthGrpId", request.getSession().getAttribute("selAuthGrpId"));
		
		while(enu.hasMoreElements()){
			String paramName = enu.nextElement();
			String[] paramValues = request.getParameterValues(paramName);
			
			//값이 1개
			if(paramValues.length == 1){
				paramMap.put(paramName, request.getParameter(paramName));
				
			}
			//값이 1개 이상
			else{
				List list = new ArrayList();
				for(int i =0; i < paramValues.length; i++){
					list.add(EgovStringUtil.null2string(paramValues[i], " "));
				}
				
				paramMap.put(paramName, list);
			}
			System.out.println("맵 세팅 정보 : =============== " + paramName + " : " + paramMap.get(paramName));
		}
		
		return paramMap;
	}
	
	/**
	 * mapAddCommonInfo
	 * 맵 객체에 로그인VO를 이용하여 공통 정보를 자동으로 세팅한다.
	 * regUsrId, regUsrIp, modifyUsrId, modifyUsrIp, licGrpId, selAuthGrpId, selPrjId
	 * @param request
	 * @return
	 */
	@SuppressWarnings({ "unchecked", "rawtypes" })
	public static Map<String, String> mapAddCommonInfo(HttpServletRequest request, Map paramMap) throws Exception{
		
		/* 입력자와 수정자 정보 및 IP, licGrpId를 loginVO에서 읽어와  login정보의 usrId와 licGrpId로 세팅한다. */
		LoginVO loginVO = (LoginVO) request.getSession().getAttribute("loginVO");
		
		String strUsrId = "admin";
		String strUsrIp = "0.0.0.0";
		String strLicGrpId = "";
		
		if( loginVO != null) {
			strUsrId = loginVO.getUsrId(); 
			strUsrIp = request.getRemoteAddr();
			strLicGrpId = loginVO.getLicGrpId();
		}
		
		/* 로그인 정보에서 입력자, 수정자, 입력자ip, 수정자ip, licGrpId,  selPrjId 강제로 세팅 */
		paramMap.put("regUsrId", strUsrId);
		paramMap.put("modifyUsrId", strUsrId);
		paramMap.put("regUsrIp", strUsrIp);
		paramMap.put("modifyUsrIp", strUsrIp);
		paramMap.put("licGrpId", strLicGrpId);
		paramMap.put("prjId", request.getSession().getAttribute("selPrjId"));
		
		return paramMap;
	}
	
	
	/**
	 * 
	 * 맵 객체에 로그인VO를 이용하여 공통 정보를 자동으로 세팅한다.
	 * regUsrId, regUsrIp, modifyUsrId, modifyUsrIp, licGrpId, selAuthGrpId, selPrjId
	 * @param request
	 * @return
	 */
	@SuppressWarnings({ "unchecked", "rawtypes", "unused" })
	public static Map<String, String> requestParamToMapJson(HttpServletRequest request, String json, boolean isInputUser) throws Exception{
		Gson gson = new GsonBuilder().setPrettyPrinting().create();
		Enumeration<String> enu = request.getParameterNames();
		
		Type type = new TypeToken<HashMap>(){}.getType();
		
		Map paramMap = (HashMap) gson.fromJson(json, type);
		
		return paramMap;
	}
	
	
	/**
	 * requestParamToMapAddSelInfoList
	 * 리퀘스트를 맵에 저장할때 true 이면 로그인VO를 이용 사용자 정보를 자동으로 세팅한다.
	 * 기본 파라미터를 맵으로 전환할때 선택한 prjId, 선택한 authGrpId를 세팅한다.
	 * regUsrId, regUsrIp, modifyUsrId, modifyUsrIp
	 * @param request
	 * @param isInputUser
	 * @param keyProperty list 
	 * @return
	 */
	@SuppressWarnings({ "unchecked", "rawtypes" })
	public static Map<String, Object> requestParamToMapAddSelInfoList(HttpServletRequest request, boolean isInputUser,String keyProperty) throws Exception{
		
		Enumeration<String> enu = request.getParameterNames();
		Map paramMap = new HashMap<String, String>();
		
		String strUsrId = "admin";
		String strUsrIp = "0.0.0.0";
		String strLicGrpId = "";
		
		/* 입력자, 수정자가 필요할 경우 true로 넘어오면 request paramMap에 입력자와 수정자를 login정보의 usrId와 licGrpId로 세팅한다. */
		if(isInputUser){
			LoginVO loginVO = (LoginVO) request.getSession().getAttribute("loginVO");
			
			if( loginVO != null) {
				strUsrId = loginVO.getUsrId();  	
				strUsrIp = request.getRemoteAddr();
				strLicGrpId = loginVO.getLicGrpId();
			}
			
			/* 로그인 정보에서 입력자 수정자, 입력자ip, 수정자ip 강제로 세팅 */
			paramMap.put("regUsrId", strUsrId);
			paramMap.put("modifyUsrId", strUsrId);
			paramMap.put("regUsrIp", strUsrIp);
			paramMap.put("modifyUsrIp", strUsrIp);
			paramMap.put("licGrpId", strLicGrpId);
		}
		
		/* selPrjId, selAuthGrpId 세팅 */
		paramMap.put("selPrjId", request.getSession().getAttribute("selPrjId"));
		paramMap.put("selAuthGrpId", request.getSession().getAttribute("selAuthGrpId"));
		
		
		String[] keyParamValues = request.getParameterValues(keyProperty);
		
		List<Map<String,String>> detailList = null;
		if(keyParamValues!=null){
			detailList=new ArrayList();
			for (int j = 0; j < keyParamValues.length; j++) {
				Map<String,String> detailMap=new HashMap<String,String>();
				detailMap.put("regUsrId", strUsrId);
				detailMap.put("modifyUsrId", strUsrId);
				detailMap.put("regUsrIp", strUsrIp);
				detailMap.put("modifyUsrIp", strUsrIp);
				detailMap.put("licGrpId", strLicGrpId);
				detailList.add(detailMap);
			}
		}
		
		while(enu.hasMoreElements()){
			String paramName = enu.nextElement(); 
			String[] paramValues = request.getParameterValues(paramName);
			
			//값이 1개
			if(paramValues.length == 1){
				if(keyParamValues.length==1){
					Map<String,String> detailMap=detailList.get(0);
					detailMap.put(paramName, request.getParameter(paramName));
					detailList.set(0, detailMap);
				}else{
					
					paramMap.put(paramName, request.getParameter(paramName));	
				}
				
				
			}
			//값이 1개 이상
			else{
				for(int i =0; i < paramValues.length; i++){
					Map<String,String> detailMap=detailList.get(i);
					detailMap.put(paramName, EgovStringUtil.null2string(paramValues[i], " "));
					detailList.set(i, detailMap);
					
				}
			}
			System.out.println("맵 세팅 정보 : =============== " + paramName + " : " + paramMap.get(paramName));
		}
		
		if(keyParamValues!=null){
			paramMap.put("list", detailList);
			System.out.println("맵 세팅 정보 : =============== list : " + detailList);
		}
		
		return paramMap;
	}
	
}

