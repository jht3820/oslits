package kr.opensoftlab.oslits.stm.stm3000.stm3100.service.impl;

import java.util.List;
import java.util.Map;

import javax.annotation.Resource;
import kr.opensoftlab.oslits.stm.stm3000.stm3100.service.Stm3100Service;
import kr.opensoftlab.oslits.stm.stm3000.stm3100.vo.Stm3100VO;

import org.springframework.stereotype.Service;

import egovframework.rte.fdl.cmmn.EgovAbstractServiceImpl;

/**
 * @Class Name : Stm3100ServiceImpl.java
 * @Description : Stm3100ServiceImpl Business Implement class
 * @Modification Information
 *
 * @author 공대영
 * @since 2018.07.02
 * @version 1.0
 * @see
 *  
 *  Copyright (C)  All right reserved.
 */
@Service("stm3100Service")
public class Stm3100ServiceImpl extends EgovAbstractServiceImpl implements Stm3100Service{


	@Resource(name="stm3100DAO")
    private Stm3100DAO stm3100DAO;
	
	@Override
	public List selectStm3100JenkinsProjectAuthList(Map paramMap) throws Exception {
		// TODO Auto-generated method stub
		return stm3100DAO.selectStm3100JenkinsProjectAuthList(paramMap);
	}
    
	@Override
	public void saveStm3100(List<Stm3100VO> list) {
		// TODO Auto-generated method stub
		for (Stm3100VO stm3100VO : list) {
			if(!stm3100VO.getIsChecked().equals(stm3100VO.getOrgChecked()) ){
				if("Y".equals(stm3100VO.getIsChecked())){
					stm3100DAO.insertStm3100(stm3100VO);
				}else if("N".equals(stm3100VO.getIsChecked())){
					stm3100DAO.deleteStm3100(stm3100VO);
				}
			}
		}
	}
	@Override
	public List<Map> selectStm3100JenkinsProjectList(Map paramMap) {
		return stm3100DAO.selectStm3100JenkinsProjectList(paramMap);
	}
   
}
