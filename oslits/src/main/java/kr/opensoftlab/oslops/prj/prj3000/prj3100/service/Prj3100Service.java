package kr.opensoftlab.oslops.prj.prj3000.prj3100.service;

import java.util.List;
import java.util.Map;

/**
 * @Class Name : Prj3100Service.java
 * @Description : Prj3100Service Business class
 * @Modification Information
 *
 * @author 진주영
 * @since 2016.03.28.
 * @version 1.0
 * @see
 *  
 *  Copyright (C) OpenSoftLab Corp. All right reserved.
 */
public interface Prj3100Service {

	/**
	 * Prj3000 라이선스 그룹에 할당된 기본 메뉴정보 목록 가져오기
	 * @param param - Map
	 * @return 
	 * @exception Exception
	 */
	@SuppressWarnings("rawtypes")
	List selectPrj3100BaseMenuList(Map paramMap) throws Exception;	

	/**
	 * Prj3000 메뉴 정보 조회
	 * @param param - Map
	 * @return 
	 * @exception Exception
	 */
	@SuppressWarnings("rawtypes")
	Map selectPrj3100MenuInfo(Map paramMap) throws Exception;	
	
	/**
	 * Prj3000 메뉴정보 등록(단건) AJAX
	 * 메뉴정보 등록후 등록 정보 결과 및 정보를 리턴한다.
	 * @param 
	 * @return 
	 * @exception Exception
	 */
	@SuppressWarnings("rawtypes")
	Map insertPrj3100MenuInfo(Map paramMap) throws Exception;
	
	/**
	 * Prj3000 메뉴정보 삭제(단건) AJAX
	 * 메뉴정보 삭제
	 * @param 
	 * @return 
	 * @exception Exception
	 */
	@SuppressWarnings("rawtypes")
	void deletePrj3100MenuInfo(Map paramMap) throws Exception;
	
	/**
	 * Prj3000 메뉴정보 수정(단건) AJAX
	 * 메뉴정보 수정
	 * @param 
	 * @return 
	 * @exception Exception
	 */
	@SuppressWarnings("rawtypes")
	void updatePrj3100MenuInfo(Map paramMap) throws Exception;
	
	/**
	 * Prj3000 선택한 산출물 확정 처리  AJAX
	 * 선택한 산출물 확정 처리 
	 * @param 
	 * @return 
	 * @exception Exception
	 */
	@SuppressWarnings("rawtypes")
	void updatePrj3100FileSnSelect(Map paramMap) throws Exception;
}
