package kr.opensoftlab.oslits.dpl.dpl2000.dpl2000.web;

import java.sql.Timestamp;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.Set;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import kr.opensoftlab.oslits.com.fms.web.service.FileMngService;
import kr.opensoftlab.oslits.com.vo.LoginVO;
import kr.opensoftlab.oslits.dpl.dpl2000.dpl2000.service.Dpl2000Service;
import kr.opensoftlab.oslits.dpl.dpl2000.dpl2000.vo.Dpl2000VO;
import kr.opensoftlab.oslits.stm.stm3000.stm3000.service.Stm3000Service;
import kr.opensoftlab.oslits.stm.stm3000.stm3000.web.Stm3000Controller;
import kr.opensoftlab.sdf.jenkins.JenkinsClient;
import kr.opensoftlab.sdf.util.CommonScrty;
import kr.opensoftlab.sdf.util.OslAgileConstant;
import kr.opensoftlab.sdf.util.PagingUtil;
import kr.opensoftlab.sdf.util.RequestConvertor;

import org.apache.http.conn.HttpHostConnectException;
import org.apache.log4j.Logger;
import org.json.simple.parser.ParseException;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;

import com.google.gson.GsonBuilder;

import egovframework.com.cmm.EgovMessageSource;
import egovframework.com.cmm.service.EgovProperties;
import egovframework.com.cmm.service.FileVO;
import egovframework.rte.fdl.cmmn.trace.LeaveaTrace;
import egovframework.rte.fdl.property.EgovPropertyService;
import egovframework.rte.ptl.mvc.tags.ui.pagination.PaginationInfo;

/**
 * @Class Name : Dpl2000Controller.java
 * @Description : Dpl2000Controller Controller class
 * @Modification Information
 *
 * @author 강재민
 * @since 2016.01.30
 * @version 1.0
 * @see
 *   수정일      수정자           수정내용
 *  -------    --------    ---------------------------
 *   2018.10.08  공대영          배포관련 기능 추가
 * 
 *  Copyright (C)  All right reserved.
 */

@Controller
public class Dpl2000Controller {
	/** 로그4j 로거 로딩 */
	protected Logger Log = Logger.getLogger(this.getClass());



	/** Dpl2000Service DI */
	@Resource(name = "dpl2000Service")
	private Dpl2000Service dpl2000Service;

	/** EgovMessageSource */
	@Resource(name = "egovMessageSource")
	EgovMessageSource egovMessageSource;

	/** EgovPropertyService */
	@Resource(name = "propertiesService")
	protected EgovPropertyService propertiesService;

	/** TRACE */
	@Resource(name = "leaveaTrace")
	LeaveaTrace leaveaTrace;

	/** FileMngService */
	@Resource(name="fileMngService")
	private FileMngService fileMngService;
	
	@Resource(name="stm3000Service")
	private Stm3000Service stm3000Service;
	


	/**
	 * Dpl2000 배포버전별 요구사항 확인 목록 조회
	 * 
	 * @desc 1. 세션의 프로젝트 ID
	 *       2. req4300VO 에 1번 을 셋팅하고 쿼리를 호출한다.
	 * @param request
	 * @param response
	 * @throws Exception
	 */
	@RequestMapping(value="/dpl/dpl2000/dpl2000/selectDpl2000ReqAjaxList.do")
	public ModelAndView selectDpl2000ReqAjaxList(@ModelAttribute("dpl2000VO") Dpl2000VO dpl2000VO, HttpServletRequest request, HttpServletResponse response, ModelMap model )	throws Exception {

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
			dpl2000VO.setPageIndex(_pageNo);
			dpl2000VO.setPageSize(_pageSize);
			dpl2000VO.setPageUnit(_pageSize);

			PaginationInfo paginationInfo = PagingUtil.getPaginationInfo(dpl2000VO); /** paging - 신규방식 */

			List<Dpl2000VO> dpl2000List = null;

			HttpSession ss = request.getSession();
			String prjId	= (String) ss.getAttribute("selPrjId");
			String licGrpId = ((LoginVO) ss.getAttribute("loginVO")).getLicGrpId();

			dpl2000VO.setPrjId(prjId);
			dpl2000VO.setLicGrpId(licGrpId);

			int totCnt = 0;
			dpl2000List = dpl2000Service.selectDpl2000List(dpl2000VO);

			/** 총 데이터의 건수 를 가져온다. */
			totCnt = dpl2000Service.selectDpl2000ListCnt(dpl2000VO);
			paginationInfo.setTotalRecordCount(totCnt);


			model.addAttribute("list", dpl2000List); 	/** 조회 목록 List 형태로 화면에 Return 한다. */

			//페이지 정보 보내기
			Map<String, Integer> pageMap = new HashMap<String, Integer>();
			pageMap.put("pageNo",dpl2000VO.getPageIndex());
			pageMap.put("listCount", dpl2000List.size());
			pageMap.put("totalPages", paginationInfo.getTotalPageCount());
			pageMap.put("totalElements", totCnt);
			pageMap.put("pageSize", _pageSize);

			model.addAttribute("page", pageMap);
			return new ModelAndView("jsonView");

		}catch(Exception ex){
			Log.error("selectDpl2000ReqAjaxList()", ex);

			//조회실패 메시지 세팅 및 저장 성공여부 세팅
			Log.error("selectDpl2000View()", ex);
			model.addAttribute("message", egovMessageSource.getMessage("fail.common.select"));
			throw new Exception(ex.getMessage());
		}
	}



	/**
	 * Dpl2000 배포버전별 요구사항 확인 디테일 정보 조회 Ajax
	 * 요구사항 정보를 조회한다.
	 * @param 
	 * @return 
	 * @exception Exception
	 */
	@SuppressWarnings("rawtypes")
	@RequestMapping(value="/dpl/dpl2000/dpl2000/selectDpl2000DetailAjax.do")
	public ModelAndView selectDpl2000DetailAjax(HttpServletRequest request, HttpServletResponse response, ModelMap model ) throws Exception {

		try{

			// request 파라미터를 map으로 변환
			Map<String, String> paramMap = RequestConvertor.requestParamToMapAddSelInfo(request, true);



			// 배포버전별 요구사항 확인 디테일 단건정보 조회
			Map selectDpl2000DetailAjax = (Map) dpl2000Service.selectDpl2000DetailAjax(paramMap);

			//배포버전별 요구사항 확인 코멘트 목록 조회
			List selectDpl2000CommentListAjax = (List) dpl2000Service.selectDpl2000CommentListAjax(paramMap);

			model.addAttribute("selectDpl2000DetailAjax", selectDpl2000DetailAjax);
			model.addAttribute("selectDpl2000CommentListAjax", selectDpl2000CommentListAjax);

			//atchFileId값을 넘기기 위한 FileVO
			FileVO fileVO = new FileVO();
			fileVO.setAtchFileId((String)selectDpl2000DetailAjax.get("atchFileId"));

			//파일 리스트 조회
			List<FileVO> fileList = fileMngService.fileDownList(fileVO);

			model.addAttribute("fileList",fileList);

			//등록 성공 메시지 세팅
			model.addAttribute("message", egovMessageSource.getMessage("success.common.select"));

			return new ModelAndView("jsonView");
		}
		catch(Exception ex){
			Log.error("selectDpl2000DetailAjax()", ex);
			//조회실패 메시지 세팅 및 저장 성공여부 세팅
			model.addAttribute("saveYN", "N");
			model.addAttribute("message", egovMessageSource.getMessage("fail.common.select"));
			return new ModelAndView("jsonView");
		}
	}



	/**
	 * Dpl2000 배포버전별 요구사항 확인 코멘트 등록 AJAX
	 * 요구사항 정보를 조회한다.
	 * @param 
	 * @return 
	 * @exception Exception
	 */
	@SuppressWarnings("rawtypes")
	@RequestMapping(value="/dpl/dpl2000/dpl2000/insertDpl2000ReqCommentInfoAjax.do")
	public ModelAndView insertDpl2000ReqCommentInfoAjax(HttpServletRequest request, HttpServletResponse response, ModelMap model ) throws Exception {
		try{
			//request 파라미터를 map으로 변환
			Map<String, String> paramMap = RequestConvertor.requestParamToMapAddSelInfo(request, true);

			//배포버전별 요구사항 확인 코멘트 등록
			dpl2000Service.insertDpl2000ReqCommentInfoAjax(paramMap);

			//배포버전별 요구사항 확인 코멘트 목록 조회
			List reqCommentList = (List) dpl2000Service.selectDpl2000ReqCommentListAjax(paramMap);
			model.addAttribute("reqCommentList", reqCommentList);

			//등록 성공 메시지 세팅
			model.addAttribute("message", egovMessageSource.getMessage("success.common.insert"));

			return new ModelAndView("jsonView");
		}
		catch(Exception ex){
			Log.error("insertDpl2000DplCommentInfoAjax()", ex);
			//조회실패 메시지 세팅 및 저장 성공여부 세팅
			model.addAttribute("saveYN", "N");
			model.addAttribute("message", egovMessageSource.getMessage("fail.common.insert"));
			return new ModelAndView("jsonView");
		}
	}

	/**
	 * 배포 계획별 요구사항 확인 및 배포 화면 이동
	 * 
	 * 
	 * @param request
	 * @param response
	 * @param model
	 * @return
	 * @throws Exception
	 */
	@RequestMapping(value="/dpl/dpl2000/dpl2000/selectDpl2000View.do")
	public String selectDpl2000View( HttpServletRequest request, HttpServletResponse response, ModelMap model ) throws Exception {

		Map<String, String> paramMap = RequestConvertor.requestParamToMapAddSelInfo(request, true);
		paramMap.put("prjId", request.getSession().getAttribute("selPrjId").toString());
		//개발주기 목록 조회
				//개발주기 목록 조회
		List<Map> dplVerList = dpl2000Service.selectDpl2000dplVerList(paramMap);
		String dplVerListJson = (new GsonBuilder().serializeNulls().create()).toJsonTree(dplVerList).toString();
		model.addAttribute("dplList", dplVerListJson);

		return "/dpl/dpl2000/dpl2000/dpl2000";
	}

	/*
	 * 배포 버전별 요구사항 확인 Job 별 빌드 결과 조회
	 */
	@SuppressWarnings({ "unchecked", "rawtypes" })
	@RequestMapping(value="/dpl/dpl2000/dpl2000/selectDpl2000JobBuildListAjax.do")
	public ModelAndView selectDpl2000JobBuildListAjax(HttpServletRequest request, HttpServletResponse response,ModelMap model) throws Exception {
		
		boolean isJenkinsConnect = true;
		try {

			// 리퀘스트에서 넘어온 파라미터를 맵으로 세팅
			Map paramMap = RequestConvertor.requestParamToMap(request, true);
			paramMap.put("prjId", request.getSession().getAttribute("selPrjId"));
			HttpSession ss = request.getSession();
			LoginVO loginVO = (LoginVO) ss.getAttribute("loginVO");

			paramMap.put("licGrpId", loginVO.getLicGrpId());
			paramMap.put("prjGrpId", (String)ss.getAttribute("selPrjGrpId"));

			List<Map> list = dpl2000Service.selectDpl2000BuildStatus(paramMap);

			for( Map map : list){
				String userId=(String)map.get("jenUsrId");
				String tokenId=(String)map.get("jenUsrTok");			
				
				//globals.properties에서 salt값 가져오기
				String salt = EgovProperties.getProperty("Globals.oslits.salt");
				
				//값 복호화
				tokenId = CommonScrty.decryptedAria(tokenId, salt);
				
				//빈값인경우 오류
				if(tokenId == null || "".equals(tokenId)){
					model.addAttribute("messageCode", "2001");
					model.addAttribute("message", egovMessageSource.getMessage("fail.common.connectJenkins"));
					return new ModelAndView("jsonView");
				}
				
				JenkinsClient client = null;
				try{
					client = new JenkinsClient(userId,tokenId);

					String url =   (String)map.get("jenUrl")+"jenkins/job/"+map.get("jenNm")+"/"+map.get("bldNum")+"/api/json";
					String content = client.excuteHttpClientJenkins(url);
					Map jenkinsMap= client.getJenkinsParser(content );

					map.put("bldSts", jenkinsMap.get("result"));
					dpl2000Service.updateDpl2000BuildStatus(map);
				}catch(HttpHostConnectException ex){
					isJenkinsConnect= false;
				}
			}


			// 배포 버전 정보 리스트 조회
			List jobList  = dpl2000Service.selectDpl2000JobBuildList(paramMap);

			model.addAttribute("jobList", jobList);
			
			if(isJenkinsConnect){
				// 조회성공메시지 세팅
				model.addAttribute("message",egovMessageSource.getMessage("success.common.select"));
			}else{
				model.addAttribute("messageCode", "2001");
				model.addAttribute("message", egovMessageSource.getMessage("fail.common.connectJenkins"));
			}
			

			return new ModelAndView("jsonView", model);
		} catch (Exception ex) {
			Log.error("selectDpl2000JobBuildListAjax()", ex);

			// 조회실패 메시지 세팅
			model.addAttribute("message",egovMessageSource.getMessage("fail.common.select"));
			return new ModelAndView("jsonView", model);
		}
	}

	/**
	 * 배포 상세 팝업 이동
	 * 
	 * @param request
	 * @param response
	 * @param model
	 * @return
	 * @throws Exception
	 */
	@RequestMapping(value="/dpl/dpl2000/dpl2000/selectDpl2001View.do")
	public String selectDpl2001View(HttpServletRequest request, HttpServletResponse response, ModelMap model ) throws Exception {
		return "/dpl/dpl2000/dpl2000/dpl2001";
	}
	
	@SuppressWarnings({ "unchecked", "rawtypes" })
	@RequestMapping(value="/dpl/dpl2000/dpl2000/selectDpl2000JobBuildList.do")
	public ModelAndView selectDpl2000JobBuildList(HttpServletRequest request, HttpServletResponse response,ModelMap model) throws Exception {
		try {

			// 리퀘스트에서 넘어온 파라미터를 맵으로 세팅
			Map paramMap = RequestConvertor.requestParamToMap(request, true);
			paramMap.put("prjId", request.getSession().getAttribute("selPrjId"));
			HttpSession ss = request.getSession();
    		LoginVO loginVO = (LoginVO) ss.getAttribute("loginVO");

        	paramMap.put("licGrpId", loginVO.getLicGrpId());
			paramMap.put("prjGrpId", (String)ss.getAttribute("selPrjGrpId"));
			// 배포 버전 정보 리스트 조회
			List jobList  = dpl2000Service.selectDpl2000JobBuildList(paramMap);

			model.addAttribute("jobList", jobList);

			// 조회성공메시지 세팅
			model.addAttribute("message",egovMessageSource.getMessage("success.common.select"));

			return new ModelAndView("jsonView", model);
		} catch (Exception ex) {
			Log.error("selectDpl2000JobBuildList()", ex);

			// 조회실패 메시지 세팅
			model.addAttribute("message",egovMessageSource.getMessage("fail.common.select"));
			return new ModelAndView("jsonView", model);
		}
	}
	
	
	/**
	 * Job의 빌드상세 내역 조회
	 * 
	 * @desc 1. Job의 빌드상세 내역 조회한다.
	 *       2.  
	 * @param request
	 * @param response
	 * @param model
	 * @return
	 * @throws Exception
	 */
	@SuppressWarnings({ "rawtypes", "unchecked" })
	@RequestMapping(value="/dpl/dpl2000/dpl2000/selectDpl2000JenkinsBuildDetailAjax.do")
	public ModelAndView selectDpl2000JenkinsBuildDetailAjax(HttpServletRequest request, HttpServletResponse response, ModelMap model ) throws Exception {

		try{

			//리퀘스트에서 넘어온 파라미터를 맵으로 세팅
			Map paramMap = RequestConvertor.requestParamToMap(request,true);

			//로그인VO 가져오기
			HttpSession ss = request.getSession();
			paramMap.put("prjId", ss.getAttribute("selPrjId"));
			paramMap.put("authGrpId", ss.getAttribute("selAuthGrpId"));
			LoginVO loginVO = (LoginVO) ss.getAttribute("loginVO");
			paramMap.put("licGrpId", loginVO.getLicGrpId());	

			List<Map> userList=stm3000Service.selectStm3000JenkinsUserList(paramMap);
			
			Map userMap = (Map)userList.get(0);
			String jenId=(String)userMap.get("jenId");
			String userId=(String)userMap.get("jenUsrId");
			String tokenId=(String)userMap.get("jenUsrTok");
			
			//globals.properties에서 salt값 가져오기
			String salt = EgovProperties.getProperty("Globals.oslits.salt");
			
			//값 복호화
			tokenId = CommonScrty.decryptedAria(tokenId, salt);
			
			//빈값인경우 오류
			if(tokenId == null || "".equals(tokenId)){
				model.addAttribute("messageCode", "2001");
				model.addAttribute("message", egovMessageSource.getMessage("fail.common.connectJenkins"));
				return new ModelAndView("jsonView");
			}
			
			//역할 체크
			//현재 세션 역할
			String selAuthGrpId = (String) ss.getAttribute("selAuthGrpId");
			paramMap.put("jenId",jenId);
			//해당 jenId의 허용 역할 목록
			List<Map> jenAuthGrpList = stm3000Service.selectStm3000JenAuthGrpList(paramMap);

			//허용 역할이 있는경우에만 체크
			if(jenAuthGrpList != null && jenAuthGrpList.size() > 0){
				//역할 있는지 체크
				boolean authChk = false;
				
				for(Map jenAuthGrpInfo : jenAuthGrpList){
					if(selAuthGrpId.equals(jenAuthGrpInfo.get("authGrpId"))){
						authChk = true;
						break;
					}
				}
				
				//허용 역할이 없는경우 오류
				if(!authChk){
					model.addAttribute("errorYn", "Y");
					model.addAttribute("consoleText", "콘솔 로그 열람 권한이 없습니다.</br></br>관리자에게 문의해주시기 바랍니다.");
					return new ModelAndView("jsonView");
					
				}
			}
			
			if(!"".equals(paramMap.get("buildnumber"))){
				JenkinsClient client = new JenkinsClient(userId,tokenId);
				
				String url =   (String)userMap.get("jenUrl")+"jenkins/job/"+paramMap.get("jobname")+"/"+paramMap.get("buildnumber")+"/api/json";
				String content = client.excuteHttpClientJenkins(url);
				Map jenkinsMap= client.getJenkinsParser(content );

				model.addAttribute("actions", jenkinsMap );


				String consoleurl =  (String)userMap.get("jenUrl")+"jenkins/job/"+paramMap.get("jobname")+"/"+paramMap.get("buildnumber")+"/consoleText";
				String consoleText = client.excuteHttpClientJenkins(consoleurl);

				model.addAttribute("consoleText", consoleText );
	
			}

			//페이지 정보 보내기

			//조회성공메시지 세팅
			model.addAttribute("errorYn", "N");
			model.addAttribute("message", egovMessageSource.getMessage("success.common.select"));

			return new ModelAndView("jsonView", paramMap);
		}
		catch(Exception ex){
			Log.error("selectDpl2000JenkinsBuildDetailAjax()", ex);
			if( ex instanceof HttpHostConnectException){
				model.addAttribute("messageCode", "2001");
				model.addAttribute("message", egovMessageSource.getMessage("fail.common.connectJenkins"));
			}else if( ex instanceof ParseException){
				model.addAttribute("messageCode", "2001");
				model.addAttribute("message", egovMessageSource.getMessage("fail.common.connectJenkins"));
			}
			
			
			else{
				model.addAttribute("message", egovMessageSource.getMessage("fail.common.select"));
			}
			return new ModelAndView("jsonView");
		}
	}
	
	/**
	 * 
	 * 빌드 이력조회 팝업
	 * 
	 * @param request
	 * @param response
	 * @param model
	 * @return
	 * @throws Exception
	 */
	@RequestMapping(value="/dpl/dpl2000/dpl2000/selectDpl2002View.do")
	public String selectDpl2002View(HttpServletRequest request, HttpServletResponse response, ModelMap model ) throws Exception {
		return "/dpl/dpl2000/dpl2000/dpl2002";
	}
	
	

	/**
	 * Jenkins 서버에 등록된 Job목록 조회
	 * 
	 * @desc 1. Jenkins 서버에 등록된 Job목록 조회한다.
	 *       2.
	 *        
	 * @param request
	 * @param response
	 * @param model
	 * @return
	 * @throws Exception
	 */
	@SuppressWarnings({ "rawtypes", "unchecked" })
	@RequestMapping(value="/dpl/dpl2000/dpl2000/selectDpl2000JenkinsJobListAjax.do")
	public ModelAndView selectDpl2000JenkinsJobListAjax(HttpServletRequest request, HttpServletResponse response, ModelMap model ) throws Exception {

		try{

			//리퀘스트에서 넘어온 파라미터를 맵으로 세팅
			Map paramMap = RequestConvertor.requestParamToMap(request,true);

			//로그인VO 가져오기
			HttpSession ss = request.getSession();
			paramMap.put("prjId", ss.getAttribute("selPrjId"));
			paramMap.put("authGrpId", ss.getAttribute("selAuthGrpId"));
			LoginVO loginVO = (LoginVO) ss.getAttribute("loginVO");
			paramMap.put("licGrpId", loginVO.getLicGrpId());				

			
			List<Map> userList=stm3000Service.selectStm3000JenkinsUserList(paramMap);
			
			Map<String,Map> jenkinsInfoMap = new HashMap<String,Map>();
			for(Map userMap  : userList){
				String jenUrl =   (String)userMap.get("jenUrl");
				if(!jenkinsInfoMap.containsKey(jenUrl)){
					jenkinsInfoMap.put(jenUrl, userMap);
				}
			}
			Set<String> keySet= jenkinsInfoMap.keySet();

			List list = new ArrayList();
			
			for(String key  : keySet){
				Map info = jenkinsInfoMap.get(key);
				 
				String userId=(String)info.get("jenUsrId");
				String tokenId=(String)info.get("jenUsrTok");
				
				//globals.properties에서 salt값 가져오기
				String salt = EgovProperties.getProperty("Globals.oslits.salt");
				
				//값 복호화
				tokenId = CommonScrty.decryptedAria(tokenId, salt);
				
				//빈값인경우 오류
				if(tokenId == null || "".equals(tokenId)){
					model.addAttribute("messageCode", "2001");
					model.addAttribute("message", egovMessageSource.getMessage("fail.common.connectJenkins"));
					return new ModelAndView("jsonView");
				}
				
				JenkinsClient client = new JenkinsClient(userId,tokenId);
				
				String url =   (String)info.get("jenUrl")+"jenkins/api/json";
				String content = "";

				content = client.excuteHttpClientJenkins(url);
				
				Map jenkinsMap= client.getJenkinsParser(content );
				/*  서버 job 목록 정보 조회 */
				List jobList =  (List) jenkinsMap.get("jobs");
				int jobListSize = jobList.size();
				
				for (int i = 0; i < jobListSize; i++) {
					Map jobMap = (Map) jobList.get(i);
					String jobName = (String) jobMap.get("name");
					String jobUrl = (String) jobMap.get("url");
					/* 사용자 권한 지정된 	목록	 */
					for (Map map: userList) {
						String jenJobNm=(String)map.get("jenNm");
						String jenJobUrl=(String)map.get("jenUrl");
						if(jenJobNm.equals(jobName) &&  key.equals(jenJobUrl) ){
							jobMap.put("jenId", (String)map.get("jenId"));
							list.add(jobMap);
						}
					}
				}
			}
			
			model.addAttribute("joblist", list );

			//조회성공메시지 세팅
			model.addAttribute("message", egovMessageSource.getMessage("success.common.select"));

			return new ModelAndView("jsonView", paramMap);
		}
		catch(Exception ex){
			Log.error("selectDpl2000JenkinsJobListAjax()", ex);
			if( ex instanceof HttpHostConnectException){
				model.addAttribute("messageCode", "2001");
				model.addAttribute("message", egovMessageSource.getMessage("fail.common.connectJenkins"));
			}
			else if( ex instanceof ParseException){
				model.addAttribute("messageCode", "2001");
				model.addAttribute("message", egovMessageSource.getMessage("fail.common.connectJenkins"));
			}
			else{
				model.addAttribute("messageCode", "2002");
				model.addAttribute("message", egovMessageSource.getMessage("fail.common.select"));
			}

			return new ModelAndView("jsonView");
		}
	}


	public String getDateFormat(long timestampdate){
		String pattern = "yyyy-MM-dd hh:mm:ss";

		SimpleDateFormat sdfCurrent = new SimpleDateFormat(pattern, Locale.KOREA);
		Timestamp ts = new Timestamp(timestampdate);
		String rtnStr = sdfCurrent.format(ts.getTime());

		return rtnStr;
	}
	

	/**
	 * 해당 Job의 빌드목록 조회
	 * 
	 * @desc 1. 해당 Job의 빌드목록 조회한다.
	 *       2. 
	 * @param request
	 * @param response
	 * @param model
	 * @return
	 * @throws Exception
	 */
	@SuppressWarnings({ "rawtypes", "unchecked" })
	@RequestMapping(value="/dpl/dpl2000/dpl2000/selectDpl2000JenkinsBuildListAjax.do")
	public ModelAndView selectDpl2000JenkinsBuildListAjax(HttpServletRequest request, HttpServletResponse response, ModelMap model ) throws Exception {

		try{

			//리퀘스트에서 넘어온 파라미터를 맵으로 세팅
			Map paramMap = RequestConvertor.requestParamToMap(request,true);

			//로그인VO 가져오기
			HttpSession ss = request.getSession();
			paramMap.put("prjId", ss.getAttribute("selPrjId"));
			paramMap.put("authGrpId", ss.getAttribute("selAuthGrpId"));
			LoginVO loginVO = (LoginVO) ss.getAttribute("loginVO");
			paramMap.put("licGrpId", loginVO.getLicGrpId());	


			List<Map> userList=stm3000Service.selectStm3000JenkinsUserList(paramMap);
			
			Map userMap = (Map)userList.get(0);
			String jenId=(String)userMap.get("jenId");
			String userId=(String)userMap.get("jenUsrId");
			String tokenId=(String)userMap.get("jenUsrTok");
			
			//역할 체크
			//현재 세션 역할
			String selAuthGrpId = (String) ss.getAttribute("selAuthGrpId");
			paramMap.put("jenId",jenId);
			//해당 jenId의 허용 역할 목록
			List<Map> jenAuthGrpList = stm3000Service.selectStm3000JenAuthGrpList(paramMap);

			//허용 역할이 있는경우에만 체크
			if(jenAuthGrpList != null && jenAuthGrpList.size() > 0){
				//역할 있는지 체크
				boolean authChk = false;
				
				for(Map jenAuthGrpInfo : jenAuthGrpList){
					if(selAuthGrpId.equals(jenAuthGrpInfo.get("authGrpId"))){
						authChk = true;
						break;
					}
				}
				
				//허용 역할이 없는경우 오류
				if(!authChk){
					model.addAttribute("MSG_CD", Stm3000Controller.JENKINS_FAIL);
					model.addAttribute("errorYn", "Y");
					model.addAttribute("consoleText", "콘솔 로그 열람 권한이 없습니다.</br></br>관리자에게 문의해주시기 바랍니다.");
					model.addAttribute("message", "빌드 실행 권한이 없습니다.</br></br>관리자에게 문의해주시기 바랍니다.");
					return new ModelAndView("jsonView");
					
				}
			}
			
			//globals.properties에서 salt값 가져오기
			String salt = EgovProperties.getProperty("Globals.oslits.salt");
			
			//값 복호화
			tokenId = CommonScrty.decryptedAria(tokenId, salt);
			
			//빈값인경우 오류
			if(tokenId == null || "".equals(tokenId)){
				model.addAttribute("MSG_CD", Stm3000Controller.JENKINS_FAIL);
				return new ModelAndView("jsonView");
			}
			
			JenkinsClient client = new JenkinsClient(userId,tokenId);
			
			String url =   (String)userMap.get("jenUrl")+"jenkins/job/"+paramMap.get("jobname")+"/api/json";
			String content = client.excuteHttpClientJenkins(url);
			Map jenkinsMap= client.getJenkinsParser(content );

			model.addAttribute("buildlist", jenkinsMap.get("builds") );
			model.addAttribute("lastBuild", jenkinsMap.get("lastBuild") );
			model.addAttribute("lastCompletedBuild", jenkinsMap.get("lastCompletedBuild") );

			//페이지 정보 보내기

			//조회성공메시지 세팅
			model.addAttribute("message", egovMessageSource.getMessage("success.common.select"));
			model.addAttribute("MSG_CD", Stm3000Controller.JENKINS_OK);
			return new ModelAndView("jsonView", paramMap);
		}
		catch(Exception ex){
			Log.error("selectDpl2000JenkinsBuildListAjax()", ex);

			if( ex instanceof HttpHostConnectException){
				model.addAttribute("MSG_CD", Stm3000Controller.JENKINS_FAIL);
			}else if( ex instanceof ParseException){
				model.addAttribute("MSG_CD", Stm3000Controller.JENKINS_FAIL);
			}else{
				model.addAttribute("MSG_CD", Stm3000Controller.JENKINS_FAIL);
			}
			//조회실패 메시지 세팅
			model.addAttribute("message", egovMessageSource.getMessage("fail.common.select"));
			return new ModelAndView("jsonView");
		}
	}

	@RequestMapping(value="/dpl/dpl2000/dpl2000/selectDpl2003BulidConsoleView.do")
	public String selectDpl2003BulidConsoleView( HttpServletRequest request, HttpServletResponse response, ModelMap model ) throws Exception {
		return "/dpl/dpl2000/dpl2000/dpl2003";
	}

	@RequestMapping(value="/dpl/dpl2000/dpl2000/selectDpl2004BulidConsoleView.do")
	public String selectDpl2004BulidConsoleView( HttpServletRequest request, HttpServletResponse response, ModelMap model ) throws Exception {
		return "/dpl/dpl2000/dpl2000/dpl2004";
	}
	
	

	/**
	 * Job의 빌드을 요청
	 * 
	 * @desc 1. Job의 빌드을 요청한다.
	 *       2. 
	 *        
	 * @param request
	 * @param response
	 * @param model
	 * @return
	 * @throws Exception
	 */
	@SuppressWarnings({ "rawtypes", "unchecked" })
	@RequestMapping(value="/dpl/dpl2000/dpl2000/runDpl2000JenkinsBuildAjax.do")
	public ModelAndView runDpl2000JenkinsBuildAjax(HttpServletRequest request, HttpServletResponse response, ModelMap model ) throws Exception {

		try{

			//리퀘스트에서 넘어온 파라미터를 맵으로 세팅
			Map paramMap = RequestConvertor.requestParamToMap(request,true);

			//로그인VO 가져오기
			HttpSession ss = request.getSession();
			paramMap.put("prjId", ss.getAttribute("selPrjId"));
			paramMap.put("authGrpId", ss.getAttribute("selAuthGrpId"));
			LoginVO loginVO = (LoginVO) ss.getAttribute("loginVO");
			paramMap.put("licGrpId", loginVO.getLicGrpId());	

			List<Map> userList=stm3000Service.selectStm3000JenkinsUserList(paramMap);
			
			Map userMap = (Map)userList.get(0);
			String userId=(String)userMap.get("jenUsrId");
			String tokenId=(String)userMap.get("jenUsrTok");
			String jobToken ="";
			int jenUsrSize = userList.size();
			
			/* 프로젝트에서 허용된 job목록에서 빌드 Token 가져오기  */
			
			jobToken=(String)userMap.get("jenTok");
			
			//globals.properties에서 salt값 가져오기
			String salt = EgovProperties.getProperty("Globals.oslits.salt");
			
			//값 복호화
			tokenId = CommonScrty.decryptedAria(tokenId, salt);
			jobToken = CommonScrty.decryptedAria(jobToken, salt);
			
			//빈값인경우 오류
			if(tokenId == null || "".equals(tokenId) || jobToken == null || "".equals(jobToken)){
				//조회실패 메시지 세팅
				model.addAttribute("message", egovMessageSource.getMessage("fail.common.select"));
				return new ModelAndView("jsonView");
			}
			
			JenkinsClient client = new JenkinsClient(userId,tokenId);
			
			String url =   (String)userMap.get("jenUrl")+"jenkins/job/"+paramMap.get("jobname")+"/build?token="+jobToken;
			String content = client.excuteHttpClientJenkins(url);
			
			
			String buildUrl =   (String)userMap.get("jenUrl")+"jenkins/job/"+paramMap.get("jobname")+"/api/json";
			String buildContent = client.excuteHttpClientJenkins(buildUrl);
			Map jenkinsMap= client.getJenkinsParser(buildContent );
			
			List buildList =  (List) jenkinsMap.get("builds");
			
			Map buildMap=(Map) buildList.get(0);
					
			paramMap.put("bldNum", String.valueOf( buildMap.get("number")));
			
			paramMap.put("jenNm", paramMap.get("jobname") );
			
			dpl2000Service.insertDpl1100logHistory(paramMap);

			model.addAttribute("content", content );
			//조회성공메시지 세팅
			model.addAttribute("message", egovMessageSource.getMessage("success.common.select"));

			return new ModelAndView("jsonView", paramMap);
		}
		catch(Exception ex){
			Log.error("runDpl2000JenkinsBuildAjax()", ex);

			//조회실패 메시지 세팅
			model.addAttribute("message", egovMessageSource.getMessage("fail.common.select"));
			return new ModelAndView("jsonView");
		}
	}
	
	/**
	 * 빌드의 콘솔 내용 조회
	 * 
	 * @desc 1. 빌드의 콘솔 내용 조회한다.
	 *       2. 
	 * @param request
	 * @param response
	 * @param model
	 * @return
	 * @throws Exception
	 */
	@SuppressWarnings({ "rawtypes", "unchecked" })
	@RequestMapping(value="/dpl/dpl2000/dpl2000/selectDpl2000JenkinsBuildConsoleAjax.do")
	public ModelAndView selectDpl2000JenkinsBuildConsoleAjax(HttpServletRequest request, HttpServletResponse response, ModelMap model ) throws Exception {

		try{

			//리퀘스트에서 넘어온 파라미터를 맵으로 세팅
			Map paramMap = RequestConvertor.requestParamToMap(request,true);

			//로그인VO 가져오기
			HttpSession ss = request.getSession();
			paramMap.put("prjId", ss.getAttribute("selPrjId"));
			paramMap.put("authGrpId", ss.getAttribute("selAuthGrpId"));
			LoginVO loginVO = (LoginVO) ss.getAttribute("loginVO");
			paramMap.put("licGrpId", loginVO.getLicGrpId());	

			List<Map> userList=stm3000Service.selectStm3000JenkinsUserList(paramMap);
			
			Map userMap = (Map)userList.get(0);
			String userId=(String)userMap.get("jenUsrId");
			String tokenId=(String)userMap.get("jenUsrTok");	
			

			//globals.properties에서 salt값 가져오기
			String salt = EgovProperties.getProperty("Globals.oslits.salt");
			
			//값 복호화
			tokenId = CommonScrty.decryptedAria(tokenId, salt);
			
			//빈값인경우 오류
			if(tokenId == null || "".equals(tokenId)){
				model.addAttribute("MSG_CD", Stm3000Controller.JENKINS_FAIL);
				return new ModelAndView("jsonView");
			}
			
			JenkinsClient client = new JenkinsClient(userId,tokenId);
			
			String url =   (String)userMap.get("jenUrl")+"jenkins/job/"+paramMap.get("jobname")+"/api/json";
			String content = client.excuteHttpClientJenkins(url);
			Map jenkinsMap= client.getJenkinsParser(content );

			
			List buildList =  (List) jenkinsMap.get("builds");
			
			Map buildMap=(Map) buildList.get(0);
		

			String consoleurl =  (String)userMap.get("jenUrl")+"jenkins/job/"+paramMap.get("jobname")+"/"+ String.valueOf(buildMap.get("number"))+"/consoleText";
			String consoleText = client.excuteHttpClientJenkins(consoleurl);

			model.addAttribute("consoleText", consoleText );


			//조회성공메시지 세팅
			model.addAttribute("message", egovMessageSource.getMessage("success.common.select"));

			return new ModelAndView("jsonView", paramMap);
		}
		catch(Exception ex){
			Log.error("selectDpl2000JenkinsBuildConsoleAjax()", ex);

			//조회실패 메시지 세팅
			model.addAttribute("message", egovMessageSource.getMessage("fail.common.select"));
			return new ModelAndView("jsonView");
		}
	}
	
	
	/**
	 * 빌드의 진행 상태를 조회
	 * 
	 * @desc 1. 빌드의 진행 상태를 조회한다.
	 *       2. 
	 * @param request
	 * @param response
	 * @param model
	 * @return
	 * @throws Exception
	 */
	@SuppressWarnings({ "rawtypes", "unchecked" })
	@RequestMapping(value="/dpl/dpl2000/dpl2000/selectDpl2000JenkinsBuildStatusAjax.do")
	public ModelAndView selectDpl2000JenkinsBuildStatusAjax(HttpServletRequest request, HttpServletResponse response, ModelMap model ) throws Exception {

		try{

			//리퀘스트에서 넘어온 파라미터를 맵으로 세팅
			Map paramMap = RequestConvertor.requestParamToMap(request,true);

			//로그인VO 가져오기
			HttpSession ss = request.getSession();
			paramMap.put("prjId", ss.getAttribute("selPrjId"));
			paramMap.put("authGrpId", ss.getAttribute("selAuthGrpId"));
			LoginVO loginVO = (LoginVO) ss.getAttribute("loginVO");
			paramMap.put("licGrpId", loginVO.getLicGrpId());	


			List<Map> userList=stm3000Service.selectStm3000JenkinsUserList(paramMap);
			
			Map userMap = (Map)userList.get(0);
			String userId=(String)userMap.get("jenUsrId");
			String tokenId=(String)userMap.get("jenUsrTok");			

			//globals.properties에서 salt값 가져오기
			String salt = EgovProperties.getProperty("Globals.oslits.salt");
			
			//값 복호화
			tokenId = CommonScrty.decryptedAria(tokenId, salt);
			
			//빈값인경우 오류
			if(tokenId == null || "".equals(tokenId)){
				model.addAttribute("MSG_CD", Stm3000Controller.JENKINS_FAIL);
				return new ModelAndView("jsonView");
			}
			
			JenkinsClient client = new JenkinsClient(userId,tokenId);
			
			
			String url =   (String)userMap.get("jenUrl")+"jenkins/job/"+paramMap.get("jobname")+"/api/json";
			String content = client.excuteHttpClientJenkins(url);
			Map jenkinsMap= client.getJenkinsParser(content );

			
			List buildList =  (List) jenkinsMap.get("builds");
			
			Map buildMap=(Map) buildList.get(0);
			
					
			String statusUrl =   (String)userMap.get("jenUrl")+"jenkins/job/"+paramMap.get("jobname")+"/"+String.valueOf( buildMap.get("number"))+"/api/json";
			String statusContent = client.excuteHttpClientJenkins(statusUrl);
			Map statusJenkinsMap= client.getJenkinsParser(statusContent );

			model.addAttribute("actions", statusJenkinsMap );
			model.addAttribute("number", buildMap.get("number") );

			//조회성공메시지 세팅
			model.addAttribute("message", egovMessageSource.getMessage("success.common.select"));

			return new ModelAndView("jsonView", paramMap);
		}
		catch(Exception ex){
			Log.error("selectDpl2000JenkinsBuildStatusAjax()", ex);

			//조회실패 메시지 세팅
			model.addAttribute("message", egovMessageSource.getMessage("fail.common.select"));
			return new ModelAndView("jsonView");
		}
	}
	
}
