package kr.opensoftlab.oslops.cmm.cmm9000.cmm9000.service;

import java.util.Map;

/**
 * @Class Name : Cmm9000Service.java
 * @Description : Cmm9000Service Business class
 * @Modification Information
 *
 * @author 정형택
 * @since 2016.01.20.
 * @version 1.0
 * @see
 *  
 *  Copyright (C)  All right reserved.
 */
public interface Cmm9000Service {
	
	/**
	 * Cmm9000 왼쪽 메뉴 영역에 표시할 각종 정보 건수를 조회(알림, 담당요구사항, 전체요구사항)
	 * @param param - LoginVO
	 * @return Map
	 * @exception Exception
	 */
	@SuppressWarnings("rawtypes")
	Map selectCmm9000LeftMenuInfos(Map paramMap) throws Exception;
	
}
