package kr.opensoftlab.oslits.cmm.cmm1000.cmm1600.service.impl;



import java.util.List;

import javax.annotation.Resource;

import kr.opensoftlab.oslits.cmm.cmm1000.cmm1600.service.Cmm1600Service;
import kr.opensoftlab.oslits.cmm.cmm1000.cmm1600.vo.Cmm1600VO;

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
	/** DAO Bean Injection */
    @Resource(name="cmm1600DAO")
    private Cmm1600DAO cmm1600DAO;

    /**
	 * 배포 조회 공통 목록 조회 
	 */
	@Override
	public List selectCmm1600CommonDplList(Cmm1600VO cmm1600vo) throws Exception {
		return cmm1600DAO.selectCmm1600CommonDplList(cmm1600vo);
	}
	/**
	 * 배포 조회 공통 목록 전체 카운트 조회
	 *  	
	 */
	@Override
	public int selectCmm1600CommonDplListCnt(Cmm1600VO cmm1600vo) throws Exception {
		return cmm1600DAO.selectCmm1600CommonDplListCnt(cmm1600vo);
	}
	
}
