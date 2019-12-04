package kr.opensoftlab.oslops.stm.stm1000.stm1200.service.impl;

import java.util.List;

import kr.opensoftlab.oslops.com.dao.ComOslitsAbstractDAO;
import kr.opensoftlab.oslops.stm.stm1000.stm1200.vo.Stm1200VO;

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
	
	/**
	 * Stm1200 전체 API 목록을 조회한다.
	 * @param stm1200vo
	 * @return List - 전체 API 목록
	 * @exception Exception
	 */
	@SuppressWarnings({ "rawtypes", "unchecked" })
	public List<Stm1200VO> selectStm1200ProjectList(Stm1200VO stm1200vo) throws Exception{
		return (List) list("stm1200DAO.selectStm1200ProjectList", stm1200vo);
	}

	/**
	 * Stm1200 그리드 페이징 처리를 위한 전체 API 목록 총 수를 조회한다.
	 * @param stm1200vo
	 * @return int 전체 API 목록 수
	 * @exception Exception
	 */
	public int selectStm1200ProjectListCnt(Stm1200VO stm1200vo) throws Exception{
		return (Integer)select("stm1200DAO.selectStm1200ProjectListCnt", stm1200vo);
	}
}
