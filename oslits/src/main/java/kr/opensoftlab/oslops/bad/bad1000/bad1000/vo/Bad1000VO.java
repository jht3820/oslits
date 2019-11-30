package kr.opensoftlab.oslops.bad.bad1000.bad1000.vo;

import kr.opensoftlab.oslops.com.vo.PageVO;

public class Bad1000VO extends PageVO {

	/** 상세 기본 Defind */
	private String rn;
	/** 라이선스 그룹 ID */
	private String licGrpId;
	/** 공지사항ID */
	private String badId;
	/** 글번호 */
	private String badNum;
	/** 글제목 */
	private String badTitle;
	/** 글내용 */
	private String badContent;
	/** 조회수 */
	private String badCnt;
	/** 작성자 ID */
	private String usrId;
	/** 작성자명 */
	private String usrNm;
	/** 공지 유무 */
	private String noticeYn;
	/** 직급 */
	private String usrPositionCdNm;
	/** 등록일 */
	private String modifyDtm;
	/** 수정일 */
	private String regDtm;
	/** 수정일시 */
	private String regDtmTime;
	/**공지여부이름*/
	private String noticeNm;
	/** 기간 검색 조건*/
	private String srchFromDt;
	private String srchToDt;
	
	public String getRegDtmTime() {
		return regDtmTime;
	}
	public void setRegDtmTime(String regDtmTime) {
		this.regDtmTime = regDtmTime;
	}
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
	
	
	public String getNoticeNm() {
		return noticeNm;
	}
	public void setNoticeNm(String noticeNm) {
		this.noticeNm = noticeNm;
	}
	public String getModifyDtm() {
		return modifyDtm;
	}
	public void setModifyDtm(String modifyDtm) {
		this.modifyDtm = modifyDtm;
	}
	public String getRegDtm() {
		return regDtm;
	}
	public void setRegDtm(String regDtm) {
		this.regDtm = regDtm;
	}
	
	
	public String getUsrPositionCdNm() {
		return usrPositionCdNm;
	}
	public void setUsrPositionCdNm(String usrPositionCdNm) {
		this.usrPositionCdNm = usrPositionCdNm;
	}
	/** 검색 조건 define */
	private String srchEvent;  /** Event : onLoad, onSelect */

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
	public String getBadId() {
		return badId;
	}
	public void setBadId(String badId) {
		this.badId = badId;
	}
	public String getBadNum() {
		return badNum;
	}
	public void setBadNum(String badNum) {
		this.badNum = badNum;
	}
	public String getBadTitle() {
		return badTitle;
	}
	public void setBadTitle(String badTitle) {
		this.badTitle = badTitle;
	}
	public String getBadContent() {
		return badContent;
	}
	public void setBadContent(String badContent) {
		this.badContent = badContent;
	}
	public String getBadCnt() {
		return badCnt;
	}
	public void setBadCnt(String badCnt) {
		this.badCnt = badCnt;
	}
	public String getUsrId() {
		return usrId;
	}
	public void setUsrId(String usrId) {
		this.usrId = usrId;
	}
	public String getUsrNm() {
		return usrNm;
	}
	public void setUsrNm(String usrNm) {
		this.usrNm = usrNm;
	}
	public String getNoticeYn() {
		return noticeYn;
	}
	public void setNoticeYn(String noticeYn) {
		this.noticeYn = noticeYn;
	}
	
}
