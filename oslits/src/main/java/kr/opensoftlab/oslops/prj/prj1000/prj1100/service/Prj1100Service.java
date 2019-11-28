package kr.opensoftlab.oslops.prj.prj1000.prj1100.service;

/**
 * @Class Name : Prj1100Service.java
 * @Description : Prj1100Service Service class
 * @Modification Information
 *
 * @author 진주영
 * @since 2018.07.19.
 * @version 1.0
 * @see
 *  
 *  Copyright (C) OpenSoftLab Corp All right reserved.
 */

import java.util.List;
import java.util.Map;

import kr.opensoftlab.oslops.prj.prj1000.prj1100.vo.Prj1100VO;


public interface Prj1100Service {
	/**
	 * 프로세스 목록 조회 
	 * @param paramMap
	 * @throws Exception
	 */
	@SuppressWarnings("rawtypes")
	List selectFlw1000ProcessList(Map paramMap) throws Exception;

	/**
	 * 프로세스 단건 조회 
	 * @param paramMap
	 * @throws Exception
	 */
	@SuppressWarnings("rawtypes")
	Map selectFlw1000ProcessInfo(Map paramMap) throws Exception;
	
	/**
	 * 프로세스 수정 (이름, json데이터)
	 * @param paramMap
	 * @throws Exception
	 */
	@SuppressWarnings("rawtypes")
	void updateFlw1000ProcessInfo(Map paramMap) throws Exception;
	
	/**
	 * 프로세스 확정 ('종료분기' 작업흐름 데이터, 프로세스 json data)
	 * @param paramMap
	 * @throws Exception
	 */
	@SuppressWarnings("rawtypes")
	void updateFlw1000ProcessConfirmInfo(Map paramMap) throws Exception;

	/**
	 * 프로세스 확정취소 ('종료분기' 작업흐름 데이터, 프로세스 json data)
	 * @param paramMap
	 * @throws Exception
	 */
	@SuppressWarnings("rawtypes")
	void updateFlw1000ProcessConfirmCancle(Map paramMap) throws Exception;

	/**
	 * 프로세스 추가
	 * @param paramMap
	 * @throws Exception
	 */
	@SuppressWarnings("rawtypes")
	String insertFlw1000ProcessInfo(Map paramMap) throws Exception;
	
	/**
	 * 프로세스 제거
	 * @param paramMap
	 * @throws Exception
	 */
	@SuppressWarnings("rawtypes")
	void deleteFlw1000ProcessInfo(Map paramMap) throws Exception;
	
	/**
	 * 프로세스에 배정된 요구사항 수
	 * @param paramMap
	 * @throws Exception
	 */
	@SuppressWarnings("rawtypes")
	int selectFlw1000ProcessReqCnt(Map paramMap) throws Exception;
	
	/**
	 * 작업흐름 조회 
	 * @param paramMap
	 * @throws Exception
	 */
	@SuppressWarnings("rawtypes")
	List selectFlw1100FlowList(Map paramMap) throws Exception;
	
	/**
	 * 작업흐름 수정
	 * @param paramMap
	 * @throws Exception
	 */
	@SuppressWarnings("rawtypes")
	void updateFlw1100FlowInfo(Map paramMap) throws Exception;
	
	/**
	 * 작업흐름  추가
	 * @param paramMap
	 * @throws Exception
	 */
	@SuppressWarnings("rawtypes")
	void insertFlw1100FlowInfo(Map paramMap) throws Exception;
	
	/**
	 * 작업흐름 제거
	 * @param paramMap
	 * @throws Exception
	 */
	@SuppressWarnings("rawtypes")
	void deleteFlw1100FlowInfo(Map paramMap) throws Exception;
	
	/**
	 * 선택 요구사항에 해당하는 추가 항목 목록
	 * @param paramMap
	 * @throws Exception
	 */
	@SuppressWarnings("rawtypes")
	List selectFlw1200ReqOptList(Map paramMap) throws Exception;
	
	/**
	 * 선택 작업흐름에 해당하는 추가 항목 목록
	 * @param paramMap
	 * @throws Exception
	 */
	@SuppressWarnings("rawtypes")
	List selectFlw1200OptList(Map paramMap) throws Exception;
	

	/**
	 * 선택 작업흐름에 해당하는 추가 항목 목록
	 * @param paramMap
	 * @throws Exception
	 */
	@SuppressWarnings("rawtypes")
	List selectFlw1300OptFileList(Map paramMap) throws Exception;
	
	/**
	 * 해당 항목에 값이 이미 추가되어있는지 확인 
	 * @param paramMap
	 * @throws Exception
	 */
	@SuppressWarnings("rawtypes")
	int selectFlw1200OptCntInfo(Map paramMap) throws Exception;
	
	/**
	 * 작업흐름 추가항목 수정
	 * @param paramMap
	 * @throws Exception
	 */
	@SuppressWarnings("rawtypes")
	void updateFlw1200OtpInfo(Map paramMap) throws Exception;
	
	/**
	 * 작업흐름 추가항목 추가
	 * @param paramMap
	 * @throws Exception
	 */
	@SuppressWarnings("rawtypes")
	void insertFlw1200OtpInfo(Map paramMap) throws Exception;
	
	/**
	 * 작업흐름 추가항목 제거
	 * @param paramMap
	 * @throws Exception
	 */
	@SuppressWarnings("rawtypes")
	void deleteFlw1200OtpInfo(Map paramMap) throws Exception;
	
	/**
	 * 작업흐름 추가 항목 입력
	 * @param paramMap
	 * @throws Exception
	 */
	@SuppressWarnings("rawtypes")
	void insertFlw1300OtpValueInfo(Map paramMap) throws Exception;
	
	/**
	 * 작업흐름 추가 항목 수정
	 * @param paramMap
	 * @throws Exception
	 */
	@SuppressWarnings("rawtypes")
	void updateFlw1300OtpValueInfo(Map paramMap) throws Exception;
	

	/**
	 * 요구사항 리비전 번호 목록 저장
	 * @param paramMap
	 * @throws Exception
	 */
	@SuppressWarnings("rawtypes")
	int insertFlw1400RevisionNumList(Map paramMap) throws Exception;

	/**
	 * 요구사항 리비전 번호 목록 제거
	 * @param paramMap
	 * @throws Exception
	 */
	@SuppressWarnings("rawtypes")
	void deleteFlw1400RevisionNumList(Map paramMap) throws Exception;
	
	/**
	 * 요구사항 리비전 번호 제거
	 * @param paramMap
	 * @throws Exception
	 */
	@SuppressWarnings("rawtypes")
	void deleteFlw1400RevisionNumInfo(Map paramMap) throws Exception;

	/**
	 * 요구사항별 리비전 목록 가져오기(Grid page)
	 * @param Prj1100VO
	 * @throws Exception
	 */
	@SuppressWarnings("rawtypes")
	List selectFlw1400ReqRevisionNumList(Prj1100VO prj1100VO) throws Exception;
	
	/**
	 * 요구사항별 리비전 목록 총 건수(Grid page)
	 * @param Prj1100VO
	 * @return 
	 * @exception Exception
	 */
	int selectFlw1400ReqRevisionNumListCnt(Prj1100VO prj1100VO) throws Exception;
	
	/**
	 * 요구사항별 리비전 단건 갯수 가져오기
	 * @param paramMap
	 * @throws Exception
	 */
	@SuppressWarnings("rawtypes")
	int selectFlw1400ReqRevisionNumCnt(Map paramMap) throws Exception;
	
	/**
	 * 작업흐름별 역할제한 정보 목록 저장
	 * @param paramMap
	 * @throws Exception
	 */
	@SuppressWarnings("rawtypes")
	int insertFlw1500FlowAuthGrpList(Map paramMap) throws Exception;

	/**
	 * 작업흐름별 역할제한 정보 목록 제거
	 * @param paramMap
	 * @throws Exception
	 */
	@SuppressWarnings("rawtypes")
	void deleteFlw1500FlowAuthGrpList(Map paramMap) throws Exception;
	
	/**
	 * 작업흐름별 역할제한 정보 저장
	 * @param paramMap
	 * @throws Exception
	 */
	@SuppressWarnings("rawtypes")
	void insertFlw1500FlowAuthGrpInfo(Map paramMap) throws Exception;
	/**
	 * 작업흐름별 역할제한 정보 제거
	 * @param paramMap
	 * @throws Exception
	 */
	@SuppressWarnings("rawtypes")
	void deleteFlw1500FlowAuthGrpInfo(Map paramMap) throws Exception;
	
	/**
	 * 작업흐름별 역할제한 정보 목록 가져오기
	 * @param paramMap
	 * @throws Exception
	 */
	@SuppressWarnings("rawtypes")
	List selectFlw1500FlowAuthGrpList(Map paramMap) throws Exception;
	
	/**
	 * 작업흐름별 역할제한 정보 단건 갯수 가져오기
	 * @param paramMap
	 * @throws Exception
	 */
	@SuppressWarnings("rawtypes")
	int selectFlw1500FlowAuthGrpCnt(Map paramMap) throws Exception;

	/**
	 * [프로세스 복사] 관리자 권한을 가지고있는 프로젝트의 프로세스 목록
	 * @param paramMap
	 * @throws Exception
	 */
	@SuppressWarnings("rawtypes")
	List selectFlw1000ProcessCopyList(Map paramMap) throws Exception;
	
	/**
	 * [프로세스 복사] prjId, processId로 프로세스 복사 
	 * @param paramMap
	 * @throws Exception
	 */
	@SuppressWarnings("rawtypes")
	void insertPrj1100ProcessCopyInfo(Map paramMap) throws Exception;

	/**
	 * 추가 항목에 존재하는 첨부파일 ID 목록 조회 
	 * @param paramMap
	 * @throws Exception
	 */
	@SuppressWarnings("rawtypes")
	List selectFlw1200FlwOptExistFileIdList(Map paramMap) throws Exception;
}
