package kr.opensoftlab.oslits.stm.stm2000.stm2100.web;

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
import kr.opensoftlab.oslits.stm.stm2000.stm2100.service.Stm2100Service;
import kr.opensoftlab.oslits.stm.stm2000.stm2100.vo.Stm2100VO;
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
	
	@Resource(name = "stm2100Service")
	private Stm2100Service stm2100Service;
	

	/**
	 * Repository 설정 화면으로 이동
	 * 
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
	 * 
	 * 프로젝트 별 Repository 권한 조회
	 * 
	 * @param request
	 * @param response
	 * @param model
	 * @return
	 * @throws Exception
	 */
	@RequestMapping(value="/stm/stm2000/stm2100/selectStm2100ProjectListAjax.do")
	public ModelAndView selectStm2100ProjectList( HttpServletRequest request, HttpServletResponse response, ModelMap model ) throws Exception {

		try{
			// request 파라미터를 map으로 변환
			Map<String, String> paramMap = RequestConvertor.requestParamToMapAddSelInfo(request, true);
			//현재 페이지 값, 보여지는 개체 수
			
			HttpSession ss = request.getSession();
    		LoginVO loginVO = (LoginVO) ss.getAttribute("loginVO");

    		
        	paramMap.put("licGrpId", loginVO.getLicGrpId());
			
			paramMap.put("prjGrpId", (String)ss.getAttribute("selPrjGrpId"));
			
			List svnList = stm2100Service.selectStm2100ProjectListAjax(paramMap);
						
			
			model.addAttribute("svnList", svnList);
			

			return new ModelAndView("jsonView");
		}
		catch(Exception ex){
			Log.error("selectStm2100ProjectList()", ex);
			throw new Exception(ex.getMessage());
		}
	}
	
	
	/**
	 *  REPOSITORY 허용 설정 등록/수정 팝업 화면으로 이동
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
	 * REPOSITORY 허용 역할그룹 설정 등록 팝업 화면이동
	 * 
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
	 * 
	 * 프로젝트별 Repository 권한 목록 조회
	 * 
	 * @param request
	 * @param response
	 * @param model
	 * @return
	 * @throws Exception
	 */
	@RequestMapping(value="/stm/stm2000/stm2100/selectStm2100ProjectAuthList.do")
	public ModelAndView selectStm2100ProjectAuthList( HttpServletRequest request, HttpServletResponse response, ModelMap model ) throws Exception {

		try{
			// request 파라미터를 map으로 변환
			Map<String, String> paramMap = RequestConvertor.requestParamToMapAddSelInfo(request, true);
			//현재 페이지 값, 보여지는 개체 수
			
			HttpSession ss = request.getSession();
    		LoginVO loginVO = (LoginVO) ss.getAttribute("loginVO");

    		
        	paramMap.put("licGrpId", loginVO.getLicGrpId());
			
			paramMap.put("prjGrpId", (String)ss.getAttribute("selPrjGrpId"));
			
			List svnList = stm2100Service.selectStm2100ProjectAuthList(paramMap);
						
			
			model.addAttribute("svnList", svnList);
			

			return new ModelAndView("jsonView");
		}
		catch(Exception ex){
			Log.error("selectStm2100ProjectAuthList()", ex);
			throw new Exception(ex.getMessage());
		}
	}
	


	/**
	 * 
	 * 프로젝트 Repository 권한 지정/해제
	 * 
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
        	
        	// 프로젝트 생성관리 수정
        	stm2100Service.saveStm2100(list);
        	
        	model.put("prjId", paramMap.get("prjId"));
      
        	model.addAttribute("message", egovMessageSource.getMessage("success.common.save"));
        	
        	return new ModelAndView("jsonView", model);
    	}
    	catch(Exception ex){
    		Log.error("saveStm2100Ajax()", ex);
    		model.addAttribute("message", egovMessageSource.getMessage("fail.common.save"));
    		return new ModelAndView("jsonView");
    	}
    }

	
}
