package kr.opensoftlab.oslits.dpl.dpl1000.dpl1100.service;

import java.util.List;
import java.util.Map;

import kr.opensoftlab.oslits.dpl.dpl1000.dpl1100.vo.Dpl1100VO;

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
	 * Dpl1100 
	 * @param param - dpl1100vo
	 * @return 
	 * @exception Exception
	 */
	@SuppressWarnings("rawtypes")
	List<Dpl1100VO> selectDpl1100ExistDplList(Map map) throws Exception;
	
	@SuppressWarnings("rawtypes")
	List<Dpl1100VO> selectDpl1100NotExistDplList(Map map) throws Exception;
	
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
	 * 요구사항 배포 계획 배정
	 * @param paramMap
	 * @throws Exception
	 */
	@SuppressWarnings("rawtypes")
	void insertDpl1200ReqDplInfo(Map paramMap) throws Exception;

	/**
	 * 요구사항 배포 계획 배정 update
	 * @param paramMap
	 * @throws Exception
	 */
	@SuppressWarnings("rawtypes")
	int updateDpl1200ReqDplInfo(Map paramMap) throws Exception;
	
	/**
	 * 요구사항 배포 계획 배정 제외
	 * @param paramMap
	 * @throws Exception
	 */
	@SuppressWarnings("rawtypes")
	void deleteDpl1200ReqDplInfo(Map paramMap) throws Exception;
}
