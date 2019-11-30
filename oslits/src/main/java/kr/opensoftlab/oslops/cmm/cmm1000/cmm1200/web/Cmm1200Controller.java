package kr.opensoftlab.oslops.cmm.cmm1000.cmm1200.web;

import java.util.List;
import java.util.Map;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import kr.opensoftlab.oslops.adm.adm7000.adm7000.service.Adm7000Service;
import kr.opensoftlab.sdf.util.RequestConvertor;

import org.apache.log4j.Logger;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestMapping;

import egovframework.com.cmm.EgovMessageSource;
import egovframework.rte.fdl.property.EgovPropertyService;


/**
 * @Class Name : Cmm1200Controller.java
 * @Description : 공통 - 조직도 화면호출(Cmm1200) 컨트롤러 클래스
 * @Modification Information
 * <pre>
 *   수정일      수정자          수정내용
 *  -------    --------    ---------------------------
 *  2018.08.01  배용진          최초 생성
 *  
 * </pre>
 *  @author 배용진
 *  @version 1.0
 *  @see 
 *  
 *  Copyright (C) Open Soft Lab Corp. All Rights Reserved
 */
@Controller
public class Cmm1200Controller {
	
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
	
	/** Adm7000Service DI */
	@Resource(name = "adm7000Service")
	private Adm7000Service adm7000Service;
	
	/**
	 * Cmm1200 조직도 선택 팝업 오픈
	 * @param 
	 * @return 
	 * @exception Exception
	 */
	@SuppressWarnings({ "unchecked", "rawtypes" })
	@RequestMapping(value="/cmm/cmm1000/cmm1200/selectCmm1200View.do")
    public String selectCmm1200View(HttpServletRequest request, HttpServletResponse response, ModelMap model ) throws Exception {
		
		try{
			//리퀘스트에서 넘어온 파라미터를 맵으로 세팅
			Map paramMap = RequestConvertor.requestParamToMapAddSelInfo(request, true);
			
			// 등록된 조직 목록 조회
    		List<Map> deptList = (List)adm7000Service.selectAdm7000DeptList(paramMap);
			
    		//조회 성공메시지 세팅
			model.addAttribute("deptList", deptList);
			model.addAttribute("message", egovMessageSource.getMessage("success.common.select"));
			
			return "/cmm/cmm1000/cmm1200/cmm1200";
			
		}catch(Exception ex){
			Log.error("selectCmm1200View()", ex);
			throw new Exception(ex.getMessage());
		}	
	}
	

}
