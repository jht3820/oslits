package kr.opensoftlab.oslops.adm.adm8000.adm8100.service;

import java.util.List;
import java.util.Map;

import kr.opensoftlab.oslops.adm.adm8000.adm8100.vo.Adm8100VO;

/**
 * @Class Name : Adm8000Service.java
 * @Description : 보고서 서비스 인터페이스
 * @Modification Information
 * <pre>
 *   수정일      수정자          수정내용
 *  -------    --------    ---------------------------
 *  2018.10.18  공대영          최초 생성
 *  
 * </pre>
 *  @author 공대영
 *  @version 1.0
 *  @see 
 *  
 *  Copyright (C) Open Soft Lab Corp. All Rights Reserved
 */
public interface Adm8100Service {

	/**
	 * Adm8100 보고서를 생성한다.
	 * @param paramMap
	 * @return
	 * @throws Exception
	 */
	@SuppressWarnings("rawtypes")
	int insertAdm8100ReportInfo(Map paramMap) throws Exception;

	/**
	 * Adm8100 보고서 목록을 조회한다.
	 * @param adm8100vo
	 * @return
	 * @throws Exception
	 */
	List<Adm8100VO> selectAdm8100ReportList(Adm8100VO adm8100vo) throws Exception;

	/**
	 * Adm8100 보고서 목록 총 건수 조회
	 * @param adm8100vo
	 * @return
	 * @throws Exception
	 */
	int selectAdm8100ReportListCnt(Adm8100VO adm8100vo) throws Exception;

	/**
	 * Adm8100 보고서 단건 조회
	 * @param paramMap
	 * @return
	 * @throws Exception
	 */
	@SuppressWarnings("rawtypes")
	List<Map> selectAdm8100ReportInfo(Map paramMap) throws Exception;

	/**
	 * Adm8100 보고서를 수정한다.
	 * @param paramMap
	 * @param adm8100voList
	 * @return
	 * @throws Exception
	 */
	@SuppressWarnings("rawtypes")
	int updateAdm8100ReportInfo(Map paramMap, List<Adm8100VO> adm8100voList) throws Exception;

	/**
	 * Adm8100 보고서를 수정한다
	 * @param adm8100vo
	 * @return
	 * @throws Exception
	 */
	void updateAdm8100ReporConfirm(Adm8100VO adm8100vo) throws Exception;

	/**
	 * Adm8100 보고서를 확정 처리한다.
	 * @param paramMap
	 * @return
	 * @throws Exception
	 */
	@SuppressWarnings("rawtypes")
	Map selectAdm8100ReportMasterInfo(Map paramMap) throws Exception;
	
	
	
}
