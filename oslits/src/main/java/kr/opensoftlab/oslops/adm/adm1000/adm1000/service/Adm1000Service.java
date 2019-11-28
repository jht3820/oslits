package kr.opensoftlab.oslops.adm.adm1000.adm1000.service;

import java.util.List;
import java.util.Map;

/**
 * @Class Name : Adm1000Service.java
 * @Description : Adm1000Service Business class
 * @Modification Information
 *
 * @author 정형택
 * @since 2016.01.12.
 * @version 1.0
 * @see
 *  
 *  Copyright (C) OpenSoftLab Corp. All right reserved.
 */
public interface Adm1000Service {
	
	
	/**
	 * Adm1000 라이선스 그룹에 할당된 기본 메뉴정보 목록 가져오기
	 * @param param - Map
	 * @return 
	 * @exception Exception
	 */
	@SuppressWarnings("rawtypes")
	List selectAdm1000BaseMenuList(Map paramMap) throws Exception;	
	
	/**
	 * Adm1000 라이선스 그룹에 할당된 선택한 롤에 배정된 기본 메뉴정보 목록 가져오기
	 * @param param - Map
	 * @return 
	 * @exception Exception
	 */
	@SuppressWarnings("rawtypes")
	List selectAdm1000AuthBaseMenuList(Map paramMap) throws Exception;	
	
	/**
	 * Adm1000 프로젝트에 생성되어 있는 권한그룹 목록 가져오기
	 * @param param - Map
	 * @return 
	 * @exception Exception
	 */
	@SuppressWarnings("rawtypes")
	List selectAdm1000PrjAuthGrpList(Map paramMap) throws Exception;	
	
	/**
	 * Adm1000 대분류에 속한 중분류 제외한 소분류 갯수 및 대분류 정보 목록 조
	 * @param param - Map
	 * @return 
	 * @exception Exception
	 */
	@SuppressWarnings("rawtypes")
	List selectAdm1000AuthGrpSmallMenuList(Map paramMap) throws Exception;	
	
	/**
	 * Adm1000 메뉴 정보 조회
	 * @param param - Map
	 * @return 
	 * @exception Exception
	 */
	@SuppressWarnings("rawtypes")
	Map selectAdm1000MenuInfo(Map paramMap) throws Exception;	
	
	/**
	 * Adm1000 메뉴정보 등록(단건) AJAX
	 * 메뉴정보 등록후 등록 정보 결과 및 정보를 리턴한다.
	 * @param 
	 * @return 
	 * @exception Exception
	 */
	@SuppressWarnings("rawtypes")
	Map insertAdm1000MenuInfo(Map paramMap) throws Exception;
	
	/**
	 * Adm1000 메뉴정보 삭제(단건) AJAX
	 * 메뉴정보 삭제
	 * @param 
	 * @return 
	 * @exception Exception
	 */
	@SuppressWarnings("rawtypes")
	void deleteAdm1000MenuInfo(Map paramMap) throws Exception;
	
	/**
	 * Adm1000 메뉴정보 수정(단건) AJAX
	 * 메뉴정보 수정
	 * @param 
	 * @return 
	 * @exception Exception
	 */
	@SuppressWarnings("rawtypes")
	void updateAdm1000MenuInfo(Map paramMap) throws Exception;
	
	/**
	 * Adm1000 메뉴권한 수정(다건) AJAX
	 * 메뉴권한 수정
	 * @param 
	 * @return 
	 * @exception Exception
	 */
	@SuppressWarnings("rawtypes")
	void saveAdm1000AuthGrpMenuAuthListAjax(List paramList) throws Exception;
	
	/**
	 * Adm1000 신규 권한 등록전 동일한 PK가 있는지 체크) AJAX
	 * 신규권한 등록전 동일한 PK가 있는지 체크
	 * @param 
	 * @return 
	 * @exception Exception
	 */
	@SuppressWarnings("rawtypes")
	Map selectAdm1000DupChkAuthGrpId(Map paramMap) throws Exception;	
	
	/**
	 * Adm1000 신규 권한 등록(단건) AJAX
	 * 신규권한 등록
	 * @param 
	 * @return 
	 * @exception Exception
	 */
	@SuppressWarnings("rawtypes")
	void insertAdm1000AuthGrpInfoAjax(Map paramMap) throws Exception;
	
	/**
	 * Adm1000 권한그룹 삭제
	 * @param param - Map
	 * @return int
	 * @exception Exception
	 */
	@SuppressWarnings("rawtypes")
	void deleteAdm1000AuthGrpInfoAjax(Map paramMap) throws Exception;
	
	/**
	 * Adm1000 권한 조회 (단건) AJAX
	 * 신규권한 조회
	 * @param 
	 * @return 
	 * @exception Exception
	 */
	@SuppressWarnings("rawtypes")
	Map selectAdm1000AuthGrpInfoAjax(Map paramMap) throws Exception;
}
