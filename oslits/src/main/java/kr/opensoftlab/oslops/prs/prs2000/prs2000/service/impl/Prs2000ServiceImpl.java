package kr.opensoftlab.oslops.prs.prs2000.prs2000.service.impl;

import java.util.List;

import javax.annotation.Resource;

import kr.opensoftlab.oslops.dpl.dpl1000.dpl1000.vo.Dpl1000VO;
import kr.opensoftlab.oslops.prs.prs2000.prs2000.service.Prs2000Service;
import kr.opensoftlab.oslops.prs.prs2000.prs2000.vo.Prs2000VO;
import kr.opensoftlab.sdf.excel.ExcelDataListResultHandler;

import org.springframework.stereotype.Service;

import egovframework.rte.fdl.cmmn.EgovAbstractServiceImpl;

/**
 * @Class Name : Prs2000ServiceImpl.java
 * @Description : Prs2000ServiceImpl Business Implement class
 * @Modification Information
 *
 * @author 강재민
 * @since 2016.01.31
 * @version 1.0
 * @see
 *  
 *  Copyright (C)  All right reserved.
 */
@Service("prs2000Service")
public class Prs2000ServiceImpl extends EgovAbstractServiceImpl implements Prs2000Service{
	/** Prs2000DAO DI */
    @Resource(name="prs2000DAO")
    private Prs2000DAO prs2000DAO;

    
    
    /**
	 * 배정 프로젝트 확인 목록을 조회한다.
	 * @param Dpl2000VO
	 * @return List 요구사항 목록
	 * @throws Exception
	 */
	public List<Prs2000VO> selectPrs2000List(Prs2000VO prs2000VO) throws Exception {
		return prs2000DAO.selectPrs2000List(prs2000VO);
	}
	
	
	
	/**
	 * 배정 프로젝트 확인 목록 총건수를 조회한다.
	 * @param Dpl2000VO
	 * @return int 배정 프로젝트 확인 목록 총건수 
	 * @throws Exception
	 */
	public int selectPrs2000ListCnt(Prs2000VO prs2000VO) throws Exception {
		 return prs2000DAO.selectPrs2000ListCnt(prs2000VO);
	}
	

	/**
	 * 배정프로젝트 목록 엑셀 조회를 한다.
	 * @params prs2000VO
	 * @return List 배정프로젝트 목록
	 * @throws Exception
	 */
	@Override
	public void selectPrs2000ExcelList(Prs2000VO prs2000VO, ExcelDataListResultHandler resultHandler) throws Exception {
		prs2000DAO.selectPrs2000ExcelList(prs2000VO, resultHandler);
	}
	
	
}
