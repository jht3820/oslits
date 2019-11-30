package kr.opensoftlab.oslops.cmm.cmm9000.cmm9000.service.impl;

import java.util.List;
import java.util.Map;

import kr.opensoftlab.oslops.com.dao.ComOslitsAbstractDAO;

import org.springframework.stereotype.Repository;

/**
 * @Class Name : Cmm9000DAO.java
 * @Description : Cmm9000DAO DAO Class
 * @Modification Information
 *
 * @author 정형택
 *  * @since 2016.01.20.
 * @version 1.0
 * @see
 *  
 *  Copyright (C)  All right reserved.
 */

@Repository("cmm9000DAO")
public class Cmm9000DAO extends ComOslitsAbstractDAO {
	
	/**
	 * Cmm9000 왼쪽 메뉴 영역에 표시할 각종 정보 건수를 조회(알림, 담당요구사항, 전체요구사항)
	 * @param param - Map
	 * @return Map
	 * @exception Exception
	 */
	@SuppressWarnings("rawtypes")
	Map selectCmm9000AlarmCntInfo(Map paramMap) throws Exception{
		return (Map) select("cmm9000DAO.selectCmm9000AlarmCntInfo", paramMap);
	}
	
	/**
	 * Cmm9000 담당 요구사항 건수 조회(전체건수 포함)
	 * @param param - Map
	 * @return Map
	 * @exception Exception
	 */
	@SuppressWarnings("rawtypes")
	Map selectCmm9000ChargeReqCntList(Map paramMap) throws Exception{
		return (Map) select("cmm9000DAO.selectCmm9000ChargeReqCntList", paramMap);
	}
	
	/**
	 * Cmm9000 전체 요구사항 건수 조회(전체건수 포함)
	 * @param param - Map
	 * @return Map
	 * @exception Exception
	 */
	@SuppressWarnings("rawtypes")
	Map selectCmm9000AllReqCntList(Map paramMap) throws Exception{
		return (Map) select("cmm9000DAO.selectCmm9000AllReqCntList", paramMap);
	}
	
	
	/**
	 * Cmm9000 좌측 알림 구분을 위해 선택된 프로젝트 타입 조회
	 * @param param - Map
	 * @return Map
	 * @exception Exception
	 */
	@SuppressWarnings("rawtypes")
	String selectCmm9000ProjectType(Map paramMap) throws Exception{
		return (String) select("cmm9000DAO.selectCmm9000ProjectType", paramMap);
	}
	
}
