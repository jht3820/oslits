package kr.opensoftlab.oslops.prs.prs2000.prs2000.service.impl;

import java.util.List;
import java.util.Map;

import kr.opensoftlab.oslops.com.dao.ComOslitsAbstractDAO;
import kr.opensoftlab.oslops.dpl.dpl1000.dpl1000.vo.Dpl1000VO;
import kr.opensoftlab.oslops.prs.prs2000.prs2000.vo.Prs2000VO;
import kr.opensoftlab.sdf.excel.ExcelDataListResultHandler;

import org.springframework.stereotype.Repository;

/**
 * @Class Name : Prs2000DAO.java
 * @Description : Prs2000DAO DAO Class
 * @Modification Information
 *
 * @author 강재민
 * @since 2016.01.31
 * @version 1.0
 * @see
 *  
 *  Copyright (C)  All right reserved.
 */

@Repository("prs2000DAO")
public class Prs2000DAO extends ComOslitsAbstractDAO {
	/**
	 * Dpl2000  배포버전별 요구사항 확인 SELECT BOX 조회
	 * @param param - Map
	 * @return 
	 * @exception Exception
	 */
	@SuppressWarnings("rawtypes")
	public List selectDpl2000View(Map paramMap) throws Exception {
		 return (List) list("dpl2000DAO.selectDpl2000View", paramMap);
    }
	
	
	
	/**
	 * 배정 프로젝트 확인 목록을 조회한다.
	 * @param Dpl2000VO
	 * @return List 요구사항 목록
	 * @throws Exception
	 */
	@SuppressWarnings("unchecked")
	public List<Prs2000VO> selectPrs2000List(Prs2000VO prs2000VO) throws Exception {
		 return (List<Prs2000VO>) list("prs2000DAO.selectPrs2000List", prs2000VO);
	}
	
	
	
	/**
	 * 배정 프로젝트 확인 목록 총건수를 조회한다.
	 * @param Dpl2000VO
	 * @return int 배정 프로젝트 확인 목록 총건수 
	 * @throws Exception
	 */
	public int  selectPrs2000ListCnt(Prs2000VO prs2000VO) throws Exception {
		 return (Integer) select("prs2000DAO.selectPrs2000ListCnt", prs2000VO);
	}
	
	

	/**
	 * 배정 프로젝트 확인 목록 을 엑셀 다운로드용으로 조회한다.
	 * @param req2000VO
	 * @return List 배정 프로젝트 확인 목록 
	 * @throws Exception
	 */
	public void  selectPrs2000ExcelList(Prs2000VO prs2000vo, ExcelDataListResultHandler resultHandler) throws Exception {
		listExcelDownSql("prs2000DAO.selectPrs2000ExcelList", prs2000vo, resultHandler);
	}
}
