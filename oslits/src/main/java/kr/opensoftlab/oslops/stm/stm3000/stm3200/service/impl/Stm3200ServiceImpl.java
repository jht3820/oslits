package kr.opensoftlab.oslops.stm.stm3000.stm3200.service.impl;

import java.util.List;

import javax.annotation.Resource;

import kr.opensoftlab.oslops.stm.stm3000.stm3200.service.Stm3200Service;
import kr.opensoftlab.oslops.stm.stm3000.stm3200.vo.Stm3200VO;

import org.springframework.stereotype.Service;

import egovframework.rte.fdl.cmmn.EgovAbstractServiceImpl;

/**
 * @Class Name : Stm3200ServiceImpl.java
 * @Description : Stm3200ServiceImpl Business Implement class
 * @Modification Information
 *
 * @author 공대영
 * @since 2018.07.02
 * @version 1.0
 * @see
 *  
 *  --------------------------------------
 *  수정일			수정자			수정내용
 *  --------------------------------------
 *  2019-03-11		배용진		 	기능 개선
 *  
 *  --------------------------------------
 *  
 *  Copyright (C)  All right reserved.
 */
@Service("stm3200Service")
public class Stm3200ServiceImpl extends EgovAbstractServiceImpl implements Stm3200Service{
	
	/** Stm3200DAO DI */
	@Resource(name="stm3200DAO")
    private Stm3200DAO stm3200DAO;
	
	/**
	 * Stm3200 라이선스 그룹의 각 프로젝트에 배정된 JENKINS JOB 전체 목록을 조회한다.
	 * @param Stm3200VO
	 * @return List - 프로젝트에 배정된 Jenkins Job 목록
	 * @throws Exception
	 */
	@Override
	public List<Stm3200VO> selectStm3200ProjectJenkinsJobAllList(Stm3200VO stm3200VO) throws Exception{
		return stm3200DAO.selectStm3200ProjectJenkinsJobAllList(stm3200VO);
	}

	/**
	 * Stm3200 라이선스 그룹의 각 프로젝트에 배정된 JENKINS JOB 전체 목록 총 건수를 조회한다.
	 * JENKINS 저장소 전체 현황 화면의 그리드 페이징 처리위해
	 * @param Stm3200VO
	 * @return List - 프로젝트에 배정된 Jenkins Job 목록
	 * @throws Exception
	 */
	@Override
	public int selectStm3200ProjectJenkinsJobAllListCnt(Stm3200VO stm3200VO) throws Exception{
		return stm3200DAO.selectStm3200ProjectJenkinsJobAllListCnt(stm3200VO);
	}
}
