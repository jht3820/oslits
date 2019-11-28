package kr.opensoftlab.oslops.adm.adm6000.adm6000.service;

import java.util.List;
import java.util.Map;



/**
 * @Class Name : Adm6000Service.java
 * @Description : Adm6000Service Business class
 * @Modification Information
 *
 * @author 김정환
 * @since 2016.03.11
 * @version 1.0
 * @see
 *  
 *  Copyright (C)  All right reserved.
 */
public interface Adm6000Service {

	
	
	
	/**
	 * 휴일 관리 내역을 조회한다.
	 * @param paramMap
	 * @return
	 * @throws Exception
	 */
	@SuppressWarnings("rawtypes")
	List selectAdm6000List(Map paramMap) throws Exception;

	/**
	 * 휴일관리 단건을 등록한다.
	 * @param paramMap
	 * @return
	 * @throws Exception
	 */
	@SuppressWarnings("rawtypes")
	String insertAdm6000HoliInfo(Map paramMap) throws Exception;
	
	/**
	 * 휴일관리 일괄등록 한다.
	 * @param paramMap
	 * @throws Exception
	 */
	@SuppressWarnings("rawtypes")
	void insertAdm6000HoliList(Map paramMap) throws Exception;
	
	/**
	 * 휴일관리 내역을 수정한다.
	 * @param paramMap
	 * @throws Exception
	 */
	@SuppressWarnings("rawtypes")
	void updateAdm6000HoliInfo(Map paramMap) throws Exception;
	
	/**
	 * 휴일관리 내역을 삭제한다. ( 단건 )
	 * @param paramMap
	 * @throws Exception
	 */
	@SuppressWarnings("rawtypes")
	void deleteAdm6000HoliInfo(Map paramMap) throws Exception;
	
}
