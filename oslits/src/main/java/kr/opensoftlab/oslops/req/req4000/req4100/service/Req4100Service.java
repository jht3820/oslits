package kr.opensoftlab.oslops.req.req4000.req4100.service;

import java.util.List;
import java.util.Map;

import kr.opensoftlab.oslops.req.req4000.req4100.vo.Req4100VO;
import kr.opensoftlab.sdf.excel.ExcelDataListResultHandler;
import kr.opensoftlab.sdf.util.ReqHistoryMngUtil;

/**
 * @Class Name : Req4000Service.java
 * @Description : Req4000Service Business class
 * @Modification Information
 *
 * @author 공대영
 * @since 2016.01.30.
 * @version 1.0
 * @see
 *  
 *  Copyright (C) OpenSoftLab Corp. All right reserved.
 */
public interface Req4100Service {


	/**
	 * Req4100 요구사항 일괄 저장(엑셀 업로드)
	 * @param prjId 
	 * @param param - Map
	 * @return 
	 * @exception Exception
	 */
	@SuppressWarnings("rawtypes")
	void insertReq4100ReqInfoListAjax(Map<String, String> paramMap, Map prjInfo, ReqHistoryMngUtil historyMng) throws Exception;

	List<Req4100VO> selectReq4100List(Req4100VO req4100vo) throws Exception;

	int selectReq4100ListCnt(Req4100VO req4100vo) throws Exception;

	String insertReq4100ReqInfoAjax(Map<String, String> paramMap) throws Exception;

	@SuppressWarnings("rawtypes")
	Map selectReq4102ReqInfoAjax(Map<String, String> paramMap) throws Exception;

	@SuppressWarnings("rawtypes")
	void updateReq4100ReqInfoAjax(Map paramMap) throws Exception;

	void deleteReq4100ReqInfoAjax(Map<String, Object> paramMap,String prjId) throws Exception;
	
	/**
	 * Req4106 요구사항 접수 완료
	 * @param param - Map
	 * @return list - List
	 * @exception Exception
	 */
	@SuppressWarnings("rawtypes")
	void updateReq4106NewReqAccpetInfoAjax(Map paramMap) throws Exception;
	
	/**
	 * Req4700 요구사항 변경 히스토리 정보 가져오기
	 * @param param - Map
	 * @return list - List
	 * @exception Exception
	 */
	@SuppressWarnings("rawtypes")
	List selectReq4700ReqHistoryList(Map paramMap) throws Exception;
	
	/**
	 * Req4900 요구사항 결재 정보 가져오기
	 * @param param - Map
	 * @return list - List
	 * @exception Exception
	 */
	@SuppressWarnings("rawtypes")
	List selectReq4900ReqSignList(Map paramMap) throws Exception;
	
	void selectReq4100ExcelList(Req4100VO req4100vo,
			ExcelDataListResultHandler resultHandler) throws Exception;
	

	/**
	 * REQ4100 요구사항 전체 목록 조회
	 * @param param - Map
	 * @return list - List
	 * @exception Exception
	 */
	@SuppressWarnings("rawtypes")
	List selectReq4100AllList(Req4100VO req4100vo) throws Exception;
	
	/**
	 * REQ4100 프로세스, 작업흐름 별 요구사항 수 조회 
	 * @param param - Map
	 * @return Integer
	 * @exception Exception
	 */
	@SuppressWarnings({ "rawtypes"})
	int selectReq4100ProcessFlowReqCnt(Map paramMap) throws Exception;
	
	/**
	 * REQ4100 프로세스, 작업흐름 별 요구사항 조회 
	 * @param param - Map
	 * @return list - List
	 * @exception Exception
	 */
	@SuppressWarnings("rawtypes")
	List selectReq4100ProcessFlowReqList(Map paramMap) throws Exception;
	
	/**
	 * REQ4100 요구사항 작업흐름 변경
	 * @param param - Map
	 * @exception Exception
	 * @desc
	 * - reqStDTm, reqEdDtm, reqStDuDtm, reqEdDuDtm, flowId, reqProType은 REQ4100 기본 요구사항 컬럼 수정
	 * - 그 외에 항목은 모두 추가 항목 추가 또는 수정
	 * - 수정 이력 발생은 통합하고 수정은 각각 수정 쿼리 실행
	 * - 작업은 실시간 CUD
	 * 2018-09-11 : 수정이력 발생 없이 우선 저장 부터
	 */
	@SuppressWarnings("rawtypes")
	boolean saveReq4100ReqFlowChgAjax(Map paramMap) throws Exception;
	@SuppressWarnings("rawtypes")
	Map selectReq4100ReqInfo(Map<String	, String> paramMap);
	
	
	/**
	 * Req4100 요구사항 엑셀 업로드 시 요청자(ADM2000) ID 검색 
	 * @param param - Map
	 * @return 
	 * @exception Exception
	 */
	@SuppressWarnings("rawtypes")
	public int selectReq4100ReqUsrChk(Map paramMap) throws Exception;


	/**
	 * REQ4100 요구사항에 포함된 리비전 목록 조회(Grid page)
	 * @param Whk1000VO
	 * @return 
	 * @exception Exception
	 */
	@SuppressWarnings("rawtypes")
	List<Map> selectReq4100RevisionList(Req4100VO req4100VO) throws Exception;
	
	/**
	 * REQ4100요구사항에 포함된 리비전 목록 총 건수 조회(Grid page)
	 * @param Whk1000VO
	 * @return 
	 * @exception Exception
	 */
	int selectReq4100RevisionListCnt(Req4100VO req4100VO) throws Exception;
	
	/**
	 * REQ4100 권한있는 프로젝트의 요구사항 검색
	 * @param param - Map
	 * @return list - List
	 * @exception Exception
	 */
	@SuppressWarnings({ "rawtypes"})
	List<Map> selectReq4100PrjAuthReqList(Map paramMap) throws Exception;

	/**
	 * Req4105 요구사항 담당자 변경
	 * @param param - Map
	 * @exception Exception
	 */
	@SuppressWarnings("rawtypes")
	void updateReq4100ReqChargerChgInfoAjax(Map paramMap) throws Exception;
	
	/**
	 * Req4100 요구사항 목록 엑셀 다운로드
	 * @param param - Map
	 * @param resultHandler ExcelDataListResultHandler
	 * @return 
	 * @exception Exception
	 */
	@SuppressWarnings("rawtypes")
	void selectReq4100ExcelList(Map paramMap, ExcelDataListResultHandler resultHandler) throws Exception;
	
	/**
	 * REQ4100 프로세스별 요구사항 조회 
	 * @param param - Map
	 * @return list - List
	 * @exception Exception
	 */
	@SuppressWarnings({ "rawtypes" })
	List<Map> selectReq4100ProcessReqList(Map paramMap) throws Exception;

	/**
	 * REQ4100 외부 팝업 요구사항 조회 - 요구사항 상세정보를 조회한다.
	 * @param param - Map
	 * @return 
	 * @exception Exception
	 */
	@SuppressWarnings("rawtypes")
	Map selectReq4110ReqInfo(Map paramMap) throws Exception;
}
