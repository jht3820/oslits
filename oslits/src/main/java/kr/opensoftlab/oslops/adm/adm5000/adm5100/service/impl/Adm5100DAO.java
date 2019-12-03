package kr.opensoftlab.oslops.adm.adm5000.adm5100.service.impl;

import java.util.List;

import kr.opensoftlab.oslops.adm.adm5000.adm5000.vo.Adm5000VO;
import kr.opensoftlab.oslops.adm.adm5000.adm5100.vo.Adm5100VO;
import kr.opensoftlab.oslops.com.dao.ComOslitsAbstractDAO;
import kr.opensoftlab.sdf.excel.ExcelDataListResultHandler;

import org.springframework.stereotype.Repository;

@Repository("adm5100DAO")
public class Adm5100DAO  extends ComOslitsAbstractDAO {
	
	
	/**
	 *  시스템 사용 이력 로그 목록을 조회한다.
	 * @param adm5100VO
	 * @return List 로그인 이력 로그 목록
	 * @throws Exception
	 */
	@SuppressWarnings("unchecked")
	public List<Adm5100VO>  selectAdm5100List(Adm5100VO adm5100vo) throws Exception {
		 return  (List<Adm5100VO>) list("adm5100DAO.selectAdm5100List", adm5100vo);
   }
	
	
	/**
	 * 시스템 사용 이력 로그 목록 총건수를 조회한다.
	 * @param adm5100VO
	 * @return  int 로그인 이력 로그 목록 총건수 
	 * @throws Exception
	 */
	public int  selectAdm5100ListCnt(Adm5100VO adm5100vo) throws Exception {
		 return  (Integer)select("adm5100DAO.selectAdm5100ListCnt", adm5100vo);
  }


	public void selectAdm5100ExcelList(Adm5100VO adm5100vo,
			ExcelDataListResultHandler resultHandler) throws Exception {
		// TODO Auto-generated method stub
		listExcelDownSql("adm5100DAO.selectAdm5100ExcelList", adm5100vo, resultHandler);
	} 
	
	//시스템 사용이력 쌓기
	public void insertAdm5100AuthLoginLog(Adm5100VO adm5100vo) throws Exception{
		insert("adm5100DAO.insertAdm5100SystemUseLog", adm5100vo);
	}
}




