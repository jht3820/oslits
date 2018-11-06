package kr.opensoftlab.oslits.stm.stm2000.stm2000.web;

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
import kr.opensoftlab.oslits.stm.stm2000.stm2000.service.Stm2000Service;
import kr.opensoftlab.oslits.stm.stm2000.stm2000.vo.Stm2000VO;
import kr.opensoftlab.sdf.svn.SVNConnector;
import kr.opensoftlab.sdf.util.CommonScrty;
import kr.opensoftlab.sdf.util.OslAgileConstant;
import kr.opensoftlab.sdf.util.PagingUtil;
import kr.opensoftlab.sdf.util.ReqHistoryMngUtil;
import kr.opensoftlab.sdf.util.RequestConvertor;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.multipart.MultipartHttpServletRequest;
import org.springframework.web.servlet.ModelAndView;
import org.tmatesoft.svn.core.SVNAuthenticationException;
import org.tmatesoft.svn.core.SVNException;
import org.tmatesoft.svn.core.io.SVNRepository;

import egovframework.com.cmm.EgovMessageSource;
import egovframework.com.cmm.service.EgovFileMngUtil;
import egovframework.com.cmm.service.EgovProperties;
import egovframework.com.cmm.service.FileVO;
import egovframework.rte.fdl.idgnr.EgovIdGnrService;
import egovframework.rte.fdl.property.EgovPropertyService;
import egovframework.rte.ptl.mvc.tags.ui.pagination.PaginationInfo;

/**
 * @Class Name : Stm2000Controller.java
 * @Description : Stm2000Controller Controller class
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
public class Stm2000Controller {

	/** 로그4j 로거 로딩 */
	private static final Logger Log = Logger.getLogger(Stm2000Controller.class);

	/*
	 * testConnect 실행시 
	 * 1.svn 접속 url의 문제가 있을시에 발생 
	 * */
	public static final String SVN_EXCEPTION = "SVN_EXCEPTION"; 
	/*
	 * testConnect 실행시 
	 * 1.svn 사용자의 아이디 혹은 패스워드가 잘못되있을 경우 발생 
	 * */	
	public static final String SVN_AUTHENTICATION_EXCEPTION = "SVN_AUTHENTICATION_EXCEPTION";

	/*
	 * testConnect 실행시  Svn 사용가능할 때 
	 * */
	public static final String SVN_OK = "SVN_OK";

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
	
	@Resource(name = "stm2000Service")
	private Stm2000Service stm2000Service;
	
	

	/**
	 * SVN Repository 설정화면으로 이동
	 * @param request
	 * @param response
	 * @param model
	 * @return
	 * @throws Exception
	 */
	@RequestMapping(value="/stm/stm2000/stm2000/selectStm2000RepositoryView.do")
	public String selectSvn1000RepositoryView(HttpServletRequest request, HttpServletResponse response, ModelMap model ) throws Exception {
		return "/stm/stm2000/stm2000/stm2000";
	}

	

	/**
	 * SVN Repository 설정 조회
	 * 
	 * @param 
	 * @return 
	 * @exception Exception
	 */
	@RequestMapping(value="/stm/stm2000/stm2000/selectStm2000RepositoryListAjaxView.do")
	public ModelAndView selectStm2000RepositoryListAjaxView(@ModelAttribute("stm2000VO") Stm2000VO stm2000VO, HttpServletRequest request, HttpServletResponse response, ModelMap model ) throws Exception {

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
			stm2000VO.setPageIndex(_pageNo);
			stm2000VO.setPageSize(_pageSize);
			stm2000VO.setPageUnit(_pageSize);
			
			
			PaginationInfo paginationInfo = PagingUtil.getPaginationInfo(stm2000VO);  /** paging - 신규방식 */

			List<Stm2000VO> stm2000List = null;

			HttpSession ss = request.getSession();
			LoginVO loginVO = (LoginVO) ss.getAttribute("loginVO");

			stm2000VO.setLoginUsrId(loginVO.getUsrId());
			stm2000VO.setLicGrpId(loginVO.getLicGrpId());
			stm2000VO.setPrjId((String) ss.getAttribute("selPrjId"));

			/**
			 * 목록 조회
			 */
			int totCnt = 0;
			stm2000List =  stm2000Service.selectStm2000RepositoryList(stm2000VO);

			
			/** 총 데이터의 건수 를 가져온다. */
			totCnt =stm2000Service.selectStm2000RepositoryListCnt(stm2000VO);
			paginationInfo.setTotalRecordCount(totCnt);
			
			model.addAttribute("list", stm2000List);
			
			//페이지 정보 보내기
			Map<String, Integer> pageMap = new HashMap<String, Integer>();
			pageMap.put("pageNo",stm2000VO.getPageIndex());
			pageMap.put("listCount", stm2000List.size());
			pageMap.put("totalPages", paginationInfo.getTotalPageCount());
			pageMap.put("totalElements", totCnt);
			pageMap.put("pageSize", _pageSize);
			
			model.addAttribute("page", pageMap);
			
			return new ModelAndView("jsonView");
		}
		catch(Exception ex){
			Log.error("selectStm2000RepositoryListAjaxView()", ex);
			throw new Exception(ex.getMessage());
		}
	}
	
	
	
	
	/**
	 * REPOSITORY 설정 등록/수정 팝업 화면으로 이동
	 * 
	 * @param request
	 * @param response
	 * @param model
	 * @return
	 * @throws Exception
	 */
	@RequestMapping(value="/stm/stm2000/stm2000/selectStm2001RepositoryDetailView.do")
	public String selectStm2001RepositoryDetailView(HttpServletRequest request, HttpServletResponse response, ModelMap model ) throws Exception {
		return "/stm/stm2000/stm2000/stm2001";
	}
	
	
	/**
	 * 
	 * 상세 SVN Repository 설정 조회
	 * 
	 * @param request
	 * @param response
	 * @param model
	 * @return
	 * @throws Exception
	 */
	@RequestMapping(value="/stm/stm2000/stm2000/selectStm2000InfoAjax.do")
	public ModelAndView selectStm2000Info(HttpServletRequest request, HttpServletResponse response, ModelMap model ) throws Exception {

		try{
			
			// request 파라미터를 map으로 변환
			Map<String, String> paramMap = RequestConvertor.requestParamToMapAddSelInfo(request, true);
			HttpSession ss = request.getSession();
			
			LoginVO loginVO = (LoginVO) ss.getAttribute("loginVO");

			paramMap.put("licGrpId", loginVO.getLicGrpId());
	
			// 요구사항 일괄저장
			Map repMap = stm2000Service.selectStm2000Info(paramMap);
			
			model.addAttribute("repInfo", repMap);
	
			//등록 성공 메시지 세팅
			model.addAttribute("message", egovMessageSource.getMessage("success.common.select"));

			return new ModelAndView("jsonView");
		}
		catch(Exception ex){
			Log.error("selectStm2000Info()", ex);


			model.addAttribute("message", egovMessageSource.getMessage("fail.common.select"));
			return new ModelAndView("jsonView");
		}
	}
	/**
	 * 
	 * 상세 SVN Repository 등록/수정
	 * 
	 * @param request
	 * @param response
	 * @param model
	 * @return
	 * @throws Exception
	 */
	@SuppressWarnings("rawtypes")
	@RequestMapping(value="/stm/stm2000/stm2000/saveSvn2000InfoAjax.do")
	public ModelAndView saveSvn2000InfoAjax(HttpServletRequest request, HttpServletResponse response, ModelMap model ) throws Exception {

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
			
			//기존 비밀번호
			String nowPw = (String)paramMap.get("nowPw");
			
			//파라미터 비밀번호
			String modifyPw = (String)paramMap.get("svnUsrPw");
			
			//암호화된 비밀번호
			String newEnPw = "";
			
			//수정인경우 비밀번호 변경되었는지 체크
			if("update".equals(type)){
				//기존 비밀번호와 현재 입력된 비밀번호가 다른경우
				if(!nowPw.equals(modifyPw)){
					//비밀번호 암호화
					newEnPw = CommonScrty.encryptedAria(modifyPw, salt);
				}else{
					newEnPw = modifyPw;
				}
			}
			//등록인경우 비밀번호 암호화
			else{
				newEnPw = CommonScrty.encryptedAria(modifyPw, salt);
			}
			
			//입력된 비밀번호 제거
			paramMap.remove("svnUsrPw");
			
			//암호화된 비밀번호 입력
			paramMap.put("svnUsrPw", newEnPw);
			
			//저장
			Object insertKey = stm2000Service.saveStm2000Info(paramMap);
	
			//등록 성공 메시지 세팅
			model.addAttribute("message", egovMessageSource.getMessage("success.common.save"));

			return new ModelAndView("jsonView");
		}
		catch(Exception ex){
			Log.error("saveSvn2000InfoAjax()", ex);

			//조회실패 메시지 세팅 및 저장 성공여부 세팅
			model.addAttribute("saveYN", "N");
			model.addAttribute("message", egovMessageSource.getMessage("fail.common.save"));
			return new ModelAndView("jsonView");
		}
	}
	
	/**
	 * SVN Repository 상세 삭제
	 * 
	 * @param request
	 * @param response
	 * @param model
	 * @return
	 * @throws Exception
	 */
	@SuppressWarnings("rawtypes")
	@RequestMapping(value="/stm/stm2000/stm2000/deleteStm2000InfoAjax.do")
	public ModelAndView deleteStm2000InfoAjax(HttpServletRequest request, HttpServletResponse response, ModelMap model ) throws Exception {

		try{
			
			
			// request 파라미터를 map으로 변환
			Map<String, String> paramMap = RequestConvertor.requestParamToMapAddSelInfo(request, true);
			HttpSession ss = request.getSession();
			
			int useCount = stm2000Service.selectStm2000UseCountInfo(paramMap);
			if(useCount==0){
				stm2000Service.deleteStm2000Info(paramMap);
				//등록 성공 메시지 세팅
				model.addAttribute("message", egovMessageSource.getMessage("success.common.delete"));
			}else{
				model.addAttribute("saveYN", "N");
				model.addAttribute("message", egovMessageSource.getMessage("fail.common.existInfo"));
			}
			
			return new ModelAndView("jsonView");
		}
		catch(Exception ex){
			Log.error("deleteStm2000InfoAjax()", ex);

			//조회실패 메시지 세팅 및 저장 성공여부 세팅
			model.addAttribute("saveYN", "N");
			model.addAttribute("message", egovMessageSource.getMessage("fail.common.save"));
			return new ModelAndView("jsonView");
		}
	}
	
	
	@SuppressWarnings("rawtypes")
	@RequestMapping(value="/stm/stm2000/stm2000/selectStm2000ConfirmConnect.do")
	public ModelAndView selectStm2000ConfirmConnect(HttpServletRequest request, HttpServletResponse response, ModelMap model ) throws Exception {

		try{
			
			// request 파라미터를 map으로 변환
			Map<String, String> paramMap = RequestConvertor.requestParamToMapAddSelInfo(request, true);
			HttpSession ss = request.getSession();
			
			Map repMap = stm2000Service.selectStm2000Info(paramMap);
			
			//globals.properties에서 salt값 가져오기
			String salt = EgovProperties.getProperty("Globals.oslits.salt");
			
			//비밀번호
			String svnUsrPw = (String)repMap.get("svnUsrPw");
			
			//비밀번호 복호화
			String newDePw = CommonScrty.decryptedAria(svnUsrPw, salt);
			
			//비밀번호 빈값인경우 오류
			if(newDePw == null || "".equals(newDePw)){
				model.addAttribute("MSG_CD", SVN_AUTHENTICATION_EXCEPTION);
				return new ModelAndView("jsonView");
			}
			
			SVNConnector connect = new SVNConnector();	
			SVNRepository repository=connect.svnConnect((String)repMap.get("svnRepUrl"), (String)repMap.get("svnUsrId"), newDePw);
			
			repository.testConnection();
			
			model.addAttribute("MSG_CD", SVN_OK);
			
			connect.close(repository);
			
			return new ModelAndView("jsonView");
		}
		catch(Exception ex){
			Log.error("selectStm2000ConfirmConnect()", ex);
			if(ex instanceof SVNAuthenticationException ){
				model.addAttribute("MSG_CD", SVN_AUTHENTICATION_EXCEPTION);
			}else if(ex instanceof SVNException ){
				model.addAttribute("MSG_CD", SVN_EXCEPTION);
			} else{
				model.addAttribute("saveYN", "N");
				model.addAttribute("message", egovMessageSource.getMessage("fail.common.save"));
			}
			//조회실패 메시지 세팅 및 저장 성공여부 세팅
			return new ModelAndView("jsonView");
		}
	}

	/**
	 * SVN Repository 허용 역할 목록 불러오기 Ajax
	 * @param 
	 * @return 
	 * @exception Exception
	 */
	@SuppressWarnings({ "rawtypes", "unchecked" })
	@RequestMapping(value="/stm/stm2000/stm2000/selectStm2000SvnAuthGrpList.do")
	public ModelAndView selectStm2000SvnAuthGrpList(HttpServletRequest request, HttpServletResponse response, ModelMap model ) throws Exception {
		
		try{
			// request 파라미터를 map으로 변환
			Map<String, String> paramMap = RequestConvertor.requestParamToMapAddSelInfo(request, true);
			
			//역할그룹 목록
			List<Map> svnAuthGrpList = stm2000Service.selectStm2000SvnAuthGrpList(paramMap);
			model.addAttribute("svnAuthGrpList", svnAuthGrpList);
			
			//성공 메시지 세팅
			model.addAttribute("errorYN", "N");
			model.addAttribute("message", egovMessageSource.getMessage("success.common.select"));
			
			return new ModelAndView("jsonView");
		}
		catch(Exception ex){
			Log.error("selectStm2000SvnAuthGrpList()", ex);
			
			//조회실패 메시지 세팅 및 저장 성공여부 세팅
			model.addAttribute("errorYN", "Y");
			model.addAttribute("message", egovMessageSource.getMessage("fail.common.select"));
			return new ModelAndView("jsonView");
		}
	}
	
	/**
	 *  SVN Repository 허용 역할 저장
	 * @param request
	 * @param model
	 * @return
	 * @throws Exception
	 */
	@SuppressWarnings({ "rawtypes"})
	@RequestMapping(value="/stm/stm2000/stm2000/insertStm2000SvnAuthGrpList.do")
	public ModelAndView insertStm2000SvnAuthGrpList(HttpServletRequest request, HttpServletResponse response, ModelMap model )	throws Exception {
		try{
			// request 파라미터를 map으로 변환
			Map paramMap = RequestConvertor.requestParamToMapAddSelInfo(request, true);
			
			//선택 역할그룹 갯수
			int selAuthCnt = Integer.parseInt((String)paramMap.get("selAuthCnt"));
			
			//실패 역할그룹 갯수 리턴
			int addFailAuthCnt = stm2000Service.insertStm2000SvnAuthGrpList(paramMap);
			
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
			Log.error("insertStm2000SvnAuthGrpList()", e);
			model.addAttribute("errorYn", "Y");
			model.addAttribute("message", egovMessageSource.getMessage("fail.common.insert"));
			return new ModelAndView("jsonView");
		}
	}
	

	/**
	 *  SVN Repository 역할그룹 제한 제거
	 * @param request
	 * @param model
	 * @return
	 * @throws Exception
	 */
	@SuppressWarnings({ "rawtypes" })
	@RequestMapping(value="/stm/stm2000/stm2000/deleteStm2000SvnAuthGrpList.do")
	public ModelAndView deleteStm2000SvnAuthGrpList(HttpServletRequest request, HttpServletResponse response, ModelMap model )	throws Exception {
		try{
			// request 파라미터를 map으로 변환
			Map paramMap = RequestConvertor.requestParamToMapAddSelInfo(request, true);
			
			//역할그룹 제거
			stm2000Service.deleteStm2000SvnAuthGrpList(paramMap);
			
			//조회 성공 메시지 세팅
			model.addAttribute("errorYn", "N");
			model.addAttribute("message", egovMessageSource.getMessage("success.common.delete"));
			
			return new ModelAndView("jsonView");

		}catch(Exception e){
			Log.error("deleteStm2000SvnAuthGrpList()", e);
			model.addAttribute("errorYn", "Y");
			model.addAttribute("message", egovMessageSource.getMessage("fail.common.delete"));
			return new ModelAndView("jsonView");
		}
	}
}
