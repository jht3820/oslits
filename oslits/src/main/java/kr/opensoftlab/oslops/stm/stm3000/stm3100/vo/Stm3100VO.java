package kr.opensoftlab.oslops.stm.stm3000.stm3100.vo;

/**
 * @Class Name : Stm3100VO.java
 * @Description : Stm3100VO Controller class
 * @Modification Information
 *
 * @author 공대영
 * @since 2018.09.05.
 * @version 1.0
 * @see
 *  
 *  Copyright (C) OpenSoftLab Corp All right reserved.
 */

import kr.opensoftlab.oslops.com.vo.PageVO;

public class Stm3100VO extends PageVO {
	
	/** 검색 조건 define */
	private String srchEvent;  /** Event : onLoad, onSelect */
	private String srchReqNm;
	private String srchReqChargerNm;
	
	/** 상세 기본 Defind */
	private String rn;
	/** 라이선스 그룹 ID */
	private String licGrpId;
	/** 프로젝트 ID */
	private String prjId;
	/** 로그인 사용자 ID */
	private String loginUsrId;
	/** jenkins id */
	private String jenId;         
	/** jenkins 명 */
	private String jenNm;
	/** jenkins 사용자 ID */
	private String jenUsrId;  
	/** jenkins 사용자 토큰 */
	private String jenUsrTok; 
	/** jenkins url */
	private String jenUrl;
	/** jenkins 설명 */
	private String jenDesc;
	/** jenkins 사용여부 */
	private String useCd; 
	/** jenkins 삭제여부 */
	private String delCd; 
	/** job id */
	private String jobId; 
	/** 원복 job id */
	private String jobRestoreId;
	/** job 토큰 키 */
	private String jobTok;
	/** job 설명 */
	private String jobDesc;
	/** job 타입 코드 */
	private String jobTypeCd;
	/** job 타입 명 */
	private String jobTypeNm;
	/** job 배정여부 */
	private String isChecked;
	/** job 배정여부 */
	private String orgChecked;
	
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
	public String getLoginUsrId() {
		return loginUsrId;
	}
	public void setLoginUsrId(String loginUsrId) {
		this.loginUsrId = loginUsrId;
	}
	public String getJenId() {
		return jenId;
	}
	public void setJenId(String jenId) {
		this.jenId = jenId;
	}
	public String getJenNm() {
		return jenNm;
	}
	public void setJenNm(String jenNm) {
		this.jenNm = jenNm;
	}
	public String getJenUsrId() {
		return jenUsrId;
	}
	public void setJenUsrId(String jenUsrId) {
		this.jenUsrId = jenUsrId;
	}
	public String getJenUsrTok() {
		return jenUsrTok;
	}
	public void setJenUsrTok(String jenUsrTok) {
		this.jenUsrTok = jenUsrTok;
	}
	public String getJenUrl() {
		return jenUrl;
	}
	public void setJenUrl(String jenUrl) {
		this.jenUrl = jenUrl;
	}
	public String getJenDesc() {
		return jenDesc;
	}
	public void setJenDesc(String jenDesc) {
		this.jenDesc = jenDesc;
	}
	public String getUseCd() {
		return useCd;
	}
	public void setUseCd(String useCd) {
		this.useCd = useCd;
	}
	public String getDelCd() {
		return delCd;
	}
	public void setDelCd(String delCd) {
		this.delCd = delCd;
	}
	public String getJobId() {
		return jobId;
	}
	public void setJobId(String jobId) {
		this.jobId = jobId;
	}
	public String getJobRestoreId() {
		return jobRestoreId;
	}
	public void setJobRestoreId(String jobRestoreId) {
		this.jobRestoreId = jobRestoreId;
	}
	public String getJobTok() {
		return jobTok;
	}
	public void setJobTok(String jobTok) {
		this.jobTok = jobTok;
	}
	public String getJobDesc() {
		return jobDesc;
	}
	public void setJobDesc(String jobDesc) {
		this.jobDesc = jobDesc;
	}
	public String getJobTypeCd() {
		return jobTypeCd;
	}
	public void setJobTypeCd(String jobTypeCd) {
		this.jobTypeCd = jobTypeCd;
	}
	public String getJobTypeNm() {
		return jobTypeNm;
	}
	public void setJobTypeNm(String jobTypeNm) {
		this.jobTypeNm = jobTypeNm;
	}
	public String getIsChecked() {
		return isChecked;
	}
	public void setIsChecked(String isChecked) {
		this.isChecked = isChecked;
	}
	public String getOrgChecked() {
		return orgChecked;
	}
	public void setOrgChecked(String orgChecked) {
		this.orgChecked = orgChecked;
	}
}
