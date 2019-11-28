package kr.opensoftlab.oslops.req.req4000.req4400.service;

import java.util.List;
import java.util.Map;

import kr.opensoftlab.oslops.req.req4000.req4400.vo.Req4400VO;


/**
 * @Class Name : Req4300Service.java
 * @Description : Req4300Service Business class
 * @Modification Information
 *
 * @author 진주영
 * @since 2018.09.11
 * @version 1.0
 * @see
 *  
 *  Copyright (C)  All right reserved.
 */
public interface Req4400Service {

	/**
	 * Req4400 요구사항 작업흐름별 작업 목록 조회(Grid page)
	 * @param Req4400VO
	 * @exception Exception
	 */
	@SuppressWarnings("rawtypes")
	List selectReq4400ReqWorkList(Req4400VO req4400VO) throws Exception;
	
	/**
	 * Req4400 요구사항 작업흐름별 작업 목록 총 건수(Grid page)
	 * @param Req4400VO
	 * @return 
	 * @exception Exception
	 */
	int selectReq4400ReqWorkListCnt(Req4400VO req4400VO) throws Exception;
	
	/**
	 * Req4400 요구사항 작업흐름별 작업 단건 조회 
	 * @param param - Map
	 * @exception Exception
	 */
	@SuppressWarnings("rawtypes")
	Map selectReq4400ReqWorkInfo(Map paramMap) throws Exception;
	
	/**
	 * Req4400 담당 작업 단건 조회
	 * @param param - Map
	 * @exception Exception
	 */
	@SuppressWarnings("rawtypes")
	List selectReq4400ReqChargerWorkList(Map paramMap) throws Exception;
	
	/**
	 * Req4400 요구사항 작업흐름별 작업 단건 추가
	 * @param param - Map
	 * @exception Exception
	 */
	@SuppressWarnings("rawtypes")
	void insertReq4400ReqWorkInfoAjax(Map paramMap) throws Exception;
	
	/**
	 * Req4400 요구사항 작업흐름별 작업 단건 수정 
	 * @param param - Map
	 * @exception Exception
	 */
	@SuppressWarnings("rawtypes")
	void updateReq4400ReqWorkInfoAjax(Map paramMap) throws Exception;
	
	/**
	 * Req4400 요구사항 작업흐름별 작업 단건 삭제
	 * @param param - Map
	 * @exception Exception
	 */
	@SuppressWarnings("rawtypes")
	void deleteReq4400ReqWorkInfoAjax(Map paramMap) throws Exception;
}
