package kr.opensoftlab.oslops.prj.prj2000.prj2000.service.impl;

import java.util.List;
import java.util.Map;

import kr.opensoftlab.oslops.com.dao.ComOslitsAbstractDAO;

import org.springframework.stereotype.Repository;

/**
 * @Class Name : Adm1000DAO.java
 * @Description : Adm1000DAO DAO Class
 * @Modification Information
 *
 * @author 정형택
 * @since 2016.01.12.
 * @version 1.0
 * @see
 *  
 *  Copyright (C) OpenSoftLab Corp. All right reserved.
 */

@Repository("prj2000DAO")
public class Prj2000DAO extends ComOslitsAbstractDAO {
	
	/**
	 * Prj2000 프로젝트에 생성되어 있는 권한그룹 목록 가져오기
	 * @param param - Map
	 * @return 
	 * @exception Exception
	 */
	@SuppressWarnings("rawtypes")
	public List selectPrj2000PrjAuthGrpList(Map paramMap) throws Exception{
		return (List) list("prj2000DAO.selectPrj2000PrjAuthGrpList", paramMap);
    }
	
	/**
	 * Prj2000 소분류 메뉴 정보 목록 조회
	 * @param param - Map
	 * @return 
	 * @exception Exception
	 */
	@SuppressWarnings("rawtypes")
	public List selectPrj2000AuthGrpSmallMenuList(Map paramMap) throws Exception{
		return (List) list("prj2000DAO.selectPrj2000AuthGrpSmallMenuList", paramMap);
    }
	
	/**
	 * Prj2000 신규 권한 등록전 동일한 PK가 있는지 체크) AJAX
	 * 신규권한 등록전 동일한 PK가 있는지 체크
	 * @param 
	 * @return 
	 * @exception Exception
	 */
	@SuppressWarnings("rawtypes")
	public Map selectPrj2000DupChkAuthGrpId(Map paramMap) throws Exception{
		return (Map) select("prj2000DAO.selectPrj2000DupChkAuthGrpId", paramMap);
	}		
	
	/**
	 * Prj2000 신규 권한 등록(단건) AJAX
	 * 신규권한 등록
	 * @param 
	 * @return 
	 * @exception Exception
	 */
	@SuppressWarnings("rawtypes")
	public String insertPrj2000AuthGrpInfoAjax(Map paramMap) throws Exception{
		return (String) insert("prj2000DAO.inserPrj2000AuthGrpInfoAjax", paramMap);
	}	
	
	/**
	 * Prj2000 권한그룹 삭제
	 * @param param - Map
	 * @return int
	 * @exception Exception
	 */
	@SuppressWarnings("rawtypes")
	public int deletePrj2000AuthGrpInfoAjax(Map paramMap) throws Exception{
		return (int) delete("prj2000DAO.deletePrj2000AuthGrpInfoAjax", paramMap);
	}
	
	/**
	 * Prj2000 권한그룹 삭제시 연결된 권한프로젝트별 사용자정보를 삭제처리
	 * @param param - Map
	 * @return int
	 * @exception Exception
	 */
	@SuppressWarnings("rawtypes")
	public int deletePrj2000AuthPrjUsrListAjax(Map paramMap) throws Exception{
		return (int) delete("prj2000DAO.deletePrj2000AuthPrjUsrListAjax", paramMap);
	}
	
	/**
	 * Prj2000 권한그룹 삭제시 연결된 메뉴별 접근권한 정보 삭제처리
	 * @param param - Map
	 * @return int
	 * @exception Exception
	 */
	@SuppressWarnings("rawtypes")
	public int deletePrj2000MenuUsrAuthListAjax(Map paramMap) throws Exception{
		return (int) delete("prj2000DAO.deletePrj2000MenuUsrAuthListAjax", paramMap);
	}
	
	/**
	 * Prj2000 메뉴권한 수정(다건) AJAX
	 * 메뉴권한 수정
	 * @param 
	 * @return 
	 * @exception Exception
	 */
	@SuppressWarnings({ "rawtypes" })
	public void savePrj2000AuthGrpMenuAuthListAjax(Map paramMap) throws Exception{
		 select("prj2000DAO.savePrj2000AuthGrpMenuAuthListAjax", paramMap);
	}
	
	
	/**
	 * Prj2000 배정된 사용자 목록 조회 AJAX
	 * 배정된 사용자 목록 조회
	 * @param 
	 * @return 
	 * @exception Exception
	 */
	@SuppressWarnings("rawtypes")
	public List selectPrj2000UsrAddListAjax(Map paramMap) throws Exception{
		return list("prj2000DAO.selectPrj2000UsrAddListAjax", paramMap);
	}
	
	/**
	 * Prj2000 배정된 사용자 목록  갯수 조회 AJAX
	 * 배정된 사용자 목록 조회
	 * @param 
	 * @return 
	 * @exception Exception
	 */
	@SuppressWarnings("rawtypes")
	public int selectPrj2000UsrCntAjax(Map paramMap) throws Exception{
		return (int)select("prj2000DAO.selectPrj2000UsrCntAjax", paramMap);
	}
	
	/**
	 * Prj2000 미배정된 사용자 목록 조회 AJAX
	 * 미배정된 사용자 목록 조회
	 * @param 
	 * @return 
	 * @exception Exception
	 */
	@SuppressWarnings("rawtypes")
	public List selectPrj2000UsrAllListAjax(Map paramMap) throws Exception{
		return list("prj2000DAO.selectPrj2000UsrAllListAjax", paramMap);
	}
	
	/**
	 * Prj2000 배정 삭제된 사용자 목록 조회 AJAX
	 * 배정 삭제된 사용자 목록조회
	 * @param 
	 * @return 
	 * @exception Exception
	 */
	@SuppressWarnings("rawtypes")
	public Map selectPrj2000DelUsrInfoListAjax(Map paramMap) throws Exception{
		return (Map) select("prj2000DAO.selectPrj2000DelUsrInfoListAjax", paramMap);
	}
	
	/**
	 * Prj2000 사용자 권한 배정 처리 AJAX
	 * 사용자 권한 배정 처리
	 * @param 
	 * @return 
	 * @exception Exception
	 */
	@SuppressWarnings("rawtypes")
	public String insertPrj2000PrjUsrAuthListAjax(Map paramMap) throws Exception{
		return (String) insert("prj2000DAO.insertPrj2000PrjUsrAuthListAjax", paramMap);
	}
	
	/**
	 * Prj2000 사용자 권한 삭제 처리 AJAX
	 * 사용자 권한 삭제 처리
	 * @param 
	 * @return 
	 * @exception Exception
	 */
	@SuppressWarnings("rawtypes")
	public int deletePrj2000PrjUsrAuthListAjax(Map paramMap) throws Exception{
		return (int) delete("prj2000DAO.deletePrj2000PrjUsrAuthListAjax", paramMap);
	}

	/**
	 * Prj2000 선택한 권한그룹을 조회한다.
	 * @param 
	 * @return 
	 * @exception Exception
	 */
	@SuppressWarnings("rawtypes")
	public Map selectPrj2000AuthGrpInfoAjax(Map paramMap) throws Exception{
		return (Map)select("prj2000DAO.selectPrj2000AuthGrpInfoAjax", paramMap);
	}
	
	/**
	 * Prj2000 권한그룹을 수정한다.
	 * @param 
	 * @return 
	 * @exception Exception
	 */
	@SuppressWarnings({ "rawtypes" })
	public int updatePrj2000AuthGrpInfoAjax(Map paramMap) throws Exception{
		return update("prj2000DAO.updatePrj2000AuthGrpInfoAjax", paramMap);
	}

	/**
	 * Prj2000 [역할그룹 복사] 관리자 권한을 가지고있는 프로젝트의 역할그룹 목록
	 * @param 
	 * @return 
	 * @exception Exception
	 */
	@SuppressWarnings("rawtypes")
	public List selectPrj2000AuthGrpCopyList(Map paramMap) throws Exception{
		return list("prj2000DAO.selectPrj2000AuthGrpCopyList", paramMap);
	}
	
	/**
	 * Prj2000 역할그룹 추가 시 현재 프로젝트의 역할그룹 최고 순번+1 값을 을 가져온다.
	 * 역할그룹 관리 - 역할그룹 등록 팝업에 사용
	 * @param 
	 * @return 
	 * @exception Exception
	 */
	@SuppressWarnings("rawtypes")
	public int selectPrj2000AuthGrpNextOrd(Map paramMap) throws Exception{
		return (int) select("prj2000DAO.selectPrj2000AuthGrpNextOrd",paramMap);
	}
}
