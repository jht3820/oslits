package kr.opensoftlab.oslops.cmm.cmm1000.cmm1300.service.impl;

import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import kr.opensoftlab.oslops.cmm.cmm1000.cmm1300.service.Cmm1300Service;
import kr.opensoftlab.oslops.com.vo.LoginVO;
import kr.opensoftlab.sdf.excel.ExcelDataListResultHandler;

import org.springframework.stereotype.Service;

import egovframework.rte.fdl.cmmn.EgovAbstractServiceImpl;

/**
 * @Class Name : Adm4100ServiceImpl.java
 * @Description : Adm4100ServiceImpl Business Implement class
 * @Modification Information
 *
 * @author 공대영	
 * @since 2018.07.31
 * @version 1.0
 * @see
 *  
 *  Copyright (C)  All right reserved.
 */
@Service("cmm1300Service")
public class Cmm1300ServiceImpl  extends EgovAbstractServiceImpl implements Cmm1300Service{

	/** Cmm1300DAO DI */
    @Resource(name="cmm1300DAO")
    private Cmm1300DAO cmm1300DAO;
    
    /**
	 * Adm4000 공통코드 마스터 조회
	 * @param
	 * @return 공통코드 마스터 목록
	 * @exception Exception
	 */
    @Override
	@SuppressWarnings("rawtypes")
	public List selectCmm1300List(Map inputMap) throws Exception {
		return cmm1300DAO.selectCmm1300List(inputMap);
    }
	

}
