package kr.opensoftlab.oslops.prj.prj3000.prj3100.service.impl;

import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import kr.opensoftlab.oslops.prj.prj3000.prj3100.service.Prj3100Service;

import org.springframework.stereotype.Service;

import egovframework.com.cmm.EgovMessageSource;
import egovframework.rte.fdl.cmmn.EgovAbstractServiceImpl;

/**
 * @Class Name : Prj3100ServiceImpl.java
 * @Description : Prj3100ServiceImpl Business Implement class
 * @Modification Information
 *
 * @author 진주영
 * @since 2016.03.28.
 * @version 1.0
 * @see
 *  
 *  Copyright (C) OpenSoftLab Corp. All right reserved.
 */

@Service("prj3100Service")
public class Prj3100ServiceImpl extends EgovAbstractServiceImpl implements Prj3100Service {

	/** Prj3100DAO DI */
    @Resource(name="prj3100DAO")
    private Prj3100DAO prj3100DAO;

    /** EgovMessageSource */
	@Resource(name = "egovMessageSource")
	EgovMessageSource egovMessageSource;
	
	/**
	 * Prj3100 라이선스 그룹에 할당된 기본 메뉴정보 목록 가져오기
	 * @param param - Map
	 * @return 
	 * @exception Exception
	 */
	@SuppressWarnings("rawtypes")
	public List selectPrj3100BaseMenuList(Map paramMap) throws Exception{
		return prj3100DAO.selectPrj3100BaseMenuList(paramMap);
	}

	/**
	 * Prj3100 메뉴 정보 조회
	 * @param param - Map
	 * @return 
	 * @exception Exception
	 */
	@SuppressWarnings("rawtypes")
	public Map selectPrj3100MenuInfo(Map paramMap) throws Exception{
		return prj3100DAO.selectPrj3100MenuInfo(paramMap);
	}
	
	/**
	 * Prj3100 메뉴정보 등록(단건) AJAX
	 * 메뉴정보 등록후 등록 정보 결과 및 정보를 리턴한다.
	 * @param 
	 * @return 
	 * @exception Exception
	 */
	@SuppressWarnings({ "rawtypes", "unchecked" })
	public Map insertPrj3100MenuInfo(Map paramMap) throws Exception{
		Map map = prj3100DAO.selectPrj3100MenuInfo(paramMap);
		
		/* 입력자 정보 삽입 */
		map.put("regUsrId", paramMap.get("regUsrId"));
		map.put("modifyUsrId", paramMap.get("modifyUsrId"));
		map.put("regUsrIp", paramMap.get("regUsrIp"));
		map.put("modifyUsrIp", paramMap.get("modifyUsrIp"));
		
		//상위메뉴정보를 이용해 하위 메뉴 기본정보 등록
		String insDocId = prj3100DAO.insertPrj3100MenuInfo(map);
		
		//생성된 키가 없으면 튕겨냄
		if(insDocId == null || "".equals(insDocId)){
			throw new Exception(egovMessageSource.getMessage("prj3000.fail.menu.insert"));
		}
		
		//생성된 menuId를 이용해 새로 등록한 메뉴 정보 조회
		map.put("docId", insDocId);
		
		Map newMap = prj3100DAO.selectPrj3100MenuInfo(map);
		
		return newMap;
	}
	
	/**
	 * Prj3100 메뉴정보 삭제(단건) AJAX
	 * 메뉴정보 삭제
	 * @param 
	 * @return 
	 * @exception Exception
	 */
	@SuppressWarnings("rawtypes")
	public void deletePrj3100MenuInfo(Map paramMap) throws Exception{
		//메뉴정보 삭제
				int delCnt = prj3100DAO.deletePrj3100MenuInfo(paramMap);
				
				//삭제된 건이 없으면 튕겨냄
				if(delCnt == 0 ){
					throw new Exception(egovMessageSource.getMessage("fail.common.delete"));
				}
	}
	
	/**
	 * Prj3100 메뉴정보 수정(단건) AJAX
	 * 메뉴정보 수정
	 * @param 
	 * @return 
	 * @exception Exception
	 */
	@SuppressWarnings("rawtypes")
	public void updatePrj3100MenuInfo(Map paramMap) throws Exception{
		//메뉴정보 수정
				int upCnt = prj3100DAO.updatePrj3100MenuInfo(paramMap);
				
				//수정된 건이 없으면 튕겨냄
				if(upCnt == 0 ){
					throw new Exception(egovMessageSource.getMessage("fail.common.update"));
				}
	}
	
	/**
	 * Prj3100 선택한 산출물 확정 처리  AJAX
	 * 선택한 산출물 확정 처리 
	 * @param 
	 * @return 
	 * @exception Exception
	 */
	@SuppressWarnings("rawtypes")
	public void updatePrj3100FileSnSelect(Map paramMap) throws Exception{
		int upCnt = prj3100DAO.updatePrj3100FileSnSelect(paramMap);
		
		//수정된 건이 없으면 튕겨냄
		if(upCnt == 0 ){
			throw new Exception(egovMessageSource.getMessage("fail.common.update"));
		}
	}
}
