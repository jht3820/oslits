package kr.opensoftlab.oslops.prj.prj2000.prj2000.service;

import java.util.List;
import java.util.Map;

/**
 * @Class Name : Prj2000Service.java
 * @Description : Prj2000Service Business class
 * @Modification Information
 *
 * @author 정형택
 * @since 2016.01.26.
 * @version 1.0
 * @see
 *  
 *  Copyright (C) OpenSoftLab Corp. All right reserved.
 */
public interface Prj2000Service {

	/**
	 * Prj2000 프로젝트에 생성되어 있는 권한그룹 목록 가져오기
	 * @param param - Map
	 * @return 
	 * @exception Exception
	 */
	@SuppressWarnings("rawtypes")
	List selectPrj2000PrjAuthGrpList(Map paramMap) throws Exception;	
	
	/**
	 * Prj2000 대분류에 속한 중분류 제외한 소분류 갯수 및 대분류 정보 목록 조
	 * @param param - Map
	 * @return 
	 * @exception Exception
	 */
	@SuppressWarnings("rawtypes")
	List selectPrj2000AuthGrpSmallMenuList(Map paramMap) throws Exception;
	
	/**
	 * Prj2000 신규 권한 등록전 동일한 PK가 있는지 체크) AJAX
	 * 신규권한 등록전 동일한 PK가 있는지 체크
	 * @param 
	 * @return 
	 * @exception Exception
	 */
	@SuppressWarnings("rawtypes")
	Map selectPrj2000DupChkAuthGrpId(Map paramMap) throws Exception;	
	
	/**
	 * Prj2000 신규 권한 등록(단건) AJAX
	 * 신규권한 등록
	 * @param 
	 * @return 
	 * @exception Exception
	 */
	@SuppressWarnings("rawtypes")
	String insertPrj2000AuthGrpInfoAjax(Map paramMap) throws Exception;
	
	/**
	 * Prj2000 권한그룹 삭제
	 * @param param - Map
	 * @return int
	 * @exception Exception
	 */
	@SuppressWarnings("rawtypes")
	void deletePrj2000AuthGrpInfoAjax(Map paramMap) throws Exception;
	
	
	/**
	 * Prj2000 메뉴권한 저장(다건) AJAX
	 * 메뉴권한 수정
	 * @param 
	 * @return 
	 * @exception Exception
	 */
	@SuppressWarnings("rawtypes")
	void savePrj2000AuthGrpMenuAuthListAjax(List paramList) throws Exception;
	
	/**
	 * Prj2000 배정된 사용자 목록 조회 AJAX
	 * 배정된 사용자 목록 조회
	 * @param 
	 * @return 
	 * @exception Exception
	 */
	@SuppressWarnings("rawtypes")
	List selectPrj2000UsrAddListAjax(Map paramMap) throws Exception;
	
	/**
	 * Prj2000 배정된 사용자 목록  갯수 조회 AJAX
	 * 배정된 사용자 목록 조회
	 * @param 
	 * @return 
	 * @exception Exception
	 */
	@SuppressWarnings("rawtypes")
	int selectPrj2000UsrCntAjax(Map paramMap) throws Exception;
	
	/**
	 * Prj2000 라이선스 그룹내 전체 사용자 목록 조회 AJAX
	 * 라이선스 그룹내 전체 사용자 목록 조회
	 * @param 
	 * @return 
	 * @exception Exception
	 */
	@SuppressWarnings("rawtypes")
	List selectPrj2000UsrAllListAjax(Map paramMap) throws Exception;
	
	/**
	 * Prj2000 배정 삭제된 사용자 목록 조회 AJAX
	 * 배정 삭제된 사용자 목록조회
	 * @param 
	 * @return 
	 * @exception Exception
	 */
	@SuppressWarnings("rawtypes")
	List selectPrj2000DelUsrInfoListAjax(Map paramMap) throws Exception;
	
	/**
	 * Prj2000 사용자 권한 배정 및 삭제 처리 AJAX
	 * 사용자 권한 배정 및 삭제 처리
	 * @param 
	 * @return 
	 * @exception Exception
	 */
	@SuppressWarnings("rawtypes")
	void savePrj2000PrjUsrAuthListAjax(Map paramMap) throws Exception;

	@SuppressWarnings("rawtypes")
	Map selectPrj2000AuthGrpInfoAjax(Map paramMap) throws Exception;

	@SuppressWarnings("rawtypes")
	int updatePrj2000AuthGrpInfoAjax(Map paramMap) throws Exception;

	/**
	 * Prj2000 [역할그룹 복사] 관리자 권한을 가지고있는 프로젝트의 역할그룹 목록
	 * @param 
	 * @return 
	 * @exception Exception
	 */
	@SuppressWarnings("rawtypes")
	List selectPrj2000AuthGrpCopyList(Map paramMap) throws Exception;
	
	/**
	 * Prj2000 역할그룹 추가 시 현재 프로젝트의 역할그룹 최고 순번+1 값을 을 가져온다.
	 * 역할그룹 관리 - 역할그룹 등록 팝업에 사용
	 * @param 
	 * @return 
	 * @exception Exception
	 */
	@SuppressWarnings("rawtypes")
	int selectPrj2000AuthGrpNextOrd(Map paramMap) throws Exception;
}
