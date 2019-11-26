package kr.opensoftlab.oslits.stm.stm1000.stm1000.service;

import java.util.List;
import java.util.Map;

import kr.opensoftlab.oslits.stm.stm1000.stm1000.vo.Stm1000VO;



/**
 * @Class Name : Stm1000Service.java
 * @Description : Stm1000Service Business class
 * @Modification Information
 *
 * @author 공대영
 *  * @since 2018.07.23.
 * @version 1.0
 * @see
 *  
 *  Copyright (C)  All right reserved.
 */
public interface Stm1000Service {
	
	/**
	 * Cmm9000 왼쪽 메뉴 영역에 표시할 각종 정보 건수를 조회(알림, 담당요구사항, 전체요구사항, 개발주기별 요구사항)
	 * @param param - LoginVO
	 * @return Map
	 * @exception Exception
	 */
	@SuppressWarnings("rawtypes")
	List<Stm1000VO> selectStm1000List(Stm1000VO stm1000vo) throws Exception;

	int selectStm1000ListCnt(Stm1000VO stm1000vo) throws Exception;

	Object saveStm1000Info(Map<String, String> paramMap) throws Exception;

	Map selectStm1000Info(Map<String, String> paramMap) throws Exception;

	void deleteStm1000Info(Map<String, String> paramMap);

	int selectStm1000UseCountInfo(Map<String, String> paramMap);

	List<Map<String, String>> selectStm1000ApiUrlList(
			Map<String, String> paramMap);


}
