package kr.opensoftlab.oslops.req.req4600.req4600.vo;

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

public class Req4600VO extends PageVO {
	
	/** 검색 조건 define */
	private String srchEvent;  /** Event : onLoad, onSelect */
	private String srchReqNm;
	private String srchReqChargerNm;
	
	/** 상세 기본 Defind */
	private String rn;
	private String licGrpId;
	private String prjId;
	private String selPrjId;
	
	private String reqId;
	
	private String progress;

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

	public String getProgress() {
		return progress;
	}

	public void setProgress(String progress) {
		this.progress = progress;
	}


	
}
