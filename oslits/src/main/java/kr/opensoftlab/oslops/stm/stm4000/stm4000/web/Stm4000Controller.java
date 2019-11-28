package kr.opensoftlab.oslops.stm.stm4000.stm4000.web;

import java.util.List;
import java.util.Map;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.apache.log4j.Logger;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;

import egovframework.com.cmm.EgovMessageSource;
import egovframework.rte.fdl.cmmn.trace.LeaveaTrace;
import egovframework.rte.fdl.property.EgovPropertyService;
import kr.opensoftlab.oslops.com.vo.LoginVO;
import kr.opensoftlab.oslops.prj.prj1000.prj1000.service.Prj1000Service;
import kr.opensoftlab.oslops.stm.stm4000.stm4000.service.Stm4000Service;
import kr.opensoftlab.sdf.util.RequestConvertor;


/**
 * @Class Name : Stm4000Controller.java
 * @Description : Stm4000Controller Controller class
 * @Modification Information
 *
 * @author 배용진
 * @since 2019.05.08.
 * @version 1.0
 * @see
 *  
 *  Copyright (C) OpenSoftLab Corp All right reserved.
 */
@Controller
public class Stm4000Controller {
	/** 로그4j 로거 로딩 */
	private static final Logger Log = Logger.getLogger(Stm4000Controller.class);
	
	/** Stm4000Service DI */
    @Resource(name = "stm4000Service")
    private Stm4000Service stm4000Service;	
	
	/** Prj1000Service DI */
    @Resource(name = "prj1000Service")
    private Prj1000Service prj1000Service;

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
	 * Stm4000 전체 프로젝트 관리 화면이동
	 * @param 
	 * @return 
	 * @exception Exception
	 */
	@RequestMapping(value="/stm/stm4000/stm4000/selectStm4000View.do")
    public String selectStm4000View(HttpServletRequest request, HttpServletResponse response, ModelMap model ) throws Exception {
        	return "/stm/stm4000/stm4000/stm4000";
    }
    
	/**
	 * Stm4000 라이선스 그룹 전체 프로젝트 목록 조회
	 * @param 
	 * @return 
	 * @exception Exception
	 */
	@SuppressWarnings({ "rawtypes", "unchecked" })
	@RequestMapping(value="/stm/stm4000/stm4000/selectStm4000LicGrpPrjListAjax.do")
	public ModelAndView selectStm4000LicGrpPrjListAjax(HttpServletRequest request, HttpServletResponse response, ModelMap model ) throws Exception {
		try{
			//리퀘스트에서 넘어온 파라미터를 맵으로 세팅
			Map<String, String> paramMap = RequestConvertor.requestParamToMap(request, true);
	    		
			// 라이선스 그룹의 전체 프로젝트 목록을 가져온다.
			List<Map> selectLicGrpAllPrjList = (List) stm4000Service.selectStm4000LicGrpPrjList(paramMap);

			model.put("adminPrjList", selectLicGrpAllPrjList);
	        	
			// 조회 성공여부 및 조회 성공 메시지 세팅
			model.addAttribute("errorYn", "N");
			model.addAttribute("message", egovMessageSource.getMessage("success.common.select"));
	 
			return new ModelAndView("jsonView", model);
		}
		catch(Exception ex){
			Log.error("selectStm4000LicGrpPrjListAjax()", ex);
			// 조회 실패여부 및 조회 실패 메시지 세팅
			model.addAttribute("errorYn", "Y");
			model.addAttribute("message", egovMessageSource.getMessage("fail.common.select"));
			throw new Exception(ex.getMessage());
		}
	}
	 
	
    /**
	 * Stm4000 프로젝트 트리에서 선택한 프로젝트의 상세 정보를 조회한다.
	 * @param 
	 * @return 
	 * @exception Exception
	 */
    @SuppressWarnings({ "rawtypes"})
	@RequestMapping(value="/stm/stm4000/stm4000/selectStm4000SelPrjInfoAjax.do")
    public ModelAndView selectStm4000SelPrjInfoAjax(HttpServletRequest request, HttpServletResponse response, ModelMap model ) throws Exception {
    	try{
    		//리퀘스트에서 넘어온 파라미터를 맵으로 세팅
    		Map<String, String> paramMap = RequestConvertor.requestParamToMap(request, true);

        	// 프로젝트 정보 조회
    		Map prjInfo = prj1000Service.selectPrj1000Info(paramMap);
        	model.put("prjInfo", prjInfo);
        	
        	// 조회 성공여부 및 조회 성공 메시지 세팅
			model.addAttribute("errorYn", "N");
			model.addAttribute("message", egovMessageSource.getMessage("success.common.select"));
        	
        	return new ModelAndView("jsonView", model);
    	}
    	catch(Exception ex){
    		Log.error("selectStm4000SelPrjInfoAjax()", ex);
    		// 조회 실패여부 및 조회 실패 메시지 세팅
    		model.addAttribute("errorYn", "Y");
    		model.addAttribute("message", egovMessageSource.getMessage("fail.common.select"));
    		throw new Exception(ex.getMessage());
    	}
    }
    
    /**
	 * Stm4000 프로젝트를 수정한다
	 * @param
	 * @return update row, 프로젝트ID
	 * @exception Exception
	 */
    @SuppressWarnings({ "rawtypes", "unchecked" })
	@RequestMapping(value="/stm/stm4000/stm4000/updateStm4000PrjInfoAjax.do")
    public ModelAndView updateStm4000PrjInfoAjax(HttpServletRequest request, HttpServletResponse response, ModelMap model ) throws Exception {
    	try{
    		// request 파라미터를 map으로 변환
        	Map<String, String> paramMap = RequestConvertor.requestParamToMap(request, true);
        	
        	// 프로젝트 약어
    		String prjAcrmStr = paramMap.get("prjAcrm");
    		
    		// 프로젝트 약어가 있을 경우
    		if(prjAcrmStr != null){
    			// Map에 프로젝트 약어 추가
        		paramMap.put("prjAcrm", prjAcrmStr.toUpperCase());
    		}
        	
        	// 프로젝트 수정
        	int updateCnt = prj1000Service.updatePrj1000Ajax(paramMap);
        	
        	// 프로젝트 ID와 프로젝트 수정 count 값을 세팅
        	model.put("prjId", paramMap.get("prjId"));
        	model.put("updateCnt", updateCnt);
        	// 수정 성공여부 및 수정 성공 메시지 세팅
        	model.addAttribute("errorYn", "N");
        	model.addAttribute("message", egovMessageSource.getMessage("success.common.update"));
        	
        	//세션 정보 가져오기
			HttpSession ss = request.getSession();
			// LoginVO를 가져온다
			LoginVO loginVO = (LoginVO) ss.getAttribute("loginVO");
			
			// 사용자 ID를 가져온다.
			String usrId = (String)loginVO.getUsrId();
			paramMap.put("usrId", usrId);
			
			// 세션 재세팅을 위한 프로젝트 목록 불러올 때 프로젝트 그룹값 Map에서 제거한다.
			// 그렇지 않으면 단위 프로젝트만 조회되어 세션에 세팅된다.
			paramMap.remove("prjGrpCd");
			
        	// 사용자 권한있는 프로젝트 목록 조회
			List<Map> prjList = (List)prj1000Service.selectPrj1000View(paramMap);

    		// 세션에 있는 기존 프로젝트 목록 제거
    		ss.removeAttribute("prjList");
    		
    		// 조회한 권한있는 프로젝트 목록을 세션에 재세팅
    		ss.setAttribute("prjList", prjList);
    		
        	return new ModelAndView("jsonView", model);
    	}
    	catch(Exception ex){
    		Log.error("updateStm4000PrjInfoAjax()", ex);
          	// 수정 실패여부 및 수정 실패 메시지 세팅
        	model.addAttribute("errorYn", "Y");
    		model.addAttribute("message", egovMessageSource.getMessage("fail.common.update"));
    		return new ModelAndView("jsonView");
    	}
    }

    /**
	 * Stm4000 프로젝트 삭제
	 * @param Map
	 * @return delete row
	 * @exception Exception
	 */
	@SuppressWarnings({ "unchecked", "rawtypes" })
	@RequestMapping(value="/stm/stm4000/stm4000/deleteStm4000PrjInfoAjax.do")
    public ModelAndView deleteStm4000PrjInfoAjax(HttpServletRequest request, HttpServletResponse response, ModelMap model ) throws Exception {
    	try{
    		// request 파라미터를 map으로 변환
        	Map<String, String> paramMap = RequestConvertor.requestParamToMap(request, true);
        	
        	//세션 정보 가져오기
			HttpSession ss = request.getSession();
			LoginVO loginVO = (LoginVO) ss.getAttribute("loginVO");
			
			//userId
			String usrId = (String)loginVO.getUsrId();
			paramMap.put("usrId", usrId);
			
        	String prjGrpCd = (String)paramMap.get("prjGrpCd");
        	
        	// 세션 재세팅을 위한 프로젝트 목록 불러올 때 프로젝트 그룹값 Map에서 제거한다.
        	// 그렇지 않으면 단위 프로젝트만 조회되어 세션에 세팅된다.
        	paramMap.remove("prjGrpCd");
        	
        	//프로젝트 그룹 제거인경우
        	if("01".equals(prjGrpCd)){
        		
        		// 프로젝트 그룹 삭제
        		prj1000Service.deletePrj1000PrjGrpAjax(paramMap);
        		
        		// 삭제 성공 메시지 세팅
            	model.addAttribute("message", egovMessageSource.getMessage("success.common.delete"));
            	
            	// 세션 재 세팅 - prjList
            	// 권한있는 프로젝트 목록 불러오기
        		List<Map> prjList = (List)prj1000Service.selectPrj1000View(paramMap);
        		
        		// 프로젝트 목록 세션 제거
        		ss.removeAttribute("prjList");
        		
        		// 세션 재 등록
        		ss.setAttribute("prjList", prjList);
        		
            	return new ModelAndView("jsonView");
        	}
        	
        	
        	// 프로젝트 제거
        	prj1000Service.deletePrj1001Ajax(paramMap);

        	String strErrCode = (String)paramMap.get("ERR_CODE");
        	String strErrMsg = (String)paramMap.get("ERR_MSG");
        	
        	//삭제 실패시 삭제 구분 N로 세팅하고 메시지 세팅하여 리턴
        	if("-1".equals(strErrCode)){
        		model.addAttribute("delYN", "N");
        		model.addAttribute("message", strErrMsg);
        		return new ModelAndView("jsonView");
        	}
        	
        	//세션 재 세팅 - prjList
        	// 권한있는 프로젝트 목록 가져오기
    		List<Map> prjList = (List)prj1000Service.selectPrj1000View(paramMap);
    		
    		// 프로젝트 목록 세션 제거
    		ss.removeAttribute("prjList");
    		
    		// 세션 재 등록
    		ss.setAttribute("prjList", prjList);
    		
        	model.addAttribute("prjId", paramMap.get("prjId"));
        	// 삭제 성공 메시지 세팅
        	model.addAttribute("message", egovMessageSource.getMessage("success.common.delete"));
        	
        	return new ModelAndView("jsonView");
    	}
    	catch(Exception ex){
    		Log.error("deleteStm4000PrjInfoAjax()", ex);
    		// 삭제 실패여부 및 삭제 실패 메시지 세팅
    		model.addAttribute("delYN", "N");
    		model.addAttribute("message", egovMessageSource.getMessage("fail.common.delete"));
    		return new ModelAndView("jsonView");
    	}
    }
	
	
	/**
	 * Stm4000  라이선스 그룹에서 관리자 권한이 있는 모든 프로젝트 목록 조회
	 * @param 
	 * @return 
	 * @exception Exception
	 */
	@SuppressWarnings({ "rawtypes", "unchecked" })
	@RequestMapping(value="/stm/stm4000/stm4000/selectStm4000LicGrpAdminPrjListAjax.do")
	public ModelAndView selectStm4000LicGrpAdminPrjListAjax(HttpServletRequest request, HttpServletResponse response, ModelMap model ) throws Exception {
		try{
			//리퀘스트에서 넘어온 파라미터를 맵으로 세팅
			Map<String, String> paramMap = RequestConvertor.requestParamToMap(request, true);
	    		
			// 라이선스 그룹에서 관리자 권한이 있는 모든 프로젝트 목록 조회
			List<Map> selectLicGrpPrjList = (List) stm4000Service.selectStm4000LicGrpAdminPrjList(paramMap);

			model.put("adminPrjList", selectLicGrpPrjList);
	        	
			// 조회 성공여부 및 조회 성공 메시지 세팅
			model.addAttribute("errorYN", "N");
			model.addAttribute("message", egovMessageSource.getMessage("success.common.select"));
	 
			return new ModelAndView("jsonView", model);
		}
		catch(Exception ex){
			Log.error("selectStm4000LicGrpPrjListAjax()", ex);
			// 조회 실패여부 및 조회 실패 메시지 세팅
			model.addAttribute("errorYN", "Y");
			model.addAttribute("message", egovMessageSource.getMessage("fail.common.select"));
			throw new Exception(ex.getMessage());
		}
	}

}
