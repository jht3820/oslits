package kr.opensoftlab.oslops.arm.arm1000.arm1000.web;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import kr.opensoftlab.oslops.arm.arm1000.arm1000.service.Arm1000Service;
import kr.opensoftlab.oslops.arm.arm1000.arm1000.vo.Arm1000VO;
import kr.opensoftlab.oslops.com.vo.LoginVO;
import kr.opensoftlab.oslops.req.req4000.req4100.service.Req4100Service;
import kr.opensoftlab.oslops.req.req4000.req4100.vo.Req4100VO;
import kr.opensoftlab.sdf.util.OslAgileConstant;
import kr.opensoftlab.sdf.util.PagingUtil;
import kr.opensoftlab.sdf.util.RequestConvertor;

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

/**
 * @Class Name : Arm1000Controller.java
 * @Description : Arm1000Controller Controller class
 * @Modification Information
 *
 * @author 진주영
 * @since 2018.01.05.
 * @version 1.0
 * @see
 *  
 *  Copyright (C) OpenSoftLab Corp All right reserved.
 */

@Controller
public class Arm1000Controller {

	/** 로그4j 로거 로딩 */
	private static final Logger Log = Logger.getLogger(Arm1000Controller.class);
	
	/** Arm1000Service DI */
    @Resource(name = "arm1000Service")
    private Arm1000Service arm1000Service;
    
    /** Req4100Service DI */
	@Resource(name = "req4100Service")
	private Req4100Service req4100Service;
	
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
	 * Arm1000 화면 이동(쪽지 목록 화면)
	 * @param 
	 * @return 
	 * @exception Exception
	 */
	@RequestMapping(value="/arm/arm1000/arm1000/selectArm1000View.do")
    public String selectArm1000View(HttpServletRequest request, HttpServletResponse response, ModelMap model ) throws Exception {
		try{
			
			// request 파라미터를 map으로 변환
			Map<String, String> paramMap = RequestConvertor.requestParamToMapAddSelInfo(request, true);
			
			//읽지 않은 쪽지 검색
			String viewChk = (String)paramMap.get("viewChk");
			model.addAttribute("viewChk", viewChk);
			
			//쪽지 보내기 
			String sendChk = (String)paramMap.get("sendChk");
			String usrIdChk = (String)paramMap.get("usrIdChk");
			String arm_reqId = (String)paramMap.get("arm_reqId");
			String arm_reqNm = (String)paramMap.get("arm_reqNm");
			String reqPrjId = (String)paramMap.get("reqPrjId");
			model.addAttribute("sendChk", sendChk);
			model.addAttribute("usrIdChk", usrIdChk);
			model.addAttribute("arm_reqId", arm_reqId);
			model.addAttribute("arm_reqNm", arm_reqNm);
			model.addAttribute("reqPrjId", reqPrjId);
			
        	return "/arm/arm1000/arm1000/arm1000";
		}catch(Exception ex){
			Log.error("selectArm1000View()", ex);
    		throw new Exception(ex.getMessage());
		}
    }
	
	/**
	 * Arm1000 쪽지 목록 조회 AJAX
	 * @param 
	 * @return 
	 * @exception Exception
	 */
	@SuppressWarnings({ "unchecked", "rawtypes" })
	@RequestMapping(value="/arm/arm1000/arm1000/selectArm1000ListAjax.do")
	public ModelAndView selectArm1000ListAjax(@ModelAttribute("arm1000VO") Arm1000VO arm1000VO, HttpServletRequest request, HttpServletResponse response, ModelMap model ) throws Exception {

		try{
			// request 파라미터를 map으로 변환
			Map<String, String> paramMap = RequestConvertor.requestParamToMapAddSelInfo(request, true);
			HttpSession ss = request.getSession();
			LoginVO loginVO = (LoginVO) ss.getAttribute("loginVO");
			
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
			arm1000VO.setPageIndex(_pageNo);
			arm1000VO.setPageSize(_pageSize);
			arm1000VO.setPageUnit(_pageSize);
			
			PaginationInfo paginationInfo = PagingUtil.getPaginationInfo(arm1000VO);  /** paging - 신규방식 */

			List<Arm1000VO> arm1000List = null;
			
			//유저 아이디 세팅
			arm1000VO.setUsrId(loginVO.getUsrId());
			paramMap.put("usrId", loginVO.getUsrId());

			/** 총 데이터의 건수 를 가져온다. */
			Map armCntMap = arm1000Service.selectArm1000AlarmCnt(paramMap);
			
			String allCnt = String.valueOf(armCntMap.get("allCnt"));
			int totCnt = Integer.parseInt(allCnt);
			paginationInfo.setTotalRecordCount(totCnt);
			
			
			
			arm1000List = arm1000Service.selectArm1000AlarmList(arm1000VO);

			model.addAttribute("list", arm1000List);

			//페이지 정보 보내기
			Map<String, Integer> pageMap = new HashMap<String, Integer>();
			pageMap.put("pageNo",arm1000VO.getPageIndex());
			pageMap.put("listCount", arm1000List.size());
			pageMap.put("totalPages", paginationInfo.getTotalPageCount());
			pageMap.put("totalElements", totCnt);
			pageMap.put("pageSize", _pageSize);

			model.addAttribute("page", pageMap);
			
			//등록 성공 결과 값
			model.addAttribute("result", "ok");
			return new ModelAndView("jsonView");
		}
		catch(Exception ex){
			Log.error("selectArm1000ListAjax()", ex);
			//등록 실패 결과 값
			model.addAttribute("result", "fail");
			return new ModelAndView("jsonView");
		}
	}
	
	/**
	 * Arm1000 쪽지 수정(읽음 또는 삭제) Ajax
	 * @param 
	 * @return 
	 * @exception Exception
	 */
	@SuppressWarnings("rawtypes")
	@RequestMapping(value="/arm/arm1000/arm1000/updateArm1000AlarmInfo.do")
	public ModelAndView updateArm1000AlarmInfo(@ModelAttribute("arm1000VO") Arm1000VO arm1000VO, HttpServletRequest request, HttpServletResponse response, ModelMap model ) throws Exception {

		try{
			// request 파라미터를 map으로 변환
			Map<String, Object> paramMap = RequestConvertor.requestParamToMapAddSelInfoList(request, true,"armId");
			
			arm1000Service.updateArm1000AlarmInfo(paramMap);
			
			//세션에 선택한 메뉴 정보 담기
    		HttpSession ss = request.getSession();
    		LoginVO loginVo = (LoginVO)ss.getAttribute("loginVO");
    		
    		Map<String, String> tempMap = new HashMap<String,String>();
    		//사용자 Id 가져오기
    		String usrId = loginVo.getUsrId();
    		tempMap.put("usrId", usrId);
    		
			Map alarmCnt = arm1000Service.selectArm1000AlarmCnt(tempMap);
			
			model.addAttribute("alarmCnt", alarmCnt);
			
			//등록 성공 결과 값
			model.addAttribute("errorYn", "N");
			model.addAttribute("message", egovMessageSource.getMessage("success.common.update"));
			return new ModelAndView("jsonView");
		}
		catch(Exception ex){
			Log.error("updateArm1000AlarmInfo()", ex);
			//등록 실패 결과 값
			model.addAttribute("errorYn", "Y");
			model.addAttribute("message", egovMessageSource.getMessage("fail.common.update"));
			return new ModelAndView("jsonView");
		}
	}
	
	/**
	 * Arm1000 쪽지 내용 불러오기 Ajax
	 * @param 
	 * @return 
	 * @exception Exception
	 */
	@SuppressWarnings({ "rawtypes" })
	@RequestMapping(value="/arm/arm1000/arm1000/selectArm1000InfoAjax.do")
	public ModelAndView selectArm1000InfoAjax(HttpServletRequest request, HttpServletResponse response, ModelMap model ) throws Exception {

		try{
			// request 파라미터를 map으로 변환
			Map<String, String> paramMap = RequestConvertor.requestParamToMapAddSelInfo(request, true);
			
			HttpSession ss = request.getSession();
			LoginVO loginVO = (LoginVO) ss.getAttribute("loginVO");
			String usrId = loginVO.getUsrId();
			
			//읽음 처리 확인
			String viewCheck = (String)paramMap.get("viewCheck");
			
			//사용자 Id넣기
			paramMap.put("usrId", usrId);
			
			//읽지 않음 상태인경우 읽음 처리
			if("02".equals(viewCheck)){
				Map<String, Object> tempMap = new HashMap<String,Object>();
				tempMap.put("viewCheck", "01");
				tempMap.put("armId", paramMap.get("armId"));
				tempMap.put("usrId", usrId);
				arm1000Service.updateArm1000AlarmInfo(tempMap);
				
				Map alarmCnt = arm1000Service.selectArm1000AlarmCnt(paramMap);
				
				
				model.addAttribute("alarmCnt", alarmCnt);
				
				//읽음 처리 전송
				model.addAttribute("viewAction", "01");
			}else{
				model.addAttribute("viewAction", "02");
			}
			
			//쪽지 내용 조회
			Map armInfo = arm1000Service.selectArm1000AlarmInfo(paramMap);
			
			model.addAttribute("armInfo", armInfo);
			
			//조회 성공 결과 값
			model.addAttribute("errorYn", "N");
			model.addAttribute("message", egovMessageSource.getMessage("success.common.select"));
			return new ModelAndView("jsonView");
		}
		catch(Exception ex){
			Log.error("selectArm1000ListAjax()", ex);
			//조회 실패 결과 값
			model.addAttribute("errorYn", "Y");
			model.addAttribute("message", egovMessageSource.getMessage("fail.common.select"));
			return new ModelAndView("jsonView");
		}
	}
	
	/**
	 * Arm1000 쪽지 작성 Ajax
	 * @param 
	 * @return 
	 * @exception Exception
	 */
	@RequestMapping(value="/arm/arm1000/arm1000/insertArm1000InfoAjax.do")
	public ModelAndView insertArm1000InfoAjax(HttpServletRequest request, HttpServletResponse response, ModelMap model ) throws Exception {

		try{
			// request 파라미터를 map으로 변환
			Map<String, String> paramMap = RequestConvertor.requestParamToMapAddSelInfo(request, true);
			
			HttpSession ss = request.getSession();
			LoginVO loginVO = (LoginVO) ss.getAttribute("loginVO");
			String usrId = loginVO.getUsrId();
			paramMap.put("sendUsrId", usrId);
			
			//쪽지 등록
			arm1000Service.insertArm1000AlarmInfo(paramMap);
			
			//등록 성공 결과 값
			model.addAttribute("errorYn", "N");
			model.addAttribute("message", egovMessageSource.getMessage("success.common.insert"));
			return new ModelAndView("jsonView");
		}
		catch(Exception ex){
			Log.error("insertArm1000InfoAjax()", ex);
			//등록 실패 결과 값
			model.addAttribute("errorYn", "Y");
			model.addAttribute("message", egovMessageSource.getMessage("fail.common.insert"));
			return new ModelAndView("jsonView");
		}
	}
	
	/**
	 * Arm1000 요구사항 목록 (자동완성) AJAX
	 * @param 
	 * @return 
	 * @exception Exception
	 */
	@SuppressWarnings({ "unused" })
	@RequestMapping(value="/arm/arm1000/arm1000/selectArm1000AutoCompleReqAjax.do")
	public ModelAndView selectArm1000AutoCompleReqAjax(HttpServletRequest request, HttpServletResponse response, ModelMap model ) throws Exception {

		try{
			// request 파라미터를 map으로 변환
			Map<String, String> paramMap = RequestConvertor.requestParamToMapAddSelInfo(request, true);
			
			HttpSession ss = request.getSession();
			LoginVO loginVO = (LoginVO) ss.getAttribute("loginVO");
			String licGrpId = loginVO.getLicGrpId();
			String prjId = (String)ss.getAttribute("selPrjId");
			
			Req4100VO req4100vo = new Req4100VO();
			
			req4100vo.setLicGrpId(licGrpId);
			req4100vo.setPrjId(prjId);
			req4100vo.setFirstIndex(0);
			
			int reqTotalCnt = req4100Service.selectReq4100ListCnt(req4100vo);
			req4100vo.setLastIndex(reqTotalCnt);
			
			List<Req4100VO> reqList = req4100Service.selectReq4100List(req4100vo);
			model.addAttribute("reqList", reqList);
			
			//등록 성공 결과 값
			model.addAttribute("errorYn", "N");
			model.addAttribute("message", egovMessageSource.getMessage("success.common.select"));
			return new ModelAndView("jsonView");
		}
		catch(Exception ex){
			Log.error("selectArm1000AutoCompleReqAjax()", ex);
			//등록 실패 결과 값
			model.addAttribute("errorYn", "Y");
			model.addAttribute("message", egovMessageSource.getMessage("fail.common.select"));
			return new ModelAndView("jsonView");
		}
	}
}
