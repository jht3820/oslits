package kr.opensoftlab.oslops.adm.adm5000.adm5100.web;



import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import kr.opensoftlab.oslops.adm.adm5000.adm5100.service.Adm5100Service;
import kr.opensoftlab.oslops.adm.adm5000.adm5100.vo.Adm5100VO;
import kr.opensoftlab.oslops.com.vo.LoginVO;
import kr.opensoftlab.oslops.req.req4000.req4100.vo.Req4100VO;
import kr.opensoftlab.sdf.excel.BigDataSheetWriter;
import kr.opensoftlab.sdf.excel.ExcelDataListResultHandler;
import kr.opensoftlab.sdf.excel.Metadata;
import kr.opensoftlab.sdf.excel.SheetHeader;
import kr.opensoftlab.sdf.util.OslAgileConstant;
import kr.opensoftlab.sdf.util.PagingUtil;
import kr.opensoftlab.sdf.util.RequestConvertor;

import org.apache.commons.lang.builder.ToStringBuilder;
import org.apache.commons.lang.builder.ToStringStyle;
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
 * @Class Name : Adm5100Controller.java
 * @Description : Adm5100Controller Controller class
 * @Modification Information
 *
 * @author 공대영
 * @since 2016.02.06.
 * @version 1.0
 * @see
 *  
 *  Copyright (C) OpenSoftLab Corp All right reserved.
 *  
 *  2017-04-05 AXISJ 그리드 적용	진주영
 */

@Controller
public class Adm5100Controller {
   
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
	
	/** Adm5100Service DI */
    @Resource(name = "adm5100Service")
    private Adm5100Service adm5100Service;
	
    @Value("${Globals.fileStorePath}")
	private String tempPath;
	
    /**
     * adm5100 시스템 사용이력 LOG 화면 조회
     * @param request
     * @param model
     * @return
     * @throws Exception
     */
	@RequestMapping(value="/adm/adm5000/adm5100/selectAdm5100View.do")
    public String selectAdm5100ListView(HttpServletRequest request, ModelMap model ) throws Exception {
        	return "/adm/adm5000/adm5100/adm5100";
    }
	
	 /**
	 * adm5100 조회버튼 클릭시 시스템 사용이력 조회 AJAX
	 * @param 
	 * @return 
	 * @exception Exception
	 */
    @SuppressWarnings({ "rawtypes", "unchecked" })
	@RequestMapping(value="/adm/adm5000/adm5100/selectAdm5100ViewAjax.do")
    public ModelAndView selectAdm5100ViewAjax(@ModelAttribute("adm5100VO") Adm5100VO adm5100VO, HttpServletRequest request, HttpServletResponse response, ModelMap model ) throws Exception {
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
			adm5100VO.setPageIndex(_pageNo);
			adm5100VO.setPageSize(_pageSize);
			adm5100VO.setPageUnit(_pageSize);
    		
			
			//로그인 라이선스 그룹 ID 설정
			adm5100VO.setLicGrpId(paramMap.get("licGrpId"));
			
			PaginationInfo paginationInfo = PagingUtil.getPaginationInfo(adm5100VO);  /** paging - 신규방식 */

			List<Adm5100VO> adm5100List = null;

			/**
			 * 상수로 정의된 EVENT_SELECT와 화면에서 세팅해서 보낸 EVENT를 비교해서 분기 처리
			 */
			if(OslAgileConstant.EVENT_SELECT.equals(adm5100VO.getSrchEvent())){
				int totCnt = 0;
				adm5100List = adm5100Service.selectAdm5100List(adm5100VO);

				/** 총 데이터의 건수 를 가져온다. */
				totCnt = adm5100Service.selectAdm5100ListCnt(adm5100VO);
				paginationInfo.setTotalRecordCount(totCnt);
			}
			else{
				int totCnt = 0;
				adm5100List = adm5100Service.selectAdm5100List(adm5100VO);

				/** 총 데이터의 건수 를 가져온다. */
				
				
				totCnt = adm5100Service.selectAdm5100ListCnt(adm5100VO);
				paginationInfo.setTotalRecordCount(totCnt);

			}

			model.addAttribute("list", adm5100List);


			//페이지 정보 보내기
			Map<String, Integer> pageMap = new HashMap<String, Integer>();
			pageMap.put("pageNo",adm5100VO.getPageIndex());
			pageMap.put("listCount", adm5100List.size());
			pageMap.put("totalPages", paginationInfo.getTotalPageCount());
			pageMap.put("totalElements", paginationInfo.getTotalRecordCount());
			pageMap.put("pageSize", _pageSize);

			model.addAttribute("page", pageMap);
			
			//등록 성공 결과 값
			model.addAttribute("result", "ok");
			return new ModelAndView("jsonView");
		}
		catch(Exception ex){
			Log.error("selectAdm5100ViewAjax()", ex);
			//등록 실패 결과 값
			model.addAttribute("result", "fail");
			return new ModelAndView("jsonView");
		}
    }
	
	
	@RequestMapping(value="/adm/adm5000/adm5100/selectAdm5100ExcelList.do")
    public ModelAndView selectAdm5100ExcelList(HttpServletRequest request, @ModelAttribute("adm5100VO") Adm5100VO adm5100VO, ModelMap model ) throws Exception {
		//엑셀 다운로드 양식의 헤더명 선언
		SheetHeader header = new SheetHeader(new String[]{
				egovMessageSource.getMessage("excel.logUsrId")
				,egovMessageSource.getMessage("excel.licGrpId")
				,egovMessageSource.getMessage("excel.usrNm")
				,egovMessageSource.getMessage("excel.logIp")
				,egovMessageSource.getMessage("excel.logUrl")
				,egovMessageSource.getMessage("excel.logTime") });
		/* 조회되는 데이터와 포멧 지정 
		 * ex 1. 수치형 new Metadata("property", XSSFCellStyle.ALIGN_RIGHT, "#,##0")
		 * ex 2. 사용자 지정 날짜 변환 new Metadata("property", ,"00-00-00") YYMMDD -> YY-MM-DD
		 * */
		@SuppressWarnings("serial")
		List<Metadata> metadatas = new ArrayList<Metadata>(); 
		metadatas.add(new Metadata("logUsrId"));
		metadatas.add(new Metadata("licGrpId"));
		metadatas.add(new Metadata("usrNm"));
		metadatas.add(new Metadata("logIp"));
		metadatas.add(new Metadata("logUrl"));		        
		metadatas.add(new Metadata("logTime"));
		
		BigDataSheetWriter writer = new BigDataSheetWriter(
				egovMessageSource.getMessage("excel.adm5100.sheetNm")
				, tempPath
				,egovMessageSource.getMessage("excel.adm5100.sheetNm")
				, metadatas);

		writer.beginSheet();

		try{

			ExcelDataListResultHandler  resultHandler = new ExcelDataListResultHandler(writer.getXMLSheetWriter(), writer.getStyleMap(), header, metadatas);
			
			HttpSession ss = request.getSession();
			LoginVO loginVO = (LoginVO) ss.getAttribute("loginVO");
			adm5100VO.setLicGrpId(loginVO.getLicGrpId());
			adm5100Service.selectAdm5100ExcelList(adm5100VO,resultHandler);

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
