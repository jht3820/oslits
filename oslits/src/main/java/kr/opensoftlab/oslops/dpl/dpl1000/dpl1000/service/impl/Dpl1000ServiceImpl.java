package kr.opensoftlab.oslits.dpl.dpl1000.dpl1000.service.impl;

import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import kr.opensoftlab.oslits.dpl.dpl1000.dpl1000.service.Dpl1000Service;
import kr.opensoftlab.oslits.dpl.dpl1000.dpl1000.vo.Dpl1000VO;
import kr.opensoftlab.sdf.excel.ExcelDataListResultHandler;

import org.springframework.stereotype.Service;

import egovframework.rte.fdl.cmmn.EgovAbstractServiceImpl;

/**
 * @Class Name : Dpl1000ServiceImpl.java
 * @Description : Dpl1000ServiceImpl Business Implement class
 * @Modification Information
 *
 * @author 정형택
 * @since 2016.01.30
 * @version 1.0
 * @see
 *  
 *  Copyright (C)  All right reserved.
 */
@Service("dpl1000Service")
public class Dpl1000ServiceImpl  extends EgovAbstractServiceImpl implements Dpl1000Service{

	/** Scpr5000DAO DI */
    @Resource(name="dpl1000DAO")
    private Dpl1000DAO dpl1000DAO;
    
    /**
	 * Dpl1000 배포 버전 리스트 조회
	 * @param
	 * @return 배포자 목록
	 * @exception Exception
	 */
	@SuppressWarnings("rawtypes")
	public List selectDpl1000DeployNmList(Map inputMap) throws Exception {
		return dpl1000DAO.selectDpl1000DeployNmList(inputMap);
    }
	
    /**
	 * Dpl1000 배포 버전 리스트 조회
	 * @param
	 * @return 배포 버전 정보 목록
	 * @exception Exception
	 */
	@SuppressWarnings("rawtypes")
	public List<Dpl1000VO> selectDpl1000DeployVerInfoList(Dpl1000VO dpl1000VO) throws Exception {
		return dpl1000DAO.selectDpl1000DeployVerInfoList(dpl1000VO);
    }
	
	/**
	 * Dpl1000 배포 버전 리스트 조회
	 * @param
	 * @return 배포 버전 정보 목록
	 * @exception Exception
	 */
	@SuppressWarnings("rawtypes")
	public Map selectDpl1000DeployVerInfo(Map map) throws Exception {
		return dpl1000DAO.selectDpl1000DeployVerInfo(map);
	}
	
    /**
	 * Dpl1000 배포 버전별 요구사항 등록 카운트
	 * @param
	 * @return 배포 버전별 요구사항 수
	 * @exception Exception
	 */
	@SuppressWarnings("rawtypes")
	public List selectDpl1000ReqCount(Map inputMap) throws Exception {
		return dpl1000DAO.selectDpl1000ReqCount(inputMap);
    }
	
	/**
	 * Dpl1000 배포 버전 등록
	 * @param param - Map
	 * @return 
	 * @exception Exception
	 */
	@SuppressWarnings("rawtypes")
	public void insertDpl1000DeployVerInfo(Map paramMap) throws Exception{
		dpl1000DAO.insertDpl1000DeployVerInfo(paramMap);
    }
	
	/**
	 * Dpl1000 배포 버전 수정
	 * @param param - Map
	 * @return 
	 * @exception Exception
	 */
	@SuppressWarnings("rawtypes")
	public void updateDpl1000DeployVerInfo(Map paramMap) throws Exception{
		dpl1000DAO.updateDpl1000DeployVerInfo(paramMap);
	}
	
	/**
	 * Dpl1000 배포 버전 리스트 삭제
	 * @param param - Map
	 * @return 
	 * @exception Exception
	 */
	@SuppressWarnings("rawtypes")
	public void deleteDpl1000DeployVerInfoList(Map paramMap) throws Exception{
		dpl1000DAO.deleteDpl1000DeployVerInfoList(paramMap);
    }
	
	/**
	 * Dpl1000 배포 버전 리스트 총 건수
	 * @param Dpl1000VO - dpl1000VO
	 * @return 
	 * @exception Exception
	 */
	@Override
	public int selectDpl1000ListCnt(Dpl1000VO dpl1000VO) throws Exception{
		return dpl1000DAO.selectDpl1000ListCnt(dpl1000VO);
	}
	

	/**
	 * 배포 버전 요구사항 목록을 엑셀 조회를 한다.
	 * @params dpl1000VO
	 * @return List 배포 버전 요구사항 목록
	 * @throws Exception
	 */
	@Override
	public void selectDpl1000ExcelList(Dpl1000VO dpl1000vo, ExcelDataListResultHandler resultHandler) throws Exception {
		dpl1000DAO.selectDpl1000ExcelList(dpl1000vo, resultHandler);
	}

	
	@Override
	public List<Dpl1000VO> selectDpl1000BuildInfoList(Dpl1000VO dpl1000VO) throws Exception{
		return dpl1000DAO.selectDpl1000BuildInfoList(dpl1000VO);
	}
	
	
	@Override
	public int selectDpl1000BuildInfoListCnt(Dpl1000VO dpl1000VO) throws Exception{
		return dpl1000DAO.selectDpl1000BuildInfoListCnt(dpl1000VO);
	}
		
	
	@Override
	public Map selectDpl1000BuildDetail(Map paramMap)  throws Exception {
		return dpl1000DAO.selectDpl1000BuildDetail(paramMap);
    }
	
	
}
