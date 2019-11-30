package kr.opensoftlab.oslops.prs.prs4000.prs4000.service;

import java.util.List;
import java.util.Map;



/**
 * @Class Name : Prs4000Service.java
 * @Description : Prs4000Service Business class
 * @Modification Information
 *
 * @author 김정환
 * @since 2016.03.11
 * @version 1.0
 * @see
 *  
 *  Copyright (C)  All right reserved.
 */
public interface Prs4000Service {

	
	
	
	/**
	 * 일정 관리 내역을 조회한다.
	 * @param paramMap
	 * @return
	 * @throws Exception
	 */
	@SuppressWarnings("rawtypes")
	List selectPrs4000List(Map paramMap) throws Exception;

	/**
	 * 일정관리 내역을 등록한다.
	 * @param paramMap
	 * @return
	 * @throws Exception
	 */
	@SuppressWarnings("rawtypes")
	String insertPrs4000SchdInfo(Map paramMap) throws Exception;
	
	/**
	 * 일정관리 내역을 수정한다.
	 * @param paramMap
	 * @throws Exception
	 */
	@SuppressWarnings("rawtypes")
	void updatePrs4000SchdInfo(Map paramMap) throws Exception;
	
	/**
	 * 일정관리 내역을 삭제한다.
	 * @param paramMap
	 * @throws Exception
	 */
	@SuppressWarnings("rawtypes")
	void deletePrs4000SchdInfo(Map paramMap) throws Exception;
	
}
