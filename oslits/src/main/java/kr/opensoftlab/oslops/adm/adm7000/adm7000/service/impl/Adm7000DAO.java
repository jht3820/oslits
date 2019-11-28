package kr.opensoftlab.oslops.adm.adm7000.adm7000.service.impl;

import java.util.List;
import java.util.Map;

import kr.opensoftlab.oslops.com.dao.ComOslitsAbstractDAO;
import kr.opensoftlab.sdf.excel.ExcelDataListResultHandler;

import org.springframework.stereotype.Repository;

/**
 * @Class Name : Adm7000DAO.java
 * @Description : 조직 관리(Adm7000) DAO 클래스
 * @Modification Information
 * <pre>
 *   수정일      수정자          수정내용
 *  -------    --------    ---------------------------
 *  2018.08.01  배용진          최초 생성
 *  
 * </pre>
 *  @author 배용진
 *  @version 1.0
 *  @see 
 *  
 *  Copyright (C) Open Soft Lab Corp. All Rights Reserved
 */
@Repository("adm7000DAO")
public class Adm7000DAO extends ComOslitsAbstractDAO {
	
	/**
	 * Adm7000 등록된 조직 목록 조회 (List)
	 * <br> - 조직 목록을 조회한다.
	 * @param param - Map
	 * @return List - 조직 목록
	 * @exception Exception
	 */
	@SuppressWarnings("rawtypes")
	public List selectAdm7000DeptList(Map paramMap) throws Exception {
		 return (List) list("adm7000DAO.selectAdm7000DeptList", paramMap);
    }
	
	
	/**
	 * Adm7000 조직 정보 조회 (단건)
	 * <br> - 1건의 조직 정보를 조회한다.
	 * @param param - Map
	 * @return 
	 * @exception Exception
	 */
	@SuppressWarnings("rawtypes")
	public Map selectAdm7000DeptInfo(Map paramMap) throws Exception{
		return (Map) select("adm7000DAO.selectAdm7000DeptInfo", paramMap);
	}
	
	
	/**
	 * Adm7000 조직 루트 디렉토리 생성
	 * <br> - 등록된 조직이 없을경우 최상위 조직(ROOT)을 생성한다.
	 * @param param - Map
	 * @return 
	 * @exception Exception
	 */
	@SuppressWarnings("rawtypes")
	public String insertAdm7000RootDeptInfo(Map paramMap) throws Exception{
		return (String) insert("adm7000DAO.insertAdm7000RootDeptInfo", paramMap);
	}
	
	
	/**
	 * Adm7000 조직 정보 등록(단건)
	 * <br> - 조직 정보를 등록한다.
	 * @param param - Map
	 * @return 
	 * @exception Exception
	 */
	@SuppressWarnings("rawtypes")
	public String insertAdm7000DeptInfo(Map paramMap) throws Exception{
		return (String) insert("adm7000DAO.insertAdm7000DeptInfo", paramMap);
	}
	
	
	/**
	 * Adm7000 조직 정보 수정(단건)
	 * <br> - 조직 정보를 수정한다.
	 * @param param - Map
	 * @return 
	 * @exception Exception
	 */
	@SuppressWarnings("rawtypes")
	public int updateAdm7000DpteInfo(Map paramMap) throws Exception{
		return (int) update("adm7000DAO.updateAdm7000DpteInfo", paramMap);
	}
	
	
	/**
	 * Adm7000 조직 정보 삭제(단건)
	 * <br> - 조직 정보를 삭제한다.
	 * @param param - Map
	 * @return 
	 * @exception Exception
	 */
	@SuppressWarnings("rawtypes")
	public int deleteAdm7000DeptInfo(Map paramMap) throws Exception{
		return (int) delete("adm7000DAO.deleteAdm7000DeptInfo", paramMap);
	}
	
	
	/**
	 * Adm7000 상위조직 조회 (list)
	 * <br> - 선택한 조직의 상위 조직명을 조회한다.
	 * @param param - Map
	 * @return List - 조직 목록
	 * @exception Exception
	 */
	@SuppressWarnings("rawtypes")
	public List selectAdm7000UpperDeptList(Map paramMap) throws Exception {
		 return (List) list("adm7000DAO.selectAdm7000UpperDeptList", paramMap);
    }
	
	
	/**
	 * Adm7000 해당 조직ID가 있는지 체크 
	 * @param param - Map
	 * @return 
	 * @exception Exception
	 */
	@SuppressWarnings("rawtypes")
	public int selectAdm7000ExistDeptChk(Map paramMap) throws Exception{
		return (int)select("adm7000DAO.selectAdm7000ExistDeptChk", paramMap);
	}
	
	
	/**
	 * Adm7000 조직 목록 엑셀 다운로드
	 * @param param - Map
	 * @return 
	 * @exception Exception
	 */
	@SuppressWarnings("rawtypes")
	public void selectAdm7000ExcelList(Map paramMap, ExcelDataListResultHandler resultHandler) throws Exception {
		listExcelDownSql("adm7000DAO.selectAdm7000DeptList", paramMap, resultHandler);
	}
}
