package kr.opensoftlab.oslops.req.req4700.req4700.service;

import java.util.Map;

public interface Req4700Service {

	/**
	 * Req4700 요구사항 변경 내용 등록
	 * @param param - Map
	 * @return 
	 * @exception Exception
	 */	
	@SuppressWarnings("rawtypes")
	String insertReq4700ReqChangeLogInfo(Map paramMap) throws Exception;
}
