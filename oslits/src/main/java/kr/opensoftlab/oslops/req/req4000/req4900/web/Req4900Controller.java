
package kr.opensoftlab.oslits.req.req4000.req4900.web;

import java.util.Map;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import kr.opensoftlab.oslits.com.fms.web.service.FileMngService;
import kr.opensoftlab.oslits.req.req4000.req4900.service.Req4900Service;
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
 * @Class Name : Req4900Controller.java
 * @Description : Req4900Controller Controller class
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
public class Req4900Controller {

	/** 로그4j 로거 로딩 */
	private final Logger Log = Logger.getLogger(this.getClass());

	/** Req4900Service DI */
	@Resource(name = "req4900Service")
	private Req4900Service req4900Service;

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
	 * Req4900 결재 승인 & 반려
	 * @param 
	 * @return 
	 * @exception Exception
	 */
	@RequestMapping(value="/req/req4000/req4900/insertReq4900SignActionAjax.do")
	public ModelAndView insertReq4900SignActionAjax(HttpServletRequest request, HttpServletResponse response, ModelMap model ) throws Exception {

		try{
			// request 파라미터를 map으로 변환
			Map<String, String> paramMap = RequestConvertor.requestParamToMapAddSelInfo(request, true);
			HttpSession ss = request.getSession();
			paramMap.put("prjId", (String)paramMap.get("selPrjId"));
			
			//프로젝트 그룹 Id
        	String prjGrpId = (String)ss.getAttribute("selPrjGrpId");
        	paramMap.put("prjGrpId", prjGrpId);
        	
			//결재 등록
			req4900Service.insertReq4900ReqSignInfo(paramMap);
			
			//등록 성공 메시지 세팅
			model.addAttribute("errorYn", "N");
			model.addAttribute("message", egovMessageSource.getMessage("success.common.insert"));

			return new ModelAndView("jsonView");
		}
		catch(Exception ex){
			Log.error("insertReq4900SignActionAjax()", ex);

			//조회실패 메시지 세팅 및 저장 성공여부 세팅
			model.addAttribute("errorYn", "Y");
			model.addAttribute("message", egovMessageSource.getMessage("fail.common.insert"));
			return new ModelAndView("jsonView");
		}
	}
	
}
