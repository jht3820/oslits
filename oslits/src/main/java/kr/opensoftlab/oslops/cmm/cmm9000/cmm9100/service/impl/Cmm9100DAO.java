package kr.opensoftlab.oslops.cmm.cmm9000.cmm9100.service.impl;

import java.util.List;
import java.util.Map;

import kr.opensoftlab.oslops.com.dao.ComOslitsAbstractDAO;

import org.springframework.stereotype.Repository;

/**
 * @Class Name : Cmm9100DAO.java
 * @Description : Cmm9100DAO DAO Class
 * @Modification Information
 *
 * @author 정형택
 * @since 2016.01.13.
 * @version 1.0
 * @see
 *  
 *  Copyright (C)  All right reserved.
 */

@Repository("cmm9100DAO")
public class Cmm9100DAO extends ComOslitsAbstractDAO {

    /**
     * Cmm9100 공통 코드 다중 목록을 조회한다.
     * @param param - 조회할 정보가 담긴 Map
     * @return Cmm9100 목록
     * @exception Exception
     */
    @SuppressWarnings("rawtypes")
    public List selectCmm9100MultiCommonCodeList(Map param) throws Exception {
    	return list("cmm9100DAO.selectCmm9100MultiCommonCodeList", param);   
    }

}
