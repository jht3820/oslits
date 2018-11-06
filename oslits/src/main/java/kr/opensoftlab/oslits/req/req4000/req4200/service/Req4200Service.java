package kr.opensoftlab.oslits.req.req4000.req4200.service;

import java.util.List;
import java.util.Map;

/**
 * @Class Name : Req4200Service.java
 * @Description : Req4200Service Business class
 * @Modification Information
 *
 * @author 정형택
 * @since 2016.02.08.
 * @version 1.0
 * @see
 *  
 *  Copyright (C) OpenSoftLab Corp. All right reserved.
 */
public interface Req4200Service {
	

	/**
	 * Req4200 요구사항 분류정보 목록 가져오기
	 * @param param - Map
	 * @return 
	 * @exception Exception
	 */
	@SuppressWarnings("rawtypes")
	List selectReq4200ReqClsList(Map paramMap) throws Exception;	
	
	/**
	 * Req4200 요구사항 분류 배정 목록 가져오기
	 * @param param - Map
	 * @return 
	 * @exception Exception
	 */
	@SuppressWarnings("rawtypes")
	List selectReq4200ReqClsAddListAjax(Map paramMap) throws Exception;	
	
	
	/**
	 * Req4200 요구사항 분류 미배정 목록 가져오기
	 * @param param - Map
	 * @return 
	 * @exception Exception
	 */
	@SuppressWarnings("rawtypes")
	List selectReq4200ReqClsDelListAjax(Map paramMap) throws Exception;	
	
	/**
	 * Req4200 요구사항 분류에 요구사항 배정 및 삭제 처리
	 * @param param - Map
	 * @return 
	 * @exception Exception
	 */
	@SuppressWarnings("rawtypes")
	void updateReq4200ReqClsAddDelListAjax(Map paramMap) throws Exception;
	

}