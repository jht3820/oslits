package kr.opensoftlab.oslops.adm.adm8000.adm8000.service.impl;

import java.util.List;
import java.util.Map;

import javax.annotation.Resource;





import kr.opensoftlab.oslops.adm.adm8000.adm8000.service.Adm8000Service;

import org.springframework.stereotype.Service;

import egovframework.rte.fdl.cmmn.EgovAbstractServiceImpl;

/**
 * @Class Name : Adm8000ServiceImpl.java
 * @Description : Adm8000ServiceImpl Business Implement class
 * @Modification Information
 *
 * @author 공대영
 * @since 2018.09.03
 * @version 1.0
 * @see
 *  
 *  Copyright (C)  All right reserved.
 */
@Service("adm8000Service")
public class Adm8000ServiceImpl  extends EgovAbstractServiceImpl implements Adm8000Service{

	/** Adm8000DAO DI */
    @Resource(name="adm8000DAO")
    private Adm8000DAO adm8000DAO;

    /**
	 * Adm8000 보고서 마스터 등록/수정한다.
	 * @param paramMap
	 * @return
	 * @throws Exception
	 */
	@Override
	public Object saveAdm8000MasterInfo(Map<String, String> paramMap) throws Exception{
		String insNewReportId ="";
		int result = 0;
		String popupGb = (String)paramMap.get("popupGb");
		
		if("insert".equals(popupGb)){
			insNewReportId = adm8000DAO.insertAdm8000MasterInfo( paramMap);
			return insNewReportId;
			//생성된 키가 없으면 튕겨냄
		}else if("update".equals(popupGb)){
			result = adm8000DAO.updateAdm8000MasterInfo(paramMap);
			return result;
		}
		return null;
	}

	/**
	 * Adm8000 보고서 기준연도 목록을 조회한다.
	 * @param paramMap
	 * @return
	 * @throws Exception
	 */
	@Override
	@SuppressWarnings("rawtypes")
	public List<Map> selectAdm8000MasterYearList(Map<String, String> paramMap) throws Exception{
		return adm8000DAO.selectAdm8000MasterYearList( paramMap);
	}
	
	/**
	 * Adm8000 보고서 마스터 목록을 조회한다.
	 * @param paramMap
	 * @return
	 * @throws Exception
	 */
	@Override
	@SuppressWarnings("rawtypes")
	public List<Map> selectAdm8000MasterList(Map<String, String> paramMap) throws Exception{
		return adm8000DAO.selectAdm8000MasterList( paramMap);
	}

	/**
	 * Adm8000 보고서 마스터 단건 조회
	 * @param paramMap
	 * @return
	 * @throws Exception
	 */
    @Override
    @SuppressWarnings({"rawtypes", "unchecked"})
	public Map selectAdm8000MasterInfo(Map map) throws Exception{
		return adm8000DAO.selectAdm8000MasterInfo(map);
	}
    
    /**
	 * Adm8000 보고서 마스터를 삭제한다.
	 * @param paramMap
	 * @return
	 * @throws Exception
	 */
	@Override
	public void deleteAdm8000MasterInfo(Map<String, String> paramMap) throws Exception{
		adm8000DAO.deleteAdm8000MasterInfo(paramMap);
		
		adm8000DAO.deleteAdm8000DetailInfo(paramMap);
	}

	/**
	 * Adm8000 보고서 디테일 등록/수정한다.
	 * @param paramMap
	 * @return
	 * @throws Exception
	 */
	@Override
	public Object saveAdm8000DetailInfo(Map<String, String> paramMap) throws Exception{
		String insNewItemId ="";
		int result = 0;
		String popupGb = (String)paramMap.get("popupGb");
		if("insert".equals(popupGb)){
			insNewItemId = adm8000DAO.insertAdm8000DetailInfo( paramMap);
			return insNewItemId;
			//생성된 키가 없으면 튕겨냄
		}else if("update".equals(popupGb)){
			result = adm8000DAO.updateAdm8000DetailInfo(paramMap);
			return result;
		}
		return null;
	}

	/**
	 * Adm8000 보고서 디테일 목록을 조회한다.
	 * @param paramMap
	 * @return
	 * @throws Exception
	 */
	@Override
	@SuppressWarnings("rawtypes")
	public List<Map> selectAdm8000DetailList(Map<String, String> paramMap) throws Exception{
		return adm8000DAO.selectAdm8000DetailList( paramMap);
	}

	/**
	 * Adm8000 보고서 디테일 단건 조회
	 * @param paramMap
	 * @return
	 * @throws Exception
	 */
	@Override
	@SuppressWarnings("rawtypes")
	public Map selectAdm8000DetailInfo(Map<String, String> paramMap) throws Exception{
		return adm8000DAO.selectAdm8000DetailInfo( paramMap);
	}

	/**
	 * Adm8000 보고서 디테일을 삭제한다.
	 * @param paramMap
	 * @return
	 * @throws Exception
	 */
	@Override
	public int deleteAdm8000DetailInfo(Map<String, String> paramMap) throws Exception{
		return adm8000DAO.deleteAdm8000DetailInfo( paramMap);
	}

	/**
	 * Adm8000 보고서 기준연도를 복사한다.
	 * @param paramMap
	 * @return
	 * @throws Exception
	 */
	@Override
	public Object saveAdm8000CopyInfo(Map<String, String> paramMap) throws Exception{
		//보고서 디테일 복사
		adm8000DAO.insertAdm8000DetailCopyInfo(paramMap);
		//보고서 마스터 복사
		adm8000DAO.insertAdm8000CopyMasterInfo(paramMap);
		
		return null;
	}
    
	
}
