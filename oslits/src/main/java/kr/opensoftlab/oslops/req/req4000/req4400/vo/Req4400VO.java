package kr.opensoftlab.oslops.req.req4000.req4400.vo;

/**
 * @Class Name : Req4400VO.java
 * @Description : Req4400VO VO class
 * @Modification Information
 *
 * @author 배용진
 * @since 2019.03.28.
 * @version 1.0
 * @see
 *  
 *  Copyright (C) OpenSoftLab Corp All right reserved.
 */

import kr.opensoftlab.oslops.com.vo.PageVO;


public class Req4400VO extends PageVO {
	
	/** 검색 조건 define */
	private String srchEvent;  /** Event : onLoad, onSelect */

	private String rn;
	/** 라이센스 그룹 ID */
	private String licGrpId;
	/** 프로젝트 ID */
	private String prjId;
	/** 프로젝트 ID */
	private String selPrjId;
	/** 요구사항 ID */
	private String reqId;
	/** 프로세스 ID */
	private String processId;
	/** 작업흐름 ID */
	private String flowId;
	/** 작업흐름 명 */
	private String flowNm;
	/** 작업 ID */
	private String workId;
	/** 작업 상태 코드 (공통코드: REQ00010)*/
	private String workStatusCd;
	/** 작업 상태 코드 명 */
	private String workStatusNm;
	/** 작업 지시내용 */
	private String workAdmContent;
	/** 작업 내용 */
	private String workContent;
	/** 담당자 ID */
	private String workChargerId;
	/** 담당자 명 */
	private String workChargerNm;
	/** 작업 시작예정일자 */
	private String workAdmStDtm;
	/** 작업 종료예정일자 */
	private String workAdmEdDtm;
	/** 작업 시작일자 */
	private String workStDtm;
	/** 작업 종료일자 */
	private String workEdDtm;
	
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
	public String getSelPrjId() {
		return selPrjId;
	}
	public void setSelPrjId(String selPrjId) {
		this.selPrjId = selPrjId;
	}
	public String getReqId() {
		return reqId;
	}
	public void setReqId(String reqId) {
		this.reqId = reqId;
	}
	public String getProcessId() {
		return processId;
	}
	public void setProcessId(String processId) {
		this.processId = processId;
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
	public String getWorkId() {
		return workId;
	}
	public void setWorkId(String workId) {
		this.workId = workId;
	}
	public String getWorkStatusCd() {
		return workStatusCd;
	}
	public void setWorkStatusCd(String workStatusCd) {
		this.workStatusCd = workStatusCd;
	}
	public String getWorkStatusNm() {
		return workStatusNm;
	}
	public void setWorkStatusNm(String workStatusNm) {
		this.workStatusNm = workStatusNm;
	}
	public String getWorkAdmContent() {
		return workAdmContent;
	}
	public void setWorkAdmContent(String workAdmContent) {
		this.workAdmContent = workAdmContent;
	}
	public String getWorkContent() {
		return workContent;
	}
	public void setWorkContent(String workContent) {
		this.workContent = workContent;
	}
	public String getWorkChargerId() {
		return workChargerId;
	}
	public void setWorkChargerId(String workChargerId) {
		this.workChargerId = workChargerId;
	}
	public String getWorkChargerNm() {
		return workChargerNm;
	}
	public void setWorkChargerNm(String workChargerNm) {
		this.workChargerNm = workChargerNm;
	}
	public String getWorkAdmStDtm() {
		return workAdmStDtm;
	}
	public void setWorkAdmStDtm(String workAdmStDtm) {
		this.workAdmStDtm = workAdmStDtm;
	}
	public String getWorkAdmEdDtm() {
		return workAdmEdDtm;
	}
	public void setWorkAdmEdDtm(String workAdmEdDtm) {
		this.workAdmEdDtm = workAdmEdDtm;
	}
	public String getWorkStDtm() {
		return workStDtm;
	}
	public void setWorkStDtm(String workStDtm) {
		this.workStDtm = workStDtm;
	}
	public String getWorkEdDtm() {
		return workEdDtm;
	}
	public void setWorkEdDtm(String workEdDtm) {
		this.workEdDtm = workEdDtm;
	}
}
