package kr.opensoftlab.oslops.dsh.dsh4000.dsh4000.web;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import kr.opensoftlab.oslops.adm.adm8000.adm8100.vo.Adm8100VO;
import kr.opensoftlab.oslops.com.fms.web.service.FileMngService;
import kr.opensoftlab.oslops.com.vo.LoginVO;
import kr.opensoftlab.oslops.dsh.dsh1000.dsh1000.service.Dsh1000Service;
import kr.opensoftlab.oslops.dsh.dsh4000.dsh4000.service.Dsh4000Service;
import kr.opensoftlab.oslops.dsh.dsh4000.dsh4000.vo.Dsh4000VO;
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
 * @Class Name : Dsh4000Controller.java
 * @Description : Dsh4000Controller Controller class
 * @Modification Information
 *
 * @author 공대영
 * @since 2018.10.26.
 * @version 1.0
 * @see
 *  
 *  Copyright (C) OpenSoftLab Corp All right reserved.
 */

@Controller
public class Dsh4000Controller {

	/**
     * Logging 을 위한 선언
     * Log는 반드시 가이드에 맞춰서 작성한다. 개발용으로는 debug 카테고리를 사용한다.
     */
	protected Logger Log = Logger.getLogger(this.getClass());

    
    /** EgovMessageSource */
	@Resource(name = "egovMessageSource")
	EgovMessageSource egovMessageSource;

	/** EgovPropertyService */
	@Resource(name = "propertiesService")
	protected EgovPropertyService propertiesService;

	/** FileMngService */
   	@Resource(name="fileMngService")
   	private FileMngService fileMngService;

   	@Resource(name="dsh4000Service")
   	private Dsh4000Service dsh4000Service;
	
	@Value("${Globals.fileStorePath}")
	private String tempPath;
	
	/** TRACE */
	@Resource(name = "leaveaTrace")
	LeaveaTrace leaveaTrace;
	/**
	 * 보고서 화면 이동
	 * 
	 * @param 
	 * @return 
	 * @exception Exception
	 */
	@SuppressWarnings({ "rawtypes", "unchecked" })
	@RequestMapping(value="/dsh/dsh4000/dsh4000/selectDsh4000View.do")
	public String selectDsh4000View(HttpServletRequest request, HttpServletResponse response, ModelMap model ) throws Exception {
		return "/dsh/dsh4000/dsh4000/dsh4000";
	}
	
	/**
	 * 
	 * 보고서 목록 조회
	 * 
	 * @param adm8100VO
	 * @param request
	 * @param response
	 * @param model
	 * @return
	 * @throws Exception
	 */
	@RequestMapping(value="/dsh/dsh4000/dsh4000/selectDsh4000ReportListAjax.do")
	public ModelAndView selectDsh4000ReportListAjax(@ModelAttribute("Dsh4000DAO") Dsh4000VO dsh4000VO, HttpServletRequest request, HttpServletResponse response, ModelMap model ) throws Exception {

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
			dsh4000VO.setPageIndex(_pageNo);
			dsh4000VO.setPageSize(_pageSize);
			dsh4000VO.setPageUnit(_pageSize);
			
			
			PaginationInfo paginationInfo = PagingUtil.getPaginationInfo(dsh4000VO);  /** paging - 신규방식 */

			List<Dsh4000VO> dsh4000List = null;

			HttpSession ss = request.getSession();
			LoginVO loginVO = (LoginVO) ss.getAttribute("loginVO");
			
			dsh4000VO.setLicGrpId(loginVO.getLicGrpId());
			dsh4000VO.setPrjId(ss.getAttribute("selPrjId").toString());

			/**
			 * 목록 조회
			 */
			int totCnt = 0;
			dsh4000List =  dsh4000Service.selectDsh4000ReportList(dsh4000VO);

			
			/** 총 데이터의 건수 를 가져온다. */
			totCnt =dsh4000Service.selectDsh4000ReportListCnt(dsh4000VO);
			paginationInfo.setTotalRecordCount(totCnt);
			
			model.addAttribute("list", dsh4000List);
			
			//페이지 정보 보내기
			Map<String, Integer> pageMap = new HashMap<String, Integer>();
			pageMap.put("pageNo",dsh4000VO.getPageIndex());
			pageMap.put("listCount", dsh4000List.size());
			pageMap.put("totalPages", paginationInfo.getTotalPageCount());
			pageMap.put("totalElements", totCnt);
			pageMap.put("pageSize", _pageSize);
			
			model.addAttribute("page", pageMap);
			
			return new ModelAndView("jsonView");
		}
		catch(Exception ex){
			Log.error("selectDsh4000ReportListAjax()", ex);
			throw new Exception(ex.getMessage());
		}
	}

	
	
}
