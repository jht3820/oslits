package kr.opensoftlab.oslops.cmm.cmm3000.cmm3200.service;

import java.util.Map;


/**
 * @Class Name : Cmm3200Service.java
 * @Description : Cmm3200Service Business class
 * @Modification Information
 *
 * @author 김정환
 * @since 2016.01.16
 * @version 1.0
 * @see
 *  
 *  Copyright (C)  All right reserved.
 */
public interface Cmm3200Service {

	/**
	 * 아이디 체크 ( 중복확인 )
	 * @param paramMap
	 * @return
	 * @throws Exception
	 */
	@SuppressWarnings("rawtypes")
	int selectCmm3200idChk(Map paramMap)  throws Exception;
	
	/**
	 * 
	 * 회원가입 처리
	 * @param paramMap
	 * @return 
	 * @exception Exception
	 */
	@SuppressWarnings("rawtypes")
	String insertCmm3200JoinIng(Map paramMap) throws Exception;
	
	
}
