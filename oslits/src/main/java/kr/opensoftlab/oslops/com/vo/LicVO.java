package kr.opensoftlab.oslops.com.vo;

/**
 * @Class Name : LicVO.java
 * @Description : LicVO VO class
 * @Modification Information
 * @Decription : 라이선스 정보를 담을 Bean 
 * 
 * @author 정형택
 * @since 2015.12.30.
 * @version 1.0
 * @see
 *  
 *  Copyright (C)  All right reserved.
 */
public class LicVO extends DefaultVO{
    
	private String licGrpId;
	private String licNo;
	private String licNm;
	private String licStartDt;
	private String licEndDt;
	private String licGbCd;
	private String permitUsrCnt;
	private String licEndCd;
    private String actLicNo; 
    private String actLicNm;
    private String actLicStartDt;
    private String actLicEndDt;
    private String actPermitUsrCnt;
    private String isActLicYn;
    
	public String getLicGrpId() {
		return licGrpId;
	}
	public void setLicGrpId(String licGrpId) {
		this.licGrpId = licGrpId;
	}
	public String getLicNo() {
		return licNo;
	}
	public void setLicNo(String licNo) {
		this.licNo = licNo;
	}
	public String getLicNm() {
		return licNm;
	}
	public void setLicNm(String licNm) {
		this.licNm = licNm;
	}
	public String getLicStartDt() {
		return licStartDt;
	}
	public void setLicStartDt(String licStartDt) {
		this.licStartDt = licStartDt;
	}
	public String getLicEndDt() {
		return licEndDt;
	}
	public void setLicEndDt(String licEndDt) {
		this.licEndDt = licEndDt;
	}
	public String getLicGbCd() {
		return licGbCd;
	}
	public void setLicGbCd(String licGbCd) {
		this.licGbCd = licGbCd;
	}
	public String getPermitUsrCnt() {
		return permitUsrCnt;
	}
	public void setPermitUsrCnt(String permitUsrCnt) {
		this.permitUsrCnt = permitUsrCnt;
	}
	public String getLicEndCd() {
		return licEndCd;
	}
	public void setLicEndCd(String licEndCd) {
		this.licEndCd = licEndCd;
	}
	public String getActLicNo() {
		return actLicNo;
	}
	public void setActLicNo(String actLicNo) {
		this.actLicNo = actLicNo;
	}
	public String getActLicNm() {
		return actLicNm;
	}
	public void setActLicNm(String actLicNm) {
		this.actLicNm = actLicNm;
	}
	public String getActLicStartDt() {
		return actLicStartDt;
	}
	public void setActLicStartDt(String actLicStartDt) {
		this.actLicStartDt = actLicStartDt;
	}
	public String getActLicEndDt() {
		return actLicEndDt;
	}
	public void setActLicEndDt(String actLicEndDt) {
		this.actLicEndDt = actLicEndDt;
	}
	public String getActPermitUsrCnt() {
		return actPermitUsrCnt;
	}
	public void setActPermitUsrCnt(String actPermitUsrCnt) {
		this.actPermitUsrCnt = actPermitUsrCnt;
	}
	public String getIsActLicYn() {
		return isActLicYn;
	}
	public void setIsActLicYn(String isActLicYn) {
		this.isActLicYn = isActLicYn;
	}

}
