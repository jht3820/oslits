package kr.opensoftlab.oslops.dpl.dpl2000.dpl2000.service.impl;

import java.util.List;
import java.util.Map;

import kr.opensoftlab.oslops.com.dao.ComOslitsAbstractDAO;
import kr.opensoftlab.oslops.dpl.dpl2000.dpl2000.vo.Dpl2000VO;

import org.springframework.stereotype.Repository;

/**
 * @Class Name : Dpl2000DAO.java
 * @Description : Dpl2000DAO DAO Class
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

@Repository("dpl2000DAO")
public class Dpl2000DAO extends ComOslitsAbstractDAO {
	
	/**
	 * Dpl2000 배포계획 결재 목록 가져오기 
	 * @param param - Map
	 * @return list 배포자 리스트
	 * @throws Exception
	 */
	@SuppressWarnings({"rawtypes" })
	public List selectDpl2000SignList(Dpl2000VO dpl2000vo)  throws Exception{
		return (List) list("dpl2000DAO.selectDpl2000SignList", dpl2000vo);
	}
	
	/**
	 * Dpl2000 배포계획 결재 목록 총건수
	 * @param Dpl2100VO - dpl1000VO
	 * @return 
	 * @exception Exception
	 */
	public int selectDpl2000SignListCnt(Dpl2000VO dpl2000vo) throws Exception {
		return (Integer) select("dpl2000DAO.selectDpl2000SignListCnt", dpl2000vo);
	}
	
	/**
	 * Dpl2000 배포계획 결재 디테일 정보
	 * @param param - Map
	 * @return list 배포 계획 리스트
	 * @throws Exception
	 */
	@SuppressWarnings({"rawtypes" })
	public Map selectDpl2000SignInfoAjax(Map map)  throws Exception{
		return (Map) select("dpl2000DAO.selectDpl2000SignInfoAjax", map);
	} 
}
