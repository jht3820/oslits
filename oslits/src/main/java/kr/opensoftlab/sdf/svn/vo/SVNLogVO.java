package kr.opensoftlab.sdf.svn.vo;

import java.text.SimpleDateFormat;


import java.util.Date;
import java.util.List;

/**
 * @Class Name : SVNLogVO.java
 * @Description : SVNLogVO SVNLogVO class
 * @Modification Information
 *
 * @author 공대영
 * @since 2018.07.02
 * @version 1.0
 * @see
 *  
 *  Copyright (C)  All right reserved.
 */

public class SVNLogVO {
	long revision = 0;
	
	String author ="";
	
	Date logDate = null;
	
	String sDate = "";
	
	public String getsDate() {
		SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd E HH:mm:ss");
		sDate =   format.format(logDate);
		return sDate;
	}


	public void setsDate(String sDate) {
		
		this.sDate = sDate;
	}


	String comment = "";

	
	public long getRevision() {
		return revision;
	}


	public void setRevision(long revision) {
		this.revision = revision;
	}


	public String getAuthor() {
		return author;
	}


	public void setAuthor(String author) {
		this.author = author;
	}


	public Date getLogDate() {
		return logDate;
	}


	public void setLogDate(Date logDate) {
		this.logDate = logDate;
	}


	public String getComment() {
		return comment;
	}


	public void setComment(String comment) {
		this.comment = comment;
	}


	public List<SVNFileVO> getSvnFileList() {
		return svnFileList;
	}


	public void setSvnFileList(List<SVNFileVO> svnFileList) {
		this.svnFileList = svnFileList;
	}


	List<SVNFileVO> svnFileList = null;



	@Override
	public String toString() {
		// TODO Auto-generated method stub
		StringBuffer buffer  = new StringBuffer();
				
		buffer.append(" {  ");
		
		buffer.append(" revision = "+revision+", ");
		
		buffer.append(" author = "+author+", ");
		
		buffer.append(" logDate = "+getsDate()+", ");
		
		buffer.append(" comment = "+comment +", ");
		
		buffer.append(" svnFileList =  "+svnFileList.toString() +"  ");
		
		buffer.append(" } \n  ");
		
		return buffer.toString();
	}
	
	
	
}
