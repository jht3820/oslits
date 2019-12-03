package kr.opensoftlab.oslops.cmm.cmm1000.cmm1600.service;

import java.util.List;

import kr.opensoftlab.oslops.cmm.cmm1000.cmm1600.vo.Cmm1600VO;

/**
 * @Class Name : Cmm1600Service.java
 * @Description : Cmm1600Service Business class
 * @Modification Information
 *
 * @author 공대영
 *  * @since 2018.10.16.
 * @version 1.0
 * @see
 *  
 *  Copyright (C)  All right reserved.
 */
public interface Cmm1600Service {
	
	/**
	 * Dpl1100 배포 계획에 배정된 요구사항 목록을 조회한다.
	 * @param Dpl1100VO
	 * @return list - 배포계획 배정된 요구사항 목록
	 * @throws Exception
	 */
	List<Cmm1600VO> selectCmm1600CommonDplList(Cmm1600VO cmm1600VO) throws Exception;
	
	/**
	 * Cmm1600 배포 계획 목록의 총 건수를 조회한다. (그리드 페이징 처리)
	 * @param Cmm1600VO
	 * @return
	 * @throws Exception
	 */
	int selectCmm1600CommonDplListCnt(Cmm1600VO cmm1600VO) throws Exception;

}
