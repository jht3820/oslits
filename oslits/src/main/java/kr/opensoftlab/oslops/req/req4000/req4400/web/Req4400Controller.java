
package kr.opensoftlab.oslops.req.req4000.req4400.web;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import kr.opensoftlab.oslops.com.fms.web.service.FileMngService;
import kr.opensoftlab.oslops.com.vo.LoginVO;
import kr.opensoftlab.oslops.req.req4000.req4400.service.Req4400Service;
import kr.opensoftlab.oslops.req.req4000.req4400.vo.Req4400VO;
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
import egovframework.com.cmm.service.EgovFileMngService;
import egovframework.com.cmm.service.EgovFileMngUtil;
import egovframework.rte.fdl.cmmn.trace.LeaveaTrace;
import egovframework.rte.fdl.idgnr.EgovIdGnrService;
import egovframework.rte.fdl.property.EgovPropertyService;
import egovframework.rte.ptl.mvc.tags.ui.pagination.PaginationInfo;

/**
 * @Class Name : Req4400Controller.java
 * @Description : Req4400Controller Controller class
 * @Modification Information
 *
 * @author 진주영
 * @since 2018.09.11.
 * @version 1.0
 * @see
 * 
 * 
 *  Copyright (C) OpenSoftLab Corp All right reserved.
 */

@Controller
public class Req4400Controller {

	/** 로그4j 로거 로딩 */
	private final Logger Log = Logger.getLogger(this.getClass());

	/** Req4400Service DI */
	@Resource(name = "req4400Service")
	private Req4400Service req4400Service;

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
	 * Req4400 요구사항 작업 목록 조회
	 * @param 
	 * @return 
	 * @exception Exception
	 */
	@SuppressWarnings({ "rawtypes", "unchecked" })
	@RequestMapping(value="/req/req4000/req4400/selectReq4400ReqWorkListAjax.do")
	public ModelAndView selectReq4400ReqWorkListAjax(@ModelAttribute("req4400VO") Req4400VO req4400VO, HttpServletRequest request, HttpServletResponse response, ModelMap model ) throws Exception {

		try{
			// request 파라미터를 map으로 변환
			Map<String, String> paramMap = RequestConvertor.requestParamToMapAddSelInfo(request, true);
			
			// 로그인 VO를 가져온다.
			HttpSession ss = request.getSession();
			LoginVO loginVO = (LoginVO) ss.getAttribute("loginVO");
						
			// 라이선스 그룹 ID, 프로젝트 ID 세팅
			req4400VO.setLicGrpId(loginVO.getLicGrpId());
						
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
				prjId = (String) request.getSession().getAttribute("selPrjId");
			}
			
			req4400VO.setPrjId(prjId);
			
			//현재 페이지 값, 보여지는 개체 수
			String _pageNo_str = paramMap.get("pageNo");
			String _pageSize_str = paramMap.get("pageSize");
					
			int _pageNo = 1;
			int _pageSize = 10;//OslAgileConstant.pageSize;
						
			// 넘어온 페이지 정보가 있다면 해당 값으로 세팅
			if(_pageNo_str != null && !"".equals(_pageNo_str)){
				_pageNo = Integer.parseInt(_pageNo_str)+1;  
			}
			if(_pageSize_str != null && !"".equals(_pageSize_str)){
				_pageSize = Integer.parseInt(_pageSize_str);  
			}
			
			// 페이지 번호, 페이지 사이즈 세팅
			req4400VO.setPageIndex(_pageNo);
			req4400VO.setPageSize(_pageSize);
			req4400VO.setPageUnit(_pageSize);
			
			PaginationInfo paginationInfo = PagingUtil.getPaginationInfo(req4400VO);  /** paging - 신규방식 */
			
			// 총 데이터 건수
			int totCnt = 0;
			
			// 총데이터 건수
			totCnt = req4400Service.selectReq4400ReqWorkListCnt(req4400VO);
			
			// 총 데이터 건수 세팅
			paginationInfo.setTotalRecordCount(totCnt);
			
			// 작업 목록 조회
			List<Map> workList = (List<Map>)req4400Service.selectReq4400ReqWorkList(req4400VO);
			
			//작업 목록 세팅
			model.addAttribute("list", workList);
			
			//페이지 정보 보내기
			Map<String, Integer> pageMap = new HashMap<String, Integer>();
			pageMap.put("pageNo",req4400VO.getPageIndex());
			pageMap.put("listCount", workList.size());
			pageMap.put("totalPages", paginationInfo.getTotalPageCount());
			pageMap.put("totalElements", totCnt);
			pageMap.put("pageSize", _pageSize);
			
			// 페이지 정보 세팅
			model.addAttribute("page", pageMap);
			
			//등록 성공 메시지 세팅
			model.addAttribute("errorYn", "N");
			model.addAttribute("message", egovMessageSource.getMessage("success.common.select"));

			return new ModelAndView("jsonView");
		}
		catch(Exception ex){
			Log.error("selectReq4400ReqWorkListAjax()", ex);

			//조회실패 메시지 세팅 및 저장 성공여부 세팅
			model.addAttribute("errorYn", "Y");
			model.addAttribute("message", egovMessageSource.getMessage("fail.common.select"));
			return new ModelAndView("jsonView");
		}
	}
	
	/**
	 * Req4400 요구사항 작업정보 추가&수정
	 * @param 
	 * @return 
	 * @exception Exception
	 */
	@RequestMapping(value="/req/req4000/req4400/saveReq4400ReqWorkInfoAjax.do")
	public ModelAndView saveReq4400ReqWorkInfoAjax(HttpServletRequest request, HttpServletResponse response, ModelMap model ) throws Exception {

		try{
			// request 파라미터를 map으로 변환
			Map<String, String> paramMap = RequestConvertor.requestParamToMapAddSelInfo(request, true);
			paramMap.put("prjId", (String)paramMap.get("selPrjId"));
			
			String type = (String) paramMap.get("type");
			
			//추가
			if("insert".equals(type)){
				req4400Service.insertReq4400ReqWorkInfoAjax(paramMap);
			}
			//수정
			else if("update".equals(type)){
				req4400Service.updateReq4400ReqWorkInfoAjax(paramMap);
			}
			
			//등록 성공 메시지 세팅
			model.addAttribute("errorYn", "N");
			model.addAttribute("message", egovMessageSource.getMessage("success.common.save"));

			return new ModelAndView("jsonView");
		}
		catch(Exception ex){
			Log.error("saveReq4400ReqWorkInfoAjax()", ex);

			//조회실패 메시지 세팅 및 저장 성공여부 세팅
			model.addAttribute("errorYn", "Y");
			model.addAttribute("message", egovMessageSource.getMessage("fail.common.save"));
			return new ModelAndView("jsonView");
		}
	}
	
	/**
	 * Req4400 요구사항 작업정보 삭제
	 * @param 
	 * @return 
	 * @exception Exception
	 */
	@RequestMapping(value="/req/req4000/req4400/deleteReq4400ReqWorkInfoAjax.do")
	public ModelAndView deleteReq4400ReqWorkInfoAjax(HttpServletRequest request, HttpServletResponse response, ModelMap model ) throws Exception {
		
		try{
			// request 파라미터를 map으로 변환
			Map<String, String> paramMap = RequestConvertor.requestParamToMapAddSelInfo(request, true);
			paramMap.put("prjId", (String)paramMap.get("selPrjId"));
			
			req4400Service.deleteReq4400ReqWorkInfoAjax(paramMap);
			
			//등록 성공 메시지 세팅
			model.addAttribute("errorYn", "N");
			model.addAttribute("message", egovMessageSource.getMessage("success.common.delete"));
			
			return new ModelAndView("jsonView");
		}
		catch(Exception ex){
			Log.error("deleteReq4400ReqWorkInfoAjax()", ex);
			
			//조회실패 메시지 세팅 및 저장 성공여부 세팅
			model.addAttribute("errorYn", "Y");
			model.addAttribute("message", egovMessageSource.getMessage("fail.common.delete"));
			return new ModelAndView("jsonView");
		}
	}
}
