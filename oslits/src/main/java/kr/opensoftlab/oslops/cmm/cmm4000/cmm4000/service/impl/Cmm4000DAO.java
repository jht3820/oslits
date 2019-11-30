package kr.opensoftlab.oslops.cmm.cmm4000.cmm4000.service.impl;

import java.util.List;
import java.util.Map;

import kr.opensoftlab.oslops.com.dao.ComOslitsAbstractDAO;
import kr.opensoftlab.oslops.com.vo.LicVO;
import kr.opensoftlab.oslops.com.vo.LoginVO;

import org.springframework.stereotype.Repository;

/**
 * @Class Name : Cm1000DAO.java
 * @Description : Cm1000DAO DAO Class
 * @Modification Information
 *
 * @author 정형택
 * @since 2013.11.06.
 * @version 1.0
 * @see
 *  
 *  Copyright (C)  All right reserved.
 */

@Repository("cmm4000DAO")
public class Cmm4000DAO extends ComOslitsAbstractDAO {
	
	/**
	 * selectCm1000List 목록을 조회한다.
	 * @param searchMap - 조회할 정보가 담긴 Map
	 * @return EGMIS_TBL_INFO 목록
	 * @exception Exception
	 */
    @SuppressWarnings("rawtypes")
	public Map selectCmm4000UsrList(Map param) throws Exception {
        return (Map) select("selectCmm4000UsrInfoCheck", param);    
    }
    
	/**
	 * selectCmm4000LoginAction 목록을 조회한다.
	 * @param 
	 * @return 
	 * @exception Exception
	 */
	public LoginVO selectCmm4000LoginAction(LoginVO loginVO) throws Exception {
        return (LoginVO) select("cmm4000DAO.selectCmm4000LoginAction", loginVO);    
    }    
	
	/**
	 * selectCmm4000SuperLoginAction 목록을 조회한다.
	 * @param 
	 * @return 
	 * @exception Exception
	 */
	public LoginVO selectCmm4000SuperLoginAction(LoginVO loginVO) throws Exception {
		return (LoginVO) select("cmm4000DAO.selectCmm4000SuperLoginAction", loginVO);    
	}    
	
	/**
	 * Cmm4000 라이선스 정보 조회
	 * @param param - 로그인 VO
	 * @return LicVO
	 * @exception Exception
	 */
	public LicVO selectCmm4000LicInfo(LoginVO loginVO) throws Exception{
		return (LicVO) select("cmm4000DAO.selectCmm4000LicInfo", loginVO);
	}
	
	/**
	 * Cmm4000 프로젝트 존재 여부 조회
	 * @param param - 로그인 VO
	 * @return Map
	 * @exception Exception
	 */
	@SuppressWarnings("rawtypes")
	public Map selectCmm4000PrjChk(LoginVO loginVO) throws Exception{
		return (Map) select("cmm4000DAO.selectCmm4000PrjChk", loginVO);
	}
	
	
	/**
	 * Cmm4000 사용자 프로젝트 권한 목록 조회
	 * @param param - Map
	 * @return Map
	 * @exception Exception
	 */
	@SuppressWarnings("rawtypes")
	public List selectCmm4000UsrPrjAuthList(Map prjMap) throws Exception{
		return (List) list("cmm4000DAO.selectCmm4000UsrPrjAuthList", prjMap);
	}
	
	/**
	 * Cmm4000 권한있는 메뉴 목록 조회
	 * @param param - Map
	 * @return Map
	 * @exception Exception
	 */
	@SuppressWarnings("rawtypes")
	public List selectCmm4000MenuList(Map paramMap) throws Exception{
		return (List) list("cmm4000DAO.selectCmm4000MenuList", paramMap);
	}
	/**
	 * [팝업]아이디 비밀번호 찾기
	 * Cmm4001 이름과 이메일 존재 여부 조회
	 * @param param - 사용자 이름, 이메일
	 * @return 검색결과 사용자 아이디 값
	 * @exception Exception
	 */
	@SuppressWarnings("rawtypes")
	public String selectCmm4000NameEmailChk(Map paramMap) throws Exception{
		return (String) select("cmm4000DAO.selectCmm4000NameEmailChk", paramMap);
	}
	
	/**
	 * [팝업]아이디 비밀번호 찾기
	 * Cmm4000 아이디와 이메일 존재 여부 조회
	 * @param param - 사용자 아이디, 이메일
	 * @return 검색결과 사용자 사용 유무 값
	 * @exception Exception
	 */
	@SuppressWarnings("rawtypes")
	String selectCmm4000IdEmailChk(Map paramMap) throws Exception{
		return (String) select("cmm4000DAO.selectCmm4000IdEmailChk", paramMap);
	}
	
	/**
	 * [팝업]아이디 비밀번호 찾기
	 * Cmm4000 해당 아이디의 비밀번호 재 설정
	 * @param param - 사용자 아이디, 재 설정 비밀번호
	 * @exception Exception
	 */
	@SuppressWarnings("rawtypes")
	public void updateCmm4000NewPw(Map paramMap) throws Exception{
		update("cmm4000DAO.updateCmm4000NewPw",paramMap);
	}
	
	/**
	 * 로그인 횟수와 차단여부를 갱신한다.
	 *  
	 * @param loginVO
	 * @throws Exception
	 */
	@SuppressWarnings("rawtypes")
	public void updateCmm4000LoginCnt(LoginVO loginVO) throws Exception{
		update("cmm4000DAO.updateCmm4000LoginCnt",loginVO);
	}
	
	
	/**
	 * 사용자의 로그인 일시를 갱신한다.
	 * @param loginVO
	 * @throws Exception
	 */
	public void updateCmm4000RecentLoginDate(LoginVO loginVO) throws Exception{
		update("cmm4000DAO.updateCmm4000RecentLoginDate",loginVO);
	}
	
	/**
	 * 사용자가 로그인하지 않은 기간이 3개월 이상인지 체크한다.
	 * @param loginVO
	 * @throws Exception
	 */
	public String selectCmm4000LastLoginChk(LoginVO loginVO) throws Exception{
		return (String) select("cmm4000DAO.selectCmm4000LastLoginChk",loginVO);
	}
	
	/**
	 * 
	 * 로그인후 메인 화면 조회
	 *  
	 * @param paramMap
	 * @return
	 * @throws Exception
	 */
	public List selectCmm4000LoginMainMenuList(Map paramMap) throws Exception{
		return (List) list("cmm4000DAO.selectCmm4000LoginMainMenuList", paramMap);
	}
	
	
	/**
	 * 사용자 최근 로그인 일시 조회(최근 접속일시에 사용)
	 * @param loginVO - loginVO
	 * @return 
	 * @exception Exception
	 */
	@SuppressWarnings("rawtypes")
	public String selectCmm4000RecentLoginDate(LoginVO loginVO) throws Exception{
		return (String) select("cmm4000DAO.selectCmm4000RecentLoginDate", loginVO);
	}
	
	
	/**
	 * 로그인한 사용자 정보 조회
	 * @param loginVO - loginVO
	 * @return 
	 * @exception Exception
	 */
	@SuppressWarnings("rawtypes")
	public Map selectCmm4000LoginUsrInfo(LoginVO loginVO) throws Exception {
		return (Map) select("cmm4000DAO.selectCmm4000LoginUsrInfo", loginVO);
	}
	
	/**
	 * 사용자의 이전, 현재 접속 IP 정보 조회
	 * @param loginVO - loginVO
	 * @return 
	 * @exception Exception
	 */
	@SuppressWarnings("rawtypes")
	public List selectCmm4000AccessIpInfo(LoginVO loginVO) throws Exception{
		return (List) list("cmm4000DAO.selectCmm4000AccessIpInfo", loginVO);
	}
	
	/**
	 * 현재 사용자의 비밀번호 체크 
	 * - 비밀번호 만료된 사용자가 로그인 시 비밀번호 변경할 때 현재 비밀번호 체크
	 * @param 
	 * @return 
	 * @exception Exception
	 */
	@SuppressWarnings("rawtypes")
	public int selectCmm4000CurtPwChk(Map paramMap) throws Exception{
		return (int)select("cmm4000DAO.selectCmm4000CurtPwChk", paramMap);
	}	
}
