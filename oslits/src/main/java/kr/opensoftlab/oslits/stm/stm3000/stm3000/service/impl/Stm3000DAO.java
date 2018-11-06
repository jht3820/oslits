package kr.opensoftlab.oslits.stm.stm3000.stm3000.service.impl;

import java.util.List;
import java.util.Map;

import kr.opensoftlab.oslits.com.dao.ComOslitsAbstractDAO;
import kr.opensoftlab.oslits.stm.stm3000.stm3000.vo.Stm3000VO;

import org.springframework.stereotype.Repository;

/**
 * @Class Name : Stm3000DAO.java
 * @Description : Stm3000DAO DAO Class
 * @Modification Information
 *
 * @author 공대영
 * @since 2018.09.03
 * @version 1.0
 * @see
 *  
 *  Copyright (C)  All right reserved.
 */

@Repository("stm3000DAO")
public class Stm3000DAO extends ComOslitsAbstractDAO {
	

	/**
	 * 젠킨스 접속 유저정보를 조회한다.
	 *  
	 * @param param - Map
	 * @return list 
	 * @throws Exception
	 */
	@SuppressWarnings({"rawtypes", "unchecked" })
	public List<Map> selectStm3000JenkinsUserList(Map map) throws Exception{
		return (List) list("stm3000DAO.selectStm3000JenkinsUserList", map);
	}

	public List<Stm3000VO> selectStm3000JobList(Stm3000VO stm3000vo) {
		// TODO Auto-generated method stub
		return (List) list("stm3000DAO.selectStm3000JobList", stm3000vo);
	}

	public int selectStm3000JobListCnt(Stm3000VO stm3000vo) {
		// TODO Auto-generated method stub
		return (Integer) select("stm3000DAO.selectStm3000JobListCnt", stm3000vo);
	}

	public Map selectStm3000JobInfo(Map<String, String> paramMap) {
		// TODO Auto-generated method stub
		return (Map) select("stm3000DAO.selectStm3000JobInfo", paramMap);
	}

	public String insertStm3000JobInfo(Map<String, String> paramMap) {
		// TODO Auto-generated method stub
		return (String) insert("stm3000DAO.insertStm3000JobInfo", paramMap);
	}

	public int updateStm3000JobInfo(Map<String, String> paramMap) {
		// TODO Auto-generated method stub
		return update("stm3000DAO.updateStm3000JobInfo", paramMap);
	}

	public int selectStm3000UseCountInfo(Map<String, String> paramMap) {
		// TODO Auto-generated method stub
		return (Integer) select("stm3000DAO.selectStm3000UseCountInfo", paramMap);
	}

	public void deleteStm3000Info(Map<String, String> paramMap) {
		// TODO Auto-generated method stub
		delete("stm3000DAO.deleteStm3000Info", paramMap);
	}

	/**
	 * jenkins 허용 역할 정보 저장
	 * @param paramMap
	 * @throws Exception
	 */
	@SuppressWarnings("rawtypes")
	public void insertStm3000JenAuthGrpInfo(Map paramMap) throws Exception {
		insert("stm3000DAO.insertStm3000JenAuthGrpInfo", paramMap);
	}
	/**
	 * jenkins 허용 역할 정보 제거
	 * @param paramMap
	 * @throws Exception
	 */
	@SuppressWarnings("rawtypes")
	public void deleteStm3000JenAuthGrpInfo(Map paramMap) throws Exception {
		delete("stm3000DAO.deleteStm3000JenAuthGrpInfo", paramMap);
	}
	
	/**
	 * jenkins 허용 역할 정보 목록 가져오기
	 * @param paramMap
	 * @throws Exception
	 */
	@SuppressWarnings("rawtypes")
	public List selectStm3000JenAuthGrpList(Map paramMap) throws Exception {
		return (List) list("stm3000DAO.selectStm3000JenAuthGrpList", paramMap);
	}
	
	/**
	 * jenkins 허용 역할 정보 단건 갯수 가져오기
	 * @param paramMap
	 * @throws Exception
	 */
	@SuppressWarnings("rawtypes")
	public int selectStm3000JenAuthGrpCnt(Map paramMap) throws Exception {
		return (int) select("stm3000DAO.selectStm3000JenAuthGrpCnt", paramMap);
	}
}
