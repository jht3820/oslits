package kr.opensoftlab.oslops.prs.prs4000.prs4000.service.impl;

import java.util.List;
import java.util.Map;

import kr.opensoftlab.oslops.com.dao.ComOslitsAbstractDAO;

import org.springframework.stereotype.Repository;

/**
 * @Class Name : Prs4000DAO.java
 * @Description : Prs4000DAO DAO Class
 * @Modification Information
 *
 * @author 김정환
 * @since 2016.03.11
 * @version 1.0
 * @see
 *  
 *  Copyright (C)  All right reserved.
 */

@Repository("prs4000DAO")
public class Prs4000DAO extends ComOslitsAbstractDAO {

	/**
	 * 일정 관리 내역을 조회한다.
	 * @param paramMap
	 * @return
	 * @throws Exception
	 */
	@SuppressWarnings("rawtypes")
	public List selectPrs4000List(Map paramMap) throws Exception {
		return (List) list("prs4000DAO.selectPrs4000List", paramMap);
	}
	
	/**
	 * 일정관리 내역을 저장한다.
	 * @param paramMap
	 * @return
	 * @throws Exception
	 */
	@SuppressWarnings("rawtypes")
	public String insertPrs4000SchdInfo(Map paramMap) throws Exception{
		return (String) insert("prs4000DAO.insertPrs4000SchdInfo", paramMap);
	}

	/**
	 * 일정관리 내역을 수정한다.
	 * @param paramMap
	 * @return
	 */
	@SuppressWarnings("rawtypes")
	public int updatePrs4000SchdInfo(Map paramMap) throws Exception {
		return (int) update("prs4000DAO.updatePrs4000SchdInfo", paramMap);
	}

	/**
	 * 일정관리 내역을 삭제한다.
	 * @param paramMap
	 * @throws Exception
	 */
	@SuppressWarnings("rawtypes")
	public void deletePrs4000SchdInfo(Map paramMap) throws Exception{
		delete("prs4000DAO.deletePrs4000SchdInfo",paramMap);
	}
	
	
}
