package kr.opensoftlab.oslits.req.req4000.req4100.service.impl;

import java.util.List;
import java.util.Map;

import kr.opensoftlab.oslits.com.dao.ComOslitsAbstractDAO;
import kr.opensoftlab.oslits.req.req4000.req4100.vo.Req4100VO;
import kr.opensoftlab.sdf.excel.ExcelDataListResultHandler;

import org.springframework.stereotype.Repository;

/**
 * @Class Name : Req4100DAO.java
 * @Description : Req4100DAO DAO Class
 * @Modification Information
 *
 * @author 공대영
 * @since 2016.02.09.
 * @version 1.0
 * @see
 *  
 *  Copyright (C) OpenSoftLab Corp. All right reserved.
 */

@Repository("req4100DAO")
public class Req4100DAO extends ComOslitsAbstractDAO {

	/**
	 * Req4100 요구사항 정보입력
	 * @param param - Map
	 * @return 
	 * @exception Exception
	 */	
	public String insertReq4100ReqInfoAjax(Map<String, String> reqMap) throws Exception{
		return (String)insert("req4100DAO.insertReq4100ReqInfoAjax", reqMap);
	}
	
	/**
	 * Req4100 요구사항 업로드 추가시 사용자(ADM2000) ID 검색 
	 * @param param - Map
	 * @return 
	 * @exception Exception
	 */
	public int selectReq4100ReqUsrChk(Map paramMap) throws Exception{
		return (int)select("req4100DAO.selectReq4100ReqUsrChk", paramMap);
	}

	@SuppressWarnings("unchecked")
	public List<Req4100VO> selectReq4100List(Req4100VO req4100vo) throws Exception{
		return (List<Req4100VO>)list("req4100DAO.selectReq4100List", req4100vo);
	}

	public int selectReq4100ListCnt(Req4100VO req4100vo) throws Exception {
		return (Integer) select("req4100DAO.selectReq4100ListCnt", req4100vo);
	}

	@SuppressWarnings("rawtypes")
	public Map selectReq4102ReqInfoAjax(Map<String, String> paramMap) throws Exception {
		return (Map) select("req4100DAO.selectReq4102ReqInfoAjax", paramMap);
	}

	public void updateReq4100ReqInfoAjax(Map<String, String> paramMap) throws Exception {
		update("req4100DAO.updateReq4100ReqInfoAjax", paramMap);
		
	}

	public void deleteReq4100ReqInfoAjax(Map<String, String> reqMap) throws Exception {
		delete("req4100DAO.deleteReq4100ReqInfoAjax", reqMap);
		
	}
	
	/**
	 * Req4106 요구사항 접수 완료
	 * @param param - Map
	 * @exception Exception
	 */
	@SuppressWarnings("rawtypes")
	public void updateReq4106NewReqAccpetInfoAjax(Map paramMap) throws Exception{
		update("req4100DAO.updateReq4106NewReqAccpetInfoAjax",paramMap);
	}
	
	/**
	 * Req4105 요구사항 작업흐름 변경 (요구사항 수정)
	 * @param param - Map
	 * @exception Exception
	 */
	@SuppressWarnings("rawtypes")
	public void updateReq4100ReqFlowChgInfoAjax(Map paramMap) throws Exception{
		update("req4100DAO.updateReq4100ReqFlowChgInfoAjax",paramMap);
	}
	
	/**
	 * Req4700 요구사항 변경 히스토리 정보 가져오기
	 * @param param - Map
	 * @return list - List
	 * @exception Exception
	 */
	@SuppressWarnings("rawtypes")
	public List selectReq4700ReqHistoryList(Map paramMap) throws Exception{
		return (List) list("req4100DAO.selectReq4700ReqHistoryList",paramMap);
	}
	
	/**
	 * Req4900 요구사항 결재 정보 가져오기
	 * @param param - Map
	 * @return list - List
	 * @exception Exception
	 */
	@SuppressWarnings("rawtypes")
	public List selectReq4900ReqSignList(Map paramMap) throws Exception{
		return (List) list("req4100DAO.selectReq4900ReqSignList",paramMap);
	}
	
	public void selectReq4100ExcelList(Req4100VO req4100vo,
			ExcelDataListResultHandler resultHandler) throws Exception {
		listExcelDownSql("req4100DAO.selectReq4100AllList", req4100vo,resultHandler);
	}
	
	/**
	 * REQ4100 요구사항 전체 목록 조회
	 * @param param - Map
	 * @return list - List
	 * @exception Exception
	 */
	@SuppressWarnings("rawtypes")
	public List selectReq4100AllList(Req4100VO req4100vo) throws Exception {
		return (List) list("req4100DAO.selectReq4100AllList",req4100vo);
	}
	
	/**
	 * REQ4100 프로세스, 작업흐름 별 요구사항 조회 
	 * @param param - Map
	 * @return list - List
	 * @exception Exception
	 */
	@SuppressWarnings({ "rawtypes", "unchecked" })
	public List<Map> selectReq4100ProcessFlowReqList(Map paramMap) throws Exception {
		return (List<Map>) list("req4100DAO.selectReq4100ProcessFlowReqList",paramMap);
	}
	
	/**
	 * REQ4100 요구사항 요청자 정보 조회 - 소속명, 이메일, 연락처
	 * @param  param - Map
	 * @return
	 * @throws Exception
	 */
	@SuppressWarnings("rawtypes")
	public Map selectReq4100ReqUserInfo(Map paramMap) throws Exception {
		return	(Map) select("req4100DAO.selectReq4100ReqUserInfo", paramMap);
	}

	public Map selectReq4100ReqInfo(Map<String, String> paramMap) {
		return (Map) select("req4100DAO.selectReq4100ReqInfo", paramMap);
	}
	
	public String insertReq4100ReqInfo(Map<String, String> paramMap) throws Exception {
		return (String)insert("req4100DAO.insertReq4100ReqInfo", paramMap);	
	}

	public int updateReq4100ReqInfo(Map paramMap) throws Exception{
		return update("req4100DAO.updateReq4100ReqInfo",paramMap);
	}
	public List<Map> selectReq4100RevisionList(Map paramMap) throws Exception{
		return (List<Map>) list("req4100DAO.selectReq4100RevisionList",paramMap);
	}

	/**
	 * REQ4100 요구사항 다음 순번정보 조회
	 * @param param - Map
	 * @return 
	 * @exception Exception
	 */
	@SuppressWarnings("rawtypes")
	public String selectReq4100NextReqOrd(Map paramMap) throws Exception{
		return (String) select("req4100DAO.selectReq4100NextReqOrd", paramMap);
	}	
	
	/**
	 * REQ4100 요구사항 첨부파일 정보 삭제
	 * - 요구사항 삭제 시 해당 요구사항의 첨부파일 정보도 삭제한다.
	 * @param param - Map
	 * @return 
	 * @exception Exception
	 */
	@SuppressWarnings("rawtypes")
	public int deleteReq4100ReqAtchFile(Map paramMap) throws Exception{
		return (int) delete("req4100DAO.deleteReq4100ReqAtchFile", paramMap);
	}
	
	/**
	 * REQ4100 요구사항 첨부파일 상세정보 삭제
	 * - 요구사항 삭제 시 해당 요구사항의 첨부파일 상세정보도 삭제한다.
	 * @param param - Map
	 * @return 
	 * @exception Exception
	 */
	@SuppressWarnings("rawtypes")
	public int deleteReq4100ReqAtchFileDetail(Map paramMap) throws Exception{
		return (int) delete("req4100DAO.deleteReq4100ReqAtchFileDetail", paramMap);
	}

	/**
	 * REQ4100 권한있는 프로젝트의 요구사항 검색
	 * @param param - Map
	 * @return list - List
	 * @exception Exception
	 */
	@SuppressWarnings({ "rawtypes", "unchecked" })
	public List<Map> selectReq4100PrjAuthReqList(Map paramMap) throws Exception {
		return (List<Map>) list("req4100DAO.selectReq4100PrjAuthReqList",paramMap);
	}
	
	/**
	 * Req4105 요구사항 담당자 변경
	 * @param param - Map
	 * @exception Exception
	 */
	@SuppressWarnings("rawtypes")
	public void updateReq4100ReqChargerChgInfoAjax(Map paramMap) throws Exception{
		update("req4100DAO.updateReq4100ReqChargerChgInfoAjax",paramMap);
	}
	
	/**
	 * Req4100 요구사항 목록 엑셀 다운로드
	 * @param param - Map
	 * @return 
	 * @exception Exception
	 */
	@SuppressWarnings("rawtypes")
	public void selectReq4100ExcelList(Map paramMap, ExcelDataListResultHandler resultHandler) throws Exception {
		listExcelDownSql("req4100DAO.selectReq4100ExcelList", paramMap, resultHandler);
	}
}