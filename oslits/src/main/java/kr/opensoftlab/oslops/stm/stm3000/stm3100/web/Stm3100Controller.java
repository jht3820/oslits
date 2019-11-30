package kr.opensoftlab.oslops.stm.stm3000.stm3100.web;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import kr.opensoftlab.oslops.com.fms.web.service.FileMngService;
import kr.opensoftlab.oslops.com.vo.LoginVO;
import kr.opensoftlab.oslops.stm.stm3000.stm3100.service.Stm3100Service;
import kr.opensoftlab.oslops.stm.stm3000.stm3100.vo.Stm3100VO;
import kr.opensoftlab.sdf.util.OslAgileConstant;
import kr.opensoftlab.sdf.util.PagingUtil;
import kr.opensoftlab.sdf.util.ReqHistoryMngUtil;
import kr.opensoftlab.sdf.util.RequestConvertor;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;

import egovframework.com.cmm.EgovMessageSource;
import egovframework.com.cmm.service.EgovFileMngUtil;
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
 *  --------------------------------------
 *  수정일			수정자			수정내용
 *  --------------------------------------
 *  2019-03-08		진주영		 	기능 개선
 *  2019-03-10		배용진		 	기능 개선
 *  
 *  --------------------------------------
 *  
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


	@Value("${Globals.fileStorePath}")
	private String tempPath;
	
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
	 * Stm3100 프로젝트에 배정된 Jenkins Job 목록을 조회한다.
	 * @param request
	 * @param response
	 * @param model
	 * @return
	 * @throws Exception
	 */
	@RequestMapping(value="/stm/stm3000/stm3100/selectStm3100JenkinsProjectListAjax.do")
    public ModelAndView selectStm3100JenkinsProjectListAjax(@ModelAttribute("stm3100VO") Stm3100VO stm3100VO, HttpServletRequest request, HttpServletResponse response, ModelMap model ) throws Exception {
    	try{
    		// request 파라미터를 map으로 변환
        	Map<String, String> paramMap = RequestConvertor.requestParamToMap(request, true);

    		//로그인VO 가져오기
    		HttpSession ss = request.getSession();
    		LoginVO loginVO = (LoginVO) ss.getAttribute("loginVO");
    		
    		//현재 페이지 값, 보여지는 개체 수
			String _pageNo_str = paramMap.get("pageNo");
			String _pageSize_str = paramMap.get("pageSize");

			int _pageNo = 1;
			int _pageSize = OslAgileConstant.pageSize;

			// 넘어온 페이지 정보가 있다면 해당 값으로 세팅
			if(_pageNo_str != null && !"".equals(_pageNo_str)){
				_pageNo = Integer.parseInt(_pageNo_str)+1;  
			}
			if(_pageSize_str != null && !"".equals(_pageSize_str)){
				_pageSize = Integer.parseInt(_pageSize_str);  
			}

			// 라이선스 그룹 ID 세팅
			stm3100VO.setLicGrpId(loginVO.getLicGrpId());
			
			//페이지 사이즈
			stm3100VO.setPageIndex(_pageNo);
			stm3100VO.setPageSize(_pageSize);
			stm3100VO.setPageUnit(_pageSize);
			
			PaginationInfo paginationInfo = PagingUtil.getPaginationInfo(stm3100VO);  /** paging - 신규방식 */
    		
			// 프로젝트에 배정된 Jenkins Job 목록
			List<Stm3100VO> jenkinsProjectList = null;
			
			// 총 데이터 건수
			int totCnt = 0;
			
			// 프로젝트에 배정된 Jenkins Job 목록 총 건수를 가져온다. 
			totCnt =  stm3100Service.selectStm3100JenkinsProjectListCnt(stm3100VO);
			paginationInfo.setTotalRecordCount(totCnt);
    		
    		// 프로젝트에 배정된 Jenkins Job 목록 조회
			jenkinsProjectList = stm3100Service.selectStm3100JenkinsProjectList(stm3100VO);

			// 프로젝트에 배정된 Jenkins Job 목록 세팅
    		model.addAttribute("list", jenkinsProjectList);
    		
    		//페이지 정보 보내기
			Map<String, Integer> pageMap = new HashMap<String, Integer>();
			pageMap.put("pageNo",stm3100VO.getPageIndex());
			pageMap.put("listCount", jenkinsProjectList.size());
			pageMap.put("totalPages", paginationInfo.getTotalPageCount());
			pageMap.put("totalElements", totCnt);
			pageMap.put("pageSize", _pageSize);

			// 페이지 정보 세팅
			model.addAttribute("page", pageMap);
    		
    		// 조회 성공 및 성공메시지 세팅
    		model.addAttribute("errorYn", 'N');
        	model.addAttribute("message", egovMessageSource.getMessage("success.common.select"));
        	
        	return new ModelAndView("jsonView", model);
    	}
    	catch(Exception ex){
    		Log.error("selectStm3100JenkinsProjectListAjax()", ex);
    		
    		// 조회 실패 및 실패메시지 세팅
    		model.addAttribute("errorYn", 'Y');
    		model.addAttribute("message", egovMessageSource.getMessage("fail.common.select"));
    		return new ModelAndView("jsonView");
    	}
    }

	/**
	 * Stm3100 프로젝트에 Jenkins Job 설정 팝업으로 이동한다.
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
	 * Stm3100 Jenkins Job 프로젝트 배정 가능 목록을 조회한다. 
	 * @param request
	 * @param response
	 * @param model
	 * @return
	 * @throws Exception
	 */
	@SuppressWarnings({ "unchecked", "rawtypes" })
	@RequestMapping(value="/stm/stm3000/stm3100/selectStm3100JenkinsProjectAuthListAjax.do")
    public ModelAndView selectStm3100JenkinsProjectAuthListAjax(@ModelAttribute("stm3100VO") Stm3100VO stm3100VO, HttpServletRequest request, HttpServletResponse response, ModelMap model ) throws Exception {
    	try{
    		// request 파라미터를 map으로 변환
        	Map<String, String> paramMap = RequestConvertor.requestParamToMap(request, true);
        	
    		//로그인VO 가져오기
    		HttpSession ss = request.getSession();
    		LoginVO loginVO = (LoginVO) ss.getAttribute("loginVO");
    		
    		//현재 페이지 값, 보여지는 개체 수
			String _pageNo_str = paramMap.get("pageNo");
			String _pageSize_str = paramMap.get("pageSize");

			int _pageNo = 1;
			int _pageSize = OslAgileConstant.pageSize;

			// 넘어온 페이지 정보가 있다면 해당 값으로 세팅
			if(_pageNo_str != null && !"".equals(_pageNo_str)){
				_pageNo = Integer.parseInt(_pageNo_str)+1;  
			}
			if(_pageSize_str != null && !"".equals(_pageSize_str)){
				_pageSize = Integer.parseInt(_pageSize_str);  
			}

			// 라이선스 그룹 ID 세팅
			stm3100VO.setLicGrpId(loginVO.getLicGrpId());
			
			//페이지 사이즈
			stm3100VO.setPageIndex(_pageNo);
			stm3100VO.setPageSize(_pageSize);
			stm3100VO.setPageUnit(_pageSize);
			
			PaginationInfo paginationInfo = PagingUtil.getPaginationInfo(stm3100VO);  /** paging - 신규방식 */
    		
			// 프로젝트에 배정 설정 Jenkins Job 목록
			List<Map> jenkinsJobList = null;
			
			// 총 데이터 건수
			int totCnt = 0;
			// 프로젝트에 배정 설정 Jenkins Job 목록 총 건수 조회
			totCnt = stm3100Service.selectStm3100JenkinsProjectAuthListCnt(stm3100VO);
			paginationInfo.setTotalRecordCount(totCnt);
			
			// 프로젝트에 배정 설정 Jenkins Job 목록을 조회한다.
			jenkinsJobList = stm3100Service.selectStm3100JenkinsProjectAuthList(stm3100VO);

			// 프로젝트에 배정 설정 Jenkins Job 목록 세팅
    		model.addAttribute("list", jenkinsJobList);
			
			//페이지 정보 보내기
			Map<String, Integer> pageMap = new HashMap<String, Integer>();
			pageMap.put("pageNo",stm3100VO.getPageIndex());
			pageMap.put("listCount", jenkinsJobList.size());
			pageMap.put("totalPages", paginationInfo.getTotalPageCount());
			pageMap.put("totalElements", totCnt);
			pageMap.put("pageSize", _pageSize);

			// 페이지 정보 세팅
			model.addAttribute("page", pageMap);
			// 조회성공여부 및 성공 메시지 세팅
    		model.addAttribute("errorYn", 'N');
        	model.addAttribute("message", egovMessageSource.getMessage("success.common.select"));
        	
        	return new ModelAndView("jsonView", model);
    	}
    	catch(Exception ex){
    		Log.error("selectStm3100JenkinsProjectAuthListAjax()", ex);
    		// 조회 실패여부 및 실패메시지 세팅
    		model.addAttribute("errorYn", 'Y');
    		model.addAttribute("message", egovMessageSource.getMessage("fail.common.select"));
    		return new ModelAndView("jsonView");
    	}
    }
	
	
	/**
	 * Stm3100 프로젝트에 Jenkins Job을 배정한다.
	 * @param request
	 * @param response
	 * @param model
	 * @return
	 * @throws Exception
	 */
	@RequestMapping(value="/stm/stm3000/stm3100/insertStm3100ProjectAddJobAjax.do")
    public ModelAndView insertStm3100ProjectAddJobAjax(HttpServletRequest request, HttpServletResponse response, ModelMap model ) throws Exception {
    	try{
    		// request 파라미터를 map으로 변환
    		Map<String, Object> paramMap = RequestConvertor.requestParamToMapAddSelInfoList(request, true,"jobId");
    		
        	HttpSession ss = request.getSession();
    		LoginVO loginVO = (LoginVO) ss.getAttribute("loginVO");
       	
    		// 라이선스 그룹 ID 세팅
    		paramMap.put("licGrpId", loginVO.getLicGrpId());
        	
    		// 프로젝트에 jenkins job을 배정한다.
    		stm3100Service.insertStm3100ProjectAddJob(paramMap);
      
        	// 저장 성공여부 및 저장성공 메시지 세팅
        	model.addAttribute("errorYn", 'N');
        	model.addAttribute("message", egovMessageSource.getMessage("success.common.insert"));
        	
        	return new ModelAndView("jsonView", model);
    	}
    	catch(Exception ex){
    		Log.error("insertStm3100ProjectAddJobAjax()", ex);
    		
    		// 저장 실패여부 및 저장 실패메시지 세팅
    		model.addAttribute("errorYn", 'Y');
    		model.addAttribute("message", egovMessageSource.getMessage("fail.common.insert"));
    		return new ModelAndView("jsonView");
    	}
    }
	
	
	/**
	 * Stm3100 프로젝트에 Jenkins Job을 배정제외 한다.
	 * @param request
	 * @param response
	 * @param model
	 * @return
	 * @throws Exception
	 */
	@RequestMapping(value="/stm/stm3000/stm3100/deleteStm3100ProjectDelJobAjax.do")
    public ModelAndView deleteStm3100ProjectDelJob(HttpServletRequest request, HttpServletResponse response, ModelMap model ) throws Exception {
    	try{
    		// request 파라미터를 map으로 변환
    		Map<String, Object> paramMap = RequestConvertor.requestParamToMapAddSelInfoList(request, true,"jobId");
    		
        	HttpSession ss = request.getSession();
    		LoginVO loginVO = (LoginVO) ss.getAttribute("loginVO");
       	
    		// 라이선스 그룹 ID 세팅
    		paramMap.put("licGrpId", loginVO.getLicGrpId());
        	
    		// 프로젝트에 jenkins job을 배정제외 한다.
    		stm3100Service.deleteStm3100ProjectDelJob(paramMap);
      
        	// 삭제 성공여부 및 삭제성공 메시지 세팅
        	model.addAttribute("errorYn", 'N');
        	model.addAttribute("message", egovMessageSource.getMessage("success.common.delete"));
        	
        	return new ModelAndView("jsonView", model);
    	}
    	catch(Exception ex){
    		Log.error("deleteStm3100ProjectDelJob()", ex);
    		
    		// 삭제 실패여부 및 삭제 실패메시지 세팅
    		model.addAttribute("errorYn", 'Y');
    		model.addAttribute("message", egovMessageSource.getMessage("fail.common.delete"));
    		return new ModelAndView("jsonView");
    	}
    }
	
	/**
	 * Stm3100 Jenkins 허용 역할 그룹 목록을 조회한다.
	 * @param 
	 * @return 
	 * @exception Exception
	 */
	@SuppressWarnings({ "rawtypes", "unchecked" })
	@RequestMapping(value="/stm/stm3000/stm3100/selectStm3100JenkinsJobAuthGrpListAjax.do")
	public ModelAndView selectStm3100JenkinsJobAuthGrpListAjax(@ModelAttribute("stm3100VO") Stm3100VO stm3100VO, HttpServletRequest request, HttpServletResponse response, ModelMap model ) throws Exception {
		
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
			int _pageSize = OslAgileConstant.pageSize;

			// 넘어온 페이지 정보가 있다면 해당 값으로 세팅
			if(_pageNo_str != null && !"".equals(_pageNo_str)){
				_pageNo = Integer.parseInt(_pageNo_str)+1;  
			}
			if(_pageSize_str != null && !"".equals(_pageSize_str)){
				_pageSize = Integer.parseInt(_pageSize_str);  
			}

			// 라이선스 그룹 ID 세팅
			stm3100VO.setLicGrpId(loginVO.getLicGrpId());
			
			//페이지 사이즈
			stm3100VO.setPageIndex(_pageNo);
			stm3100VO.setPageSize(_pageSize);
			stm3100VO.setPageUnit(_pageSize);
			
			PaginationInfo paginationInfo = PagingUtil.getPaginationInfo(stm3100VO);  /** paging - 신규방식 */
    		
			// 프로젝트에 배정 설정 Jenkins Job 목록
			List<Map> jenAuthGrpList = null;
			
			// 총 데이터 건수
			int totCnt = 0;
			// Jenkins Job 허용 역할 그룹 목록 총 건수 조회
			totCnt = stm3100Service.selectStm3100JenkinsJobAuthGrpListCnt(stm3100VO);
			paginationInfo.setTotalRecordCount(totCnt);
			
			// 역할그룹 목록 조회
			jenAuthGrpList = stm3100Service.selectStm3100JenkinsJobAuthGrpList(stm3100VO);

			model.addAttribute("list", jenAuthGrpList);
			
			//페이지 정보 보내기
			Map<String, Integer> pageMap = new HashMap<String, Integer>();
			pageMap.put("pageNo",stm3100VO.getPageIndex());
			pageMap.put("listCount", jenAuthGrpList.size());
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
			Log.error("selectStm3100JenkinsJobAuthGrpListAjax()", ex);
			
			// 조회실패여부 및 조회 실패 메시지 세팅
			model.addAttribute("errorYN", "Y");
			model.addAttribute("message", egovMessageSource.getMessage("fail.common.select"));
			return new ModelAndView("jsonView");
		}
	}
	
	
	/**
	 * Stm3100 Jenkins Job 허용 역할 그룹을 등록한다.
	 * @param request
	 * @param model
	 * @return
	 * @throws Exception
	 */
	@SuppressWarnings("rawtypes")
	@RequestMapping(value="/stm/stm3000/stm3000/insertStm3100JenkinsJobAuthGrpInfoAjax.do")
	public ModelAndView insertStm3100JenkinsJobAuthGrpInfoAjax(HttpServletRequest request, HttpServletResponse response, ModelMap model )	throws Exception {
		try{
			// request 파라미터를 map으로 변환
			Map<String, Object> paramMap = RequestConvertor.requestParamToMapAddSelInfoList(request, true,"authGrpId");

			List selAuthMapList = (List)paramMap.get("list");
			// 선택한 역할그룹 수
			int selAuthCnt = selAuthMapList.size();
			
			// 역할그룹 등록 및 등록 실패 역할그룹 수 리턴
			int addFailAuthCnt = stm3100Service.insertStm3100JenkinsJobAuthGrpInfo(paramMap);
			
			//선택 역할그룹 갯수와 실패 역할그룹 갯수가 같은경우 에러
			if(selAuthCnt == addFailAuthCnt){
				// 등록 실패여부 및 실패 메시지 세팅
				model.addAttribute("errorYn", "Y");
				model.addAttribute("message", "선택된 모든 역할그룹이 중복됩니다.");
			}
			//실패 역할그룹 갯수가 0이상인경우 메시지 변경
			else if(addFailAuthCnt > 0){
				// 등록 성공 여부 및 성공 메시지, 등록 실패한 역할그룹 수 메시지 세팅
				model.addAttribute("errorYn", "N");
				model.addAttribute("message", egovMessageSource.getMessage("success.common.insert")+"</br>"+addFailAuthCnt+"건의 중복 선택 역할그룹은 제외되었습니다.");
			}else{
				//등록  성공여부 및 등록성공 메시지 세팅
				model.addAttribute("errorYn", "N");
				model.addAttribute("message", egovMessageSource.getMessage("success.common.insert"));
			}
			
			return new ModelAndView("jsonView");

		}catch(Exception e){
			Log.error("insertStm3100JenkinsJobAuthGrpInfoAjax()", e);
			// 등록 실패여부 및 실패메시지 세팅
			model.addAttribute("errorYn", "Y");
			model.addAttribute("message", egovMessageSource.getMessage("fail.common.insert"));
			return new ModelAndView("jsonView");
		}
	}
	
	/**
	 * Stm3100 Jenkins Job 허용 역할 그룹을 삭제한다.
	 * @param request
	 * @param model
	 * @return
	 * @throws Exception
	 */
	@RequestMapping(value="/stm/stm3000/stm3100/deleteStm3100JenkinsJobAuthGrpInfoAjax.do")
	public ModelAndView deleteStm3100JenkinsJobAuthGrpInfoAjax(HttpServletRequest request, HttpServletResponse response, ModelMap model )	throws Exception {
		try{
			// request 파라미터를 map으로 변환
			Map<String, Object> paramMap = RequestConvertor.requestParamToMapAddSelInfoList(request, true,"authGrpId");
			
			// 역할그룹 삭제
			stm3100Service.deleteStm3100JenkinsJobAuthGrpInfo(paramMap);
			
			// 삭제 성공여부 및 삭제 성공 메시지 세팅
			model.addAttribute("errorYn", "N");
			model.addAttribute("message", egovMessageSource.getMessage("success.common.delete"));
			
			return new ModelAndView("jsonView");

		}catch(Exception e){
			Log.error("deleteStm3100JenkinsJobAuthGrpInfoAjax()", e);
			
			// 삭제 실패여부 및 삭제 실패메시지 세팅
			model.addAttribute("errorYn", "Y");
			model.addAttribute("message", egovMessageSource.getMessage("fail.common.delete"));
			return new ModelAndView("jsonView");
		}
	}

}
