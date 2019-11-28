package kr.opensoftlab.oslops.stm.stm3000.stm3000.web;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import kr.opensoftlab.oslops.com.exception.UserDefineException;
import kr.opensoftlab.oslops.com.fms.web.service.FileMngService;
import kr.opensoftlab.oslops.com.vo.LoginVO;
import kr.opensoftlab.oslops.dpl.dpl1000.dpl1000.service.Dpl1000Service;
import kr.opensoftlab.oslops.stm.stm3000.stm3000.service.Stm3000Service;
import kr.opensoftlab.oslops.stm.stm3000.stm3000.vo.Jen1000VO;
import kr.opensoftlab.oslops.stm.stm3000.stm3000.vo.Jen1100VO;
import kr.opensoftlab.sdf.jenkins.JenkinsClient;
import kr.opensoftlab.sdf.util.CommonScrty;
import kr.opensoftlab.sdf.util.OslAgileConstant;
import kr.opensoftlab.sdf.util.PagingUtil;
import kr.opensoftlab.sdf.util.ReqHistoryMngUtil;
import kr.opensoftlab.sdf.util.RequestConvertor;

import org.apache.http.conn.HttpHostConnectException;
import org.apache.log4j.Logger;
import org.json.simple.JSONObject;
import org.json.simple.parser.ParseException;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;

import egovframework.com.cmm.EgovMessageSource;
import egovframework.com.cmm.service.EgovFileMngUtil;
import egovframework.com.cmm.service.EgovProperties;
import egovframework.rte.fdl.idgnr.EgovIdGnrService;
import egovframework.rte.fdl.property.EgovPropertyService;
import egovframework.rte.ptl.mvc.tags.ui.pagination.PaginationInfo;

/**
 * @Class Name : Stm3000Controller.java
 * @Description : Stm3000Controller Controller class
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
 *  2019-03-07		진주영		 	기능 개선
 *  
 *  
 *  --------------------------------------
 *  
 *  
 *  Copyright (C)  All right reserved.
 */

@Controller
public class Stm3000Controller {

	/** 로그4j 로거 로딩 */
	private static final Logger Log = Logger.getLogger(Stm3000Controller.class);

	public static final String JENKINS_OK = "JENKINS_OK";

	public static final String JENKINS_SETTING_WORNING = "JENKINS_SETTING_WORNING";

	public static final String JENKINS_WORNING_URL = "JENKINS_WORNING_URL";

	public static final String JENKINS_FAIL = "JENKINS_FAIL";

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

	@Resource(name = "stm3000Service")
	private Stm3000Service stm3000Service;

    /** Dpl1000Service DI */
    @Resource(name = "dpl1000Service")
    private Dpl1000Service dpl1000Service;

	@Resource(name = "jenkinsClient")
	private JenkinsClient jenkinsClient;
	
	/**
	 * JENKINS 설정 화면으로 이동
	 * @param 
	 * @return 
	 * @exception Exception
	 */

	@RequestMapping(value="/stm/stm3000/stm3000/selectStm3000JobView.do")
	public String selectStm3000JobView( HttpServletRequest request, HttpServletResponse response, ModelMap model ) throws Exception {
		return "/stm/stm3000/stm3000/stm3000";
	}

	/**
	 *  JENKINS 설정 등록/수정 팝업 화면으로 이동
	 *  
	 * @param request
	 * @param response
	 * @param model
	 * @return
	 * @throws Exception
	 */
	@RequestMapping(value="/stm/stm3000/stm3000/selectStm3001JenkinsDetailView.do")
	public String selectStm3001JenkinsDetailView( HttpServletRequest request, HttpServletResponse response, ModelMap model ) throws Exception {
		return "/stm/stm3000/stm3000/stm3001";
	}
	
	/**
	 *  JOB 설정 등록/수정 팝업 화면으로 이동
	 *  
	 * @param request
	 * @param response
	 * @param model
	 * @return
	 * @throws Exception
	 */
	@SuppressWarnings("rawtypes")
	@RequestMapping(value="/stm/stm3000/stm3000/selectStm3002JobDetailView.do")
	public String selectStm3002JobDetailView( HttpServletRequest request, HttpServletResponse response, ModelMap model ) throws Exception {
		try{
			// request 파라미터를 map으로 변환
			Map<String, String> paramMap = RequestConvertor.requestParamToMapAddSelInfo(request, true);
			
			//jenkins list
			List<Map> jenkinsList = stm3000Service.selectStm3000JenkinsNormalList(paramMap);
			model.addAttribute("jenkinsList",jenkinsList);
		}catch(Exception e){
			Log.error(e);
		}
		return "/stm/stm3000/stm3000/stm3002";
	}
	
	
	/**
	 *  JENKINS 상세 정보 화면
	 *  
	 * @param request
	 * @param response
	 * @param model
	 * @return
	 * @throws Exception
	 */
	@SuppressWarnings({ "rawtypes", "unchecked" })
	@RequestMapping(value="/stm/stm3000/stm3000/selectStm3003JenkinsDetailView.do")
	public String selectStm3003JenkinsDetailView( HttpServletRequest request, HttpServletResponse response, ModelMap model ) throws Exception {
		try{
			// request 파라미터를 map으로 변환
			Map<String, String> paramMap = RequestConvertor.requestParamToMapAddSelInfo(request, true);
			
			//jenkins 정보
			String userId= paramMap.get("jenUsrId");
			String tokenId= paramMap.get("jenUsrTok");
			String jenUrl= paramMap.get("jenUrl");
			
			//globals.properties에서 salt값 가져오기
			String salt = EgovProperties.getProperty("Globals.lunaops.salt");
			
			//빈값인경우
			if(tokenId == null || "".equals(tokenId)){
				//조회실패 메시지 세팅
				model.addAttribute("errorYn", "Y");
				model.addAttribute("message", "JENKINS 연결에 실패했습니다.");
			}
			
			//값 복호화
			String deTokenId = CommonScrty.decryptedAria(tokenId, salt);
			
			//JENKINS SETTING
			jenkinsClient.setUser(userId);
			jenkinsClient.setPassword(deTokenId);
			
			try{
				//JENKINS 정보 불러오기
				String buildUrl = jenUrl+"/api/json";
				String buildContent = jenkinsClient.excuteHttpClientJenkins(buildUrl);
				Map jenMap = jenkinsClient.getJenkinsParser(buildContent);
				
				model.addAttribute("jenMap",jenMap);
				
				//JOB 목록
				List<JSONObject> jobs = (List)jenMap.get("jobs");
				
				model.addAttribute("jobs",jobs);
				
			}catch(Exception e){
				System.out.println(e);
				model.addAttribute("errorYn", "Y");
				model.addAttribute("message", "JENKINS 연결에 실패했습니다.");
			}
		}catch(Exception e){
			Log.error(e);
		}
		return "/stm/stm3000/stm3000/stm3003";
	}
	
	/**
	 *  JOB 상세 정보 화면
	 *  
	 * @param request
	 * @param response
	 * @param model
	 * @return
	 * @throws Exception
	 */
	@SuppressWarnings("rawtypes")
	@RequestMapping(value="/stm/stm3000/stm3000/selectStm3004JobDetailView.do")
	public String selectStm3004JenkinsDetailView( HttpServletRequest request, HttpServletResponse response, ModelMap model ) throws Exception {
		try{
			// request 파라미터를 map으로 변환
			Map<String, String> paramMap = RequestConvertor.requestParamToMapAddSelInfo(request, true);
			
			//빌드 목록
			List jobBldNumList = dpl1000Service.selectDpl1400DplBldNumList(paramMap);
			model.addAttribute("jobBldNumList",jobBldNumList);
		}catch(Exception e){
			Log.error(e);
		}
		return "/stm/stm3000/stm3000/stm3004";
	}
	
	/**
	 * Jenkins 설정 목록 조회
	 * @param 
	 * @return 
	 * @exception Exception
	 */
	@RequestMapping(value="/stm/stm3000/stm3000/selectStm3000JenkinsListAjax.do")
	public ModelAndView selectStm3000JenkinsListAjax(@ModelAttribute("stm3000VO") Jen1000VO jen1000VO, HttpServletRequest request, HttpServletResponse response, ModelMap model ) throws Exception {

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
			jen1000VO.setPageIndex(_pageNo);
			jen1000VO.setPageSize(_pageSize);
			jen1000VO.setPageUnit(_pageSize);


			PaginationInfo paginationInfo = PagingUtil.getPaginationInfo(jen1000VO);  /** paging - 신규방식 */

			List<Jen1000VO> stm3000List = null;

			HttpSession ss = request.getSession();
			LoginVO loginVO = (LoginVO) ss.getAttribute("loginVO");

			jen1000VO.setLoginUsrId(loginVO.getUsrId());
			jen1000VO.setLicGrpId(loginVO.getLicGrpId());

			/**
			 * 목록 조회
			 */
			int totCnt = 0;
			stm3000List =   stm3000Service.selectStm3000JenkinsList(jen1000VO);


			/** 총 데이터의 건수 를 가져온다. */
			totCnt =  stm3000Service.selectStm3000JenkinsListCnt(jen1000VO);
			paginationInfo.setTotalRecordCount(totCnt);

			model.addAttribute("list", stm3000List);

			//페이지 정보 보내기
			Map<String, Integer> pageMap = new HashMap<String, Integer>();
			pageMap.put("pageNo",jen1000VO.getPageIndex());
			pageMap.put("listCount", stm3000List.size());
			pageMap.put("totalPages", paginationInfo.getTotalPageCount());
			pageMap.put("totalElements", totCnt);
			pageMap.put("pageSize", _pageSize);

			model.addAttribute("page", pageMap);

			return new ModelAndView("jsonView");
		}
		catch(Exception ex){
			Log.error("selectStm3000JenkinsListAjax()", ex);
			throw new Exception(ex.getMessage());
		}
	}
	
	/**
	 * Job 설정 목록 조회
	 * @param 
	 * @return 
	 * @exception Exception
	 */
	@RequestMapping(value="/stm/stm3000/stm3000/selectStm3000JobListAjax.do")
	public ModelAndView selectStm3000JobListAjax(@ModelAttribute("stm3000VO") Jen1100VO jen1100VO, HttpServletRequest request, HttpServletResponse response, ModelMap model ) throws Exception {

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
			jen1100VO.setPageIndex(_pageNo);
			jen1100VO.setPageSize(_pageSize);
			jen1100VO.setPageUnit(_pageSize);
			jen1100VO.setPrjId((String)request.getSession().getAttribute("selPrjId"));


			PaginationInfo paginationInfo = PagingUtil.getPaginationInfo(jen1100VO);  /** paging - 신규방식 */

			List<Jen1100VO> stm3000List = null;

			HttpSession ss = request.getSession();
			LoginVO loginVO = (LoginVO) ss.getAttribute("loginVO");

			jen1100VO.setLoginUsrId(loginVO.getUsrId());
			jen1100VO.setLicGrpId(loginVO.getLicGrpId());

			/**
			 * 목록 조회
			 */
			int totCnt = 0;
			stm3000List =   stm3000Service.selectStm3000JobList(jen1100VO);


			/** 총 데이터의 건수 를 가져온다. */
			totCnt =  stm3000Service.selectStm3000JobListCnt(jen1100VO);
			paginationInfo.setTotalRecordCount(totCnt);

			model.addAttribute("list", stm3000List);

			//페이지 정보 보내기
			Map<String, Integer> pageMap = new HashMap<String, Integer>();
			pageMap.put("pageNo",jen1100VO.getPageIndex());
			pageMap.put("listCount", stm3000List.size());
			pageMap.put("totalPages", paginationInfo.getTotalPageCount());
			pageMap.put("totalElements", totCnt);
			pageMap.put("pageSize", _pageSize);

			model.addAttribute("page", pageMap);

			return new ModelAndView("jsonView");
		}
		catch(Exception ex){
			Log.error("selectStm3000JobListAjax()", ex);
			throw new Exception(ex.getMessage());
		}
	}


	/*
	 *  Jenkins 설정 상세 조회
	 */
	@SuppressWarnings("rawtypes")
	@RequestMapping(value="/stm/stm3000/stm3000/selectStm3000JenkinsDetailAjax.do")
	public ModelAndView selectStm3000JenkinsDetailAjax(HttpServletRequest request, HttpServletResponse response, ModelMap model ) throws Exception {
		try{
			// request 파라미터를 map으로 변환
			Map<String, String> paramMap = RequestConvertor.requestParamToMapAddSelInfo(request, true);

			//jenkins 정보 조회
			Map jenMap = stm3000Service.selectStm3000JenkinsInfo(paramMap);

			model.addAttribute("jenInfo", jenMap);

			//등록 성공 메시지 세팅
			model.addAttribute("message", egovMessageSource.getMessage("success.common.select"));

			return new ModelAndView("jsonView");
		}
		catch(Exception ex){
			Log.error("selectStm3000JenkinsDetailAjax()", ex);


			model.addAttribute("message", egovMessageSource.getMessage("fail.common.select"));
			return new ModelAndView("jsonView");
		}
	}

	
	/*
	 *  Job 설정 상세 조회
	 */
	@SuppressWarnings("rawtypes")
	@RequestMapping(value="/stm/stm3000/stm3000/selectStm3000JobDetailAjax.do")
	public ModelAndView selectStm3000JobDetailAjax(HttpServletRequest request, HttpServletResponse response, ModelMap model ) throws Exception {
		try{
			// request 파라미터를 map으로 변환
			Map<String, String> paramMap = RequestConvertor.requestParamToMapAddSelInfo(request, true);
			
			//job 정보 조회
			Map jobMap = stm3000Service.selectStm3000JobInfo(paramMap);
			
			model.addAttribute("jobInfo", jobMap);
			
			//등록 성공 메시지 세팅
			model.addAttribute("message", egovMessageSource.getMessage("success.common.select"));
			
			return new ModelAndView("jsonView");
		}
		catch(Exception ex){
			Log.error("selectStm3000JobDetailAjax()", ex);
			
			
			model.addAttribute("message", egovMessageSource.getMessage("fail.common.select"));
			return new ModelAndView("jsonView");
		}
	}
	


	/**
	 *
	 * Jenkins 저장
	 *  
	 * @param request
	 * @param response
	 * @param model
	 * @return
	 * @throws Exception
	 */
	@RequestMapping(value="/stm/stm3000/stm3000/saveStm3001JenkinsInfoAjax.do")
	public ModelAndView saveStm3001JenkinsInfoAjax(HttpServletRequest request, HttpServletResponse response, ModelMap model ) throws Exception {

		try{


			// request 파라미터를 map으로 변환
			Map<String, String> paramMap = RequestConvertor.requestParamToMapAddSelInfo(request, true);
			HttpSession ss = request.getSession();

			LoginVO loginVO = (LoginVO) ss.getAttribute("loginVO");

			paramMap.put("licGrpId", loginVO.getLicGrpId());	
			
			//save type
			String type = (String) paramMap.get("type");
			
			//globals.properties에서 salt값 가져오기
			String salt = EgovProperties.getProperty("Globals.lunaops.salt");
			
			//기존 값
			String nowJenUsrTok = (String)paramMap.get("nowJenUsrTok");
			
			//파라미터 값
			String jenUsrTok = (String)paramMap.get("jenUsrTok");
			
			//암호화된 값
			String newJenUsrTok = "";
			try{
				String userId=(String)paramMap.get("jenUsrId");
				String tokenId=(String)paramMap.get("jenUsrTok");
				
				//Jenkins 수정인경우
				if(type != null && "update".equals(type)){
					//빈값인경우 오류
					if(tokenId == null || "".equals(tokenId)){
						model.addAttribute("MSG_CD", JENKINS_FAIL);
						return new ModelAndView("jsonView");
					}
					
					//기존 값과 동일한경우 복호화해서 사용
					if(nowJenUsrTok.equals(jenUsrTok)){
						//값 복호화
						tokenId = CommonScrty.decryptedAria(tokenId, salt);
					}else{
						tokenId = jenUsrTok;
					}
				}
				
				//JENKINS SETTING
				jenkinsClient.setUser(userId);
				jenkinsClient.setPassword(tokenId);
				
				String url =   (String)paramMap.get("jenUrl")+"/api/json";
				String content = "";
	
				content = jenkinsClient.excuteHttpClientJenkins(url);
	
				jenkinsClient.getJenkinsParser(content );
			}
			catch(Exception ex){
				Log.error("selectStm3000URLConnect()", ex);
				if( ex instanceof HttpHostConnectException){
					model.addAttribute("MSG_CD", JENKINS_FAIL);
				}else if( ex instanceof ParseException){
					model.addAttribute("MSG_CD", JENKINS_FAIL);
				}else if( ex instanceof IllegalArgumentException){
					model.addAttribute("MSG_CD", JENKINS_WORNING_URL);
				}else if( ex instanceof UserDefineException){
					model.addAttribute("MSG_CD", ex.getMessage());
				}else{
					model.addAttribute("MSG_CD", JENKINS_FAIL);
				}
				//조회실패 메시지 세팅 및 저장 성공여부 세팅			
				return new ModelAndView("jsonView");
			}
			
			//수정인경우 값 변경되었는지 체크
			if("update".equals(type)){
				//기존 값과 파라미터값 비교
				if(!nowJenUsrTok.equals(jenUsrTok)){
					//값 암호화
					newJenUsrTok = CommonScrty.encryptedAria(jenUsrTok, salt);
				}else{
					newJenUsrTok = jenUsrTok;
				}
			}
			//등록인경우 값 암호화
			else{
				newJenUsrTok = CommonScrty.encryptedAria(jenUsrTok, salt);
			}

			//입력된 token제거
			paramMap.remove("jenUsrTok");
			
			//암호화된  token 입력
			paramMap.put("jenUsrTok", newJenUsrTok);
			
			//jenkins 저장
			stm3000Service.saveStm3000JenkinsInfo(paramMap);

			//등록 성공 메시지 세팅
			model.addAttribute("errorYn", "N");
			model.addAttribute("message", egovMessageSource.getMessage("success.common.save"));

			return new ModelAndView("jsonView");
		}
		catch(Exception ex){
			Log.error("saveStm3001JenkinsInfoAjax()", ex);

			//조회실패 메시지 세팅 및 저장 성공여부 세팅
			model.addAttribute("errorYn", "Y");
			model.addAttribute("message", egovMessageSource.getMessage("fail.common.save"));
			return new ModelAndView("jsonView");
		}
	}
	
	/**
	 *
	 * Job 저장
	 *  
	 * @param request
	 * @param response
	 * @param model
	 * @return
	 * @throws Exception
	 */
	@RequestMapping(value="/stm/stm3000/stm3000/saveStm3002JobInfoAjax.do")
	public ModelAndView saveStm3002JobInfoAjax(HttpServletRequest request, HttpServletResponse response, ModelMap model ) throws Exception {
		
		try{
			// request 파라미터를 map으로 변환
			Map<String, String> paramMap = RequestConvertor.requestParamToMapAddSelInfo(request, true);
			HttpSession ss = request.getSession();
			
			LoginVO loginVO = (LoginVO) ss.getAttribute("loginVO");
			
			paramMap.put("licGrpId", loginVO.getLicGrpId());	
			
			//save type
			String type = (String) paramMap.get("type");
			
			//수정 아닌경우 체크
			if(!"update".equals(type)){
				//이미 등록된 JOB인지 체크하기
				int jobCheck = stm3000Service.selectStm3000JobUseCountInfo(paramMap);
				
				if(jobCheck > 0){
					//등록실패 메시지 세팅 및 저장 성공여부 세팅
					model.addAttribute("errorYn", "Y");
					model.addAttribute("message", "이미 추가된 JOB입니다.");
					return new ModelAndView("jsonView");
				}
			}
			
			//globals.properties에서 salt값 가져오기
			String salt = EgovProperties.getProperty("Globals.lunaops.salt");
			
			//기존 값
			String nowJobTok = (String)paramMap.get("nowJobTok");
			
			//파라미터 값
			String jobTok = (String)paramMap.get("jobTok");
			
			//암호화된 값
			String newJobTok = "";
			try{
				String jenUrl=(String)paramMap.get("jenUrl");
				String jobId=(String)paramMap.get("jobId");
				String userId=(String)paramMap.get("jenUsrId");
				String tokenId=(String)paramMap.get("jenUsrTok");
				
				//빈값인경우 오류
				if(tokenId == null || "".equals(tokenId)){
					model.addAttribute("MSG_CD", JENKINS_FAIL);
					return new ModelAndView("jsonView");
				}
				
				//값 복호화
				tokenId = CommonScrty.decryptedAria(tokenId, salt);
				
				//JENKINS SETTING
				jenkinsClient.setUser(userId);
				jenkinsClient.setPassword(tokenId);
				
				String url = jenUrl+"/api/json";
				String content = "";
				
				content = jenkinsClient.excuteHttpClientJenkins(url);
				
				jenkinsClient.getJenkinsParser(content );
				
				//JOB TOKEN KEY 확인
				String deJobTok = jobTok;
				
				//기존 값과 파라미터값 비교
				if(nowJobTok.equals(jobTok)){
					//값 복호화
					deJobTok = CommonScrty.decryptedAria(jobTok, salt);
				}
				
				if(deJobTok == null || "".equals(deJobTok)){
					model.addAttribute("MSG_CD", "JOB TOKEN 값이 없습니다.");
					return new ModelAndView("jsonView");
				}
				
				url = jenUrl+"/job/"+jobId+"/config.xml";
				String settingJobTok = "";
				
				settingJobTok = jenkinsClient.excuteHttpClientJobToken(url,deJobTok);
				
				//복호화 token 값과 실제 token값 확인
				if(!deJobTok.equals(settingJobTok)){
					model.addAttribute("MSG_CD", "JOB TOKEN KEY값을 확인해주세요.");
					return new ModelAndView("jsonView");
				}
				
			}
			catch(Exception ex){
				Log.error("selectStm3000URLConnect()", ex);
				if( ex instanceof HttpHostConnectException){
					model.addAttribute("MSG_CD", JENKINS_FAIL);
				}else if( ex instanceof ParseException){
					model.addAttribute("MSG_CD", JENKINS_FAIL);
				}else if( ex instanceof IllegalArgumentException){
					model.addAttribute("MSG_CD", JENKINS_WORNING_URL);
				}else if( ex instanceof UserDefineException){
					model.addAttribute("MSG_CD", ex.getMessage());
				}else{
					model.addAttribute("MSG_CD", JENKINS_FAIL);
				}
				//조회실패 메시지 세팅 및 저장 성공여부 세팅			
				return new ModelAndView("jsonView");
			}
			
			//수정인경우 값 변경되었는지 체크
			if("update".equals(type)){
				//기존 값과 파라미터값 비교
				if(!nowJobTok.equals(jobTok)){
					//값 암호화
					newJobTok = CommonScrty.encryptedAria(jobTok, salt);
				}else{
					newJobTok = jobTok;
				}
			}
			//등록인경우 값 암호화
			else{
				newJobTok = CommonScrty.encryptedAria(jobTok, salt);
			}
			
			//입력된 jobTok 제거
			paramMap.remove("jobTok");
			
			//암호화된  jobTok 입력
			paramMap.put("jobTok", newJobTok);
			
			//job 등록
			stm3000Service.saveStm3000JobInfo(paramMap);
			
			//등록 성공 메시지 세팅
			model.addAttribute("errorYn", "N");
			model.addAttribute("message", egovMessageSource.getMessage("success.common.save"));
			
			return new ModelAndView("jsonView");
		}
		catch(Exception ex){
			Log.error("saveStm3002JobInfoAjax()", ex);
			
			//조회실패 메시지 세팅 및 저장 성공여부 세팅
			model.addAttribute("errorYn", "Y");
			model.addAttribute("message", egovMessageSource.getMessage("fail.common.save"));
			return new ModelAndView("jsonView");
		}
	}

	/**
	 * Jenkins 상세정보 삭제
	 * 
	 * @param request
	 * @param response
	 * @param model
	 * @return
	 * @throws Exception
	 */
	@RequestMapping(value="/stm/stm3000/stm3000/deleteStm3000JenkinsInfoAjax.do")
	public ModelAndView deleteStm3000JenkinsInfoAjax(HttpServletRequest request, HttpServletResponse response, ModelMap model ) throws Exception {

		try{
			// request 파라미터를 map으로 변환
			Map<String, String> paramMap = RequestConvertor.requestParamToMapAddSelInfo(request, true);
			
			//삭제 유무 변경 '01'
			paramMap.put("delCd", "01");
			
			stm3000Service.deleteStm3000JenkinsInfo(paramMap);
			
			//등록 성공 메시지 세팅
			model.addAttribute("message", egovMessageSource.getMessage("success.common.delete"));


			return new ModelAndView("jsonView");
		}
		catch(Exception ex){
			Log.error("deleteStm3000JenkinsInfoAjax()", ex);

			//조회실패 메시지 세팅 및 저장 성공여부 세팅
			model.addAttribute("saveYN", "N");
			model.addAttribute("message", egovMessageSource.getMessage("fail.common.save"));
			return new ModelAndView("jsonView");
		}
	}
	
	/**
	 * Job 상세정보 삭제
	 * 
	 * @param request
	 * @param response
	 * @param model
	 * @return
	 * @throws Exception
	 */
	@RequestMapping(value="/stm/stm3000/stm3000/deleteStm3000JobInfoAjax.do")
	public ModelAndView deleteStm3000JobInfoAjax(HttpServletRequest request, HttpServletResponse response, ModelMap model ) throws Exception {
		
		try{
			// request 파라미터를 map으로 변환
			Map<String, String> paramMap = RequestConvertor.requestParamToMapAddSelInfo(request, true);
			
			// 프로젝트 ID 세팅
			String prjId = paramMap.get("selPrjId");
			paramMap.put("prjId", prjId);
			
			stm3000Service.deleteStm3000JobInfo(paramMap);
			
			//등록 성공 메시지 세팅
			model.addAttribute("message", egovMessageSource.getMessage("success.common.delete"));
			
			
			return new ModelAndView("jsonView");
		}
		catch(Exception ex){
			Log.error("deleteStm3000JobInfoAjax()", ex);
			
			//조회실패 메시지 세팅 및 저장 성공여부 세팅
			model.addAttribute("saveYN", "N");
			model.addAttribute("message", egovMessageSource.getMessage("fail.common.save"));
			return new ModelAndView("jsonView");
		}
	}
	
	/**
	 * Jenkins 접속 확인
	 * 
	 * @param request
	 * @param response
	 * @param model
	 * @return
	 * @throws Exception
	 */
	@SuppressWarnings("rawtypes")
	@RequestMapping(value="/stm/stm3000/stm3000/selectStm3000ConfirmConnect.do")
	public ModelAndView selectStm3000ConfirmConnect(HttpServletRequest request, HttpServletResponse response, ModelMap model ) throws Exception {

		try{

			// request 파라미터를 map으로 변환
			Map<String, String> paramMap = RequestConvertor.requestParamToMapAddSelInfo(request, true);

			Map jenMap = stm3000Service.selectStm3000JenkinsInfo(paramMap);
			String userId=(String)jenMap.get("jenUsrId");
			String tokenId=(String)jenMap.get("jenUsrTok");
			
			//globals.properties에서 salt값 가져오기
			String salt = EgovProperties.getProperty("Globals.lunaops.salt");
			
			//값 복호화
			String newTokenId = CommonScrty.decryptedAria(tokenId, salt);
			
			//빈값인경우 오류
			if(newTokenId == null || "".equals(newTokenId)){
				model.addAttribute("MSG_CD", JENKINS_SETTING_WORNING);
				return new ModelAndView("jsonView");
			}

			//JENKINS SETTING
			jenkinsClient.setUser(userId);
			jenkinsClient.setPassword(newTokenId);
			
			String url =   (String)jenMap.get("jenUrl")+"/api/json";
			String content = "";

			content = jenkinsClient.excuteHttpClientJenkins(url);

			jenkinsClient.getJenkinsParser(content );

			model.addAttribute("MSG_CD", JENKINS_OK);


			return new ModelAndView("jsonView");
		}
		catch(Exception ex){
			Log.error("selectStm3000ConfirmConnect()", ex);
			if( ex instanceof HttpHostConnectException){
				model.addAttribute("MSG_CD", JENKINS_FAIL);
			}else if( ex instanceof ParseException){
				model.addAttribute("MSG_CD", JENKINS_FAIL);
			}else if( ex instanceof UserDefineException){
					model.addAttribute("MSG_CD", ex.getMessage());
			}else{
				model.addAttribute("MSG_CD", JENKINS_FAIL);
			}
			//조회실패 메시지 세팅 및 저장 성공여부 세팅			
			return new ModelAndView("jsonView");
		}
	}
	
	/**
	 * job 접속 확인
	 * 
	 * @param request
	 * @param response
	 * @param model
	 * @return
	 * @throws Exception
	 */
	@SuppressWarnings("rawtypes")
	@RequestMapping(value="/stm/stm3000/stm3000/selectStm3000JobConfirmConnect.do")
	public ModelAndView selectStm3000JobConfirmConnect(HttpServletRequest request, HttpServletResponse response, ModelMap model ) throws Exception {
		
		try{
			
			// request 파라미터를 map으로 변환
			Map<String, String> paramMap = RequestConvertor.requestParamToMapAddSelInfo(request, true);
			
			//job 정보 조회
			Map jobMap = stm3000Service.selectStm3000JobInfo(paramMap);
			
			//정보 불러오기
			String jenUsrId=(String)jobMap.get("jenUsrId");
			String jenUsrTok=(String)jobMap.get("jenUsrTok");
			String jobTok=(String)jobMap.get("jobTok");
			String jenUrl=(String)jobMap.get("jenUrl");
			String jobId=(String)jobMap.get("jobId");
			
			//globals.properties에서 salt값 가져오기
			String salt = EgovProperties.getProperty("Globals.lunaops.salt");
			
			//값 복호화
			String deJenUsrTok = CommonScrty.decryptedAria(jenUsrTok, salt);
			String deJobTok = CommonScrty.decryptedAria(jobTok, salt);
			
			//빈값인경우 오류
			if(deJenUsrTok == null || "".equals(deJenUsrTok)){
				model.addAttribute("MSG_CD", JENKINS_SETTING_WORNING);
				return new ModelAndView("jsonView");
			}
			if(deJobTok == null || "".equals(deJobTok)){
				model.addAttribute("MSG_CD", "JOB TOKEN 값이 없습니다.");
				return new ModelAndView("jsonView");
			}
			
			//JENKINS SETTING
			jenkinsClient.setUser(jenUsrId);
			jenkinsClient.setPassword(deJenUsrTok);
			
			String url = jenUrl+"/job/"+jobId+"/config.xml";
			String settingJobTok = "";
			
			settingJobTok = jenkinsClient.excuteHttpClientJobToken(url,deJobTok);
			
			//복호화 token 값과 실제 token값 확인
			if(!deJobTok.equals(settingJobTok)){
				model.addAttribute("MSG_CD", "JOB TOKEN KEY값을 확인해주세요.");
				
				return new ModelAndView("jsonView");
			}
			
			model.addAttribute("MSG_CD", JENKINS_OK);
			
			
			return new ModelAndView("jsonView");
		}
		catch(Exception ex){
			Log.error("selectStm3000JobConfirmConnect()", ex);
			if( ex instanceof HttpHostConnectException){
				model.addAttribute("MSG_CD", JENKINS_FAIL);
			}else if( ex instanceof ParseException){
				model.addAttribute("MSG_CD", JENKINS_FAIL);
			}else if( ex instanceof UserDefineException){
				model.addAttribute("MSG_CD", ex.getMessage());
			}else{
				model.addAttribute("MSG_CD", JENKINS_FAIL);
			}
			//조회실패 메시지 세팅 및 저장 성공여부 세팅			
			return new ModelAndView("jsonView");
		}
	}

	/**
	 * 사용가능한 job 선택 팝업 이동 
	 * @param request
	 * @param response
	 * @param model
	 * @return
	 * @throws Exception
	 */
	@RequestMapping(value="/stm/stm3000/stm3000/selectStm3002JobView.do")
	public String selectStm3002JobView( HttpServletRequest request, HttpServletResponse response, ModelMap model ) throws Exception {
		return "/stm/stm3000/stm3000/stm3002";
	}
	
	/**
	 * 
	 * Jenkins URL 검증 + JOBList + 로컬 DB 원복 JOB List 가져오기
	 * 
	 * @param request
	 * @param response
	 * @param model
	 * @return
	 * @throws Exception
	 */
	@SuppressWarnings("rawtypes")
	@RequestMapping(value="/stm/stm3000/stm3000/selectStm3000URLConnect.do")
	public ModelAndView selectStm3000URLConnect(HttpServletRequest request, HttpServletResponse response, ModelMap model ) throws Exception {

		try{

			// request 파라미터를 map으로 변환
			Map<String, String> paramMap = RequestConvertor.requestParamToMapAddSelInfo(request, true);

			String userId= (String)paramMap.get("jenUsrId");
			String tokenId= (String)paramMap.get("jenUsrTok");
			
			//globals.properties에서 salt값 가져오기
			String salt = EgovProperties.getProperty("Globals.lunaops.salt");
			
			//값 복호화
			tokenId = CommonScrty.decryptedAria(tokenId, salt);
			
			//빈값인경우 오류
			if(tokenId == null || "".equals(tokenId)){
				model.addAttribute("MSG_CD", JENKINS_FAIL);
				return new ModelAndView("jsonView");
			}
			
			//JENKINS SETTING
			jenkinsClient.setUser(userId);
			jenkinsClient.setPassword(tokenId);
			
			String url =   (String)paramMap.get("jenUrl")+"/api/json";
			String content = "";

			content = jenkinsClient.excuteHttpClientJenkins(url);

			Map jenkinsMap= jenkinsClient.getJenkinsParser(content );
			List jobList =  (List) jenkinsMap.get("jobs");

			//job List
			model.addAttribute("list", jobList);
			model.addAttribute("MSG_CD", JENKINS_OK);
			
			//원복 목록 가져오기
			paramMap.put("restoreSelJobType", "03");
			
			//job restore list
			List<Map> jobRestoreList = stm3000Service.selectStm3000JobNormalList(paramMap);
			
			model.addAttribute("jobRestoreList", jobRestoreList);
			return new ModelAndView("jsonView");
		}
		catch(Exception ex){
			Log.error("selectStm3000URLConnect()", ex);
			if( ex instanceof HttpHostConnectException){
				model.addAttribute("MSG_CD", JENKINS_FAIL);
			}else if( ex instanceof ParseException){
				model.addAttribute("MSG_CD", JENKINS_FAIL);
			}else if( ex instanceof IllegalArgumentException){
				model.addAttribute("MSG_CD", JENKINS_WORNING_URL);
			}else if( ex instanceof UserDefineException){
					model.addAttribute("MSG_CD", ex.getMessage());
			}else{
				model.addAttribute("MSG_CD", JENKINS_FAIL);
			}
			//조회실패 메시지 세팅 및 저장 성공여부 세팅			
			return new ModelAndView("jsonView");
		}
	}
	

	/**
	 * jenkins 허용 역할 목록 불러오기 Ajax
	 * @param 
	 * @return 
	 * @exception Exception
	 */
	@SuppressWarnings({ "rawtypes", "unchecked" })
	@RequestMapping(value="/stm/stm3000/stm3000/selectStm3000JenAuthGrpList.do")
	public ModelAndView selectStm3000JenAuthGrpList(HttpServletRequest request, HttpServletResponse response, ModelMap model ) throws Exception {
		
		try{
			// request 파라미터를 map으로 변환
			Map<String, String> paramMap = RequestConvertor.requestParamToMapAddSelInfo(request, true);
			
			//역할그룹 목록
			List<Map> jenAuthGrpList = stm3000Service.selectStm3000JenAuthGrpList(paramMap);
			model.addAttribute("jenAuthGrpList", jenAuthGrpList);
			
			//성공 메시지 세팅
			model.addAttribute("errorYN", "N");
			model.addAttribute("message", egovMessageSource.getMessage("success.common.select"));
			
			return new ModelAndView("jsonView");
		}
		catch(Exception ex){
			Log.error("selectStm3000JenAuthGrpList()", ex);
			
			//조회실패 메시지 세팅 및 저장 성공여부 세팅
			model.addAttribute("errorYN", "Y");
			model.addAttribute("message", egovMessageSource.getMessage("fail.common.select"));
			return new ModelAndView("jsonView");
		}
	}
	
	/**
	 *  jenkins 허용 역할 저장
	 * @param request
	 * @param model
	 * @return
	 * @throws Exception
	 */
	@SuppressWarnings({ "rawtypes" })
	@RequestMapping(value="/stm/stm3000/stm3000/insertStm3000JenAuthGrpList.do")
	public ModelAndView insertStm3000JenAuthGrpList(HttpServletRequest request, HttpServletResponse response, ModelMap model )	throws Exception {
		try{
			// request 파라미터를 map으로 변환
			Map paramMap = RequestConvertor.requestParamToMapAddSelInfo(request, true);
			
			//선택 역할그룹 갯수
			int selAuthCnt = Integer.parseInt((String)paramMap.get("selAuthCnt"));
			
			//실패 역할그룹 갯수 리턴
			int addFailAuthCnt = stm3000Service.insertStm3000JenAuthGrpList(paramMap);
			
			//선택 역할그룹 갯수와 실패 역할그룹 갯수가 같은경우 에러
			if(selAuthCnt == addFailAuthCnt){
				//실패 갯수 메시지 세팅
				model.addAttribute("errorYn", "Y");
				model.addAttribute("message", "선택된 모든 역할그룹이 중복됩니다.");
			}
			//실패 역할그룹 갯수가 0이상인경우 메시지 변경
			else if(addFailAuthCnt > 0){
				//실패 갯수 메시지 세팅
				model.addAttribute("errorYn", "N");
				model.addAttribute("message", egovMessageSource.getMessage("success.common.insert")+"</br>"+addFailAuthCnt+"건의 중복 선택 역할그룹은 제외되었습니다.");
			}else{
				//조회 성공 메시지 세팅
				model.addAttribute("errorYn", "N");
				model.addAttribute("message", egovMessageSource.getMessage("success.common.insert"));
			}
			
			return new ModelAndView("jsonView");

		}catch(Exception e){
			Log.error("insertStm3000JenAuthGrpList()", e);
			model.addAttribute("errorYn", "Y");
			model.addAttribute("message", egovMessageSource.getMessage("fail.common.insert"));
			return new ModelAndView("jsonView");
		}
	}
	

	/**
	 *  jenkins 역할그룹 제한 제거
	 * @param request
	 * @param model
	 * @return
	 * @throws Exception
	 */
	@SuppressWarnings({ "rawtypes"})
	@RequestMapping(value="/stm/stm3000/stm3000/deleteStm3000JenAuthGrpList.do")
	public ModelAndView deleteStm3000JenAuthGrpList(HttpServletRequest request, HttpServletResponse response, ModelMap model )	throws Exception {
		try{
			// request 파라미터를 map으로 변환
			Map paramMap = RequestConvertor.requestParamToMapAddSelInfo(request, true);
			
			//역할그룹 제거
			stm3000Service.deleteStm3000JenAuthGrpList(paramMap);
			
			//조회 성공 메시지 세팅
			model.addAttribute("errorYn", "N");
			model.addAttribute("message", egovMessageSource.getMessage("success.common.delete"));
			
			return new ModelAndView("jsonView");

		}catch(Exception e){
			Log.error("deleteStm3000JenAuthGrpList()", e);
			model.addAttribute("errorYn", "Y");
			model.addAttribute("message", egovMessageSource.getMessage("fail.common.delete"));
			return new ModelAndView("jsonView");
		}
	}
}
