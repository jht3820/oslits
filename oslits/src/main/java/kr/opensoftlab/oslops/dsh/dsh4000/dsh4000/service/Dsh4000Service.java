package kr.opensoftlab.oslops.dsh.dsh4000.dsh4000.service;

import java.util.List;
import java.util.Map;

import kr.opensoftlab.oslops.adm.adm8000.adm8100.vo.Adm8100VO;
import kr.opensoftlab.oslops.dsh.dsh4000.dsh4000.vo.Dsh4000VO;

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
public interface Dsh4000Service {

	
	public List<Dsh4000VO> selectDsh4000ReportList(Dsh4000VO dsh4000vo);


	public int selectDsh4000ReportListCnt(Dsh4000VO dsh4000vo);
		
}
