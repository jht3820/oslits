package kr.opensoftlab.oslops.cmm.cmm1000.cmm1000.service.impl;



import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import kr.opensoftlab.oslops.adm.adm2000.adm2000.vo.Adm2000VO;
import kr.opensoftlab.oslops.cmm.cmm1000.cmm1000.service.Cmm1000Service;
import kr.opensoftlab.oslops.cmm.cmm1000.cmm1000.vo.Cmm1000VO;

import org.springframework.stereotype.Service;

import egovframework.rte.fdl.cmmn.EgovAbstractServiceImpl;

/**
 * @Class Name : Cmm1000ServiceImpl.java
 * @Description : Cmm1000ServiceImpl Business Implement class
 * @Modification Information
 *
 * @author 공대영
 *  * @since 2018.07.23.
 * @version 1.0
 * @see
 *  
 *  Copyright (C)  All right reserved.
 */

@Service("cmm1000Service")
public class Cmm1000ServiceImpl extends EgovAbstractServiceImpl implements Cmm1000Service {
	/** DAO Bean Injection */
    @Resource(name="cmm1000DAO")
    private Cmm1000DAO cmm1000DAO;

    /**
	 * 사용자 조회 공통 목록 조회 
	 */
	@Override
	public List selectCmm1000CommonUserList(Cmm1000VO cmm1000vo) throws Exception {
		return cmm1000DAO.selectCmm1000CommonUserList(cmm1000vo);
	}
	/**
	 * 사용자 조회 공통 목록 전체 카운트 조회
	 *  	
	 */
	public int selectCmm1000CommonUserListCnt(Cmm1000VO cmm1000vo) throws Exception {
		return cmm1000DAO.selectCmm1000CommonUserListCnt(cmm1000vo);
	}
	
}
