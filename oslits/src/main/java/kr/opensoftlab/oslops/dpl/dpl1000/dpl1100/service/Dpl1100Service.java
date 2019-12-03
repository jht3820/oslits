package kr.opensoftlab.oslops.dpl.dpl1000.dpl1100.service;

import java.util.List;
import java.util.Map;

import kr.opensoftlab.oslops.dpl.dpl1000.dpl1100.vo.Dpl1100VO;

/**
 * @Class Name : Dpl1000Service.java
 * @Description : Dpl1000Service Business class
 * @Modification Information
 *
 * @author 공대영
 * @since 2016.02.08
 * @version 1.0
 * @see
 *  
 *  Copyright (C)  All right reserved.
 */

public interface Dpl1100Service {

	/**
	 * Dpl1100 배포 계획에 배정된 요구사항 목록을 조회한다.
	 * @param Dpl1100VO
	 * @return list - 배포계획 배정된 요구사항 목록
	 * @throws Exception
	 */
	@SuppressWarnings("rawtypes")
	List<Map> selectDpl1100ExistDplList(Dpl1100VO dpl1100VO) throws Exception;

	/**
	 * Dpl1100 배포 계획에 배정된 요구사항 목록 총 건수를 조회한다. (그리드 페이징 처리) 
	 * @param Dpl1100VO
	 * @return int - 배포계획 배정된 요구사항 총 건수
	 * @throws Exception
	 */
	int selectDpl1100ExistDplListCnt(Dpl1100VO dpl1100VO) throws Exception ;
	
	/**
	 * Dpl1100 배포계획 미배정된 요구사항 목록을 조회한다.
	 * - 미배정 요구사항 : 배포계획 저장이 있는 작업흐름에 속해있으며, 아직 배포계획에 배정되지 않은 요구사항
	 * @param Dpl1100VO
	 * @return list
	 * @throws Exception
	 */
	@SuppressWarnings("rawtypes")
	List<Map> selectDpl1100NotExistDplList(Dpl1100VO dpl1100VO) throws Exception;
	
	
	/**
	 * Dpl1100 배포계획 미배정된 요구사항 목록 총 건수를 조회한다. (그리드 페이징 처리) 
	 * @param Dpl1100VO
	 * @return int - 배포계획 미배정된 요구사항 총 건수
	 * @throws Exception
	 */
	int selectDpl1100NotExistDplListCnt(Dpl1100VO dpl1100VO) throws Exception;
	
	
	void updateDpl1100Dpl(Map<String, Object> paramMap) throws Exception;

	@SuppressWarnings("rawtypes")
	String selectDpl1100ExistBuildInfo(Map paramMap) throws Exception;

	/**
	 * 요구사항 배포 계획 배정
	 * @param paramMap
	 * @throws Exception
	 */
	@SuppressWarnings("rawtypes")
	List selectDpl1100ReqDplList(Map paramMap) throws Exception;
	
	/**
	 * Dpl1100 배포계획에 요구사항을 배정한다.
	 * @param paramMap
	 * @return
	 * @throws Exception
	 */
	@SuppressWarnings("rawtypes")
	void insertDpl1100ReqDplInfo(Map paramMap) throws Exception;

	/**
	 * Dpl1100 배포계획에 배정된 요구사항을 배정제외한다.
	 * @param paramMap
	 * @throws Exception
	 */
	@SuppressWarnings("rawtypes")
	void deleteDpl1100ReqDplInfo(Map paramMap) throws Exception;
	
	/**
	 * 요구사항 배포 계획 배정 update
	 * @param paramMap
	 * @throws Exception
	 */
	@SuppressWarnings("rawtypes")
	int updateDpl1200ReqDplInfo(Map paramMap) throws Exception;
	
	
}
