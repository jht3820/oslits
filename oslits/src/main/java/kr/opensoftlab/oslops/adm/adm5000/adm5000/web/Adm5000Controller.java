package kr.opensoftlab.oslops.adm.adm5000.adm5000.web;



import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import kr.opensoftlab.oslops.adm.adm5000.adm5000.service.Adm5000Service;
import kr.opensoftlab.oslops.adm.adm5000.adm5000.vo.Adm5000VO;
import kr.opensoftlab.oslops.com.vo.LoginVO;
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
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.servlet.ModelAndView;

import egovframework.com.cmm.EgovMessageSource;
import egovframework.rte.fdl.property.EgovPropertyService;
import egovframework.rte.ptl.mvc.tags.ui.pagination.PaginationInfo;

/**
 * @Class Name : Adm5000Controller.java
 * @Description : Adm5000Controller Controller class
 * @Modification Information
 *
 * @author 안세웅
 * @since 2016.01.16.
 * @version 1.0
 * @see
 *  
 *  Copyright (C) OpenSoftLab Corp All right reserved.
 */

@Controller
public class Adm5000Controller {

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

	/** Adm5000Service DI */
	@Resource(name = "adm5000Service")
	private Adm5000Service adm5000Service;

	@Value("${Globals.fileStorePath}")
	private String tempPath;
	
	 /**
		 * Adm5000 로그인 이력 화면
		 * @param 
		 * @return 
		 * @exception Exception
		 */
		@RequestMapping(value="/adm/adm5000/adm5000/selectAdm5000View.do")
	    public String selectDpl1000View(HttpServletRequest request, HttpServletResponse response, ModelMap model ) throws Exception {
	    	return "/adm/adm5000/adm5000/adm5000";
	    }

	/**
	 * Adm5000 로그 인/아웃 이력 정보 리스트를 가져온다.
	 * @param 
	 * @return 
	 * @exception Exception
	 */
	@RequestMapping(method=RequestMethod.POST, value="/adm/adm5000/adm5000/selectAdm5000ListAjax.do")
	public ModelAndView selectAdm5000ListView(HttpServletRequest request, @ModelAttribute("adm5000VO") Adm5000VO adm5000VO, ModelMap model ) throws Exception {
		try{
			//리퀘스트에서 넘어온 파라미터를 맵으로 세팅
        	Map<String, String> paramMap = RequestConvertor.requestParamToMap(request, true);

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
			adm5000VO.setPageIndex(_pageNo);
			adm5000VO.setPageSize(_pageSize);
			adm5000VO.setPageUnit(_pageSize);
    		
        	//로그인 라이선스 그룹 ID 설정
			adm5000VO.setLicGrpId(paramMap.get("licGrpId"));
        	
        	PaginationInfo paginationInfo = PagingUtil.getPaginationInfo(adm5000VO); /** paging - 신규방식 */
        	List<Adm5000VO> adm5000List = null;

			adm5000List = adm5000Service.selectAdm5000List(adm5000VO);
			
			/** 총 데이터의 건수 를 가져온다. */
			paginationInfo.setTotalRecordCount(adm5000Service.selectAdm5000ListCnt(adm5000VO));
			
			model.addAttribute("list", adm5000List);

			//페이지 정보 보내기
			Map<String, Integer> pageMap = new HashMap<String, Integer>();
			pageMap.put("pageNo",adm5000VO.getPageIndex());
			pageMap.put("listCount", adm5000List.size());
			pageMap.put("totalPages", paginationInfo.getTotalPageCount());
			pageMap.put("totalElements", adm5000List.size());
			pageMap.put("pageSize", _pageSize);

			model.addAttribute("page", pageMap);
			
			//조회성공메시지 세팅
        	model.addAttribute("message", egovMessageSource.getMessage("success.common.select"));
        	
        	return new ModelAndView("jsonView", model);
		}
		catch(Exception ex){
			Log.error("selectAdm5000ListAjax()", ex);
    		
    		//조회실패 메시지 세팅
    		model.addAttribute("message", egovMessageSource.getMessage("fail.common.select"));
    		return new ModelAndView("jsonView", model);
		}
	}


	@RequestMapping(value="/adm/adm5000/adm5000/selectAdm5000ExcelList.do")
	public ModelAndView selectAdm5000ExcelList(HttpServletRequest request, @ModelAttribute("adm5000VO") Adm5000VO adm5000VO, ModelMap model ) throws Exception {
		//엑셀 다운로드 양식의 헤더명 선언
		SheetHeader header = new SheetHeader(new String[]{
				egovMessageSource.getMessage("excel.loginUsrId")
				,egovMessageSource.getMessage("excel.licGrpId")
				,egovMessageSource.getMessage("excel.usrNm")
				,egovMessageSource.getMessage("excel.loginIp")
				,egovMessageSource.getMessage("excel.loginTime")
				,egovMessageSource.getMessage("excel.logoutTime") });
		/* 조회되는 데이터와 포멧 지정 
		 * ex 1. 수치형 new Metadata("property", XSSFCellStyle.ALIGN_RIGHT, "#,##0")
		 * ex 2. 사용자 지정 날짜 변환 new Metadata("property", ,"00-00-00") YYMMDD -> YY-MM-DD
		 * */
		List<Metadata> metadatas = new ArrayList<Metadata>(); 
		metadatas.add(new Metadata("loginUsrId"));
		metadatas.add(new Metadata("licGrpId"));
		metadatas.add(new Metadata("usrNm"));
		metadatas.add(new Metadata("loginIp"));
		metadatas.add(new Metadata("loginTime"));		        
		metadatas.add(new Metadata("logoutTime"));

		BigDataSheetWriter writer = new BigDataSheetWriter(
				egovMessageSource.getMessage("excel.adm5000.sheetNm")
				, tempPath
				,egovMessageSource.getMessage("excel.adm5000.sheetNm")
				, metadatas);

		writer.beginSheet();

		try{

			ExcelDataListResultHandler  resultHandler = new ExcelDataListResultHandler(writer.getXMLSheetWriter(), writer.getStyleMap(), header, metadatas);
			
			HttpSession ss = request.getSession();
			LoginVO loginVO = (LoginVO) ss.getAttribute("loginVO");
			adm5000VO.setLicGrpId(loginVO.getLicGrpId());
			adm5000Service.selectAdm5000ExcelList(adm5000VO,resultHandler);

		}
		catch(Exception ex){
			Log.error("selectAdm5000ExcelList()", ex);
			throw new Exception(ex.getMessage());
		}finally{
			writer.endSheet();
		}

		return writer.getModelAndView();
	}


}
