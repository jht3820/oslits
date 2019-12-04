package kr.opensoftlab.oslops.req.req4500.req4500.service.impl;

import java.util.List;
import java.util.Map;

import kr.opensoftlab.oslops.com.dao.ComOslitsAbstractDAO;

import org.springframework.stereotype.Repository;

/**
 * @Class Name : Req4000DAO.java
 * @Description : Req4000DAO DAO Class
 * @Modification Information
 *
 * @author 정형택
 * @since 2016.01.30.
 * @version 1.0
 * @see
 *  
 *  Copyright (C) OpenSoftLab Corp. All right reserved.
 */

@Repository("req4500DAO")
public class Req4500DAO extends ComOslitsAbstractDAO {

	public List<Map> selectReq4500ReqHistoryList(Map<String, String> paramMap) {
		// TODO Auto-generated method stub
		return (List<Map>) list("req4500DAO.selectReq4500ReqHistoryList",paramMap);
	}

	public List<Map> selectReq4500ReqSignList(Map<String, String> paramMap) {
		// TODO Auto-generated method stub
		return (List<Map>) list("req4500DAO.selectReq4500ReqSignList",paramMap);
	}

	public List selectReq4500ChgDetailList(Map<String, String> paramMap) {
		// TODO Auto-generated method stub
		return (List<Map>) list("req4500DAO.selectReq4500ChgDetailList",paramMap);
	}
}