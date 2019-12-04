package kr.opensoftlab.oslops.prj.prj1000.prj1100.vo;

/**
 * @Class Name : Prj1100VO.java
 * @Description : Prj1100VO VO class
 * @Modification Information
 *
 * @author 배용진
 * @since 2019.03.29.
 * @version 1.0
 * @see
 *  
 *  Copyright (C) OpenSoftLab Corp All right reserved.
 */

import kr.opensoftlab.oslops.com.vo.PageVO;


public class Prj1100VO extends PageVO {
	
	/** 검색 조건 define */
	private String srchEvent;  /** Event : onLoad, onSelect */
	
	private String rn;
	/** 라이선스 그룹 ID */
	private String licGrpId;
	/** 프로젝트 ID */
	private String prjId;
	/** 요구사항 ID */
	private String reqId;
	/** 프로세스 ID */
	private String processId;
	/** 작업흐름 ID */
	private String flowId;
	
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
}
