package kr.opensoftlab.oslops.dsh.dsh1000.dsh1000.service.impl;

/**
 * @Class Name : Dsh1000Service.java
 * @Description : Dsh1000Service Service class
 * @Modification Information
 *
 * @author 정형택
 * @since 2016.02.10.
 * @version 1.0
 * @see
 *  
 *  Copyright (C) OpenSoftLab Corp All right reserved.
 */

import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import kr.opensoftlab.oslops.dsh.dsh1000.dsh1000.service.Dsh1000Service;
import kr.opensoftlab.oslops.dsh.dsh1000.dsh1000.vo.Dsh1000VO;

import org.springframework.stereotype.Service;

import egovframework.rte.fdl.cmmn.EgovAbstractServiceImpl;

@Service("dsh1000Service")
public class Dsh1000ServiceImpl extends EgovAbstractServiceImpl implements Dsh1000Service {
	
	/** DAO Bean Injection */
    @Resource(name="dsh1000DAO")
    private Dsh1000DAO dsh1000DAO;  
    
	/**
	 * [차트1] 프로세스별 총갯수 + 최종 완료 갯수
	 * @param paramMap
	 * @return List
	 * @throws Exception
	 */
	@SuppressWarnings("rawtypes")
	public List selectDsh1000ProcessReqCntList(Map paramMap) throws Exception{
		return dsh1000DAO.selectDsh1000ProcessReqCntList(paramMap);
	}
	
	/**
	 * [차트2] 월별 프로세스별 요구사항 갯수
	 * @param paramMap
	 * @return List
	 * @throws Exception
	 */
	@SuppressWarnings("rawtypes")
	public List selectDsh1000MonthProcessReqCntList(Map paramMap) throws Exception{
		return dsh1000DAO.selectDsh1000MonthProcessReqCntList(paramMap);
	}
	
	@Override	
	public List selectDsh1000ReqList(Dsh1000VO dsh1000VO) throws Exception{
		return dsh1000DAO.selectDsh1000ReqList(dsh1000VO);
	}

	@Override
	public int selectDsh1000ReqListCnt(Dsh1000VO dsh1000VO) throws Exception{
		return dsh1000DAO.selectDsh1000ReqListCnt(dsh1000VO);
	}
}
