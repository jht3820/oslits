package kr.opensoftlab.oslops.adm.adm5000.adm5000.service;

import java.util.List;
import java.util.Map;

import kr.opensoftlab.oslops.adm.adm5000.adm5000.vo.Adm5000VO;
import kr.opensoftlab.sdf.excel.ExcelDataListResultHandler;

public interface Adm5000Service {
	
	/**
	 * 로그인 이력 로그 목록을 조회한다.
	 * @param adm5000VO
	 * @return List 로그인 이력 로그 목록
	 * @throws Exception
	 */
	List<Adm5000VO> selectAdm5000List(Adm5000VO adm5000VO) throws Exception;
	
	
	/**
	 * 로그인 이력 로그 목록 총건수를 조회한다.
	 * @param adm5000VO
	 * @return  int 로그인 이력 로그 목록 총건수 
	 * @throws Exception
	 */
	int selectAdm5000ListCnt(Adm5000VO adm5000VO) throws Exception;


	void selectAdm5000ExcelList(Adm5000VO adm5000vo,
			ExcelDataListResultHandler resultHandler) throws Exception;	
	
	//로그인 이력 쌓기
	void insertAdm5000AuthLoginLog(Adm5000VO adm5000vo) throws Exception;
	
	//로그아웃 이력 쌓기
	void updateAdm5000AuthLogoutLog(Adm5000VO adm5000vo) throws Exception;
	
	
	/**
	 * Adm5000 계정 사용기간 체크를 위한 마지막 로그인 정보 조회 (단건)
	 * @param param - Map
	 * @return 
	 * @exception Exception
	 */
	@SuppressWarnings("rawtypes")
	public Map selectAdm5000LastLogin(Map paramMap) throws Exception;
	
	
	/**
	 * Adm5000 최근 로그인 정보 조회 (단건)
	 * @param param - Map
	 * @return 
	 * @exception Exception
	 */
	@SuppressWarnings("rawtypes")
	public Map selectAdm5000RecentLogin(Map paramMap) throws Exception;
}
