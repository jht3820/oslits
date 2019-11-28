package kr.opensoftlab.oslops.prs.prs2000.prs2000.service;

import java.util.List;

import kr.opensoftlab.oslops.prs.prs2000.prs2000.vo.Prs2000VO;
import kr.opensoftlab.sdf.excel.ExcelDataListResultHandler;


/**
 * @Class Name : Prs2000Service.java
 * @Description : Prs2000Service Business class
 * @Modification Information
 *
 * @author 강재민
 * @since 2016.01.31
 * @version 1.0
 * @see
 *  
 *  Copyright (C)  All right reserved.
 */
public interface Prs2000Service {
	/**
	 * 배정 프로젝트 확인 목록을 조회한다.
	 * @param Dpl2000VO
	 * @return List 요구사항 목록
	 * @throws Exception
	 */
	List<Prs2000VO> selectPrs2000List(Prs2000VO prs2000VO) throws Exception;
	
	
	
	/**
	 * 배정 프로젝트 확인 목록 총건수를 조회한다.
	 * @param Dpl2000VO
	 * @return int 배정 프로젝트 확인 목록 총건수 
	 * @throws Exception
	 */
	int selectPrs2000ListCnt(Prs2000VO prs2000VO) throws Exception;
	

	/**
	 * 배정 프로젝트 확인 목록 엑셀 조회를 한다.
	 * @params prs2000VO
	 * @return List 배정 프로젝트 확인
	 * @throws Exception
	 */
	void selectPrs2000ExcelList(Prs2000VO prs2000VO,ExcelDataListResultHandler resultHandler) throws Exception;
}
