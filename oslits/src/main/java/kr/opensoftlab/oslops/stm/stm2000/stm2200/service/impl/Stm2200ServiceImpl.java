package kr.opensoftlab.oslops.stm.stm2000.stm2200.service.impl;

import java.util.List;

import javax.annotation.Resource;

import kr.opensoftlab.oslops.stm.stm2000.stm2200.service.Stm2200Service;
import kr.opensoftlab.oslops.stm.stm2000.stm2200.vo.Stm2200VO;

import org.springframework.stereotype.Service;

import egovframework.rte.fdl.cmmn.EgovAbstractServiceImpl;

/**
 * @Class Name : Stm2200ServiceImpl.java
 * @Description : Stm2200ServiceImpl Business Implement class
 * @Modification Information
 *
 * @author 공대영
 * @since 2018.07.02
 * @version 1.0
 * @see
 *  
 *  Copyright (C)  All right reserved.
 */
@Service("stm2200Service")
public class Stm2200ServiceImpl extends EgovAbstractServiceImpl implements Stm2200Service{
	
	/** Stm2200DAO DI */
	@Resource(name="stm2200DAO")
    private Stm2200DAO stm2200DAO;
	

	/**
	 * Stm2200 프로젝트 별 배정된 SVN Repository 전체 목록을 조회한다.
	 * @param stm2200vo
	 * @return List - 프로젝트 별 배정된 SVN Repository 전체 목록
	 * @exception Exception
	 */
	@Override
	public List<Stm2200VO> selectStm2200RepProjectList(Stm2200VO stm2200vo) throws Exception{
		return stm2200DAO.selectStm2200RepProjectList(stm2200vo);
	}
	
	/**
	 * Stm2200 프로젝트별 배정된 SVN Repository 전체 목록 총 건수를 조회한다.
	 * @param stm2200vo
	 * @return int - 프로젝트 별 배정된 SVN Repository 전체 목록 총 건수
	 * @exception Exception
	 */
	@Override
	public int selectStm2200RepProjectListCnt(Stm2200VO stm2200vo) throws Exception{
		return stm2200DAO.selectStm2200RepProjectListCnt(stm2200vo);
	}
	
	
}
