package kr.opensoftlab.oslops.cmm.cmm1000.cmm1800.service.impl;

import java.util.List;
import java.util.Map;

import kr.opensoftlab.oslops.cmm.cmm1000.cmm1700.vo.Cmm1700VO;
import kr.opensoftlab.oslops.com.dao.ComOslitsAbstractDAO;

import org.springframework.stereotype.Repository;

/**
 * @Class Name : Cmm1800DAO.java
 * @Description : Cmm1800DAO DAO Class
 * @Modification Information
 *
 * @author 공대영
 *  * @since 2018.10.16.
 * @version 1.0
 * @see
 *  
 *  Copyright (C)  All right reserved.
 */

@Repository("cmm1800DAO")
public class Cmm1800DAO extends ComOslitsAbstractDAO {

	public List<Map> selectCmm1800ProcessList(Map<String, String> paramMap) {
		// TODO Auto-generated method stub
		return (List) list("cmm1800DAO.selectCmm1800ProcessList", paramMap);
	}
	


}
