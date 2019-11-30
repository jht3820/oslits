package kr.opensoftlab.oslops.prj.prj1000.prj1000.service.impl;

import java.util.List;
import java.util.Map;

import org.springframework.stereotype.Repository;

import kr.opensoftlab.oslops.com.dao.ComOslitsAbstractDAO;


/**
 * @Class Name : Prj1000DAO.java
 * @Description : Prj1000DAO DAO Class
 * @Modification Information
 *
 * @author 강재민
 * @since 2016.01.23.
 * @version 1.0
 * @see
 *  
 *  Copyright (C) OpenSoftLab Corp. All right reserved.
 */
@Repository("prj1000DAO")
public class Prj1000DAO extends ComOslitsAbstractDAO {
	/**
	 * Prj1000 프로젝트 생성관리 목록 조회
	 * @param param - Map
	 * @return 
	 * @exception Exception
	 */
	@SuppressWarnings("rawtypes")
	public List selectPrj1000View(Map paramMap) throws Exception {
		 return (List) list("prj1000DAO.selectPrj1000View", paramMap);
    }
	
	
	/**
	 * Prj1000 프로젝트 단건 조회
	 * @param param - Map
	 * @return 
	 * @exception Exception
	 */
	@SuppressWarnings("rawtypes")
	public Map selectPrj1000Info(Map paramMap) throws Exception {
		 return (Map) select("prj1000DAO.selectPrj1000Info", paramMap);
    }
	
	/**
	 * Prj1000 프로젝트 그룹 존재하는지 체크
	 * @param param - Map
	 * @return 
	 * @exception Exception
	 */
	@SuppressWarnings("rawtypes")
	public List selectPrj1000PrjGrpExistCheck(Map paramMap) throws Exception {
		return (List) list("prj1000DAO.selectPrj1000PrjGrpExistCheck", paramMap);
	}
	
	/**
	 * Prj1000 프로젝트 그룹 ID에 해당하는 프로젝트가 존재하는지 체크
	 * @param param - Map
	 * @return 
	 * @exception Exception
	 */
	@SuppressWarnings("rawtypes")
	public List selectPrj1000PrjGrpIdExistCheck(Map paramMap) throws Exception {
		return (List) list("prj1000DAO.selectPrj1000PrjGrpIdExistCheck", paramMap);
	}
	
	/**
	 * Prj1000 옵션정보 페이지에 접속 권한이 있는 프로젝트 목록 가져오기
	 * @param param - Map
	 * @return 
	 * @exception Exception
	 */
	@SuppressWarnings("rawtypes")
	public List selectPrj1000AuthGrpAndMenuIdPrjListAjax(Map paramMap) throws Exception {
		return (List) list("prj1000DAO.selectPrj1000AuthGrpAndMenuIdPrjListAjax", paramMap);
	}
	
	/**
	 * Prj1000 프로젝트 그룹 생성(insert) AJAX
	 * @param Map
	 * @exception Exception
	 */
	@SuppressWarnings("rawtypes")
	public String insertPrj1000PrjGrpAjax(Map paramMap) throws Exception{
		return (String) insert("prj1000DAO.insertPrj1000PrjGrpAjax", paramMap);
	}
	
	/**
	 * Prj1000 프로젝트 생성관리 등록(insert) AJAX
	 * @param Map
	 * @return 프로젝트 ID
	 * @exception Exception
	 */
	@SuppressWarnings("rawtypes")
	public Map insertPrj1001Ajax(Map paramMap) throws Exception{
		return (Map) select("prj1000DAO.insertPrj1001InfoAjax", paramMap);
	}
	
	
	
	/**
	 * Prj1000 프로젝트 생성관리 등록(update) AJAX
	 * @param Map
	 * @return update row
	 * @exception Exception
	 */
	@SuppressWarnings("rawtypes")
	public int updatePrj1000Ajax(Map paramMap) throws Exception{
		return (int) update("prj1000DAO.updatePrj1000Ajax", paramMap);
	}
	
	
	
	/**
	 * Prj1000 프로젝트 그룹 제거(delete) AJAX
	 * @param Map
	 * @return delete row
	 * @exception Exception
	 */
	@SuppressWarnings("rawtypes")
	public void deletePrj1000PrjGrpAjax(Map paramMap) throws Exception{
		delete("prj1000DAO.deletePrj1000PrjGrpAjax", paramMap);
	}
	
	/**
	 * Prj1000 프로젝트 생성관리 등록(delete) AJAX
	 * @param Map
	 * @return delete row
	 * @exception Exception
	 */
	@SuppressWarnings("rawtypes")
	public Map deletePrj1001Ajax(Map paramMap) throws Exception{
		return (Map) select("prj1000DAO.deletePrj1001Ajax", paramMap);
	}
	
	/**
	 * Prj1000 프로젝트 그룹 목록을 조회한다.
	 * @param Map
	 * @return
	 * @exception Exception
	 */
	@SuppressWarnings({"rawtypes", "unchecked"})
	public List<Map> selectPrj1000ProjectGroupListAjax(Map<String, String> paramMap) {
		return (List) list("prj1000DAO.selectPrj1000ProjectGroupListAjax", paramMap);
	}
	
	/**
	 * Prj1000 프로젝트 약어가 이미 등록되어있는지 체크한다.
	 * @param Map
	 * @return
	 * @exception Exception
	 */
	@SuppressWarnings("rawtypes")
	public int selectPrj1000ProjectAcronymCount(Map paramMap) throws Exception{
		return (Integer)select("prj1000DAO.selectPrj1000ProjectAcronymCount",paramMap);
	}
	
	/**
	 * Prj1000 개인정보 수정 - 사용자의 메인 프로젝트를 변경한다.
	 * @param Map
	 * @return
	 * @exception Exception
	 */
	@SuppressWarnings("rawtypes")
	public int updatePrj1000UserProjectId(Map paramMap) throws Exception{
		return update("prj1000DAO.updatePrj1000UserProjectId",paramMap);
	}
	

	/**
	 * Prj1000 관리 권한 있는 프로젝트 목록 검색
	 * @param param - Map
	 * @return 
	 * @exception Exception
	 */
	@SuppressWarnings("rawtypes")
	public List selectPrj1000AdminPrjList(Map paramMap) throws Exception {
		return (List) list("prj1000DAO.selectPrj1000AdminPrjList", paramMap);
	}
	

	/**
	 * Prj1000 프로젝트 마법사 생성
	 * @param Map
	 * @exception Exception
	 */
	@SuppressWarnings("rawtypes")
	public String insertPrj1000PrjWizardAjax(Map paramMap) throws Exception{
		return (String) insert("prj1000DAO.insertPrj1000PrjWizardAjax", paramMap);
	}
}
