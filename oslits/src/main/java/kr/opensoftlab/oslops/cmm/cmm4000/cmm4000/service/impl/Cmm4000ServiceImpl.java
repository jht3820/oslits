package kr.opensoftlab.oslits.cmm.cmm4000.cmm4000.service.impl;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import kr.opensoftlab.oslits.adm.adm2000.adm2000.service.impl.Adm2000DAO;
import kr.opensoftlab.oslits.adm.adm5000.adm5000.service.impl.Adm5000DAO;
import kr.opensoftlab.oslits.adm.adm5000.adm5200.service.impl.Adm5200DAO;
import kr.opensoftlab.oslits.cmm.cmm4000.cmm4000.service.Cmm4000Service;
import kr.opensoftlab.oslits.com.vo.LicVO;
import kr.opensoftlab.oslits.com.vo.LoginVO;
import kr.opensoftlab.sdf.util.CalendarUtil;

import org.springframework.stereotype.Service;

import egovframework.com.utl.sim.service.EgovFileScrty;
import egovframework.rte.fdl.cmmn.EgovAbstractServiceImpl;

/**
 * @Class Name : Cm1000ServiceImpl.java
 * @Description : Cm1000ServiceImpl Business Implement class
 * @Modification Information
 *
 * @author 정형택
 * @since 2013.11.06.
 * @version 1.0
 * @see
 *  
 *  Copyright (C)  All right reserved.
 */

@Service("cmm4000Service")
public class Cmm4000ServiceImpl extends EgovAbstractServiceImpl implements Cmm4000Service {

	/** Cmm4000DAO DI */
	@Resource(name="cmm4000DAO")
	private Cmm4000DAO cmm4000DAO;
	
	/** Adm5000DAO DI */
	@Resource(name="adm5000DAO")
	private Adm5000DAO adm5000DAO;
	
	/** Adm5200DAO DI */
	@Resource(name="adm5200DAO")
	private Adm5200DAO adm5200DAO;
	
	@Resource(name="adm2000DAO")
	private Adm2000DAO adm2000DAO;
	
	
	public final static int LOGIN_SUCCESS = 1000;// 로그인 성공
	
	public final static int NO_SEARCH_ID = 1001;// 아이디 없음
	
	public final static int ACCOUNT_LOCK = 1002;// 계정 잠금
	
	public final static int WRONG_PASSWORD = 1003;// 패스워드 입력 오류
	
	public final static int NO_USE_USER = 1004;// 사용여부 허가 안된 유저
	
	public final static int DUPULICATE_USER_LOGIN = 1005;// 중복로그인 확인후 접속
	
	public final static int NOT_USED_3_MONTHS = 1006;// 접속한지 3개월이 넘은 유저
	
	public final static int PW_EXPORED = 1007;// 비밀번호 사용기간 만료된 유저
	
	public final static int INITIAL_ACCOUNT_WAIT_MINUTE_AFTER = 1008;// 계정 초기화 처리 이후 대기시간 지남
	
	public final static int INITIAL_PASSWORD_CHANGE = 1009;// 비밀번호 초기화 이후 최초접속 비밀번호 변경
	
	public final static int MAX_WRONG_PASSWORD_LIMIT_COUNT = 5;// 패스워드 입력 오류 제한
	
	public final static int INITIAL_ACCOUNT_WAIT_MINUTE = 10;// 초기화 적용후 

	/**
	 * 로그인 처리 - 사용자 정보 및 패스워드 체크.
	 * @param param - 조회할 정보가 담긴 VO
	 * @return 사용자 정보 목록
	 * @exception Exception
	 */
	public LoginVO selectCmm4000LoginAction(LoginVO loginVO) throws Exception {

		// 1. 입력한 비밀번호를 암호화한다.
		String enUsrPw = EgovFileScrty.encryptPassword(loginVO.getUsrPw(), loginVO.getUsrId());
		loginVO.setUsrPw(enUsrPw);

		// 2. 아이디와 암호화된 비밀번호가 DB와 일치하는지 확인한다.
		LoginVO rtnLoginVO = cmm4000DAO.selectCmm4000LoginAction(loginVO);

		/* 아이디 없음  */
		if(rtnLoginVO==null){
			rtnLoginVO = new LoginVO();
			rtnLoginVO.setLoginStatus(NO_SEARCH_ID);
		}else{
			if("01".equals(rtnLoginVO.getUseCd())){
				/* 계정 잠금  */
				if("02".equals( rtnLoginVO.getBlock() )){
					rtnLoginVO.setLoginStatus(ACCOUNT_LOCK);
				}else{

			    	/* 로그인 하지 않은 기간 체크 */
			    	String loginExprYN = cmm4000DAO.selectCmm4000LastLoginChk(rtnLoginVO);

					/* 비밀번호 만료일 조회에 사용할 map */
					Map<String, String> paramMap = new HashMap<String, String>();
					paramMap.put("licGrpId", rtnLoginVO.getLicGrpId());
			    	paramMap.put("usrId", rtnLoginVO.getUsrId());	
			    	String nowUsrPw 	= EgovFileScrty.encryptPassword(rtnLoginVO.getUsrId(),rtnLoginVO.getUsrId());	// 패스워드 암호화
					/* 비밀번호 만료일 */
					int limitDay = adm5200DAO.selectAdm5200PwChangeDateCheck(paramMap);
					
					/* 로그인하지 않은지 3개월 이상 지나면 차단 */
					if( "Y".equals(loginExprYN) ){
						rtnLoginVO.setBlock("02");
						rtnLoginVO.setLoginStatus(NOT_USED_3_MONTHS);
					}else if( limitDay <= 0 ){	/* 비밀번호 사용 만료일이 되면 차단 */
						rtnLoginVO.setBlock("02");
						rtnLoginVO.setLoginStatus(PW_EXPORED);
					}else{
						
						int pwFailCnt =0;
						/* 패스워드 입력 실패  */
						if(! enUsrPw.equals( rtnLoginVO.getUsrPw() ) ){
							pwFailCnt = rtnLoginVO.getPwFailCnt();
							pwFailCnt += 1;
							if(pwFailCnt >=  MAX_WRONG_PASSWORD_LIMIT_COUNT){
								rtnLoginVO.setBlock("02"); /* 01 :  성공  02 : 실패  */
								paramMap.put("blkLog", "비밀번호 입력 오류 횟수 초과");	
							}else{
								rtnLoginVO.setBlock("01"); /* 01 :  성공  02 : 실패  */
							}
							rtnLoginVO.setPwFailCnt(pwFailCnt);
							rtnLoginVO.setLoginStatus(WRONG_PASSWORD);
						}else{
							if("Y".equals( rtnLoginVO.getIniYn() ) && rtnLoginVO.getModMin() >= INITIAL_ACCOUNT_WAIT_MINUTE ){
								rtnLoginVO.setBlock("02"); /* 01 :  성공  02 : 실패  */
								paramMap.put("blkLog", "비밀번호 초기화 재접속 대기시간 초과");
								rtnLoginVO.setLoginStatus(INITIAL_ACCOUNT_WAIT_MINUTE_AFTER);								
							}else{
								/**
								 * 비밀번호가 초기화 상태가 
								 */
								if(nowUsrPw.equals(rtnLoginVO.getUsrPw()) ){
									rtnLoginVO.setLoginStatus(INITIAL_PASSWORD_CHANGE);
								}else{
									pwFailCnt = 0;
									rtnLoginVO.setPwFailCnt(pwFailCnt);
									rtnLoginVO.setBlock("01"); /* 01 :  성공  02 : 실패  */
									rtnLoginVO.setLoginStatus(LOGIN_SUCCESS);
								}
							}
						}
					}
					/**
					 * 비밀번호가 초기화 상태가 아닌경우 처리
					 */
					if(rtnLoginVO.getLoginStatus() != INITIAL_PASSWORD_CHANGE){
						rtnLoginVO.setModifyUsrId(rtnLoginVO.getUsrId());
						rtnLoginVO.setModifyUsrIp(loginVO.getModifyUsrIp());
						if("02".equals( rtnLoginVO.getBlock()) ){
							paramMap.put("regUsrId", rtnLoginVO.getUsrId());
							paramMap.put("regUsrIp", loginVO.getModifyUsrIp());
							adm2000DAO.insertAdm2100BlockLog(paramMap);				  
						}
						cmm4000DAO.updateCmm4000LoginCnt(rtnLoginVO);
					}
					
				}
			}else{
				rtnLoginVO.setLoginStatus(NO_USE_USER);
			}
		}


		return rtnLoginVO;
	}

	/**
	 * Cmm4000 라이선스 정보 조회
	 * @param param - 로그인 VO
	 * @return LicVO
	 * @exception Exception
	 */
	public LicVO selectCmm4000LicInfo(LoginVO loginVO) throws Exception{
		return cmm4000DAO.selectCmm4000LicInfo(loginVO);
	}

	/**
	 * Cmm4000 프로젝트 존재 여부 조회
	 * @param param - 로그인 VO
	 * @return Map
	 * @exception Exception
	 */
	@SuppressWarnings("rawtypes")
	public Map selectCmm4000PrjChk(LoginVO loginVO) throws Exception{
		return cmm4000DAO.selectCmm4000PrjChk(loginVO);
	}

	/**
	 * Cmm4000 사용자 프로젝트 권한 목록 조회
	 * @param param - Map
	 * @return Map
	 * @exception Exception
	 */
	@SuppressWarnings("rawtypes")
	public List selectCmm4000UsrPrjAuthList(Map prjMap) throws Exception{
		return (List) cmm4000DAO.selectCmm4000UsrPrjAuthList(prjMap);
	}

	/**
	 * Cmm4000 권한있는 메뉴 목록 조회
	 * @param param - Map
	 * @return Map
	 * @exception Exception
	 */
	@SuppressWarnings("rawtypes")
	public List selectCmm4000MenuList(Map paramMap) throws Exception{
		return (List) cmm4000DAO.selectCmm4000MenuList(paramMap);
	}

	/**
	 * [팝업]아이디 비밀번호 찾기
	 * Cmm4000 이름과 이메일 존재 여부 조회
	 * @param param - 사용자 이름, 이메일
	 * @return 검색결과 사용자 아이디 값
	 * @exception Exception
	 */
	@SuppressWarnings("rawtypes")
	public String selectCmm4000NameEmailChk(Map paramMap) throws Exception {
		return cmm4000DAO.selectCmm4000NameEmailChk(paramMap);
	}

	/**
	 * [팝업]아이디 비밀번호 찾기
	 * Cmm4000 아이디와 이메일 존재 여부 조회
	 * @param param - 사용자 아이디, 이메일
	 * @return 검색결과 사용자 사용 유무 값
	 * @exception Exception
	 */
	@SuppressWarnings("rawtypes")
	public String selectCmm4000IdEmailChk(Map paramMap) throws Exception{
		return cmm4000DAO.selectCmm4000IdEmailChk(paramMap);
	}
	/**
	 * [팝업]아이디 비밀번호 찾기
	 * Cmm4000 해당 아이디의 비밀번호 재 설정
	 * @param param - 사용자 아이디, 재 설정 비밀번호
	 * @exception Exception
	 */
	@SuppressWarnings("rawtypes")
	public void updateCmm4000NewPwe(Map paramMap) throws Exception{
		cmm4000DAO.updateCmm4000NewPw(paramMap);
	}

	/**
	 * 사용자의 로그인 일시를 갱신한다.
	 *  
	 * @param paramMap
	 * @throws Exception
	 */
	@Override
	public void updateCmm4000RecentLoginDate(LoginVO loginVO) throws Exception {
		cmm4000DAO.updateCmm4000RecentLoginDate(loginVO);
	}

	@Override
	public List selectCmm4000LoginMainMenuList(Map paramMap) throws Exception {
		// TODO Auto-generated method stub
		return cmm4000DAO.selectCmm4000LoginMainMenuList(paramMap);
	}

	/**
	 * 사용자 최근 로그인 일시 조회(최근 접속일시에 사용)
	 * @param loginVO - loginVO
	 * @return 
	 * @exception Exception
	 */
	@SuppressWarnings("rawtypes")
	@Override
	public String selectCmm4000RecentLoginDate(LoginVO loginVO) throws Exception{
		return cmm4000DAO.selectCmm4000RecentLoginDate(loginVO);
	}

	@Override
	public String updateCmm4000AccountInit(Map<String, String> param) throws Exception {
		// TODO Auto-generated method stub
		String usrId 		= (String) param.get("usrId");
		String usrPw 		= (String) param.get("usrPw");
		
		String enUsrPw 	= EgovFileScrty.encryptPassword(usrPw, usrId);	// 패스워드 암호화
		
		param.put("enUsrPw", enUsrPw);
		param.put("iniYn", "N");
		if(adm2000DAO.updateAdm2000AccountInit(param) > 0 ){
			return enUsrPw;
		}
		return "";
	}

	/**
	 * 로그인한 사용자 정보 조회
	 * @param loginVO - loginVO
	 * @return 
	 * @exception Exception
	 */
	@SuppressWarnings("rawtypes")
	@Override
	public Map selectCmm4000LoginUsrInfo(LoginVO loginVO) throws Exception {
		return cmm4000DAO.selectCmm4000LoginUsrInfo(loginVO);
	}
}

