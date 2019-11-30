package kr.opensoftlab.oslops.req.req2000.req2000.service.impl;

/**
 * @Class Name : Req2000Service.java
 * @Description : Req2000Service Service class
 * @Modification Information
 *
 * @author 정형택
 * @since 2016.01.23.
 * @version 1.0
 * @see
 *  
 *  Copyright (C) OpenSoftLab Corp All right reserved.
 */

import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import kr.opensoftlab.oslops.req.req2000.req2000.service.Req2000Service;
import kr.opensoftlab.oslops.req.req4000.req4100.vo.Req4100VO;
import kr.opensoftlab.sdf.excel.ExcelDataListResultHandler;

import org.springframework.stereotype.Service;

import egovframework.com.utl.fcc.service.EgovStringUtil;
import egovframework.rte.fdl.cmmn.EgovAbstractServiceImpl;

@Service("req2000Service")
public class Req2000ServiceImpl extends EgovAbstractServiceImpl implements Req2000Service {

	/** DAO Bean Injection */
	@Resource(name="req2000DAO")
	private Req2000DAO req2000DAO;

	/**
	 * 요구사항 코멘트 목록을 조회한다.
	 * @param Map
	 * @return List 요구사항 코멘트 목록
	 * @throws Exception
	 */
	@SuppressWarnings({ "rawtypes", "unchecked" })
	public List selectReq2000ReqCommentListAjax(Map paramMap) throws Exception{
		return req2000DAO.selectReq2000ReqCommentListAjax(paramMap);
	}
	
	/**
	 * 요구사항 코멘트 등록.
	 * @param Map
	 * @return  void 
	 * @throws Exception
	 */
	@SuppressWarnings("rawtypes")
	public void insertReq2000ReqCommentInfo(Map paramMap) throws Exception{
		String reqCmntSeq = req2000DAO.insertReq2000ReqCommentInfo(paramMap);

		if( "".equals(EgovStringUtil.isNullToString(reqCmntSeq)) ){
			throw new Exception("요구사항 코멘트 등록 실패");
		}
	}

	/**
	 * req2000 요구사항 목록 엑셀 다운로드
	 * @param param - Map
	 * @param resultHandler ExcelDataListResultHandler
	 * @return 
	 * @exception Exception
	 */
	@Override
	public void selectReq2000ExcelList(Map paramMap, ExcelDataListResultHandler resultHandler) throws Exception {
		req2000DAO.selectReq2000ExcelList(paramMap, resultHandler);
	}

}
