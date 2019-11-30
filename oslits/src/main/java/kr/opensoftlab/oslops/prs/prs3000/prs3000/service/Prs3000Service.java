package kr.opensoftlab.oslops.prs.prs3000.prs3000.service;

import java.util.Map;

import kr.opensoftlab.oslops.com.vo.LoginVO;


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
	 * @return Map
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
	 * Prs3001 비밀번호 수정(UPDATE) AJAX
	 * @param Map
	 * @return int
	 * @exception Exception
	 */
	int updatePrs3001(Map<String, String> paramMap) throws Exception;

	/**
	 * Prs3002 대시보드 표시 구분, 메인 프로젝트 조회
	 * @param Map
	 * @return Map
	 * @exception Exception
	 */
	@SuppressWarnings("rawtypes")
	Map selectPrs3002(Map<String, String> paramMap) throws Exception;


	/**
	 * Prs3002 기타정보 구분 수정
	 * @param Map
	 * @return int
	 * @exception Exception
	 */
	int updatePrs3002(Map<String, String> paramMap) throws Exception;


	/**
	 * Prs3000LoginVO 세션 갱신
	 * @param Map
	 * @return LoginVO
	 * @exception Exception
	 */
	LoginVO selectPrs3000LoginVO(Map<String, String> paramMap) throws Exception;
}
