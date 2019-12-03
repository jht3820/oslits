package kr.opensoftlab.oslops.cmm.cmm4000.cmm4000.service;

import java.util.List;
import java.util.Map;

import kr.opensoftlab.oslops.com.vo.LicVO;
import kr.opensoftlab.oslops.com.vo.LoginVO;

/**
 * @Class Name : Cmm4000Service.java
 * @Description : Cmm4000Service Business class
 * @Modification Information
 *
 * @author 정형택
 * @since 2013.11.06.
 * @version 1.0
 * @see
 *  
 *  Copyright (C)  All right reserved.
 */
public interface Cmm4000Service {
	
	
	/**
	 * Cmm4000 로그인 처리
	 * @param param - LoginVO
	 * @return 
	 * @exception Exception
	 */
	LoginVO selectCmm4000LoginAction(LoginVO loginVO) throws Exception;	
	
	/**
	 * Cmm4000 라이선스 정보 조회
	 * @param param - LoginVO
	 * @return LicVO
	 * @exception Exception
	 */
	LicVO selectCmm4000LicInfo(LoginVO loginVO) throws Exception;	
	
	/**
	 * Cmm4000 프로젝트 존재 여부 조회
	 * @param param - LoginVO
	 * @return Map
	 * @exception Exception
	 */
	@SuppressWarnings("rawtypes")
	Map selectCmm4000PrjChk(LoginVO loginVO) throws Exception;
	
	/**
	 * Cmm4000 사용자 프로젝트 권한 목록 조회
	 * @param param - Map
	 * @return Map
	 * @exception Exception
	 */
	@SuppressWarnings("rawtypes")
	List selectCmm4000UsrPrjAuthList(Map prjMap) throws Exception;
	
	/**
	 * Cmm4000 권한있는 메뉴 목록 조회
	 * @param param - Map
	 * @return Map
	 * @exception Exception
	 */
	@SuppressWarnings("rawtypes")
	List selectCmm4000MenuList(Map paramMap) throws Exception;

	/**
	 * [팝업]아이디 비밀번호 찾기
	 * Cmm4000 이름과 이메일 존재 여부 조회
	 * @param param - 사용자 이름, 이메일
	 * @return 검색결과 사용자 아이디 값
	 * @exception Exception
	 */
	@SuppressWarnings("rawtypes")
	String selectCmm4000NameEmailChk(Map paramMap) throws Exception;
	
	/**
	 * [팝업]아이디 비밀번호 찾기
	 * Cmm4000 아이디와 이메일 존재 여부 조회
	 * @param param - 사용자 아이디, 이메일
	 * @return 검색결과 사용자 사용 유무 값
	 * @exception Exception
	 */
	@SuppressWarnings("rawtypes")
	String selectCmm4000IdEmailChk(Map paramMap) throws Exception;
	
	/**
	 * [팝업]아이디 비밀번호 찾기
	 * Cmm4000 해당 아이디의 비밀번호 재 설정
	 * @param param - 사용자 아이디, 재 설정 비밀번호
	 * @exception Exception
	 */
	@SuppressWarnings("rawtypes")
	void updateCmm4000NewPwe(Map paramMap) throws Exception;
	
	/**
	 * 사용자의 로그인 일시를 갱신한다.
	 * @param paramMap
	 * @throws Exception
	 */
	public void updateCmm4000RecentLoginDate(LoginVO loginVO) throws Exception;
	
	public List selectCmm4000LoginMainMenuList(Map paramMap) throws Exception;
	
	
	/**
	 * 사용자 최근 로그인 일시 조회(최근 접속일시에 사용)
	 * @param loginVO - loginVO
	 * @return 
	 * @exception Exception
	 */
	@SuppressWarnings("rawtypes")
	public String selectCmm4000RecentLoginDate(LoginVO loginVO) throws Exception;

	String updateCmm4000AccountInit(Map<String, String> param)  throws Exception ;
	
	
	/**
	 * 로그인한 사용자 정보 조회
	 * @param loginVO - loginVO
	 * @return 
	 * @exception Exception
	 */
	@SuppressWarnings("rawtypes")
	public Map selectCmm4000LoginUsrInfo(LoginVO loginVO) throws Exception;

	/**
	 * 사용자의 이전, 현재 접속 IP 정보 조회
	 * @param loginVO - loginVO
	 * @return 
	 * @exception Exception
	 */
	@SuppressWarnings("rawtypes")
	public List selectCmm4000AccessIpInfo(LoginVO loginVO) throws Exception;
	
	/**
	 * 현재 사용자의 비밀번호 체크 
	 * - 비밀번호 만료된 사용자가 로그인 시 비밀번호 변경할 때 현재 비밀번호 체크
	 * @param 
	 * @return 
	 * @exception Exception
	 */
	@SuppressWarnings("rawtypes")
	int selectCmm4000CurtPwChk(Map paramMap) throws Exception;
	
	/**
	 * 비밀번호 만료된 사용자의 비밀번호 변경
	 * @param
	 * @return 
	 * @exception Exception
	 */
	String updateCmm4000PasswordExprInit(Map<String, String> param) throws Exception;
}
