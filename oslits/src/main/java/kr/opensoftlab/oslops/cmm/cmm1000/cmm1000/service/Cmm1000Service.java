package kr.opensoftlab.oslops.cmm.cmm1000.cmm1000.service;

import java.util.List;
import java.util.Map;

import kr.opensoftlab.oslops.adm.adm2000.adm2000.vo.Adm2000VO;
import kr.opensoftlab.oslops.cmm.cmm1000.cmm1000.vo.Cmm1000VO;

/**
 * @Class Name : Cmm1000Service.java
 * @Description : Cmm1000Service Business class
 * @Modification Information
 *
 * @author 공대영
 *  * @since 2018.07.23.
 * @version 1.0
 * @see
 *  
 *  Copyright (C)  All right reserved.
 */
public interface Cmm1000Service {
	
	/**
	 * 사용자 조회 공통 목록 조회 
	 * @param adm2000vo
	 * @return
	 * @throws Exception
	 */
	List selectCmm1000CommonUserList(Cmm1000VO cmm1000vo) throws Exception;
	
	/**
	 * 
	 * 사용자 조회 공통 목록 전체 카운트 조회
	 * 
	 * @param adm2000vo
	 * @return
	 * @throws Exception
	 */
	int selectCmm1000CommonUserListCnt(Cmm1000VO cmm1000vo) throws Exception;

	
	
	
	
}
