package kr.opensoftlab.oslops.adm.adm2000.adm2000.service.impl;

import java.util.List;
import java.util.Map;

import kr.opensoftlab.oslops.adm.adm2000.adm2000.vo.Adm2000VO;
import kr.opensoftlab.oslops.com.dao.ComOslitsAbstractDAO;
import kr.opensoftlab.sdf.excel.ExcelDataListResultHandler;

import org.springframework.stereotype.Repository;


/**
 * @Class Name : Adm2000DAO.java
 * @Description : Adm2000DAO DAO Class
 * @Modification Information
 * <pre>
 *   수정일      수정자          수정내용
 *  -------    --------    ---------------------------
 *  2016.01.28  김정환          최초 생성
 *  2018.08.10  배용진          차단여부 수정 추가
 *  
 * </pre>
 * @author 김정환
 * @since 2016.01.28.
 * @version 1.0
 * @see
 *  
 *  Copyright (C) Open Soft Lab Corp. All Rights Reserved
 */
@Repository("adm2000DAO")
public class Adm2000DAO extends ComOslitsAbstractDAO {
	
	@SuppressWarnings("unchecked")
	public List<Adm2000VO> selectAdm2000UsrList(Adm2000VO adm2000VO) throws Exception{
		return (List<Adm2000VO>) list("adm2000DAO.selectAdm2000UsrList", adm2000VO);    
	}
	
	public int  selectAdm2000UsrListCnt(Adm2000VO adm2000VO) throws Exception {
		 return  (Integer)select("adm2000DAO.selectAdm2000UsrListCnt", adm2000VO);
	} 
	
    @SuppressWarnings("rawtypes")
	public Map selectAdm2000UsrInfo(Map param) throws Exception {
        return (Map) select("adm2000DAO.selectAdm2000UsrInfo", param);    
    }
    
	@SuppressWarnings("rawtypes")
	public String insertAdm2000UsrInfo(Map paramMap) throws Exception{
		
		return (String) insert("adm2000DAO.insertAdm2000UsrInfo", paramMap);
		
	}

	@SuppressWarnings("rawtypes")
	public int updateAdm2000UsrInfo(Map paramMap) throws Exception{
		return (int) update("adm2000DAO.updateAdm2000UsrInfo", paramMap );
	}

	
	@SuppressWarnings({ "deprecation", "rawtypes" })
	public int selectCmm2000IdCheck(Map paramMap)  throws Exception{
		return (int) getSqlMapClientTemplate().queryForObject("adm2000DAO.selectCmm2000IdCheck", paramMap);
	}
	
	@SuppressWarnings({ "deprecation", "rawtypes" })
	public int selectCmm2000EmailCheck(Map paramMap)  throws Exception{
		return (int) getSqlMapClientTemplate().queryForObject("adm2000DAO.selectCmm2000EmailCheck", paramMap);
	}

	
	@SuppressWarnings("rawtypes")
	public int updateAdm2000UseCd(Map paramMap) throws Exception{
		return (int) update("adm2000DAO.updateAdm2000UseCd", paramMap );
	}

	/**
	 * 사용자를 삭제한다
	 * @param 
	 * @return 
	 * @throws Exception
	 */
	@SuppressWarnings("rawtypes")
	public int deleteAdm2000UsrInfo(Map paramMap) throws Exception{
		return (int) update("adm2000DAO.deleteAdm2000UsrInfo", paramMap);
	}
	
	/**
	 * 사용자 목록을 엑셀 다운로드용으로 조회한다.
	 * @param adm2000VO
	 * @return 사용자 목록
	 * @throws Exception
	 */
	public void  selectAdm2000ExcelList(Adm2000VO adm2000vo, ExcelDataListResultHandler resultHandler) throws Exception {
		listExcelDownSql("adm2000DAO.selectAdm2000ExcelList", adm2000vo, resultHandler);
	}

	/**
	 * 사용자 일괄 등록한다.
	 * @param adm2000VO
	 * @return 사용자 목록
	 * @throws Exception
	 */
	public String insertAdm2000AdmInfoAjax(Map<String, String> paramMap) throws Exception {
		return (String)insert("adm2000DAO.insertAdm2000UsrInfo", paramMap);
	}
	
	/**
	 * 프로젝트별 사용자 존재 여부 조회
	 * @param 
	 * @return 프로젝트 포함된 사용자 수
	 * @throws Exception
	 */
	public int selectAdm1300ExistUsrInProject(Map<String, String> paramMap) throws Exception {
		return (int)select("adm2000DAO.selectAdm1300ExistUsrInProject", paramMap);
	}

	/**
	 * 이전 패스워드 조회
	 * @param paramMap
	 * @return
	 * @throws Exception
	 */
	public String selectAdm2000PwCheck(Map<String, String> paramMap) throws Exception {
		return (String) select("adm2000DAO.selectAdm2000PwCheck", paramMap);
	}
	



	
	/**
	 * Adm2000 사용자 차단여부 수정 (단건)
	 * @param param - Map
	 * @return
	 * @exception Exception
	 */
	@SuppressWarnings("rawtypes")
	public int updateAdm2000Block(Map paramMap) throws Exception{
		return (int) update("adm2000DAO.updateAdm2000Block", paramMap );
	}

	
	/**
	 * Adm2000 사용자가 속해있는 조직목록 조회
	 * @param  param - Map
	 * @return
	 * @throws Exception
	 */
	@SuppressWarnings("rawtypes")
	public List selectAdm2000ExistUsrInDept(Map paramMap) throws Exception {
		return(List) list("adm2000DAO.selectAdm2000ExistUsrInDept", paramMap);
	}

	public int updateAdm2000AccountInit(Map<String, String> paramMap) {
		// TODO Auto-generated method stub
		return update("adm2000DAO.updateAdm2000AccountInit", paramMap );
	}

	public void insertAdm2100BlockLog(Map paramMap) {
		// TODO Auto-generated method stub
		insert("adm2000DAO.insertAdm2100BlockLog", paramMap );
	}
	
	/**
	 * Adm2000 비밀번호 만료된 사용자 비밀번호 및 차단여부 초기화
	 * @param  param - Map
	 * @return
	 * @throws Exception
	 */
	public int updateAdm2000PasswordExprInit(Map<String, String> paramMap) {
		return update("adm2000DAO.updateAdm2000PasswordExprInit", paramMap );
	}
	
}