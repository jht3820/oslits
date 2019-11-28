package kr.opensoftlab.oslops.stm.stm1000.stm1000.service;

import java.util.List;
import java.util.Map;

import kr.opensoftlab.oslops.stm.stm1000.stm1000.vo.Stm1000VO;



/**
 * @Class Name : Stm1000Service.java
 * @Description : Stm1000Service Business class
 * @Modification Information
 *
 * @author 공대영
 *  * @since 2018.07.23.
 * @version 1.0
 * @see
 *  
 *  Copyright (C)  All right reserved.
 */
public interface Stm1000Service {
	
	/**
	 * Stm1000 API 관리 목록을 조회한다.
	 * @param stm1000vo
	 * @return List - API 관리 목록
	 * @exception Exception
	 */	
	List<Stm1000VO> selectStm1000List(Stm1000VO stm1000vo) throws Exception;

	/**
	 * Stm1000 페이징 처리를 위한 API 관리 목록 총 건수 조회한다.
	 * @param stm1000vo
	 * @return int API 목록 수
	 * @exception Exception
	 */	
	int selectStm1000ListCnt(Stm1000VO stm1000vo) throws Exception;

	/**
	 * Stm1000 API를 등록/수정 한다.
	 * @param paramMap - Map
	 * @return
	 * @exception Exception
	 */
	Object saveStm1000Info(Map<String, String> paramMap) throws Exception;

	/**
	 * Stm1000 API 정보를 단건 조회한다.
	 * @param paramMap - Map
	 * @return Map API 단건 정보
	 * @exception Exception
	 */	
	@SuppressWarnings("rawtypes")
	Map selectStm1000Info(Map<String, String> paramMap) throws Exception;

	/**
	 * Stm1000 API 정보를 삭제한다.
	 * @param paramMap - Map
	 * @return
	 * @exception Exception
	 */
	void deleteStm1000Info(Map<String, String> paramMap) throws Exception;

	/**
	 * Stm1000 API 등록 상태를 확인한다.
	 * @param paramMap - Map
	 * @return
	 * @exception Exception
	 */
	int selectStm1000UseCountInfo(Map<String, String> paramMap)throws Exception;

	/**
	 * Stm1000 API토큰 정보로  URL 목록을 조회한다.
	 * @param paramMap - Map
	 * @return List - URL 목록
	 * @exception Exception
	 */
	List<Map<String, String>> selectStm1000ApiUrlList(Map<String, String> paramMap)throws Exception;

}
