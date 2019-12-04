package kr.opensoftlab.oslops.cmm.cmm1000.cmm1400.service.impl;

import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import kr.opensoftlab.oslops.cmm.cmm1000.cmm1300.service.Cmm1300Service;
import kr.opensoftlab.oslops.cmm.cmm1000.cmm1400.service.Cmm1400Service;
import kr.opensoftlab.oslops.com.vo.LoginVO;
import kr.opensoftlab.sdf.excel.ExcelDataListResultHandler;

import org.springframework.stereotype.Service;

import egovframework.rte.fdl.cmmn.EgovAbstractServiceImpl;

/**
 * @Class Name : Cmm1400ServiceImpl.java
 * @Description : Cmm1400ServiceImpl Business Implement class
 * @Modification Information
 *
 * @author 공대영	
 * @since 2018.07.31
 * @version 1.0
 * @see
 *  
 *  Copyright (C)  All right reserved.
 */
@Service("cmm1400Service")
public class Cmm1400ServiceImpl  extends EgovAbstractServiceImpl implements Cmm1400Service{

	/** Cmm1400DAO DI */
    @Resource(name="cmm1400DAO")
    private Cmm1400DAO cmm1400DAO;

	@Override
	public List<Map> selectCmm1400SVNRepositoryList(Map<String, String> paramMap) {
		// TODO Auto-generated method stub
		return cmm1400DAO.selectCmm1400SVNRepositoryList(paramMap);
	}
    
   

}
