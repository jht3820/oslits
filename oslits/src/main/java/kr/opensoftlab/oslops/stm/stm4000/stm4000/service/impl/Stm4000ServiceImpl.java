package kr.opensoftlab.oslops.stm.stm4000.stm4000.service.impl;

import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import org.springframework.stereotype.Service;

import egovframework.rte.fdl.cmmn.EgovAbstractServiceImpl;
import kr.opensoftlab.oslops.stm.stm4000.stm4000.service.Stm4000Service;


/**
 * @Class Name : Stm4000ServiceImpl.java
 * @Description : Stm4000ServiceImpl Business Implement class
 * @Modification Information
 *
 * @author 배용진
 * @since 2019.05.08.
 * @version 1.0
 * @see
 *  
 *  Copyright (C) OpenSoftLab Corp. All right reserved.
 */
@Service("stm4000Service")
public class Stm4000ServiceImpl extends EgovAbstractServiceImpl implements Stm4000Service {
	

	/** Stm4000DAO DI */
    @Resource(name="stm4000DAO")
    private Stm4000DAO stm4000DAO;	

	/**
	 * Stm4000 라이선스 그룹에 있는 모든 프로젝트 목록을 조회한다.
	 * @param param - Map
	 * @return list - 라이선스 그룹의 프로젝트 목록
	 * @exception Exception
	 */
   	@SuppressWarnings("rawtypes")
   	public List selectStm4000LicGrpPrjList(Map paramMap) throws Exception {
   		return stm4000DAO.selectStm4000LicGrpPrjList(paramMap) ;
   	} 	
	
	/**
	 * Stm4000  관리 권한 있는 라이선스 그룹 내 모든 프로젝트 목록 검색
	 * @param param - Map
	 * @return 
	 * @exception Exception
	 */
	@SuppressWarnings("rawtypes")
	public List selectStm4000LicGrpAdminPrjList(Map paramMap) throws Exception {
		return stm4000DAO.selectStm4000LicGrpAdminPrjList(paramMap);
	}
}








