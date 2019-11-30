package kr.opensoftlab.oslops.cmm.cmm9000.cmm9200.web;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import kr.opensoftlab.oslops.cmm.cmm9000.cmm9200.service.Cmm9200Service;
import kr.opensoftlab.sdf.util.RequestConvertor;

import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.servlet.ModelAndView;

import egovframework.com.cmm.EgovMessageSource;

/**
 * @Class Name : Cmm9200Controller.java
 * @Description : 공통으로 사용자 목록을 조회하여 사용하는 클래스
 * @Modification Information
 *
 * @author 정형택
 * @since 2016.02.07.
 * @version 1.0
 * @see
 *  
 *  Copyright (C)  All right reserved.
 */

@Controller
public class Cmm9200Controller {

	/** Cmm9200Service DI */
    @Resource(name = "cmm9200Service")
    private Cmm9200Service cmm9200Service;

    /** EgovMessageSource */
	@Resource(name = "egovMessageSource")
	EgovMessageSource egovMessageSource;
	
    /**
     * 사용자 정보 목록 조회
     * @param request
     * @param response
     * @throws Exception
     */
    @SuppressWarnings({ "rawtypes", "unchecked", "finally" })
    @RequestMapping(method=RequestMethod.POST, value="/cmm/cmm9000/cmm9200/selectCmm9200PrjUsrList.do")
    public ModelAndView selectCmm9200PrjUsrList(HttpServletRequest request, HttpServletResponse response, ModelMap model)	throws Exception {

    	String code = "";
    	String text = "";
    	
    	Map rtnMap = new HashMap();
    	
    	try{
    		// request 파라미터를 map으로 변환
    		Map<String, String> param = RequestConvertor.requestParamToMapAddSelInfo(request, true);
    		
    		// 목록 조회
    		List<Map> prjUsrList = cmm9200Service.selectCmm9200PrjUsrList(param);
    		
    		for(Map usrMap : prjUsrList){
				code += usrMap.get("usrId") + "|";
       			text += usrMap.get("usrNm") + "|";
    		}
			
			/* outOfBound Exception 제거를 위해 길이 비교 */
			if(code.length() > 0){
				code = code.substring(0,code.length()-1);
				text = text.substring(0,text.length()-1);
			}
			
    		rtnMap.put("usrIdcode", code);
    		rtnMap.put("usrNmtext", text);
    		
    		rtnMap.put("prjUsrList", prjUsrList);
    		rtnMap.put("ERROR_CODE","1");
    		rtnMap.put("ERROR_MSG",egovMessageSource.getMessage("cmm9200.success.prjUsr.select"));
    		
    	}catch(Exception ex){
    		ex.printStackTrace();
    		rtnMap = new HashMap();
    		rtnMap.put("ERROR_CODE","-1");
    		rtnMap.put("ERROR_MSG",egovMessageSource.getMessage("cmm9200.fail.prjUsr.select"));
    		
    	}finally{
    		return new ModelAndView("jsonView", rtnMap);
    	}
    }
    

}
