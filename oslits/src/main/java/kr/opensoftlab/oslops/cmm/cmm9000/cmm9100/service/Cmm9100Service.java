package kr.opensoftlab.oslops.cmm.cmm9000.cmm9100.service;

import java.util.List;
import java.util.Map;

/**
 * @Class Name : Cmm9100Service.java
 * @Description : Cmm9100Service Business class
 * @Modification Information
 *
 * @author 정형택
 * @since 2016.01.14.
 * @version 1.0
 * @see
 *  
 *  Copyright (C)  All right reserved.
 */
public interface Cmm9100Service {
	/**
	 * 공통 코드 다중 목록을 조회한다.
	 * @param param - 조회할 정보가 담긴 Map
	 * @return 공통코드 목록
	 * @exception Exception
	 */
	@SuppressWarnings("rawtypes")
	List selectCmm9100MultiCommonCodeList(Map param) throws Exception;
}
