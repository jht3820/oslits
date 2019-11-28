package kr.opensoftlab.oslops.adm.adm5000.adm5100.service;

import java.util.List;
import java.util.Map;

import kr.opensoftlab.oslops.adm.adm5000.adm5000.vo.Adm5000VO;
import kr.opensoftlab.oslops.adm.adm5000.adm5100.vo.Adm5100VO;
import kr.opensoftlab.sdf.excel.ExcelDataListResultHandler;

public interface Adm5100Service {
	
	/**
	 * 시스템 사용 이력 로그 목록을 조회한다.
	 * @param adm5100VO
	 * @return List 로그인 이력 로그 목록
	 * @throws Exception
	 */
	List<Adm5100VO> selectAdm5100List(Adm5100VO adm5100VO) throws Exception;
	
	
	/**
	 *  시스템 사용 이력 로그 목록 총건수를 조회한다.
	 * @param adm5100VO
	 * @return  int 로그인 이력 로그 목록 총건수 
	 * @throws Exception
	 */
	int selectAdm5100ListCnt(Adm5100VO adm5100VO) throws Exception;


	void selectAdm5100ExcelList(Adm5100VO adm5100vo,
			ExcelDataListResultHandler resultHandler) throws Exception;	
	//시스템 사용이력 쌓기
	void insertAdm5100AuthLoginLog(Adm5100VO adm5100vo) throws Exception;
	
}
