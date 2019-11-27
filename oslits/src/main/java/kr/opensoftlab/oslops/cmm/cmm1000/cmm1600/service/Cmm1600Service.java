package kr.opensoftlab.oslits.cmm.cmm1000.cmm1600.service;

import java.util.List;
import java.util.Map;

import kr.opensoftlab.oslits.adm.adm2000.adm2000.vo.Adm2000VO;
import kr.opensoftlab.oslits.cmm.cmm1000.cmm1000.vo.Cmm1000VO;
import kr.opensoftlab.oslits.cmm.cmm1000.cmm1600.vo.Cmm1600VO;

/**
 * @Class Name : Cmm1600Service.java
 * @Description : Cmm1600Service Business class
 * @Modification Information
 *
 * @author 공대영
 *  * @since 2018.10.16.
 * @version 1.0
 * @see
 *  
 *  Copyright (C)  All right reserved.
 */
public interface Cmm1600Service {
	
	/**
	 * 배포 조회 공통 목록 조회 
	 * @param cmm1600vo
	 * @return
	 * @throws Exception
	 */
	List selectCmm1600CommonDplList(Cmm1600VO cmm1600vo) throws Exception;
	
	/**
	 * 
	 * 배포 조회 공통 목록 전체 카운트 조회
	 * 
	 * @param cmm1600vo
	 * @return
	 * @throws Exception
	 */
	int selectCmm1600CommonDplListCnt(Cmm1600VO cmm1600vo) throws Exception;

	
	
	
	
}
