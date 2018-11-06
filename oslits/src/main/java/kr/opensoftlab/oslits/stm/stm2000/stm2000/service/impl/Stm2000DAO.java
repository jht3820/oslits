package kr.opensoftlab.oslits.stm.stm2000.stm2000.service.impl;

import java.util.List;
import java.util.Map;

import kr.opensoftlab.oslits.com.dao.ComOslitsAbstractDAO;
import kr.opensoftlab.oslits.stm.stm2000.stm2000.vo.Stm2000VO;

import org.springframework.stereotype.Repository;

/**
 * @Class Name : Stm2000DAO.java
 * @Description : Stm2000DAO DAO Class
 * @Modification Information
 *
 * @author 공대영
 * @since 2018.07.19
 * @version 1.0
 * @see
 *  
 *  Copyright (C)  All right reserved.
 */

@Repository("stm2000DAO")
public class Stm2000DAO extends ComOslitsAbstractDAO {
	/**
	 * svn  Repository 목록 SELECT BOX 조회
	 * @param param - Map
	 * @return 
	 * @exception Exception
	 */
	
	public List<Stm2000VO> selectStm2000RepositoryList(Stm2000VO stm2000vo) {
		return (List) list("stm2000DAO.selectStm2000RepositoryList", stm2000vo);
	}

	public int selectStm2000RepositoryListCnt(Stm2000VO stm2000vo) {
		return (Integer) select("stm2000DAO.selectStm2000RepositoryListCnt", stm2000vo);
	}
	/**
	 * 
	 * SVN 상세 조회
	 * 
	 * @param paramMap
	 * @return
	 */
	public Map selectStm2000Info(Map<String, String> paramMap) {
		// TODO Auto-generated method stub
		return (Map) select("stm2000DAO.selectStm2000Info", paramMap);
	}
	/**
	 * 
	 * SVN Repository 입력
	 * 
	 * @param paramMap
	 * @return
	 */
	public String insertStm2000Info(Map<String, String> paramMap) {
		return (String) insert("stm2000DAO.insertStm2000Info", paramMap);
	}
	/**
	 * SVN Repository 수정
	 * 
	 * @param paramMap
	 * @return
	 */
	public int updateStm2000Info(Map<String, String> paramMap) {
		return update("stm2000DAO.updateStm2000Info", paramMap);
	}

	public int selectStm2000UseCountInfo(Map<String, String> paramMap) {
		// TODO Auto-generated method stub
		return (Integer) select("stm2000DAO.selectStm2000UseCountInfo", paramMap);
	}

	public void deleteStm2000Info(Map<String, String> paramMap) {
		// TODO Auto-generated method stub
		delete("stm2000DAO.deleteStm2000Info", paramMap);
	}	

	/**
	 * SVN Repository 허용 역할 정보 저장
	 * @param paramMap
	 * @throws Exception
	 */
	@SuppressWarnings("rawtypes")
	public void insertStm2000SvnAuthGrpInfo(Map paramMap) throws Exception {
		insert("stm2000DAO.insertStm2000SvnAuthGrpInfo", paramMap);
	}
	/**
	 * SVN Repository 허용 역할 정보 제거
	 * @param paramMap
	 * @throws Exception
	 */
	@SuppressWarnings("rawtypes")
	public void deleteStm2000SvnAuthGrpInfo(Map paramMap) throws Exception {
		delete("stm2000DAO.deleteStm2000SvnAuthGrpInfo", paramMap);
	}
	
	/**
	 * SVN Repository 허용 역할 정보 목록 가져오기
	 * @param paramMap
	 * @throws Exception
	 */
	@SuppressWarnings("rawtypes")
	public List selectStm2000SvnAuthGrpList(Map paramMap) throws Exception {
		return (List) list("stm2000DAO.selectStm2000SvnAuthGrpList", paramMap);
	}
	
	/**
	 * SVN Repository 허용 역할 정보 단건 갯수 가져오기
	 * @param paramMap
	 * @throws Exception
	 */
	@SuppressWarnings("rawtypes")
	public int selectStm2000SvnAuthGrpCnt(Map paramMap) throws Exception {
		return (int) select("stm2000DAO.selectStm2000SvnAuthGrpCnt", paramMap);
	}
}
