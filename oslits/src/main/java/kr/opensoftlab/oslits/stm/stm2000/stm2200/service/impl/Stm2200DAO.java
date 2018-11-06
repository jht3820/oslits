package kr.opensoftlab.oslits.stm.stm2000.stm2200.service.impl;

import java.util.List;
import java.util.Map;

import kr.opensoftlab.oslits.com.dao.ComOslitsAbstractDAO;

import kr.opensoftlab.oslits.stm.stm2000.stm2200.vo.Stm2200VO;


import org.springframework.stereotype.Repository;

/**
 * @Class Name : Stm2200DAO.java
 * @Description : Stm2200DAO DAO Class
 * @Modification Information
 *
 * @author 공대영
 * @since 2018.07.19
 * @version 1.0
 * @see
 *  
 *  Copyright (C)  All right reserved.
 */

@Repository("stm2200DAO")
public class Stm2200DAO extends ComOslitsAbstractDAO {
	/**
	 * svn  Repository 목록 SELECT BOX 조회
	 * @param param - Map
	 * @return 
	 * @exception Exception
	 */
	
	public List<Stm2200VO> selectStm2200RepProjectList(Stm2200VO stm2200vo) {
		return (List) list("stm2200DAO.selectStm2200RepProjectList", stm2200vo);
	}

	public int selectStm2200RepProjectListCnt(Stm2200VO stm2200vo) {
		return (Integer) select("stm2200DAO.selectStm2200RepProjectListCnt", stm2200vo);
	}

}
