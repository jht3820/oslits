package kr.opensoftlab.oslits.cmm.cmm1000.cmm1400.service.impl;

import java.util.List;
import java.util.Map;

import kr.opensoftlab.oslits.com.dao.ComOslitsAbstractDAO;
import kr.opensoftlab.oslits.com.vo.LoginVO;
import kr.opensoftlab.sdf.excel.ExcelDataListResultHandler;

import org.springframework.stereotype.Repository;

/**
 * @Class Name : Cmm1300DAO.java
 * @Description : Cmm1300DAO DAO Class
 * @Modification Information
 *
 * @author 공대영
 * @since 2018.07.31
 * @version 1.0
 * @see
 *  
 *  Copyright (C)  All right reserved.
 */

@Repository("cmm1400DAO")
public class Cmm1400DAO extends ComOslitsAbstractDAO {
	
	public List<Map> selectCmm1400SVNRepositoryList(Map<String, String> paramMap) {
		// TODO Auto-generated method stub
		return (List) list("cmm1400DAO.selectCmm1400SVNRepositoryList", paramMap);
	}

}
