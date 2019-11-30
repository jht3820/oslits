package kr.opensoftlab.oslops.dpl.dpl1000.dpl1000.service;

import java.util.List;
import java.util.Map;

import kr.opensoftlab.oslops.dpl.dpl1000.dpl1000.vo.Dpl1000VO;
import kr.opensoftlab.oslops.dpl.dpl1000.dpl1000.vo.Dpl1300VO;
import kr.opensoftlab.sdf.excel.ExcelDataListResultHandler;
import kr.opensoftlab.sdf.jenkins.vo.BuildVO;

/**
 * @Class Name : Dpl1000Service.java
 * @Description : Dpl1000Service Business class
 * @Modification Information
 *
 * @author 정형택
 * @since 2016.01.30
 * @version 1.0
 * @see
 *  
 *  --------------------------------------
 *  수정일			수정자			수정내용
 *  --------------------------------------
 *  2019-03-07		진주영		 	기능 개선
 *  
 *  
 *  --------------------------------------
 *  
 *  Copyright (C)  All right reserved.
 */

public interface Dpl1000Service {

	/**
	 * Dpl1000 배포자 리스트 등록
	 * @param param - Map
	 * @return 
	 * @exception Exception
	 */
	@SuppressWarnings("rawtypes")
	List selectDpl1000DeployNmList(Map inputMap) throws Exception;
	
	/**
	 * Dpl1000배포 계획 정보 일반 목록(No Page) 가져오기
	 * @param param - Map
	 * @return list 배포 계획 리스트
	 * @throws Exception
	 */
	@SuppressWarnings({"rawtypes" })
	List selectDpl1000DeployVerNormalList(Map inputMap)  throws Exception;
	
	/**
	 * Dpl1000 배포 계획 리스트 조회
	 * @param dpl1000VO  - Dpl1000VO 
	 * @return 
	 * @exception Exception
	 */
	List<Dpl1000VO> selectDpl1000DeployVerInfoList(Dpl1000VO dpl1000VO) throws Exception;
	
	/**
	 * Dpl1000 배포 계획 단일 정보 조회 
	 * @param map  - Map
	 * @return 
	 * @exception Exception
	 */
	@SuppressWarnings("rawtypes")
	Map selectDpl1000DeployVerInfo(Map map) throws Exception;
	
    /**
	 * Dpl1000 배포 계획별 요구사항 등록 카운트
	 * @param
	 * @return 배포 계획별 요구사항 수
	 * @exception Exception
	 */
	@SuppressWarnings("rawtypes")
	List selectDpl1000ReqCount(Map inputMap) throws Exception;
	/**
	 * Dpl1300 배포 계획에 배정된 JOB 목록
	 * @param param - Map
	 * @throws Exception
	 */
	@SuppressWarnings({"rawtypes" })
	List selectDpl1300DeployJobList(Map inputMap)  throws Exception;
	
	/**
	 * Dpl1300배포 계획 배정 JOB 목록 가져오기 (그리드)
	 * @param param - Map
	 * @return list 배포 계획 리스트
	 * @throws Exception
	 */
	List<Dpl1300VO> selectDpl1300dplJobGridList(Dpl1300VO dpl1300VO)  throws Exception;
	
	/**
	 * Dpl1300 배포 계획 배정 JOB 목록 리스트 총건수
	 * @return 
	 * @exception Exception
	 */
	int selectDpl1300dplJobGridListCnt(Dpl1300VO dpl1300VO) throws Exception;
	
	/**
	 * Dpl1000 배포 계획 등록
	 * @param param - Map
	 * @return 
	 * @exception Exception
	 */
	@SuppressWarnings("rawtypes")
	void insertDpl1000DeployVerInfo(Map paramMap) throws Exception;
	
	
	/**
	 * Dpl1000 배포 계획 수정
	 * @param param - Map
	 * @return 
	 * @exception Exception
	 */
	@SuppressWarnings("rawtypes")
	void updateDpl1000DeployVerInfo(Map paramMap) throws Exception;
	
	/**
	 * Dpl1000 배포 계획 배포 상태 수정
	 * @param param - Map
	 * @return 
	 * @exception Exception
	 */
	@SuppressWarnings("rawtypes")
	void updateDpl1000DplStsCdInfo(Map paramMap) throws Exception;
	
	/**
	 * Dpl1000 배포 계획 삭제
	 * @param param - Map
	 * @return 
	 * @exception Exception
	 */
	@SuppressWarnings("rawtypes")
	void deleteDpl1000DeployVerInfo(Map paramMap) throws Exception;
	
	/**
	 * Dpl1000 배포 계획 리스트 총 건수
	 * @param Dpl2000VO - dpl1000VO
	 * @return 
	 * @exception Exception
	 */
	int selectDpl1000ListCnt(Dpl1000VO dpl1000VO) throws Exception;
	
	
	/**
	 * 배포 계획 요구사항 목록을 엑셀 조회를 한다.
	 * @params dpl1000VO
	 * @return List 배포 계획 요구사항 목록
	 * @throws Exception
	 */
	void selectDpl1000ExcelList(Dpl1000VO dpl1000vo,ExcelDataListResultHandler resultHandler) throws Exception;

	int selectDpl1000BuildInfoListCnt(Dpl1000VO dpl1000vo) throws Exception;

	List<Dpl1000VO> selectDpl1000BuildInfoList(Dpl1000VO dpl1000vo) throws Exception;


	/**
	 * Dpl1400 배포 계획에 배정된 JOB에 해당하는 배포 실행 이력 조회 
	 * @param param - Map
	 * @throws Exception
	 */
	@SuppressWarnings({"rawtypes" })
	Map selectDpl1400DplJobBuildInfo(Map map)  throws Exception;
	
	/**
	 * Dpl1400 배포 계획에 배정된 JOB에 해당하는 배포 실행 이력 단건 조회 
	 * @param param - Map
	 * @throws Exception
	 */
	@SuppressWarnings({"rawtypes" })
	Map selectDpl1400DplSelBuildInfoAjax(Map map)  throws Exception;
	
	/**
	 * Dpl1300 배포계획에 등록된 Job 삭제 
	 * @param param - Map
	 * @throws Exception
	 */
	@SuppressWarnings("rawtypes")
	void deleteDpl1300DplJobList(Map paramMap)  throws Exception;
	
	
	/**
	 * Dpl1400 배포 계획 실행 이력 등록
	 * @param param - Map
	 * @return 
	 * @exception Exception
	 */
	void insertDpl1400DeployJobBuildLogInfo(BuildVO buildVo) throws Exception;
	
	/**
	 * Dpl1300 dpl1300 빌드에서 stm3000 Job정보 불러올때 사용
	 * @param param - Map
	 * @throws Exception
	 */
	@SuppressWarnings({"rawtypes" })
	Map selectDpl1300ToStm3000JobInfo(Map map)  throws Exception;
	
	
	/**
	 * Dpl1000 배포 계획 실행,수정,결재 이력
	 * @param param - Map
	 * @throws Exception
	 */
	@SuppressWarnings({"rawtypes" })
	List selectDpl1000DplHistoryList(Map inputMap)  throws Exception;
	
	
	/**
	 * Dpl1000 배포 계획 결재 요청
	 * @param param - Map
	 * @throws Exception
	 */
	@SuppressWarnings({"rawtypes" })
	void insertDpl1000DplSignRequestList(Map paramMap)  throws Exception;
	
	/**
	 * Dpl1000 Job 빌드 목록 조회
	 * @param param - Map
	 * @throws Exception
	 */
	@SuppressWarnings({"rawtypes" })
	List selectDpl1400DplBldNumList(Map inputMap)  throws Exception;
	
	/**
	 * Dpl1500 배포계획 수정이력 CHG_ID 구하기
	 * @param param - Map
	 * @return 
	 * @exception Exception
	 */	
	@SuppressWarnings("rawtypes")
	String selectDpl1500NewChgId(Map paramMap) throws Exception;
	
	/**
	 * Dpl1500 배포계획 수정이력 등록
	 * @param param - Map
	 * @return 
	 * @exception Exception
	 */	
	@SuppressWarnings("rawtypes")
	String insertDpl1500ModifyHistoryInfo(Map paramMap) throws Exception;
	
	/**
	 * Dpl1500 배포계획 항목 수정이력 등록
	 * @param param - Map
	 * @return 
	 * @exception Exception
	 */	
	@SuppressWarnings("rawtypes")
	void insertDpl1500DplInfoModifyList(Map paramMap) throws Exception;
	
	/**
	 * Dpl1500 배포계획 수정이력 목록 조회
	 * @param param - Map
	 * @return 
	 * @exception Exception
	 */	
	@SuppressWarnings("rawtypes")
	List selectDpl1500ModifyHistoryList(Map paramMap) throws Exception;
	
	/**
	 * Dpl1000 모든 배포계획 자동배포 목록 가져오기
	 * @param param - Map
	 * @return 
	 * @exception Exception
	 */	
	@SuppressWarnings("rawtypes")
	List selectDpl1000AllDplList(Map paramMap) throws Exception;
}
