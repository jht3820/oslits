package kr.opensoftlab.oslops.chk.chk1000.chk1100.service.impl;

import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import org.springframework.stereotype.Service;

import egovframework.rte.fdl.cmmn.EgovAbstractServiceImpl;
import kr.opensoftlab.oslops.chk.chk1000.chk1100.service.Chk1100Service;
import kr.opensoftlab.oslops.chk.chk1000.chk1100.vo.Chk1100VO;
import kr.opensoftlab.sdf.excel.ExcelDataListResultHandler;

/**
 * @Class Name : Chk1100ServiceImpl.java
 * @Description : Chk1100ServiceImpl Business Implement class
 * @Modification Information
 *
 * @author 진승환
 * @since 2016.01.30
 * @version 1.0
 * @see
 *  
 *  Copyright (C)  All right reserved.
 */
@Service("chk1100Service")
public class Chk1100ServiceImpl extends EgovAbstractServiceImpl implements Chk1100Service{

	/** Chk1100DAO DI */
    @Resource(name="chk1100DAO")
    private Chk1100DAO chk1100DAO;
    
	/**
	 * 요구사항 결재목록을 조회한다.
	 * @param 	chk1100VO
	 * @return	List  요구사항 결재목록을 조회한다.
	 * @throws 	Exception
	 */
    @SuppressWarnings("rawtypes")
	public List selectChk1100List(Chk1100VO chk1100VO) throws Exception{
    	return chk1100DAO.selectChk1100List(chk1100VO);
    }
    
	/**
	 * 요구사항 결재 목록 총건수를 조회한다.
	 * @param 	chk1100VO
	 * @return  int 요구사항 결재 목록 총건수를 조회한다.
	 * @throws 	Exception
	 */
    public int selectChk1100ListCnt(Chk1100VO chk1100VO) throws Exception{
    	return (int)chk1100DAO.selectChk1100ListCnt(chk1100VO);
    }
    
	/**
	 * 선택된 요구사항의 승인/반려시 필요한 작업흐름을 조회한다.
	 * @param 	inputMap
	 * @return  Map 작업흐름 정보
	 * @throws 	Exception
	 */
    @SuppressWarnings("rawtypes")
	@Override
	public Map selectFlw1100FlowList(Map inputMap) throws Exception {
		return (Map)chk1100DAO.selectFlw1100FlowList(inputMap);
	}
    
    /**
	 * 선택된 요구사항의 세부정보를 가져온다.
	 * @param 	inputMap
	 * @return  Map 요구사항의 세부정보
	 * @throws 	Exception
	 */
	@SuppressWarnings("rawtypes")
    public Map selectChk1100ChkInfoAjax(Map inputMap) throws Exception{
		return (Map)chk1100DAO.selectChk1100ChkInfoAjax(inputMap);
	}

	@Override
	public void selectChk1100ExcelList(Chk1100VO chk1100vo,
			ExcelDataListResultHandler resultHandler) throws Exception {
		chk1100DAO.selectChk1100ExcelList(chk1100vo,	resultHandler);
		
	}

	/**
	 * 요구사항 검수거부 목록 조회
	 * @param 	Map paramMap
	 * @throws 	Exception
	 */
	@SuppressWarnings("rawtypes")
	public List  selectChk1100ReqChkRejectList(Map paramMap) throws Exception {
		 return chk1100DAO.selectChk1100ReqChkRejectList(paramMap);
	}


}
