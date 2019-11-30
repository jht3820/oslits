package kr.opensoftlab.oslops.adm.adm5000.adm5000.vo;

import kr.opensoftlab.oslops.com.vo.PageVO;

public class Adm5000VO extends PageVO {
	
	/** 검색 조건 define */
	private String srchEvent;  /** Event : onLoad, onSelect */
	private String srchlicGrpId;
	private String srchloginUsrId;
	private String srchloginUsrNm;
	private String srchloginIp;
	private String srchFromDt;
	private String srchToDt;
	
	/** 상세 기본 Defind */
	private String rn;
	private String licGrpId;
	private String loginUsrId;
	private String loginUsrNm;
	private String loginIp;
	private String loginTime;
	private String logoutTime;
	
	private String loginStatus;
		
	public String getLoginStatus() {
		return loginStatus;
	}
	public void setLoginStatus(String loginStatus) {
		this.loginStatus = loginStatus;
	}
	public String getRn() {
		return rn;
	}
	public void setRn(String rn) {
		this.rn = rn;
	}
	public String getSrchEvent() {
		return srchEvent;
	}
	public void setSrchEvent(String srchEvent) {
		this.srchEvent = srchEvent;
	}
	public String getSrchlicGrpId() {
		return srchlicGrpId;
	}
	public void setSrchlicGrpId(String srchlicGrpId) {
		this.srchlicGrpId = srchlicGrpId;
	}
	public String getSrchloginUsrId() {
		return srchloginUsrId;
	}
	public void setSrchloginUsrId(String srchloginUsrId) {
		this.srchloginUsrId = srchloginUsrId;
	}
	public String getSrchloginUsrNm() {
		return srchloginUsrNm;
	}
	public void setSrchloginUsrNm(String srchloginUsrNm) {
		this.srchloginUsrNm = srchloginUsrNm;
	}
	public String getSrchloginIp() {
		return srchloginIp;
	}
	public void setSrchloginIp(String srchloginIp) {
		this.srchloginIp = srchloginIp;
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
	public String getLicGrpId() {
		return licGrpId;
	}
	public void setLicGrpId(String licGrpId) {
		this.licGrpId = licGrpId;
	}
	public String getLoginUsrId() {
		return loginUsrId;
	}
	public void setLoginUsrId(String loginUsrId) {
		this.loginUsrId = loginUsrId;
	}
	public String getLoginUsrNm() {
		return loginUsrNm;
	}
	public void setLoginUsrNm(String loginUsrNm) {
		this.loginUsrNm = loginUsrNm;
	}
	public String getLoginIp() {
		return loginIp;
	}
	public void setLoginIp(String loginIp) {
		this.loginIp = loginIp;
	}
	public String getLoginTime() {
		return loginTime;
	}
	public void setLoginTime(String loginTime) {
		this.loginTime = loginTime;
	}
	public String getLogoutTime() {
		return logoutTime;
	}
	public void setLogoutTime(String logoutTime) {
		this.logoutTime = logoutTime;
	}

	


	
	
	
	
	
	
	
	
}
