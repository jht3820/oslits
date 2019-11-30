package kr.opensoftlab.oslops.adm.adm8000.adm8000.web;

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
import egovframework.rte.fdl.property.EgovPropertyService;
import kr.opensoftlab.oslops.adm.adm8000.adm8000.service.Adm8000Service;
import kr.opensoftlab.oslops.com.vo.LoginVO;
import kr.opensoftlab.sdf.util.ModuleUseCheck;
import kr.opensoftlab.sdf.util.RequestConvertor;

/**
 * @Class Name : Adm8000Controller.java
 * @Description : Adm8000Controller Controller class
 * @Modification Information
 *
 * @author 공대영
 *  * @since 2018.10.22.
 * @version 1.0
 * @see
 *  
 *  Copyright (C)  All right reserved.
 */

@Controller
public class Adm8000Controller {

	/** 로그4j 로거 로딩 */
	private static final Logger Log = Logger.getLogger(Adm8000Controller.class);

	/** EgovMessageSource */
	@Resource(name = "egovMessageSource")
	EgovMessageSource egovMessageSource;

	/** EgovPropertyService */
	@Resource(name = "propertiesService")
	protected EgovPropertyService propertiesService;


	@Value("${Globals.fileStorePath}")
	private String tempPath;

	@Resource(name = "adm8000Service")
	private Adm8000Service adm8000Service;

	/** ModuleUseCheck DI */
	@Resource(name = "moduleUseCheck")
	private ModuleUseCheck moduleUseCheck;
	
	/**
	 * 보고서 설정화면 이동	 
	 * 
	 * 
	 * @param request
	 * @param response
	 * @param model
	 * @return
	 * @throws Exception
	 */
	@RequestMapping(value="/adm/adm8000/adm8000/selectAdm8000View.do")
	public String selectAdm8000View( HttpServletRequest request, HttpServletResponse response, ModelMap model ) throws Exception {
		boolean reportModuleUseChk = false;
		
		try {
			//보고서 모듈 사용 가능한지 체크
			reportModuleUseChk = moduleUseCheck.isReportModuleUseChk();
		}catch(Exception e) {
			Log.debug(e.getMessage());
		}
		model.addAttribute("reportModuleUseChk",reportModuleUseChk);
		return "/adm/adm8000/adm8000/adm8000";
	}
	
	
	/**
	 * 
	 * 등록된 보고서 마스터 연도 조회
	 * 
	 * @param request
	 * @param response
	 * @param model
	 * @return
	 * @throws Exception
	 */
	@SuppressWarnings("rawtypes")
	@RequestMapping(value="/adm/adm8000/adm8000/selectAdm8000MasterYearList.do")
	public ModelAndView selectAdm8000MasterYearList(HttpServletRequest request, HttpServletResponse response, ModelMap model ) throws Exception {

		try{


			// request 파라미터를 map으로 변환
			Map<String, String> paramMap = RequestConvertor.requestParamToMapAddSelInfo(request, true);
			HttpSession ss = request.getSession();

			LoginVO loginVO = (LoginVO) ss.getAttribute("loginVO");

			paramMap.put("prjId", ss.getAttribute("selPrjId").toString());
			paramMap.put("licGrpId", loginVO.getLicGrpId());		


			// 요구사항 일괄저장
			List<Map> list = adm8000Service.selectAdm8000MasterYearList(paramMap);

			model.addAttribute("list", list);

			//등록 성공 메시지 세팅
			model.addAttribute("message", egovMessageSource.getMessage("success.common.select"));

			return new ModelAndView("jsonView");
		}
		catch(Exception ex){
			Log.error("selectAdm8000MasterYearList()", ex);


			model.addAttribute("message", egovMessageSource.getMessage("fail.common.select"));
			return new ModelAndView("jsonView");
		}
	}
	
	/**
	 * 
	 * 연도별 보고서 마스터 조회
	 * 
	 * @param request
	 * @param response
	 * @param model
	 * @return
	 * @throws Exception
	 */
	@RequestMapping(value="/adm/adm8000/adm8000/selectAdm8000MasterList.do")
	public ModelAndView selectAdm8000MasterList(HttpServletRequest request, HttpServletResponse response, ModelMap model ) throws Exception {

		try{


			// request 파라미터를 map으로 변환
			Map<String, String> paramMap = RequestConvertor.requestParamToMapAddSelInfo(request, true);
			HttpSession ss = request.getSession();

			LoginVO loginVO = (LoginVO) ss.getAttribute("loginVO");

			paramMap.put("prjId", ss.getAttribute("selPrjId").toString());
			paramMap.put("licGrpId", loginVO.getLicGrpId());		


			// 요구사항 일괄저장
			List<Map> list = adm8000Service.selectAdm8000MasterList(paramMap);

			model.addAttribute("list", list);

			//등록 성공 메시지 세팅
			model.addAttribute("message", egovMessageSource.getMessage("success.common.select"));

			return new ModelAndView("jsonView");
		}
		catch(Exception ex){
			Log.error("selectAdm8000MasterList()", ex);


			model.addAttribute("message", egovMessageSource.getMessage("fail.common.select"));
			return new ModelAndView("jsonView");
		}
	}
	
	/**
	 * 
	 * 보고서 마스터 삭제
	 * 
	 * @param request
	 * @param response
	 * @param model
	 * @return
	 * @throws Exception
	 */
	@SuppressWarnings("rawtypes")
	@RequestMapping(value="/adm/adm8000/adm8000/deleteAdm8000MasterInfoAjax.do")
	public ModelAndView deleteAdm8000MasterInfoAjax(HttpServletRequest request, HttpServletResponse response, ModelMap model ) throws Exception {

		try{


			// request 파라미터를 map으로 변환
			Map<String, String> paramMap = RequestConvertor.requestParamToMapAddSelInfo(request, true);
			HttpSession ss = request.getSession();
			LoginVO loginVO = (LoginVO) ss.getAttribute("loginVO");

			paramMap.put("prjId", ss.getAttribute("selPrjId").toString());
			paramMap.put("licGrpId", loginVO.getLicGrpId());
		
			adm8000Service.deleteAdm8000MasterInfo(paramMap);
				//등록 성공 메시지 세팅
			model.addAttribute("message", egovMessageSource.getMessage("success.common.delete"));
			

			return new ModelAndView("jsonView");
		}
		catch(Exception ex){
			Log.error("deleteAdm8000MasterInfoAjax()", ex);

			//조회실패 메시지 세팅 및 저장 성공여부 세팅
			model.addAttribute("saveYN", "N");
			model.addAttribute("message", egovMessageSource.getMessage("fail.common.save"));
			return new ModelAndView("jsonView");
		}
	}
	
	/**
	 * 
	 * 보고서 설정 마스터 화면으로 이동
	 * 
	 * @param request
	 * @param response
	 * @param model
	 * @return
	 * @throws Exception
	 */
	@RequestMapping(value="/adm/adm8000/adm8000/selectAdm8001View.do")
	public String selectAdm8001View( HttpServletRequest request, HttpServletResponse response, ModelMap model ) throws Exception {
		return "/adm/adm8000/adm8000/adm8001";
	}
	/**
	 * 보고서 마스터 저장
	 * 
	 * @param request
	 * @param response
	 * @param model
	 * @return
	 * @throws Exception
	 */
	@SuppressWarnings("rawtypes")
	@RequestMapping(value="/adm/adm8000/adm8000/saveAdm8000MasterInfoAjax.do")
	public ModelAndView saveAdm8000MasterInfoAjax(HttpServletRequest request, HttpServletResponse response, ModelMap model ) throws Exception {

		try{
			// request 파라미터를 map으로 변환
			Map<String, String> paramMap = RequestConvertor.requestParamToMapAddSelInfo(request, true);
			HttpSession ss = request.getSession();

			LoginVO loginVO = (LoginVO) ss.getAttribute("loginVO");
			paramMap.put("prjId", ss.getAttribute("selPrjId").toString());
			paramMap.put("licGrpId", loginVO.getLicGrpId());	


			// 요구사항 일괄저장
			Object insertKey = adm8000Service.saveAdm8000MasterInfo(paramMap);


			//등록 성공 메시지 세팅
			model.addAttribute("message", egovMessageSource.getMessage("success.common.save"));

			return new ModelAndView("jsonView");
		}
		catch(Exception ex){
			Log.error("saveAdm8000MasterInfoAjax()", ex);

			//조회실패 메시지 세팅 및 저장 성공여부 세팅
			model.addAttribute("saveYN", "N");
			model.addAttribute("message", egovMessageSource.getMessage("fail.common.save"));
			return new ModelAndView("jsonView");
		}
	}
	
	@RequestMapping(value="/adm/adm8000/adm8000/selectAdm8000MasterInfo.do")
	public ModelAndView selectAdm8000MasterInfo(HttpServletRequest request, HttpServletResponse response, ModelMap model ) throws Exception {

		try{


			// request 파라미터를 map으로 변환
			Map<String, String> paramMap = RequestConvertor.requestParamToMapAddSelInfo(request, true);
			HttpSession ss = request.getSession();

			LoginVO loginVO = (LoginVO) ss.getAttribute("loginVO");

			paramMap.put("prjId", ss.getAttribute("selPrjId").toString());
			paramMap.put("licGrpId", loginVO.getLicGrpId());		


			// 보고서 마스터 단건 조회
			Map reportInfo = adm8000Service.selectAdm8000MasterInfo(paramMap);

			model.addAttribute("reportInfo", reportInfo);

			//등록 성공 메시지 세팅
			model.addAttribute("message", egovMessageSource.getMessage("success.common.select"));

			return new ModelAndView("jsonView");
		}
		catch(Exception ex){
			Log.error("selectAdm8000MasterInfo()", ex);


			model.addAttribute("message", egovMessageSource.getMessage("fail.common.select"));
			return new ModelAndView("jsonView");
		}
	}
	

	/**
	 * 
	 * 보고서 디테일 화면 이동
	 * 
	 * @param request
	 * @param response
	 * @param model
	 * @return
	 * @throws Exception
	 */
	@RequestMapping(value="/adm/adm8000/adm8000/selectAdm8002View.do")
	public String selectAdm8002View( HttpServletRequest request, HttpServletResponse response, ModelMap model ) throws Exception {
		return "/adm/adm8000/adm8000/adm8002";
	}
	
	
	/**
	 * 
	 * 보고서 디테일 저장
	 * 
	 * @param request
	 * @param response
	 * @param model
	 * @return
	 * @throws Exception
	 */
	@SuppressWarnings("rawtypes")
	@RequestMapping(value="/adm/adm8000/adm8000/saveAdm8000DetailInfoAjax.do")
	public ModelAndView saveAdm8000DetailInfoAjax(HttpServletRequest request, HttpServletResponse response, ModelMap model ) throws Exception {

		try{
			// request 파라미터를 map으로 변환
			Map<String, String> paramMap = RequestConvertor.requestParamToMapAddSelInfo(request, true);
			HttpSession ss = request.getSession();

			LoginVO loginVO = (LoginVO) ss.getAttribute("loginVO");
			paramMap.put("prjId", ss.getAttribute("selPrjId").toString());
			paramMap.put("licGrpId", loginVO.getLicGrpId());	


			// 요구사항 일괄저장
			Object insertKey = adm8000Service.saveAdm8000DetailInfo(paramMap);


			//등록 성공 메시지 세팅
			model.addAttribute("message", egovMessageSource.getMessage("success.common.save"));

			return new ModelAndView("jsonView");
		}
		catch(Exception ex){
			Log.error("saveAdm8000DetailInfoAjax()", ex);

			//조회실패 메시지 세팅 및 저장 성공여부 세팅
			model.addAttribute("saveYN", "N");
			model.addAttribute("message", egovMessageSource.getMessage("fail.common.save"));
			return new ModelAndView("jsonView");
		}
	}
	
	/**
	 * 
	 * 보고서 디테일 목록 조회
	 * 
	 * @param request
	 * @param response
	 * @param model
	 * @return
	 * @throws Exception
	 */
	@RequestMapping(value="/adm/adm8000/adm8000/selectAdm8000DetailList.do")
	public ModelAndView selectAdm8000DetailList(HttpServletRequest request, HttpServletResponse response, ModelMap model ) throws Exception {
		
		try{
			
			
			// request 파라미터를 map으로 변환
			Map<String, String> paramMap = RequestConvertor.requestParamToMapAddSelInfo(request, true);
			HttpSession ss = request.getSession();
			
			LoginVO loginVO = (LoginVO) ss.getAttribute("loginVO");
			
			paramMap.put("prjId", ss.getAttribute("selPrjId").toString());
			paramMap.put("licGrpId", loginVO.getLicGrpId());		
			
			
			// 보고서 마스터 단건 조회
			List<Map> list = adm8000Service.selectAdm8000DetailList(paramMap);
			
			model.addAttribute("list", list);
			
			//등록 성공 메시지 세팅
			model.addAttribute("message", egovMessageSource.getMessage("success.common.select"));
			
			return new ModelAndView("jsonView");
		}
		catch(Exception ex){
			Log.error("selectAdm8000DetailList()", ex);
			
			
			model.addAttribute("message", egovMessageSource.getMessage("fail.common.select"));
			return new ModelAndView("jsonView");
		}
	}

	/**
	 * 
	 * 보고서 디테일 상세 조회
	 * 
	 * @param request
	 * @param response
	 * @param model
	 * @return
	 * @throws Exception
	 */
	@RequestMapping(value="/adm/adm8000/adm8000/selectAdm8000DetailInfo.do")
	public ModelAndView selectAdm8000DetailInfo(HttpServletRequest request, HttpServletResponse response, ModelMap model ) throws Exception {
		
		try{
			
			
			// request 파라미터를 map으로 변환
			Map<String, String> paramMap = RequestConvertor.requestParamToMapAddSelInfo(request, true);
			HttpSession ss = request.getSession();
			
			LoginVO loginVO = (LoginVO) ss.getAttribute("loginVO");
			
			paramMap.put("prjId", ss.getAttribute("selPrjId").toString());
			paramMap.put("licGrpId", loginVO.getLicGrpId());		
			
			
			// 보고서 마스터 단건 조회
			Map detailInfo = adm8000Service.selectAdm8000DetailInfo(paramMap);
			
			model.addAttribute("detailInfo", detailInfo);
			
			//등록 성공 메시지 세팅
			model.addAttribute("message", egovMessageSource.getMessage("success.common.select"));
			
			return new ModelAndView("jsonView");
		}
		catch(Exception ex){
			Log.error("selectAdm8000DetailInfo()", ex);
			
			
			model.addAttribute("message", egovMessageSource.getMessage("fail.common.select"));
			return new ModelAndView("jsonView");
		}
	}
	
	
	/**
	 * 
	 * 보고서 디테일 삭제
	 * 
	 * @param request
	 * @param response
	 * @param model
	 * @return
	 * @throws Exception
	 */
	@SuppressWarnings("rawtypes")
	@RequestMapping(value="/adm/adm8000/adm8000/deleteAdm8000DetailInfoAjax.do")
	public ModelAndView deleteAdm8000DetailInfoAjax(HttpServletRequest request, HttpServletResponse response, ModelMap model ) throws Exception {

		try{


			// request 파라미터를 map으로 변환
			Map<String, String> paramMap = RequestConvertor.requestParamToMapAddSelInfo(request, true);
			HttpSession ss = request.getSession();
			LoginVO loginVO = (LoginVO) ss.getAttribute("loginVO");

			paramMap.put("prjId", ss.getAttribute("selPrjId").toString());
			paramMap.put("licGrpId", loginVO.getLicGrpId());
		
			adm8000Service.deleteAdm8000DetailInfo(paramMap);
				//등록 성공 메시지 세팅
			model.addAttribute("message", egovMessageSource.getMessage("success.common.delete"));
			

			return new ModelAndView("jsonView");
		}
		catch(Exception ex){
			Log.error("deleteAdm8000DetailInfoAjax()", ex);

			//조회실패 메시지 세팅 및 저장 성공여부 세팅
			model.addAttribute("saveYN", "N");
			model.addAttribute("message", egovMessageSource.getMessage("fail.common.save"));
			return new ModelAndView("jsonView");
		}
	}
	
	@RequestMapping(value="/adm/adm8000/adm8000/selectAdm8003View.do")
	public String selectAdm8003View( HttpServletRequest request, HttpServletResponse response, ModelMap model ) throws Exception {
		return "/adm/adm8000/adm8000/adm8003";
	}
	
	
	@SuppressWarnings("rawtypes")
	@RequestMapping(value="/adm/adm8000/adm8000/saveAdm8000CopyInfo.do")
	public ModelAndView saveAdm8000CopyInfo(HttpServletRequest request, HttpServletResponse response, ModelMap model ) throws Exception {

		try{
			// request 파라미터를 map으로 변환
			Map<String, String> paramMap = RequestConvertor.requestParamToMapAddSelInfo(request, true);
			HttpSession ss = request.getSession();

			LoginVO loginVO = (LoginVO) ss.getAttribute("loginVO");
			paramMap.put("prjId", ss.getAttribute("selPrjId").toString());
			paramMap.put("licGrpId", loginVO.getLicGrpId());	


			// 요구사항 일괄저장
			Object insertKey = adm8000Service.saveAdm8000CopyInfo(paramMap);


			//등록 성공 메시지 세팅
			model.addAttribute("message", egovMessageSource.getMessage("success.common.save"));

			return new ModelAndView("jsonView");
		}
		catch(Exception ex){
			Log.error("saveAdm8000CopyInfo()", ex);

			//조회실패 메시지 세팅 및 저장 성공여부 세팅
			model.addAttribute("saveYN", "N");
			model.addAttribute("message", egovMessageSource.getMessage("fail.common.save"));
			return new ModelAndView("jsonView");
		}
	}
	

}
