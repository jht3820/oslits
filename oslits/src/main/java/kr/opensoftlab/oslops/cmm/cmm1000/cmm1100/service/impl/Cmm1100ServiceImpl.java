package kr.opensoftlab.oslops.cmm.cmm1000.cmm1100.service.impl;

import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import kr.opensoftlab.oslops.cmm.cmm1000.cmm1100.service.Cmm1100Service;

import org.springframework.stereotype.Service;

import egovframework.rte.fdl.cmmn.EgovAbstractServiceImpl;


/**
 * @Class Name : Cmm1100ServiceImpl.java
 * @Description : Cmm1100ServiceImpl Business Implement class
 * @Modification Information
 *
 * @author 공대영
 * @since 2018.09.11.
 * @version 1.0
 * @see
 *  
 *  Copyright (C) OpenSoftLab Corp. All right reserved.
 */
@Service("cmm1100Service")
public class Cmm1100ServiceImpl extends EgovAbstractServiceImpl implements Cmm1100Service {
	/** Adm1000DAO DI */
    @Resource(name="cmm1100DAO")
    private Cmm1100DAO cmm1100DAO;
    
    /**
   	 * Prj1000 프로젝트 생성관리 목록 조회
   	 * @param param - Map
   	 * @return 
   	 * @exception Exception
   	 */
   	@SuppressWarnings("rawtypes")
   	public List selectCmm1100View(Map paramMap) throws Exception {
   		return cmm1100DAO.selectCmm1100View(paramMap) ;
   	}
   	
   	
}