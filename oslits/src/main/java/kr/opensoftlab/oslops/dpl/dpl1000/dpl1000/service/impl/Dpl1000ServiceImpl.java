package kr.opensoftlab.oslops.dpl.dpl1000.dpl1000.service.impl;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Timer;

import javax.annotation.Resource;

import kr.opensoftlab.oslops.arm.arm1000.arm1000.service.impl.Arm1000DAO;
import kr.opensoftlab.oslops.dpl.dpl1000.dpl1000.service.Dpl1000Service;
import kr.opensoftlab.oslops.dpl.dpl1000.dpl1000.vo.Dpl1000VO;
import kr.opensoftlab.oslops.dpl.dpl1000.dpl1000.vo.Dpl1300VO;
import kr.opensoftlab.oslops.dpl.dpl2000.dpl2100.service.Dpl2100Service;
import kr.opensoftlab.oslops.stm.stm3000.stm3000.service.Stm3000Service;
import kr.opensoftlab.sdf.excel.ExcelDataListResultHandler;
import kr.opensoftlab.sdf.jenkins.vo.AutoBuildVO;
import kr.opensoftlab.sdf.jenkins.vo.BuildVO;
import kr.opensoftlab.sdf.jenkins.vo.GlobalDplListVO;

import org.codehaus.jettison.json.JSONArray;
import org.codehaus.jettison.json.JSONException;
import org.codehaus.jettison.json.JSONObject;
import org.springframework.stereotype.Service;

import com.fasterxml.jackson.databind.ObjectMapper;

import egovframework.rte.fdl.cmmn.EgovAbstractServiceImpl;

/**
 * @Class Name : Dpl1000ServiceImpl.java
 * @Description : Dpl1000ServiceImpl Business Implement class
 * @Modification Information
 *
 * @author 정형택
 * @since 2016.01.30
 * @version 1.0
 * @see
 *  
 *  --------------------------------------
 *  수정일			수정자			수정내용
 *  --------------------------------------
 *  2019-03-07		진주영		 	기능 개선
 *  
 *  
 *  --------------------------------------
 *  
 *  Copyright (C)  All right reserved.
 */
@Service("dpl1000Service")
public class Dpl1000ServiceImpl  extends EgovAbstractServiceImpl implements Dpl1000Service{

	/** DAO Bean Injection */
    @Resource(name="arm1000DAO")
    private Arm1000DAO arm1000DAO;
    
	/** dpl1000DAO DI */
    @Resource(name="dpl1000DAO")
    private Dpl1000DAO dpl1000DAO;
    
    /** Dpl2100Service DI */
    @Resource(name = "dpl2100Service")
    private Dpl2100Service dpl2100Service;
    
	@Resource(name = "stm3000Service")
	private Stm3000Service stm3000Service;
	
    /**
	 * Dpl1000 배포 계획 리스트 조회
	 * @param
	 * @return 배포자 목록
	 * @exception Exception
	 */
	@SuppressWarnings("rawtypes")
	public List selectDpl1000DeployNmList(Map inputMap) throws Exception {
		return dpl1000DAO.selectDpl1000DeployNmList(inputMap);
    }
	
	/**
	 * Dpl1000배포 계획 정보 일반 목록(No Page) 가져오기
	 * @param param - Map
	 * @return list 배포 계획 리스트
	 * @throws Exception
	 */
	@SuppressWarnings({"rawtypes" })
	public List selectDpl1000DeployVerNormalList(Map inputMap)  throws Exception{
		return dpl1000DAO.selectDpl1000DeployVerNormalList(inputMap);
	} 
	
    /**
	 * Dpl1000 배포 계획 리스트 조회
	 * @param
	 * @return 배포 계획 정보 목록
	 * @exception Exception
	 */
	public List<Dpl1000VO> selectDpl1000DeployVerInfoList(Dpl1000VO dpl1000VO) throws Exception {
		return dpl1000DAO.selectDpl1000DeployVerInfoList(dpl1000VO);
    }
	
	/**
	 * Dpl1000 배포 계획 리스트 조회
	 * @param
	 * @return 배포 계획 정보 목록
	 * @exception Exception
	 */
	@SuppressWarnings("rawtypes")
	public Map selectDpl1000DeployVerInfo(Map map) throws Exception {
		return dpl1000DAO.selectDpl1000DeployVerInfo(map);
	}
	
    /**
	 * Dpl1000 배포 계획별 요구사항 등록 카운트
	 * @param
	 * @return 배포 계획별 요구사항 수
	 * @exception Exception
	 */
	@SuppressWarnings("rawtypes")
	public List selectDpl1000ReqCount(Map inputMap) throws Exception {
		return dpl1000DAO.selectDpl1000ReqCount(inputMap);
    }
	
	/**
	 * Dpl1300 배포 계획에 배정된 JOB 목록
	 * @param param - Map
	 * @throws Exception
	 */
	@SuppressWarnings({"rawtypes" })
	public List selectDpl1300DeployJobList(Map inputMap)  throws Exception{
		return dpl1000DAO.selectDpl1300DeployJobList(inputMap);
	}
	
	/**
	 * Dpl1300배포 계획 배정 JOB 목록 가져오기 (그리드)
	 * @param param - Map
	 * @return list 배포 계획 리스트
	 * @throws Exception
	 */
	@SuppressWarnings("unchecked")
	public List<Dpl1300VO> selectDpl1300dplJobGridList(Dpl1300VO dpl1300VO)  throws Exception{
		return dpl1000DAO.selectDpl1300dplJobGridList(dpl1300VO);
	} 
	
	/**
	 * Dpl1300 배포 계획 배정 JOB 목록 리스트 총건수
	 * @return 
	 * @exception Exception
	 */
	public int selectDpl1300dplJobGridListCnt(Dpl1300VO dpl1300VO) throws Exception {
		return dpl1000DAO.selectDpl1300dplJobGridListCnt(dpl1300VO);
	}
	
	/**
	 * Dpl1000 배포 계획 등록
	 * @param param - Map
	 * @return 
	 * @exception Exception
	 */
	@SuppressWarnings({ "rawtypes", "unchecked" })
	public void insertDpl1000DeployVerInfo(Map subParamMap) throws Exception{
		Map paramMap = new HashMap<>();
		paramMap.putAll(subParamMap);
		
		//JsonData있는 경우 String으로 변환
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
		paramMap = rtnMap;
		
		//배포 상태 대기 고정
		paramMap.put("dplStsCd", "01");
		
		//prjId
		String prjId = (String)paramMap.get("prjId");
		
		//dplId
		String dplId = dpl1000DAO.insertDpl1000DeployVerInfo(paramMap);
		paramMap.put("dplId", dplId);
		
    	//json array parse
    	JSONArray selJobList = new JSONArray(paramMap.get("selJobList").toString());
    	
    	//json array loop
    	for(int i=0; i<selJobList.length(); i++){
    		JSONObject jsonObj = selJobList.getJSONObject(i);
    		
    		//selJobList
    		HashMap<String, Object> jobInfo = new ObjectMapper().readValue(jsonObj.toString(), HashMap.class) ;
    		jobInfo.put("prjId", prjId);
    		jobInfo.put("dplId", dplId);
    		
    		dpl1000DAO.insertDpl1300DeployJobInfo(jobInfo);
    	}
    	
    	//요청자
    	paramMap.put("signRegUsrId", paramMap.get("regUsrId"));
    	paramMap.put("signStsCd", "04");
    	paramMap.put("signTxt", paramMap.get("dplSignTxt"));
    	
    	//결재 기안 등록
    	dpl2100Service.insertDpl2100DplSignWaitInfo(paramMap);
    	
    	//등록자
    	String regUsrId = (String)paramMap.get("regUsrId");
    	
    	//배포자
    	String dplUsrId = (String)paramMap.get("dplUsrId");
    	
    	//배포계획명
    	String dplNm = (String)paramMap.get("dplNm");
    	
    	
    	//등록자와 배포자가 다른경우 쪽지 발송
    	if(!dplUsrId.equals(regUsrId)){
    		//배포자 쪽지
    		Map<String,String> armMap = new HashMap<String,String>();
    		armMap.put("usrId", dplUsrId);
    		armMap.put("sendUsrId", regUsrId);
    		armMap.put("title", "["+dplNm+"] 배포계획에 배포자로 지정되었습니다.");
    		armMap.put("content", "["+dplNm+"] 배포계획에 배포자로 지정되었습니다.<br>해당 배포계획을 확인해주세요.");
    		armMap.put("reqIds", "<span name='tagDplId' id='tagDplId' prj-id='"+prjId+"' dpl-id='"+dplId+"' onclick='fnSpanDplDetailOpen(this)'>"+dplNm+"<li class='fa fa-share'></li></span>");
    		
    		//쪽지 등록
    		arm1000DAO.insertArm1000AlarmInfo(armMap);
    	}
    }

	/**
	 * Dpl1000 배포 계획 수정
	 * @param param - Map
	 * @return 
	 * @exception Exception
	 */
	@SuppressWarnings({ "rawtypes", "unchecked" })
	public void updateDpl1000DeployVerInfo(Map subParamMap) throws Exception{
		
		Map paramMap = new HashMap<>();
		paramMap.putAll(subParamMap);
		
		//수정이력 저장
		this.insertDpl1500DplInfoModifyList(paramMap);
		
		//JsonData있는 경우 String으로 변환
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
		paramMap = rtnMap;
		
		//수정 전 데이터 조회
		Map beforeDplInfo = dpl1000DAO.selectDpl1000DeployVerInfo(paramMap);
		
		//입력된 JOB내용 전체 삭제
		dpl1000DAO.deleteDpl1300DplJobList(paramMap);
		
		//새로 입력된 JOB 등록
		//prjId
		String prjId = (String)paramMap.get("prjId");
		
    	//배포계획 수정
    	dpl1000DAO.updateDpl1000DeployVerInfo(paramMap);
    	
		//dplId
		String dplId = (String)beforeDplInfo.get("dplId");
		paramMap.put("dplId", dplId);
		
    	//json array parse
    	JSONArray selJobList = new JSONArray(paramMap.get("selJobList").toString());
    	
    	//json array loop
    	for(int i=0; i<selJobList.length(); i++){
    		JSONObject jsonObj = selJobList.getJSONObject(i);
    		
    		//selJobList
    		HashMap<String, Object> jobInfo = new ObjectMapper().readValue(jsonObj.toString(), HashMap.class) ;
    		jobInfo.put("prjId", prjId);
    		jobInfo.put("dplId", dplId);
    		
    		dpl1000DAO.insertDpl1300DeployJobInfo(jobInfo);
    	}
    	
    	//요청자
    	paramMap.put("signRegUsrId", paramMap.get("regUsrId"));
    	
    	//등록자
    	String regUsrId = (String)paramMap.get("regUsrId");
    	
    	//배포자
    	String dplUsrId = (String)paramMap.get("dplUsrId");
    	
    	//배포자 이전
    	String beforeDplUsrId = (String)beforeDplInfo.get("dplUsrId");
    	
    	//배포계획명
    	String dplNm = (String)paramMap.get("dplNm");
    	
    	//배포자 변경된경우 배포자 쪽지 전송
    	if(!dplUsrId.equals(regUsrId)){
    		//등록자와 배포자가 다른경우 쪽지 발송
    		if(!beforeDplUsrId.equals(dplUsrId)){
    			//배포자 쪽지
    			Map<String,String> armMap = new HashMap<String,String>();
    			armMap.put("usrId", dplUsrId);
    			armMap.put("sendUsrId", regUsrId);
    			armMap.put("title", "["+dplNm+"] 배포계획에 배포자로 지정되었습니다.");
    			armMap.put("content", "["+dplNm+"] 배포계획에 배포자로 지정되었습니다.<br>해당 배포계획을 확인해주세요.");
    			armMap.put("reqIds", "<span name='tagDplId' id='tagDplId' prj-id='"+prjId+"' dpl-id='"+dplId+"' onclick='fnSpanDplDetailOpen(this)'>"+dplNm+"<li class='fa fa-share'></li></span>");
    			
    			//쪽지 등록
    			arm1000DAO.insertArm1000AlarmInfo(armMap);
    		}
    	}
    	
    	//요청자
    	paramMap.put("signRegUsrId", paramMap.get("regUsrId"));
    	paramMap.put("signStsCd", "05");
    	paramMap.put("signTxt", paramMap.get("dplSignTxt"));
    	
    	//결재 기안 등록
    	dpl2100Service.insertDpl2100DplSignWaitInfo(paramMap);
	}
	
	/**
	 * Dpl1000 배포 계획 배포 상태 수정
	 * @param param - Map
	 * @return 
	 * @exception Exception
	 */
	@SuppressWarnings({ "rawtypes", "unchecked" })
	public void updateDpl1000DplStsCdInfo(Map paramMap) throws Exception{
		//기존 배포계획 정보 불러오기
		Map beforeDplInfo = dpl1000DAO.selectDpl1000DeployVerInfo(paramMap);
		
		//배포 상태 수정
		dpl1000DAO.updateDpl1000DplStsCdInfo(paramMap);
		
		//새로운 NEW_CHG_ID 구하기
		String newChgId = dpl1000DAO.selectDpl1500NewChgId(paramMap);
		paramMap.put("chgId", newChgId);
		
		//Map세팅
		Map<String, Object> map = new HashMap<String, Object>();
		map.putAll(paramMap);
		
		//추가 정보 대입
		map.put("chgNum", 0);
		map.put("chgTypeCd", "01");
		map.put("chgNm", "배포 상태");	//항목 명
		map.put("preVal", beforeDplInfo.get("dplStsCd")); //기존 값
		map.put("chgVal", paramMap.get("dplStsCd"));	//변경 값
		map.put("chgOptTypeCd", "02");	//항목 타입
		map.put("chgCommonCd", "DPL00001");	//공통코드
		map.put("chgUsrId", paramMap.get("regUsrId"));
		
		//수정이력 등록
		dpl1000DAO.insertDpl1500ModifyHistoryInfo(map);
	}
	
	/**
	 * Dpl1000 배포 계획 삭제
	 * @param param - Map
	 * @return 
	 * @exception Exception
	 */
	@SuppressWarnings({ "rawtypes", "unchecked" })
	public void deleteDpl1000DeployVerInfo(Map paramMap) throws Exception{
		
		List<Map<String,String>> list = (List<Map<String,String>>) paramMap.get("list");
		
		//prjId
		String prjId = (String)paramMap.get("prjId");
		
		int listSize = list.size();
		List<AutoBuildVO> autoList = GlobalDplListVO.getDplList();
		
		for (int i = 0; i < listSize; i++) {
			Map<String,String> dplMap = list.get(i);
			dplMap.put("prjId", prjId);
			
			//배포 Id
			String dplId = dplMap.get("dplId");
			
			//배포 계획 삭제
			dpl1000DAO.deleteDpl1000DeployVerInfo(dplMap);
			
			//해당 배포에 스케줄러 있는지 체크
			for(int j=0;j<autoList.size();j++){
				AutoBuildVO autoInfo = autoList.get(j);
				
				//자동 배포 prjId,dplId
				String targetPrjId = autoInfo.getPrjId();
				String targetDplId = autoInfo.getDplAutoAfterCd();

				//prjId, dplId check
				if(prjId.equals(targetPrjId) && dplId.equals(targetDplId)){
					//timer 종료
					Timer timer = autoInfo.getAutoDplTimer();
					timer.cancel();
					
					//해당 자동배포 info 제거
					autoList.remove(j);
					break;
				}
			}
		}
    }
	
	/**
	 * Dpl1000 배포 계획 리스트 총 건수
	 * @param Dpl2000VO - dpl1000VO
	 * @return 
	 * @exception Exception
	 */
	@Override
	public int selectDpl1000ListCnt(Dpl1000VO dpl1000VO) throws Exception{
		return dpl1000DAO.selectDpl1000ListCnt(dpl1000VO);
	}
	

	/**
	 * 배포 계획 요구사항 목록을 엑셀 조회를 한다.
	 * @params dpl1000VO
	 * @return List 배포 계획 요구사항 목록
	 * @throws Exception
	 */
	@Override
	public void selectDpl1000ExcelList(Dpl1000VO dpl1000vo, ExcelDataListResultHandler resultHandler) throws Exception {
		dpl1000DAO.selectDpl1000ExcelList(dpl1000vo, resultHandler);
	}

	
	@Override
	public List<Dpl1000VO> selectDpl1000BuildInfoList(Dpl1000VO dpl1000VO) throws Exception{
		return dpl1000DAO.selectDpl1000BuildInfoList(dpl1000VO);
	}
	
	
	@Override
	public int selectDpl1000BuildInfoListCnt(Dpl1000VO dpl1000VO) throws Exception{
		return dpl1000DAO.selectDpl1000BuildInfoListCnt(dpl1000VO);
	}
	
	/**
	 * Dpl1400 배포 계획에 배정된 JOB에 해당하는 배포 실행 이력 조회 
	 * @param param - Map
	 * @throws Exception
	 */
	@SuppressWarnings({"rawtypes" })
	public Map selectDpl1400DplJobBuildInfo(Map map)  throws Exception{
		return dpl1000DAO.selectDpl1400DplJobBuildInfo(map);
	}
	
	/**
	 * Dpl1400 배포 계획에 배정된 JOB에 해당하는 배포 실행 이력 단건 조회 
	 * @param param - Map
	 * @throws Exception
	 */
	@SuppressWarnings({"rawtypes" })
	public Map selectDpl1400DplSelBuildInfoAjax(Map map)  throws Exception{
		return dpl1000DAO.selectDpl1400DplSelBuildInfoAjax(map);
	}
	
	/**
	 * Dpl1300 배포계획에 등록된 Job 삭제 
	 * @param param - Map
	 * @throws Exception
	 */
	@SuppressWarnings("rawtypes")
	public void deleteDpl1300DplJobList(Map paramMap)  throws Exception{
		dpl1000DAO.deleteDpl1300DplJobList(paramMap);
	}
	
	/**
	 * Dpl1400 배포 계획 실행 이력 등록
	 * @param param - Map
	 * @return 
	 * @exception Exception
	 */
	public void insertDpl1400DeployJobBuildLogInfo(BuildVO buildVo) throws Exception{
		dpl1000DAO.insertDpl1400DeployJobBuildLogInfo(buildVo);
    }
	
	/**
	 * Dpl1300 dpl1300 빌드에서 stm3000 Job정보 불러올때 사용
	 * @param param - Map
	 * @throws Exception
	 */
	@SuppressWarnings({"rawtypes", "unchecked" })
	public Map selectDpl1300ToStm3000JobInfo(Map map)  throws Exception{
		return stm3000Service.selectStm3000JobInfo(map);
	}
	
	/**
	 * Dpl1000 배포 계획 실행,수정,결재 이력
	 * @param param - Map
	 * @throws Exception
	 */
	@SuppressWarnings({"rawtypes" })
	public List selectDpl1000DplHistoryList(Map inputMap)  throws Exception{
		return dpl1000DAO.selectDpl1000DplHistoryList(inputMap);
	}
	
	/**
	 * Dpl1000 배포 계획 결재 요청
	 * @param param - Map
	 * @throws Exception
	 */
	@SuppressWarnings({"rawtypes", "unchecked" })
	public void insertDpl1000DplSignRequestList(Map paramMap)  throws Exception{
		//요청자
    	paramMap.put("signRegUsrId", paramMap.get("regUsrId"));
    	
    	paramMap.put("signStsCd", "01");
    	paramMap.put("signTxt", paramMap.get("dplSignTxt"));
    	
    	//결재 등록
    	dpl2100Service.insertDpl2100DplSignWaitInfo(paramMap);
    	
    	//등록자
    	String regUsrId = (String)paramMap.get("regUsrId");
    	
    	//결재자
    	String signUsrId = (String)paramMap.get("signUsrId");
    	
    	//배포계획명
    	String dplNm = (String)paramMap.get("dplNm");
    	
    	//등록자와 결재자가 다른경우 쪽지 발송
		if(!signUsrId.equals(regUsrId)){
    		//프로젝트
    		String prjId = (String) paramMap.get("prjId");
    		
    		//배포계획
    		String dplId = (String) paramMap.get("dplId");
    		
    		//결재자 쪽지
    		Map<String,String> armMap = new HashMap<String,String>();
    		armMap.put("usrId", signUsrId);
    		armMap.put("sendUsrId", regUsrId);
    		armMap.put("title", "["+dplNm+"] 배포계획에 결재자로 지정되었습니다.");
    		armMap.put("content", "["+dplNm+"] 배포계획에 결재자로 지정되었습니다.<br>해당 배포계획을 확인해주세요.");
    		armMap.put("reqIds", "<span name='tagDplId' id='tagDplId' prj-id='"+prjId+"' dpl-id='"+dplId+"' onclick='fnSpanDplDetailOpen(this)'>"+dplNm+"<li class='fa fa-share'></li></span>");
    		
    		//쪽지 등록
    		arm1000DAO.insertArm1000AlarmInfo(armMap);
    	}
	}
	
	/**
	 * Dpl1000 Job 빌드 목록 조회
	 * @param param - Map
	 * @throws Exception
	 */
	@SuppressWarnings({"rawtypes" })
	public List selectDpl1400DplBldNumList(Map inputMap)  throws Exception{
		return dpl1000DAO.selectDpl1400DplBldNumList(inputMap);
	}
	
	/**
	 * Dpl1500 배포계획 수정이력 CHG_ID 구하기
	 * @param param - Map
	 * @return 
	 * @exception Exception
	 */	
	@SuppressWarnings("rawtypes")
	public String selectDpl1500NewChgId(Map paramMap) throws Exception{
		return dpl1000DAO.selectDpl1500NewChgId(paramMap);
	}
	
	/**
	 * Dpl1500 배포계획 수정이력 등록
	 * @param param - Map
	 * @return 
	 * @exception Exception
	 */	
	@SuppressWarnings("rawtypes")
	public String insertDpl1500ModifyHistoryInfo(Map paramMap) throws Exception{
		return dpl1000DAO.insertDpl1500ModifyHistoryInfo(paramMap);
	}
	
	
	/**
	 * Dpl1500 배포계획 항목 수정이력 등록
	 * @param param - Map
	 * @return 
	 * @exception Exception
	 */	
	@SuppressWarnings({ "rawtypes", "unchecked" })
	public void insertDpl1500DplInfoModifyList(Map paramMap) throws Exception{
		//기존 배포계획 정보 불러오기
		Map beforeDplInfo = dpl1000DAO.selectDpl1000DeployVerInfo(paramMap);
		
		//새로운 NEW_CHG_ID 구하기
		String newChgId = dpl1000DAO.selectDpl1500NewChgId(paramMap);
		paramMap.put("chgId", newChgId);
		
		//수정이력 순번
		int chgNum = 0;
		
		for( Object key : paramMap.keySet() ) {
				String jsonVal = "";
				if(paramMap.get(key) instanceof ArrayList){
					jsonVal = paramMap.get(key).toString();
				}else{
					jsonVal = (String) paramMap.get(key);
				}
				
				JSONObject jsonObj = null;
				
				//기본 항목
				Map defaultReqInfo = new HashMap();
				
				//JsonData가 아닌 기본 String으로 넘겨받은 경우 skip
				try{
					jsonObj = new JSONObject(jsonVal);
				}catch(JSONException jsonE){
					defaultReqInfo.put(key, paramMap.get(key).toString());
					continue;
				}catch(Exception e){
					continue;
				}
				//수정이력 저장 유무
				String modifyset = String.valueOf(jsonObj.get("modifySetCd"));
				
				//수정이력 저장 유무가 '02' 저장 안함일경우 skip
				if("02".equals(modifyset)){
					continue;
				}
				
				//항목 정보
				String paramVal = String.valueOf(jsonObj.get("optVal"));
				
				//기본 항목 또는 추가 항목인지 확인 opttarget 체크
				String opttarget = String.valueOf(jsonObj.get("chgDetailOptTarget"));
				String dplInfoVal = "";
				String chgTypeCd = "00";
				
				//기본 항목
				if("01".equals(opttarget)){
					//배포계획 기본 컬럼에 없는경우 skip
					if(beforeDplInfo.containsKey(key)){
						dplInfoVal = String.valueOf(beforeDplInfo.get(key));
						chgTypeCd = "01";	//배포계획 수정
					}else{
						continue;
					}
				}
				
				//넘겨 받은 값이 빈 값인지 체크
				if(paramVal == null || "".equals(paramVal) || "undefined".equals(paramVal)){
					//빈값인경우 String "" 대입
					paramVal = "";
				}
				if(dplInfoVal == null || "".equals(dplInfoVal) || "null".equals(dplInfoVal)){
					dplInfoVal = "";
				}
				
				
				//기존 요구사항 필드 값과 현재 값이 다른 경우 수정이력 쌓기
				if(!(paramVal).equals(dplInfoVal)){
					//Map세팅
					Map<String, Object> map = new HashMap<String, Object>();
					map.putAll(paramMap);
					
					//추가 정보 대입
					map.put("chgNum", chgNum);
					map.put("chgTypeCd", chgTypeCd);
					map.put("chgNm", jsonObj.get("optNm"));	//항목 명
					map.put("preVal", dplInfoVal); //기존 값
					map.put("chgVal", jsonObj.get("optVal"));	//변경 값
					map.put("chgOptTypeCd", jsonObj.get("chgDetailOptType"));	//항목 타입
					map.put("chgCommonCd", jsonObj.get("chgDetailCommonCd"));	//공통코드
					map.put("chgUsrId", paramMap.get("regUsrId"));
					
					//수정이력 등록
					dpl1000DAO.insertDpl1500ModifyHistoryInfo(map);
					
					//순번 세팅
					chgNum++;
				}
		}
		
	}
	
	
	/**
	 * Dpl1500 배포계획 수정이력 목록 조회
	 * @param param - Map
	 * @return 
	 * @exception Exception
	 */	
	@SuppressWarnings("rawtypes")
	public List selectDpl1500ModifyHistoryList(Map paramMap) throws Exception{
		return dpl1000DAO.selectDpl1500ModifyHistoryList(paramMap);
	}
	
	/**
	 * Dpl1000 모든 배포계획 자동배포 목록 가져오기
	 * @param param - Map
	 * @return 
	 * @exception Exception
	 */	
	@SuppressWarnings("rawtypes")
	public List selectDpl1000AllDplList(Map paramMap) throws Exception{
		return dpl1000DAO.selectDpl1000AllDplList(paramMap);
	}
}
