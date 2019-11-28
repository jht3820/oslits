package kr.opensoftlab.oslops.stm.stm3000.stm3100.service.impl;

import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import kr.opensoftlab.oslops.stm.stm3000.stm3100.service.Stm3100Service;
import kr.opensoftlab.oslops.stm.stm3000.stm3100.vo.Stm3100VO;

import org.springframework.stereotype.Service;

import egovframework.rte.fdl.cmmn.EgovAbstractServiceImpl;

/**
 * @Class Name : Stm3100ServiceImpl.java
 * @Description : Stm3100ServiceImpl Business Implement class
 * @Modification Information
 *
 * @author 공대영
 * @since 2018.07.02
 * @version 1.0
 * @see
 *  
 *  Copyright (C)  All right reserved.
 */
@Service("stm3100Service")
public class Stm3100ServiceImpl extends EgovAbstractServiceImpl implements Stm3100Service{

	/** Stm3100DAO DI */
	@Resource(name="stm3100DAO")
    private Stm3100DAO stm3100DAO;
	
	/**
	 * Stm3100 프로젝트에 배정된 Jenkins Job 목록을 조회한다. 
	 * @param paramMap
	 * @return
	 * @throws Exception
	 */
	@Override
	public List<Stm3100VO> selectStm3100JenkinsProjectList(Stm3100VO stm3100VO) throws Exception{
		return stm3100DAO.selectStm3100JenkinsProjectList(stm3100VO);
	}
	
	/**
	 * Stm3100 프로젝트에 배정된 Jenkins Job 목록 총 건수를 조회한다. (그리드 페이징 처리) 
	 * @param Stm3100VO
	 * @return
	 * @throws Exception
	 */
	public int selectStm3100JenkinsProjectListCnt(Stm3100VO stm3100VO) throws Exception {
		return stm3100DAO.selectStm3100JenkinsProjectListCnt(stm3100VO);
	}
	
	/**
	 * Stm3100프로젝트에 배정 설정  Jenkins Job 목록을 조회한다. 
	 * @param paramMap
	 * @return List - 프로젝트에 배정 설정  Jenkins Job 목록
	 * @throws Exception
	 */
	@SuppressWarnings("rawtypes")
	@Override
	public List selectStm3100JenkinsProjectAuthList(Stm3100VO stm3100VO) throws Exception {
		return stm3100DAO.selectStm3100JenkinsProjectAuthList(stm3100VO);
	}
	
	/**
	 * Stm3100 프로젝트에 배정 설정  Jenkins Job 목록 총 건수를 조회한다. (그리드 페이징 처리) 
	 * @param paramMap
	 * @return
	 * @throws Exception
	 */
	public int selectStm3100JenkinsProjectAuthListCnt(Stm3100VO stm3100VO) throws Exception {
		return stm3100DAO.selectStm3100JenkinsProjectAuthListCnt(stm3100VO);
	}
    
	/**
	 * Stm3100 프로젝트에 Jenkins Job을 배정한다.
	 * @param Stm3100VO
	 * @return
	 * @throws Exception
	 */
	@SuppressWarnings({ "unchecked", "rawtypes" })
	public void insertStm3100ProjectAddJob(Map paramMap) throws Exception{
		
		// 배정할 jenkins job 목록을 가져온다.
		List<Map<String,String>> list = (List<Map<String,String>>) paramMap.get("list");
		
		// 라이선스 그룹 ID를 가져온다.
		String licGrpId = (String)paramMap.get("licGrpId");

		int listSize = list.size();
		
		// 프로젝트에 Jenkins Job을 배정한다.
		for (int i = 0; i < listSize; i++) {
			
			Map<String,String> addJobMap = list.get(i);
			addJobMap.put("licGrpId", licGrpId);
			// 프로젝트에 Job 배정
			stm3100DAO.insertStm3100ProjectAddJob(addJobMap);
		}
	}

	/**
	 * Stm3100 프로젝트에 Jenkins Job을 배정제외 한다.
	 * @param Stm3100VO
	 * @return
	 * @throws Exception
	 */
	@SuppressWarnings({ "unchecked", "rawtypes" })
	public void deleteStm3100ProjectDelJob(Map paramMap) throws Exception{
				
		// 배정 제외할  jenkins job 목록을 가져온다.
		List<Map<String,String>> list = (List<Map<String,String>>) paramMap.get("list");
				
		// 라이선스 그룹 ID를 가져온다.
		String licGrpId = (String)paramMap.get("licGrpId");

		int listSize = list.size();
				
		// 프로젝트에 Jenkins Job을 배정 제외한다.
		for (int i = 0; i < listSize; i++) {
					
			Map<String,String> delJobMap = list.get(i);
			delJobMap.put("licGrpId", licGrpId);
			// 프로젝트에 Job 배정제외
			stm3100DAO.deleteStm3100ProjectDelJob(delJobMap);
		}

	}
	
	/**
	 * Stm3100 Jenkins Job 허용 역할 그룹 일반 목록을 조회한다.
	 * @param paramMap
	 * @throws Exception
	 */
	@SuppressWarnings("rawtypes")
	public List selectJen1300JenkinsJobAuthGrpNormalList(Map paramMap) throws Exception {
		return stm3100DAO.selectJen1300JenkinsJobAuthGrpNormalList(paramMap);
	}
	
	/**
	 * Stm3100 Jenkins Job 허용 역할 그룹 목록을 조회한다.
	 * @param paramMap
	 * @throws Exception
	 */
	@SuppressWarnings("rawtypes")
	public List selectStm3100JenkinsJobAuthGrpList(Stm3100VO stm3100VO) throws Exception {
		return stm3100DAO.selectStm3100JenkinsJobAuthGrpList(stm3100VO);
	}
	
	/**
	 * Stm3100 Jenkins job 허용 역할 그룹  목록 총 건수를 조회한다. (그리드 페이징 처리) 
	 * @param Stm3100VO
	 * @return
	 * @throws Exception
	 */
	public int selectStm3100JenkinsJobAuthGrpListCnt(Stm3100VO stm3100VO) throws Exception {
		return stm3100DAO.selectStm3100JenkinsJobAuthGrpListCnt(stm3100VO);
	}
	
	/**
	 * Stm3100 Jenkins Job에 허용 역할 그룹이 등록되어있는지 체크한다.
	 * @param paramMap
	 * @throws Exception
	 */
	@SuppressWarnings("rawtypes")
	public int selectStm3100JenkinsJobAuthGrpCnt(Map paramMap) throws Exception {
		return stm3100DAO.selectStm3100JenkinsJobAuthGrpCnt(paramMap);
	}
	
	/**
	 * Stm3100 Jenkins Job 허용 역할 그룹을 등록한다.
	 * @param paramMap
	 * @return addFailAuthCnt 등록 실패한 허용 역할그룹 수
	 * @throws Exception
	 */
	@SuppressWarnings({ "unchecked", "rawtypes" })
	public int insertStm3100JenkinsJobAuthGrpInfo(Map paramMap) throws Exception {

		// 추가할 역할 그룹 목록을 가져온다.
		List<Map<String,String>> list = (List<Map<String,String>>) paramMap.get("list");

		// 추가 실패된 역할그룹 수
		int addFailAuthCnt = 0;
		int listSize = list.size();
		
		// Jenkins Job에 역할그룹을 추가한다.
		for (int i = 0; i < listSize; i++) {
					
			Map<String,String> addAuthGrpMap = list.get(i);
			
			// 이미 등록된 역할 그룹인지 체크
			int flowAuthCnt = stm3100DAO.selectStm3100JenkinsJobAuthGrpCnt(addAuthGrpMap);
			
			// 등록되지 않은 역할 그룹일 경우 추가
			if(flowAuthCnt == 0){
				stm3100DAO.insertStm3100JenkinsJobAuthGrpInfo(addAuthGrpMap);
			}else{
				addFailAuthCnt++;
			}
		}
		return addFailAuthCnt;
	}
	
	/**
	 * jenkins Jenkins Job 허용 역할 그룹을 삭제한다.
	 * @param paramMap
	 * @throws Exception
	 */
	@SuppressWarnings({ "unchecked", "rawtypes" })
	public void deleteStm3100JenkinsJobAuthGrpInfo(Map paramMap) throws Exception {
		
		// 삭제할 역할 그룹 목록을 가져온다.
		List<Map<String,String>> list = (List<Map<String,String>>) paramMap.get("list");

		int listSize = list.size();
				
		// Jenkins Job에 역할그룹을 삭제한다.
		for (int i = 0; i < listSize; i++) {
							
			Map<String,String> delAuthGrpMap = list.get(i);
			// 역할그룹 삭제
			stm3100DAO.deleteStm3100JenkinsJobAuthGrpInfo(delAuthGrpMap);
		}	
	}

	/**
	 * Jenkins JOB_RESTORE 삭제
	 * @param paramMap
	 * @throws Exception
	 */
	@SuppressWarnings("rawtypes")
	public void updateJen1100JenkinsJobRestoreInfo(Map paramMap) throws Exception {
		stm3100DAO.updateJen1100JenkinsJobRestoreInfo(paramMap);
	}
}
