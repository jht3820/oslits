package kr.opensoftlab.oslops.cmm.cmm9000.cmm9100.web;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.StringTokenizer;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import kr.opensoftlab.oslops.cmm.cmm9000.cmm9100.service.Cmm9100Service;
import kr.opensoftlab.sdf.util.RequestConvertor;

import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.servlet.ModelAndView;

import egovframework.com.cmm.EgovMessageSource;

/**
 * @Class Name : Cmm9100Controller.java
 * @Description : 공통으로 공통코드를 조회하여 사용하는 클래스
 * @Modification Information
 *
 * @author 정형택
 * @since 2016.01.14.
 * @version 1.0
 * @see Copyright (C) All right reserved.
 */

@Controller
public class Cmm9100Controller {

	/** EgmisTblInfoService DI */
	@Resource(name = "cmm9100Service")
	private Cmm9100Service cmm9100Service;

	/** EgovMessageSource */
	@Resource(name = "egovMessageSource")
	EgovMessageSource egovMessageSource;
	
	/**
	 * 공통 코드정보 목록 조회
	 * 
	 * @param request
	 * @param response
	 * @throws Exception
	 */
	@SuppressWarnings({ "rawtypes", "unchecked", "finally" })
	@RequestMapping(method = RequestMethod.POST, value = "/cmm/cmm9000/cmm9100/selectCmm9100MultiCommonCodeList.do")
	public ModelAndView selectCmm9100MultiCommonCodeList(
			HttpServletRequest request, HttpServletResponse response,
			ModelMap modelMap) throws Exception {
		// 결과 코드 세팅
		String code = "";
		String text = "";
		String subCdRef1 = "";
		
		String mstCdStr = (String) request.getParameter("mstCdStr");
		StringTokenizer st = new StringTokenizer(mstCdStr, "|");

		Map rtnMap = new HashMap();

		try {
			// request 파라미터를 map으로 변환
			Map<String, String> param = RequestConvertor.requestParamToMap(
					request, true);
			param.put("mstCds", param.get("mstCds").replaceAll("&apos;", "'"));

			// 목록 조회
			List<Map> commonCodeList = cmm9100Service
					.selectCmm9100MultiCommonCodeList(param); // 공통코드목록 - 조회파라미터
																// Map 사용 예

			/* 넘겨받은 bigClsCdArr 갯수 만큼 code와 text를 만들어 담는다. */
			while (st.hasMoreElements()) {

				/* 해당 bigClsCd와 같은 내역을 리스트에서 꺼내 담는다. */
				String mstCd = st.nextElement().toString();
				code = "";
				text = "";
				subCdRef1 = "";
				
				for (Map comboMap : commonCodeList) {
					if (mstCd.equals(comboMap.get("mstCd"))) {
						code += comboMap.get("subCd") + "|";
						text += comboMap.get("subCdNm") + "|";
						subCdRef1 += comboMap.get("subCdRef1") + "|";
						// System.out.println(mstCd);
						// System.out.println(code);
						// System.out.println(text);
					}
				}

				/* outOfBound Exception 제거를 위해 길이 비교 */
				if (code.length() > 0) {
					code = code.substring(0, code.length() - 1);
					text = text.substring(0, text.length() - 1);
					subCdRef1 = subCdRef1.substring(0, subCdRef1.length() - 1);
				}

				/* 맵에 대분류코드+code, text 라는 key 명으로 해당 코드, 텍스트 세팅한다. */
				rtnMap.put("mstCd" + mstCd + "code", code);
				rtnMap.put("mstCd" + mstCd + "text", text);
				
				if(subCdRef1.length() > 0) {
					rtnMap.put("subCdRef1" + mstCd + "code", subCdRef1);
				}
				// System.out.println("mstCd" + mstCd + "code =============== "
				// + rtnMap.get("mstCd" + mstCd + "code"));
				// System.out.println("mstCd" + mstCd + "text =============== "
				// + rtnMap.get("mstCd" + mstCd + "text"));
			}

			rtnMap.put("commonCodeList", commonCodeList);
			rtnMap.put("ERROR_CODE", "1");
			rtnMap.put("ERROR_MSG", egovMessageSource.getMessage("cmm9100.success.cmmCom.select"));

			// return new ModelAndView("jsonView", map);

		} catch (Exception ex) {
			ex.printStackTrace();
			rtnMap = new HashMap();
			rtnMap.put("ERROR_CODE", "-1");
			rtnMap.put("ERROR_MSG", egovMessageSource.getMessage("cmm9100.fail.cmmCom.select"));

		} finally {
			return new ModelAndView("jsonView", rtnMap);
		}
	}
}
