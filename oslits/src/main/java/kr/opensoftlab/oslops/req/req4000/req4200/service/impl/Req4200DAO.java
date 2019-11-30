package kr.opensoftlab.oslops.req.req4000.req4200.service.impl;

import java.util.List;
import java.util.Map;

import kr.opensoftlab.oslops.com.dao.ComOslitsAbstractDAO;
import kr.opensoftlab.oslops.req.req4000.req4200.vo.Req4200VO;

import org.springframework.stereotype.Repository;

/**
 * @Class Name : Req4200DAO.java
 * @Description : Req4200DAO DAO Class
 * @Modification Information
 *
 * @author 정형택
 * @since 2016.01.30.
 * @version 1.0
 * @see
 *  
 *  Copyright (C) OpenSoftLab Corp. All right reserved.
 */

@Repository("req4200DAO")
public class Req4200DAO extends ComOslitsAbstractDAO {
	
	/**
	 * Req4200 요구사항 분류정보 목록 가져오기
	 * @param param - Map
	 * @return 
	 * @exception Exception
	 */
	@SuppressWarnings("rawtypes")
	public List selectReq4200ReqClsList(Map paramMap) throws Exception {
		 return (List) list("req4200DAO.selectReq4200ReqClsList", paramMap);
    }
	
	/**
	 * Req4200 요구사항 분류 배정 목록 가져오기
	 * @param param - Map
	 * @return 
	 * @exception Exception
	 */
	@SuppressWarnings("rawtypes")
	public List selectReq4200ReqClsAddListAjax(Req4200VO req4200VO) throws Exception{
		return (List) list("req4200DAO.selectReq4200ReqClsAddListAjax", req4200VO);
	}
	
	/**
	 * Req4200 분류에 배정된 요구사항 총 건수 조회
	 * @param param - Map
	 * @return 
	 * @exception Exception
	 */
	public int selectReq4200ReqClsAddListCnt(Req4200VO req4200VO) throws Exception {
		return (Integer) select("req4200DAO.selectReq4100ReqClsAddListCnt", req4200VO);
	}
	
	/**
	 * Req4000 요구사항 분류 미배정 목록 가져오기
	 * @param param - Map
	 * @return 
	 * @exception Exception
	 */
	@SuppressWarnings("rawtypes")
	public List selectReq4200ReqClsDelListAjax(Req4200VO req4200VO) throws Exception{
		return (List) list("req4200DAO.selectReq4200ReqClsDelListAjax", req4200VO);
	}
	
	/**
	 * Req4200 분류에 미배정된 요구사항 총 건수 조회
	 * @param param - Map
	 * @return 
	 * @exception Exception
	 */
	public int selectReq4200ReqClsDelListCnt(Req4200VO req4200VO) throws Exception {
		return (Integer) select("req4200DAO.selectReq4100ReqClsDelListCnt", req4200VO);
	}
	
	/**
	 * Req4200 요구사항 분류에 요구사항 배정 및 삭제 처리
	 * @param param - Map
	 * @return 
	 * @exception Exception
	 */
	@SuppressWarnings("rawtypes")
	int updateReq4200ReqClsAddDelListAjax(Map paramMap) throws Exception{
		return update("req4200DAO.updateReq4200ReqClsAddDelListAjax", paramMap);
	}
	
}