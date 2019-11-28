package kr.opensoftlab.oslops.cmm.cmm1000.cmm1000.service.impl;

import java.util.List;
import java.util.Map;


import kr.opensoftlab.oslops.cmm.cmm1000.cmm1000.vo.Cmm1000VO;
import kr.opensoftlab.oslops.com.dao.ComOslitsAbstractDAO;

import org.springframework.stereotype.Repository;

/**
 * @Class Name : Cmm1000DAO.java
 * @Description : Cmm1000DAO DAO Class
 * @Modification Information
 *
 * @author 공대영
 *  * @since 2018.07.23.
 * @version 1.0
 * @see
 *  
 *  Copyright (C)  All right reserved.
 */

@Repository("cmm1000DAO")
public class Cmm1000DAO extends ComOslitsAbstractDAO {
	
	
	/**
	 * 
	 * 사용자 조회 공통 목록 조회 
	 *  
	 * @param cmm1000VO
	 * @return
	 * @throws Exception
	 */
	public List selectCmm1000CommonUserList(Cmm1000VO cmm1000VO) throws Exception {
		return (List) list("cmm1000DAO.selectCmm1000CommonUserList", cmm1000VO);
	}
	
	/**
	 * 
	 * 	사용자 조회 공통 목록 전체 카운트 조회 
	 * 
	 * @param cmm1000VO
	 * @return
	 * @throws Exception
	 */
	public int selectCmm1000CommonUserListCnt(Cmm1000VO cmm1000VO) throws Exception {
		return  (Integer)select("cmm1000DAO.selectCmm1000CommonUserListCnt", cmm1000VO);
	}

}
