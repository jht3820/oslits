package kr.opensoftlab.oslops.dsh.dsh3000.dsh3000.service.impl;

/**
 * @Class Name : Dsh3000Service.java
 * @Description : Dsh3000Service Service class
 * @Modification Information
 *
 * @author 진주영
 * @since 2018.11.02.
 * @version 1.0
 * @see
 *  
 *  Copyright (C) OpenSoftLab Corp All right reserved.
 */

import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import kr.opensoftlab.oslops.dsh.dsh3000.dsh3000.service.Dsh3000Service;
import kr.opensoftlab.oslops.dsh.dsh3000.dsh3000.vo.Dsh3000VO;

import org.springframework.stereotype.Service;

import egovframework.rte.fdl.cmmn.EgovAbstractServiceImpl;

@Service("dsh3000Service")
public class Dsh3000ServiceImpl extends EgovAbstractServiceImpl implements Dsh3000Service {
	
	/** DAO Bean Injection */
    @Resource(name="dsh3000DAO")
    private Dsh3000DAO dsh3000DAO;  
    
    /**
	 * [차트1]  프로젝트별 총갯수 + 최종 완료 갯수
	 * @param paramMap
	 * @return List
	 * @throws Exception
	 */
	@SuppressWarnings("rawtypes")
	public List selectDsh3000PrjReqCntList(Map paramMap) throws Exception{
		return dsh3000DAO.selectDsh3000PrjReqCntList(paramMap);
	}
	
    /**
	 * [차트2]  월별 프로젝트별 요구사항 갯수
	 * @param paramMap
	 * @return List
	 * @throws Exception
	 */
	@SuppressWarnings("rawtypes")
	public List selectDsh3000MonthPrjReqCntList(Map paramMap) throws Exception{
		return dsh3000DAO.selectDsh3000MonthPrjReqCntList(paramMap);
	}

	/**
	 * [프로젝트별 차트] 프로세스별 요구사항 총갯수 + 최종 완료 갯수
	 * @param paramMap
	 * @return List
	 * @throws Exception
	 */
	@SuppressWarnings("rawtypes")
	public List selectDsh3000PrjProcessReqCntList(Map paramMap) throws Exception{
		return dsh3000DAO.selectDsh3000PrjProcessReqCntList(paramMap);
	}
	
	/**
	 * [프로젝트별 차트]  월별 프로세스별 요구사항 갯수
	 * @param paramMap
	 * @return List
	 * @throws Exception
	 */
	@SuppressWarnings("rawtypes")
	public List selectDsh3000MonthPrjProcessReqCntList(Map paramMap) throws Exception{
		return dsh3000DAO.selectDsh3000MonthPrjProcessReqCntList(paramMap);
	}

	/**
	 * 대시보드 프로젝트별 기간 비교 경고
	 * @param paramMap
	 * @return List
	 * @throws Exception
	 */
	@SuppressWarnings("rawtypes")
	public List selectDsh3000PrjReqDtmOverAlertList(Map paramMap) throws Exception{
		return dsh3000DAO.selectDsh3000PrjReqDtmOverAlertList(paramMap);
	}

	/**
	 * [전체 그룹 차트] 프로젝트 그룹별 총갯수 + 최종 완료 갯수
	 * @param paramMap
	 * @return List
	 * @throws Exception
	 */
	@SuppressWarnings("rawtypes")
	public List selectDsh3000AllPrjGrpReqCntList(Map paramMap) throws Exception{
		return dsh3000DAO.selectDsh3000AllPrjGrpReqCntList(paramMap);
	}
	
	/**
	 * [전체 그룹 차트] 월별 프로젝트 그룹별 요구사항 갯수
	 * @param paramMap
	 * @return List
	 * @throws Exception
	 */
	@SuppressWarnings("rawtypes")
	public List selectDsh3000MonthAllPrjGrpReqCntList(Map paramMap) throws Exception{
		return dsh3000DAO.selectDsh3000MonthAllPrjGrpReqCntList(paramMap);
	}

	/**
	 * [프로젝트 그룹별 차트] 프로젝트 그룹별 총갯수 + 최종 완료 갯수
	 * @param paramMap
	 * @return List
	 * @throws Exception
	 */
	@SuppressWarnings("rawtypes")
	public List selectDsh3000PrjGrpReqCntList(Map paramMap) throws Exception{
		return dsh3000DAO.selectDsh3000PrjGrpReqCntList(paramMap);
	}
	
	/**
	 * [프로젝트 그룹별 차트]  월별 프로젝트 그룹별 요구사항 갯수
	 * @param paramMap
	 * @return List
	 * @throws Exception
	 */
	@SuppressWarnings("rawtypes")
	public List selectDsh3000MonthPrjGrpReqCntList(Map paramMap) throws Exception{
		return dsh3000DAO.selectDsh3000MonthPrjGrpReqCntList(paramMap);
	}
	
	/**
	 * 대시보드 프로젝트 그룹별 기간 비교 경고
	 * @param paramMap
	 * @return List
	 * @throws Exception
	 */
	@SuppressWarnings("rawtypes")
	public List selectDsh3000PrjGrpReqDtmOverAlertList(Map paramMap) throws Exception{
		return dsh3000DAO.selectDsh3000PrjGrpReqDtmOverAlertList(paramMap);
	}
	
	/**
	 * 통합대시보드 - 통합데이터, 프로젝트 그룹별 신호등, 차트 클릭 시 요구사항 목록 조회 
	 * @param paramMap
	 * @return List
	 * @throws Exception
	 */
	@SuppressWarnings("rawtypes")
	public List selectDsh3000ReqList(Dsh3000VO dsh3000VO) throws Exception{
		return dsh3000DAO.selectDsh3000ReqList(dsh3000VO);
	}

	/**
	 * 통합대시보드 - 통합데이터, 프로젝트 그룹별 신호등, 차트 클릭 시 요구사항 목록 조회 
	 * @param paramMap
	 * @return List
	 * @throws Exception
	 */
	public int selectDsh3000ReqListCnt(Dsh3000VO dsh3000VO) throws Exception{
		return dsh3000DAO.selectDsh3000ReqListCnt(dsh3000VO);
	}
}
