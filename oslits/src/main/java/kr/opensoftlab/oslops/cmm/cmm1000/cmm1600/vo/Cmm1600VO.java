package kr.opensoftlab.oslits.cmm.cmm1000.cmm1600.vo;

import kr.opensoftlab.oslits.com.vo.PageVO;
import kr.opensoftlab.sdf.excel.annotation.ExcelColumn;

/**
 * 
 * @author Kong
 *
 */
public class Cmm1600VO extends PageVO {
	
    private String    prjId;              /* 프로젝트 ID */ 
    private String    dplId;              /* 배포 ID */ 
    private String    dplNm;              /* 배포명 */  
    private String    dplDt;              /* 배포 일자 */ 
    private String    dplUsrId;              /* 배포자 */ 
    private String    dplUsrNm;              /* 배포자 */ 

	private String    dplTxt;              /* 배포 계획 설명 */

    private String    dplStsNm;              /* 배포 상태명 */

    private String    dplSts;              /* 배포 상태명 */

    public String getDplUsrNm() {
		return dplUsrNm;
	}
	public void setDplUsrNm(String dplUsrNm) {
		this.dplUsrNm = dplUsrNm;
	}
    
    public String getDplSts() {
		return dplSts;
	}
	public void setDplSts(String dplSts) {
		this.dplSts = dplSts;
	}
	public String getLicGrpId() {
		return licGrpId;
	}
	public void setLicGrpId(String licGrpId) {
		this.licGrpId = licGrpId;
	}
	private String    licGrpId;            

    public String getDplStsNm() {
		return dplStsNm;
	}
	public void setDplStsNm(String dplStsNm) {
		this.dplStsNm = dplStsNm;
	}
	private String    searchPopTxt;              /* 배포 계획 설명 */
	
    public String getSearchPopTxt() {
		return searchPopTxt;
	}
	public void setSearchPopTxt(String searchPopTxt) {
		this.searchPopTxt = searchPopTxt;
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
	public String getDplTxt() {
		return dplTxt;
	}
	public void setDplTxt(String dplTxt) {
		this.dplTxt = dplTxt;
	} 
    
}
