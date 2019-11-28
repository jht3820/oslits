package kr.opensoftlab.oslops.cmm.cmm9000.cmm9200.service.impl;

import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import kr.opensoftlab.oslops.cmm.cmm9000.cmm9200.service.Cmm9200Service;

import org.springframework.stereotype.Service;

import egovframework.rte.fdl.cmmn.EgovAbstractServiceImpl;

/**
 * @Class Name : Cmm9200ServiceImpl.java
 * @Description : Cmm9200ServiceImpl Business Implement class
 * @Modification Information
 *
 * @author 정형택
 * @since 2016.01.14.
 * @version 1.0
 * @see
 *  
 *  Copyright (C)  All right reserved.
 */

@Service("cmm9200Service")
public class Cmm9200ServiceImpl extends EgovAbstractServiceImpl implements Cmm9200Service {

    @Resource(name="cmm9200DAO")
    private Cmm9200DAO cmm9200DAO;
    
	/**
	 * 공통으로 사용할 사용자 정보 목록을 조회한다.(콤보박스용)
	 * @param param - 조회할 정보가 담긴 Map
	 * @return 사용자 정보 목록
	 * @exception Exception
	 */
	@SuppressWarnings("rawtypes")
	public List selectCmm9200PrjUsrList(Map param) throws Exception{
		return cmm9200DAO.selectCmm9200PrjUsrList(param);
	}

}
