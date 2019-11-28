package kr.opensoftlab.oslops.cmm.cmm9000.cmm9100.service.impl;

import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import kr.opensoftlab.oslops.cmm.cmm9000.cmm9100.service.Cmm9100Service;

import org.springframework.stereotype.Service;

import egovframework.rte.fdl.cmmn.EgovAbstractServiceImpl;

/**
 * @Class Name : Cmm9100ServiceImpl.java
 * @Description : Cmm9100ServiceImpl Business Implement class
 * @Modification Information
 *
 * @author 정형택
 * @since 2016.01.14.
 * @version 1.0
 * @see
 *  
 *  Copyright (C)  All right reserved.
 */

@Service("cmm9100Service")
public class Cmm9100ServiceImpl extends EgovAbstractServiceImpl implements Cmm9100Service {

    @Resource(name="cmm9100DAO")
    private Cmm9100DAO cmm9100DAO;
    
    /**
     * 공통 코드 다중 목록을 조회한다.(파라미터넘길때 TO로 맵 사용)
     * @param param - 조회할 정보가 담긴 Map
     * @return 공통 코드 목록
     * @exception Exception
     */
    @SuppressWarnings("rawtypes")
    public List selectCmm9100MultiCommonCodeList(Map param) throws Exception {
    	return cmm9100DAO.selectCmm9100MultiCommonCodeList(param); 
    }
}
