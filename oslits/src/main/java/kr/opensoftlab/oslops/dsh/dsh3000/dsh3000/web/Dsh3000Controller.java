package kr.opensoftlab.oslops.dsh.dsh3000.dsh3000.web;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import kr.opensoftlab.oslops.com.fms.web.service.FileMngService;
import kr.opensoftlab.oslops.com.vo.LoginVO;
import kr.opensoftlab.oslops.com.vo.PageVO;
import kr.opensoftlab.oslops.dsh.dsh1000.dsh1000.service.Dsh1000Service;
import kr.opensoftlab.oslops.dsh.dsh2000.dsh2000.service.Dsh2000Service;
import kr.opensoftlab.oslops.dsh.dsh3000.dsh3000.service.Dsh3000Service;
import kr.opensoftlab.oslops.dsh.dsh3000.dsh3000.vo.Dsh3000VO;
import kr.opensoftlab.oslops.prj.prj1000.prj1000.service.Prj1000Service;
import kr.opensoftlab.oslops.prj.prj1000.prj1100.service.Prj1100Service;
import kr.opensoftlab.oslops.req.req1000.req1000.service.Req1000Service;
import kr.opensoftlab.oslops.req.req1000.req1000.vo.Req1000VO;
import kr.opensoftlab.oslops.req.req4000.req4100.service.Req4100Service;
import kr.opensoftlab.oslops.req.req4000.req4400.service.Req4400Service;
import kr.opensoftlab.sdf.util.OslAgileConstant;
import kr.opensoftlab.sdf.util.PagingUtil;
import kr.opensoftlab.sdf.util.RequestConvertor;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;

import com.google.gson.GsonBuilder;

import egovframework.com.cmm.EgovMessageSource;
import egovframework.rte.fdl.cmmn.trace.LeaveaTrace;
import egovframework.rte.fdl.property.EgovPropertyService;
import egovframework.rte.ptl.mvc.tags.ui.pagination.PaginationInfo;

/**
 * @Class Name : Dsh3000Controller.java
 * @Description : Dsh3000Controller Controller class
 * @Modification Information
 *
 * @author 진주영
 * @since 2018.09.19.
 * @version 1.0
 * @see
 *  
 *  Copyright (C) OpenSoftLab Corp All right reserved.
 */

@Controller
public class Dsh3000Controller {

	/**
     * Logging 을 위한 선언
     * Log는 반드시 가이드에 맞춰서 작성한다. 개발용으로는 debug 카테고리를 사용한다.
     */
	protected Logger Log = Logger.getLogger(this.getClass());
	
	/** Dsh1000Service DI */
    @Resource(name = "dsh1000Service")
    private Dsh1000Service dsh1000Service;

    /** Dsh2000Service DI */
    @Resource(name = "dsh2000Service")
    private Dsh2000Service dsh2000Service;
    
    /** Dsh3000Service DI */
    @Resource(name = "dsh3000Service")
    private Dsh3000Service dsh3000Service;

    /** Req1000Service DI */
	@Resource(name = "req1000Service")
	private Req1000Service req1000Service;
	
    /** Req4100Service DI */
	@Resource(name = "req4100Service")
	private Req4100Service req4100Service;
	
	/** Req4400Service DI */
	@Resource(name = "req4400Service")
	private Req4400Service req4400Service;
	
	/** Prj1000Service DI */
    @Resource(name = "prj1000Service")
    private Prj1000Service prj1000Service;

	/** Prj1100Service DI */
    @Resource(name = "prj1100Service")
    private Prj1100Service prj1100Service;
    
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
	
	/** TRACE */
	@Resource(name = "leaveaTrace")
	LeaveaTrace leaveaTrace;
	/**
	 * Dsh3000 화면 이동
	 * @param 
	 * @return 
	 * @exception Exception
	 */
	@SuppressWarnings({ "rawtypes", "unchecked" })
	@RequestMapping(value="/dsh/dsh3000/dsh3000/selectDsh3000View.do")
	public String selectDsh3000View(HttpServletRequest request, HttpServletResponse response, ModelMap model ) throws Exception {
		
		try{
			// request 파라미터를 map으로 변환
			Map<String, String> paramMap = RequestConvertor.requestParamToMapAddSelInfo(request, true);
			//세션
			HttpSession ss = request.getSession();
			
			//로그인VO 가져오기
    		LoginVO loginVO = (LoginVO) ss.getAttribute("loginVO");
    		paramMap.put("usrId", loginVO.getUsrId());
    		
			List<Map> prjList = (List)prj1000Service.selectPrj1000View(paramMap);
			String prjListJson	= (new GsonBuilder().serializeNulls().create()).toJsonTree(prjList).toString();
			
			
			model.addAttribute("prjList", prjListJson.replaceAll("<", "&lt"));
			return "/dsh/dsh3000/dsh3000/dsh3000";
		}
		catch(Exception ex){
			Log.error("selectDsh3000View()", ex);
			throw new Exception(ex.getMessage());
		}
	}

	/**
	 * Dsh3000 대시보드 데이터 조회 Ajax
	 * -프로젝트 데이터
	 * @param 
	 * @return 
	 * @exception Exception
	 */
	@SuppressWarnings({ "rawtypes", "unchecked" })
	@RequestMapping(value="/dsh/dsh3000/dsh3000/selectDsh3000DashBoardPrjDataAjax.do")
	public ModelAndView selectDsh3000DashBoardPrjDataAjax(HttpServletRequest request, HttpServletResponse response, ModelMap model ) throws Exception {

		try{

			// request 파라미터를 map으로 변환
			Map<String, String> paramMap = RequestConvertor.requestParamToMapAddSelInfo(request, true);

			//프로젝트 ID 가져오기
    		HttpSession ss = request.getSession();
    		
    		String prjId = (String) paramMap.get("prjId");
    		
    		
    		LoginVO loginVO = (LoginVO) ss.getAttribute("loginVO");
    		
    		String usrId = (String) loginVO.getUsrId();
    		paramMap.put("usrId", usrId);
    		
    		//[차트1] 프로세스별 총갯수 + 최종 완료 갯수
    		List<Map> processReqCnt = dsh1000Service.selectDsh1000ProcessReqCntList(paramMap);
    		
    		//[차트2] 월별 프로세스별 요구사항 갯수
    		List<Map> monthProcessReqCnt = dsh1000Service.selectDsh1000MonthProcessReqCntList(paramMap);
    		
    		//프로세스 목록
			List<Map> processList = dsh2000Service.selectDsh2000ProcessReqRatioList(paramMap);

			//프로세스별 기간 경고 조회
			List<Map> reqDtmOverAlertList = dsh2000Service.selectDsh2000ReqDtmOverAlertList(paramMap);
			
			//작업흐름 목록
			List<Map> flowList = prj1100Service.selectFlw1100FlowList(paramMap);
			
			Req1000VO req1000Vo = new Req1000VO();
			req1000Vo.setPrjId(prjId);
			req1000Vo.setLoginUsrId(usrId);
			req1000Vo.setReqProType("01"); //접수 요청중 요구사항만
			
			//해당 프로젝트 접수 대기 목록
			List<Req1000VO> newReqList = req1000Service.selectReq1000AllList(req1000Vo);
			
			model.addAttribute("processReqCnt", processReqCnt);
			model.addAttribute("monthProcessReqCnt", monthProcessReqCnt);
			model.addAttribute("processList", processList);
			model.addAttribute("flowList", flowList);
			model.addAttribute("newReqList", newReqList);
			model.addAttribute("reqDtmOverAlertList", reqDtmOverAlertList);
			
			//등록 성공 메시지 세팅
			model.addAttribute("errorYN", "N");
			model.addAttribute("message", egovMessageSource.getMessage("success.common.select"));

			return new ModelAndView("jsonView");
		}
		catch(Exception ex){
			Log.error("selectDsh3000DashBoardPrjDataAjax()", ex);

			//조회실패 메시지 세팅 및 저장 성공여부 세팅
			model.addAttribute("errorYN", "Y");
			model.addAttribute("message", egovMessageSource.getMessage("fail.common.select"));
			return new ModelAndView("jsonView");
		}
	}
	
	/**
	 * Dsh3000 대시보드 부분 데이터 조회 Ajax
	 * @param 
	 * @return 
	 * @exception Exception
	 */
	@SuppressWarnings({ "rawtypes", "unchecked" })
	@RequestMapping(value="/dsh/dsh3000/dsh3000/selectDsh3000DashBoardSubDataAjax.do")
	public ModelAndView selectDsh3000DashBoardSubDataAjax(HttpServletRequest request, HttpServletResponse response, ModelMap model ) throws Exception {

		try{

			// request 파라미터를 map으로 변환
			Map<String, String> paramMap = RequestConvertor.requestParamToMapAddSelInfo(request, true);

			//프로젝트 ID 가져오기
    		HttpSession ss = request.getSession();
    		
    		String prjId = (String) paramMap.get("prjId");
    		LoginVO loginVO = (LoginVO) ss.getAttribute("loginVO");
    		
    		String usrId = (String) loginVO.getUsrId();
    		paramMap.put("usrId", usrId);

    		//부분 타입
    		String redoId = (String) paramMap.get("redoId");
    		
    		if(redoId == null){
    			//조회실패 메시지 세팅 및 저장 성공여부 세팅
    			model.addAttribute("errorYN", "Y");
    			model.addAttribute("message", egovMessageSource.getMessage("fail.common.select"));
    			return new ModelAndView("jsonView");
    		}
    		
    		//전체 프로젝트 그룹
    		if("prjGrpAllChart".equals(redoId)){
    			//[그룹 차트] 프로젝트 그룹별 총갯수 + 최종 완료 갯수
    			List<Map> projectGrpReqCnt = dsh3000Service.selectDsh3000AllPrjGrpReqCntList(paramMap);
    			
    			//[그룹 차트] 월별 프로젝트 그룹별 요구사항 갯수
    			List<Map> monthPrjGrpReqCnt = dsh3000Service.selectDsh3000MonthAllPrjGrpReqCntList(paramMap);
    			
    			model.addAttribute("projectGrpReqCnt", projectGrpReqCnt);
        		model.addAttribute("monthPrjGrpReqCnt", monthPrjGrpReqCnt);
    		}
    		//프로젝트 그룹
    		else if("prjGrpChart".equals(redoId)){
    			//[차트1] 프로젝트별 총갯수 + 최종 완료 갯수
        		List<Map> projectReqCnt = dsh3000Service.selectDsh3000PrjReqCntList(paramMap);
        		
        		//[차트2] 월별 프로젝트별 요구사항 갯수
        		List<Map> monthPrjReqCnt = dsh3000Service.selectDsh3000MonthPrjReqCntList(paramMap);
        		
        		model.addAttribute("projectReqCnt", projectReqCnt);
        		model.addAttribute("monthPrjReqCnt", monthPrjReqCnt);
    		}
    		//프로젝트
    		else if("project".equals(redoId)){
    			//[차트1] 프로세스별 총갯수 + 최종 완료 갯수
        		List<Map> processReqCnt = dsh1000Service.selectDsh1000ProcessReqCntList(paramMap);
        		
        		//[차트2] 월별 프로세스별 요구사항 갯수
        		List<Map> monthProcessReqCnt = dsh1000Service.selectDsh1000MonthProcessReqCntList(paramMap);
        		
        		model.addAttribute("processReqCnt", processReqCnt);
        		model.addAttribute("monthProcessReqCnt", monthProcessReqCnt);
    		}
    		//접수
    		else if("request".equals(redoId)){
    			Req1000VO req1000Vo = new Req1000VO();
    			req1000Vo.setPrjId(prjId);
    			req1000Vo.setLoginUsrId(usrId);
    			req1000Vo.setReqProType("01"); //접수 요청중 요구사항만
    			
    			//해당 프로젝트 접수 대기 목록
    			List<Req1000VO> newReqList = req1000Service.selectReq1000AllList(req1000Vo);
    			
    			model.addAttribute("newReqList", newReqList);
    		}
    		//프로세스
    		else if("process".equals(redoId)){
    			//작업흐름 목록
    			List<Map> flowList = prj1100Service.selectFlw1100FlowList(paramMap);
    			
    			model.addAttribute("flowList", flowList);
    		}

			//등록 성공 메시지 세팅
			model.addAttribute("errorYN", "N");
			model.addAttribute("message", egovMessageSource.getMessage("success.common.select"));

			return new ModelAndView("jsonView");
		}
		catch(Exception ex){
			Log.error("selectDsh3000DashBoardSubDataAjax()", ex);

			//조회실패 메시지 세팅 및 저장 성공여부 세팅
			model.addAttribute("errorYN", "Y");
			model.addAttribute("message", egovMessageSource.getMessage("fail.common.select"));
			return new ModelAndView("jsonView");
		}
	}
	
	/**
	 * Dsh300 통합대시보드 접수태기 요구사항 목록 조회
	 * @param 
	 * @return 
	 * @exception Exception
	 */
	@SuppressWarnings("rawtypes")
	@RequestMapping(value="/dsh/dsh3000/dsh3000/selectDsh3000AcceptRequestAjax.do")
	public ModelAndView selectDsh3000AcceptRequestAjax(HttpServletRequest request, HttpServletResponse response, ModelMap model ) throws Exception {

		try{
			// request 파라미터를 map으로 변환
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
			
			// pageVO 생성
			PageVO pageVo = new PageVO();
			
			//페이지 사이즈
			pageVo.setPageIndex(_pageNo);
			pageVo.setPageSize(_pageSize);
			pageVo.setPageUnit(_pageSize);
			
			PaginationInfo paginationInfo = PagingUtil.getPaginationInfo(pageVo);  /** paging - 신규방식 */

			HttpSession ss = request.getSession();
			LoginVO loginVO = (LoginVO) ss.getAttribute("loginVO");
			// 라이선스 그룹 ID를 가져와 Map에 세팅한다.
			String licGrpId = loginVO.getLicGrpId();
			paramMap.put("licGrpId", licGrpId);
    		
			/** 총 데이터의 건수 를 가져온다. */
			int totCnt = req1000Service.selectReq1000IntegratedDshAcceptReqListCnt(paramMap);
			paginationInfo.setTotalRecordCount(totCnt);
			
			// first, last index 세팅
			paramMap.put("firstIndex", String.valueOf(pageVo.getFirstIndex()));
			paramMap.put("lastIndex", String.valueOf(pageVo.getLastIndex()));
			
			// 접수대기 요구사항 목록을 조회한다.
    		List<Map> dsh3000AcceptReqList = req1000Service.selectReq1000IntegratedDshAcceptReqList(paramMap);

    		//페이지 정보 보내기
			Map<String, Integer> pageMap = new HashMap<String, Integer>();
			pageMap.put("pageNo",pageVo.getPageIndex());
			pageMap.put("listCount", dsh3000AcceptReqList.size());
			pageMap.put("totalPages", paginationInfo.getTotalPageCount());
			pageMap.put("totalElements", totCnt);
			pageMap.put("pageSize", _pageSize);
    		
    		model.addAttribute("page", pageMap);
    		model.addAttribute("acceptTotalCnt", totCnt);
			model.addAttribute("acceptReqList", dsh3000AcceptReqList);

			// 조회성공여부 및 조회성공 메시지 세팅
			model.addAttribute("errorYn", "N");
			model.addAttribute("message", egovMessageSource.getMessage("success.common.select"));
			
			return new ModelAndView("jsonView");
		}
		catch(Exception ex){
			Log.error("selectDsh3000AcceptRequestAjax()", ex);
			// 조회 실패여부 및 조회 실패 메시지세팅
			model.addAttribute("errorYn", "Y");
			model.addAttribute("message", egovMessageSource.getMessage("fail.common.select"));
			return new ModelAndView("jsonView");
		}
	}
	
	/**
	 * Dsh3000 프로세스, 작업흐름 별 요구사항 목록
	 * @param 
	 * @return 
	 * @exception Exception
	 */
	@SuppressWarnings({ "rawtypes", "unchecked" })
	@RequestMapping(value="/dsh/dsh3000/dsh3000/selectDsh3000ProFlowRequestAjax.do")
	public ModelAndView selectDsh3000ProFlowRequestAjax(HttpServletRequest request, HttpServletResponse response, ModelMap model ) throws Exception {

		try{

			// request 파라미터를 map으로 변환
			Map<String, String> paramMap = RequestConvertor.requestParamToMapAddSelInfo(request, true);

			// 세션
    		HttpSession ss = request.getSession();
    		
    		// 프로젝트 ID를 가져와 map에 세팅한다.
    		String prjId = (String) paramMap.get("prjId");
    		paramMap.put("prjId", prjId);
    		
    		// 로그인 vo를 가져온다.
    		LoginVO loginVO = (LoginVO) ss.getAttribute("loginVO");
    		String usrId = (String) loginVO.getUsrId();
    		// 사용자 id를 map에 세팅한다.
    		paramMap.put("usrId", usrId);
    		
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
			
			// pageVO 생성
			PageVO pageVo = new PageVO();
			
			//페이지 사이즈
			pageVo.setPageIndex(_pageNo);
			pageVo.setPageSize(_pageSize);
			pageVo.setPageUnit(_pageSize);
			
			PaginationInfo paginationInfo = PagingUtil.getPaginationInfo(pageVo);
    		
			/** 총 데이터의 건수 를 가져온다. */
			int totCnt = req4100Service.selectReq4100ProcessFlowReqCnt(paramMap);
			paginationInfo.setTotalRecordCount(totCnt);
			
			// first, last index 세팅
			paramMap.put("firstIndex", String.valueOf(pageVo.getFirstIndex()));
			paramMap.put("lastIndex", String.valueOf(pageVo.getLastIndex()));
			
			// 요구사항 목록을 조회한다.
    		List<Map> proFlowReqList = req4100Service.selectReq4100ProcessFlowReqList(paramMap);
    		
    		//페이지 정보 보내기
			Map<String, Integer> pageMap = new HashMap<String, Integer>();
			pageMap.put("pageNo",pageVo.getPageIndex());
			pageMap.put("listCount", proFlowReqList.size());
			pageMap.put("totalPages", paginationInfo.getTotalPageCount());
			pageMap.put("totalElements", totCnt);
			pageMap.put("pageSize", _pageSize);
    		
    		model.addAttribute("page", pageMap);
			model.addAttribute("proFlowReqList", proFlowReqList);

			//등록 성공 메시지 세팅
			model.addAttribute("errorYN", "N");
			model.addAttribute("message", egovMessageSource.getMessage("success.common.select"));

			return new ModelAndView("jsonView");
		}
		catch(Exception ex){
			Log.error("selectDsh3000ProFlowRequestAjax()", ex);

			//조회실패 메시지 세팅 및 저장 성공여부 세팅
			model.addAttribute("errorYN", "Y");
			model.addAttribute("message", egovMessageSource.getMessage("fail.common.select"));
			return new ModelAndView("jsonView");
		}
	}
	
	/**
	 * Dsh3000 대시보드 데이터 조회 Ajax
	 * -프로젝트 그룹 데이터
	 * @param 
	 * @return 
	 * @exception Exception
	 */
	@SuppressWarnings({ "rawtypes", "unchecked" })
	@RequestMapping(value="/dsh/dsh3000/dsh3000/selectDsh3000DashBoardPrjGrpDataAjax.do")
	public ModelAndView selectDsh3000DashBoardPrjGrpDataAjax(HttpServletRequest request, HttpServletResponse response, ModelMap model ) throws Exception {

		try{

			// request 파라미터를 map으로 변환
			Map<String, String> paramMap = RequestConvertor.requestParamToMapAddSelInfo(request, true);

			//프로젝트 ID 가져오기
    		HttpSession ss = request.getSession();
    		
    		LoginVO loginVO = (LoginVO) ss.getAttribute("loginVO");
    		
    		String usrId = (String) loginVO.getUsrId();
    		paramMap.put("usrId", usrId);
    		//[차트1] 프로젝트별 총갯수 + 최종 완료 갯수
    		List<Map> projectReqCnt = dsh3000Service.selectDsh3000PrjReqCntList(paramMap);
    		
    		//[차트2] 월별 프로젝트별 요구사항 갯수
    		List<Map> monthPrjReqCnt = dsh3000Service.selectDsh3000MonthPrjReqCntList(paramMap);
    		
    		//[프로젝트별 차트] 프로세스별 요구사항 총갯수 + 최종 완료 갯수
    		List<Map> prjProcessReqCntList = dsh3000Service.selectDsh3000PrjProcessReqCntList(paramMap);
    		
    		//[프로젝트별 차트]  월별 프로세스별 요구사항 갯수
    		List<Map> monthPrjProcessReqCntList = dsh3000Service.selectDsh3000MonthPrjProcessReqCntList(paramMap);
    		
    		//프로세스별 기간 경고 조회
			List<Map> prjReqDtmOverAlertList = dsh3000Service.selectDsh3000PrjReqDtmOverAlertList(paramMap);
			
			model.addAttribute("projectReqCnt", projectReqCnt);
			model.addAttribute("monthPrjReqCnt", monthPrjReqCnt);
			model.addAttribute("prjProcessReqCntList", prjProcessReqCntList);
			model.addAttribute("monthPrjProcessReqCntList", monthPrjProcessReqCntList);
			model.addAttribute("prjReqDtmOverAlertList", prjReqDtmOverAlertList);
			
			//등록 성공 메시지 세팅
			model.addAttribute("errorYN", "N");
			model.addAttribute("message", egovMessageSource.getMessage("success.common.select"));

			return new ModelAndView("jsonView");
		}
		catch(Exception ex){
			Log.error("selectDsh3000DashBoardPrjGrpDataAjax()", ex);

			//조회실패 메시지 세팅 및 저장 성공여부 세팅
			model.addAttribute("errorYN", "Y");
			model.addAttribute("message", egovMessageSource.getMessage("fail.common.select"));
			return new ModelAndView("jsonView");
		}
	}
	
	/**
	 * Dsh3000 대시보드  프로젝트 그룹 전체 데이터 조회 Ajax
	 * -프로젝트 그룹 전체 데이터
	 * @param 
	 * @return 
	 * @exception Exception
	 */
	@SuppressWarnings({ "rawtypes", "unchecked" })
	@RequestMapping(value="/dsh/dsh3000/dsh3000/selectDsh3000DashBoardPrjGrpAllDataAjax.do")
	public ModelAndView selectDsh3000DashBoardPrjGrpAllDataAjax(HttpServletRequest request, HttpServletResponse response, ModelMap model ) throws Exception {
		
		try{
			
			// request 파라미터를 map으로 변환
			Map<String, String> paramMap = RequestConvertor.requestParamToMapAddSelInfo(request, true);
			
			//프로젝트 ID 가져오기
			HttpSession ss = request.getSession();
			
			LoginVO loginVO = (LoginVO) ss.getAttribute("loginVO");
			
			String usrId = (String) loginVO.getUsrId();
			paramMap.put("usrId", usrId);
			//[그룹 차트] 프로젝트 그룹별 총갯수 + 최종 완료 갯수
			List<Map> projectGrpReqCnt = dsh3000Service.selectDsh3000AllPrjGrpReqCntList(paramMap);
			
			//[그룹 차트] 월별 프로젝트 그룹별 요구사항 갯수
			List<Map> monthPrjGrpReqCnt = dsh3000Service.selectDsh3000MonthAllPrjGrpReqCntList(paramMap);
			
			//[프로젝트 그룹별 차트] 프로젝트 그룹별 총갯수 + 최종 완료 갯수
    		List<Map> prjGrpReqCntList = dsh3000Service.selectDsh3000PrjGrpReqCntList(paramMap);
    		
    		//[프로젝트 그룹별 차트]  월별 프로젝트 그룹별 요구사항 갯수
    		List<Map> monthPrjGrpReqCntList = dsh3000Service.selectDsh3000MonthPrjGrpReqCntList(paramMap);
    		
			//대시보드 프로젝트 그룹별 기간 비교 경고
			List<Map> prjGrpReqDtmOverAlertList = dsh3000Service.selectDsh3000PrjGrpReqDtmOverAlertList(paramMap);
			
			model.addAttribute("projectGrpReqCnt", projectGrpReqCnt);
			model.addAttribute("monthPrjGrpReqCnt", monthPrjGrpReqCnt);
			model.addAttribute("prjGrpReqCntList", prjGrpReqCntList);
			model.addAttribute("monthPrjGrpReqCntList", monthPrjGrpReqCntList);
			model.addAttribute("prjGrpReqDtmOverAlertList", prjGrpReqDtmOverAlertList);
			
			//등록 성공 메시지 세팅
			model.addAttribute("errorYN", "N");
			model.addAttribute("message", egovMessageSource.getMessage("success.common.select"));
			
			return new ModelAndView("jsonView");
		}
		catch(Exception ex){
			Log.error("selectDsh3000DashBoardPrjGrpAllDataAjax()", ex);
			
			//조회실패 메시지 세팅 및 저장 성공여부 세팅
			model.addAttribute("errorYN", "Y");
			model.addAttribute("message", egovMessageSource.getMessage("fail.common.select"));
			return new ModelAndView("jsonView");
		}
	}
	
	/**
	 * Dsh3001 통합대시보드  요구사항 목록 팝업 호출
	 * @param 
	 * @return 
	 * @exception Exception
	 */
	@RequestMapping(value="/dsh/dsh3000/dsh3000/selectDsh3001View.do")
	public String selectDsh1001View(HttpServletRequest request, HttpServletResponse response, ModelMap model ) throws Exception {
		return "/dsh/dsh3000/dsh3000/dsh3001";
	}
	
	/**
	 * Dsh3000 통합대시보드 - 통합데이터, 프로젝트 그룹별 신호등, 차트 클릭 시 요구사항 목록 조회 
	 * @param 
	 * @return 
	 * @exception Exception
	 */
	@SuppressWarnings("rawtypes")
	@RequestMapping(value="/dsh/dsh3000/dsh3000/selectDsh3000ReqList.do")
	public ModelAndView selectDsh3000ReqList(@ModelAttribute("dsh3000VO") Dsh3000VO dsh3000VO, HttpServletRequest request, HttpServletResponse response, ModelMap model)	throws Exception {
		
    	try{
    		//리퀘스트에서 넘어온 파라미터를 맵으로 세팅
        	Map<String, String> paramMap = RequestConvertor.requestParamToMapAddSelInfo(request, true);
        	
       		//현재 페이지 값, 보여지는 개체 수
			String _pageNo_str = paramMap.get("pageNo");
			String _pageSize_str = paramMap.get("pageSize");
			
			int _pageNo = 1;
			int _pageSize = 10;
			
			if(_pageNo_str != null && !"".equals(_pageNo_str)){
				_pageNo = Integer.parseInt(_pageNo_str)+1;  
			}
			if(_pageSize_str != null && !"".equals(_pageSize_str)){
				_pageSize = Integer.parseInt(_pageSize_str);  
			}
			
			//페이지 사이즈
			dsh3000VO.setPageIndex(_pageNo);
			dsh3000VO.setPageSize(_pageSize);
			dsh3000VO.setPageUnit(_pageSize);
        	
    		PaginationInfo paginationInfo = PagingUtil.getPaginationInfo(dsh3000VO);  //** paging - 신규방식 */
        	List dsh3000List = null;

			HttpSession ss = request.getSession();
			LoginVO loginVO = (LoginVO) ss.getAttribute("loginVO");
			// 라이선스 그룹, 사용자 id 세팅
			dsh3000VO.setLicGrpId(loginVO.getLicGrpId());
			dsh3000VO.setUsrId(loginVO.getUsrId());
			
    		// 통합대시보드 요구사항 목록 조회 - 신호등, 차트 클릭 시 해당하는 요구사항 목록 조회
			dsh3000List = dsh3000Service.selectDsh3000ReqList(dsh3000VO);
		    
    		// 총 건수
		    int totCnt = dsh3000Service.selectDsh3000ReqListCnt(dsh3000VO);
		    paginationInfo.setTotalRecordCount(totCnt);
		    
		    model.addAttribute("list", dsh3000List);
		    
		    //페이지 정보 보내기
			Map<String, Integer> pageMap = new HashMap<String, Integer>();
			pageMap.put("pageNo",dsh3000VO.getPageIndex());
			pageMap.put("listCount", dsh3000List.size());
			pageMap.put("totalPages", paginationInfo.getTotalPageCount());
			pageMap.put("totalElements", totCnt);
			pageMap.put("pageSize", _pageSize);

			model.addAttribute("page", pageMap);
        	
        	// 조회 성공여부 및 조회성공 메시지 세팅
			model.addAttribute("errorYN", "Y");
        	model.addAttribute("message", egovMessageSource.getMessage("success.common.select"));
        	
        	return new ModelAndView("jsonView", model);
    	}catch(Exception ex){
    		Log.error("selectDsh3000ReqList()", ex);
    		
    		// 조회 실패여부 및 조회실패 메시지 세팅
    		model.addAttribute("errorYN", "N");
    		model.addAttribute("message", egovMessageSource.getMessage("fail.common.select"));
    		return new ModelAndView("jsonView", model);
    	}
	}
}
