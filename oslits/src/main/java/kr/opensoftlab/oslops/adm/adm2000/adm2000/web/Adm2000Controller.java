package kr.opensoftlab.oslops.adm.adm2000.adm2000.web;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;
import java.util.regex.Pattern;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.multipart.MultipartHttpServletRequest;
import org.springframework.web.servlet.ModelAndView;

import egovframework.com.cmm.EgovMessageSource;
import egovframework.com.utl.fcc.service.EgovStringUtil;
import egovframework.rte.fdl.idgnr.EgovIdGnrService;
import egovframework.rte.ptl.mvc.tags.ui.pagination.PaginationInfo;
import kr.opensoftlab.oslops.adm.adm2000.adm2000.service.Adm2000Service;
import kr.opensoftlab.oslops.adm.adm2000.adm2000.vo.Adm2000VO;
import kr.opensoftlab.oslops.adm.adm5000.adm5200.service.Adm5200Service;
import kr.opensoftlab.oslops.com.fms.web.service.FileMngService;
import kr.opensoftlab.oslops.com.vo.LoginVO;
import kr.opensoftlab.sdf.excel.BigDataSheetWriter;
import kr.opensoftlab.sdf.excel.ExcelDataListResultHandler;
import kr.opensoftlab.sdf.excel.Metadata;
import kr.opensoftlab.sdf.excel.SheetHeader;
import kr.opensoftlab.sdf.excel.SheetParser;
import kr.opensoftlab.sdf.util.OslAgileConstant;
import kr.opensoftlab.sdf.util.PagingUtil;
import kr.opensoftlab.sdf.util.RequestConvertor;


/**
 * @Class Name : Amd2000Controller.java
 * @Description : Amd2000Controller Controller class
 * @Modification Information
 * <pre>
 *   수정일      수정자          수정내용
 *  -------    --------    ---------------------------
 *  2016.01.28  김정환          최초 생성
 *  2018.08.10  배용진          차단여부 수정 추가
 *  
 * </pre>
 * @author 김정환
 * @since 2016.01.28.
 * @version 1.0
 * @see
 *  
 *  Copyright (C) Open Soft Lab Corp. All Rights Reserved
 */
@Controller
public class Adm2000Controller {

	/** 로그4j 로거 로딩 */
	private static final Logger Log = Logger.getLogger(Adm2000Controller.class);
	
    /** EgovMessageSource */
	@Resource(name = "egovMessageSource")
	EgovMessageSource egovMessageSource;
	
	/** Adm2000Service DI */
    @Resource(name = "adm2000Service")
    private Adm2000Service adm2000Service;
    
	/** Adm5200Service DI */
    @Resource(name = "adm5200Service")
    private Adm5200Service adm5200Service;
    
	@Value("${Globals.fileStorePath}")
	private String tempPath;
	
	@Resource(name = "egovFileIdGnrService")
	private EgovIdGnrService idgenService;
	
	/** FileMngService */
   	@Resource(name="fileMngService")
   	private FileMngService fileMngService;
   	
    
    /**
	 * ADM2000 사용자 정보 페이지 진입  
	 * @param 
	 * @return 
	 * @exception Exception
	 */
	@RequestMapping(value="/adm/adm2000/adm2000/selectAdm2000View.do")
    public String selectAdm2000View(HttpServletRequest request, HttpServletResponse response, ModelMap model ) throws Exception {
    	return "/adm/adm2000/adm2000/adm2000";
    }
	
	/**
	 * ADM2002 사용자 일괄등록 팝업 페이지 진입  
	 * @param 
	 * @return 
	 * @exception Exception
	 */
	@RequestMapping(value="/adm/adm2000/adm2000/selectAdm2002View.do")
	public String selectAdm2002View(HttpServletRequest request, HttpServletResponse response, ModelMap model ) throws Exception {
			return "/adm/adm2000/adm2000/adm2002";
	}
    
    /**
     * ADM2000 사용자 정보 목록 조회  
     * @param request
     * @param response
     * @throws Exception
     */
	@RequestMapping(value="/adm/adm2000/adm2000/selectAdm2000ListAjax.do")
	public ModelAndView selectAdm2000View(@ModelAttribute("adm2000VO") Adm2000VO adm2000VO, HttpServletRequest request, HttpServletResponse response, ModelMap model)	throws Exception {
		
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
			adm2000VO.setPageIndex(_pageNo);
			adm2000VO.setPageSize(_pageSize);
			adm2000VO.setPageUnit(_pageSize);
        	
    		PaginationInfo paginationInfo = PagingUtil.getPaginationInfo(adm2000VO);  /** paging - 신규방식 */
        	List<Adm2000VO> adm2000List = null;

			HttpSession ss = request.getSession();
			LoginVO loginVO = (LoginVO) ss.getAttribute("loginVO");
			adm2000VO.setLicGrpId(loginVO.getLicGrpId());

    		// 목록 조회
		    adm2000List = adm2000Service.selectAdm2000UsrList(adm2000VO);
		    
    		// 총 건수
		    int totCnt = adm2000Service.selectAdm2000UsrListCnt(adm2000VO);
		    paginationInfo.setTotalRecordCount(totCnt);
		    
		    model.addAttribute("list", adm2000List);
		    
		    //페이지 정보 보내기
			Map<String, Integer> pageMap = new HashMap<String, Integer>();
			pageMap.put("pageNo",adm2000VO.getPageIndex());
			pageMap.put("listCount", adm2000List.size());
			pageMap.put("totalPages", paginationInfo.getTotalPageCount());
			pageMap.put("totalElements", totCnt);
			pageMap.put("pageSize", _pageSize);

			model.addAttribute("page", pageMap);
        	
        	//조회성공메시지 세팅
        	model.addAttribute("message", egovMessageSource.getMessage("success.common.select"));
        	
        	return new ModelAndView("jsonView", model);
    	}catch(Exception ex){
    		Log.error("selectAdm2000UsrList()", ex);
    		
    		model.addAttribute("selectYN", "N");
    		//조회실패 메시지 세팅
    		model.addAttribute("message", egovMessageSource.getMessage("fail.common.select"));
    		return new ModelAndView("jsonView", model);
    	}
	}	
    
	
    
    /**
	 * ADM2001 레이어 팝업
	 * @param 
	 * @return 
	 * @exception Exception
	 */
    @SuppressWarnings("rawtypes")
	@RequestMapping(value="/adm/adm2000/adm2000/selectAdm2001View.do")
    public String selectAdm2001UsrInfo(HttpServletRequest request, HttpServletResponse response, ModelMap model) throws Exception {
    	
		// request 파라미터를 map으로 변환
    	Map<String, String> param = RequestConvertor.requestParamToMap(request, true);
    	
    	Map adm2000UsrInfo = null;
    	
		HttpSession ss = request.getSession();
		LoginVO loginVO = (LoginVO) ss.getAttribute("loginVO");
		
		param.put("licGrpId", loginVO.getLicGrpId());
		
    	if( "U".equals(param.get("proStatus"))){ // 수정일 경우
    		adm2000UsrInfo = adm2000Service.selectAdm2000UsrInfo( param );
    	}else if( "S".equals(param.get("proStatus"))){ // 상세보기일 경우
    		adm2000UsrInfo = adm2000Service.selectAdm2000UsrInfo( param );
    	}

    	model.put("adm2000UsrInfo", adm2000UsrInfo);
    	
    	return "/adm/adm2000/adm2000/adm2001";
    }
    
    /**
     * 사용자 등록 및 수정
     * @param request
     * @param response
     * @param model
     * @return
     * @throws Exception
     */
	@RequestMapping(value="/adm/adm2000/adm2000/insertAdm2000UsrInfo.do")
    public ModelAndView saveAdm2000UsrInfo(HttpServletRequest request, HttpServletResponse response, ModelMap model ) throws Exception {
		try{
			// request 파라미터를 map으로 변환
        	Map<String, String> paramMap = RequestConvertor.requestParamToMap(request, true);
        	String proStatus = (String) paramMap.get("proStatus");
    		//로그인VO 가져오기
			HttpSession ss = request.getSession();
			LoginVO loginVO = (LoginVO) ss.getAttribute("loginVO");
			
    		paramMap.put("licGrpId", loginVO.getLicGrpId());	// 사용자 라이선스 그룹
        	paramMap.put("regUsrId", loginVO.getUsrId()); 		// 사용자 ID
        	paramMap.put("regUsrIp", request.getRemoteAddr()); // 사용자 IP

        	// 사용자명
    		String usrNm = (String)paramMap.get("usrNm");
    		
    		// 사용자명 정규식 체크
    		if(!Pattern.matches("^[0-9a-zA-Zㄱ-ㅎㅏ-ㅣ가-힣_-]*$", usrNm)){
    			// 정규식에 해당하지 않을 경우
    			model.addAttribute("errorYn", "Y");
    			model.addAttribute("message", "이름은 한글, 영문, 숫자, 특수문자( _ -) 만 입력가능합니다.");
    			return new ModelAndView("jsonView", model);
    	    }
        	
        	adm2000Service.saveAdm2000UsrInfo(paramMap);
        	
        	//등록 성공 메시지 세팅
        	model.addAttribute("successYn", "Y");
        	
        	if( "I".equals(proStatus)){
        		model.addAttribute("message", egovMessageSource.getMessage("success.common.insert"));
    		}
    		if("U".equals(proStatus)){
    			model.addAttribute("message", egovMessageSource.getMessage("success.common.update"));
    		}
        	
        	
        	return new ModelAndView("jsonView");
        
		}catch(Exception e){
			Log.error("saveAdm2000UsrInfo()", e);
			// 저장 실패여부 및 저장실패 메시지 세팅
    		model.addAttribute("successYn", "N");
    		model.addAttribute("message", egovMessageSource.getMessage("fail.common.insert"));
    		
    		return new ModelAndView("jsonView");
		}

	}
	
	/**
	 * 아이디 중복 체크
	 * @param request
	 * @param model
	 * @return
	 * @throws Exception
	 */
	@RequestMapping(value = "/adm/adm2000/adm2000/selectCmm2000IdCheck.do", produces="text/plain;charset=UTF-8")
	public ModelAndView selectCmm2000IdCheck (HttpServletRequest request, ModelMap model) throws Exception   {

		
		try{
			// request 파라미터를 map으로 변환
			Map<String, String> param = RequestConvertor.requestParamToMap(request, true);

			// 사용자 입력 정보 할당
			String usrId = param.get("usrId");

			// 아이디 입력값이 없을 경우 retrun
			if( "".equals(EgovStringUtil.isNullToString(usrId)) ) {
				
				model.put("idChk", "N");
				model.addAttribute("message", egovMessageSource.getMessage("fail.required.dupid"));

				return new ModelAndView("jsonView", model);
			}
			
			// DB 사용자 정보 조회
			int idChkCnt =  adm2000Service.selectCmm2000IdCheck( param );
			
			// DB 사용자 정보 조회 결과 확인
			if( idChkCnt == 0) {
				// 사용자 정보 DB 조회 결과 없음
				model.put("chkId", "Y");
				model.addAttribute("message", egovMessageSource.getMessage("success.common.dupid"));
			}else{
				model.put("chkId", "N");
				model.addAttribute("message", egovMessageSource.getMessage("fail.common.dupid"));
			}
			
			return new ModelAndView("jsonView", model);
			
		}catch(Exception e){
			Log.error("selectCmm2000IdCheck", e);
			model.addAttribute("message", egovMessageSource.getMessage("fail.common.select"));
			throw new Exception(e.getMessage());
		}
	}
	
	/**
	 * 이메일 중복 체크
	 * @param request
	 * @param model
	 * @return
	 * @throws Exception
	 */
	@RequestMapping(value = "/adm/adm2000/adm2000/selectCmm2000EmailCheck.do", produces="text/plain;charset=UTF-8")
	public ModelAndView selectCmm2000EmailCheck (HttpServletRequest request, ModelMap model) throws Exception   {

		try{
			// request 파라미터를 map으로 변환
			Map<String, String> param = RequestConvertor.requestParamToMap(request, true);

			// 사용자 입력 정보 할당
			String usrEmail = param.get("usrEmail");

			// 이메일 입력값이 없을 경우 retrun
			if( "".equals(EgovStringUtil.isNullToString(usrEmail)) ) {
				
				model.put("chkEmail", "N");
				model.addAttribute("message", egovMessageSource.getMessage("fail.required.email"));
				
				return new ModelAndView("jsonView", model);
			}
			
			// DB 사용자 정보 조회
			int idChkCnt =  adm2000Service.selectCmm2000EmailCheck( param );
			
			// DB 사용자 정보 조회 결과 확인
			if( idChkCnt == 0) {
				// 사용자 정보 DB 조회 결과 없음
				model.put("chkEmail", "Y");
				model.addAttribute("message", egovMessageSource.getMessage("success.common.email"));
			}else{
				model.put("chkEmail", "N");
				model.addAttribute("message", egovMessageSource.getMessage("fail.common.email"));
			}
			
			return new ModelAndView("jsonView", model);
			
		}catch(Exception e){
			Log.error("selectCmm2000EmailCheck", e);
			model.addAttribute("message", egovMessageSource.getMessage("fail.common.select"));
			throw new Exception(e.getMessage());
		}
	}
	
	
	/**
	 * 사용 유무 수정
	 * @param request
	 * @param response
	 * @param model
	 * @return
	 * @throws Exception
	 */
	@RequestMapping(value="/adm/adm2000/adm2000/updateAdm2000UseCd.do")
    public ModelAndView updateAdm2000UseCd(HttpServletRequest request, HttpServletResponse response, ModelMap model ) throws Exception {
		try{
			// request 파라미터를 map으로 변환
        	Map<String, String> paramMap = RequestConvertor.requestParamToMap(request, true);
        	
    		//로그인VO 가져오기
			HttpSession ss = request.getSession();
			LoginVO loginVO = (LoginVO) ss.getAttribute("loginVO");
			
    		paramMap.put("licGrpId", loginVO.getLicGrpId());		// 사용자 라이선스 그룹
        	paramMap.put("regUsrId", loginVO.getUsrId()); 			// 사용자 ID
        	paramMap.put("regUsrIp", request.getRemoteAddr()); // 사용자 IP
        	
        	adm2000Service.updateAdm2000UseCd(paramMap);
        	
        	//등록 성공 메시지 세팅
        	model.addAttribute("successYn", "Y");
        	model.addAttribute("message", egovMessageSource.getMessage("success.common.save"));
        	
        	return new ModelAndView("jsonView");
        	
		}catch(Exception e){
			Log.error("saveAdm2000UsrInfo()", e);
			
    		model.addAttribute("successYn", "N");
    		model.addAttribute("message", egovMessageSource.getMessage("fail.common.save"));
    		
    		return new ModelAndView("jsonView");
		}

	}
	
	
	/**
	 * 사용자 삭제
	 * @param request
	 * @param response
	 * @param model
	 * @return
	 * @throws Exception
	 */
	@RequestMapping(method=RequestMethod.POST, value="/adm/adm2000/adm2000/deleteAdm2000UsrInfo.do")
    public ModelAndView deleteAdm2000UsrInfo(HttpServletRequest request, HttpServletResponse response, ModelMap model ) throws Exception {
		try{
			// request 파라미터를 map으로 변환
        	Map<String, String> paramMap = RequestConvertor.requestParamToMap(request, true);
        	
    		//로그인VO 가져오기
			HttpSession ss = request.getSession();
			LoginVO loginVO = (LoginVO) ss.getAttribute("loginVO");
			
    		paramMap.put("licGrpId", loginVO.getLicGrpId());		// 사용자 라이선스 그룹
        	paramMap.put("regUsrId", loginVO.getUsrId()); 			// 사용자 ID
        	paramMap.put("regUsrIp", request.getRemoteAddr()); // 사용자 IP
        	
        	adm2000Service.deleteAdm2000UsrInfo(paramMap);
        	
        	// 삭제 성공 메시지 세팅
        	model.addAttribute("successYn", "Y");
        	model.addAttribute("message", egovMessageSource.getMessage("success.common.delete"));
        	
        	return new ModelAndView("jsonView");
        	
		}catch(Exception e){
			Log.error("saveAdm2000UsrInfo()", e);
			
    		model.addAttribute("successYn", "N");
    		model.addAttribute("message", e.getMessage()==null?egovMessageSource.getMessage("fail.common.delete"):e.getMessage());
    		
    		return new ModelAndView("jsonView");
		}

	}
	
	
	/**
	 * Adm2000 화면 이동(이동시 요구사항 목록 조회)
	 * @param 
	 * @return 
	 * @exception Exception
	 */
	@RequestMapping(value="/adm/adm2000/adm2000/selectAdm2000ExcelList.do")
	public ModelAndView selectAdm2000ExcelList(@ModelAttribute("adm2000VO") Adm2000VO adm2000VO, HttpServletRequest request, HttpServletResponse response, ModelMap model ) throws Exception {


		//엑셀 다운로드 양식의 헤더명 선언
		SheetHeader header = new SheetHeader(new String[]{
				egovMessageSource.getMessage("excel.useCd")
				,egovMessageSource.getMessage("excel.usrId")
				,egovMessageSource.getMessage("excel.usrNm")
				,egovMessageSource.getMessage("excel.telno")
				,egovMessageSource.getMessage("excel.email")
				,egovMessageSource.getMessage("excel.etc")});

		/* 조회되는 데이터와 포멧 지정 
		 * ex 1. 수치형 new Metadata("property", XSSFCellStyle.ALIGN_RIGHT, "#,##0")
		 * ex 2. 사용자 지정 날짜 변환 new Metadata("property", ,"00-00-00") YYMMDD -> YY-MM-DD
		 * */
		List<Metadata> metadatas = new ArrayList<Metadata>(); 
		metadatas.add(new Metadata("useCd"));
		metadatas.add(new Metadata("usrId"));
		metadatas.add(new Metadata("usrNm"));
		metadatas.add(new Metadata("telno"));
		metadatas.add(new Metadata("email"));		        
		metadatas.add(new Metadata("etc"));		        


		BigDataSheetWriter writer = new BigDataSheetWriter(egovMessageSource.getMessage("excel.adm2000.sheetNm")
				, tempPath
				, egovMessageSource.getMessage("excel.adm2000.sheetNm")
				, metadatas);

		writer.beginSheet();

		try{

			HttpSession ss = request.getSession();
			LoginVO loginVO = (LoginVO) ss.getAttribute("loginVO");
			adm2000VO.setLicGrpId(loginVO.getLicGrpId());
			
			ExcelDataListResultHandler  resultHandler = new ExcelDataListResultHandler(writer.getXMLSheetWriter(), writer.getStyleMap(), header, metadatas);

			adm2000Service.selectAdm2000ExcelList(adm2000VO,resultHandler);

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
	 * 사용자 정보가 기록된 엑셀파일 업로드 처리
	 * 엑셀의 사용자 정보 리스트 전달
	 * @param
	 * @return
	 * @exception Exception
	 */
	@SuppressWarnings({ "unchecked", "rawtypes" })
	@RequestMapping(value="/adm/adm2000/adm2000/excelUpload.do" ,method = RequestMethod.POST)
	public ModelAndView uploadExcelParseToAjax(final MultipartHttpServletRequest multiRequest,  HttpServletResponse response ) throws Exception {
		List<Object> excelList =null;
		Map jsonMap =new HashMap();

		Map<String, MultipartFile> fileMap = multiRequest.getFileMap();
		try{
			Set<Entry<String,  MultipartFile>> entrySet = fileMap.entrySet();
			Class clz = Adm2000VO.class;
			
			for (Entry<String,  MultipartFile> entry : entrySet) {
				SheetParser parser = new SheetParser(entry.getValue().getInputStream(),1);
				excelList = parser.createEntity(clz, 1);
			}
		}
		catch(Exception ex){
			Log.error("selectExcelUploadView()", ex);
			throw new Exception(ex.getMessage());
		}

		jsonMap.put("parseList", excelList);
		return  new ModelAndView("jsonView",jsonMap);
	}
	
	/**
	 * Adm2000 사용자 정보 일괄저장 AJAX
	 * 요구사항 정보를 일괄 저장한다.
	 * @param 
	 * @return 
	 * @exception Exception
	 */
	@RequestMapping(value="/adm/adm2000/adm2000/insertAdm2000AdmInfoListAjax.do")
	public ModelAndView insertAdm2000AdmInfoListAjax(HttpServletRequest request, HttpServletResponse response, ModelMap model ) throws Exception {

		try{
			// request 파라미터를 map으로 변환
			Map<String, String> paramMap = RequestConvertor.requestParamToMap(request, true);
						
			HttpSession ss = request.getSession();

			String prjId = (String) ss.getAttribute("selPrjId");
	
			// 사용자 정보 일괄저장
			adm2000Service.insertAdm2000AdmInfoListAjax(paramMap, prjId);

			//등록 성공 메시지 세팅
			model.addAttribute("message", egovMessageSource.getMessage("success.common.insert"));

			return new ModelAndView("jsonView");
		}
		catch(Exception ex){
			Log.error("insertAdm2000AdmInfoAjax()", ex);

			//조회실패 메시지 세팅 및 저장 성공여부 세팅
			model.addAttribute("saveYN", "N");
			model.addAttribute("message", egovMessageSource.getMessage("fail.common.insert"));
			return new ModelAndView("jsonView");
		}
	}
	

	
	
	/**
	 * ADM2000  사용자 차단여부 수정(단건) Ajax
	 * @param 
	 * @return 
	 * @exception Exception
	 */
	@RequestMapping(value="/adm/adm2000/adm2000/updateAdm2000Block.do")
    public ModelAndView updateAdm2000BlockAjax(HttpServletRequest request, HttpServletResponse response, ModelMap model ) throws Exception {
		try{
			// request 파라미터를 map으로 변환
        	Map<String, String> paramMap = RequestConvertor.requestParamToMap(request, true);
        	
    		//로그인VO 가져오기
			HttpSession ss = request.getSession();
			LoginVO loginVO = (LoginVO) ss.getAttribute("loginVO");
			
    		paramMap.put("licGrpId", loginVO.getLicGrpId());		// 사용자 라이선스 그룹
        	paramMap.put("regUsrId", loginVO.getUsrId()); 			// 사용자 ID
        	paramMap.put("regUsrIp", request.getRemoteAddr()); 		// 사용자 IP
        	
        	// 차단여부 수정
        	adm2000Service.updateAdm2000Block(paramMap);
        	
        	// 수정 메시지 세팅
        	model.addAttribute("successYn", "Y");
        	model.addAttribute("message", egovMessageSource.getMessage("success.common.update"));
        	
        	return new ModelAndView("jsonView");
        	
		}catch(Exception e){
			Log.error("updateAdm2000BlockAjax()", e);
			
    		model.addAttribute("successYn", "N");
    		model.addAttribute("message", egovMessageSource.getMessage("fail.common.update"));
    		
    		return new ModelAndView("jsonView");
		}
	}

	
	/**
	 * ADM2000  사용자가 속한 조직목록 조회 Ajax
	 * @param 
	 * @return 
	 * @exception Exception
	 */
	@SuppressWarnings({ "rawtypes" })
	@RequestMapping(value="/adm/adm2000/adm2000/selectAdm2000ExistUsrInDeptAjax.do")
    public ModelAndView selectAdm2000ExistUsrInDeptAjax(HttpServletRequest request, HttpServletResponse response, ModelMap model ) throws Exception {
		try{
			// request 파라미터를 map으로 변환
        	Map<String, String> paramMap = RequestConvertor.requestParamToMap(request, true);
        	
    		//로그인VO 가져오기
			HttpSession ss = request.getSession();
			LoginVO loginVO = (LoginVO) ss.getAttribute("loginVO");
			
    		paramMap.put("licGrpId", loginVO.getLicGrpId());		// 사용자 라이선스 그룹
        	paramMap.put("regUsrId", loginVO.getUsrId()); 			// 사용자 ID
        	paramMap.put("regUsrIp", request.getRemoteAddr()); 		// 사용자 IP
        	
        	// 사용자가 속한 조직목록 조회
        	List userDeptList = adm2000Service.selectAdm2000ExistUsrInDept(paramMap);
        	
        	model.addAttribute("userDeptList", userDeptList);
        	
        	// 조회 메시지 세팅
        	model.addAttribute("successYn", "Y");
        	model.addAttribute("message", egovMessageSource.getMessage("success.common.select"));
        	
        	return new ModelAndView("jsonView");
        	
		}catch(Exception e){
			Log.error("selectAdm2000ExistUsrInDeptAjax()", e);
			
    		model.addAttribute("successYn", "N");
    		model.addAttribute("message", egovMessageSource.getMessage("fail.common.select"));
    		
    		return new ModelAndView("jsonView");
		}
	}
	
	
	/**
	 * ADM2000  사용자가 1년이내 사용했던 비밀번호 체크(단건) Ajax
	 * @param 
	 * @return 
	 * @exception Exception
	 */
	@RequestMapping(value="/adm/adm2000/adm2000/selectAdm2000BeforeUsedPwChkAjax.do")
    public ModelAndView selectAdm2000BeforeUsedPwChkAjax(HttpServletRequest request, HttpServletResponse response, ModelMap model ) throws Exception {
		try{
			// request 파라미터를 map으로 변환
        	Map<String, String> paramMap = RequestConvertor.requestParamToMap(request, true);
        	
    		//로그인VO 가져오기
			HttpSession ss = request.getSession();
			LoginVO loginVO = (LoginVO) ss.getAttribute("loginVO");
			
			// 사용자 라이선스 그룹
    		paramMap.put("licGrpId", loginVO.getLicGrpId());		
        	
        	// 사용자 1년이내 사용한 비밀번호 체크 
        	String isUsedPw = adm5200Service.selectAdm5200BeforeUsedPwCheck(paramMap);
        	
        	// 비밀번호 체크결과 세팅
        	model.addAttribute("isUsedPw", isUsedPw);
        	
        	// 조회 메시지 세팅
        	model.addAttribute("successYn", "Y");
        	model.addAttribute("message", egovMessageSource.getMessage("success.common.select"));
        	
        	return new ModelAndView("jsonView");
        	
		}catch(Exception e){
			Log.error("selectAdm2000BeforeUsedPwChkAjax()", e);
			
    		model.addAttribute("successYn", "N");
    		model.addAttribute("message", egovMessageSource.getMessage("fail.common.select"));
    		
    		return new ModelAndView("jsonView");
		}
	}
	
	/**
	 *
	 * 비밀번호 초기화
	 * 
	 * @param request
	 * @param response
	 * @param model
	 * @return
	 * @throws Exception
	 */
	@RequestMapping(value="/adm/adm2000/adm2000/updateAdm2000AccountInit.do")
    public ModelAndView updateAdm2000AccountInit(HttpServletRequest request, HttpServletResponse response, ModelMap model ) throws Exception {
		try{
			// request 파라미터를 map으로 변환
        	Map<String, String> paramMap = RequestConvertor.requestParamToMap(request, true);
        	
    		//로그인VO 가져오기
			HttpSession ss = request.getSession();
			LoginVO loginVO = (LoginVO) ss.getAttribute("loginVO");
			
    		paramMap.put("licGrpId", loginVO.getLicGrpId());		// 사용자 라이선스 그룹
        	paramMap.put("regUsrId", loginVO.getUsrId()); 			// 사용자 ID
        	paramMap.put("regUsrIp", request.getRemoteAddr()); 		// 사용자 IP
        	
        	// 차단여부 수정
        	adm2000Service.updateAdm2000AccountInit(paramMap);
        	
        	// 수정 메시지 세팅
        	model.addAttribute("successYn", "Y");
        	model.addAttribute("message", egovMessageSource.getMessage("success.common.update"));
        	
        	return new ModelAndView("jsonView");
        	
		}catch(Exception e){
			Log.error("updateAdm2000AccountInit()", e);
			
    		model.addAttribute("successYn", "N");
    		model.addAttribute("message", egovMessageSource.getMessage("fail.common.update"));
    		
    		return new ModelAndView("jsonView");
		}
	}
	
	/**
	 * ADM2002 사용자 반려사유 입력 팝업 페이지 진입  
	 * @param 
	 * @return 
	 * @exception Exception
	 */
	@RequestMapping(value="/adm/adm2000/adm2000/selectAdm2003View.do")
	public String selectAdm2003View(HttpServletRequest request, HttpServletResponse response, ModelMap model ) throws Exception {
			return "/adm/adm2000/adm2000/adm2003";
	}
}
