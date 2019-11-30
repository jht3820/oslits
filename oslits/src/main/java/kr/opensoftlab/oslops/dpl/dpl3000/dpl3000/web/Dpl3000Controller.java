package kr.opensoftlab.oslops.dpl.dpl3000.dpl3000.web;

import java.util.Date;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;

import egovframework.com.cmm.EgovMessageSource;
import egovframework.com.cmm.service.EgovProperties;
import egovframework.rte.fdl.property.EgovPropertyService;
import kr.opensoftlab.oslops.com.exception.UserDefineException;
import kr.opensoftlab.oslops.com.vo.LoginVO;
import kr.opensoftlab.oslops.dpl.dpl1000.dpl1000.service.Dpl1000Service;
import kr.opensoftlab.oslops.dpl.dpl1000.dpl1100.service.Dpl1100Service;
import kr.opensoftlab.oslops.stm.stm3000.stm3000.service.Stm3000Service;
import kr.opensoftlab.oslops.stm.stm3000.stm3100.service.Stm3100Service;
import kr.opensoftlab.sdf.jenkins.JenkinsClient;
import kr.opensoftlab.sdf.jenkins.service.BuildService;
import kr.opensoftlab.sdf.jenkins.vo.BuildVO;
import kr.opensoftlab.sdf.util.CommonScrty;
import kr.opensoftlab.sdf.util.RequestConvertor;

/**
 * @Class Name : Dpl3000Controller.java
 * @Description : Dpl3000Controller Controller class
 * @Modification Information
 *
 * @author 진주영
 * @since 2019.03.11
 * @version 1.0
 * @see
 *  
 *  --------------------------------------
 *  수정일			수정자			수정내용
 *  --------------------------------------
 *  
 *  
 *  --------------------------------------
 *  
 *  Copyright (C)  All right reserved.
 */

@Controller
public class Dpl3000Controller {
	
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
    
    /** Stm3000Service DI */
	@Resource(name = "stm3000Service")
	private Stm3000Service stm3000Service;
	
	/** Stm3100Service DI */
	@Resource(name = "stm3100Service")
	private Stm3100Service stm3100Service;
	
	/** JenkinsClient DI */
	@Resource(name = "jenkinsClient")
	private JenkinsClient jenkinsClient;
	
	/** BuildService DI */
	@Resource(name = "buildService")
	private BuildService buildService;
	
	
	@Value("${Globals.fileStorePath}")
	private String tempPath;
    
    
    /**
	 * Dpl3000  배포 계획 실행 화면
	 * @param 
	 * @return 
	 * @exception Exception
	 */
	@RequestMapping(value="/dpl/dpl3000/dpl3000/selectDpl3000View.do")
    public String selectDpl3000View(HttpServletRequest request, HttpServletResponse response, ModelMap model ) throws Exception {
    	return "/dpl/dpl3000/dpl3000/dpl3000";
    }
	
	
	/**
	 * JOB 수동 배포
	 * 
	 * @desc 1. Job의 빌드을 요청한다.
	 *        
	 * @param request
	 * @param response
	 * @param model
	 * @return
	 * @throws Exception
	 */
	@SuppressWarnings({ "rawtypes"})
	@RequestMapping(value="/dpl/dpl3000/dpl3000/selectDpl3000JobBuildAjax.do")
	public ModelAndView selectDpl3000JobBuildAjax(HttpServletRequest request, HttpServletResponse response, ModelMap model ) throws Exception {

		try{
			//리퀘스트에서 넘어온 파라미터를 맵으로 세팅
    		Map<String, String> paramMap = RequestConvertor.requestParamToMapAddSelInfo(request, true);
    		
    		//세션
    		HttpSession ss = request.getSession();
    		paramMap.put("prjId", (String) ss.getAttribute("selPrjId"));
    		
    		//라이선스 그룹 ID넣기
    		paramMap.remove("licGrpId");
    		LoginVO loginVO = (LoginVO) ss.getAttribute("loginVO");
    		paramMap.put("licGrpId", loginVO.getLicGrpId());
    		
    		//jenkins 정보
    		Map jenMap = stm3000Service.selectStm3000JenkinsInfo(paramMap);
    		
    		//배포계획 정보
    		Map dplMap = dpl1000Service.selectDpl1000DeployVerInfo(paramMap);
    		
    		//배포자 Id
			String dplUsrId = (String) dplMap.get("dplUsrId");
			
    		//배포자가 아닌경우 빌드 오류
    		if(!loginVO.getUsrId().equals(dplUsrId)){
    			//조회실패 메시지 세팅
    			model.addAttribute("errorYn", "Y");
				model.addAttribute("message", "배포자만 실행이 가능합니다.");
				return new ModelAndView("jsonView");
    		}
    		
    		//배포 방법
    		String dplTypeCd = (String) dplMap.get("dplTypeCd");
    		
    		//배포 상태
    		String dplStsCd = (String)dplMap.get("dplStsCd");
    		
    		//배포 실패 후 처리
    		String dplAutoAfterCd = (String)dplMap.get("dplAutoAfterCd");
    		
    		//배포방법이 자동인경우 오류
    		if("01".equals(dplTypeCd)){
    			//배포 상태 실패, 실패 후 처리 수동 아닌경우 오류
    			if(!"03".equals(dplStsCd) && !"01".equals(dplAutoAfterCd)){
    				//조회실패 메시지 세팅
    				model.addAttribute("errorYn", "Y");
    				model.addAttribute("message", "수동 실행이 불가능한 자동 배포 계획입니다.");
    				return new ModelAndView("jsonView");
    			}
    		}
    		
    		//배포상태가 성공인경우 조건 체크
    		if("02".equals(dplStsCd)){
    			//조회실패 메시지 세팅
    			model.addAttribute("errorYn", "Y");
				model.addAttribute("message", "배포 상태가 성공인 배포 계획의 JOB은 수동 실행이 불가능합니다.");
				return new ModelAndView("jsonView");
    		}
    		
    		//jenkins 정보
    		String userId= (String)jenMap.get("jenUsrId");
			String tokenId= (String)jenMap.get("jenUsrTok");
			String jenUrl= (String)jenMap.get("jenUrl");
			
			//JOB 정보
			//String jobId = (String)paramMap.get("jobId");
			//String jobRestoreId = (String)paramMap.get("jobRestoreId");
			String jobToken = (String)paramMap.get("jobTok");
			
			//빈값인경우 오류
			if(tokenId == null || "".equals(tokenId) || jobToken == null || "".equals(jobToken)){
				//조회실패 메시지 세팅
				model.addAttribute("errorYn", "Y");
				model.addAttribute("message", egovMessageSource.getMessage("fail.deploy.build"));
				model.addAttribute("MSG_CD", "토큰 값 오류");
				return new ModelAndView("jsonView");
			}
			
			//Map에 데이터 세팅
			paramMap.put("userId", userId);
			paramMap.put("tokenId", tokenId);
			paramMap.put("jenUrl", jenUrl);
			paramMap.put("dplId", (String)dplMap.get("dplId"));
			paramMap.put("dplTypeCd", (String)dplMap.get("dplTypeCd"));
			paramMap.put("dplAutoAfterCd", (String)dplMap.get("dplAutoAfterCd"));
			paramMap.put("dplRestoreCd", (String)dplMap.get("dplRestoreCd"));
			paramMap.put("regUsrId", loginVO.getUsrId());
			paramMap.put("regUsrIp", request.getRemoteAddr());
			
			//buildVo 세팅
			BuildVO buildVo = buildService.insertBuildVoSetting(paramMap);
			
			buildVo.setBldResult("START");
			buildVo.setBldResultMsg("빌드 준비 중");
			buildVo.setEstimatedDuration(10000);
			
			//빌드 정보 쌓기
			dpl1000Service.insertDpl1400DeployJobBuildLogInfo(buildVo);
			
			//JOB 빌드
			model = buildService.insertJobBuildAction(buildVo,model);
			
			return new ModelAndView("jsonView");
			
		}catch(Exception ex){
			Log.error("selectDpl3000JobBuildAjax()", ex);
			
			//조회실패 메시지 세팅
			model.addAttribute("errorYn", "Y");
			model.addAttribute("message", egovMessageSource.getMessage("fail.common.select"));
			return new ModelAndView("jsonView");
		}
	}
	
	/**
	 * JOB CONSOLE LOG 조회
	 * 
	 * @param request
	 * @param response
	 * @param model
	 * @return
	 * @throws Exception
	 */
	@SuppressWarnings({ "rawtypes", "unchecked" })
	@RequestMapping(value="/dpl/dpl3000/dpl3000/selectDpl3000JobConsoleLogAjax.do")
	public ModelAndView selectDpl3000JobConsoleLogAjax(HttpServletRequest request, HttpServletResponse response, ModelMap model ) throws Exception {
		
		try{
			//리퀘스트에서 넘어온 파라미터를 맵으로 세팅
			Map<String, String> paramMap = RequestConvertor.requestParamToMapAddSelInfo(request, true);
			
			//세션
			HttpSession ss = request.getSession();
			paramMap.put("prjId", (String) ss.getAttribute("selPrjId"));
			
			//라이선스 그룹 ID넣기
			paramMap.remove("licGrpId");
			LoginVO loginVO = (LoginVO) ss.getAttribute("loginVO");
			paramMap.put("licGrpId", loginVO.getLicGrpId());
			
			//jenkins 정보
			String userId= (String)paramMap.get("jenUsrId");
			String tokenId= (String)paramMap.get("jenUsrTok");
			String jenUrl= (String)paramMap.get("jenUrl");
			
			//JOB 정보
			String jobId = (String)paramMap.get("jobId");
			
			//빈값인경우 오류
			if(tokenId == null || "".equals(tokenId)){
				//조회실패 메시지 세팅
				model.addAttribute("message", egovMessageSource.getMessage("fail.deploy.build"));
				model.addAttribute("MSG_CD", "토큰 값 오류");
				return new ModelAndView("jsonView");
			}
			
			//globals.properties에서 salt값 가져오기
			String salt = EgovProperties.getProperty("Globals.lunaops.salt");
			
			//값 복호화
			String deTokenId = CommonScrty.decryptedAria(tokenId, salt);
			
			jenkinsClient.setUser(userId);
			jenkinsClient.setPassword(deTokenId);
			
			//빌드 번호
			String bldNum = (String)paramMap.get("bldNum");
			
			//콘솔 로그 조회 유무 
			String requestConsole = (String)paramMap.get("requestConsole");
			
			String consoleText = "";
			try{
				//빌드 정보 불러오기
				String buildUrl = jenUrl+"/job/"+jobId+"/"+bldNum+"/api/json";
				String buildContent = jenkinsClient.excuteHttpClientJenkins(buildUrl);
				Map buildMap = jenkinsClient.getJenkinsParser(buildContent);
				
				//빌드 정보
				model.addAttribute("buildMap", buildMap);
				
				/** 빌드 결과가 있는 경우 로컬 결과 값 불러오기 **/
				//빌드 중
				boolean building = (boolean) buildMap.get("building");
				
				//빌드 결과
				String bldResult = (String) buildMap.get("result");
				
				//빌드 중이 아닌 경우
				if(!building){
					if(bldResult != null && !"null".equals(bldResult)) {
						//결과 값 존재하는 경우 로컬 결과 값 불러오기
						Map localBldInfo = dpl1000Service.selectDpl1400DplJobBuildInfo(paramMap);
						
						//결과 값
						String localBldResult = (String)localBldInfo.get("bldResult");
						
						//START ,PROGRESS 아닌 경우
						if(localBldResult != null && !"START".equals(localBldResult) && !"PROGRESS".equals(localBldResult)){
							//로컬 DB 값 전달
							model.addAttribute("localBldInfo", localBldInfo);
						}
					}
				}
				
				//콘솔 로그 조회인지 체크
				if(requestConsole != null && !"".equals(requestConsole) && "Y".equals(requestConsole)){
					//콘솔 조회 권한 체크
		 			List<Map> dplRunAuthGrp = stm3100Service.selectJen1300JenkinsJobAuthGrpNormalList(paramMap);
		 			
		 			//체크 변수
	 				boolean authGrpChk = false;
	 				
		 			//허용 권한그룹이 존재하는 경우
		 			if(dplRunAuthGrp != null){
		 				//배포 정보
		 				Map dpl1000DplInfo = dpl1000Service.selectDpl1000DeployVerInfo(paramMap);
		 				
		 				//사용자 Id
		 				String usrId = (String)loginVO.getUsrId();
		 				
		 				//배포자 Id
		 				String dplUsrId = (String) dpl1000DplInfo.get("dplUsrId");
		 				
		 				//배포자인경우 권한 체크 X
		 				if(usrId.equals(dplUsrId)){
		 					authGrpChk = true;
		 				}
		 				
		 				//선택 권한
		 				String selAuthGrpId = (String)ss.getAttribute("selAuthGrpId");
		 				
		 				//허용 권한 목록 없는 경우 전체 공개
		 				if(dplRunAuthGrp.size() == 0){
		 					authGrpChk = true;
		 				}else{
			 				for(Map authGrp : dplRunAuthGrp){
			 					String authGrpId = (String) authGrp.get("authGrpId");
			 					
			 					//권한 같은지 체크
			 					if(selAuthGrpId.equals(authGrpId)){
			 						authGrpChk = true;
			 						break;
			 					}
			 				}
		 				}
		 			}
		 			
		 			//권한 없는 경우 error
	 				if(!authGrpChk){
	 					consoleText = "허용 권한이 없습니다.";
	 				}else{
	 					//콘솔로그 가져오기
						String consoleurl =  jenUrl+"/job/"+jobId+"/"+bldNum+"/consoleText";
						consoleText = jenkinsClient.excuteHttpClientJenkins(consoleurl);
	 				}
				}
	 			
				//콘솔 정보
				model.addAttribute("consoleText", consoleText);
			}catch(UserDefineException uex){
				//최초 빌드 정보 없는경우 404오류
				model.addAttribute("message", egovMessageSource.getMessage("fail.common.select"));
				model.addAttribute("errorYn", "Y");
				return new ModelAndView("jsonView");
			}
			
			model.addAttribute("errorYn", "N");
			return new ModelAndView("jsonView");
			
		}catch(Exception ex){
			Log.error("selectDpl3000JobConsoleLogAjax()", ex);
			
			//조회실패 메시지 세팅
			model.addAttribute("message", egovMessageSource.getMessage("fail.common.select"));
			model.addAttribute("errorYn", "Y");
			return new ModelAndView("jsonView");
		}
	}
	
	
    /**
 	 * Dpl3000 배포 상태 변경
 	 * @param 
 	 * @return 
 	 * @exception Exception
 	 */
 	@RequestMapping(value="/dpl/dpl3000/dpl3000/updateDpl3000DplStsCdView.do")
    public ModelAndView updateDpl3000DplStsCdView(HttpServletRequest request, HttpServletResponse response, ModelMap model ) throws Exception {
		try{
			
    		//리퀘스트에서 넘어온 파라미터를 맵으로 세팅
    		Map<String, String> paramMap = RequestConvertor.requestParamToMapAddSelInfo(request, true);
    		
    		//session
    		HttpSession ss = request.getSession();
    		
			paramMap.put("prjId", (String)ss.getAttribute("selPrjId"));
			
			//배포 상태 변경
			dpl1000Service.updateDpl1000DplStsCdInfo(paramMap);
			
			//조회성공메시지 세팅
    		model.addAttribute("errorYn", "N");
			model.addAttribute("message", egovMessageSource.getMessage("success.common.update"));
			return new ModelAndView("jsonView", model);
		}
		catch(Exception ex){
			Log.error("updateDpl3000DplStsCdView()", ex);
			
			//조회실패 메시지 세팅
			model.addAttribute("errorYn", "Y");
			model.addAttribute("message", egovMessageSource.getMessage("fail.common.update"));
			return new ModelAndView("jsonView", model);
		}
	}
 	
 	/**
 	 * Dpl3000 선택 JOB 콘솔로그 조회
 	 * @param 
 	 * @return 
 	 * @exception Exception
 	 */
 	@SuppressWarnings("rawtypes")
	@RequestMapping(value="/dpl/dpl3000/dpl3000/selectDpl3000DplConsoleLogView.do")
 	public ModelAndView selectDpl3000DplConsoleLogView(HttpServletRequest request, HttpServletResponse response, ModelMap model ) throws Exception {
 		try{
 			
 			//리퀘스트에서 넘어온 파라미터를 맵으로 세팅
 			Map<String, String> paramMap = RequestConvertor.requestParamToMapAddSelInfo(request, true);
 			
 			//session
 			HttpSession ss = request.getSession();
 			paramMap.put("prjId", (String)ss.getAttribute("selPrjId"));
 			
 			//JOB 콘솔 로그 불러오기
 			Map localJobInfo = dpl1000Service.selectDpl1400DplJobBuildInfo(paramMap);
 			model.addAttribute("localJobInfo", localJobInfo);
 			
 			//조회성공메시지 세팅
 			model.addAttribute("errorYn", "N");
 			model.addAttribute("message", egovMessageSource.getMessage("success.common.update"));
 			return new ModelAndView("jsonView", model);
 		}
 		catch(Exception ex){
 			Log.error("updateDpl3000DplStsCdView()", ex);
 			
 			//조회실패 메시지 세팅
 			model.addAttribute("errorYn", "Y");
 			model.addAttribute("message", egovMessageSource.getMessage("fail.common.update"));
 			return new ModelAndView("jsonView", model);
 		}
 	}
 }
