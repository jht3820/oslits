package kr.opensoftlab.oslops.cmm.cmm1000.cmm1800.service.impl;



import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import kr.opensoftlab.oslops.cmm.cmm1000.cmm1700.service.Cmm1700Service;
import kr.opensoftlab.oslops.cmm.cmm1000.cmm1700.vo.Cmm1700VO;
import kr.opensoftlab.oslops.cmm.cmm1000.cmm1800.service.Cmm1800Service;

import org.springframework.stereotype.Service;

import egovframework.rte.fdl.cmmn.EgovAbstractServiceImpl;

/**
 * @Class Name : Cmm1700ServiceImpl.java
 * @Description : Cmm1700ServiceImpl Business Implement class
 * @Modification Information
 *
 * @author 공대영
 *  * @since 2018.10.16.
 * @version 1.0
 * @see
 *  
 *  Copyright (C)  All right reserved.
 */

@Service("cmm1800Service")
public class Cmm1800ServiceImpl extends EgovAbstractServiceImpl implements Cmm1800Service {
	/** DAO Bean Injection */
    @Resource(name="cmm1800DAO")
    private Cmm1800DAO cmm1800DAO;

	@Override
	public List<Map> selectCmm1800ProcessList(Map<String, String> paramMap)
			throws Exception {
		// TODO Auto-generated method stub
		return cmm1800DAO.selectCmm1800ProcessList(paramMap);
	}

    /**
	 * 역할 조회 공통 목록 조회 
	 */
	
	
}
