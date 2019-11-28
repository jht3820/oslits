package kr.opensoftlab.oslops.cmm.cmm1000.cmm1400.web;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.apache.log4j.Logger;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;
import org.tmatesoft.svn.core.SVNAuthenticationException;
import org.tmatesoft.svn.core.SVNException;
import org.tmatesoft.svn.core.io.SVNRepository;

import egovframework.com.cmm.EgovMessageSource;
import egovframework.com.cmm.service.EgovProperties;
import egovframework.rte.fdl.property.EgovPropertyService;
import kr.opensoftlab.oslops.cmm.cmm1000.cmm1400.service.Cmm1400Service;
import kr.opensoftlab.oslops.cmm.cmm4000.cmm4000.service.Cmm4000Service;
import kr.opensoftlab.oslops.com.exception.UserDefineException;
import kr.opensoftlab.oslops.com.vo.LoginVO;
import kr.opensoftlab.oslops.stm.stm2000.stm2000.service.Stm2000Service;
import kr.opensoftlab.oslops.stm.stm2000.stm2000.web.Stm2000Controller;
import kr.opensoftlab.sdf.svn.SVNConnector;
import kr.opensoftlab.sdf.svn.vo.SVNFileVO;
import kr.opensoftlab.sdf.svn.vo.SVNLogVO;
import kr.opensoftlab.sdf.util.CommonScrty;
import kr.opensoftlab.sdf.util.RequestConvertor;


/**
 * @Class Name : Cmm1400Controller.java
 * @Description : SVN 리비젼 선택 공통팝업 컨트롤러 클래스
 * @Modification Information
 * <pre>
 *   수정일      수정자          수정내용
 *  -------    --------    ---------------------------
 *  2018.08.01  공대영         최초 생성
 *  
 * </pre>
 *  @author 공대영
 *  @version 1.0
 *  @see 
 *  
 *  Copyright (C) Open Soft Lab Corp. All Rights Reserved
 */
@Controller
public class Cmm1400Controller {
	
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
	
	@Resource(name = "cmm1400Service")
	Cmm1400Service cmm1400Service;
	
	@Resource(name = "stm2000Service")
	private Stm2000Service stm2000Service;
	
	@Resource(name = "cmm4000Service")
	private Cmm4000Service cmm4000Service;
	
	/**
	 * Svn 리비전 선택 공통 팝업
	 * @param 
	 * @return 
	 * @exception Exception
	 */
	@RequestMapping(value="/cmm/cmm1000/cmm1400/selectCmm1400View.do")
	public String selectCmm1400View( HttpServletRequest request, HttpServletResponse response, ModelMap model ) throws Exception {
		return "/cmm/cmm1000/cmm1400/cmm1400";
	}
	

	/**
	 * 소스코드 확인 팝업창 호출
	 * @param 
	 * @return 
	 * @exception Exception
	 */
	@RequestMapping(value="/cmm/cmm1000/cmm1400/selectCmm1401FileContentView.do")
	public String selectCmm1401FileContentView(HttpServletRequest request, HttpServletResponse response, ModelMap model) throws Exception {
		return "/cmm/cmm1000/cmm1400/cmm1401";
	}
	
	
	/**
	 * 
	 * @param 
	 * @return 
	 * @exception Exception
	 */
	@SuppressWarnings("rawtypes")
	@RequestMapping(value="/cmm/cmm1000/cmm1400/selectCmm1400SVNRepositoryList.do")
	public ModelAndView selectCmm1400SVNRepositoryList( HttpServletRequest request, HttpServletResponse response, ModelMap model ) throws Exception {
		try{
			Map<String, String> paramMap = RequestConvertor.requestParamToMapAddSelInfo(request, true);
			
			HttpSession ss = request.getSession();
			
			String licGrpId = ((LoginVO) ss.getAttribute("loginVO")).getLicGrpId();
			paramMap.put("licGrpId", licGrpId);
			
			
			List<Map> svnList = cmm1400Service.selectCmm1400SVNRepositoryList(paramMap);
			
			model.addAttribute("list", svnList); 	/** 조회 목록 List 형태로 화면에 Return 한다. */
			model.addAttribute("message", egovMessageSource.getMessage("success.common.select"));
		
			return new ModelAndView("jsonView");

		}catch(Exception ex){
			Log.error("selectCmm1400SVNRepositoryList()", ex);
	
			model.addAttribute("message", egovMessageSource.getMessage("fail.common.select"));
			throw new Exception(ex.getMessage());
		}
	}
	
	
	/**
	 * 
	 * 라스트 리비젼 조회
	 * 
	 * @param request
	 * @param response
	 * @param model
	 * @return
	 * @throws Exception
	 */
	@SuppressWarnings({ "rawtypes", "unchecked" })
	@RequestMapping(value="/cmm/cmm1000/cmm1400/selectCmm1400LastRevisionAjax.do")
	public ModelAndView selectCmm1400LastRevisionAjax(HttpServletRequest request, HttpServletResponse response, ModelMap model ) throws Exception {

		try{

			//리퀘스트에서 넘어온 파라미터를 맵으로 세팅
			Map paramMap = RequestConvertor.requestParamToMap(request,true);

			//SVN Id로 Svn정보 불러오기
			Map svnInfo = stm2000Service.selectStm2000Info(paramMap);
			
			String svnUrl=(String)svnInfo.get("svnRepUrl");
			String userId= (String)svnInfo.get("svnUsrId");
			String svnUsrPw = (String)svnInfo.get("svnUsrPw");	//암호화된 암호
			
			//값이 null
			if(svnUsrPw == null || "".equals(svnUsrPw)){
				model.addAttribute("MSG_CD", Stm2000Controller.SVN_AUTHENTICATION_EXCEPTION);
				return new ModelAndView("jsonView");
			}
			
			//globals.properties에서 salt값 가져오기
			String salt = EgovProperties.getProperty("Globals.lunaops.salt");
			
			//암호 복호화
			String userPw = CommonScrty.decryptedAria(svnUsrPw, salt);
			
			//값이 null
			if(userPw == null || "".equals(userPw)){
				model.addAttribute("MSG_CD", Stm2000Controller.SVN_AUTHENTICATION_EXCEPTION);
				return new ModelAndView("jsonView");
			}
			
			SVNConnector conn = new SVNConnector();
			SVNRepository repository = conn.svnConnect(svnUrl , userId , userPw);

			Long lastRevision = repository.getLatestRevision();

			conn.close(repository);

			model.addAttribute("lastRevision", lastRevision);

			//조회성공메시지 세팅
			model.addAttribute("message", egovMessageSource.getMessage("success.common.select"));
			model.addAttribute("MSG_CD", Stm2000Controller.SVN_OK);
			return new ModelAndView("jsonView", paramMap);
		}
		catch(UserDefineException ude) {
			// 접속실패여부 및 접속실패 메시지 세팅
			model.addAttribute("MSG_CD", Stm2000Controller.SVN_MODULE_USE_EXCEPTION);
			return new ModelAndView("jsonView");
		}
		catch(Exception ex){
			Log.error("selectCmm1400LastRevisionAjax()", ex);
			if(ex instanceof SVNAuthenticationException ){
				model.addAttribute("MSG_CD", Stm2000Controller.SVN_AUTHENTICATION_EXCEPTION);
			}else if(ex instanceof SVNException ){
				model.addAttribute("MSG_CD", Stm2000Controller.SVN_EXCEPTION);
			} else{
				model.addAttribute("MSG_CD", Stm2000Controller.SVN_EXCEPTION);
			}
			
			return new ModelAndView("jsonView");
		}
	}
	
	


	/**
	 * SVN Revision 목록조회
	 * 
	 * @desc 1. SVN Revision 목록조회을 조회한다.
	 *       2. 
	 * @param Svn1000VO
	 * @param request
	 * @param response
	 * @throws Exception
	 */
	@SuppressWarnings("rawtypes")
	@RequestMapping(value="/cmm/cmm1000/cmm1400/selectCmm1400RevisionAjaxList.do")
	public ModelAndView selectCmm1400RevisionAjaxList( HttpServletRequest request, HttpServletResponse response, ModelMap model )	throws Exception {

		try{
			// request 파라미터를 map으로 변환
			Map<String, String> paramMap = RequestConvertor.requestParamToMapAddSelInfo(request, true);
			//현재 페이지 값, 보여지는 개체 수
			
			HttpSession ss = request.getSession();
			String prjId	= (String) ss.getAttribute("selPrjId");
			String licGrpId = ((LoginVO) ss.getAttribute("loginVO")).getLicGrpId();

		
			Long startRevision =0l;
			if(paramMap.get("startRevision")!=null){
				startRevision = Long.parseLong((String) paramMap.get("startRevision"));	
			}

			Long lastRevision =0l;
			if(paramMap.get("lastRevision")!=null){
				lastRevision = Long.parseLong((String) paramMap.get("lastRevision"));	
			}
			
			//SVN Id로 Svn정보 불러오기
			Map svnInfo = stm2000Service.selectStm2000Info(paramMap);
			
			String svnUrl=(String)svnInfo.get("svnRepUrl");
			String userId= (String)svnInfo.get("svnUsrId");
			String svnUsrPw = (String)svnInfo.get("svnUsrPw");	//암호화된 암호
			
			//값이 null
			if(svnUsrPw == null || "".equals(svnUsrPw)){
				model.addAttribute("MSG_CD", Stm2000Controller.SVN_AUTHENTICATION_EXCEPTION);
				return new ModelAndView("jsonView");
			}
			
			//globals.properties에서 salt값 가져오기
			String salt = EgovProperties.getProperty("Globals.lunaops.salt");
			
			//암호 복호화
			String userPw = CommonScrty.decryptedAria(svnUsrPw, salt);
		
			//값이 null
			if(userPw == null || "".equals(userPw)){
				model.addAttribute("MSG_CD", Stm2000Controller.SVN_AUTHENTICATION_EXCEPTION);
				return new ModelAndView("jsonView");
			}
			
			SVNConnector conn = new SVNConnector();
			SVNRepository repository = conn.svnConnect(svnUrl , userId , userPw);
			
			List<SVNLogVO> list = null;
			
			if(  lastRevision > repository.getLatestRevision() ){
				list = conn.selectSVNLogList(repository,startRevision,repository.getLatestRevision() );
			}else{
				list = conn.selectSVNLogList(repository,startRevision,lastRevision);
			}

			conn.close(repository);

			model.addAttribute("list", list); 	/** 조회 목록 List 형태로 화면에 Return 한다. */

			//조회성공메시지 세팅
			model.addAttribute("message", egovMessageSource.getMessage("success.common.select"));
			model.addAttribute("MSG_CD", Stm2000Controller.SVN_OK);
			return new ModelAndView("jsonView", paramMap);

		}catch(UserDefineException ude) {
			// 접속실패여부 및 접속실패 메시지 세팅
			model.addAttribute("message", ude.getMessage());
			return new ModelAndView("jsonView");
		}catch(Exception ex){
			Log.error("selectCmm1400RevisionAjaxList()", ex);

			model.addAttribute("message", egovMessageSource.getMessage("fail.common.select"));
			throw new Exception(ex.getMessage());
		}
	}

	

	/**
	 * Revision의 반영된 파일 정보를 조회
	 * 
	 * @desc 1. Revision의 반영된 파일 정보를 조회한다.
	 *       2.
	 *         
	 * @param request
	 * @param response
	 * @param model
	 * @return
	 * @throws Exception
	 */
	@SuppressWarnings({ "rawtypes", "unchecked" })
	@RequestMapping(value="/cmm/cmm1000/cmm1400/selectCmm1400FileDirAjaxList.do")
	public ModelAndView selectCmm1400FileDirAjaxList(HttpServletRequest request, HttpServletResponse response, ModelMap model ) throws Exception {

		try{

			//리퀘스트에서 넘어온 파라미터를 맵으로 세팅
			Map paramMap = RequestConvertor.requestParamToMap(request,true);

			//로그인VO 가져오기
			HttpSession ss = request.getSession();
			
			// 사용자 ID
			LoginVO loginVO = (LoginVO) ss.getAttribute("loginVO");
			String usrId = loginVO.getUsrId();
			
			// 프로젝트 ID
			String prjId = "";

			// 프로젝트의 권한 목록
			List<Map> prjUsrAuthList = new ArrayList<Map>();

			// 팝업에서 넘어온 프로젝트 Id 있을경우 세팅
			if(paramMap.get("prjId") != null){
				prjId = (String)paramMap.get("prjId");
				
				// 넘어온 프로젝트 ID와 사용자 ID를 Map에 세팅한다.
				Map<String,String> authParamMap = new HashMap<String,String>();
				authParamMap.put("prjId", prjId);
				authParamMap.put("usrId", usrId);
				
				// 프로젝트의 권한 목록을 조회한다.
				List<Map> popupPrjAuthList = cmm4000Service.selectCmm4000UsrPrjAuthList(authParamMap);
				
				// ArrayList에 세팅
				prjUsrAuthList.addAll(popupPrjAuthList);
				
			// 넘어온 프로젝트 Id 없을경우
			}else{
				// 세션의 선택된 프로젝트 Id를 가져온다.
				prjId = (String) ss.getAttribute("selPrjId");
				
				// 세션에 있는 선택된 권한그룹 Id를 가져와 Map에 세팅한다.
				Map<String,String> authMap = new HashMap<String,String>();
				authMap.put("authGrpId", (String) ss.getAttribute("selAuthGrpId"));
				prjUsrAuthList.add(authMap);
			}
			
			// 프로젝트 Id를 paramMap에 담는다.
			paramMap.put("prjId", prjId);
			
			Long revision =0l;
			// 파라미터에 revision이 있을경우
			if(paramMap.get("revision")!=null){
				// revision을 가져온다.
				revision = Long.parseLong((String) paramMap.get("revision"));	
			}

			//SVN Id로 Svn정보 불러오기
			Map svnInfo = stm2000Service.selectStm2000Info(paramMap);
			
			// SVN 정보에서 SVN url, SVN 사용자 Id, SVN 사용자 pw를 가져온다.
			String svnUrl=(String)svnInfo.get("svnRepUrl");
			String userId= (String)svnInfo.get("svnUsrId");
			String svnUsrPw = (String)svnInfo.get("svnUsrPw");	//암호화된 암호
			
			// SVN 사용자 pw 값이 null일 경우
			if(svnUsrPw == null || "".equals(svnUsrPw)){
				model.addAttribute("MSG_CD", Stm2000Controller.SVN_AUTHENTICATION_EXCEPTION);
				return new ModelAndView("jsonView");
			}
			
			//해당 svnRepId의 허용 역할 목록
			List<Map> svnAuthGrpList = stm2000Service.selectStm2000SvnAuthGrpList(paramMap);

			// SVN 허용 역할이 있는경우에만 체크
			if(svnAuthGrpList != null && svnAuthGrpList.size() > 0){
				//역할 있는지 체크
				boolean authChk = false;
				// 프로젝트의 허용역할 목록이 있는경우
				if(prjUsrAuthList != null && prjUsrAuthList.size() > 0) {
					// SVN 하용 역할과 프로젝트의 허용역할 목록을 비교하여 권한체크				
					for(Map svnAuthGrpInfo : svnAuthGrpList){
						// SVN 허용역할
						String svnAuthGrp = (String)svnAuthGrpInfo.get("authGrpId");

						boolean prjauthChk = false;
						// 프로젝트의 허용역할 loop
						for(Map prjUsrAuthInfo : prjUsrAuthList) {

							// SVN 허용역할과 프로젝트의 허용역할 비교
							if(svnAuthGrp.equals(prjUsrAuthInfo.get("authGrpId"))){
								prjauthChk = true;
								break;
							}
						}
						// SVN 허용역할 목록에 프로젝트 허용역할이 있을 경우
						if(prjauthChk) {
							authChk = true;
							break;
						}
					}
				}

				//허용 역할이 없는경우 오류
				if(!authChk){
					model.addAttribute("MSG_CD", Stm2000Controller.SVN_AUTHENTICATION_EXCEPTION);
					model.addAttribute("errorYn", "Y");
					model.addAttribute("consoleText", "콘솔 로그 열람 권한이 없습니다.</br></br>관리자에게 문의해주시기 바랍니다.");
					return new ModelAndView("jsonView");
				}
			}
			
			//globals.properties에서 salt값 가져오기
			String salt = EgovProperties.getProperty("Globals.lunaops.salt");
			
			//암호 복호화
			String userPw = CommonScrty.decryptedAria(svnUsrPw, salt);
						
			//값이 null
			if(userPw == null || "".equals(userPw)){
				model.addAttribute("MSG_CD", Stm2000Controller.SVN_AUTHENTICATION_EXCEPTION);
				return new ModelAndView("jsonView");
			}
			
			SVNConnector conn = new SVNConnector();
			SVNRepository repository = conn.svnConnect(svnUrl , userId , userPw);

			List<SVNLogVO> list = conn.selectSVNLogList(repository,revision,revision);

			Map<String,Map> pathMap = new HashMap<String,Map>();			

			for(SVNLogVO logVO  : list ) {
				for(SVNFileVO fileVO  : logVO.getSvnFileList() ) {
					pathMap =conn.setDirectoryMap( fileVO.getPath() , fileVO.getType()  , pathMap );
				}

			}
			//라이선스 그룹에 할당된 메뉴목록 가져오기
			List<Map> baseDocList = null;

			baseDocList  = conn.setDirectoryList( pathMap);

			conn.close(repository);



			model.addAttribute("baseDocList", baseDocList);
			model.addAttribute("MSG_CD", Stm2000Controller.SVN_OK);
			//조회성공메시지 세팅
			model.addAttribute("message", egovMessageSource.getMessage("success.common.select"));

			return new ModelAndView("jsonView", paramMap);
		}
		catch(Exception ex){
			Log.error("selectCmm1400FileDirAjaxList()", ex);
			
			if(ex instanceof SVNAuthenticationException ){
				model.addAttribute("MSG_CD", Stm2000Controller.SVN_AUTHENTICATION_EXCEPTION);
			}else if(ex instanceof SVNException ){
				model.addAttribute("MSG_CD", Stm2000Controller.SVN_EXCEPTION);
			} else{
				model.addAttribute("MSG_CD", Stm2000Controller.SVN_EXCEPTION);
			}
			//조회실패 메시지 세팅
			model.addAttribute("message", egovMessageSource.getMessage("fail.common.select"));
			return new ModelAndView("jsonView");
		}
				
	}
	

	/**
	 * 
	 * 대상 Revision 대상 파일의 내용 조회
	 * 
	 * @desc 1. 대상 Revision 대상 파일의 내용 조회한다.
	 *       2.
	 *  
	 * @param request
	 * @param response
	 * @param model
	 * @return
	 * @throws Exception
	 */
	@SuppressWarnings({ "rawtypes", "unchecked" })
	@RequestMapping(value="/cmm/cmm1000/cmm1400/selectCmm1400FileContentAjax.do")
	public ModelAndView selectCmm1400FileContentAjax(HttpServletRequest request, HttpServletResponse response, ModelMap model ) throws Exception {

		try{

			//리퀘스트에서 넘어온 파라미터를 맵으로 세팅
			Map paramMap = RequestConvertor.requestParamToMap(request,true);

			//로그인VO 가져오기
			HttpSession ss = request.getSession();
			paramMap.put("prjId", ss.getAttribute("selPrjId"));
			paramMap.put("authGrpId", ss.getAttribute("selAuthGrpId"));
			Long revision =0l;
			
			if(paramMap.get("revision")!=null){
				revision = Long.parseLong((String) paramMap.get("revision"));	
			}

			String path = (String) paramMap.get("path");

			//SVN Id로 Svn정보 불러오기
			Map svnInfo = stm2000Service.selectStm2000Info(paramMap);
			
			String svnUrl=(String)svnInfo.get("svnRepUrl");
			String userId= (String)svnInfo.get("svnUsrId");
			String svnUsrPw = (String)svnInfo.get("svnUsrPw");	//암호화된 암호
			
			//값이 null
			if(svnUsrPw == null || "".equals(svnUsrPw)){
				model.addAttribute("MSG_CD", Stm2000Controller.SVN_AUTHENTICATION_EXCEPTION);
				return new ModelAndView("jsonView");
			}
			
			//globals.properties에서 salt값 가져오기
			String salt = EgovProperties.getProperty("Globals.lunaops.salt");
			
			//암호 복호화
			String userPw = CommonScrty.decryptedAria(svnUsrPw, salt);
			
			//값이 null
			if(userPw == null || "".equals(userPw)){
				model.addAttribute("MSG_CD", Stm2000Controller.SVN_AUTHENTICATION_EXCEPTION);
				return new ModelAndView("jsonView");
			}
			
			System.out.println("#########");
			System.out.println(revision);
			SVNConnector conn = new SVNConnector();
			SVNRepository repository = conn.svnConnect(svnUrl , userId , userPw);

			List<SVNLogVO> list = conn.selectSVNLogList(repository,revision,revision);

			String content = conn.getFileContent(repository, path, revision);

			conn.close(repository);

			model.addAttribute("content", content);

			//조회성공메시지 세팅
			model.addAttribute("message", egovMessageSource.getMessage("success.common.select"));

			return new ModelAndView("jsonView", paramMap);
		}
		catch(Exception ex){
			Log.error("selectCmm1400FileContentAjax()", ex);

			//조회실패 메시지 세팅
			model.addAttribute("message", egovMessageSource.getMessage("fail.common.select"));
			return new ModelAndView("jsonView");
		}
	}
	
	


}
