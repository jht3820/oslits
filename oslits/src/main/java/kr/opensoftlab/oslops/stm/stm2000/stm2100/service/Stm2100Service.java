package kr.opensoftlab.oslops.stm.stm2000.stm2100.service;

import java.util.List;
import java.util.Map;

import kr.opensoftlab.oslops.stm.stm2000.stm2100.vo.Stm2100VO;



/**
 * @Class Name : Stm2100Service.java
 * @Description : Stm2100Service Business class
 * @Modification Information
 *
 * @author 공대영
 * @since 2018.07.02
 * @version 1.0
 * @see
 *  
 *  Copyright (C)  All right reserved.
 */
public interface Stm2100Service {

	/**
	 * Stm2100 프로젝트에 배정된 SVN Repository 목록을 조회한다.
	 * @param paramMap
	 * @return 
	 * @exception Exception
	 */
	@SuppressWarnings("rawtypes")
	List selectStm2100ProjectListAjax(Map paramMap) throws Exception;

	/**
	 * Stm2100 프로젝트에  SVN Repository를 배정/배정제외 한다.
	 * @param list - SVN Repository 목록
	 * @return 
	 * @exception Exception
	 */
	void saveStm2100(List<Stm2100VO> list) throws Exception;

	/**
	 * Stm2100 프로젝트에 배정 가능한 SVN Repository 목록을 조회한다.
	 * @param paramMap
	 * @return List - 배정 가능 SVN Repository 목록
	 * @exception Exception
	 */
	@SuppressWarnings("rawtypes")
	List selectStm2100ProjectAuthList(Map paramMap) throws Exception;

}
