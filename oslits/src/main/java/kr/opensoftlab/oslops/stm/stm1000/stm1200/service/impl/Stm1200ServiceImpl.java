package kr.opensoftlab.oslops.stm.stm1000.stm1200.service.impl;



import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import kr.opensoftlab.oslops.stm.stm1000.stm1000.service.Stm1000Service;
import kr.opensoftlab.oslops.stm.stm1000.stm1000.vo.Stm1000VO;
import kr.opensoftlab.oslops.stm.stm1000.stm1200.service.Stm1200Service;
import kr.opensoftlab.oslops.stm.stm1000.stm1200.vo.Stm1200VO;

import org.springframework.stereotype.Service;

import egovframework.rte.fdl.cmmn.EgovAbstractServiceImpl;

/**
 * @Class Name : Stm1200ServiceImpl.java
 * @Description : Stm1200ServiceImpl Business Implement class
 * @Modification Information
 *
 * @author 공대영
 *  * @since 2018.07.23.
 * @version 1.0
 * @see
 *  
 *  Copyright (C)  All right reserved.
 */

@Service("stm1200Service")
public class Stm1200ServiceImpl extends EgovAbstractServiceImpl implements Stm1200Service {
	
	/** Stm1200DAO DI */
    @Resource(name="stm1200DAO")
    private Stm1200DAO stm1200DAO;  
	
    /**
	 * Stm1200 전체 API 목록을 조회한다.
	 * @param stm1200vo
	 * @return List - 전체 API 목록
	 * @exception Exception
	 */
	@Override
	public List<Stm1200VO> selectStm1200ProjectList(Stm1200VO stm1200vo) throws Exception{
		return stm1200DAO.selectStm1200ProjectList( stm1200vo);
	}
	
	/**
	 * Stm1200 그리드 페이징 처리를 위한 전체 API 목록 총 수를 조회한다.
	 * @param stm1200vo
	 * @return int 전체 API 목록 수
	 * @exception Exception
	 */
	@Override
	public int selectStm1200ProjectListCnt(Stm1200VO stm1200vo) throws Exception{
		return stm1200DAO.selectStm1200ProjectListCnt( stm1200vo);
	}
}
