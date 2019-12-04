package kr.opensoftlab.oslops.dpl.dpl2000.dpl2000.service;

import java.util.List;
import java.util.Map;

import kr.opensoftlab.oslops.dpl.dpl2000.dpl2000.vo.Dpl2000VO;

/**
 * @Class Name : Dpl1000Service.java
 * @Description : Dpl1000Service Business class
 * @Modification Information
 *
 * @author 진주영
 * @since 2019.03.11
 * @version 1.0
 * @see
 *  
 *  --------------------------------------
 *  수정일			수정자			수정내용
 *  --------------------------------------
 *  
 *  
 *  --------------------------------------
 *  
 *  Copyright (C)  All right reserved.
 */

public interface Dpl2000Service {
	/**
	 * Dpl2000 배포계획 결재 목록 가져오기 
	 * @param param - Map
	 * @return list 배포자 리스트
	 * @throws Exception
	 */
	@SuppressWarnings({"rawtypes" })
	List selectDpl2000SignList(Dpl2000VO dpl2000vo)  throws Exception;
	
	/**
	 * Dpl2000 배포계획 결재 목록 총건수
	 * @param Dpl2100VO - dpl1000VO
	 * @return 
	 * @exception Exception
	 */
	int selectDpl2000SignListCnt(Dpl2000VO dpl2000vo) throws Exception;
	
	/**
	 * Dpl2000 배포계획 결재 디테일 정보
	 * @param param - Map
	 * @return list 배포 계획 리스트
	 * @throws Exception
	 */
	@SuppressWarnings({"rawtypes" })
	Map selectDpl2000SignInfoAjax(Map map)  throws Exception;

}
