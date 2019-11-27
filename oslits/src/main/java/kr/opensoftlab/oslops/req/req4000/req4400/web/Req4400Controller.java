
package kr.opensoftlab.oslits.req.req4000.req4400.web;

import java.util.List;
import java.util.Map;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import kr.opensoftlab.oslits.com.fms.web.service.FileMngService;
import kr.opensoftlab.oslits.req.req4000.req4400.service.Req4400Service;
import kr.opensoftlab.sdf.util.ReqHistoryMngUtil;
import kr.opensoftlab.sdf.util.RequestConvertor;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;

import egovframework.com.cmm.EgovMessageSource;
import egovframework.com.cmm.service.EgovFileMngService;
import egovframework.com.cmm.service.EgovFileMngUtil;
import egovframework.rte.fdl.cmmn.trace.LeaveaTrace;
import egovframework.rte.fdl.idgnr.EgovIdGnrService;
import egovframework.rte.fdl.property.EgovPropertyService;

/**
 * @Class Name : Req4400Controller.java
 * @Description : Req4400Controller Controller class
 * @Modification Information
 *
 * @author 진주영
 * @since 2018.09.11.
 * @version 1.0
 * @see
 * 
 * 
 *  Copyright (C) OpenSoftLab Corp All right reserved.
 */

@Controller
public class Req4400Controller {

	/** 로그4j 로거 로딩 */
	private final Logger Log = Logger.getLogger(this.getClass());

	/** Req4400Service DI */
	@Resource(name = "req4400Service")
	private Req4400Service req4400Service;

	/** EgovMessageSource */
	@Resource(name = "egovMessageSource")
	EgovMessageSource egovMessageSource;

	/** EgovPropertyService */
	@Resource(name = "propertiesService")
	protected EgovPropertyService propertiesService;

	/** FileMngService */
   	@Resource(name="fileMngService")
   	private FileMngService fileMngService;
   	
	/** EgovFileMngUtil - 파일 업로드 Util */
	@Resource(name="EgovFileMngUtil")
	private EgovFileMngUtil fileUtil;
	
	/** 파일 제어 서비스 */
	@Resource(name = "EgovFileMngService")
	private EgovFileMngService fileService;
	
	/** TRACE */
	@Resource(name = "leaveaTrace")
	LeaveaTrace leaveaTrace;
	
	@Value("${Globals.fileStorePath}")
	private String tempPath;
	
	@Resource(name = "egovFileIdGnrService")
	private EgovIdGnrService idgenService;
	
	@Resource(name = "historyMng")
	private ReqHistoryMngUtil historyMng;
	
	/**
	 * Req4400 요구사항 작업 목록 조회
	 * @param 
	 * @return 
	 * @exception Exception
	 */
	@SuppressWarnings({ "rawtypes", "unchecked" })
	@RequestMapping(value="/req/req4000/req4400/selectReq4400ReqWorkListAjax.do")
	public ModelAndView selectReq4400ReqWorkListAjax(HttpServletRequest request, HttpServletResponse response, ModelMap model ) throws Exception {

		try{
			// request 파라미터를 map으로 변환
			Map<String, String> paramMap = RequestConvertor.requestParamToMapAddSelInfo(request, true);
			paramMap.put("prjId", (String)paramMap.get("selPrjId"));
			
			//작업 목록 조회
			List<Map> workList = (List<Map>)req4400Service.selectReq4400ReqWorkList(paramMap);
			model.addAttribute("workList", workList);
			
			//등록 성공 메시지 세팅
			model.addAttribute("errorYn", "N");
			model.addAttribute("message", egovMessageSource.getMessage("success.common.select"));

			return new ModelAndView("jsonView");
		}
		catch(Exception ex){
			Log.error("selectReq4400ReqWorkListAjax()", ex);

			//조회실패 메시지 세팅 및 저장 성공여부 세팅
			model.addAttribute("errorYn", "Y");
			model.addAttribute("message", egovMessageSource.getMessage("fail.common.select"));
			return new ModelAndView("jsonView");
		}
	}
	
	/**
	 * Req4400 요구사항 작업정보 추가&수정
	 * @param 
	 * @return 
	 * @exception Exception
	 */
	@SuppressWarnings("rawtypes")
	@RequestMapping(value="/req/req4000/req4400/saveReq4400ReqWorkInfoAjax.do")
	public ModelAndView saveReq4400ReqWorkInfoAjax(HttpServletRequest request, HttpServletResponse response, ModelMap model ) throws Exception {

		try{
			// request 파라미터를 map으로 변환
			Map<String, String> paramMap = RequestConvertor.requestParamToMapAddSelInfo(request, true);
			paramMap.put("prjId", (String)paramMap.get("selPrjId"));
			
			String type = (String) paramMap.get("type");
			
			//추가
			if("insert".equals(type)){
				req4400Service.insertReq4400ReqWorkInfoAjax(paramMap);
			}
			//수정
			else if("update".equals(type)){
				req4400Service.updateReq4400ReqWorkInfoAjax(paramMap);
			}
			
			//작업 목록 조회
			List<Map> workList = (List<Map>)req4400Service.selectReq4400ReqWorkList(paramMap);
			model.addAttribute("workList", workList);
			
			//등록 성공 메시지 세팅
			model.addAttribute("errorYn", "N");
			model.addAttribute("message", egovMessageSource.getMessage("success.common.save"));

			return new ModelAndView("jsonView");
		}
		catch(Exception ex){
			Log.error("saveReq4400ReqWorkInfoAjax()", ex);

			//조회실패 메시지 세팅 및 저장 성공여부 세팅
			model.addAttribute("errorYn", "Y");
			model.addAttribute("message", egovMessageSource.getMessage("fail.common.save"));
			return new ModelAndView("jsonView");
		}
	}
	
	/**
	 * Req4400 요구사항 작업정보 삭제
	 * @param 
	 * @return 
	 * @exception Exception
	 */
	@SuppressWarnings({ "rawtypes", "unchecked" })
	@RequestMapping(value="/req/req4000/req4400/deleteReq4400ReqWorkInfoAjax.do")
	public ModelAndView deleteReq4400ReqWorkInfoAjax(HttpServletRequest request, HttpServletResponse response, ModelMap model ) throws Exception {
		
		try{
			// request 파라미터를 map으로 변환
			Map<String, String> paramMap = RequestConvertor.requestParamToMapAddSelInfo(request, true);
			paramMap.put("prjId", (String)paramMap.get("selPrjId"));
			
			req4400Service.deleteReq4400ReqWorkInfoAjax(paramMap);
			
			//작업 목록 조회
			List<Map> workList = (List<Map>)req4400Service.selectReq4400ReqWorkList(paramMap);
			model.addAttribute("workList", workList);
			
			//등록 성공 메시지 세팅
			model.addAttribute("errorYn", "N");
			model.addAttribute("message", egovMessageSource.getMessage("success.common.delete"));
			
			return new ModelAndView("jsonView");
		}
		catch(Exception ex){
			Log.error("deleteReq4400ReqWorkInfoAjax()", ex);
			
			//조회실패 메시지 세팅 및 저장 성공여부 세팅
			model.addAttribute("errorYn", "Y");
			model.addAttribute("message", egovMessageSource.getMessage("fail.common.delete"));
			return new ModelAndView("jsonView");
		}
	}
}
