package kr.opensoftlab.oslops.stm.stm2000.stm2100.service.impl;

import java.util.List;
import java.util.Map;

import kr.opensoftlab.oslops.com.dao.ComOslitsAbstractDAO;
import kr.opensoftlab.oslops.stm.stm2000.stm2100.vo.Stm2100VO;

import org.springframework.stereotype.Repository;

/**
 * @Class Name : Stm2100DAO.java
 * @Description : Stm2100DAO DAO Class
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

	/**
	 * Stm2100 프로젝트에 배정 가능한 SVN Repository 목록을 조회한다.
	 * @param paramMap
	 * @return List - 배정 가능 SVN Repository 목록
	 * @exception Exception
	 */
	@SuppressWarnings("rawtypes")
	public List selectStm2100ProjectAuthList(Map paramMap) throws Exception {
		return (List) list("stm2100DAO.selectStm2100ProjectAuthList", paramMap);
	}

	/**
	 * Stm2100 프로젝트에  SVN Repository를 배정한다.
	 * @param stm2100vo
	 * @return 
	 * @exception Exception
	 */
	public void insertStm2100(Stm2100VO stm2100vo) throws Exception{
		insert("stm2100DAO.insertStm2100",stm2100vo);
	}

	/**
	 * Stm2100 프로젝트에  SVN Repository를 배정제외 한다.
	 * @param stm2100vo
	 * @return 
	 * @exception Exception
	 */
	public void deleteStm2100(Stm2100VO stm2100vo) throws Exception{
		delete("stm2100DAO.deleteStm2100",stm2100vo);
	}

	/**
	 * Stm2100 프로젝트에 배정된 SVN Repository 목록을 조회한다.
	 * @param paramMap
	 * @return 
	 * @exception Exception
	 */
	@SuppressWarnings("rawtypes")
	public List selectStm2100ProjectListAjax(Map paramMap) throws Exception {
		return (List) list("stm2100DAO.selectStm2100ProjectListAjax", paramMap);
	}

}
