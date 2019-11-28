package kr.opensoftlab.oslops.req.req4000.req4800.service;

import java.util.List;
import java.util.Map;

public interface Req4800Service {
	/**
	 * Req4800 요구사항 수정이력 목록 가져오기
	 * @param param - Map
	 * @return 
	 * @exception Exception
	 */	
	@SuppressWarnings("rawtypes")
	List<Map> selectReq4800ChgDetailList(Map paramMap) throws Exception;
	
	/**
	 * Req4800 요구사항 변경사항 검색 후 수정이력 등록
	 * @param param - Map
	 * @return 
	 * @exception Exception
	 */	
	@SuppressWarnings("rawtypes")
	void insertReq4800ModifyHistoryList(Map paramMap) throws Exception;
		
	/**
	 * Req4800 요구사항 수정이력 등록
	 * @param param - Map
	 * @return 
	 * @exception Exception
	 */	
	@SuppressWarnings("rawtypes")
	String insertReq4800ModifyHistoryInfo(Map paramMap) throws Exception;
	
	/**
	 * Req4800 요구사항 수정이력 삭제
	 * - 요구사항 삭제 시 해당 요구사항의 수정이력도 삭제한다.
	 * @param param - Map
	 * @return 
	 * @exception Exception
	 */
	@SuppressWarnings("rawtypes")
	int deleteReq4800ReqHistoryInfo(Map paramMap) throws Exception;
	
}
