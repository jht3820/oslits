package kr.opensoftlab.oslits.stm.stm1000.stm1100.service.impl;

import java.util.List;
import java.util.Map;

import kr.opensoftlab.oslits.com.dao.ComOslitsAbstractDAO;
import kr.opensoftlab.oslits.stm.stm1000.stm1100.vo.Stm1100VO;

import org.springframework.stereotype.Repository;

/**
 * @Class Name : Stm1100DAO.java
 * @Description : Stm1100DAO DAO Class
 * @Modification Information
 *
 * @author 공대영
 *  * @since 2018.08.14.
 * @version 1.0
 * @see
 *  
 *  Copyright (C)  All right reserved.
 */

@Repository("stm1100DAO")
public class Stm1100DAO extends ComOslitsAbstractDAO {
	
	//
	public List<Map> selectStm1100ProjectAuthList(Map<String, String> paramMap) {
		// TODO Auto-generated method stub
		return (List<Map>) list("stm1100DAO.selectStm1100ProjectAuthList", paramMap);
	}

	public void insertStm1100(Stm1100VO stm1100vo) {
		// TODO Auto-generated method stub
		insert("stm1100DAO.insertStm1100", stm1100vo);
		
	}

	public void deleteStm1100(Stm1100VO stm1100vo) {
		// TODO Auto-generated method stub
		delete("stm1100DAO.deleteStm1100", stm1100vo);
	}

	public List selectStm1100ProjectListAjax(Map<String, String> paramMap) {
		// TODO Auto-generated method stub
		return (List<Map>) list("stm1100DAO.selectStm1100ProjectListAjax", paramMap);
	}
}
