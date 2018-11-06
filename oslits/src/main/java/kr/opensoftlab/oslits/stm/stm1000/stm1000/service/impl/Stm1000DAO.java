package kr.opensoftlab.oslits.stm.stm1000.stm1000.service.impl;

import java.util.List;
import java.util.Map;

import kr.opensoftlab.oslits.com.dao.ComOslitsAbstractDAO;
import kr.opensoftlab.oslits.stm.stm1000.stm1000.vo.Stm1000VO;

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
	
	
	public List<Stm1000VO> selectStm1000List(Stm1000VO stm1000vo) {
		// TODO Auto-generated method stub
		return (List) list("stm1000DAO.selectStm1000List", stm1000vo);
	}

	public int selectStm1000ListCnt(Stm1000VO stm1000vo) {
		// TODO Auto-generated method stub
		return (Integer)select("stm1000DAO.selectStm1000ListCnt", stm1000vo);
	}

	public String insertStm1001Info(Map<String, String> paramMap) {
		// TODO Auto-generated method stub
		return (String) insert("stm1000DAO.insertStm1001Info", paramMap);
	}

	public Map selectStm1000Info(Map<String, String> paramMap) {
		// TODO Auto-generated method stub
		return (Map)select("stm1000DAO.selectStm1000Info", paramMap);
	}

	public int updateStm1001Info(Map<String, String> paramMap) {
		// TODO Auto-generated method stub
		return update("stm1000DAO.updateStm1001Info", paramMap);
	}
	
	/**
	 * API 관리 삭제 
	 * @param paramMap
	 */
	public void deleteStm1000Info(Map<String, String> paramMap) {
		// TODO Auto-generated method stub
		delete("stm1000DAO.deleteStm1000Info", paramMap);
	}	
	
	public int selectStm1000UseCountInfo(Map<String, String> paramMap) {
		// TODO Auto-generated method stub
		return (Integer)select("stm1000DAO.selectStm1000UseCountInfo", paramMap);
	}
	
	public List<Map<String, String> > selectStm1000ApiUrlList(Map<String, String> paramMap) {
		// TODO Auto-generated method stub
		return (List) list("stm1000DAO.selectStm1000ApiUrlList", paramMap);
	}
	
	
}
