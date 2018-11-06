package kr.opensoftlab.oslits.stm.stm1000.stm1200.service.impl;



import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import kr.opensoftlab.oslits.stm.stm1000.stm1000.service.Stm1000Service;
import kr.opensoftlab.oslits.stm.stm1000.stm1000.vo.Stm1000VO;
import kr.opensoftlab.oslits.stm.stm1000.stm1200.service.Stm1200Service;
import kr.opensoftlab.oslits.stm.stm1000.stm1200.vo.Stm1200VO;

import org.springframework.stereotype.Service;

import egovframework.rte.fdl.cmmn.EgovAbstractServiceImpl;

/**
 * @Class Name : Stm1000ServiceImpl.java
 * @Description : Stm1000ServiceImpl Business Implement class
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
	/** DAO Bean Injection */
    @Resource(name="stm1200DAO")
    private Stm1200DAO stm1200DAO;  
	
	
	@Override
	public List<Stm1200VO> selectStm1200ProjectList(Stm1200VO stm1200vo) {
		// TODO Auto-generated method stub
		return stm1200DAO.selectStm1200ProjectList( stm1200vo);
	}
	
	@Override
	public int selectStm1200ProjectListCnt(Stm1200VO stm1200vo) {
		// TODO Auto-generated method stub
		return stm1200DAO.selectStm1200ProjectListCnt( stm1200vo);
	}
}
