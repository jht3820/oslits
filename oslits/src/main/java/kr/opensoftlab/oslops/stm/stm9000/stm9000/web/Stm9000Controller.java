package kr.opensoftlab.oslops.stm.stm9000.stm9000.web;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;


import javax.servlet.http.HttpServletRequest;

import kr.opensoftlab.oslops.com.fms.web.service.FileMngService;
import kr.opensoftlab.oslops.stm.stm9000.stm9000.service.Stm9000Service;
import kr.opensoftlab.oslops.stm.stm9000.stm9000.vo.Stm9000VO;
import kr.opensoftlab.sdf.util.OslAgileConstant;
import kr.opensoftlab.sdf.util.PagingUtil;
import kr.opensoftlab.sdf.util.ReqHistoryMngUtil;









import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;

import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.ModelAndView;

import egovframework.com.cmm.EgovMessageSource;
import egovframework.com.cmm.service.EgovFileMngUtil;
import egovframework.rte.fdl.idgnr.EgovIdGnrService;
import egovframework.rte.fdl.property.EgovPropertyService;
import egovframework.rte.ptl.mvc.tags.ui.pagination.PaginationInfo;


/**
 * @Class Name : Stm9000Controller.java
 * @Description : Stm9000Controller Controller class
 * @Modification Information
 *
 * @author 공대영
 *  * @since 2018.07.23.
 * @version 1.0
 * @see
 *  
 *  Copyright (C)  All right reserved.
 */

@RestController	
@RequestMapping(value="/restapi")
public class Stm9000Controller {

	/** 로그4j 로거 로딩 */
	private static final Logger Log = Logger.getLogger(Stm9000Controller.class);

	public static final String REST_REQ_TYPE_OSL = "REQ";

	public static final String REST_REQ_TYPE_ORG = "ORG";

	/** EgovMessageSource */
	@Resource(name = "egovMessageSource")
	EgovMessageSource egovMessageSource;

	/** EgovPropertyService */
	@Resource(name = "propertiesService")
	protected EgovPropertyService propertiesService;


	@Value("${Globals.fileStorePath}")
	private String tempPath;

	@Resource(name = "stm9000Service")
	private Stm9000Service stm9000Service;


	/**
	 * 
	 * 요구사항 목록 조회
	 * 
	 * @return
	 * @throws Exception
	 */
	@RequestMapping(value="/selectReqList/{pageNo}/{pageSize}")
	public ModelAndView selectStm9000ListAjaxView( @PathVariable("pageNo") String pageNo , @PathVariable("pageSize") String pageSize ,HttpServletRequest request ) throws Exception {
		Map paramMap = new HashMap();
		Map resultMap = new HashMap();
		Map statusMap = new HashMap();

		Stm9000VO stm9000VO = new Stm9000VO();
		try{
			stm9000VO.setPrjId((String)request.getAttribute("prjId"));
			stm9000VO.setLicGrpId((String) request.getAttribute("licGrpId"));
			String  reqUsrNm= request.getParameter("reqUsrNm");
			if(reqUsrNm==null){
				reqUsrNm="";
			}

			int _pageNo = 1;
			int _pageSize = OslAgileConstant.pageSize;

			if(pageNo != null && !"".equals(pageNo)){
				_pageNo = Integer.parseInt(pageNo)+1;  
			}
			if(pageSize != null && !"".equals(pageSize)){
				_pageSize = Integer.parseInt(pageSize);  
			}

			//페이지 사이즈
			stm9000VO.setPageIndex(_pageNo);
			stm9000VO.setPageSize(_pageSize);
			stm9000VO.setPageUnit(_pageSize);
			stm9000VO.setRestApiReqUsrNm(reqUsrNm);

			PaginationInfo paginationInfo = PagingUtil.getPaginationInfo(stm9000VO);  /** paging - 신규방식 */

			int totCnt = stm9000Service.selectStm9000ListCnt(stm9000VO);
			paginationInfo.setTotalRecordCount(totCnt);

			List reqList  = stm9000Service.selectStm9000List(stm9000VO);

			Map<String, Integer> pageMap = new HashMap<String, Integer>();
			pageMap.put("pageNo",stm9000VO.getPageIndex());
			pageMap.put("listCount", reqList.size());
			pageMap.put("totalPages", paginationInfo.getTotalPageCount());
			pageMap.put("totalElements", totCnt);
			pageMap.put("pageSize", _pageSize);

			statusMap.put("codename", "성공");
			statusMap.put("code", "200");
			statusMap.put("page", pageMap);
			
			resultMap.put("status", statusMap);
			resultMap.put("data", reqList);
			
		}catch(Exception e){
			Log.debug(e.getMessage());
			statusMap.put("codename", "실패");
			statusMap.put("code", "100");
			statusMap.put("errorMessage", e.getMessage());
			resultMap.put("status", statusMap);
		}

		return new ModelAndView("jsonView",resultMap);
	}

	/**
	 * 요구사항 상세 조회
	 * 
	 * @param reqId
	 * @return
	 * @throws Exception
	 */
	@RequestMapping(value="/selectReqDetail/{type}/{reqId}")
	public ModelAndView selectStm9000InfoAjaxView(@PathVariable("type") String type , @PathVariable("reqId") String reqId ,HttpServletRequest request ) throws Exception {
		Map paramMap = new HashMap();
		Map resultMap = new HashMap();
		Map statusMap = new HashMap();
		try{
			paramMap.put("licGrpId", request.getAttribute("licGrpId"));
			paramMap.put("prjId", request.getAttribute("prjId"));

			if(type.equals(REST_REQ_TYPE_OSL)){
				paramMap.put("reqId", reqId);
			}else if(type.equals(REST_REQ_TYPE_ORG)){
				paramMap.put("orgReqId", reqId);
			}


			Map reqInfoMap = (Map) stm9000Service.selectStm9000DetailInfo(paramMap);

			if(reqInfoMap!=null){
				

				List<Map> reqChgList = stm9000Service.selectStm9000ReqHistoryList(paramMap);

				reqInfoMap.put("reqChgList", reqChgList);
				//결재 정보
				List<Map> reqChkList = stm9000Service.selectStm9000ReqSignList(paramMap);

				reqInfoMap.put("reqChkList", reqChkList);
				// 요구사항 수정이력 조회
				List reqChgDetailList = stm9000Service.selectStm9000ChgDetailList(paramMap);

				reqInfoMap.put("reqChgDetailList", reqChgDetailList);
			}



			statusMap.put("code", "200");
			statusMap.put("codename", "성공");
			resultMap.put("status", statusMap);
			resultMap.put("data", reqInfoMap);
		}catch(Exception e){
			statusMap.put("code", "100");
			statusMap.put("codename", "실패");
			statusMap.put("errorMessage", e.getMessage());
			resultMap.put("status", statusMap);
		}
		return new ModelAndView("jsonView",resultMap);
	}


}
