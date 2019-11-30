package kr.opensoftlab.oslops.prs.prs4000.prs4000.service.impl;

import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import kr.opensoftlab.oslops.prs.prs4000.prs4000.service.Prs4000Service;

import org.springframework.stereotype.Service;

import egovframework.com.cmm.EgovMessageSource;
import egovframework.com.utl.fcc.service.EgovStringUtil;
import egovframework.rte.fdl.cmmn.EgovAbstractServiceImpl;

/**
 * @Class Name : Prs4000ServiceImpl.java
 * @Description : Prs4000ServiceImpl Business Implement class
 * @Modification Information
 *
 * @author 김정환
 * @since 2016.03.11
 * @version 1.0
 * @see
 *  
 *  Copyright (C)  All right reserved.
 */
@Service("prs4000Service")
public class Prs4000ServiceImpl extends EgovAbstractServiceImpl implements Prs4000Service{
	
	/** Prs4000DAO DI */
    @Resource(name="prs4000DAO")
    private Prs4000DAO prs4000DAO;
    
    /** EgovMessageSource */
	@Resource(name = "egovMessageSource")
	EgovMessageSource egovMessageSource;
    
	/**
	 * 일정 관리 내역을 조회한다.
	 * @param paramMap
	 * @return
	 * @throws Exception
	 */
	@SuppressWarnings("rawtypes")
	public List selectPrs4000List(Map paramMap) throws Exception {
		return prs4000DAO.selectPrs4000List(paramMap) ;
	}

	/**
	 * 일정관리 내역을 등록한다.
	 * @param paramMap
	 * @return
	 * @throws Exception
	 */
	@SuppressWarnings("rawtypes")
	public String insertPrs4000SchdInfo(Map paramMap) throws Exception{

		String schdSeq = prs4000DAO.insertPrs4000SchdInfo(paramMap);
		
		//생성된 키가 없으면 튕겨냄
		if( "".equals(EgovStringUtil.isNullToString(schdSeq)) ){
			throw new Exception(egovMessageSource.getMessage("fail.common.insert"));
		}
		
		return schdSeq;
		
	}
	
	/**
	 * 일정관리 내역을 수정한다.
	 * @param paramMap
	 * @throws Exception
	 */
	@SuppressWarnings({ "rawtypes" })
	public void updatePrs4000SchdInfo(Map paramMap) throws Exception{
		
		//메뉴정보 수정
		int upCnt = prs4000DAO.updatePrs4000SchdInfo(paramMap);
		
		//수정된 건이 없으면 튕겨냄
		if(upCnt == 0 ){
			throw new Exception(egovMessageSource.getMessage("fail.common.update"));
		}
	}
	
	/**
	 * 일정관리 내역을 삭제한다.
	 * @param paramMap
	 * @throws Exception
	 */
	@SuppressWarnings({ "rawtypes" })
	public void deletePrs4000SchdInfo(Map paramMap) throws Exception{
		prs4000DAO.deletePrs4000SchdInfo(paramMap);
	}
	
}
