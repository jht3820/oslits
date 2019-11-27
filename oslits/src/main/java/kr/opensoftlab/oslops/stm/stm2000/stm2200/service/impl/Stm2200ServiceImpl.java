package kr.opensoftlab.oslits.stm.stm2000.stm2200.service.impl;

import java.util.List;
import java.util.Map;

import javax.annotation.Resource;



import kr.opensoftlab.oslits.stm.stm2000.stm2200.service.Stm2200Service;
import kr.opensoftlab.oslits.stm.stm2000.stm2200.vo.Stm2200VO;


import org.springframework.stereotype.Service;

import egovframework.com.cmm.EgovMessageSource;
import egovframework.com.utl.fcc.service.EgovStringUtil;
import egovframework.rte.fdl.cmmn.EgovAbstractServiceImpl;

/**
 * @Class Name : Svn1000ServiceImpl.java
 * @Description : Svn1000ServiceImpl Business Implement class
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
	/** Scpr5000DAO DI */

	@Resource(name="stm2200DAO")
    private Stm2200DAO stm2200DAO;
	

	@Override
	public List<Stm2200VO> selectStm2200RepProjectList(Stm2200VO stm2200vo) {
		return stm2200DAO.selectStm2200RepProjectList(stm2200vo);
	}
	
	@Override
	public int selectStm2200RepProjectListCnt(Stm2200VO stm2200vo) {
		return stm2200DAO.selectStm2200RepProjectListCnt(stm2200vo);
	}
	
	
}
