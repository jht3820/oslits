package kr.opensoftlab.oslits.stm.stm2000.stm2100.service.impl;

import java.util.List;
import java.util.Map;

import kr.opensoftlab.oslits.com.dao.ComOslitsAbstractDAO;
import kr.opensoftlab.oslits.stm.stm2000.stm2100.vo.Stm2100VO;

import org.springframework.stereotype.Repository;

/**
 * @Class Name : Svn1100DAO.java
 * @Description : Svn1100DAO DAO Class
 * @Modification Information
 *
 * @author 공대영
 * @since 2018.08.16
 * @version 1.0
 * @see
 *  
 *  Copyright (C)  All right reserved.
 */

@Repository("stm2100DAO")
public class Stm2100DAO extends ComOslitsAbstractDAO {

	public List selectStm2100ProjectAuthList(Map paramMap) throws Exception {
		return (List) list("stm2100DAO.selectStm2100ProjectAuthList", paramMap);
	}

	public void insertStm2100(Stm2100VO stm2100vo) {
		insert("stm2100DAO.insertStm2100",stm2100vo);
	}

	public void deleteStm2100(Stm2100VO stm2100vo) {
		delete("stm2100DAO.deleteStm2100",stm2100vo);
	}

	public List selectStm2100ProjectListAjax(Map paramMap) throws Exception {
		return (List) list("stm2100DAO.selectStm2100ProjectListAjax", paramMap);
	}

}
