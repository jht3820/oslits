package kr.opensoftlab.oslops.adm.adm1000.adm1000.service.impl;

import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import kr.opensoftlab.oslops.adm.adm1000.adm1000.service.Adm1000Service;
import kr.opensoftlab.oslops.com.exception.UserDefineException;

import org.springframework.stereotype.Service;

import egovframework.com.cmm.EgovMessageSource;
import egovframework.rte.fdl.cmmn.EgovAbstractServiceImpl;

/**
 * @Class Name : Adm1000ServiceImpl.java
 * @Description : Adm1000ServiceImpl Business Implement class
 * @Modification Information
 *
 * @author 정형택
 * @since 2016.01.12.
 * @version 1.0
 * @see
 *  
 *  Copyright (C) OpenSoftLab Corp. All right reserved.
 */

@Service("adm1000Service")
public class Adm1000ServiceImpl extends EgovAbstractServiceImpl implements Adm1000Service {

	/** Adm1000DAO DI */
    @Resource(name="adm1000DAO")
    private Adm1000DAO adm1000DAO;

    /** EgovMessageSource */
   	@Resource(name = "egovMessageSource")
   	EgovMessageSource egovMessageSource;
   	
    /**
	 * Adm1000 라이선스 그룹에 할당된 기본 메뉴정보 목록 가져오기
	 * @param param - Map
	 * @return 
	 * @exception Exception
	 */
	@SuppressWarnings("rawtypes")
	public List selectAdm1000BaseMenuList(Map paramMap) throws Exception {
		return adm1000DAO.selectAdm1000BaseMenuList(paramMap) ;
    }
    /**
	 * Adm1000 라이선스 그룹에 할당된 선택한 롤에 배정된 기본 메뉴정보 목록 가져오기
	 * @param param - Map
	 * @return 
	 * @exception Exception
	 */
	@SuppressWarnings("rawtypes")
	public List selectAdm1000AuthBaseMenuList(Map paramMap) throws Exception {
		return adm1000DAO.selectAdm1000AuthBaseMenuList(paramMap) ;
    }
	
	/**
	 * Adm1000 프로젝트에 생성되어 있는 권한그룹 목록 가져오기
	 * @param param - Map
	 * @return 
	 * @exception Exception
	 */
	@SuppressWarnings("rawtypes")
	public List selectAdm1000PrjAuthGrpList(Map paramMap) throws Exception{
		return adm1000DAO.selectAdm1000PrjAuthGrpList(paramMap) ;
    }
	
	/**
	 * Adm1000 소분류 메뉴 정보 목록 조회
	 * @param param - Map
	 * @return 
	 * @exception Exception
	 */
	@SuppressWarnings("rawtypes")
	public List selectAdm1000AuthGrpSmallMenuList(Map paramMap) throws Exception{
		return adm1000DAO.selectAdm1000AuthGrpSmallMenuList(paramMap) ;
    }
	
	/**
	 * Adm1000 메뉴 정보 조회
	 * @param param - Map
	 * @return 
	 * @exception Exception
	 */
	@SuppressWarnings("rawtypes")
	public Map selectAdm1000MenuInfo(Map paramMap) throws Exception{
		return adm1000DAO.selectAdm1000MenuInfo(paramMap) ;
	}
	
	/**
	 * Adm1000 메뉴정보 등록(단건) AJAX
	 * 메뉴정보 등록후 등록 정보 결과 및 정보를 리턴한다.
	 * @param 
	 * @return 
	 * @exception Exception
	 */
	@SuppressWarnings({ "rawtypes", "unchecked" })
	public Map insertAdm1000MenuInfo(Map paramMap) throws Exception{
		
		Map map = adm1000DAO.selectAdm1000MenuInfo(paramMap);
		
		/* 입력자 정보 삽입 */
		map.put("regUsrId", paramMap.get("regUsrId"));
		map.put("modifyUsrId", paramMap.get("modifyUsrId"));
		map.put("regUsrIp", paramMap.get("regUsrIp"));
		map.put("modifyUsrIp", paramMap.get("modifyUsrIp"));
		
		//상위메뉴정보를 이용해 하위 메뉴 기본정보 등록
		String insMenuId = adm1000DAO.insertAdm1000MenuInfo(map);
		
		//생성된 키가 없으면 튕겨냄
		if(insMenuId == null || "".equals(insMenuId)){
			
			//"메뉴 등록시 상위메뉴를 이용해 생성된 하위메뉴 키가 없음"
			throw new Exception(egovMessageSource.getMessage("adm1000.fail.menu.insert"));
		}
		
		//생성된 menuId를 이용해 새로 등록한 메뉴 정보 조회
		map.put("menuId", insMenuId);
		
		Map newMap = adm1000DAO.selectAdm1000MenuInfo(map);
		
		return newMap;
	}
	
	/**
	 * Adm1000 메뉴정보 삭제(단건) AJAX
	 * 메뉴정보 삭제
	 * @param 
	 * @return 
	 * @exception Exception
	 */
	@SuppressWarnings({ "rawtypes" })
	public void deleteAdm1000MenuInfo(Map paramMap) throws Exception{
		
		//메뉴정보 삭제
		int delCnt = adm1000DAO.deleteAdm1000MenuInfo(paramMap);
		
		//삭제된 건이 없으면 튕겨냄
		if(delCnt == 0 ){
			throw new Exception(egovMessageSource.getMessage("fail.common.delete"));
		}
	}
	
	/**
	 * Adm1000 메뉴정보 수정(단건) AJAX
	 * 메뉴정보 수정
	 * @param 
	 * @return 
	 * @exception Exception
	 */
	@SuppressWarnings({ "rawtypes" })
	public void updateAdm1000MenuInfo(Map paramMap) throws Exception{
		
		//메뉴정보 수정
		int upCnt = adm1000DAO.updateAdm1000MenuInfo(paramMap);
		
		String prjType = paramMap.get("prjType") == null ? "" :(String) paramMap.get("prjType");
		/*
		 * 수정이슈 처리 안함
		 * List<Map> menuList = null;
		if( "01".equals( prjType) || "02".equals( prjType)  ){
			menuList = adm1000DAO.selectAdm1000deleteMenuList(paramMap);
			for(Map menuMap :menuList ){
				adm1000DAO.deleteAdm1000projectTypeAuth(menuMap);
			}
		}*/
				
		//수정된 건이 없으면 튕겨냄
		if(upCnt == 0 ){
			throw new Exception(egovMessageSource.getMessage("fail.common.update"));
		}
	}
	
	/**
	 * Adm1000 메뉴권한 수정(다건) AJAX
	 * 메뉴권한 수정
	 * @param 
	 * @return 
	 * @exception Exception
	 */
	@SuppressWarnings({ "rawtypes", "unchecked" })
	public void saveAdm1000AuthGrpMenuAuthListAjax(List paramList) throws Exception{
		
		for(Map paramMap : (List<Map>)paramList){
			
			String strStatus = (String) paramMap.get("status");
			
			//만약 맵의 상태가 U 업데이트 상태이면
			if("U".equals(strStatus)){
				adm1000DAO.saveAdm1000AuthGrpMenuAuthListAjax(paramMap);
			}

			//저장 실패일 경우 모두 rollback;
			if("-1".equals(paramMap.get("ERR_CODE"))){
				throw new UserDefineException((String) paramMap.get("ERR_MSG"));
			}
		}
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
		return adm1000DAO.selectAdm1000DupChkAuthGrpId(paramMap);
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
		adm1000DAO.insertAdm1000AuthGrpInfoAjax(paramMap);
	}
	
	/**
	 * Adm1000 권한그룹 삭제
	 * @param param - Map
	 * @return int
	 * @exception Exception
	 */
	@SuppressWarnings("rawtypes")
	public void deleteAdm1000AuthGrpInfoAjax(Map paramMap) throws Exception{
		//권한그룹을 삭제처리
		int delCnt = adm1000DAO.deleteAdm1000AuthGrpInfoAjax(paramMap);
		
		//삭제된 건수가 없으면 튕겨냄
		if(delCnt == 0 ){
			throw new Exception(egovMessageSource.getMessage("fail.common.delete"));
		}
		
		//메뉴별 접근권한 정보 삭제처리
		adm1000DAO.deleteAdm1000MenuUsrAuthListAjax(paramMap);
	}
	
	/**
	 * Adm1000 신규 권한 등록(단건) AJAX
	 * 신규권한 등록
	 * @param 
	 * @return 
	 * @exception Exception
	 */
	@SuppressWarnings("rawtypes")
	public Map selectAdm1000AuthGrpInfoAjax(Map paramMap) throws Exception{
		return adm1000DAO.selectAdm1000AuthGrpInfoAjax(paramMap);
	}
}
