package kr.opensoftlab.oslops.prj.prj1000.prj1100.service.impl;

/**
 * @Class Name : Prj1100Service.java
 * @Description : Prj1100Service Service class
 * @Modification Information
 *
 * @author 진주영
 * @since 2018.07.19.
 * @version 1.0
 * @see
 *  
 *  Copyright (C) OpenSoftLab Corp All right reserved.
 */

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import kr.opensoftlab.oslops.com.fms.web.service.FileMngService;
import kr.opensoftlab.oslops.prj.prj1000.prj1100.service.Prj1100Service;
import kr.opensoftlab.oslops.prj.prj1000.prj1100.vo.Prj1100VO;

import org.codehaus.jettison.json.JSONObject;
import org.springframework.stereotype.Service;

import egovframework.rte.fdl.cmmn.EgovAbstractServiceImpl;
import egovframework.rte.fdl.idgnr.EgovIdGnrService;

@Service("prj1100Service")
public class Prj1100ServiceImpl extends EgovAbstractServiceImpl implements Prj1100Service {
	
	/** DAO Bean Injection */
    @Resource(name="prj1100DAO")
    private Prj1100DAO prj1100DAO; 
   	
    /**
	 * 프로세스 목록 조회 
	 * @param paramMap
	 * @throws Exception
	 */
	@SuppressWarnings("rawtypes")
	public List selectFlw1000ProcessList(Map paramMap) throws Exception {
		return prj1100DAO.selectFlw1000ProcessList(paramMap);
	}

	/**
	 * 프로세스 단건 조회 
	 * @param paramMap
	 * @throws Exception
	 */
	@SuppressWarnings("rawtypes")
	public Map selectFlw1000ProcessInfo(Map paramMap) throws Exception {
		return prj1100DAO.selectFlw1000ProcessInfo(paramMap);
	}
	/**
	 * 프로세스 수정 (이름, json데이터)
	 * @param paramMap
	 * @throws Exception
	 */
	@SuppressWarnings("rawtypes")
	public void updateFlw1000ProcessInfo(Map paramMap) throws Exception {
		prj1100DAO.updateFlw1000ProcessInfo(paramMap);
	}

	/**
	 * 프로세스 확정 ('종료분기' 작업흐름 데이터, 프로세스 json data)
	 * @param paramMap
	 * @throws Exception
	 */
	@SuppressWarnings("rawtypes")
	public void updateFlw1000ProcessConfirmInfo(Map paramMap) throws Exception {
		//작업흐름 추가
		prj1100DAO.insertFlw1100FlowInfo(paramMap);
		
		Map<String, String> newMap = new HashMap<String, String>();
		newMap.put("prjId", (String)paramMap.get("prjId"));
		newMap.put("processId", (String)paramMap.get("processId"));
		newMap.put("flowId", (String)paramMap.get("lastFlowId"));
		newMap.put("flowNextId", (String)paramMap.get("flowId"));
		
		//마지막 작업흐름 flowNextId - 종료 작업흐름에 연결
		prj1100DAO.updateFlw1100FlowInfo(newMap);
		
		//프로세스 수정
		prj1100DAO.updateFlw1000ProcessInfo(paramMap);
	}

	/**
	 * 프로세스 확정취소 ('종료분기' 작업흐름 데이터, 프로세스 json data)
	 * @param paramMap
	 * @throws Exception
	 */
	@SuppressWarnings("rawtypes")
	public void updateFlw1000ProcessConfirmCancle(Map paramMap) throws Exception {
		Map<String, String> newMap = new HashMap<String, String>();
		newMap.put("prjId", (String)paramMap.get("prjId"));
		newMap.put("processId", (String)paramMap.get("processId"));
		newMap.put("flowId", (String)paramMap.get("prevFlowId"));
		newMap.put("flowNextId", "null");
		
		//작업흐름 수정
		prj1100DAO.updateFlw1100FlowInfo(newMap);
		
		newMap = new HashMap<String, String>();
		newMap.put("prjId", (String)paramMap.get("prjId"));
		newMap.put("processId", (String)paramMap.get("processId"));
		newMap.put("flowId", (String)paramMap.get("endFlowId"));
		
		//작업흐름 제거
		prj1100DAO.deleteFlw1100FlowInfo(newMap);
		
		newMap.put("processConfirmCd", "01");
		newMap.put("processJsonData", (String)paramMap.get("processJsonData"));
		
		//프로세스 수정
		prj1100DAO.updateFlw1000ProcessInfo(newMap);
	}
	
	/**
	 * 프로세스 추가
	 * @param paramMap
	 * @throws Exception
	 */
	@SuppressWarnings("rawtypes")
	public String insertFlw1000ProcessInfo(Map paramMap) throws Exception {
		return (String) prj1100DAO.insertFlw1000ProcessInfo(paramMap);
	}
	
	/**
	 * 프로세스 제거
	 * @param paramMap
	 * @throws Exception
	 */
	@SuppressWarnings("rawtypes")
	public void deleteFlw1000ProcessInfo(Map paramMap) throws Exception {
		//프로세스 제거
		prj1100DAO.deleteFlw1000ProcessInfo(paramMap);
		
		//작업흐름 제거
		prj1100DAO.deleteFlw1100FlowInfo(paramMap);
		
		//추가항목 제거
		prj1100DAO.deleteFlw1200OtpInfo(paramMap);
	}
	
	/**
	 * 프로세스에 배정된 요구사항 수
	 * @param paramMap
	 * @throws Exception
	 */
	@SuppressWarnings("rawtypes")
	public int selectFlw1000ProcessReqCnt(Map paramMap) throws Exception {
		return prj1100DAO.selectFlw1000ProcessReqCnt(paramMap);
	}
	
	/**
	 * 작업흐름 조회 
	 * @param paramMap
	 * @throws Exception
	 */
	@SuppressWarnings("rawtypes")
	public List selectFlw1100FlowList(Map paramMap) throws Exception {
		return prj1100DAO.selectFlw1100FlowList(paramMap);
	}
	
	/**
	 * 작업흐름 수정
	 * @param paramMap
	 * @throws Exception
	 */
	@SuppressWarnings("rawtypes")
	public void updateFlw1100FlowInfo(Map paramMap) throws Exception {
		//작업흐름 수정
		prj1100DAO.updateFlw1100FlowInfo(paramMap);
		
		//프로세스 JsonData수정
		prj1100DAO.updateFlw1000ProcessInfo(paramMap);
	}
	
	
	/**
	 * 작업흐름  추가
	 * @param paramMap
	 * @throws Exception
	 */
	@SuppressWarnings("rawtypes")
	public void insertFlw1100FlowInfo(Map paramMap) throws Exception {
		//작업흐름 추가
		prj1100DAO.insertFlw1100FlowInfo(paramMap);
		
		//프로세스 JsonData수정
		prj1100DAO.updateFlw1000ProcessInfo(paramMap);
	}
	
	/**
	 * 작업흐름 제거
	 * @param paramMap
	 * @throws Exception
	 */
	@SuppressWarnings("rawtypes")
	public void deleteFlw1100FlowInfo(Map paramMap) throws Exception {
		//작업흐름 제거
		prj1100DAO.deleteFlw1100FlowInfo(paramMap);
		
		//해당하는 추가 항목 제거
		prj1100DAO.deleteFlw1200OtpInfo(paramMap);
		
		//프로세스 JsonData수정
		prj1100DAO.updateFlw1000ProcessInfo(paramMap);
	}
	
	/**
	 * 선택 요구사항에 해당하는 추가 항목 목록
	 * @param paramMap
	 * @throws Exception
	 */
	@SuppressWarnings("rawtypes")
	public List selectFlw1200ReqOptList(Map paramMap) throws Exception {
		return prj1100DAO.selectFlw1200ReqOptList(paramMap);
	}
	
	/**
	 * 선택 작업흐름에 해당하는 추가 항목 목록
	 * @param paramMap
	 * @throws Exception
	 */
	@SuppressWarnings("rawtypes")
	public List selectFlw1200OptList(Map paramMap) throws Exception {
		return prj1100DAO.selectFlw1200OptList(paramMap);
	}

	/**
	 * 선택 작업흐름에 해당하는 추가 항목 목록
	 * @param paramMap
	 * @throws Exception
	 */
	@SuppressWarnings("rawtypes")
	public List selectFlw1300OptFileList(Map paramMap) throws Exception {
		return prj1100DAO.selectFlw1300OptFileList(paramMap);
	}
	
	/**
	 * 해당 항목에 값이 이미 추가되어있는지 확인 
	 * @param paramMap
	 * @throws Exception
	 */
	@SuppressWarnings("rawtypes")
	public int selectFlw1200OptCntInfo(Map paramMap) throws Exception {
		 return prj1100DAO.selectFlw1200OptCntInfo(paramMap);
	}
	
	/**
	 * 작업흐름 추가항목 수정
	 * @param paramMap
	 * @throws Exception
	 */
	@SuppressWarnings("rawtypes")
	public void updateFlw1200OtpInfo(Map paramMap) throws Exception {
		prj1100DAO.updateFlw1200OtpInfo(paramMap);
	}
	
	/**
	 * 작업흐름 추가항목 추가
	 * @param paramMap
	 * @throws Exception
	 */
	@SuppressWarnings({ "rawtypes" })
	public void insertFlw1200OtpInfo(Map paramMap) throws Exception {
		//추가 항목 추가
		prj1100DAO.insertFlw1200OtpInfo(paramMap);
		
		//프로세스 JsonData수정
		prj1100DAO.updateFlw1000ProcessInfo(paramMap);
	}
	
	/**
	 * 작업흐름 추가항목 제거
	 * @param paramMap
	 * @throws Exception
	 */
	@SuppressWarnings("rawtypes")
	public void deleteFlw1200OtpInfo(Map paramMap) throws Exception {
		//추가 항목 제거
		prj1100DAO.deleteFlw1200OtpInfo(paramMap);
		
		//프로세스 JsonData수정
		prj1100DAO.updateFlw1000ProcessInfo(paramMap);
	}
	
	/**
	 * 작업흐름 추가 항목 입력
	 * @param paramMap
	 * @throws Exception
	 */
	@SuppressWarnings("rawtypes")
	public void insertFlw1300OtpValueInfo(Map paramMap) throws Exception {
		prj1100DAO.insertFlw1300OtpValueInfo(paramMap);
	}
	
	/**
	 * 작업흐름 추가 항목 수정
	 * @param paramMap
	 * @throws Exception
	 */
	@SuppressWarnings("rawtypes")
	public void updateFlw1300OtpValueInfo(Map paramMap) throws Exception {
		prj1100DAO.updateFlw1300OtpValueInfo(paramMap);
	}
	

	/**
	 * 요구사항 리비전 번호 목록 저장
	 * @param paramMap
	 * @throws Exception
	 */
	@SuppressWarnings({ "rawtypes", "unchecked" })
	public int insertFlw1400RevisionNumList(Map paramMap) throws Exception {
		int selRepNumCnt = Integer.parseInt((String)paramMap.get("selRepNumCnt"));
		
		//추가 실패된 갯수
		int addFailRepNumCnt = 0;
		
		if(selRepNumCnt > 1){
			List<String> selRepNumList = (List<String>) paramMap.get("selRepNum");
			
			for(String selRepNumInfo : selRepNumList){
				Map newMap = new HashMap<String, String>();
				
				JSONObject jsonObj = null;
				jsonObj = new JSONObject(selRepNumInfo);
				
				newMap.put("licGrpId", String.valueOf(paramMap.get("licGrpId")));
				newMap.put("prjId", String.valueOf(paramMap.get("prjId")));
				newMap.put("reqId", String.valueOf(paramMap.get("reqId")));
				newMap.put("processId", String.valueOf(paramMap.get("processId")));
				newMap.put("flowId", String.valueOf(paramMap.get("flowId")));
				newMap.put("svnRepId", String.valueOf(jsonObj.get("svnRepId")));
				newMap.put("revisionNum", String.valueOf(jsonObj.get("revisionNum")));
				newMap.put("revisionComment", String.valueOf(jsonObj.get("revisionComment")));
				
				//리비전 레코드 존재하는지 체크
				int reqRepCnt = prj1100DAO.selectFlw1400ReqRevisionNumCnt(newMap);
				
				//레코드 없는경우 insert
				if(reqRepCnt == 0){
					prj1100DAO.insertFlw1400RevisionNumInfo(newMap);
				}else{
					addFailRepNumCnt++;
				}
			}
		}else{
			String selRepNumInfo = (String) paramMap.get("selRepNum");
			
			Map newMap = new HashMap<String, String>();
			
			JSONObject jsonObj = null;
			jsonObj = new JSONObject(selRepNumInfo);
			
			newMap.put("licGrpId", String.valueOf(paramMap.get("licGrpId")));
			newMap.put("prjId", String.valueOf(paramMap.get("prjId")));
			newMap.put("reqId", String.valueOf(paramMap.get("reqId")));
			newMap.put("processId", String.valueOf(paramMap.get("processId")));
			newMap.put("flowId", String.valueOf(paramMap.get("flowId")));
			newMap.put("svnRepId", String.valueOf(jsonObj.get("svnRepId")));
			newMap.put("revisionNum", String.valueOf(jsonObj.get("revisionNum")));
			newMap.put("revisionComment", String.valueOf(jsonObj.get("revisionComment")));
			
			//리비전 레코드 존재하는지 체크
			int reqRepCnt = prj1100DAO.selectFlw1400ReqRevisionNumCnt(newMap);
			
			//레코드 없는경우 insert
			if(reqRepCnt == 0){
				prj1100DAO.insertFlw1400RevisionNumInfo(newMap);
			}else{
				addFailRepNumCnt++;
			}
		}
		
		return addFailRepNumCnt;
	}
	
	/**
	 * 요구사항 리비전 번호 목록 제거
	 * @param paramMap
	 * @throws Exception
	 */
	@SuppressWarnings({ "rawtypes", "unchecked" })
	public void deleteFlw1400RevisionNumList(Map paramMap) throws Exception {
		int selRepNumCnt = Integer.parseInt((String)paramMap.get("selRepNumCnt"));
		
		if(selRepNumCnt > 1){
			List<String> selRepNumList = (List<String>) paramMap.get("selRepNum");
			
			for(String selRepNumInfo : selRepNumList){
				Map newMap = new HashMap<String, String>();
				
				JSONObject jsonObj = null;
				jsonObj = new JSONObject(selRepNumInfo);
				
				newMap.put("prjId", String.valueOf(paramMap.get("prjId")));
				newMap.put("reqId", String.valueOf(paramMap.get("reqId")));
				newMap.put("processId", String.valueOf(paramMap.get("processId")));
				newMap.put("flowId", String.valueOf(paramMap.get("flowId")));
				newMap.put("svnRepId", String.valueOf(jsonObj.get("svnRepId")));
				newMap.put("revisionNum", String.valueOf(jsonObj.get("revisionNum")));
				
				//리비전 제거
				prj1100DAO.deleteFlw1400RevisionNumInfo(newMap);
			}
		}else{
			String selRepNumInfo = (String) paramMap.get("selRepNum");
			
			Map newMap = new HashMap<String, String>();
			
			JSONObject jsonObj = null;
			jsonObj = new JSONObject(selRepNumInfo);
			
			newMap.put("prjId", String.valueOf(paramMap.get("prjId")));
			newMap.put("reqId", String.valueOf(paramMap.get("reqId")));
			newMap.put("processId", String.valueOf(paramMap.get("processId")));
			newMap.put("flowId", String.valueOf(paramMap.get("flowId")));
			newMap.put("svnRepId", String.valueOf(jsonObj.get("svnRepId")));
			newMap.put("revisionNum", String.valueOf(jsonObj.get("revisionNum")));
			
			//리비전 제거
			prj1100DAO.deleteFlw1400RevisionNumInfo(newMap);
		}
	}
	
	/**
	 * 요구사항 리비전 번호 단건 제거
	 * @param paramMap
	 * @throws Exception
	 */
	@SuppressWarnings("rawtypes")
	public void deleteFlw1400RevisionNumInfo(Map paramMap) throws Exception {
		prj1100DAO.deleteFlw1400RevisionNumInfo(paramMap);
	}
	
	/**
	 * 요구사항별 리비전 목록 가져오기(Grid page)
	 * @param Prj1100VO
	 * @throws Exception
	 */
	@SuppressWarnings("rawtypes")
	public List selectFlw1400ReqRevisionNumList(Prj1100VO prj1100VO) throws Exception {
		return prj1100DAO.selectFlw1400ReqRevisionNumList(prj1100VO);
	}
	
	/**
	 * 요구사항별 리비전 목록 총 건수(Grid page)
	 * @param Prj1100VO
	 * @return 
	 * @exception Exception
	 */
	public int selectFlw1400ReqRevisionNumListCnt(Prj1100VO prj1100VO) throws Exception {
		return prj1100DAO.selectFlw1400ReqRevisionNumListCnt(prj1100VO);
	}

	/**
	 * 요구사항별 리비전 단건 갯수 가져오기
	 * @param paramMap
	 * @throws Exception
	 */
	@SuppressWarnings("rawtypes")
	public int selectFlw1400ReqRevisionNumCnt(Map paramMap) throws Exception {
		return prj1100DAO.selectFlw1400ReqRevisionNumCnt(paramMap);
	}

	/**
	 * 작업흐름별 역할제한 정보 목록 저장
	 * @param paramMap
	 * @throws Exception
	 */
	@SuppressWarnings({ "rawtypes", "unchecked" })
	public int insertFlw1500FlowAuthGrpList(Map paramMap) throws Exception {
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
				newMap.put("processId", String.valueOf(paramMap.get("processId")));
				newMap.put("flowId", String.valueOf(paramMap.get("flowId")));
				newMap.put("authGrpId", String.valueOf(jsonObj.get("authGrpId")));
				newMap.put("authGrpTargetCd", String.valueOf(jsonObj.get("authGrpTargetCd")));
				
				//레코드 존재하는지 체크
				int flowAuthCnt = prj1100DAO.selectFlw1500FlowAuthGrpCnt(newMap);
				
				//레코드 없는경우 insert
				if(flowAuthCnt == 0){
					prj1100DAO.insertFlw1500FlowAuthGrpInfo(newMap);
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
			newMap.put("processId", String.valueOf(paramMap.get("processId")));
			newMap.put("flowId", String.valueOf(paramMap.get("flowId")));
			newMap.put("authGrpId", String.valueOf(jsonObj.get("authGrpId")));
			newMap.put("authGrpTargetCd", String.valueOf(jsonObj.get("authGrpTargetCd")));
			
			//레코드 존재하는지 체크
			int reqRepCnt = prj1100DAO.selectFlw1500FlowAuthGrpCnt(newMap);
			
			//레코드 없는경우 insert
			if(reqRepCnt == 0){
				prj1100DAO.insertFlw1500FlowAuthGrpInfo(newMap);
			}else{
				addFailAuthCnt++;
			}
		}
		
		return addFailAuthCnt;
	}
	
	/**
	 * 작업흐름별 역할제한 정보 제거
	 * @param paramMap
	 * @throws Exception
	 */
	@SuppressWarnings({ "rawtypes", "unchecked" })
	public void deleteFlw1500FlowAuthGrpList(Map paramMap) throws Exception {
		int selAuthCnt = Integer.parseInt((String)paramMap.get("selAuthCnt"));
		
		if(selAuthCnt > 1){
			List<String> selAuthList = (List<String>) paramMap.get("selAuth");
			
			for(String selAuthInfo : selAuthList){
				Map newMap = new HashMap<String, String>();
				
				JSONObject jsonObj = null;
				jsonObj = new JSONObject(selAuthInfo);
				
				newMap.put("prjId", String.valueOf(paramMap.get("prjId")));
				newMap.put("processId", String.valueOf(paramMap.get("processId")));
				newMap.put("flowId", String.valueOf(paramMap.get("flowId")));
				newMap.put("authGrpId", String.valueOf(jsonObj.get("authGrpId")));
				newMap.put("authGrpTargetCd", String.valueOf(jsonObj.get("authGrpTargetCd")));
				
				//제거
				prj1100DAO.deleteFlw1500FlowAuthGrpInfo(newMap);
			}
		}else{
			String selAuthInfo = (String) paramMap.get("selAuth");
			
			Map newMap = new HashMap<String, String>();
			
			JSONObject jsonObj = null;
			jsonObj = new JSONObject(selAuthInfo);
			
			newMap.put("prjId", String.valueOf(paramMap.get("prjId")));
			newMap.put("processId", String.valueOf(paramMap.get("processId")));
			newMap.put("flowId", String.valueOf(paramMap.get("flowId")));
			newMap.put("authGrpId", String.valueOf(jsonObj.get("authGrpId")));
			newMap.put("authGrpTargetCd", String.valueOf(jsonObj.get("authGrpTargetCd")));
			
			//제거
			prj1100DAO.deleteFlw1500FlowAuthGrpInfo(newMap);
		}
	}
	
	/**
	 * 작업흐름별 역할제한 정보 저장
	 * @param paramMap
	 * @throws Exception
	 */
	@SuppressWarnings("rawtypes")
	public void insertFlw1500FlowAuthGrpInfo(Map paramMap) throws Exception {
		prj1100DAO.insertFlw1500FlowAuthGrpInfo(paramMap);
	}
	/**
	 * 작업흐름별 역할제한 정보 단건 제거
	 * @param paramMap
	 * @throws Exception
	 */
	@SuppressWarnings("rawtypes")
	public void deleteFlw1500FlowAuthGrpInfo(Map paramMap) throws Exception {
		prj1100DAO.deleteFlw1500FlowAuthGrpInfo(paramMap);
	}
	
	/**
	 * 작업흐름별 역할제한 정보 목록 가져오기
	 * @param paramMap
	 * @throws Exception
	 */
	@SuppressWarnings("rawtypes")
	public List selectFlw1500FlowAuthGrpList(Map paramMap) throws Exception {
		return prj1100DAO.selectFlw1500FlowAuthGrpList(paramMap);
	}
	
	/**
	 * 작업흐름별 역할제한 정보 단건 갯수 가져오기
	 * @param paramMap
	 * @throws Exception
	 */
	@SuppressWarnings("rawtypes")
	public int selectFlw1500FlowAuthGrpCnt(Map paramMap) throws Exception {
		return prj1100DAO.selectFlw1500FlowAuthGrpCnt(paramMap);
	}

	/**
	 * [프로세스 복사] 관리자 권한을 가지고있는 프로젝트의 프로세스 목록
	 * @param paramMap
	 * @throws Exception
	 */
	@SuppressWarnings("rawtypes")
	public List selectFlw1000ProcessCopyList(Map paramMap) throws Exception {
		return prj1100DAO.selectFlw1000ProcessCopyList(paramMap);
	}
	
	/**
	 * [프로세스 복사] prjId, processId로 프로세스 복사 
	 * @param paramMap
	 * @throws Exception
	 */
	@SuppressWarnings({ "rawtypes", "unchecked" })
	public void insertPrj1100ProcessCopyInfo(Map paramMap) throws Exception{
		//작업흐름 목록 가져오기
		List<Map> flowList = prj1100DAO.selectFlw1100FlowList(paramMap);

		//추가항목 목록 가져오기
		List<Map> flowOptList = prj1100DAO.selectFlw1200OptList(paramMap);
		
		//허용 역할 목록 가져오기
		List<Map> flowAuthGrpList = prj1100DAO.selectFlw1500FlowAuthGrpList(paramMap);
				
		paramMap.remove("prjId");
		paramMap.put("prjId", paramMap.get("selPrjId"));
		
		//프로세스 추가하기
		paramMap.put("processConfirmCd", "02");
		String processJsonData = (String) paramMap.get("processJsonData");
		paramMap.remove("processJsonData");
		
		String newProcessId = prj1100DAO.insertFlw1000ProcessInfo(paramMap);
		
		paramMap.put("processJsonData", processJsonData);
		paramMap.put("processId", newProcessId);
		
		prj1100DAO.updateFlw1000ProcessInfo(paramMap);
		
		for(Map flowInfo: flowList){
			//prjId, processId 교체
			flowInfo.remove("prjId");
			flowInfo.remove("processId");
			
			flowInfo.put("prjId", paramMap.get("prjId"));
			flowInfo.put("processId", newProcessId);
			
			//작업흐름 추가
			prj1100DAO.insertFlw1100FlowInfo(flowInfo);
		}
		

		for(Map flowOptInfo: flowOptList){
			//prjId, processId 교체
			flowOptInfo.remove("prjId");
			flowOptInfo.remove("processId");
			
			flowOptInfo.put("prjId", paramMap.get("prjId"));
			flowOptInfo.put("processId",newProcessId);
			
			//항목 추가
			prj1100DAO.insertFlw1200OtpCopyInfo(flowOptInfo);
		}
		
		
		for(Map flowAuthGrpInfo: flowAuthGrpList){
			//prjId, processId 교체
			flowAuthGrpInfo.remove("prjId");
			flowAuthGrpInfo.remove("processId");
			
			flowAuthGrpInfo.put("prjId", paramMap.get("prjId"));
			flowAuthGrpInfo.put("processId", newProcessId);
			
			//허용 권한 그룹 추가
			prj1100DAO.insertFlw1500FlowAuthGrpInfo(flowAuthGrpInfo);
		}
	}
	

	/**
	 * 추가 항목에 존재하는 첨부파일 ID 목록 조회 
	 * @param paramMap
	 * @throws Exception
	 */
	@SuppressWarnings("rawtypes")
	public List selectFlw1200FlwOptExistFileIdList(Map paramMap) throws Exception {
		return prj1100DAO.selectFlw1200FlwOptExistFileIdList(paramMap);
	}
}
