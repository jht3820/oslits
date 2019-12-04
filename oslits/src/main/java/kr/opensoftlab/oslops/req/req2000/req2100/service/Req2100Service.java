package kr.opensoftlab.oslops.req.req2000.req2100.service;

import java.util.List;
import java.util.Map;

/**
 * @Class Name : Req2100Service.java
 * @Description : Req2100Service Service class
 * @Modification Information
 *
 * @author 배용진
 * @since 2018.11.07.
 * @version 1.0
 * @see
 *  
 *  Copyright (C) OpenSoftLab Corp All right reserved.
 */

public interface Req2100Service {

	/**
	 * Req2100 프로세스 별 요구사항 처리완료 건수 조회
	 * @param Map
	 * @return List
	 * @throws Exception
	 */
	@SuppressWarnings("rawtypes")
	List<Map> selectReq2100ProcessReqEndCntAjax(Map paramMap) throws Exception;
}
