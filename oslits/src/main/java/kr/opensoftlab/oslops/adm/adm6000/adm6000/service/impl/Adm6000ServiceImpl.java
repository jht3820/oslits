package kr.opensoftlab.oslops.adm.adm6000.adm6000.service.impl;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Iterator;

import javax.annotation.Resource;

import kr.opensoftlab.oslops.adm.adm6000.adm6000.service.Adm6000Service;

import org.springframework.stereotype.Service;

import egovframework.com.cmm.EgovMessageSource;
import egovframework.com.utl.fcc.service.EgovDateUtil;
import egovframework.com.utl.fcc.service.EgovStringUtil;
import egovframework.rte.fdl.cmmn.EgovAbstractServiceImpl;

/**
 * @Class Name : Adm6000ServiceImpl.java
 * @Description : Adm6000ServiceImpl Business Implement class
 * @Modification Information
 *
 * @author 김정환
 * @since 2016.03.11
 * @version 1.0
 * @see
 *  
 *  Copyright (C)  All right reserved.
 */
@Service("adm6000Service")
public class Adm6000ServiceImpl extends EgovAbstractServiceImpl implements Adm6000Service{
	/** Adm6000DAO DI */
    @Resource(name="adm6000DAO")
    private Adm6000DAO adm6000DAO;
    
    /** EgovMessageSource */
	@Resource(name = "egovMessageSource")
	EgovMessageSource egovMessageSource;
	
	/**
	 * 휴일 관리 내역을 조회한다.
	 * @param paramMap
	 * @return
	 * @throws Exception
	 */
	@SuppressWarnings("rawtypes")
	public List selectAdm6000List(Map paramMap) throws Exception {
		return adm6000DAO.selectAdm6000List(paramMap) ;
	}

	/**
	 * 휴일관리 단건을 등록한다.
	 * @param paramMap
	 * @return
	 * @throws Exception
	 */
	@SuppressWarnings("rawtypes")
	public String insertAdm6000HoliInfo(Map paramMap) throws Exception{

		return adm6000DAO.insertAdm6000HoliInfo(paramMap);
		
	}
	
	
	
	/**
	 * 휴일관리 내역을 수정한다.
	 * @param paramMap
	 * @throws Exception
	 */
	@SuppressWarnings({ "rawtypes" })
	public void updateAdm6000HoliInfo(Map paramMap) throws Exception{
		
		//메뉴정보 수정
		int upCnt = adm6000DAO.updateAdm6000HoliInfo(paramMap);
		
		//수정된 건이 없으면 튕겨냄
		if(upCnt == 0 ){
			throw new Exception(egovMessageSource.getMessage("fail.common.update"));
		}
	}
	
	/**
	 * 휴일관리 내역을 삭제한다. ( 단건 )
	 * @param paramMap
	 * @throws Exception
	 */
	@SuppressWarnings({ "rawtypes" })
	public void deleteAdm6000HoliInfo(Map paramMap) throws Exception{
		adm6000DAO.deleteAdm6000HoliInfo(paramMap);
	}

	/**
	 * 휴일관리 일괄등록 한다.
	 * @param paramMap
	 * @throws Exception
	 */
	@SuppressWarnings({ "rawtypes", "unchecked", "unused" })
	public void insertAdm6000HoliList(Map paramMap) throws Exception {
		
		String baseDate	= (String) paramMap.get("baseDate");
		String licGrpId	= (String) paramMap.get("licGrpId");
		
		int isLeap = 0;
		
		/* 해당연도의 윤연 여부 */
		boolean isLeapYr = EgovDateUtil.isLeapYear(Integer.parseInt(baseDate));
		
		/* 기존 해당연도의 데이터 삭제 */
		adm6000DAO.deleteAdm6000HoliList( paramMap );
		
		/* 양력 휴일 등록 */
		adm6000DAO.insertAdm6000HoliList( paramMap );
		
		/* 음력 휴일 등록 */
		List<Map>  SolarList = adm6000DAO.selectHoliCdList( paramMap );

		for (Map solarMap : SolarList){
			
			String subCdRef1 = solarMap.get("subCdRef1").toString();
			String subCdRef2 = solarMap.get("subCdRef2").toString();
			
			solarMap.put("licGrpId"		, licGrpId);
			solarMap.put("baseDate"		, baseDate);
			solarMap.put("usrId" 			, (String) paramMap.get("usrId"));
			solarMap.put("usrIp" 			, (String) paramMap.get("usrIp"));
			
			/* 윤연일 경우 */
			if( isLeapYr ){
				/* 2월달일경우 */
				if("02".equals(subCdRef1.substring(0, 2))){
					isLeap = 1;	// 윤달
				}else{
					isLeap = 0;
				}
			/* 윤연이 아닐경우 */
			}else{
				isLeap = 0;
			}
			
			/* 음력 -> 양력 변환 */
			String solarDt = EgovDateUtil.toSolar(baseDate+subCdRef1, isLeap);
			solarMap.put("subCdRef1", solarDt);
			
			adm6000DAO.insertAdm6000HoliSolarList(solarMap);
			
			/* 추석 및 설날은 전날 , 다음날을 구해온다 */
			if("0815".equals(subCdRef1) || "0101".equals(subCdRef1)){
				String befsubCdRef1 = EgovDateUtil.addDay(solarDt, -1);		// 추석 및 설날 전일
				solarMap.put("subCdRef1", befsubCdRef1);
				adm6000DAO.insertAdm6000HoliSolarList(solarMap);
				
				String aftsubCdRef1 = EgovDateUtil.addDay(solarDt, 1);		// 추석 및 설날 다음날
				solarMap.put("subCdRef1", aftsubCdRef1);
				adm6000DAO.insertAdm6000HoliSolarList(solarMap);
			}
			
			
			
		}
	}
	
}
