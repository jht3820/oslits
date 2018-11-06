package kr.opensoftlab.oslits.stm.stm3000.stm3200.service.impl;

import java.util.List;
import java.util.Map;

import kr.opensoftlab.oslits.com.dao.ComOslitsAbstractDAO;
import kr.opensoftlab.oslits.stm.stm3000.stm3200.vo.Stm3200VO;

import org.springframework.stereotype.Repository;

/**
 * @Class Name : Stm3200DAO.java
 * @Description : Stm3200DAO DAO Class
 * @Modification Information
 *
 * @author 공대영
 * @since 2018.07.19
 * @version 1.0
 * @see
 *  
 *  Copyright (C)  All right reserved.
 */

@Repository("stm3200DAO")
public class Stm3200DAO extends ComOslitsAbstractDAO {
	
	
	public List<Stm3200VO> selectStm3200JobProjectList(Stm3200VO stm3200VO) {
		return (List) list("stm3200DAO.selectStm3200JobProjectList", stm3200VO);
	}

	public int selectStm3200JobProjectListCnt(Stm3200VO stm3200VO) {
		return (Integer) select("stm3200DAO.selectStm3200JobProjectListCnt", stm3200VO);
	}

}
