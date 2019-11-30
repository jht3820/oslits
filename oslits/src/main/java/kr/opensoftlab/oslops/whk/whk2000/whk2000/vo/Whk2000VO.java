package kr.opensoftlab.oslops.whk.whk2000.whk2000.vo;

/**
 * @Class Name : Whk2000VO.java
 * @Description : Whk2000 VO
 * @Modification Information
 *
 * @author 진주영
 * @since 2019.05.24.
 * @version 1.0
 * @see
 *  
 *  Copyright (C) OpenSoftLab Corp All right reserved.
 */

import kr.opensoftlab.oslops.com.vo.PageVO;


public class Whk2000VO extends PageVO {
	
	/** 검색 조건 define */
	private String srchEvent;  /** Event : onLoad, onSelect */
	
	/** 상세 기본 Defind */
	private String rn;
	private String licGrpId;
	private String usrId;
	private String webhookId;
	private String webhookNm;
	private String platformTypeCd;
	private String platformTypeNm;
	private String webhookUrl;
	private String whkReqDeleteCd;	
	private String whkReqDeleteNm;	
	private String whkFlowChgCd;
	private String whkFlowChgNm;
	private String whkNewAcceptCd;	
	private String whkNewAcceptNm;	
	private String whkNewRejectCd;
	private String whkNewRejectNm;
	private String whkSignRequestCd;
	private String whkSignRequestNm;
	private String whkSignAcceptCd;
	private String whkSignAcceptNm;
	private String whkSignRejectCd;
	private String whkSignRejectNm;
	private String whkUserChgCd;
	private String whkUserChgNm;
	private String whkReqDoneCd;	
	private String whkReqDoneNm;
	private String whkMemoCd;
	private String whkMemoNm;
	private String useCd;
	private String useNm;
	public String getSrchEvent() {
		return srchEvent;
	}
	public void setSrchEvent(String srchEvent) {
		this.srchEvent = srchEvent;
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
	public String getUsrId() {
		return usrId;
	}
	public void setUsrId(String usrId) {
		this.usrId = usrId;
	}
	public String getWebhookId() {
		return webhookId;
	}
	public void setWebhookId(String webhookId) {
		this.webhookId = webhookId;
	}
	
	public String getWebhookNm() {
		return webhookNm;
	}
	public void setWebhookNm(String webhookNm) {
		this.webhookNm = webhookNm;
	}
	public String getPlatformTypeCd() {
		return platformTypeCd;
	}
	public void setPlatformTypeCd(String platformTypeCd) {
		this.platformTypeCd = platformTypeCd;
	}
	
	public String getPlatformTypeNm() {
		return platformTypeNm;
	}
	public void setPlatformTypeNm(String platformTypeNm) {
		this.platformTypeNm = platformTypeNm;
	}
	public String getWebhookUrl() {
		return webhookUrl;
	}
	public void setWebhookUrl(String webhookUrl) {
		this.webhookUrl = webhookUrl;
	}
	public String getWhkReqDeleteCd() {
		return whkReqDeleteCd;
	}
	public void setWhkReqDeleteCd(String whkReqDeleteCd) {
		this.whkReqDeleteCd = whkReqDeleteCd;
	}
	public String getWhkReqDeleteNm() {
		return whkReqDeleteNm;
	}
	public void setWhkReqDeleteNm(String whkReqDeleteNm) {
		this.whkReqDeleteNm = whkReqDeleteNm;
	}
	public String getWhkFlowChgCd() {
		return whkFlowChgCd;
	}
	public void setWhkFlowChgCd(String whkFlowChgCd) {
		this.whkFlowChgCd = whkFlowChgCd;
	}
	public String getWhkFlowChgNm() {
		return whkFlowChgNm;
	}
	public void setWhkFlowChgNm(String whkFlowChgNm) {
		this.whkFlowChgNm = whkFlowChgNm;
	}
	public String getWhkNewAcceptCd() {
		return whkNewAcceptCd;
	}
	public void setWhkNewAcceptCd(String whkNewAcceptCd) {
		this.whkNewAcceptCd = whkNewAcceptCd;
	}
	public String getWhkNewAcceptNm() {
		return whkNewAcceptNm;
	}
	public void setWhkNewAcceptNm(String whkNewAcceptNm) {
		this.whkNewAcceptNm = whkNewAcceptNm;
	}
	public String getWhkNewRejectCd() {
		return whkNewRejectCd;
	}
	public void setWhkNewRejectCd(String whkNewRejectCd) {
		this.whkNewRejectCd = whkNewRejectCd;
	}
	public String getWhkNewRejectNm() {
		return whkNewRejectNm;
	}
	public void setWhkNewRejectNm(String whkNewRejectNm) {
		this.whkNewRejectNm = whkNewRejectNm;
	}
	public String getWhkSignRequestCd() {
		return whkSignRequestCd;
	}
	public void setWhkSignRequestCd(String whkSignRequestCd) {
		this.whkSignRequestCd = whkSignRequestCd;
	}
	public String getWhkSignRequestNm() {
		return whkSignRequestNm;
	}
	public void setWhkSignRequestNm(String whkSignRequestNm) {
		this.whkSignRequestNm = whkSignRequestNm;
	}
	public String getWhkSignAcceptCd() {
		return whkSignAcceptCd;
	}
	public void setWhkSignAcceptCd(String whkSignAcceptCd) {
		this.whkSignAcceptCd = whkSignAcceptCd;
	}
	public String getWhkSignAcceptNm() {
		return whkSignAcceptNm;
	}
	public void setWhkSignAcceptNm(String whkSignAcceptNm) {
		this.whkSignAcceptNm = whkSignAcceptNm;
	}
	public String getWhkSignRejectCd() {
		return whkSignRejectCd;
	}
	public void setWhkSignRejectCd(String whkSignRejectCd) {
		this.whkSignRejectCd = whkSignRejectCd;
	}
	public String getWhkSignRejectNm() {
		return whkSignRejectNm;
	}
	public void setWhkSignRejectNm(String whkSignRejectNm) {
		this.whkSignRejectNm = whkSignRejectNm;
	}
	public String getWhkUserChgCd() {
		return whkUserChgCd;
	}
	public void setWhkUserChgCd(String whkUserChgCd) {
		this.whkUserChgCd = whkUserChgCd;
	}
	public String getWhkUserChgNm() {
		return whkUserChgNm;
	}
	public void setWhkUserChgNm(String whkUserChgNm) {
		this.whkUserChgNm = whkUserChgNm;
	}
	public String getWhkReqDoneCd() {
		return whkReqDoneCd;
	}
	public void setWhkReqDoneCd(String whkReqDoneCd) {
		this.whkReqDoneCd = whkReqDoneCd;
	}
	public String getWhkReqDoneNm() {
		return whkReqDoneNm;
	}
	public void setWhkReqDoneNm(String whkReqDoneNm) {
		this.whkReqDoneNm = whkReqDoneNm;
	}
	public String getWhkMemoCd() {
		return whkMemoCd;
	}
	public void setWhkMemoCd(String whkMemoCd) {
		this.whkMemoCd = whkMemoCd;
	}
	public String getWhkMemoNm() {
		return whkMemoNm;
	}
	public void setWhkMemoNm(String whkMemoNm) {
		this.whkMemoNm = whkMemoNm;
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
	
	
}
