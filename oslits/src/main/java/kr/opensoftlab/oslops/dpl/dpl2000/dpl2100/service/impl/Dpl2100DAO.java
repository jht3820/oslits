package kr.opensoftlab.oslops.dpl.dpl2000.dpl2100.service.impl;

import java.util.List;
import java.util.Map;

import kr.opensoftlab.oslops.com.dao.ComOslitsAbstractDAO;
import kr.opensoftlab.oslops.dpl.dpl2000.dpl2100.vo.Dpl2100VO;

import org.springframework.stereotype.Repository;

/**
 * @Class Name : Dpl2100DAO.java
 * @Description : Dpl2100DAO DAO Class
 * @Modification Information
 *
 * @author 진주영
 * @since 2019.03.11
 * @version 1.0
 * @see
 *  
 *  --------------------------------------
 *  수정일			수정자			수정내용
 *  --------------------------------------
 *  
 *  
 *  --------------------------------------
 *  
 *  Copyright (C)  All right reserved.
 */

@Repository("dpl2100DAO")
public class Dpl2100DAO extends ComOslitsAbstractDAO {
	
	/**
	 * Dpl2100 배포계획 결재 목록 가져오기 
	 * @param param - Map
	 * @return list 배포자 리스트
	 * @throws Exception
	 */
	@SuppressWarnings({"rawtypes" })
	public List selectDpl2100SignList(Dpl2100VO dpl2100vo)  throws Exception{
		return (List) list("dpl2100DAO.selectDpl2100SignList", dpl2100vo);
	}
	
	/**
	 * Dpl2100 배포계획 결재 목록 총건수
	 * @param Dpl2100VO - dpl1000VO
	 * @return 
	 * @exception Exception
	 */
	public int selectDpl2100SignListCnt(Dpl2100VO dpl2100vo) throws Exception {
		return (Integer) select("dpl2100DAO.selectDpl2100SignListCnt", dpl2100vo);
	}
	
	/**
	 * Dpl2100 배포계획 결재 디테일 정보
	 * @param param - Map
	 * @return list 배포 계획 리스트
	 * @throws Exception
	 */
	@SuppressWarnings({"rawtypes" })
	public Map selectDpl2100SignInfoAjax(Map map)  throws Exception{
		return (Map) select("dpl2100DAO.selectDpl2100SignInfoAjax", map);
	} 
	
	/**
	 * Dpl2100 배포계획 결재자 등록
	 * @param param - Map
	 * @return 
	 * @exception Exception
	 */
	@SuppressWarnings("rawtypes")
	public void insertDpl2100DplSignInfo(Map paramMap) throws Exception{
		insert("dpl2100DAO.insertDpl2100DplSignInfo", paramMap);
	}
}
