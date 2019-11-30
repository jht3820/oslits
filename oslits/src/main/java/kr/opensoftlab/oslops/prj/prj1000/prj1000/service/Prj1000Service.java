package kr.opensoftlab.oslops.prj.prj1000.prj1000.service;

import java.util.List;
import java.util.Map;


/**
 * @Class Name : Prj1000Service.java
 * @Description : Prj1000Service Business class
 * @Modification Information
 *
 * @author 강재민
 * @since 2016.01.23.
 * @version 1.0
 * @see
 *  
 *  Copyright (C) OpenSoftLab Corp. All right reserved.
 */
public interface Prj1000Service {
	/**
	 * Prj1000 프로젝트 생성관리 목록 조회
	 * @param param - Map
	 * @return 
	 * @exception Exception
	 */
	@SuppressWarnings("rawtypes")
	List selectPrj1000View(Map paramMap) throws Exception;
	
	/**
	 * Prj1000 프로젝트 생성관리 단건 조회
	 * @param param - Map
	 * @return 
	 * @exception Exception
	 */
	@SuppressWarnings("rawtypes")
	Map selectPrj1000Info(Map paramMap) throws Exception;
	
	/**
	 * Prj1000 프로젝트 그룹 존재하는지 체크
	 * @param param - Map
	 * @return 
	 * @exception Exception
	 */
	@SuppressWarnings("rawtypes")
	List selectPrj1000PrjGrpExistCheck(Map paramMap) throws Exception;
	
	/**
	 * Prj1000 프로젝트 그룹 ID에 해당하는 프로젝트가 존재하는지 체크
	 * @param param - Map
	 * @return 
	 * @exception Exception
	 */
	@SuppressWarnings("rawtypes")
	List selectPrj1000PrjGrpIdExistCheck(Map paramMap) throws Exception;
	
	/**
	 * Prj1000 옵션정보 페이지에 접속 권한이 있는 프로젝트 목록 가져오기
	 * @param param - Map
	 * @return 
	 * @exception Exception
	 */
	@SuppressWarnings("rawtypes")
	List selectPrj1000AuthGrpAndMenuIdPrjListAjax(Map paramMap) throws Exception;
	
	/**
	 * Prj1000 프로젝트 그룹 생성(insert) AJAX
	 * @param Map
	 * @exception Exception
	 */
	@SuppressWarnings("rawtypes")
	String insertPrj1000PrjGrpAjax(Map paramMap) throws Exception;
	
	/**
	 * Prj1000 프로젝트 생성관리 등록(insert) AJAX
	 * @param Map
	 * @return 프로젝트 ID
	 * @exception Exception
	 */
	@SuppressWarnings("rawtypes")
	String insertPrj1001Ajax(Map paramMap) throws Exception;
	
	
	
	/**
	 * Prj1000 프로젝트 생성관리 등록(update) AJAX
	 * @param Map
	 * @return update row
	 * @exception Exception
	 */
	@SuppressWarnings("rawtypes")
	int updatePrj1000Ajax(Map paramMap) throws Exception;
	
	/**
	 * Prj1000 프로젝트 그룹 제거(delete) AJAX
	 * @param Map
	 * @return delete row
	 * @exception Exception
	 */
	@SuppressWarnings("rawtypes")
	void deletePrj1000PrjGrpAjax(Map paramMap) throws Exception;
	
	/**
	 * Prj1000 프로젝트 생성관리 등록(delete) AJAX
	 * @param Map
	 * @return delete row
	 * @exception Exception
	 */
	@SuppressWarnings("rawtypes")
	void deletePrj1001Ajax(Map paramMap) throws Exception;
	
	List<Map> selectPrj1000ProjectGroupListAjax(Map<String, String> paramMap);
	
	int selectPrj1000ProjectAcronymCount(Map paramMap) throws Exception;
	

	/**
	 * Prj1000 관리 권한 있는 프로젝트 목록 검색
	 * @param param - Map
	 * @return 
	 * @exception Exception
	 */
	@SuppressWarnings("rawtypes")
	List selectPrj1000AdminPrjList(Map paramMap) throws Exception;
	
	/**
	 * Prj1000 프로젝트 생성 마법사
	 * @param param - Map
	 * @return 
	 * @exception Exception
	 */
	@SuppressWarnings("rawtypes")
	String insertPrj1000WizardProject(Map paramMap) throws Exception;
}
