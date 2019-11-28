package kr.opensoftlab.oslops.stm.stm4000.stm4100.service.impl;

import java.util.List;
import java.util.Map;

import org.springframework.stereotype.Repository;

import kr.opensoftlab.oslops.com.dao.ComOslitsAbstractDAO;
import kr.opensoftlab.oslops.stm.stm4000.stm4100.vo.Stm4100VO;


/**
 * @Class Name : Stm4100DAO.java
 * @Description : Stm4100DAO DAO Class
 * @Modification Information
 *
 * @author 배용진
 * @since 2019.05.10.
 * @version 1.0
 * @see
 *  
 *  Copyright (C) OpenSoftLab Corp. All right reserved.
 */
@Repository("stm4100DAO")
public class Stm4100DAO extends ComOslitsAbstractDAO {
	
	/**
	 * Stm4100 라이선스 그룹의 모든 프로젝트와 각 프로젝트에 있는 업무역할을 조회
	 * @param paramMap - Map
	 * @return list - 라이선스 그룹의 프로젝트 목록, 프로젝트의 권한그룹 목록
	 * @exception Exception
	 */
	@SuppressWarnings("rawtypes")
	public List selectStm4100PrjAuthList(Map paramMap) throws Exception {
		 return (List) list("stm4100DAO.selectPrj1000PrjAuthList", paramMap);
    }
	
	/**
	 * Stm4100 프로젝트 권한에 배정된 사용자 목록 조회
	 * @param stm4100VO
	 * @return list 배정된 사용자 목록
	 * @exception Exception
	 */
	@SuppressWarnings("rawtypes")
	public List selectStm4100UsrAddList(Stm4100VO stm4100VO) throws Exception{
		return (List)list("stm4100DAO.selectAdm1300UsrAddList", stm4100VO);
	}
	
	/**
	 * Stm4100  프로젝트 권한에 배정된 사용자 목록 총 건수 : 그리드 페이징 처리
	 * @param stm4100VO
	 * @return int 배정된 사용자 목록
	 * @exception Exception
	 */
	public int selectStm4100UsrAddListCnt(Stm4100VO stm4100VO) throws Exception{
		return (int)select("stm4100DAO.selectAdm1300UsrAddListCnt", stm4100VO);
	}
	
	/**
	 * Stm4100 선택한 프로젝트 권한에 배정되지 않은 전체 사용자 목록 목록 조회
	 * @param stm4100VO
	 * @return list 배정되지 않은 사용자 목록
	 * @exception Exception
	 */
	@SuppressWarnings("rawtypes")
	public List selectStm4100UsrAllList(Stm4100VO stm4100VO) throws Exception{
		return (List)list("stm4100DAO.selectAdm2000UsrAllList", stm4100VO);
	}
	
	/**
	 * Stm4100  선택한 프로젝트별 권한에 배정되지 않은 전체 사용자 목록 총건수 : 그리드 페이징 처리
	 * @param stm4100VO
	 * @return int 배정되지 않은 사용자 목록
	 * @exception Exception
	 */
	public int selectStm4100UsrAllListCnt(Stm4100VO stm4100VO) throws Exception{
		return (int)select("stm4100DAO.selectAdm2000UsrAllListCnt", stm4100VO);
	}
	
	/**
	 * Stm4100 프로젝트 권한에 사용자를 배정한다.
	 * @param paramMap
	 * @return 
	 * @exception Exception
	 */
	@SuppressWarnings("rawtypes")
	public String insertStm4100PrjUsrAuthList(Map paramMap) throws Exception{
		return (String) insert("stm4100DAO.insertAdm1300PrjUsrAuthList", paramMap);
	}
	
	/**
	 * Stm4100 프로젝트 권한에서 사용자를 삭제한다.
	 * @param 
	 * @return 
	 * @exception Exception
	 */
	@SuppressWarnings("rawtypes")
	public int deleteStm4100PrjUsrAuthList(Map paramMap) throws Exception{
		return (int) delete("stm4100DAO.deleteAdm1300PrjUsrAuthList", paramMap);
	}
	
	/**
	 * Stm4100 프로젝트 권한 그룹에 배정된 사용자수 조회
	 * 권한에서 사용자 삭제 시 사용자 1명일 경우 삭제 불가체크에 사용
	 * @param 
	 * @return 
	 * @exception Exception
	 */
	@SuppressWarnings("rawtypes")
	public int selectStm4100UsrCntCheck(Map paramMap) throws Exception{
		return (int)select("stm4100DAO.selectAdm1300UsrCntCheck", paramMap);
	}
}
