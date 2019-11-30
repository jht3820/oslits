package kr.opensoftlab.oslops.stm.stm1000.stm1000.service.impl;

import java.util.List;
import java.util.Map;

import kr.opensoftlab.oslops.com.dao.ComOslitsAbstractDAO;
import kr.opensoftlab.oslops.stm.stm1000.stm1000.vo.Stm1000VO;

import org.springframework.stereotype.Repository;

/**
 * @Class Name : Stm1000DAO.java
 * @Description : Stm1000DAO DAO Class
 * @Modification Information
 *
 * @author 공대영
 *  * @since 2018.07.23.
 * @version 1.0
 * @see
 *  
 *  Copyright (C)  All right reserved.
 */

@Repository("stm1000DAO")
public class Stm1000DAO extends ComOslitsAbstractDAO {
	
	/**
	 * Stm1000 API 관리 목록을 조회한다.
	 * @param stm1000vo
	 * @return List - API 관리 목록
	 * @exception Exception
	 */	
	@SuppressWarnings({ "rawtypes", "unchecked" })
	public List<Stm1000VO> selectStm1000List(Stm1000VO stm1000vo) throws Exception{
		return (List) list("stm1000DAO.selectStm1000List", stm1000vo);
	}

	/**
	 * Stm1000 페이징 처리를 위한 API 관리 목록 총 건수 조회한다.
	 * @param stm1000vo
	 * @return int API 목록 수
	 * @exception Exception
	 */	
	public int selectStm1000ListCnt(Stm1000VO stm1000vo) throws Exception{
		return (Integer)select("stm1000DAO.selectStm1000ListCnt", stm1000vo);
	}

	/**
	 * Stm1000 API를 등록한다.
	 * @param paramMap - Map
	 * @return
	 * @exception Exception
	 */	
	public String insertStm1001Info(Map<String, String> paramMap) throws Exception{
		return (String) insert("stm1000DAO.insertStm1001Info", paramMap);
	}

	/**
	 * Stm1000 API 정보를 단건 조회한다.
	 * @param paramMap - Map
	 * @return Map API 단건 정보
	 * @exception Exception
	 */	
	@SuppressWarnings("rawtypes")
	public Map selectStm1000Info(Map<String, String> paramMap) throws Exception{
		return (Map)select("stm1000DAO.selectStm1000Info", paramMap);
	}

	/**
	 * Stm1000 API 정보를 수정한다.
	 * @param paramMap - Map
	 * @return
	 * @exception Exception
	 */	
	public int updateStm1001Info(Map<String, String> paramMap) throws Exception{
		return update("stm1000DAO.updateStm1001Info", paramMap);
	}
	
	/**
	 * Stm1000 API 정보를 삭제한다.
	 * @param paramMap - Map
	 * @return
	 * @exception Exception
	 */	
	public void deleteStm1000Info(Map<String, String> paramMap) throws Exception{
		delete("stm1000DAO.deleteStm1000Info", paramMap);
	}	
	
	/**
	 * Stm1000 API 등록 상태를 확인한다.
	 * @param paramMap - Map
	 * @return
	 * @exception Exception
	 */	
	public int selectStm1000UseCountInfo(Map<String, String> paramMap) throws Exception{
		return (Integer)select("stm1000DAO.selectStm1000UseCountInfo", paramMap);
	}
	
	/**
	 * Stm1000 API토큰 정보로  URL 목록을 조회한다.
	 * @param paramMap - Map
	 * @return List - URL 목록
	 * @exception Exception
	 */	
	@SuppressWarnings({ "rawtypes", "unchecked" })
	public List<Map<String, String> > selectStm1000ApiUrlList(Map<String, String> paramMap) throws Exception{
		return (List) list("stm1000DAO.selectStm1000ApiUrlList", paramMap);
	}

}
