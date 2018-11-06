package kr.opensoftlab.oslits.adm.adm8000.adm8000.service.impl;

import java.util.List;
import java.util.Map;

import kr.opensoftlab.oslits.com.dao.ComOslitsAbstractDAO;
import kr.opensoftlab.oslits.stm.stm3000.stm3000.vo.Stm3000VO;

import org.springframework.stereotype.Repository;

import com.sun.star.uno.Exception;

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

	public String insertAdm8000MasterInfo(Map<String, String> paramMap) {
		// TODO Auto-generated method stub
		return (String)insert("adm8000DAO.insertAdm8000MasterInfo", paramMap);
	}

	public List<Map> selectAdm8000MasterYearList(Map<String, String> paramMap) {
		// TODO Auto-generated method stub
		return (List) list("adm8000DAO.selectAdm8000MasterYearList", paramMap);
	}

	
	public List<Map> selectAdm8000MasterList(Map<String, String> paramMap) {
		// TODO Auto-generated method stub
		return (List) list("adm8000DAO.selectAdm8000MasterList", paramMap);
	}
	
	public Map selectAdm8000MasterInfo(Map<String, String> paramMap) {
		// TODO Auto-generated method stub
		return (Map) select("adm8000DAO.selectAdm8000MasterInfo", paramMap);
	}
	
	public int updateAdm8000MasterInfo(Map<String, String> paramMap) {
		// TODO Auto-generated method stub
		return update("adm8000DAO.updateAdm8000MasterInfo", paramMap);
	}
	
	public void deleteAdm8000MasterInfo(Map<String, String> paramMap) {
		// TODO Auto-generated method stub
		delete("adm8000DAO.deleteAdm8000MasterInfo", paramMap);
	}

	public String insertAdm8000DetailInfo(Map<String, String> paramMap) {
		// TODO Auto-generated method stub 
		return (String)insert("adm8000DAO.insertAdm8000DetailInfo", paramMap);
	}
	
	public List<Map> selectAdm8000DetailList(Map<String, String> paramMap) {
		// TODO Auto-generated method stub
		return (List) list("adm8000DAO.selectAdm8000DetailList", paramMap);
	}
	
	public Map selectAdm8000DetailInfo(Map<String, String> paramMap) {
		// TODO Auto-generated method stub
		return (Map) select("adm8000DAO.selectAdm8000DetailInfo", paramMap);
	}
	
	public int updateAdm8000DetailInfo(Map<String, String> paramMap) {
		// TODO Auto-generated method stub
		return update("adm8000DAO.updateAdm8000DetailInfo", paramMap);
	}
	
	public int deleteAdm8000DetailInfo(Map<String, String> paramMap) {
		// TODO Auto-generated method stub
		return delete("adm8000DAO.deleteAdm8000DetailInfo", paramMap);
	}

	public void insertAdm8000DetailCopyInfo(Map<String, String> paramMap) {
		// TODO Auto-generated method stub
		insert("adm8000DAO.insertAdm8000DetailCopyInfo", paramMap);
	}

	public void insertAdm8000CopyMasterInfo(Map<String, String> paramMap) {
		// TODO Auto-generated method stub
		insert("adm8000DAO.insertAdm8000MasterCopyInfo", paramMap);
	}
}
