package kr.opensoftlab.oslops.cmm.cmm1000.cmm1600.web;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import kr.opensoftlab.oslops.cmm.cmm1000.cmm1000.service.Cmm1000Service;
import kr.opensoftlab.oslops.cmm.cmm1000.cmm1000.vo.Cmm1000VO;
import kr.opensoftlab.oslops.cmm.cmm1000.cmm1600.service.Cmm1600Service;
import kr.opensoftlab.oslops.cmm.cmm1000.cmm1600.vo.Cmm1600VO;
import kr.opensoftlab.oslops.com.vo.LoginVO;
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
	 * Cmm1600 배포 계획 목록 선택 팝업으로 이동한다.
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
	 * Cmm1600 (공통)배포 계획 목록을 조회한다.
	 * 공통에서 배포목록 조회는 배포 상태가 대기인 상태, 결제여부가 승인인 건만 조회한다. 
	 * (req4105.jsp에서 배포상태 코드(dplStsCd) 01로 전달)
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
        	
        	// 로그인 VO를 가져온다.
        	HttpSession ss = request.getSession();
			LoginVO loginVO = (LoginVO) ss.getAttribute("loginVO");
        	
			// 라이선스 그룹 ID와 프로젝트 ID를 가져온다.
			String licGrpId = loginVO.getLicGrpId();
			String prjId = (String)ss.getAttribute("selPrjId");
			
			// 라이선스 그룹 ID와 프로젝트 ID 세팅
			cmm1600VO.setLicGrpId(licGrpId);
			cmm1600VO.setPrjId(prjId);
			
//			cmm1600VO.setPrjId((String) request.getSession().getAttribute("selPrjId"));
			
       		//현재 페이지 값, 보여지는 개체 수
			String _pageNo_str = paramMap.get("pageNo");
			String _pageSize_str = paramMap.get("pageSize");
			
			int _pageNo = 1;
			int _pageSize = OslAgileConstant.pageSize;
			
			// 페이지 값과 보여주닌 개수(pageSize)가 있다면 세팅
			if(_pageNo_str != null && !"".equals(_pageNo_str)){
				_pageNo = Integer.parseInt(_pageNo_str)+1;  
			}
			if(_pageSize_str != null && !"".equals(_pageSize_str)){
				_pageSize = Integer.parseInt(_pageSize_str);  
			}
			
			// 페이지 사이즈 세팅
			cmm1600VO.setPageIndex(_pageNo);
			cmm1600VO.setPageSize(_pageSize);
			cmm1600VO.setPageUnit(_pageSize);
			
    		PaginationInfo paginationInfo = PagingUtil.getPaginationInfo(cmm1600VO);  /** paging - 신규방식 */
        	
    		// 배포 계획 목록
    		List<Cmm1600VO> cmm1600DplList = null;

    		// 패고 계획 목록 총 건수를 조회한다.
    		int totCnt = 0;
		    totCnt = cmm1600Service.selectCmm1600CommonDplListCnt(cmm1600VO);
		    paginationInfo.setTotalRecordCount(totCnt);
		    
    		// 배포 계획 목록 조회
		    cmm1600DplList = cmm1600Service.selectCmm1600CommonDplList(cmm1600VO);
		    
		    // 배포 계획 목록 세팅
		    model.addAttribute("list", cmm1600DplList);
		    
		    //페이지 정보 보내기
			Map<String, Integer> pageMap = new HashMap<String, Integer>();
			pageMap.put("pageNo",cmm1600VO.getPageIndex());
			pageMap.put("listCount", cmm1600DplList.size());
			pageMap.put("totalPages", paginationInfo.getTotalPageCount());
			pageMap.put("totalElements", totCnt);
			pageMap.put("pageSize", _pageSize);

			// 페이지 정보 세팅
			model.addAttribute("page", pageMap);
        	
        	// 조회 성공여부 및 조회성공 메시지 세팅
			model.addAttribute("errorYn", "N");
        	model.addAttribute("message", egovMessageSource.getMessage("success.common.select"));
        	
        	return new ModelAndView("jsonView", model);
    	}catch(Exception ex){
    		Log.error("selectCmm1600CommonDplListAjax()", ex);
    		
    		// 조회 실패여부 및 조회실패 메시지 세팅
    		model.addAttribute("errorYn", "N");
    		model.addAttribute("message", egovMessageSource.getMessage("fail.common.select"));
    		return new ModelAndView("jsonView", model);
    	}
	}	
	
}
