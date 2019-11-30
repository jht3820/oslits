package kr.opensoftlab.oslops.cmm.cmm1000.cmm1700.vo;

import kr.opensoftlab.oslops.com.vo.PageVO;
import kr.opensoftlab.sdf.excel.annotation.ExcelColumn;

/**
 * 
 * @author Kong
 *
 */
public class Cmm1700VO extends PageVO {
	
    private String    licGrpId;              /* 라이선스그룹 ID */
	private String    prjId;              /* 프로젝트 ID */ 
	private String    authGrpId;              /* 권한 그룹 ID */ 
	private String    authGrpNm;              /* 권한명 */ 
	private String    authGrpDesc;              /* 권한설명 */ 
	private String    createDt;              /* 생성일자 */ 
	private String    useCd;              /* 사용여부(코드 : CMM00001) */ 
	private String    ord;              /* 권한순서 */ 
	private String    usrTyp;              /* 사용자 유형(코드 : ADM00004) */ 
	
	private String    searchPopTxt;               

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
	public String getAuthGrpId() {
		return authGrpId;
	}
	public void setAuthGrpId(String authGrpId) {
		this.authGrpId = authGrpId;
	}
	public String getAuthGrpNm() {
		return authGrpNm;
	}
	public void setAuthGrpNm(String authGrpNm) {
		this.authGrpNm = authGrpNm;
	}
	public String getAuthGrpDesc() {
		return authGrpDesc;
	}
	public void setAuthGrpDesc(String authGrpDesc) {
		this.authGrpDesc = authGrpDesc;
	}
	public String getCreateDt() {
		return createDt;
	}
	public void setCreateDt(String createDt) {
		this.createDt = createDt;
	}
	public String getUseCd() {
		return useCd;
	}
	public void setUseCd(String useCd) {
		this.useCd = useCd;
	}
	public String getOrd() {
		return ord;
	}
	public void setOrd(String ord) {
		this.ord = ord;
	}
	public String getUsrTyp() {
		return usrTyp;
	}
	public void setUsrTyp(String usrTyp) {
		this.usrTyp = usrTyp;
	}

	public String getSearchPopTxt() {
		return searchPopTxt;
	}
	public void setSearchPopTxt(String searchPopTxt) {
		this.searchPopTxt = searchPopTxt;
	}
    
}
