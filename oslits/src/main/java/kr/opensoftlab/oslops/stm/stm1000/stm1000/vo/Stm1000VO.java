package kr.opensoftlab.oslops.stm.stm1000.stm1000.vo;

/**
 * @Class Name : Stm1000VO.java
 * @Description : Stm1000VO VO class
 * @Modification Information
 *
 * @author 공대영
 *  * @since 2018.07.23.
 * @version 1.0
 * @see
 *  
 *  Copyright (C) OpenSoftLab Corp All right reserved.
 */

import kr.opensoftlab.oslops.com.vo.PageVO;

public class Stm1000VO extends PageVO {
	
	/** 검색 조건 define */
	private String srchEvent;  /** Event : onLoad, onSelect */
	private String srchReqNm;
	private String srchReqChargerNm;
	
	/** 상세 기본 Defind */
	private String rn;
	private String licGrpId;
	private String prjId;
	private String reqId;
	
	private String apiId;
	private String apiNm;
	private String apiTxt;
	private String apiTok;
	private String loginUsrId;

	private String useCd;
	
	private String apiUrl;
	
	private String regDtm;
	
	private String useNm;
	
	private String prjNm;

	public String getPrjNm() {
		return prjNm;
	}

	public void setPrjNm(String prjNm) {
		this.prjNm = prjNm;
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

	public String getReqId() {
		return reqId;
	}

	public void setReqId(String reqId) {
		this.reqId = reqId;
	}

	public String getApiId() {
		return apiId;
	}

	public void setApiId(String apiId) {
		this.apiId = apiId;
	}

	public String getApiNm() {
		return apiNm;
	}

	public void setApiNm(String apiNm) {
		this.apiNm = apiNm;
	}

	public String getApiTxt() {
		return apiTxt;
	}

	public void setApiTxt(String apiTxt) {
		this.apiTxt = apiTxt;
	}

	public String getApiTok() {
		return apiTok;
	}

	public void setApiTok(String apiTok) {
		this.apiTok = apiTok;
	}

	public String getLoginUsrId() {
		return loginUsrId;
	}

	public void setLoginUsrId(String loginUsrId) {
		this.loginUsrId = loginUsrId;
	}

	public String getUseCd() {
		return useCd;
	}

	public void setUseCd(String useCd) {
		this.useCd = useCd;
	}

	public String getApiUrl() {
		return apiUrl;
	}

	public void setApiUrl(String apiUrl) {
		this.apiUrl = apiUrl;
	}

	public String getRegDtm() {
		return regDtm;
	}

	public void setRegDtm(String regDtm) {
		this.regDtm = regDtm;
	}

	public String getUseNm() {
		return useNm;
	}

	public void setUseNm(String useNm) {
		this.useNm = useNm;
	}
	
	
		
}
