package kr.opensoftlab.oslits.stm.stm1000.stm1100.service.impl;



import java.net.URLEncoder;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import kr.opensoftlab.oslits.stm.stm1000.stm1000.vo.Stm1000VO;
import kr.opensoftlab.oslits.stm.stm1000.stm1100.service.Stm1100Service;
import kr.opensoftlab.oslits.stm.stm1000.stm1100.vo.Stm1100VO;

import org.springframework.stereotype.Service;

import egovframework.com.utl.sim.service.EgovFileScrty;
import egovframework.rte.fdl.cmmn.EgovAbstractServiceImpl;

/**
 * @Class Name : Api1100ServiceImpl.java
 * @Description : Api1100ServiceImpl Business Implement class
 * @Modification Information
 *
 * @author 공대영
 *  * @since 2018.08.14.
 * @version 1.0
 * @see
 *  
 *  Copyright (C)  All right reserved.
 */

@Service("stm1100Service")
public class Stm1100ServiceImpl extends EgovAbstractServiceImpl implements Stm1100Service {
	/** DAO Bean Injection */
    @Resource(name="stm1100DAO")
    private Stm1100DAO stm1100DAO;  
    @Override
    public List<Map> selectStm1100ProjectAuthList(Map<String, String> paramMap) {
		// TODO Auto-generated method stub
		return stm1100DAO.selectStm1100ProjectAuthList(paramMap);
	}
	@Override
	public void saveStm1100(List<Stm1100VO> list) throws Exception {
		// TODO Auto-generated method stub

		for (Stm1100VO stm1100VO : list) {
			if(!stm1100VO.getIsChecked().equals(stm1100VO.getOrgChecked()) ){
				if("Y".equals(stm1100VO.getIsChecked())){
					String licGrpId 		= stm1100VO.getLicGrpId();
					String prjId 		= stm1100VO.getPrjId();

					String apiTok 	= EgovFileScrty.encryptPassword(licGrpId, prjId);	// 토큰키 생성
					
					stm1100VO.setApiTok(apiTok);
					stm1100DAO.insertStm1100(stm1100VO);
				}else if("N".equals(stm1100VO.getIsChecked())){
					stm1100DAO.deleteStm1100(stm1100VO);
				}
			}
		}
		
	}
	@Override
	public List selectStm1100ProjectListAjax(Map<String, String> paramMap) {
		// TODO Auto-generated method stub
		return stm1100DAO.selectStm1100ProjectListAjax(paramMap);
	}

	
}
