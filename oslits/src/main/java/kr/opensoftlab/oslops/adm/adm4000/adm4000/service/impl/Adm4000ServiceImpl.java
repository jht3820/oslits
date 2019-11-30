package kr.opensoftlab.oslops.adm.adm4000.adm4000.service.impl;

import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import kr.opensoftlab.oslops.adm.adm4000.adm4000.service.Adm4000Service;
import kr.opensoftlab.oslops.com.vo.LoginVO;
import kr.opensoftlab.sdf.excel.ExcelDataListResultHandler;

import org.springframework.stereotype.Service;

import egovframework.rte.fdl.cmmn.EgovAbstractServiceImpl;

/**
 * @Class Name : Adm4000ServiceImpl.java
 * @Description : Adm4000ServiceImpl Business Implement class
 * @Modification Information
 *
 * @author 정형택
 * @since 2016.01.31
 * @version 1.0
 * @see
 *  
 *  Copyright (C)  All right reserved.
 */
@Service("adm4000Service")
public class Adm4000ServiceImpl  extends EgovAbstractServiceImpl implements Adm4000Service{

	/** Scpr5000DAO DI */
    @Resource(name="adm4000DAO")
    private Adm4000DAO adm4000DAO;
    
    /**
	 * Adm4000 공통코드 마스터 조회
	 * @param
	 * @return 공통코드 마스터 목록
	 * @exception Exception
	 */
	@SuppressWarnings("rawtypes")
	public List selectAdm4000CommonCodeMasterList(Map inputMap) throws Exception {
		return adm4000DAO.selectAdm4000CommonCodeMasterList(inputMap);
    }
	
	/**
	 * Adm4000 공통코드 디테일 조회
	 * @param
	 * @return 공통코드 디테일 목록
	 * @exception Exception
	 */
	@SuppressWarnings("rawtypes")
	public List selectAdm4000CommonCodeDetailList(Map inputMap) throws Exception {
		return adm4000DAO.selectAdm4000CommonCodeDetailList(inputMap);
    }
	
	/**
	 * Adm4000 공통코드 마스터 정보 저장
	 * @param param - Map
	 * @return 
	 * @exception Exception
	 */
	@SuppressWarnings("rawtypes")
	public void saveAdm4000CommonCodeMaster(Map paramMap) throws Exception{
		adm4000DAO.saveAdm4000CommonCodeMaster(paramMap);
    }
	
	/**
	 * Adm4000 배포 버전 등록
	 * @param param - Map
	 * @return 
	 * @exception Exception
	 */
	@SuppressWarnings("rawtypes")
	public void updateAdm4000CommonCodeInfo(Map paramMap) throws Exception{
		adm4000DAO.updateAdm4000CommonCodeInfo(paramMap);
    }

	/**
	 * Adm4000 배포 버전 리스트 삭제
	 * @param param - Map
	 * @return 
	 * @exception Exception
	 */
	@SuppressWarnings("rawtypes")
	public void deleteAdm4000CommonCodeInfoList(Map paramMap) throws Exception{
		adm4000DAO.deleteAdm4000CommonCodeInfoList(paramMap);
    }

	@Override
	@SuppressWarnings("rawtypes")
	public void saveAdm4000CommonCodeDetail(Map paramMap)  throws Exception{
		// TODO Auto-generated method stub
		adm4000DAO.saveAdm4000CommonCodeDetail(paramMap);
		
	}

	@Override
	@SuppressWarnings("rawtypes")
	public void deleteAdm4000CommonCodeDetail(Map paramMap)  throws Exception{
		// TODO Auto-generated method stub
		adm4000DAO.deleteAdm4000CommonCodeDetail(paramMap);
	}

	@Override
	@SuppressWarnings("rawtypes")
	public void deleteAdm4000CommonCodeMaster(Map paramMap) throws Exception {
		// TODO Auto-generated method stub
		adm4000DAO.deleteAdm4000CommonCodeMaster(paramMap);
	}

	@Override
	public void selectAdm4000MasterExcelList(LoginVO loginVO, ExcelDataListResultHandler resultHandler) throws Exception {
		adm4000DAO.selectAdm4000MasterExcelList(loginVO, resultHandler);
	}
	
	@Override
	public void selectAdm4000DetailExcelList(Map paramMap, ExcelDataListResultHandler resultHandler) throws Exception {
		adm4000DAO.selectAdm4000DetailExcelList(paramMap, resultHandler);
	}
	@Override
	public int selectAdm4000CommonCodeCount(Map paramMap) throws Exception {
		return adm4000DAO.selectAdm4000CommonCodeCount(paramMap);
	}
	@Override
	public int selectAdm4000CommonDetailCodeCount(Map paramMap) throws Exception {
		return adm4000DAO.selectAdm4000CommonDetailCodeCount(paramMap);
	}

}
