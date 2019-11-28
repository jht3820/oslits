package kr.opensoftlab.oslops.dpl.dpl2000.dpl2000.web;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import kr.opensoftlab.oslops.com.vo.LoginVO;
import kr.opensoftlab.oslops.dpl.dpl1000.dpl1000.service.Dpl1000Service;
import kr.opensoftlab.oslops.dpl.dpl2000.dpl2000.service.Dpl2000Service;
import kr.opensoftlab.oslops.dpl.dpl2000.dpl2000.vo.Dpl2000VO;
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
 * @Class Name : Dpl2000Controller.java
 * @Description : Dpl2000Controller Controller class
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
public class Dpl2000Controller {

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
    
    /** Dpl2000Service DI */
    @Resource(name = "dpl2000Service")
    private Dpl2000Service dpl2000Service;
    
	@Value("${Globals.fileStorePath}")
	private String tempPath;
    
    
    /**
	 * Dpl2000  배포 계획 결재 요청 현황
	 * @param 
	 * @return 
	 * @exception Exception
	 */
	@RequestMapping(value="/dpl/dpl2000/dpl2000/selectDpl2000View.do")
    public String selectDpl2000View(HttpServletRequest request, HttpServletResponse response, ModelMap model ) throws Exception {
    	return "/dpl/dpl2000/dpl2000/dpl2000";
    }
		
	/**
	 * Dpl2000 배포계획 목록 AJAX 조회
	 * @param request
	 * @param response
	 * @throws Exception
	 */
	@SuppressWarnings("unchecked")
	@RequestMapping(value="/dpl/dpl2000/dpl2000/selectDpl2000AjaxView.do")
	public ModelAndView selectDpl2000AjaxView(@ModelAttribute("dpl2000VO") Dpl2000VO dpl2000VO, HttpServletRequest request, HttpServletResponse response, ModelMap model )	throws Exception {

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
			dpl2000VO.setPageIndex(_pageNo);
			dpl2000VO.setPageSize(_pageSize);
			dpl2000VO.setPageUnit(_pageSize);
						
			PaginationInfo paginationInfo = PagingUtil.getPaginationInfo(dpl2000VO); /** paging - 신규방식 */

			List<Dpl2000VO> dpl2000List = null;

			HttpSession ss = request.getSession();
			String prjId	= (String) ss.getAttribute("selPrjId");
			String licGrpId	= (String) paramMap.get("licGrpId");
			String usrId = ((LoginVO) ss.getAttribute("loginVO")).getUsrId();

			dpl2000VO.setPrjId(prjId);
			dpl2000VO.setLicGrpId(licGrpId);
			
			//로그인한 사용자 결재 요청 현황만 가져오기
			dpl2000VO.setDpl2000LoginUsrIdChk(usrId);
			
			int totCnt = 0;
			dpl2000List = dpl2000Service.selectDpl2000SignList(dpl2000VO);

			/** 총 데이터의 건수 를 가져온다. */
			totCnt = dpl2000Service.selectDpl2000SignListCnt(dpl2000VO);
			paginationInfo.setTotalRecordCount(totCnt);

			//페이지 정보 보내기
			Map<String, Integer> pageMap = new HashMap<String, Integer>();
			pageMap.put("pageNo",dpl2000VO.getPageIndex());
			pageMap.put("listCount", dpl2000List.size());
			pageMap.put("totalPages", paginationInfo.getTotalPageCount());
			pageMap.put("totalElements", totCnt);
			pageMap.put("pageSize", _pageSize);

			model.addAttribute("selectYN", "Y");
			model.addAttribute("page", pageMap);
			model.addAttribute("list", dpl2000List); 			/** 조회 목록 List 형태로 화면에 Return 한다. */

			return new ModelAndView("jsonView");

		}catch(Exception ex){
			Log.error("selectDpl2000AjaxView()", ex);
			//조회실패 메시지 세팅 및 저장 성공여부 세팅
			model.addAttribute("selectYN", "N");
			model.addAttribute("message", egovMessageSource.getMessage("fail.common.select"));
			return new ModelAndView("jsonView");
		}
	}
}
