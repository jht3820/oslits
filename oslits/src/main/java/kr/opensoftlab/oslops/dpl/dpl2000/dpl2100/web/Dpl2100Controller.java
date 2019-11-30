package kr.opensoftlab.oslops.dpl.dpl2000.dpl2100.web;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import kr.opensoftlab.oslops.com.vo.LoginVO;
import kr.opensoftlab.oslops.dpl.dpl2000.dpl2100.service.Dpl2100Service;
import kr.opensoftlab.oslops.dpl.dpl2000.dpl2100.vo.Dpl2100VO;
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

import egovframework.com.cmm.EgovMessageSource;
import egovframework.rte.fdl.property.EgovPropertyService;
import egovframework.rte.ptl.mvc.tags.ui.pagination.PaginationInfo;

/**
 * @Class Name : Dpl2100Controller.java
 * @Description : Dpl2100Controller Controller class
 * @Modification Information
 *
 * @author 진주영
 * @since 2019.03.11
 * @version 1.0
 * @see
 *  
 *  --------------------------------------
 *  수정일			수정자			수정내용
 *  --------------------------------------
 *  
 *  
 *  --------------------------------------
 *  
 *  Copyright (C)  All right reserved.
 */

@Controller
public class Dpl2100Controller {

	/**
     * Logging 을 위한 선언
     * Log는 반드시 가이드에 맞춰서 작성한다. 개발용으로는 debug 카테고리를 사용한다.
     */
	protected Logger Log = Logger.getLogger(this.getClass());
	
	/** System Property 를 사용하기 위한 Bean Injection */
	@Resource(name = "propertiesService")
	protected EgovPropertyService propertiesService;
	
	/** EgovMessageSource */
	@Resource(name = "egovMessageSource")
	EgovMessageSource egovMessageSource;
    
    /** Dpl2100Service DI */
    @Resource(name = "dpl2100Service")
    private Dpl2100Service dpl2100Service;
    
	@Value("${Globals.fileStorePath}")
	private String tempPath;
    
    
    /**
	 * Dpl2100  배포 계획 정보 조회
	 * @param 
	 * @return 
	 * @exception Exception
	 */
	@RequestMapping(value="/dpl/dpl2000/dpl2100/selectDpl2100View.do")
    public String selectDpl2100View(HttpServletRequest request, HttpServletResponse response, ModelMap model ) throws Exception {
    	return "/dpl/dpl2000/dpl2100/dpl2100";
    }
	
	/**
	 * Dpl2100  배포 계획 결재 승인 팝업
	 * @param 
	 * @return 
	 * @exception Exception
	 */
	@RequestMapping(value="/dpl/dpl2000/dpl2100/selectDpl2101View.do")
	public String selectDpl2101View(HttpServletRequest request, HttpServletResponse response, ModelMap model ) throws Exception {
		return "/dpl/dpl2000/dpl2100/dpl2101";
	}
	
	/**
	 * Dpl2100  배포 계획 결재 반려 팝업
	 * @param 
	 * @return 
	 * @exception Exception
	 */
	@RequestMapping(value="/dpl/dpl2000/dpl2100/selectDpl2102View.do")
	public String selectDpl2102View(HttpServletRequest request, HttpServletResponse response, ModelMap model ) throws Exception {
		return "/dpl/dpl2000/dpl2100/dpl2102";
	}
	
	/**
	 * Dpl2100 배포계획 목록 AJAX 조회
	 * @param request
	 * @param response
	 * @throws Exception
	 */
	@SuppressWarnings("unchecked")
	@RequestMapping(value="/dpl/dpl2000/dpl2100/selectDpl2100AjaxView.do")
	public ModelAndView selectDpl2000AjaxView(@ModelAttribute("dpl2100VO") Dpl2100VO dpl2100VO, HttpServletRequest request, HttpServletResponse response, ModelMap model )	throws Exception {

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
			dpl2100VO.setPageIndex(_pageNo);
			dpl2100VO.setPageSize(_pageSize);
			dpl2100VO.setPageUnit(_pageSize);
						
			PaginationInfo paginationInfo = PagingUtil.getPaginationInfo(dpl2100VO); /** paging - 신규방식 */

			List<Dpl2100VO> dpl2100List = null;

			HttpSession ss = request.getSession();
			String prjId	= (String) ss.getAttribute("selPrjId");
			String licGrpId	= (String) paramMap.get("licGrpId");
			String usrId = ((LoginVO) ss.getAttribute("loginVO")).getUsrId();

			dpl2100VO.setPrjId(prjId);
			dpl2100VO.setLicGrpId(licGrpId);
			
			//로그인한 사용자 결재 요청 현황만 가져오기
			dpl2100VO.setDpl2000LoginUsrIdChk(usrId);
			
			int totCnt = 0;
			dpl2100List = dpl2100Service.selectDpl2100SignList(dpl2100VO);

			/** 총 데이터의 건수 를 가져온다. */
			totCnt = dpl2100Service.selectDpl2100SignListCnt(dpl2100VO);
			paginationInfo.setTotalRecordCount(totCnt);

			//페이지 정보 보내기
			Map<String, Integer> pageMap = new HashMap<String, Integer>();
			pageMap.put("pageNo",dpl2100VO.getPageIndex());
			pageMap.put("listCount", dpl2100List.size());
			pageMap.put("totalPages", paginationInfo.getTotalPageCount());
			pageMap.put("totalElements", totCnt);
			pageMap.put("pageSize", _pageSize);

			model.addAttribute("selectYN", "Y");
			model.addAttribute("page", pageMap);
			model.addAttribute("list", dpl2100List); 			/** 조회 목록 List 형태로 화면에 Return 한다. */

			return new ModelAndView("jsonView");

		}catch(Exception ex){
			Log.error("selectDpl2000AjaxView()", ex);
			//조회실패 메시지 세팅 및 저장 성공여부 세팅
			model.addAttribute("selectYN", "N");
			model.addAttribute("message", egovMessageSource.getMessage("fail.common.select"));
			return new ModelAndView("jsonView");
		}
	}	
	
	/**
	 * Dpl2100 결재 승인 & 반려
	 * @param 
	 * @return 
	 * @exception Exception
	 */
	@RequestMapping(value="/dpl/dpl2000/dpl2100/insertDpl2100SignActionAjax.do")
	public ModelAndView insertDpl2100SignActionAjax(HttpServletRequest request, HttpServletResponse response, ModelMap model ) throws Exception {

		try{
			// request 파라미터를 map으로 변환
			Map<String, String> paramMap = RequestConvertor.requestParamToMapAddSelInfo(request, true);
			paramMap.put("prjId", (String)paramMap.get("selPrjId"));
        	
			//결재 등록
			dpl2100Service.insertDpl2100DplSignInfo(paramMap);
			
			//등록 성공 메시지 세팅
			model.addAttribute("errorYn", "N");
			model.addAttribute("message", egovMessageSource.getMessage("success.common.insert"));

			return new ModelAndView("jsonView");
		}
		catch(Exception ex){
			Log.error("insertDpl2100SignActionAjax()", ex);

			//조회실패 메시지 세팅 및 저장 성공여부 세팅
			model.addAttribute("errorYn", "Y");
			model.addAttribute("message", egovMessageSource.getMessage("fail.common.insert"));
			return new ModelAndView("jsonView");
		}
	}
}
