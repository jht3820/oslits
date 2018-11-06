package kr.opensoftlab.oslits.stm.stm3000.stm3200.service.impl;

import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import kr.opensoftlab.oslits.stm.stm3000.stm3200.service.Stm3200Service;
import kr.opensoftlab.oslits.stm.stm3000.stm3200.vo.Stm3200VO;

import org.springframework.stereotype.Service;

import egovframework.rte.fdl.cmmn.EgovAbstractServiceImpl;

/**
 * @Class Name : dpl2100ServiceImpl.java
 * @Description : dpl2100ServiceImpl Business Implement class
 * @Modification Information
 *
 * @author 공대영
 * @since 2018.07.02
 * @version 1.0
 * @see
 *  
 *  Copyright (C)  All right reserved.
 */
@Service("stm3200Service")
public class Stm3200ServiceImpl extends EgovAbstractServiceImpl implements Stm3200Service{
	/** Scpr5000DAO DI */

	@Resource(name="stm3200DAO")
    private Stm3200DAO stm3200DAO;
	
 
	
	@Override
	public List<Stm3200VO> selectStm3200JobProjectList(Stm3200VO stm3200VO) {
		return stm3200DAO.selectStm3200JobProjectList(stm3200VO);
	}
	
	@Override
	public int selectStm3200JobProjectListCnt(Stm3200VO stm3200VO) {
		return stm3200DAO.selectStm3200JobProjectListCnt(stm3200VO);
	}
	
}
