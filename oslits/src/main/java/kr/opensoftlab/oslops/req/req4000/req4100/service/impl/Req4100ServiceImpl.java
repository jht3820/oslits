package kr.opensoftlab.oslops.req.req4000.req4100.service.impl;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import org.codehaus.jettison.json.JSONException;
import org.codehaus.jettison.json.JSONObject;
import org.json.simple.JSONArray;
import org.json.simple.parser.JSONParser;
import org.springframework.stereotype.Service;

import com.fasterxml.jackson.databind.ObjectMapper;

import egovframework.com.cmm.EgovMessageSource;
import egovframework.com.cmm.service.EgovProperties;
import egovframework.rte.fdl.cmmn.EgovAbstractServiceImpl;
import egovframework.rte.fdl.idgnr.EgovIdGnrService;
import kr.opensoftlab.oslops.adm.adm2000.adm2000.service.impl.Adm2000DAO;
import kr.opensoftlab.oslops.adm.adm6000.adm6000.service.Adm6000Service;
import kr.opensoftlab.oslops.arm.arm1000.arm1000.service.impl.Arm1000DAO;
import kr.opensoftlab.oslops.com.exception.UserDefineException;
import kr.opensoftlab.oslops.com.fms.web.service.FileMngService;
import kr.opensoftlab.oslops.dpl.dpl1000.dpl1000.service.impl.Dpl1000DAO;
import kr.opensoftlab.oslops.dpl.dpl1000.dpl1100.service.impl.Dpl1100DAO;
import kr.opensoftlab.oslops.prj.prj1000.prj1100.service.impl.Prj1100DAO;
import kr.opensoftlab.oslops.req.req1000.req1000.service.impl.Req1000DAO;
import kr.opensoftlab.oslops.req.req4000.req4100.service.Req4100Service;
import kr.opensoftlab.oslops.req.req4000.req4100.vo.Req4100VO;
import kr.opensoftlab.oslops.req.req4000.req4800.service.Req4800Service;
import kr.opensoftlab.oslops.req.req4000.req4900.service.impl.Req4900DAO;
import kr.opensoftlab.oslops.req.req4700.req4700.service.impl.Req4700DAO;
import kr.opensoftlab.sdf.excel.ExcelDataListResultHandler;
import kr.opensoftlab.sdf.util.CommonScrty;
import kr.opensoftlab.sdf.util.ReqHistoryMngUtil;
import kr.opensoftlab.sdf.util.WebhookSend;

/**
 * @Class Name : Req4100ServiceImpl.java
 * @Description : Req4100ServiceImpl Business Implement class
 * @Modification Information
 *
 * @author 정형택
 * @since 2016.01.12.
 * @version 1.0
 * @see
 *  
 *  Copyright (C) OpenSoftLab Corp. All right reserved.
 */

@Service("req4100Service")
public class Req4100ServiceImpl extends EgovAbstractServiceImpl implements Req4100Service {
	/** DAO Bean Injection */
    @Resource(name="arm1000DAO")
    private Arm1000DAO arm1000DAO;
    
	/** DAO Bean Injection */
    @Resource(name="adm2000DAO")
    private Adm2000DAO adm2000DAO;
    
	/** Dpl1000DAO DI */
    @Resource(name="dpl1000DAO")
    private Dpl1000DAO dpl1000DAO;
    
    /** DAO Bean Injection */
    @Resource(name="dpl1100DAO")
    private Dpl1100DAO dpl1100DAO;
    
    /** Req4100DAO DI */
    @Resource(name="req4100DAO")
    private Req4100DAO req4100DAO;
    
    /** Req4700DAO DI */
    @Resource(name="req4700DAO")
    private Req4700DAO req4700DAO;
    
    /** Req4900DAO DI */
    @Resource(name="req4900DAO")
    private Req4900DAO req4900DAO;
    
    /** Req1000DAO DI */
    @Resource(name="req1000DAO")
    private Req1000DAO req1000DAO;
    
    /** Prj1100DAO DI */
    @Resource(name="prj1100DAO")
    private Prj1100DAO prj1100DAO;
    
	/** Adm6000Service DI */
	@Resource(name = "adm6000Service")
	private Adm6000Service adm6000Service;
	
	/** Req4800Service DI */
	@Resource(name = "req4800Service")
	private Req4800Service req4800Service;
	
	/** FileMngService */
   	@Resource(name="fileMngService")
   	private FileMngService fileMngService;
   	
   	@Resource(name = "egovFileIdGnrService")
	private EgovIdGnrService idgenService;
   	
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
	private Map<String, String> selectReq4100JsonToMap(Map paramMap){
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
	 * Req4100 요구사항 일괄저장 (엑셀 업로드시 일괄저장)
	 * @param param - Map
	 * @param param - prjId 프로젝트 id
	 * @return 
	 * @exception Exception
	 */
	@SuppressWarnings({ "unchecked", "rawtypes" })
	@Override
	public void insertReq4100ReqInfoListAjax(Map<String, String> paramMap, Map prjInfo, ReqHistoryMngUtil historyMng) throws Exception {
		//JsonData있는 경우 String으로 변환
		paramMap = selectReq4100JsonToMap(paramMap);

		//service에 넘길 List Data
		List<Map<String, String>> reqDatas = new ArrayList();
				
		//넘겨 받은 JSON 데이터
		String jsonData = paramMap.get("jsonData");
		
		//JSON파서 선언
		JSONParser jsonParser = new JSONParser();
		
		// 프로젝트 ID
		String prjId = (String)prjInfo.get("prjId");
		// 프로젝트 약어
		String prjAcrm = (String)prjInfo.get("prjAcrm");
		
		//넘겨 받은 데이터 JSON Array로 파싱
		JSONArray jsonArray = (JSONArray) jsonParser.parse(jsonData);
		for(int i=0; i<jsonArray.size(); i++){
			org.json.simple.JSONObject token = (org.json.simple.JSONObject) jsonArray.get(i);
			
			//JSON to Object
			HashMap<String, String> tokenObj = new ObjectMapper().readValue(token.toString(), HashMap.class) ;
			
			reqDatas.add(tokenObj);
		}
		
		for(Map<String, String> data : reqDatas){
			
			data.put("licGrpId",paramMap.get("licGrpId"));
			data.put("regUsrId",paramMap.get("regUsrId"));
			data.put("regUsrIp",paramMap.get("regUsrIp"));
			data.put("modifyUsrId",paramMap.get("modifyUsrId"));
			data.put("modifyUsrIp",paramMap.get("modifyUsrIp"));
			data.put("prjId", prjId);
			
			// 등록이므로 처리유형은 접수요청(01)
			data.put("reqProType","01");

			// 접수유형
			String reqNewType = data.get("reqNewType");
			
			// 요청자 정보 직접입력 여부
			String reqInputType = data.get("reqInputType");
			
			// 접수유형이 게시판이 아니고, 요청자 정보 직접입력여부가 N일 경우
			if( !"05".equals(reqNewType) && "N".equals(reqInputType.toUpperCase()) ){
				
				String reqUsrEmail = data.get("reqUsrEmail");
				String reqUsrNum = data.get("reqUsrNum");
				String reqUsrPositionNm = data.get("reqUsrPositionNm");
				String reqUsrDutyNm = data.get("reqUsrDutyNm");
				
				// 요청자 정보 조회용 데이터 세팅
				paramMap.put("reqUsrId", data.get("reqUsrId"));
				paramMap.put("reqUsrEmail", reqUsrEmail);
				paramMap.put("reqUsrNum", reqUsrNum);
				
				// 요청자 정보 조회
				Map userInfo = req4100DAO.selectReq4100ReqUserInfo(paramMap);
				
				// 요청자 정보가 null 이면 튕겨냄 (잘못된 사용자 ID 입력됨)
				if(userInfo == null){
					throw new Exception(egovMessageSource.getMessage("req4100.notFoundUsr"));
				}
				
				// 조회된 요청자 정보 값 세팅
				data.put("reqUsrNm", userInfo.get("usrNm").toString());
				data.put("reqUsrDeptNm", userInfo.get("deptNm").toString());
				
				// 엑셀파일에 이메일을 직접 입력하지 않았을 경우 
				// 조회된 요청자 정보에서 이메일 값을 가져와 세팅
				if( reqUsrEmail == null || "".equals(reqUsrEmail) ){
					data.put("reqUsrEmail", userInfo.get("email").toString());
				}
				
				// 엑셀파일에 연락처를 직접 입력하지 않았을 경우
				// 조회된 요청자 정보에서 연락처 값을 가져와 세팅
				if( reqUsrNum == null || "".equals(reqUsrNum) ){
					data.put("reqUsrNum", userInfo.get("telno").toString());
				}
				
				// 엑셀파일에 직급을 직접 입력하지 않았을 경우
				// 조회된 요청자 정보에서 직급 값을 가져와 세팅
				if( reqUsrPositionNm == null || "".equals(reqUsrPositionNm) ){
					data.put("reqUsrPositionNm", userInfo.get("usrPositionNm").toString());
				}
				
				// 엑셀파일에 직책을 입력하지 않았을 경우
				// 조회된 요청자 정보에서 직책 값을 가져와 세팅
				if( reqUsrDutyNm == null || "".equals(reqUsrDutyNm) ){
					data.put("reqUsrDutyNm", userInfo.get("usrDutyNm").toString());
				}
			}
				
			// 요구사항 순번,현재 날짜(YYYYMMDD) 조회
			Map ordInfoMap = req1000DAO.selectReq1000NextReqOrd(data);
			// 요구사항 순번
			String newReqOrd = (String)ordInfoMap.get("reqOrd");
			// 현재 날짜
			String currentDt = (String)ordInfoMap.get("reqDt");
			
			// 프로젝트 약어가 있다면 '-' 를 붙여서 요구사항 순번 생성 (ex) OSLPRJ-20190901-1
			if(prjAcrm != null && !"".equals(prjAcrm)){
				newReqOrd = prjAcrm+"-"+currentDt+"-"+newReqOrd;
			}
			
			data.put("reqOrd", newReqOrd);

			//atch_file_id 강제 생성
			String atchFileIdString = idgenService.getNextStringId();
			fileMngService.insertFileMasterInfo(atchFileIdString);
			data.put("atchFileId",atchFileIdString);
			
			// 엑셀 업로드 요구사항 등록
			String newReqId = req4100DAO.insertReq4100ReqInfoAjax(data);
			// 프로젝트 ID와 요구사항 ID로 요구사항 key값  SHA-256 암호화	
			String enReqKey = CommonScrty.encryptedAria(prjId, newReqId);
			
			Map<String, String> newReqMap = new HashMap<String, String>();
			// 프로젝트 ID와 요구사항 ID, 암호화 한 요구사항 키값을 Map에 세팅
			newReqMap.put("prjId", prjId);
			newReqMap.put("reqId", newReqId);
			newReqMap.put("reqKey", enReqKey);
			
			// 요구사항 key값 update
			req1000DAO.updateReq1000ReqKey(newReqMap);
			
		}
	}

	/**
	 * Req4100 요구사항 목록 조회
	 * @param req4100vo
	 * @return List<Req4100VO> 요구사항 목록
	 * @exception Exception
	 */
	@Override
	public List<Req4100VO> selectReq4100List(Req4100VO req4100vo)  throws Exception{
		return req4100DAO.selectReq4100List(req4100vo);
	}

	/**
	 * Req4100 요구사항 목록 총 건수 조회 : 그리드 페이징 처리
	 * @param Req4100VO
	 * @return int 요구사항 총 목록
	 * @exception Exception
	 */
	@Override
	public int selectReq4100ListCnt(Req4100VO req4100vo)  throws Exception{
		return req4100DAO.selectReq4100ListCnt(req4100vo);
	}
	
	/**
	 * Req4100 요구사항 단건 등록
	 * @param param - Map
	 * @return newReqId 요구사항 ID
	 * @exception Exception
	 */
	@SuppressWarnings({ "rawtypes" })
	@Override
	public String insertReq4100ReqInfoAjax(Map<String, String> paramMap)  throws Exception{
		
		//JsonData있는 경우 String으로 변환
		paramMap = selectReq4100JsonToMap(paramMap);
		
		// 프로젝트 약어
		String prjAcrm 	= paramMap.get("prjAcrm");

		// 요구사항 순번,현재 날짜(YYYYMMDD) 조회
		Map ordInfoMap = req1000DAO.selectReq1000NextReqOrd(paramMap);
		// 요구사항 순번
		String newReqOrd = (String)ordInfoMap.get("reqOrd");
		// 현재 날짜
		String currentDt = (String)ordInfoMap.get("reqDt");
					
		// 프로젝트 약어가 있다면 '-' 를 붙여서 요구사항 순번 생성 (ex) OSLPRJ-20190901-1
		if(prjAcrm != null && !"".equals(prjAcrm)){
			newReqOrd = prjAcrm+"-"+currentDt+"-"+newReqOrd;
		}
		
		paramMap.put("reqOrd", newReqOrd);
		
		// 신규 요구사항 등록
		String newReqId = req4100DAO.insertReq4100ReqInfoAjax(paramMap);
   		
		/*
		 *  요구사항의 고유한 해시값을 등록한다. 
		 *  외부에서 URL로 요구사항 정보 요청시 등록한 요구사항 key값으로 요구사항의 정보를
		 *  조회하여 보여준다.
		 */
		// 프로젝트 ID를 가져온다.
		String prjId = (String)paramMap.get("prjId");
		
		// 프로젝트 ID와 요구사항 ID로 요구사항 key값  SHA-256 암호화	
		String enReqKey = CommonScrty.encryptedAria(prjId, newReqId);
		
		Map<String, String> newReqMap = new HashMap<String, String>();
		// 프로젝트 ID와 요구사항 ID, 암호화 한 요구사항 키값을 Map에 세팅
		newReqMap.put("prjId", prjId);
		newReqMap.put("reqId", newReqId);
		newReqMap.put("reqKey", enReqKey);
		
		// 요구사항 key값 update
		req1000DAO.updateReq1000ReqKey(newReqMap);
		
		return newReqId;
		
	}

	@SuppressWarnings("rawtypes")
	@Override
	public Map selectReq4102ReqInfoAjax(Map<String, String> paramMap)  throws Exception{
		return req4100DAO.selectReq4102ReqInfoAjax(paramMap);
	}
	
    /**
	 * Req4100 요구사항 단건 수정
	 * @param param - Map
	 * @return newReqId 요구사항 ID
	 * @exception Exception
	 */
	@SuppressWarnings({ "unchecked", "rawtypes" })
	@Override
	public void updateReq4100ReqInfoAjax(Map paramMap)  throws Exception{
		//수정이력 정보 생성
		req4800Service.insertReq4800ModifyHistoryList(paramMap);
		
		//JsonData있는 경우 String으로 변환
		paramMap = selectReq4100JsonToMap(paramMap);
				
		//이전 담당자
		String beforeReqChargerId = (String) paramMap.get("before_reqChargerId");
				
		//현재 담당자
		String reqChargerId = (String) paramMap.get("reqChargerId");	
		
		//쪽지 기본 정보 세팅
		String reqNm = (String)paramMap.get("reqNm");
		String usrId = (String)paramMap.get("regUsrId");
		
		String prjGrpId = (String)paramMap.get("selPrjGrpId");
		String prjId = (String)paramMap.get("prjId");
		String reqId = (String)paramMap.get("reqId");
		String alarmTitle = "["+reqNm+"] 요구사항에 담당자로 지정되었습니다.";
		
		//이전 담당자가 있는 경우에만
		if(!"null".equals(beforeReqChargerId)){
			//담당자 변경됬는지 확인 && 현재 담당자가 자신이 아닐경우에만 쪽지 발송
			if(reqChargerId != null && !"".equals(reqChargerId) && !beforeReqChargerId.equals(reqChargerId) && !reqChargerId.equals(usrId)){
				//현재 담당자에게 쪽지 발송
				Map<String,String> armMap = new HashMap<String,String>();
				armMap.put("usrId", reqChargerId);
				armMap.put("sendUsrId", usrId);
				armMap.put("title", alarmTitle);
				armMap.put("content", "["+reqNm+"] 요구사항에 담당자로 지정되었습니다.<br>해당 요구사항을 확인해주세요.");
				armMap.put("reqIds", "<span name='tagReqId' id='tagReqId' prj-grp-id='"+prjGrpId+"' prj-id='"+prjId+"' req-id='"+reqId+"' onclick='fnSpanReqDetailOpen(this)'>"+reqNm+"<li class='fa fa-share'></li></span>");
				
				//쪽지 등록
				arm1000DAO.insertArm1000AlarmInfo(armMap);
			}
		}	
		
		req4100DAO.updateReq4100ReqInfoAjax(paramMap);
	}

    /**
	 * Req4100 요구사항 일괄 삭제
	 * @param param - Map
	 * @return prjId 프로젝트 ID
	 * @exception Exception
	 */
	@SuppressWarnings({ "unchecked", "rawtypes" })
	@Override
	public void deleteReq4100ReqInfoAjax(Map<String, Object> paramMap,String prjId)  throws Exception{
		
		List<Map<String,String>> list = (List<Map<String,String>>) paramMap.get("list");
		
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
		
		try{
			//프로젝트 웹훅 목록
			webHookStatus = webhookSend.projectWebhookSetting(prjId);
			whkRegUsrNm =  (String)paramMap.get("whkRegUsrNm");
			
		}catch(Exception e){
			//웹혹 오류시 skip
		}
		/*************************/
		
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
				throw new Exception(egovMessageSource.getMessage("req4100.canNotDeleted.ReceptionType"));
			}
			/****************
			 * 사용자 웹훅 - 요구사항
			 * 프로젝트 웹훅 - 건수 체크
			 ****************/
			boolean webHookUsrStatus = false;
			Map webhookBeforeReqInfo = null;
			
			try{
				// 현재 요구사항 정보
				webhookBeforeReqInfo = req1000DAO.selectReq1000ReqInfo(reqMap);
				
				//사용자 웹훅 정보 세팅
				String reqUsrId = (String) webhookBeforeReqInfo.get("reqUsrId");
				webHookUsrStatus = webhookSend.userWebhookSetting((String)paramMap.get("licGrpId"), reqUsrId);
			}catch(Exception subE){}
			
			//프로젝트 웹훅 동작시에만 처리
			if(webHookStatus && deleteReqMessageCnt < 11 && webhookBeforeReqInfo != null){
				
				//프로젝트명 얻기
				if(i == 0){
					prjNm = (String)webhookBeforeReqInfo.get("prjNm");
				}
				
				deleteReqMessage += "["+webhookBeforeReqInfo.get("reqOrd")+"] "+webhookBeforeReqInfo.get("reqNm")+"\n";
				deleteReqMessageCnt++;
			}
			//사용자 웹훅 동작시에만 처리
			if(webHookUsrStatus && webhookBeforeReqInfo != null){
				//프로젝트명 얻기
				if(i == 0){
					prjNm = (String)webhookBeforeReqInfo.get("prjNm");
				}
				//추가 정보 입력
				webhookSend.setConnectInfo("프로젝트",  prjNm);
				
				//요구사항 접수등록
				String messasgeJson = webhookSend.messageJsonSetting(whkRegUsrNm+"님이 요구사항 ["+webhookBeforeReqInfo.get("reqOrd")+"](을)를 삭제했습니다.","#4b73eb");
				webhookSend.usrSend(messasgeJson, "whkReqDeleteCd");
			}
			/****************/
			
			// 요구사항 삭제
			req4100DAO.deleteReq4100ReqInfoAjax(reqMap);
			
			// 해당 요구사항의 수정이력 삭제
			req4800Service.deleteReq4800ReqHistoryInfo(reqMap);
			
			// 해당 요구사항의 첨부파일이 있을경우
			if( atchFileId != null && !"".equals(atchFileId) ){
				reqMap.put("atchFileId", atchFileId);
				
				// 요구사항의 첨부파일 정보 삭제
				req4100DAO.deleteReq4100ReqAtchFile(reqMap);
				// 요구사항의 첨부파일 상세정보 삭제
				req4100DAO.deleteReq4100ReqAtchFileDetail(reqMap);
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
	 * Req4106 요구사항 접수 완료
	 * @param param - Map
	 * @return list - List
	 * @exception Exception
	 */
	@SuppressWarnings({ "rawtypes", "unchecked" })
	public void updateReq4106NewReqAccpetInfoAjax(Map paramMap) throws Exception{

		//요구사항 단건 조회
		Map beforeReqInfo = req4100DAO.selectReq4102ReqInfoAjax(paramMap); 
		
		//현재 요구사항의 프로세스 ID 조회
		String reqProType = (String) beforeReqInfo.get("reqProType");
		
		//reqProType이 접수 요청이 아닌 경우 오류
		if(reqProType != null && !"01".equals(reqProType)) {
			throw new UserDefineException("이미 처리된 요구사항입니다.");
		}
		
		//프로세스 Id 가져오기
		String processId = (String) paramMap.get("processId");
		
				
		//접수 완료&반려, 요구사항 수정
		req4100DAO.updateReq4106NewReqAccpetInfoAjax(paramMap);
		
		//쪽지 필요 값
		String prjGrpId = (String)paramMap.get("selPrjGrpId");
		String prjId = (String)paramMap.get("prjId");
		String reqId = (String)paramMap.get("reqId");
		String reqNm = (String) paramMap.get("reqNm");
		String usrId = (String) paramMap.get("usrId");
		String reqChargerId = (String) paramMap.get("reqChargerId");
		String newReqCdNm = "승인";
		String newReqRejectCmnt = "";
		
		/*****************************
		 * 웹훅 세팅 (사용가능한지 체크)
		 * -프로젝트 웹훅 세팅
		 * -사용자 웹훅 세팅
		*******************************/
		//프로젝트 웹훅 세팅
		boolean webHookStatus = false;
		
		//사용자 웹훅 세팅
		boolean webHookUsrStatus = false;
		String whkRegUsrNm = "";
		
		try{
			//프로젝트
			webHookStatus = webhookSend.projectWebhookSetting(prjId);
			whkRegUsrNm =  (String)paramMap.get("whkRegUsrNm");
			
			//사용자 웹훅 정보 세팅
			String reqUsrId = (String) paramMap.get("reqUsrId");
			webHookUsrStatus = webhookSend.userWebhookSetting((String)paramMap.get("licGrpId"), reqUsrId);
		}catch(Exception e){
			//웹혹 오류시 skip
		}
		/*************************/
		
		//접수 승인에만 변경이력, 담당자 쪽지 등록
		if(!"reject".equals(processId)){	//승인
			//다음 작업흐름 추가 항목 검색
			Map<String, String> newOptMap = new HashMap<String,String>();
			newOptMap.put("licGrpId",(String)paramMap.get("licGrpId"));
			newOptMap.put("prjId",(String)paramMap.get("prjId"));
			newOptMap.put("processId",(String)paramMap.get("processId"));
			newOptMap.put("flowId",(String)paramMap.get("flowId"));
			List<Map> reqOptList = prj1100DAO.selectFlw1200OptList(newOptMap);
			
			//추가항목 루프
			for(Map reqOptInfo : reqOptList){
				String itemCode = (String)reqOptInfo.get("itemCode");
				
				//분류 첨부파일일경우 atchFileId 생성
				if("03".equals(itemCode)){
					//atch_file_id 강제 생성
					String atchFileIdString = idgenService.getNextStringId();
					fileMngService.insertFileMasterInfo(atchFileIdString);
					
					//항목 저장
					reqOptInfo.put("reqId",(String)paramMap.get("reqId"));
					reqOptInfo.put("itemValue",atchFileIdString);
					prj1100DAO.insertFlw1300OtpValueInfo(reqOptInfo);
				}
			}
			
			
			//자신이 담당자인경우 쪽지 발송 안함
			if(!usrId.equals(reqChargerId)){
				//현재 담당자에게 쪽지 발송
				Map<String,String> armMap = new HashMap<String,String>();
				armMap.put("usrId", (String) paramMap.get("reqChargerId"));
				armMap.put("sendUsrId", (String) paramMap.get("usrId"));
				armMap.put("title", "["+reqNm+"] 접수 완료 요구사항에 담당자로 지정되었습니다.");
				armMap.put("content", "["+reqNm+"] 접수 완료 요구사항에 담당자로 지정되었습니다.<br>해당 요구사항을 확인해주세요.");
				armMap.put("reqIds", "<span name='tagReqId' id='tagReqId' prj-grp-id='"+prjGrpId+"' prj-id='"+prjId+"' req-id='"+reqId+"' onclick='fnSpanReq4105Open(this)'>"+reqNm+"<li class='fa fa-share'></li></span>");
				
				//쪽지 등록
				arm1000DAO.insertArm1000AlarmInfo(armMap);
			}
			//변경이력 등록할 데이터 세팅
			Map newMap = paramMap;
			newMap.put("reqChgType", "03"); //접수 완료
			newMap.put("chgFlowId", paramMap.get("flowId"));
			newMap.put("chgChargerId", paramMap.get("reqChargerId"));
			newMap.put("chgUsrId", paramMap.get("regUsrId"));
			//요구사항 변경이력 등록
			req4700DAO.insertReq4700ReqChangeLogInfo(newMap);

		}else{	//반려
			newReqCdNm = "반려";
			newReqRejectCmnt = "<br><br>반려사유: "+ paramMap.get("reqAcceptTxt");
		}
		
		/*****************************
		 * 웹훅 메시지 내용 세팅
		*******************************/
		if(webHookStatus || webHookUsrStatus){
			//현재 요구사항 정보
			Map webhookBeforeReqInfo = req4100DAO.selectReq4100ReqInfo(paramMap);

			//프로젝트 url값 가져오기
			String projectUrl = EgovProperties.getProperty("Globals.project.url");
			
			//추가 정보 입력
			webhookSend.setConnectInfo("프로젝트",  (String) webhookBeforeReqInfo.get("prjNm"));
			webhookSend.setConnectInfo("요구사항 명",  (String) webhookBeforeReqInfo.get("reqNm"));
			webhookSend.setConnectInfo("요청자",  (String) webhookBeforeReqInfo.get("reqUsrNm"));
			
			// 접수의견 있을 경우만 세팅
			if(webhookBeforeReqInfo.get("reqAcceptTxt") != null) {
				webhookSend.setConnectInfo("접수 "+newReqCdNm+" 의견",  (String)webhookBeforeReqInfo.get("reqAcceptTxt"));
			}
			
			//요구사항 접수등록
			String messasgeJson = webhookSend.messageJsonSetting(whkRegUsrNm+"님이 요구사항 ["+webhookBeforeReqInfo.get("reqOrd")+"]("+projectUrl+"/data/req/"+webhookBeforeReqInfo.get("reqKey")+")(을)를 접수 "+newReqCdNm+" 했습니다."
					,(String) webhookBeforeReqInfo.get("flowTitleBgColor"));
			//승인 코드 전송
			if(!"reject".equals(processId)){
				//프로젝트 웹훅
				if(webHookStatus){
					webhookSend.prjSend(messasgeJson, "whkNewAcceptCd");
				}
				
				//사용자 웹훅 동작시에만 처리
				if(webHookUsrStatus){
					webhookSend.usrSend(messasgeJson, "whkNewAcceptCd");
				}
			}
			//반려 코드 전송
			else{
				//프로젝트 웹훅
				if(webHookStatus){
					webhookSend.prjSend(messasgeJson, "whkNewRejectCd");
				}
				//사용자 웹훅 동작시에만 처리
				if(webHookUsrStatus){
					webhookSend.usrSend(messasgeJson, "whkNewRejectCd");
				}
			}
		}
		/*******************************/
		
		//요청자에게 쪽지 발송
		String reqUsrId = (String) paramMap.get("reqUsrId");

		//요청자와 승인자와 다른경우에만 쪽지 발송 요청자가 없으면 발송안함
		if(reqUsrId != null && !"".equals(reqUsrId) &&!usrId.equals(reqUsrId)){
			Map<String,String> armMap = new HashMap<String,String>();
			armMap.put("usrId", reqUsrId);
			armMap.put("sendUsrId", usrId);
			armMap.put("title", "["+reqNm+"] 요청사항 접수가 "+newReqCdNm+"되었습니다.");
			armMap.put("content", "["+reqNm+"] 요청사항 접수가 "+newReqCdNm+"되었습니다.<br>해당 요청사항을 확인해주세요."+newReqRejectCmnt);
			armMap.put("reqIds", "<span name='tagReqId' id='tagReqId' prj-grp-id='"+prjGrpId+"' prj-id='"+prjId+"' req-id='"+reqId+"' onclick='fnSpanReq4108Open(this)'>"+reqNm+"<li class='fa fa-share'></li></span>");
			
			//쪽지 등록
			arm1000DAO.insertArm1000AlarmInfo(armMap);
		}
	}
	
	/**
	 * Req4700 요구사항 변경 히스토리 정보 가져오기
	 * @param param - Map
	 * @return list - List
	 * @exception Exception
	 */
	@SuppressWarnings("rawtypes")
	public List selectReq4700ReqHistoryList(Map paramMap) throws Exception{
		return (List) req4100DAO.selectReq4700ReqHistoryList(paramMap);
	}
	
	/**
	 * Req4900 요구사항 결재 정보 가져오기
	 * @param param - Map
	 * @return list - List
	 * @exception Exception
	 */
	@SuppressWarnings("rawtypes")
	public List selectReq4900ReqSignList(Map paramMap) throws Exception{
		return (List) req4100DAO.selectReq4900ReqSignList(paramMap);
	}

	@Override
	public void selectReq4100ExcelList(Req4100VO req4100vo,
			ExcelDataListResultHandler resultHandler) throws Exception {
		req4100DAO.selectReq4100ExcelList(req4100vo,resultHandler);
	}
	
	/**
	 * REQ4100 요구사항 전체 목록 조회
	 * @param param - Map
	 * @return list - List
	 * @exception Exception
	 */
	@SuppressWarnings("rawtypes")
	public List selectReq4100AllList(Req4100VO req4100vo) throws Exception {
		return req4100DAO.selectReq4100AllList(req4100vo);
	}
	
	/**
	 * REQ4100 프로세스, 작업흐름 별 요구사항 수 조회 
	 * @param param - Map
	 * @return Integer
	 * @exception Exception
	 */
	@SuppressWarnings({ "rawtypes"})
	public int selectReq4100ProcessFlowReqCnt(Map paramMap) throws Exception {
		return req4100DAO.selectReq4100ProcessFlowReqCnt(paramMap);
	}
	/**
	 * REQ4100 프로세스, 작업흐름 별 요구사항 조회 
	 * @param param - Map
	 * @return list - List
	 * @exception Exception
	 */
	@SuppressWarnings("rawtypes")
	public List selectReq4100ProcessFlowReqList(Map paramMap) throws Exception {
		return req4100DAO.selectReq4100ProcessFlowReqList(paramMap);
	}
	
	/**
	 * REQ4100 요구사항 작업흐름 변경
	 * @param param - Map
	 * @exception Exception
	 * @desc
	 * - reqStDTm, reqEdDtm, reqStDuDtm, reqEdDuDtm, flowId, reqProType은 REQ4100 기본 요구사항 컬럼 수정
	 * - 그 외에 항목은 모두 추가 항목 추가 또는 수정
	 * - 수정 이력 발생은 통합하고 수정은 각각 수정 쿼리 실행
	 * - 작업은 실시간 CUD
	 * 2018-09-11 : 수정이력 발생 없이 우선 저장 부터
	 */
	@SuppressWarnings({ "rawtypes", "unchecked" })
	public boolean saveReq4100ReqFlowChgAjax(Map paramMap) throws Exception {
		//수정이력 정보 생성
		req4800Service.insertReq4800ModifyHistoryList(paramMap);
		
		//기본 항목
		Map defaultReqInfo = new HashMap();
		
		//json, string 추가 항목 구분
		for( Object key : paramMap.keySet() ) {
			//fileList의 경우 arrayList기 때문에 skip
			if("fileList".equals(key)){
				continue;
			}
			String jsonVal = paramMap.get(key).toString();
			JSONObject jsonObj = null;
			
			//JsonData가 아닌 기본 String으로 넘겨받은 경우 각각 추가
			try{
				jsonObj = new JSONObject(jsonVal);
			}catch(JSONException jsonE){
				defaultReqInfo.put(key, paramMap.get(key).toString());
				continue;
			}
			
			//항목 타겟
			String chgDetailOptTarget = String.valueOf(jsonObj.get("chgDetailOptTarget"));
			String optVal = String.valueOf(jsonObj.get("optVal"));
			String optFlowId = String.valueOf(jsonObj.get("optFlowId"));
			
			//기본 항목
			if("01".equals(chgDetailOptTarget)){
				defaultReqInfo.put(key, optVal);
			}
			//추가 항목
			else if("02".equals(chgDetailOptTarget)){
				
				/** 추가 항목 추가&수정 **/
				Map newParamMap = new HashMap();
				newParamMap.putAll(paramMap);
				
				//flowId는 option에서 세팅
				if(newParamMap.containsKey("flowId")){
					newParamMap.remove("flowId");
				}
				newParamMap.put("flowId", optFlowId);
				newParamMap.put("itemId", key);
				newParamMap.put("itemValue", optVal);
				
				//추가 항목 있는지 체크
				int optCnt = prj1100DAO.selectFlw1200OptCntInfo(newParamMap);
				
				//있는 경우 update 없는 경우 insert
				if(optCnt > 0){
					prj1100DAO.updateFlw1300OtpValueInfo(newParamMap);
				}else{
					prj1100DAO.insertFlw1300OtpValueInfo(newParamMap);
				}
			}
			//배포 계획
			else if("03".equals(chgDetailOptTarget)){
				
				Map newParamMap = new HashMap();
				newParamMap.putAll(paramMap);
				
				//flowId는 option에서 세팅
				if(newParamMap.containsKey("flowId")){
					newParamMap.remove("flowId");
				}
				newParamMap.put("flowId", optFlowId);
				
				newParamMap.remove("dplId");
				//이전 배포 계획 있는지 체크
				List beforeDplList = dpl1100DAO.selectDpl1100ReqDplList(newParamMap);
				
				newParamMap.put("dplId", optVal);
				
				//배포 계획 있는지 체크
				List optCnt = dpl1100DAO.selectDpl1100ReqDplList(newParamMap);
				
				//있는 경우 이전 값 update 없는 경우 insert
				if(optCnt != null && optCnt.size() > 0){
					//dplId가 null인경우 해당 flowId 제거
					if(optVal == null || "".equals(optVal)){

						//기존 배포계획 ID가져오기
						Map dplInfo = (Map) beforeDplList.get(0);
						String beforeDplId = (String) dplInfo.get("dplId"); 
						newParamMap.remove("dplId");
						newParamMap.put("dplId", beforeDplId);
						
						//새로운 NEW_CHG_ID 구하기
						String newChgId = dpl1000DAO.selectDpl1500NewChgId(newParamMap);
						
						//배포계획에 배정제외 수정이력 쌓기
						newParamMap.put("chgId", newChgId);
						newParamMap.put("chgNum", 0);
						newParamMap.put("chgTypeCd", "03");
						newParamMap.put("chgNm", paramMap.get("reqId"));
						newParamMap.put("chgOptTypeCd", "04");	//항목 타입
						newParamMap.put("chgUsrId", paramMap.get("regUsrId"));
						
						//수정이력 등록
						dpl1000DAO.insertDpl1500ModifyHistoryInfo(newParamMap);
						
						dpl1100DAO.deleteDpl1100ReqDplInfo(newParamMap);
					}else{
						//기존 배포계획 ID가져오기
						Map dplInfo = (Map) beforeDplList.get(0);
						String beforeDplId = (String) dplInfo.get("dplId"); 
						
						//기존 배포 계획 id와 현재 id가 같은경우 동작 안함
						if(!beforeDplId.equals(optVal)) {
						
							//새로운 NEW_CHG_ID 구하기
							String newChgId = dpl1000DAO.selectDpl1500NewChgId(newParamMap);
							
							//배포계획에 배정추가 수정이력 쌓기
							newParamMap.put("chgId", newChgId);
							newParamMap.put("chgNum", 0);
							newParamMap.put("chgTypeCd", "02");
							newParamMap.put("chgNm", paramMap.get("reqId"));
							newParamMap.put("chgOptTypeCd", "04");	//항목 타입
							newParamMap.put("chgUsrId", paramMap.get("regUsrId"));
							
							//수정이력 등록
							dpl1000DAO.insertDpl1500ModifyHistoryInfo(newParamMap);
							
							dpl1100DAO.updateDpl1200ReqDplInfo(newParamMap);
						}
					}
				}else{
					//dplId가 null이 아닌 경우만 insert 그 외에 아무 동작 안함
					if(optVal != null && !"".equals(optVal)){
						//이전 배포 계획이 있는 경우
						if(beforeDplList != null && beforeDplList.size() > 0){
							//기존 배포계획 ID가져오기
							Map dplInfo = (Map) beforeDplList.get(0);
							String beforeDplId = (String) dplInfo.get("dplId"); 
							
							//이전 배포 계획 ID와 현재 배포계획 ID가 다른경우
							if(!optVal.equals(beforeDplId)){
								Map newMap = new HashMap<>();
								
								//배포계획에 배정제외 수정이력 쌓기
								newMap.put("prjId", newParamMap.get("prjId"));
								newMap.put("dplId", beforeDplId);
								
								//새로운 NEW_CHG_ID 구하기
								String newChgId = dpl1000DAO.selectDpl1500NewChgId(newMap);
								
								newMap.put("chgId", newChgId);
								newMap.put("chgNum", 0);
								newMap.put("chgTypeCd", "03");
								newMap.put("chgNm", paramMap.get("reqId"));
								newMap.put("chgOptTypeCd", "04");	//항목 타입
								newMap.put("chgUsrId", paramMap.get("regUsrId"));
								
								//수정이력 등록
								dpl1000DAO.insertDpl1500ModifyHistoryInfo(newMap);

								newMap.put("reqId", newParamMap.get("reqId"));
								newMap.put("processId", newParamMap.get("processId"));
								newMap.put("flowId", newParamMap.get("flowId"));
								
								//요구사항 배포계획 배정 제외
								dpl1100DAO.deleteDpl1100ReqDplInfo(newMap);
							}
						}
						//새로운 NEW_CHG_ID 구하기
						String newChgId = dpl1000DAO.selectDpl1500NewChgId(newParamMap);
						
						//배포계획에 배정추가 수정이력 쌓기
						newParamMap.put("chgId", newChgId);
						newParamMap.put("chgNum", 0);
						newParamMap.put("chgTypeCd", "02");
						newParamMap.put("chgNm", paramMap.get("reqId"));	//항목 명
						newParamMap.put("chgOptTypeCd", "04");	//항목 타입
						newParamMap.put("chgUsrId", paramMap.get("regUsrId"));
						
						//수정이력 등록
						dpl1000DAO.insertDpl1500ModifyHistoryInfo(newParamMap);
						
						dpl1100DAO.insertDpl1100ReqDplInfo(newParamMap);
					}
				}
			}
		}
		String prjId = (String)defaultReqInfo.get("prjId");
		/*****************************
		 * 웹훅 세팅 (사용가능한지 체크)
		 * -프로젝트 웹훅 세팅
		 * -사용자 웹훅 세팅
		*******************************/
		//프로젝트 웹훅 세팅
		boolean webHookStatus = false;
		//사용자 웹훅 세팅
		boolean webHookUsrStatus = false;
		
		String whkRegUsrNm = "";
		String projectUrl = "";
		Map webhookBeforeReqInfo = null;
		Map webhookAfterReqInfo = null;
		try{
			webHookStatus = webhookSend.projectWebhookSetting(prjId);
			whkRegUsrNm =  (String)paramMap.get("whkRegUsrNm");
			
			//현재 요구사항 정보
			webhookBeforeReqInfo = req4100DAO.selectReq4100ReqInfo(defaultReqInfo);

			//이전 담당자
			String reqBeforeChargerId = (String) webhookBeforeReqInfo.get("reqChargerId");
			
			//이전 담당자 빈 값 아닌경우 사용자 웹훅 세팅
			if(reqBeforeChargerId != null && !"".equals(reqBeforeChargerId)){
				webHookUsrStatus = webhookSend.userWebhookSetting((String)paramMap.get("licGrpId"), reqBeforeChargerId);
			}
			
			//프로젝트 url값 가져오기
			projectUrl = EgovProperties.getProperty("Globals.project.url");
		}catch(Exception e){
			//웹혹 오류시 skip
		}
		/*************************/
		/** 기본 항목 요구사항 수정 **/
		//다음 작업흐름이 종료인경우 최종 종료처리
		String flowNextNextId = (String) defaultReqInfo.get("flowNextNextId");
		
		if(flowNextNextId != null && "null".equals(flowNextNextId)){
			//최종 완료 처리
			defaultReqInfo.put("reqProType", "04");
		}
		
		//요구사항 작업흐름 변경 처리
		req4100DAO.updateReq4100ReqFlowChgInfoAjax(defaultReqInfo);
		
		//담당자 변경 처리 (대기)
		String preReqUsrId = (String) defaultReqInfo.get("preReqUsrId");
		String reqChargerId = (String) defaultReqInfo.get("reqChargerId");
		
		//쪽지 필요 값
		String prjGrpId = (String)defaultReqInfo.get("selPrjGrpId");
		String reqId = (String)defaultReqInfo.get("reqId");
		String reqNm = (String) defaultReqInfo.get("reqNm");
		
		
		
		//담당자 변경된경우 쪽지 발송
		if(!reqChargerId.equals(preReqUsrId)){
			//현재 담당자에게 쪽지 발송
			Map<String,String> armMap = new HashMap<String,String>();
			armMap.put("usrId", (String) reqChargerId);
			armMap.put("sendUsrId", (String) defaultReqInfo.get("regUsrId"));
			armMap.put("title", "["+reqNm+"] 요구사항에 담당자로 지정되었습니다.");
			armMap.put("content", "["+reqNm+"] 요구사항에 담당자로 지정되었습니다.<br>해당 요구사항을 확인해주세요.");
			armMap.put("reqIds", "<span name='tagReqId' id='tagReqId' prj-grp-id='"+prjGrpId+"' prj-id='"+prjId+"' req-id='"+reqId+"' onclick='fnSpanReq4105Open(this)'>"+reqNm+"<li class='fa fa-share'></li></span>");
			
			//쪽지 등록
			arm1000DAO.insertArm1000AlarmInfo(armMap);
			
			//변경이력 등록할 데이터 세팅
			Map newMap = new HashMap();
			newMap.putAll(paramMap);
			newMap.put("reqChgType", "02"); //담당자
			newMap.put("chgUsrId", paramMap.get("regUsrId"));
			newMap.put("preChargerId", preReqUsrId);
			newMap.put("chgChargerId", reqChargerId);
			
			//요구사항 변경이력 등록
			req4700DAO.insertReq4700ReqChangeLogInfo(newMap);
			
			/*****************************
			 * 웹훅 메시지 내용 세팅
			 * -요구사항 담당자 변경 웹훅
			 * -이전 담당자에게 변경 웹훅
			 * -변경 담당자에게 변경 웹훅
			*******************************/
			if(webHookStatus || webHookUsrStatus){
				//현재 요구사항 정보
				webhookAfterReqInfo = req4100DAO.selectReq4100ReqInfo(defaultReqInfo);
				
				//기본 추가 정보 입력
				webhookSend.setConnectInfo("프로젝트",  (String) webhookAfterReqInfo.get("prjNm"));
				webhookSend.setConnectInfo("요구사항 명",  (String) webhookAfterReqInfo.get("reqNm"));
				webhookSend.setConnectInfo("이전 담당자",  (String) webhookBeforeReqInfo.get("reqChargerNm"));
				webhookSend.setConnectInfo("변경 담당자",  (String) webhookAfterReqInfo.get("reqChargerNm"));
				
				//요구사항 접수등록
				String messasgeJson = webhookSend.messageJsonSetting(whkRegUsrNm+"님이 요구사항 ["+webhookBeforeReqInfo.get("reqOrd")+"]("+projectUrl+"/data/req/"+webhookBeforeReqInfo.get("reqKey")+")의 담당자를 변경 했습니다."
						,(String) webhookAfterReqInfo.get("flowTitleBgColor"));
				
				//프로젝트 웹훅 발송
				if(webHookStatus){
					webhookSend.prjSend(messasgeJson, "whkUserChgCd");
				}
				
				//사용자 웹훅 동작시에만 처리
				if(webHookUsrStatus){
					webhookSend.usrSend(messasgeJson, "whkUserChgCd");
				}
			}
			
			try{
				//변경 담당자 웹훅 세팅
				webHookUsrStatus = webhookSend.userWebhookSetting((String)paramMap.get("licGrpId"), reqChargerId);
				
				if(webHookUsrStatus){
					//기본 추가 정보 입력
					webhookSend.setConnectInfo("프로젝트",  (String) webhookAfterReqInfo.get("prjNm"));
					webhookSend.setConnectInfo("요구사항 명",  (String) webhookAfterReqInfo.get("reqNm"));
					webhookSend.setConnectInfo("이전 담당자",  (String) webhookBeforeReqInfo.get("reqChargerNm"));
					webhookSend.setConnectInfo("변경 담당자",  (String) webhookAfterReqInfo.get("reqChargerNm"));
					
					//요구사항 접수등록
					String messasgeJson = webhookSend.messageJsonSetting(whkRegUsrNm+"님이 요구사항 ["+webhookBeforeReqInfo.get("reqOrd")+"]("+projectUrl+"/data/req/"+webhookBeforeReqInfo.get("reqKey")+")의 담당자로 지정 했습니다."
							,(String) webhookAfterReqInfo.get("flowTitleBgColor"));
					webhookSend.usrSend(messasgeJson, "whkUserChgCd");
				}
				
			}catch(Exception e){
				//웹혹 오류시 skip
			}
			/*******************************/
		}
		
		//결재 처리인경우 결재 이력 쌓기
		String flowSignCd = (String) defaultReqInfo.get("flowSignCd");
		if(flowSignCd != null && "01".equals(flowSignCd)){
			//결재자 ID
			String reqSignId = (String) defaultReqInfo.get("reqSignId");
			
			Map newParamMap = new HashMap();
			newParamMap.putAll(paramMap);
			newParamMap.put("signFlowId", paramMap.get("flowNextId"));
			newParamMap.put("signUsrId", reqSignId);
			newParamMap.put("signCd", "01");
			newParamMap.put("preFlowId", paramMap.get("flowId"));
			
			req4900DAO.insertReq4900ReqSignInfo(newParamMap);
			
			// 사용자 ID를 가져온다.
			String regUsrId = (String)paramMap.get("regUsrId");
			
			// 결재자가 자기 자신이 아닐 경우에만 결재 요청 쪽지 발송
			if(!reqSignId.equals(regUsrId)) {
				//결재 요청 쪽지 발송
				Map<String,String> armMap = new HashMap<String,String>();
				armMap.put("usrId", reqSignId);
				armMap.put("sendUsrId", reqChargerId);
				armMap.put("title", "["+reqNm+"] 요구사항에 결재자로 지정되었습니다.");
				armMap.put("content", "["+reqNm+"] 요구사항에 결재자로 지정되었습니다.<br>해당 요구사항을 확인해주세요.");
				armMap.put("reqIds", "<span name='tagReqId' id='tagReqId' prj-grp-id='"+prjGrpId+"' prj-id='"+prjId+"' req-id='"+reqId+"' onclick='fnSpanReq4105Open(this)'>"+reqNm+"<li class='fa fa-share'></li></span>");
				
				//쪽지 등록
				arm1000DAO.insertArm1000AlarmInfo(armMap);
			}
			
			/*****************************
			 * 웹훅 메시지 내용 세팅
			 * -결재자 웹훅 목록 조회
			 * -요구사항 결재 요청
			*******************************/
			try{
				//결재자 웹훅 세팅
				webHookUsrStatus = webhookSend.userWebhookSetting((String)paramMap.get("licGrpId"), reqSignId);
			}catch(Exception e){
				//웹혹 오류시 skip
			}
			
			if(webHookStatus || webHookUsrStatus){
				//현재 요구사항 정보
				webhookAfterReqInfo = req4100DAO.selectReq4100ReqInfo(defaultReqInfo);
				
				//기본 추가 정보 입력
				webhookSend.setConnectInfo("프로젝트",  (String) webhookAfterReqInfo.get("prjNm"));
				webhookSend.setConnectInfo("요구사항 명",  (String) webhookAfterReqInfo.get("reqNm"));
				webhookSend.setConnectInfo("결재자",  (String) webhookAfterReqInfo.get("signUsrNm"));
				
				//요구사항 접수등록
				String messasgeJson = webhookSend.messageJsonSetting(whkRegUsrNm+"님이 요구사항 ["+webhookBeforeReqInfo.get("reqOrd")+"]("+projectUrl+"/data/req/"+webhookBeforeReqInfo.get("reqKey")+")(을)를 결재 요청 했습니다."
						,(String) webhookAfterReqInfo.get("flowTitleBgColor"));
				
				//프로젝트 웹훅 발송
				if(webHookStatus){
					webhookSend.prjSend(messasgeJson, "whkSignRequestCd");
				}
				
				//사용자 웹훅 발송
				if(webHookUsrStatus){
					webhookSend.usrSend(messasgeJson, "whkSignRequestCd");
				}
			}
			/*******************************/
		}else{
			//최종완료를 제외한 작업흐름에서 fileId 생성
			if(flowNextNextId != null && !"null".equals(flowNextNextId)){
				//다음 작업흐름 추가 항목 검색
				Map<String, String> newOptMap = new HashMap<String,String>();
				newOptMap.put("licGrpId",(String)paramMap.get("licGrpId"));
				newOptMap.put("prjId",(String)paramMap.get("prjId"));
				newOptMap.put("reqId",(String)paramMap.get("reqId"));
				newOptMap.put("processId",(String)paramMap.get("processId"));
				newOptMap.put("flowId",(String)paramMap.get("flowNextId"));
				List<Map> reqOptList = prj1100DAO.selectFlw1200OptList(newOptMap);
				
				//추가항목 루프
				for(Map reqOptInfo : reqOptList){
					String itemCode = (String)reqOptInfo.get("itemCode");
					reqOptInfo.put("reqId",(String)paramMap.get("reqId"));
					
					//분류 첨부파일일경우 atchFileId 생성
					if("03".equals(itemCode)){
						//추가 항목 있는지 체크
						int optCnt = prj1100DAO.selectFlw1200OptCntInfo(reqOptInfo);
						
						//없는 경우 insert
						if(optCnt == 0){
							//atch_file_id 강제 생성
							String atchFileIdString = idgenService.getNextStringId();
							fileMngService.insertFileMasterInfo(atchFileIdString);
							
							//항목 저장
							reqOptInfo.put("itemValue",atchFileIdString);
							
							prj1100DAO.insertFlw1300OtpValueInfo(reqOptInfo);
						}
					}
				}
			}
			
			//다음 변경 작업흐름 Id존재하는지 체크 (없는경우 임시저장)
			String flowNextId = (String)paramMap.get("flowNextId");
			if(flowNextId != null){
				//결재 없는경우 변경이력 쌓기
				//변경이력 등록할 데이터 세팅
				Map newMap = new HashMap();
				newMap.putAll(paramMap);
				newMap.put("reqChgType", "01"); //작업흐름
				newMap.put("chgUsrId", paramMap.get("regUsrId"));
				newMap.put("preFlowId", paramMap.get("flowId"));
				newMap.put("chgFlowId", paramMap.get("flowNextId"));
				newMap.put("preChargerId", preReqUsrId);
				newMap.put("chgChargerId", reqChargerId);
				
				//요구사항 변경이력 등록
				req4700DAO.insertReq4700ReqChangeLogInfo(newMap);
				
				/*****************************
				 * 웹훅 메시지 내용 세팅
				 * -요구사항 담당자 웹훅 목록 조회
				 * -요구사항 작업흐름 변경
				*******************************/
				try{
					//이전 담당자 빈 값 아닌경우 사용자 웹훅 세팅
					webHookUsrStatus = webhookSend.userWebhookSetting((String)paramMap.get("licGrpId"), preReqUsrId);
				}catch(Exception e){
					//웹혹 오류시 skip
				}
				
				if(webHookStatus || webHookUsrStatus){
					//현재 요구사항 정보
					webhookAfterReqInfo = req4100DAO.selectReq4100ReqInfo(defaultReqInfo);
					
					//이전, 변경 결재자
					String beforeFlowId = (String) webhookBeforeReqInfo.get("flowId");
					String afterFlowId = (String) webhookAfterReqInfo.get("flowId");
					
					//이전 작업흐름 변경 작업흐름 체크
					if(beforeFlowId != null && !beforeFlowId.equals(afterFlowId)){
						//기본 추가 정보 입력
						webhookSend.setConnectInfo("프로젝트",  (String) webhookAfterReqInfo.get("prjNm"));
						webhookSend.setConnectInfo("요구사항 명",  (String) webhookAfterReqInfo.get("reqNm"));
						webhookSend.setConnectInfo("이전 작업흐름",  (String) webhookBeforeReqInfo.get("flowNm"));
						webhookSend.setConnectInfo("변경 작업흐름",  (String) webhookAfterReqInfo.get("flowNm"));
						
						//요구사항 작업흐름 변경
						String messasgeJson = webhookSend.messageJsonSetting(whkRegUsrNm+"님이 요구사항 ["+webhookBeforeReqInfo.get("reqOrd")+"]("+projectUrl+"/data/req/"+webhookBeforeReqInfo.get("reqKey")+")의 작업흐름을 변경 했습니다."
								,(String) webhookAfterReqInfo.get("flowTitleBgColor"));
						
						//프로젝트 웹훅 발송
						if(webHookStatus){
							webhookSend.prjSend(messasgeJson, "whkFlowChgCd");
						}
						
						//사용자 웹훅 발송
						if(webHookUsrStatus){
							webhookSend.usrSend(messasgeJson, "whkFlowChgCd");
						}
					}
				}
				/*******************************/
			}
		}
		
		return true;
	}
	
	@SuppressWarnings("rawtypes")
	@Override
	public Map selectReq4100ReqInfo(Map<String, String> paramMap) {
		return req4100DAO.selectReq4100ReqInfo(paramMap);
	}

	/**
	 * Req4100 요구사항 엑셀 업로드 시 요청자(ADM2000) ID 검색  
	 * @param param - Map
	 * @return 
	 * @exception Exception
	 */
	@SuppressWarnings({ "rawtypes"})
	@Override
	public int selectReq4100ReqUsrChk(Map paramMap)throws Exception {
		return req4100DAO.selectReq4100ReqUsrChk(paramMap);
	}
	
	/**
	 * REQ4100 요구사항에 포함된 리비전 목록 조회(Grid page)
	 * @param Whk1000VO
	 * @return 
	 * @exception Exception
	 */
	@SuppressWarnings("rawtypes")
	public List<Map> selectReq4100RevisionList(Req4100VO req4100VO) throws Exception{
		return req4100DAO.selectReq4100RevisionList(req4100VO);
	}
	
	/**
	 * REQ4100요구사항에 포함된 리비전 목록 총 건수 조회(Grid page)
	 * @param Whk1000VO
	 * @return 
	 * @exception Exception
	 */
	public int selectReq4100RevisionListCnt(Req4100VO req4100VO) throws Exception {
		return req4100DAO.selectReq4100RevisionListCnt(req4100VO);
	}
	/**
	 * REQ4100 권한있는 프로젝트의 요구사항 검색
	 * @param param - Map
	 * @return list - List
	 * @exception Exception
	 */
	@SuppressWarnings({ "rawtypes" })
	public List<Map> selectReq4100PrjAuthReqList(Map paramMap) throws Exception {
		return req4100DAO.selectReq4100PrjAuthReqList(paramMap);
	}
	

	/**
	 * Req4105 요구사항 담당자 변경
	 * @param param - Map
	 * @exception Exception
	 */
	@SuppressWarnings("rawtypes")
	public void updateReq4100ReqChargerChgInfoAjax(Map paramMap) throws Exception{
		req4100DAO.updateReq4100ReqChargerChgInfoAjax(paramMap);
		
		String reqId = (String)paramMap.get("reqId");
		String reqNm = (String)paramMap.get("reqNm");
		String prjId = (String)paramMap.get("prjId");
		String prjGrpId = (String)paramMap.get("prjGrpId");
			//현재 담당자에게 쪽지 발송
		Map<String,String> armMap = new HashMap<String,String>();
		armMap.put("usrId", (String) paramMap.get("reqChargerId"));
		armMap.put("sendUsrId", (String) paramMap.get("usrId"));
		armMap.put("title", "["+reqNm+"] 접수 완료 요구사항에 담당자로 이관되었습니다.");
		armMap.put("content", "["+reqNm+"] 접수 완료 요구사항에 담당자로 이관되었습니다.<br>해당 요구사항을 확인해주세요.");
		armMap.put("reqIds", "<span name='tagReqId' id='tagReqId' prj-grp-id='"+prjGrpId+"' prj-id='"+prjId+"' req-id='"+reqId+"' onclick='fnSpanReq4105Open(this)'>"+reqNm+"<li class='fa fa-share'></li></span>");
			
			//쪽지 등록
		arm1000DAO.insertArm1000AlarmInfo(armMap);
	}

	
	/**
	 * Req4100 요구사항 목록 엑셀 다운로드
	 * @param param - Map
	 * @param resultHandler ExcelDataListResultHandler
	 * @return 
	 * @exception Exception
	 */
	@SuppressWarnings("rawtypes")
	@Override
	public void selectReq4100ExcelList(Map paramMap, ExcelDataListResultHandler resultHandler) throws Exception {
		req4100DAO.selectReq4100ExcelList(paramMap, resultHandler);
	}
	
	
	/**
	 * REQ4100 프로세스별 요구사항 조회 
	 * @param param - Map
	 * @return list - List
	 * @exception Exception
	 */
	@SuppressWarnings({ "rawtypes"})
	public List<Map> selectReq4100ProcessReqList(Map paramMap) throws Exception {
		return req4100DAO.selectReq4100ProcessReqList(paramMap);
	}

	
	/**
	 * REQ4100 외부 팝업 요구사항 조회 - 요구사항 상세정보를 조회한다.
	 * @param param - Map
	 * @return 
	 * @exception Exception
	 */
	@SuppressWarnings("rawtypes")
	public Map selectReq4110ReqInfo(Map paramMap) throws Exception {
		return req4100DAO.selectReq4110ReqInfo(paramMap);
	}	
}
