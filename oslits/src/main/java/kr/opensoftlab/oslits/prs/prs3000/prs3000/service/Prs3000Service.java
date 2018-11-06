package kr.opensoftlab.oslits.prs.prs3000.prs3000.service;

import java.util.List;
import java.util.Map;

import kr.opensoftlab.oslits.com.vo.LoginVO;


/**
 * @Class Name : Prs3000Service.java
 * @Description : Prs3000Service Business class
 * @Modification Information
 *
 * @author 강재민
 * @since 2016.01.31
 * @version 1.0
 * @see
 *  
 *  Copyright (C)  All right reserved.
 */
public interface Prs3000Service {
	/**
	 * selectPrs3000 개인정보를 조회한다.(단건)
	 * @param Map
	 * @return int
	 * @exception Exception
	 */
	@SuppressWarnings("rawtypes")
	Map selectPrs3000(Map paramMap) throws Exception;
	
	
	
	/**
	 * Prs3000 개인정보 수정 화면(이메일 체크) AJAX
	 * @param Map
	 * @return int
	 * @exception Exception
	 */
	@SuppressWarnings("rawtypes")
	int selectPrs3000emailChRepAjax(Map paramMap) throws Exception;
	
	
	
	/**
	 * Prs3000 개인정보 수정(UPDATE) AJAX
	 * @param Map
	 * @return int
	 * @exception Exception
	 */
	@SuppressWarnings("rawtypes")
	int updatePrs3000(Map paramMap) throws Exception;
	
	
	/**
	 * Prs3000 개인정보 수정 화면(수정 전 비밀번호 조회) AJAX
	 * @param Map
	 * @return int
	 * @exception Exception
	 */
	@SuppressWarnings("rawtypes")
	String selectPrs3000PwCheck(Map paramMap) throws Exception;
}
