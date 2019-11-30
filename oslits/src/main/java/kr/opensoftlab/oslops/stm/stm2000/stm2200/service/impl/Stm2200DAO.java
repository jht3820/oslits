package kr.opensoftlab.oslops.stm.stm2000.stm2200.service.impl;

import java.util.List;

import kr.opensoftlab.oslops.com.dao.ComOslitsAbstractDAO;
import kr.opensoftlab.oslops.stm.stm2000.stm2200.vo.Stm2200VO;

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
	 * Stm2200 프로젝트 별 배정된 SVN Repository 전체 목록을 조회한다.
	 * @param stm2200vo
	 * @return List - 프로젝트 별 배정된 SVN Repository 전체 목록
	 * @exception Exception
	 */
	@SuppressWarnings({ "rawtypes", "unchecked" })
	public List<Stm2200VO> selectStm2200RepProjectList(Stm2200VO stm2200vo) throws Exception{
		return (List) list("stm2200DAO.selectStm2200RepProjectList", stm2200vo);
	}

	/**
	 * Stm2200 프로젝트별 배정된 SVN Repository 전체 목록 총 건수를 조회한다.
	 * @param stm2200vo
	 * @return int - 프로젝트 별 배정된 SVN Repository 전체 목록 총 건수
	 * @exception Exception
	 */
	public int selectStm2200RepProjectListCnt(Stm2200VO stm2200vo) throws Exception{
		return (Integer) select("stm2200DAO.selectStm2200RepProjectListCnt", stm2200vo);
	}

}
