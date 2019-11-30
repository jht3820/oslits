package kr.opensoftlab.oslops.adm.adm6000.adm6000.service.impl;

import java.util.List;
import java.util.Map;

import kr.opensoftlab.oslops.com.dao.ComOslitsAbstractDAO;

import org.springframework.stereotype.Repository;

/**
 * @Class Name : Adm6000DAO.java
 * @Description : Adm6000DAO DAO Class
 * @Modification Information
 *
 * @author 김정환
 * @since 2016.03.11
 * @version 1.0
 * @see
 *  
 *  Copyright (C)  All right reserved.
 */

@Repository("adm6000DAO")
public class Adm6000DAO extends ComOslitsAbstractDAO {

	/**
	 * 휴일 관리 내역을 조회한다.
	 * @param paramMap
	 * @return
	 * @throws Exception
	 */
	@SuppressWarnings("rawtypes")
	public List selectAdm6000List(Map paramMap) throws Exception {
		return (List) list("adm6000DAO.selectAdm6000List", paramMap);
	}
	
	/**
	 * 휴일관리 단건을 저장한다.
	 * @param paramMap
	 * @return
	 * @throws Exception
	 */
	@SuppressWarnings("rawtypes")
	public String insertAdm6000HoliInfo(Map paramMap) throws Exception{
		return (String) insert("adm6000DAO.insertAdm6000HoliInfo", paramMap);
	}

	/**
	 * 휴일관리 내역을 수정한다.
	 * @param paramMap
	 * @return
	 */
	@SuppressWarnings("rawtypes")
	public int updateAdm6000HoliInfo(Map paramMap)  throws Exception{
		return (int) update("adm6000DAO.updateAdm6000HoliInfo", paramMap);
	}

	/**
	 * 휴일관리 내역을 삭제한다. ( 단건 )
	 * @param paramMap
	 * @throws Exception
	 */
	@SuppressWarnings("rawtypes")
	public void deleteAdm6000HoliInfo(Map paramMap) throws Exception{
		delete("adm6000DAO.deleteAdm6000HoliInfo",paramMap);
	}

	/**
	 * 휴일관리 일괄삭제 한다.
	 * @param paramMap
	 * @throws Exception
	 */
	@SuppressWarnings("rawtypes")
	public void deleteAdm6000HoliList(Map paramMap) throws Exception{
		delete("adm6000DAO.deleteAdm6000HoliList",paramMap);
	}
	


	@SuppressWarnings("rawtypes")
	public List selectHoliCdList(Map pMap) throws Exception{
		return (List) list("adm6000DAO.selectHoliCdList", pMap);
	}

	/**
	 * 휴일관리(양력) 일괄등록 한다.
	 * @param paramMap
	 * @throws Exception
	 */
	@SuppressWarnings("rawtypes")
	public void insertAdm6000HoliList(Map paramMap) throws Exception{
		insert("adm6000DAO.insertAdm6000HoliList",paramMap);
	}
	
	/**
	 * 휴일관리(음력) 일괄등록 한다.
	 * @param paramMap
	 * @throws Exception
	 */
	public void insertAdm6000HoliSolarList(Map solarMap) throws Exception {
		insert("adm6000DAO.insertAdm6000HoliSolarList",solarMap );
	}
	
	
}
