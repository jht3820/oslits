package kr.opensoftlab.oslops.adm.adm8000.adm8000.service.impl;

import java.util.List;
import java.util.Map;

import org.springframework.stereotype.Repository;

import kr.opensoftlab.oslops.com.dao.ComOslitsAbstractDAO;

/**
 * @Class Name : Adm8000DAO.java
 * @Description : Adm8000DAO DAO Class
 * @Modification Information
 *
 * @author 공대영
 * @since 2018.09.03
 * @version 1.0
 * @see
 *  
 *  Copyright (C)  All right reserved.
 */

@Repository("adm8000DAO")
public class Adm8000DAO extends ComOslitsAbstractDAO {

	/**
	 * Adm8000 보고서 마스터 등록한다.
	 * @param paramMap
	 * @return
	 * @throws Exception
	 */
	public String insertAdm8000MasterInfo(Map<String, String> paramMap) throws Exception{
		return (String)insert("adm8000DAO.insertAdm8000MasterInfo", paramMap);
	}

	/**
	 * Adm8000 보고서 기준연도 목록을 조회한다.
	 * @param paramMap
	 * @return
	 * @throws Exception
	 */
	@SuppressWarnings({"rawtypes", "unchecked"})
	public List<Map> selectAdm8000MasterYearList(Map<String, String> paramMap) throws Exception{
		return (List) list("adm8000DAO.selectAdm8000MasterYearList", paramMap);
	}

	/**
	 * Adm8000 보고서 마스터 목록을 조회한다.
	 * @param paramMap
	 * @return
	 * @throws Exception
	 */
	@SuppressWarnings({"rawtypes", "unchecked"})
	public List<Map> selectAdm8000MasterList(Map<String, String> paramMap) throws Exception{
		return (List) list("adm8000DAO.selectAdm8000MasterList", paramMap);
	}
	
	/**
	 * Adm8000 보고서 마스터 단건 조회한다.
	 * @param paramMap
	 * @return
	 * @throws Exception
	 */
	@SuppressWarnings("rawtypes")
	public Map selectAdm8000MasterInfo(Map<String, String> paramMap) throws Exception{
		return (Map) select("adm8000DAO.selectAdm8000MasterInfo", paramMap);
	}
	
	/**
	 * Adm8000 보고서 마스터 수정한다.
	 * @param paramMap
	 * @return
	 * @throws Exception
	 */
	public int updateAdm8000MasterInfo(Map<String, String> paramMap) throws Exception{
		return update("adm8000DAO.updateAdm8000MasterInfo", paramMap);
	}
	
	/**
	 * Adm8000 보고서 마스터 삭제한다.
	 * @param paramMap
	 * @return
	 * @throws Exception
	 */
	public void deleteAdm8000MasterInfo(Map<String, String> paramMap) throws Exception{
		delete("adm8000DAO.deleteAdm8000MasterInfo", paramMap);
	}

	/**
	 * Adm8000 보고서 디테일을 등록한다.
	 * @param paramMap
	 * @return
	 * @throws Exception
	 */
	public String insertAdm8000DetailInfo(Map<String, String> paramMap) throws Exception{
		return (String)insert("adm8000DAO.insertAdm8000DetailInfo", paramMap);
	}
	
	/**
	 * Adm8000 보고서 디테일 목록을 조회한다.
	 * @param paramMap
	 * @return
	 * @throws Exception
	 */
	@SuppressWarnings({"rawtypes", "unchecked"})
	public List<Map> selectAdm8000DetailList(Map<String, String> paramMap) throws Exception{
		return (List) list("adm8000DAO.selectAdm8000DetailList", paramMap);
	}
	
	/**
	 * Adm8000 보고서 디테일 단건 조회
	 * @param paramMap
	 * @return
	 * @throws Exception
	 */
	@SuppressWarnings("rawtypes")
	public Map selectAdm8000DetailInfo(Map<String, String> paramMap) throws Exception{
		return (Map) select("adm8000DAO.selectAdm8000DetailInfo", paramMap);
	}
	
	/**
	 * Adm8000 보고서 디테일을 수정한다.
	 * @param paramMap
	 * @return
	 * @throws Exception
	 */
	public int updateAdm8000DetailInfo(Map<String, String> paramMap) throws Exception{
		return update("adm8000DAO.updateAdm8000DetailInfo", paramMap);
	}
	
	/**
	 * Adm8000 보고서 디테일을 삭제한다.
	 * @param paramMap
	 * @return
	 * @throws Exception
	 */
	public int deleteAdm8000DetailInfo(Map<String, String> paramMap) throws Exception{
		return delete("adm8000DAO.deleteAdm8000DetailInfo", paramMap);
	}

	/**
	 * Adm8000 보고서 기준연도의 디테일 항목 복사
	 * @param paramMap
	 * @return
	 * @throws Exception
	 */
	public void insertAdm8000DetailCopyInfo(Map<String, String> paramMap) throws Exception{
		insert("adm8000DAO.insertAdm8000DetailCopyInfo", paramMap);
	}

	/**
	 * Adm8000 보고서 기준연도의 마스터 항목 복사
	 * @param paramMap
	 * @return
	 * @throws Exception
	 */
	public void insertAdm8000CopyMasterInfo(Map<String, String> paramMap) throws Exception{
		insert("adm8000DAO.insertAdm8000MasterCopyInfo", paramMap);
	}
}
