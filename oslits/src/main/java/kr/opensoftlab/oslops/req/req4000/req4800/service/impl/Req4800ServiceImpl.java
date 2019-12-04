package kr.opensoftlab.oslops.req.req4000.req4800.service.impl;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import kr.opensoftlab.oslops.dpl.dpl1000.dpl1100.service.impl.Dpl1100DAO;
import kr.opensoftlab.oslops.prj.prj1000.prj1100.service.impl.Prj1100DAO;
import kr.opensoftlab.oslops.req.req4000.req4100.service.impl.Req4100DAO;
import kr.opensoftlab.oslops.req.req4000.req4800.service.Req4800Service;

import org.codehaus.jettison.json.JSONException;
import org.codehaus.jettison.json.JSONObject;
import org.springframework.stereotype.Service;

import egovframework.com.cmm.service.FileVO;
import egovframework.rte.fdl.cmmn.EgovAbstractServiceImpl;


@Service("req4800Service")
public class Req4800ServiceImpl  extends EgovAbstractServiceImpl implements Req4800Service{

	private static final String MOD_CHG_GB_CD  = "01"; // 요구사항 수정
	private static final String FLOW_MOD_CHG_GB_CD  = "02"; // 추가항목 수정
	private static final String FILE_ADD_CHG_GB_CD  = "03"; // 첨부파일 추가
	private static final String FILE_DEL_CHG_GB_CD  = "04"; // 첨부파일 삭제
	private static final String DPL_CHG_GB_CD  = "05"; // 배포
	//private final String WORK_ADD_CHG_GB_CD  = "05"; // 작업 추가
	//private final String WORK_MOD_CHG_GB_CD  = "05"; // 작업 수정
	
    @Resource(name="req4100DAO")
    private Req4100DAO req4100DAO;
    
    @Resource(name="prj1100DAO")
    private Prj1100DAO prj1100DAO;
    
    @Resource(name="req4800DAO")
    private Req4800DAO req4800DAO;
    
    @Resource(name="dpl1100DAO")
    private Dpl1100DAO dpl1100DAO;
    
    /**
	 * Req4800 요구사항 수정이력 목록 가져오기
	 * @param param - Map
	 * @return 
	 * @exception Exception
	 */	
	@SuppressWarnings("rawtypes")
	public List<Map> selectReq4800ChgDetailList(Map paramMap) throws Exception{
		return req4800DAO.selectReq4800ChgDetailList(paramMap);
	}
	
    /**
	 * Req4800 요구사항 변경사항 검색 후 수정이력 등록
	 * @param param - Map
	 * @return 
	 * @exception Exception
	 */	
	@SuppressWarnings({ "rawtypes", "unchecked" })
	@Override
	public void insertReq4800ModifyHistoryList(Map paramMap) throws Exception{

		//기존 요구사항 정보 불러오기
		Map reqInfoMap = (Map) req4100DAO.selectReq4102ReqInfoAjax(paramMap);
		
		//추가 항목 정보 불러오기
		List<Map> addOptList = (List<Map>) prj1100DAO.selectFlw1200ReqOptList(paramMap);
		
		//List to Map
		Map addOptMap = new HashMap<>();
		for (Map addOptInfo : addOptList) {
			addOptMap.put(addOptInfo.get("itemId"),addOptInfo.get("itemValue"));
		}
		
		//배포 정보 정보 불러오기
		List<Map> dplList = (List<Map>) dpl1100DAO.selectDpl1100ReqDplList(paramMap);
		      
		//List to Map
		Map dplMap = new HashMap<>();
		for (Map dplInfo : dplList) {
		   dplMap.put("dplId_"+dplInfo.get("flowId"),dplInfo.get("dplId"));
		}
		
		//새로운 NEW_CHG_DETAIL_ID 구하기
		String newChgDetailId = req4800DAO.selectReq4800NewChgDetailId(paramMap);
		paramMap.put("newChgDetailId", newChgDetailId);
		
		//수정이력 순번
		int chgDetailNum = 0;
		
		for( Object key : paramMap.keySet() ) {
			//파일인경우 첨부파일 수정이력 추가
			if(key.equals("fileList")){
				String fileActionType = (String)paramMap.get("fileActionType");
				String CHG_GB_CD = "00";
				if("add".equals(fileActionType)){
					CHG_GB_CD = FILE_ADD_CHG_GB_CD;
				}
				else if("del".equals(fileActionType)){
					CHG_GB_CD = FILE_DEL_CHG_GB_CD;
				}
				
				//첨부파일 목록
				List<FileVO> files = (List<FileVO>)paramMap.get(key);
				if(files != null){
					for(FileVO vo : files ){
						//Map세팅
						Map<String, Object> map = new HashMap<String, Object>();
						map.putAll(paramMap);
						
						//추가 정보 대입
						map.put("chgDetailNum", chgDetailNum);
						map.put("chgDetailType", CHG_GB_CD);	//첨부파일
						map.put("chgDetailNm", "첨부 파일");	//항목 명
						map.put("chgDetailVal", vo.getOrignlFileNm());	//변경 값
						
						//로그 등록
						req4800DAO.insertReq4800ModifyHistoryInfo(map);
						
						//순번 세팅
						chgDetailNum++;
					}
				}
				
			}else{	//그 외에 항목 수정인경우
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
				String reqInfoVal = "";
				String chgDetailType = "00";
				
				//기본 항목
				if("01".equals(opttarget)){
					//요구사항 기본 컬럼에 없는경우 skip
					if(reqInfoMap.containsKey(key)){
						reqInfoVal = String.valueOf(reqInfoMap.get(key));
						chgDetailType = MOD_CHG_GB_CD;
					}else{
						continue;
					}
				}
				//추가 항목
				else if("02".equals(opttarget)){
					reqInfoVal = String.valueOf(addOptMap.get(key));
					chgDetailType = FLOW_MOD_CHG_GB_CD;
				}
				//배포
				else if("03".equals(opttarget)){
					reqInfoVal = String.valueOf(dplMap.get(key));
					chgDetailType = DPL_CHG_GB_CD;
				}
				
				//넘겨 받은 값이 빈 값인지 체크
				if(paramVal == null || "".equals(paramVal) || "undefined".equals(paramVal)){
					//빈값인경우 String "" 대입
					paramVal = "";
				}
				if(reqInfoVal == null || "".equals(reqInfoVal) || "null".equals(reqInfoVal)){
					reqInfoVal = "";
				}
				
				
				//기존 요구사항 필드 값과 현재 값이 다른 경우 수정이력 쌓기
				if(!(paramVal).equals(reqInfoVal)){
					//Map세팅
					Map<String, Object> map = new HashMap<String, Object>();
					map.putAll(paramMap);
					
					//추가 정보 대입
					map.put("chgDetailNum", chgDetailNum);
					map.put("chgDetailType", chgDetailType);
					map.put("chgDetailNm", jsonObj.get("optNm"));	//항목 명
					map.put("preDetailVal", reqInfoVal); //기존 값
					map.put("chgDetailVal", jsonObj.get("optVal"));	//변경 값
					map.put("chgDetailOptType", jsonObj.get("chgDetailOptType"));	//항목 타입
					map.put("chgDetailCommonCd", jsonObj.get("chgDetailCommonCd"));	//공통코드
					
					//로그 등록
					req4800DAO.insertReq4800ModifyHistoryInfo(map);
					
					//순번 세팅
					chgDetailNum++;
				}
				
			}
		}
		
		
	}
	
	/**
	 * Req4800 요구사항 수정이력 등록
	 * @param param - Map
	 * @return 
	 * @exception Exception
	 */	
	@SuppressWarnings("rawtypes")
	@Override
	public String insertReq4800ModifyHistoryInfo(Map paramMap) throws Exception{
		return req4800DAO.insertReq4800ModifyHistoryInfo(paramMap);
	}
	
	
	/**
	 * Req4800 요구사항 수정이력 삭제
	 * - 요구사항 삭제 시 해당 요구사항의 수정이력도 삭제한다.
	 * @param param - Map
	 * @return 
	 * @exception Exception
	 */
	@SuppressWarnings("rawtypes")
	public int deleteReq4800ReqHistoryInfo(Map paramMap) throws Exception{
		return req4800DAO.deleteReq4800ReqHistoryInfo(paramMap);
	}
}
