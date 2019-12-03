package kr.opensoftlab.oslops.adm.adm2000.adm2000.vo;

import kr.opensoftlab.oslops.com.vo.PageVO;
import kr.opensoftlab.sdf.excel.annotation.ExcelColumn;

/**
 * 
 * @author nekogun
 *
 */
public class Adm2000VO extends PageVO {
	
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
	/** 사용자 사용여부 코드명 */
	private String useCdNm;
	/** 근무 시작 시간 */
	private String wkStTm;
	/** 근무 종료 시간 */
	private String wkEdTm;
	/** 휴식 시작 시간 */
	private String bkStTm;
	/** 휴식 종료 시간 */
	private String bkEdTm;
	/** 비밀번호 실패횟수 */
	private String pwFailCnt;
	/** 차단여부 */
	private String block;
	/** 차단여부 명 */
	private String blockNm;
	/** 최근 로그인 일시 */
	private String recLoginDtm;
	/** 장기 미접속(3개월) 여부 */
	private String loginExprYn;
	/** 조직명(쿼리로 생성) */
	private String deptName;
	/** 이전 차단여부 */
	private String beforeBlock;
	/** 요구사항 메인 컬러 */
	private String reqMainColor;
	/** 요구사항 폰트 컬러 */
	private String reqFontColor;
	/** 대시보드 표시 구분 코드 */
	private String dshDisplayCd;
	/** 대시보드 표시 구분명 */
	private String dshDisplayNm;
	
	/** 프로젝트 id */
	private String prjId;
	/** 검색어 */
	private String searchPopTxt;

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
	
	public String getUseCdNm() {
		return useCdNm;
	}

	public void setUseCdNm(String useCdNm) {
		this.useCdNm = useCdNm;
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
	
	public String getPwFailCnt() {
		return pwFailCnt;
	}

	public void setPwFailCnt(String pwFailCnt) {
		this.pwFailCnt = pwFailCnt;
	}

	public String getBlock() {
		return block;
	}

	public void setBlock(String block) {
		this.block = block;
	}
	
	public String getBlockNm() {
		return blockNm;
	}

	public void setBlockNm(String blockNm) {
		this.blockNm = blockNm;
	}
	
	public String getRecLoginDtm() {
		return recLoginDtm;
	}

	public void setRecLoginDtm(String recLoginDtm) {
		this.recLoginDtm = recLoginDtm;
	}
	
	public String getLoginExprYn() {
		return loginExprYn;
	}

	public void setLoginExprYn(String loginExprYn) {
		this.loginExprYn = loginExprYn;
	}
	
	public String getDeptName() {
		return deptName;
	}

	public void setDeptName(String deptName) {
		this.deptName = deptName;
	}
	
	public String getBeforeBlock() {
		return beforeBlock;
	}

	public void setBeforeBlock(String beforeBlock) {
		this.beforeBlock = beforeBlock;
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

}
