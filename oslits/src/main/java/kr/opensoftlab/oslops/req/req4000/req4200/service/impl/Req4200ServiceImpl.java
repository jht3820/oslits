package kr.opensoftlab.oslops.req.req4000.req4200.service.impl;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import kr.opensoftlab.oslops.req.req4000.req4200.service.Req4200Service;
import kr.opensoftlab.oslops.req.req4000.req4200.vo.Req4200VO;
import kr.opensoftlab.oslops.req.req4000.req4800.service.impl.Req4800DAO;

import org.codehaus.jettison.json.JSONArray;
import org.codehaus.jettison.json.JSONObject;
import org.springframework.stereotype.Service;

import com.fasterxml.jackson.databind.ObjectMapper;

import egovframework.com.cmm.EgovMessageSource;
import egovframework.rte.fdl.cmmn.EgovAbstractServiceImpl;

/**
 * @Class Name : Req4200ServiceImpl.java
 * @Description : Req4200ServiceImpl Business Implement class
 * @Modification Information
 *
 * @author 정형택
 * @since 2016.01.12.
 * @version 1.0
 * @see
 *  
 *  Copyright (C) OpenSoftLab Corp. All right reserved.
 */

@Service("req4200Service")
public class Req4200ServiceImpl extends EgovAbstractServiceImpl implements Req4200Service {

	/** Req4200DAO DI */
    @Resource(name="req4200DAO")
    private Req4200DAO req4200DAO;
    
    /** Req4800DAO DI */
    @Resource(name="req4800DAO")
    private Req4800DAO req4800DAO;
    
    /** EgovMessageSource */
	@Resource(name = "egovMessageSource")
	EgovMessageSource egovMessageSource;

    /**
	 * Req4200 요구사항 분류 정보 목록 가져오기
	 * @param param - Map
	 * @return 
	 * @exception Exception
	 */
	@SuppressWarnings("rawtypes")
	public List selectReq4200ReqClsList(Map paramMap) throws Exception {
		return req4200DAO.selectReq4200ReqClsList(paramMap) ;
    }
	
	/**
	 * Req4000 요구사항 분류 배정 목록 가져오기
	 * @param param - Map
	 * @return 
	 * @exception Exception
	 */
	@SuppressWarnings("rawtypes")
	public List selectReq4200ReqClsAddListAjax(Req4200VO req4200VO) throws Exception{
		return req4200DAO.selectReq4200ReqClsAddListAjax(req4200VO) ;
	}
	
	/**
	 * Req4200 분류에 배정된 요구사항 총 건수 조회
	 * @param param - Map
	 * @return 
	 * @exception Exception
	 */
	public int selectReq4200ReqClsAddListCnt(Req4200VO req4200VO) throws Exception {
		return req4200DAO.selectReq4200ReqClsAddListCnt(req4200VO);
	}
	
	/**
	 * Req4000 요구사항 분류 미배정 목록 가져오기
	 * @param param - Map
	 * @return 
	 * @exception Exception
	 */
	@SuppressWarnings("rawtypes")
	public List selectReq4200ReqClsDelListAjax(Req4200VO req4200VO) throws Exception{
		return req4200DAO.selectReq4200ReqClsDelListAjax(req4200VO) ;
	}
	
	/**
	 * Req4200 분류에 미배정된 요구사항 총 건수 조회
	 * @param param - Map
	 * @return 
	 * @exception Exception
	 */
	public int selectReq4200ReqClsDelListCnt(Req4200VO req4200VO) throws Exception {
		return req4200DAO.selectReq4200ReqClsDelListCnt(req4200VO);
	}
	
	/**
	 * Req4200 요구사항 분류에 요구사항 배정 및 삭제 처리
	 * @param param - Map
	 * @return 
	 * @exception Exception
	 */
	@SuppressWarnings({ "unchecked", "rawtypes" })
	public void updateReq4200ReqClsAddDelListAjax(Map paramMap) throws Exception{

		// 프로젝트 ID를 가져온다.
		String prjId = (String)paramMap.get("prjId");
				
		// 배정,미배정 요구사항 목록
		JSONArray selReqList = new JSONArray(paramMap.get("selReqList").toString());

		// 요구사항을 배정, 배정제외 한다.
    	for(int i=0; i < selReqList.length(); i++){
    		
    		JSONObject jsonObj = selReqList.getJSONObject(i);
    		HashMap<String, Object> reqInfoMap = new ObjectMapper().readValue(jsonObj.toString(), HashMap.class) ;

    		// 요구사항 배정, 배정제외를 구분하기 위한 구분값
    		String type = String.valueOf(jsonObj.get("type"));
    		
    		// 요구사항 분류 Id, 분류명을 가져온다.
    		String reqClsId = String.valueOf(jsonObj.get("reqClsId"));
    		String reqClsNm = String.valueOf(jsonObj.get("reqClsNm"));
    		
    		// 사용자 Id, Ip를 가져온다.
    		String usrId = (String)paramMap.get("modifyUsrId");
    		String usrIp = (String)paramMap.get("modifyUsrIp");
 
    		// 수정이력에 사용될 변경전 값, 변경후 값
    		String preDetailVal = null;
    		String chgDetailVal = null;
    		
    		// 분류에 배정할 경우
    		if("add".equals(type)) {
    			// 변경 후 값에 분류명을 담는다.
    			chgDetailVal = reqClsNm;
    		// 분류에서 배정제외일 경우
    		}else if("del".equals(type)) {
    			// 분류ID null로 변경
    			reqClsId = null;
    			// 변경 전 값에 분류명을 담는다.
    			preDetailVal = reqClsNm;
    		}
    		
    		// Map에 정보 세팅
    		reqInfoMap.put("prjId", prjId);
    		reqInfoMap.put("reqId", String.valueOf(jsonObj.get("reqId")));
    		reqInfoMap.put("reqClsId", reqClsId);
    		// 최종 수정자 ID, IP 세팅
    		reqInfoMap.put("modifyUsrId", usrId);
    		reqInfoMap.put("modifyUsrIp", usrIp);
    		
    		// 분류에 요구사항 배정/배정제외 한다.
    		req4200DAO.updateReq4200ReqClsAddDelListAjax(reqInfoMap);
    		
    		// 새로운 수정이력 ID 생성
    		String newChgId = req4800DAO.selectReq4800NewChgDetailId(reqInfoMap);
    		// 수정이력 정보 세팅
    		// 수정이력 ID
    		reqInfoMap.put("newChgDetailId", newChgId);	
    		// 수정이력 순번 - 분류 항목 1개만 수정하는것이므로 순번은 0으로 고정
    		reqInfoMap.put("chgDetailNum", "0");	
    		// 수정이력 구분 - 요구사항 수정이므로 01(요구사항 수정) 세팅
    		reqInfoMap.put("chgDetailType", "01");
    		// 수정이력 항목 명 
    		reqInfoMap.put("chgDetailNm", "요구사항 분류");
    		// 수정이력 항목 타입 - 요구사항 분류는 기본항목이므로 01 세팅
    		reqInfoMap.put("chgDetailOptType", "01");
    		// 수정된 분류의 변경전값, 변경후값 세팅
    		reqInfoMap.put("preDetailVal", preDetailVal);
    		reqInfoMap.put("chgDetailVal", chgDetailVal);
    		reqInfoMap.put("regUsrId", usrId);
    		reqInfoMap.put("regUsrIp", usrIp);
    		
    		// 요구사항의 수정이력을 등록한다.
    		req4800DAO.insertReq4800ModifyHistoryInfo(reqInfoMap);
    	}
	}
}
