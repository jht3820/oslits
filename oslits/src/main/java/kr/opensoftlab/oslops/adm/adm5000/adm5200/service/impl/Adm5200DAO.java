package kr.opensoftlab.oslops.adm.adm5000.adm5200.service.impl;

import java.util.Map;

import kr.opensoftlab.oslops.com.dao.ComOslitsAbstractDAO;

import org.springframework.stereotype.Repository;


/**
 * @Class Name : Adm5200DAO.java
 * @Description : 사용자 변경이력 관리(Adm5200) DAO 클래스
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
@Repository("adm5200DAO")
public class Adm5200DAO  extends ComOslitsAbstractDAO {
	

	/**
	 * Adm5200 사용자 변경이력 등록
	 * @param param - Map
	 * @return 
	 * @exception Exception
	 */
	@SuppressWarnings("rawtypes")
	public void insertAdm5200UserChangeLog(Map paramMap) throws Exception{
		 insert("adm5200DAO.insertAdm5200UserChangeLog", paramMap);
	}
	
	
	/**
	 * Adm5200  사용자 비밀번호 변경일 체크
	 * @param param - Map
	 * @return 
	 * @exception Exception
	 */
	@SuppressWarnings("rawtypes")
	public int selectAdm5200PwChangeDateCheck(Map paramMap) throws Exception{
		return (Integer) select("adm5200DAO.selectAdm5200PwChangeDateCheck", paramMap);
	}
	
	
	/**
	 * Adm5200 사용자가 1년이내 사용했던 비밀번호 체크
	 * @param param - Map
	 * @return 
	 * @exception Exception
	 */
	@SuppressWarnings("rawtypes")
	public String selectAdm5200BeforeUsedPwCheck(Map paramMap) throws Exception{
		return (String) select("adm5200DAO.selectAdm5200BeforeUsedPwCheck", paramMap);
	}

	/**
	 * Adm5200 비밀번호 최근수정일시 조회
	 * @param param - Map
	 * @return 
	 * @exception Exception
	 */
	public String selectPrs3000recentPassAjax(Map<String, String> paramMap) {
		return (String) select("adm5200DAO.selectPrs3000recentPassAjax", paramMap);
	}	
}




