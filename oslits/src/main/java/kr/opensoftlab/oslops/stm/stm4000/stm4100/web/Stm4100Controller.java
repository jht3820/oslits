package kr.opensoftlab.oslops.stm.stm4000.stm4100.web;

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
import kr.opensoftlab.oslops.prj.prj1000.prj1000.service.Prj1000Service;
import kr.opensoftlab.oslops.stm.stm4000.stm4100.service.Stm4100Service;
import kr.opensoftlab.oslops.stm.stm4000.stm4100.vo.Stm4100VO;
import kr.opensoftlab.sdf.util.OslAgileConstant;
import kr.opensoftlab.sdf.util.PagingUtil;
import kr.opensoftlab.sdf.util.RequestConvertor;


/**
 * @Class Name : Stm4100Controller.java
 * @Description : Stm4100Controller Controller class
 * @Modification Information
 *
 * @author 배용진
 * @since 2019.05.10.
 * @version 1.0
 * @see
 *  
 *  Copyright (C) OpenSoftLab Corp All right reserved.
 */
@Controller
public class Stm4100Controller {
	/** 로그4j 로거 로딩 */
	private static final Logger Log = Logger.getLogger(Stm4100Controller.class);

	
	/** Stm4100Service DI */
    @Resource(name = "stm4100Service")
    private Stm4100Service stm4100Service;	
	
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
	 * Stm4100 프로젝트 업무역할 사용자 배정 화면이동
	 * @param 
	 * @return 
	 * @exception Exception
	 */
	@RequestMapping(value="/stm/stm4000/stm4100/selectStm4100View.do")
    public String selectStm4100View(HttpServletRequest request, HttpServletResponse response, ModelMap model ) throws Exception {
        	return "/stm/stm4000/stm4100/stm4100";
    }
    
	
	/**
	 * Stm4100 라이선스 그룹의 모든 프로젝트와 각 프로젝트에 있는 업무역할을 조회
	 * @param 
	 * @return 
	 * @exception Exception
	 */
	@SuppressWarnings({ "rawtypes", "unchecked" })
	@RequestMapping(value="/stm/stm4000/stm4100/selectStm4100PrjAuthListAjax.do")
	public ModelAndView selectStm4100PrjAuthListAjax(HttpServletRequest request, HttpServletResponse response, ModelMap model ) throws Exception {
		try{
			//리퀘스트에서 넘어온 파라미터를 맵으로 세팅
			Map<String, String> paramMap = RequestConvertor.requestParamToMap(request, true);
	    		
			// 라이선스 그룹의 전체 프로젝트 목록을 가져온다.
			List<Map> selectLicGrpAllPrjList = stm4100Service.selectStm4100PrjAuthList(paramMap);

			model.put("adminPrjList", selectLicGrpAllPrjList);
	        	
			// 조회 성공여부 및 조회 성공 메시지 세팅
			model.addAttribute("errorYn", "N");
			model.addAttribute("message", egovMessageSource.getMessage("success.common.select"));
	 
			return new ModelAndView("jsonView", model);
		}
		catch(Exception ex){
			Log.error("selectStm4100PrjAuthListAjax()", ex);
			// 조회 실패여부 및 조회 실패 메시지 세팅
			model.addAttribute("errorYn", "Y");
			model.addAttribute("message", egovMessageSource.getMessage("fail.common.select"));
			throw new Exception(ex.getMessage());
		}
	}
	
	
	/**
	 * Stm4100 프로젝트 권한에 배정/미배정 사용자 목록을 조회한다.
	 * @param 
	 * @return 
	 * @exception Exception
	 */
	@SuppressWarnings({ "rawtypes", "unchecked" })
	@RequestMapping(value="/stm/stm4000/stm4100/selectStm4100PrjUsrListAjax.do")
	public ModelAndView selectStm4100PrjUsrListAjax(@ModelAttribute("stm4100VO") Stm4100VO stm4100VO, HttpServletRequest request, HttpServletResponse response, ModelMap model ) throws Exception {
		
		try{
			// request 파라미터를 map으로 변환
			Map<String, String> paramMap = RequestConvertor.requestParamToMapAddSelInfo(request, true);
			
			//로그인VO 가져오기
    		HttpSession ss = request.getSession();
    		LoginVO loginVO = (LoginVO) ss.getAttribute("loginVO");
    		
			//현재 페이지 값, 보여지는 개체 수
			String _pageNo_str = paramMap.get("pageNo");
			String _pageSize_str = paramMap.get("pageSize");

			int _pageNo = 1;
			int _pageSize = 15; //OslAgileConstant.pageSize;

			// 넘어온 페이지 정보가 있다면 해당 값으로 세팅
			if(_pageNo_str != null && !"".equals(_pageNo_str)){
				_pageNo = Integer.parseInt(_pageNo_str)+1;  
			}
			if(_pageSize_str != null && !"".equals(_pageSize_str)){
				_pageSize = Integer.parseInt(_pageSize_str);  
			}

			// 라이선스 그룹 ID 세팅
			stm4100VO.setLicGrpId(loginVO.getLicGrpId());
			
			//페이지 사이즈
			stm4100VO.setPageIndex(_pageNo);
			stm4100VO.setPageSize(_pageSize);
			stm4100VO.setPageUnit(_pageSize);
			
			PaginationInfo paginationInfo = PagingUtil.getPaginationInfo(stm4100VO);  /** paging - 신규방식 */
    		
			// 프로젝트 권한에 배정된 사용자 목록
			List<Map> usrList = null;
			
			// 총 데이터 건수
			int totCnt = 0;

			// 프로젝트 권한 배정 사용자 조회
			if ("usrAdd".equals(paramMap.get("selectMode"))) {
							
				// 프로젝트 권한에 배정된 사용자 목록 총 건수 조회
				totCnt = stm4100Service.selectStm4100UsrAddListCnt(stm4100VO);
				// 프로젝트 권한에 배정된 사용자 목록 조회
				usrList = stm4100Service.selectStm4100UsrAddList(stm4100VO);

			// 프로젝트 권한 미배정 사용자 조회
			} else if ("usrAll".equals(paramMap.get("selectMode"))) {
				// 프로젝트 권한에 배정되지 않은 사용자 목록 총 건수 조회
				totCnt = stm4100Service.selectStm4100UsrAllListCnt(stm4100VO);
				// 프로젝트 권한에 배정되지 않은 사용자 목록 조회
				usrList = stm4100Service.selectStm4100UsrAllList(stm4100VO);
			}
					
			paginationInfo.setTotalRecordCount(totCnt);
			model.addAttribute("list", usrList);
			
			//페이지 정보 보내기
			Map<String, Integer> pageMap = new HashMap<String, Integer>();
			pageMap.put("pageNo",stm4100VO.getPageIndex());
			pageMap.put("listCount", usrList.size());
			pageMap.put("totalPages", paginationInfo.getTotalPageCount());
			pageMap.put("totalElements", totCnt);
			pageMap.put("pageSize", _pageSize);

			// 페이지 정보 세팅
			model.addAttribute("page", pageMap);
			
			// 조회 성공여부 및 조회 성공 메시지 세팅
			model.addAttribute("errorYN", "N");
			model.addAttribute("message", egovMessageSource.getMessage("success.common.select"));
			
			return new ModelAndView("jsonView");
		}
		catch(Exception ex){
			Log.error("selectStm4100PrjUsrListAjax()", ex);
			
			// 조회실패여부 및 조회 실패 메시지 세팅
			model.addAttribute("errorYN", "Y");
			model.addAttribute("message", egovMessageSource.getMessage("fail.common.select"));
			return new ModelAndView("jsonView");
		}
	}
	
    /**
	 * Stm4100 프로젝트 권한에 사용자를 배정 또는 권한에서 사용자를 삭제한다.
	 * @param 
	 * @return 
	 * @exception Exception
	 */
    @SuppressWarnings({ "rawtypes", "unchecked" })
	@RequestMapping(value="/stm/stm4000/stm4100/saveStm4100PrjUsrAuthListAjax.do")
    public ModelAndView saveStm4100PrjUsrAuthListAjax(HttpServletRequest request, HttpServletResponse response, ModelMap model ) throws Exception {
    	
    	try{
    		//리퀘스트에서 넘어온 파라미터를 맵으로 세팅
    		Map<String, Object> paramMap = RequestConvertor.requestParamToMapAddSelInfoList(request, true,"authGrpId");
    		
        	HttpSession ss = request.getSession();
    		LoginVO loginVO = (LoginVO) ss.getAttribute("loginVO");
       	
    		// 라이선스 그룹 ID 세팅
    		paramMap.put("licGrpId", loginVO.getLicGrpId());
    		
    		// 등록/삭제를 구분할 status 값을 가져온다.
    		String status = request.getParameter("status");
    		// 프로젝트 ID를 가져온다.
    		String prjId = request.getParameter("prjId");

    		// 권한에서 사용자 삭제일 경우
    		if("delete".equals(status)){
    			
    			Map<String, String> newMap = new HashMap<String, String>();
    			// 프로젝트 ID 세팅
    			newMap.put("prjId", prjId);
    			// 프로젝트의 권한 그룹에 등록된 사용자 조회
    			int authUsrCnt = stm4100Service.selectStm4100UsrCntCheck(newMap);
    			// 등록된 사용자 1명일 경우 삭제 불가 처리
    			if(authUsrCnt == 1){
    				// 삭제 실패여부 및 삭제 실패 메시지 세팅
    				model.addAttribute("errorYn", "Y");
    				model.addAttribute("message", egovMessageSource.getMessage("fail.common.prjAuthGrpUsrChk"));
    				return new ModelAndView("jsonView");
    	        }else {
    	        	// 사용자 배정 삭제 처리
    	        	stm4100Service.deleteStm4100PrjUsrAuthList(paramMap);
    	        }
    		// 권한에 사용자 추가일 경우	
    		}else if("insert".equals(status)){
    			// 사용자 배정
    			stm4100Service.insertStm4100PrjUsrAuthList(paramMap);
    		}
    		
        	// 사용자 권한 추가 후 권한있는 프로젝트 재 조회하여 세션에 세팅한다.
			// 사용자 ID를 가져온다.
			String usrId = (String)loginVO.getUsrId();
			paramMap.put("usrId", usrId);
	
        	// 사용자 권한있는 프로젝트 목록 조회
			List<Map> prjList = (List)prj1000Service.selectPrj1000View(paramMap);

    		// 세션에 있는 기존 프로젝트 목록 제거
    		ss.removeAttribute("prjList");
    		
    		// 조회한 권한있는 프로젝트 목록을 세션에 재세팅
    		ss.setAttribute("prjList", prjList);
    		
        	// 저장 성공여부 및 저장 성공 메시지를 세팅한다.
        	model.addAttribute("errorYn", "N");
        	model.addAttribute("message", egovMessageSource.getMessage("success.common.save"));
        	
        	return new ModelAndView("jsonView");
    	}
    	catch(Exception ex){
    		Log.error("saveStm4100PrjUsrAuthListAjax()", ex);
    		
    		// 저장 실패여부 및 저장 성공 메시지를 세팅한다.
    		model.addAttribute("errorYn", "Y");
    		model.addAttribute("message", egovMessageSource.getMessage("fail.common.save"));
    		return new ModelAndView("jsonView");
    	}
    }
	
	
	

}
