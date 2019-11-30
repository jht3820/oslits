
package kr.opensoftlab.oslops.dpl.dpl1000.dpl1000.web;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.servlet.ModelAndView;

import com.google.gson.GsonBuilder;

import egovframework.com.cmm.EgovMessageSource;
import egovframework.rte.fdl.property.EgovPropertyService;
import egovframework.rte.ptl.mvc.tags.ui.pagination.PaginationInfo;
import kr.opensoftlab.oslops.cmm.cmm4000.cmm4000.service.Cmm4000Service;
import kr.opensoftlab.oslops.com.vo.LoginVO;
import kr.opensoftlab.oslops.dpl.dpl1000.dpl1000.service.Dpl1000Service;
import kr.opensoftlab.oslops.dpl.dpl1000.dpl1000.vo.Dpl1000VO;
import kr.opensoftlab.oslops.dpl.dpl1000.dpl1000.vo.Dpl1300VO;
import kr.opensoftlab.oslops.dpl.dpl1000.dpl1100.service.Dpl1100Service;
import kr.opensoftlab.oslops.stm.stm3000.stm3000.service.Stm3000Service;
import kr.opensoftlab.oslops.stm.stm3000.stm3100.service.Stm3100Service;
import kr.opensoftlab.sdf.jenkins.JenkinsClient;
import kr.opensoftlab.sdf.util.OslAgileConstant;
import kr.opensoftlab.sdf.util.PagingUtil;
import kr.opensoftlab.sdf.util.RequestConvertor;

/**
 * @Class Name : Dpl1000Controller.java
 * @Description : Dpl1000Controller Controller class
 * @Modification Information
 *
 * @author 정형택
 * @since 2016.01.30
 * @version 1.0
 * @see
 *  
 *  --------------------------------------
 *  수정일			수정자			수정내용
 *  --------------------------------------
 *  2019-03-07		진주영		 	기능 개선
 *  
 *  
 *  --------------------------------------
 *  
 *  Copyright (C)  All right reserved.
 */

@Controller
public class Dpl1000Controller {

	/**
     * Logging 을 위한 선언
     * Log는 반드시 가이드에 맞춰서 작성한다. 개발용으로는 debug 카테고리를 사용한다.
     */
	protected Logger Log = Logger.getLogger(this.getClass());
	
	/** System Property 를 사용하기 위한 Bean Injection */
	@Resource(name = "propertiesService")
	protected EgovPropertyService propertiesService;
	
	/** EgovMessageSource */
	@Resource(name = "egovMessageSource")
	EgovMessageSource egovMessageSource;
    
    /** Dpl1000Service DI */
    @Resource(name = "dpl1000Service")
    private Dpl1000Service dpl1000Service;
    
	/** Dpl1000Service DI */
    @Resource(name = "dpl1100Service")
    private Dpl1100Service dpl1100Service;
    
    /** Stm3000Service DI */
	@Resource(name = "stm3000Service")
	private Stm3000Service stm3000Service;
	
	/** Stm3100Service DI */
	@Resource(name = "stm3100Service")
	private Stm3100Service stm3100Service;
	
	 /** Cmm4000Service DI */
    @Resource(name = "cmm4000Service")
    private Cmm4000Service cmm4000Service;
	
	/** JenkinsClient DI */
	@Resource(name = "jenkinsClient")
	private JenkinsClient jenkinsClient;
	
	@Value("${Globals.fileStorePath}")
	private String tempPath;
    
    
    /**
	 * Dpl1000  배포 계획 정보 조회
	 * @param 
	 * @return 
	 * @exception Exception
	 */
	@RequestMapping(value="/dpl/dpl1000/dpl1000/selectDpl1000View.do")
    public String selectDpl1000View(HttpServletRequest request, HttpServletResponse response, ModelMap model ) throws Exception {
    	return "/dpl/dpl1000/dpl1000/dpl1000";
    }
	
    /**
 	 * Dpl1001 배포 계획 등록 팝업
 	 * 
 	 * @param 
 	 * @return 
 	 * @exception Exception
 	 */
     @SuppressWarnings({ "rawtypes", "unchecked" })
 	@RequestMapping(value="/dpl/dpl1000/dpl1000/selectDpl1001View.do")
     public String selectDpl1001View(HttpServletRequest request, HttpServletResponse response, ModelMap model ) throws Exception {
 		
    	 try{
 			// request 파라미터를 map으로 변환
 			Map<String, String> paramMap = RequestConvertor.requestParamToMapAddSelInfo(request, true);
 			paramMap.put("prjId", (String)request.getSession().getAttribute("selPrjId"));
 			
 			Map dpl1000DplInfo = null;
 			List<Map> dpl1000DplJobList = null;
 			String dpl1000DplJobListJson = "{}";
 			
 			
 			// 팝업 타입 - 등록/수정/상세보기
 			String pageType = paramMap.get("popupGb");
 			
 			//수정또는 상세보기이면 배포버전 정보를 조회하여 화면에 세팅한다.
 			if( "update".equals(pageType) || "select".equals(pageType)){
 				dpl1000DplInfo = dpl1000Service.selectDpl1000DeployVerInfo(paramMap);
 				dpl1000DplJobList = dpl1000Service.selectDpl1300DeployJobList(paramMap);
 				
 				dpl1000DplJobListJson = (new GsonBuilder().serializeNulls().create()).toJsonTree(dpl1000DplJobList).toString();
 			}
 			
 			model.put("dpl1000DplInfo", dpl1000DplInfo);
 			model.put("dpl1000DplJobList", dpl1000DplJobList);
 			model.put("dpl1000DplJobListJson", dpl1000DplJobListJson.replaceAll("<", "&lt"));

 			return "/dpl/dpl1000/dpl1000/dpl1001";
 		}
 		catch(Exception ex){
 			Log.error("selectReq1001View()", ex);
 			throw new Exception(ex.getMessage());
 		}
     }
    
     
     /**
      * Dpl1002 JOB 등록 팝업
      * 
      * @param 
      * @return 
      * @exception Exception
      */
     @RequestMapping(value="/dpl/dpl1000/dpl1000/selectDpl1002View.do")
     public String selectDpl1002View(HttpServletRequest request, HttpServletResponse response, ModelMap model ) throws Exception {
		 return "/dpl/dpl1000/dpl1000/dpl1002";
     }
     
     /**
      * Dpl1003 배포계획 상세 팝업
      * 
      * @param 
      * @return 
      * @exception Exception
      */
     @SuppressWarnings("rawtypes")
	@RequestMapping(value="/dpl/dpl1000/dpl1000/selectDpl1003View.do")
     public String selectDpl1003View(HttpServletRequest request, HttpServletResponse response, ModelMap model ) throws Exception {
    	  try{
 			// request 파라미터를 map으로 변환
 			Map<String, String> paramMap = RequestConvertor.requestParamToMapAddSelInfo(request, true);
 			
 			//배포계획 상세 정보
 			Map dpl1000DplInfo = dpl1000Service.selectDpl1000DeployVerInfo(paramMap);
			
 			model.put("prjId", paramMap.get("prjId"));
 			model.put("dpl1000DplInfo", dpl1000DplInfo);
 			
 			// 어느 화면에서 배포계획 상세 팝업을 호출했는지 구분하기 위한 값
 			// 1.통합대시보드 - 요구사항 상세 - 배포계획 상세  or 2.쪽지에서 다른 프로젝트의 요구사항 상세 - 배포계획 상세
 			// 이렇게 배포계획 상세 팝업 호출시 callView 값을 가지고 콘솔 로그 확인시 어떻게 권한을 체크할지 판단한다.
 			model.put("callView", paramMap.get("callView"));

 			return "/dpl/dpl1000/dpl1000/dpl1003";
 		}
 		catch(Exception ex){
 			Log.error("selectDpl1003View()", ex);
 			throw new Exception(ex.getMessage());
 		}
    	 
     }

     /**
	 * Dpl1000 배포자 리스트를 가져온다.
	 * @param 
	 * @return 
	 * @exception Exception
	 */
	@SuppressWarnings({ "unchecked", "rawtypes" })
	@RequestMapping(method=RequestMethod.POST, value="/dpl/dpl1000/dpl1000/selectDpl1000DeployNmListAjax.do")
    public ModelAndView selectDpl1000DeployNmListAjax(HttpServletRequest request, HttpServletResponse response, ModelMap model ) throws Exception {
    	try{

    		//리퀘스트에서 넘어온 파라미터를 맵으로 세팅
        	Map paramMap = RequestConvertor.requestParamToMap(request, true);
        	paramMap.put("prjId", request.getSession().getAttribute("selPrjId"));

        	//배포 계획 정보 리스트 조회
        	List<Map> dplDeployNmList = (List) dpl1000Service.selectDpl1000DeployNmList(paramMap);
        	
        	model.addAttribute("dplDeployNmList", dplDeployNmList);
        	
        	//조회성공메시지 세팅
        	model.addAttribute("message", egovMessageSource.getMessage("success.common.select"));
        	
        	return new ModelAndView("jsonView", model);
    	}
    	catch(Exception ex){
    		Log.error("selectDpl1000DeployNmListAjax()", ex);
    		
    		//조회실패 메시지 세팅
    		model.addAttribute("message", egovMessageSource.getMessage("fail.common.select"));
    		return new ModelAndView("jsonView", model);
    	}
    }
    
	/**
	 * Dpl1000 배포 계획 정보 리스트를 가져온다.
	 * @param 
	 * @return 
	 * @exception Exception
	 */
	@RequestMapping(method=RequestMethod.POST, value="/dpl/dpl1000/dpl1000/selectDpl1000DeployVerInfoListAjax.do")
    public ModelAndView selectDpl1000DeployVerInfoListAjax(@ModelAttribute("dpl1000VO") Dpl1000VO dpl1000VO, HttpServletRequest request, HttpServletResponse response, ModelMap model ) throws Exception {
    	try{

    		//리퀘스트에서 넘어온 파라미터를 맵으로 세팅
        	Map<String, String> paramMap = RequestConvertor.requestParamToMapAddSelInfo(request, true);
        	
        	//현재 페이지 값, 보여지는 개체 수
			String _pageNo_str = paramMap.get("pageNo");
			String _pageSize_str = paramMap.get("pageSize");
			
			int _pageNo = 1;
			int _pageSize = OslAgileConstant.pageSize;
			
			if(_pageNo_str != null && !"".equals(_pageNo_str)){
				_pageNo = Integer.parseInt(_pageNo_str)+1;  
			}
			if(_pageSize_str != null && !"".equals(_pageSize_str)){
				_pageSize = Integer.parseInt(_pageSize_str);  
			}
			
			//페이지 사이즈
			dpl1000VO.setPageIndex(_pageNo);
			dpl1000VO.setPageSize(_pageSize);
			dpl1000VO.setPageUnit(_pageSize);
        	
        	
        	PaginationInfo paginationInfo = PagingUtil.getPaginationInfo(dpl1000VO); /** paging - 신규방식 */
			List<Dpl1000VO> dpl1000List = null;
			
			HttpSession ss = request.getSession();
			String prjId	= (String) ss.getAttribute("selPrjId");
			String licGrpId	= (String) paramMap.get("licGrpId");
			
			dpl1000VO.setPrjId(prjId);
			dpl1000VO.setLicGrpId(licGrpId);
        	//배포 계획 정보 리스트 조회
        	dpl1000List = (List<Dpl1000VO>) dpl1000Service.selectDpl1000DeployVerInfoList(dpl1000VO);
        	/** 총 데이터의 건수 를 가져온다. */
			int totCnt = dpl1000Service.selectDpl1000ListCnt(dpl1000VO);
					
        	paginationInfo.setTotalRecordCount(totCnt);
        	
        	model.addAttribute("list", dpl1000List);
        	
			//페이지 정보 보내기
			Map<String, Integer> pageMap = new HashMap<String, Integer>();
			pageMap.put("pageNo",dpl1000VO.getPageIndex());
			pageMap.put("listCount", dpl1000List.size());
			pageMap.put("totalPages", paginationInfo.getTotalPageCount());
			pageMap.put("totalElements", totCnt);
			pageMap.put("pageSize", _pageSize);
			
			model.addAttribute("page", pageMap);
        	
        	//조회성공메시지 세팅
        	model.addAttribute("message", egovMessageSource.getMessage("success.common.select"));
        	
        	return new ModelAndView("jsonView", model);
    	}
    	catch(Exception ex){
    		Log.error("selectDpl1000DeployInfoListAjax()", ex);
    		
    		//조회실패 메시지 세팅
    		model.addAttribute("message", egovMessageSource.getMessage("fail.common.select"));
    		return new ModelAndView("jsonView", model);
    	}
    }
	
	/**
	 * Dpl1300 배포 계획 배정 JOB 정보 리스트를 가져온다.
	 * @param 
	 * @return 
	 * @exception Exception
	 */
	@RequestMapping(method=RequestMethod.POST, value="/dpl/dpl1000/dpl1000/selectDpl1300DplJobListAjax.do")
    public ModelAndView selectDpl1300DplJobListAjax(@ModelAttribute("dpl1300VO") Dpl1300VO dpl1300VO, HttpServletRequest request, HttpServletResponse response, ModelMap model ) throws Exception {
    	try{

    		//리퀘스트에서 넘어온 파라미터를 맵으로 세팅
        	Map<String, String> paramMap = RequestConvertor.requestParamToMapAddSelInfo(request, true);
        	
        	//현재 페이지 값, 보여지는 개체 수
			String _pageNo_str = paramMap.get("pageNo");
			String _pageSize_str = paramMap.get("pageSize");
			
			int _pageNo = 1;
			int _pageSize = OslAgileConstant.pageSize;
			
			if(_pageNo_str != null && !"".equals(_pageNo_str)){
				_pageNo = Integer.parseInt(_pageNo_str)+1;  
			}
			if(_pageSize_str != null && !"".equals(_pageSize_str)){
				_pageSize = Integer.parseInt(_pageSize_str);  
			}
			
			//페이지 사이즈
			dpl1300VO.setPageIndex(_pageNo);
			dpl1300VO.setPageSize(_pageSize);
			dpl1300VO.setPageUnit(_pageSize);
        	
        	
        	PaginationInfo paginationInfo = PagingUtil.getPaginationInfo(dpl1300VO); /** paging - 신규방식 */
			List<Dpl1300VO> dpl1300List = null;
			
			HttpSession ss = request.getSession();
			String licGrpId	= (String) paramMap.get("licGrpId");

			String  prjId = "";
			// 넘어온 프로젝트 Id 있을경우 세팅
			if(paramMap.get("popupPrjId") != null){
				prjId = paramMap.get("popupPrjId");
			}
			else if(paramMap.get("prjId") != null){
				prjId = paramMap.get("prjId");
			}
			// 넘어온 프로젝트 Id 없을경우
			else{
				// 세션의 선택된 프로젝트 아이디 가져오기
				prjId = (String) ss.getAttribute("selPrjId");
			}
			
			dpl1300VO.setPrjId(prjId);
			dpl1300VO.setLicGrpId(licGrpId);
        	//배포 계획 정보 리스트 조회
        	dpl1300List = (List<Dpl1300VO>) dpl1000Service.selectDpl1300dplJobGridList(dpl1300VO);
        	/** 총 데이터의 건수 를 가져온다. */
			int totCnt = dpl1000Service.selectDpl1300dplJobGridListCnt(dpl1300VO);
					
        	paginationInfo.setTotalRecordCount(totCnt);
        	
        	model.addAttribute("list", dpl1300List);
        	
			//페이지 정보 보내기
			Map<String, Integer> pageMap = new HashMap<String, Integer>();
			pageMap.put("pageNo",dpl1300VO.getPageIndex());
			pageMap.put("listCount", dpl1300List.size());
			pageMap.put("totalPages", paginationInfo.getTotalPageCount());
			pageMap.put("totalElements", totCnt);
			pageMap.put("pageSize", _pageSize);
			
			model.addAttribute("page", pageMap);
        	
        	//조회성공메시지 세팅
        	model.addAttribute("message", egovMessageSource.getMessage("success.common.select"));
        	
        	return new ModelAndView("jsonView", model);
    	}
    	catch(Exception ex){
    		Log.error("selectDpl1300DplJobListAjax()", ex);
    		
    		//조회실패 메시지 세팅
    		model.addAttribute("message", egovMessageSource.getMessage("fail.common.select"));
    		return new ModelAndView("jsonView", model);
    	}
    }
	
	/**
	 * Dpl1000 배포 계획 등록(단건).
	 * 배포 계획 등록후 등록 정보 결과 및 정보를 화면으로 리턴한다.
	 * @param 
	 * @return 
	 * @exception Exception
	 */
	@RequestMapping(method=RequestMethod.POST, value="/dpl/dpl1000/dpl1000/saveDpl1000DeployVerInfoAjax.do")
    public ModelAndView saveDpl1000DeployVerInfoAjax(HttpServletRequest request, HttpServletResponse response, ModelMap model ) throws Exception {
    	try{

    		//리퀘스트에서 넘어온 파라미터를 맵으로 세팅
    		Map<String, String> paramMap = RequestConvertor.requestParamToMapAddSelInfo(request, true);
    		HttpSession ss = request.getSession();
    		String prjId = (String) ss.getAttribute("selPrjId");
			paramMap.put("prjId", prjId);
			
			if( paramMap.get("popupGb").toString().equals("insert") ){
	        	//배포 계획 등록
	        	dpl1000Service.insertDpl1000DeployVerInfo(paramMap);
			} else {
				//배포 계획 수정
				dpl1000Service.updateDpl1000DeployVerInfo(paramMap);
			}
        	//조회성공메시지 세팅
        	model.addAttribute("message", egovMessageSource.getMessage("success.common.save"));
        	
        	return new ModelAndView("jsonView", model);
    	}
    	catch(Exception ex){
    		Log.error("saveDpl1000DeployVerInfoAjax()", ex);
    		
    		//조회실패 메시지 세팅
    		model.addAttribute("message", egovMessageSource.getMessage("fail.common.save"));
    		return new ModelAndView("jsonView", model);
    	}
	}
	
	
	/**
	 * Dpl1000 배포 계획 결재 요청
	 * @param 
	 * @return 
	 * @exception Exception
	 */
	@RequestMapping(method=RequestMethod.POST, value="/dpl/dpl1000/dpl1000/insertDpl1000DplsignRequestAjax.do")
    public ModelAndView insertDpl1000DplsignRequestAjax(HttpServletRequest request, HttpServletResponse response, ModelMap model ) throws Exception {
    	try{

    		//리퀘스트에서 넘어온 파라미터를 맵으로 세팅
    		Map<String, String> paramMap = RequestConvertor.requestParamToMapAddSelInfo(request, true);
    		HttpSession ss = request.getSession();
    		String prjId = (String) ss.getAttribute("selPrjId");
			paramMap.put("prjId", prjId);
			
			//결재 요청
			dpl1000Service.insertDpl1000DplSignRequestList(paramMap);
			
        	//조회성공메시지 세팅
			model.addAttribute("errorYn", "N");
        	model.addAttribute("message", egovMessageSource.getMessage("success.common.insert"));
        	
        	return new ModelAndView("jsonView", model);
    	}
    	catch(Exception ex){
    		Log.error("insertDpl1000DplsignRequestAjax()", ex);
    		
    		//조회실패 메시지 세팅
    		model.addAttribute("errorYn", "Y");
    		model.addAttribute("message", egovMessageSource.getMessage("fail.common.insert"));
    		return new ModelAndView("jsonView", model);
    	}
	}
	
	/**
	 * Dpl1000 배포 계획 리스트 삭제.
	 * 배포 계획 삭제후 삭제 정보 결과 및 정보를 화면으로 리턴한다.
	 * @param 
	 * @return 
	 * @exception Exception
	 */
	@RequestMapping(method=RequestMethod.POST, value="/dpl/dpl1000/dpl1000/deleteDpl1000DeployVerInfoListAjax.do")
    public ModelAndView deleteDpl1000DeployVerInfoListAjax(HttpServletRequest request, HttpServletResponse response, ModelMap model) throws Exception {
    	try{
    		//리퀘스트에서 넘어온 파라미터를 맵으로 세팅
			Map<String, Object> paramMap = RequestConvertor.requestParamToMapAddSelInfoList(request, true,"dplId");
        	paramMap.put("prjId", request.getSession().getAttribute("selPrjId"));
        	
    		dpl1000Service.deleteDpl1000DeployVerInfo(paramMap);
    		
    		//조회성공메시지 세팅
    		model.addAttribute("successYn", "Y");
        	model.addAttribute("message", egovMessageSource.getMessage("success.common.delete"));
        	
        	return new ModelAndView("jsonView", model);
    	}
    	catch(Exception ex){
    		Log.error("deleteSpr1000SprintInfoListAjax()", ex);
    		
    		//조회실패 메시지 세팅
    		model.addAttribute("message", egovMessageSource.getMessage("fail.common.delete"));
    		return new ModelAndView("jsonView", model);
    	}
    }
	
	/**
	 * Dpl1000 배포 계획 정보 리스트를 가져온다.
	 * @param 
	 * @return 
	 * @exception Exception
	 */
	@RequestMapping(value="/dpl/dpl1000/dpl1000/selectDpl1000BuildInfoListAjax.do")
    public ModelAndView selectDpl1000BuildInfoListAjax(@ModelAttribute("dpl1000VO") Dpl1000VO dpl1000VO, HttpServletRequest request, HttpServletResponse response, ModelMap model ) throws Exception {
    	try{

    		//리퀘스트에서 넘어온 파라미터를 맵으로 세팅
        	Map<String, String> paramMap = RequestConvertor.requestParamToMapAddSelInfo(request, true);
        	
        	//현재 페이지 값, 보여지는 개체 수
			String _pageNo_str = paramMap.get("pageNo");
			String _pageSize_str = paramMap.get("pageSize");
			
			int _pageNo = 1;
			int _pageSize = OslAgileConstant.pageSize;
			
			if(_pageNo_str != null && !"".equals(_pageNo_str)){
				_pageNo = Integer.parseInt(_pageNo_str)+1;  
			}
			if(_pageSize_str != null && !"".equals(_pageSize_str)){
				_pageSize = Integer.parseInt(_pageSize_str);  
			}
			
			//페이지 사이즈
			dpl1000VO.setPageIndex(_pageNo);
			dpl1000VO.setPageSize(_pageSize);
			dpl1000VO.setPageUnit(_pageSize);
        	
        	
        	PaginationInfo paginationInfo = PagingUtil.getPaginationInfo(dpl1000VO); /** paging - 신규방식 */
			List<Dpl1000VO> dpl1000List = null;
			
			HttpSession ss = request.getSession();
			String prjId	= (String) ss.getAttribute("selPrjId");
			String licGrpId	= (String) paramMap.get("licGrpId");
			
			dpl1000VO.setPrjId(prjId);
			dpl1000VO.setLicGrpId(licGrpId);
			
			paramMap.put("prjId", prjId);
			
		
						
        	//배포 계획 정보 리스트 조회
        	dpl1000List = (List<Dpl1000VO>) dpl1000Service.selectDpl1000BuildInfoList(dpl1000VO);
        	/** 총 데이터의 건수 를 가져온다. */
			int totCnt = dpl1000Service.selectDpl1000BuildInfoListCnt(dpl1000VO);
					
        	paginationInfo.setTotalRecordCount(totCnt);
        	
        	model.addAttribute("list", dpl1000List);
        	
			//페이지 정보 보내기
			Map<String, Integer> pageMap = new HashMap<String, Integer>();
			pageMap.put("pageNo",dpl1000VO.getPageIndex());
			pageMap.put("listCount", dpl1000List.size());
			pageMap.put("totalPages", paginationInfo.getTotalPageCount());
			pageMap.put("totalElements", totCnt);
			pageMap.put("pageSize", _pageSize);
			
			model.addAttribute("page", pageMap);
        	
        	//조회성공메시지 세팅
        	model.addAttribute("message", egovMessageSource.getMessage("success.common.select"));
        	
        	return new ModelAndView("jsonView", model);
    	}
    	catch(Exception ex){
    		Log.error("selectDpl1000BuildInfoListAjax()", ex);
    		
    		//조회실패 메시지 세팅
    		model.addAttribute("message", egovMessageSource.getMessage("fail.common.select"));
    		return new ModelAndView("jsonView", model);
    	}
    }

     /**
	 * Dpl1000 배포 이력 조회
	 * @param 
	 * @return 
	 * @exception Exception
	 */
	@SuppressWarnings({ "unchecked", "rawtypes" })
	@RequestMapping(method=RequestMethod.POST, value="/dpl/dpl1000/dpl1000/selectDpl1000DplHistoryListAjax.do")
    public ModelAndView selectDpl1000DplHistoryListAjax(HttpServletRequest request, HttpServletResponse response, ModelMap model ) throws Exception {
    	try{

    		//리퀘스트에서 넘어온 파라미터를 맵으로 세팅
        	Map paramMap = RequestConvertor.requestParamToMap(request, true);

        	//배포 계획 정보 리스트 조회
        	List<Map> dplDplHistoryList = dpl1000Service.selectDpl1000DplHistoryList(paramMap);
        	
        	//수정이력 조회
        	List<Map> dplModifyHistoryList = dpl1000Service.selectDpl1500ModifyHistoryList(paramMap);
        	
        	//해당 배포 계획 JOB List 조회
			List<Map> jobList = dpl1000Service.selectDpl1300DeployJobList(paramMap);
			
        	model.addAttribute("dplDplHistoryList", dplDplHistoryList);
        	model.addAttribute("dplModifyHistoryList", dplModifyHistoryList);
        	model.addAttribute("jobList", jobList);
        	
        	//조회성공메시지 세팅
        	model.addAttribute("message", egovMessageSource.getMessage("success.common.select"));
        	
        	return new ModelAndView("jsonView", model);
    	}
    	catch(Exception ex){
    		Log.error("selectDpl1000DplHistoryListAjax()", ex);
    		
    		//조회실패 메시지 세팅
    		model.addAttribute("message", egovMessageSource.getMessage("fail.common.select"));
    		return new ModelAndView("jsonView", model);
    	}
    }
	
	/**
	 * Dpl1400 배포 실행 이력 SEQ로 콘솔로그 조회
	 * @param 
	 * @return 
	 * @exception Exception
	 */
	@SuppressWarnings({ "unchecked", "rawtypes" })
	@RequestMapping(method=RequestMethod.POST, value="/dpl/dpl1000/dpl1000/selectDpl1400DplSelBuildConsoleLogAjax.do")
	public ModelAndView selectDpl1400DplSelBuildConsoleLogAjax(HttpServletRequest request, HttpServletResponse response, ModelMap model ) throws Exception {
		try{
			
    		//리퀘스트에서 넘어온 파라미터를 맵으로 세팅
    		Map<String, String> paramMap = RequestConvertor.requestParamToMapAddSelInfo(request, true);
    		
    		//session
    		HttpSession ss = request.getSession();
    		LoginVO loginVO = (LoginVO)ss.getAttribute("loginVO");
			paramMap.put("licGrpId",loginVO.getLicGrpId());
			
			// 프로젝트 ID
    		String prjId = (String) paramMap.get("prjId");
	
    		// 프로젝트의 권한 목록
    		List<Map> prjUsrAuthList = new ArrayList<Map>();

    		//사용자 Id
			String usrId = (String)loginVO.getUsrId();
    		
    		if(prjId == null || "".equals(prjId)) {
    			paramMap.put("prjId", (String)ss.getAttribute("selPrjId"));
    			// 현재 프로젝트의 권한
        		String selAuthGrpId = (String) ss.getAttribute("selAuthGrpId");
    			Map<String,String> authMap = new HashMap<String,String>();
				authMap.put("authGrpId", selAuthGrpId);
				prjUsrAuthList.add(authMap);
    		}else {
    			// 프로젝트의 사용자 권한을 조회하기 위한 Map
        		Map<String,String> authParamMap = new HashMap<String,String>();
    			authParamMap.put("prjId", prjId);
				authParamMap.put("usrId", usrId);
				List<Map> prjAuthList = cmm4000Service.selectCmm4000UsrPrjAuthList(authParamMap);
				prjUsrAuthList.addAll(prjAuthList);
    		}
    		
    		//배포자 허용 권한그룹 조회
 			List<Map> dplRunAuthGrp = stm3100Service.selectJen1300JenkinsJobAuthGrpNormalList(paramMap);
 			
 			//허용 권한그룹이 존재하는 경우
 			if(dplRunAuthGrp != null && dplRunAuthGrp.size() > 0){
 				//배포 정보
 				Map dpl1000DplInfo = dpl1000Service.selectDpl1000DeployVerInfo(paramMap);
 				
 				//배포자 Id
 				String dplUsrId = (String) dpl1000DplInfo.get("dplUsrId");
 				
 				//배포자인경우 권한 체크 X
 				if(!usrId.equals(dplUsrId)){
	 				//선택 권한
	 				//String selAuthGrpId = (String)ss.getAttribute("selAuthGrpId");
	 				
	 				//체크 변수
	 				boolean authGrpChk = false;
	 				
	 				for(Map authGrp : dplRunAuthGrp){
	 					String authGrpId = (String) authGrp.get("authGrpId");
	 					
						boolean prjauthChk = false;
						// 프로젝트의 허용역할 loop
						for(Map prjUsrAuthInfo : prjUsrAuthList) {
							
							// 배포 권한 목록에 프로젝트의 권한 비교
							if(authGrpId.equals(prjUsrAuthInfo.get("authGrpId"))){
								prjauthChk = true;
								break;
							}
						}
						// 배포 권한 목록에 프로젝트의 권한이 있을 경우
						if(prjauthChk) {
							authGrpChk = true;
							break;
						}
	 				}
	 				
	 				//권한 없는 경우 error
	 				if(!authGrpChk){
	 					//조회성공메시지 세팅
	 					model.addAttribute("errorYn", "Y");
	 					model.addAttribute("message", "허용 권한이 없습니다.");
	 					return new ModelAndView("jsonView", model);
	 				}
 				}
 			}
 			
    		//배포계획 정보
    		Map dpl1400InfoMap = dpl1000Service.selectDpl1400DplSelBuildInfoAjax(paramMap);
 			
    		model.addAttribute("dpl1400InfoMap", dpl1400InfoMap);
			//조회성공메시지 세팅
    		model.addAttribute("errorYn", "N");
			model.addAttribute("message", egovMessageSource.getMessage("success.common.select"));
			return new ModelAndView("jsonView", model);
		}
		catch(Exception ex){
			Log.error("selectDpl1400DplSelBuildConsoleLogAjax()", ex);
			
			//조회실패 메시지 세팅
			model.addAttribute("errorYn", "Y");
			model.addAttribute("message", "콘솔 내용 조회 오류");
			return new ModelAndView("jsonView", model);
		}
	}
	
}
