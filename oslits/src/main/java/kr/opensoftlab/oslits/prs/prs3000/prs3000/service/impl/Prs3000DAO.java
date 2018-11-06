package kr.opensoftlab.oslits.prs.prs3000.prs3000.service.impl;

import java.util.List;
import java.util.Map;

import kr.opensoftlab.oslits.com.dao.ComOslitsAbstractDAO;
import kr.opensoftlab.oslits.com.vo.LoginVO;

import org.springframework.stereotype.Repository;

/**
 * @Class Name : Prs3000DAO.java
 * @Description : Prs3000DAO DAO Class
 * @Modification Information
 *
 * @author 강재민
 * @since 2016.01.31
 * @version 1.0
 * @see
 *  
 *  Copyright (C)  All right reserved.
 */

@Repository("prs3000DAO")
public class Prs3000DAO extends ComOslitsAbstractDAO {
	/**
	 * selectPrs3000 개인정보를 조회한다.(단건)
	 * @param Map
	 * @return int
	 * @exception Exception
	 */
	@SuppressWarnings("rawtypes")
	public Map selectPrs3000(Map paramMap) throws Exception {
        return (Map) select("prs3000DAO.selectPrs3000", paramMap);    
    }   
	
	
	
	/**
	 * Prs3000 개인정보 수정 화면(이메일 체크) AJAX
	 * @param Map
	 * @return int
	 * @exception Exception
	 */
	@SuppressWarnings("rawtypes")
	public int selectPrs3000emailChRepAjax(Map paramMap) throws Exception {
		 return (int) select("prs3000DAO.selectPrs3000emailChRepAjax", paramMap);
    }
	
	
	
	/**
	 * Prs3000 개인정보 수정(UPDATE) AJAX
	 * @param Map
	 * @return int
	 * @exception Exception
	 */
	@SuppressWarnings("rawtypes")
	public int updatePrs3000(Map paramMap) throws Exception {
		 return (int) update("prs3000DAO.updatePrs3000", paramMap);
    }


	/**
	 * 이전 패스워드 조회
	 * @param paramMap
	 * @return
	 * @throws Exception
	 */
	@SuppressWarnings("rawtypes")
	public String selectPrs3000PwCheck(Map paramMap) throws Exception {
		return (String) select("prs3000DAO.selectPrs3000PwCheck", paramMap);
	}
	
}
