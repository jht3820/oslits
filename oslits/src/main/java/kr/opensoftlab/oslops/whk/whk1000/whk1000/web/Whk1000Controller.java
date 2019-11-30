
package kr.opensoftlab.oslops.whk.whk1000.whk1000.web;

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
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.servlet.ModelAndView;

import egovframework.com.cmm.EgovMessageSource;
import egovframework.rte.fdl.property.EgovPropertyService;
import egovframework.rte.ptl.mvc.tags.ui.pagination.PaginationInfo;
import kr.opensoftlab.oslops.com.exception.UserDefineException;
import kr.opensoftlab.oslops.whk.whk1000.whk1000.service.Whk1000Service;
import kr.opensoftlab.oslops.whk.whk1000.whk1000.vo.Whk1000VO;
import kr.opensoftlab.sdf.util.OslAgileConstant;
import kr.opensoftlab.sdf.util.PagingUtil;
import kr.opensoftlab.sdf.util.RequestConvertor;
import kr.opensoftlab.sdf.util.WebhookSend;

/**
 * @Class Name : Whk1000Controller.java
 * @Description : Whk1000Controller Controller class
 * @Modification Information
 *
 * @author 진주영
 * @since 2019.05.21
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
public class Whk1000Controller {

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
    
    /** Whk1000Service DI */
    @Resource(name = "whk1000Service")
    private Whk1000Service whk1000Service;
    
    /** Whk1000Service DI */
    @Resource(name = "webhookSend")
    private WebhookSend webhookSend;

    /**
	 * whk1000 개인 웹훅 관리
	 * @param 
	 * @return 
	 * @exception Exception
	 */
	@RequestMapping(value="/whk/whk1000/whk1000/selectWhk1000View.do")
    public String selectWhk1000View(HttpServletRequest request, HttpServletResponse response, ModelMap model ) throws Exception {
    	return "/whk/whk1000/whk1000/whk1000";
    }

    /**
	 * whk1001 웹훅 추가&수정
	 * @param 
	 * @return 
	 * @exception Exception
	 */
	@SuppressWarnings("rawtypes")
	@RequestMapping(value="/whk/whk1000/whk1000/selectWhk1001View.do")
    public String selectWhk1001View(HttpServletRequest request, HttpServletResponse response, ModelMap model ) throws Exception {
		
		// request 파라미터를 map으로 변환
    	Map<String, String> paramMap = RequestConvertor.requestParamToMap(request, true);
    	
    	Map whk1000WhkInfo = null;
		if( "U".equals(paramMap.get("proStatus"))){ // 수정일 경우
			//세션에서 프로젝트 id 가져오기
			HttpSession ss = request.getSession();
			String prjId = (String) ss.getAttribute("selPrjId");
			
			paramMap.put("prjId", prjId);
			
    		whk1000WhkInfo = whk1000Service.selectWhk1000PrjWhkInfo(paramMap);
    	}
		
		model.put("whk1000WhkInfo", whk1000WhkInfo);
    	return "/whk/whk1000/whk1000/whk1001";
    }
	

	/**
	 * whk1000 웹훅 목록 조회
	 * @param 
	 * @return 
	 * @exception Exception
	 */
	@RequestMapping(method=RequestMethod.POST, value="/whk/whk1000/whk1000/selectWhk1000PrjWhkListAjax.do")
    public ModelAndView selectWhk1000PrjWhkListAjax(@ModelAttribute("whk1000VO") Whk1000VO whk1000VO, HttpServletRequest request, HttpServletResponse response, ModelMap model ) throws Exception {
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
			whk1000VO.setPageIndex(_pageNo);
			whk1000VO.setPageSize(_pageSize);
			whk1000VO.setPageUnit(_pageSize);
        	
        	
        	PaginationInfo paginationInfo = PagingUtil.getPaginationInfo(whk1000VO); /** paging - 신규방식 */
			List<Whk1000VO> whk1000List = null;
			
			HttpSession ss = request.getSession();
			String prjId	= (String) ss.getAttribute("selPrjId");
			
			whk1000VO.setPrjId(prjId);
			
        	//배포 계획 정보 리스트 조회
        	whk1000List = (List<Whk1000VO>) whk1000Service.selectWhk1000PrjWhkList(whk1000VO);
        	
        	/** 총 데이터의 건수 를 가져온다. */
			int totCnt = whk1000Service.selectWhk1000PrjWhkListCnt(whk1000VO);
					
        	paginationInfo.setTotalRecordCount(totCnt);
        	
        	model.addAttribute("list", whk1000List);
        	
			//페이지 정보 보내기
			Map<String, Integer> pageMap = new HashMap<String, Integer>();
			pageMap.put("pageNo",whk1000VO.getPageIndex());
			pageMap.put("listCount", whk1000List.size());
			pageMap.put("totalPages", paginationInfo.getTotalPageCount());
			pageMap.put("totalElements", totCnt);
			pageMap.put("pageSize", _pageSize);
			
			model.addAttribute("page", pageMap);
        	
        	//조회성공메시지 세팅
        	model.addAttribute("message", egovMessageSource.getMessage("success.common.select"));
        	
        	return new ModelAndView("jsonView", model);
    	}
    	catch(Exception ex){
    		Log.error("selectWhk1000PrjWhkListAjax()", ex);
    		
    		//조회실패 메시지 세팅
    		model.addAttribute("message", egovMessageSource.getMessage("fail.common.select"));
    		return new ModelAndView("jsonView", model);
    	}
    }
	
	/**
	 * whk1000 웹훅 연결 테스트
	 * @param 
	 * @return 
	 * @exception Exception
	 */
	@SuppressWarnings({ "unchecked", "rawtypes" })
	@RequestMapping(method=RequestMethod.POST, value="/whk/whk1000/whk1000/selectWhk1000WebhookConnectTest.do")
	public ModelAndView selectWhk1000WebhookConnectTest(HttpServletRequest request, HttpServletResponse response, ModelMap model ) throws Exception {
		try{
			
			//리퀘스트에서 넘어온 파라미터를 맵으로 세팅
			Map<String, String> paramMap = RequestConvertor.requestParamToMapAddSelInfo(request, true);
			
			//프로젝트명 구하기
			HttpSession ss = request.getSession();
			String prjId = (String) paramMap.get("selPrjId");
			String prjNm = "";
			List<Map> prjList = (List<Map>) ss.getAttribute("prjList");

			if(prjList != null && prjList.size() > 0){
				//prjList loop
				for(Map prjInfo : prjList){
					if(prjId.equals(prjInfo.get("prjId"))){
						prjNm = (String) prjInfo.get("prjNm");
						break;
					}
				}
			}
			
			//플랫폼 종류
			String platformTypeCd = paramMap.get("platformTypeCd");
			
			//웹훅 url
			String webhookUrl = paramMap.get("webhookUrl");
			
			//웹훅 내용
			String inMessage = "["+prjNm+"] 프로젝트 에서 Webhook 연결을 시도합니다.";

			//타입에 따라 등록, 수정
			String proStatus = paramMap.get("proStatus");
			
			//등록
			if("I".equals(proStatus)){
				inMessage += "\n- 연결을 성공했습니다.";
			}
			//수정
			else{
				inMessage += "\n- 옵션 항목이 수정되었습니다.";
			}
			
			//웹훅 테스트
			webhookSend.webhookConnectTest(inMessage, webhookUrl, platformTypeCd);
			
			//오류 발생한경우 오류 내역 전송
			model.addAttribute("returnData", webhookSend.getReturnDataList());
			
			return new ModelAndView("jsonView", model);
		}
		catch(UserDefineException ude) {
			//조회실패 메시지 세팅
			model.addAttribute("returnData", webhookSend.getReturnDataList());
			return new ModelAndView("jsonView", model);
		}
		catch(Exception ex){
			Log.error("selectWhk1000WebhookConnectTest()", ex);
			
			//조회실패 메시지 세팅
			model.addAttribute("message", egovMessageSource.getMessage("fail.request.msg"));
			return new ModelAndView("jsonView", model);
		}
	}
	
	/**
	 * whk1000 웹훅 등록
	 * @param 
	 * @return 
	 * @exception Exception
	 */
	@RequestMapping(method=RequestMethod.POST, value="/whk/whk1000/whk1000/insertWhk1000WebhookInfo.do")
	public ModelAndView insertWhk1000WebhookInfo(HttpServletRequest request, HttpServletResponse response, ModelMap model ) throws Exception {
		try{
			
			//리퀘스트에서 넘어온 파라미터를 맵으로 세팅
			Map<String, String> paramMap = RequestConvertor.requestParamToMapAddSelInfo(request, true);
			
			//세션에서 프로젝트 id 가져오기
			HttpSession ss = request.getSession();
			String prjId = (String) ss.getAttribute("selPrjId");
			
			paramMap.put("prjId", prjId);
			
			//웹훅 등록
			whk1000Service.insertWhk1000PrjWhkInfo(paramMap);
			
			model.addAttribute("errorYn", "N");
    		model.addAttribute("message", egovMessageSource.getMessage("success.common.insert"));
    		
			return new ModelAndView("jsonView", model);
		}
		catch(UserDefineException ude) {
			//조회실패 메시지 세팅
			model.addAttribute("message", ude.getMessage());
			return new ModelAndView("jsonView", model);
		}
		catch(Exception ex){
			Log.error("insertWhk1000WebhookInfo()", ex);
			
			//조회실패 메시지 세팅
			model.addAttribute("errorYn", "Y");
    		model.addAttribute("message", egovMessageSource.getMessage("fail.common.insert"));
			return new ModelAndView("jsonView", model);
		}
	}
	
	/**
	 * whk1000 웹훅 수정
	 * @param 
	 * @return 
	 * @exception Exception
	 */
	@RequestMapping(method=RequestMethod.POST, value="/whk/whk1000/whk1000/updateWhk1000WebhookInfo.do")
	public ModelAndView updateWhk1000WebhookInfo(HttpServletRequest request, HttpServletResponse response, ModelMap model ) throws Exception {
		try{
			
			//리퀘스트에서 넘어온 파라미터를 맵으로 세팅
			Map<String, String> paramMap = RequestConvertor.requestParamToMapAddSelInfo(request, true);
			
			//세션에서 프로젝트 id 가져오기
			HttpSession ss = request.getSession();
			String prjId = (String) ss.getAttribute("selPrjId");
			
			paramMap.put("prjId", prjId);
			
			//웹훅 수정
			whk1000Service.updateWhk1000PrjWhkInfo(paramMap);
			
			model.addAttribute("errorYn", "N");
			model.addAttribute("message", egovMessageSource.getMessage("success.common.update"));
			
			return new ModelAndView("jsonView", model);
		}
		catch(UserDefineException ude) {
			//조회실패 메시지 세팅
			model.addAttribute("message", ude.getMessage());
			return new ModelAndView("jsonView", model);
		}
		catch(Exception ex){
			Log.error("updateWhk1000WebhookInfo()", ex);
			
			//조회실패 메시지 세팅
			model.addAttribute("errorYn", "Y");
			model.addAttribute("message", egovMessageSource.getMessage("fail.common.update"));
			return new ModelAndView("jsonView", model);
		}
	}
	
	/**
	 * whk1000 웹훅 삭제
	 * @param 
	 * @return 
	 * @exception Exception
	 */
	@RequestMapping(method=RequestMethod.POST, value="/whk/whk1000/whk1000/deleteWhk1000WebhookList.do")
	public ModelAndView deleteWhk1000WebhookList(HttpServletRequest request, HttpServletResponse response, ModelMap model ) throws Exception {
		try{
			
			//리퀘스트에서 넘어온 파라미터를 맵으로 세팅
			Map<String, Object> paramMap = RequestConvertor.requestParamToMapAddSelInfoList(request, true,"webhookId");
			
			//세션에서 프로젝트 id 가져오기
			HttpSession ss = request.getSession();
			String prjId = (String) ss.getAttribute("selPrjId");
			
			paramMap.put("prjId", prjId);
			
			whk1000Service.deleteWhk1000PrjWhkList(paramMap);
			
			
			model.addAttribute("errorYn", "N");
			model.addAttribute("message", egovMessageSource.getMessage("success.common.delete"));
			
			return new ModelAndView("jsonView", model);
		}
		catch(Exception ex){
			Log.error("deleteWhk1000WebhookList()", ex);
			
			//조회실패 메시지 세팅
			model.addAttribute("errorYn", "Y");
			model.addAttribute("message", egovMessageSource.getMessage("fail.common.delete"));
			return new ModelAndView("jsonView", model);
		}
	}
}
