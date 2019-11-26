package kr.opensoftlab.oslits.cmm.cmm1000.cmm1600.web;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import kr.opensoftlab.oslits.cmm.cmm1000.cmm1000.service.Cmm1000Service;
import kr.opensoftlab.oslits.cmm.cmm1000.cmm1000.vo.Cmm1000VO;
import kr.opensoftlab.oslits.cmm.cmm1000.cmm1600.service.Cmm1600Service;
import kr.opensoftlab.oslits.cmm.cmm1000.cmm1600.vo.Cmm1600VO;
import kr.opensoftlab.oslits.com.vo.LoginVO;
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
import egovframework.rte.fdl.property.EgovPropertyService;
import egovframework.rte.ptl.mvc.tags.ui.pagination.PaginationInfo;


/**
 * @Class Name : Cmm1600Controller.java
 * @Description : 배포 버젼 컨트롤러 클래스
 * @Modification Information
 * <pre>
 *   수정일      수정자          수정내용
 *  -------    --------    ---------------------------
 *  2018.08.01  공대영          최초 생성
 *  
 * </pre>
 *  @author 공대영
 *  @version 1.0
 *  @see 
 *  
 *  Copyright (C) Open Soft Lab Corp. All Rights Reserved
 */
@Controller
public class Cmm1600Controller {
	
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
	
	@Resource(name = "cmm1600Service")
	Cmm1600Service cmm1600Service;
	
	
	
	
	/**
	 *
	 * 배포 조회 공통 팝업 화면 이동
	 * 
	 * @param request
	 * @param response
	 * @param model
	 * @return
	 * @throws Exception
	 */
	@RequestMapping(value="/cmm/cmm1000/cmm1600/selectCmm1600View.do")
	public String selectCmm1600View(HttpServletRequest request, HttpServletResponse response, ModelMap model ) throws Exception {
			return "/cmm/cmm1000/cmm1600/cmm1600";
	}	

	
	

	/**
	 * 
	 * 배포 정보 조회 공통 목록 조회
	 * 
	 * @param cmm1600VO
	 * @param request
	 * @param response
	 * @param model
	 * @return
	 * @throws Exception
	 */
	@RequestMapping(value="/cmm/cmm1000/cmm1600/selectCmm1600CommonDplListAjax.do")
	public ModelAndView selectCmm1600CommonDplListAjax(@ModelAttribute("cmm1600VO") Cmm1600VO cmm1600VO, HttpServletRequest request, HttpServletResponse response, ModelMap model)	throws Exception {
		
    	try{
    		//리퀘스트에서 넘어온 파라미터를 맵으로 세팅
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
			cmm1600VO.setPageIndex(_pageNo);
			cmm1600VO.setPageSize(_pageSize);
			cmm1600VO.setPageUnit(_pageSize);
			
			cmm1600VO.setPrjId((String) request.getSession().getAttribute("selPrjId"));
        	
    		PaginationInfo paginationInfo = PagingUtil.getPaginationInfo(cmm1600VO);  /** paging - 신규방식 */
        	List<Cmm1600VO> cmm1600List = null;

			HttpSession ss = request.getSession();
			LoginVO loginVO = (LoginVO) ss.getAttribute("loginVO");
			cmm1600VO.setLicGrpId(loginVO.getLicGrpId());
    		// 목록 조회  authGrpIds
			cmm1600List = cmm1600Service.selectCmm1600CommonDplList(cmm1600VO);
		    
    		// 총 건수
		    int totCnt = cmm1600Service.selectCmm1600CommonDplListCnt(cmm1600VO);
		    paginationInfo.setTotalRecordCount(totCnt);
		    
		    model.addAttribute("list", cmm1600List);
		    
		    //페이지 정보 보내기
			Map<String, Integer> pageMap = new HashMap<String, Integer>();
			pageMap.put("pageNo",cmm1600VO.getPageIndex());
			pageMap.put("listCount", cmm1600List.size());
			pageMap.put("totalPages", paginationInfo.getTotalPageCount());
			pageMap.put("totalElements", totCnt);
			pageMap.put("pageSize", _pageSize);

			model.addAttribute("page", pageMap);
        	
        	//조회성공메시지 세팅
        	model.addAttribute("message", egovMessageSource.getMessage("success.common.select"));
        	
        	return new ModelAndView("jsonView", model);
    	}catch(Exception ex){
    		Log.error("selectCmm1600CommonDplListAjax()", ex);
    		
    		//조회실패 메시지 세팅
    		model.addAttribute("message", egovMessageSource.getMessage("fail.common.select"));
    		return new ModelAndView("jsonView", model);
    	}
	}	
	
}
