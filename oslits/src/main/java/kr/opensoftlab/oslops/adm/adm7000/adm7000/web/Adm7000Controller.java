package kr.opensoftlab.oslops.adm.adm7000.adm7000.web;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import kr.opensoftlab.oslops.adm.adm7000.adm7000.service.Adm7000Service;
import kr.opensoftlab.oslops.com.vo.LoginVO;
import kr.opensoftlab.sdf.excel.BigDataSheetWriter;
import kr.opensoftlab.sdf.excel.ExcelDataListResultHandler;
import kr.opensoftlab.sdf.excel.Metadata;
import kr.opensoftlab.sdf.excel.SheetHeader;
import kr.opensoftlab.sdf.util.RequestConvertor;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;

import egovframework.com.cmm.EgovMessageSource;
import egovframework.com.utl.fcc.service.EgovStringUtil;
import egovframework.rte.fdl.property.EgovPropertyService;


/**
 * @Class Name : Adm7000Controller.java
 * @Description : 조직 관리(Adm7000) 컨트롤러 클래스
 * @Modification Information
 * <pre>
 *   수정일      수정자          수정내용
 *  -------    --------    ---------------------------
 *  2018.08.01  배용진          최초 생성
 *  
 * </pre>
 *  @author 배용진
 *  @version 1.0
 *  @see 
 *  
 *  Copyright (C) Open Soft Lab Corp. All Rights Reserved
 */
@Controller
public class Adm7000Controller {
	
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
	
	/** Adm7000Service DI */
	@Resource(name = "adm7000Service")
	private Adm7000Service adm7000Service;
	
	/** 파일 경로 **/
	@Value("${Globals.fileStorePath}")
	private String tempPath;
	
    /**
	 * Adm7000 조직 관리 화면이동
	 * <br> - 조직관리 메뉴 클릭 시 조직관리 화면으로 이동한다.
	 * @param 
	 * @return 
	 * @exception Exception
	 */
	@SuppressWarnings({ "unchecked", "rawtypes" })
	@RequestMapping(value="/adm/adm7000/adm7000/selectAdm7000View.do")
    public String selectAdm7000View(HttpServletRequest request, HttpServletResponse response, ModelMap model ) throws Exception {
		
		try{
			//리퀘스트에서 넘어온 파라미터를 맵으로 세팅
			Map paramMap = RequestConvertor.requestParamToMapAddSelInfo(request, true);

			//로그인VO 가져오기
    		HttpSession ss = request.getSession();
    		LoginVO loginVO = (LoginVO) ss.getAttribute("loginVO");
    		
    		paramMap.put("licGrpId", loginVO.getLicGrpId());
    		paramMap.put("deptName", egovMessageSource.getMessage("adm7000.success.deptTitle"));
    		
    		// 등록된 조직 목록 조회
    		List<Map> deptList = (List)adm7000Service.selectAdm7000DeptList(paramMap);
        	
        	model.addAttribute("deptList", deptList);
			
			return "/adm/adm7000/adm7000/adm7000";
		}
		catch(Exception ex){
			Log.error("selectAdm7000View()", ex);
			throw new Exception(ex.getMessage());
		}
    }
	
    /**
	 * Adm7000 조직 추가 팝업창 오픈
	 * <br> - 추가버튼 클릭 시 등록 팝업창을 연다.
	 * @param 
	 * @return 
	 * @exception Exception
	 */
	@RequestMapping(value="/adm/adm7000/adm7000/selectAdm7001View.do")
    public String selectAdm7001View(HttpServletRequest request, HttpServletResponse response, ModelMap model ) throws Exception {
		return "/adm/adm7000/adm7000/adm7001";
	}
	
	
	/**
	 * ADM7000  조직 목록 조회(List) Ajax
	 * <br> - 조직목록을 조회한다.
	 * @param 
	 * @return 
	 * @exception Exception
	 */
	@SuppressWarnings({ "rawtypes", "unchecked" })
	@RequestMapping(value="/adm/amd7000/adm7000/selectAdm7000DeptListAjax.do")
	public ModelAndView selectAdm7000DeptListAjax(HttpServletRequest request, HttpServletResponse response, ModelMap model ) throws Exception {
		
		try{			
			//리퀘스트에서 넘어온 파라미터를 맵으로 세팅
			Map paramMap = RequestConvertor.requestParamToMapAddSelInfo(request, true);
			
			// 등록된 조직 목록 조회
    		List<Map> deptList = (List)adm7000Service.selectAdm7000DeptList(paramMap);
			
    		//조회 성공메시지 세팅
			model.addAttribute("deptList", deptList);
			model.addAttribute("errorYn", "N");
			model.addAttribute("message", egovMessageSource.getMessage("success.common.select"));
    		
			return new ModelAndView("jsonView");
			
		}catch(Exception ex){
			Log.error("selectAdm7000DeptListAjax()", ex);

			//조회실패 메시지 세팅
			model.addAttribute("errorYn", "Y");
			model.addAttribute("message", egovMessageSource.getMessage("fail.common.select"));
			return new ModelAndView("jsonView");
		}
	}
	
	/**
	 * Amd7000 조직 조회(단건) AJAX
	 * <br> - 1건의 조직 정보를 조회한다(상세조회)
	 * @param 
	 * @return 
	 * @exception Exception
	 */
	@SuppressWarnings({ "rawtypes", "unchecked" })
	@RequestMapping(value="/adm/adm7000/adm7000/selectAdm7000DeptInfoAjax.do")
	public ModelAndView selectAdm7000DeptInfoAjax(HttpServletRequest request, HttpServletResponse response, ModelMap model ) throws Exception {
		
		try{		
			//리퀘스트에서 넘어온 파라미터를 맵으로 세팅
			Map paramMap = RequestConvertor.requestParamToMapAddSelInfo(request, true);
			
			//조직 정보조회
			Map deptInfoMap = (Map) adm7000Service.selectAdm7000DeptInfo(paramMap);
			
			//조회성공메시지 세팅
			model.addAttribute("message", egovMessageSource.getMessage("success.common.select"));

			return new ModelAndView("jsonView", deptInfoMap);
			
		}catch(Exception ex){
			Log.error("selectAdm7000DeptInfoAjax()", ex);

			//조회실패 메시지 세팅
			model.addAttribute("message", egovMessageSource.getMessage("fail.common.select"));
			return new ModelAndView("jsonView");
		}
	}	
	
	/**
	 * Adm7000 조직 등록(단건) AJAX
	 * <br> - 조직 등록 작업 후 등록 결과 및 등록 정보를 화면으로 리턴한다.
	 * @param 
	 * @return 
	 * @exception Exception
	 */
	@SuppressWarnings({ "rawtypes", "unchecked" })
	@RequestMapping(value="/adm/adm7000/adm7000/insertAdm7000DeptInfoAjax.do")
	public ModelAndView insertAdm7000DeptInfoAjax(HttpServletRequest request, HttpServletResponse response, ModelMap model ) throws Exception {
		
		try{
			//리퀘스트에서 넘어온 파라미터를 맵으로 세팅
			Map paramMap = RequestConvertor.requestParamToMapAddSelInfo(request, true);
			
			// 신규 조직 등록
			Map deptInfoMap = (Map)adm7000Service.insertAdm7000DeptInfo(paramMap);
			
			//등록 성공 메시지 세팅
			model.addAttribute("errorYn", "N");
			model.addAttribute("message", egovMessageSource.getMessage("success.common.insert"));
			
			return new ModelAndView("jsonView", deptInfoMap);
		
		}catch(Exception ex){
			Log.error("insertAdm7000DeptInfoAjax()", ex);

			//등록실패 메시지 세팅
			model.addAttribute("errorYn", "Y");
			model.addAttribute("message", egovMessageSource.getMessage("fail.common.insert"));
			return new ModelAndView("jsonView");
		}
	}
	
	
	/**
	 * Adm7000 조직 수정(단건) AJAX
	 * <br> - 조직 정보를 수정한다.
	 * @param 
	 * @return 
	 * @exception Exception
	 */
	@SuppressWarnings({ "rawtypes", "unchecked" })
	@RequestMapping(value="/adm/adm7000/adm7000/updateAdm7000DpteInfoAjax.do")
	public ModelAndView updateAdm7000DpteInfoAjax(HttpServletRequest request, HttpServletResponse response, ModelMap model ) throws Exception {
		
		try{			
			//리퀘스트에서 넘어온 파라미터를 맵으로 세팅
			Map paramMap = RequestConvertor.requestParamToMapAddSelInfo(request, true);
			
			// 조직정보 수정
			adm7000Service.updateAdm7000DpteInfo(paramMap);
		
			//수정 성공 메시지 세팅
			model.addAttribute("errorYn", "N");
			model.addAttribute("message", egovMessageSource.getMessage("success.common.update"));

			return new ModelAndView("jsonView");
			
		}catch(Exception ex){
			Log.error("updateAdm7000DpteInfoAjax()", ex);

			//수정실패 메시지 세팅
			model.addAttribute("errorYn", "Y");
			model.addAttribute("message", egovMessageSource.getMessage("fail.common.update"));
			return new ModelAndView("jsonView");
		}	
	}	
	
	
	/**
	 * Adm7000 조직 정보 삭제(단건) AJAX
	 * <br> - 조직 정보를 삭제한다.
	 * @param 
	 * @return 
	 * @exception Exception
	 */
	@SuppressWarnings("rawtypes")
	@RequestMapping(value="/adm/adm7000/amd7000/deleteAdm7000DeptInfoAjax.do")
	public ModelAndView deleteAdm7000DeptInfoAjax(HttpServletRequest request, HttpServletResponse response, ModelMap model ) throws Exception {
		
		try{
			//리퀘스트에서 넘어온 파라미터를 맵으로 세팅
			Map paramMap = RequestConvertor.requestParamToMapAddSelInfo(request, true);
			
			// 조직정보 삭제
			adm7000Service.deleteAdm7000DeptInfo(paramMap);
			
			// 삭제 성공 메시지 세팅
			model.addAttribute("errorYn", "N");
			model.addAttribute("message", egovMessageSource.getMessage("success.common.delete"));
			
			return new ModelAndView("jsonView");
			
		}catch(Exception ex){
			Log.error("deleteAdm7000DeptInfoAjax()", ex);

			//수정실패 메시지 세팅
			model.addAttribute("errorYn", "Y");
			model.addAttribute("message", egovMessageSource.getMessage("fail.common.delete"));
			return new ModelAndView("jsonView");
		}
	}
	
	
	/**
	 * ADM7000  상위조직 목록 조회(List) Ajax
	 * <br> - 선택한 조직의 상위 조직명을 조회한다.
	 * @param 
	 * @return 
	 * @exception Exception
	 */
	@SuppressWarnings({ "rawtypes", "unchecked" })
	@RequestMapping(value="/adm/amd7000/adm7000/selectAdm7000UpperDeptListAjax.do")
	public ModelAndView selectAdm7000UpperDeptListAjax(HttpServletRequest request, HttpServletResponse response, ModelMap model ) throws Exception {
		
		try{			
			//리퀘스트에서 넘어온 파라미터를 맵으로 세팅
			Map paramMap = RequestConvertor.requestParamToMapAddSelInfo(request, true);
			
			// 선택된 조직의 상위 조직명을 조회
    		List updeptList = (List)adm7000Service.selectAdm7000UpperDeptList(paramMap);
			
    		//조회 성공메시지 세팅
			model.addAttribute("upperDeptList", updeptList);
			model.addAttribute("message", egovMessageSource.getMessage("success.common.select"));
    		
			return new ModelAndView("jsonView");
			
		}catch(Exception ex){
			Log.error("selectAdm7000UpperDeptListAjax()", ex);

			//조회실패 메시지 세팅
			model.addAttribute("message", egovMessageSource.getMessage("fail.common.select"));
			return new ModelAndView("jsonView");
		}
	}

	// end
	
	
	// TODO
	/**
	 * 요구사항 엑셀 업로드시 요청자 ID가 존재하는지 체크
	 * @param request
	 * @param model
	 * @return
	 * @throws Exception
	 */
	@SuppressWarnings({ "rawtypes", "unchecked" })
	@RequestMapping(value="/adm/amd7000/adm7000/selectAdm7000ExistDeptChk.do")
	public ModelAndView selectAdm7000ExistDeptChk(HttpServletRequest request, HttpServletResponse response, ModelMap model )	throws Exception {

		// 값을 넘기기 위한 HasMap
		HashMap<String, String> chkMap = new HashMap<String, String>();
		try{

			// request 파라미터를 map으로 변환
			Map param = RequestConvertor.requestParamToMap(request, true);

			// 엑셀에 입력된 조직ID
			String inDeptId = (String)param.get("inDeptId");
			param.put("deptId", inDeptId);

			int chkDeptId  = 0;
			
			// DB 조직 정보 조회
			chkDeptId = adm7000Service.selectAdm7000ExistDeptChk(param);
			
			// 조직 정보 조회 결과 확인
			if( chkDeptId == 0) {
				// 조직 정보 DB 조회 결과 없음
				chkMap.put("chkDeptId", "N");
			}else{
				chkMap.put("chkDeptId", "Y");
			}

			//조회 성공 메시지 세팅
			model.addAttribute("errorYn", "N");
			model.addAttribute("message", egovMessageSource.getMessage("success.common.select"));
			
			return new ModelAndView("jsonView", chkMap);

		}catch(Exception e){
			Log.error("selectAdm7000ExistDeptChk()", e);
			model.addAttribute("errorYn", "Y");
			model.addAttribute("message", egovMessageSource.getMessage("fail.common.save"));
			return new ModelAndView("jsonView");
		}
	}
	
	
	/**
	 * 조직관리 엑셀 조회
	 * @param 
	 * @return 
	 * @exception Exception
	 */
	@SuppressWarnings({ "rawtypes" })
	@RequestMapping(value="/adm/amd7000/adm7000/selectAdm7000ExcelList.do")
	public ModelAndView selectAdm7000ExcelList(HttpServletRequest request, HttpServletResponse response, ModelMap model ) throws Exception {
		//리퀘스트에서 넘어온 파라미터를 맵으로 세팅
		Map paramMap = RequestConvertor.requestParamToMapAddSelInfo(request, true);

		//엑셀 다운로드 양식의 헤더명 선언
		String strDeptId = egovMessageSource.getMessage("excel.deptId");			// 조직 ID
		String strUpperDeptId = egovMessageSource.getMessage("excel.upperDeptId");	// 상위조직 ID
		String strDeptName = egovMessageSource.getMessage("excel.deptName");		// 조직명
		String strLvl = egovMessageSource.getMessage("excel.lvl");					// 레벨
		String strOrd = egovMessageSource.getMessage("excel.ord");					// 순번
		String strDeptEtc = egovMessageSource.getMessage("excel.deptEtc");			// 비고
		
		SheetHeader header = new SheetHeader(new String[]{strDeptId, strUpperDeptId, strDeptName, strLvl, strOrd, strDeptEtc});
		
		/* 조회되는 데이터와 포멧 지정 
		 * ex 1. 수치형 new Metadata("property", XSSFCellStyle.ALIGN_RIGHT, "#,##0")
		 * ex 2. 사용자 지정 날짜 변환 new Metadata("property", ,"00-00-00") YYMMDD -> YY-MM-DD
		 * */
		List<Metadata> metadatas = new ArrayList<Metadata>(); 
		metadatas.add(new Metadata("deptId"));
		metadatas.add(new Metadata("upperDeptId"));
		metadatas.add(new Metadata("deptName"));
		metadatas.add(new Metadata("lvl"));		        
		metadatas.add(new Metadata("ord"));
		metadatas.add(new Metadata("deptEtc"));

		BigDataSheetWriter writer = new BigDataSheetWriter(egovMessageSource.getMessage("excel.adm7000.sheetNm"), tempPath, egovMessageSource.getMessage("excel.adm7000.sheetNm"), metadatas);

		writer.beginSheet();

		try{

			ExcelDataListResultHandler  resultHandler = new ExcelDataListResultHandler(writer.getXMLSheetWriter(), writer.getStyleMap(), header, metadatas);

			adm7000Service.selectAdm7000ExcelList(paramMap,resultHandler);

		}
		catch(Exception ex){
			Log.error("selectAdm7000ExcelList()", ex);
			throw new Exception(ex.getMessage());
		}finally{
			writer.endSheet();
		}

		return writer.getModelAndView();
	}
	
}
