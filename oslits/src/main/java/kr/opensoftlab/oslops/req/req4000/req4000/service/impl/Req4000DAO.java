package kr.opensoftlab.oslops.req.req4000.req4000.service.impl;

import java.util.List;
import java.util.Map;

import kr.opensoftlab.oslops.com.dao.ComOslitsAbstractDAO;
import kr.opensoftlab.sdf.excel.ExcelDataListResultHandler;

import org.springframework.stereotype.Repository;

/**
 * @Class Name : Req4000DAO.java
 * @Description : Req4000DAO DAO Class
 * @Modification Information
 *
 * @author 정형택
 * @since 2016.01.30.
 * @version 1.0
 * @see
 *  
 *  Copyright (C) OpenSoftLab Corp. All right reserved.
 */

@Repository("req4000DAO")
public class Req4000DAO extends ComOslitsAbstractDAO {
	
	/**
	 * Req4000 요구사항 분류정보 목록 가져오기
	 * @param param - Map
	 * @return 
	 * @exception Exception
	 */
	@SuppressWarnings("rawtypes")
	public List selectReq4000ReqClsList(Map paramMap) throws Exception {
		 return (List) list("req4000DAO.selectReq4000ReqClsList", paramMap);
    }
	
	/**
	 * Req4000 요구사항 분류 정보 조회
	 * @param param - Map
	 * @return 
	 * @exception Exception
	 */
	@SuppressWarnings("rawtypes")
	public Map selectReq4000ReqClsInfo(Map paramMap) throws Exception{
		return (Map) select("req4000DAO.selectReq4000ReqClsInfo", paramMap);
	}
	
	/**
	 * Req4000 요구사항 분류 정보 등록
	 * @param param - Map
	 * @return 
	 * @exception Exception
	 */
	@SuppressWarnings("rawtypes")
	public String insertReq4000ReqClsInfo(Map paramMap) throws Exception{
		return (String) insert("req4000DAO.insertReq4000ReqClsInfo", paramMap);
	}
	
	/**
	 * Req4000 요구사항 분류 정보 삭제시 삭제 가능한지(분류에 요구사항 배정된 것이 있는지 체크)
	 * 			- 삭제해도 될 경우(요구사항 배정된건이 없음) Y
	 * 			- 삭제하면 안될 경우(요구사항 배정된 건이 있음) N
	 * @param param - Map
	 * @return 
	 * @exception Exception
	 */
	@SuppressWarnings("rawtypes")
	public Map selectReq4000DelPosibleYn(Map paramMap) throws Exception{
		return (Map) select("req4000DAO.selectReq4000DelPosibleYn", paramMap);
	}
	
	/**
	 * Req4000 요구사항 분류 정보 삭제
	 * @param param - Map
	 * @return 
	 * @exception Exception
	 */
	@SuppressWarnings("rawtypes")
	public int deleteReq4000ReqClsInfo(Map paramMap) throws Exception{
		return (int) delete("req4000DAO.deleteReq4000ReqClsInfo", paramMap);
	}
	
	/**
	 * Req4000 요구사항 분류 정보 수정
	 * @param param - Map
	 * @return 
	 * @exception Exception
	 */
	@SuppressWarnings("rawtypes")
	public int updateReq4000ReqClsInfo(Map paramMap) throws Exception{
		return (int) update("req4000DAO.updateReq4000ReqClsInfo", paramMap);
	}

	@SuppressWarnings("rawtypes")
	public void selectReq4000ExcelList(Map paramMap,
			ExcelDataListResultHandler resultHandler) throws Exception {
		// TODO Auto-generated method stub
		listExcelDownSql("req4000DAO.selectReq4000ReqClsList", paramMap, resultHandler);
		
	}

	/**
	 * Req4000 [프로젝트 마법사] 요구사항 분류 정보 등록
	 * @param param - Map
	 * @return 
	 * @exception Exception
	 */
	@SuppressWarnings("rawtypes")
	public String insertReq4000WizardReqClsInfo(Map paramMap) throws Exception{
		return (String) insert("req4000DAO.insertReq4000WizardReqClsInfo", paramMap);
	}
}