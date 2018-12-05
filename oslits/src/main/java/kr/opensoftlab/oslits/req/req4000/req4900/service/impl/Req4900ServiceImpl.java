package kr.opensoftlab.oslits.req.req4000.req4900.service.impl;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import kr.opensoftlab.oslits.arm.arm1000.arm1000.service.impl.Arm1000DAO;
import kr.opensoftlab.oslits.com.fms.web.service.FileMngService;
import kr.opensoftlab.oslits.prj.prj1000.prj1100.service.impl.Prj1100DAO;
import kr.opensoftlab.oslits.req.req4000.req4100.service.impl.Req4100DAO;
import kr.opensoftlab.oslits.req.req4000.req4900.service.Req4900Service;
import kr.opensoftlab.oslits.req.req4700.req4700.service.impl.Req4700DAO;

import org.springframework.stereotype.Service;

import egovframework.rte.fdl.cmmn.EgovAbstractServiceImpl;
import egovframework.rte.fdl.idgnr.EgovIdGnrService;


@Service("req4900Service")
public class Req4900ServiceImpl  extends EgovAbstractServiceImpl implements Req4900Service{
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
		String regUsrId = (String) paramMap.get("regUsrId"); //결재자
		//결재 요청자
		String signRegUsrId = (String) paramMap.get("signRegUsrId"); //요청자
		
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
			
			
		}else if("03".equals(signCd)){
			signCdNm = "반려";
			signRejectCmnt = "<br><br>반려사유: "+ paramMap.get("signRejectCmnt");
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
