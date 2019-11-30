package kr.opensoftlab.oslops.req.req2000.req2100.service.impl;

/**
 * @Class Name : Req2100Service.java
 * @Description : Req2100Service Service class
 * @Modification Information
 *
 * @author 배용진
 * @since 2018.11.07.
 * @version 1.0
 * @see
 *  
 *  Copyright (C) OpenSoftLab Corp All right reserved.
 */

import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import kr.opensoftlab.oslops.req.req2000.req2100.service.Req2100Service;

import org.springframework.stereotype.Service;

import egovframework.rte.fdl.cmmn.EgovAbstractServiceImpl;

@Service("req2100Service")
public class Req2100ServiceImpl extends EgovAbstractServiceImpl implements Req2100Service {

	/** DAO Bean Injection */
	@Resource(name="req2100DAO")
	private Req2100DAO req2100DAO;
	
	/**
	 * Req2100 프로세스 별 요구사항 처리완료 건수 조회
	 * @param Map
	 * @return List
	 * @throws Exception
	 */
	@SuppressWarnings("rawtypes")
	@Override
	public List<Map> selectReq2100ProcessReqEndCntAjax(Map paramMap) throws Exception {
		return req2100DAO.selectReq2100ProcessReqEndCntAjax(paramMap);
	}

}
