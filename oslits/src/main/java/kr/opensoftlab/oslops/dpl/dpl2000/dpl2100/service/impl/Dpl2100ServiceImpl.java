package kr.opensoftlab.oslops.dpl.dpl2000.dpl2100.service.impl;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Timer;

import javax.annotation.Resource;

import kr.opensoftlab.oslops.arm.arm1000.arm1000.service.impl.Arm1000DAO;
import kr.opensoftlab.oslops.com.exception.UserDefineException;
import kr.opensoftlab.oslops.dpl.dpl1000.dpl1000.service.impl.Dpl1000DAO;
import kr.opensoftlab.oslops.dpl.dpl2000.dpl2100.service.Dpl2100Service;
import kr.opensoftlab.oslops.dpl.dpl2000.dpl2100.vo.Dpl2100VO;
import kr.opensoftlab.sdf.jenkins.service.BuildService;
import kr.opensoftlab.sdf.jenkins.task.AutoJobBuildTask;
import kr.opensoftlab.sdf.jenkins.vo.AutoBuildVO;
import kr.opensoftlab.sdf.jenkins.vo.GlobalDplListVO;

import org.springframework.stereotype.Service;

import egovframework.rte.fdl.cmmn.EgovAbstractServiceImpl;

/**
 * @Class Name : Dpl2100ServiceImpl.java
 * @Description : Dpl2100ServiceImpl Business Implement class
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
@Service("dpl2100Service")
public class Dpl2100ServiceImpl  extends EgovAbstractServiceImpl implements Dpl2100Service{
	/** DAO Bean Injection */
    @Resource(name="arm1000DAO")
    private Arm1000DAO arm1000DAO;
    
	/** Dpl1000DAO DI */
    @Resource(name="dpl1000DAO")
    private Dpl1000DAO dpl1000DAO;
    
    /** Dpl2100DAO DI */
    @Resource(name="dpl2100DAO")
    private Dpl2100DAO dpl2100DAO;
    
	/** BuildService DI */
	@Resource(name = "buildService")
	private BuildService buildService;
	
    /**
	 * Dpl2100 배포계획 결재 목록 가져오기 
	 * @param param - Map
	 * @return list 배포자 리스트
	 * @throws Exception
	 */
	@SuppressWarnings({"rawtypes" })
	public List selectDpl2100SignList(Dpl2100VO dpl2100vo)  throws Exception{
		return dpl2100DAO.selectDpl2100SignList(dpl2100vo);
	}
	
	/**
	 * Dpl2100 배포계획 결재 목록 총건수
	 * @param Dpl2100VO - dpl1000VO
	 * @return 
	 * @exception Exception
	 */
	public int selectDpl2100SignListCnt(Dpl2100VO dpl2100vo) throws Exception {
		return dpl2100DAO.selectDpl2100SignListCnt(dpl2100vo);
	}
	
	/**
	 * Dpl2100 배포계획 결재 디테일 정보
	 * @param param - Map
	 * @return list 배포 계획 리스트
	 * @throws Exception
	 */
	@SuppressWarnings({"rawtypes" })
	public Map selectDpl2100SignInfoAjax(Map map)  throws Exception{
		return dpl2100DAO.selectDpl2100SignInfoAjax(map);
	} 
	
	/**
	 * Dpl2100 배포계획 결재자 등록 (dpl1000에서 대기상태 등록)
	 * @param param - Map
	 * @return 
	 * @exception Exception
	 */
	@SuppressWarnings({ "rawtypes", "unchecked" })
	public void insertDpl2100DplSignWaitInfo(Map paramMap) throws Exception{
		//결재 기안
		paramMap.put("signRejectTxt", "");
		
		dpl2100DAO.insertDpl2100DplSignInfo(paramMap);
	}
	
	/**
	 * Dpl2100 배포계획 결재자 등록 (승인&반려)
	 * @param param - Map
	 * @return 
	 * @exception Exception
	 */
	@SuppressWarnings({ "rawtypes", "unchecked" })
	public void insertDpl2100DplSignInfo(Map paramMap) throws Exception{
		//자동 배포 등록
		boolean autoDplInsert = false;
		
		//결재 정보 조회
		Map requestSignInfo = dpl2100DAO.selectDpl2100SignInfoAjax(paramMap);
		
		//accept or reject
		String type = (String)paramMap.get("type");
		
		String signCdNm = "승인";
		String signRejectCmnt = "";	//반려사유

		//prjId
		String prjId = (String)paramMap.get("prjId");
		
		//배포계획 ID
		String dplId = (String)paramMap.get("dplId");
		
		//배포계획명
		String dplNm = (String)requestSignInfo.get("dplNm");
		
		//결재자
		String signUsrId = (String) requestSignInfo.get("signUsrId");
		
		//결재 요청자
		String signRegUsrId = (String) requestSignInfo.get("signRegUsrId");
		
		requestSignInfo.remove("signStsCd");
		
		//배포 정보
		Map dplInfo = null;
		
		//결재 승인
		if("accept".equals(type)){
			//배포 정보 불러오기
			dplInfo = dpl1000DAO.selectDpl1000DeployVerInfo(paramMap);
			
			String dplTypeCd = (String)dplInfo.get("dplTypeCd");
			
			//배포 방법이 자동인경우 배포 스케줄러 등록 Flag = true
			if("01".equals(dplTypeCd)){
				autoDplInsert = true;
			}
			//승인사유
			String signAcceptTxt = (String)paramMap.get("signTxt");
			
			signCdNm = "승인";
			signRejectCmnt = "</br></br>승인사유: "+ signAcceptTxt;
			requestSignInfo.remove("signTxt");
			requestSignInfo.put("signTxt", signAcceptTxt);
			requestSignInfo.put("signStsCd", "02");
		}
		//결재 반려
		else if("reject".equals(type)){
			//반려사유
			String signRejectTxt = (String)paramMap.get("signRejectTxt");
			
			signCdNm = "반려";
			signRejectCmnt = "</br></br>반려사유: "+ signRejectTxt;
			
			requestSignInfo.put("signRejectTxt", signRejectTxt);
			requestSignInfo.put("signStsCd", "03");
			
		}else{
			//그 외에 타입 오류
			throw new UserDefineException();
		}
		
		//결재 요청자가 자신경우 쪽지 발송 안함
		if(!signUsrId.equals(signRegUsrId)){
			//현재 담당자에게 쪽지 발송
			Map<String,String> armMap = new HashMap<String,String>();
    		armMap.put("usrId", signRegUsrId);
    		armMap.put("sendUsrId", signUsrId);
    		armMap.put("title", "["+dplNm+"] 배포계획의 결재가 "+signCdNm+" 되었습니다.");
    		armMap.put("content", "["+dplNm+"]  배포계획의 결재가 "+signCdNm+" 되었습니다.<br>해당 배포계획을 확인해주세요."+signRejectCmnt);
    		armMap.put("reqIds", "<span name='tagDplId' id='tagDplId' prj-id='"+prjId+"' dpl-id='"+dplId+"' onclick='fnSpanDplDetailOpen(this)'>"+dplNm+"<li class='fa fa-share'></li></span>");
    		
			//쪽지 등록
			arm1000DAO.insertArm1000AlarmInfo(armMap);
		}
		
		//결재 등록
		dpl2100DAO.insertDpl2100DplSignInfo(requestSignInfo);
		
		//자동 배포 등록하기
		if(autoDplInsert){
			//배포VO
			AutoBuildVO autoBuildVo = buildService.selectAutoDplJobListSetting(dplInfo);
			
			AutoJobBuildTask autoJobBuildTask = new AutoJobBuildTask();
			autoJobBuildTask.setAutoBuildVo(autoBuildVo);
			
			Timer bldTimer = autoBuildVo.getAutoDplTimer();
			Long arriveTime = autoBuildVo.getArriveTime();
			
			//실행 예정 시간이 0보다 작은경우 실행 안함
			if(arriveTime > 0){
				//작업 시작
				bldTimer.schedule(autoJobBuildTask, arriveTime);
				
				//task목록에 넣기
				GlobalDplListVO.getDplList().add(autoBuildVo);
			}
		}
	}
}
