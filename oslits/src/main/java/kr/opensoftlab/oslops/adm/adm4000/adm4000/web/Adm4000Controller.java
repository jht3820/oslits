package kr.opensoftlab.oslops.adm.adm4000.adm4000.web;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import kr.opensoftlab.oslops.adm.adm4000.adm4000.service.Adm4000Service;
import kr.opensoftlab.oslops.com.vo.LoginVO;
import kr.opensoftlab.oslops.com.vo.PageVO;
import kr.opensoftlab.sdf.excel.BigDataSheetWriter;
import kr.opensoftlab.sdf.excel.ExcelDataListResultHandler;
import kr.opensoftlab.sdf.excel.Metadata;
import kr.opensoftlab.sdf.excel.SheetHeader;
import kr.opensoftlab.sdf.util.PagingUtil;
import kr.opensoftlab.sdf.util.RequestConvertor;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;

import com.google.gson.GsonBuilder;

import egovframework.com.cmm.EgovMessageSource;
import egovframework.rte.fdl.property.EgovPropertyService;
import egovframework.rte.ptl.mvc.tags.ui.pagination.PaginationInfo;

/**
 * @Class Name : Adm4000Controller.java
 * @Description : Adm4000Controller Controller class
 * @Modification Information
 *
 * @author 정형택
 * @since 2016.01.31
 * @version 1.0
 * @see
 *  
 *  Copyright (C)  All right reserved.
 */

@Controller
public class Adm4000Controller {

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
	
	
	/** Adm4000Service DI */
    @Resource(name = "adm4000Service")
    private Adm4000Service adm4000Service;
    
	@Value("${Globals.fileStorePath}")
	private String tempPath;
    
    /**
	 * Adm4000 공통코드 마스터 페이지 
	 * @param 
	 * @return 
	 * @exception Exception
	 */
	@RequestMapping(value="/adm/adm4000/adm4000/selectAdm4000View.do")
    public String selectAdm4000View(HttpServletRequest request, HttpServletResponse response, ModelMap model ) throws Exception {
    	return "/adm/adm4000/adm4000/adm4000";
    }
    
    /**
	 * Adm4000 공통코드 마스터 정보를 가져온다.
	 * @param 
	 * @return 
	 * @exception Exception
	 */
    @SuppressWarnings({ "rawtypes", "unchecked" })
	@RequestMapping(value="/adm/adm4000/adm4000/selectAdm4000CommonCodeMasterListAjax.do")
    public ModelAndView selectAdm4000CommonCodeMasterListAjax(HttpServletRequest request, HttpServletResponse response, ModelMap model ) throws Exception {
    	try{
    		//리퀘스트에서 넘어온 파라미터를 맵으로 세팅
        	Map<String, String> paramMap = RequestConvertor.requestParamToMapAddSelInfo(request, true);

        	//공통코드 마스터 정보 조회
        	List<Map> admMasterCodeList = (List) adm4000Service.selectAdm4000CommonCodeMasterList(paramMap);
        	
        	//PageVO 생성
    		PageVO pageVo = new PageVO();
    		pageVo.setSearchCd(paramMap.get("searchCd"));
    		pageVo.setSearchSelect(paramMap.get("searchSelect"));
    		pageVo.setSearchTxt(paramMap.get("searchTxt"));
        	
    		//pageVO 세팅
			String pageIndexStr = paramMap.get("pageNo");
			int pageIndex = 1;
			if(pageIndexStr != null && !"".equals(pageIndexStr)){
				pageIndex = Integer.parseInt(pageIndexStr);
			}
			pageVo.setPageIndex(pageIndex);
			pageVo.setPageSize(20);
			pageVo.setPageUnit(20);
			

			PaginationInfo paginationInfo = PagingUtil.getPaginationInfo(pageVo);
			paginationInfo.setTotalRecordCount(admMasterCodeList.size());
			
			model.addAttribute("list",admMasterCodeList);

			//페이지 정보 보내기
			Map<String, Integer> pageMap = new HashMap<String, Integer>();
			
			pageMap.put("pageNo",pageVo.getPageIndex());
			pageMap.put("listCount", admMasterCodeList.size());
			pageMap.put("pageCount", paginationInfo.getTotalPageCount());
			
			model.addAttribute("page", pageMap);
			
			model.addAttribute("errorYN", "N");
			model.addAttribute("message", egovMessageSource.getMessage("success.common.select"));
			
			return new ModelAndView("jsonView");
    	}
    	catch(Exception ex){
    		Log.error("selectAdm4000CommonCodeMasterListAjax()", ex);

			//조회실패 메시지 세팅 및 저장 성공여부 세팅
			model.addAttribute("errorYN", "Y");
			model.addAttribute("message", egovMessageSource.getMessage("fail.common.select"));
			return new ModelAndView("jsonView");
    	}
    }
	
    /**
	 * Adm4001 공통코드 마스터 추가/수정 팝업 화면 이동
	 * @param 
	 * @return 
	 * @exception Exception
	 */
	@SuppressWarnings("rawtypes")
	@RequestMapping(value="/adm/adm4000/adm4000/selectAdm4001View.do")
    public String selectAdm4001View(HttpServletRequest request, HttpServletResponse response, ModelMap model ) throws Exception {
    	try{
    		
    		//리퀘스트에서 넘어온 파라미터를 맵으로 세팅
        	Map paramMap = RequestConvertor.requestParamToMap(request, true);
        	
        	String infomap = (new GsonBuilder().serializeNulls().create()).toJson(paramMap).toString();
        	
        	model.addAttribute("infoMap", infomap.replaceAll("<", "&lt"));
        	
        	return "/adm/adm4000/adm4000/adm4001";
    	}
    	catch(Exception ex){
    		Log.error("selectAdm4000View()", ex);
    		model.addAttribute("message", egovMessageSource.getMessage("fail.common.select"));
    		throw new Exception(ex.getMessage());
    	}
    }
	
    /**
	 * Adm4002 공통코드 마스터 추가/수정 팝업 화면 이동
	 * @param 
	 * @return 
	 * @exception Exception
	 */
	@SuppressWarnings("rawtypes")
	@RequestMapping(value="/adm/adm4000/adm4000/selectAdm4002View.do")
    public String selectAdm4002View(HttpServletRequest request, HttpServletResponse response, ModelMap model ) throws Exception {
    	try{
    		
    		//리퀘스트에서 넘어온 파라미터를 맵으로 세팅
        	Map paramMap = RequestConvertor.requestParamToMap(request, true);
        	
        	String infomap = (new GsonBuilder().serializeNulls().create()).toJson(paramMap).toString();
        	
        	model.addAttribute("infoMap", infomap.replaceAll("<", "&lt"));
        	
        	return "/adm/adm4000/adm4000/adm4002";
    	}
    	catch(Exception ex){
    		Log.error("selectAdm4002View()", ex);
    		model.addAttribute("message", egovMessageSource.getMessage("fail.common.select"));
    		throw new Exception(ex.getMessage());
    	}
    }
    
	/**
	 * Adm4000 공통코드 디테일 정보를 가져온다.
	 * @param 
	 * @return 
	 * @exception Exception
	 */
	@SuppressWarnings({ "unchecked", "rawtypes" })
	@RequestMapping(value="/adm/adm4000/adm4000/selectAdm4000CommonCodeDetailListAjax.do")
    public ModelAndView selectAdm4000CommonCodeDetailListAjax(HttpServletRequest request, HttpServletResponse response, ModelMap model ) throws Exception {
    	try{
    		//리퀘스트에서 넘어온 파라미터를 맵으로 세팅
        	Map<String, String> paramMap = RequestConvertor.requestParamToMapAddSelInfo(request, true);
        	List<Map> admDetailCodeList = (List) adm4000Service.selectAdm4000CommonCodeDetailList(paramMap);

        	//PageVO 생성
    		PageVO pageVo = new PageVO();
    		pageVo.setSearchSelect(paramMap.get("searchSelect"));
    		pageVo.setSearchTxt(paramMap.get("searchTxt"));
    		pageVo.setSearchCd(paramMap.get("searchCd"));
        	
    		//pageVO 세팅
			String pageIndexStr = paramMap.get("pageNo");
			int pageIndex = 1;
			if(pageIndexStr != null && !"".equals(pageIndexStr)){
				pageIndex = Integer.parseInt(pageIndexStr);
			}
			pageVo.setPageIndex(pageIndex);
			pageVo.setPageSize(20);
			pageVo.setPageUnit(20);

			PaginationInfo paginationInfo = PagingUtil.getPaginationInfo(pageVo);
			paginationInfo.setTotalRecordCount(admDetailCodeList.size());
			
			//공통코드 디테일 정보 조회  Globals.hma.auth
			model.addAttribute("list",admDetailCodeList);

			//페이지 정보 보내기
			Map<String, Integer> pageMap = new HashMap<String, Integer>();
			
			pageMap.put("pageNo",pageVo.getPageIndex());
			pageMap.put("listCount", admDetailCodeList.size());
			pageMap.put("pageCount", paginationInfo.getTotalPageCount());
			
			model.addAttribute("page", pageMap);
			
			model.addAttribute("errorYN", "N");
			model.addAttribute("message", egovMessageSource.getMessage("success.common.select"));
			
			return new ModelAndView("jsonView");
    	}
    	catch(Exception ex){
    		Log.error("selectAdm4000CommonCodeDetailListAjax()", ex);

			//조회실패 메시지 세팅 및 저장 성공여부 세팅
			model.addAttribute("errorYN", "Y");
			model.addAttribute("message", egovMessageSource.getMessage("fail.common.select"));
			return new ModelAndView("jsonView");
    	}
    }
	
	/**
	 * Adm4000 공통코드 마스터 저장(등록,수정)(단건)
	 * 공통코드 마스터 저장후 결과 및 정보를 화면으로 리턴한다.
	 * @param 
	 * @return 
	 * @exception Exception
	 */
	@SuppressWarnings({ "rawtypes" })
	@RequestMapping(method=RequestMethod.POST, value="/adm/adm4000/adm4000/saveAdm4000CommonCodeMasterInfoAjax.do")
    public ModelAndView saveAdm4000CommonCodeMasterAjax(HttpServletRequest request, HttpServletResponse response, ModelMap model ) throws Exception {
    	try{

    		//리퀘스트에서 넘어온 파라미터를 맵으로 세팅
    		Map paramMap = RequestConvertor.requestParamToMap(request, true);
        	
        	int count=0;
        	if( "insert".equals(paramMap.get("mode") )){
        		count =  adm4000Service.selectAdm4000CommonCodeCount(paramMap);
        	}
        	if(count == 0){
        		adm4000Service.saveAdm4000CommonCodeMaster(paramMap);
        		model.addAttribute("saveYN", "Y");
        	}else{
        		model.addAttribute("saveYN", "N");
        		model.addAttribute("duplicateYN", "Y");
        	}
        	//조회성공메시지 세팅
        	model.addAttribute("message", egovMessageSource.getMessage("success.common.save"));
        	
        	return new ModelAndView("jsonView", model);
    	}
    	catch(Exception ex){
    		Log.error("insertAdm4000CommonCodeMasterAjax()", ex);
    		
    		//조회실패 메시지 세팅
    		model.addAttribute("message", egovMessageSource.getMessage("fail.common.save"));
    		return new ModelAndView("jsonView", model);
    	}
	}
	
	/**
	 * Adm4000 공통코드 마스터 삭제.(단건)
	 * 공통코드 마스터 삭제후 삭제 정보 결과 및 정보를 화면으로 리턴한다.
	 * @param 
	 * @return 
	 * @exception Exception
	 */
	@SuppressWarnings({ "rawtypes" })
	@RequestMapping(method=RequestMethod.POST, value="/adm/adm4000/adm4000/deleteAdm4000CommonCodeMasterAjax.do")
    public ModelAndView deleteAdm4000CommonCodeMasterAjax(HttpServletRequest request, HttpServletResponse response, ModelMap model ) throws Exception {
    	try{

    		//리퀘스트에서 넘어온 파라미터를 맵으로 세팅
        	Map paramMap = RequestConvertor.requestParamToMap(request,true);
        	
        	String stmUseYn = (String)paramMap.get("stmUseYn");
        	
        	// 마스터코드의 시스템 사용여부가 사용(Y)일 경우 삭제되면 안되므로 튕겨냄
        	if("Y".equals(stmUseYn)){
        		throw new Exception(egovMessageSource.getMessage("adm4000.systemUseCode.notDelete"));
			}
        	
        	adm4000Service.deleteAdm4000CommonCodeMaster(paramMap);
        	
        	//조회성공메시지 세팅
            model.addAttribute("message", egovMessageSource.getMessage("success.common.delete"));
        	
        	return new ModelAndView("jsonView", model);
    	}
    	catch(Exception ex){
    		Log.error("deleteAdm4000CommonCodeMasterAjax()", ex);
    		
    		//조회실패 메시지 세팅
    		model.addAttribute("message", egovMessageSource.getMessage("fail.common.delete"));
    		return new ModelAndView("jsonView", model);
    	}
    }
	
	/**
	 * Adm4000 공통코드 Detail 저장(등록,수정)(단건)
	 * 공통코드 마스터 저장후 결과 및 정보를 화면으로 리턴한다.
	 * @param 
	 * @return 
	 * @exception Exception
	 */
	@SuppressWarnings({ "rawtypes" })
	@RequestMapping(method=RequestMethod.POST, value="/adm/adm4000/adm4000/saveAdm4000CommonCodeDetailInfoAjax.do")
    public ModelAndView saveAdm4000CommonCodeDetailInfoAjax(HttpServletRequest request, HttpServletResponse response, ModelMap model ) throws Exception {
    	try{

    		//리퀘스트에서 넘어온 파라미터를 맵으로 세팅
        	Map paramMap = RequestConvertor.requestParamToMap(request, true);
        	
        	// 등록/수정 구분하기 위한 타입
        	String pageType = (String)paramMap.get("pageType");
        	// 마스터 코드 시스템 사용여부
        	String stmUseYn = (String)paramMap.get("stmUseYn");
        	
        	// 마스터 코드의 시스템 사용여부가 사용(Y)일 경우 디테일 코드가 등록되면 안되므로 튕겨냄
        	if("insert".equals(pageType) && "Y".equals(stmUseYn)){
        		throw new Exception(egovMessageSource.getMessage("adm4000.systemUseCode.notInsertDetail"));
			}
        	        	
        	int count=0;
        	if( "insert".equals( pageType )){
        		count =  adm4000Service.selectAdm4000CommonDetailCodeCount(paramMap);
        	}
        	if(count == 0){
        		adm4000Service.saveAdm4000CommonCodeDetail(paramMap);
        		model.addAttribute("saveYN", "Y");
            	
            	//조회성공메시지 세팅
            	model.addAttribute("message", egovMessageSource.getMessage("success.common.save"));
        	}else{
        		model.addAttribute("saveYN", "N");
        		model.addAttribute("duplicateYN", "Y");
        	}
        	        	
        	return new ModelAndView("jsonView", model);
    	}
    	catch(Exception ex){
    		Log.error("saveAdm4000CommonCodeDetailInfoAjax()", ex);
    		
    		//조회실패 메시지 세팅
    		model.addAttribute("saveYN", "N");
    		model.addAttribute("message", egovMessageSource.getMessage("fail.common.save"));
    		return new ModelAndView("jsonView", model);
    	}
	}
	
	/**
	 * Adm4000 공통코드 디테일 삭제.(단건)
	 * 공통코드 디테일 삭제후 삭제 정보 결과 및 정보를 화면으로 리턴한다.
	 * @param 
	 * @return 
	 * @exception Exception
	 */
	@SuppressWarnings({ "rawtypes" })
	@RequestMapping(method=RequestMethod.POST, value="/adm/adm4000/adm4000/deleteAdm4000CommonCodeDetailAjax.do")
    public ModelAndView deleteAdm4000CommonCodeDetailAjax(HttpServletRequest request, HttpServletResponse response, ModelMap model ) throws Exception {
    	try{

    		//리퀘스트에서 넘어온 파라미터를 맵으로 세팅
        	Map paramMap = RequestConvertor.requestParamToMap(request,true);
        	
        	String stmUseYn = (String)paramMap.get("stmUseYn");
        	
        	// 마스터코드의 시스템 사용여부가 사용(Y)일 경우 디테일코드도 삭제되면 안되므로 튕겨냄
        	if("Y".equals(stmUseYn)){
        		throw new Exception(egovMessageSource.getMessage("adm4000.systemUseCode.notDelete"));
			}
        	
        	adm4000Service.deleteAdm4000CommonCodeDetail(paramMap);
        	
        	//조회성공메시지 세팅
            model.addAttribute("message", egovMessageSource.getMessage("success.common.delete"));
        	
        	return new ModelAndView("jsonView", model);
    	}
    	catch(Exception ex){
    		Log.error("deleteAdm4000CommonCodeDetailAjax()", ex);
    		
    		//조회실패 메시지 세팅
    		model.addAttribute("message", egovMessageSource.getMessage("fail.common.delete"));
    		return new ModelAndView("jsonView", model);
    	}
    }
	
	/**
	 * Adm4000(공통코드) 엑셀 다운로드
	 * @param 
	 * @return 
	 * @exception Exception
	 */
	@RequestMapping(
				value="/adm/adm4000/adm4000/selectAdm4000ExcelList.do" 				
				,params={"MasterOrDetail","mstCd"}
			)
	public ModelAndView selectAdm4000ExcelList(@RequestParam(value="MasterOrDetail") String type,
			@RequestParam(value="mstCd") String mstCd,
			HttpServletRequest request, 
			HttpServletResponse response, ModelMap model ) throws Exception {

		SheetHeader header = null;
		/* 조회되는 데이터와 포멧 지정 
		 * ex 1. 수치형 new Metadata("property", XSSFCellStyle.ALIGN_RIGHT, "#,##0")
		 * ex 2. 사용자 지정 날짜 변환 new Metadata("property", ,"00-00-00") YYMMDD -> YY-MM-DD
		 * */
		List<Metadata> metadatas = new ArrayList<Metadata>();
		//엑셀 다운로드 양식의 헤더명 선언
		if(type.toLowerCase().equals("master")){
			header = new SheetHeader(new String[]{
					egovMessageSource.getMessage("excel.mstCd")
					,egovMessageSource.getMessage("excel.upperMstCd")
					,egovMessageSource.getMessage("excel.mstCdNm")
					,egovMessageSource.getMessage("excel.mstCdDesc")
					,egovMessageSource.getMessage("excel.ord")
					,egovMessageSource.getMessage("excel.useYn")});
			
			metadatas.add(new Metadata("mstCd"));
			metadatas.add(new Metadata("upperMstCd"));
			metadatas.add(new Metadata("mstCdNm"));
			metadatas.add(new Metadata("mstCdDesc"));
			metadatas.add(new Metadata("ord"));		        
			metadatas.add(new Metadata("useYn"));	
		}else{
			header = new SheetHeader(new String[]{
					egovMessageSource.getMessage("excel.mstCd")
					,egovMessageSource.getMessage("excel.subCd")
					,egovMessageSource.getMessage("excel.subCdNm")
					,egovMessageSource.getMessage("excel.subCdRef1")
					,egovMessageSource.getMessage("excel.subCdRef2")
					,egovMessageSource.getMessage("excel.subCdDesc")
					,egovMessageSource.getMessage("excel.ord")
					,egovMessageSource.getMessage("excel.useYn")});
			
			metadatas.add(new Metadata("mstCd"));
			metadatas.add(new Metadata("subCd"));
			metadatas.add(new Metadata("subCdNm"));
			metadatas.add(new Metadata("subCdRef1"));
			metadatas.add(new Metadata("subCdRef2"));		        
			metadatas.add(new Metadata("subCdDesc"));	
			metadatas.add(new Metadata("ord"));
			metadatas.add(new Metadata("useYn"));
		}

		BigDataSheetWriter writer = new BigDataSheetWriter(
				egovMessageSource.getMessage("excel.adm4000.sheetNm")
				, tempPath
				, egovMessageSource.getMessage("excel.adm4000.sheetNm")
				, metadatas);

		writer.beginSheet();

		try{

			HttpSession ss = request.getSession();
			LoginVO loginVO = (LoginVO) ss.getAttribute("loginVO");
			
			
			
			ExcelDataListResultHandler  resultHandler = new ExcelDataListResultHandler(writer.getXMLSheetWriter(), writer.getStyleMap(), header, metadatas);
			if(type.toLowerCase().equals("master")){
				adm4000Service.selectAdm4000MasterExcelList(loginVO,resultHandler);
			}else{
				Map<String, String> map = new HashMap<>();
				map.put("licGrpId", loginVO.getLicGrpId());
				map.put("mstCd", mstCd);
				adm4000Service.selectAdm4000DetailExcelList(map,resultHandler);
			}
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
