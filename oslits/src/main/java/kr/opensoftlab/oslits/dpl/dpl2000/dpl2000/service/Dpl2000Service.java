package kr.opensoftlab.oslits.dpl.dpl2000.dpl2000.service;

import java.util.List;
import java.util.Map;

import kr.opensoftlab.oslits.dpl.dpl2000.dpl2000.vo.Dpl2000VO;


/**
 * @Class Name : Dpl2000Service.java
 * @Description : Dpl2000Service Business class
 * @Modification Information
 *
 * @author 강재민
 * @since 2016.01.30
 * @version 1.0
 * @see
 *  
 *  Copyright (C)  All right reserved.
 */
public interface Dpl2000Service {
	/**
	 * Dpl2000 배포버전별 요구사항 확인 SELECT BOX 조회
	 * @param param - Map
	 * @return 
	 * @exception Exception
	 */
	@SuppressWarnings("rawtypes")
	List selectDpl2000SelectBox(Map paramMap) throws Exception;
	
	
	/**
	 * Dpl2000 배포 버전 목록 조회
	 * @param param - Map
	 * @return 
	 * @exception Exception
	 */
	@SuppressWarnings("rawtypes")
	List selectDpl2000dplVerList(Map paramMap) throws Exception;
	
	
	
	/**
	 * 배포버전별 요구사항 확인 목록을 조회한다.
	 * @param Dpl2000VO
	 * @return List 요구사항 목록
	 * @throws Exception
	 */
	List<Dpl2000VO> selectDpl2000List(Dpl2000VO dpl2000VO) throws Exception;
	
	
	
	/**
	 * 배포버전별 요구사항 확인 목록 총건수를 조회한다.
	 * @param Dpl2000VO
	 * @return  int  배포버전별 요구사항 확인 목록 총건수 
	 * @throws Exception
	 */
	int selectDpl2000ListCnt(Dpl2000VO dpl2000VO) throws Exception;
	
	
	
	/**
	 * 배포버전별 요구사항 확인 디테일 단건정보 조회
	 * @param Map
	 * @return Map 배포버전별 요구사항 확인 디테일 단건정보
	 * @throws Exception
	 */
	@SuppressWarnings("rawtypes")
	Map selectDpl2000DetailAjax(Map paramMap) throws Exception;
	
	
	
	/**
	 * 배포버전별 요구사항 확 코멘트 목록 조회
	 * @param Map
	 * @return List 배포버전별 요구사항 확인 코멘트 목록
	 * @throws Exception
	 */
	@SuppressWarnings("rawtypes")
	List<Dpl2000VO> selectDpl2000CommentListAjax(Map paramMap) throws Exception;
	
	
	
	/**
	 * 배포버전별 요구사항 확인 코멘트 등록
	 * @param Map
	 * @return void 
	 * @throws Exception
	 */
	@SuppressWarnings("rawtypes")
	void insertDpl2000ReqCommentInfoAjax(Map paramMap) throws Exception;
	
	
	
	/**
	 * 배포버전별 요구사항 확인 코멘트 목록 조회
	 * @param Map
	 * @return List 배포버전별 요구사항 확인 코멘트 목록
	 * @throws Exception
	 */
	@SuppressWarnings("rawtypes")
	List<Map> selectDpl2000ReqCommentListAjax(Map paramMap) throws Exception;


	List<Map> selectDpl2000JobBuildList(Map paramMap) throws Exception;


	List selectDpl2000BuildStatus(Map paramMap) throws Exception;


	int updateDpl2000BuildStatus(Map paramMap) throws Exception;


	void insertDpl1100logHistory(Map paramMap) throws Exception;
}
