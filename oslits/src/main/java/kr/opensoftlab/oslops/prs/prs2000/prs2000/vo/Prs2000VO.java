package kr.opensoftlab.oslops.prs.prs2000.prs2000.vo;

/**
 * @Class Name : Prs2000VO.java
 * @Description : Prs2000VO Controller class
 * @Modification Information
 *
 * @author 강재민
 * @since 2016.01.301.
 * @version 1.0
 * @see
 *  
 *  Copyright (C) OpenSoftLab Corp All right reserved.
 */

import kr.opensoftlab.oslops.com.vo.PageVO;

public class Prs2000VO extends PageVO {
	
	/** 검색 조건 define */
	private String srchEvent;  /** Event : onLoad, onSelect */
	private String srchLicGrpId;
	private String srchUsrId;
	
	/** 상세 기본 Defind */
	private String rn;
	private String prjId;
	private String prjNm;
	private String startDt;
	private String endDt;
	private String prjDesc;
	private String usrId;
	private String prjUsrCnt;
	public String getSrchEvent() {
		return srchEvent;
	}
	public void setSrchEvent(String srchEvent) {
		this.srchEvent = srchEvent;
	}
	public String getSrchLicGrpId() {
		return srchLicGrpId;
	}
	public void setSrchLicGrpId(String srchLicGrpId) {
		this.srchLicGrpId = srchLicGrpId;
	}
	public String getSrchUsrId() {
		return srchUsrId;
	}
	public void setSrchUsrId(String srchUsrId) {
		this.srchUsrId = srchUsrId;
	}
	public String getRn() {
		return rn;
	}
	public void setRn(String rn) {
		this.rn = rn;
	}
	public String getPrjId() {
		return prjId;
	}
	public void setPrjId(String prjId) {
		this.prjId = prjId;
	}
	public String getPrjNm() {
		return prjNm;
	}
	public void setPrjNm(String prjNm) {
		this.prjNm = prjNm;
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
	public String getPrjDesc() {
		return prjDesc;
	}
	public void setPrjDesc(String prjDesc) {
		this.prjDesc = prjDesc;
	}
	public String getUsrId() {
		return usrId;
	}
	public void setUsrId(String usrId) {
		this.usrId = usrId;
	}
	public String getPrjUsrCnt() {
		return prjUsrCnt;
	}
	public void setPrjUsrCnt(String prjUsrCnt) {
		this.prjUsrCnt = prjUsrCnt;
	}
}
