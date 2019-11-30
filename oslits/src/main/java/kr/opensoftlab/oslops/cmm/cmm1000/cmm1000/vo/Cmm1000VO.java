package kr.opensoftlab.oslops.cmm.cmm1000.cmm1000.vo;

import kr.opensoftlab.oslops.com.vo.PageVO;
import kr.opensoftlab.sdf.excel.annotation.ExcelColumn;

/**
 * 
 * @author Kong
 *
 */
public class Cmm1000VO extends PageVO {
	
	/** 조회 내용 */
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
	private String deptId;
	/** 비고 */
	@ExcelColumn(position=5)
	private String etc;
	/** 라이센스 그룹ID */
	private String licGrpId;
	/** 사용자 사용여부 */
	@ExcelColumn(position=6)
	private String useCd;

	//직책
	private String usrPositionCd;
	private String usrPositionNm;
	
	//직급
	private String usrDutyCd;
	private String usrDutyNm;
	
	/** 조직명(쿼리로 생성) */
	private String deptName;
	
	/** 프로젝트 id */
	private String prjId;
	/** 검색어 */
	private String searchPopTxt;
	
	private String authGrpNm;

	private String authGrpIds;
	
	private String acceptUseCd;

	// 큐브리드 java function에서 사용자 권한그룹 명을 조회하기 위해 사용하는 값
	// (큐브리드에서 group_concat 사용불가로 별도로 권한그룹 명 조회 함수 생성하여 사용)
	private String authGrpIdList;
	
	public String getAcceptUseCd() {
		return acceptUseCd;
	}

	public void setAcceptUseCd(String acceptUseCd) {
		this.acceptUseCd = acceptUseCd;
	}

	public String getAuthGrpIds() {
		return authGrpIds;
	}

	public void setAuthGrpIds(String authGrpIds) {
		this.authGrpIds = authGrpIds;
	}

	public String getAuthGrpNm() {
		return authGrpNm;
	}

	public void setAuthGrpNm(String authGrpNm) {
		this.authGrpNm = authGrpNm;
	}

	public String getLicGrpId() {
		return licGrpId;
	}

	public void setLicGrpId(String licGrpId) {
		this.licGrpId = licGrpId;
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

	public String getRn() {
		return rn;
	}

	public void setRn(String rn) {
		this.rn = rn;
	}

	public String getUseCd() {
		return useCd;
	}

	public void setUseCd(String useCd) {
		this.useCd = useCd;
	}

	
	
	public String getDeptName() {
		return deptName;
	}

	public void setDeptName(String deptName) {
		this.deptName = deptName;
	}
	
	
	public String getPrjId() {
		return prjId;
	}

	public void setPrjId(String prjId) {
		this.prjId = prjId;
	}
	
	public String getSearchPopTxt() {
		return searchPopTxt;
	}

	public void setSearchPopTxt(String searchPopTxt) {
		this.searchPopTxt = searchPopTxt;
	}

	public String getAuthGrpIdList() {
		return authGrpIdList;
	}

	public void setAuthGrpIdList(String authGrpIdList) {
		this.authGrpIdList = authGrpIdList;
	}
	public String getUsrPositionCd() {
		return usrPositionCd;
	}

	public void setUsrPositionCd(String usrPositionCd) {
		this.usrPositionCd = usrPositionCd;
	}

	public String getUsrPositionNm() {
		return usrPositionNm;
	}

	public void setUsrPositionNm(String usrPositionNm) {
		this.usrPositionNm = usrPositionNm;
	}

	public String getUsrDutyCd() {
		return usrDutyCd;
	}

	public void setUsrDutyCd(String usrDutyCd) {
		this.usrDutyCd = usrDutyCd;
	}

	public String getUsrDutyNm() {
		return usrDutyNm;
	}

	public void setUsrDutyNm(String usrDutyNm) {
		this.usrDutyNm = usrDutyNm;
	}

	
}
