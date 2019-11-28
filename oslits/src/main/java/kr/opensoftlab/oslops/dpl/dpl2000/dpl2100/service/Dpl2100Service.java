package kr.opensoftlab.oslops.dpl.dpl2000.dpl2100.service;

import java.util.List;
import java.util.Map;

import kr.opensoftlab.oslops.dpl.dpl2000.dpl2100.vo.Dpl2100VO;

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

public interface Dpl2100Service {
	/**
	 * Dpl2100 배포계획 결재 목록 가져오기 
	 * @param param - Map
	 * @return list 배포자 리스트
	 * @throws Exception
	 */
	@SuppressWarnings({"rawtypes" })
	List selectDpl2100SignList(Dpl2100VO dpl2100vo)  throws Exception;
	
	/**
	 * Dpl2100 배포계획 결재 목록 총건수
	 * @param Dpl2100VO - dpl1000VO
	 * @return 
	 * @exception Exception
	 */
	int selectDpl2100SignListCnt(Dpl2100VO dpl2100vo) throws Exception;
	
	/**
	 * Dpl2100 배포계획 결재 디테일 정보
	 * @param param - Map
	 * @return list 배포 계획 리스트
	 * @throws Exception
	 */
	@SuppressWarnings({"rawtypes" })
	Map selectDpl2100SignInfoAjax(Map map)  throws Exception;

	/**
	 * Dpl2100 배포계획 결재자 등록
	 * @param param - Map
	 * @return 
	 * @exception Exception
	 */
	@SuppressWarnings("rawtypes")
	void insertDpl2100DplSignWaitInfo(Map paramMap) throws Exception;
	
	/**
	 * Dpl2100 배포계획 결재자 등록
	 * @param param - Map
	 * @return 
	 * @exception Exception
	 */
	@SuppressWarnings("rawtypes")
	void insertDpl2100DplSignInfo(Map paramMap) throws Exception;
}
