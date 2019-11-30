package kr.opensoftlab.oslops.req.req1000.req1000.vo;

/**
 * @Class Name : Req1000Controller.java
 * @Description : Req1000Controller Controller class
 * @Modification Information
 *
 * @author 정형택
 * @since 2016.01.23.
 * @version 1.0
 * @see
 *  
 *  Copyright (C) OpenSoftLab Corp All right reserved.
 */

import kr.opensoftlab.oslops.com.vo.PageVO;

public class Req1000VO extends PageVO {
	
	/** 검색 조건 define */
	private String srchEvent;  /** Event : onLoad, onSelect */
	private String srchReqNm;
	private String srchReqChargerNm;
	private String srchFromDt;
	private String srchToDt;
	private String mode;
	
	/** 상세 기본 Defind */
	private String rn;
	private String licGrpId;
	private String prjId;
	private String prjNm;
	private String selPrjId;
	private String popupPrjId;
	private String reqId;
	private String reqOrd;
	private String reqClsId;
	private String reqClsNm;	
	private String reqNo;
	private String reqUsrId;
	private String reqUsrNm;
	private String reqDtm;
	private String reqUsrDeptNm;
	private String reqUsrPositionNm;
	private String reqUsrDutyNm;
	private String reqUsrEmail;
	private String reqUsrNum;
	private String reqNm;
	private String reqDesc;
	private String reqChargerId;
	private String reqCompleteRatio;
	private String reqFp;
	private String reqExFp;
	private String reqStDtm;
	private String reqEdDtm;
	private String reqStDuDtm;
	private String reqEdDuDtm;
	private String reqProType;
	private String reqProTypeNm;
	private String reqNewType;
	private String reqNewTypeNm;
	private String reqAcceptTxt;
	private String sclCd;
	private String reqTypeCd;
	private String piaCd;
	private String labInp;
	private String atchFileId;
	private String cbAtchFileId;
	private String milestoneId;
	private String processId;
	private String processNm;
	private String flowId;
	private String flowNm;
	private String useCd;
	private String loginUsrId;
	private String regDtmDay;
	private String reqKey;

	public String getSrchEvent() {
		return srchEvent;
	}
	public void setSrchEvent(String srchEvent) {
		this.srchEvent = srchEvent;
	}
	public String getSrchReqNm() {
		return srchReqNm;
	}
	public void setSrchReqNm(String srchReqNm) {
		this.srchReqNm = srchReqNm;
	}
	public String getSrchReqChargerNm() {
		return srchReqChargerNm;
	}
	public void setSrchReqChargerNm(String srchReqChargerNm) {
		this.srchReqChargerNm = srchReqChargerNm;
	}
	public String getSrchFromDt() {
		return srchFromDt;
	}
	public void setSrchFromDt(String srchFromDt) {
		this.srchFromDt = srchFromDt;
	}
	public String getSrchToDt() {
		return srchToDt;
	}
	public void setSrchToDt(String srchToDt) {
		this.srchToDt = srchToDt;
	}
	
	public String getMode() {
		return mode;
	}
	public void setMode(String mode) {
		this.mode = mode;
	}
	public String getRn() {
		return rn;
	}
	public void setRn(String rn) {
		this.rn = rn;
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
	public String getSelPrjId() {
		return selPrjId;
	}
	public void setSelPrjId(String selPrjId) {
		this.selPrjId = selPrjId;
	}
	
	public String getPopupPrjId() {
		return popupPrjId;
	}
	public void setPopupPrjId(String popupPrjId) {
		this.popupPrjId = popupPrjId;
	}
	public String getPrjNm() {
		return prjNm;
	}
	public void setPrjNm(String prjNm) {
		this.prjNm = prjNm;
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
	public String getReqNo() {
		return reqNo;
	}
	public void setReqNo(String reqNo) {
		this.reqNo = reqNo;
	}
	public String getReqUsrId() {
		return reqUsrId;
	}
	public void setReqUsrId(String reqUsrId) {
		this.reqUsrId = reqUsrId;
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
	public String getReqUsrDeptNm() {
		return reqUsrDeptNm;
	}
	public void setReqUsrDeptNm(String reqUsrDeptNm) {
		this.reqUsrDeptNm = reqUsrDeptNm;
	}
	
	public String getReqUsrPositionNm() {
		return reqUsrPositionNm;
	}
	public void setReqUsrPositionNm(String reqUsrPositionNm) {
		this.reqUsrPositionNm = reqUsrPositionNm;
	}
	public String getReqUsrDutyNm() {
		return reqUsrDutyNm;
	}
	public void setReqUsrDutyNm(String reqUsrDutyNm) {
		this.reqUsrDutyNm = reqUsrDutyNm;
	}
	public String getReqUsrEmail() {
		return reqUsrEmail;
	}
	public void setReqUsrEmail(String reqUsrEmail) {
		this.reqUsrEmail = reqUsrEmail;
	}
	public String getReqUsrNum() {
		return reqUsrNum;
	}
	public void setReqUsrNum(String reqUsrNum) {
		this.reqUsrNum = reqUsrNum;
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
	public String getReqCompleteRatio() {
		return reqCompleteRatio;
	}
	public void setReqCompleteRatio(String reqCompleteRatio) {
		this.reqCompleteRatio = reqCompleteRatio;
	}
	
	public String getReqFp() {
		return reqFp;
	}
	public void setReqFp(String reqFp) {
		this.reqFp = reqFp;
	}
	public String getReqExFp() {
		return reqExFp;
	}
	public void setReqExFp(String reqExFp) {
		this.reqExFp = reqExFp;
	}
	public String getReqStDtm() {
		return reqStDtm;
	}
	public void setReqStDtm(String reqStDtm) {
		this.reqStDtm = reqStDtm;
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
	public String getReqEdDtm() {
		return reqEdDtm;
	}
	public void setReqEdDtm(String reqEdDtm) {
		this.reqEdDtm = reqEdDtm;
	}
	public String getReqProType() {
		return reqProType;
	}
	public void setReqProType(String reqProType) {
		this.reqProType = reqProType;
	}
	public String getReqNewType() {
		return reqNewType;
	}
	public void setReqNewType(String reqNewType) {
		this.reqNewType = reqNewType;
	}
	public String getReqProTypeNm() {
		return reqProTypeNm;
	}
	public void setReqProTypeNm(String reqProTypeNm) {
		this.reqProTypeNm = reqProTypeNm;
	}
	public String getReqNewTypeNm() {
		return reqNewTypeNm;
	}
	public void setReqNewTypeNm(String reqNewTypeNm) {
		this.reqNewTypeNm = reqNewTypeNm;
	}
	public String getReqAcceptTxt() {
		return reqAcceptTxt;
	}
	public void setReqAcceptTxt(String reqAcceptTxt) {
		this.reqAcceptTxt = reqAcceptTxt;
	}
	
	public String getSclCd() {
		return sclCd;
	}
	public void setSclCd(String sclCd) {
		this.sclCd = sclCd;
	}
	public String getReqTypeCd() {
		return reqTypeCd;
	}
	public void setReqTypeCd(String reqTypeCd) {
		this.reqTypeCd = reqTypeCd;
	}
	public String getPiaCd() {
		return piaCd;
	}
	public void setPiaCd(String piaCd) {
		this.piaCd = piaCd;
	}
	public String getLabInp() {
		return labInp;
	}
	public void setLabInp(String labInp) {
		this.labInp = labInp;
	}
	public String getAtchFileId() {
		return atchFileId;
	}
	public void setAtchFileId(String atchFileId) {
		this.atchFileId = atchFileId;
	}
	public String getCbAtchFileId() {
		return cbAtchFileId;
	}
	public void setCbAtchFileId(String cbAtchFileId) {
		this.cbAtchFileId = cbAtchFileId;
	}
	public String getMilestoneId() {
		return milestoneId;
	}
	public void setMilestoneId(String milestoneId) {
		this.milestoneId = milestoneId;
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
	public String getUseCd() {
		return useCd;
	}
	public void setUseCd(String useCd) {
		this.useCd = useCd;
	}
	public String getLoginUsrId() {
		return loginUsrId;
	}
	public void setLoginUsrId(String loginUsrId) {
		this.loginUsrId = loginUsrId;
	}
	
	public String getRegDtmDay() {
		return regDtmDay;
	}
	public void setRegDtmDay(String regDtmDay) {
		this.regDtmDay = regDtmDay;
	}
	public String getReqKey() {
		return reqKey;
	}
	public void setReqKey(String reqKey) {
		this.reqKey = reqKey;
	}
	
}
