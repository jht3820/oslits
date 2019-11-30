package kr.opensoftlab.oslops.bad.bad1000.bad1000.service;

import java.util.List;
import java.util.Map;

import kr.opensoftlab.oslops.bad.bad1000.bad1000.vo.Bad1000VO;

/**
 * @Class Name : Bad1000Service.java
 * @Description : Bad1000Service Business class
 * @Modification Information
 *
 * @author 전예지
 * @since 2019.08.29
 * @version 1.0
 * @see
 *  
 *  Copyright (C)  All right reserved.
 */
public interface Bad1000Service {

	/**
	 * Bad1000 공지사항 목록을 조회한다.
	 * @param Bad1000VO
	 * @return List - 공지사항 목록
	 * @exception Exception
	 */
	List<Bad1000VO> selectbad1000BoardList(Bad1000VO bad1000vo) throws Exception;

	/**
	 * Bad1000 공지사항 목록 총 수를 조회한다.
	 * @param Bad1000VO
	 * @return List - 공지사항 목록 수
	 * @exception Exception
	 */
	int selectbad1000BoardListCnt(Bad1000VO bad1000vo) throws Exception;

	/**
	 * Bad1000 공지사항 정보 단건을 조회한다.
	 * @param Map
	 * @return Map
	 * @exception Exception
	 */
	@SuppressWarnings("rawtypes")
	Map selectBad1001Info(Map<String, String> paramMap) throws Exception;

	/**
	 * Bad1001 공지사항 글을 등록/수정 한다.
	 * @param Map
	 * @return Map
	 * @exception Exception
	 */
	int saveBad1001Info(Map<String, String> paramMap) throws Exception;

	/**
	 * bad1000 공지사항 글을 삭제한다.
	 * @param request
	 * @param response
	 * @param model
	 * @return
	 * @throws Exception
	 */
	int deleteBad1000Info(Map<String, Object> paramMap) throws Exception;
	
}
