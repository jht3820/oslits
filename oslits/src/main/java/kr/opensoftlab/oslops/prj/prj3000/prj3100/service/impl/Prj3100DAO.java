package kr.opensoftlab.oslops.prj.prj3000.prj3100.service.impl;

import java.util.List;
import java.util.Map;

import kr.opensoftlab.oslops.com.dao.ComOslitsAbstractDAO;

import org.springframework.stereotype.Repository;

/**
 * @Class Name : Prj3100DAO.java
 * @Description : Prj3100DAO DAO Class
 * @Modification Information
 *
 * @author 진주영
 * @since 2016.03.28.
 * @version 1.0
 * @see
 *  
 *  Copyright (C) OpenSoftLab Corp. All right reserved.
 */

@Repository("prj3100DAO")
public class Prj3100DAO extends ComOslitsAbstractDAO {

	/**
	 * Prj3100 라이선스 그룹에 할당된 기본 메뉴정보 목록 가져오기
	 * @param param - Map
	 * @return 
	 * @exception Exception
	 */
	@SuppressWarnings("rawtypes")
	public List selectPrj3100BaseMenuList(Map paramMap) throws Exception{
		return (List)list("prj3100DAO.selectPrj3100BaseMenuList",paramMap);
	}

	/**
	 * Prj3100 메뉴 정보 조회
	 * @param param - Map
	 * @return 
	 * @exception Exception
	 */
	@SuppressWarnings("rawtypes")
	public Map selectPrj3100MenuInfo(Map paramMap) throws Exception{
		return (Map) select("prj3100DAO.selectPrj3100MenuInfo", paramMap);
	}
	
	/**
	 * Prj3100 메뉴정보 등록(단건) AJAX
	 * 메뉴정보 등록후 등록 정보 결과 및 정보를 리턴한다.
	 * @param 
	 * @return 
	 * @exception Exception
	 */
	@SuppressWarnings("rawtypes")
	public String insertPrj3100MenuInfo(Map paramMap) throws Exception{
		return (String) insert("prj3100DAO.insertPrj3100MenuInfo", paramMap);
	}
	
	/**
	 * Prj3100 메뉴정보 삭제(단건) AJAX
	 * 메뉴정보 삭제
	 * @param 
	 * @return 
	 * @exception Exception
	 */
	@SuppressWarnings("rawtypes")
	public int deletePrj3100MenuInfo(Map paramMap) throws Exception{
		return delete("prj3100DAO.deletePrj3100MenuInfo", paramMap);
	}
	
	/**
	 * Prj3100 메뉴정보 수정(단건) AJAX
	 * 메뉴정보 수정
	 * @param 
	 * @return 
	 * @exception Exception
	 */
	@SuppressWarnings("rawtypes")
	public int updatePrj3100MenuInfo(Map paramMap) throws Exception{
		return update("prj3100DAO.updatePrj3100MenuInfo", paramMap);
	}
	
	/**
	 * Prj3100 선택한 산출물 확정 처리  AJAX
	 * 선택한 산출물 확정 처리 
	 * @param 
	 * @return 
	 * @exception Exception
	 */
	@SuppressWarnings("rawtypes")
	public int updatePrj3100FileSnSelect(Map paramMap) throws Exception{
		return update("prj3100DAO.updatePrj3100FileSnSelect", paramMap);
	}
	
}
