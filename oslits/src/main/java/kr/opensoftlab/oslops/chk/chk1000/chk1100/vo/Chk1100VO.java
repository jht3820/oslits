package kr.opensoftlab.oslops.chk.chk1000.chk1100.vo;

import kr.opensoftlab.oslops.com.vo.PageVO;

public class Chk1100VO extends PageVO{
	
	/** 검색 조건 define */
	private String srchEvent;  /** Event : onLoad, onSelect */

	/** 상세 기본 Defind */
	private String rn;
	private String licGrpId;
	private String prjId;
	private String reqId;
	private String accptCd;
	private String accptNm;
	private String reqNm;
	private String reqNo;
	private String reqTypeCd;
	private String reqImprtCd;
	private String reqImprtNm;
	private String reqLink;
	private String reqDevWkTm;
	private String reqDesc;
	private String reqChgDtm;
	private String reqReqDtm;
	private String reqChargerId;
	private String reqChargerNm;
	private String reqChargerDt;
	private String reqUsrId;
	private String reqUsrNm;
	private String reqDtm;
	private String reqStatusCd;
	private String reqStatusNm;
	private String accptRejctTxt;
	private String accptDtm;
	private String accptUsrId;
	private String accptUsrIp;
	private String atchFileId;
	private String reqClsId;
	private String reqClsNm;	
	private String sprintId;
	private String sprintNm;
	private String flowId;
	private String flowNm;
	private String useCd;
	private String loginUsrId;
	private String upperReqId;
	private String showUpperReqId;
	private String upperCnt;
	private String reqStDtm;
	private String reqEdDtm;
	
	private String signCd;
	private String signCdNm;
	
	private String signUsrId;	
	private String signUsrNm;
	
	private String flowNextId;
	private String flowSignStopCd;

	public String getFlowNextId() {
		return flowNextId;
	}
	public void setFlowNextId(String flowNextId) {
		this.flowNextId = flowNextId;
	}
	public String getType() {
		return type;
	}
	public void setType(String type) {
		this.type = type;
	}
	public String getProcessId() {
		return processId;
	}
	public void setProcessId(String processId) {
		this.processId = processId;
	}
	public String getSignFlowId() {
		return signFlowId;
	}
	public void setSignFlowId(String signFlowId) {
		this.signFlowId = signFlowId;
	}
	public String getSignFlowNm() {
		return signFlowNm;
	}
	public void setSignFlowNm(String signFlowNm) {
		this.signFlowNm = signFlowNm;
	}
	public String getSignFlowTitleBgColor() {
		return signFlowTitleBgColor;
	}
	public void setSignFlowTitleBgColor(String signFlowTitleBgColor) {
		this.signFlowTitleBgColor = signFlowTitleBgColor;
	}
	public String getSignFlowTitleColor() {
		return signFlowTitleColor;
	}
	public void setSignFlowTitleColor(String signFlowTitleColor) {
		this.signFlowTitleColor = signFlowTitleColor;
	}
	public String getSignUsrImg() {
		return signUsrImg;
	}
	public void setSignUsrImg(String signUsrImg) {
		this.signUsrImg = signUsrImg;
	}
	public String getSignRejectCmnt() {
		return signRejectCmnt;
	}
	public void setSignRejectCmnt(String signRejectCmnt) {
		this.signRejectCmnt = signRejectCmnt;
	}
	public String getSignDtm() {
		return signDtm;
	}
	public void setSignDtm(String signDtm) {
		this.signDtm = signDtm;
	}
	public String getRegDtm() {
		return regDtm;
	}
	public void setRegDtm(String regDtm) {
		this.regDtm = regDtm;
	}
	public String getRegUsrId() {
		return regUsrId;
	}
	public void setRegUsrId(String regUsrId) {
		this.regUsrId = regUsrId;
	}
	public String getRegUsrNm() {
		return regUsrNm;
	}
	public void setRegUsrNm(String regUsrNm) {
		this.regUsrNm = regUsrNm;
	}
	public String getRegUsrImg() {
		return regUsrImg;
	}
	public void setRegUsrImg(String regUsrImg) {
		this.regUsrImg = regUsrImg;
	}
	public String getRegUsrIp() {
		return regUsrIp;
	}
	public void setRegUsrIp(String regUsrIp) {
		this.regUsrIp = regUsrIp;
	}
	private String type;
;
	private String processId;
	private String signFlowId;
	private String signFlowNm;
	private String signFlowTitleBgColor;
	private String signFlowTitleColor;

	private String signUsrImg;

	private String signRejectCmnt; 
	private String signDtm;
	private String regDtm;
	private String regUsrId; 
	private String regUsrNm;
	private String regUsrImg; 
	private String regUsrIp;
	
	
	public String getSignUsrNm() {
		return signUsrNm;
	}
	public void setSignUsrNm(String signUsrNm) {
		this.signUsrNm = signUsrNm;
	}
	public String getSignUsrId() {
		return signUsrId;
	}
	public void setSignUsrId(String signUsrId) {
		this.signUsrId = signUsrId;
	}
	public String getSignCdNm() {
		return signCdNm;
	}
	public void setSignCdNm(String signCdNm) {
		this.signCdNm = signCdNm;
	}
	private String signId;
	
	public String getSignId() {
		return signId;
	}
	public void setSignId(String signId) {
		this.signId = signId;
	}
	public String getSignCd() {
		return signCd;
	}
	public void setSignCd(String signCd) {
		this.signCd = signCd;
	}
	public String getShowUpperReqId() {
		return showUpperReqId;
	}
	public void setShowUpperReqId(String showUpperReqId) {
		this.showUpperReqId = showUpperReqId;
	}
	public String getUpperReqId() {
		return upperReqId;
	}
	public void setUpperReqId(String upperReqId) {
		this.upperReqId = upperReqId;
	}
	public String getUpperCnt() {
		return upperCnt;
	}
	public void setUpperCnt(String upperCnt) {
		this.upperCnt = upperCnt;
	}
	public String getSrchEvent() {
		return srchEvent;
	}
	public void setSrchEvent(String srchEvent) {
		this.srchEvent = srchEvent;
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
	public String getReqId() {
		return reqId;
	}
	public void setReqId(String reqId) {
		this.reqId = reqId;
	}
	public String getAccptCd() {
		return accptCd;
	}
	public void setAccptCd(String accptCd) {
		this.accptCd = accptCd;
	}
	public String getAccptNm() {
		return accptNm;
	}
	public void setAccptNm(String accptNm) {
		this.accptNm = accptNm;
	}
	public String getReqNm() {
		return reqNm;
	}
	public void setReqNm(String reqNm) {
		this.reqNm = reqNm;
	}
	public String getReqNo() {
		return reqNo;
	}
	public void setReqNo(String reqNo) {
		this.reqNo = reqNo;
	}
	public String getReqTypeCd() {
		return reqTypeCd;
	}
	public void setReqTypeCd(String reqTypeCd) {
		this.reqTypeCd = reqTypeCd;
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
	public String getReqLink() {
		return reqLink;
	}
	public void setReqLink(String reqLink) {
		this.reqLink = reqLink;
	}
	public String getReqDevWkTm() {
		return reqDevWkTm;
	}
	public void setReqDevWkTm(String reqDevWkTm) {
		this.reqDevWkTm = reqDevWkTm;
	}
	public String getReqDesc() {
		return reqDesc;
	}
	public void setReqDesc(String reqDesc) {
		this.reqDesc = reqDesc;
	}
	public String getReqChgDtm() {
		return reqChgDtm;
	}
	public void setReqChgDtm(String reqChgDtm) {
		this.reqChgDtm = reqChgDtm;
	}
	public String getReqReqDtm() {
		return reqReqDtm;
	}
	public void setReqReqDtm(String reqReqDtm) {
		this.reqReqDtm = reqReqDtm;
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
	public String getReqChargerDt() {
		return reqChargerDt;
	}
	public void setReqChargerDt(String reqChargerDt) {
		this.reqChargerDt = reqChargerDt;
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
	public String getReqStatusCd() {
		return reqStatusCd;
	}
	public void setReqStatusCd(String reqStatusCd) {
		this.reqStatusCd = reqStatusCd;
	}
	public String getReqStatusNm() {
		return reqStatusNm;
	}
	public void setReqStatusNm(String reqStatusNm) {
		this.reqStatusNm = reqStatusNm;
	}
	public String getAccptRejctTxt() {
		return accptRejctTxt;
	}
	public void setAccptRejctTxt(String accptRejctTxt) {
		this.accptRejctTxt = accptRejctTxt;
	}
	public String getAccptDtm() {
		return accptDtm;
	}
	public void setAccptDtm(String accptDtm) {
		this.accptDtm = accptDtm;
	}
	public String getAccptUsrId() {
		return accptUsrId;
	}
	public void setAccptUsrId(String accptUsrId) {
		this.accptUsrId = accptUsrId;
	}
	public String getAccptUsrIp() {
		return accptUsrIp;
	}
	public void setAccptUsrIp(String accptUsrIp) {
		this.accptUsrIp = accptUsrIp;
	}
	public String getAtchFileId() {
		return atchFileId;
	}
	public void setAtchFileId(String atchFileId) {
		this.atchFileId = atchFileId;
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
	public String getFlowSignStopCd() {
		return flowSignStopCd;
	}
	public void setFlowSignStopCd(String flowSignStopCd) {
		this.flowSignStopCd = flowSignStopCd;
	}
	

}
