package kr.opensoftlab.oslits.req.req1000.req1000.service.impl;

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

import kr.opensoftlab.oslits.arm.arm1000.arm1000.service.impl.Arm1000DAO;
import kr.opensoftlab.oslits.req.req1000.req1000.service.Req1000Service;
import kr.opensoftlab.oslits.req.req1000.req1000.vo.Req1000VO;
import kr.opensoftlab.oslits.req.req4000.req4800.service.Req4800Service;
import kr.opensoftlab.sdf.excel.ExcelDataListResultHandler;

import org.codehaus.jettison.json.JSONException;
import org.codehaus.jettison.json.JSONObject;
import org.springframework.stereotype.Service;

import egovframework.com.cmm.EgovMessageSource;
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
	@SuppressWarnings({ "rawtypes" })
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
		
		//구분이 등록이면
		if("insert".equals(popupType)){
			
			// 요구사항 순번 조회
			String nextOrd = req1000DAO.selectReq1000NextReqOrd(convertParamMap);
			String newReqOrd = nextOrd;
			
			// 프로젝트 약어가 있다면 '-' 를 붙여서 요구사항 순번 생성
			if(prjAcrm != null && !"".equals(prjAcrm)){
				newReqOrd = prjAcrm+"-"+nextOrd;
			}
			
			convertParamMap.put("reqOrd", newReqOrd);

			String insNewReqId = req1000DAO.insertReq1001ReqInfo(convertParamMap);
			
			//생성된 키가 없으면 튕겨냄
			if(insNewReqId == null || "".equals(insNewReqId)){
				throw new Exception(egovMessageSource.getMessage("fail.common.insert"));
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
	@SuppressWarnings("unchecked")
	@Override
	public void deleteReq1000ReqInfo(Map<String, Object> paramMap)  throws Exception{
		
		String prjId = (String)paramMap.get("prjId");
		
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
	
}
