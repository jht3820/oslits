package kr.opensoftlab.oslops.stm.stm3000.stm3100.service.impl;

import java.util.List;
import java.util.Map;

import kr.opensoftlab.oslops.com.dao.ComOslitsAbstractDAO;
import kr.opensoftlab.oslops.stm.stm3000.stm3100.vo.Stm3100VO;

import org.springframework.stereotype.Repository;

/**
 * @Class Name : Stm3100DAO.java
 * @Description : Stm3100DAO DAO Class
 * @Modification Information
 *
 * @author 공대영
 * @since 2018.07.19
 * @version 1.0
 * @see
 *  
 *  Copyright (C)  All right reserved.
 */

@Repository("stm3100DAO")
public class Stm3100DAO extends ComOslitsAbstractDAO {
	
	
	/**
	 * Stm3100 프로젝트에 배정된 Jenkins Job 목록을 조회한다. 
	 * @param Stm3100VO
	 * @return List - 프로젝트에 배정된 Jenkins Job 목록
	 * @throws Exception
	 */
	@SuppressWarnings({ "unchecked", "rawtypes" })
	public List<Stm3100VO> selectStm3100JenkinsProjectList(Stm3100VO stm3100VO) throws Exception{
		return (List) list("stm3100DAO.selectStm3100JenkinsProjectList", stm3100VO);
	}
	
	/**
	 * Stm3100 프로젝트에 배정된 Jenkins Job 목록 총 건수를 조회한다. (그리드 페이징 처리) 
	 * @param Stm3100VO
	 * @return
	 * @throws Exception
	 */
	public int selectStm3100JenkinsProjectListCnt(Stm3100VO stm3100VO) throws Exception {
		return (Integer) select("stm3100DAO.selectStm3100JenkinsProjectListCnt", stm3100VO);
	}

	/**
	 * Stm3100 프로젝트에 배정 설정  Jenkins Job 목록을 조회한다. 
	 * @param paramMap
	 * @return List - 프로젝트에 배정 설정 Jenkins Job 목록
	 * @throws Exception
	 */
	@SuppressWarnings("rawtypes")
	public List selectStm3100JenkinsProjectAuthList(Stm3100VO stm3100VO) throws Exception {
		return (List) list("stm3100DAO.selectStm3100JenkinsProjectAuthList", stm3100VO);
	}
		
	/**
	 * Stm3100프로젝트에 배정 설정  Jenkins Job 목록 총 건수를 조회한다. (그리드 페이징 처리) 
	 * @param paramMap
	 * @return
	 * @throws Exception
	 */
	public int selectStm3100JenkinsProjectAuthListCnt(Stm3100VO stm3100VO) throws Exception {
		return (Integer) select("stm3100DAO.selectStm3100JenkinsProjectAuthListCnt", stm3100VO);
	}
	
	/**
	 * Stm3100 프로젝트에 Jenkins Job을 배정한다.
	 * @param Stm3100VO
	 * @return
	 * @throws Exception
	 */
	@SuppressWarnings("rawtypes")
	public void insertStm3100ProjectAddJob(Map paramMap) throws Exception{
		insert("stm3100DAO.insertJen1200ProjectAddJob",paramMap);
	}

	/**
	 * Stm3100 프로젝트에 Jenkins Job을 배정제외 한다.
	 * @param Stm3100VO
	 * @return
	 * @throws Exception
	 */
	@SuppressWarnings("rawtypes")
	public void deleteStm3100ProjectDelJob(Map paramMap) throws Exception{
		delete("stm3100DAO.deleteJen1200ProjectDelJob",paramMap);
	}
	
	/**
	 * Stm3100 Jenkins Job 허용 역할 그룹 일반 목록을 조회한다.
	 * @param paramMap
	 * @throws Exception
	 */
	@SuppressWarnings("rawtypes")
	public List selectJen1300JenkinsJobAuthGrpNormalList(Map paramMap) throws Exception {
		return (List) list("stm3100DAO.selectJen1300JenkinsJobAuthGrpNormalList", paramMap);
	}
	
	/**
	 * Stm3100 Jenkins Job 허용 역할 그룹 목록을 조회한다.
	 * @param paramMap
	 * @throws Exception
	 */
	@SuppressWarnings("rawtypes")
	public List selectStm3100JenkinsJobAuthGrpList(Stm3100VO stm3100VO) throws Exception {
		return (List) list("stm3100DAO.selectJen1300JenkinsJobAuthGrpList", stm3100VO);
	}
	
	/**
	 * Stm3100 Jenkins job 허용 역할 그룹  목록 총 건수를 조회한다. (그리드 페이징 처리) 
	 * @param Stm3100VO
	 * @return
	 * @throws Exception
	 */
	public int selectStm3100JenkinsJobAuthGrpListCnt(Stm3100VO stm3100VO) throws Exception {
		return (Integer) select("stm3100DAO.selectJen1300JenkinsJobAuthGrpListCnt", stm3100VO);
	}
	
	/**
	 * Stm3100 Jenkins Job에 허용 역할 그룹이 등록되어있는지 체크한다.
	 * @param paramMap
	 * @throws Exception
	 */
	@SuppressWarnings("rawtypes")
	public int selectStm3100JenkinsJobAuthGrpCnt(Map paramMap) throws Exception {
		return (int) select("stm3100DAO.selectJen1300JenkinsJobAuthGrpCnt", paramMap);
	}
	
	/**
	 * Stm3100 Jenkins Job 허용 역할 그룹을 등록한다.
	 * @param paramMap
	 * @throws Exception
	 */
	@SuppressWarnings("rawtypes")
	public void insertStm3100JenkinsJobAuthGrpInfo(Map paramMap) throws Exception {
		insert("stm3100DAO.insertJen1300JenkinsJobAuthGrpInfo", paramMap);
	}
	
	/**
	 * jenkins Jenkins Job 허용 역할 그룹을 삭제한다.
	 * @param paramMap
	 * @throws Exception
	 */
	@SuppressWarnings("rawtypes")
	public void deleteStm3100JenkinsJobAuthGrpInfo(Map paramMap) throws Exception {
		delete("stm3100DAO.deleteJen1300JenkinsJobAuthGrpInfo", paramMap);
	}

	/**
	 * Jenkins JOB_RESTORE 삭제
	 * @param paramMap
	 * @throws Exception
	 */
	@SuppressWarnings("rawtypes")
	public void updateJen1100JenkinsJobRestoreInfo(Map paramMap) throws Exception {
		update("stm3100DAO.updateJen1100JenkinsJobRestoreInfo", paramMap);
	}
}
