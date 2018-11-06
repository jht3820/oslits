package kr.opensoftlab.oslits.stm.stm3000.stm3000.service.impl;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import kr.opensoftlab.oslits.stm.stm3000.stm3000.service.Stm3000Service;
import kr.opensoftlab.oslits.stm.stm3000.stm3000.vo.Stm3000VO;

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

	/** Scpr5000DAO DI */
    @Resource(name="stm3000DAO")
    private Stm3000DAO stm3000DAO;
    
    @Override
	public List<Map> selectStm3000JenkinsUserList(Map map) throws Exception{
		return stm3000DAO.selectStm3000JenkinsUserList(map);
	}

	@Override
	public List<Stm3000VO> selectStm3000JobList(Stm3000VO stm3000vo) {
		// TODO Auto-generated method stub
		return stm3000DAO.selectStm3000JobList(stm3000vo);
	}

	@Override
	public int selectStm3000JobListCnt(Stm3000VO stm3000vo) {
		// TODO Auto-generated method stub
		return stm3000DAO.selectStm3000JobListCnt(stm3000vo);
	}

	@Override
	public Map selectStm3000JobInfo(Map<String, String> paramMap) {
		// TODO Auto-generated method stub
		return stm3000DAO.selectStm3000JobInfo(paramMap);
	}

	@Override
	public Object saveStm3000JobInfo(Map<String, String> paramMap) {
		// TODO Auto-generated method stub
		String insNewJenId ="";
		int result = 0;
		String popupGb = (String)paramMap.get("popupGb");
		
		if("insert".equals(popupGb)){
			insNewJenId = stm3000DAO.insertStm3000JobInfo(paramMap);
			return insNewJenId;
			//생성된 키가 없으면 튕겨냄
		}else if("update".equals(popupGb)){
			result = stm3000DAO.updateStm3000JobInfo(paramMap);
			return result;
		}
		return null;
	}

	@Override
	public int selectStm3000UseCountInfo(Map<String, String> paramMap) {
		// TODO Auto-generated method stub
		return stm3000DAO.selectStm3000UseCountInfo(paramMap);
	}

	@Override
	public void deleteStm3000Info(Map<String, String> paramMap) {
		// TODO Auto-generated method stub
		stm3000DAO.deleteStm3000Info(paramMap);
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
