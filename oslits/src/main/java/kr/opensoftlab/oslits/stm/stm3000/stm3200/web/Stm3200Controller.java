package kr.opensoftlab.oslits.stm.stm3000.stm3200.web;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;




















import kr.opensoftlab.oslits.com.fms.web.service.FileMngService;
import kr.opensoftlab.oslits.com.vo.LoginVO;
import kr.opensoftlab.oslits.stm.stm3000.stm3200.service.Stm3200Service;
import kr.opensoftlab.oslits.stm.stm3000.stm3200.vo.Stm3200VO;
import kr.opensoftlab.sdf.util.OslAgileConstant;
import kr.opensoftlab.sdf.util.PagingUtil;
import kr.opensoftlab.sdf.util.ReqHistoryMngUtil;
import kr.opensoftlab.sdf.util.RequestConvertor;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.multipart.MultipartHttpServletRequest;
import org.springframework.web.servlet.ModelAndView;

import egovframework.com.cmm.EgovMessageSource;
import egovframework.com.cmm.service.EgovFileMngUtil;
import egovframework.com.cmm.service.FileVO;
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

	@Value("${Globals.fileStorePath}")
	private String tempPath;

	/** EgovFileMngUtil - 파일 업로드 Util */
	@Resource(name="EgovFileMngUtil")
	private EgovFileMngUtil fileUtil;	

	@Resource(name = "egovFileIdGnrService")
	private EgovIdGnrService idgenService;

	@Resource(name = "historyMng")
	private ReqHistoryMngUtil historyMng;
	
	@Resource(name = "stm3200Service")
	private Stm3200Service stm3200Service;
	
	/**
	 * 전체 JENKINS 관리 목록 화면이동
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
	 * 전체 JENKINS 관리 목록 조회
	 * 
	 * @param stm3200VO
	 * @param request
	 * @param response
	 * @param model
	 * @return
	 * @throws Exception
	 */
	@RequestMapping(value="/stm/stm3000/stm3200/selectStm3200JobProjectListAjax.do")
	public ModelAndView selectStm3200JobProjectListAjax(@ModelAttribute("stm3200VO") Stm3200VO stm3200VO, HttpServletRequest request, HttpServletResponse response, ModelMap model ) throws Exception {

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
			stm3200VO.setPageIndex(_pageNo);
			stm3200VO.setPageSize(_pageSize);
			stm3200VO.setPageUnit(_pageSize);
			
			
			PaginationInfo paginationInfo = PagingUtil.getPaginationInfo(stm3200VO);  /** paging - 신규방식 */

			List<Stm3200VO> stm3200List = null;

			HttpSession ss = request.getSession();
			LoginVO loginVO = (LoginVO) ss.getAttribute("loginVO");

			stm3200VO.setLoginUsrId(loginVO.getUsrId());
			stm3200VO.setLicGrpId(loginVO.getLicGrpId());
			stm3200VO.setPrjId((String) ss.getAttribute("selPrjId"));

			/**
			 * 목록 조회
			 */
			int totCnt = 0;
			stm3200List =   stm3200Service.selectStm3200JobProjectList(stm3200VO);

			
			/** 총 데이터의 건수 를 가져온다. */
			totCnt =  stm3200Service.selectStm3200JobProjectListCnt(stm3200VO);
			paginationInfo.setTotalRecordCount(totCnt);
			
			model.addAttribute("list", stm3200List);
			
			//페이지 정보 보내기
			Map<String, Integer> pageMap = new HashMap<String, Integer>();
			pageMap.put("pageNo",stm3200VO.getPageIndex());
			pageMap.put("listCount", stm3200List.size());
			pageMap.put("totalPages", paginationInfo.getTotalPageCount());
			pageMap.put("totalElements", totCnt);
			pageMap.put("pageSize", _pageSize);
			
			model.addAttribute("page", pageMap);
			
			return new ModelAndView("jsonView");
		}
		catch(Exception ex){
			Log.error("selectStm3200JobProjectListAjax()", ex);
			throw new Exception(ex.getMessage());
		}
	}
	
}
