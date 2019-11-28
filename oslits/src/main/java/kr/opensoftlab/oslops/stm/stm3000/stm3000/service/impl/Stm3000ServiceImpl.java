package kr.opensoftlab.oslops.stm.stm3000.stm3000.service.impl;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import kr.opensoftlab.oslops.stm.stm3000.stm3000.service.Stm3000Service;
import kr.opensoftlab.oslops.stm.stm3000.stm3000.vo.Jen1000VO;
import kr.opensoftlab.oslops.stm.stm3000.stm3000.vo.Jen1100VO;
import kr.opensoftlab.oslops.stm.stm3000.stm3100.service.impl.Stm3100DAO;

import org.codehaus.jettison.json.JSONObject;
import org.springframework.stereotype.Service;

import egovframework.rte.fdl.cmmn.EgovAbstractServiceImpl;

/**
 * @Class Name : Stm3000ServiceImpl.java
 * @Description : Stm3000ServiceImpl Business Implement class
 * @Modification Information
 *
 * @author 공대영
 * @since 2018.09.03
 * @version 1.0
 * @see
 *  
 *  Copyright (C)  All right reserved.
 */
@Service("stm3000Service")
public class Stm3000ServiceImpl  extends EgovAbstractServiceImpl implements Stm3000Service{

	/** Stm3000DAO DI */
    @Resource(name="stm3000DAO")
    private Stm3000DAO stm3000DAO;
  
	/** Stm3100DAO DI */
    @Resource(name="stm3100DAO")
    private Stm3100DAO stm3100DAO;    

	/**
	 * jenkins 일반 목록 조회
	 * @param paramMap
	 * @throws Exception
	 */
	@SuppressWarnings({ "rawtypes" })
	public List<Map> selectStm3000JenkinsNormalList(Map paramMap) throws Exception {
		return stm3000DAO.selectStm3000JenkinsNormalList(paramMap);
	}
	
	/**
	 * job 일반 목록 조회
	 * @param paramMap
	 * @throws Exception
	 */
	@SuppressWarnings({"rawtypes" })
	public List<Map> selectStm3000JobNormalList(Map paramMap) throws Exception {
		return stm3000DAO.selectStm3000JobNormalList(paramMap);
	}
	
	/**
	 * jenkins 목록 조회
	 * @param paramMap
	 * @throws Exception
	 */
	public List<Jen1000VO> selectStm3000JenkinsList(Jen1000VO jen1000VO) throws Exception {
		return stm3000DAO.selectStm3000JenkinsList(jen1000VO);
	}
	
	/**
	 * job 목록 조회
	 * @param paramMap
	 * @throws Exception
	 */
	public List<Jen1100VO> selectStm3000JobList(Jen1100VO jen1100VO) throws Exception {
		return stm3000DAO.selectStm3000JobList(jen1100VO);
	}
	
	/**
	 * Jenkins 목록 총건수
	 * @param paramMap
	 * @throws Exception
	 */
	public int selectStm3000JenkinsListCnt(Jen1000VO jen1000VO) throws Exception {
		return stm3000DAO.selectStm3000JenkinsListCnt(jen1000VO);
	}
	
	/**
	 * Job 목록 총건수
	 * @param paramMap
	 * @throws Exception
	 */
	public int selectStm3000JobListCnt(Jen1100VO jen1100VO) throws Exception {
		return stm3000DAO.selectStm3000JobListCnt(jen1100VO);
	}
	
	/**
	 * Jenkins 정보 조회(단건)
	 * @param paramMap
	 * @throws Exception
	 */
	@SuppressWarnings("rawtypes")
	public Map selectStm3000JenkinsInfo(Map<String, String> paramMap) throws Exception {
		return stm3000DAO.selectStm3000JenkinsInfo(paramMap);
	}
	
	/**
	 * Job 정보 조회(단건)
	 * @param paramMap
	 * @throws Exception
	 */
	@SuppressWarnings("rawtypes")
	public Map selectStm3000JobInfo(Map<String, String> paramMap) throws Exception {
		return stm3000DAO.selectStm3000JobInfo(paramMap);
	}
	
	/**
	 * Jenkins 등록
	 * @param paramMap
	 * @throws Exception
	 */
	public String insertStm3000JenkinsInfo(Map<String, String> paramMap) throws Exception {
		return stm3000DAO.insertStm3000JenkinsInfo(paramMap);
	}
	
	/**
	 * Job 등록
	 * @param paramMap
	 * @throws Exception
	 */
	public String insertStm3000JobInfo(Map<String, String> paramMap) throws Exception {
		return stm3000DAO.insertStm3000JobInfo(paramMap);
	}
	
	/**
	 * Jenkins 수정
	 * @param paramMap
	 * @throws Exception
	 */
	public int updateStm3000JenkinsInfo(Map<String, String> paramMap) throws Exception {
		return stm3000DAO.updateStm3000JenkinsInfo(paramMap);
	}
	
	/**
	 * Job 수정
	 * @param paramMap
	 * @throws Exception
	 */
	public int updateStm3000JobInfo(Map<String, String> paramMap) throws Exception {
		return stm3000DAO.updateStm3000JobInfo(paramMap);
	}

	/**
	 * Jenkins 삭제
	 * @param paramMap
	 * @throws Exception
	 */
	public void deleteStm3000JenkinsInfo(Map<String, String> paramMap)  throws Exception{
		stm3000DAO.deleteStm3000JenkinsInfo(paramMap);
	}
	
	/**
	 * Job 삭제
	 * @param paramMap
	 * @throws Exception
	 */
	public void deleteStm3000JobInfo(Map<String, String> paramMap) throws Exception {
		
		//job 삭제
		stm3000DAO.deleteStm3000JobInfo(paramMap);
		
		//프로젝트 배정 JOB 삭제
		stm3100DAO.deleteStm3100ProjectDelJob(paramMap);
		
		//JOB 허용권한 삭제
		stm3100DAO.deleteStm3100JenkinsJobAuthGrpInfo(paramMap);

		//원복 JOB 대상 삭제
		stm3100DAO.updateJen1100JenkinsJobRestoreInfo(paramMap);
	}
	
	/**
	 * Jenkins 등록 상태 확인
	 * @param paramMap
	 * @throws Exception
	 */
	public int selectStm3000JenkinsUseCountInfo(Map<String, String> paramMap)  throws Exception{
		return stm3000DAO.selectStm3000JenkinsUseCountInfo(paramMap);
	}
	
	/**
	 * Job 등록 상태 확인
	 * @param paramMap
	 * @throws Exception
	 */
	public int selectStm3000JobUseCountInfo(Map<String, String> paramMap) throws Exception {
		return stm3000DAO.selectStm3000JobUseCountInfo(paramMap);
	}

	/**
	 * Jenkins 저장 또는 수정
	 * @param paramMap
	 * @throws Exception
	 */
	public Object saveStm3000JenkinsInfo(Map<String, String> paramMap)  throws Exception{
		String insNewJenId ="";
		int result = 0;
		String popupGb = (String)paramMap.get("popupGb");
		
		if("insert".equals(popupGb)){
			insNewJenId = stm3000DAO.insertStm3000JenkinsInfo(paramMap);
			return insNewJenId;
		}else if("update".equals(popupGb)){
			result = stm3000DAO.updateStm3000JenkinsInfo(paramMap);
			return result;
		}
		return null;
	}
	
	/**
	 * Job 저장 또는 수정
	 * @param paramMap
	 * @throws Exception
	 */
	public Object saveStm3000JobInfo(Map<String, String> paramMap)  throws Exception{
		String insNewJenId ="";
		int result = 0;
		String popupGb = (String)paramMap.get("popupGb");
		
		if("insert".equals(popupGb)){
			insNewJenId = stm3000DAO.insertStm3000JobInfo(paramMap);
			return insNewJenId;
		}else if("update".equals(popupGb)){
			//수정일때 jobType이 원복에서 변경된 경우 원복 제거
			String beforeJobTypeCd = (String)paramMap.get("beforeJobTypeCd");
			String jobTypeCd = (String)paramMap.get("jobTypeCd");
			
			//원복인경우
			if("03".equals(beforeJobTypeCd)){
				//변경된경우
				if(!jobTypeCd.equals(beforeJobTypeCd)) {
					//원복 JOB 대상 삭제
					stm3100DAO.updateJen1100JenkinsJobRestoreInfo(paramMap);
				}
			}
			
			result = stm3000DAO.updateStm3000JobInfo(paramMap);
			return result;
		}
		return null;
	}
	
	/**
	 * 젠킨스 접속 유저정보를 조회한다.
	 *  
	 * @param param - Map
	 * @return list 
	 * @throws Exception
	 */
	@SuppressWarnings({"rawtypes"})
	public List<Map> selectStm3000JenkinsUserList(Map map) throws Exception{
		return stm3000DAO.selectStm3000JenkinsUserList(map);
	}
	
	/**
	 * jenkins 허용 역할 정보 목록 저장
	 * @param paramMap
	 * @throws Exception
	 */
	@SuppressWarnings({ "rawtypes", "unchecked" })
	public int insertStm3000JenAuthGrpList(Map paramMap) throws Exception {
		int selAuthCnt = Integer.parseInt((String)paramMap.get("selAuthCnt"));
		
		//추가 실패된 갯수
		int addFailAuthCnt = 0;
		
		if(selAuthCnt > 1){
			List<String> selAuthList = (List<String>) paramMap.get("selAuth");
			
			for(String selAuthInfo : selAuthList){
				Map newMap = new HashMap<String, String>();
				
				JSONObject jsonObj = null;
				jsonObj = new JSONObject(selAuthInfo);
				
				newMap.put("licGrpId", String.valueOf(paramMap.get("licGrpId")));
				newMap.put("prjId", String.valueOf(paramMap.get("prjId")));
				newMap.put("jenId", String.valueOf(paramMap.get("jenId")));
				newMap.put("authGrpId", String.valueOf(jsonObj.get("authGrpId")));
				
				//레코드 존재하는지 체크
				int flowAuthCnt = stm3000DAO.selectStm3000JenAuthGrpCnt(newMap);
				
				//레코드 없는경우 insert
				if(flowAuthCnt == 0){
					stm3000DAO.insertStm3000JenAuthGrpInfo(newMap);
				}else{
					addFailAuthCnt++;
				}
			}
		}else{
			String selAuthInfo = (String) paramMap.get("selAuth");
			
			Map newMap = new HashMap<String, String>();
			
			JSONObject jsonObj = null;
			jsonObj = new JSONObject(selAuthInfo);
			
			newMap.put("licGrpId", String.valueOf(paramMap.get("licGrpId")));
			newMap.put("prjId", String.valueOf(paramMap.get("prjId")));
			newMap.put("jenId", String.valueOf(paramMap.get("jenId")));
			newMap.put("authGrpId", String.valueOf(jsonObj.get("authGrpId")));
			
			//레코드 존재하는지 체크
			int reqRepCnt = stm3000DAO.selectStm3000JenAuthGrpCnt(newMap);
			
			//레코드 없는경우 insert
			if(reqRepCnt == 0){
				stm3000DAO.insertStm3000JenAuthGrpInfo(newMap);
			}else{
				addFailAuthCnt++;
			}
		}
		
		return addFailAuthCnt;
	}
	
	/**
	 * jenkins 허용 역할정보 제거
	 * @param paramMap
	 * @throws Exception
	 */
	@SuppressWarnings({ "rawtypes", "unchecked" })
	public void deleteStm3000JenAuthGrpList(Map paramMap) throws Exception {
		int selAuthCnt = Integer.parseInt((String)paramMap.get("selAuthCnt"));
		
		if(selAuthCnt > 1){
			List<String> selAuthList = (List<String>) paramMap.get("selAuth");
			
			for(String selAuthInfo : selAuthList){
				Map newMap = new HashMap<String, String>();
				
				JSONObject jsonObj = null;
				jsonObj = new JSONObject(selAuthInfo);
				
				newMap.put("prjId", String.valueOf(paramMap.get("prjId")));
				newMap.put("jenId", String.valueOf(paramMap.get("jenId")));
				newMap.put("authGrpId", String.valueOf(jsonObj.get("authGrpId")));
				
				//제거
				stm3000DAO.deleteStm3000JenAuthGrpInfo(newMap);
			}
		}else{
			String selAuthInfo = (String) paramMap.get("selAuth");
			
			Map newMap = new HashMap<String, String>();
			
			JSONObject jsonObj = null;
			jsonObj = new JSONObject(selAuthInfo);
			
			newMap.put("prjId", String.valueOf(paramMap.get("prjId")));
			newMap.put("jenId", String.valueOf(paramMap.get("jenId")));
			newMap.put("authGrpId", String.valueOf(jsonObj.get("authGrpId")));
			
			//제거
			stm3000DAO.deleteStm3000JenAuthGrpInfo(newMap);
		}
	}

	/**
	 * jenkins 허용 역할 정보 저장
	 * @param paramMap
	 * @throws Exception
	 */
	@SuppressWarnings("rawtypes")
	public void insertStm3000JenAuthGrpInfo(Map paramMap) throws Exception {
		stm3000DAO.insertStm3000JenAuthGrpInfo(paramMap);
	}
	/**
	 * jenkins 허용 역할 정보 제거
	 * @param paramMap
	 * @throws Exception
	 */
	@SuppressWarnings("rawtypes")
	public void deleteStm3000JenAuthGrpInfo(Map paramMap) throws Exception {
		stm3000DAO.deleteStm3000JenAuthGrpInfo(paramMap);
	}
	
	/**
	 * jenkins 허용 역할 정보 목록 가져오기
	 * @param paramMap
	 * @throws Exception
	 */
	@SuppressWarnings("rawtypes")
	public List selectStm3000JenAuthGrpList(Map paramMap) throws Exception {
		return stm3000DAO.selectStm3000JenAuthGrpList(paramMap);
	}
	
	/**
	 * jenkins 허용 역할 정보 단건 갯수 가져오기
	 * @param paramMap
	 * @throws Exception
	 */
	@SuppressWarnings("rawtypes")
	public int selectStm3000JenAuthGrpCnt(Map paramMap) throws Exception {
		return stm3000DAO.selectStm3000JenAuthGrpCnt(paramMap);
	}
}
