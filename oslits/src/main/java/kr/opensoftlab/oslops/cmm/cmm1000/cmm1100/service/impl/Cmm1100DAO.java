package kr.opensoftlab.oslops.cmm.cmm1000.cmm1100.service.impl;

import java.util.List;
import java.util.Map;

import org.springframework.stereotype.Repository;

import kr.opensoftlab.oslops.com.dao.ComOslitsAbstractDAO;


/**
 * @Class Name : Cmm1100DAO.java
 * @Description : Cmm1100DAO DAO Class
 * @Modification Information
 *
 * @author 공대영
 * @since 2018.09.11.
 * @version 1.0
 * @see
 *  
 *  Copyright (C) OpenSoftLab Corp. All right reserved.
 */
@Repository("cmm1100DAO")
public class Cmm1100DAO extends ComOslitsAbstractDAO {
	/**
	 * Prj1000 프로젝트 생성관리 목록 조회
	 * @param param - Map
	 * @return 
	 * @exception Exception
	 */
	@SuppressWarnings("rawtypes")
	public List selectCmm1100View(Map paramMap) throws Exception {
		 return (List) list("cmm1100DAO.selectCmm1100View", paramMap);
    }
	
	
}
