package kr.opensoftlab.oslops.adm.adm5000.adm5200.service;


import java.util.Map;


/**
 * @Class Name : Adm5200Service.java
 * @Description : 사용자 변경이력 관리(Adm5200) 서비스 인터페이스
 * @Modification Information
 * <pre>
 *   수정일      수정자          수정내용
 *  -------    --------    ---------------------------
 *  2018.08.25  배용진          최초 생성
 *  
 * </pre>
 *  @author 배용진
 *  @version 1.0
 *  @see 
 *  
 *  Copyright (C) Open Soft Lab Corp. All Rights Reserved
 */
public interface Adm5200Service {

	/**
	 * Adm5200 사용자 변경이력 등록
	 * @param param - Map
	 * @return 
	 * @exception Exception
	 */
	@SuppressWarnings("rawtypes")
	public void insertAdm5200UserChangeLog(Map paramMap) throws Exception;
	
	/**
	 * Adm5200  사용자 비밀번호 변경일 체크
	 * @param param - Map
	 * @return 
	 * @exception Exception
	 */
	@SuppressWarnings("rawtypes")
	public int selectAdm5200PwChangeDateCheck(Map paramMap) throws Exception;
	
	/**
	 * Adm5200 사용자가 1년이내 사용했던 비밀번호 체크
	 * @param param - Map
	 * @return 
	 * @exception Exception
	 */
	@SuppressWarnings("rawtypes")
	public String selectAdm5200BeforeUsedPwCheck(Map paramMap) throws Exception;
	
}
