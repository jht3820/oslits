package kr.opensoftlab.oslops.adm.adm1000.adm1000.service.impl;

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

@Repository("adm1000DAO")
public class Adm1000DAO extends ComOslitsAbstractDAO {
	
	/**
	 * Adm1000 라이선스 그룹에 할당된 기본 메뉴정보 목록 가져오기
	 * @param param - Map
	 * @return 
	 * @exception Exception
	 */
	@SuppressWarnings("rawtypes")
	public List selectAdm1000BaseMenuList(Map paramMap) throws Exception {
		 return (List) list("adm1000DAO.selectAdm1000BaseMenuList", paramMap);
    }
	
	/**
	 * Adm1000 라이선스 그룹에 할당된 선택한 롤에 배정된 기본 메뉴정보 목록 가져오기
	 * @param param - Map
	 * @return 
	 * @exception Exception
	 */
	@SuppressWarnings("rawtypes")
	public List selectAdm1000AuthBaseMenuList(Map paramMap) throws Exception {
		 return (List) list("adm1000DAO.selectAdm1000AuthBaseMenuList", paramMap);
    }
	
	/**
	 * Adm1000 프로젝트에 생성되어 있는 권한그룹 목록 가져오기
	 * @param param - Map
	 * @return 
	 * @exception Exception
	 */
	@SuppressWarnings("rawtypes")
	public List selectAdm1000PrjAuthGrpList(Map paramMap) throws Exception{
		return (List) list("adm1000DAO.selectAdm1000PrjAuthGrpList", paramMap);
    }
	
	/**
	 * Adm1000 소분류 메뉴 정보 목록 조회
	 * @param param - Map
	 * @return 
	 * @exception Exception
	 */
	@SuppressWarnings("rawtypes")
	public List selectAdm1000AuthGrpSmallMenuList(Map paramMap) throws Exception{
		return (List) list("adm1000DAO.selectAdm1000AuthGrpSmallMenuList", paramMap);
    }	
	
	/**
	 * Adm1000 메뉴 정보 조회
	 * @param param - Map
	 * @return 
	 * @exception Exception
	 */
	@SuppressWarnings("rawtypes")
	public Map selectAdm1000MenuInfo(Map paramMap) throws Exception{
		return (Map) select("adm1000DAO.selectAdm1000MenuInfo", paramMap);
	}
	
	/**
	 * Adm1000 메뉴 정보 등록
	 * @param param - Map
	 * @return 
	 * @exception Exception
	 */
	@SuppressWarnings("rawtypes")
	public String insertAdm1000MenuInfo(Map paramMap) throws Exception{
		return (String) insert("adm1000DAO.insertAdm1000MenuInfo", paramMap);
	}
	
	/**
	 * Adm1000 메뉴 정보 삭제
	 * @param param - Map
	 * @return 
	 * @exception Exception
	 */
	@SuppressWarnings("rawtypes")
	public int deleteAdm1000MenuInfo(Map paramMap) throws Exception{
		return (int) delete("adm1000DAO.deleteAdm1000MenuInfo", paramMap);
	}
	
	/**
	 * Adm1000 메뉴 정보 수정
	 * @param param - Map
	 * @return 
	 * @exception Exception
	 */
	@SuppressWarnings("rawtypes")
	public int updateAdm1000MenuInfo(Map paramMap) throws Exception{
		return (int) update("adm1000DAO.updateAdm1000MenuInfo", paramMap);
	}
	
	/**
	 * Adm1000 메뉴권한 수정(다건) AJAX
	 * 메뉴권한 수정
	 * @param 
	 * @return 
	 * @exception Exception
	 */
	@SuppressWarnings({ "rawtypes" })
	public void saveAdm1000AuthGrpMenuAuthListAjax(Map paramMap) throws Exception{
		select ("adm1000DAO.saveAdm1000AuthGrpMenuAuthListAjax", paramMap);
	}
	
	/**
	 * Adm1000 신규 권한 등록전 동일한 PK가 있는지 체크) AJAX
	 * 신규권한 등록전 동일한 PK가 있는지 체크
	 * @param 
	 * @return 
	 * @exception Exception
	 */
	@SuppressWarnings("rawtypes")
	public Map selectAdm1000DupChkAuthGrpId(Map paramMap) throws Exception{
		return (Map) select("adm1000DAO.selectAdm1000DupChkAuthGrpId", paramMap);
	}		
	
	/**
	 * Adm1000 신규 권한 등록(단건) AJAX
	 * 신규권한 등록
	 * @param 
	 * @return 
	 * @exception Exception
	 */
	@SuppressWarnings("rawtypes")
	public void insertAdm1000AuthGrpInfoAjax(Map paramMap) throws Exception{
		insert("adm1000DAO.inserAdm1000AuthGrpInfoAjax", paramMap);
	}	
	
	/**
	 * Adm1000 권한그룹 삭제
	 * @param param - Map
	 * @return int
	 * @exception Exception
	 */
	@SuppressWarnings("rawtypes")
	public int deleteAdm1000AuthGrpInfoAjax(Map paramMap) throws Exception{
		return (int) delete("adm1000DAO.deleteAdm1000AuthGrpInfoAjax", paramMap);
	}
	
	/**
	 * Adm1000 권한그룹 삭제시 연결된 메뉴별 접근권한 정보 삭제처리
	 * @param param - Map
	 * @return int
	 * @exception Exception
	 */
	@SuppressWarnings("rawtypes")
	public int deleteAdm1000MenuUsrAuthListAjax(Map paramMap) throws Exception{
		return (int) delete("adm1000DAO.deleteAdm1000MenuUsrAuthListAjax", paramMap);
	}
	
	/**
	 * Adm1000 권한 조회 (단건) AJAX
	 * 권한 조회 
	 * @param 
	 * @return 
	 * @exception Exception
	 */
	@SuppressWarnings("rawtypes")
	public Map selectAdm1000AuthGrpInfoAjax(Map paramMap) throws Exception{
		return (Map) select("adm1000DAO.selectAdm1000AuthGrpInfoAjax", paramMap);
	}	
	/**
	 * 
	 * 메뉴 프로젝트 유형 변경시 권한 삭제 목록 조회
	 * 
	 * @param paramMap
	 * @return
	 * @throws Exception
	 */
	public List selectAdm1000deleteMenuList(Map paramMap) throws Exception{
		return (List) list("adm1000DAO.selectAdm1000deleteMenuList", paramMap);
    }	
	
	/**
	 * 프로젝트 권한 삭제
	 * 
	 * @param paramMap
	 * @return
	 * @throws Exception
	 */
	@SuppressWarnings("rawtypes")
	public int deleteAdm1000projectTypeAuth(Map paramMap) throws Exception{
		return (int) delete("adm1000DAO.deleteAdm1000projectTypeAuth", paramMap);
	}
	
}
