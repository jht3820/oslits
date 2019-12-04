package kr.opensoftlab.oslops.req.req2000.req2000.service.impl;

import java.util.List;
import java.util.Map;

import kr.opensoftlab.oslops.com.dao.ComOslitsAbstractDAO;
import kr.opensoftlab.oslops.req.req4000.req4100.vo.Req4100VO;
import kr.opensoftlab.sdf.excel.ExcelDataListResultHandler;

import org.springframework.stereotype.Repository;

@Repository("req2000DAO")
public class Req2000DAO  extends ComOslitsAbstractDAO {

	
	/**
	 * 요구사항 코멘트 목록을 조회한다.
	 * @param Map
	 * @return List 요구사항 코멘트 목록
	 * @throws Exception
	 */
	@SuppressWarnings("rawtypes")
	List selectReq2000ReqCommentListAjax(Map paramMap) throws Exception{
		return (List) list("req2000DAO.selectReq2000ReqCommentListAjax", paramMap);
	}
	
	/**
	 * 요구사항 코멘트 등록.
	 * @param Map
	 * @return  void 
	 * @throws Exception
	 */
	@SuppressWarnings("rawtypes")
	public String insertReq2000ReqCommentInfo(Map paramMap) throws Exception{
		return (String) insert("req2000DAO.insertReq2000ReqCommentInfo", paramMap);
	}

	/**
	 * Req2000 요구사항 목록 엑셀 다운로드
	 * @param param - Map
	 * @return 
	 * @exception Exception
	 */
	public void selectReq2000ExcelList(Map paramMap, ExcelDataListResultHandler resultHandler) throws Exception {
		listExcelDownSql("req4100DAO.selectReq4100ExcelList", paramMap, resultHandler);
	}
}