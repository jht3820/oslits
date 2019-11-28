package kr.opensoftlab.oslops.chk.chk1000.chk1100.service;

import java.util.List;
import java.util.Map;

import kr.opensoftlab.oslops.chk.chk1000.chk1100.vo.Chk1100VO;
import kr.opensoftlab.sdf.excel.ExcelDataListResultHandler;

/**
 * @Class Name : Chk1100Service.java
 * @Description : Chk1100Service Business class
 * @Modification Information
 *
 * @author 진승환
 * @since 2016.01.30
 * @version 1.0
 * @see
 *  
 *  Copyright (C)  All right reserved.
 */
public interface Chk1100Service {

	/**
	 * 요구사항 결재목록을 조회한다.
	 * @param 	chk1100VO
	 * @return	List  요구사항 결재목록을 조회한다.
	 * @throws 	Exception
	 */
	@SuppressWarnings("rawtypes")
	List selectChk1100List(Chk1100VO chk1100VO) throws Exception;
	
	/**
	 * 요구사항 결재 목록 총건수를 조회한다.
	 * @param 	chk1100VO
	 * @return  int 요구사항 결재 목록 총건수를 조회한다.
	 * @throws 	Exception
	 */
	int selectChk1100ListCnt(Chk1100VO chk1100VO) throws Exception;
	
	/**
	 * 선택된 요구사항의 승인/반려시 필요한 작업흐름을 조회한다.
	 * @param 	inputMap
	 * @return  Map 작업흐름 정보
	 * @throws 	Exception
	 */
	@SuppressWarnings("rawtypes")
	public Map selectFlw1100FlowList(Map inputMap) throws Exception;
	
	
	/**
	 * 선택된 요구사항의 세부정보를 가져온다.
	 * @param 	inputMap
	 * @return  Map 요구사항의 세부정보
	 * @throws 	Exception
	 */
	@SuppressWarnings("rawtypes")
	Map selectChk1100ChkInfoAjax(Map inputMap) throws Exception;
	

	void selectChk1100ExcelList(Chk1100VO chk1100vo,
			ExcelDataListResultHandler resultHandler) throws Exception;
	
	/**
	 * 요구사항 검수거부 목록 조회
	 * @param 	Map paramMap
	 * @throws 	Exception
	 */
	@SuppressWarnings("rawtypes")
	List  selectChk1100ReqChkRejectList(Map paramMap) throws Exception;
}
