package kr.opensoftlab.oslops.chk.chk1000.chk1000.service;

import java.util.List;
import java.util.Map;

import kr.opensoftlab.oslops.chk.chk1000.chk1000.vo.Chk1000VO;
import kr.opensoftlab.sdf.excel.ExcelDataListResultHandler;

/**
 * @Class Name : Chk1000Service.java
 * @Description : Chk1000Service Business class
 * @Modification Information
 *
 * @author 진승환
 * @since 2016.01.30
 * @version 1.0
 * @see
 *  
 *  Copyright (C)  All right reserved.
 */
public interface Chk1000Service {
	
	/**
	 * 요구사항 중 개발완료인 것만 조회한다.
	 * @param 	chk1000VO
	 * @return 	List 요구사항 개발완료 목록
	 * @throws 	Exception
	 */
	@SuppressWarnings("rawtypes")
	List selectChk1000List(Chk1000VO chk1000VO) throws Exception;
	
	/**
	 * 요구사항 중 개발완료 목록 총건수를 조회한다.
	 * @param 	chk1000VO
	 * @return  int 요구사항 개발완료 목록 총건수 
	 * @throws 	Exception
	 */
	int selectChk1000ListCnt(Chk1000VO chk1000VO) throws Exception;
	
	/**
	 * 선택된 요구사항의 세부정보를 가져온다.
	 * @param 	inputMap
	 * @return  Map 요구사항의 세부정보
	 * @throws 	Exception
	 */
	@SuppressWarnings("rawtypes")
	Map selectChk1000ChkInfoAjax(Map inputMap) throws Exception;

	void selectChk1000ExcelList(Chk1000VO chk1000vo,
			ExcelDataListResultHandler resultHandler) throws Exception;
}
