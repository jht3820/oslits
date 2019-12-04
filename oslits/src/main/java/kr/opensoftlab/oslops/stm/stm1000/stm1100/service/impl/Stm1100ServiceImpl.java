package kr.opensoftlab.oslops.stm.stm1000.stm1100.service.impl;



import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import kr.opensoftlab.oslops.stm.stm1000.stm1100.service.Stm1100Service;
import kr.opensoftlab.oslops.stm.stm1000.stm1100.vo.Stm1100VO;

import org.springframework.stereotype.Service;

import egovframework.com.utl.sim.service.EgovFileScrty;
import egovframework.rte.fdl.cmmn.EgovAbstractServiceImpl;

/**
 * @Class Name : Stm1100ServiceImpl.java
 * @Description : Stm1100ServiceImpl Business Implement class
 * @Modification Information
 *
 * @author 공대영
 * @since 2018.08.14.
 * @version 1.0
 * @see
 *  
 *  Copyright (C)  All right reserved.
 */

@Service("stm1100Service")
public class Stm1100ServiceImpl extends EgovAbstractServiceImpl implements Stm1100Service {
	
	/** Stm1100DAO DI */
    @Resource(name="stm1100DAO")
    private Stm1100DAO stm1100DAO; 
    
    /**
   	 * Stm1100 프로젝트에 사용 가능한 API 목록을 조회한다.
   	 * @param paramMap
   	 * @return List - 프로젝트에 등록 가능한 API 관리 목록
   	 * @exception Exception
   	 */	
   	@SuppressWarnings("rawtypes")
    @Override
    public List<Map> selectStm1100ProjectAuthList(Map<String, String> paramMap) throws Exception{
		return stm1100DAO.selectStm1100ProjectAuthList(paramMap);
	}
   	
   	/**
	 * Stm1100 프로젝트에 API를 등록/삭제한다.
	 * @param list - Stm1100VO list
	 * @exception Exception
	 */	
	@Override
	public void saveStm1100(List<Stm1100VO> list) throws Exception {

		for (Stm1100VO stm1100VO : list) {
			if(!stm1100VO.getIsChecked().equals(stm1100VO.getOrgChecked()) ){
				if("Y".equals(stm1100VO.getIsChecked())){
					// 라이센스 그룹 ID
					String licGrpId = stm1100VO.getLicGrpId();
					// 프로젝트 ID
					String prjId 	= stm1100VO.getPrjId();
					// api 토큰키
					String apiTok 	= EgovFileScrty.encryptPassword(licGrpId, prjId);	// 토큰키 생성
					
					stm1100VO.setApiTok(apiTok);
					// 프로젝트에 API 등록
					stm1100DAO.insertStm1100(stm1100VO);
				}else if("N".equals(stm1100VO.getIsChecked())){
					// 프로젝트에서 API 삭제
					stm1100DAO.deleteStm1100(stm1100VO);
				}
			}
		}
		
	}
	
	/**
	 * Stm1100 프로젝트에 등록되어있는 API 목록을 조회한다.
	 * @param paramMap
	 * @exception Exception
	 */
	@SuppressWarnings("rawtypes")
	@Override
	public List selectStm1100ProjectListAjax(Map<String, String> paramMap) throws Exception{
		return stm1100DAO.selectStm1100ProjectListAjax(paramMap);
	}

}
