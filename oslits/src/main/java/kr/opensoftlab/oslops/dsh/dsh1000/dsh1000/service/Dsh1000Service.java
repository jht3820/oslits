package kr.opensoftlab.oslops.dsh.dsh1000.dsh1000.service;

/**
 * @Class Name : Dsh1000Service.java
 * @Description : Dsh1000Service Service class
 * @Modification Information
 *
 * @author 정형택
 * @since 2016.02.10.
 * @version 1.0
 * @see
 *  
 *  Copyright (C) OpenSoftLab Corp All right reserved.
 */

import java.util.List;
import java.util.Map;

import kr.opensoftlab.oslops.dsh.dsh1000.dsh1000.vo.Dsh1000VO;


public interface Dsh1000Service {
	
	/**
	 * [차트1] 프로세스별 총갯수 + 최종 완료 갯수
	 * @param paramMap
	 * @return List
	 * @throws Exception
	 */
	@SuppressWarnings("rawtypes")
	List selectDsh1000ProcessReqCntList(Map paramMap) throws Exception;
	
	/**
	 * [차트2] 월별 프로세스별 요구사항 갯수
	 * @param paramMap
	 * @return List
	 * @throws Exception
	 */
	@SuppressWarnings("rawtypes")
	List selectDsh1000MonthProcessReqCntList(Map paramMap) throws Exception;

	List selectDsh1000ReqList(Dsh1000VO dsh1000vo) throws Exception;

	int selectDsh1000ReqListCnt(Dsh1000VO dsh1000vo) throws Exception;
	
}
