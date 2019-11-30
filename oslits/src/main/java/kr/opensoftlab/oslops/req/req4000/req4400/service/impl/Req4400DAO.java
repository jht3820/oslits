package kr.opensoftlab.oslops.req.req4000.req4400.service.impl;

import java.util.List;
import java.util.Map;

import kr.opensoftlab.oslops.com.dao.ComOslitsAbstractDAO;
import kr.opensoftlab.oslops.req.req4000.req4400.vo.Req4400VO;

import org.springframework.stereotype.Repository;

/**
 * @Class Name : Req4400DAO.java
 * @Description : Req4400DAO DAO Class
 * @Modification Information
 *
 * @author 진주영
 * @since 2018.09.11
 * @version 1.0
 * @see
 *  
 *  Copyright (C)  All right reserved.
 */

@Repository("req4400DAO")
public class Req4400DAO extends ComOslitsAbstractDAO {

	/**
	 * Req4400 요구사항 작업흐름별 작업 목록 조회(Grid page)
	 * @param Req4400VO
	 * @exception Exception
	 */
	@SuppressWarnings("rawtypes")
	public List selectReq4400ReqWorkList(Req4400VO req4400VO) throws Exception{
		return (List) list("req4400DAO.selectReq4400ReqWorkList",req4400VO);
	}
	
	/**
	 * Req4400 요구사항 작업흐름별 작업 목록 총 건수(Grid page)
	 * @param Req4400VO
	 * @return 
	 * @exception Exception
	 */
	public int selectReq4400ReqWorkListCnt(Req4400VO req4400VO) throws Exception {
		return (Integer) select("req4400DAO.selectReq4400ReqWorkListCnt", req4400VO);
	}
	
	/**
	 * Req4400 요구사항 작업흐름별 작업 단건 조회 
	 * @param param - Map
	 * @exception Exception
	 */
	@SuppressWarnings("rawtypes")
	public Map selectReq4400ReqWorkInfo(Map paramMap) throws Exception{
		return (Map) select("req4400DAO.selectReq4400ReqWorkInfo",paramMap);
	}

	/**
	 * Req4400 담당 작업 단건 조회
	 * @param param - Map
	 * @exception Exception
	 */
	@SuppressWarnings("rawtypes")
	public List selectReq4400ReqChargerWorkList(Map paramMap) throws Exception{
		return (List) list("req4400DAO.selectReq4400ReqChargerWorkList",paramMap);
	}
	
	/**
	 * Req4400 요구사항 작업흐름별 작업 단건 추가
	 * @param param - Map
	 * @exception Exception
	 */
	@SuppressWarnings("rawtypes")
	public void insertReq4400ReqWorkInfoAjax(Map paramMap) throws Exception{
		insert("req4400DAO.insertReq4400ReqWorkInfoAjax",paramMap);
	}
	
	/**
	 * Req4400 요구사항 작업흐름별 작업 단건 수정 
	 * @param param - Map
	 * @exception Exception
	 */
	@SuppressWarnings("rawtypes")
	public void updateReq4400ReqWorkInfoAjax(Map paramMap) throws Exception{
		update("req4400DAO.updateReq4400ReqWorkInfoAjax",paramMap);
	}
	
	/**
	 * Req4400 요구사항 작업흐름별 작업 단건 삭제
	 * @param param - Map
	 * @exception Exception
	 */
	@SuppressWarnings("rawtypes")
	public void deleteReq4400ReqWorkInfoAjax(Map paramMap) throws Exception{
		delete("req4400DAO.deleteReq4400ReqWorkInfoAjax",paramMap);
	}
}
