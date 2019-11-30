package kr.opensoftlab.oslops.stm.stm4000.stm4100.vo;

import kr.opensoftlab.oslops.com.vo.PageVO;
import kr.opensoftlab.sdf.excel.annotation.ExcelColumn;

/**
 * 
 * @author nekogun
 *
 */
public class Stm4100VO extends PageVO {
	
	/** 라이센스 그룹ID */
	private String licGrpId;
	/** 프로젝트 id */
	private String prjId;
	/** 권한그룹 Id */
	private String authGrpId;

	private String rn;
	/** 아이디 */
	@ExcelColumn(position=0)
	private String usrId;
	/** 이름 */
	@ExcelColumn(position=1)
	private String usrNm;
	/** 이메일 */
	@ExcelColumn(position=3)
	private String email;
	/** 전화 */
	@ExcelColumn(position=2)
	private String telno;
	/** 소속 조직ID */
	@ExcelColumn(position=4)
	private String deptNm;
	/** 비고 */
	@ExcelColumn(position=5)
	private String etc;
	
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
	public String getAuthGrpId() {
		return authGrpId;
	}
	public void setAuthGrpId(String authGrpId) {
		this.authGrpId = authGrpId;
	}
	public String getRn() {
		return rn;
	}
	public void setRn(String rn) {
		this.rn = rn;
	}
	public String getUsrId() {
		return usrId;
	}
	public void setUsrId(String usrId) {
		this.usrId = usrId;
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
	public String getDeptNm() {
		return deptNm;
	}
	public void setDeptNm(String deptNm) {
		this.deptNm = deptNm;
	}
	public String getEtc() {
		return etc;
	}
	public void setEtc(String etc) {
		this.etc = etc;
	}
}
