package kr.opensoftlab.oslits.stm.stm3000.stm3100.service.impl;

import java.util.List;
import java.util.Map;

import kr.opensoftlab.oslits.com.dao.ComOslitsAbstractDAO;
import kr.opensoftlab.oslits.stm.stm3000.stm3100.vo.Stm3100VO;

import org.springframework.stereotype.Repository;

/**
 * @Class Name : Stm3100DAO.java
 * @Description : Stm3100DAO DAO Class
 * @Modification Information
 *
 * @author 공대영
 * @since 2018.07.19
 * @version 1.0
 * @see
 *  
 *  Copyright (C)  All right reserved.
 */

@Repository("stm3100DAO")
public class Stm3100DAO extends ComOslitsAbstractDAO {
	
	@SuppressWarnings("rawtypes")
	public List selectStm3100JenkinsProjectAuthList(Map paramMap) throws Exception {
		return (List) list("stm3100DAO.selectStm3100JenkinsProjectAuthList", paramMap);
	}
	
	
	public void insertStm3100(Stm3100VO stm3100VO) {
		insert("stm3100DAO.insertStm3100",stm3100VO);
	}

	public void deleteStm3100(Stm3100VO stm3100VO) {
		delete("stm3100DAO.deleteStm3100",stm3100VO);
	}
	
	
	
	public List<Map> selectStm3100JenkinsProjectList(Map paramMap) {
		return (List) list("stm3100DAO.selectStm3100JenkinsProjectList", paramMap);
	}
	
}
