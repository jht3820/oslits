package kr.opensoftlab.oslits.stm.stm3000.stm3000.service;

import java.util.List;
import java.util.Map;

import kr.opensoftlab.oslits.stm.stm3000.stm3000.vo.Stm3000VO;

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

	List<Map> selectStm3000JenkinsUserList(Map map) throws Exception;

	List<Stm3000VO> selectStm3000JobList(Stm3000VO stm3000vo);

	int selectStm3000JobListCnt(Stm3000VO stm3000vo);

	Map selectStm3000JobInfo(Map<String, String> paramMap);

	Object saveStm3000JobInfo(Map<String, String> paramMap);

	int selectStm3000UseCountInfo(Map<String, String> paramMap);

	void deleteStm3000Info(Map<String, String> paramMap);


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
