package kr.opensoftlab.oslops.stm.stm3000.stm3000.vo;

/**
 * @Class Name : Stm3000VO.java
 * @Description : Stm3000VO Controller class
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

public class Jen1100VO extends PageVO {
	
	/** 검색 조건 define */
	private String srchEvent;  /** Event : onLoad, onSelect */
	private String srchReqNm;
	private String srchReqChargerNm;
	
	/** 상세 기본 Defind */
	private String rn;
	private String licGrpId;
	private String loginUsrId;
	private String prjId;

	private String    jenId;              /* JENKINS 코드 */ 
	private String    jenNm;              /* JENKINS 명 */
    private String    jenUsrId;              /* JENKINS 사용자 ID */ 
    private String    jenUsrTok;              /* JENKINS 사용자 TOKEN KEY */
    private String    jenUrl; 				/* JENKINS Url */
    private String    jobId;              /* JOB ID(Name) */
    private String    jobRestoreId;              /* 원복 JOB ID(NAME)  */ 
    private String    jobTok;              /* JOB TOKEN KEY */
    private String    jobDesc;              /* JOB Comment */
    private String    jobTypeCd;              /* JOB TYPE(코드: DPL00002) */
    private String	jobTypeNm;
    
    private String    useCd;              /* 사용여부(코드 : CMM00001) */ 
    private String    useNm;
    private String	projectJenkinsCheck;
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
	public String getLoginUsrId() {
		return loginUsrId;
	}
	public void setLoginUsrId(String loginUsrId) {
		this.loginUsrId = loginUsrId;
	}
	
	public String getPrjId() {
		return prjId;
	}
	public void setPrjId(String prjId) {
		this.prjId = prjId;
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
	public String getUseCd() {
		return useCd;
	}
	public void setUseCd(String useCd) {
		this.useCd = useCd;
	}
	public String getUseNm() {
		return useNm;
	}
	public void setUseNm(String useNm) {
		this.useNm = useNm;
	}
	public String getProjectJenkinsCheck() {
		return projectJenkinsCheck;
	}
	public void setProjectJenkinsCheck(String projectJenkinsCheck) {
		this.projectJenkinsCheck = projectJenkinsCheck;
	}
	
}
