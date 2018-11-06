package kr.opensoftlab.oslits.prj.prj1000.prj1000.service.impl;

import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import kr.opensoftlab.oslits.prj.prj1000.prj1000.service.Prj1000Service;

import org.springframework.stereotype.Service;

import egovframework.rte.fdl.cmmn.EgovAbstractServiceImpl;


/**
 * @Class Name : Prj1000ServiceImpl.java
 * @Description : Prj1000ServiceImpl Business Implement class
 * @Modification Information
 *
 * @author 강재민
 * @since 2016.01.23.
 * @version 1.0
 * @see
 *  
 *  Copyright (C) OpenSoftLab Corp. All right reserved.
 */
@Service("prj1000Service")
public class Prj1000ServiceImpl extends EgovAbstractServiceImpl implements Prj1000Service {
	/** Adm1000DAO DI */
    @Resource(name="prj1000DAO")
    private Prj1000DAO prj1000DAO;
    
    /**
   	 * Prj1000 프로젝트 생성관리 목록 조회
   	 * @param param - Map
   	 * @return 
   	 * @exception Exception
   	 */
   	@SuppressWarnings("rawtypes")
   	public List selectPrj1000View(Map paramMap) throws Exception {
   		return prj1000DAO.selectPrj1000View(paramMap) ;
   	}
   	
   	
    /**
   	 * Prj1000 프로젝트 단건 조회
   	 * @param param - Map
   	 * @return 
   	 * @exception Exception
   	 */
   	@SuppressWarnings("rawtypes")
   	public Map selectPrj1000Info(Map paramMap) throws Exception {
   		return prj1000DAO.selectPrj1000Info(paramMap) ;
   	}
   	
   	/**
	 * Prj1000 프로젝트 그룹 존재하는지 체크
	 * @param param - Map
	 * @return 
	 * @exception Exception
	 */
	@SuppressWarnings("rawtypes")
	public List selectPrj1000PrjGrpExistCheck(Map paramMap) throws Exception {
		return prj1000DAO.selectPrj1000PrjGrpExistCheck(paramMap);
	}
   	
	/**
	 * Prj1000 프로젝트 그룹 ID에 해당하는 프로젝트가 존재하는지 체크
	 * @param param - Map
	 * @return 
	 * @exception Exception
	 */
	@SuppressWarnings("rawtypes")
	public List selectPrj1000PrjGrpIdExistCheck(Map paramMap) throws Exception {
		return prj1000DAO.selectPrj1000PrjGrpIdExistCheck(paramMap);
	}
	
   	/**
   	 * Prj1000 옵션정보 페이지에 접속 권한이 있는 프로젝트 목록 가져오기
   	 * @param param - Map
   	 * @return 
   	 * @exception Exception
   	 */
   	@SuppressWarnings("rawtypes")
   	public List selectPrj1000AuthGrpAndMenuIdPrjListAjax(Map paramMap) throws Exception {
   		return prj1000DAO.selectPrj1000AuthGrpAndMenuIdPrjListAjax(paramMap) ;
   	}
   	
   	/**
	 * Prj1000 프로젝트 그룹 생성(insert) AJAX
	 * @param Map
	 * @exception Exception
	 */
	@SuppressWarnings("rawtypes")
	public String insertPrj1000PrjGrpAjax(Map paramMap) throws Exception{
		return (String) prj1000DAO.insertPrj1000PrjGrpAjax(paramMap);
	}
   	
   	/**
	 * Prj1000 프로젝트 생성관리 등록(insert) AJAX
	 * @param Map
	 * @return 프로젝트 ID
	 * @exception Exception
	 */
	@SuppressWarnings({ "rawtypes" })
	public String insertPrj1001Ajax(Map paramMap) throws Exception{
		//프로젝트 생성관리 등록
		String prjId = "";
		
		prj1000DAO.insertPrj1001Ajax(paramMap);
		
		//프로시저 인서트 실패일 경우
		if("-1".equals(paramMap.get("ERR_CODE"))){
			throw new Exception((String) paramMap.get("ERR_MSG"));
		}
		//프로시저 인서트 성공일 경우
		else{
			prjId = (String) paramMap.get("NEW_PRJ_ID");
			return prjId;
		}
	}
	
	
	
	/**
	 * Prj1000 프로젝트 생성관리 수정(update) AJAX
	 * @param Map
	 * @return update row
	 * @exception Exception
	 */
	@SuppressWarnings({ "rawtypes" })
	public int updatePrj1000Ajax(Map paramMap) throws Exception{
		//프로젝트 생성관리 수정
		return prj1000DAO.updatePrj1000Ajax(paramMap);
	}
	
	/**
	 * Prj1000 프로젝트 그룹 제거(delete) AJAX
	 * @param Map
	 * @return delete row
	 * @exception Exception
	 */
	@SuppressWarnings("rawtypes")
	public void deletePrj1000PrjGrpAjax(Map paramMap) throws Exception{
		prj1000DAO.deletePrj1000PrjGrpAjax(paramMap);
	}
	
	/**
	 * Prj1000 프로젝트 생성관리 삭제(delete) AJAX
	 * @param Map
	 * @return delete row
	 * @exception Exception
	 */
	@SuppressWarnings({ "rawtypes"})
	public void deletePrj1001Ajax(Map paramMap) throws Exception{
		
		prj1000DAO.updatePrj1000UserProjectId(paramMap);
		//프로젝트 생성관리 삭제
		prj1000DAO.deletePrj1001Ajax(paramMap);
		
	}


	@Override
	public List<Map> selectPrj1000ProjectGroupListAjax(
			Map<String, String> paramMap) {

		return prj1000DAO.selectPrj1000ProjectGroupListAjax(paramMap) ;
	}
	
	@Override
	public int selectPrj1000ProjectAcronymCount(Map paramMap) throws Exception{
		return prj1000DAO.selectPrj1000ProjectAcronymCount(paramMap);
	}
	
}