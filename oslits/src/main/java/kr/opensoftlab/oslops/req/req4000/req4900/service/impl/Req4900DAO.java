package kr.opensoftlab.oslops.req.req4000.req4900.service.impl;

import java.util.Map;

import kr.opensoftlab.oslops.com.dao.ComOslitsAbstractDAO;

import org.springframework.stereotype.Repository;

/**
 * @Class Name : Req4900DAO.java
 * @Description : Req4900DAO DAO Class
 * @Modification Information
 *
 * @author 진주영
 * @since 2017.06.07
 * @version 1.0
 * @see
 *  
 *  Copyright (C)  All right reserved.
 */

@Repository("req4900DAO")
public class Req4900DAO extends ComOslitsAbstractDAO {
	
	/**
	 * Req4900 요구사항 결재정보 등록
	 * @param param - Map
	 * @return 
	 * @exception Exception
	 */	
	@SuppressWarnings("rawtypes")
	public String insertReq4900ReqSignInfo(Map paramMap) throws Exception{
		return (String)insert("req4900DAO.insertReq4900ReqSignInfo", paramMap);
	}
	
}
