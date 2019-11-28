package kr.opensoftlab.oslops.stm.stm2000.stm2200.service;

import java.util.List;

import kr.opensoftlab.oslops.stm.stm2000.stm2200.vo.Stm2200VO;


/**
 * @Class Name : Stm2200Service.java
 * @Description : Stm2200Service Business class
 * @Modification Information
 *
 * @author 공대영
 * @since 2018.07.02
 * @version 1.0
 * @see
 *  
 *  Copyright (C)  All right reserved.
 */
public interface Stm2200Service {

	/**
	 * Stm2200 프로젝트 별 배정된 SVN Repository 전체 목록을 조회한다.
	 * @param stm2200vo
	 * @return List - 프로젝트 별 배정된 SVN Repository 전체 목록
	 * @exception Exception
	 */
	List<Stm2200VO> selectStm2200RepProjectList(Stm2200VO stm2200vo) throws Exception;

	/**
	 * Stm2200 프로젝트별 배정된 SVN Repository 전체 목록 총 건수를 조회한다.
	 * @param stm2200vo
	 * @return int - 프로젝트 별 배정된 SVN Repository 전체 목록 총 건수
	 * @exception Exception
	 */
	int selectStm2200RepProjectListCnt(Stm2200VO stm2200vo) throws Exception;

}
