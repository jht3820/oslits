package kr.opensoftlab.oslops.com.vo;

import javax.websocket.Session;

import java.io.Serializable;

/**
 * @Class Name : ServerSession.java
 * @Description : ServerSession VO class
 * @Modification Information
 *
 * @author 진주영
 * @since 2016.05.03
 * @version 1.0
 * @see
 *  
 *  Copyright (C)  All right reserved.
 */
public class ServerSession implements Serializable{
	
	private static final long serialVersionUID = 1L;
	
	private LoginVO loginVo;
	private Session session;
	private String authCode;
	private String prjId;
	private String sprId;
	private String selAuth;
	private String userId;
	private String userName;
	private String usrImgId;
	
	//들어온 시간
	private String joinDate;
	
	public ServerSession() {
		super();
	}
	public ServerSession(Session session,String authCode){
		this.session = session;
		this.authCode = authCode;
	}
	public LoginVO getLoginVo() {
		return loginVo;
	}
	public void setLoginVo(LoginVO loginVo) {
		this.loginVo = loginVo;
	}
	public Session getSession() {
		return session;
	}
	public void setSession(Session session) {
		this.session = session;
	}
	public String getAuthCode() {
		return authCode;
	}
	public void setAuthCode(String authCode) {
		this.authCode = authCode;
	}
	public String getPrjId() {
		return prjId;
	}
	public void setPrjId(String prjId) {
		this.prjId = prjId;
	}
	public String getSprId() {
		return sprId;
	}
	public void setSprId(String sprId) {
		this.sprId = sprId;
	}
	public String getSelAuth() {
		return selAuth;
	}
	public void setSelAuth(String selAuth) {
		this.selAuth = selAuth;
	}
	public String getUserId() {
		return userId;
	}
	public void setUserId(String userId) {
		this.userId = userId;
	}
	public String getJoinDate() {
		return joinDate;
	}
	public void setJoinDate(String joinDate) {
		this.joinDate = joinDate;
	}
	public static long getSerialversionuid() {
		return serialVersionUID;
	}
	public String getUserName() {
		return userName;
	}
	public void setUserName(String userName) {
		this.userName = userName;
	}
	public String getUsrImgId() {
		return usrImgId;
	}
	public void setUsrImgId(String usrImgId) {
		this.usrImgId = usrImgId;
	}
	
}
