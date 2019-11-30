package kr.opensoftlab.oslops.adm.adm8000.adm8000.vo;

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

public class Adm8000VO extends PageVO {
	
	/** 검색 조건 define */
	private String srchEvent;  /** Event : onLoad, onSelect */
	private String srchReqNm;
	private String srchReqChargerNm;
	
	/** 상세 기본 Defind */
	private String rn;
	private String licGrpId;
	private String prjId;
	private String loginUsrId;
	

	private String    jenId;              /* JENKINS JOB 코드 */ 
    private String    jenNm;              /* JENKINS JOB 명 */ 
    private String    jenTxt;              /* JENKINS JOB Comment */ 
    private String    jenTok;              /* JENKINS JOB TOKEN KEY */ 
    private String    jenUsrId;              /* JENKINS 사용자 ID */ 
    private String    jenUsrTok;              /* JENKINS 사용자 TOKEN KEY */ 
    private String    useCd;              /* 사용여부(코드 : CMM00001) */ 
    private String    jenUrl;
    private String    useNm; 
    
    private String    prjNm;
	
	public String getPrjNm() {
		return prjNm;
	}
	public void setPrjNm(String prjNm) {
		this.prjNm = prjNm;
	}
	public String getUseNm() {
		return useNm;
	}
	public void setUseNm(String useNm) {
		this.useNm = useNm;
	}
	public String getJenUrl() {
		return jenUrl;
	}
	public void setJenUrl(String jenUrl) {
		this.jenUrl = jenUrl;
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
	public String getJenTxt() {
		return jenTxt;
	}
	public void setJenTxt(String jenTxt) {
		this.jenTxt = jenTxt;
	}
	public String getJenTok() {
		return jenTok;
	}
	public void setJenTok(String jenTok) {
		this.jenTok = jenTok;
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
	public String getUseCd() {
		return useCd;
	}
	public void setUseCd(String useCd) {
		this.useCd = useCd;
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
