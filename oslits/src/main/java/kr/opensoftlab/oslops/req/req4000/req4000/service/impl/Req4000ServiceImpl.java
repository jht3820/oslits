package kr.opensoftlab.oslops.req.req4000.req4000.service.impl;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import kr.opensoftlab.oslops.req.req4000.req4000.service.Req4000Service;
import kr.opensoftlab.sdf.excel.ExcelDataListResultHandler;

import org.springframework.stereotype.Service;

import egovframework.com.cmm.EgovMessageSource;
import egovframework.rte.fdl.cmmn.EgovAbstractServiceImpl;

/**
 * @Class Name : Req4000ServiceImpl.java
 * @Description : Req4000ServiceImpl Business Implement class
 * @Modification Information
 *
 * @author 정형택
 * @since 2016.01.12.
 * @version 1.0
 * @see
 *  
 *  Copyright (C) OpenSoftLab Corp. All right reserved.
 */

@Service("req4000Service")
public class Req4000ServiceImpl extends EgovAbstractServiceImpl implements Req4000Service {

	/** Req4000DAO DI */
    @Resource(name="req4000DAO")
    private Req4000DAO req4000DAO;
    
    /** EgovMessageSource */
	@Resource(name = "egovMessageSource")
	EgovMessageSource egovMessageSource;

    /**
	 * Req4000 요구사항 분류 정보 목록 가져오기
	 * @param param - Map
	 * @return 
	 * @exception Exception
	 */
	@SuppressWarnings("rawtypes")
	public List selectReq4000ReqClsList(Map paramMap) throws Exception {
		return req4000DAO.selectReq4000ReqClsList(paramMap) ;
    }
	
	/**
	 * Req4000 요구사항 분류정보 조회
	 * @param param - Map
	 * @return 
	 * @exception Exception
	 */
	@SuppressWarnings("rawtypes")
	public Map selectReq4000ReqClsInfo(Map paramMap) throws Exception{
		return req4000DAO.selectReq4000ReqClsInfo(paramMap) ;
	}
	
	/**
	 * Req4000 요구사항 분류정보 등록(단건) AJAX
	 * 요구사항 분류정보 등록후 등록 정보 결과 및 정보를 리턴한다.
	 * @param 
	 * @return 
	 * @exception Exception
	 */
	@SuppressWarnings({ "rawtypes", "unchecked" })
	public Map insertReq4000ReqClsInfo(Map paramMap) throws Exception{
		Map map = req4000DAO.selectReq4000ReqClsInfo(paramMap);
		
		/* 기본 정보 삽입 */
		map.put("regUsrId", paramMap.get("regUsrId"));
		map.put("modifyUsrId", paramMap.get("modifyUsrId"));
		map.put("regUsrIp", paramMap.get("regUsrIp"));
		map.put("modifyUsrIp", paramMap.get("modifyUsrIp"));
		map.put("selPrjId", paramMap.get("selPrjId"));
		
		//상위 분류 정보를 이용해 하위 메뉴 기본정보 등록
		String insReqClsId = req4000DAO.insertReq4000ReqClsInfo(map);
		
		//생성된 키가 없으면 튕겨냄
		if(insReqClsId == null || "".equals(insReqClsId)){
			throw new Exception(egovMessageSource.getMessage("req4000.notFoundUpperMenu.fail"));
		}
		
		//생성된 menuId를 이용해 새로 등록한 메뉴 정보 조회
		map.put("reqClsId", insReqClsId);
		
		Map newMap = req4000DAO.selectReq4000ReqClsInfo(map);
		
		return newMap;
	}
	
	/**
	 * Req4000 분류에 요구사항이 배정되어있는지 체크한다.
	 * @param 
	 * @return List<String> 요구사항이 배정된 분류 명 목록
	 * @exception Exception
	 */
	@SuppressWarnings({ "rawtypes", "unchecked"})
	public List<String> deleteReq4000ReqClsAssignChk(Map paramMap) throws Exception{
		
		List<Map<String,String>> list = (List<Map<String,String>>) paramMap.get("list");
		// 프로젝트 ID 세팅
		String prjId = (String)paramMap.get("selPrjId");
		
		// 요구사항이 배정된 분류 목록
		List<String> notDelReqClsNmList = new ArrayList<String>();
		
		int listSize = list.size();
		for (int i = 0; i < listSize; i++) {
			
			Map<String,String> delClsMap = list.get(i);
			delClsMap.put("prjId", prjId);
			
			// 분류 삭제여부 체크
			Map rsltMap = req4000DAO.selectReq4000DelPosibleYn(delClsMap);
			// 삭제여부 값을 가져온다.
			String delYn = (String) rsltMap.get("delYn");
			// 분류에 요구사항이 배정되어 있을 경우
			if("N".equals(delYn)){
				// 요구사항 배정된 분류 List에 추가한다.
				notDelReqClsNmList.add(delClsMap.get("reqClsNm"));
			}
		}
		return notDelReqClsNmList;
	}
	
	/**
	 * Req4000 분류 정보 삭제
	 * 요구사항 분류를 삭제한다.
	 * @param 
	 * @return 
	 * @exception Exception
	 */
	@SuppressWarnings({ "rawtypes", "unchecked"})
	public void deleteReq4000ReqClsInfo(Map paramMap) throws Exception{
		
		List<Map<String,String>> list = (List<Map<String,String>>) paramMap.get("list");
	
		// 프로젝트 ID 세팅
		String prjId = (String)paramMap.get("selPrjId");
		
		int listSize = list.size();

		// 요구사항 분류를 삭제한다.
		for (int i = 0; i < listSize; i++) {
			
			Map<String,String> delClsMap = list.get(i);
			delClsMap.put("prjId", prjId);

			// 요구사항 분류 정보 삭제
			int delCnt = req4000DAO.deleteReq4000ReqClsInfo(delClsMap);
				
			// 삭제된 건이 없으면 튕겨냄
			if(delCnt == 0 ){
				throw new Exception(egovMessageSource.getMessage("fail.common.delete"));
			}
		}
	}
	
	/**
	 * Req4000 분류정보 수정(단건) AJAX
	 * 메뉴정보 수정
	 * @param 
	 * @return 
	 * @exception Exception
	 */
	@SuppressWarnings({ "rawtypes" })
	public void updateReq4000ReqClsInfo(Map paramMap) throws Exception{
		
		//메뉴정보 수정
		int upCnt = req4000DAO.updateReq4000ReqClsInfo(paramMap);
		
		//수정된 건이 없으면 튕겨냄
		if(upCnt == 0 ){
			throw new Exception(egovMessageSource.getMessage("fail.common.update"));
		}
	}

	@SuppressWarnings("rawtypes")
	@Override
	public void selectReq4000ExcelList(Map paramMap,
			ExcelDataListResultHandler resultHandler) throws Exception {
		req4000DAO.selectReq4000ExcelList(paramMap,resultHandler);
		
	}
}
