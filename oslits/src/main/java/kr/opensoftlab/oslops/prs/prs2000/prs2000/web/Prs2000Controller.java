package kr.opensoftlab.oslops.prs.prs2000.prs2000.web;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import kr.opensoftlab.oslops.com.vo.LoginVO;
import kr.opensoftlab.oslops.prs.prs2000.prs2000.service.Prs2000Service;
import kr.opensoftlab.oslops.prs.prs2000.prs2000.vo.Prs2000VO;
import kr.opensoftlab.sdf.excel.BigDataSheetWriter;
import kr.opensoftlab.sdf.excel.ExcelDataListResultHandler;
import kr.opensoftlab.sdf.excel.Metadata;
import kr.opensoftlab.sdf.excel.SheetHeader;
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
import egovframework.rte.fdl.cmmn.trace.LeaveaTrace;
import egovframework.rte.fdl.property.EgovPropertyService;
import egovframework.rte.ptl.mvc.tags.ui.pagination.PaginationInfo;

/**
 * @Class Name : Prs2000Controller.java
 * @Description : Prs2000Controller Controller class
 * @Modification Information
 *
 * @author 강재민
 * @since 2016.01.31
 * @version 1.0
 * @see
 *  
 *  Copyright (C)  All right reserved.
 */

@Controller
public class Prs2000Controller {
	/** 로그4j 로거 로딩 */
	protected Logger Log = Logger.getLogger(this.getClass());
	
	/** Dpl2000Service DI */
    @Resource(name = "prs2000Service")
    private Prs2000Service prs2000Service;
    
    /** EgovMessageSource */
	@Resource(name = "egovMessageSource")
	EgovMessageSource egovMessageSource;

	/** EgovPropertyService */
	@Resource(name = "propertiesService")
	protected EgovPropertyService propertiesService;

	/** TRACE */
	@Resource(name = "leaveaTrace")
	LeaveaTrace leaveaTrace;
	
	@Value("${Globals.fileStorePath}")
	private String tempPath;
	 /**
	 * Prs2000배정 프로젝트 확인 목록 페이지
	 * @param 
	 * @return 
	 * @exception Exception
	 */
	@RequestMapping(value="/prs/prs2000/prs2000/selectPrs2000View.do")
    public String selectPrs2000View(HttpServletRequest request, HttpServletResponse response, ModelMap model ) throws Exception {
    	return "/prs/prs2000/prs2000/prs2000";
    }
	
	
	/**
	 * Prs2000배정 프로젝트 확인 목록 조회
	 * @param 
	 * @return 
	 * @exception Exception
	 */
	@RequestMapping(value="/prs/prs2000/prs2000/selectPrs2000ListAjax.do")
    public ModelAndView selectPrs2000ListAjax(@ModelAttribute("prs2000VO") Prs2000VO prs2000VO, HttpServletRequest request, HttpServletResponse response, ModelMap model ) throws Exception {
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
			prs2000VO.setPageIndex(_pageNo);
			prs2000VO.setPageSize(_pageSize);
			prs2000VO.setPageUnit(_pageSize);
    		
    		prs2000VO.setSrchLicGrpId(paramMap.get("licGrpId"));
			prs2000VO.setSrchUsrId(paramMap.get("regUsrId"));
			
    		PaginationInfo paginationInfo = PagingUtil.getPaginationInfo(prs2000VO); 
    		int totCnt = 0;
			List<Prs2000VO> prs2000List = null;
			
    		/** 조회 목록 List 형태로 화면에 Return 한다. */
    		prs2000List = prs2000Service.selectPrs2000List(prs2000VO);
		    
		    /** 총 데이터의 건수 를 가져온다. */
		    totCnt = prs2000Service.selectPrs2000ListCnt(prs2000VO);
		    paginationInfo.setTotalRecordCount(totCnt);
		    /** 총 데이터의 건수 를 가져온다. */
			model.addAttribute( "paginationInfo", paginationInfo );
    		
			model.addAttribute("list", prs2000List);
        	
			//페이지 정보 보내기
			Map<String, Integer> pageMap = new HashMap<String, Integer>();
			pageMap.put("pageNo",prs2000VO.getPageIndex());
			pageMap.put("listCount", prs2000List.size());
			pageMap.put("pageCount", paginationInfo.getTotalPageCount());
			pageMap.put("totalPages", paginationInfo.getTotalPageCount());
			pageMap.put("totalElements", totCnt);
			pageMap.put("pageSize", _pageSize);

			model.addAttribute("page", pageMap);
        	//조회성공메시지 세팅
        	model.addAttribute("message", egovMessageSource.getMessage("success.common.select"));
        	
        	return new ModelAndView("jsonView", model);
    	}
    	catch(Exception ex){
    		Log.error("selectPrs2000View()", ex);
    		model.addAttribute("message", egovMessageSource.getMessage("fail.common.select"));
    		throw new Exception(ex.getMessage());
    	}
    }
	
	/**
	 * Req2000 화면 이동(이동시 요구사항 목록 조회)
	 * @param 
	 * @return 
	 * @exception Exception
	 */
	@RequestMapping(value="/prs/prs2000/prs2000/selectPrs2000ExcelList.do")
	public ModelAndView selectDpl1000ExcelList(@ModelAttribute("prs2000VO") Prs2000VO prs2000VO, HttpServletRequest request, HttpServletResponse response, ModelMap model ) throws Exception {

		//엑셀 다운로드 양식의 헤더명 선언
		String strPrjNm = egovMessageSource.getMessage("excel.prjNm");
		String strPrjDesc = egovMessageSource.getMessage("excel.prjDesc");
		String strPrjUsrCnt = egovMessageSource.getMessage("excel.prjUsrCnt");
		String strStartDt = egovMessageSource.getMessage("excel.startDt");
		String strEndDt = egovMessageSource.getMessage("excel.endDt");
		SheetHeader header = new SheetHeader(new String[]{strPrjNm, strPrjDesc,strPrjUsrCnt ,strStartDt ,strEndDt});

		/* 조회되는 데이터와 포멧 지정 
		 * ex 1. 수치형 new Metadata("property", XSSFCellStyle.ALIGN_RIGHT, "#,##0")
		 * ex 2. 사용자 지정 날짜 변환 new Metadata("property", ,"00-00-00") YYMMDD -> YY-MM-DD
		 * */
		List<Metadata> metadatas = new ArrayList<Metadata>(); 
		metadatas.add(new Metadata("prjNm"));
		metadatas.add(new Metadata("prjDesc"));
		metadatas.add(new Metadata("prjUsrCnt"));
		metadatas.add(new Metadata("startDt"));		        
		metadatas.add(new Metadata("endDt"));		        

		BigDataSheetWriter writer = new BigDataSheetWriter(egovMessageSource.getMessage("excel.prs2000.sheetNm"), tempPath, egovMessageSource.getMessage("excel.prs2000.sheetNm"), metadatas);

		writer.beginSheet();

		try{

			HttpSession ss = request.getSession();
			LoginVO loginVO = (LoginVO) ss.getAttribute("loginVO");

			prs2000VO.setPrjId((String) ss.getAttribute("selPrjId"));

    		prs2000VO.setSrchLicGrpId(loginVO.getLicGrpId());
			prs2000VO.setSrchUsrId(loginVO.getRegUsrId());
			
			ExcelDataListResultHandler  resultHandler = new ExcelDataListResultHandler(writer.getXMLSheetWriter(), writer.getStyleMap(), header, metadatas);

			prs2000Service.selectPrs2000ExcelList(prs2000VO, resultHandler);

		}
		catch(Exception ex){
			Log.error("selectReq2000ExcelDownList()", ex);
			throw new Exception(ex.getMessage());
		}finally{
			writer.endSheet();
		}
		
		return writer.getModelAndView();
	}
}
