package kr.opensoftlab.oslits.cmm.cmm1000.cmm1600.service.impl;

import java.util.List;

import kr.opensoftlab.oslits.cmm.cmm1000.cmm1600.vo.Cmm1600VO;
import kr.opensoftlab.oslits.com.dao.ComOslitsAbstractDAO;

import org.springframework.stereotype.Repository;

/**
 * @Class Name : Cmm1600DAO.java
 * @Description : Cmm1600DAO DAO Class
 * @Modification Information
 *
 * @author 공대영
 *  * @since 2018.10.16.
 * @version 1.0
 * @see
 *  
 *  Copyright (C)  All right reserved.
 */

@Repository("cmm1600DAO")
public class Cmm1600DAO extends ComOslitsAbstractDAO {
	
	
	/**
	 * 
	 * 배포 조회 공통 목록 조회 
	 *  
	 * @param cmm1600VO
	 * @return
	 * @throws Exception
	 */
	public List selectCmm1600CommonDplList(Cmm1600VO cmm1600VO) throws Exception {
		return (List) list("cmm1600DAO.selectCmm1600CommonDplList", cmm1600VO);
	}
	
	/**
	 * 
	 * 	사용자 조회 공통 목록 전체 카운트 조회 
	 * 
	 * @param cmm1000VO
	 * @return
	 * @throws Exception
	 */
	public int selectCmm1600CommonDplListCnt(Cmm1600VO cmm1600VO) throws Exception {
		return  (Integer)select("cmm1600DAO.selectCmm1600CommonDplListCnt", cmm1600VO);
	}

}
