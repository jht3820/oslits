package kr.opensoftlab.oslops.stm.stm4000.stm4100.service;

import java.util.List;
import java.util.Map;

import kr.opensoftlab.oslops.stm.stm4000.stm4100.vo.Stm4100VO;


/**
 * @Class Name : Stm4100Service.java
 * @Description : Stm4100Service Business class
 * @Modification Information
 *
 * @author 배용진
 * @since 2019.05.10.
 * @version 1.0
 * @see
 *  
 *  Copyright (C) OpenSoftLab Corp. All right reserved.
 */
public interface Stm4100Service {
	
	/**
	 * Stm4100 라이선스 그룹의 모든 프로젝트와 각 프로젝트에 있는 업무역할을 조회
	 * @param paramMap - Map
	 * @return list - 라이선스 그룹의 프로젝트 목록, 프로젝트의 권한그룹 목록
	 * @exception Exception
	 */
	@SuppressWarnings("rawtypes")
	List selectStm4100PrjAuthList(Map paramMap) throws Exception;
	
	/**
	 * Stm4100 프로젝트 권한에 배정된 사용자 목록 조회
	 * @param stm4100VO
	 * @return list 배정된 사용자 목록
	 * @exception Exception
	 */
	@SuppressWarnings("rawtypes")
	List selectStm4100UsrAddList(Stm4100VO stm4100VO) throws Exception;
	
	/**
	 * Stm4100  프로젝트 권한에 배정된 사용자 목록 총 건수 : 그리드 페이징 처리
	 * @param stm4100VO
	 * @return int 배정된 사용자 목록
	 * @exception Exception
	 */
	int selectStm4100UsrAddListCnt(Stm4100VO stm4100VO) throws Exception;
	
	/**
	 * Stm4100 선택한 프로젝트 권한에 배정되지 않은 전체 사용자 목록 목록 조회
	 * @param stm4100VO
	 * @return list 배정되지 않은 사용자 목록
	 * @exception Exception
	 */
	@SuppressWarnings("rawtypes")
	List selectStm4100UsrAllList(Stm4100VO stm4100VO) throws Exception;
	
	/**
	 * Stm4100  선택한 프로젝트별 권한에 배정되지 않은 전체 사용자 목록 총건수 : 그리드 페이징 처리
	 * @param stm4100VO
	 * @return int 배정되지 않은 사용자 목록
	 * @exception Exception
	 */
	int selectStm4100UsrAllListCnt(Stm4100VO stm4100VO) throws Exception;
	
	/**
	 * Stm4100 프로젝트 권한에 사용자를 배정한다.
	 * @param paramMap
	 * @return 
	 * @exception Exception
	 */
	@SuppressWarnings("rawtypes")
	void insertStm4100PrjUsrAuthList(Map paramMap) throws Exception;
	
	/**
	 * Stm4100 프로젝트 권한에서 사용자를 삭제한다.
	 * @param 
	 * @return 
	 * @exception Exception
	 */
	@SuppressWarnings("rawtypes")
	void deleteStm4100PrjUsrAuthList(Map paramMap) throws Exception;
	
	/**
	 * Stm4100 프로젝트 권한 그룹에 배정된 사용자수 조회
	 * 권한에서 사용자 삭제 시 사용자 1명일 경우 삭제 불가체크에 사용
	 * @param 
	 * @return 
	 * @exception Exception
	 */
	@SuppressWarnings("rawtypes")
	int selectStm4100UsrCntCheck(Map paramMap) throws Exception;
}
