package kr.opensoftlab.oslops.dsh.dsh2000.dsh2000.web;

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
import kr.opensoftlab.oslops.cmm.cmm4000.cmm4000.service.Cmm4000Service;
import kr.opensoftlab.oslops.com.fms.web.service.FileMngService;
import kr.opensoftlab.oslops.com.vo.LoginVO;
import kr.opensoftlab.oslops.dsh.dsh1000.dsh1000.service.Dsh1000Service;
import kr.opensoftlab.oslops.dsh.dsh2000.dsh2000.service.Dsh2000Service;
import kr.opensoftlab.oslops.dsh.dsh2000.dsh2000.vo.Dsh2000VO;
import kr.opensoftlab.oslops.prj.prj1000.prj1000.service.Prj1000Service;
import kr.opensoftlab.oslops.req.req1000.req1000.service.Req1000Service;
import kr.opensoftlab.oslops.req.req4000.req4100.service.Req4100Service;
import kr.opensoftlab.oslops.req.req4000.req4400.service.Req4400Service;
import kr.opensoftlab.sdf.util.PagingUtil;
import kr.opensoftlab.sdf.util.RequestConvertor;

/**
 * @Class Name : Dsh1000Controller.java
 * @Description : Dsh1000Controller Controller class
 * @Modification Information
 *
 * @author 진주영
 * @since 2018.09.20.
 * @version 1.0
 * @see
 *  
 *  Copyright (C) OpenSoftLab Corp All right reserved.
 */

@Controller
public class Dsh2000Controller {

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
    /** EgovMessageSource */
	@Resource(name = "egovMessageSource")
	EgovMessageSource egovMessageSource;

	/** EgovPropertyService */
	@Resource(name = "propertiesService")
	protected EgovPropertyService propertiesService;
	
	@Value("${Globals.fileStorePath}")
	private String tempPath;
	
	/** TRACE */
	@Resource(name = "leaveaTrace")
	LeaveaTrace leaveaTrace;
    
    /**
	 * Dsh2000 화면 이동
	 * @param 
	 * @return 
	 * @exception Exception
	 */
	@RequestMapping(value="/dsh/dsh2000/dsh2000/selectDsh2000View.do")
    public String selectDsh2000View(HttpServletRequest request, HttpServletResponse response, ModelMap model ) throws Exception {
        return "/dsh/dsh2000/dsh2000/dsh2000";
    }
	
    /**
	 * Dsh2001 화면 이동 (계획대비 미처리 요구사항 팝업)
	 * @param 
	 * @return 
	 * @exception Exception
	 */
	@RequestMapping(value="/dsh/dsh2000/dsh2000/selectDsh2001View.do")
    public String selectDsh2001View(HttpServletRequest request, HttpServletResponse response, ModelMap model ) throws Exception {
        return "/dsh/dsh2000/dsh2000/dsh2001";
    }
	
	/**
	 * Dsh2000 대시보드 데이터 조회 Ajax
	 * @param 
	 * @return 
	 * @exception Exception
	 */
	@SuppressWarnings({ "rawtypes", "unchecked" })
	@RequestMapping(value="/dsh/dsh2000/dsh2000/selectDsh2000DashBoardDataAjax.do")
	public ModelAndView selectDsh2000DashBoardDataAjax(HttpServletRequest request, HttpServletResponse response, ModelMap model ) throws Exception {

		try{
			// request 파라미터를 map으로 변환
			Map<String, String> paramMap = RequestConvertor.requestParamToMapAddSelInfo(request, true);
			
			//로그인VO 가져오기
			HttpSession ss = request.getSession();
			LoginVO loginVO = (LoginVO) ss.getAttribute("loginVO");
			String usrId = (String) loginVO.getUsrId();
    		paramMap.put("usrId", usrId);
			
			String prjId = (String) ss.getAttribute("selPrjId");
			paramMap.put("prjId", prjId);
			
	    	//선택 프로젝트 단건 조회
			Map prjInfo = dsh2000Service.selectDsh2000PrjReqAllInfo(paramMap);
			
			//프로세스 목록 조회
			List<Map> processList = dsh2000Service.selectDsh2000ProcessReqRatioList(paramMap);
			
			//프로세스별 기간 경고 조회
			List<Map> reqDtmOverAlertList = dsh2000Service.selectDsh2000ReqDtmOverAlertList(paramMap);
			
			//작업흐름 목록 조회
			List<Map> flowList = dsh2000Service.selectDsh2000FlowList(paramMap);
			
			//계획대비 미처리건수 조회
			List<Map> reqDtmOverList = dsh2000Service.selectDsh2000ReqDtmOverList(paramMap);
			
			//산출물 제출예정일 대비 미제출 건수
			List<Map> docDtmOverList = dsh2000Service.selectDsh2000DocDtmOverList(paramMap);
			
			//[차트1] 프로세스별 총갯수 + 최종 완료 갯수
    		List<Map> processReqCnt = dsh1000Service.selectDsh1000ProcessReqCntList(paramMap);
    		
    		//[차트2] 월별 프로세스별 요구사항 갯수
    		List<Map> monthProcessReqCnt = dsh1000Service.selectDsh1000MonthProcessReqCntList(paramMap);
			
			model.addAttribute("prjInfo",prjInfo);
			model.addAttribute("processList",processList);
			model.addAttribute("flowList",flowList);
			model.addAttribute("reqDtmOverList",reqDtmOverList);
			model.addAttribute("docDtmOverList",docDtmOverList);
			model.addAttribute("processReqCnt",processReqCnt);
			model.addAttribute("monthProcessReqCnt",monthProcessReqCnt);
			model.addAttribute("reqDtmOverAlertList",reqDtmOverAlertList);
			
			//등록 성공 메시지 세팅
			model.addAttribute("errorYN", "N");
			model.addAttribute("message", egovMessageSource.getMessage("success.common.select"));

			return new ModelAndView("jsonView");
		}
		catch(Exception ex){
			Log.error("selectDsh2000DashBoardDataAjax()", ex);

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
	@RequestMapping(value="/dsh/dsh2000/dsh2000/selectDsh2000DashBoardSubDataAjax.do")
	public ModelAndView selectDsh2000DashBoardSubDataAjax(HttpServletRequest request, HttpServletResponse response, ModelMap model ) throws Exception {

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
    			//선택 프로젝트 단건 조회
    			Map prjInfo = dsh2000Service.selectDsh2000PrjReqAllInfo(paramMap);
    			
    			//[차트1] 프로세스별 총갯수 + 최종 완료 갯수
        		List<Map> processReqCnt = dsh1000Service.selectDsh1000ProcessReqCntList(paramMap);
        		
        		//[차트2] 월별 프로세스별 요구사항 갯수
        		List<Map> monthProcessReqCnt = dsh1000Service.selectDsh1000MonthProcessReqCntList(paramMap);
        		
        		model.addAttribute("prjInfo", prjInfo);
        		model.addAttribute("processReqCnt", processReqCnt);
        		model.addAttribute("monthProcessReqCnt", monthProcessReqCnt);
    		}
    		//계획대비 미처리 건수
    		else if("reqDtmOver".equals(redoId)){
    			//계획대비 미처리건수 조회
    			List<Map> reqDtmOverList = dsh2000Service.selectDsh2000ReqDtmOverList(paramMap);
    			
    			model.addAttribute("reqDtmOverList", reqDtmOverList);
    		}
    		//산출물 미제출 건수
    		else if("docDtmOver".equals(redoId)){
    			//산출물 제출예정일 대비 미제출 건수
    			List<Map> docDtmOverList = dsh2000Service.selectDsh2000DocDtmOverList(paramMap);
    			
    			model.addAttribute("docDtmOverList", docDtmOverList);
    		}
    		//프로세스
    		else if("process".equals(redoId)){
    			//프로세스 목록 조회
    			List<Map> processList = dsh2000Service.selectDsh2000ProcessReqRatioList(paramMap);
    			
    			//프로세스별 기간 경고 조회
    			List<Map> reqDtmOverAlertList = dsh2000Service.selectDsh2000ReqDtmOverAlertList(paramMap);
    			
    			//작업흐름 목록 조회
    			List<Map> flowList = dsh2000Service.selectDsh2000FlowList(paramMap);
    			
    			model.addAttribute("processList", processList);
    			model.addAttribute("flowList", flowList);
    			model.addAttribute("reqDtmOverAlertList", reqDtmOverAlertList);
    		}

			//등록 성공 메시지 세팅
			model.addAttribute("errorYN", "N");
			model.addAttribute("message", egovMessageSource.getMessage("success.common.select"));

			return new ModelAndView("jsonView");
		}
		catch(Exception ex){
			Log.error("selectDsh2000DashBoardSubDataAjax()", ex);

			//조회실패 메시지 세팅 및 저장 성공여부 세팅
			model.addAttribute("errorYN", "Y");
			model.addAttribute("message", egovMessageSource.getMessage("fail.common.select"));
			return new ModelAndView("jsonView");
		}
	}
	
	/**
	 * Dsh2000 계획대비 미처리 건수 요구사항 목록을 조회한다.
	 * 계획대비 미처리 건수 차트 클릭 시 나오는 팝업에 사용
	 * @param 
	 * @return 
	 * @exception Exception
	 */
	@SuppressWarnings("rawtypes")
	@RequestMapping(value="/dsh/dsh2000/dsh2000/selectDsh2000ReqDtmOverAlertListAjax.do")
	public ModelAndView selectDsh2000ReqDtmOverAlertListAjax(@ModelAttribute("dsh2000VO") Dsh2000VO dsh2000VO, HttpServletRequest request, HttpServletResponse response, ModelMap model)	throws Exception {
		
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
			dsh2000VO.setPageIndex(_pageNo);
			dsh2000VO.setPageSize(_pageSize);
			dsh2000VO.setPageUnit(_pageSize);
			
			dsh2000VO.setPrjId((String) request.getSession().getAttribute("selPrjId"));
        	
    		PaginationInfo paginationInfo = PagingUtil.getPaginationInfo(dsh2000VO);  /** paging - 신규방식 */
        	
    		List dsh2000ReqTimeOverList = null;

			HttpSession ss = request.getSession();
			LoginVO loginVO = (LoginVO) ss.getAttribute("loginVO");
			dsh2000VO.setLicGrpId(loginVO.getLicGrpId());

			// 총 건수
		    int totCnt = dsh2000Service.selectDsh2000ProcessReqDtmOverListCnt(dsh2000VO);
		    paginationInfo.setTotalRecordCount(totCnt);
			
    		// 프로세스별 계획대비 미처리건수 요구사항 목록 조회
		    dsh2000ReqTimeOverList = dsh2000Service.selectDsh2000ProcessReqDtmOverList(dsh2000VO);
		    
		    model.addAttribute("list", dsh2000ReqTimeOverList);
		    
		    //페이지 정보 보내기
			Map<String, Integer> pageMap = new HashMap<String, Integer>();
			pageMap.put("pageNo",dsh2000VO.getPageIndex());
			pageMap.put("listCount", dsh2000ReqTimeOverList.size());
			pageMap.put("totalPages", paginationInfo.getTotalPageCount());
			pageMap.put("totalElements", totCnt);
			pageMap.put("pageSize", _pageSize);

			model.addAttribute("page", pageMap);
        	
        	// 조회성공여부 및 조회성공메시지 세팅
			model.addAttribute("errorYn", "N");
        	model.addAttribute("message", egovMessageSource.getMessage("success.common.select"));
        	
        	return new ModelAndView("jsonView", model);
    	}catch(Exception ex){
    		
    		// 조회실패 여부 및 조회실패 메시지 세팅
    		model.addAttribute("errorYn", "Y");
    		model.addAttribute("message", egovMessageSource.getMessage("fail.common.select"));
    		return new ModelAndView("jsonView", model);
    	}
	}	
}
