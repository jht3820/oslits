package kr.opensoftlab.oslops.req.req4600.req4600.service;

import java.util.List;
import java.util.Map;


/**
 * @Class Name : Req4600Service.java
 * @Description : Req4600Service Business class
 * @Modification Information
 *
 * @author 진주영
 * @since 2017.06.07
 * @version 1.0
 * @see
 *  
 *  Copyright (C)  All right reserved.
 */
public interface Req4600Service {

	/**
	 * Req4600 요구사항 클립보드 내용 등록
	 * @param param - Map
	 * @return 
	 * @exception Exception
	 */	
	@SuppressWarnings("rawtypes")
	String insertReq4600CBPaste(Map paramMap) throws Exception;
	
	/**
	 * 요구사항 CB_ATCH_FILE_ID 등록
	 * @param 	param - Map
	 * @throws 	Exception
	 */
	@SuppressWarnings("rawtypes")
	int updateReq4100CbAtchFileId(Map paramMap) throws Exception;
	
	/**
	 * 요구사항 클립보드 데이터 불러오기
	 * @param 	param - Map
	 * @return	List
	 * @throws 	Exception
	 */
	@SuppressWarnings("rawtypes")
	List<Map>  selectReq4600CBContentList(Map paramMap) throws Exception;
	
	/**
	 * 요구사항 클립보드 데이터 FILE_SN값 구하기
	 * @return	List
	 * @throws 	Exception
	 */
	@SuppressWarnings("rawtypes")
	int  selectReq4600CBFileSnMax(Map paramMap) throws Exception;
	
	/**
	 * 요구사항 클립보드 데이터 단건 조회
	 * @param 	param - Map
	 * @return	Map
	 * @throws 	Exception
	 */
	@SuppressWarnings("rawtypes")
	Map  selectReq4600CBContentInfo(Map paramMap) throws Exception;
	
	/**
	 * 요구사항 클립보드 데이터 단건 삭제
	 * @param 	param - Map
	 * @return	Map
	 * @throws 	Exception
	 */
	@SuppressWarnings("rawtypes")
	void deleteReq4600CBContentInfo(Map paramMap) throws Exception;
	
	/**
	 * 요구사항 클립보드 데이터 단건 수정
	 * @param 	param - Map
	 * @return	int
	 * @throws 	Exception
	 */
	@SuppressWarnings("rawtypes")
	int updateReq4600CBContentInfo(Map paramMap) throws Exception;
	
	/**
	 * 요구사항 클립보드 다음 SEQ 구하기
	 * @param 	param - Map
	 * @return	Map
	 * @throws 	Exception
	 */
	@SuppressWarnings("rawtypes")
	int selectReq4600CBMaxSeq(Map paramMap) throws Exception;
	
	/**
	 * REQ4100 요구사항 WBS 항목 조회
	 * @param param - Map
	 * @return list - List
	 * @exception Exception
	 */
	@SuppressWarnings("rawtypes")
	List selectReq4600ReqWbsListAjax(Map paramMap) throws Exception;

	/**
	 * Req4600 요구사항 WBS 진척률 수정
	 * @param paramMap
	 * @return
	 * @throws Exception
	 */
	@SuppressWarnings("rawtypes")
	void updateReq4600ProgresInfo(Map paramMap) throws Exception;
}
