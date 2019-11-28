package kr.opensoftlab.oslops.cmm.cmm9000.cmm9200.service.impl;

import java.util.List;
import java.util.Map;

import kr.opensoftlab.oslops.com.dao.ComOslitsAbstractDAO;

import org.springframework.stereotype.Repository;

/**
 * @Class Name : Cmm9200DAO.java
 * @Description : Cmm9200DAO DAO Class
 * @Modification Information
 *
 * @author 정형택
 * @since 2016.01.13.
 * @version 1.0
 * @see
 *  
 *  Copyright (C)  All right reserved.
 */

@Repository("cmm9200DAO")
public class Cmm9200DAO extends ComOslitsAbstractDAO {

	/**
	 * 공통으로 사용할 사용자 정보 목록을 조회한다.(콤보박스용)
	 * @param param - 조회할 정보가 담긴 Map
	 * @return 사용자 정보 목록
	 * @exception Exception
	 */
    @SuppressWarnings("rawtypes")
    public List selectCmm9200PrjUsrList(Map param) throws Exception {
    	return list("cmm9200DAO.selectCmm9200PrjUsrList", param);   
    }



}
