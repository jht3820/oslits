package kr.opensoftlab.oslops.req.req1000.req1000.service;

/**
 * @Class Name : Req1000Service.java
 * @Description : Req1000Service Service class
 * @Modification Information
 *
 * @author 정형택
 * @since 2016.01.23.
 * @version 1.0
 * @see
 *  
 *  Copyright (C) OpenSoftLab Corp All right reserved.
 */

import java.util.List;
import java.util.Map;

import kr.opensoftlab.oslops.req.req1000.req1000.vo.Req1000VO;
import kr.opensoftlab.sdf.excel.ExcelDataListResultHandler;

public interface Req1000Service {
	
	/**
	 * 요구사항 목록을 조회한다.
	 * @param req1000VO
	 * @return List 로그인 이력 로그 목록
	 * @throws Exception
	 */
	List<Req1000VO>  selectReq1000AllList(Req1000VO req1000VO) throws Exception;
	
	/**
	 * 요구사항 목록을 조회한다.
	 * @param req1000VO
	 * @return List 요구사항 목록
	 * @throws Exception
	 */
	List<Req1000VO> selectReq1000List(Req1000VO req1000VO) throws Exception;
	
	/**
	 * 요구사항 정보을 조회한다.
	 * @param req1000VO
	 * @return List 요구사항 목록
	 * @throws Exception
	 */
	@SuppressWarnings("rawtypes")
	Map selectReq1000ReqInfo(Map paramMap) throws Exception;
	
	/**
	 * 요구사항 목록 총건수를 조회한다.
	 * @param req1000VO
	 * @return  int 요구사항 목록 총건수 
	 * @throws Exception
	 */
	int selectReq1000ListCnt(Req1000VO req1000VO) throws Exception;	
	
	/**
	 * Req1000 요구사항 개발공수, 담당자 수정
	 * @param 
	 * @return 
	 * @exception Exception
	 */
	@SuppressWarnings({ "rawtypes" })
	void updateReq1001ReqSubInfo(Map paramMap) throws Exception;
	
	/**
	 * Req1000 요구사항 저장(등록, 수정, 삭제) AJAX
	 * 요구사항 등록후 등록 정보 결과 및 정보를 리턴한다.
	 * @param 
	 * @return 
	 * @exception Exception
	 */
	@SuppressWarnings({ "rawtypes" })
	public Object saveReq1000ReqInfo(Map paramMap) throws Exception;

	void selectReq1000ExcelList(Req1000VO req1000vo,
			ExcelDataListResultHandler resultHandler) throws Exception;
	
	/**
	 * Req1000 요구사항 삭제(여러건) AJAX
	 * 요구사항 등록후 등록 정보 결과 및 정보를 리턴한다.
	 * @param 
	 * @return 
	 * @exception Exception
	 */
	public void deleteReq1000ReqInfo(Map<String, Object> paramMap) throws Exception;
	
	/**
	 * Req1000 요구사항 요청자 정보 조회 - 소속명, 이메일, 연락처
	 * @param  param - Map
	 * @return
	 * @throws Exception
	 */
	@SuppressWarnings("rawtypes")
	public Map selectReq1000ReqUserInfo(Map paramMap) throws Exception;
	
	/**
	 * Req1000 현재 요구사항이 속한 프로젝트명, 프로젝트 약어 조회
	 * @param  param - Map
	 * @return 체계명
	 * @throws Exception
	 */
	@SuppressWarnings("rawtypes")
	public Map selectReq1000ReqPrjInfo(Map paramMap) throws Exception;
	
	/**
	 * 톱합대시보드의 요구사항 접수대기 목록을 조회한다.
	 * @param paramMap
	 * @return List 접수대기 요구사항 목록
	 * @throws Exception
	 */
	@SuppressWarnings("rawtypes")
	public List<Map> selectReq1000IntegratedDshAcceptReqList(Map paramMap) throws Exception;
	
	/**
	 * 통합대시보드의 요구사항 접수대기 목록 총 건수를 조회한다.
	 * @param paramMap
	 * @return int 접수대기 요구사항 목록 총 건수
	 * @throws Exception
	 */
	@SuppressWarnings("rawtypes")
	public int selectReq1000IntegratedDshAcceptReqListCnt(Map paramMap) throws Exception;
}
