package kr.opensoftlab.oslops.cmm.cmm1000.cmm1100.service;

import java.util.List;
import java.util.Map;


/**
 * @Class Name : Cmm1100Service.java
 * @Description : Cmm1100Service Business class
 * @Modification Information
 *
 * @author 공대영
 * @since 2018.09.11.
 * @version 1.0
 * @see
 *  
 *  Copyright (C) OpenSoftLab Corp. All right reserved.
 */
public interface Cmm1100Service {
	/**
	 * Prj1000 프로젝트 생성관리 목록 조회
	 * @param param - Map
	 * @return 
	 * @exception Exception
	 */
	@SuppressWarnings("rawtypes")
	List selectCmm1100View(Map paramMap) throws Exception;
	
}
