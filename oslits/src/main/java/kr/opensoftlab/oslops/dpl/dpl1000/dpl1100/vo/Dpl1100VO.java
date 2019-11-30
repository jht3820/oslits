package kr.opensoftlab.oslops.dpl.dpl1000.dpl1100.vo;

/**
 * @Class Name : Dpl1100VO.java
 * @Description : Dpl1100VO Controller class
 * @Modification Information
 *
 * @author 배용진
 * @since 2019.03.11.
 * @version 1.0
 * @see
 *  
 *  Copyright (C) OpenSoftLab Corp All right reserved.
 */

import kr.opensoftlab.oslops.com.vo.PageVO;

public class Dpl1100VO extends PageVO {
	
	/** 검색 조건 define */
	private String srchEvent;  /** Event : onLoad, onSelect */
	/** 검색 배포계획 ID */
	private String srchDplId;
	/** 
	 * <페이징 처리 유무>
	 * 배포계획에 배정/미배정된 요구사항 조회시 페이징 처리 필요,
	 * 요구사항 단건 조회시 해당 요구사항이 배포계획에 배정되어있는지 조회할 떄에는
	 * 페이징 처리 필요없음
	 */
	private String pagingYn;
	
	/**** 상세 기본 Defind ****/
	/** 라이선스 그룹 ID */
	private String licGrpId;
	/** 프로젝트 ID */
	private String prjId;
	
	/**** 배포 계획 정보 ****/
	/** 배포 ID */
	private String dplId;
	/** 배포 명 */
	private String dplNm;
	/** 배포 버전 */
	private String dplVer;
	/** 배포 진행상태 코드 */
	private String dplStsCd;
	/** 배포 진행상태 명 */
	private String dplStsNm;
	/** 배포 일시 */
	private String dplDt; 
	/** 배포자 DI */
	private String dplUsrId; 
	/** 배포자 명 */
	private String dplUsrNm; 
	/** 배포 방법 코드 */
	private String dplTypeCd; 
	/** 배포 방법 명 */
	private String dplTypeNm;
	/** 배포 설명 */
	private String dplDesc; 
	
	/**** 요구사항 정보 ****/
	/** 요구사항 ID */
	private String reqId;
	/** 요구사항 순번 */
	private String reqOrd;
	/** 공문번호 */
	private String reqNo;
	/** 요구사항 명 */
	private String reqNm;
	/** 요구사항 내용 */
	private String reqDesc;
	/** 담당자 ID */
	private String reqChargerId;
	/** 담당자 명 */
	private String reqChargerNm;
	/** 요청자 ID */
	private String reqUsrID;
	/** 요청자 명 */
	private String reqUsrNm;
	/** 요청일자 */
	private String reqDtm;
	/** 작업 시작 일자 */
	private String reqStDtm;
	/** 작업 종료 일자 */
	private String reqEdDtm;
	/** 작업 시작 예정일자 */
	private String reqStDuDtm;
	/** 작업 종료 예정일자 */
	private String reqEdDuDtm;
	/** 요구사항 유형 코드 */
	private String reqTypeCd;
	/** 요구사항 유형 코드 명 */
	private String reqTypeNm;
	/** 요구사항 처리유형 코드 */
	private String reqProType; 
	/** 요구사항 처리유형 명 */
	private String reqProTypeNm;
	/** 요구사항 접수유형 코드 */
	private String reqNewType;
	/** 요구사항 접수유형 명 */
	private String reqNewTypeNm;
	/** 프로세스 ID */
	private String processId;
	/** 프로세스 명 */
	private String processNm;
	/** 작업흐름 ID */
	private String flowId;
	/** 작업흐름 명 */
	private String flowNm;
	
	public String getSrchEvent() {
		return srchEvent;
	}
	public void setSrchEvent(String srchEvent) {
		this.srchEvent = srchEvent;
	}
	public String getSrchDplId() {
		return srchDplId;
	}
	public void setSrchDplId(String srchDplId) {
		this.srchDplId = srchDplId;
	}
	public String getPagingYn() {
		return pagingYn;
	}
	public void setPagingYn(String pagingYn) {
		this.pagingYn = pagingYn;
	}
	public String getLicGrpId() {
		return licGrpId;
	}
	public void setLicGrpId(String licGrpId) {
		this.licGrpId = licGrpId;
	}
	public String getPrjId() {
		return prjId;
	}
	public void setPrjId(String prjId) {
		this.prjId = prjId;
	}
	public String getDplId() {
		return dplId;
	}
	public void setDplId(String dplId) {
		this.dplId = dplId;
	}
	public String getDplNm() {
		return dplNm;
	}
	public void setDplNm(String dplNm) {
		this.dplNm = dplNm;
	}
	public String getDplVer() {
		return dplVer;
	}
	public void setDplVer(String dplVer) {
		this.dplVer = dplVer;
	}
	public String getDplStsCd() {
		return dplStsCd;
	}
	public void setDplStsCd(String dplStsCd) {
		this.dplStsCd = dplStsCd;
	}
	public String getDplStsNm() {
		return dplStsNm;
	}
	public void setDplStsNm(String dplStsNm) {
		this.dplStsNm = dplStsNm;
	}
	public String getDplDt() {
		return dplDt;
	}
	public void setDplDt(String dplDt) {
		this.dplDt = dplDt;
	}
	public String getDplUsrId() {
		return dplUsrId;
	}
	public void setDplUsrId(String dplUsrId) {
		this.dplUsrId = dplUsrId;
	}
	public String getDplUsrNm() {
		return dplUsrNm;
	}
	public void setDplUsrNm(String dplUsrNm) {
		this.dplUsrNm = dplUsrNm;
	}
	public String getDplTypeCd() {
		return dplTypeCd;
	}
	public void setDplTypeCd(String dplTypeCd) {
		this.dplTypeCd = dplTypeCd;
	}
	public String getDplTypeNm() {
		return dplTypeNm;
	}
	public void setDplTypeNm(String dplTypeNm) {
		this.dplTypeNm = dplTypeNm;
	}
	public String getDplDesc() {
		return dplDesc;
	}
	public void setDplDesc(String dplDesc) {
		this.dplDesc = dplDesc;
	}
	public String getReqId() {
		return reqId;
	}
	public void setReqId(String reqId) {
		this.reqId = reqId;
	}
	public String getReqOrd() {
		return reqOrd;
	}
	public void setReqOrd(String reqOrd) {
		this.reqOrd = reqOrd;
	}
	public String getReqNo() {
		return reqNo;
	}
	public void setReqNo(String reqNo) {
		this.reqNo = reqNo;
	}
	public String getReqNm() {
		return reqNm;
	}
	public void setReqNm(String reqNm) {
		this.reqNm = reqNm;
	}
	public String getReqDesc() {
		return reqDesc;
	}
	public void setReqDesc(String reqDesc) {
		this.reqDesc = reqDesc;
	}
	public String getReqChargerId() {
		return reqChargerId;
	}
	public void setReqChargerId(String reqChargerId) {
		this.reqChargerId = reqChargerId;
	}
	public String getReqChargerNm() {
		return reqChargerNm;
	}
	public void setReqChargerNm(String reqChargerNm) {
		this.reqChargerNm = reqChargerNm;
	}
	public String getReqUsrID() {
		return reqUsrID;
	}
	public void setReqUsrID(String reqUsrID) {
		this.reqUsrID = reqUsrID;
	}
	public String getReqUsrNm() {
		return reqUsrNm;
	}
	public void setReqUsrNm(String reqUsrNm) {
		this.reqUsrNm = reqUsrNm;
	}
	public String getReqDtm() {
		return reqDtm;
	}
	public void setReqDtm(String reqDtm) {
		this.reqDtm = reqDtm;
	}
	public String getReqStDtm() {
		return reqStDtm;
	}
	public void setReqStDtm(String reqStDtm) {
		this.reqStDtm = reqStDtm;
	}
	public String getReqEdDtm() {
		return reqEdDtm;
	}
	public void setReqEdDtm(String reqEdDtm) {
		this.reqEdDtm = reqEdDtm;
	}
	public String getReqStDuDtm() {
		return reqStDuDtm;
	}
	public void setReqStDuDtm(String reqStDuDtm) {
		this.reqStDuDtm = reqStDuDtm;
	}
	public String getReqEdDuDtm() {
		return reqEdDuDtm;
	}
	public void setReqEdDuDtm(String reqEdDuDtm) {
		this.reqEdDuDtm = reqEdDuDtm;
	}
	public String getReqTypeCd() {
		return reqTypeCd;
	}
	public void setReqTypeCd(String reqTypeCd) {
		this.reqTypeCd = reqTypeCd;
	}
	public String getReqTypeNm() {
		return reqTypeNm;
	}
	public void setReqTypeNm(String reqTypeNm) {
		this.reqTypeNm = reqTypeNm;
	}
	public String getReqProType() {
		return reqProType;
	}
	public void setReqProType(String reqProType) {
		this.reqProType = reqProType;
	}
	public String getReqProTypeNm() {
		return reqProTypeNm;
	}
	public void setReqProTypeNm(String reqProTypeNm) {
		this.reqProTypeNm = reqProTypeNm;
	}
	public String getReqNewType() {
		return reqNewType;
	}
	public void setReqNewType(String reqNewType) {
		this.reqNewType = reqNewType;
	}
	public String getReqNewTypeNm() {
		return reqNewTypeNm;
	}
	public void setReqNewTypeNm(String reqNewTypeNm) {
		this.reqNewTypeNm = reqNewTypeNm;
	}
	public String getProcessId() {
		return processId;
	}
	public void setProcessId(String processId) {
		this.processId = processId;
	}
	public String getProcessNm() {
		return processNm;
	}
	public void setProcessNm(String processNm) {
		this.processNm = processNm;
	}
	public String getFlowId() {
		return flowId;
	}
	public void setFlowId(String flowId) {
		this.flowId = flowId;
	}
	public String getFlowNm() {
		return flowNm;
	}
	public void setFlowNm(String flowNm) {
		this.flowNm = flowNm;
	}

}
