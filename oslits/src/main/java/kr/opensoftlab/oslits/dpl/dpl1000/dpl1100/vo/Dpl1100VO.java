package kr.opensoftlab.oslits.dpl.dpl1000.dpl1100.vo;

/**
 * @Class Name : Req1000Controller.java
 * @Description : Req1000Controller Controller class
 * @Modification Information
 *
 * @author 정형택
 * @since 2016.01.29.
 * @version 1.0
 * @see
 *  
 *  Copyright (C) OpenSoftLab Corp All right reserved.
 */

import kr.opensoftlab.oslits.com.vo.PageVO;

public class Dpl1100VO extends PageVO {
	
	/** 검색 조건 define */
	private String srchEvent;  /** Event : onLoad, onSelect */

	private String srchDplId;
	
	
	/** 상세 기본 Defind */
	private String licGrpId;
	public String getLicGrpId() {
		return licGrpId;
	}

	public void setLicGrpId(String licGrpId) {
		this.licGrpId = licGrpId;
	}

	/** 개발주기정보 */
	private String prjId;
	private String sprintId;
	private String sprintNm;
	private String startDt;
	private String endDt;
	
	/** 요청사항 정보 */
	private String reqId;
	private String reqClsId;
	private String reqClsNm; 
	private String reqNm;
	private String reqDesc;
	private String flowId;
	private String flowNm;
	private String reqImprtCd;
	private String reqImprtNm;
	private String reqDtm;
	
	private String dplId;
	private String dplNm;
	
	private String reqChargerNm;
	private String reqDevWkTm;
	

	public String getReqDevWkTm() {
		return reqDevWkTm;
	}

	public void setReqDevWkTm(String reqDevWkTm) {
		this.reqDevWkTm = reqDevWkTm;
	}

	public String getReqChargerNm() {
		return reqChargerNm;
	}

	public void setReqChargerNm(String reqChargerNm) {
		this.reqChargerNm = reqChargerNm;
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

	public String getPrjId() {
		return prjId;
	}

	public void setPrjId(String prjId) {
		this.prjId = prjId;
	}

	public String getSprintId() {
		return sprintId;
	}

	public void setSprintId(String sprintId) {
		this.sprintId = sprintId;
	}

	public String getSprintNm() {
		return sprintNm;
	}

	public void setSprintNm(String sprintNm) {
		this.sprintNm = sprintNm;
	}

	public String getStartDt() {
		return startDt;
	}

	public void setStartDt(String startDt) {
		this.startDt = startDt;
	}

	public String getEndDt() {
		return endDt;
	}

	public void setEndDt(String endDt) {
		this.endDt = endDt;
	}

	public String getReqId() {
		return reqId;
	}

	public void setReqId(String reqId) {
		this.reqId = reqId;
	}

	public String getReqClsId() {
		return reqClsId;
	}

	public void setReqClsId(String reqClsId) {
		this.reqClsId = reqClsId;
	}

	public String getReqClsNm() {
		return reqClsNm;
	}

	public void setReqClsNm(String reqClsNm) {
		this.reqClsNm = reqClsNm;
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

	public String getReqImprtCd() {
		return reqImprtCd;
	}

	public void setReqImprtCd(String reqImprtCd) {
		this.reqImprtCd = reqImprtCd;
	}

	public String getReqImprtNm() {
		return reqImprtNm;
	}

	public void setReqImprtNm(String reqImprtNm) {
		this.reqImprtNm = reqImprtNm;
	}

	public String getReqDtm() {
		return reqDtm;
	}

	public void setReqDtm(String reqDtm) {
		this.reqDtm = reqDtm;
	}

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
	
	
	
	
	
}
