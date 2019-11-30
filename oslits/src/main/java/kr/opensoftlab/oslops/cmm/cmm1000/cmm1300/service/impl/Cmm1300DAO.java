package kr.opensoftlab.oslops.cmm.cmm1000.cmm1300.service.impl;

import java.util.List;
import java.util.Map;

import kr.opensoftlab.oslops.com.dao.ComOslitsAbstractDAO;
import kr.opensoftlab.oslops.com.vo.LoginVO;
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

@Repository("cmm1300DAO")
public class Cmm1300DAO extends ComOslitsAbstractDAO {
	
	/**
	 * Adm4000 공통코드 마스터 조회
	 * @param param - Map
	 * @return list 공통코드 마스터 리스트
	 * @throws Exception
	 */
	@SuppressWarnings({"rawtypes" })
	public List selectCmm1300List(Map inputMap)  throws Exception{
		return (List) list("cmm1300DAO.selectCmm1300List", inputMap);
	}

}
