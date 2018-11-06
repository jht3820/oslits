package kr.opensoftlab.oslits.stm.stm1000.stm1200.service.impl;

import java.util.List;
import java.util.Map;

import kr.opensoftlab.oslits.com.dao.ComOslitsAbstractDAO;
import kr.opensoftlab.oslits.stm.stm1000.stm1200.vo.Stm1200VO;

import org.springframework.stereotype.Repository;

/**
 * @Class Name : Stm1200DAO.java
 * @Description : Stm1200DAO DAO Class
 * @Modification Information
 *
 * @author 공대영
 *  * @since 2018.07.23.
 * @version 1.0
 * @see
 *  
 *  Copyright (C)  All right reserved.
 */

@Repository("stm1200DAO")
public class Stm1200DAO extends ComOslitsAbstractDAO {
	
	
	
	public List<Stm1200VO> selectStm1200ProjectList(Stm1200VO stm1200vo) {
		// TODO Auto-generated method stub
		return (List) list("stm1200DAO.selectStm1200ProjectList", stm1200vo);
	}

	public int selectStm1200ProjectListCnt(Stm1200VO stm1200vo) {
		// TODO Auto-generated method stub
		return (Integer)select("stm1200DAO.selectStm1200ProjectListCnt", stm1200vo);
	}
}
