package kr.opensoftlab.oslops.stm.stm4000.stm4000.service;

import java.util.List;
import java.util.Map;


/**
 * @Class Name : Stm4000Service.java
 * @Description : Stm4000Service Business class
 * @Modification Information
 *
 * @author 배용진
 * @since 2019.05.08.
 * @version 1.0
 * @see
 *  
 *  Copyright (C) OpenSoftLab Corp. All right reserved.
 */
public interface Stm4000Service {
	
	/**
	 * Stm4000 라이선스 그룹에 있는 모든 프로젝트 목록을 조회한다.
	 * @param param - Map
	 * @return list - 라이선스 그룹의 프로젝트 목록
	 * @exception Exception
	 */
	@SuppressWarnings("rawtypes")
	List selectStm4000LicGrpPrjList(Map paramMap) throws Exception;

	/**
	 * Stm4000  관리 권한 있는 라이선스 그룹 내 모든 프로젝트 목록 검색
	 * @param param - Map
	 * @return 
	 * @exception Exception
	 */
	@SuppressWarnings("rawtypes")
	List selectStm4000LicGrpAdminPrjList(Map paramMap) throws Exception;
	
}
