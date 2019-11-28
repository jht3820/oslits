package kr.opensoftlab.oslops.com.vo;

/**
 * @Class Name : SessionVO.java
 * @Description : SessionVO VO class
 * @Modification Information
 *
 * @author 정형택
 * @since 2015.12.30.
 * @version 1.0
 * @see
 *  
 *  Copyright (C)  All right reserved.
 */
public class SessionVO extends DefaultVO{
    
    private String sessionId;
    private String usrId;
    
	public String getSessionId() {
		return sessionId;
	}
	public void setSessionId(String sessionId) {
		this.sessionId = sessionId;
	}
	public String getUsrId() {
		return usrId;
	}
	public void setUsrId(String usrId) {
		this.usrId = usrId;
	}
    
}
