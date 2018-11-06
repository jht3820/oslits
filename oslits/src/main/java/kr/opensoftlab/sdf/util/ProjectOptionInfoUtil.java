package kr.opensoftlab.sdf.util;

import java.util.List;
import java.util.Map;

import org.springframework.ui.ModelMap;


/**
 * ProjectOptionInfoUtil.java 클래스
 * 
 * @author 김현종
 * @since 2017. 07. 21.
 * @version 1.0
 * @see
 * @decription 프로젝트 옵션정보에 대한 값을 세팅 modelMap 에 세팅
 * 
 * <pre>
 * << 개정이력(Modification Information) >>
 *   
 *   수정일     		 		수정자            수정내용
 *  ---------------    -------------   ------------------------------
 * </pre>
 */

public class ProjectOptionInfoUtil {
	
	/**
	 * 프로젝트 옵션정보의 값을 ModelMap 객체와 ParamMap객체에 세팅
	 * @param prjList : 프로젝트 옵션 정보 목록
	 * @return
	 * @exception Exception
	 */
	@SuppressWarnings("rawtypes")
	public static void settingOptionCodeToModelAndParam(List<Map> prjOptList , ModelMap model, Map<String, String> paramMap) {
		
		if(prjOptList != null && prjOptList.size() > 0)
		for(Map map : prjOptList) {
			if(map.get("mstCd").toString().equals("PRJ00007")){
				model.addAttribute("sprintAuth", map.get("optStateCd").equals("01")?OslAgileConstant.YES:OslAgileConstant.NO);
			}else if(map.get("mstCd").toString().equals("PRJ00008")){
				model.addAttribute("sprintWkTmZero", map.get("optStateCd").equals("01")?OslAgileConstant.YES:OslAgileConstant.NO);
			}else if(map.get("mstCd").toString().equals("PRJ00009")){
				model.addAttribute("reqMoveFlowChk", map.get("optStateCd").equals("01")?OslAgileConstant.YES:OslAgileConstant.NO);
			}else if(map.get("mstCd").toString().equals("PRJ00010")){
				model.addAttribute("reqThinDetailInfo", map.get("optStateCd").equals("01")?OslAgileConstant.YES:OslAgileConstant.NO);
			}else if(map.get("mstCd").toString().equals("PRJ00011")){
				model.addAttribute("sprintWorkFlowResolution", map);
			}else if(map.get("mstCd").toString().equals("PRJ00012")){
				model.addAttribute("sprintWorkBoardDirection", map.get("optStateCd"));
			}
			paramMap.put("mstCd", map.get("mstCd").toString());
		}
	}
}
