package kr.opensoftlab.oslops.stm.stm3000.stm3200.web;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import kr.opensoftlab.oslops.com.fms.web.service.FileMngService;
import kr.opensoftlab.oslops.com.vo.LoginVO;
import kr.opensoftlab.oslops.stm.stm3000.stm3200.service.Stm3200Service;
import kr.opensoftlab.oslops.stm.stm3000.stm3200.vo.Stm3200VO;
import kr.opensoftlab.sdf.util.OslAgileConstant;
import kr.opensoftlab.sdf.util.PagingUtil;
import kr.opensoftlab.sdf.util.ReqHistoryMngUtil;
import kr.opensoftlab.sdf.util.RequestConvertor;

import org.apache.log4j.Logger;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;

import egovframework.com.cmm.EgovMessageSource;
import egovframework.rte.fdl.idgnr.EgovIdGnrService;
import egovframework.rte.fdl.property.EgovPropertyService;
import egovframework.rte.ptl.mvc.tags.ui.pagination.PaginationInfo;

/**
 * @Class Name : Stm3200Controller.java
 * @Description : Stm3200Controller Controller class
 * @Modification Information
 *
 * @author 공대영
 *  * @since 2018.07.23.
 * @version 1.0
 * @see
 *  
 *  --------------------------------------
 *  수정일			수정자			수정내용
 *  --------------------------------------
 *  2019-03-11		배용진		 	기능 개선
 *  
 *  -------------------------------------- 
 *  
 *  Copyright (C)  All right reserved.
 */

@Controller
public class Stm3200Controller {

	/** 로그4j 로거 로딩 */
	private static final Logger Log = Logger.getLogger(Stm3200Controller.class);

	/** EgovMessageSource */
	@Resource(name = "egovMessageSource")
	EgovMessageSource egovMessageSource;

	/** EgovPropertyService */
	@Resource(name = "propertiesService")
	protected EgovPropertyService propertiesService;

	/** FileMngService */
	@Resource(name="fileMngService")
	private FileMngService fileMngService;

	@Resource(name = "egovFileIdGnrService")
	private EgovIdGnrService idgenService;

	@Resource(name = "historyMng")
	private ReqHistoryMngUtil historyMng;
	
	/** Stm3200Service DI */
	@Resource(name = "stm3200Service")
	private Stm3200Service stm3200Service;
	
	/**
	 * Stm3200 JENKINS 저장소 전체현황 화면으로 이동한다.
	 * @param request
	 * @param response
	 * @param model
	 * @return
	 * @throws Exception
	 */
	@RequestMapping(value="/stm/stm3000/stm3200/selectStm3200View.do")
	public String selectStm3200View(HttpServletRequest request, HttpServletResponse response, ModelMap model ) throws Exception {
		return "/stm/stm3000/stm3200/stm3200";
	}
	
	
	/**
	 * 
	 * Stm3200  라이선스 그룹의 각 프로젝트에 배정된 JENKINS JOB 전체 목록을 조회한다.
	 * @param stm3200VO
	 * @param request
	 * @param response
	 * @param model
	 * @return
	 * @throws Exception
	 */
	@RequestMapping(value="/stm/stm3000/stm3200/selectStm3200ProjectJenkinsJobAllListAjax.do")
	public ModelAndView selectStm3200ProjectJenkinsJobAllListAjax(@ModelAttribute("stm3200VO") Stm3200VO stm3200VO, HttpServletRequest request, HttpServletResponse response, ModelMap model ) throws Exception {

		try{
			// request 파라미터를 map으로 변환
			Map<String, String> paramMap = RequestConvertor.requestParamToMapAddSelInfo(request, true);
			
			// 로그인 vo 가져오기
			HttpSession ss = request.getSession();
			LoginVO loginVO = (LoginVO) ss.getAttribute("loginVO");
			
			//현재 페이지 값, 보여지는 개체 수
			String _pageNo_str = paramMap.get("pageNo");
			String _pageSize_str = paramMap.get("pageSize");
			
			int _pageNo = 1;
			int _pageSize = OslAgileConstant.pageSize;
			
			// 넘어온 페이지 정보가 있다면 해당 값으로 세팅
			if(_pageNo_str != null && !"".equals(_pageNo_str)){
				_pageNo = Integer.parseInt(_pageNo_str)+1;  
			}
			if(_pageSize_str != null && !"".equals(_pageSize_str)){
				_pageSize = Integer.parseInt(_pageSize_str);  
			}
			
			// 라이선스 그룹 ID 세팅
			stm3200VO.setLicGrpId(loginVO.getLicGrpId());
			
			// 페이지 번호, 페이지 사이즈 세팅
			stm3200VO.setPageIndex(_pageNo);
			stm3200VO.setPageSize(_pageSize);
			stm3200VO.setPageUnit(_pageSize);
			
			PaginationInfo paginationInfo = PagingUtil.getPaginationInfo(stm3200VO);  /** paging - 신규방식 */

			// 라이선스 그룹의 모든 프로젝트에 배정된 Job 목록
			List<Stm3200VO> allPrjJenkinsJobList = null;

			// 총 데이터 건수
			int totCnt = 0;
			totCnt = stm3200Service.selectStm3200ProjectJenkinsJobAllListCnt(stm3200VO);  
			// 총 데이터 건수 세팅
			paginationInfo.setTotalRecordCount(totCnt);
			
			// 라이선스 그룹의 모든 프로젝트에 배정된 Job 목록을 조회한다.
			allPrjJenkinsJobList = stm3200Service.selectStm3200ProjectJenkinsJobAllList(stm3200VO);
			
			// 라이선스 그룹의 모든 프로젝트에 배정된 Job 목록 세팅
			model.addAttribute("list", allPrjJenkinsJobList);
			
			//페이지 정보 보내기
			Map<String, Integer> pageMap = new HashMap<String, Integer>();
			pageMap.put("pageNo",stm3200VO.getPageIndex());
			pageMap.put("listCount", allPrjJenkinsJobList.size());
			pageMap.put("totalPages", paginationInfo.getTotalPageCount());
			pageMap.put("totalElements", totCnt);
			pageMap.put("pageSize", _pageSize);
			
			// 페이지 정보 세팅
			model.addAttribute("page", pageMap);
			
			// 조회 성공 및 성공메시지 세팅
    		model.addAttribute("errorYn", 'N');
        	model.addAttribute("message", egovMessageSource.getMessage("success.common.select"));
			
			return new ModelAndView("jsonView");
		}
		catch(Exception ex){
			Log.error("selectStm3200ProjectJenkinsJobAllListAjax()", ex);
			
			// 조회 실패 및 실패메시지 세팅
    		model.addAttribute("errorYn", 'Y');
    		model.addAttribute("message", egovMessageSource.getMessage("fail.common.select"));
			throw new Exception(ex.getMessage());
		}
	}
	
}
