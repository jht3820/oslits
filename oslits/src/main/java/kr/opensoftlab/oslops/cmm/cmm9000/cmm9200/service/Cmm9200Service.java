package kr.opensoftlab.oslops.cmm.cmm9000.cmm9200.service;

import java.util.List;
import java.util.Map;

/**
 * @Class Name : Cmm9200Service.java
 * @Description : Cmm9200Service Business class
 * @Modification Information
 *
 * @author 정형택
 * @since 2016.01.14.
 * @version 1.0
 * @see
 *  
 *  Copyright (C)  All right reserved.
 */
public interface Cmm9200Service {
	/**
	 * 공통으로 사용할 사용자 정보 목록을 조회한다.(콤보박스용)
	 * @param param - 조회할 정보가 담긴 Map
	 * @return 사용자 정보 목록
	 * @exception Exception
	 */
	@SuppressWarnings("rawtypes")
	List selectCmm9200PrjUsrList(Map param) throws Exception;
	
	
}
