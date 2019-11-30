package kr.opensoftlab.oslops.whk.whk2000.whk2000.web;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

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
import kr.opensoftlab.oslops.whk.whk2000.whk2000.service.Whk2000Service;
import kr.opensoftlab.oslops.whk.whk2000.whk2000.vo.Whk2000VO;
import kr.opensoftlab.sdf.util.OslAgileConstant;
import kr.opensoftlab.sdf.util.PagingUtil;
import kr.opensoftlab.sdf.util.RequestConvertor;
import kr.opensoftlab.sdf.util.WebhookSend;

/**
 * @Class Name : Whk2000Controller.java
 * @Description : Whk2000Controller Controller class
 * @Modification Information
 *
 * @author 진주영
 * @since 2019.05.24
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
public class Whk2000Controller {

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
    
    /** Whk2000Service DI */
    @Resource(name = "whk2000Service")
    private Whk2000Service whk2000Service;
    
    /** WebhookSend */
    @Resource(name = "webhookSend")
    private WebhookSend webhookSend;

    /**
	 * whk2000 사용자 웹훅 관리
	 * @param 
	 * @return 
	 * @exception Exception
	 */
	@RequestMapping(value="/whk/whk2000/whk2000/selectWhk2000View.do")
    public String selectWhk2000View(HttpServletRequest request, HttpServletResponse response, ModelMap model ) throws Exception {
    	return "/whk/whk2000/whk2000/whk2000";
    }

    /**
	 * whk2001 사용자 웹훅 추가&수정
	 * @param 
	 * @return 
	 * @exception Exception
	 */
	@SuppressWarnings("rawtypes")
	@RequestMapping(value="/whk/whk2000/whk2000/selectWhk2001View.do")
    public String selectWhk2001View(HttpServletRequest request, HttpServletResponse response, ModelMap model ) throws Exception {
		
		// request 파라미터를 map으로 변환
    	Map<String, String> paramMap = RequestConvertor.requestParamToMap(request, true);
    	
    	Map whk2000WhkInfo = null;
		if( "U".equals(paramMap.get("proStatus"))){ // 수정일 경우
			paramMap.put("usrId", paramMap.get("regUsrId"));
			
    		whk2000WhkInfo = whk2000Service.selectWhk2000UsrWhkInfo(paramMap);
    	}
		
		model.put("whk2000WhkInfo", whk2000WhkInfo);
    	return "/whk/whk2000/whk2000/whk2001";
    }
	

	/**
	 * whk2000 사용자 웹훅 목록 조회
	 * @param 
	 * @return 
	 * @exception Exception
	 */
	@RequestMapping(method=RequestMethod.POST, value="/whk/whk2000/whk2000/selectWhk2000UsrWhkListAjax.do")
    public ModelAndView selectWhk2000UsrWhkListAjax(@ModelAttribute("whk2000VO") Whk2000VO whk2000VO, HttpServletRequest request, HttpServletResponse response, ModelMap model ) throws Exception {
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
			whk2000VO.setPageIndex(_pageNo);
			whk2000VO.setPageSize(_pageSize);
			whk2000VO.setPageUnit(_pageSize);
        	
        	
        	PaginationInfo paginationInfo = PagingUtil.getPaginationInfo(whk2000VO); /** paging - 신규방식 */
			List<Whk2000VO> whk2000List = null;
			
			whk2000VO.setLicGrpId(paramMap.get("licGrpId"));
			whk2000VO.setUsrId(paramMap.get("regUsrId"));
			
        	//배포 계획 정보 리스트 조회
        	whk2000List = (List<Whk2000VO>) whk2000Service.selectWhk2000UsrWhkList(whk2000VO);
        	
        	/** 총 데이터의 건수 를 가져온다. */
			int totCnt = whk2000Service.selectWhk2000UsrWhkListCnt(whk2000VO);
					
        	paginationInfo.setTotalRecordCount(totCnt);
        	
        	model.addAttribute("list", whk2000List);
        	
			//페이지 정보 보내기
			Map<String, Integer> pageMap = new HashMap<String, Integer>();
			pageMap.put("pageNo",whk2000VO.getPageIndex());
			pageMap.put("listCount", whk2000List.size());
			pageMap.put("totalPages", paginationInfo.getTotalPageCount());
			pageMap.put("totalElements", totCnt);
			pageMap.put("pageSize", _pageSize);
			
			model.addAttribute("page", pageMap);
        	
        	//조회성공메시지 세팅
        	model.addAttribute("message", egovMessageSource.getMessage("success.common.select"));
        	
        	return new ModelAndView("jsonView", model);
    	}
    	catch(Exception ex){
    		Log.error("selectWhk2000UsrWhkListAjax()", ex);
    		
    		//조회실패 메시지 세팅
    		model.addAttribute("message", egovMessageSource.getMessage("fail.common.select"));
    		return new ModelAndView("jsonView", model);
    	}
    }
	
	/**
	 * whk2000 사용자 웹훅 연결 테스트
	 * @param 
	 * @return 
	 * @exception Exception
	 */
	@RequestMapping(method=RequestMethod.POST, value="/whk/whk2000/whk2000/selectWhk2000WebhookConnectTest.do")
	public ModelAndView selectWhk2000WebhookConnectTest(HttpServletRequest request, HttpServletResponse response, ModelMap model ) throws Exception {
		try{
			
			//리퀘스트에서 넘어온 파라미터를 맵으로 세팅
			Map<String, String> paramMap = RequestConvertor.requestParamToMapAddSelInfo(request, true);
			
			//사용자 명
			String usrNm = paramMap.get("whkRegUsrNm");
			
			//플랫폼 종류
			String platformTypeCd = paramMap.get("platformTypeCd");
			
			//사용자 웹훅 url
			String webhookUrl = paramMap.get("webhookUrl");
			
			//웹훅 내용
			String inMessage = "["+usrNm+"] 님께서 Webhook 연결을 시도합니다.";
			
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
			
			//사용자 웹훅 테스트
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
			Log.error("selectWhk2000WebhookConnectTest()", ex);
			
			//조회실패 메시지 세팅
			model.addAttribute("message", egovMessageSource.getMessage("fail.request.msg"));
			return new ModelAndView("jsonView", model);
		}
	}
	
	/**
	 * whk2000 사용자 웹훅 등록
	 * @param 
	 * @return 
	 * @exception Exception
	 */
	@RequestMapping(method=RequestMethod.POST, value="/whk/whk2000/whk2000/insertWhk2000WebhookInfo.do")
	public ModelAndView insertWhk2000WebhookInfo(HttpServletRequest request, HttpServletResponse response, ModelMap model ) throws Exception {
		try{
			
			//리퀘스트에서 넘어온 파라미터를 맵으로 세팅
			Map<String, String> paramMap = RequestConvertor.requestParamToMapAddSelInfo(request, true);
			
			paramMap.put("usrId", paramMap.get("regUsrId"));
			
			//사용자 웹훅 등록
			whk2000Service.insertWhk2000UsrWhkInfo(paramMap);
			
			model.addAttribute("errorYn", "N");
    		model.addAttribute("message", egovMessageSource.getMessage("success.common.insert"));
    		
			return new ModelAndView("jsonView", model);
		}
		catch(Exception ex){
			Log.error("insertWhk2000WebhookInfo()", ex);
			
			//조회실패 메시지 세팅
			model.addAttribute("errorYn", "Y");
    		model.addAttribute("message", egovMessageSource.getMessage("fail.common.insert"));
			return new ModelAndView("jsonView", model);
		}
	}
	
	/**
	 * whk2000 사용자 웹훅 수정
	 * @param 
	 * @return 
	 * @exception Exception
	 */
	@RequestMapping(method=RequestMethod.POST, value="/whk/whk2000/whk2000/updateWhk2000WebhookInfo.do")
	public ModelAndView updateWhk2000WebhookInfo(HttpServletRequest request, HttpServletResponse response, ModelMap model ) throws Exception {
		try{
			
			//리퀘스트에서 넘어온 파라미터를 맵으로 세팅
			Map<String, String> paramMap = RequestConvertor.requestParamToMapAddSelInfo(request, true);
			paramMap.put("usrId", paramMap.get("regUsrId"));
			
			//사용자 웹훅 수정
			whk2000Service.updateWhk2000UsrWhkInfo(paramMap);
			
			model.addAttribute("errorYn", "N");
			model.addAttribute("message", egovMessageSource.getMessage("success.common.update"));
			
			return new ModelAndView("jsonView", model);
		}
		catch(Exception ex){
			Log.error("updateWhk2000WebhookInfo()", ex);
			
			//조회실패 메시지 세팅
			model.addAttribute("errorYn", "Y");
			model.addAttribute("message", egovMessageSource.getMessage("fail.common.update"));
			return new ModelAndView("jsonView", model);
		}
	}
	
	/**
	 * whk2000 사용자 웹훅 삭제
	 * @param 
	 * @return 
	 * @exception Exception
	 */
	@RequestMapping(method=RequestMethod.POST, value="/whk/whk2000/whk2000/deleteWhk2000WebhookList.do")
	public ModelAndView deleteWhk2000WebhookList(HttpServletRequest request, HttpServletResponse response, ModelMap model ) throws Exception {
		try{
			
			//리퀘스트에서 넘어온 파라미터를 맵으로 세팅
			Map<String, Object> paramMap = RequestConvertor.requestParamToMapAddSelInfoList(request, true,"webhookId");
			paramMap.put("usrId", paramMap.get("regUsrId"));
			
			whk2000Service.deleteWhk2000UsrWhkList(paramMap);
			
			
			model.addAttribute("errorYn", "N");
			model.addAttribute("message", egovMessageSource.getMessage("success.common.delete"));
			
			return new ModelAndView("jsonView", model);
		}
		catch(Exception ex){
			Log.error("deleteWhk2000WebhookList()", ex);
			
			//조회실패 메시지 세팅
			model.addAttribute("errorYn", "Y");
			model.addAttribute("message", egovMessageSource.getMessage("fail.common.delete"));
			return new ModelAndView("jsonView", model);
		}
	}
}
