package kr.opensoftlab.oslops.req.req2000.req2000.service;

/**
 * @Class Name : Req2000Service.java
 * @Description : Req2000Service Service class
 * @Modification Information
 *
 * @author 정형택
 * @since 2016.01.23.
 * @version 1.0
 * @see
 *  
 *  Copyright (C) OpenSoftLab Corp All right reserved.
 */

import java.util.List;
import java.util.Map;

import kr.opensoftlab.oslops.req.req4000.req4100.vo.Req4100VO;
import kr.opensoftlab.sdf.excel.ExcelDataListResultHandler;

public interface Req2000Service {

	/**
	 * 요구사항 코멘트 목록을 조회한다.
	 * @param Map
	 * @return List 요구사항 코멘트 목록
	 * @throws Exception
	 */
	@SuppressWarnings("rawtypes")
	List<Map> selectReq2000ReqCommentListAjax(Map paramMap) throws Exception;
	
	/**
	 * 요구사항 코멘트 등록.
	 * @param Map
	 * @return  void 
	 * @throws Exception
	 */
	@SuppressWarnings("rawtypes")
	void insertReq2000ReqCommentInfo(Map paramMap) throws Exception;
	
	/**
	 * Req2000 요구사항 목록 엑셀 다운로드
	 * @param param - Map
	 * @param resultHandler ExcelDataListResultHandler
	 * @return 
	 * @exception Exception
	 */
	void selectReq2000ExcelList(Map paramMap, ExcelDataListResultHandler resultHandler) throws Exception;

}
