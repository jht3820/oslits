package kr.opensoftlab.oslops.stm.stm2000.stm2000.service.impl;

import java.util.List;
import java.util.Map;

import kr.opensoftlab.oslops.com.dao.ComOslitsAbstractDAO;
import kr.opensoftlab.oslops.stm.stm2000.stm2000.vo.Stm2000VO;

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
	 * Stm2000 SVN Repository 목록을 조회한다.
	 * @param Stm2000VO
	 * @return List - SVN Repository 목록
	 * @exception Exception
	 */
	@SuppressWarnings({ "rawtypes", "unchecked" })
	public List<Stm2000VO> selectStm2000RepositoryList(Stm2000VO stm2000vo) throws Exception{
		return (List) list("stm2000DAO.selectStm2000RepositoryList", stm2000vo);
	}

	/**
	 * Stm2000 SVN Repository 목록 총 수를 조회한다.
	 * @param Stm2000VO
	 * @return SVN Repository 목록 수
	 * @exception Exception
	 */
	public int selectStm2000RepositoryListCnt(Stm2000VO stm2000vo) throws Exception{
		return (Integer) select("stm2000DAO.selectStm2000RepositoryListCnt", stm2000vo);
	}
	
	/**
	 * Stm2000 SVN Repository 단건 조회한다.
	 * @param paramMap
	 * @return
	 * @exception Exception
	 */
	@SuppressWarnings("rawtypes")
	public Map selectStm2000Info(Map<String, String> paramMap) throws Exception{
		return (Map) select("stm2000DAO.selectStm2000Info", paramMap);
	}
	
	/**
	 * Stm2000 SVN Repository를 등록한다.
	 * @param paramMap
	 * @return
	 * @exception Exception
	 */
	public String insertStm2000Info(Map<String, String> paramMap) throws Exception{
		return (String) insert("stm2000DAO.insertStm2000Info", paramMap);
	}
	
	/**
	 * Stm2000 SVN Repository를 수정한다.
	 * @param paramMap
	 * @return
	 * @exception Exception
	 */
	public int updateStm2000Info(Map<String, String> paramMap) throws Exception{
		return update("stm2000DAO.updateStm2000Info", paramMap);
	}

	/**
	 * Stm2000 SVN Repository 상태를 확인한다.
	 * @param paramMap
	 * @return
	 * @exception Exception
	 */
	public int selectStm2000UseCountInfo(Map<String, String> paramMap) throws Exception{
		return (Integer) select("stm2000DAO.selectStm2000UseCountInfo", paramMap);
	}

	/**
	 * Stm2000 SVN Repository를 삭제한다.
	 * @param paramMap
	 * @return
	 * @exception Exception
	 */
	public void deleteStm2000Info(Map<String, String> paramMap) throws Exception{
		delete("stm2000DAO.deleteStm2000Info", paramMap);
	}	

	/**
	 * Stm2000 SVN Repository 허용 역할 정보를 저장한다.
	 * @param paramMap
	 * @return
	 * @throws Exception
	 */
	@SuppressWarnings("rawtypes")
	public void insertStm2000SvnAuthGrpInfo(Map paramMap) throws Exception {
		insert("stm2000DAO.insertStm2000SvnAuthGrpInfo", paramMap);
	}
	
	/**
	 * Stm2000 SVN Repository 허용 역할 정보를 제거한다.
	 * @param paramMap
	 * @return
	 * @throws Exception
	 */
	@SuppressWarnings("rawtypes")
	public void deleteStm2000SvnAuthGrpInfo(Map paramMap) throws Exception {
		delete("stm2000DAO.deleteStm2000SvnAuthGrpInfo", paramMap);
	}
	
	/**
	 * Stm2000 SVN Repository 허용 역할 정보 목록을 조회한다.
	 * @param paramMap
	 * @return List SVN Repository 허용 역할 목록
	 * @throws Exception
	 */
	@SuppressWarnings("rawtypes")
	public List selectStm2000SvnAuthGrpList(Map paramMap) throws Exception {
		return (List) list("stm2000DAO.selectStm2000SvnAuthGrpList", paramMap);
	}
	
	/**
	 * Stm2000 그리드 페이징 처리를 위한 SVN Repository 허용 역할 목록 총 수를 조회한다.
	 * @param paramMap
	 * @return SVN Repository 허용 역할 목록 총 수
	 * @throws Exception
	 */
	@SuppressWarnings("rawtypes")
	public int selectStm2000SvnAuthGrpCnt(Map paramMap) throws Exception {
		return (int) select("stm2000DAO.selectStm2000SvnAuthGrpCnt", paramMap);
	}
}
