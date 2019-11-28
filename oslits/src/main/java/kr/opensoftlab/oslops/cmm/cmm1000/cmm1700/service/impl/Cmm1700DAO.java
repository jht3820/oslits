package kr.opensoftlab.oslops.cmm.cmm1000.cmm1700.service.impl;

import java.util.List;

import kr.opensoftlab.oslops.cmm.cmm1000.cmm1700.vo.Cmm1700VO;
import kr.opensoftlab.oslops.com.dao.ComOslitsAbstractDAO;

import org.springframework.stereotype.Repository;

/**
 * @Class Name : Cmm1700DAO.java
 * @Description : Cmm1700DAO DAO Class
 * @Modification Information
 *
 * @author 공대영
 *  * @since 2018.10.16.
 * @version 1.0
 * @see
 *  
 *  Copyright (C)  All right reserved.
 */

@Repository("cmm1700DAO")
public class Cmm1700DAO extends ComOslitsAbstractDAO {
	
	
	/**
	 * 
	 * 역할 조회 공통 목록 조회 
	 *  
	 * @param cmm1600VO
	 * @return
	 * @throws Exception
	 */
	public List selectCmm1700CommonAuthList(Cmm1700VO cmm1700VO) throws Exception {
		return (List) list("cmm1700DAO.selectCmm1700CommonAuthList", cmm1700VO);
	}
	
	/**
	 * 
	 * 	역할 조회 공통 목록 전체 카운트 조회 
	 * 
	 * @param cmm1000VO
	 * @return
	 * @throws Exception
	 */
	public int selectCmm1700CommonAuthListCnt(Cmm1700VO cmm1700VO) throws Exception {
		return  (Integer)select("cmm1700DAO.selectCmm1700CommonAuthListCnt", cmm1700VO);
	}

}
