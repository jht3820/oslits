package kr.opensoftlab.oslops.dsh.dsh2000.dsh2000.service;

/**
 * @Class Name : Dsh2000Service.java
 * @Description : Dsh2000Service Service class
 * @Modification Information
 *
 * @author 진주영
 * @since 2018.09.20.
 * @version 1.0
 * @see
 *  
 *  Copyright (C) OpenSoftLab Corp All right reserved.
 */

import java.util.List;
import java.util.Map;

import kr.opensoftlab.oslops.dsh.dsh2000.dsh2000.vo.Dsh2000VO;


public interface Dsh2000Service {
	
	/**
	 * 대시보드 프로젝트 정보 단건 조회
	 * @param paramMap
	 * @throws Exception
	 */
	@SuppressWarnings("rawtypes")
	Map selectDsh2000PrjReqAllInfo(Map paramMap) throws Exception;
	
	/**
	 * 대시보드 프로세스 목록 및 진행상태 조회
	 * @param paramMap
	 * @return List
	 * @throws Exception
	 */
	@SuppressWarnings("rawtypes")
	List selectDsh2000ProcessReqRatioList(Map paramMap) throws Exception;
	
	/**
	 * 대시보드 작업흐름 목록 및 진행현황 조회
	 * @param paramMap
	 * @return List
	 * @throws Exception
	 */
	@SuppressWarnings("rawtypes")
	List selectDsh2000FlowList(Map paramMap) throws Exception;
	
	/**
	 * 대시보드 요구사항 계획대비 미처리 건수(종료예정기간 < NVL(종료기간,현재시간))
	 * @param paramMap
	 * @return List
	 * @throws Exception
	 */
	@SuppressWarnings("rawtypes")
	List selectDsh2000ReqDtmOverList(Map paramMap) throws Exception;
	
	/**
	 * 대시보드 산출물 제출예정일 대비 미제출 건수
	 * @param paramMap
	 * @return List
	 * @throws Exception
	 */
	@SuppressWarnings("rawtypes")
	List selectDsh2000DocDtmOverList(Map paramMap) throws Exception;
	
	/**
	 * 대시보드 프로세스별 기간 비교 경고
	 * @param paramMap
	 * @return List
	 * @throws Exception
	 */
	@SuppressWarnings("rawtypes")
	List selectDsh2000ReqDtmOverAlertList(Map paramMap) throws Exception;
	
	/**
	 * 프로세스 별 계획대비 미처리 건수 요구사항 목록
	 * 계획대비 미처리 건수 차트 클릭 시 나오는 팝업에 사용
	 * @param Dsh1000VO
	 * @return List
	 * @throws Exception
	 */
	@SuppressWarnings("rawtypes")
	List selectDsh2000ProcessReqDtmOverList(Dsh2000VO dsh2000VO) throws Exception;

	/**
	 * 프로세스 별 계획대비 미처리 건수 요구사항 목록 총 건수
	 * 계획대비 미처리 건수 차트 클릭 시 나오는 팝업에 사용
	 * @param Dsh1000VO
	 * @return List
	 * @throws Exception
	 */
	int selectDsh2000ProcessReqDtmOverListCnt(Dsh2000VO dsh2000VO) throws Exception;
}
