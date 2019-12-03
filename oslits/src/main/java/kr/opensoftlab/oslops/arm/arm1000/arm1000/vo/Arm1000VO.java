package kr.opensoftlab.oslops.arm.arm1000.arm1000.vo;

/**
 * @Class Name : Arm1000VO.java
 * @Description : Arm1000 VO class
 * @Modification Information
 *
 * @author 진주영
 * @since 2018.01.05.
 * @version 1.0
 * @see
 *  
 *  Copyright (C) OpenSoftLab Corp All right reserved.
 */

import kr.opensoftlab.oslops.com.vo.PageVO;


public class Arm1000VO extends PageVO {
	
	/** 검색 조건 define */
	private String srchEvent;  /** Event : onLoad, onSelect */
	private String srchReqDesc;
	private String srchReqClsNm;
	private String srchReqNm;
	
	/** 상세 기본 Defind */
	private String rn;
	private String armId;
	private String usrId;
	private String sendUsrId;
	private String title;
	private String content;
	private String viewCheck;
	private String delCheck;
	private String reqIds;
	private String regDtm;
	private String CheckDtm;
	private String delDtm;
	
	
	public String getSrchEvent() {
		return srchEvent;
	}
	public void setSrchEvent(String srchEvent) {
		this.srchEvent = srchEvent;
	}
	public String getSrchReqDesc() {
		return srchReqDesc;
	}
	public void setSrchReqDesc(String srchReqDesc) {
		this.srchReqDesc = srchReqDesc;
	}
	public String getSrchReqClsNm() {
		return srchReqClsNm;
	}
	public void setSrchReqClsNm(String srchReqClsNm) {
		this.srchReqClsNm = srchReqClsNm;
	}
	public String getSrchReqNm() {
		return srchReqNm;
	}
	public void setSrchReqNm(String srchReqNm) {
		this.srchReqNm = srchReqNm;
	}
	public String getRn() {
		return rn;
	}
	public void setRn(String rn) {
		this.rn = rn;
	}
	public String getArmId() {
		return armId;
	}
	public void setArmId(String armId) {
		this.armId = armId;
	}
	public String getUsrId() {
		return usrId;
	}
	public void setUsrId(String usrId) {
		this.usrId = usrId;
	}
	public String getSendUsrId() {
		return sendUsrId;
	}
	public void setSendUsrId(String sendUsrId) {
		this.sendUsrId = sendUsrId;
	}
	public String getTitle() {
		return title;
	}
	public void setTitle(String title) {
		this.title = title;
	}
	public String getContent() {
		return content;
	}
	public void setContent(String content) {
		this.content = content;
	}
	public String getViewCheck() {
		return viewCheck;
	}
	public void setViewCheck(String viewCheck) {
		this.viewCheck = viewCheck;
	}
	public String getDelCheck() {
		return delCheck;
	}
	public void setDelCheck(String delCheck) {
		this.delCheck = delCheck;
	}
	public String getReqIds() {
		return reqIds;
	}
	public void setReqIds(String reqIds) {
		this.reqIds = reqIds;
	}
	public String getRegDtm() {
		return regDtm;
	}
	public void setRegDtm(String regDtm) {
		this.regDtm = regDtm;
	}
	public String getCheckDtm() {
		return CheckDtm;
	}
	public void setCheckDtm(String checkDtm) {
		CheckDtm = checkDtm;
	}
	public String getDelDtm() {
		return delDtm;
	}
	public void setDelDtm(String delDtm) {
		this.delDtm = delDtm;
	}
	
}
