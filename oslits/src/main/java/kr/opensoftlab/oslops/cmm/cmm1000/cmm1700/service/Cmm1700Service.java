package kr.opensoftlab.oslops.cmm.cmm1000.cmm1700.service;

import java.util.List;
import java.util.Map;

import kr.opensoftlab.oslops.adm.adm2000.adm2000.vo.Adm2000VO;
import kr.opensoftlab.oslops.cmm.cmm1000.cmm1000.vo.Cmm1000VO;
import kr.opensoftlab.oslops.cmm.cmm1000.cmm1600.vo.Cmm1600VO;
import kr.opensoftlab.oslops.cmm.cmm1000.cmm1700.vo.Cmm1700VO;

/**
 * @Class Name : Cmm1700Service.java
 * @Description : Cmm1700Service Business class
 * @Modification Information
 *
 * @author 공대영
 *  * @since 2018.10.16.
 * @version 1.0
 * @see
 *  
 *  Copyright (C)  All right reserved.
 */
public interface Cmm1700Service {
	
	int selectCmm1700CommonAuthListCnt(Cmm1700VO cmm1700vo) throws Exception;

	List selectCmm1700CommonAuthList(Cmm1700VO cmm1700vo) throws Exception;

	
	
	
	
}
