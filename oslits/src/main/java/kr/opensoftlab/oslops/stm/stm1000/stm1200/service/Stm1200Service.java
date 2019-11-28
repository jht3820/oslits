package kr.opensoftlab.oslops.stm.stm1000.stm1200.service;

import java.util.List;

import kr.opensoftlab.oslops.stm.stm1000.stm1200.vo.Stm1200VO;



/**
 * @Class Name : Stm1200Service.java
 * @Description : Stm1200Service Business class
 * @Modification Information
 *
 * @author 공대영
 * @since 2018.07.23.
 * @version 1.0
 * @see
 *  
 *  Copyright (C)  All right reserved.
 */
public interface Stm1200Service {
	
	/**
	 * Stm1200 전체 API 목록을 조회한다.
	 * @param stm1200vo
	 * @return List - 전체 API 목록
	 * @exception Exception
	 */
	List<Stm1200VO> selectStm1200ProjectList(Stm1200VO stm1200vo) throws Exception;

	/**
	 * Stm1200 그리드 페이징 처리를 위한 전체 API 목록 총 수를 조회한다.
	 * @param stm1200vo
	 * @return int 전체 API 목록 수
	 * @exception Exception
	 */
	int selectStm1200ProjectListCnt(Stm1200VO stm1200vo) throws Exception;
	
}
