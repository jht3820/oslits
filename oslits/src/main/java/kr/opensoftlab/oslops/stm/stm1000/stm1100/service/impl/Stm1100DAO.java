package kr.opensoftlab.oslops.stm.stm1000.stm1100.service.impl;

import java.util.List;
import java.util.Map;

import kr.opensoftlab.oslops.com.dao.ComOslitsAbstractDAO;
import kr.opensoftlab.oslops.stm.stm1000.stm1100.vo.Stm1100VO;

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
	
	/**
	 * Stm1100 프로젝트에 사용 가능한 API 목록을 조회한다.
	 * @param paramMap
	 * @return List - 프로젝트에 등록 가능한 API 관리 목록
	 * @exception Exception
	 */	
	@SuppressWarnings({ "rawtypes", "unchecked" })
	public List<Map> selectStm1100ProjectAuthList(Map<String, String> paramMap) throws Exception{
		return (List<Map>) list("stm1100DAO.selectStm1100ProjectAuthList", paramMap);
	}

	/**
	 * Stm1100 프로젝트에 API를 등록한다.
	 * @param stm1100vo
	 * @exception Exception
	 */	
	public void insertStm1100(Stm1100VO stm1100vo) throws Exception{
		insert("stm1100DAO.insertStm1100", stm1100vo);
		
	}

	/**
	 * Stm1100 프로젝트에 등록된 API를 삭제한다.
	 * @param stm1100vo
	 * @exception Exception
	 */	
	public void deleteStm1100(Stm1100VO stm1100vo) throws Exception{
		delete("stm1100DAO.deleteStm1100", stm1100vo);
	}

	/**
	 * Stm1100 프로젝트에 등록되어있는 API 목록을 조회한다.
	 * @param paramMap
	 * @exception Exception
	 */
	@SuppressWarnings({ "rawtypes", "unchecked" })
	public List selectStm1100ProjectListAjax(Map<String, String> paramMap) throws Exception{
		return (List<Map>) list("stm1100DAO.selectStm1100ProjectListAjax", paramMap);
	}
}
