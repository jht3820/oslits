package kr.opensoftlab.oslits.stm.stm3000.stm3000.web;

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
import kr.opensoftlab.oslits.stm.stm3000.stm3000.service.Stm3000Service;
import kr.opensoftlab.oslits.stm.stm3000.stm3000.vo.Stm3000VO;
import kr.opensoftlab.sdf.jenkins.JenkinsClient;
import kr.opensoftlab.sdf.util.CommonScrty;
import kr.opensoftlab.sdf.util.OslAgileConstant;
import kr.opensoftlab.sdf.util.PagingUtil;
import kr.opensoftlab.sdf.util.ReqHistoryMngUtil;
import kr.opensoftlab.sdf.util.RequestConvertor;

import org.apache.http.conn.HttpHostConnectException;
import org.apache.log4j.Logger;
import org.json.simple.parser.ParseException;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.multipart.MultipartHttpServletRequest;
import org.springframework.web.servlet.ModelAndView;

import egovframework.com.cmm.EgovMessageSource;
import egovframework.com.cmm.service.EgovFileMngUtil;
import egovframework.com.cmm.service.EgovProperties;
import egovframework.com.cmm.service.FileVO;
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
	 * Jenkins 설정 목록 조회
	 * @param 
	 * @return 
	 * @exception Exception
	 */
	@RequestMapping(value="/stm/stm3000/stm3000/selectStm3000JobListAjax.do")
	public ModelAndView selectStm3000JobListAjax(@ModelAttribute("stm3000VO") Stm3000VO stm3000VO, HttpServletRequest request, HttpServletResponse response, ModelMap model ) throws Exception {

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
			stm3000VO.setPageIndex(_pageNo);
			stm3000VO.setPageSize(_pageSize);
			stm3000VO.setPageUnit(_pageSize);


			PaginationInfo paginationInfo = PagingUtil.getPaginationInfo(stm3000VO);  /** paging - 신규방식 */

			List<Stm3000VO> stm3000List = null;

			HttpSession ss = request.getSession();
			LoginVO loginVO = (LoginVO) ss.getAttribute("loginVO");

			stm3000VO.setLoginUsrId(loginVO.getUsrId());
			stm3000VO.setLicGrpId(loginVO.getLicGrpId());
			stm3000VO.setPrjId((String) ss.getAttribute("selPrjId"));

			/**
			 * 목록 조회
			 */
			int totCnt = 0;
			stm3000List =   stm3000Service.selectStm3000JobList(stm3000VO);


			/** 총 데이터의 건수 를 가져온다. */
			totCnt =  stm3000Service.selectStm3000JobListCnt(stm3000VO);
			paginationInfo.setTotalRecordCount(totCnt);

			model.addAttribute("list", stm3000List);

			//페이지 정보 보내기
			Map<String, Integer> pageMap = new HashMap<String, Integer>();
			pageMap.put("pageNo",stm3000VO.getPageIndex());
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



	/**
	 *  JENKINS 설정 등록/수정 팝업 화면으로 이동
	 *  
	 * @param request
	 * @param response
	 * @param model
	 * @return
	 * @throws Exception
	 */
	@RequestMapping(value="/stm/stm3000/stm3000/selectStm3001JobDetailView.do")
	public String selectStm3001JobDetailView( HttpServletRequest request, HttpServletResponse response, ModelMap model ) throws Exception {
		return "/stm/stm3000/stm3000/stm3001";
	}




	/*
	 *  Jenkins 설정 상세 조회
	 */
	@RequestMapping(value="/stm/stm3000/stm3000/selectStm3000JobDetailAjax.do")
	public ModelAndView selectStm3000JobDetailAjax(HttpServletRequest request, HttpServletResponse response, ModelMap model ) throws Exception {

		try{


			// request 파라미터를 map으로 변환
			Map<String, String> paramMap = RequestConvertor.requestParamToMapAddSelInfo(request, true);
			HttpSession ss = request.getSession();

			LoginVO loginVO = (LoginVO) ss.getAttribute("loginVO");

			paramMap.put("licGrpId", loginVO.getLicGrpId());	


			// 요구사항 일괄저장
			Map jenMap = stm3000Service.selectStm3000JobInfo(paramMap);

			model.addAttribute("jenInfo", jenMap);

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
	 * Jenkins 상세정보 저장
	 *  
	 * @param request
	 * @param response
	 * @param model
	 * @return
	 * @throws Exception
	 */
	@SuppressWarnings("rawtypes")
	@RequestMapping(value="/stm/stm3000/stm3000/saveStm3000JobInfoAjax.do")
	public ModelAndView saveStm3000JobInfoAjax(HttpServletRequest request, HttpServletResponse response, ModelMap model ) throws Exception {

		try{


			// request 파라미터를 map으로 변환
			Map<String, String> paramMap = RequestConvertor.requestParamToMapAddSelInfo(request, true);
			HttpSession ss = request.getSession();

			LoginVO loginVO = (LoginVO) ss.getAttribute("loginVO");

			paramMap.put("licGrpId", loginVO.getLicGrpId());	
			
			//save type
			String type = (String) paramMap.get("type");
			
			//globals.properties에서 salt값 가져오기
			String salt = EgovProperties.getProperty("Globals.oslits.salt");
			
			//기존 값
			String nowJenUsrTok = (String)paramMap.get("nowJenUsrTok");
			String nowJenTok = (String)paramMap.get("nowJenTok");
			
			//파라미터 값
			String jenUsrTok = (String)paramMap.get("jenUsrTok");
			String jenTok = (String)paramMap.get("jenTok");
			
			//암호화된 값
			String newJenUsrTok = "";
			String newJenTok = "";
			
			//수정인경우 값 변경되었는지 체크
			if("update".equals(type)){
				//기존 값과 파라미터값 비교
				if(!nowJenUsrTok.equals(jenUsrTok)){
					//값 암호화
					newJenUsrTok = CommonScrty.encryptedAria(jenUsrTok, salt);
				}else{
					newJenUsrTok = jenUsrTok;
				}
				if(!nowJenTok.equals(jenTok)){
					//값 암호화
					newJenTok = CommonScrty.encryptedAria(jenTok, salt);
				}else{
					newJenTok = nowJenTok;
				}
				/*
				String tmpnewJenUsrTok = CommonScrty.encryptedAria(jenUsrTok, salt);
				String tmpnewJenTok = CommonScrty.encryptedAria(nowJenTok, salt);
				System.out.println("############");
				System.out.println(tmpnewJenUsrTok);
				System.out.println(tmpnewJenTok);*/
			}
			//등록인경우 값 암호화
			else{
				newJenUsrTok = CommonScrty.encryptedAria(jenUsrTok, salt);
				newJenTok = CommonScrty.encryptedAria(jenTok, salt);
			}

			//입력된 비밀번호 제거
			paramMap.remove("jenUsrTok");
			paramMap.remove("jenTok");
			
			//암호화된 비밀번호 입력
			paramMap.put("jenUsrTok", newJenUsrTok);
			paramMap.put("jenTok", newJenTok);
			
			// 요구사항 일괄저장
			Object insertKey = stm3000Service.saveStm3000JobInfo(paramMap);


			//등록 성공 메시지 세팅
			model.addAttribute("message", egovMessageSource.getMessage("success.common.save"));

			return new ModelAndView("jsonView");
		}
		catch(Exception ex){
			Log.error("saveStm3000JobInfoAjax()", ex);

			//조회실패 메시지 세팅 및 저장 성공여부 세팅
			model.addAttribute("saveYN", "N");
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
	@SuppressWarnings("rawtypes")
	@RequestMapping(value="/stm/stm3000/stm3000/deleteStm3000InfoAjax.do")
	public ModelAndView deleteStm3000InfoAjax(HttpServletRequest request, HttpServletResponse response, ModelMap model ) throws Exception {

		try{


			// request 파라미터를 map으로 변환
			Map<String, String> paramMap = RequestConvertor.requestParamToMapAddSelInfo(request, true);
			HttpSession ss = request.getSession();

			int useCount = stm3000Service.selectStm3000UseCountInfo(paramMap);
			if(useCount==0){
				stm3000Service.deleteStm3000Info(paramMap);
				//등록 성공 메시지 세팅
				model.addAttribute("message", egovMessageSource.getMessage("success.common.delete"));
			}else{
				model.addAttribute("saveYN", "N");
				model.addAttribute("message", egovMessageSource.getMessage("fail.common.existInfo"));
			}

			return new ModelAndView("jsonView");
		}
		catch(Exception ex){
			Log.error("deleteStm3000InfoAjax()", ex);

			//조회실패 메시지 세팅 및 저장 성공여부 세팅
			model.addAttribute("saveYN", "N");
			model.addAttribute("message", egovMessageSource.getMessage("fail.common.save"));
			return new ModelAndView("jsonView");
		}
	}

	@SuppressWarnings("rawtypes")
	@RequestMapping(value="/stm/stm3000/stm3000/selectStm3000ConfirmConnect.do")
	public ModelAndView selectStm3000ConfirmConnect(HttpServletRequest request, HttpServletResponse response, ModelMap model ) throws Exception {

		try{

			// request 파라미터를 map으로 변환
			Map<String, String> paramMap = RequestConvertor.requestParamToMapAddSelInfo(request, true);
			HttpSession ss = request.getSession();

			Map jenMap = stm3000Service.selectStm3000JobInfo(paramMap);
			String userId=(String)jenMap.get("jenUsrId");
			String tokenId=(String)jenMap.get("jenUsrTok");
			
			//globals.properties에서 salt값 가져오기
			String salt = EgovProperties.getProperty("Globals.oslits.salt");
			
			//값 복호화
			String newTokenId = CommonScrty.decryptedAria(tokenId, salt);
			
			//빈값인경우 오류
			if(newTokenId == null || "".equals(newTokenId)){
				model.addAttribute("MSG_CD", JENKINS_SETTING_WORNING);
				return new ModelAndView("jsonView");
			}
			
			String jenJobNm=(String)jenMap.get("jenNm");
			JenkinsClient client = new JenkinsClient(userId,newTokenId);

			String url =   (String)jenMap.get("jenUrl")+"jenkins/api/json";
			String content = "";

			content = client.excuteHttpClientJenkins(url);

			Map jenkinsMap= client.getJenkinsParser(content );
			List jobList =  (List) jenkinsMap.get("jobs");
			int jobListSize = jobList.size();
			boolean isUseJob = false;
			for (int i = 0; i < jobListSize; i++) {
				Map jobMap = (Map) jobList.get(i);
				String jobName = (String) jobMap.get("name");
				if(jenJobNm.equals(jobName)){
					isUseJob = true;
				}
			}
			if(isUseJob){
				model.addAttribute("MSG_CD", JENKINS_OK);
			}else{
				model.addAttribute("MSG_CD", JENKINS_SETTING_WORNING);
			}

			return new ModelAndView("jsonView");
		}
		catch(Exception ex){
			Log.error("selectStm3000ConfirmConnect()", ex);
			if( ex instanceof HttpHostConnectException){
				model.addAttribute("MSG_CD", JENKINS_FAIL);
			}else if( ex instanceof ParseException){
				model.addAttribute("MSG_CD", JENKINS_FAIL);
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
	 * 사용가능한 job 목록 조회
	 * 
	 * @param request
	 * @param response
	 * @param model
	 * @return
	 * @throws Exception
	 */
	
	@SuppressWarnings("rawtypes")
	@RequestMapping(value="/stm/stm3000/stm3000/selectStm3000JobList.do")
	public ModelAndView selectStm3000JobList(HttpServletRequest request, HttpServletResponse response, ModelMap model ) throws Exception {
		try{
			// request 파라미터를 map으로 변환
			Map<String, String> paramMap = RequestConvertor.requestParamToMapAddSelInfo(request, true);
			HttpSession ss = request.getSession();
			String userId=(String)paramMap.get("jenUsrId");
			String tokenId=(String)paramMap.get("jenUsrTok");

			//save type
			String type = (String) paramMap.get("type");
			
			//Job 수정인경우 복호화해서 체크
			if(type != null && "update".equals(type)){
				//globals.properties에서 salt값 가져오기
				String salt = EgovProperties.getProperty("Globals.oslits.salt");
				
				//값 복호화
				tokenId = CommonScrty.decryptedAria(tokenId, salt);
				
				System.out.println("#############");
				System.out.println(tokenId);
				//빈값인경우 오류
				if(tokenId == null || "".equals(tokenId)){
					model.addAttribute("MSG_CD", JENKINS_FAIL);
					return new ModelAndView("jsonView");
				}
			}
			
			JenkinsClient client = new JenkinsClient(userId,tokenId);

			String url =   (String)paramMap.get("jenUrl")+"jenkins/api/json";
			String content = "";

			content = client.excuteHttpClientJenkins(url);

			Map jenkinsMap= client.getJenkinsParser(content );
			List jobList =  (List) jenkinsMap.get("jobs");

			model.addAttribute("list", jobList);

			model.addAttribute("message", egovMessageSource.getMessage("success.common.select"));

			return new ModelAndView("jsonView");
		}
		catch(Exception ex){
			Log.error("selectStm3000JobDetailAjax()", ex);


			model.addAttribute("message", egovMessageSource.getMessage("fail.common.select"));

			//조회실패 메시지 세팅 및 저장 성공여부 세팅			
			return new ModelAndView("jsonView");
		}
	}
	
	
	/**
	 * 
	 * Jenkins URL 검증
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
			HttpSession ss = request.getSession();

			String userId=(String)paramMap.get("jenUsrId");
			String tokenId=(String)paramMap.get("jenUsrTok");
		
			//save type
			String type = (String) paramMap.get("type");
			
			//Job 수정인경우 복호화해서 체크
			if(type != null && "update".equals(type)){
				//globals.properties에서 salt값 가져오기
				String salt = EgovProperties.getProperty("Globals.oslits.salt");
				
				//값 복호화
				tokenId = CommonScrty.decryptedAria(tokenId, salt);
				
				//빈값인경우 오류
				if(tokenId == null || "".equals(tokenId)){
					model.addAttribute("MSG_CD", JENKINS_FAIL);
					return new ModelAndView("jsonView");
				}
			}
			
			JenkinsClient client = new JenkinsClient(userId,tokenId);

			String url =   (String)paramMap.get("jenUrl")+"jenkins/api/json";
			String content = "";

			content = client.excuteHttpClientJenkins(url);

			Map jenkinsMap= client.getJenkinsParser(content );
			
			model.addAttribute("MSG_CD", JENKINS_OK);
			
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
	@SuppressWarnings({ "rawtypes", "unchecked" })
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
	@SuppressWarnings({ "rawtypes", "unchecked" })
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
