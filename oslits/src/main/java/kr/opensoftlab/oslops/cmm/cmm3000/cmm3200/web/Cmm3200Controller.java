package kr.opensoftlab.oslops.cmm.cmm3000.cmm3200.web;

import java.util.HashMap;
import java.util.Map;
import java.util.Random;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import kr.opensoftlab.oslops.cmm.cmm3000.cmm3200.service.Cmm3200Service;
import kr.opensoftlab.sdf.util.RequestConvertor;

import org.apache.log4j.Logger;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;

import egovframework.com.cmm.EgovMessageSource;
import egovframework.com.cmm.service.EgovProperties;
import egovframework.com.cop.ems.service.EgovSndngMailService;
import egovframework.com.cop.ems.service.SndngMailVO;
import egovframework.com.utl.fcc.service.EgovStringUtil;
import egovframework.com.utl.sim.service.EgovFileScrty;
import egovframework.rte.fdl.cmmn.trace.LeaveaTrace;
import egovframework.rte.fdl.property.EgovPropertyService;

/**
 * @Class Name : Cmm3200Controller.java
 * @Description : Cmm3200Controller Controller class
 * @Modification Information
 *
 * @author 김정환
 * @since 2016.01.16
 * @version 1.0
 * @see
 *  
 *  Copyright (C)  All right reserved.
 */

@Controller
public class Cmm3200Controller {

	/** 로그4j 로거 로딩 */
	private static final Logger Log = Logger.getLogger(Cmm3200Controller.class);
	
	/** 이메일 옵션 properties */
	//인증번호 재 발송 대기시간
	private static final int ReSendTime = Integer.parseInt(EgovProperties.getProperty("Globals.mail.reSend"));

	//인증번호 세션 만료 시간
	private static final int SessionTime = Integer.parseInt(EgovProperties.getProperty("Globals.mail.sessionTime"));
	
	
	/** Cmm3200Service DI */
    @Resource(name = "cmm3200Service")
    private Cmm3200Service cmm3200Service;
    
    /** EgovMessageSource */
	@Resource(name = "egovMessageSource")
	EgovMessageSource egovMessageSource;

	/** EgovPropertyService */
	@Resource(name = "propertiesService")
	protected EgovPropertyService propertiesService;

	/** EgovSndngMailService */
	@Resource(name = "egovSndngMailService")
	private EgovSndngMailService egovSndngMailService;
	
	/** TRACE */
	@Resource(name = "leaveaTrace")
	LeaveaTrace leaveaTrace;
	
	/**
	 * Cmm3200 화면 이동
	 * @param 
	 * @return 
	 * @exception Exception
	 */
	@RequestMapping(value="/cmm/cmm3000/cmm3200/selectCmm3200View.do")
	public String selectCmm3200View(HttpServletRequest request, ModelMap model) throws Exception {
		//이메일 인증여부 전달
		String emailAuthChk = EgovProperties.getProperty("Globals.lunaops.emailAuth");
		model.addAttribute("emailAuthChk", emailAuthChk);
		
		return "/cmm/cmm3000/cmm3200/cmm3200";
	}

	/**
	 * 아이디 중복 체크
	 * @param request
	 * @param model
	 * @return
	 * @throws Exception
	 */
	@RequestMapping(value = "/cmm/cmm3000/cmm3200/selectCmm3200IdCheck.do", produces="text/plain;charset=UTF-8")
	  public ModelAndView selectCmm3200IdCheck (HttpServletRequest request, ModelMap model) throws Exception   {

		// 값을 넘기기 위한 HasMap
		HashMap<String, String> map = new HashMap<String, String>();
		try{

			// request 파라미터를 map으로 변환
			Map<String, String> param = RequestConvertor.requestParamToMap(request, true);

			/* 파라미터 로그찍기 */
			if (Log.isDebugEnabled()) {
				Log.debug("\n=====> parameter map : " + param);
			}

			// 사용자 입력 정보 할당
			String UsrInId = param.get("InUsrId");

			
			// 아이디 입력값이 없을 경우 retrun
			if( "".equals(EgovStringUtil.isNullToString(UsrInId)) ) {
				
				map.put("idChk", "N");

				return new ModelAndView("jsonView", map);
			}
			

			//사용자 입력 정보 paramMap 맵핑
			Map<String, String> paramMap = new HashMap<String, String>();
			paramMap.put("usrId", UsrInId);

			int idChkCnt  = 1;
			// DB 사용자 정보 조회
			idChkCnt =  cmm3200Service.selectCmm3200idChk( paramMap );
			
			// DB 사용자 정보 조회 결과 확인
			if( idChkCnt == 0) {
				// 사용자 정보 DB 조회 결과 없음
				map.put("chkId", "Y");
				
			}else{
				map.put("chkId", "N");
			}

		}catch(Exception e){
			Log.error("selectCmm3200idChk", e);
			return new ModelAndView("jsonView", map);
		}
		return new ModelAndView("jsonView", map);
	}
	
	
	
	/**
	 * 인증번호 발송
	 * @param request
	 * @param model
	 * @return
	 * @throws Exception
	 */
	@RequestMapping(value = "/cmm/cmm3000/cmm3200/selectCmm3200MailSend.do", produces="text/plain;charset=UTF-8")
	  public ModelAndView selectCmm3200MailSend (HttpServletRequest request, ModelMap model) throws Exception   {

		// 값을 넘기기 위한 HasMap
		HashMap<String, String> map = new HashMap<String, String>();
		
		try{
			// request 파라미터를 map으로 변환
			Map<String, String> param = RequestConvertor.requestParamToMap(request, true);

			/* 파라미터 로그찍기 */    
			if (Log.isDebugEnabled()) {
				Log.debug("\n=====> parameter map : " + param);
			}
			
			// 사용자 입력 정보 할당
			String UsrInEmail = param.get("InEmail");
			
			// 이메일 입력값이 없을 경우.
			if("".equals(EgovStringUtil.isNullToString(UsrInEmail)) ) {
				 
				map.put("emailChk", "N");
				
				return new ModelAndView("jsonView", map);
			} else {
				map.put("emailChk", "Y");
			}


			// 이메일 전송 문구
			map.put("emailSend","");
			map.put("emailSendTime", "N");

			/* 이메일 재사용시간 구하기 */
			if(request.getSession() != null && request.getSession().getAttribute("EmailAuthTime") != null) {
				// 이전 이메일 전송 시간을 세션에서 가져오기 (unixtime)
				Long unixtimeTemp = (Long)request.getSession().getAttribute("EmailAuthTime");

				// 현재 시간의 unixtime
				Long systemTimeTemp = System.currentTimeMillis() / 1000;

				// 현재 시간이 이전 전송 시간을 지나지 않았다면 경고 문구 
				if(unixtimeTemp != null && ((unixtimeTemp+ReSendTime) > systemTimeTemp)) {
					// 이메일 재 전송시간 몇 초 남았는지 전달
					map.put("emailSendTime",""+((unixtimeTemp+ReSendTime)-systemTimeTemp));
					
					return new ModelAndView("jsonView", map);
				}
			}

			/* 인증 코드 생성 */
			StringBuffer buf =new StringBuffer();
			
			// 인증코드 10자리
			buf = randomAuthNumber(10);

			//인증 코드 암호화 SHA-256
			String enUsrCode = EgovFileScrty.encryptPassword(buf.toString(), "Search");
			
			/* 메일 전송 로직 */
			SndngMailVO mailVO = new SndngMailVO();
			mailVO.setDsptchPerson("admin@opensoftlab.kr");				// 발신자
			mailVO.setRecptnPerson(UsrInEmail);									// 수신자
			mailVO.setSj(egovMessageSource.getMessage("cmm3200.success.emailTitle.send"));		// 제목
			mailVO.setEmailCn(
					egovMessageSource.getMessage("cmm3200.success.emailContents.send")
					+buf
					);
			
			// 이메일 전송 객체, 성공 = true / 실패 = false
			boolean sendingMailResult = egovSndngMailService.sndngMail(mailVO);
			
			if(sendingMailResult){
				// 메일 전송 성공 
				map.put("emailSend", egovMessageSource.getMessage("cmm3200.success.emailAuthNum.send"));
			}else{
				// 메일 전송 실패
				map.put("emailSend", egovMessageSource.getMessage("cmm3200.fail.emailAuthNum.send"));
			}
			 
			/* 세션 데이터 등록 */
			
			// 인증 코드
			request.getSession().setAttribute("EmailAuthValue", enUsrCode);
			
			// 이메일 인증 코드, 재 발급 대기 시간
			request.getSession().setAttribute("EmailAuthTime", System.currentTimeMillis() / 1000);
		
			// 전체 세션 만료 시간
			request.getSession().setMaxInactiveInterval(SessionTime*60);
			
		}catch(Exception e){
			Log.error("selectCmm3200MailSend", e);
			return new ModelAndView("jsonView", map);
		}
		
		return new ModelAndView("jsonView", map);
    }
	
	
	
	/**
	 * 인증번호 확인
	 * @param request
	 * @param model
	 * @return
	 * @throws Exception
	 */
	@RequestMapping(value = "/cmm/cmm3000/cmm3200/selectCmm3200SendValCheck.do", produces="text/plain;charset=UTF-8")
	  public ModelAndView selectCmm3200SendValCheck (HttpServletRequest request, ModelMap model) throws Exception   {

		// 값을 넘기기 위한 HasMap
		HashMap<String, String> map = new HashMap<String, String>();
		try{

			// request 파라미터를 map으로 변환
			Map<String, String> param = RequestConvertor.requestParamToMap(request, true);

			/* 파라미터 로그찍기 */    
			if (Log.isDebugEnabled()) {
				Log.debug("\n=====> parameter map : " + param);
			}

			// 사용자 입력 정보 할당
			String InSendVal = param.get("InSendVal");
			

			// 사용자 입력 정보 중 '인증번호' 입력 값이 없을 경우(null)
			 if( "".equals(EgovStringUtil.isNullToString(InSendVal)) ) {
				 
				 
				map.put("authNum", "N");

				return new ModelAndView("jsonView", map);
			}


			/* 세션에 암호화된 인증번호 존재하는지 체크 */
			if(request.getSession() != null && request.getSession().getAttribute("EmailAuthValue") != null){
				map.put("authNum", "Y");

				//사용자가 입력한 인증 번호 암호화
				String enUsrInAN = EgovFileScrty.encryptPassword(InSendVal, "Search");

				
				//세션에 저장된 인증번호
				String enSessionAuthNum = (String)request.getSession().getAttribute("EmailAuthValue");

				if(enSessionAuthNum != null){
					
					//입력한 인증 값과 세션의 인증 값이 같을 경우
					if(enUsrInAN.equals(enSessionAuthNum)){
						
						//생성된 세션 초기화
						request.getSession().removeAttribute("EmailAuthValue");
						request.getSession().removeAttribute("EmailAuthTime");
					}else{
						//입력된 인증번호가 잘못된 경우
						map.put("authNum", "N");
						
						return new ModelAndView("jsonView", map);
					}
				}
			}else{
				//세션에 인증번호 없을 경우 에러
				map.put("authNum", "N");
				
				return new ModelAndView("jsonView", map);
			}
		}catch(Exception e){
			Log.error("selectCmm4001ChkAction", e);
			return new ModelAndView("jsonView", map);
		}
		return new ModelAndView("jsonView", map);
	}
	
	/**
	 * 인증 코드 생성 로직
	 * @param MaxNum : 인증코드 최대 갯수 (default = 10, Max = 30)
	 * @return
	 */
	private StringBuffer randomAuthNumber(int MaxNum) throws Exception   {
		int paramMaxNum = MaxNum;
		/* 인증 코드 생성 로직 
		 * rand: 랜덤 객체
		 * buf : 인증 코드를 저장하려는 StringBuffer
		 * 인증 코드 MaxNum자리 생성 (숫자+문자)
		 * 숫자 0~9
		 * 문자 a~z 
		 */
		if(paramMaxNum < 0 && paramMaxNum > 30){
			paramMaxNum = 10;
		}
		Random rand =new Random();
		StringBuffer buf =new StringBuffer();
		
		//인증 코드는 MaxNum자리
		for(int i=0;i<paramMaxNum;i++){
			// boolean 랜덤 생성 (랜덤으로 true or false 생성)
			if(rand.nextBoolean()){
				// 생성된 값이 true인 경우
				// 문자열 랜덤 생성 a~z
				buf.append((char)((int)(rand.nextInt(26))+97));
			}else{
				// 생성된 값이 false인 경우
				// 숫자 랜덤 생성 0~9
				buf.append((rand.nextInt(10))); 
			}
		}
		return buf;
	}
	
	/**
	 * 회원가입 처리
 	 * 1. 기초 메뉴 정보 세팅			- 메뉴정보(ROOTSYSTEM_GRP 복사해서 새로 생성한 LIC_GRP_ID로 생성)
	 * 2. 사용자 정보 등록			- 사용자정보(가입한 사용자 정보 인서트)
	 * 3. 라이선스 그룹 등록 			- 라이선스그룹 정보(가입한 USR_ID + "_GRP" EX> id = jht1215 이면 jht1215_GRP 로 생성) 
	 * 4. 라이선스 정보 등록   		- 라이선스 정보(가입시 선택한 라이선스 정보를 공통코드를 이용하여 정보를 얻어서 생성)
	 * 5. 공통코드 마스터 등록			- LIC_GRP_ID 기준 기초 공통코드 마스터 데이터(ROOTSYSTEM_GRP 복사하여 새로 생성한 LIC_GRP_ID로 생성)
	 * 6. 공통코드 디테일 등록			- LIC_GRP_ID 기준 기초 공통코드 디테일 데이터(ROOTSYSTEM_GRP 복사하여 새로 생성한 LIC_GRP_ID로 생성)
	 * 7. 메뉴별 접근권한 정보 등록		- LIC_GRP_ID 기준 메뉴별 접근권한 정보 등록(ROOTSYSTEM_PRJ 복사하여 새로 생성한 LIC_GRP_ID로 생성)
	 * 8. 권한그룹 정보 등록       		- LIC_GRP_ID 기준 권한그룹 정보 등록(ROOTSYSTEM_PRJ 복사하여 새로 생성한 LIC_GRP_ID로 생성)
	 * 9. 조직 루트 디렉토리 정보 등록 	- LIC_GRP_ID 기준 조직 루트 디렉토리 정보 등록( 조직 루트디렉토리를 생성 )
	 * @param request
	 * @param response
	 * @param model
	 * @return
	 * @throws Exception
	 */
	@RequestMapping(value="/cmm/cmm3000/cmm3200/insertCmm3200JoinIng.do")
    public ModelAndView insertCmm3200JoinIng(HttpServletRequest request, HttpServletResponse response, ModelMap model ) throws Exception {
		try{
			// request 파라미터를 map으로 변환
        	Map<String, String> paramMap = RequestConvertor.requestParamToMap(request, true);
        	
        	paramMap.put("usrIp", request.getRemoteAddr()); // 사용자 IP
        	
        	// 회원가입시 필요한 정보 등록
        	String successYn = cmm3200Service.insertCmm3200JoinIng(paramMap);
        	
        	//등록 성공 메시지 세팅
        	
        	model.addAttribute("successYn", successYn);
        	model.addAttribute("message", egovMessageSource.getMessage("success.common.insert"));
        	return new ModelAndView("jsonView");
        	
        	//return new ModelAndView("jsonView", usrInfoMap);
        	
		}catch(Exception e){
			Log.error("insertCmm3200JoinIng()", e);
    		model.addAttribute("successYn", "N");
    		model.addAttribute("message", egovMessageSource.getMessage("fail.common.insert"));
    		
    		return new ModelAndView("jsonView");
		}

	}
	
	
	
	
	
}
