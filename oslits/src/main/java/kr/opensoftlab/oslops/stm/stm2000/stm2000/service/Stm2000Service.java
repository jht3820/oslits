package kr.opensoftlab.oslops.stm.stm2000.stm2000.service;

import java.util.List;
import java.util.Map;

import kr.opensoftlab.oslops.stm.stm2000.stm2000.vo.Stm2000VO;



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

	
	/**
	 * Stm2000 SVN Repository 목록을 조회한다.
	 * @param Stm2000VO
	 * @return List - SVN Repository 목록
	 * @exception Exception
	 */
	List<Stm2000VO> selectStm2000RepositoryList(Stm2000VO stm2000vo) throws Exception;

	/**
	 * Stm2000 SVN Repository 목록 총 수를 조회한다.
	 * @param Stm2000VO
	 * @return SVN Repository 목록 수
	 * @exception Exception
	 */
	int selectStm2000RepositoryListCnt(Stm2000VO stm2000vo) throws Exception;

	/**
	 * Stm2000 SVN Repository 단건 조회한다.
	 * @param paramMap
	 * @return
	 * @exception Exception
	 */
	@SuppressWarnings("rawtypes")
	Map selectStm2000Info(Map<String, String> paramMap) throws Exception;

	/**
	 * Stm2000 SVN Repository를 등록/수정한다.
	 * @param paramMap
	 * @return
	 * @exception Exception
	 */
	Object saveStm2000Info(Map<String, String> paramMap) throws Exception;

	/**
	 * Stm2000 SVN Repository 상태를 확인한다.
	 * @param paramMap
	 * @return
	 * @exception Exception
	 */
	int selectStm2000UseCountInfo(Map<String, String> paramMap) throws Exception;

	/**
	 * Stm2000 SVN Repository를 삭제한다.
	 * @param paramMap
	 * @return
	 * @exception Exception
	 */
	void deleteStm2000Info(Map<String, String> paramMap) throws Exception;
	
	/**
	 * Stm2000 SVN Repository 허용 역할 목록을 저장한다.
	 * @param paramMap
	 * @return
	 * @throws Exception
	 */
	@SuppressWarnings("rawtypes")
	int insertStm2000SvnAuthGrpList(Map paramMap) throws Exception;

	/**
	 * Stm2000 SVN Repository 허용 역할 목록을 제거한다.
	 * @param paramMap
	 * @return
	 * @throws Exception
	 */
	@SuppressWarnings("rawtypes")
	void deleteStm2000SvnAuthGrpList(Map paramMap) throws Exception;
	
	/**
	 * Stm2000 SVN Repository 허용 역할 정보를 저장한다. (단건)
	 * @param paramMap
	 * @return
	 * @throws Exception
	 */
	@SuppressWarnings("rawtypes")
	void insertStm2000SvnAuthGrpInfo(Map paramMap) throws Exception;
	
	/**
	 * Stm2000 SVN Repository 허용 역할 정보를 제거한다. (단건)
	 * @param paramMap
	 * @return
	 * @throws Exception
	 */
	@SuppressWarnings("rawtypes")
	void deleteStm2000SvnAuthGrpInfo(Map paramMap) throws Exception;
	
	/**
	 * Stm2000 SVN Repository 허용 역할 정보 목록을 조회한다.
	 * @param paramMap
	 * @return List SVN Repository 허용 역할 목록
	 * @throws Exception
	 */
	@SuppressWarnings("rawtypes")
	List selectStm2000SvnAuthGrpList(Map paramMap) throws Exception;
	
	/**
	 * Stm2000 그리드 페이징 처리를 위한 SVN Repository 허용 역할 목록 총 수를 조회한다.
	 * @param paramMap
	 * @return SVN Repository 허용 역할 목록 총 수
	 * @throws Exception
	 */
	@SuppressWarnings("rawtypes")
	int selectStm2000SvnAuthGrpCnt(Map paramMap) throws Exception;
}
