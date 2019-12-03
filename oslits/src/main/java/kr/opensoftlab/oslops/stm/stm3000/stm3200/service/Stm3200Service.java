package kr.opensoftlab.oslops.stm.stm3000.stm3200.service;

import java.util.List;

import kr.opensoftlab.oslops.stm.stm3000.stm3200.vo.Stm3200VO;


/**
 * @Class Name : Stm3200Service.java
 * @Description : Stm3200Service Business class
 * @Modification Information
 *
 * @author 공대영
 * @since 2018.07.02
 * @version 1.0
 * @see
 *  
 *  
 *  --------------------------------------
 *  수정일			수정자			수정내용
 *  --------------------------------------
 *  2019-03-11		배용진		 	기능 개선
 *  
 *  --------------------------------------
 *  
 *  Copyright (C)  All right reserved.
 */
public interface Stm3200Service {

	/**
	 * Stm3200 라이선스 그룹의 각 프로젝트에 배정된 JENKINS JOB 전체 목록을 조회한다.
	 * @param Stm3200VO
	 * @return List - 프로젝트에 배정된 Jenkins Job 목록
	 * @throws Exception
	 */
	List<Stm3200VO> selectStm3200ProjectJenkinsJobAllList(Stm3200VO stm3200VO) throws Exception;

	/**
	 * Stm3200 라이선스 그룹의 각 프로젝트에 배정된 JENKINS JOB 전체 목록 총 건수를 조회한다.
	 * JENKINS 저장소 전체 현황 화면의 그리드 페이징 처리위해
	 * @param Stm3200VO
	 * @return List - 프로젝트에 배정된 Jenkins Job 목록
	 * @throws Exception
	 */
	int selectStm3200ProjectJenkinsJobAllListCnt(Stm3200VO stm3200VO) throws Exception;
}
