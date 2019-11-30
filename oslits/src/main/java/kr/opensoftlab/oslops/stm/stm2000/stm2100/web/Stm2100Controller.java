package kr.opensoftlab.oslops.stm.stm2000.stm2100.web;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;



























import kr.opensoftlab.oslops.com.fms.web.service.FileMngService;
import kr.opensoftlab.oslops.com.vo.LoginVO;
import kr.opensoftlab.oslops.stm.stm2000.stm2100.service.Stm2100Service;
import kr.opensoftlab.oslops.stm.stm2000.stm2100.vo.Stm2100VO;
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
 * @Class Name : Stm2100Controller.java
 * @Description : Stm2100Controller Controller class
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
public class Stm2100Controller {

	/** 로그4j 로거 로딩 */
	private static final Logger Log = Logger.getLogger(Stm2100Controller.class);

	/** EgovMessageSource */
	@Resource(name = "egovMessageSource")
	EgovMessageSource egovMessageSource;

	/** EgovPropertyService */
	@Resource(name = "propertiesService")
	protected EgovPropertyService propertiesService;


	@Value("${Globals.fileStorePath}")
	private String tempPath;
	
	/** Stm2100Service DI */
	@Resource(name = "stm2100Service")
	private Stm2100Service stm2100Service;
	

	/**
	 * Stm2100 SVN Repository 배정관리 화면으로 이동한다.
	 * @param request
	 * @param response
	 * @param model
	 * @return
	 * @throws Exception
	 */
	@RequestMapping(value="/stm/stm2000/stm2100/selectStm2100View.do")
	public String selectStm2100View(HttpServletRequest request, HttpServletResponse response, ModelMap model ) throws Exception {
		return "/stm/stm2000/stm2100/stm2100";
	}
	
	/**
	 * Stm2100 프로젝트에 배정된 SVN Repository 목록을 조회한다.
	 * @param request
	 * @param response
	 * @param model
	 * @return
	 * @throws Exception
	 */
	@SuppressWarnings("rawtypes")
	@RequestMapping(value="/stm/stm2000/stm2100/selectStm2100ProjectListAjax.do")
	public ModelAndView selectStm2100ProjectList( HttpServletRequest request, HttpServletResponse response, ModelMap model ) throws Exception {

		try{
			// request 파라미터를 map으로 변환
			Map<String, String> paramMap = RequestConvertor.requestParamToMapAddSelInfo(request, true);

			HttpSession ss = request.getSession();
    		LoginVO loginVO = (LoginVO) ss.getAttribute("loginVO");

        	paramMap.put("licGrpId", loginVO.getLicGrpId());
			paramMap.put("prjGrpId", (String)ss.getAttribute("selPrjGrpId"));
			
			// 프로젝트에 배정된 SVN Repository 목록을 조회
			List svnList = stm2100Service.selectStm2100ProjectListAjax(paramMap);
						
			model.addAttribute("svnList", svnList);
			
			// 조회성공 여부 및 조회 성공메시지 세팅
			model.addAttribute("errorYn", "N");
			model.addAttribute("message", egovMessageSource.getMessage("success.common.select"));

			return new ModelAndView("jsonView");
		}
		catch(Exception ex){
			Log.error("selectStm2100ProjectList()", ex);
			// 조회 실패여부 및 실패메시지 세팅
			model.addAttribute("errorYn", "Y");
			model.addAttribute("message", egovMessageSource.getMessage("fail.common.select"));
			return new ModelAndView("jsonView");
		}
	}
	
	/**
	 * Stm2100 SVN Repository 배정/배정제외 팝업으로 이동한다.
	 * @param request
	 * @param response
	 * @param model
	 * @return
	 * @throws Exception
	 */
	@RequestMapping(value="/stm/stm2000/stm2100/selectStm2101View.do")
	public String selectStm2101View(HttpServletRequest request, HttpServletResponse response, ModelMap model ) throws Exception {
		return "/stm/stm2000/stm2100/stm2101";
	}

	/**
	 * Stm2100 SVN Repository 허용 역할그룹 설정 등록 팝업으로 이동한다.
	 * @param request
	 * @param response
	 * @param model
	 * @return
	 * @throws Exception
	 */
	@RequestMapping(value="/stm/stm2000/stm2100/selectStm2102View.do")
	public String selectStm2102View(HttpServletRequest request, HttpServletResponse response, ModelMap model ) throws Exception {
		return "/stm/stm2000/stm2100/stm2102";
	}
	
	/**
	 * Stm2100 프로젝트에 배정 가능한 SVN Repository 목록을 조회한다.
	 * @param request
	 * @param response
	 * @param model
	 * @return
	 * @throws Exception
	 */
	@SuppressWarnings("rawtypes")
	@RequestMapping(value="/stm/stm2000/stm2100/selectStm2100ProjectAuthList.do")
	public ModelAndView selectStm2100ProjectAuthList( HttpServletRequest request, HttpServletResponse response, ModelMap model ) throws Exception {

		try{
			// request 파라미터를 map으로 변환
			Map<String, String> paramMap = RequestConvertor.requestParamToMapAddSelInfo(request, true);
			
			HttpSession ss = request.getSession();
    		LoginVO loginVO = (LoginVO) ss.getAttribute("loginVO");

        	paramMap.put("licGrpId", loginVO.getLicGrpId());
			paramMap.put("prjGrpId", (String)ss.getAttribute("selPrjGrpId"));
			
			// 프로젝트에 배정 가능한 SVN Repository 목록 조회
			List svnList = stm2100Service.selectStm2100ProjectAuthList(paramMap);

			model.addAttribute("svnList", svnList);
			
			// 조회성공 여부 및 조회 성공메시지 세팅
			model.addAttribute("errorYn", "N");
			model.addAttribute("message", egovMessageSource.getMessage("success.common.select"));

			return new ModelAndView("jsonView");
		}
		catch(Exception ex){
			Log.error("selectStm2100ProjectAuthList()", ex);
			// 조회 실패여부 및 조회실패 메시지 세팅
			model.addAttribute("errorYn", "Y");
			model.addAttribute("message", egovMessageSource.getMessage("fail.common.select"));
			return new ModelAndView("jsonView");
		}
	}
	
	/**
	 * Stm2100 프로젝트에  SVN Repository를 배정/배정제외 한다.
	 * @param request
	 * @param response
	 * @param model
	 * @return
	 * @throws Exception
	 */
	@RequestMapping(value="/stm/stm2000/stm2100/saveStm2100Ajax.do")
    public ModelAndView saveStm2100Ajax(HttpServletRequest request, HttpServletResponse response, ModelMap model ) throws Exception {
    	try{
    		// request 파라미터를 map으로 변환
        	Map<String, String> paramMap = RequestConvertor.requestParamToMap(request, true);
        	
        	HttpSession ss = request.getSession();
    		LoginVO loginVO = (LoginVO) ss.getAttribute("loginVO");

        	
        	String[] svnRepIds =  request.getParameterValues("svnRepId");
        	String[] isCheckeds =  request.getParameterValues("isChecked");
        	String[] orgCheckeds =  request.getParameterValues("orgChecked");
        	
        	List<Stm2100VO> list = new ArrayList<Stm2100VO>();
        	
        	Stm2100VO stm2100VO =null;
        	
        	for (int i = 0; i < svnRepIds.length; i++) {
        		stm2100VO  = new Stm2100VO();
        		
        		stm2100VO.setPrjId(paramMap.get("prjId"));
        		stm2100VO.setSvnRepId(svnRepIds[i]);
        		stm2100VO.setIsChecked( isCheckeds[i]  );
        		stm2100VO.setOrgChecked(orgCheckeds[i]);
        		
        		stm2100VO.setRegUsrId(paramMap.get("regUsrId"));
        		stm2100VO.setRegUsrIp(paramMap.get("regUsrIp"));
        		stm2100VO.setLicGrpId(loginVO.getLicGrpId());
        		
        		list.add(stm2100VO);
			}
        	
        	// 프로젝트에 SVN Repository를 배정/배정제외
        	stm2100Service.saveStm2100(list);
        	
        	model.put("prjId", paramMap.get("prjId"));
        	// 저장 성공 메시지 세팅
        	model.addAttribute("message", egovMessageSource.getMessage("success.common.save"));
        	
        	return new ModelAndView("jsonView", model);
    	}
    	catch(Exception ex){
    		Log.error("saveStm2100Ajax()", ex);
    		// 저장 실패 메시지 세팅
    		model.addAttribute("message", egovMessageSource.getMessage("fail.common.save"));
    		return new ModelAndView("jsonView");
    	}
    }

	
}
