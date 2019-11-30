package kr.opensoftlab.oslops.adm.adm8000.adm8000.service;

import java.util.List;
import java.util.Map;

/**
 * @Class Name : Adm8000Service.java
 * @Description : Adm8000Service Business class
 * @Modification Information
 *
 * @author 공대영
 * @since 2018.09.03
 * @version 1.0 * @see
 *  
 *  Copyright (C)  All right reserved.
 */

public interface Adm8000Service {

	/**
	 * Adm8000 보고서 마스터 등록/수정한다.
	 * @param paramMap
	 * @return
	 * @throws Exception
	 */
	Object saveAdm8000MasterInfo(Map<String, String> paramMap) throws Exception;

	/**
	 * Adm8000 보고서 기준연도 목록을 조회한다.
	 * @param paramMap
	 * @return
	 * @throws Exception
	 */
	@SuppressWarnings("rawtypes")
	List<Map> selectAdm8000MasterYearList(Map<String, String> paramMap) throws Exception;

	/**
	 * Adm8000 보고서 마스터 목록을 조회한다.
	 * @param paramMap
	 * @return
	 * @throws Exception
	 */
	@SuppressWarnings("rawtypes")
	List<Map> selectAdm8000MasterList(Map<String, String> paramMap) throws Exception;

	/**
	 * Adm8000 보고서 마스터 단건 조회
	 * @param paramMap
	 * @return
	 * @throws Exception
	 */
	@SuppressWarnings("rawtypes")
	Map selectAdm8000MasterInfo(Map map) throws Exception;

	/**
	 * Adm8000 보고서 마스터를 삭제한다.
	 * @param paramMap
	 * @return
	 * @throws Exception
	 */
	void deleteAdm8000MasterInfo(Map<String, String> paramMap) throws Exception;

	/**
	 * Adm8000 보고서 디테일 등록/수정한다.
	 * @param paramMap
	 * @return
	 * @throws Exception
	 */
	Object saveAdm8000DetailInfo(Map<String, String> paramMap) throws Exception;

	/**
	 * Adm8000 보고서 디테일 목록을 조회한다.
	 * @param paramMap
	 * @return
	 * @throws Exception
	 */
	@SuppressWarnings("rawtypes")
	List<Map> selectAdm8000DetailList(Map<String, String> paramMap) throws Exception;

	/**
	 * Adm8000 보고서 디테일 단건 조회
	 * @param paramMap
	 * @return
	 * @throws Exception
	 */
	@SuppressWarnings("rawtypes")
	Map selectAdm8000DetailInfo(Map<String, String> paramMap) throws Exception;

	/**
	 * Adm8000 보고서 디테일을 삭제한다.
	 * @param paramMap
	 * @return
	 * @throws Exception
	 */
	int deleteAdm8000DetailInfo(Map<String, String> paramMap) throws Exception;

	/**
	 * Adm8000 보고서 기준연도를 복사한다.
	 * @param paramMap
	 * @return
	 * @throws Exception
	 */
	Object saveAdm8000CopyInfo(Map<String, String> paramMap) throws Exception;

}
