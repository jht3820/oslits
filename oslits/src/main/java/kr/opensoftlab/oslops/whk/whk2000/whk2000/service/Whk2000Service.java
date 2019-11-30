package kr.opensoftlab.oslops.whk.whk2000.whk2000.service;

/**
 * @Class Name : Whk2000Service.java
 * @Description : Whk2000Service Service class
 * @Modification Information
 *
 * @author 진주영
 * @since 2019.05.24.
 * @version 1.0
 * @see
 *  
 *  Copyright (C) OpenSoftLab Corp All right reserved.
 */

import java.util.List;
import java.util.Map;

import kr.opensoftlab.oslops.whk.whk2000.whk2000.vo.Whk2000VO;


public interface Whk2000Service {

	/**
	 * 사용자 웹훅 목록 조회
	 * @param pageVO
	 * @return List 사용자 웹훅 목록
	 * @throws Exception
	 */
	List<Whk2000VO>  selectWhk2000UsrWhkList(Whk2000VO whk2000vo) throws Exception;
	
	/**
	 * 사용자 웹훅 목록 총건수
	 * @param Whk2000VO
	 * @throws Exception
	 */
	int  selectWhk2000UsrWhkListCnt(Whk2000VO whk2000vo) throws Exception;
	
	/**
	 * 사용자 웹훅 전체 목록 조회 (no paging)
	 * @param Map
	 * @return List 사용자 웹훅 목록
	 * @throws Exception
	 */
	@SuppressWarnings({ "rawtypes"})
	List<Map> selectWhk2000UsrWhkAllList(Map paramMap) throws Exception;
	
	/**
	 * 사용자 웹훅 단건 조회
	 * @param Map
	 * @return Map 사용자 웹훅 정보
	 * @throws Exception
	 */
	@SuppressWarnings({ "rawtypes" })
	Map selectWhk2000UsrWhkInfo(Map paramMap) throws Exception;
	
	/**
	 * 사용자 웹훅 정보 등록
	 * @param param - Map
	 * @return 
	 * @exception Exception
	 */
	@SuppressWarnings("rawtypes")
	String insertWhk2000UsrWhkInfo(Map paramMap) throws Exception;
	
	/**
	 * 사용자 웹훅 정보 수정
	 * @param 
	 * @return 
	 * @exception Exception
	 */
	@SuppressWarnings({ "rawtypes" })
	void updateWhk2000UsrWhkInfo(Map paramMap) throws Exception;
	/**
	 * 사용자 웹훅 정보 삭제
	 * @param param - Map
	 * @return 
	 * @exception Exception
	 */
	@SuppressWarnings("rawtypes")
	void deleteWhk2000UsrWhkList(Map paramMap) throws Exception;
}
