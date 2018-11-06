package kr.opensoftlab.oslits.dpl.dpl1000.dpl1000.service;

import java.util.List;
import java.util.Map;

import kr.opensoftlab.oslits.dpl.dpl1000.dpl1000.vo.Dpl1000VO;
import kr.opensoftlab.sdf.excel.ExcelDataListResultHandler;

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
	 * Dpl1000 배포 버전 리스트 조회
	 * @param dpl1000VO  - Dpl1000VO 
	 * @return 
	 * @exception Exception
	 */
	List<Dpl1000VO> selectDpl1000DeployVerInfoList(Dpl1000VO dpl1000VO) throws Exception;
	
	/**
	 * Dpl1000 배포 버전 단일 정보 조회 
	 * @param map  - Map
	 * @return 
	 * @exception Exception
	 */
	Map selectDpl1000DeployVerInfo(Map map) throws Exception;
	
    /**
	 * Dpl1000 배포 버전별 요구사항 등록 카운트
	 * @param
	 * @return 배포 버전별 요구사항 수
	 * @exception Exception
	 */
	@SuppressWarnings("rawtypes")
	public List selectDpl1000ReqCount(Map inputMap) throws Exception;
	/**
	
	/**
	 * Dpl1000 배포 버전 등록
	 * @param param - Map
	 * @return 
	 * @exception Exception
	 */
	@SuppressWarnings("rawtypes")
	public void insertDpl1000DeployVerInfo(Map paramMap) throws Exception;
	
	
	/**
	 * Dpl1000 배포 버전 등록
	 * @param param - Map
	 * @return 
	 * @exception Exception
	 */
	@SuppressWarnings("rawtypes")
	public void updateDpl1000DeployVerInfo(Map paramMap) throws Exception;
	
	/**
	 * Dpl1000 배포 버전 리스트 삭제
	 * @param param - Map
	 * @return 
	 * @exception Exception
	 */
	@SuppressWarnings("rawtypes")
	public void deleteDpl1000DeployVerInfoList(Map paramMap) throws Exception;
	
	/**
	 * Dpl1000 배포 버전 리스트 총 건수
	 * @param Dpl1000VO - dpl1000VO
	 * @return 
	 * @exception Exception
	 */
	public int selectDpl1000ListCnt(Dpl1000VO dpl1000VO) throws Exception;
	
	
	/**
	 * 배포 버전 요구사항 목록을 엑셀 조회를 한다.
	 * @params dpl1000VO
	 * @return List 배포 버전 요구사항 목록
	 * @throws Exception
	 */
	void selectDpl1000ExcelList(Dpl1000VO dpl1000vo,ExcelDataListResultHandler resultHandler) throws Exception;

	int selectDpl1000BuildInfoListCnt(Dpl1000VO dpl1000vo) throws Exception;

	List<Dpl1000VO> selectDpl1000BuildInfoList(Dpl1000VO dpl1000vo) throws Exception;

	Map selectDpl1000BuildDetail(Map paramMap) throws Exception;

}
