package kr.opensoftlab.oslops.cmm.cmm1000.cmm1700.service.impl;



import java.util.List;

import javax.annotation.Resource;

import kr.opensoftlab.oslops.cmm.cmm1000.cmm1700.service.Cmm1700Service;
import kr.opensoftlab.oslops.cmm.cmm1000.cmm1700.vo.Cmm1700VO;

import org.springframework.stereotype.Service;

import egovframework.rte.fdl.cmmn.EgovAbstractServiceImpl;

/**
 * @Class Name : Cmm1700ServiceImpl.java
 * @Description : Cmm1700ServiceImpl Business Implement class
 * @Modification Information
 *
 * @author 공대영
 *  * @since 2018.10.16.
 * @version 1.0
 * @see
 *  
 *  Copyright (C)  All right reserved.
 */

@Service("cmm1700Service")
public class Cmm1700ServiceImpl extends EgovAbstractServiceImpl implements Cmm1700Service {
	/** DAO Bean Injection */
    @Resource(name="cmm1700DAO")
    private Cmm1700DAO cmm1700DAO;

    /**
	 * 역할 조회 공통 목록 조회 
	 */
	@Override
	public List selectCmm1700CommonAuthList(Cmm1700VO cmm1700vo) throws Exception {
		return cmm1700DAO.selectCmm1700CommonAuthList(cmm1700vo);
	}
	/**
	 * 역할 조회 공통 목록 전체 카운트 조회
	 *  	
	 */
	@Override
	public int selectCmm1700CommonAuthListCnt(Cmm1700VO cmm1700vo) throws Exception {
		return cmm1700DAO.selectCmm1700CommonAuthListCnt(cmm1700vo);
	}
	
}
