package kr.opensoftlab.oslops.prj.prj2000.prj2000.service.impl;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import org.springframework.stereotype.Service;

import egovframework.com.cmm.EgovMessageSource;
import egovframework.rte.fdl.cmmn.EgovAbstractServiceImpl;
import kr.opensoftlab.oslops.com.exception.UserDefineException;
import kr.opensoftlab.oslops.prj.prj2000.prj2000.service.Prj2000Service;

/**
 * @Class Name : Prj2000ServiceImpl.java
 * @Description : Prj2000ServiceImpl Business Implement class
 * @Modification Information
 *
 * @author 정형택
 * @since 2016.01.26.
 * @version 1.0
 * @see
 *  
 *  Copyright (C) OpenSoftLab Corp. All right reserved.
 */

@Service("prj2000Service")
public class Prj2000ServiceImpl extends EgovAbstractServiceImpl implements Prj2000Service {

	/** Prj2000DAO DI */
    @Resource(name="prj2000DAO")
    private Prj2000DAO prj2000DAO;

    /** EgovMessageSource */
	@Resource(name = "egovMessageSource")
	EgovMessageSource egovMessageSource;
	
	/**
	 * Adm1000 프로젝트에 생성되어 있는 권한그룹 목록 가져오기
	 * @param param - Map
	 * @return 
	 * @exception Exception
	 */
	@SuppressWarnings("rawtypes")
	public List selectPrj2000PrjAuthGrpList(Map paramMap) throws Exception{
		return prj2000DAO.selectPrj2000PrjAuthGrpList(paramMap) ;
    }
	
	/**
	 * Prj2000 소분류 메뉴 정보 목록 조회
	 * @param param - Map
	 * @return 
	 * @exception Exception
	 */
	@SuppressWarnings("rawtypes")
	public List selectPrj2000AuthGrpSmallMenuList(Map paramMap) throws Exception{
		return prj2000DAO.selectPrj2000AuthGrpSmallMenuList(paramMap) ;
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
		return prj2000DAO.selectPrj2000DupChkAuthGrpId(paramMap);
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
		return (String) prj2000DAO.insertPrj2000AuthGrpInfoAjax(paramMap);
	}
	
	/**
	 * Prj2000 권한그룹 삭제
	 * @param param - Map
	 * @return int
	 * @exception Exception
	 */
	@SuppressWarnings({ "rawtypes", "unchecked" })
	public void deletePrj2000AuthGrpInfoAjax(Map paramMap) throws Exception{
		//권한그룹을 삭제처리
		int delCnt = prj2000DAO.deletePrj2000AuthGrpInfoAjax(paramMap);

		//삭제된 건수가 없으면 튕겨냄
		if(delCnt == 0 ){
			throw new Exception(egovMessageSource.getMessage("prj2000.fail.prjAuth.delete"));
		}
		
		//권한프로젝트별 사용자정보를 삭제처리
		paramMap.put("prjId", paramMap.get("selPrjId"));	//프로젝트 ID로 선택한 프로젝트 ID 세팅.
		prj2000DAO.deletePrj2000AuthPrjUsrListAjax(paramMap);
		
		//메뉴별 접근권한 정보 삭제처리
		prj2000DAO.deletePrj2000MenuUsrAuthListAjax(paramMap);
	}
	
	/**
	 * Prj2000 메뉴권한 수정(다건) AJAX
	 * 메뉴권한 수정
	 * @param 
	 * @return 
	 * @exception Exception
	 */
	@SuppressWarnings({ "rawtypes", "unchecked" })
	public void savePrj2000AuthGrpMenuAuthListAjax(List paramList) throws Exception{
		
		for(Map paramMap : (List<Map>)paramList){
			
			String strStatus = (String) paramMap.get("status");
			
			//만약 맵의 상태가 U 업데이트 상태이면
			if("U".equals(strStatus)){
				prj2000DAO.savePrj2000AuthGrpMenuAuthListAjax(paramMap);
			}

			//저장 실패일 경우 모두 rollback;
			if("-1".equals(paramMap.get("ERR_CODE"))){
				throw new UserDefineException((String) paramMap.get("ERR_MSG"));
			}
		}
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
		return prj2000DAO.selectPrj2000UsrAddListAjax(paramMap) ;
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
		return prj2000DAO.selectPrj2000UsrCntAjax(paramMap);
	}
	
	/**
	 * Prj2000 라이선스 그룹내 전체 사용자 목록 조회 AJAX
	 * 라이선스 그룹내 전체 사용자 목록 조회
	 * @param 
	 * @return 
	 * @exception Exception
	 */
	@SuppressWarnings("rawtypes")
	public List selectPrj2000UsrAllListAjax(Map paramMap) throws Exception{
		return prj2000DAO.selectPrj2000UsrAllListAjax(paramMap) ;
	}
	
	/**
	 * Prj2000 배정 삭제된 사용자 목록 조회 AJAX
	 * 배정 삭제된 사용자 목록조회
	 * @param 
	 * @return 
	 * @exception Exception
	 */
	@SuppressWarnings({"rawtypes", "unchecked"})
	@Override
	public List selectPrj2000DelUsrInfoListAjax(Map paramMap) throws Exception {

		List usrList = new ArrayList<Map>();
		
		// 사용자 Id 문자열을  , 로 자른다. 
		String usrIdStr = (String) paramMap.get("strUsrIdInSql");
		String[] usrIdList = usrIdStr.split(",");
		
		// 배정 삭제된 사용자 목록 조회
		for(String usrId : usrIdList) {
			paramMap.put("usrId", usrId);
			Map userMap = prj2000DAO.selectPrj2000DelUsrInfoListAjax(paramMap);
			usrList.add(userMap);
		}
		return usrList;
	}
	
	/**
	 * Prj2000 사용자 권한 배정 및 삭제 처리 AJAX
	 * 사용자 권한 배정 및 삭제 처리
	 * @param 
	 * @return 
	 * @exception Exception
	 */
	@SuppressWarnings({"rawtypes", "unchecked"})
	public void savePrj2000PrjUsrAuthListAjax(Map paramMap) throws Exception{
		
		String status = (String) paramMap.get("status");

		// 사용자 Id 문자열을  , 로 자른다. 
		String usrIdStr = (String) paramMap.get("strUsrIdInSql");
		String[] usrIdList = usrIdStr.split(",");
		
		for(String usrId : usrIdList) {
			
			paramMap.put("usrId", usrId);
			
			if("I".equals(status)){
				prj2000DAO.insertPrj2000PrjUsrAuthListAjax(paramMap);	//유저권한정보 등록
			}
			else if("D".equals(status)){
				prj2000DAO.deletePrj2000PrjUsrAuthListAjax(paramMap);	//유저권한정보 삭제
			}
		}
	}
	
	/**
	 * Prj2000 선택한 권한그룹을 조회한다.
	 * @param 
	 * @return 
	 * @exception Exception
	 */
	@SuppressWarnings("rawtypes")
	@Override
	public Map selectPrj2000AuthGrpInfoAjax(Map paramMap) throws Exception{
		return prj2000DAO.selectPrj2000AuthGrpInfoAjax(paramMap);
	}
	
	/**
	 * Prj2000 권한그룹을 수정한다.
	 * @param 
	 * @return 
	 * @exception Exception
	 */
	@Override
	@SuppressWarnings({ "rawtypes" })
	public int updatePrj2000AuthGrpInfoAjax(Map paramMap) throws Exception{
		return prj2000DAO.updatePrj2000AuthGrpInfoAjax(paramMap);
	}


	/**
	 * Prj2000 [역할그룹 복사] 관리자 권한을 가지고있는 프로젝트의 역할그룹 목록
	 * @param 
	 * @return 
	 * @exception Exception
	 */
	@SuppressWarnings("rawtypes")
	public List selectPrj2000AuthGrpCopyList(Map paramMap) throws Exception{
		return prj2000DAO.selectPrj2000AuthGrpCopyList(paramMap);
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
		return prj2000DAO.selectPrj2000AuthGrpNextOrd(paramMap);
	}
}
