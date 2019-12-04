package kr.opensoftlab.oslops.prj.prj3000.prj3000.service.impl;

import java.util.List;
import java.util.Map;

import kr.opensoftlab.oslops.com.dao.ComOslitsAbstractDAO;

import org.springframework.stereotype.Repository;

/**
 * @Class Name : Prj3000DAO.java
 * @Description : Prj3000DAO DAO Class
 * @Modification Information
 *
 * @author 진주영
 * @since 2016.03.23.
 * @version 1.0
 * @see
 *  
 *  Copyright (C) OpenSoftLab Corp. All right reserved.
 */

@Repository("prj3000DAO")
public class Prj3000DAO extends ComOslitsAbstractDAO {

	/**
	 * Prj3000 라이선스 그룹에 할당된 기본 메뉴정보 목록 가져오기
	 * @param param - Map
	 * @return 
	 * @exception Exception
	 */
	@SuppressWarnings("rawtypes")
	public List selectPrj3000BaseMenuList(Map paramMap) throws Exception{
		return (List)list("prj3000DAO.selectPrj3000BaseMenuList",paramMap);
	}

	/**
	 * Prj3000 메뉴 정보 조회
	 * @param param - Map
	 * @return 
	 * @exception Exception
	 */
	@SuppressWarnings("rawtypes")
	public Map selectPrj3000MenuInfo(Map paramMap) throws Exception{
		return (Map) select("prj3000DAO.selectPrj3000MenuInfo", paramMap);
	}
	
	/**
	 * Prj3000 메뉴정보 등록(단건) AJAX
	 * 메뉴정보 등록후 등록 정보 결과 및 정보를 리턴한다.
	 * @param 
	 * @return 
	 * @exception Exception
	 */
	@SuppressWarnings("rawtypes")
	public String insertPrj3000MenuInfo(Map paramMap) throws Exception{
		return (String) insert("prj3000DAO.insertPrj3000MenuInfo", paramMap);
	}
	/**
	 * 산출물 메뉴 루트 디렉토리 생성
	 * @param 
	 * @return 
	 * @exception Exception
	 */
	@SuppressWarnings("rawtypes")
	public String insertPrj3000RootMenuInfo(Map paramMap) throws Exception{
		return (String) insert("prj3000DAO.insertPrj3000RootMenuInfo", paramMap);
	}
	
	/**
	 * Prj3000 메뉴정보 삭제(단건) AJAX
	 * 메뉴정보 삭제
	 * @param 
	 * @return 
	 * @exception Exception
	 */
	@SuppressWarnings("rawtypes")
	public int deletePrj3000MenuInfo(Map paramMap) throws Exception{
		return delete("prj3000DAO.deletePrj3000MenuInfo", paramMap);
	}
	
	/**
	 * Prj3000 메뉴정보 수정(단건) AJAX
	 * 메뉴정보 수정
	 * @param 
	 * @return 
	 * @exception Exception
	 */
	@SuppressWarnings("rawtypes")
	public int updatePrj3000MenuInfo(Map paramMap) throws Exception{
		return update("prj3000DAO.updatePrj3000MenuInfo", paramMap);
	}
	
	/**
	 * Prj3000 선택한 산출물 확정 처리  AJAX
	 * 선택한 산출물 확정 처리 
	 * @param 
	 * @return 
	 * @exception Exception
	 */
	@SuppressWarnings("rawtypes")
	public int updatePrj3000FileSnSelect(Map paramMap) throws Exception{
		return update("prj3000DAO.updatePrj3000FileSnSelect", paramMap);
	}
	
	/**
	 * Prj3000 프로젝트에 할당된 선택 하위 메뉴 불러오기(첨부파일 압축 다운로드 사용)
	 * @param param - Map
	 * @return 
	 * @exception Exception
	 */
	@SuppressWarnings("rawtypes")
	public List selectPrj3000MenuTree(Map paramMap) throws Exception{
		return (List)list("prj3000DAO.selectPrj3000MenuTree",paramMap);
	}
	
	/**
	 * Prj3000 ROOTSYSTEM_PRJ 산출물 메뉴 정보 가져오기
	 * @param param - Map
	 * @return 
	 * @exception Exception
	 */
	@SuppressWarnings("rawtypes")
	public List selectPrj3000RootMenuList(Map paramMap) throws Exception{
		return (List)list("prj3000DAO.selectPrj3000RootMenuList",paramMap);
	}

	/**
	 * Prj3000 [프로젝트 마법사] 단순 산출물 정보 가져오기
	 * @param param - Map
	 * @return 
	 * @exception Exception
	 */
	@SuppressWarnings("rawtypes")
	public Map selectPrj3000WizardMenuInfo(Map paramMap) throws Exception{
		return (Map) select("prj3000DAO.selectPrj3000WizardMenuInfo", paramMap);
	}
	
	/**
	 * [프로젝트 마법사] 개발문서 양식 생성(전체 정보 입력값)
	 * @param 
	 * @return 
	 * @exception Exception
	 */
	@SuppressWarnings("rawtypes")
	public String insertPrj3000ParamMenuInfo(Map paramMap) throws Exception{
		return (String) insert("prj3000DAO.insertPrj3000ParamMenuInfo", paramMap);
	}
}
