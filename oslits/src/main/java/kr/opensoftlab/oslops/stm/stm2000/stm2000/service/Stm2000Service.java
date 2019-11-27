package kr.opensoftlab.oslits.stm.stm2000.stm2000.service;

import java.util.List;
import java.util.Map;

import kr.opensoftlab.oslits.stm.stm2000.stm2000.vo.Stm2000VO;



/**
 * @Class Name : Stm2000Service.java
 * @Description : Stm2000Service Business class
 * @Modification Information
 *
 * @author 공대영
 * @since 2018.07.02
 * @version 1.0
 * @see
 *  
 *  Copyright (C)  All right reserved.
 */
public interface Stm2000Service {

	

	List<Stm2000VO> selectStm2000RepositoryList(Stm2000VO stm2000vo)
			throws Exception;

	int selectStm2000RepositoryListCnt(Stm2000VO stm2000vo) throws Exception;

	Map selectStm2000Info(Map<String, String> paramMap);

	Object saveStm2000Info(Map<String, String> paramMap);

	int selectStm2000UseCountInfo(Map<String, String> paramMap);

	void deleteStm2000Info(Map<String, String> paramMap);
	
	/**
	 * SVN Repository 허용 역할 정보 목록 저장
	 * @param paramMap
	 * @throws Exception
	 */
	@SuppressWarnings("rawtypes")
	int insertStm2000SvnAuthGrpList(Map paramMap) throws Exception;
	/**
	 * SVN Repository 허용 역할 정보 목록 제거
	 * @param paramMap
	 * @throws Exception
	 */
	@SuppressWarnings("rawtypes")
	void deleteStm2000SvnAuthGrpList(Map paramMap) throws Exception;
	
	
	/**
	 * SVN Repository 허용 역할 정보 저장
	 * @param paramMap
	 * @throws Exception
	 */
	@SuppressWarnings("rawtypes")
	void insertStm2000SvnAuthGrpInfo(Map paramMap) throws Exception;
	/**
	 * SVN Repository 허용 역할 정보 제거
	 * @param paramMap
	 * @throws Exception
	 */
	@SuppressWarnings("rawtypes")
	void deleteStm2000SvnAuthGrpInfo(Map paramMap) throws Exception;
	
	/**
	 * SVN Repository 허용 역할 정보 목록 가져오기
	 * @param paramMap
	 * @throws Exception
	 */
	@SuppressWarnings("rawtypes")
	List selectStm2000SvnAuthGrpList(Map paramMap) throws Exception;
	
	/**
	 * SVN Repository 허용 역할 정보 단건 갯수 가져오기
	 * @param paramMap
	 * @throws Exception
	 */
	@SuppressWarnings("rawtypes")
	int selectStm2000SvnAuthGrpCnt(Map paramMap) throws Exception;
}
