package kr.opensoftlab.oslops.com.vo;

/**
 * @Class Name : LoginVO.java
 * @Description : LoginVO VO class
 * @Modification Information
 * @Decription : 로그인시 사용자 정보를 담을 Bean 
 * 
 * @author 정형택
 * @since 2015.12.30.
 * @version 1.0
 * @see
 *  
 *  Copyright (C)  All right reserved.
 */
public class LoginVO extends DefaultVO{
    
    private String usrId;
    private String licGrpId;
    private String usrPw;
    private String usrNm;
    private String email;
    private String telno;
    private String deptId;
    private String etc;
    private String wkStTm;
    private String wkEdTm;
    private String bkStTm;
    private String bkEdTm;
    private int pwFailCnt;
    private String block;
    private String usrImgId;
    private String useCd;
    private String reqMainColor;
    private String reqFontColor;
    private String regDtm;
    private String regUsrId;
    private String regUsrIp;
    private String modifyDtm;
    private String modifyUsrId;
    private String modifyUsrIp;
    private String loginYn;
    private String actLicNo; 
    private String actLicNm;
    private String actLicStartDt;
    private String actLicEndDt;
    private String actPermitUsrCnt;
    private String isActLicYn;
    private String dshDisplayCd;
    private String dshDisplayNm;
    
    private String prjId;
    private String prjNm;
    private String admYn;
    private String iniYn;
    
    private int modMin;
    
    public int getModMin() {
		return modMin;
	}
	public void setModMin(int modMin) {
		this.modMin = modMin;
	}
	public String getIniYn() {
		return iniYn;
	}
	public void setIniYn(String iniYn) {
		this.iniYn = iniYn;
	}
	public String getAdmYn() {
		return admYn;
	}
	public void setAdmYn(String admYn) {
		this.admYn = admYn;
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
	private int loginStatus;

	public int getLoginStatus() {
		return loginStatus;
	}
	public void setLoginStatus(int loginStatus) {
		this.loginStatus = loginStatus;
	}
	public String getUsrId() {
		return usrId;
	}
	public void setUsrId(String usrId) {
		this.usrId = usrId;
	}
	public String getLicGrpId() {
		return licGrpId;
	}
	public void setLicGrpId(String licGrpId) {
		this.licGrpId = licGrpId;
	}
	public String getUsrPw() {
		return usrPw;
	}
	public void setUsrPw(String usrPw) {
		this.usrPw = usrPw;
	}
	public String getUsrNm() {
		return usrNm;
	}
	public void setUsrNm(String usrNm) {
		this.usrNm = usrNm;
	}
	public String getEmail() {
		return email;
	}
	public void setEmail(String email) {
		this.email = email;
	}
	public String getTelno() {
		return telno;
	}
	public void setTelno(String telno) {
		this.telno = telno;
	}
	public String getDeptId() {
		return deptId;
	}
	public void setDeptId(String deptId) {
		this.deptId = deptId;
	}
	public String getEtc() {
		return etc;
	}
	public void setEtc(String etc) {
		this.etc = etc;
	}

	public String getWkStTm() {
		return wkStTm;
	}
	public void setWkStTm(String wkStTm) {
		this.wkStTm = wkStTm;
	}
	public String getWkEdTm() {
		return wkEdTm;
	}
	public void setWkEdTm(String wkEdTm) {
		this.wkEdTm = wkEdTm;
	}
	
	public String getBkStTm() {
		return bkStTm;
	}
	public void setBkStTm(String bkStTm) {
		this.bkStTm = bkStTm;
	}
	public String getBkEdTm() {
		return bkEdTm;
	}
	public void setBkEdTm(String bkEdTm) {
		this.bkEdTm = bkEdTm;
	}

	public int getPwFailCnt() {
		return pwFailCnt;
	}
	public void setPwFailCnt(int pwFailCnt) {
		this.pwFailCnt = pwFailCnt;
	}
	public String getBlock() {
		return block;
	}
	public void setBlock(String block) {
		this.block = block;
	}
	public String getUsrImgId() {
		return usrImgId;
	}
	public void setUsrImgId(String usrImgId) {
		this.usrImgId = usrImgId;
	}
	public String getUseCd() {
		return useCd;
	}
	public void setUseCd(String useCd) {
		this.useCd = useCd;
	}
	
	public String getReqMainColor() {
		return reqMainColor;
	}
	public void setReqMainColor(String reqMainColor) {
		this.reqMainColor = reqMainColor;
	}
	public String getReqFontColor() {
		return reqFontColor;
	}
	public void setReqFontColor(String reqFontColor) {
		this.reqFontColor = reqFontColor;
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
	public String getLoginYn() {
		return loginYn;
	}
	public void setLoginYn(String loginYn) {
		this.loginYn = loginYn;
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
	
	public String getDshDisplayCd() {
		return dshDisplayCd;
	}
	public void setDshDisplayCd(String dshDisplayCd) {
		this.dshDisplayCd = dshDisplayCd;
	}
	
	public String getDshDisplayNm() {
		return dshDisplayNm;
	}
	public void setDshDisplayNm(String dshDisplayNm) {
		this.dshDisplayNm = dshDisplayNm;
	}
	@Override
	public String toString() {
		return "LoginVO [usrId=" + usrId + ", licGrpId=" + licGrpId + ", usrPw=" + usrPw + ", usrNm=" + usrNm
				+ ", email=" + email + ", telno=" + telno + ", deptId=" + deptId + ", etc=" + etc + ", wkStTm=" + wkStTm
				+ ", wkEdTm=" + wkEdTm + ", bkStTm=" + bkStTm + ", bkEdTm=" + bkEdTm + ", pwFailCnt=" + pwFailCnt
				+ ", block=" + block + ", usrImgId=" + usrImgId + ", useCd=" + useCd + ", reqMainColor=" + reqMainColor
				+ ", reqFontColor=" + reqFontColor + ", regDtm=" + regDtm + ", regUsrId=" + regUsrId + ", regUsrIp="
				+ regUsrIp + ", modifyDtm=" + modifyDtm + ", modifyUsrId=" + modifyUsrId + ", modifyUsrIp="
				+ modifyUsrIp + ", loginYn=" + loginYn + ", actLicNo=" + actLicNo + ", actLicNm=" + actLicNm
				+ ", actLicStartDt=" + actLicStartDt + ", actLicEndDt=" + actLicEndDt + ", actPermitUsrCnt="
				+ actPermitUsrCnt + ", isActLicYn=" + isActLicYn + ", dshDisplayCd=" + dshDisplayCd + ", dshDisplayNm="
				+ dshDisplayNm + ", prjId=" + prjId + ", prjNm=" + prjNm + ", admYn=" + admYn + ", iniYn=" + iniYn
				+ ", modMin=" + modMin + ", loginStatus=" + loginStatus + "]";
	}

    
    
}
