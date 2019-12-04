package kr.opensoftlab.oslops.adm.adm4000.adm4000.service.impl;

import java.util.List;
import java.util.Map;

import kr.opensoftlab.oslops.com.dao.ComOslitsAbstractDAO;
import kr.opensoftlab.oslops.com.vo.LoginVO;
import kr.opensoftlab.sdf.excel.ExcelDataListResultHandler;

import org.springframework.stereotype.Repository;

/**
 * @Class Name : Adm4000DAO.java
 * @Description : Adm4000DAO DAO Class
 * @Modification Information
 *
 * @author 정형택
 * @since 2016.01.30
 * @version 1.0
 * @see
 *  
 *  Copyright (C)  All right reserved.
 */

@Repository("adm4000DAO")
public class Adm4000DAO extends ComOslitsAbstractDAO {
	
	/**
	 * Adm4000 공통코드 마스터 조회
	 * @param param - Map
	 * @return list 공통코드 마스터 리스트
	 * @throws Exception
	 */
	@SuppressWarnings({"rawtypes" })
	public List selectAdm4000CommonCodeMasterList(Map inputMap)  throws Exception{
		return (List) list("adm4000DAO.selectAdm4000CommonCodeMasterList", inputMap);
	}
	
	/**
	 * Adm4000 공통코드 디테일 조회
	 * @param param - Map
	 * @return list 공통코드 디테일 리스트
	 * @throws Exception
	 */
	@SuppressWarnings({"rawtypes" })
	public List selectAdm4000CommonCodeDetailList(Map inputMap)  throws Exception{
		return (List) list("adm4000DAO.selectAdm4000CommonCodeDetailList", inputMap);
	}
	
	/**
	 * Adm4000 공통코드 마스터 정보 저장
	 * @param param - Map
	 * @return 
	 * @exception Exception
	 */
	@SuppressWarnings("rawtypes")
	public void saveAdm4000CommonCodeMaster(Map paramMap) throws Exception{
		insert("adm4000DAO.saveAdm4000CommonCodeMaster", paramMap);
    }
	
	/**
	 * Adm4000 배포 버전 등록
	 * @param param - Map
	 * @return 
	 * @exception Exception
	 */
	@SuppressWarnings("rawtypes")
	public void updateAdm4000CommonCodeInfo(Map paramMap) throws Exception{
		insert("adm4000DAO.insertAdm4000CommonCodeInfo", paramMap);
    }
	
	/**
	 * Adm4000 배포 버전 리스트 삭제
	 * @param param - Map
	 * @return 
	 * @exception Exception
	 */
	@SuppressWarnings("rawtypes")
	public void deleteAdm4000CommonCodeInfoList(Map paramMap) throws Exception{
		insert("adm4000DAO.deleteAdm4000CommonCodeInfoList", paramMap);
    }

	@SuppressWarnings("rawtypes")
	public void saveAdm4000CommonCodeDetail(Map paramMap)  throws Exception{
		insert("adm4000DAO.saveAdm4000CommonCodeDetail", paramMap);
		
	}

	@SuppressWarnings("rawtypes")
	public void deleteAdm4000CommonCodeDetail(Map paramMap) throws Exception {
		delete("adm4000DAO.deleteAdm4000CommonCodeDetail", paramMap);
	}

	@SuppressWarnings("rawtypes")
	public void deleteAdm4000CommonCodeMaster(Map paramMap) throws Exception {
		/* 공통코드 디테일 삭제 */
		delete("adm4000DAO.deleteAdm4000CommonCodeDetailToMstCd", paramMap);
		
		/* 공통코드 마스터 삭제 */
		delete("adm4000DAO.deleteAdm4000CommonCodeMaster", paramMap);
		
	}
	
	public void  selectAdm4000MasterExcelList(LoginVO loginVO, ExcelDataListResultHandler resultHandler) throws Exception {
		listExcelDownSql("adm4000DAO.selectAdm4000MasterExcelList", loginVO, resultHandler);
	}
	
	public void  selectAdm4000DetailExcelList(Map paramMap, ExcelDataListResultHandler resultHandler) throws Exception {
		listExcelDownSql("adm4000DAO.selectAdm4000DetailExcelList", paramMap, resultHandler);
	}

	public int selectAdm4000CommonCodeCount(Map paramMap) throws Exception {
		return (Integer)select("adm4000DAO.selectAdm4000CommonCodeCount", paramMap);
	}

	public int selectAdm4000CommonDetailCodeCount(Map paramMap) throws Exception {
		return (Integer)select("adm4000DAO.selectAdm4000CommonDetailCodeCount", paramMap);
	}

}
