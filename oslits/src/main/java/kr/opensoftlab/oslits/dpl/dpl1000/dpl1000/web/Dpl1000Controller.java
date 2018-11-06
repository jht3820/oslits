package kr.opensoftlab.oslits.dpl.dpl1000.dpl1000.web;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import kr.opensoftlab.oslits.com.vo.LoginVO;
import kr.opensoftlab.oslits.dpl.dpl1000.dpl1000.service.Dpl1000Service;
import kr.opensoftlab.oslits.dpl.dpl1000.dpl1000.vo.Dpl1000VO;
import kr.opensoftlab.oslits.dpl.dpl1000.dpl1100.service.Dpl1100Service;
import kr.opensoftlab.sdf.excel.BigDataSheetWriter;
import kr.opensoftlab.sdf.excel.ExcelDataListResultHandler;
import kr.opensoftlab.sdf.excel.Metadata;
import kr.opensoftlab.sdf.excel.SheetHeader;
import kr.opensoftlab.sdf.jenkins.JenkinsClient;
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
 * @Class Name : Dpl1000Controller.java
 * @Description : Dpl1000Controller Controller class
 * @Modification Information
 *
 * @author 정형택
 * @since 2016.01.30
 * @version 1.0
 * @see
 *  
 *  Copyright (C)  All right reserved.
 */

@Controller
public class Dpl1000Controller {

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
	
	
	/** Dpl1000Service DI */
    @Resource(name = "dpl1000Service")
    private Dpl1000Service dpl1000Service;
    
	/** Dpl1000Service DI */
    @Resource(name = "dpl1100Service")
    private Dpl1100Service dpl1100Service;
    
	@Value("${Globals.fileStorePath}")
	private String tempPath;
    
    
    /**
	 * Dpl1000  배포 버전 정보 조회
	 * @param 
	 * @return 
	 * @exception Exception
	 */
	@RequestMapping(value="/dpl/dpl1000/dpl1000/selectDpl1000View.do")
    public String selectDpl1000View(HttpServletRequest request, HttpServletResponse response, ModelMap model ) throws Exception {
    	return "/dpl/dpl1000/dpl1000/dpl1000";
    }
    /**
 	 * Dpl1001 배포 버전 등록 팝업
 	 * 
 	 * @param 
 	 * @return 
 	 * @exception Exception
 	 */
     @SuppressWarnings({ "rawtypes" })
 	@RequestMapping(value="/dpl/dpl1000/dpl1000/selectDpl1001View.do")
     public String selectDpl1001View(HttpServletRequest request, HttpServletResponse response, ModelMap model ) throws Exception {
 		
    	 try{
 			// request 파라미터를 map으로 변환
 			Map<String, String> paramMap = RequestConvertor.requestParamToMapAddSelInfo(request, true);

 			Map dpl1000DplInfo = null;

 			// 팝업 타입 - 등록/수정/상세보기
 			String pageType = paramMap.get("popupGb");
 			
 			//수정또는 상세보기이면 배포버전 정보를 조회하여 화면에 세팅한다.
 			if( "update".equals(pageType) || "select".equals(pageType) ){
 				dpl1000DplInfo = dpl1000Service.selectDpl1000DeployVerInfo(paramMap);
 			}

 			model.put("dpl1000DplInfo", dpl1000DplInfo);

 			return "/dpl/dpl1000/dpl1000/dpl1001";
 		}
 		catch(Exception ex){
 			Log.error("selectReq1001View()", ex);
 			throw new Exception(ex.getMessage());
 		}
     }
    
    /**
	 * Dpl1000 배포자 리스트를 가져온다.
	 * @param 
	 * @return 
	 * @exception Exception
	 */
	@SuppressWarnings({ "unchecked", "rawtypes" })
	@RequestMapping(method=RequestMethod.POST, value="/dpl/dpl1000/dpl1000/selectDpl1000DeployNmListAjax.do")
    public ModelAndView selectDpl1000DeployNmListAjax(HttpServletRequest request, HttpServletResponse response, ModelMap model ) throws Exception {
    	try{

    		//리퀘스트에서 넘어온 파라미터를 맵으로 세팅
        	Map paramMap = RequestConvertor.requestParamToMap(request, true);
        	paramMap.put("prjId", request.getSession().getAttribute("selPrjId"));

        	//배포 버전 정보 리스트 조회
        	List<Map> dplDeployNmList = (List) dpl1000Service.selectDpl1000DeployNmList(paramMap);
        	
        	model.addAttribute("dplDeployNmList", dplDeployNmList);
        	
        	//조회성공메시지 세팅
        	model.addAttribute("message", egovMessageSource.getMessage("success.common.select"));
        	
        	return new ModelAndView("jsonView", model);
    	}
    	catch(Exception ex){
    		Log.error("selectDpl1000DeployNmListAjax()", ex);
    		
    		//조회실패 메시지 세팅
    		model.addAttribute("message", egovMessageSource.getMessage("fail.common.select"));
    		return new ModelAndView("jsonView", model);
    	}
    }
    
	/**
	 * Dpl1000 배포 버전 정보 리스트를 가져온다.
	 * @param 
	 * @return 
	 * @exception Exception
	 */
	@RequestMapping(method=RequestMethod.POST, value="/dpl/dpl1000/dpl1000/selectDpl1000DeployVerInfoListAjax.do")
    public ModelAndView selectDpl1000DeployVerInfoListAjax(@ModelAttribute("dpl1000VO") Dpl1000VO dpl1000VO, HttpServletRequest request, HttpServletResponse response, ModelMap model ) throws Exception {
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
			dpl1000VO.setPageIndex(_pageNo);
			dpl1000VO.setPageSize(_pageSize);
			dpl1000VO.setPageUnit(_pageSize);
        	
        	
        	PaginationInfo paginationInfo = PagingUtil.getPaginationInfo(dpl1000VO); /** paging - 신규방식 */
			List<Dpl1000VO> dpl1000List = null;
			
			HttpSession ss = request.getSession();
			String prjId	= (String) ss.getAttribute("selPrjId");
			String licGrpId	= (String) paramMap.get("licGrpId");
			
			dpl1000VO.setPrjId(prjId);
			dpl1000VO.setLicGrpId(licGrpId);
        	//배포 버전 정보 리스트 조회
        	dpl1000List = (List<Dpl1000VO>) dpl1000Service.selectDpl1000DeployVerInfoList(dpl1000VO);
        	/** 총 데이터의 건수 를 가져온다. */
			int totCnt = dpl1000Service.selectDpl1000ListCnt(dpl1000VO);
					
        	paginationInfo.setTotalRecordCount(totCnt);
        	
        	model.addAttribute("list", dpl1000List);
        	
			//페이지 정보 보내기
			Map<String, Integer> pageMap = new HashMap<String, Integer>();
			pageMap.put("pageNo",dpl1000VO.getPageIndex());
			pageMap.put("listCount", dpl1000List.size());
			pageMap.put("totalPages", paginationInfo.getTotalPageCount());
			pageMap.put("totalElements", totCnt);
			pageMap.put("pageSize", _pageSize);
			
			model.addAttribute("page", pageMap);
        	
        	//조회성공메시지 세팅
        	model.addAttribute("message", egovMessageSource.getMessage("success.common.select"));
        	
        	return new ModelAndView("jsonView", model);
    	}
    	catch(Exception ex){
    		Log.error("selectDpl1000DeployInfoListAjax()", ex);
    		
    		//조회실패 메시지 세팅
    		model.addAttribute("message", egovMessageSource.getMessage("fail.common.select"));
    		return new ModelAndView("jsonView", model);
    	}
    }
	
	/**
	 * Dpl1000 배포 버전 등록(단건).
	 * 배포 버전 등록후 등록 정보 결과 및 정보를 화면으로 리턴한다.
	 * @param 
	 * @return 
	 * @exception Exception
	 */
	@SuppressWarnings({ "rawtypes" })
	@RequestMapping(method=RequestMethod.POST, value="/dpl/dpl1000/dpl1000/insertDpl1000DeployVerInfoAjax.do")
    public ModelAndView insertDpl1000DeployVerInfoAjax(HttpServletRequest request, HttpServletResponse response, ModelMap model ) throws Exception {
    	try{

    		//리퀘스트에서 넘어온 파라미터를 맵으로 세팅
    		Map<String, String> paramMap = RequestConvertor.requestParamToMapAddSelInfo(request, true);
    		HttpSession ss = request.getSession();
    		String prjId = (String) ss.getAttribute("selPrjId");
			paramMap.put("prjId", prjId);
			
			if( paramMap.get("popupGb").toString().equals("insert") ){
				dpl1000Service.insertDpl1000DeployVerInfo(paramMap);
			} else {
				dpl1000Service.updateDpl1000DeployVerInfo(paramMap);
			}
        	//조회성공메시지 세팅
        	model.addAttribute("message", egovMessageSource.getMessage("success.common.insert"));
        	
        	return new ModelAndView("jsonView", model);
    	}
    	catch(Exception ex){
    		Log.error("insertDpl1000DeployVerInfoAjax()", ex);
    		
    		//조회실패 메시지 세팅
    		model.addAttribute("message", egovMessageSource.getMessage("fail.common.insert"));
    		return new ModelAndView("jsonView", model);
    	}
	}
	
	/**
	 * Dpl1000 배포 버전 리스트 삭제.
	 * 배포 버전 삭제후 삭제 정보 결과 및 정보를 화면으로 리턴한다.
	 * @param 
	 * @return 
	 * @exception Exception
	 */
	@SuppressWarnings({ "rawtypes" })
	@RequestMapping(method=RequestMethod.POST, value="/dpl/dpl1000/dpl1000/deleteDpl1000DeployVerInfoListAjax.do")
    public ModelAndView deleteDpl1000DeployVerInfoListAjax(HttpServletRequest request, HttpServletResponse response, ModelMap model) throws Exception {
    	try{
    		//리퀘스트에서 넘어온 파라미터를 맵으로 세팅
        	Map paramMap = RequestConvertor.requestParamToMap(request);
        	paramMap.put("prjId", request.getSession().getAttribute("selPrjId"));
        	
        	
        	List cnt = (List) dpl1000Service.selectDpl1000ReqCount(paramMap);
        	 
        	if(cnt.size()>0){	
        		//조회성공메시지 세팅
        		model.addAttribute("delYN", "N");
            	model.addAttribute("message", egovMessageSource.getMessage("fail.common.delete.upperMenuExist"));
        	}
        	else{
        		dpl1000Service.deleteDpl1000DeployVerInfoList(paramMap);
        		
        		//조회성공메시지 세팅
        		model.addAttribute("successYn", "Y");
            	model.addAttribute("message", egovMessageSource.getMessage("success.common.delete"));
        	}
        	
        	return new ModelAndView("jsonView", model);
    	}
    	catch(Exception ex){
    		Log.error("deleteSpr1000SprintInfoListAjax()", ex);
    		
    		//조회실패 메시지 세팅
    		model.addAttribute("message", egovMessageSource.getMessage("fail.common.delete"));
    		return new ModelAndView("jsonView", model);
    	}
    }
	
	/**
	 * Dpl1000 엑셀 다운로드
	 * @param 
	 * @return 
	 * @exception Exception
	 */
	@RequestMapping(value="/dpl/dpl1000/dpl1000/selectDpl1000ExcelList.do")
	public ModelAndView selectDpl1000ExcelList(@ModelAttribute("dpl1000VO") Dpl1000VO dpl1000VO, HttpServletRequest request, HttpServletResponse response, ModelMap model ) throws Exception {


		//엑셀 다운로드 양식의 헤더명 선언
		SheetHeader header = new SheetHeader(new String[]{
				egovMessageSource.getMessage("excel.dplId")
				,egovMessageSource.getMessage("excel.dplVer")
				,egovMessageSource.getMessage("excel.dplNm")
				,egovMessageSource.getMessage("excel.dplDt")
				,egovMessageSource.getMessage("excel.dplUsrId")
				});

		/* 조회되는 데이터와 포멧 지정 
		 * ex 1. 수치형 new Metadata("property", XSSFCellStyle.ALIGN_RIGHT, "#,##0")
		 * ex 2. 사용자 지정 날짜 변환 new Metadata("property", ,"00-00-00") YYMMDD -> YY-MM-DD
		 * */
		List<Metadata> metadatas = new ArrayList<Metadata>(); 
		metadatas.add(new Metadata("dplId"));
		metadatas.add(new Metadata("dplVer"));
		metadatas.add(new Metadata("dplNm"));
		metadatas.add(new Metadata("dplDt"));
		metadatas.add(new Metadata("dplUsrId"));		        


		BigDataSheetWriter writer = new BigDataSheetWriter(
				egovMessageSource.getMessage("excel.dpl1000.sheetNm")
				, tempPath
				,egovMessageSource.getMessage("excel.dpl1000.sheetNm")
				, metadatas);

		writer.beginSheet();

		try{

			HttpSession ss = request.getSession();
			LoginVO loginVO = (LoginVO) ss.getAttribute("loginVO");

			String prjId	= (String) ss.getAttribute("selPrjId");
			String licGrpId	= (String) loginVO.getLicGrpId();
			
			dpl1000VO.setPrjId(prjId);
			dpl1000VO.setLicGrpId(licGrpId);
			
			ExcelDataListResultHandler  resultHandler = new ExcelDataListResultHandler(writer.getXMLSheetWriter(), writer.getStyleMap(), header, metadatas);

			dpl1000Service.selectDpl1000ExcelList(dpl1000VO,resultHandler);

		}
		catch(Exception ex){
			Log.error("selectReq2000ExcelDownList()", ex);
			throw new Exception(ex.getMessage());
		}finally{
			writer.endSheet();
		}
		
		return writer.getModelAndView();
	}
	
	
	/**
	 * Dpl1000 배포 버전 정보 리스트를 가져온다.
	 * @param 
	 * @return 
	 * @exception Exception
	 */
	@RequestMapping(value="/dpl/dpl1000/dpl1000/selectDpl1000BuildInfoListAjax.do")
    public ModelAndView selectDpl1000BuildInfoListAjax(@ModelAttribute("dpl1000VO") Dpl1000VO dpl1000VO, HttpServletRequest request, HttpServletResponse response, ModelMap model ) throws Exception {
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
			dpl1000VO.setPageIndex(_pageNo);
			dpl1000VO.setPageSize(_pageSize);
			dpl1000VO.setPageUnit(_pageSize);
        	
        	
        	PaginationInfo paginationInfo = PagingUtil.getPaginationInfo(dpl1000VO); /** paging - 신규방식 */
			List<Dpl1000VO> dpl1000List = null;
			
			HttpSession ss = request.getSession();
			String prjId	= (String) ss.getAttribute("selPrjId");
			String licGrpId	= (String) paramMap.get("licGrpId");
			
			dpl1000VO.setPrjId(prjId);
			dpl1000VO.setLicGrpId(licGrpId);
			
			paramMap.put("prjId", prjId);
			
		
						
        	//배포 버전 정보 리스트 조회
        	dpl1000List = (List<Dpl1000VO>) dpl1000Service.selectDpl1000BuildInfoList(dpl1000VO);
        	/** 총 데이터의 건수 를 가져온다. */
			int totCnt = dpl1000Service.selectDpl1000BuildInfoListCnt(dpl1000VO);
					
        	paginationInfo.setTotalRecordCount(totCnt);
        	
        	model.addAttribute("list", dpl1000List);
        	
			//페이지 정보 보내기
			Map<String, Integer> pageMap = new HashMap<String, Integer>();
			pageMap.put("pageNo",dpl1000VO.getPageIndex());
			pageMap.put("listCount", dpl1000List.size());
			pageMap.put("totalPages", paginationInfo.getTotalPageCount());
			pageMap.put("totalElements", totCnt);
			pageMap.put("pageSize", _pageSize);
			
			model.addAttribute("page", pageMap);
        	
        	//조회성공메시지 세팅
        	model.addAttribute("message", egovMessageSource.getMessage("success.common.select"));
        	
        	return new ModelAndView("jsonView", model);
    	}
    	catch(Exception ex){
    		Log.error("selectDpl1000BuildInfoListAjax()", ex);
    		
    		//조회실패 메시지 세팅
    		model.addAttribute("message", egovMessageSource.getMessage("fail.common.select"));
    		return new ModelAndView("jsonView", model);
    	}
    }
	

	
	
	    @SuppressWarnings({ "rawtypes" })
	 	@RequestMapping(value="/dpl/dpl1000/dpl1000/selectDpl1000BuildDetailAjax.do")
	     public ModelAndView selectDpl1000BuildDetailAjax(HttpServletRequest request, HttpServletResponse response, ModelMap model ) throws Exception {
	 		
	    	 try{
	 			// request 파라미터를 map으로 변환
	 			Map<String, String> paramMap = RequestConvertor.requestParamToMapAddSelInfo(request, true);
	 			
	 			HttpSession ss = request.getSession();
				String prjId	= (String) ss.getAttribute("selPrjId");		
				paramMap.put("prjId", prjId);
				
	 			Map buildMap = null;

	 			//수정모드이면 요구사항 정보 조회하여 화면에 세팅한다.
	 			
	 			buildMap = dpl1000Service.selectDpl1000BuildDetail(paramMap);
	 			

	 			model.put("buildMap", buildMap);
	 			
	 			model.addAttribute("message", egovMessageSource.getMessage("success.common.select"));
	        	
	 			return new ModelAndView("jsonView", model);
	 		}
	 		catch(Exception ex){
	 			Log.error("selectDpl1000BuildDetailAjax()", ex);
	 			model.addAttribute("message", egovMessageSource.getMessage("fail.common.select"));
	    		return new ModelAndView("jsonView", model);
	 		}
	     }
	
	
	
}
