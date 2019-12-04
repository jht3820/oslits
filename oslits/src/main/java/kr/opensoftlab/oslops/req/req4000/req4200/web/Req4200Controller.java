package kr.opensoftlab.oslops.req.req4000.req4200.web;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.apache.log4j.Logger;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;

import egovframework.com.cmm.EgovMessageSource;
import egovframework.rte.fdl.cmmn.trace.LeaveaTrace;
import egovframework.rte.fdl.property.EgovPropertyService;
import egovframework.rte.ptl.mvc.tags.ui.pagination.PaginationInfo;
import kr.opensoftlab.oslops.com.vo.LoginVO;
import kr.opensoftlab.oslops.req.req4000.req4200.service.Req4200Service;
import kr.opensoftlab.oslops.req.req4000.req4200.vo.Req4200VO;
import kr.opensoftlab.sdf.util.OslAgileConstant;
import kr.opensoftlab.sdf.util.PagingUtil;
import kr.opensoftlab.sdf.util.RequestConvertor;

/**
 * @Class Name : Req4200Controller.java
 * @Description : Req4200Controller Controller class
 * @Modification Information
 *
 * @author 정형택
 * @since 2016.01.30.
 * @version 1.0
 * @see
 *  
 *  Copyright (C) OpenSoftLab Corp All right reserved.
 */

@Controller
public class Req4200Controller {

	/** 로그4j 로거 로딩 */
	private final Logger Log = Logger.getLogger(this.getClass());
	
	/** Req4200Service DI */
    @Resource(name = "req4200Service")
    private Req4200Service req4200Service;
    
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
	 * Req4200 화면 이동(이동시 요구사항 분류 정보 목록 조회)
	 * @param 
	 * @return 
	 * @exception Exception
	 */
	@SuppressWarnings({ "unchecked", "rawtypes" })
	@RequestMapping(value="/req/req4000/req4200/selectReq4200View.do")
    public String selectReq4200View(HttpServletRequest request, HttpServletResponse response, ModelMap model ) throws Exception {
    	
    	try{
    		//리퀘스트에서 넘어온 파라미터를 맵으로 세팅
        	Map paramMap = RequestConvertor.requestParamToMapAddSelInfo(request, true);
        	
        	//요구사항 분류 목록 가져오기
        	List<Map> reqClsList = (List) req4200Service.selectReq4200ReqClsList(paramMap);
        	
        	model.addAttribute("reqClsList", reqClsList);
        	
        	return "/req/req4000/req4200/req4200";
    	}
    	catch(Exception ex){
    		Log.error("selectReq4200View()", ex);
    		throw new Exception(ex.getMessage());
    	}
    }
    
	/**
	 * Req4200 Ajax 요구사항 분류정보 목록 조회
	 * @param 
	 * @return 
	 * @exception Exception
	 */
    @SuppressWarnings({ "rawtypes", "unchecked" })
	@RequestMapping(value="/req/req4000/req4200/selectReq4200ReqClsListAjax.do")
    public ModelAndView selectReq4200ReqClsListAjax(HttpServletRequest request, HttpServletResponse response, ModelMap model ) throws Exception {
    	
    	try{
        	
    		//리퀘스트에서 넘어온 파라미터를 맵으로 세팅
        	Map paramMap = RequestConvertor.requestParamToMapAddSelInfo(request, true);
        	
        	//요구사항 분류목록 가져오기
        	List<Map> reqClsList = (List) req4200Service.selectReq4200ReqClsList(paramMap);
        	
        	//조회성공메시지 세팅
        	model.addAttribute("reqClsList", reqClsList);
        	model.addAttribute("message", egovMessageSource.getMessage("success.common.select"));
        	
        	return new ModelAndView("jsonView");
    	}
    	catch(Exception ex){
    		Log.error("selectReq4200ReqClsListAjax()", ex);
    		
    		//조회실패 메시지 세팅
    		model.addAttribute("message", egovMessageSource.getMessage("fail.common.select"));
    		return new ModelAndView("jsonView");
    	}
    }
	
    /**
	 * Req4200 Ajax 요구사항 분류 배정 목록 및 미배정 목록 조회
	 * @param 
	 * @return 
	 * @exception Exception
	 */
    @SuppressWarnings({ "rawtypes", "unchecked" })
   	@RequestMapping(value="/req/req4000/req4200/selectReq4200ReqClsAddDelListAjax.do")
       public ModelAndView selectReq4200ReqClsAddDelListAjax(@ModelAttribute("req4200VO") Req4200VO req4200VO, HttpServletRequest request, HttpServletResponse response, ModelMap model ) throws Exception {
       	
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
			req4200VO.setPageIndex(_pageNo);
			req4200VO.setPageSize(_pageSize);
			req4200VO.setPageUnit(_pageSize);
			
			PaginationInfo paginationInfo = PagingUtil.getPaginationInfo(req4200VO);  /** paging - 신규방식 */

			HttpSession ss = request.getSession();
			LoginVO loginVO = (LoginVO) ss.getAttribute("loginVO");

			// 라이센스 그룹 ID 및 프로젝트 ID 세팅
			req4200VO.setLicGrpId(loginVO.getLicGrpId());
			req4200VO.setPrjId((String) ss.getAttribute("selPrjId"));
			
			// 분류 배정/미배정 요구사항 목록
			List<Req4200VO> reqClsList = null;
			
           	// 총 건수
			int totCnt = 0;
			
			// 분류에 요구사항 배정된 요구사항 조회
           	if("clsAdd".equals(paramMap.get("clsMode"))){
           		// 분류에 배정된 요구사항 총 건수 조회
    			totCnt = req4200Service.selectReq4200ReqClsAddListCnt(req4200VO);
    			paginationInfo.setTotalRecordCount(totCnt);
   	        	//요구사항 분류 배정 목록 가져오기
   	        	reqClsList = (List) req4200Service.selectReq4200ReqClsAddListAjax(req4200VO);
   	        	
   	        // 분류에 미배정된 요구사항 조회	
           	}else if("clsDel".equals(paramMap.get("clsMode"))){
           		// 분류에 미배정된 요구사항 총 건수 조회
    			totCnt = req4200Service.selectReq4200ReqClsDelListCnt(req4200VO);
    			paginationInfo.setTotalRecordCount(totCnt);
           		//요구사항 분류 미배정 목록 가져오기
           		reqClsList = (List) req4200Service.selectReq4200ReqClsDelListAjax(req4200VO);	
           	}
           	
           	//페이지 정보 보내기
			Map<String, Integer> pageMap = new HashMap<String, Integer>();
			pageMap.put("pageNo",req4200VO.getPageIndex());
			pageMap.put("listCount", reqClsList.size());
			pageMap.put("totalPages", paginationInfo.getTotalPageCount());
			pageMap.put("totalElements", totCnt);
			pageMap.put("pageSize", _pageSize);

			model.addAttribute("page", pageMap);

           	//요구사항 분류 배정, 미배정 목록 세팅
           	model.addAttribute("list", reqClsList);
           	
           	//조회성공메시지 세팅
           	model.addAttribute("errorYn", "N");
           	model.addAttribute("message", egovMessageSource.getMessage("success.common.select"));
           	
           	return new ModelAndView("jsonView");
       	}
       	catch(Exception ex){
       		Log.error("selectReq4200ReqClsAddDelListAjax()", ex);
       		
       		//조회실패 메시지 세팅
       		model.addAttribute("errorYn", "Y");
       		model.addAttribute("message", egovMessageSource.getMessage("fail.common.select"));
       		return new ModelAndView("jsonView");
       	}
	}
    
    /**
	 * Req4200 분류에 요구사항을 배정/배정제외 한다.
	 * @param 
	 * @return 
	 * @exception Exception
	 */
    @SuppressWarnings({ "rawtypes" , "unchecked"})
	@RequestMapping(value="/req/req4000/req4200/updateReq4200ReqClsAddDelListAjax.do")
    public ModelAndView updateReq4200ReqClsAddDelListAjax(HttpServletRequest request, HttpServletResponse response, ModelMap model ) throws Exception {
    	
    	try{
    		//리퀘스트에서 넘어온 파라미터를 맵으로 세팅
        	Map paramMap = RequestConvertor.requestParamToMapAddSelInfo(request, true);
        	
    		System.out.println(paramMap);
    		// 세션에서 현재 선택된 프로젝트 id를 가져온다.
    		HttpSession ss = request.getSession();
    		String prjId = (String)ss.getAttribute("selPrjId");
    		
    		// Map에 프로젝트 Id 세팅
    		paramMap.put("prjId", prjId);
    		
        	//요구사항 분류에 요구사항 배정 및 삭제 처리
        	req4200Service.updateReq4200ReqClsAddDelListAjax(paramMap);
        	
        	//조회성공메시지 세팅
        	model.addAttribute("message", egovMessageSource.getMessage("success.common.select"));
        	
        	return new ModelAndView("jsonView");
    	}
    	catch(Exception ex){
    		Log.error("updateReq4200ReqClsAddDelListAjax()", ex);
    		
    		//조회실패 메시지 세팅
    		model.addAttribute("saveYn", "N");
    		model.addAttribute("message", egovMessageSource.getMessage("fail.common.select"));
    		return new ModelAndView("jsonView");
    	}
    }
    
}
