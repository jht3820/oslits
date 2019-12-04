package kr.opensoftlab.oslops.whk.whk2000.whk2000.service.impl;

import java.util.List;
import java.util.Map;

import org.springframework.stereotype.Repository;

import kr.opensoftlab.oslops.com.dao.ComOslitsAbstractDAO;
import kr.opensoftlab.oslops.whk.whk2000.whk2000.vo.Whk2000VO;

/**
 * @Class Name : Whk2000DAO.java
 * @Description : Whk2000DAO DAO Class
 * @Modification Information
 *
 * @author 진주영
 * @since 2019.05.24.
 * @version 1.0
 * @see
 *  
 *  Copyright (C) OpenSoftLab Corp. All right reserved.
 */
@Repository("whk2000DAO")
public class Whk2000DAO  extends ComOslitsAbstractDAO {
	
	/**
	 * 사용자 웹훅 목록 조회
	 * @param pageVO
	 * @return List 사용자 웹훅 목록
	 * @throws Exception
	 */
	@SuppressWarnings({ "unchecked" })
	public List<Whk2000VO>  selectWhk2000UsrWhkList(Whk2000VO whk2000vo) throws Exception {
		 return  (List<Whk2000VO>) list("whk2000DAO.selectWhk2000UsrWhkList", whk2000vo);
	}
	
	/**
	 * 사용자 웹훅 목록 총건수
	 * @param Whk2000VO
	 * @throws Exception
	 */
	public int  selectWhk2000UsrWhkListCnt(Whk2000VO whk2000vo) throws Exception {
		 return  (Integer)select("whk2000DAO.selectWhk2000UsrWhkListCnt", whk2000vo);
	} 
	
	/**
	 * 사용자 웹훅 전체 목록 조회 (no paging)
	 * @param Map
	 * @return List 사용자 웹훅 목록
	 * @throws Exception
	 */
	@SuppressWarnings({ "rawtypes", "unchecked" })
	public List<Map> selectWhk2000UsrWhkAllList(Map paramMap) throws Exception{
		return (List<Map>) list("whk2000DAO.selectWhk2000UsrWhkAllList", paramMap);
	}
	
	/**
	 * 사용자 웹훅 단건 조회
	 * @param Map
	 * @return Map 사용자 웹훅 정보
	 * @throws Exception
	 */
	@SuppressWarnings({ "rawtypes" })
	public Map selectWhk2000UsrWhkInfo(Map paramMap) throws Exception{
		return (Map) select("whk2000DAO.selectWhk2000UsrWhkInfo", paramMap);
	}
	
	/**
	 * 사용자 웹훅 정보 등록
	 * @param param - Map
	 * @return 
	 * @exception Exception
	 */
	@SuppressWarnings("rawtypes")
	public String insertWhk2000UsrWhkInfo(Map paramMap) throws Exception{
		return (String) insert("whk2000DAO.insertWhk2000UsrWhkInfo", paramMap);
	}
	
	/**
	 * 사용자 웹훅 정보 수정
	 * @param 
	 * @return 
	 * @exception Exception
	 */
	@SuppressWarnings({ "rawtypes" })
	public void updateWhk2000UsrWhkInfo(Map paramMap) throws Exception{
		update("whk2000DAO.updateWhk2000UsrWhkInfo",paramMap);
	}
	/**
	 * 사용자 웹훅 정보 삭제
	 * @param param - Map
	 * @return 
	 * @exception Exception
	 */
	@SuppressWarnings("rawtypes")
	public int deleteWhk2000UsrWhkInfo(Map paramMap) throws Exception{
		return (int) delete("whk2000DAO.deleteWhk2000UsrWhkInfo", paramMap);
	}
}