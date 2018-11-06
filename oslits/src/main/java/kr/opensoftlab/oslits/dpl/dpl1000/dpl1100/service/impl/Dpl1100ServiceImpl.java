package kr.opensoftlab.oslits.dpl.dpl1000.dpl1100.service.impl;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import kr.opensoftlab.oslits.dpl.dpl1000.dpl1100.service.Dpl1100Service;
import kr.opensoftlab.oslits.dpl.dpl1000.dpl1100.vo.Dpl1100VO;

import org.codehaus.jettison.json.JSONObject;
import org.springframework.stereotype.Service;

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

	/** Scpr5000DAO DI */
    @Resource(name="dpl1100DAO")
    private Dpl1100DAO dpl1100DAO;
    

    /**
	 * Dpl1100 
	 * @param
	 * @return 
	 * @exception Exception
	 */
	@SuppressWarnings("rawtypes")
	@Override
	public List<Dpl1100VO> selectDpl1100ExistDplList(Map map) throws Exception{
		return dpl1100DAO.selectDpl1100ExistDplList(map);
	}


	@SuppressWarnings("rawtypes")
	@Override
	public List<Dpl1100VO> selectDpl1100NotExistDplList(Map map) throws Exception {
		return dpl1100DAO.selectDpl1100NotExistDplList(map);
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
	 * 요구사항 배포 계획 배정
	 * @param paramMap
	 * @throws Exception
	 */
	@SuppressWarnings({ "unchecked", "rawtypes" })
	public void insertDpl1200ReqDplInfo(Map paramMap) throws Exception {
		int selReqCnt = Integer.parseInt((String)paramMap.get("selReqCnt"));
		
		if(selReqCnt > 1){
			List<String> selReqList = (List<String>) paramMap.get("selReq");
			
			for(String selReqInfo : selReqList){
				Map newMap = new HashMap<String, String>();
				
				JSONObject jsonObj = null;
				jsonObj = new JSONObject(selReqInfo);
				
				newMap.put("prjId", String.valueOf(paramMap.get("prjId")));
				newMap.put("reqId", String.valueOf(jsonObj.get("reqId")));
				newMap.put("processId", String.valueOf(jsonObj.get("processId")));
				newMap.put("flowId", String.valueOf(jsonObj.get("flowId")));
				newMap.put("dplId", String.valueOf(jsonObj.get("dplId")));
				
				//요구사항 배정
				dpl1100DAO.insertDpl1200ReqDplInfo(newMap);
			}
		}else{
			String selReqInfo = (String) paramMap.get("selReq");
			
			Map newMap = new HashMap<String, String>();
			
			JSONObject jsonObj = null;
			jsonObj = new JSONObject(selReqInfo);
			
			newMap.put("prjId", String.valueOf(paramMap.get("prjId")));
			newMap.put("reqId", String.valueOf(jsonObj.get("reqId")));
			newMap.put("processId", String.valueOf(jsonObj.get("processId")));
			newMap.put("flowId", String.valueOf(jsonObj.get("flowId")));
			newMap.put("dplId", String.valueOf(jsonObj.get("dplId")));
			
			//요구사항 배정
			dpl1100DAO.insertDpl1200ReqDplInfo(newMap);
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
	
	/**
	 * 요구사항 배포 계획 배정 제외
	 * @param paramMap
	 * @throws Exception
	 */
	@SuppressWarnings({ "unchecked", "rawtypes" })
	public void deleteDpl1200ReqDplInfo(Map paramMap) throws Exception {
		int selReqCnt = Integer.parseInt((String)paramMap.get("selReqCnt"));
		
		if(selReqCnt > 1){
			List<String> selReqList = (List<String>) paramMap.get("selReq");
			
			for(String selReqInfo : selReqList){
				Map newMap = new HashMap<String, String>();
				
				JSONObject jsonObj = null;
				jsonObj = new JSONObject(selReqInfo);
				
				newMap.put("prjId", String.valueOf(paramMap.get("prjId")));
				newMap.put("reqId", String.valueOf(jsonObj.get("reqId")));
				newMap.put("processId", String.valueOf(jsonObj.get("processId")));
				newMap.put("flowId", String.valueOf(jsonObj.get("flowId")));
				newMap.put("dplId", String.valueOf(jsonObj.get("dplId")));
				
				//요구사항 배정 제외
				dpl1100DAO.deleteDpl1200ReqDplInfo(newMap);
			}
		}else{
			String selReqInfo = (String) paramMap.get("selReq");
			
			Map newMap = new HashMap<String, String>();
			
			JSONObject jsonObj = null;
			jsonObj = new JSONObject(selReqInfo);
			
			newMap.put("prjId", String.valueOf(paramMap.get("prjId")));
			newMap.put("reqId", String.valueOf(jsonObj.get("reqId")));
			newMap.put("processId", String.valueOf(jsonObj.get("processId")));
			newMap.put("flowId", String.valueOf(jsonObj.get("flowId")));
			newMap.put("dplId", String.valueOf(jsonObj.get("dplId")));
			
			//요구사항 배정 제외
			dpl1100DAO.deleteDpl1200ReqDplInfo(newMap);
		}
	}
}
