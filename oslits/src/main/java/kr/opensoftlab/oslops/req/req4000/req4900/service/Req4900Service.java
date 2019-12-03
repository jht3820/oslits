package kr.opensoftlab.oslops.req.req4000.req4900.service;

import java.util.Map;

public interface Req4900Service {

	/**
	 * Req4900 요구사항 결재정보 등록
	 * @param param - Map
	 * @return 
	 * @exception Exception
	 */	
	@SuppressWarnings("rawtypes")
	void insertReq4900ReqSignInfo(Map paramMap) throws Exception;
	
}
