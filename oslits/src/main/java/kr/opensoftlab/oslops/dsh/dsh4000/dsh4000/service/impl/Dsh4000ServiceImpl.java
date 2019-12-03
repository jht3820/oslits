package kr.opensoftlab.oslops.dsh.dsh4000.dsh4000.service.impl;

import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import kr.opensoftlab.oslops.adm.adm7000.adm7000.service.Adm7000Service;
import kr.opensoftlab.oslops.adm.adm8000.adm8000.service.Adm8000Service;
import kr.opensoftlab.oslops.adm.adm8000.adm8100.service.Adm8100Service;
import kr.opensoftlab.oslops.adm.adm8000.adm8100.vo.Adm8100VO;
import kr.opensoftlab.oslops.dsh.dsh4000.dsh4000.service.Dsh4000Service;
import kr.opensoftlab.oslops.dsh.dsh4000.dsh4000.vo.Dsh4000VO;

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
@Service("dsh4000Service")
public class Dsh4000ServiceImpl extends EgovAbstractServiceImpl implements Dsh4000Service {

	/** Adm8000DAO DI */
    @Resource(name="dsh4000DAO")
    private Dsh4000DAO dsh4000DAO;
    
    /** EgovMessageSource */
	@Resource(name = "egovMessageSource")
	EgovMessageSource egovMessageSource;


	@Override
	public List<Dsh4000VO> selectDsh4000ReportList(Dsh4000VO dsh4000vo) {
		// TODO Auto-generated method stub
		return dsh4000DAO.selectDsh4000ReportList(dsh4000vo);
	}

	@Override
	public int selectDsh4000ReportListCnt(Dsh4000VO dsh4000vo) {
		// TODO Auto-generated method stub
		return dsh4000DAO.selectDsh4000ReportListCnt(dsh4000vo);
	}
	
	
	
}
