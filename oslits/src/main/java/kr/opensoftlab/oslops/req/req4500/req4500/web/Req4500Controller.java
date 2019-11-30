package kr.opensoftlab.oslops.req.req4500.req4500.web;

import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import kr.opensoftlab.oslops.com.fms.web.service.FileMngService;
import kr.opensoftlab.oslops.com.vo.LoginVO;
import kr.opensoftlab.oslops.req.req1000.req1000.service.Req1000Service;
import kr.opensoftlab.oslops.req.req4000.req4100.service.Req4100Service;
import kr.opensoftlab.oslops.req.req4000.req4100.vo.Req4100VO;
import kr.opensoftlab.oslops.req.req4000.req4800.service.Req4800Service;
import kr.opensoftlab.oslops.req.req4500.req4500.service.Req4500Service;
import kr.opensoftlab.sdf.util.ReqHistoryMngUtil;
import kr.opensoftlab.sdf.util.RequestConvertor;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;

import egovframework.com.cmm.EgovMessageSource;
import egovframework.com.cmm.service.EgovFileMngUtil;
import egovframework.rte.fdl.cmmn.trace.LeaveaTrace;
import egovframework.rte.fdl.idgnr.EgovIdGnrService;
import egovframework.rte.fdl.property.EgovPropertyService;

/**
 * @Class Name : Req4500Controller.java
 * @Description : Req4500Controller Controller class
 * @Modification Information
 *
 * @author 진주영
 * @since 2018.01.11.
 * @version 1.0
 * @see
 *  
 *  Copyright (C) OpenSoftLab Corp All right reserved.
 */

@Controller
public class Req4500Controller {

	/**
	 * Logging 을 위한 선언
	 * Log는 반드시 가이드에 맞춰서 작성한다. 개발용으로는 debug 카테고리를 사용한다.
	 */
	protected Logger Log = Logger.getLogger(this.getClass());

	
	/** Req4100Service DI */
	@Resource(name = "req4100Service")
	private Req4100Service req4100Service;
	
	/** Req4500Service DI */
    @Resource(name = "req4500Service")
    private Req4500Service req4500Service;
	
	/** EgovMessageSource */
	@Resource(name = "egovMessageSource")
	EgovMessageSource egovMessageSource;

	/** EgovPropertyService */
	@Resource(name = "propertiesService")
	protected EgovPropertyService propertiesService;

	/** TRACE */
	@Resource(name = "leaveaTrace")
	LeaveaTrace leaveaTrace;
		
	@Value("${Globals.fileStorePath}")
	private String tempPath;
	
	/**
	 * Req4500 화면 이동
	 * @param 
	 * @return 
	 * @exception Exception
	 */
	@RequestMapping(value="/req/req4000/req4500/selectReq4500View.do")
	public String selectReq4500View(HttpServletRequest request, HttpServletResponse response, ModelMap model ) throws Exception {
		return "/req/req4000/req4500/req4500";
	}
	
	/**
	 * Req4500 변경이력 조회 Ajax
	 * @param request
	 * @param response
	 * @param model
	 * @return
	 * @throws Exception
	 */
	@SuppressWarnings({ "rawtypes", "unchecked" })
	@RequestMapping(value="/req/req4000/req4500/selectReq4500ListAjax.do")
	public ModelAndView selectReq4500ListAjax(HttpServletRequest request, HttpServletResponse response, ModelMap model ) throws Exception {
		try{
			// request 파라미터를 map으로 변환
			Map<String, String> paramMap = RequestConvertor.requestParamToMapAddSelInfo(request, true);
			
			//현재 페이지 값, 보여지는 개체 수
			String _pageNo_str = paramMap.get("pageNo");
			String _pageSize_str = paramMap.get("pageSize");
			
			int pageNo = Integer.parseInt(_pageNo_str);
			int pageSize = Integer.parseInt(_pageSize_str);
			
			//세션
			HttpSession ss = request.getSession();
			
			//세션의 선택된 프로젝트 아이디 가져오기
			String prjId = (String) ss.getAttribute("selPrjId");
			
			//프로젝트 ID 세팅
			paramMap.put("prjId", prjId);
			
			String sel=paramMap.get("searchSelect");
			String selCd=paramMap.get("searchCd");
			List<Map> reqChgList = new ArrayList<Map>();
			//결재 정보
			List<Map> reqChkList =  new ArrayList<Map>();
			// 요구사항 수정이력 조회
			List reqChgDetailList =  new ArrayList<Map>();
			if("type".equals(sel)){
				if("01".equals(selCd)){
					reqChgList = req4500Service.selectReq4500ReqHistoryList(paramMap);
				}else if("02".equals(selCd)){
					reqChgDetailList = req4500Service.selectReq4500ChgDetailList(paramMap);
				}else if("03".equals(selCd)){
					reqChkList = req4500Service.selectReq4500ReqSignList(paramMap);
				}
			}else{
				reqChgList = req4500Service.selectReq4500ReqHistoryList(paramMap);
				
				reqChkList = req4500Service.selectReq4500ReqSignList(paramMap);
				// 요구사항 수정이력 조회				
				reqChgDetailList = req4500Service.selectReq4500ChgDetailList(paramMap);
			}
			
			List<Map> reqList = reqChgList;
					
			reqList.addAll(reqChkList);
			reqList.addAll(reqChgDetailList);
			if(!"type".equals(sel)){
			//기준 정렬 인터페이스 선언
				Comparator<Map> mapComparator = new Comparator<Map>() {
					@Override
				    public int compare(Map m1, Map m2) {
						//내림차순
				        return ((Timestamp) m2.get("regDtm")).compareTo((Timestamp) m1.get("regDtm"));
				    }
				};
				//요구사항 목록 정렬
				Collections.sort(reqList, mapComparator);
			}
						
			setServerPage(reqList, pageSize,  pageNo, model );
			
			model.addAttribute("errorYn", "N");
			model.addAttribute("message", egovMessageSource.getMessage("success.common.select"));
			return new ModelAndView("jsonView");

		}catch(Exception e){
			Log.error("selectReq4500ListAjax()", e);

			model.addAttribute("errorYn", "Y");
			model.addAttribute("message", egovMessageSource.getMessage("fail.common.select"));

			return new ModelAndView("jsonView");
		}

	}
	
	/**
	 * Req4500 화면에 요구사항 이력을 구하여 해당 수만큼 이력을 보여준다. 
	 * @param selectList 조회된 데이터 리스트
	 * @param pageSize 페이지 사이즈
	 * @param currentPage 현제 페이지
	 * @param model
	 * @return
	 * @throws Exception
	 */
	@SuppressWarnings({ "rawtypes", "unchecked" })
	public void setServerPage(List selectList,int pageSize, int currentPage, ModelMap model ){
		
		// 조회된 데이터 사이즈를 가져온다.
		int selectListSize = selectList.size();
		
		List list = new ArrayList();
		
		// 첫번째 인덱스, 마지막 인덱스를 구한다.
		int firstIndex = pageSize  * (currentPage -1);
		int lastIndex = pageSize  * currentPage ;
		
		// 더보기 버튼 표시유무
		boolean isNextView = true;
		
		// 다음 페이지 번호
		int nextPage = currentPage + 1;
		
		// 마지막 인덱스가 조회된 데이터 수보다 작을 경우
		// 마지막 인덱스 = 조회된 데이터 수, 더보기 버튼 표시하지 않음
		if(lastIndex >= selectListSize){
			lastIndex = selectListSize;
			isNextView = false;
		}

		// firstIndex, lastIndex 수만큼 데이터를 세팅
		for (int i = firstIndex; i < lastIndex; i++) {
			list.add(selectList.get(i));
		}
		
		// 세팅된 데이터 전달
		model.addAttribute("isNextView",isNextView);
		model.addAttribute("nextPage",nextPage);
		model.addAttribute("reqList",list);
	}
	
	
	@SuppressWarnings({ "rawtypes", "unchecked" })
	@RequestMapping(value="/req/req4000/req4500/selectReq4500ReqNmListAjax.do")
	public ModelAndView selectReq4500ReqNmListAjax(HttpServletRequest request, HttpServletResponse response, ModelMap model ) throws Exception {
		try{
			// request 파라미터를 map으로 변환
			Map<String, String> paramMap = RequestConvertor.requestParamToMapAddSelInfo(request, true);
			
			//세션
			HttpSession ss = request.getSession();
			
			//세션의 선택된 프로젝트 아이디 가져오기
			String prjId = (String) ss.getAttribute("selPrjId");
			
			//프로젝트 ID 세팅
			paramMap.put("prjId", prjId);
			
	
			
			LoginVO loginVO = (LoginVO) ss.getAttribute("loginVO");
			String licGrpId = loginVO.getLicGrpId();
			
			Req4100VO req4100vo = new Req4100VO();
			
			req4100vo.setLicGrpId(licGrpId);
			req4100vo.setPrjId(prjId);
			req4100vo.setFirstIndex(0);
			
			int reqTotalCnt = req4100Service.selectReq4100ListCnt(req4100vo);
			req4100vo.setLastIndex(reqTotalCnt);
			
			List<Req4100VO> reqAllList = req4100Service.selectReq4100List(req4100vo);
			model.addAttribute("reqAllList", reqAllList);
			
			//데이터 전달

			model.addAttribute("errorYn", "N");
			model.addAttribute("message", egovMessageSource.getMessage("success.common.select"));
			return new ModelAndView("jsonView");

		}catch(Exception e){
			Log.error("selectReq4500ReqNmListAjax()", e);

			model.addAttribute("errorYn", "Y");
			model.addAttribute("message", egovMessageSource.getMessage("fail.common.select"));

			return new ModelAndView("jsonView");
		}

	}
}
