package kr.opensoftlab.oslits.adm.adm8000.adm8100.service;

import java.util.List;
import java.util.Map;

import kr.opensoftlab.oslits.adm.adm8000.adm8100.vo.Adm8100VO;

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

	int insertAdm8100ReportInfo(Map paramMap) throws Exception;

	List<Adm8100VO> selectAdm8100ReportList(Adm8100VO adm8100vo);

	int selectAdm8100ReportListCnt(Adm8100VO adm8100vo);

	List<Map> selectAdm8100ReportInfo(Map paramMap);

	int updateAdm8100ReportInfo(Map paramMap, List<Adm8100VO> adm8100voList);

	void updateAdm8100ReporConfirm(Adm8100VO adm8100vo);

	Map selectAdm8100ReportMasterInfo(Map paramMap);
	
	
	
}
