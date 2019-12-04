package kr.opensoftlab.oslops.dpl.dpl1000.dpl1100.service.impl;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import kr.opensoftlab.oslops.dpl.dpl1000.dpl1000.service.impl.Dpl1000DAO;
import kr.opensoftlab.oslops.dpl.dpl1000.dpl1100.service.Dpl1100Service;
import kr.opensoftlab.oslops.dpl.dpl1000.dpl1100.vo.Dpl1100VO;

import org.codehaus.jettison.json.JSONArray;
import org.codehaus.jettison.json.JSONObject;
import org.springframework.stereotype.Service;

import com.fasterxml.jackson.databind.ObjectMapper;

import egovframework.rte.fdl.cmmn.EgovAbstractServiceImpl;

/**
 * @Class Name : Dpl1100ServiceImpl.java
 * @Description : Dpl1100ServiceImpl Business Implement class
 * @Modification Information
 *
 * @author 공대영
 * @since 2016.02.08
 * @version 1.0
 * @see
 *  
 *  Copyright (C)  All right reserved.
 */
@Service("dpl1100Service")
public class Dpl1100ServiceImpl  extends EgovAbstractServiceImpl implements Dpl1100Service{

	/** Dpl1000DAO DI */
    @Resource(name="dpl1000DAO")
    private Dpl1000DAO dpl1000DAO;
    
    /** Dpl1100DAO DI */
    @Resource(name="dpl1100DAO")
    private Dpl1100DAO dpl1100DAO;
    
	/**
	 * Dpl1100 배포 계획에 배정된 요구사항 목록을 조회한다.
	 * @param Dpl1100VO
	 * @return list - 배포계획 배정된 요구사항 목록
	 * @throws Exception
	 */
	@SuppressWarnings("rawtypes")
	public List<Map> selectDpl1100ExistDplList(Dpl1100VO dpl1100VO) throws Exception{
		return dpl1100DAO.selectDpl1100ExistDplList(dpl1100VO);
	}

	/**
	 * Dpl1100 배포 계획에 배정된 요구사항 목록 총 건수를 조회한다. (그리드 페이징 처리) 
	 * @param Dpl1100VO
	 * @return int - 배포계획 배정된 요구사항 총 건수
	 * @throws Exception
	 */
	public int selectDpl1100ExistDplListCnt(Dpl1100VO dpl1100VO) throws Exception {
		return dpl1100DAO.selectDpl1100ExistDplListCnt(dpl1100VO);
	}

	/**
	 * Dpl1100 배포계획 미배정된 요구사항 목록을 조회한다.
	 * - 미배정 요구사항 : 배포계획 저장이 있는 작업흐름에 속해있으며, 아직 배포계획에 배정되지 않은 요구사항
	 * @param Dpl1100VO
	 * @return list
	 * @throws Exception
	 */
	@SuppressWarnings("rawtypes")
	@Override
	public List<Map> selectDpl1100NotExistDplList(Dpl1100VO dpl1100VO) throws Exception {
		return dpl1100DAO.selectDpl1100NotExistDplList(dpl1100VO);
	}
	
	/**
	 * Dpl1100 배포계획 미배정된 요구사항 목록 총 건수를 조회한다. (그리드 페이징 처리) 
	 * @param Dpl1100VO
	 * @return int - 배포계획 미배정된 요구사항 총 건수
	 * @throws Exception
	 */
	public int selectDpl1100NotExistDplListCnt(Dpl1100VO dpl1100VO) throws Exception {
		return dpl1100DAO.selectDpl1100NotExistDplListCnt(dpl1100VO);
	}


	@SuppressWarnings({ "unchecked", "rawtypes" })
	@Override
	public void updateDpl1100Dpl(Map<String, Object> paramMap) throws Exception{
		List<Map<String,String>> list = (List<Map<String,String>>) paramMap.get("list");
		
		if(list == null){
			dpl1100DAO.updateDpl1100Dpl(paramMap);
		}else{
			//list가 null일경우 paramMap으로 세팅
			int listSize = list.size();
			
			for (int i = 0; i < listSize; i++) {
				Map reqMap = list.get(i);
				reqMap.put("selPrjId", (String)paramMap.get("selPrjId"));
				if(list.size() > 1){
					reqMap.put("dplId", (String)paramMap.get("dplId"));
				}
	
				dpl1100DAO.updateDpl1100Dpl(reqMap);
			}
		}
	}

	
	@SuppressWarnings("rawtypes")
	@Override
	public String selectDpl1100ExistBuildInfo(Map paramMap) throws Exception {
		return dpl1100DAO.selectDpl1100ExistBuildInfo(paramMap);
	}
	

	/**
	 * 요구사항 배포 계획 배정
	 * @param paramMap
	 * @throws Exception
	 */
	@SuppressWarnings("rawtypes")
	public List selectDpl1100ReqDplList(Map paramMap) throws Exception {
		return dpl1100DAO.selectDpl1100ReqDplList(paramMap);
	}
	
	/**
	 * Dpl1100 배포계획에 요구사항을 배정한다.
	 * @param paramMap
	 * @throws Exception
	 */
	@SuppressWarnings({ "unchecked", "rawtypes" })
	public void insertDpl1100ReqDplInfo(Map paramMap) throws Exception {
		
		// 프로젝트 ID를 가져온다.
		String prjId = (String)paramMap.get("prjId");
		
		// 배정 요구사항 목록 json array parse
    	JSONArray selReqList = new JSONArray(paramMap.get("selReqList").toString());
    	
		//새로운 NEW_CHG_ID 구하기
		String newChgId = "";
		
		//수정이력 순번
		int chgNum = 0;
		
    	// 배포계획에 배정 요구사항 목록을 등록
    	for(int i=0; i < selReqList.length(); i++){
    		
    		JSONObject jsonObj = selReqList.getJSONObject(i);
    		
    		// 배포계획에 배정할 요구사항 Map
    		HashMap<String, Object> addReqInfoMap = new ObjectMapper().readValue(jsonObj.toString(), HashMap.class) ;
			
    		// Map에 정보 세팅
    		addReqInfoMap.put("prjId", prjId);
    		addReqInfoMap.put("reqId", String.valueOf(jsonObj.get("reqId")));
    		addReqInfoMap.put("processId", String.valueOf(jsonObj.get("processId")));
    		addReqInfoMap.put("flowId", String.valueOf(jsonObj.get("flowId")));
    		addReqInfoMap.put("dplId", String.valueOf(jsonObj.get("dplId")));
    		
    		if(i == 0){
				//새로운 NEW_CHG_ID 구하기
				newChgId = dpl1000DAO.selectDpl1500NewChgId(addReqInfoMap);
			}
    		
    		// 배포계획에 요구사항 배정
			dpl1100DAO.insertDpl1100ReqDplInfo(addReqInfoMap);
			
			//추가 정보 대입
			addReqInfoMap.put("chgId", newChgId);
			addReqInfoMap.put("chgNum", chgNum);
			addReqInfoMap.put("chgTypeCd", "02");
			addReqInfoMap.put("chgNm", String.valueOf(jsonObj.get("reqId")));	//항목 명
			addReqInfoMap.put("chgOptTypeCd", "04");	//항목 타입
			addReqInfoMap.put("chgUsrId", paramMap.get("regUsrId"));
			
			//수정이력 등록
			dpl1000DAO.insertDpl1500ModifyHistoryInfo(addReqInfoMap);
			chgNum++;
    	}
	}

	/**
	 * Dpl1100 배포계획에 배정된 요구사항을 배정제외한다.
	 * @param paramMap
	 * @throws Exception
	 */
	@SuppressWarnings({ "unchecked", "rawtypes" })
	public void deleteDpl1100ReqDplInfo(Map paramMap) throws Exception {
		
		// 프로젝트 ID를 가져온다.
		String prjId = (String)paramMap.get("prjId");
				
		// 배정제외할 요구사항 목록 json array parse
		JSONArray selReqList = new JSONArray(paramMap.get("selReqList").toString());
		
		//새로운 NEW_CHG_ID
		String newChgId = "";
		
		//수정이력 순번
		int chgNum = 0;
		
		
		// 배포계획에 배정 요구사항 목록을 등록
		for(int i=0; i < selReqList.length(); i++){
		    		
			JSONObject jsonObj = selReqList.getJSONObject(i);
		    		
			// 배포계획에서 배정 제외할 요구사항 Map
			HashMap<String, Object> delReqInfoMap = new ObjectMapper().readValue(jsonObj.toString(), HashMap.class) ;
					
			// Map에 정보 세팅
			delReqInfoMap.put("prjId", prjId);
			delReqInfoMap.put("reqId", String.valueOf(jsonObj.get("reqId")));
			delReqInfoMap.put("processId", String.valueOf(jsonObj.get("processId")));
			delReqInfoMap.put("flowId", String.valueOf(jsonObj.get("flowId")));
			delReqInfoMap.put("dplId", String.valueOf(jsonObj.get("dplId")));
		    		
			if(i == 0){
				//새로운 NEW_CHG_ID 구하기
				newChgId = dpl1000DAO.selectDpl1500NewChgId(delReqInfoMap);
			}
			
			// 배포계획에 요구사항 배정 제외
			dpl1100DAO.deleteDpl1100ReqDplInfo(delReqInfoMap);
			
			//추가 정보 대입
			delReqInfoMap.put("chgId", newChgId);
			delReqInfoMap.put("chgNum", chgNum);
			delReqInfoMap.put("chgTypeCd", "03");
			delReqInfoMap.put("chgNm", String.valueOf(jsonObj.get("reqId")));	//항목 명
			delReqInfoMap.put("chgOptTypeCd", "04");	//항목 타입
			delReqInfoMap.put("chgUsrId", paramMap.get("regUsrId"));
			
			//수정이력 등록
			dpl1000DAO.insertDpl1500ModifyHistoryInfo(delReqInfoMap);
			chgNum++;
		}
	}
	
	
	/**
	 * 요구사항 배포 계획 배정 update
	 * @param paramMap
	 * @throws Exception
	 */
	@SuppressWarnings("rawtypes")
	public int updateDpl1200ReqDplInfo(Map paramMap) throws Exception {
		return dpl1100DAO.updateDpl1200ReqDplInfo(paramMap);
	}
	

}
