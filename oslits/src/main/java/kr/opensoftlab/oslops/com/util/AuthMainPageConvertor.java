package kr.opensoftlab.oslops.com.util;




/**
 * RequestConvertor.java 클래스
 * 
 * @author 정형택
 * @since 2013. 11. 12.
 * @version 1.0
 * @see
 * @decription UI에서 넘어온 request parameter를 map 형식으로 변환시켜주는 유틸 클래스
 * 
 * <pre>
 * << 개정이력(Modification Information) >>
 *   
 *   수정일     		 		수정자            수정내용
 *  ---------------    -------------   ------------------------------
 *   2013. 11. 12.   		정형택        	최초 생성
 * </pre>
 */

public class AuthMainPageConvertor {

	/**
	 * String으로 AUTH_GRP_ID 를 넘기면 메인페이지 URL로 리턴
	 * @param String
	 * @return
	 * @throws Exception
	 */
	public static String authMainPageConvert(String authGrpId) throws Exception{
		String strMainUrl = "/index";
		
		//임시 모든 로그인 사용자 대시보드 - 운영으로
		strMainUrl = "redirect:/dsh/dsh1000/dsh1000/selectDsh1000View.do";
		
		/* - 2018-08-09 제거 작업 대상
		if("PO".equals(authGrpId)){
			strMainUrl = "redirect:/cmm/cmm5000/cmm5000/selectCmm5000MainMove.do";
		}
		else if("SM".equals(authGrpId)){
			strMainUrl = "redirect:/cmm/cmm5000/cmm5100/selectCmm5100MainMove.do";
		}
		else if("ST".equals(authGrpId)){
			strMainUrl = "redirect:/cmm/cmm5000/cmm5200/selectCmm5200MainMove.do";
		}
		else if("TT".equals(authGrpId)){
			strMainUrl = "redirect:/cmm/cmm5000/cmm5300/selectCmm5300MainMove.do";
		}
		else if("CS".equals(authGrpId)){
			strMainUrl = "redirect:/cmm/cmm5000/cmm5400/selectCmm5400MainMove.do";
		}
		*/
		return strMainUrl;
	}			
}

