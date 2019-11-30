package kr.opensoftlab.oslops.adm.adm8000.adm8100.service.impl;

import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import org.springframework.stereotype.Service;

import egovframework.com.cmm.EgovMessageSource;
import egovframework.rte.fdl.cmmn.EgovAbstractServiceImpl;
import kr.opensoftlab.oslops.adm.adm8000.adm8100.service.Adm8100Service;
import kr.opensoftlab.oslops.adm.adm8000.adm8100.vo.Adm8100VO;

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

	/** Adm8100DAO DI */
    @Resource(name="adm8100DAO")
    private Adm8100DAO adm8100DAO;
    
    /** EgovMessageSource */
	@Resource(name = "egovMessageSource")
	EgovMessageSource egovMessageSource;

	/**
	 * Adm8100 보고서를 생성한다.
	 * @param paramMap
	 * @return
	 * @throws Exception
	 */
	@SuppressWarnings("rawtypes")
	@Override
	public int insertAdm8100ReportInfo(Map paramMap) throws Exception {
		adm8100DAO.insertAdm8100ReportMasterInfo(paramMap);
		adm8100DAO.insertAdm8100ReportInfo(paramMap);
		return 0;
	}

	/**
	 * Adm8100 보고서 목록을 조회한다.
	 * @param adm8100vo
	 * @return
	 * @throws Exception
	 */
	@Override
	public List<Adm8100VO> selectAdm8100ReportList(Adm8100VO adm8100vo) throws Exception{
		return adm8100DAO.selectAdm8100ReportList(adm8100vo);
	}

	/**
	 * Adm8100 보고서 목록 총 건수 조회
	 * @param adm8100vo
	 * @return
	 * @throws Exception
	 */
	@Override
	public int selectAdm8100ReportListCnt(Adm8100VO adm8100vo) throws Exception{
		return adm8100DAO.selectAdm8100ReportListCnt(adm8100vo);
	}
	
	/**
	 * Adm8100 보고서 단건 조회
	 * @param paramMap
	 * @return
	 * @throws Exception
	 */
	@SuppressWarnings("rawtypes")
	@Override
	public List<Map> selectAdm8100ReportInfo(Map paramMap) throws Exception{
		return adm8100DAO.selectAdm8100ReportInfo(paramMap);
	}

	/**
	 * Adm8100 보고서를 수정한다.
	 * @param paramMap
	 * @param adm8100voList
	 * @return
	 * @throws Exception
	 */
	@SuppressWarnings("rawtypes")
	@Override
	public int updateAdm8100ReportInfo(Map paramMap ,List<Adm8100VO> adm8100voList) throws Exception{
		for (Adm8100VO adm8100VO : adm8100voList) {
			adm8100DAO.updateAdm8100ReportInfo(adm8100VO);			
		}
		if(adm8100voList.size()>0){
			Adm8100VO adm8100VO = adm8100voList.get(0);
			adm8100DAO.updateAdm8100ReportWriteYn(paramMap);
		}
		return 0;
	}
	
	/**
	 * Adm8100 보고서를 확정 처리한다.
	 * @param adm8100vo
	 * @return
	 * @throws Exception
	 */
	@Override
	public void updateAdm8100ReporConfirm(Adm8100VO adm8100vo) throws Exception{
		adm8100DAO.updateAdm8100ReporConfirm(adm8100vo);
	}
	
	/**
	 * Adm8100 보고서를 마스터 정보 조회
	 * @param paramMap
	 * @return
	 * @throws Exception
	 */
	@SuppressWarnings("rawtypes")
	@Override
	public Map selectAdm8100ReportMasterInfo(Map paramMap) throws Exception{
		return adm8100DAO.selectAdm8100ReportMasterInfo(paramMap);
	}
	
}
