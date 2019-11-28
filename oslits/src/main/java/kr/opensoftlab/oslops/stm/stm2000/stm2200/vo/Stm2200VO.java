package kr.opensoftlab.oslops.stm.stm2000.stm2200.vo;

/**
 * @Class Name : Svn1000VO.java
 * @Description : Svn1000VO Controller class
 * @Modification Information
 *
 * @author 공대영
 * @since 2018.07.27.
 * @version 1.0
 * @see
 *  
 *  Copyright (C) OpenSoftLab Corp All right reserved.
 */

import kr.opensoftlab.oslops.com.vo.PageVO;

public class Stm2200VO extends PageVO {

	/** 검색 조건 define */
	private String srchEvent;  /** Event : onLoad, onSelect */
	private String srchReqNm;
	private String srchReqChargerNm;

	/** 상세 기본 Defind */
	private String rn;
	private String licGrpId;
	private String prjId;
	private String loginUsrId ="";

	private String    svnRepId;              /* SVN Repository 코드 */ 
	private String    svnRepNm;              /* SVN Repository 명 */ 
	private String    svnRepUrl;              /* SVN Repository URL */ 
	private String    svnTxt;              /* SVN RepositoryI Comment */ 
	private String    useCd;              /* 사용여부(코드 : CMM00001) */ 
	private String    useNm;              /* 사용여부(코드 : CMM00001) */

	private String    prjNm;


	public String getPrjNm() {
		return prjNm;
	}
	public void setPrjNm(String prjNm) {
		this.prjNm = prjNm;
	}
	public String getSvnRepId() {
		return svnRepId;
	}
	public void setSvnRepId(String svnRepId) {
		this.svnRepId = svnRepId;
	}
	public String getSvnRepNm() {
		return svnRepNm;
	}
	public void setSvnRepNm(String svnRepNm) {
		this.svnRepNm = svnRepNm;
	}
	public String getSvnRepUrl() {
		return svnRepUrl;
	}
	public void setSvnRepUrl(String svnRepUrl) {
		this.svnRepUrl = svnRepUrl;
	}
	public String getSvnTxt() {
		return svnTxt;
	}
	public void setSvnTxt(String svnTxt) {
		this.svnTxt = svnTxt;
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
	public String getLoginUsrId() {
		return loginUsrId;
	}
	public void setLoginUsrId(String loginUsrId) {
		this.loginUsrId = loginUsrId;
	}
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

}
