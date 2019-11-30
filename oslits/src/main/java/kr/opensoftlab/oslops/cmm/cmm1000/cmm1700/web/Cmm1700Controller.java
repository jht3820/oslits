package kr.opensoftlab.oslops.cmm.cmm1000.cmm1700.web;

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
import egovframework.rte.fdl.property.EgovPropertyService;
import egovframework.rte.ptl.mvc.tags.ui.pagination.PaginationInfo;
import kr.opensoftlab.oslops.cmm.cmm1000.cmm1700.service.Cmm1700Service;
import kr.opensoftlab.oslops.cmm.cmm1000.cmm1700.vo.Cmm1700VO;
import kr.opensoftlab.oslops.com.vo.LoginVO;
import kr.opensoftlab.sdf.util.OslAgileConstant;
import kr.opensoftlab.sdf.util.PagingUtil;
import kr.opensoftlab.sdf.util.RequestConvertor;


/**
 * @Class Name : Cmm1700Controller.java
 * @Description : 역할 조회 컨트롤러 클래스
 * @Modification Information
 * <pre>
 *   수정일      수정자          수정내용
 *  -------    --------    ---------------------------
 *  2018.10.16  공대영          최초 생성
 *  
 * </pre>
 *  @author 공대영
 *  @version 1.0
 *  @see 
 *  
 *  Copyright (C) Open Soft Lab Corp. All Rights Reserved
 */
@Controller
public class Cmm1700Controller {
	
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
	
	@Resource(name = "cmm1700Service")
	Cmm1700Service cmm1700Service;
	
	
	
	
	/**
	 *
	 * 역할그룹 목록 조회 공통 팝업 이동
	 * @param request
	 * @param response
	 * @param model
	 * @return
	 * @throws Exception
	 */
	@RequestMapping(value="/cmm/cmm1000/cmm1700/selectCmm1700View.do")
	public String selectCmm1700View(HttpServletRequest request, HttpServletResponse response, ModelMap model ) throws Exception {
			return "/cmm/cmm1000/cmm1700/cmm1700";
	}	

	/**
	 * 
	 * 프로젝트의 역할그룹 목록을 조회한다.
	 * @param cmm1700VO
	 * @param request
	 * @param response
	 * @param model
	 * @return
	 * @throws Exception
	 */
	@SuppressWarnings("unchecked")
	@RequestMapping(value="/cmm/cmm1000/cmm1700/selectCmm1700CommonAuthListAjax.do")
	public ModelAndView selectCmm1700CommonAuthListAjax(@ModelAttribute("cmm1700VO") Cmm1700VO cmm1700VO, HttpServletRequest request, HttpServletResponse response, ModelMap model)	throws Exception {
		
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
			cmm1700VO.setPageIndex(_pageNo);
			cmm1700VO.setPageSize(_pageSize);
			cmm1700VO.setPageUnit(_pageSize);
			
			// 프로젝트 ID를 가져온다.
			String prjId = paramMap.get("prjId");
			// 파라미터에 프로젝트 Id가 없을 경우
			if(prjId == null || "".equals(prjId)){
				// 현재 선택한 프로젝트 ID를 세팅한다.
				prjId = (String) request.getSession().getAttribute("selPrjId");
			}
			
			cmm1700VO.setPrjId(prjId);
        	
    		PaginationInfo paginationInfo = PagingUtil.getPaginationInfo(cmm1700VO);  /** paging - 신규방식 */
        	List<Cmm1700VO> cmm1700List = null;

			HttpSession ss = request.getSession();
			LoginVO loginVO = (LoginVO) ss.getAttribute("loginVO");
			cmm1700VO.setLicGrpId(loginVO.getLicGrpId());
    		// 목록 조회  authGrpIds
			cmm1700List = cmm1700Service.selectCmm1700CommonAuthList(cmm1700VO);
		    
    		// 총 건수
		    int totCnt = cmm1700Service.selectCmm1700CommonAuthListCnt(cmm1700VO);
		    paginationInfo.setTotalRecordCount(totCnt);
		    
		    model.addAttribute("list", cmm1700List);
		    
		    //페이지 정보 보내기
			Map<String, Integer> pageMap = new HashMap<String, Integer>();
			pageMap.put("pageNo",cmm1700VO.getPageIndex());
			pageMap.put("listCount", cmm1700List.size());
			pageMap.put("totalPages", paginationInfo.getTotalPageCount());
			pageMap.put("totalElements", totCnt);
			pageMap.put("pageSize", _pageSize);

			model.addAttribute("page", pageMap);
        	
        	//조회성공메시지 세팅
        	model.addAttribute("message", egovMessageSource.getMessage("success.common.select"));
        	
        	return new ModelAndView("jsonView", model);
    	}catch(Exception ex){
    		Log.error("selectCmm1700CommonAuthListAjax()", ex);
    		
    		//조회실패 메시지 세팅
    		model.addAttribute("message", egovMessageSource.getMessage("fail.common.select"));
    		return new ModelAndView("jsonView", model);
    	}
	}	
	
}
