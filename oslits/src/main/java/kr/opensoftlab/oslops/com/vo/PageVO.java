package kr.opensoftlab.oslops.com.vo;

/**
 * @Class Name : PageVO.java
 * @Description : PageVO VO class
 * @Modification Information
 * @Decription : 페이지 처리 공통 정보를 담은 VO 
 * 
 * @author 안세웅
 * @since 2016.01.22.
 * @version 1.0
 * @see
 *  
 *  Copyright (C) OpenSoftLab All right reserved.
 */

import java.beans.Expression;
import java.lang.reflect.Method;

import kr.opensoftlab.sdf.util.OslAgileConstant;
import egovframework.com.utl.fcc.service.EgovStringUtil;

	public class PageVO extends DefaultVO{ 
	
	/** 정렬순서(DESC,ASC) */
	private String sortOrdr = "";
	
	/** 검색조건(드롭다운) */
	private String searchSelect = "";
	
	/** 검색조건(텍스트) */
	private String searchTxt = "";

	/** 검색조건( 코드 ) */
	private String searchCd = "";
	
	/** 현재페이지 */
	private int pageIndex = OslAgileConstant.pageIndex;

	/** 페이지갯수 - 한 페이지에 표시되는 Record 수 */
	private int pageUnit = OslAgileConstant.pageUnit;

	/** 페이지사이즈 - 화면 하단의 페이지번호 표시되는 단위*/
	private int pageSize = OslAgileConstant.pageSize; 
 
	/** firstIndex */
	private int firstIndex = OslAgileConstant.firstIndex;

	/** lastIndex */
	private int lastIndex = OslAgileConstant.lastIndex;

	/** recordCountPerPage */
	private int recordCountPerPage = OslAgileConstant.recordCountPerPage;

	/** rowNo */ 
	private int rowNo = OslAgileConstant.rowNo; 
	
	/** pageIndex와 같은 값 (axisJ Grid)**/
	private int pageNo;

	/**
	 * sortOrdr attribute를 리턴한다.
	 * 
	 * @return the sortOrdr
	 */
	public String getSortOrdr() {
		return sortOrdr;
	}

	/**
	 * sortOrdr attribute 값을 설정한다.
	 * 
	 * @param sortOrdr
	 *            the sortOrdr to set
	 */
	public void setSortOrdr(String sortOrdr) {
		this.sortOrdr = sortOrdr;
	}

	/**
	 * pageIndex attribute를 리턴한다.
	 * 
	 * @return the pageIndex
	 */
	public int getPageIndex() {
		return pageIndex;
	}

	/**
	 * pageIndex attribute 값을 설정한다.
	 * 
	 * @param pageIndex
	 *            the pageIndex to set
	 */
	public void setPageIndex(int pageIndex) {
		this.pageIndex = pageIndex;
	}

	/**
	 * pageUnit attribute를 리턴한다.
	 * 
	 * @return the pageUnit
	 */
	public int getPageUnit() {
		return pageUnit;
	}

	/**
	 * pageUnit attribute 값을 설정한다.
	 * 
	 * @param pageUnit
	 *            the pageUnit to set
	 */
	public void setPageUnit(int pageUnit) {
		this.pageUnit = pageUnit;
	}

	public String getSearchTxt() {
		return searchTxt;
	}

	public void setSearchTxt(String searchTxt) {
		this.searchTxt = searchTxt;
	}

	/**
	 * pageSize attribute를 리턴한다.
	 * 
	 * @return the pageSize
	 */
	public int getPageSize() {
		return pageSize;
	}

	/**
	 * pageSize attribute 값을 설정한다.
	 * 
	 * @param pageSize
	 *            the pageSize to set
	 */
	public void setPageSize(int pageSize) {
		this.pageSize = pageSize;
	}

	/**
	 * firstIndex attribute를 리턴한다.
	 * 
	 * @return the firstIndex
	 */
	public int getFirstIndex() {
		return firstIndex;
	}

	/**
	 * firstIndex attribute 값을 설정한다.
	 * 
	 * @param firstIndex
	 *            the firstIndex to set
	 */
	public void setFirstIndex(int firstIndex) {
		this.firstIndex = firstIndex;
	}

	/**
	 * lastIndex attribute를 리턴한다.
	 * 
	 * @return the lastIndex
	 */
	public int getLastIndex() {
		return lastIndex;
	}

	public String getSearchSelect() {
		return searchSelect;
	}

	public void setSearchSelect(String searchSelect) {
		this.searchSelect = searchSelect;
	}

	/**
	 * lastIndex attribute 값을 설정한다.
	 * 
	 * @param lastIndex
	 *            the lastIndex to set
	 */
	public void setLastIndex(int lastIndex) {
		this.lastIndex = lastIndex;
	}

	/**
	 * recordCountPerPage attribute를 리턴한다.
	 * 
	 * @return the recordCountPerPage
	 */
	public int getRecordCountPerPage() {
		return recordCountPerPage;
	}

	/**
	 * recordCountPerPage attribute 값을 설정한다.
	 * 
	 * @param recordCountPerPage
	 *            the recordCountPerPage to set
	 */
	public void setRecordCountPerPage(int recordCountPerPage) {
		this.recordCountPerPage = recordCountPerPage;
	}

	/**
	 * rowNo attribute를 리턴한다.
	 * 
	 * @return the rowNo
	 */
	public int getRowNo() {
		return rowNo;
	}

	
	public int getPageNo() {
		return pageNo;
	}

	public void setPageNo(int pageNo) {
		this.pageNo = pageNo;
	}

	/**
	 * rowNo attribute 값을 설정한다.
	 * 
	 * @param rowNo
	 *            the rowNo to set
	 */
	public void setRowNo(int rowNo) {
		this.rowNo = rowNo;
	}
	public String toPrint() {
		StringBuffer sb = new StringBuffer();
		Method[] mth = this.getClass().getMethods();
		Expression exp = null;
		String key = "";
		sb.append("this Class Name =>");
		sb.append(this.getClass().getName());
		sb.append("\n");
		for( int i = 0 ; i < mth.length ; i ++) {
			if ( mth[i].getReturnType().getName().equals("java.lang.String") ) {
				if ( !mth[i].getName().equals("toPrint") ) {
					exp = new Expression(this, mth[i].getName(), null);
					try {
						key = exp.getMethodName();
						sb.append(  key + "=>" + EgovStringUtil.nullConvert(exp.getValue()) );
						sb.append("\n");
					} catch (Exception e) {
						//System.out.println(e.getMessage());
						e.printStackTrace();
					}
				}
			}
		}
		return sb.toString();
	}
	
	/** VO를 디폴트 값으로 초기화 시킨다. */
	public void initDefaultInfo(){
		this.setPageUnit(OslAgileConstant.pageUnit);
		this.setPageSize(OslAgileConstant.pageSize);
		this.setSortOrdr(OslAgileConstant.sortOrdr);
		this.setSearchSelect(OslAgileConstant.searchSelect);
		this.setSearchTxt(OslAgileConstant.searchTxt);
		this.setPageIndex(OslAgileConstant.pageIndex);
		this.setFirstIndex(OslAgileConstant.firstIndex);
		this.setLastIndex(OslAgileConstant.lastIndex);
		this.setRecordCountPerPage(OslAgileConstant.recordCountPerPage);
		this.setRowNo(OslAgileConstant.rowNo);
	}

	/**
	 * @return the searchCd
	 */
	public String getSearchCd() {
		return searchCd;
	}

	/**
	 * @param searchCd the searchCd to set
	 */
	public void setSearchCd(String searchCd) {
		this.searchCd = searchCd;
	}
	
}
