package kr.opensoftlab.oslops.cmm.cmm1000.cmm1000.web;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import kr.opensoftlab.oslops.cmm.cmm1000.cmm1000.service.Cmm1000Service;
import kr.opensoftlab.oslops.cmm.cmm1000.cmm1000.vo.Cmm1000VO;
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
 * @Class Name : Cmm1000Controller.java
 * @Description : 사용자팝업 컨트롤러 클래스
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
public class Cmm1000Controller {
	
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
	
	@Resource(name = "cmm1000Service")
	Cmm1000Service cmm1000Service;
	
	
	
	
	/**
	 *
	 * 사용자 정보 조회 공통 팝업 화면 이동
	 * 
	 * @param request
	 * @param response
	 * @param model
	 * @return
	 * @throws Exception
	 */
	@RequestMapping(value="/cmm/cmm1000/cmm1000/selectCmm1000View.do")
	public String selectCmm1000View(HttpServletRequest request, HttpServletResponse response, ModelMap model ) throws Exception {
			return "/cmm/cmm1000/cmm1000/cmm1000";
	}	

	
	

	/**
	 * 
	 * 사용자 정보 조회 공통 목록 조회
	 * 
	 * @param adm2000VO
	 * @param request
	 * @param response
	 * @param model
	 * @return
	 * @throws Exception
	 */
	@SuppressWarnings("unchecked")
	@RequestMapping(value="/cmm/cmm1000/cmm1000/selectCmm1000CommonUserListAjax.do")
	public ModelAndView selectCmm1000CommonUserListAjax(@ModelAttribute("cmm1000VO") Cmm1000VO cmm1000VO, HttpServletRequest request, HttpServletResponse response, ModelMap model)	throws Exception {
		
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
			cmm1000VO.setPageIndex(_pageNo);
			cmm1000VO.setPageSize(_pageSize);
			cmm1000VO.setPageUnit(_pageSize);
			
			cmm1000VO.setPrjId((String) request.getSession().getAttribute("selPrjId"));
        	
    		PaginationInfo paginationInfo = PagingUtil.getPaginationInfo(cmm1000VO);  /** paging - 신규방식 */
        	List<Cmm1000VO> cmm1000List = null;

			HttpSession ss = request.getSession();
			LoginVO loginVO = (LoginVO) ss.getAttribute("loginVO");
			cmm1000VO.setLicGrpId(loginVO.getLicGrpId());
			
			
			String[] authGrpIds =request.getParameterValues("authGrpId");
			// 권한그룹 ID 문자열
			String sAuthGrpIds ="";
			// 권한그룹 명 조회 Function을 사용하기 위한 권한그룹 ID 문자열
			String authGrpIdListStr = "";
			if( authGrpIds != null){
				for (int i = 0; i < authGrpIds.length; i++) {
					if(i==0){
						sAuthGrpIds += "  '"+authGrpIds[i]+"' ";
						authGrpIdListStr = authGrpIds[i];
					}else{
						sAuthGrpIds += ", '"+authGrpIds[i]+"' ";
						authGrpIdListStr += ","+authGrpIds[i];
					}
				}
				cmm1000VO.setAuthGrpIds(sAuthGrpIds);
				// 권한그룹 명 조회 Java Stored Function을 사용하기 위한 권한그룹 ID 문자열을 VO에 담는다.
				// 문자열은 AUT000001,AUT000002 등의 형식으로 만들어지며 큐브리드 Function에서 가공하여 사용한다.
				cmm1000VO.setAuthGrpIdList(authGrpIdListStr);
			}

    		// 목록 조회  authGrpIds
			cmm1000List = cmm1000Service.selectCmm1000CommonUserList(cmm1000VO);
		    
    		// 총 건수
		    int totCnt = cmm1000Service.selectCmm1000CommonUserListCnt(cmm1000VO);
		    paginationInfo.setTotalRecordCount(totCnt);
		    
		    model.addAttribute("list", cmm1000List);
		    
		    //페이지 정보 보내기
			Map<String, Integer> pageMap = new HashMap<String, Integer>();
			pageMap.put("pageNo",cmm1000VO.getPageIndex());
			pageMap.put("listCount", cmm1000List.size());
			pageMap.put("totalPages", paginationInfo.getTotalPageCount());
			pageMap.put("totalElements", totCnt);
			pageMap.put("pageSize", _pageSize);

			model.addAttribute("page", pageMap);
        	
        	//조회성공메시지 세팅
        	model.addAttribute("message", egovMessageSource.getMessage("success.common.select"));
        	
        	return new ModelAndView("jsonView", model);
    	}catch(Exception ex){
    		Log.error("selectCmm1000CommonUserListAjax()", ex);
    		
    		//조회실패 메시지 세팅
    		model.addAttribute("message", egovMessageSource.getMessage("fail.common.select"));
    		return new ModelAndView("jsonView", model);
    	}
	}	
	
}
