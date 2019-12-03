package kr.opensoftlab.oslops.dsh.dsh4000.dsh4000.vo;

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

public class Dsh4000VO extends PageVO {
	
	
	/** 상세 기본 Defind */
	 private String    licGrpId;              /* 라이선스그룹 ID */ 
     private String    prjId;              /* 프로젝트 ID */ 
     private String    meaDtm;              /* 측정연월 */ 
     private String    meaFmDtm;              /* 측정연월 */ 
     public String getMeaFmDtm() {
		return meaFmDtm;
	}
	public void setMeaFmDtm(String meaFmDtm) {
		this.meaFmDtm = meaFmDtm;
	}
	private String    reportCd;              /* 보고서 구분 코드 */ 
     private String    reportNm;              /* 보고서 구분명 */ 
     private String    itemCd;              /* 측정항목 코드 */ 
     private String    itemNm;              /* 측정항목 명 */ 
     private String    indexCd;              /* 지표구분 코드 */ 
     private String    indexNm;              /* 지표구분 명 */ 
     private String    weightVal;              /* 가중치 */ 
     private String    periodCd;              /* 보고/평가주기 코드 */ 
     private String    periodNm;              /* 보고/평가주기 명 */ 
     private String    periodOrd;              /* 순번 */ 
     private String    processId;              /* 프로세스 ID */ 
     private String    meaVal;              /* 측정값 */ 
     private String    apprVal;              /* 평가수준 */ 
     private String    optVal;              /* 적정/부적정 */ 
     private String    modifyApprVal;              /* 변경 측정값 */ 
     private String    modifyOptVal;              /* 변경 평가수준 */

     private String    confYn;              /* 확정여부 */
     private String    writeYn;              /* 작성여부 */

     public String getConfYn() {
		return confYn;
	}
	public void setConfYn(String confYn) {
		this.confYn = confYn;
	}
	public String getWriteYn() {
		return writeYn;
	}
	public void setWriteYn(String writeYn) {
		this.writeYn = writeYn;
	}
	private String    confYnNm;              /* 확정여부 */
     private String    writeYnNm;              /* 작성여부 */

     private String    startDt;      
     private String    endDt;      
     
     private String    tmNm;      
     private String    chargerNm;      
     private String    pmNm;      
     
	public String getTmNm() {
		return tmNm;
	}
	public void setTmNm(String tmNm) {
		this.tmNm = tmNm;
	}
	public String getChargerNm() {
		return chargerNm;
	}
	public void setChargerNm(String chargerNm) {
		this.chargerNm = chargerNm;
	}
	public String getPmNm() {
		return pmNm;
	}
	public void setPmNm(String pmNm) {
		this.pmNm = pmNm;
	}
	public String getConfYnNm() {
		return confYnNm;
	}
	public void setConfYnNm(String confYnNm) {
		this.confYnNm = confYnNm;
	}
	public String getWriteYnNm() {
		return writeYnNm;
	}
	public void setWriteYnNm(String writeYnNm) {
		this.writeYnNm = writeYnNm;
	}
	public String getStartDt() {
		return startDt;
	}
	public void setStartDt(String startDt) {
		this.startDt = startDt;
	}
	public String getEndDt() {
		return endDt;
	}
	public void setEndDt(String endDt) {
		this.endDt = endDt;
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
	public String getMeaDtm() {
		return meaDtm;
	}
	public void setMeaDtm(String meaDtm) {
		this.meaDtm = meaDtm;
	}
	public String getReportCd() {
		return reportCd;
	}
	public void setReportCd(String reportCd) {
		this.reportCd = reportCd;
	}
	public String getReportNm() {
		return reportNm;
	}
	public void setReportNm(String reportNm) {
		this.reportNm = reportNm;
	}
	public String getItemCd() {
		return itemCd;
	}
	public void setItemCd(String itemCd) {
		this.itemCd = itemCd;
	}
	public String getItemNm() {
		return itemNm;
	}
	public void setItemNm(String itemNm) {
		this.itemNm = itemNm;
	}
	public String getIndexCd() {
		return indexCd;
	}
	public void setIndexCd(String indexCd) {
		this.indexCd = indexCd;
	}
	public String getIndexNm() {
		return indexNm;
	}
	public void setIndexNm(String indexNm) {
		this.indexNm = indexNm;
	}
	public String getWeightVal() {
		return weightVal;
	}
	public void setWeightVal(String weightVal) {
		this.weightVal = weightVal;
	}
	public String getPeriodCd() {
		return periodCd;
	}
	public void setPeriodCd(String periodCd) {
		this.periodCd = periodCd;
	}
	public String getPeriodNm() {
		return periodNm;
	}
	public void setPeriodNm(String periodNm) {
		this.periodNm = periodNm;
	}
	public String getPeriodOrd() {
		return periodOrd;
	}
	public void setPeriodOrd(String periodOrd) {
		this.periodOrd = periodOrd;
	}
	public String getProcessId() {
		return processId;
	}
	public void setProcessId(String processId) {
		this.processId = processId;
	}
	public String getMeaVal() {
		return meaVal;
	}
	public void setMeaVal(String meaVal) {
		this.meaVal = meaVal;
	}
	public String getApprVal() {
		return apprVal;
	}
	public void setApprVal(String apprVal) {
		this.apprVal = apprVal;
	}
	public String getOptVal() {
		return optVal;
	}
	public void setOptVal(String optVal) {
		this.optVal = optVal;
	}
	public String getModifyApprVal() {
		return modifyApprVal;
	}
	public void setModifyApprVal(String modifyApprVal) {
		this.modifyApprVal = modifyApprVal;
	}
	public String getModifyOptVal() {
		return modifyOptVal;
	}
	public void setModifyOptVal(String modifyOptVal) {
		this.modifyOptVal = modifyOptVal;
	} 
	
	
}
