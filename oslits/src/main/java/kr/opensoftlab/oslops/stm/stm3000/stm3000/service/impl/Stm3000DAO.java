package kr.opensoftlab.oslops.stm.stm3000.stm3000.service.impl;

import java.util.List;
import java.util.Map;

import kr.opensoftlab.oslops.com.dao.ComOslitsAbstractDAO;
import kr.opensoftlab.oslops.stm.stm3000.stm3000.vo.Jen1000VO;
import kr.opensoftlab.oslops.stm.stm3000.stm3000.vo.Jen1100VO;

import org.springframework.stereotype.Repository;

/**
 * @Class Name : Stm3000DAO.java
 * @Description : Stm3000DAO DAO Class
 * @Modification Information
 *
 * @author 진주영
 * @since 2019.03.07
 * @version 1.0
 * @see
 *  
 *  Copyright (C)  All right reserved.
 */

@Repository("stm3000DAO")
public class Stm3000DAO extends ComOslitsAbstractDAO {
	
	/**
	 * jenkins 일반 목록 조회
	 * @param paramMap
	 * @throws Exception
	 */
	@SuppressWarnings({ "unchecked", "rawtypes" })
	public List<Map> selectStm3000JenkinsNormalList(Map paramMap) throws Exception {
		return (List) list("stm3000DAO.selectStm3000JenkinsNormalList", paramMap);
	}
	
	/**
	 * job 일반 목록 조회
	 * @param paramMap
	 * @throws Exception
	 */
	@SuppressWarnings({ "unchecked", "rawtypes" })
	public List<Map> selectStm3000JobNormalList(Map paramMap) throws Exception {
		return (List) list("stm3000DAO.selectStm3000JobNormalList", paramMap);
	}
	
	/**
	 * jenkins 목록 조회
	 * @param paramMap
	 * @throws Exception
	 */
	@SuppressWarnings({ "unchecked", "rawtypes" })
	public List<Jen1000VO> selectStm3000JenkinsList(Jen1000VO jen1000VO) throws Exception {
		return (List) list("stm3000DAO.selectStm3000JenkinsList", jen1000VO);
	}
	
	/**
	 * job 목록 조회
	 * @param paramMap
	 * @throws Exception
	 */
	@SuppressWarnings({ "unchecked", "rawtypes" })
	public List<Jen1100VO> selectStm3000JobList(Jen1100VO jen1100VO) throws Exception {
		return (List) list("stm3000DAO.selectStm3000JobList", jen1100VO);
	}
	
	/**
	 * Jenkins 목록 총건수
	 * @param paramMap
	 * @throws Exception
	 */
	public int selectStm3000JenkinsListCnt(Jen1000VO jen1000VO) throws Exception {
		return (Integer) select("stm3000DAO.selectStm3000JenkinsListCnt", jen1000VO);
	}
	
	/**
	 * Job 목록 총건수
	 * @param paramMap
	 * @throws Exception
	 */
	public int selectStm3000JobListCnt(Jen1100VO jen1100VO) throws Exception {
		return (Integer) select("stm3000DAO.selectStm3000JobListCnt", jen1100VO);
	}
	
	/**
	 * Jenkins 정보 조회(단건)
	 * @param paramMap
	 * @throws Exception
	 */
	@SuppressWarnings("rawtypes")
	public Map selectStm3000JenkinsInfo(Map<String, String> paramMap) throws Exception {
		return (Map) select("stm3000DAO.selectStm3000JenkinsInfo", paramMap);
	}
	
	/**
	 * Job 정보 조회(단건)
	 * @param paramMap
	 * @throws Exception
	 */
	@SuppressWarnings("rawtypes")
	public Map selectStm3000JobInfo(Map<String, String> paramMap) throws Exception {
		return (Map) select("stm3000DAO.selectStm3000JobInfo", paramMap);
	}
	
	/**
	 * Jenkins 등록
	 * @param paramMap
	 * @throws Exception
	 */
	public String insertStm3000JenkinsInfo(Map<String, String> paramMap) throws Exception {
		return (String) insert("stm3000DAO.insertStm3000JenkinsInfo", paramMap);
	}
	
	/**
	 * Job 등록
	 * @param paramMap
	 * @throws Exception
	 */
	public String insertStm3000JobInfo(Map<String, String> paramMap) throws Exception {
		return (String) insert("stm3000DAO.insertStm3000JobInfo", paramMap);
	}

	/**
	 * Jenkins 수정
	 * @param paramMap
	 * @throws Exception
	 */
	public int updateStm3000JenkinsInfo(Map<String, String> paramMap)  throws Exception{
		return update("stm3000DAO.updateStm3000JenkinsInfo", paramMap);
	}
	
	/**
	 * Job 수정
	 * @param paramMap
	 * @throws Exception
	 */
	public int updateStm3000JobInfo(Map<String, String> paramMap)  throws Exception{
		return update("stm3000DAO.updateStm3000JobInfo", paramMap);
	}
	
	/**
	 * Jenkins 삭제
	 * @param paramMap
	 * @throws Exception
	 */
	public void deleteStm3000JenkinsInfo(Map<String, String> paramMap)  throws Exception{
		update("stm3000DAO.deleteStm3000JenkinsInfo", paramMap);
	}
	
	/**
	 * Job 삭제
	 * @param paramMap
	 * @throws Exception
	 */
	public void deleteStm3000JobInfo(Map<String, String> paramMap) throws Exception {
		delete("stm3000DAO.deleteStm3000JobInfo", paramMap);
	}
	
	/**
	 * Jenkins 등록 상태 확인
	 * @param paramMap
	 * @throws Exception
	 */
	public int selectStm3000JenkinsUseCountInfo(Map<String, String> paramMap)  throws Exception{
		return (Integer) select("stm3000DAO.selectStm3000JenkinsUseCountInfo", paramMap);
	}
	
	/**
	 * Job 등록 상태 확인
	 * @param paramMap
	 * @throws Exception
	 */
	public int selectStm3000JobUseCountInfo(Map<String, String> paramMap)  throws Exception{
		return (Integer) select("stm3000DAO.selectStm3000JobUseCountInfo", paramMap);
	}

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
