package kr.opensoftlab.oslops.req.req4000.req4100.vo;

/**
 * @Class Name : Req1000Controller.java
 * @Description : Req1000Controller Controller class
 * @Modification Information
 *
 * @author 공대영
 * @since 2016.02.09.
 * @version 1.0
 * @see
 *  
 *  Copyright (C) OpenSoftLab Corp All right reserved.
 */

import kr.opensoftlab.oslops.com.vo.PageVO;
import kr.opensoftlab.sdf.excel.annotation.ExcelColumn;


public class Req4100VO extends PageVO {
	
	/** 검색 조건 define */
	private String srchEvent;  /** Event : onLoad, onSelect */
	private String srchReqDesc;
	private String srchReqClsNm;
	private String srchReqNm;
	/** 기간 검색 조건*/
	private String srchFromDt;
	private String srchToDt;
	
	/** 상세 기본 Defind */
	private String rn;
	private String licGrpId;
	private String prjId;
	private String selPrjId;
	private String popupPrjId;
	private String reqId;
	private String reqClsId;
	private String reqClsNm;
	/** 엑셀 컬럼 두번째 - 공문번호 */
	@ExcelColumn(position=1)
	private String reqNo;
	/** 엑셀 컬럼 일곱번째 - 요청자 ID */
	@ExcelColumn(position=6)
	private String reqUsrId;
	/** 엑셀 컬럼 여덟번째 - 요청자 명*/
	@ExcelColumn(position=7)
	private String reqUsrNm;
	/** 엑셀 컬럼 다섯번째 - 요청일자 */
	@ExcelColumn(position=4)
	private String reqDtm;
	/** 엑셀 컬럼 아홉번째 - 요청자 소속명 */
	@ExcelColumn(position=8)
	private String reqUsrDeptNm;
	/** 엑셀 컬럼 열번째 - 요청자 이메일 */
	@ExcelColumn(position=9)
	private String reqUsrEmail;
	/** 엑셀 컬럼 열한번째 - 요청자 연락처 */
	@ExcelColumn(position=10)
	private String reqUsrNum;
	/** 엑셀 컬럼 세번째 - 요구사항명 */
	@ExcelColumn(position=2)
	private String reqNm;
	/** 엑셀 컬럼 네번째 - 요구사항 설명 */
	@ExcelColumn(position=3)
	private String reqDesc;
	/** 엑셀 컬럼 열두번째 - 요청자 직급 */
	@ExcelColumn(position=11)
	private String reqUsrPositionNm;
	/** 엑셀 컬럼 열세번째 - 요청자 직책 */
	@ExcelColumn(position=12)
	private String reqUsrDutyNm;
	private String reqChargerId;
	private String reqCompleteRatio;
	private String reqFp;
	private String reqExFp;
	private String reqStDtm;
	private String reqEdDtm;
	private String reqStDuDtm;
	private String reqEdDuDtm;
	private String reqProType;
	private String reqProTypeNm;
	/** 엑셀 컬럼 첫번째 - 접수유형 */
	@ExcelColumn(position=0)
	private String reqNewType;
	private String reqNewTypeNm;
	private String reqAcceptTxt;
	private String sclCd;
	private String reqTypeCd;
	private String piaCd;
	private String labInp;
	private String atchFileId;
	private String cbAtchFileId;
	private String milestoneId;
	private String processId;
	private String processNm;
	private String flowId;
	private String flowNm;
	private String useCd;
	private String loginUsrId;
	private String regDtmDay;
	private String reqKey;
	
	private String restApiReqUsrNm;
	
	//보고서 페이지에서 사용되는 변수
	private String reportMode;
	private String searchStDtm;
	private String searchEdDtm;
	
	// 대시보드의 작업흐름의 담당/전체 요구사항 클릭시 사용변수
	private String type;
	
	public String getType() {
		return type;
	}
	public void setType(String type) {
		this.type = type;
	}
	/** 엑셀업로드 시에만 사용하기 위한 요청자 정보 직접입력 여부값 */
	/** 엑셀 컬럼 여섯번째 - 요청자 정보 직접입력 여부 */
	@ExcelColumn(position=5)
	private String reqInputType;

	public String getSrchFromDt() {
		return srchFromDt;
	}
	public void setSrchFromDt(String srchFromDt) {
		this.srchFromDt = srchFromDt;
	}
	public String getSrchToDt() {
		return srchToDt;
	}
	public void setSrchToDt(String srchToDt) {
		this.srchToDt = srchToDt;
	}
	public String getRestApiReqUsrNm() {
		return restApiReqUsrNm;
	}
	public void setRestApiReqUsrNm(String restApiReqUsrNm) {
		this.restApiReqUsrNm = restApiReqUsrNm;
	}
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
	
	public String getPopupPrjId() {
		return popupPrjId;
	}
	public void setPopupPrjId(String popupPrjId) {
		this.popupPrjId = popupPrjId;
	}
	public String getSelPrjId() {
		return selPrjId;
	}
	public void setSelPrjId(String selPrjId) {
		this.selPrjId = selPrjId;
	}
	public String getReqId() {
		return reqId;
	}
	public void setReqId(String reqId) {
		this.reqId = reqId;
	}
	public String getReqClsId() {
		return reqClsId;
	}
	public void setReqClsId(String reqClsId) {
		this.reqClsId = reqClsId;
	}
	public String getReqClsNm() {
		return reqClsNm;
	}
	public void setReqClsNm(String reqClsNm) {
		this.reqClsNm = reqClsNm;
	}
	public String getReqNo() {
		return reqNo;
	}
	public void setReqNo(String reqNo) {
		this.reqNo = reqNo;
	}
	public String getReqUsrId() {
		return reqUsrId;
	}
	public void setReqUsrId(String reqUsrId) {
		this.reqUsrId = reqUsrId;
	}
	public String getReqUsrNm() {
		return reqUsrNm;
	}
	public void setReqUsrNm(String reqUsrNm) {
		this.reqUsrNm = reqUsrNm;
	}
	public String getReqDtm() {
		return reqDtm;
	}
	public void setReqDtm(String reqDtm) {
		this.reqDtm = reqDtm;
	}
	public String getReqUsrDeptNm() {
		return reqUsrDeptNm;
	}
	public void setReqUsrDeptNm(String reqUsrDeptNm) {
		this.reqUsrDeptNm = reqUsrDeptNm;
	}
	
	public String getReqUsrPositionNm() {
		return reqUsrPositionNm;
	}
	public void setReqUsrPositionNm(String reqUsrPositionNm) {
		this.reqUsrPositionNm = reqUsrPositionNm;
	}
	public String getReqUsrDutyNm() {
		return reqUsrDutyNm;
	}
	public void setReqUsrDutyNm(String reqUsrDutyNm) {
		this.reqUsrDutyNm = reqUsrDutyNm;
	}
	public String getReqUsrEmail() {
		return reqUsrEmail;
	}
	public void setReqUsrEmail(String reqUsrEmail) {
		this.reqUsrEmail = reqUsrEmail;
	}
	public String getReqUsrNum() {
		return reqUsrNum;
	}
	public void setReqUsrNum(String reqUsrNum) {
		this.reqUsrNum = reqUsrNum;
	}
	public String getReqNm() {
		return reqNm;
	}
	public void setReqNm(String reqNm) {
		this.reqNm = reqNm;
	}
	public String getReqDesc() {
		return reqDesc;
	}
	public void setReqDesc(String reqDesc) {
		this.reqDesc = reqDesc;
	}
	public String getReqChargerId() {
		return reqChargerId;
	}
	public void setReqChargerId(String reqChargerId) {
		this.reqChargerId = reqChargerId;
	}
	
	public String getReqCompleteRatio() {
		return reqCompleteRatio;
	}
	public void setReqCompleteRatio(String reqCompleteRatio) {
		this.reqCompleteRatio = reqCompleteRatio;
	}
	
	public String getReqFp() {
		return reqFp;
	}
	public void setReqFp(String reqFp) {
		this.reqFp = reqFp;
	}
	public String getReqExFp() {
		return reqExFp;
	}
	public void setReqExFp(String reqExFp) {
		this.reqExFp = reqExFp;
	}
	public String getReqStDtm() {
		return reqStDtm;
	}
	public void setReqStDtm(String reqStDtm) {
		this.reqStDtm = reqStDtm;
	}
	
	public String getReqStDuDtm() {
		return reqStDuDtm;
	}
	public void setReqStDuDtm(String reqStDuDtm) {
		this.reqStDuDtm = reqStDuDtm;
	}
	public String getReqEdDuDtm() {
		return reqEdDuDtm;
	}
	public void setReqEdDuDtm(String reqEdDuDtm) {
		this.reqEdDuDtm = reqEdDuDtm;
	}
	public String getReqEdDtm() {
		return reqEdDtm;
	}
	public void setReqEdDtm(String reqEdDtm) {
		this.reqEdDtm = reqEdDtm;
	}
	public String getReqProType() {
		return reqProType;
	}
	public void setReqProType(String reqProType) {
		this.reqProType = reqProType;
	}
	public String getReqNewType() {
		return reqNewType;
	}
	public void setReqNewType(String reqNewType) {
		this.reqNewType = reqNewType;
	}
	public String getReqProTypeNm() {
		return reqProTypeNm;
	}
	public void setReqProTypeNm(String reqProTypeNm) {
		this.reqProTypeNm = reqProTypeNm;
	}
	public String getReqNewTypeNm() {
		return reqNewTypeNm;
	}
	public void setReqNewTypeNm(String reqNewTypeNm) {
		this.reqNewTypeNm = reqNewTypeNm;
	}
	public String getReqAcceptTxt() {
		return reqAcceptTxt;
	}
	public void setReqAcceptTxt(String reqAcceptTxt) {
		this.reqAcceptTxt = reqAcceptTxt;
	}
	
	public String getSclCd() {
		return sclCd;
	}
	public void setSclCd(String sclCd) {
		this.sclCd = sclCd;
	}
	public String getReqTypeCd() {
		return reqTypeCd;
	}
	public void setReqTypeCd(String reqTypeCd) {
		this.reqTypeCd = reqTypeCd;
	}
	public String getPiaCd() {
		return piaCd;
	}
	public void setPiaCd(String piaCd) {
		this.piaCd = piaCd;
	}
	public String getLabInp() {
		return labInp;
	}
	public void setLabInp(String labInp) {
		this.labInp = labInp;
	}
	public String getAtchFileId() {
		return atchFileId;
	}
	public void setAtchFileId(String atchFileId) {
		this.atchFileId = atchFileId;
	}
	public String getCbAtchFileId() {
		return cbAtchFileId;
	}
	public void setCbAtchFileId(String cbAtchFileId) {
		this.cbAtchFileId = cbAtchFileId;
	}
	public String getMilestoneId() {
		return milestoneId;
	}
	public void setMilestoneId(String milestoneId) {
		this.milestoneId = milestoneId;
	}
	public String getProcessId() {
		return processId;
	}
	public void setProcessId(String processId) {
		this.processId = processId;
	}
	public String getProcessNm() {
		return processNm;
	}
	public void setProcessNm(String processNm) {
		this.processNm = processNm;
	}
	public String getFlowId() {
		return flowId;
	}
	public void setFlowId(String flowId) {
		this.flowId = flowId;
	}
	public String getFlowNm() {
		return flowNm;
	}
	public void setFlowNm(String flowNm) {
		this.flowNm = flowNm;
	}
	public String getUseCd() {
		return useCd;
	}
	public void setUseCd(String useCd) {
		this.useCd = useCd;
	}
	public String getLoginUsrId() {
		return loginUsrId;
	}
	public void setLoginUsrId(String loginUsrId) {
		this.loginUsrId = loginUsrId;
	}
	public String getRegDtmDay() {
		return regDtmDay;
	}
	public void setRegDtmDay(String regDtmDay) {
		this.regDtmDay = regDtmDay;
	}
	public String getReqInputType() {
		return reqInputType;
	}
	public void setReqInputType(String reqInputType) {
		this.reqInputType = reqInputType;
	}
	public String getReqKey() {
		return reqKey;
	}
	public void setReqKey(String reqKey) {
		this.reqKey = reqKey;
	}
	public String getReportMode() {
		return reportMode;
	}
	public void setReportMode(String reportMode) {
		this.reportMode = reportMode;
	}
	public String getSearchStDtm() {
		return searchStDtm;
	}
	public void setSearchStDtm(String searchStDtm) {
		this.searchStDtm = searchStDtm;
	}
	public String getSearchEdDtm() {
		return searchEdDtm;
	}
	public void setSearchEdDtm(String searchEdDtm) {
		this.searchEdDtm = searchEdDtm;
	}
	
}
