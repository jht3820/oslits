package kr.opensoftlab.oslits.dsh.dsh2000.dsh2000.web;

import java.util.List;
import java.util.Map;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import kr.opensoftlab.oslits.cmm.cmm4000.cmm4000.service.Cmm4000Service;
import kr.opensoftlab.oslits.com.fms.web.service.FileMngService;
import kr.opensoftlab.oslits.com.vo.LoginVO;
import kr.opensoftlab.oslits.dsh.dsh1000.dsh1000.service.Dsh1000Service;
import kr.opensoftlab.oslits.dsh.dsh2000.dsh2000.service.Dsh2000Service;
import kr.opensoftlab.oslits.prj.prj1000.prj1000.service.Prj1000Service;
import kr.opensoftlab.oslits.req.req1000.req1000.service.Req1000Service;
import kr.opensoftlab.oslits.req.req1000.req1000.vo.Req1000VO;
import kr.opensoftlab.oslits.req.req4000.req4100.service.Req4100Service;
import kr.opensoftlab.oslits.req.req4000.req4400.service.Req4400Service;
import kr.opensoftlab.sdf.util.RequestConvertor;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;

import egovframework.com.cmm.EgovMessageSource;
import egovframework.rte.fdl.cmmn.trace.LeaveaTrace;
import egovframework.rte.fdl.property.EgovPropertyService;

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
    
    /** Cmm4000Service DI */
    @Resource(name = "cmm4000Service")
    private Cmm4000Service cmm4000Service;

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
    		
    		Req1000VO req1000Vo = new Req1000VO();
			req1000Vo.setPrjId(prjId);
			req1000Vo.setLoginUsrId(usrId);
			req1000Vo.setReqProType("01"); //접수 요청중 요구사항만
			
			//해당 프로젝트 접수 대기 목록
			List<Req1000VO> newReqList = req1000Service.selectReq1000AllList(req1000Vo);
			
			//담당 작업 목록
			List<Map> workList = req4400Service.selectReq4400ReqChargerWorkList(paramMap);
			
			//담당 결재 대기 목록
			//reqId제거하고 호출
			paramMap.remove("reqId");
			paramMap.put("signUsrId", usrId);
			List<Map> signList = req4100Service.selectReq4900ReqSignList(paramMap);
			
			model.addAttribute("prjInfo",prjInfo);
			model.addAttribute("processList",processList);
			model.addAttribute("flowList",flowList);
			model.addAttribute("reqDtmOverList",reqDtmOverList);
			model.addAttribute("docDtmOverList",docDtmOverList);
			model.addAttribute("processReqCnt",processReqCnt);
			model.addAttribute("monthProcessReqCnt",monthProcessReqCnt);
			model.addAttribute("reqDtmOverAlertList",reqDtmOverAlertList);
			model.addAttribute("newReqList", newReqList);
			model.addAttribute("workList", workList);
			model.addAttribute("signList", signList);
			
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
    		//작업 목록
    		else if("work".equals(redoId)){
    			//담당 작업 목록
    			List<Map> workList = req4400Service.selectReq4400ReqChargerWorkList(paramMap);
    			
    			model.addAttribute("workList", workList);
    		}
    		//결재 목록
    		else if("sign".equals(redoId)){
    			//담당 결재 대기 목록
    			//reqId제거하고 호출
    			paramMap.remove("reqId");
    			paramMap.put("signUsrId", usrId);
    			List<Map> signList = req4100Service.selectReq4900ReqSignList(paramMap);
    			
    			model.addAttribute("signList", signList);
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
}
