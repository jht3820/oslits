package kr.opensoftlab.oslops.chk.chk1000.chk1000.service.impl;

import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import org.springframework.stereotype.Service;

import egovframework.rte.fdl.cmmn.EgovAbstractServiceImpl;
import kr.opensoftlab.oslops.chk.chk1000.chk1000.service.Chk1000Service;
import kr.opensoftlab.oslops.chk.chk1000.chk1000.vo.Chk1000VO;
import kr.opensoftlab.sdf.excel.ExcelDataListResultHandler;

/**
 * @Class Name : Chk1000ServiceImpl.java
 * @Description : Chk1000ServiceImpl Business Implement class
 * @Modification Information
 *
 * @author 진승환
 * @since 2016.01.30
 * @version 1.0
 * @see
 *  
 *  Copyright (C)  All right reserved.
 */
@Service("chk1000Service")
public class Chk1000ServiceImpl extends EgovAbstractServiceImpl implements Chk1000Service{
    
	/** Chk1000DAO DI */
    @Resource(name="chk1000DAO")
    private Chk1000DAO chk1000DAO;
    
    /**
	 * 요구사항 중 개발완료 목록 조회
	 * @param chk1000VO
	 * @return 요구사항 개발완료 목록
	 * @exception Exception
	 */
    @SuppressWarnings("rawtypes")
	public List selectChk1000List(Chk1000VO chk1000VO) throws Exception{
    	return chk1000DAO.selectChk1000List(chk1000VO);
    }
    
    /**
	 * 요구사항 중 개발완료 목록 총건수를 조회한다.
	 * @param chk1000VO
	 * @return  int 요구사항 개발완료 목록 총건수 
	 * @throws Exception
	 */
	public int selectChk1000ListCnt(Chk1000VO chk1000VO) throws Exception{
		return (int)chk1000DAO.selectChk1000ListCnt(chk1000VO);
	}
	
	/**
	 * 선택된 요구사항의 세부정보를 가져온다.
	 * @param inputMap
	 * @return  Map 요구사항의 세부정보
	 * @throws Exception
	 */
	@SuppressWarnings("rawtypes")
	public Map selectChk1000ChkInfoAjax(Map inputMap) throws Exception{
		return (Map)chk1000DAO.selectChk1000ChkInfoAjax(inputMap);
	}

	@Override
	public void selectChk1000ExcelList(Chk1000VO chk1000vo,
			ExcelDataListResultHandler resultHandler) throws Exception {
		chk1000DAO.selectChk1000ExcelList(chk1000vo,resultHandler);
		
	}
}
