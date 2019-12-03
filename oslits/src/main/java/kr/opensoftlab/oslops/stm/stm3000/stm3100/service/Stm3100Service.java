package kr.opensoftlab.oslops.stm.stm3000.stm3100.service;

import java.util.List;
import java.util.Map;

import kr.opensoftlab.oslops.stm.stm3000.stm3100.vo.Stm3100VO;


/**
 * @Class Name : Stm3100Service.java
 * @Description : Stm3100Service Business class
 * @Modification Information
 *
 * @author 공대영
 * @since 2018.07.02
 * @version 1.0
 * @see
 *  
 *  Copyright (C)  All right reserved.
 */
public interface Stm3100Service {

	/**
	 * Stm3100프로젝트에 배정된 Jenkins Job 목록을 조회한다. 
	 * @param paramMap
	 * @return
	 * @throws Exception
	 */
	List<Stm3100VO> selectStm3100JenkinsProjectList(Stm3100VO stm3100VO) throws Exception;

	/**
	 * Stm3100 프로젝트에 배정된 Jenkins Job 목록 총 건수를 조회한다. (그리드 페이징 처리) 
	 * @param Stm3100VO
	 * @return
	 * @throws Exception
	 */
	int selectStm3100JenkinsProjectListCnt(Stm3100VO stm3100VO) throws Exception;
	
	/**
	 * Stm3100 프로젝트에 배정 설정  Jenkins Job 목록을 조회한다. 
	 * @param paramMap
	 * @return List - 프로젝트에 배정 설정  Jenkins Job 목록
	 * @throws Exception
	 */
	@SuppressWarnings("rawtypes")
	List selectStm3100JenkinsProjectAuthList(Stm3100VO stm3100VO) throws Exception;

	/**
	 * Stm3100 프로젝트에 배정 설정  Jenkins Job 목록 총 건수를 조회한다. (그리드 페이징 처리) 
	 * @param paramMap
	 * @return
	 * @throws Exception
	 */
	int selectStm3100JenkinsProjectAuthListCnt(Stm3100VO stm3100VO) throws Exception;
	
	/**
	 * Stm3100 프로젝트에 Jenkins Job을 배정한다.
	 * @param Stm3100VO
	 * @return
	 * @throws Exception
	 */
	@SuppressWarnings("rawtypes")
	void insertStm3100ProjectAddJob(Map paramMap) throws Exception;

	/**
	 * Stm3100 프로젝트에 Jenkins Job을 배정제외 한다.
	 * @param Stm3100VO
	 * @return
	 * @throws Exception
	 */
	@SuppressWarnings("rawtypes")
	void deleteStm3100ProjectDelJob(Map paramMap) throws Exception;
	
	/**
	 * Stm3100 Jenkins Job 허용 역할 그룹 일반 목록을 조회한다.
	 * @param paramMap
	 * @throws Exception
	 */
	@SuppressWarnings("rawtypes")
	List selectJen1300JenkinsJobAuthGrpNormalList(Map paramMap) throws Exception;
	
	/**
	 * Stm3100 Jenkins Job 허용 역할 그룹 목록을 조회한다.
	 * @param paramMap
	 * @throws Exception
	 */
	@SuppressWarnings("rawtypes")
	List selectStm3100JenkinsJobAuthGrpList(Stm3100VO stm3100VO) throws Exception;
	
	/**
	 * Stm3100 Jenkins job 허용 역할 그룹  목록 총 건수를 조회한다. (그리드 페이징 처리) 
	 * @param Stm3100VO
	 * @return
	 * @throws Exception
	 */
	int selectStm3100JenkinsJobAuthGrpListCnt(Stm3100VO stm3100VO) throws Exception;
	
	/**
	 * Stm3100 Jenkins Job에 허용 역할 그룹이 등록되어있는지 체크한다.
	 * @param paramMap
	 * @throws Exception
	 */
	@SuppressWarnings("rawtypes")
	int selectStm3100JenkinsJobAuthGrpCnt(Map paramMap) throws Exception;
	
	/**
	 * Stm3100 Jenkins Job 허용 역할 그룹을 등록한다.
	 * @param paramMap
	 * @throws Exception
	 */
	@SuppressWarnings("rawtypes")
	int insertStm3100JenkinsJobAuthGrpInfo(Map paramMap) throws Exception;
	
	/**
	 * jenkins Jenkins Job 허용 역할 그룹을 삭제한다.
	 * @param paramMap
	 * @throws Exception
	 */
	@SuppressWarnings("rawtypes")
	void deleteStm3100JenkinsJobAuthGrpInfo(Map paramMap) throws Exception;

	/**
	 * Jenkins JOB_RESTORE 삭제
	 * @param paramMap
	 * @throws Exception
	 */
	@SuppressWarnings("rawtypes")
	void updateJen1100JenkinsJobRestoreInfo(Map paramMap) throws Exception;
}
