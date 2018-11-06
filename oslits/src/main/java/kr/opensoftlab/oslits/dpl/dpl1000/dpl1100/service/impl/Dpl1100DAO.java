package kr.opensoftlab.oslits.dpl.dpl1000.dpl1100.service.impl;

import java.util.List;
import java.util.Map;

import kr.opensoftlab.oslits.com.dao.ComOslitsAbstractDAO;
import kr.opensoftlab.oslits.dpl.dpl1000.dpl1100.vo.Dpl1100VO;

import org.springframework.stereotype.Repository;

/**
 * @Class Name : Dpl1100DAO.java
 * @Description : Dpl1100DAO DAO Class
 * @Modification Information
 *
 * @author 공대영
 * @since 2016.02.08
 * @version 1.0
 * @see
 *  
 *  Copyright (C)  All right reserved.
 */

@Repository("dpl1100DAO")
public class Dpl1100DAO extends ComOslitsAbstractDAO {
	

	/**
	 * Dpl1100 
	 * @param param - dpl1100vo
	 * @return list 
	 * @throws Exception
	 */
	@SuppressWarnings({"rawtypes", "unchecked" })
	public List<Dpl1100VO> selectDpl1100ExistDplList(Map map) throws Exception{
		return (List) list("dpl1100DAO.selectDpl1100ExistDplList", map);
	}

	@SuppressWarnings({ "rawtypes", "unchecked" })
	public List<Dpl1100VO> selectDpl1100NotExistDplList(Map map)  throws Exception{
		return (List) list("dpl1100DAO.selectDpl1100NotExistDplList", map);
	}

	@SuppressWarnings("rawtypes")
	public int updateDpl1100Dpl(Map paramMap) throws Exception {
		return update("dpl1100DAO.updateDpl1100Dpl", paramMap);
	}
	
	@SuppressWarnings("rawtypes")
	public void insertDpl1100logHistory(Map paramMap) throws Exception {
		insert("dpl1100DAO.insertDpl1100logHistory", paramMap);
	}
	
	@SuppressWarnings("rawtypes")
	public String selectDpl1100ExistBuildInfo(Map paramMap) throws Exception {
		return (String) select("dpl1100DAO.selectDpl1100ExistBuildInfo", paramMap);
	}
	
	/**
	 * 요구사항 배포 계획 배정
	 * @param paramMap
	 * @throws Exception
	 */
	@SuppressWarnings("rawtypes")
	public List selectDpl1100ReqDplList(Map paramMap) throws Exception {
		return (List) list("dpl1100DAO.selectDpl1100ReqDplList", paramMap);
	}
	
	/**
	 * 요구사항 배포 계획 배정
	 * @param paramMap
	 * @throws Exception
	 */
	@SuppressWarnings("rawtypes")
	public void insertDpl1200ReqDplInfo(Map paramMap) throws Exception {
		insert("dpl1100DAO.insertDpl1200ReqDplInfo", paramMap);
	}
	
	/**
	 * 요구사항 배포 계획 배정 update
	 * @param paramMap
	 * @throws Exception
	 */
	@SuppressWarnings("rawtypes")
	public int updateDpl1200ReqDplInfo(Map paramMap) throws Exception {
		return update("dpl1100DAO.updateDpl1200ReqDplInfo", paramMap);
	}
	
	/**
	 * 요구사항 배포 계획 배정 제외
	 * @param paramMap
	 * @throws Exception
	 */
	@SuppressWarnings("rawtypes")
	public void deleteDpl1200ReqDplInfo(Map paramMap) throws Exception {
		delete("dpl1100DAO.deleteDpl1200ReqDplInfo", paramMap);
	}
}
