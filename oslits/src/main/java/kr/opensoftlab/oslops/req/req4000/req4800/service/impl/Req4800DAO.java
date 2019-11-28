package kr.opensoftlab.oslops.req.req4000.req4800.service.impl;

import java.util.List;
import java.util.Map;

import kr.opensoftlab.oslops.com.dao.ComOslitsAbstractDAO;

import org.springframework.stereotype.Repository;

/**
 * @Class Name : Req4800DAO.java
 * @Description : Req4800DAO DAO Class
 * @Modification Information
 *
 * @author 진주영
 * @since 2018.08.30
 * @version 1.0
 * @see
 *  
 *  Copyright (C)  All right reserved.
 */

@Repository("req4800DAO")
public class Req4800DAO extends ComOslitsAbstractDAO {
	
	/**
	 * Req4800 요구사항 수정이력 목록 가져오기
	 * @param param - Map
	 * @return 
	 * @exception Exception
	 */	
	@SuppressWarnings({ "rawtypes", "unchecked" })
	public List<Map> selectReq4800ChgDetailList(Map paramMap) throws Exception{
		return (List<Map>) list("req4800DAO.selectReq4800ChgDetailList", paramMap);
	}
	
	/**
	 * Req4800 요구사항 수정이력 NEW_CHG_DETAIL_ID 구하기
	 * @param param - Map
	 * @return 
	 * @exception Exception
	 */	
	@SuppressWarnings("rawtypes")
	public String selectReq4800NewChgDetailId(Map paramMap) throws Exception{
		return (String)select("req4800DAO.selectReq4800NewChgDetailId", paramMap);
	}
	
	/**
	 * Req4800 요구사항 수정이력 등록
	 * @param param - Map
	 * @return 
	 * @exception Exception
	 */	
	@SuppressWarnings("rawtypes")
	public String insertReq4800ModifyHistoryInfo(Map paramMap) throws Exception{
		return (String)insert("req4800DAO.insertReq4800ModifyHistoryInfo", paramMap);
	}
	
	/**
	 * Req4800 요구사항 수정이력 삭제
	 * - 요구사항 삭제 시 해당 요구사항의 수정이력도 삭제한다.
	 * @param param - Map
	 * @return 
	 * @exception Exception
	 */
	@SuppressWarnings("rawtypes")
	public int deleteReq4800ReqHistoryInfo(Map paramMap) throws Exception{
		return (int) delete("req4800DAO.deleteReq4800ReqHistoryInfo", paramMap);
	}
	
}
