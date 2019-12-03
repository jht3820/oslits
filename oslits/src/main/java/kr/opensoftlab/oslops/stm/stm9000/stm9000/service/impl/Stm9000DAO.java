package kr.opensoftlab.oslops.stm.stm9000.stm9000.service.impl;

import java.util.List;
import java.util.Map;

import kr.opensoftlab.oslops.com.dao.ComOslitsAbstractDAO;

import kr.opensoftlab.oslops.stm.stm9000.stm9000.vo.Stm9000VO;

import org.springframework.stereotype.Repository;

/**
 * @Class Name : Stm9000DAO.java
 * @Description : Stm9000DAO DAO Class
 * @Modification Information
 *
 * @author 공대영
 *  * @since 2018.07.23.
 * @version 1.0
 * @see
 *  
 *  Copyright (C)  All right reserved.
 */

@Repository("stm9000DAO")
public class Stm9000DAO extends ComOslitsAbstractDAO {
	
	
	public List<Stm9000VO> selectStm9000List(Stm9000VO stm9000VO) {
		// TODO Auto-generated method stub
		return (List) list("stm9000DAO.selectStm9000List", stm9000VO);
	}

	public int selectStm9000ListCnt(Stm9000VO stm9000VO) {
		// TODO Auto-generated method stub
		return (Integer)select("stm9000DAO.selectStm9000ListCnt", stm9000VO);
	}

	public Map selectStm9000DetailInfo(Map paramMap) {
		// TODO Auto-generated method stub
		return (Map)select("stm9000DAO.selectStm9000DetailInfo", paramMap);
	}
	
	public List<Map> selectStm9000ReqHistoryList(Map paramMap) {
		// TODO Auto-generated method stub
		return (List) list("stm9000DAO.selectStm9000ReqHistoryList", paramMap);
	}

	public List<Map> selectStm9000ReqSignList(Map paramMap) {
		// TODO Auto-generated method stub
		return (List) list("stm9000DAO.selectStm9000ReqSignList", paramMap);
	}

	public List<Map> selectStm9000ChgDetailList(Map paramMap) {
		// TODO Auto-generated method stub
		return (List) list("stm9000DAO.selectStm9000ChgDetailList", paramMap);
	}

}
