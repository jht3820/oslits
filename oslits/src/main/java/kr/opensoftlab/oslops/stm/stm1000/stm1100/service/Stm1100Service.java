package kr.opensoftlab.oslops.stm.stm1000.stm1100.service;

import java.util.List;
import java.util.Map;

import kr.opensoftlab.oslops.stm.stm1000.stm1100.vo.Stm1100VO;

/**
 * @Class Name : Api1100Service.java
 * @Description : Api1100Service Business class
 * @Modification Information
 *
 * @author 공대영
 *  * @since 2018.08.14.
 * @version 1.0
 * @see
 *  
 *  Copyright (C)  All right reserved.
 */
public interface Stm1100Service {

	/**
	 * Stm1100 프로젝트에 사용 가능한 API 목록을 조회한다.
	 * @param paramMap
	 * @return List - 프로젝트에 등록 가능한 API 관리 목록
	 * @exception Exception
	 */	
	@SuppressWarnings("rawtypes")
	List<Map> selectStm1100ProjectAuthList(Map<String, String> paramMap) throws Exception;

	/**
	 * Stm1100 프로젝트에 등록되어있는 API 목록을 조회한다.
	 * @param paramMap
	 * @exception Exception
	 */
	@SuppressWarnings("rawtypes")
	List selectStm1100ProjectListAjax(Map<String, String> paramMap) throws Exception;

	/**
	 * Stm1100 프로젝트에 API를 등록/삭제한다.
	 * @param list - Stm1100VO list
	 * @exception Exception
	 */	
	void saveStm1100(List<Stm1100VO> list) throws Exception;
}
