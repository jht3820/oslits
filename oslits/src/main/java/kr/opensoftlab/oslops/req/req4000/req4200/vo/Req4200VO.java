package kr.opensoftlab.oslops.req.req4000.req4200.vo;

/**
 * @Class Name : Req4200VO.java
 * @Description : Req4200VO VO class
 * @Modification Information
 *
 * @author 배용진
 * @since 2019.03.07.
 * @version 1.0
 * @see
 *  
 *  Copyright (C) OpenSoftLab Corp All right reserved.
 */

import kr.opensoftlab.oslops.com.vo.PageVO;
import kr.opensoftlab.sdf.excel.annotation.ExcelColumn;


public class Req4200VO extends PageVO {
	
	/** 검색 조건 define */
	private String srchEvent;  /** Event : onLoad, onSelect */
	private String srchReqDesc;
	private String srchReqClsNm;
	private String srchReqNm;
	/** 기간 검색 조건*/
	private String srchFromDt;
	private String srchToDt;
	
	/** 상세 기본 Defind */
	/** 요구사항 배정/미배정 구분값 */
	private String clsMode;
	private String rn;
	/** 라이센스 그룹 ID */
	private String licGrpId;
	/** 프로젝트  ID */
	private String prjId;
	private String selPrjId;
	/** 요구사항 순번 */
	private String reqOrd;
	/** 요구사항 ID */
	private String reqId;
	/** 요구사항 분류 ID */
	private String reqClsId;
	/** 요구사항 분류 명 */
	private String reqClsNm;
	/** 공문번호 */
	private String reqNo;
	/** 요청자 ID */
	private String reqUsrId;
	/** 요청자 명 */
	private String reqUsrNm;
	/** 요청일 */
	private String reqDtm;
	/** 요청자 소속 */
	private String reqUsrDeptNm;
	/** 요청자 이메일 */
	private String reqUsrEmail;
	/** 요청자 연락처 */
	private String reqUsrNum;
	/** 요구사항 명 */
	private String reqNm;
	/** 요구사항 설명 */
	private String reqDesc;
	/** 담당자 ID */
	private String reqChargerId;
	/** 담당자 명 */
	private String reqChargerNm;
	/** 요구사항 진척률 */
	private String reqCompleteRatio;
	/** 요구사항 예상 FP */
	private String reqExFp;
	/** 요구사항 FP */
	private String reqFp;
	/** 작업 시작일자 */
	private String reqStDtm;
	/** 작업 종료일자 */
	private String reqEdDtm;
	/** 작업 시작 예정일자 */
	private String reqStDuDtm;
	/** 작업 종료 예정일자 */
	private String reqEdDuDtm;
	/** 처리유형 코드 */
	private String reqProType;
	/** 처리유형 명 */
	private String reqProTypeNm;
	/** 접수유형 코드 */
	private String reqNewType;
	/** 접수유형 명 */
	private String reqNewTypeNm;
	/** 시스템 구분 코드 */
	private String sclCd;
	/** 시스템 구분 명 */
	private String sclNm;
	/** 요구사항 유형 코드 */
	private String reqTypeCd;
	/** 요구사항 유형 명 */
	private String reqTypeNm;
	/** 성능 개선활동 여부 코드 */
	private String piaCd;
	/** 성능 개선활동 명 */
	private String piaNm;
	/** 투입 인력 */
	private String labInp;
	/** 첨부파일 ID */
	private String atchFileId;
	/** 클립보드 파일 ID */
	private String cbAtchFileId;
	/** 마일스톤 ID */
	private String milestoneId;
	/** 프로세스 ID */
	private String processId;
	/** 프로세스 명 */
	private String processNm;
	/** 작업흐름 ID */
	private String flowId;
	/** 작업흐름 명 */
	private String flowNm;
	/** 사용여부 */
	private String useCd;
	/** 체계별 요구사항 ID */
	private String orgReqId;
	/** 최초등록일시 */
	private String regDtm;
	/** 최초등록자 ID */
	private String regUsrId;
	/** 최초등록자 IP */
	private String regUsrIp;
	/** 최종수정일시 */
	private String modifyDtm;
	/** 최종수정자 ID */
	private String modifyUsrId;
	/** 최종수정자 IP */
	private String modifyUsrIp;
	
	
	public String getSrchEvent() {
		return srchEvent;
	}
	public void setSrchEvent(String srchEvent) {
		this.srchEvent = srchEvent;
	}
	public String getSrchReqDesc() {
		return srchReqDesc;
	}
	public void setSrchReqDesc(String srchReqDesc) {
		this.srchReqDesc = srchReqDesc;
	}
	public String getSrchReqClsNm() {
		return srchReqClsNm;
	}
	public void setSrchReqClsNm(String srchReqClsNm) {
		this.srchReqClsNm = srchReqClsNm;
	}
	public String getSrchReqNm() {
		return srchReqNm;
	}
	public void setSrchReqNm(String srchReqNm) {
		this.srchReqNm = srchReqNm;
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
	public String getClsMode() {
		return clsMode;
	}
	public void setClsMode(String clsMode) {
		this.clsMode = clsMode;
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
	public String getReqOrd() {
		return reqOrd;
	}
	public void setReqOrd(String reqOrd) {
		this.reqOrd = reqOrd;
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
	public String getReqChargerNm() {
		return reqChargerNm;
	}
	public void setReqChargerNm(String reqChargerNm) {
		this.reqChargerNm = reqChargerNm;
	}
	public String getReqCompleteRatio() {
		return reqCompleteRatio;
	}
	public void setReqCompleteRatio(String reqCompleteRatio) {
		this.reqCompleteRatio = reqCompleteRatio;
	}
	public String getReqExFp() {
		return reqExFp;
	}
	public void setReqExFp(String reqExFp) {
		this.reqExFp = reqExFp;
	}
	public String getReqFp() {
		return reqFp;
	}
	public void setReqFp(String reqFp) {
		this.reqFp = reqFp;
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
	public String getSclCd() {
		return sclCd;
	}
	public void setSclCd(String sclCd) {
		this.sclCd = sclCd;
	}
	public String getSclNm() {
		return sclNm;
	}
	public void setSclNm(String sclNm) {
		this.sclNm = sclNm;
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
	public String getPiaCd() {
		return piaCd;
	}
	public void setPiaCd(String piaCd) {
		this.piaCd = piaCd;
	}
	public String getPiaNm() {
		return piaNm;
	}
	public void setPiaNm(String piaNm) {
		this.piaNm = piaNm;
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
	public String getOrgReqId() {
		return orgReqId;
	}
	public void setOrgReqId(String orgReqId) {
		this.orgReqId = orgReqId;
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
	public String getRegUsrIp() {
		return regUsrIp;
	}
	public void setRegUsrIp(String regUsrIp) {
		this.regUsrIp = regUsrIp;
	}
	public String getModifyDtm() {
		return modifyDtm;
	}
	public void setModifyDtm(String modifyDtm) {
		this.modifyDtm = modifyDtm;
	}
	public String getModifyUsrId() {
		return modifyUsrId;
	}
	public void setModifyUsrId(String modifyUsrId) {
		this.modifyUsrId = modifyUsrId;
	}
	public String getModifyUsrIp() {
		return modifyUsrIp;
	}
	public void setModifyUsrIp(String modifyUsrIp) {
		this.modifyUsrIp = modifyUsrIp;
	}
}
