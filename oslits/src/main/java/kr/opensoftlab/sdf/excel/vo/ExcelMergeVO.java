package kr.opensoftlab.sdf.excel.vo;

public class ExcelMergeVO {
	
	private String FROM;
	private String TO;
	
	public ExcelMergeVO(String from, String to){
		this.FROM = from;
		this.TO = to;
	}
	
	public String getFROM() {
		return FROM;
	}
	public void setFROM(String fROM) {
		FROM = fROM;
	}
	public String getTO() {
		return TO;
	}
	public void setTO(String tO) {
		TO = tO;
	}
}
