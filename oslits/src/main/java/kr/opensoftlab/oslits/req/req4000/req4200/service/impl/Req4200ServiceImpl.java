package kr.opensoftlab.oslits.req.req4000.req4200.service.impl;

import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import kr.opensoftlab.oslits.req.req4000.req4200.service.Req4200Service;

import org.springframework.stereotype.Service;

import egovframework.com.cmm.EgovMessageSource;
import egovframework.rte.fdl.cmmn.EgovAbstractServiceImpl;

/**
 * @Class Name : Req4200ServiceImpl.java
 * @Description : Req4200ServiceImpl Business Implement class
 * @Modification Information
 *
 * @author 정형택
 * @since 2016.01.12.
 * @version 1.0
 * @see
 *  
 *  Copyright (C) OpenSoftLab Corp. All right reserved.
 */

@Service("req4200Service")
public class Req4200ServiceImpl extends EgovAbstractServiceImpl implements Req4200Service {

	/** Req4200DAO DI */
    @Resource(name="req4200DAO")
    private Req4200DAO req4200DAO;
    
    /** EgovMessageSource */
	@Resource(name = "egovMessageSource")
	EgovMessageSource egovMessageSource;

    /**
	 * Req4200 요구사항 분류 정보 목록 가져오기
	 * @param param - Map
	 * @return 
	 * @exception Exception
	 */
	@SuppressWarnings("rawtypes")
	public List selectReq4200ReqClsList(Map paramMap) throws Exception {
		return req4200DAO.selectReq4200ReqClsList(paramMap) ;
    }
	
	/**
	 * Req4000 요구사항 분류 배정 목록 가져오기
	 * @param param - Map
	 * @return 
	 * @exception Exception
	 */
	@SuppressWarnings("rawtypes")
	public List selectReq4200ReqClsAddListAjax(Map paramMap) throws Exception{
		return req4200DAO.selectReq4200ReqClsAddListAjax(paramMap) ;
	}
	
	
	/**
	 * Req4000 요구사항 분류 미배정 목록 가져오기
	 * @param param - Map
	 * @return 
	 * @exception Exception
	 */
	@SuppressWarnings("rawtypes")
	public List selectReq4200ReqClsDelListAjax(Map paramMap) throws Exception{
		return req4200DAO.selectReq4200ReqClsDelListAjax(paramMap) ;
	}
	
	/**
	 * Req4200 요구사항 분류에 요구사항 배정 및 삭제 처리
	 * @param param - Map
	 * @return 
	 * @exception Exception
	 */
	@SuppressWarnings("rawtypes")
	public void updateReq4200ReqClsAddDelListAjax(Map paramMap) throws Exception{
		int rsltCnt = (Integer) req4200DAO.updateReq4200ReqClsAddDelListAjax(paramMap);
		
		//인서트 처리 되지 않았으면 처리 실패
		if(rsltCnt <= 0){
			throw new Exception(egovMessageSource.getMessage("req4200.reqCls.fail"));
		}
	}
}
