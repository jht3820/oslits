package kr.opensoftlab.oslops.adm.adm5000.adm5000.service.impl;

import java.util.List;
import java.util.Map;

import kr.opensoftlab.oslops.adm.adm5000.adm5000.vo.Adm5000VO;
import kr.opensoftlab.oslops.com.dao.ComOslitsAbstractDAO;
import kr.opensoftlab.sdf.excel.ExcelDataListResultHandler;

import org.springframework.stereotype.Repository;

@Repository("adm5000DAO")
public class Adm5000DAO  extends ComOslitsAbstractDAO {
	
	
	/**
	 * 로그인 이력 로그 목록을 조회한다.
	 * @param adm5000VO
	 * @return List 로그인 이력 로그 목록
	 * @throws Exception
	 */
	@SuppressWarnings("unchecked")
	public List<Adm5000VO>  selectAdm5000List(Adm5000VO adm5000vo) throws Exception {
		 return  (List<Adm5000VO>) list("adm5000DAO.selectAdm5000List", adm5000vo);
   }
	
	
	/**
	 * 로그인 이력 로그 목록 총건수를 조회한다.
	 * @param adm5000VO
	 * @return  int 로그인 이력 로그 목록 총건수 
	 * @throws Exception
	 */
	public int  selectAdm5000ListCnt(Adm5000VO adm5000vo) throws Exception {
		 return  (Integer)select("adm5000DAO.selectAdm5000ListCnt", adm5000vo);
  }


	public void selectAdm5000ExcelList(Adm5000VO adm5000vo,
			ExcelDataListResultHandler resultHandler) throws Exception {
		// TODO Auto-generated method stub
		listExcelDownSql("adm5000DAO.selectAdm5000ExcelList", adm5000vo, resultHandler);	
	} 
	
	//로그인 이력 쌓기
	public void insertAdm5000AuthLoginLog(Adm5000VO adm5000vo) throws Exception{
		insert("adm5000DAO.insertAdm5000AuthLoginLog", adm5000vo);
	}
	
	//로그아웃 이력 쌓기
	void updateAdm5000AuthLogoutLog(Adm5000VO adm5000vo) throws Exception{
		update("adm5000DAO.updateAdm5000AuthLogoutLog", adm5000vo);
	}
		
	/**
	 * Adm5000 계정 사용기간 체크를 위한 마지막 로그인 정보 조회 (단건)
	 * @param param - Map
	 * @return 
	 * @exception Exception
	 */
	@SuppressWarnings("rawtypes")
	public Map selectAdm5000LastLogin(Map paramMap) throws Exception{
		return (Map) select("adm5000DAO.selectAdm5000LastLogin", paramMap);
	}
	
	
	/**
	 * Adm5000 최근 로그인 정보 조회 (단건)
	 * @param param - Map
	 * @return 
	 * @exception Exception
	 */
	@SuppressWarnings("rawtypes")
	public Map selectAdm5000RecentLogin(Map paramMap) throws Exception{
		return (Map) select("adm5000DAO.selectAdm5000RecentLogin", paramMap);
	}
	
}




