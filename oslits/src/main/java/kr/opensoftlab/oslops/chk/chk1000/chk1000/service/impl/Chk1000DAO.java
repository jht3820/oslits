package kr.opensoftlab.oslops.chk.chk1000.chk1000.service.impl;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import kr.opensoftlab.oslops.chk.chk1000.chk1000.vo.Chk1000VO;
import kr.opensoftlab.oslops.com.dao.ComOslitsAbstractDAO;
import kr.opensoftlab.sdf.excel.ExcelDataListResultHandler;

import org.springframework.stereotype.Repository;

/**
 * @Class Name : Chk1000DAO.java
 * @Description : Chk1000DAO DAO Class
 * @Modification Information
 *
 * @author 진승환
 * @since 2016.01.30
 * @version 1.0
 * @see
 *  
 *  Copyright (C)  All right reserved.
 */

@Repository("chk1000DAO")
public class Chk1000DAO  extends ComOslitsAbstractDAO {

	/**
	 * 본인이 결재자인 경우의 요구사항 결재정보를 조회한다.
	 * @param 	chk1000VO
	 * @return	List 요구사항 결재정보
	 * @throws 	Exception
	 */
	@SuppressWarnings("unchecked")
	public List<Chk1000VO>  selectChk1000List(Chk1000VO chk1000VO) throws Exception {
		 return  (List<Chk1000VO>) list("chk1000DAO.selectChk1000List", chk1000VO);
	}
	
	/**
	 * 요구사항 중 개발완료 목록 총건수를 조회한다.
	 * @param chk1000VO
	 * @return  int 요구사항 중 개발완료 목록 총건수
	 * @throws Exception
	 */
	public int  selectChk1000ListCnt(Chk1000VO chk1000VO) throws Exception {
		 return  (int)select("chk1000DAO.selectChk1000ListCnt", chk1000VO);
	} 
	
	/**
	 * 선택된 요구사항의 세부정보를 가져온다.
	 * @param inputMap
	 * @return  Map 요구사항의 세부정보
	 * @throws Exception
	 */
	@SuppressWarnings("rawtypes")
	public Map selectChk1000ChkInfoAjax(Map inputMap) throws Exception{
		return (Map)select("chk1000DAO.selectChk1000ChkInfoAjax", inputMap);
	}

	public void selectChk1000ExcelList(Chk1000VO chk1000vo,
			ExcelDataListResultHandler resultHandler) throws Exception {
		// TODO Auto-generated method stub
		listExcelDownSql("chk1000DAO.selectChk1000ExcelList", chk1000vo , resultHandler );
	}
}
