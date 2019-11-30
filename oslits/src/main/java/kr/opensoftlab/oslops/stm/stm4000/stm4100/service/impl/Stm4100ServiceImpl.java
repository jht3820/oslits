package kr.opensoftlab.oslops.stm.stm4000.stm4100.service.impl;

import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import org.springframework.stereotype.Service;

import egovframework.rte.fdl.cmmn.EgovAbstractServiceImpl;
import kr.opensoftlab.oslops.stm.stm4000.stm4100.service.Stm4100Service;
import kr.opensoftlab.oslops.stm.stm4000.stm4100.vo.Stm4100VO;


/**
 * @Class Name : Stm4100ServiceImpl.java
 * @Description : Stm4100ServiceImpl Business Implement class
 * @Modification Information
 *
 * @author 배용진
 * @since 2019.05.10.
 * @version 1.0
 * @see
 *  
 *  Copyright (C) OpenSoftLab Corp. All right reserved.
 */
@Service("stm4100Service")
public class Stm4100ServiceImpl extends EgovAbstractServiceImpl implements Stm4100Service {
	
	/** Stm4100DAO DI */
    @Resource(name="stm4100DAO")
    private Stm4100DAO stm4100DAO;	
	
	/**
	 * Stm41000 라이선스 그룹의 모든 프로젝트와 각 프로젝트에 있는 업무역할을 조회
	 * @param paramMap - Map
	 * @return list - 라이선스 그룹의 프로젝트 목록, 프로젝트의 권한그룹 목록
	 * @exception Exception
	 */
	@SuppressWarnings("rawtypes")
	public List selectStm4100PrjAuthList(Map paramMap) throws Exception {
		 return stm4100DAO.selectStm4100PrjAuthList(paramMap);
    }
	
	/**
	 * Stm4100 프로젝트 권한에 배정된 사용자 목록 조회
	 * @param stm4100VO
	 * @return list 배정된 사용자 목록
	 * @exception Exception
	 */
	@SuppressWarnings("rawtypes")
	public List selectStm4100UsrAddList(Stm4100VO stm4100VO) throws Exception{
		return stm4100DAO.selectStm4100UsrAddList(stm4100VO);
	}
	
	/**
	 * Stm4100  프로젝트 권한에 배정된 사용자 목록 총 건수 : 그리드 페이징 처리
	 * @param stm4100VO
	 * @return int 배정된 사용자 목록
	 * @exception Exception
	 */
	public int selectStm4100UsrAddListCnt(Stm4100VO stm4100VO) throws Exception{
		return stm4100DAO.selectStm4100UsrAddListCnt(stm4100VO);
	}
	
	/**
	 * Stm4100 선택한 프로젝트 권한에 배정되지 않은 전체 사용자 목록 목록 조회
	 * @param stm4100VO
	 * @return list 배정되지 않은 사용자 목록
	 * @exception Exception
	 */
	@SuppressWarnings("rawtypes")
	public List selectStm4100UsrAllList(Stm4100VO stm4100VO) throws Exception{
		return stm4100DAO.selectStm4100UsrAllList(stm4100VO);
	}
	
	/**
	 * Stm4100  선택한 프로젝트별 권한에 배정되지 않은 전체 사용자 목록 총건수 : 그리드 페이징 처리
	 * @param stm4100VO
	 * @return int 배정되지 않은 사용자 목록
	 * @exception Exception
	 */
	public int selectStm4100UsrAllListCnt(Stm4100VO stm4100VO) throws Exception{
		return stm4100DAO.selectStm4100UsrAllListCnt(stm4100VO);
	}
	
	/**
	 * Stm4100 프로젝트 권한에 사용자를 배정한다.
	 * @param paramMap
	 * @return 
	 * @exception Exception
	 */
	@SuppressWarnings({"rawtypes", "unchecked"})
	public void insertStm4100PrjUsrAuthList(Map paramMap) throws Exception{
		
		// 역할그룹에 배정할 사용자 목록을 가져온다.
		List<Map<String,String>> list = (List<Map<String,String>>) paramMap.get("list");
		
		int listSize = list.size();
				
		// 역할그룹에 사용자를 배정한다.
		for (int i = 0; i < listSize; i++) {
			Map<String,String> authUsrInfoMap = list.get(i);
			// 역할그룹 사용자 배정
			stm4100DAO.insertStm4100PrjUsrAuthList(authUsrInfoMap);
		}
	}
	
	/**
	 * Stm4100 프로젝트 권한에서 사용자를 삭제한다.
	 * @param 
	 * @return 
	 * @exception Exception
	 */
	@SuppressWarnings({"rawtypes", "unchecked"})
	public void deleteStm4100PrjUsrAuthList(Map paramMap) throws Exception{
		
		// 역할그룹에 배정할 사용자 목록을 가져온다.
		List<Map<String,String>> list = (List<Map<String,String>>) paramMap.get("list");
		
		int listSize = list.size();
				
		// 역할그룹에서 사용자를 삭제한다.
		for (int i = 0; i < listSize; i++) {
			Map<String,String> authUsrInfoMap = list.get(i);
			// 역할그룹 사용자 삭제
			 stm4100DAO.deleteStm4100PrjUsrAuthList(authUsrInfoMap);
		}
	}
	
	/**
	 * Stm4100 프로젝트 권한 그룹에 배정된 사용자수 조회
	 * 권한에서 사용자 삭제 시 사용자 1명일 경우 삭제 불가체크에 사용
	 * @param paramMap
	 * @return 프로젝트 권한에 배정된 사용자 수
	 * @exception Exception
	 */
	@SuppressWarnings("rawtypes")
	public int selectStm4100UsrCntCheck(Map paramMap) throws Exception{
		return stm4100DAO.selectStm4100UsrCntCheck(paramMap);
	}

}








