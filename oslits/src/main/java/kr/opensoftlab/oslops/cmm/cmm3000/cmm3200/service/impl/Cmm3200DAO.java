package kr.opensoftlab.oslops.cmm.cmm3000.cmm3200.service.impl;

import java.util.Map;

import kr.opensoftlab.oslops.com.dao.ComOslitsAbstractDAO;

import org.springframework.stereotype.Repository;

/**
 * @Class Name : Cmm3200DAO.java
 * @Description : Cmm3200DAO DAO Class
 * @Modification Information
 *
 * @author 김정환
 * @since 2016.01.16
 * @version 1.0
 * @see
 *  
 *  Copyright (C)  All right reserved.
 */

@Repository("cmm3200DAO")
public class Cmm3200DAO extends ComOslitsAbstractDAO {

	/**
	 * 아이디 중복 체크
	 * @param paramMap
	 * @return
	 * @throws Exception
	 */
	@SuppressWarnings({ "deprecation", "rawtypes" })
	public int selectCmm3200idChk(Map paramMap)  throws Exception{
		return (int) getSqlMapClientTemplate().queryForObject("cmm3200DAO.selectCmm3200idChk", paramMap);
	}

	/**
	 * 초기메뉴정보 세팅
	 * @param paramMap
	 * @return 
	 * @throws Exception
	 */
	@SuppressWarnings("rawtypes")
	public String insertAdm1000MenuBegin(Map paramMap)  throws Exception{
		return (String) insert("cmm3200DAO.insertAdm1000MenuBegin", paramMap);
	}

	/**
	 * 사용자 정보 등록
	 * @param paramMap
	 * @throws Exception
	 */
	@SuppressWarnings("rawtypes")
	public String insertAdm2000UserInfo(Map paramMap)  throws Exception{
		return (String) insert("cmm3200DAO.insertAdm2000UserInfo", paramMap);
	}

	/**
	 * 라이선스 그룹 등록
	 * @param paramMap
	 * @throws Exception
	 */
	@SuppressWarnings("rawtypes")
	public String insertAdm3100LicenceGroup(Map paramMap)  throws Exception{
		return (String) insert("cmm3200DAO.insertAdm3100LicenceGroup", paramMap);
	}

	/**
	 * 라이선스 정보 등록
	 * @param paramMap
	 * @throws Exception
	 */
	@SuppressWarnings("rawtypes")
	public String insertAdm3000LicenceInfo(Map paramMap)  throws Exception{
		return (String) insert("cmm3200DAO.insertAdm3000LicenceInfo", paramMap);
	}

	/**
	 * 공통코드 마스터 등록
	 * @param paramMap
	 * @throws Exception
	 */
	@SuppressWarnings("rawtypes")
	public String insertAdm4000CodeM(Map paramMap)  throws Exception{
		return (String) insert("cmm3200DAO.insertAdm4000CodeM", paramMap);
	}
	
	/**
	 * 공통코드 디테일 등록
	 * @param paramMap
	 * @throws Exception
	 */
	@SuppressWarnings("rawtypes")
	public String insertAdm4100CodeD(Map paramMap)  throws Exception{
		return (String) insert("cmm3200DAO.insertAdm4100CodeD", paramMap);
	}
	
	/**
	 * 메뉴별 접근권한 정보 등록
	 * @param paramMap
	 * @throws Exception
	 */
	@SuppressWarnings("rawtypes")
	public String insertAdm4100MenuAuthInfo(Map paramMap)  throws Exception{
		return (String) insert("cmm3200DAO.insertAdm4100MenuAuthInfo", paramMap);
	}

	/**
	 * 권한그룹 정보 등록
	 * @param paramMap
	 * @throws Exception
	 */
	@SuppressWarnings("rawtypes")
	public String insertAdm4100AuthGroupInfo(Map paramMap)  throws Exception{
		return (String) insert("cmm3200DAO.insertAdm4100AuthGroupInfo", paramMap);
	}
	
	/**
	 * 권한그룹 정보 등록
	 * @param paramMap
	 * @throws Exception
	 */
	@SuppressWarnings("rawtypes")
	public String insertAdm4100RootDeptInfo(Map paramMap)  throws Exception{
		return (String) insert("cmm3200DAO.insertAdm4100RootDeptInfo", paramMap);
	}


	


}
