package kr.opensoftlab.oslops.cmm.cmm4000.cmm4000.service.impl;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import kr.opensoftlab.oslops.adm.adm2000.adm2000.service.impl.Adm2000DAO;
import kr.opensoftlab.oslops.adm.adm5000.adm5000.service.impl.Adm5000DAO;
import kr.opensoftlab.oslops.adm.adm5000.adm5200.service.impl.Adm5200DAO;
import kr.opensoftlab.oslops.cmm.cmm4000.cmm4000.service.Cmm4000Service;
import kr.opensoftlab.oslops.com.vo.LicVO;
import kr.opensoftlab.oslops.com.vo.LoginVO;

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
	
	public final static int INITIAL_ACCOUNT_WAIT_MINUTE = 1440;// 초기화 적용후 

	/**
	 * 로그인 처리 - 사용자 정보 및 패스워드 체크.
	 * 1. 사용자가 있는지 체크
	 * 2. 사용자가 있을 경우 사용유무 체크 - 사용유무 사용(01)인지 체크
	 * 3. 사용자의 차단유무 체크 - 차단유무가 차단(02)일 경우 로그인 불가
	 * 4. 사용자가 입력한 비밀번호 체크 - 비밀번호 5회 잘못입력시 차단
	 * 5. 로그인하지 않은 기간 체크 - 3개월 이상 로그인하지 않았을 경우 차단
	 * 6. 비밀번호 만료일 체크 - 6개월이상 비밀번호 변경하지 않았을 경우 로그인시 비밀번호 변경 팝업 호출
	 * 
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

		// 사용자 정보 없을 경우
		if(rtnLoginVO==null){
			rtnLoginVO = new LoginVO();
			rtnLoginVO.setLoginStatus(NO_SEARCH_ID);
		}else{
			// 사용자의 사용유무가 사용(01)일 경우
			if("01".equals(rtnLoginVO.getUseCd())){
				// 사용자 계정 차단여부 체크, 차단일 경우 로그인 불가
				if("02".equals( rtnLoginVO.getBlock() )){
					rtnLoginVO.setLoginStatus(ACCOUNT_LOCK);
				// 계정이 차단이 아닐경우
				}else{
					Map<String, String> paramMap = new HashMap<String, String>();
					paramMap.put("licGrpId", rtnLoginVO.getLicGrpId());
			    	paramMap.put("usrId", rtnLoginVO.getUsrId());	
			    	
					int pwFailCnt =0;
					
					// 비밀번호 잘못 입력 시
					if(! enUsrPw.equals( rtnLoginVO.getUsrPw() ) ){
						// 비밀번호 실패 횟수를 가져온다
						pwFailCnt = rtnLoginVO.getPwFailCnt();
						pwFailCnt += 1;
						// 비밀번호 실패 횟수 5회 이상일 경우
						if(pwFailCnt >=  MAX_WRONG_PASSWORD_LIMIT_COUNT){
							// 사용자 차단여부값 02로 변경
							rtnLoginVO.setBlock("02"); // 01 : 정상  02 : 차단 
							paramMap.put("blkLog", "비밀번호 입력 오류 횟수 초과");	
						}else{
							rtnLoginVO.setBlock("01"); // 01 : 정상  02 : 차단 
						}
						// 비밀번호 실패횟수, Login Status값 세팅
						rtnLoginVO.setPwFailCnt(pwFailCnt);
						rtnLoginVO.setLoginStatus(WRONG_PASSWORD);
					// 정상적인 비밀번호 입력 시	
					}else{

						// 로그인 하지 않은 기간 체크 
				    	String loginExprYN = cmm4000DAO.selectCmm4000LastLoginChk(rtnLoginVO);

						// 비밀번호 만료일
						int limitDay = adm5200DAO.selectAdm5200PwChangeDateCheck(paramMap);
						
						// 로그인하지 않은 기간이 3개월 이상일 경우
						if( "Y".equals(loginExprYN) ){
							// 차단여부 값 02(차단)으로 세팅
							rtnLoginVO.setBlock("02");
							rtnLoginVO.setLoginStatus(NOT_USED_3_MONTHS);
							paramMap.put("blkLog", "시스템에 로그인하지 않은지 3개월 이상으로 차단");
						// 비밀번호 사용 만료되었을 경우(6개월간 변경하지 않았을 경우)
						}else if( limitDay <= 0 ){	
							// its와 동일하게 변경 - 비밀번호 만료이후 비밀번호 재설정 팝업 닫아도 차단안되도록
							//rtnLoginVO.setBlock("02");
							rtnLoginVO.setLoginStatus(PW_EXPORED);
							paramMap.put("blkLog", "비밀번호 사용기간 만료로 차단");
						}else {
							// 비밀번호 초기화를 했고, 초기화 대기시간 초과했을 경우
							if("Y".equals( rtnLoginVO.getIniYn() ) && rtnLoginVO.getModMin() >= INITIAL_ACCOUNT_WAIT_MINUTE ){
								rtnLoginVO.setBlock("02"); // 01 : 정상, 02 : 차단
								paramMap.put("blkLog", "비밀번호 초기화 재접속 대기시간 초과");
								rtnLoginVO.setLoginStatus(INITIAL_ACCOUNT_WAIT_MINUTE_AFTER);								
							}else{
								// 패스워드 암호화
								String nowUsrPw = EgovFileScrty.encryptPassword(rtnLoginVO.getUsrId(),rtnLoginVO.getUsrId());
								
								// 비밀번호 초기화 상태일 경우
								if(nowUsrPw.equals(rtnLoginVO.getUsrPw()) ){
									// Login Status 세팅
									rtnLoginVO.setLoginStatus(INITIAL_PASSWORD_CHANGE);
								// 비밀번호 초기화가 아닐경우	
								}else{
									pwFailCnt = 0;
									rtnLoginVO.setPwFailCnt(pwFailCnt);
									rtnLoginVO.setBlock("01"); // 01 : 정상, 02 : 차단
									rtnLoginVO.setLoginStatus(LOGIN_SUCCESS);
								}
							}
						}
					}
					
					// 비밀번호 초기화 상태가 아닐경우
					if(rtnLoginVO.getLoginStatus() != INITIAL_PASSWORD_CHANGE){
						// 수정자 ID, IP 세팅
						rtnLoginVO.setModifyUsrId(rtnLoginVO.getUsrId());
						rtnLoginVO.setModifyUsrIp(loginVO.getModifyUsrIp());
						
						// 사용자의 차단여부가 02(차단)일 경우
						if("02".equals( rtnLoginVO.getBlock()) ){
							paramMap.put("regUsrId", rtnLoginVO.getUsrId());
							paramMap.put("regUsrIp", loginVO.getModifyUsrIp());
							// 차단로그 등록
							adm2000DAO.insertAdm2100BlockLog(paramMap);				  
						}
						// 비밀번호 실패횟수 수정
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

	@SuppressWarnings("rawtypes")
	@Override
	public List selectCmm4000LoginMainMenuList(Map paramMap) throws Exception {
		return cmm4000DAO.selectCmm4000LoginMainMenuList(paramMap);
	}

	/**
	 * 사용자 최근 로그인 일시 조회(최근 접속일시에 사용)
	 * @param loginVO - loginVO
	 * @return 
	 * @exception Exception
	 */
	@Override
	public String selectCmm4000RecentLoginDate(LoginVO loginVO) throws Exception{
		return cmm4000DAO.selectCmm4000RecentLoginDate(loginVO);
	}

	/**
	 * 사용자 비밀번호 초기화 후 비밀번호 재설정
	 * @param 
	 * @return 
	 * @exception Exception
	 */
	@Override
	public String updateCmm4000AccountInit(Map<String, String> param) throws Exception {
		
		String usrId 		= (String) param.get("usrId");
		String usrPw 		= (String) param.get("usrPw");
		
		String enUsrPw 	= EgovFileScrty.encryptPassword(usrPw, usrId);	// 패스워드 암호화
		
		param.put("enUsrPw", enUsrPw);
		param.put("iniYn", "N");
		if(adm2000DAO.updateAdm2000AccountInit(param) > 0 ){
			
			// 비밀번호 초기화 후 로그인할 비밀번호 변경 => 사용자 변경이력으로 남김
			param.put("logState", "U");
			param.put("pwChangeState", "Y");
			adm5200DAO.insertAdm5200UserChangeLog(param);
			
			return enUsrPw;
		}
		return "";
	}
	
	/**
	 * 비밀번호 만료된 사용자의 비밀번호 변경
	 * @param
	 * @return 
	 * @exception Exception
	 */
	@Override
	public String updateCmm4000PasswordExprInit(Map<String, String> param) throws Exception {

		String usrId 		= (String) param.get("usrId");
		String usrPw 		= (String) param.get("usrPw");
		
		String enUsrPw 	= EgovFileScrty.encryptPassword(usrPw, usrId);	// 패스워드 암호화
		
		// 변경한 비밀번호 및 차단여부 세팅
		param.put("enUsrPw", enUsrPw);
		param.put("block", "01");
		
		if(adm2000DAO.updateAdm2000PasswordExprInit(param) > 0 ){
			
			// 비밀번호 초기화 후 로그인할 비밀번호 변경 => 사용자 변경이력으로 남김
			param.put("logState", "U");
			param.put("pwChangeState", "Y");
			adm5200DAO.insertAdm5200UserChangeLog(param);
			
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
	
	/**
	 * 사용자의 이전, 현재 접속 IP 정보 조회
	 * @param loginVO - loginVO
	 * @return 
	 * @exception Exception
	 */
	@SuppressWarnings("rawtypes")
	@Override
	public List selectCmm4000AccessIpInfo(LoginVO loginVO) throws Exception {
		return cmm4000DAO.selectCmm4000AccessIpInfo(loginVO);
	}
	
	/**
	 * 현재 사용자의 비밀번호 체크 
	 * - 비밀번호 만료된 사용자가 로그인 시 비밀번호 변경할 때 현재 비밀번호 체크
	 * @param 
	 * @return 
	 * @exception Exception
	 */
	@SuppressWarnings({ "rawtypes", "unchecked"})
	public int selectCmm4000CurtPwChk(Map paramMap) throws Exception{
		// 사용자 ID
		String usrId = (String)paramMap.get("usrId");
		// 사용자의 현재 비밀번호
		String usrPw = (String)paramMap.get("usrCurtPw");
		
		// 비밀번호 암호화
		String enUsrPw 	= EgovFileScrty.encryptPassword(usrPw, usrId);	// 패스워드 암호화
		
		paramMap.put("usrCurtPw", enUsrPw);
		
		// 현재 사용자 비밀번호 체크
		int pwChkVal = cmm4000DAO.selectCmm4000CurtPwChk(paramMap);
		
		return pwChkVal;
	}
}

