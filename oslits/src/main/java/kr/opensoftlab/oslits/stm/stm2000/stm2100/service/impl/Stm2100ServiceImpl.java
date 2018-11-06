package kr.opensoftlab.oslits.stm.stm2000.stm2100.service.impl;

import java.util.List;
import java.util.Map;

import javax.annotation.Resource;




import kr.opensoftlab.oslits.stm.stm2000.stm2100.service.Stm2100Service;
import kr.opensoftlab.oslits.stm.stm2000.stm2100.vo.Stm2100VO;

import org.springframework.stereotype.Service;







import egovframework.rte.fdl.cmmn.EgovAbstractServiceImpl;

/**
 * @Class Name : Svn1100ServiceImpl.java
 * @Description : Svn1100ServiceImpl Business Implement class
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
	/** Scpr5000DAO DI */

	@Resource(name="stm2100DAO")
    private Stm2100DAO stm2100DAO;
	
	@Override
	public List selectStm2100ProjectAuthList(Map paramMap) throws Exception{ 		
		return stm2100DAO.selectStm2100ProjectAuthList(paramMap);
	}

	@Override
	public void saveStm2100(List<Stm2100VO> list) {
		// TODO Auto-generated method stub
		for (Stm2100VO stm2100VO : list) {
			if(!stm2100VO.getIsChecked().equals(stm2100VO.getOrgChecked()) ){
				if("Y".equals(stm2100VO.getIsChecked())){
					stm2100DAO.insertStm2100(stm2100VO);
				}else if("N".equals(stm2100VO.getIsChecked())){
					stm2100DAO.deleteStm2100(stm2100VO);
				}
			}
		}
	}
	@Override
	public List selectStm2100ProjectListAjax(Map paramMap) throws Exception {
		return stm2100DAO.selectStm2100ProjectListAjax(paramMap);
	}

}
