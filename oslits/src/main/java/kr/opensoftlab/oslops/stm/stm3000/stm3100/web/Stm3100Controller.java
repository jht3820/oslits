package kr.opensoftlab.oslits.stm.stm3000.stm3100.web;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;




















import kr.opensoftlab.oslits.com.fms.web.service.FileMngService;
import kr.opensoftlab.oslits.com.vo.LoginVO;
import kr.opensoftlab.oslits.stm.stm3000.stm3100.service.Stm3100Service;
import kr.opensoftlab.oslits.stm.stm3000.stm3100.vo.Stm3100VO;
import kr.opensoftlab.sdf.util.ReqHistoryMngUtil;
import kr.opensoftlab.sdf.util.RequestConvertor;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.multipart.MultipartHttpServletRequest;
import org.springframework.web.servlet.ModelAndView;

import egovframework.com.cmm.EgovMessageSource;
import egovframework.com.cmm.service.EgovFileMngUtil;
import egovframework.com.cmm.service.FileVO;
import egovframework.rte.fdl.idgnr.EgovIdGnrService;
import egovframework.rte.fdl.property.EgovPropertyService;
import egovframework.rte.ptl.mvc.tags.ui.pagination.PaginationInfo;

/**
 * @Class Name : Stm3100Controller.java
 * @Description : Stm3100Controller Controller class
 * @Modification Information
 *
 * @author 공대영
 *  * @since 2018.07.23.
 * @version 1.0
 * @see
 *  
 *  Copyright (C)  All right reserved.
 */

@Controller
public class Stm3100Controller {

	/** 로그4j 로거 로딩 */
	private static final Logger Log = Logger.getLogger(Stm3100Controller.class);

	/** EgovMessageSource */
	@Resource(name = "egovMessageSource")
	EgovMessageSource egovMessageSource;

	/** EgovPropertyService */
	@Resource(name = "propertiesService")
	protected EgovPropertyService propertiesService;


	/** FileMngService */
	@Resource(name="fileMngService")
	private FileMngService fileMngService;

	@Value("${Globals.fileStorePath}")
	private String tempPath;

	/** EgovFileMngUtil - 파일 업로드 Util */
	@Resource(name="EgovFileMngUtil")
	private EgovFileMngUtil fileUtil;	

	@Resource(name = "egovFileIdGnrService")
	private EgovIdGnrService idgenService;

	@Resource(name = "historyMng")
	private ReqHistoryMngUtil historyMng;
	
	@Resource(name = "stm3100Service")
	private Stm3100Service stm3100Service;
	

	/**
	 * JENKINS 관리 화면이동
	 * @param request
	 * @param response
	 * @param model
	 * @return
	 * @throws Exception
	 */
	@RequestMapping(value="/stm/stm3000/stm3100/selectStm3100View.do")
	public String selectStm3100View(HttpServletRequest request, HttpServletResponse response, ModelMap model ) throws Exception {
		return "/stm/stm3000/stm3100/stm3100";
	}
	/**
	 * 
	 * JENKINS 관리 목록 조회
	 * 
	 * @param request
	 * @param response
	 * @param model
	 * @return
	 * @throws Exception
	 */
	@SuppressWarnings({ "unchecked", "rawtypes" })
	@RequestMapping(value="/stm/stm3000/stm3100/selectStm3100JenkinsProjectListAjax.do")
    public ModelAndView selectStm3100JenkinsProjectListAjax(HttpServletRequest request, HttpServletResponse response, ModelMap model ) throws Exception {
    	try{
    		// request 파라미터를 map으로 변환
        	Map<String, String> paramMap = RequestConvertor.requestParamToMap(request, true);
        	
 
    		//로그인VO 가져오기
    		HttpSession ss = request.getSession();
    		LoginVO loginVO = (LoginVO) ss.getAttribute("loginVO");
    		String selPrjGrpId = (String)ss.getAttribute("selPrjGrpId");
    		
    		paramMap.put("licGrpId", loginVO.getLicGrpId());
    		paramMap.put("prjGrpId", selPrjGrpId);
    		List<Map> jenList = stm3100Service.selectStm3100JenkinsProjectList(paramMap);


    		model.addAttribute("jenList", jenList);
    		model.addAttribute("errorYn", 'N');
        	model.addAttribute("message", egovMessageSource.getMessage("success.common.select"));
        	
        	return new ModelAndView("jsonView", model);
    	}
    	catch(Exception ex){
    		Log.error("selectStm3100JenkinsProjectListAjax()", ex);
    		
    		model.addAttribute("errorYn", 'Y');
    		model.addAttribute("message", egovMessageSource.getMessage("fail.common.select"));
    		return new ModelAndView("jsonView");
    	}
    }

	/**
	 * JENKINS 허용 설정 등록 팝업 화면이동
	 * 
	 * @param request
	 * @param response
	 * @param model
	 * @return
	 * @throws Exception
	 */
	@RequestMapping(value="/stm/stm3000/stm3100/selectStm3101View.do")
	public String selectStm3101View(HttpServletRequest request, HttpServletResponse response, ModelMap model ) throws Exception {
		return "/stm/stm3000/stm3100/stm3101";
	}
	
	/**
	 * JENKINS 허용 역할그룹 설정 등록 팝업 화면이동
	 * 
	 * @param request
	 * @param response
	 * @param model
	 * @return
	 * @throws Exception
	 */
	@RequestMapping(value="/stm/stm3000/stm3100/selectStm3102View.do")
	public String selectStm3102View(HttpServletRequest request, HttpServletResponse response, ModelMap model ) throws Exception {
		return "/stm/stm3000/stm3100/stm3102";
	}
	
	/**
	 * 
	 * Jenkins 프로젝트 권한 설정 목록 조회
	 * 
	 * @param request
	 * @param response
	 * @param model
	 * @return
	 * @throws Exception
	 */
	@SuppressWarnings({ "unchecked", "rawtypes" })
	@RequestMapping(value="/stm/stm3000/stm3100/selectStm3100JenkinsProjectAuthListAjax.do")
    public ModelAndView selectStm3100JenkinsProjectAuthListAjax(HttpServletRequest request, HttpServletResponse response, ModelMap model ) throws Exception {
    	try{
    		// request 파라미터를 map으로 변환
        	Map<String, String> paramMap = RequestConvertor.requestParamToMap(request, true);
        	
 
    		//로그인VO 가져오기
    		HttpSession ss = request.getSession();
    		LoginVO loginVO = (LoginVO) ss.getAttribute("loginVO");
    		String selPrjGrpId = (String)ss.getAttribute("selPrjGrpId");
    		
    		paramMap.put("licGrpId", loginVO.getLicGrpId());
    		paramMap.put("prjGrpId", selPrjGrpId);
    		List<Map> jenList = stm3100Service.selectStm3100JenkinsProjectAuthList(paramMap);


    		model.addAttribute("jenList", jenList);
    		model.addAttribute("errorYn", 'N');
        	model.addAttribute("message", egovMessageSource.getMessage("success.common.select"));
        	
        	return new ModelAndView("jsonView", model);
    	}
    	catch(Exception ex){
    		Log.error("selectStm3100JenkinsProjectAuthListAjax()", ex);
    		
    		model.addAttribute("errorYn", 'Y');
    		model.addAttribute("message", egovMessageSource.getMessage("fail.common.select"));
    		return new ModelAndView("jsonView");
    	}
    }
	
	
	/**
	 * 
	 * Jenkins 프로젝트 권한 설정 지정/해제
	 * 
	 * @param request
	 * @param response
	 * @param model
	 * @return
	 * @throws Exception
	 */
	@RequestMapping(value="/stm/stm3000/stm3100/saveStm3100Ajax.do")
    public ModelAndView saveStm3100Ajax(HttpServletRequest request, HttpServletResponse response, ModelMap model ) throws Exception {
    	try{
    		// request 파라미터를 map으로 변환
        	Map<String, String> paramMap = RequestConvertor.requestParamToMap(request, true);
        	HttpSession ss = request.getSession();
    		LoginVO loginVO = (LoginVO) ss.getAttribute("loginVO");
       	
        	String[] jenIds =  request.getParameterValues("jenId");
        	String[] isCheckeds =  request.getParameterValues("isChecked");
        	String[] orgCheckeds =  request.getParameterValues("orgChecked");
        	
        	List<Stm3100VO> list = new ArrayList<Stm3100VO>();
        	
        	Stm3100VO stm3100VO =null;
        	
        	for (int i = 0; i < jenIds.length; i++) {
        		stm3100VO  = new Stm3100VO();
        		
        		stm3100VO.setPrjId(paramMap.get("prjId"));
        		stm3100VO.setJenId(jenIds[i]);
        		stm3100VO.setIsChecked( isCheckeds[i]  );
        		stm3100VO.setOrgChecked(orgCheckeds[i]);
        		
        		stm3100VO.setRegUsrId(paramMap.get("regUsrId"));
        		stm3100VO.setRegUsrIp(paramMap.get("regUsrIp"));
        		stm3100VO.setLicGrpId(loginVO.getLicGrpId());
        		
        		list.add(stm3100VO);
			}
        	
        	// 프로젝트 생성관리 수정
        	stm3100Service.saveStm3100(list);
        	
        	model.put("prjId", paramMap.get("prjId"));
      
        	model.addAttribute("message", egovMessageSource.getMessage("success.common.save"));
        	
        	return new ModelAndView("jsonView", model);
    	}
    	catch(Exception ex){
    		Log.error("saveStm3100Ajax()", ex);
    		model.addAttribute("message", egovMessageSource.getMessage("fail.common.save"));
    		return new ModelAndView("jsonView");
    	}
    }

}
