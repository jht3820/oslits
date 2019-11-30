package kr.opensoftlab.oslops.arm.arm1000.arm1000.service.impl;

import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import kr.opensoftlab.oslops.arm.arm1000.arm1000.service.Arm1000Service;
import kr.opensoftlab.oslops.arm.arm1000.arm1000.vo.Arm1000VO;
import kr.opensoftlab.sdf.util.WebhookSend;

import org.apache.log4j.Logger;
import org.springframework.stereotype.Service;

import egovframework.com.cmm.EgovMessageSource;
import egovframework.rte.fdl.cmmn.EgovAbstractServiceImpl;

/**
 * @Class Name : Arm1000ServiceImpl.java
 * @Description : Arm1000ServiceImpl Business Implement class
 * @Modification Information
 *
 * @author 진주영
 * @since 2018.01.03.
 * @version 1.0
 * @see
 *  
 *  Copyright (C) OpenSoftLab Corp. All right reserved.
 */

@Service("arm1000Service")
public class Arm1000ServiceImpl extends EgovAbstractServiceImpl implements Arm1000Service {

	/** 로그4j 로거 로딩 */
	private final Logger Log = Logger.getLogger(this.getClass());
	
	/** Arm1000DAO DI */
    @Resource(name="arm1000DAO")
    private Arm1000DAO arm1000DAO;

    /** EgovMessageSource */
   	@Resource(name = "egovMessageSource")
   	EgovMessageSource egovMessageSource;

    /** Whk1000Service DI */
    @Resource(name = "webhookSend")
    private WebhookSend webhookSend;
   	/**
	 * Arm1000 사용자 쪽지 목록 가져오기
	 * @param param - Map
	 * @return 
	 * @exception Exception
	 */
	@SuppressWarnings("rawtypes")
	public List selectArm1000AlarmList(Arm1000VO arm1000VO) throws Exception {
		 return arm1000DAO.selectArm1000AlarmList(arm1000VO);
    }
	
	/**
	 * Arm1000 사용자 쪽지 갯수 가져오기
	 * @param param - Map
	 * @return 
	 * @exception Exception
	 */
	@SuppressWarnings("rawtypes")
	public Map selectArm1000AlarmCnt(Map<String, String> paramMap) throws Exception {
		return arm1000DAO.selectArm1000AlarmCnt(paramMap);
	}
	/**
	 * Arm1000 쪽지 등록
	 * @param param - Map
	 * @return 
	 * @exception Exception
	 */
	@SuppressWarnings("rawtypes")
	public String insertArm1000AlarmInfo(Map paramMap) throws Exception{
		/*****************************
		 * 웹훅 세팅 (사용가능한지 체크)
		 * -사용자 웹훅 세팅(요청자)
		*******************************/
		//사용자 웹훅 세팅
		boolean webHookUsrStatus = false;
		String whkRegUsrNm = "";
		
		try{
			whkRegUsrNm =  (String)paramMap.get("whkRegUsrNm");

			//수신 대상
			String usrId = (String) paramMap.get("usrId");
			
			//사용자 웹훅 정보 세팅
			webHookUsrStatus = webhookSend.userWebhookSetting((String)paramMap.get("licGrpId"), usrId);
			
			/*****************************
			 * 웹훅 메시지 내용 세팅
			 * -쪽지 발송
			*******************************/
			if(webHookUsrStatus){
				//쪽지 제목, 내용
				String memoTitle = (String)paramMap.get("title");
				String memoContent = (String)paramMap.get("content");
				
				//쪽지 내용 br치환
				memoContent = memoContent.replaceAll("</br>", "\n");
				//쪽지 메시지 세팅
				String messasgeJson = webhookSend.messageJsonSetting(whkRegUsrNm+"님으로부터 쪽지가 도착했습니다.\n\n["+memoTitle+"]\n"+memoContent,"#4b73eb");
				webhookSend.usrSend(messasgeJson, "whkSignRejectCd");
			}
			/*******************************/
		}catch(Exception e){
			//웹혹 오류시 skip
			Log.debug(e.getMessage());
		}
		
		return arm1000DAO.insertArm1000AlarmInfo(paramMap);
	}
	
	/**
	 * Arm1000 쪽지 수정 (삭제 또는 읽음 처리)
	 * @param param - Map
	 * @return 
	 * @exception Exception
	 */
	@SuppressWarnings({"unchecked" })
	public void updateArm1000AlarmInfo(Map<String, Object> paramMap) throws Exception{
		List<Map<String,String>> list = (List<Map<String,String>>) paramMap.get("list");
		
		//list가 null일경우 paramMap으로 세팅
		if(list == null){
			arm1000DAO.updateArm1000AlarmInfo(paramMap);
		}else{
			int listSize = list.size();
			
			for (int i = 0; i < listSize; i++) {
				Map<String,String> reqMap = list.get(i);
				reqMap.put("usrId", reqMap.get("regUsrId"));
				if("01".equals(paramMap.get("viewCheck"))){
					reqMap.put("viewCheck", (String)paramMap.get("viewCheck"));
				}
				else if("Y".equals(paramMap.get("delCheck"))){
					reqMap.put("delCheck", (String)paramMap.get("delCheck"));
				}
				
				arm1000DAO.updateArm1000AlarmInfo(reqMap);
			}
		}
	}
	
	/**
	 * Arm1000 사용자 쪽지 내용 불러오기(단건)
	 * @param param - Map
	 * @return 
	 * @exception Exception
	 */
	@SuppressWarnings("rawtypes")
	public Map selectArm1000AlarmInfo(Map paramMap) throws Exception {
		return arm1000DAO.selectArm1000AlarmInfo(paramMap);
	}
	
	/**
	 * Arm1000 쪽지 삭제 (사용자 제거)
	 * @param param - Map
	 * @return 
	 * @exception Exception
	 */
	@SuppressWarnings("rawtypes")
	public void updateArm1000AlarmList(Map paramMap) throws Exception{
		arm1000DAO.updateArm1000AlarmList(paramMap);
	}
	
	/**
	 * Adm1300 해당 프로젝트 권한 목록 불러오기
	 * @param param - Map
	 * @return 
	 * @exception Exception
	 */
	@SuppressWarnings({ "rawtypes" })
	public List selectAdm1300AuthUsrList(Map paramMap) throws Exception {
		return arm1000DAO.selectAdm1300AuthUsrList(paramMap);
	}
}
