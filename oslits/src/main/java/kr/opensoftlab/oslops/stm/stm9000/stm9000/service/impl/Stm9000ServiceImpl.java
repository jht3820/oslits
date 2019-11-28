package kr.opensoftlab.oslops.stm.stm9000.stm9000.service.impl;



import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import kr.opensoftlab.oslops.stm.stm1000.stm1000.service.Stm1000Service;
import kr.opensoftlab.oslops.stm.stm1000.stm1000.vo.Stm1000VO;
import kr.opensoftlab.oslops.stm.stm9000.stm9000.service.Stm9000Service;
import kr.opensoftlab.oslops.stm.stm9000.stm9000.vo.Stm9000VO;

import org.springframework.stereotype.Service;

import egovframework.rte.fdl.cmmn.EgovAbstractServiceImpl;

/**
 * @Class Name : Stm9000ServiceImpl.java
 * @Description : Stm9000ServiceImpl Business Implement class
 * @Modification Information
 *
 * @author 공대영
 *  * @since 2018.10.17.
 * @version 1.0
 * @see
 *  
 *  Copyright (C)  All right reserved.
 */

@Service("stm9000Service")
public class Stm9000ServiceImpl extends EgovAbstractServiceImpl implements Stm9000Service {
	/** DAO Bean Injection */
    @Resource(name="stm9000DAO")
    private Stm9000DAO stm9000DAO;  
    
    @Override
    public List<Stm9000VO> selectStm9000List(Stm9000VO stm9000VO) {
		// TODO Auto-generated method stub
		return stm9000DAO.selectStm9000List(stm9000VO);
	}
    
    @Override
	public int selectStm9000ListCnt(Stm9000VO stm9000VO) {
		// TODO Auto-generated method stub
		return stm9000DAO.selectStm9000ListCnt(stm9000VO);
	}

    @Override
    public Map selectStm9000DetailInfo(Map paramMap) {
    	// TODO Auto-generated method stub
    	return stm9000DAO.selectStm9000DetailInfo(paramMap);
    }
    
    @Override
    public List<Map> selectStm9000ReqHistoryList(Map paramMap) {
    	// TODO Auto-generated method stub
    	return stm9000DAO.selectStm9000ReqHistoryList(paramMap);
    }

    @Override
    public List<Map> selectStm9000ReqSignList(Map paramMap) {
    	// TODO Auto-generated method stub
    	return stm9000DAO.selectStm9000ReqSignList(paramMap);
    }

    @Override
    public List<Map> selectStm9000ChgDetailList(Map paramMap) {
    	// TODO Auto-generated method stub
    	return stm9000DAO.selectStm9000ChgDetailList(paramMap);
    }
	
}
