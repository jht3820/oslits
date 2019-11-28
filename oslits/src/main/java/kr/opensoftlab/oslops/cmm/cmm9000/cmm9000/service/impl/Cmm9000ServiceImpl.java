package kr.opensoftlab.oslops.cmm.cmm9000.cmm9000.service.impl;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import kr.opensoftlab.oslops.cmm.cmm9000.cmm9000.service.Cmm9000Service;

import org.springframework.stereotype.Service;

import egovframework.rte.fdl.cmmn.EgovAbstractServiceImpl;

/**
 * @Class Name : Cmm9000ServiceImpl.java
 * @Description : Cmm9000ServiceImpl Business Implement class
 * @Modification Information
 *
 * @author 정형택
 * @since 2016.01.20.
 * @version 1.0
 * @see
 *  
 *  Copyright (C)  All right reserved.
 */

@Service("cmm9000Service")
public class Cmm9000ServiceImpl extends EgovAbstractServiceImpl implements Cmm9000Service {

	/** Cmm9000DAO DI */
    @Resource(name="cmm9000DAO")
    private Cmm9000DAO cmm9000DAO;
    
	/**
	 * Cmm9000 왼쪽 메뉴 영역에 표시할 각종 정보 건수를 조회(알림, 담당요구사항, 전체요구사항)
	 * @param param - Map
	 * @return Map
	 * @exception Exception
	 */
	@SuppressWarnings({ "rawtypes", "unchecked" })
	public Map selectCmm9000LeftMenuInfos(Map paramMap) throws Exception{
		Map menuInfoMap = new HashMap();
		
		Map alarmMap = new HashMap();
		Map chargeReqList = new HashMap();
		Map allReqList = new HashMap();
		
		//알림 건수 정보 조회
		alarmMap = cmm9000DAO.selectCmm9000AlarmCntInfo(paramMap);
		
		//담당 요구사항 건수 리스트 조회
		chargeReqList = cmm9000DAO.selectCmm9000ChargeReqCntList(paramMap);
		
		//전체 요구사항 건수 리스트 조회
		allReqList = cmm9000DAO.selectCmm9000AllReqCntList(paramMap);
		
		// 현재 선택된 프로젝트 타입 조회
		String selPrjType = cmm9000DAO.selectCmm9000ProjectType(paramMap);
		
		menuInfoMap.put("alarmMap", alarmMap);
		menuInfoMap.put("chargeReqList", chargeReqList);
		menuInfoMap.put("allReqList", allReqList);
		menuInfoMap.put("selPrjType", selPrjType);
		
		return menuInfoMap;
	}
}
