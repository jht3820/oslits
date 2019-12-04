package kr.opensoftlab.oslops.stm.stm3000.stm3000.service;

import java.util.List;
import java.util.Map;

import kr.opensoftlab.oslops.stm.stm3000.stm3000.vo.Jen1000VO;
import kr.opensoftlab.oslops.stm.stm3000.stm3000.vo.Jen1100VO;

/**
 * @Class Name : Stm3000Service.java
 * @Description : Stm3000Service Business class
 * @Modification Information
 *
 * @author 공대영
 * @since 2018.09.03
 * @version 1.0
 * @see
 *  
 *  Copyright (C)  All right reserved.
 */

public interface Stm3000Service {

	/**
	 * jenkins 일반 목록 조회
	 * @param paramMap
	 * @throws Exception
	 */
	@SuppressWarnings({ "rawtypes" })
	List<Map> selectStm3000JenkinsNormalList(Map paramMap) throws Exception;
	
	/**
	 * job 일반 목록 조회
	 * @param paramMap
	 * @throws Exception
	 */
	@SuppressWarnings({  "rawtypes" })
	List<Map> selectStm3000JobNormalList(Map paramMap) throws Exception;
	
	/**
	 * jenkins 목록 조회
	 * @param paramMap
	 * @throws Exception
	 */
	List<Jen1000VO> selectStm3000JenkinsList(Jen1000VO jen1000VO) throws Exception;
	
	/**
	 * job 목록 조회
	 * @param paramMap
	 * @throws Exception
	 */
	List<Jen1100VO> selectStm3000JobList(Jen1100VO jen1100VO) throws Exception;
	
	/**
	 * Jenkins 목록 총건수
	 * @param paramMap
	 * @throws Exception
	 */
	int selectStm3000JenkinsListCnt(Jen1000VO jen1000VO) throws Exception;
	
	/**
	 * Job 목록 총건수
	 * @param paramMap
	 * @throws Exception
	 */
	int selectStm3000JobListCnt(Jen1100VO jen1100VO) throws Exception;
	
	/**
	 * Jenkins 정보 조회(단건)
	 * @param paramMap
	 * @throws Exception
	 */
	@SuppressWarnings("rawtypes")
	Map selectStm3000JenkinsInfo(Map<String, String> paramMap) throws Exception;
	
	/**
	 * Job 정보 조회(단건)
	 * @param paramMap
	 * @throws Exception
	 */
	@SuppressWarnings("rawtypes")
	Map selectStm3000JobInfo(Map<String, String> paramMap) throws Exception;
	
	/**
	 * Jenkins 등록
	 * @param paramMap
	 * @throws Exception
	 */
	String insertStm3000JenkinsInfo(Map<String, String> paramMap) throws Exception;
	
	/**
	 * Job 등록
	 * @param paramMap
	 * @throws Exception
	 */
	String insertStm3000JobInfo(Map<String, String> paramMap) throws Exception;

	/**
	 * Jenkins 수정
	 * @param paramMap
	 * @throws Exception
	 */
	int updateStm3000JenkinsInfo(Map<String, String> paramMap) throws Exception;
	
	/**
	 * Job 수정
	 * @param paramMap
	 * @throws Exception
	 */
	int updateStm3000JobInfo(Map<String, String> paramMap) throws Exception;
	
	/**
	 * Jenkins 삭제
	 * @param paramMap
	 * @throws Exception
	 */
	void deleteStm3000JenkinsInfo(Map<String, String> paramMap) throws Exception;
	
	/**
	 * Job 삭제
	 * @param paramMap
	 * @throws Exception
	 */
	void deleteStm3000JobInfo(Map<String, String> paramMap) throws Exception;
	
	/**
	 * Jenkins 등록 상태 확인
	 * @param paramMap
	 * @throws Exception
	 */
	int selectStm3000JenkinsUseCountInfo(Map<String, String> paramMap) throws Exception;
	
	/**
	 * Job 등록 상태 확인
	 * @param paramMap
	 * @throws Exception
	 */
	int selectStm3000JobUseCountInfo(Map<String, String> paramMap) throws Exception;
	
	/**
	 * Jenkins 저장 또는 수정
	 * @param paramMap
	 * @throws Exception
	 */
	Object saveStm3000JenkinsInfo(Map<String, String> paramMap)  throws Exception;
	
	/**
	 * Job 저장 또는 수정
	 * @param paramMap
	 * @throws Exception
	 */
	Object saveStm3000JobInfo(Map<String, String> paramMap)  throws Exception;
	
	/**
	 * 젠킨스 접속 유저정보를 조회한다.
	 *  
	 * @param param - Map
	 * @return list 
	 * @throws Exception
	 */
	@SuppressWarnings({"rawtypes"})
	List<Map> selectStm3000JenkinsUserList(Map map) throws Exception;
	
	/**
	 * jenkins 허용 역할 정보 목록 저장
	 * @param paramMap
	 * @throws Exception
	 */
	@SuppressWarnings("rawtypes")
	int insertStm3000JenAuthGrpList(Map paramMap) throws Exception;
	/**
	 * jenkins 허용 역할 정보 목록 제거
	 * @param paramMap
	 * @throws Exception
	 */
	@SuppressWarnings("rawtypes")
	void deleteStm3000JenAuthGrpList(Map paramMap) throws Exception;
	
	/**
	 * jenkins 허용 역할 정보 저장
	 * @param paramMap
	 * @throws Exception
	 */
	@SuppressWarnings("rawtypes")
	void insertStm3000JenAuthGrpInfo(Map paramMap) throws Exception;
	/**
	 * jenkins 허용 역할 정보 제거
	 * @param paramMap
	 * @throws Exception
	 */
	@SuppressWarnings("rawtypes")
	void deleteStm3000JenAuthGrpInfo(Map paramMap) throws Exception;
	
	/**
	 * jenkins 허용 역할 정보 목록 가져오기
	 * @param paramMap
	 * @throws Exception
	 */
	@SuppressWarnings("rawtypes")
	List selectStm3000JenAuthGrpList(Map paramMap) throws Exception;
	
	/**
	 * jenkins 허용 역할 정보 단건 갯수 가져오기
	 * @param paramMap
	 * @throws Exception
	 */
	@SuppressWarnings("rawtypes")
	int selectStm3000JenAuthGrpCnt(Map paramMap) throws Exception;
}
