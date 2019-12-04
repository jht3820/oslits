package kr.opensoftlab.oslops.bad.bad1000.bad1000.web;

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
import egovframework.rte.ptl.mvc.tags.ui.pagination.PaginationInfo;
import kr.opensoftlab.oslops.bad.bad1000.bad1000.service.Bad1000Service;
import kr.opensoftlab.oslops.bad.bad1000.bad1000.vo.Bad1000VO;
import kr.opensoftlab.oslops.com.vo.LoginVO;
import kr.opensoftlab.sdf.util.OslAgileConstant;
import kr.opensoftlab.sdf.util.PagingUtil;
import kr.opensoftlab.sdf.util.RequestConvertor;


/**
 * @Class Name : Bad1000Controller.java
 * @Description : Bad1000Controller Controller class
 * @Modification Information
 *
 * @author 전예지
 * @since 2019.8.29.
 * @version 1.0
 * @see
 *  
 *  Copyright (C)  All right reserved.
 */

@Controller
public class Bad1000Controller {


	/** 로그4j 로거 로딩 */
	private static final Logger Log = Logger.getLogger(Bad1000Controller.class);

	/** EgovMessageSource */
	@Resource(name = "egovMessageSource")
	EgovMessageSource egovMessageSource;
	
	/** Bad1000Service DI */
	@Resource(name = "bad1000Service")
	private Bad1000Service bad1000Service;
	
	/**
	 * Bad1000 공지사항 화면으로 이동한다.
	 * @param request
	 * @param response
	 * @param model
	 * @return
	 * @throws Exception
	 */
	@RequestMapping(value="/bad/bad1000/bad1000/selectBad1000View.do")
	public String selectBad1000View(HttpServletRequest request, HttpServletResponse response, ModelMap model ) throws Exception {
		return "/bad/bad1000/bad1000/bad1000";
	}
	
	/**
	 * Bad1000 공지사항 목록을 조회한다.
	 * @param 
	 * @return 
	 * @exception Exception
	 */
	@RequestMapping(value="/bad/bad1000/bad1000/selectbad1000BoardListAjaxView.do")
	public ModelAndView selectbad1000BoardListAjaxView(@ModelAttribute("bad1000VO") Bad1000VO bad1000VO, HttpServletRequest request, HttpServletResponse response, ModelMap model ) throws Exception {

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
			
			//페이지 사이즈
			bad1000VO.setPageIndex(_pageNo);
			bad1000VO.setPageSize(_pageSize);
			bad1000VO.setPageUnit(_pageSize);
			
			
			PaginationInfo paginationInfo = PagingUtil.getPaginationInfo(bad1000VO);  /** paging - 신규방식 */

			List<Bad1000VO> bad1000List = null;

			HttpSession ss = request.getSession();
			LoginVO loginVO = (LoginVO) ss.getAttribute("loginVO");

			bad1000VO.setUsrId(loginVO.getUsrId());
			bad1000VO.setLicGrpId(loginVO.getLicGrpId());

			/**
			 * 목록 조회
			 */
			int totCnt = 0;
			bad1000List =  bad1000Service.selectbad1000BoardList(bad1000VO);

			
			/** 총 데이터의 건수 를 가져온다. */
			totCnt =bad1000Service.selectbad1000BoardListCnt(bad1000VO);
			paginationInfo.setTotalRecordCount(totCnt);
			
			model.addAttribute("list", bad1000List);
			
			//페이지 정보 보내기
			Map<String, Integer> pageMap = new HashMap<String, Integer>();
			pageMap.put("pageNo",bad1000VO.getPageIndex());
			pageMap.put("listCount", bad1000List.size());
			pageMap.put("totalPages", paginationInfo.getTotalPageCount());
			pageMap.put("totalElements", totCnt);
			pageMap.put("pageSize", _pageSize);
			
			model.addAttribute("page", pageMap);
			
			// 조회 성공여부 및 성공 메시지 세팅
			model.addAttribute("errorYn", 'N');
			model.addAttribute("message", egovMessageSource.getMessage("success.common.select"));
			
			return new ModelAndView("jsonView");
		}
		catch(Exception ex){
			Log.error("selectbad1000BoardListAjaxView()", ex);
			// 조회 실패여부 및 실패 메시지 세팅
			model.addAttribute("errorYn", 'Y');
			model.addAttribute("message", egovMessageSource.getMessage("fail.common.select"));
			return new ModelAndView("jsonView");
		}
	}
	
	/**
	 * Bad1001 공지사항  등록/수정 팝업으로 이동한다.
	 * @param request
	 * @param response
	 * @param model
	 * @return
	 * @throws Exception
	 */
	@RequestMapping(value="/bad/bad1000/bad1000/selectbad1001View.do")
	public String selectbad1001View(HttpServletRequest request, HttpServletResponse response, ModelMap model ) throws Exception {
		return "/bad/bad1000/bad1000/bad1001";
	}
	
	/**
	 * Bad1002 공지사항 상세정보 화면이동
	 * @param request
	 * @param response
	 * @param model
	 * @return
	 * @throws Exception
	 */
	@SuppressWarnings("rawtypes")
	@RequestMapping(value="/bad/bad1000/bad1000/selectbad1002View.do")
	public String selectbad1002View(HttpServletRequest request, HttpServletResponse response, ModelMap model ) throws Exception {
		try{
			
			// request 파라미터를 map으로 변환
			Map<String, String> paramMap = RequestConvertor.requestParamToMapAddSelInfo(request, true);
	
			// 공지사항 정보 단건 조회
			Map badDetailInfo = bad1000Service.selectBad1001Info(paramMap);
			model.addAttribute("badDetailInfo", badDetailInfo);
		}
		catch(Exception ex){
			Log.error("selectbad1002View()", ex);
		}
		return "/bad/bad1000/bad1000/bad1002";
	}
	

	/**
	 * Bad1000 공지사항  정보를 단건 조회한다.
	 * @param request
	 * @param response
	 * @param model
	 * @return
	 * @throws Exception
	 */
	@SuppressWarnings("rawtypes")
	@RequestMapping(value="/bad/bad1000/bad1000/selectbad1001InfoAjax.do")
	public ModelAndView selectbad1001InfoAjax(HttpServletRequest request, HttpServletResponse response, ModelMap model ) throws Exception {
		try{
			
			// request 파라미터를 map으로 변환
			Map<String, String> paramMap = RequestConvertor.requestParamToMapAddSelInfo(request, true);
	
			// 공지사항 정보 단건 조회
			Map badDetailInfo = bad1000Service.selectBad1001Info(paramMap);
			
			if(badDetailInfo != null) {
				// 조회 성공여부 
				model.addAttribute("errorYn", "N");
				model.addAttribute("badDetailInfo", badDetailInfo);
			} else {
				// 조회 실패여부 
				model.addAttribute("errorYn", "Y");
			}
	
			return new ModelAndView("jsonView");
		}
		catch(Exception ex){
			Log.error("selectbad1001InfoAjax()", ex);

			// 조회실패 세팅
			model.addAttribute("errorYn", "Y");
			return new ModelAndView("jsonView");
		}
	}
	
	
	/**
	 * bad1000 공지사항 글을 등록/수정한다.
	 * @param request
	 * @param response
	 * @param model
	 * @return
	 * @throws Exception
	 */
	@RequestMapping(value="/bad/bad1000/bad1000/saveBad1001InfoAjax.do")
	public ModelAndView saveBad1001InfoAjax(HttpServletRequest request, HttpServletResponse response, ModelMap model ) throws Exception {

		try{
			// request 파라미터를 map으로 변환
			Map<String, String> paramMap = RequestConvertor.requestParamToMapAddSelInfo(request, true);

			// 공지사항 등록/수정
			int resultCnt = bad1000Service.saveBad1001Info(paramMap);
			// 등록 또는 수정된 건 없을경우
			if(resultCnt < 1) {
				// 저장 성공여부 및 저장성공 메시지 세팅
				model.addAttribute("errorYn", "Y");
				model.addAttribute("message", egovMessageSource.getMessage("success.common.save"));
				return new ModelAndView("jsonView");
			}

			// 저장 성공여부 및 저장성공 메시지 세팅
			model.addAttribute("errorYn", "N");
			model.addAttribute("message", egovMessageSource.getMessage("success.common.save"));
			
			return new ModelAndView("jsonView");
		}
		catch(Exception ex){
			Log.error("saveBad1001InfoAjax()", ex);

			// 저장 실패여부 및 저장실패 메시지 세팅
			model.addAttribute("errorYn", "Y");
			model.addAttribute("message", egovMessageSource.getMessage("fail.common.save"));
			return new ModelAndView("jsonView");
		}
	}
	
	/**
	 * bad1000 공지사항 글을 삭제한다.
	 * @param request
	 * @param response
	 * @param model
	 * @return
	 * @throws Exception
	 */
	@RequestMapping(value="/bad/bad1000/bad1000/deleteBad1000InfoAjax.do")
	public ModelAndView deleteBad1000InfoAjax(HttpServletRequest request, HttpServletResponse response, ModelMap model ) throws Exception {

		try{
			// request 파라미터를 map으로 변환
			Map<String, Object> paramMap = RequestConvertor.requestParamToMapAddSelInfoList(request, true, "badId");
				
			// 공지사항 삭제
			int resultCount = bad1000Service.deleteBad1000Info(paramMap);
			// 삭제 건수 없을 경우
			if(resultCount < 1){
				// 삭제 실패여부 및 삭제실패 메시지 세팅
				model.addAttribute("errorYn", "Y");
				model.addAttribute("message", egovMessageSource.getMessage("fail.common.delete"));
				return new ModelAndView("jsonView");
			}
			
			// 삭제 성공여부 및 삭제성공 메시지 세팅
			model.addAttribute("errorYn", "N");
			model.addAttribute("message", egovMessageSource.getMessage("success.common.delete"));
			return new ModelAndView("jsonView");
		}
		catch(Exception ex){
			Log.error("deleteBad1000InfoAjax()", ex);

			// 삭제 실패여부 및 삭제실패 메시지 세팅
			model.addAttribute("errorYn", "Y");
			model.addAttribute("message", egovMessageSource.getMessage("fail.common.delete"));
			return new ModelAndView("jsonView");
		}
	}
	
}
