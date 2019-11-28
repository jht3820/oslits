package kr.opensoftlab.oslops.dsh.dsh1000.dsh1000.web;

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
import org.springframework.web.servlet.ModelAndView;

import egovframework.com.cmm.EgovMessageSource;
import egovframework.rte.fdl.cmmn.trace.LeaveaTrace;
import egovframework.rte.fdl.property.EgovPropertyService;
import egovframework.rte.ptl.mvc.tags.ui.pagination.PaginationInfo;
import kr.opensoftlab.oslops.com.fms.web.service.FileMngService;
import kr.opensoftlab.oslops.com.vo.LoginVO;
import kr.opensoftlab.oslops.com.vo.PageVO;
import kr.opensoftlab.oslops.dsh.dsh1000.dsh1000.service.Dsh1000Service;
import kr.opensoftlab.oslops.dsh.dsh1000.dsh1000.vo.Dsh1000VO;
import kr.opensoftlab.oslops.dsh.dsh2000.dsh2000.service.Dsh2000Service;
import kr.opensoftlab.oslops.prj.prj1000.prj1000.service.Prj1000Service;
import kr.opensoftlab.oslops.prj.prj1000.prj1100.service.Prj1100Service;
import kr.opensoftlab.oslops.prs.prs3000.prs3000.service.Prs3000Service;
import kr.opensoftlab.oslops.req.req1000.req1000.service.Req1000Service;
import kr.opensoftlab.oslops.req.req4000.req4100.service.Req4100Service;
import kr.opensoftlab.oslops.req.req4000.req4400.service.Req4400Service;
import kr.opensoftlab.sdf.util.OslAgileConstant;
import kr.opensoftlab.sdf.util.PagingUtil;
import kr.opensoftlab.sdf.util.RequestConvertor;

/**
 * @Class Name : Dsh1000Controller.java
 * @Description : Dsh1000Controller Controller class
 * @Modification Information
 *
 * @author 진주영
 * @since 2018.08.20.
 * @version 1.0
 * @see
 *  
 *  Copyright (C) OpenSoftLab Corp All right reserved.
 */

@Controller
public class Dsh1000Controller {

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

	/** Dpl2000Service DI */
    @Resource(name = "prs3000Service")
    private Prs3000Service prs3000Service;
    
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
	 * Dsh1000 화면 이동
	 * @param 
	 * @return 
	 * @exception Exception
	 */
	@SuppressWarnings({ "rawtypes" })
	@RequestMapping(value="/dsh/dsh1000/dsh1000/selectDsh1000View.do")
	public String selectDsh1000View(HttpServletRequest request, HttpServletResponse response, ModelMap model ) throws Exception {
		
		try{
			
			// request 파라미터를 map으로 변환
			Map<String, String> paramMap = RequestConvertor.requestParamToMapAddSelInfo(request, true);
			
			//프로젝트 목록 조회
			//로그인VO 가져오기
    		HttpSession ss = request.getSession();
    		LoginVO loginVO = (LoginVO) ss.getAttribute("loginVO");
    		
    		paramMap.put("usrId", loginVO.getUsrId());
    		
        	//선택 프로젝트 단건 조회
    		Map prjInfo = prj1000Service.selectPrj1000Info(paramMap);
    		
    		model.addAttribute("prjNm",prjInfo.get("prjNm"));

    		Map usrInfo = prs3000Service.selectPrs3000(paramMap);
    		model.addAttribute("usrInfo", usrInfo);
			return "/dsh/dsh1000/dsh1000/dsh1000";
		}
		catch(Exception ex){
			Log.error("selectDsh1000View()", ex);
			throw new Exception(ex.getMessage());
		}
	}
	
	/**
	 * Dsh1000 대시보드 데이터 조회 Ajax
	 * @param 
	 * @return 
	 * @exception Exception
	 */
	@SuppressWarnings({ "rawtypes", "unchecked" })
	@RequestMapping(value="/dsh/dsh1000/dsh1000/selectDsh1000DashBoardDataAjax.do")
	public ModelAndView selectDsh1000DashBoardDataAjax(HttpServletRequest request, HttpServletResponse response, ModelMap model ) throws Exception {

		try{

			// request 파라미터를 map으로 변환
			Map<String, String> paramMap = RequestConvertor.requestParamToMapAddSelInfo(request, true);

			//프로젝트 ID 가져오기
    		HttpSession ss = request.getSession();
    		
    		String prjId = (String) ss.getAttribute("selPrjId");
    		paramMap.put("prjId", prjId);
    		LoginVO loginVO = (LoginVO) ss.getAttribute("loginVO");
    		
    		String usrId = (String) loginVO.getUsrId();
    		paramMap.put("usrId", usrId);
    		
    		//[차트1] 프로세스별 총갯수 + 최종 완료 갯수
    		List<Map> processReqCnt = dsh1000Service.selectDsh1000ProcessReqCntList(paramMap);
    		
    		//[차트2] 월별 프로세스별 요구사항 갯수
    		List<Map> monthProcessReqCnt = dsh1000Service.selectDsh1000MonthProcessReqCntList(paramMap);
    		
    		//프로세스 목록
			List<Map> processList = prj1100Service.selectFlw1000ProcessList(paramMap);
			
			//프로세스별 기간 경고 조회
			List<Map> reqDtmOverAlertList = dsh2000Service.selectDsh2000ReqDtmOverAlertList(paramMap);
			
			//작업흐름 목록
			List<Map> flowList = prj1100Service.selectFlw1100FlowList(paramMap);
			
			model.addAttribute("processReqCnt", processReqCnt);
			model.addAttribute("monthProcessReqCnt", monthProcessReqCnt);
			model.addAttribute("processList", processList);
			model.addAttribute("reqDtmOverAlertList", reqDtmOverAlertList);
			model.addAttribute("flowList", flowList);
			
			//등록 성공 메시지 세팅
			model.addAttribute("errorYN", "N");
			model.addAttribute("message", egovMessageSource.getMessage("success.common.select"));

			return new ModelAndView("jsonView");
		}
		catch(Exception ex){
			Log.error("selectDsh1000DashBoardDataAjax()", ex);

			//조회실패 메시지 세팅 및 저장 성공여부 세팅
			model.addAttribute("errorYN", "Y");
			model.addAttribute("message", egovMessageSource.getMessage("fail.common.select"));
			return new ModelAndView("jsonView");
		}
	}
	
	/**
	 * Dsh1000 대시보드 부분 데이터 조회 Ajax
	 * @param 
	 * @return 
	 * @exception Exception
	 */
	@SuppressWarnings({ "rawtypes", "unchecked" })
	@RequestMapping(value="/dsh/dsh1000/dsh1000/selectDsh1000DashBoardSubDataAjax.do")
	public ModelAndView selectDsh1000DashBoardSubDataAjax(HttpServletRequest request, HttpServletResponse response, ModelMap model ) throws Exception {

		try{

			// request 파라미터를 map으로 변환
			Map<String, String> paramMap = RequestConvertor.requestParamToMapAddSelInfo(request, true);

			//프로젝트 ID 가져오기
    		HttpSession ss = request.getSession();
    		
    		String prjId = (String) ss.getAttribute("selPrjId");
    		paramMap.put("prjId", prjId);
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
    		
    		//차트
    		if("chart".equals(redoId)){
    			//[차트1] 프로세스별 총갯수 + 최종 완료 갯수
        		List<Map> processReqCnt = dsh1000Service.selectDsh1000ProcessReqCntList(paramMap);
        		
        		//[차트2] 월별 프로세스별 요구사항 갯수
        		List<Map> monthProcessReqCnt = dsh1000Service.selectDsh1000MonthProcessReqCntList(paramMap);
        		
        		model.addAttribute("processReqCnt", processReqCnt);
        		model.addAttribute("monthProcessReqCnt", monthProcessReqCnt);
    		}
    		//프로세스
    		else if("process".equals(redoId)){
    			//작업흐름 목록
    			List<Map> flowList = prj1100Service.selectFlw1100FlowList(paramMap);
    			
    			//프로세스별 기간 경고 조회
    			List<Map> reqDtmOverAlertList = dsh2000Service.selectDsh2000ReqDtmOverAlertList(paramMap);
    			
    			model.addAttribute("flowList", flowList);
    			model.addAttribute("reqDtmOverAlertList", reqDtmOverAlertList);
    		}

			//등록 성공 메시지 세팅
			model.addAttribute("errorYN", "N");
			model.addAttribute("message", egovMessageSource.getMessage("success.common.select"));

			return new ModelAndView("jsonView");
		}
		catch(Exception ex){
			Log.error("selectDsh1000DashBoardSubDataAjax()", ex);

			//조회실패 메시지 세팅 및 저장 성공여부 세팅
			model.addAttribute("errorYN", "Y");
			model.addAttribute("message", egovMessageSource.getMessage("fail.common.select"));
			return new ModelAndView("jsonView");
		}
	}
	
	/**
	 * Dsh1000 프로세스, 작업흐름 별 요구사항 목록
	 * @param 
	 * @return 
	 * @exception Exception
	 */
	@SuppressWarnings({ "rawtypes", "unchecked" })
	@RequestMapping(value="/dsh/dsh1000/dsh1000/selectDsh1000ProFlowRequestAjax.do")
	public ModelAndView selectDsh1000ProFlowRequestAjax(HttpServletRequest request, HttpServletResponse response, ModelMap model ) throws Exception {

		try{
			// request 파라미터를 map으로 변환
			Map<String, String> paramMap = RequestConvertor.requestParamToMapAddSelInfo(request, true);

			//프로젝트 ID 가져오기
    		HttpSession ss = request.getSession();
    		
    		String prjId = (String) ss.getAttribute("selPrjId");
    		paramMap.put("prjId", prjId);
    		LoginVO loginVO = (LoginVO) ss.getAttribute("loginVO");
    		
    		String usrId = (String) loginVO.getUsrId();
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
			PageVO pageVo = new PageVO();
			
			//페이지 사이즈
			pageVo.setPageIndex(_pageNo);
			pageVo.setPageSize(_pageSize);
			pageVo.setPageUnit(_pageSize);
			
			PaginationInfo paginationInfo = PagingUtil.getPaginationInfo(pageVo);
			
			/** 총 데이터의 건수 를 가져온다. */
			int totCnt = req4100Service.selectReq4100ProcessFlowReqCnt(paramMap);
			paginationInfo.setTotalRecordCount(totCnt);
			
			paramMap.put("firstIndex", String.valueOf(pageVo.getFirstIndex()));
			paramMap.put("lastIndex", String.valueOf(pageVo.getLastIndex()));
			
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
			Log.error("selectDsh1000ProFlowRequestAjax()", ex);

			//조회실패 메시지 세팅 및 저장 성공여부 세팅
			model.addAttribute("errorYN", "Y");
			model.addAttribute("message", egovMessageSource.getMessage("fail.common.select"));
			return new ModelAndView("jsonView");
		}
	}
	
	
	/**
	 * Dsh1000 프로세스별 요구사항 목록
	 * @param 
	 * @return 
	 * @exception Exception
	 */
	@SuppressWarnings({ "rawtypes" })
	@RequestMapping(value="/dsh/dsh1000/dsh1000/selectReq4100ProcessReqList.do")
	public ModelAndView selectReq4100ProcessReqList(HttpServletRequest request, HttpServletResponse response, ModelMap model ) throws Exception {

		try{
			// request 파라미터를 map으로 변환
			Map<String, String> paramMap = RequestConvertor.requestParamToMapAddSelInfo(request, true);

			//프로젝트 ID 가져오기
    		HttpSession ss = request.getSession();
    		
    		String prjId = (String) ss.getAttribute("selPrjId");
    		paramMap.put("prjId", prjId);
    		LoginVO loginVO = (LoginVO) ss.getAttribute("loginVO");
    		
    		String usrId = (String) loginVO.getUsrId();
    		paramMap.put("usrId", usrId);
    		
    		List<Map> proFlowReqList = req4100Service.selectReq4100ProcessReqList(paramMap);
    		
			model.addAttribute("proFlowReqList", proFlowReqList);
			
			//등록 성공 메시지 세팅
			model.addAttribute("errorYN", "N");
			model.addAttribute("message", egovMessageSource.getMessage("success.common.select"));

			return new ModelAndView("jsonView");
		}
		catch(Exception ex){
			Log.error("selectReq4100ProcessReqList()", ex);

			//조회실패 메시지 세팅 및 저장 성공여부 세팅
			model.addAttribute("errorYN", "Y");
			model.addAttribute("message", egovMessageSource.getMessage("fail.common.select"));
			return new ModelAndView("jsonView");
		}
	}
	
	/**
	 * Dsh1001 대시보드  요구사항 목록 팝업 호출
	 * @param 
	 * @return 
	 * @exception Exception
	 */
	@RequestMapping(value="/dsh/dsh1000/dsh1000/selectDsh1001View.do")
	public String selectDsh1001View(HttpServletRequest request, HttpServletResponse response, ModelMap model ) throws Exception {
		return "/dsh/dsh1000/dsh1000/dsh1001";
	}
	
	/**
	 * Dsh1001 대시보드 신호등, 차트 클릭 시 요구사항 목록 조회 
	 * @param 
	 * @return 
	 * @exception Exception
	 */
	@SuppressWarnings("unchecked")
	@RequestMapping(value="/dsh/dsh1000/dsh1000/selectDsh1000ReqList.do")
	public ModelAndView selectDsh1000ReqList(@ModelAttribute("dsh1000VO") Dsh1000VO dsh1000VO, HttpServletRequest request, HttpServletResponse response, ModelMap model)	throws Exception {
		
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
			dsh1000VO.setPageIndex(_pageNo);
			dsh1000VO.setPageSize(_pageSize);
			dsh1000VO.setPageUnit(_pageSize);
			
			// projectId 파라미터를 가져온다. 통합대시보드에서 프로젝트의 신호등 클릭 시 projectId 파라미터를 전달한다.
			String projectId = paramMap.get("projectId");
			// projectId 파라미터가 있을경우 
			if(projectId != null && !"".equals(projectId)) {
				dsh1000VO.setPrjId(projectId);
			// projectId 파라미터가 없을경우 
			}else {
				// session에서 현재 선택된 프로젝트 id를 가져와 세팅한다.
				dsh1000VO.setPrjId((String) request.getSession().getAttribute("selPrjId"));
			}
        	
    		PaginationInfo paginationInfo = PagingUtil.getPaginationInfo(dsh1000VO);  /** paging - 신규방식 */
        	List<Dsh1000VO> dsh1000List = null;

			HttpSession ss = request.getSession();
			LoginVO loginVO = (LoginVO) ss.getAttribute("loginVO");
			dsh1000VO.setLicGrpId(loginVO.getLicGrpId());

    		// 목록 조회  authGrpIds
			dsh1000List = dsh1000Service.selectDsh1000ReqList(dsh1000VO);
		    
    		// 총 건수
		    int totCnt = dsh1000Service.selectDsh1000ReqListCnt(dsh1000VO);
		    paginationInfo.setTotalRecordCount(totCnt);
		    
		    model.addAttribute("list", dsh1000List);
		    
		    //페이지 정보 보내기
			Map<String, Integer> pageMap = new HashMap<String, Integer>();
			pageMap.put("pageNo",dsh1000VO.getPageIndex());
			pageMap.put("listCount", dsh1000List.size());
			pageMap.put("totalPages", paginationInfo.getTotalPageCount());
			pageMap.put("totalElements", totCnt);
			pageMap.put("pageSize", _pageSize);

			model.addAttribute("page", pageMap);
        	
        	//조회성공메시지 세팅
        	model.addAttribute("message", egovMessageSource.getMessage("success.common.select"));
        	
        	return new ModelAndView("jsonView", model);
    	}catch(Exception ex){
    		Log.error("selectDsh1000ReqList()", ex);
    		
    		//조회실패 메시지 세팅
    		model.addAttribute("message", egovMessageSource.getMessage("fail.common.select"));
    		return new ModelAndView("jsonView", model);
    	}
	}	
	
	
	
}
