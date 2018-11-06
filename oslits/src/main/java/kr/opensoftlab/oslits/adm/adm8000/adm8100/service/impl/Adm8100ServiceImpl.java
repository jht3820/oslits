package kr.opensoftlab.oslits.adm.adm8000.adm8100.service.impl;

import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import kr.opensoftlab.oslits.adm.adm7000.adm7000.service.Adm7000Service;
import kr.opensoftlab.oslits.adm.adm8000.adm8000.service.Adm8000Service;
import kr.opensoftlab.oslits.adm.adm8000.adm8100.service.Adm8100Service;
import kr.opensoftlab.oslits.adm.adm8000.adm8100.vo.Adm8100VO;

import org.springframework.stereotype.Service;

import egovframework.com.cmm.EgovMessageSource;
import egovframework.rte.fdl.cmmn.EgovAbstractServiceImpl;

/**
 * @Class Name : Adm8000ServiceImpl.java
 * @Description : 보고서 서비스 클래스
 * @Modification Information
 * <pre>
 *   수정일      수정자          수정내용
 *  -------    --------    ---------------------------
 *  2018.10.18  공대영          최초 생성
 *  
 * </pre>
 *  @author 공대영
 *  @version 1.0
 *  @see 
 *  
 *  Copyright (C) Open Soft Lab Corp. All Rights Reserved
 */
@Service("adm8100Service")
public class Adm8100ServiceImpl extends EgovAbstractServiceImpl implements Adm8100Service {

	/** Adm8000DAO DI */
    @Resource(name="adm8100DAO")
    private Adm8100DAO adm8100DAO;
    
    /** EgovMessageSource */
	@Resource(name = "egovMessageSource")
	EgovMessageSource egovMessageSource;

	@Override
	public int insertAdm8100ReportInfo(Map paramMap) throws Exception {
		// TODO Auto-generated method stub
		adm8100DAO.insertAdm8100ReportMasterInfo(paramMap);
		adm8100DAO.insertAdm8100ReportInfo(paramMap);
		return 0;
	}

	@Override
	public List<Adm8100VO> selectAdm8100ReportList(Adm8100VO adm8100vo) {
		// TODO Auto-generated method stub
		return adm8100DAO.selectAdm8100ReportList(adm8100vo);
	}

	@Override
	public int selectAdm8100ReportListCnt(Adm8100VO adm8100vo) {
		// TODO Auto-generated method stub
		return adm8100DAO.selectAdm8100ReportListCnt(adm8100vo);
	}
	
	@Override
	public List<Map> selectAdm8100ReportInfo(Map paramMap) {
		// TODO Auto-generated method stub
		return adm8100DAO.selectAdm8100ReportInfo(paramMap);
	}

	@Override
	public int updateAdm8100ReportInfo(Map paramMap ,List<Adm8100VO> adm8100voList) {
		// TODO Auto-generated method stub
		for (Adm8100VO adm8100VO : adm8100voList) {
			adm8100DAO.updateAdm8100ReportInfo(adm8100VO);			
		}
		if(adm8100voList.size()>0){
			Adm8100VO adm8100VO = adm8100voList.get(0);
			adm8100DAO.updateAdm8100ReportWriteYn(paramMap);
		}
		return 0;
	}
	
	@Override
	public void updateAdm8100ReporConfirm(Adm8100VO adm8100vo){
		adm8100DAO.updateAdm8100ReporConfirm(adm8100vo);
	}
	
	@Override
	public Map selectAdm8100ReportMasterInfo(Map paramMap){
		return adm8100DAO.selectAdm8100ReportMasterInfo(paramMap);
	}
	
}
