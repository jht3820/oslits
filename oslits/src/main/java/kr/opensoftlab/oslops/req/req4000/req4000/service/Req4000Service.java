package kr.opensoftlab.oslops.req.req4000.req4000.service;

import java.util.List;
import java.util.Map;

import kr.opensoftlab.sdf.excel.ExcelDataListResultHandler;

/**
 * @Class Name : Req4000Service.java
 * @Description : Req4000Service Business class
 * @Modification Information
 *
 * @author 정형택
 * @since 2016.01.30.
 * @version 1.0
 * @see
 *  
 *  Copyright (C) OpenSoftLab Corp. All right reserved.
 */
public interface Req4000Service {
	
	/**
	 * Req4000 요구사항 분류정보 목록 가져오기
	 * @param param - Map
	 * @return 
	 * @exception Exception
	 */
	@SuppressWarnings("rawtypes")
	List selectReq4000ReqClsList(Map paramMap) throws Exception;	
	
	
	/**
	 * Req4000 요구사항 분류 정보 조회
	 * @param param - Map
	 * @return 
	 * @exception Exception
	 */
	@SuppressWarnings("rawtypes")
	Map selectReq4000ReqClsInfo(Map paramMap) throws Exception;	
	
	/**
	 * Req4000 요구사항 분류 정보 등록(단건) AJAX
	 * 메뉴정보 등록후 등록 정보 결과 및 정보를 리턴한다.
	 * @param 
	 * @return 
	 * @exception Exception
	 */
	@SuppressWarnings("rawtypes")
	Map insertReq4000ReqClsInfo(Map paramMap) throws Exception;
	

	/**
	 * Req4000 분류에 요구사항이 배정되어있는지 체크한다.
	 * @param 
	 * @return List<String> 요구사항이 배정된 분류 명 목록
	 * @exception Exception
	 */
	@SuppressWarnings("rawtypes")
	List<String> deleteReq4000ReqClsAssignChk(Map paramMap) throws Exception;
	
	
	/**
	 * Req4000 요구사항 분류 정보 삭제
	 * @param 
	 * @return 
	 * @exception Exception
	 */
	@SuppressWarnings("rawtypes")
	void deleteReq4000ReqClsInfo(Map paramMap) throws Exception;
	
	/**
	 * Req4000 요구사항 분류 정보 수정(단건) AJAX
	 * 메뉴정보 수정
	 * @param 
	 * @return 
	 * @exception Exception
	 */
	@SuppressWarnings("rawtypes")
	void updateReq4000ReqClsInfo(Map paramMap) throws Exception;


	@SuppressWarnings("rawtypes")
	void selectReq4000ExcelList(Map paramMap,
			ExcelDataListResultHandler resultHandler) throws Exception;
}
