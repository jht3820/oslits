package kr.opensoftlab.oslops.req.req4000.req4400.service.impl;

import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import kr.opensoftlab.oslops.req.req4000.req4400.service.Req4400Service;
import kr.opensoftlab.oslops.req.req4000.req4400.vo.Req4400VO;

import org.springframework.stereotype.Service;

import egovframework.rte.fdl.cmmn.EgovAbstractServiceImpl;

/**
 * @Class Name : Req4300ServiceImpl.java
 * @Description : Req4300ServiceImpl Business Implement class
 * @Modification Information
 *
 * @author 진주영
 * @since 2018.09.11
 * @version 1.0
 * @see
 *  
 *  Copyright (C)  All right reserved.
 */
@Service("req4400Service")
public class Req4400ServiceImpl  extends EgovAbstractServiceImpl implements Req4400Service{
	/** Req4400DAO DI */
    @Resource(name="req4400DAO")
    private Req4400DAO req4400DAO;

	
	/**
	 * Req4400 요구사항 작업흐름별 작업 목록 조회(Grid page)
	 * @param Req4400VO
	 * @exception Exception
	 */
	@SuppressWarnings("rawtypes")
	public List selectReq4400ReqWorkList(Req4400VO req4400VO) throws Exception{
		return (List) req4400DAO.selectReq4400ReqWorkList(req4400VO);
	}
	
	/**
	 * Req4400 요구사항 작업흐름별 작업 목록 총 건수(Grid page)
	 * @param Req4400VO
	 * @return 
	 * @exception Exception
	 */
	public int selectReq4400ReqWorkListCnt(Req4400VO req4400VO) throws Exception {
		return req4400DAO.selectReq4400ReqWorkListCnt(req4400VO);
	}
	
	/**
	 * Req4400 요구사항 작업흐름별 작업 단건 조회 
	 * @param param - Map
	 * @exception Exception
	 */
	@SuppressWarnings("rawtypes")
	public Map selectReq4400ReqWorkInfo(Map paramMap) throws Exception{
		return (Map)req4400DAO.selectReq4400ReqWorkInfo(paramMap);
	}
	
	/**
	 * Req4400 담당 작업 단건 조회
	 * @param param - Map
	 * @exception Exception
	 */
	@SuppressWarnings("rawtypes")
	public List selectReq4400ReqChargerWorkList(Map paramMap) throws Exception{
		return (List)req4400DAO.selectReq4400ReqChargerWorkList(paramMap);
	}
	
	/**
	 * Req4400 요구사항 작업흐름별 작업 단건 추가
	 * @param param - Map
	 * @exception Exception
	 */
	@SuppressWarnings("rawtypes")
	public void insertReq4400ReqWorkInfoAjax(Map paramMap) throws Exception{
		req4400DAO.insertReq4400ReqWorkInfoAjax(paramMap);
	}
	
	/**
	 * Req4400 요구사항 작업흐름별 작업 단건 수정 
	 * @param param - Map
	 * @exception Exception
	 */
	@SuppressWarnings("rawtypes")
	public void updateReq4400ReqWorkInfoAjax(Map paramMap) throws Exception{
		req4400DAO.updateReq4400ReqWorkInfoAjax(paramMap);
	}
	
	/**
	 * Req4400 요구사항 작업흐름별 작업 단건 삭제
	 * @param param - Map
	 * @exception Exception
	 */
	@SuppressWarnings("rawtypes")
	public void deleteReq4400ReqWorkInfoAjax(Map paramMap) throws Exception{
		req4400DAO.deleteReq4400ReqWorkInfoAjax(paramMap);
	}
}
