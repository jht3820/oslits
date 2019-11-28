package kr.opensoftlab.oslops.req.req4700.req4700.service.impl;

import java.util.Map;

import kr.opensoftlab.oslops.com.dao.ComOslitsAbstractDAO;

import org.springframework.stereotype.Repository;

/**
 * @Class Name : Req4700DAO.java
 * @Description : Req4700DAO DAO Class
 * @Modification Information
 *
 * @author 진주영
 * @since 2018.09.06
 * @version 1.0
 * @see
 *  
 *  Copyright (C)  All right reserved.
 */

@Repository("req4700DAO")
public class Req4700DAO extends ComOslitsAbstractDAO {
	
	/**
	 * Req4700 요구사항 변경 내용 등록
	 * @param param - Map
	 * @return 
	 * @exception Exception
	 */	
	@SuppressWarnings("rawtypes")
	public String insertReq4700ReqChangeLogInfo(Map paramMap) throws Exception{
		return (String)insert("req4700DAO.insertReq4700ReqChangeLogInfo", paramMap);
	}
	
}
