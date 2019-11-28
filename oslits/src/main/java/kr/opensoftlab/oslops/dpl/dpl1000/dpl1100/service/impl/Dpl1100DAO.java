package kr.opensoftlab.oslops.dpl.dpl1000.dpl1100.service.impl;

import java.util.List;
import java.util.Map;

import kr.opensoftlab.oslops.com.dao.ComOslitsAbstractDAO;
import kr.opensoftlab.oslops.dpl.dpl1000.dpl1100.vo.Dpl1100VO;

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
 *   --------------------------------------
 *  수정일			수정자			수정내용
 *  --------------------------------------
 *  2019-03-11		배용진		 	기능 개선
 *  
 *  --------------------------------------
 *  
 *  Copyright (C)  All right reserved.
 */

@Repository("dpl1100DAO")
public class Dpl1100DAO extends ComOslitsAbstractDAO {
	
	/**
	 * Dpl1100 배포 계획에 배정된 요구사항 목록을 조회한다.
	 * @param Dpl1100VO
	 * @return list - 배포계획 배정된 요구사항 목록
	 * @throws Exception
	 */
	@SuppressWarnings({"rawtypes", "unchecked" })
	public List<Map> selectDpl1100ExistDplList(Dpl1100VO dpl1100VO) throws Exception{ // Dpl1100VO dpl1100VO
		return (List) list("dpl1100DAO.selectDpl1100ExistDplList", dpl1100VO);
	}

	/**
	 * Dpl1100 배포 계획에 배정된 요구사항 목록 총 건수를 조회한다. (그리드 페이징 처리) 
	 * @param Dpl1100VO
	 * @return int - 배포계획 배정된 요구사항 총 건수
	 * @throws Exception
	 */
	public int selectDpl1100ExistDplListCnt(Dpl1100VO dpl1100VO) throws Exception {
		return (Integer) select("dpl1100DAO.selectDpl1100ExistDplListCnt", dpl1100VO);
	}

	/**
	 * Dpl1100 배포계획 미배정된 요구사항 목록을 조회한다.
	 * - 미배정 요구사항 : 배포계획 저장이 있는 작업흐름에 속해있으며, 아직 배포계획에 배정되지 않은 요구사항
	 * @param Dpl1100VO
	 * @return list
	 * @throws Exception
	 */
	@SuppressWarnings({ "rawtypes", "unchecked" })
	public List<Map> selectDpl1100NotExistDplList(Dpl1100VO dpl1100VO)  throws Exception{
		return (List) list("dpl1100DAO.selectDpl1100NotExistDplList", dpl1100VO);
	}

	/**
	 * Dpl1100 배포계획 미배정된 요구사항 목록 총 건수를 조회한다. (그리드 페이징 처리) 
	 * @param Dpl1100VO
	 * @return int - 배포계획 미배정된 요구사항 총 건수
	 * @throws Exception
	 */
	public int selectDpl1100NotExistDplListCnt(Dpl1100VO dpl1100VO) throws Exception {
		return (Integer) select("dpl1100DAO.selectDpl1100NotExistDplListCnt", dpl1100VO);
	}
	
	/*************************************************/
	
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
	 * 요구사항 배포 계획 배정 목록
	 * @param paramMap
	 * @throws Exception
	 */
	@SuppressWarnings("rawtypes")
	public List selectDpl1100ReqDplList(Map paramMap) throws Exception {
		return (List) list("dpl1100DAO.selectDpl1100ReqDplList", paramMap);
	}
	
	/**
	 * Dpl1100 배포계획에 요구사항을 배정한다.
	 * @param paramMap
	 * @return
	 * @throws Exception
	 */
	@SuppressWarnings("rawtypes")
	public void insertDpl1100ReqDplInfo(Map paramMap) throws Exception {
		insert("dpl1100DAO.insertDpl1100ReqDplInfo", paramMap);
	}
	
	/**
	 * 요구사항 배포 계획 배정 제외
	 * @param paramMap
	 * @throws Exception
	 */
	@SuppressWarnings("rawtypes")
	public void deleteDpl1100ReqDplInfo(Map paramMap) throws Exception {
		delete("dpl1100DAO.deleteDpl1100ReqDplInfo", paramMap);
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
	
	
}
