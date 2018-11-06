package kr.opensoftlab.sdf.svn.vo;

/**
 * @Class Name : SVNFileVO.java
 * @Description : SVNFileVO SVNFileVO class
 * @Modification Information
 *
 * @author 공대영
 * @since 2018.07.02
 * @version 1.0
 * @see
 *  
 *  Copyright (C)  All right reserved.
 */

public class SVNFileVO {

	private char type;

	private String path = "";
	
	private String kind = "";

	public String getKind() {
		return kind;
	}

	public void setKind(String kind) {
		this.kind = kind;
	}

	public char getType() {
		return type;
	}

	public void setType(char type) {
		this.type = type;
	}

	public String getPath() {
		return path;
	}

	public void setPath(String path) {
		this.path = path;
	}

	@Override
	public String toString() {
		// TODO Auto-generated method stub
		StringBuffer buffer  = new StringBuffer();

		buffer.append(" {  ");

		buffer.append(" path = "+path+", ");

		String typeName = "";
		
		if( 'A'==type ) {
			typeName = "Added";
		}else if( 'M'==type ) {
			typeName = "Modified";
		}else if( 'D'==type ) {
			typeName = "Deleted";
		}
		
		buffer.append(" type = "+typeName+" , ");
		
		buffer.append(" kind = "+kind+" ");	

		buffer.append(" }  ");

		return buffer.toString();
	}




}
