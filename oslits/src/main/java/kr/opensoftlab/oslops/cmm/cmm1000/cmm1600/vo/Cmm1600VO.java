package kr.opensoftlab.oslops.cmm.cmm1000.cmm1600.vo;

import kr.opensoftlab.oslops.com.vo.PageVO;
import kr.opensoftlab.sdf.excel.annotation.ExcelColumn;

/**
 * 
 * @author Kong
 *
 */
public class Cmm1600VO extends PageVO {
	
	private String rn;
	
	/** 라이선스 그룹 ID */
	private String licGrpId;
	/** 프로젝트 ID */
	private String prjId; 
	/** 배포 ID */
	private String dplId;		
	/** 배포 명 */
	private String dplNm; 		
	/** 배포 버전 */
	private String dplVer;  		
	/** 배포 일시 */
	private String dplDt;		
	/** 배포자 ID */
	private String dplUsrId;  		
	/** 배포자 명 */
	private String dplUsrNm;  		
	/** 배포 설명 */
	private String dplDesc;
	/** 배포 진행상태 코드 (공통코드: DPL00001)*/
	private String dplStsCd;
	/** 배포 진행상태 명 */
	private String dplStsNm;  		
	/** 배포 방법 코드(공통코드: DPL00003) */
	private String dplTypeCd;
	/** 배포 방법 명 */
	private String dplTypeNm;  	
	/** 자동배포 후 처리 코드 (공통코드: DPL00004) */
	private String dplAutoAfterCd; 
	/** 자동배포 후 처리 명 */
	private String dplAutoAfterNm; 
	/** 자동배포 일시 */
	private String dplAutoDtm;
	/** 원복 타입 코드 (공통코드: DPL00005) */
	private String dplRestoreCd;  	
	/** 원복 타입 명 */
	private String dplRestoreNm;
	/** 결제 상태 코드 */
	private String signStsCd;  	
	/** 결제 상태 명 */
	private String signStsNm;
	/** 결제자 ID */
	private String signUsrId;
	/** 결제자 명 */
	private String signUsrNm;
	/** 결제자 이미지 */
	private String signUsrImg;
	/** 결제 일시 */
	private String signDtm;
	
	
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
	public String getDplId() {
		return dplId;
	}
	public void setDplId(String dplId) {
		this.dplId = dplId;
	}
	public String getDplNm() {
		return dplNm;
	}
	public void setDplNm(String dplNm) {
		this.dplNm = dplNm;
	}
	public String getDplVer() {
		return dplVer;
	}
	public void setDplVer(String dplVer) {
		this.dplVer = dplVer;
	}
	public String getDplDt() {
		return dplDt;
	}
	public void setDplDt(String dplDt) {
		this.dplDt = dplDt;
	}
	public String getDplUsrId() {
		return dplUsrId;
	}
	public void setDplUsrId(String dplUsrId) {
		this.dplUsrId = dplUsrId;
	}
	public String getDplUsrNm() {
		return dplUsrNm;
	}
	public void setDplUsrNm(String dplUsrNm) {
		this.dplUsrNm = dplUsrNm;
	}
	public String getDplDesc() {
		return dplDesc;
	}
	public void setDplDesc(String dplDesc) {
		this.dplDesc = dplDesc;
	}
	public String getDplStsCd() {
		return dplStsCd;
	}
	public void setDplStsCd(String dplStsCd) {
		this.dplStsCd = dplStsCd;
	}
	public String getDplStsNm() {
		return dplStsNm;
	}
	public void setDplStsNm(String dplStsNm) {
		this.dplStsNm = dplStsNm;
	}
	public String getDplTypeCd() {
		return dplTypeCd;
	}
	public void setDplTypeCd(String dplTypeCd) {
		this.dplTypeCd = dplTypeCd;
	}
	public String getDplTypeNm() {
		return dplTypeNm;
	}
	public void setDplTypeNm(String dplTypeNm) {
		this.dplTypeNm = dplTypeNm;
	}
	public String getDplAutoAfterCd() {
		return dplAutoAfterCd;
	}
	public void setDplAutoAfterCd(String dplAutoAfterCd) {
		this.dplAutoAfterCd = dplAutoAfterCd;
	}
	public String getDplAutoAfterNm() {
		return dplAutoAfterNm;
	}
	public void setDplAutoAfterNm(String dplAutoAfterNm) {
		this.dplAutoAfterNm = dplAutoAfterNm;
	}
	public String getDplAutoDtm() {
		return dplAutoDtm;
	}
	public void setDplAutoDtm(String dplAutoDtm) {
		this.dplAutoDtm = dplAutoDtm;
	}
	public String getDplRestoreCd() {
		return dplRestoreCd;
	}
	public void setDplRestoreCd(String dplRestoreCd) {
		this.dplRestoreCd = dplRestoreCd;
	}
	public String getDplRestoreNm() {
		return dplRestoreNm;
	}
	public void setDplRestoreNm(String dplRestoreNm) {
		this.dplRestoreNm = dplRestoreNm;
	}
	public String getSignStsCd() {
		return signStsCd;
	}
	public void setSignStsCd(String signStsCd) {
		this.signStsCd = signStsCd;
	}
	public String getSignStsNm() {
		return signStsNm;
	}
	public void setSignStsNm(String signStsNm) {
		this.signStsNm = signStsNm;
	}
	public String getSignUsrId() {
		return signUsrId;
	}
	public void setSignUsrId(String signUsrId) {
		this.signUsrId = signUsrId;
	}
	public String getSignUsrNm() {
		return signUsrNm;
	}
	public void setSignUsrNm(String signUsrNm) {
		this.signUsrNm = signUsrNm;
	}
	public String getSignUsrImg() {
		return signUsrImg;
	}
	public void setSignUsrImg(String signUsrImg) {
		this.signUsrImg = signUsrImg;
	}
	public String getSignDtm() {
		return signDtm;
	}
	public void setSignDtm(String signDtm) {
		this.signDtm = signDtm;
	}
}
