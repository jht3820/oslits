package kr.opensoftlab.oslops.stm.stm2000.stm2100.service.impl;

import java.util.List;
import java.util.Map;

import javax.annotation.Resource;







import kr.opensoftlab.oslops.stm.stm2000.stm2100.service.Stm2100Service;
import kr.opensoftlab.oslops.stm.stm2000.stm2100.vo.Stm2100VO;

import org.springframework.stereotype.Service;










import egovframework.rte.fdl.cmmn.EgovAbstractServiceImpl;

/**
 * @Class Name : Stm2100ServiceImpl.java
 * @Description : Stm2100ServiceImpl Business Implement class
 * @Modification Information
 *
 * @author 공대영
 * @since 2018.08.16
 * @version 1.0
 * @see
 *  
 *  Copyright (C)  All right reserved.
 */
@Service("stm2100Service")
public class Stm2100ServiceImpl extends EgovAbstractServiceImpl implements Stm2100Service{
	
	/** Stm2100DAO DI */
	@Resource(name="stm2100DAO")
    private Stm2100DAO stm2100DAO;
	
	/**
	 * Stm2100 프로젝트에 배정 가능한 SVN Repository 목록을 조회한다.
	 * @param paramMap
	 * @return List - 배정 가능 SVN Repository 목록
	 * @exception Exception
	 */
	@SuppressWarnings("rawtypes")
	@Override
	public List selectStm2100ProjectAuthList(Map paramMap) throws Exception{ 		
		return stm2100DAO.selectStm2100ProjectAuthList(paramMap);
	}

	/**
	 * Stm2100 프로젝트에  SVN Repository를 배정/배정제외 한다.
	 * @param list - SVN Repository 목록
	 * @return 
	 * @exception Exception
	 */
	@Override
	public void saveStm2100(List<Stm2100VO> list) throws Exception{
		for (Stm2100VO stm2100VO : list) {
			if(!stm2100VO.getIsChecked().equals(stm2100VO.getOrgChecked()) ){
				if("Y".equals(stm2100VO.getIsChecked())){
					// 프로젝트이 SVN Repository를 배정한다.
					stm2100DAO.insertStm2100(stm2100VO);
				}else if("N".equals(stm2100VO.getIsChecked())){
					// 프로젝트에 SVN Repository를 배정제외 한다.
					stm2100DAO.deleteStm2100(stm2100VO);
				}
			}
		}
	}
	
	/**
	 * Stm2100 프로젝트에 배정된 SVN Repository 목록을 조회한다.
	 * @param paramMap
	 * @return 
	 * @exception Exception
	 */
	@SuppressWarnings("rawtypes")
	@Override
	public List selectStm2100ProjectListAjax(Map paramMap) throws Exception {
		return stm2100DAO.selectStm2100ProjectListAjax(paramMap);
	}

}
