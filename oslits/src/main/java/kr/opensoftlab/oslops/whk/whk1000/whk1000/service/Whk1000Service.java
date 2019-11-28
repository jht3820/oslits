package kr.opensoftlab.oslops.whk.whk1000.whk1000.service;

/**
 * @Class Name : Whk1000Service.java
 * @Description : Whk1000Service Service class
 * @Modification Information
 *
 * @author 진주영
 * @since 2019.05.22.
 * @version 1.0
 * @see
 *  
 *  Copyright (C) OpenSoftLab Corp All right reserved.
 */

import java.util.List;
import java.util.Map;

import kr.opensoftlab.oslops.whk.whk1000.whk1000.vo.Whk1000VO;

public interface Whk1000Service {

	/**
	 * 웹훅 목록 조회
	 * @param pageVO
	 * @return List 웹훅 목록
	 * @throws Exception
	 */
	List<Whk1000VO>  selectWhk1000PrjWhkList(Whk1000VO whk1000vo) throws Exception;
	
	/**
	 * 웹훅 목록 총건수
	 * @param req1000VO
	 * @throws Exception
	 */
	int  selectWhk1000PrjWhkListCnt(Whk1000VO whk1000vo) throws Exception;
	
	/**
	 * 웹훅 전체 목록 조회 (no paging)
	 * @param Map
	 * @return Map 요구사항 정보
	 * @throws Exception
	 */
	@SuppressWarnings({ "rawtypes"})
	List<Map> selectWhk1000PrjWhkAllList(Map paramMap) throws Exception;

	/**
	 * 웹훅 단건 조회
	 * @param Map
	 * @return Map 웹훅 정보
	 * @throws Exception
	 */
	@SuppressWarnings({ "rawtypes" })
	Map selectWhk1000PrjWhkInfo(Map paramMap) throws Exception;
	
	/**
	 * 웹훅 정보 등록
	 * @param param - Map
	 * @return 
	 * @exception Exception
	 */
	@SuppressWarnings("rawtypes")
	String insertWhk1000PrjWhkInfo(Map paramMap) throws Exception;
	
	/**
	 * 웹훅 정보 수정
	 * @param 
	 * @return 
	 * @exception Exception
	 */
	@SuppressWarnings({ "rawtypes" })
	void updateWhk1000PrjWhkInfo(Map paramMap) throws Exception;
	/**
	 * 웹훅 정보 삭제
	 * @param param - Map
	 * @return 
	 * @exception Exception
	 */
	@SuppressWarnings("rawtypes")
	int deleteWhk1000PrjWhkInfo(Map paramMap) throws Exception;
	

	/**
	 * 웹훅 정보 목록 삭제
	 * @param param - Map
	 * @return 
	 * @exception Exception
	 */
	@SuppressWarnings("rawtypes")
	void deleteWhk1000PrjWhkList(Map paramMap) throws Exception;
}
