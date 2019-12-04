package kr.opensoftlab.oslops.req.req4000.req4900.service.impl;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import org.apache.log4j.Logger;
import org.springframework.stereotype.Service;

import egovframework.com.cmm.service.EgovProperties;
import egovframework.rte.fdl.cmmn.EgovAbstractServiceImpl;
import egovframework.rte.fdl.idgnr.EgovIdGnrService;
import kr.opensoftlab.oslops.arm.arm1000.arm1000.service.impl.Arm1000DAO;
import kr.opensoftlab.oslops.com.fms.web.service.FileMngService;
import kr.opensoftlab.oslops.prj.prj1000.prj1100.service.impl.Prj1100DAO;
import kr.opensoftlab.oslops.req.req4000.req4100.service.impl.Req4100DAO;
import kr.opensoftlab.oslops.req.req4000.req4900.service.Req4900Service;
import kr.opensoftlab.oslops.req.req4700.req4700.service.impl.Req4700DAO;
import kr.opensoftlab.sdf.util.WebhookSend;


@Service("req4900Service")
public class Req4900ServiceImpl  extends EgovAbstractServiceImpl implements Req4900Service{

	/** 로그4j 로거 로딩 */
	private final Logger Log = Logger.getLogger(this.getClass());
	
	/** DAO Bean Injection */
    @Resource(name="arm1000DAO")
    private Arm1000DAO arm1000DAO;
    
    @Resource(name="req4100DAO")
    private Req4100DAO req4100DAO;

    @Resource(name="req4700DAO")
    private Req4700DAO req4700DAO;
    
    @Resource(name="req4900DAO")
    private Req4900DAO req4900DAO;

    /** Prj1100DAO DI */
    @Resource(name="prj1100DAO")
    private Prj1100DAO prj1100DAO;

    /** Whk1000Service DI */
    @Resource(name = "webhookSend")
    private WebhookSend webhookSend;
    
    /** FileMngService */
   	@Resource(name="fileMngService")
   	private FileMngService fileMngService;
   	
   	@Resource(name = "egovFileIdGnrService")
	private EgovIdGnrService idgenService;
   	
    /**
	 * Req4900 요구사항 결재정보 등록
	 * @param param - Map
	 * @return 
	 * @exception Exception
	 */	
	@SuppressWarnings({ "rawtypes", "unchecked" })
	public void insertReq4900ReqSignInfo(Map paramMap) throws Exception{
		
		String signCd = (String) paramMap.get("signCd");
		
		//쪽지 필요 값
		String prjGrpId = (String)paramMap.get("selPrjGrpId");
		String prjId = (String)paramMap.get("prjId");
		String reqId = (String)paramMap.get("reqId");
		String reqNm = (String) paramMap.get("reqNm");
		String signUsrId = (String) paramMap.get("signUsrId"); //결재자
		//결재 요청자
		String signRegUsrId = (String) paramMap.get("signRegUsrId"); //요청자
		
		//결재 반려시 종료 유무
		String flowSignStopCd = (String) paramMap.get("flowSignStopCd");
		
		/*****************************
		 * 웹훅 세팅 (사용가능한지 체크)
		 * -프로젝트 웹훅 세팅
		 * -사용자 웹훅 세팅(요청자)
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

			//사용자 웹훅 정보 세팅
			webHookUsrStatus = webhookSend.userWebhookSetting((String)paramMap.get("licGrpId"), signRegUsrId);
			
			//현재 요구사항 정보
			webhookBeforeReqInfo = req4100DAO.selectReq4100ReqInfo(paramMap);

			//프로젝트 url값 가져오기
			projectUrl = EgovProperties.getProperty("Globals.project.url");
		}catch(Exception e){
			//웹혹 오류시 skip
			Log.debug(e.getMessage());
		}
		/*************************/
		
		//결재 반려이고 종료처리인경우 최종완료 작업흐름 구하기
		if(signCd != null && "03".equals(signCd) && flowSignStopCd != null && "01".equals(flowSignStopCd)){
			List<Map> flowList = prj1100DAO.selectFlw1100FlowList(paramMap);
			
			String endNextId = "";
			//루프
			for(Map flowInfo : flowList){
				if(flowInfo.get("flowNextId") == null){
					endNextId = (String) flowInfo.get("flowId");
					break;
				}
			}

			//다음 작업흐름 Id 교체
			paramMap.remove("signFlowId");
			paramMap.remove("flowNextNextId");
			
			paramMap.put("signFlowId", endNextId);
			paramMap.put("flowNextNextId", null);
		}
		
		req4900DAO.insertReq4900ReqSignInfo(paramMap);
		
		String signCdNm = "승인";
		String signRejectCmnt = "";	//반려사유
		
		//결재 승인
		if(signCd != null && "02".equals(signCd)){
			//다음 작업흐름이 종료인경우 최종 종료처리
			String flowNextNextId = (String) paramMap.get("flowNextNextId");
			
			if(flowNextNextId != null && "null".equals(flowNextNextId)){
				//최종 완료 처리
				paramMap.put("reqProType", "04");
			}else{ //최종완료 아닌경우에만 fileId 생성
				//다음 작업흐름 추가 항목 검색
				Map<String, String> newOptMap = new HashMap<String,String>();
				newOptMap.put("licGrpId",(String)paramMap.get("licGrpId"));
				newOptMap.put("prjId",(String)paramMap.get("prjId"));
				newOptMap.put("processId",(String)paramMap.get("processId"));
				newOptMap.put("flowId",(String)paramMap.get("signFlowId"));
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
			
			//결재 승인인경우 요구사항 작업흐름 변경처리
			req4100DAO.updateReq4100ReqFlowChgInfoAjax(paramMap);
			
			//변경이력 등록할 데이터 세팅
			Map newMap = new HashMap();
			newMap.putAll(paramMap);
			newMap.put("chgUsrId", paramMap.get("regUsrId"));
			newMap.put("chgFlowId", paramMap.get("signFlowId"));
			
			newMap.put("reqChgType", "01"); //작업흐름 변경
			
			//요구사항 변경이력 등록
			req4700DAO.insertReq4700ReqChangeLogInfo(newMap);
			
			/*****************************
			 * 웹훅 메시지 내용 세팅
			 * -요구사항 최종완료 처리
			*******************************/
			if(webHookStatus || webHookUsrStatus){
				//현재 요구사항 정보
				webhookAfterReqInfo = req4100DAO.selectReq4100ReqInfo(paramMap);
			
				//기본 추가 정보 입력
				webhookSend.setConnectInfo("프로젝트",  (String) webhookAfterReqInfo.get("prjNm"));
				
				//최종 완료 처리
				if(flowNextNextId != null && "null".equals(flowNextNextId)){
					webhookSend.setConnectInfo("프로세스",  (String) webhookAfterReqInfo.get("processNm"));
					webhookSend.setConnectInfo("요구사항 명",  (String) webhookAfterReqInfo.get("reqNm"));
					
					//요구사항 메시지 등록
					String messasgeJson = webhookSend.messageJsonSetting("결재가 승인되었습니다.\n결재자 "+whkRegUsrNm+"님이 요구사항 ["+webhookBeforeReqInfo.get("reqOrd")+"]("+projectUrl+"/data/req/"+webhookBeforeReqInfo.get("reqKey")+")(을)를 최종완료 처리했습니다."
							,(String) webhookAfterReqInfo.get("flowTitleBgColor"));
					//프로젝트 웹훅
					if(webHookStatus){
						webhookSend.prjSend(messasgeJson, "whkReqDoneCd");
					}
					
					//사용자 웹훅 동작시에만 처리
					if(webHookUsrStatus){
						webhookSend.usrSend(messasgeJson, "whkReqDoneCd");
					}
				}
				else{
					//기본 추가 정보 입력
					webhookSend.setConnectInfo("요구사항 명",  (String) webhookAfterReqInfo.get("reqNm"));
					webhookSend.setConnectInfo("이전 작업흐름",  (String) webhookBeforeReqInfo.get("flowNm"));
					webhookSend.setConnectInfo("변경 작업흐름",  (String) webhookAfterReqInfo.get("flowNm"));
					
					//요구사항 작업흐름 변경
					String messasgeJson = webhookSend.messageJsonSetting("결재자 "+whkRegUsrNm+"님이 요구사항 ["+webhookBeforeReqInfo.get("reqOrd")+"]("+projectUrl+"/data/req/"+webhookBeforeReqInfo.get("reqKey")+")(을)를 결재 승인 처리했습니다."
							,(String) webhookAfterReqInfo.get("flowTitleBgColor"));
					//프로젝트 웹훅
					if(webHookStatus){
						webhookSend.prjSend(messasgeJson, "whkSignAcceptCd");
					}
					
					//사용자 웹훅 동작시에만 처리
					if(webHookUsrStatus){
						webhookSend.usrSend(messasgeJson, "whkSignAcceptCd");
					}
				}
			}
			/*******************************/
		}else if("03".equals(signCd)){
			signCdNm = "반려";
			signRejectCmnt = "<br><br>반려사유: "+ paramMap.get("signRejectCmnt");

			String addMessage = "";
			//반려시 
			if("01".equals(flowSignStopCd)){
				//reqProType, flowId수정위해 02로 전달
				paramMap.remove("signCd");
				paramMap.put("signCd", "02");
				
				//최종 완료 처리 - 결재 반려종료 상태
				paramMap.put("reqProType", "05");
				
				//결재 승인인경우 요구사항 작업흐름 변경처리
				req4100DAO.updateReq4100ReqFlowChgInfoAjax(paramMap);
				
				//변경이력 등록할 데이터 세팅
				Map newMap = new HashMap();
				newMap.putAll(paramMap);
				newMap.put("chgUsrId", paramMap.get("regUsrId"));
				newMap.put("preFlowId", paramMap.get("preFlowId"));
				newMap.put("chgFlowId", paramMap.get("signFlowId"));
				
				newMap.put("reqChgType", "01"); //작업흐름 변경
				
				//요구사항 변경이력 등록
				req4700DAO.insertReq4700ReqChangeLogInfo(newMap);
				
				addMessage = "\n해당 요구사항은 작업흐름 옵션에 따라 최종완료 처리 되었습니다.";
			}
			
			/*****************************
			 * 웹훅 메시지 내용 세팅
			 * -결재 반려 처리
			*******************************/
			if(webHookStatus || webHookUsrStatus){
				//현재 요구사항 정보
				webhookAfterReqInfo = req4100DAO.selectReq4100ReqInfo(paramMap);
			
				//기본 추가 정보 입력
				webhookSend.setConnectInfo("프로젝트",  (String) webhookAfterReqInfo.get("prjNm"));
				webhookSend.setConnectInfo("요구사항 명",  (String) webhookAfterReqInfo.get("reqNm"));
				webhookSend.setConnectInfo("반려 사유",  (String) paramMap.get("signRejectCmnt"));
				
				//요구사항 메시지 등록
				String messasgeJson = webhookSend.messageJsonSetting("결재자 "+whkRegUsrNm+"님이 요구사항 ["+webhookBeforeReqInfo.get("reqOrd")+"]("+projectUrl+"/data/req/"+webhookBeforeReqInfo.get("reqKey")+")(을)를 결재 반려 처리했습니다."+addMessage
						,(String) webhookAfterReqInfo.get("flowTitleBgColor"));
				//프로젝트 웹훅
				if(webHookStatus){
					webhookSend.prjSend(messasgeJson, "whkSignRejectCd");
				}
				
				//사용자 웹훅 동작시에만 처리
				if(webHookUsrStatus){
					webhookSend.usrSend(messasgeJson, "whkSignRejectCd");
				}
			}
			/*******************************/
		}
		
		//결재 요청자가 자신경우 쪽지 발송 안함
		if(!signUsrId.equals(signRegUsrId)){
			//현재 담당자에게 쪽지 발송
			Map<String,String> armMap = new HashMap<String,String>();
			armMap.put("usrId", signRegUsrId);
			armMap.put("sendUsrId", signUsrId);
			armMap.put("title", "["+reqNm+"] 요구사항 결재가 "+signCdNm+"되었습니다.");
			armMap.put("content", "["+reqNm+"] 요구사항 결재가 "+signCdNm+"되었습니다.<br>해당 요구사항을 확인해주세요."+signRejectCmnt);
			armMap.put("reqIds", "<span name='tagReqId' id='tagReqId' prj-grp-id='"+prjGrpId+"' prj-id='"+prjId+"' req-id='"+reqId+"' onclick='fnSpanReq4105Open(this)'>"+reqNm+"<li class='fa fa-share'></li></span>");
			
			//쪽지 등록
			arm1000DAO.insertArm1000AlarmInfo(armMap);
		}
	}
	
}
