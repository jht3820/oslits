package kr.opensoftlab.oslops.req.req1000.req1000.service.impl;

/**
 * @Class Name : Req1000Service.java
 * @Description : Req1000Service Service class
 * @Modification Information
 *
 * @author 정형택
 * @since 2016.01.23.
 * @version 1.0
 * @see
 *  
 *  Copyright (C) OpenSoftLab Corp All right reserved.
 */

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import kr.opensoftlab.oslops.arm.arm1000.arm1000.service.impl.Arm1000DAO;
import kr.opensoftlab.oslops.req.req1000.req1000.service.Req1000Service;
import kr.opensoftlab.oslops.req.req1000.req1000.vo.Req1000VO;
import kr.opensoftlab.oslops.req.req4000.req4800.service.Req4800Service;
import kr.opensoftlab.sdf.excel.ExcelDataListResultHandler;
import kr.opensoftlab.sdf.util.CommonScrty;
import kr.opensoftlab.sdf.util.WebhookSend;

import org.codehaus.jettison.json.JSONException;
import org.codehaus.jettison.json.JSONObject;
import org.springframework.stereotype.Service;

import egovframework.com.cmm.EgovMessageSource;
import egovframework.com.cmm.service.EgovProperties;
import egovframework.rte.fdl.cmmn.EgovAbstractServiceImpl;
import egovframework.rte.fdl.idgnr.EgovIdGnrService;

@Service("req1000Service")
public class Req1000ServiceImpl extends EgovAbstractServiceImpl implements Req1000Service {
	
	/** DAO Bean Injection */
    @Resource(name="arm1000DAO")
    private Arm1000DAO arm1000DAO;
    
	/** DAO Bean Injection */
    @Resource(name="req1000DAO")
    private Req1000DAO req1000DAO;  
    
    @Resource(name = "egovFileIdGnrService")
	private EgovIdGnrService idgenService;
    
    /** Req4800Service DI */
	@Resource(name = "req4800Service")
	private Req4800Service req4800Service;
	
    /** EgovMessageSource */
	@Resource(name = "egovMessageSource")
	EgovMessageSource egovMessageSource;

    /** Whk1000Service DI */
    @Resource(name = "webhookSend")
    private WebhookSend webhookSend;
	
	/**
	 * jsonData로 넘겨받은 data를 String으로 변환
	 * @param paramMap
	 * @return
	 */
	@SuppressWarnings({ "rawtypes", "unchecked" })
	private Map<String, String> selectReq1000JsonToMap(Map paramMap){
		Map rtnMap = new HashMap();
		for( Object key : paramMap.keySet() ) {
			String jsonVal = "";
			try{
				jsonVal = (String) paramMap.get(key);
			}catch(ClassCastException cce){	//cast변환 오류인경우 skip
				continue;
			}
			
			JSONObject jsonObj = null;
			
			//JsonData가 아닌 기본 String으로 넘겨받은 경우 skip
			try{
				jsonObj = new JSONObject(jsonVal);
				rtnMap.put(key, jsonObj.getString("optVal"));
			}catch(JSONException jsonE){
				rtnMap.put(key, jsonVal);
			}catch(NullPointerException npe){
				rtnMap.put(key, jsonVal);
			}
		}
		return rtnMap;
	}
	
	
	/**
	 * 요구사항 목록을 조회한다.
	 * @param req1000VO
	 * @return List 로그인 이력 로그 목록
	 * @throws Exception
	 */
	public List<Req1000VO>  selectReq1000AllList(Req1000VO req1000vo) throws Exception {
		 return  req1000DAO.selectReq1000AllList(req1000vo);
	}
	
	/**
	 * 요구사항 목록을 조회한다.
	 * @param req1000VO
	 * @return List 요구사항 목록
	 * @throws Exception
	 */
	public List<Req1000VO> selectReq1000List(Req1000VO req1000vo) throws Exception {
		return req1000DAO.selectReq1000List(req1000vo);
	}
	
	/**
	 * 요구사항 정보을 조회한다.
	 * @param Map
	 * @return Map 요구사항 정보
	 * @throws Exception
	 */
	@SuppressWarnings("rawtypes")
	public Map selectReq1000ReqInfo(Map paramMap) throws Exception{
		return req1000DAO.selectReq1000ReqInfo(paramMap);
	}
	
	/**
	 * 요구사항 로그 목록 총건수를 조회한다.
	 * @param req1000VO
	 * @return  int 요구사항 목록 총건수 
	 * @throws Exception
	 */
	public int selectReq1000ListCnt(Req1000VO req1000vo) throws Exception {
		 return req1000DAO.selectReq1000ListCnt(req1000vo);
	}
	/**
	 * Req1000 요구사항 개발공수, 담당자 수정
	 * @param 
	 * @return 
	 * @exception Exception
	 */
	@SuppressWarnings({ "rawtypes" })
	public void updateReq1001ReqSubInfo(Map paramMap) throws Exception{
		req1000DAO.updateReq1001ReqSubInfo(paramMap);
	}
	
	/**
	 * Req1000 요구사항 등록(단건) AJAX
	 * 요구사항 등록후 등록 정보 결과 및 정보를 리턴한다.
	 * @param 
	 * @return 
	 * @exception Exception
	 */
	@SuppressWarnings({ "rawtypes" })
	public Object saveReq1000ReqInfo(Map paramMap) throws Exception{
		
		Map<String, String> convertParamMap = selectReq1000JsonToMap(paramMap);
		
		String popupType	= convertParamMap.get("popupType");
		String reqProType 	= convertParamMap.get("reqProType");
		String prjAcrm 		= convertParamMap.get("prjAcrm");
		
		/*****************************
		 * 웹훅 세팅 (사용가능한지 체크)
		 * -프로젝트 웹훅 세팅
		 * -현재 요구사항 정보 저장
		*******************************/
		// 프로젝트 ID를 가져온다.
		String prjId = (String)convertParamMap.get("prjId");
		
		//프로젝트 웹훅 세팅
		boolean webHookStatus = false;
		
		String whkRegUsrNm = "";
		
		try{
			webHookStatus = webhookSend.projectWebhookSetting(prjId);
			whkRegUsrNm =  (String)paramMap.get("whkRegUsrNm");
		}catch(Exception e){
			//웹혹 오류시 skip
		}
		/*************************/
		
		//구분이 등록이면
		if("insert".equals(popupType)){
			
			// 요구사항 순번,현재 날짜(YYYYMMDD) 조회
			Map ordInfoMap = req1000DAO.selectReq1000NextReqOrd(convertParamMap);
			// 요구사항 순번
			String newReqOrd = (String)ordInfoMap.get("reqOrd");
			// 현재 날짜
			String currentDt = (String)ordInfoMap.get("reqDt");
			
			// 프로젝트 약어가 있다면 '-' 를 붙여서 요구사항 순번 생성  (ex) OSLPRJ-20190901-1
			if(prjAcrm != null && !"".equals(prjAcrm)){
				newReqOrd = prjAcrm+"-"+currentDt+"-"+newReqOrd;
			}
			
			convertParamMap.put("reqOrd", newReqOrd);

			String insNewReqId = req1000DAO.insertReq1001ReqInfo(convertParamMap);
			
			//생성된 키가 없으면 튕겨냄
			if(insNewReqId == null || "".equals(insNewReqId)){
				throw new Exception(egovMessageSource.getMessage("fail.common.insert"));
			}
	
			/*
			 *  요구사항의 고유한 해시값을 등록한다. 
			 *  외부에서 URL로 요구사항 정보 요청시 등록한 요구사항 key값으로 요구사항의 정보를
			 *  조회하여 보여준다.
			 */
			
    		// 프로젝트 ID와 요구사항 ID로 요구사항 key값  SHA-256 암호화	
    		String enReqKey = CommonScrty.encryptedAria(prjId, insNewReqId);
    		
			Map<String, String> newReqMap = new HashMap<String, String>();
			// 프로젝트 ID와 요구사항 ID, 암호화 한 요구사항 키값을 Map에 세팅
			newReqMap.put("prjId", prjId);
			newReqMap.put("reqId", insNewReqId);
			newReqMap.put("reqKey", enReqKey);
			
    		// 요구사항 key값 update
    		req1000DAO.updateReq1000ReqKey(newReqMap);
			
    		/*****************************
    		 * 웹훅 메시지 내용 세팅
    		 * -웹훅 연결 가능상태인지 체크
    		*******************************/
    		if(webHookStatus){
    			//현재 요구사항 정보
    			Map webhookBeforeReqInfo = req1000DAO.selectReq1000ReqInfo(newReqMap);

    			//프로젝트 url값 가져오기
    			String projectUrl = EgovProperties.getProperty("Globals.project.url");
    			
    			// 요구사항 설명의 </br>태그 치환
    			// 태그 치환하지 않을 시 잔디 메시지에서 </br>태그가 그대로 표시됨
    			String reqDescStr = (String) webhookBeforeReqInfo.get("reqDesc");
    			reqDescStr = reqDescStr.replace("</br>", "\n");
    			
    			//추가 정보 입력
    			webhookSend.setConnectInfo("프로젝트",  (String) webhookBeforeReqInfo.get("prjNm"));
    			webhookSend.setConnectInfo("요청 명",  (String) webhookBeforeReqInfo.get("reqNm"));
    			webhookSend.setConnectInfo("요청 내용",  reqDescStr);
    			
    			//요구사항 접수등록
    			String messasgeJson = webhookSend.messageJsonSetting(whkRegUsrNm+"님이 요구사항 ["+newReqOrd+"]("+projectUrl+"/data/req/"+enReqKey+")(을)를 생성했습니다.","#4b73eb");
    			webhookSend.prjSend(messasgeJson, "whkNewInsertCd");
    		}
			return insNewReqId;
		}
		//구분이 수정이면
		else if("update".equals(popupType)){
			
			// 수정일 때 처리유형이 접수요청(01)이 아닐경우 튕겨냄
			if(!"01".equals(reqProType)){
				throw new Exception(egovMessageSource.getMessage("fail.common.update"));
			}
			//수정이력 정보 생성
			req4800Service.insertReq4800ModifyHistoryList(paramMap);
			
			//정보 수정
			int uptCnt = req1000DAO.updateReq1001ReqInfo(convertParamMap);
			
			//수정된 건이 없으면 튕겨냄
			if(uptCnt == 0 ){
				throw new Exception(egovMessageSource.getMessage("fail.common.update"));
			}

			/*****************************
    		 * 웹훅 메시지 내용 세팅
    		 * -웹훅 연결 가능상태인지 체크
    		*******************************/
    		if(webHookStatus){
    			//현재 요구사항 정보
    			Map webhookBeforeReqInfo = req1000DAO.selectReq1000ReqInfo(paramMap);

    			//프로젝트 url값 가져오기
    			String projectUrl = EgovProperties.getProperty("Globals.project.url");
    			
    			// 요구사항 설명의 </br>태그 치환
    			// 태그 치환하지 않을 시 잔디 메시지에서 </br>태그가 그대로 표시됨
    			String reqDescStr = (String) webhookBeforeReqInfo.get("reqDesc");
    			reqDescStr = reqDescStr.replace("</br>", "\n");
    			
    			//추가 정보 입력
    			webhookSend.setConnectInfo("프로젝트",  (String) webhookBeforeReqInfo.get("prjNm"));
    			webhookSend.setConnectInfo("요청 명",  (String) webhookBeforeReqInfo.get("reqNm"));
    			webhookSend.setConnectInfo("요청 내용",  reqDescStr);
    			
    			//요구사항 접수등록
    			String messasgeJson = webhookSend.messageJsonSetting(whkRegUsrNm+"님이 요구사항 ["+webhookBeforeReqInfo.get("reqOrd")+"]("+projectUrl+"/data/req/"+webhookBeforeReqInfo.get("reqKey")+")(을)를 수정했습니다.","#4b73eb");
    			webhookSend.prjSend(messasgeJson, "whkReqUpdateCd");
    		}
    		/*****************************/
			return uptCnt;
		}

		return false;
	}

	@Override
	public void selectReq1000ExcelList(Req1000VO req1000vo,
			ExcelDataListResultHandler resultHandler) throws Exception{
		req1000DAO.selectReq1000ExcelList(req1000vo, resultHandler);
		
	}
	
	/**
	 * Req1000 요구사항 삭제(여러건) AJAX
	 * 요구사항 등록후 등록 정보 결과 및 정보를 리턴한다.
	 * @param 
	 * @return 
	 * @exception Exception
	 */
	@SuppressWarnings({ "unchecked", "rawtypes" })
	@Override
	public void deleteReq1000ReqInfo(Map<String, Object> paramMap)  throws Exception{
		
		String prjId = (String)paramMap.get("prjId");
		
		/*****************************
		 * 웹훅 세팅 (사용가능한지 체크)
		 * -프로젝트 웹훅 세팅
		 * -삭제 처리된 요구사항 목록
		*******************************/
		//프로젝트 웹훅 세팅
		boolean webHookStatus = false;
		String whkRegUsrNm = "";
		String deleteReqMessage = "";
		int deleteReqMessageCnt = 0;
		String prjNm = "";
		String projectUrl = "";
		
		try{
			webHookStatus = webhookSend.projectWebhookSetting(prjId);
			whkRegUsrNm =  (String)paramMap.get("whkRegUsrNm");
			
			//프로젝트 url값 가져오기
			projectUrl = EgovProperties.getProperty("Globals.project.url");
		}catch(Exception e){
			//웹혹 오류시 skip
		}
		/*************************/
		
		List<Map<String,String>> list = (List<Map<String,String>>) paramMap.get("list");
		
		int listSize = list.size();
		
		for (int i = 0; i < listSize; i++) {
			Map<String,String> reqMap = list.get(i);
			reqMap.put("prjId", prjId);
			
			// 요구사항 처리유형
			String reqProType = reqMap.get("reqProType");
			// 요구사항 첨부파일 ID
			String atchFileId = reqMap.get("atchFileId");
			
			// 삭제하려는 요구사항의 처리 유형이 접수대기(01)이 아니면 튕겨낸다.
			if( !"01".equals(reqProType) ){
				throw new Exception(egovMessageSource.getMessage("req1000.canNotDeleted.ReceptionType"));
			}
			
			/****************
			 * 프로젝트 웹훅 - 건수 체크
			 ****************/
			//프로젝트 웹훅 동작시에만 처리
			if(webHookStatus && deleteReqMessageCnt < 11){
				// 현재 요구사항 정보
				Map  webhookBeforeReqInfo = req1000DAO.selectReq1000ReqInfo(reqMap);
				
				//프로젝트명 얻기
				if(i == 0){
					prjNm = (String)webhookBeforeReqInfo.get("prjNm");
				}
				
				deleteReqMessage += "["+webhookBeforeReqInfo.get("reqOrd")+"] "+webhookBeforeReqInfo.get("reqNm")+"\n";
				deleteReqMessageCnt++;
			}
			/****************/
			
			
			// 요구사항 삭제
			req1000DAO.deleteReq1001ReqInfo(reqMap);
			
			// 해당 요구사항의 수정이력 삭제
			req4800Service.deleteReq4800ReqHistoryInfo(reqMap);
			
			// 해당 요구사항의 첨부파일이 있을경우
			if( atchFileId != null && !"".equals(atchFileId) ){
				reqMap.put("atchFileId", atchFileId);
				
				// 요구사항의 첨부파일 정보 삭제
				req1000DAO.deleteReq1000ReqAtchFile(reqMap);
				// 요구사항의 첨부파일 상세정보 삭제
				req1000DAO.deleteReq1000ReqAtchFileDetail(reqMap);
			}
		}
		
		/*****************************
		 * 웹훅 메시지 내용 세팅
		 * -웹훅 연결 가능상태인지 체크
		*******************************/
		if(webHookStatus){
			//추가 정보 입력
			webhookSend.setConnectInfo("프로젝트",  prjNm);
			webhookSend.setConnectInfo("삭제된 요구사항 (11건 이상 생략)",  deleteReqMessage);
			
			//요구사항 접수등록
			String messasgeJson = webhookSend.messageJsonSetting(whkRegUsrNm+"님이 요구사항 "+listSize+"건을 삭제했습니다.","#4b73eb");
			webhookSend.prjSend(messasgeJson, "whkReqDeleteCd");
		}
		/*****************************/
	}
	
	
	/**
	 * Req1000 요구사항 요청자 정보 조회 - 소속명, 이메일, 연락처
	 * @param  param - Map
	 * @return
	 * @throws Exception
	 */
	@SuppressWarnings("rawtypes")
	@Override
	public Map selectReq1000ReqUserInfo(Map paramMap) throws Exception {
		return	req1000DAO.selectReq1000ReqUserInfo(paramMap);
	}
	
	
	/**
	 * Req1000 현재 요구사항이 속한 프로젝트명, 프로젝트 약어 조회
	 * @param  param - Map
	 * @return 체계명
	 * @throws Exception
	 */
	@SuppressWarnings("rawtypes")
	public Map selectReq1000ReqPrjInfo(Map paramMap) throws Exception {
		return	req1000DAO.selectReq1000ReqPrjInfo(paramMap);
	}
	
	/**
	 * 톱합대시보드의 요구사항 접수대기 목록을 조회한다.
	 * @param paramMap
	 * @return List 접수대기 요구사항 목록
	 * @throws Exception
	 */
	@SuppressWarnings("rawtypes")
	public List<Map> selectReq1000IntegratedDshAcceptReqList(Map paramMap) throws Exception {
		return req1000DAO.selectReq1000IntegratedDshAcceptReqList(paramMap);
	}
	
	/**
	 * 통합대시보드의 요구사항 접수대기 목록 총 건수를 조회한다.
	 * @param paramMap
	 * @return int 접수대기 요구사항 목록 총 건수
	 * @throws Exception
	 */
	@SuppressWarnings("rawtypes")
	public int selectReq1000IntegratedDshAcceptReqListCnt(Map paramMap) throws Exception {
		 return req1000DAO.selectReq1000IntegratedDshAcceptReqListCnt(paramMap);
	} 
}
