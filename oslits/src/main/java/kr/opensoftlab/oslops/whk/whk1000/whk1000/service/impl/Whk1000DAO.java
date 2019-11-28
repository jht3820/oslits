package kr.opensoftlab.oslops.whk.whk1000.whk1000.service.impl;

import java.util.List;
import java.util.Map;

import org.springframework.stereotype.Repository;

import kr.opensoftlab.oslops.com.dao.ComOslitsAbstractDAO;
import kr.opensoftlab.oslops.whk.whk1000.whk1000.vo.Whk1000VO;

/**
 * @Class Name : Whk1000DAO.java
 * @Description : Whk1000DAO DAO Class
 * @Modification Information
 *
 * @author 진주영
 * @since 2019.05.21.
 * @version 1.0
 * @see
 *  
 *  Copyright (C) OpenSoftLab Corp. All right reserved.
 */
@Repository("whk1000DAO")
public class Whk1000DAO  extends ComOslitsAbstractDAO {
	
	/**
	 * 웹훅 목록 조회
	 * @param pageVO
	 * @return List 웹훅 목록
	 * @throws Exception
	 */
	@SuppressWarnings({ "unchecked" })
	public List<Whk1000VO>  selectWhk1000PrjWhkList(Whk1000VO whk1000vo) throws Exception {
		 return  (List<Whk1000VO>) list("whk1000DAO.selectWhk1000PrjWhkList", whk1000vo);
	}
	
	/**
	 * 웹훅 목록 총건수
	 * @param Whk1000VO
	 * @throws Exception
	 */
	public int  selectWhk1000PrjWhkListCnt(Whk1000VO whk1000vo) throws Exception {
		 return  (Integer)select("whk1000DAO.selectWhk1000PrjWhkListCnt", whk1000vo);
	} 
	
	/**
	 * 웹훅 전체 목록 조회 (no paging)
	 * @param Map
	 * @return List 웹훅 목록
	 * @throws Exception
	 */
	@SuppressWarnings({ "rawtypes", "unchecked" })
	public List<Map> selectWhk1000PrjWhkAllList(Map paramMap) throws Exception{
		return (List<Map>) list("whk1000DAO.selectWhk1000PrjWhkAllList", paramMap);
	}
	
	/**
	 * 웹훅 단건 조회
	 * @param Map
	 * @return Map 웹훅 정보
	 * @throws Exception
	 */
	@SuppressWarnings({ "rawtypes" })
	public Map selectWhk1000PrjWhkInfo(Map paramMap) throws Exception{
		return (Map) select("whk1000DAO.selectWhk1000PrjWhkInfo", paramMap);
	}
	
	/**
	 * 웹훅 정보 등록
	 * @param param - Map
	 * @return 
	 * @exception Exception
	 */
	@SuppressWarnings("rawtypes")
	public String insertWhk1000PrjWhkInfo(Map paramMap) throws Exception{
		return (String) insert("whk1000DAO.insertWhk1000PrjWhkInfo", paramMap);
	}
	
	/**
	 * 웹훅 정보 수정
	 * @param 
	 * @return 
	 * @exception Exception
	 */
	@SuppressWarnings({ "rawtypes" })
	public void updateWhk1000PrjWhkInfo(Map paramMap) throws Exception{
		update("whk1000DAO.updateWhk1000PrjWhkInfo",paramMap);
	}
	/**
	 * 웹훅 정보 삭제
	 * @param param - Map
	 * @return 
	 * @exception Exception
	 */
	@SuppressWarnings("rawtypes")
	public int deleteWhk1000PrjWhkInfo(Map paramMap) throws Exception{
		return (int) delete("whk1000DAO.deleteWhk1000PrjWhkInfo", paramMap);
	}
}