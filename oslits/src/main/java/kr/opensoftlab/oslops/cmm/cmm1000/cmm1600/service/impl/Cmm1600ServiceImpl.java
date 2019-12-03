package kr.opensoftlab.oslops.cmm.cmm1000.cmm1600.service.impl;



import java.util.List;

import javax.annotation.Resource;

import kr.opensoftlab.oslops.cmm.cmm1000.cmm1600.service.Cmm1600Service;
import kr.opensoftlab.oslops.cmm.cmm1000.cmm1600.vo.Cmm1600VO;

import org.springframework.stereotype.Service;

import egovframework.rte.fdl.cmmn.EgovAbstractServiceImpl;

/**
 * @Class Name : Cmm1600ServiceImpl.java
 * @Description : Cmm1600ServiceImpl Business Implement class
 * @Modification Information
 *
 * @author 공대영
 *  * @since 2018.10.16.
 * @version 1.0
 * @see
 *  
 *  Copyright (C)  All right reserved.
 */

@Service("cmm1600Service")
public class Cmm1600ServiceImpl extends EgovAbstractServiceImpl implements Cmm1600Service {
	
	
	/** Cmm1600DAO DI */
    @Resource(name="cmm1600DAO")
    private Cmm1600DAO cmm1600DAO;

    
    /**
	 * Cmm1600 배포 계획 목록을 조회 한다.(공통)
	 * @param cmm1600VO
	 * @return
	 * @throws Exception
	 */
    @Override
	public List<Cmm1600VO> selectCmm1600CommonDplList(Cmm1600VO cmm1600VO) throws Exception {
		return cmm1600DAO.selectCmm1600CommonDplList(cmm1600VO);
	}
    
    /**
	 * Cmm1600 배포 계획 목록의 총 건수를 조회한다. (그리드 페이징 처리)
	 * @param Cmm1600VO
	 * @return
	 * @throws Exception
	 */
	@Override
	public int selectCmm1600CommonDplListCnt(Cmm1600VO cmm1600VO) throws Exception {
		return cmm1600DAO.selectCmm1600CommonDplListCnt(cmm1600VO);
	}
	
}
