package kr.opensoftlab.oslops.adm.adm2000.adm2000.service;

import java.util.List;
import java.util.Map;

import kr.opensoftlab.oslops.adm.adm2000.adm2000.vo.Adm2000VO;
import kr.opensoftlab.sdf.excel.ExcelDataListResultHandler;


/**
 * @Class Name : Adm2000Service.java
 * @Description : Adm2000Service Business class
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
public interface Adm2000Service {
	
	/**
	 * 사용자 정보 목록 조회
	 * @param adm2000VO
	 * @return
	 * @throws Exception
	 */
	List<Adm2000VO> selectAdm2000UsrList(Adm2000VO adm2000VO) throws Exception;

	/**
	 * 총 건수 조회
	 * @param adm2000VO
	 * @return
	 * @throws Exception
	 */
	int selectAdm2000UsrListCnt(Adm2000VO adm2000VO) throws Exception;
	
	/**
	 * 사용자 단건 조회
	 * @param param
	 * @return
	 * @throws Exception
	 */
	@SuppressWarnings("rawtypes")
	Map selectAdm2000UsrInfo(Map param) throws Exception;	
	
	/**
	 * 사용자 등록 및 수정
	 * @param paramMap
	 * @throws Exception
	 */
	@SuppressWarnings("rawtypes")
	public void saveAdm2000UsrInfo(Map paramMap) throws Exception;
	
	/**
	 * 아이디 중복 체크
	 * @param paramMap
	 * @return
	 * @throws Exception
	 */
	@SuppressWarnings("rawtypes")
	int selectCmm2000IdCheck(Map paramMap)  throws Exception;
	
	/**
	 * 이메일 중복 체크
	 * @param paramMap
	 * @return
	 * @throws Exception
	 */
	@SuppressWarnings("rawtypes")
	int selectCmm2000EmailCheck(Map paramMap)  throws Exception;

	/**
	 * 사용유무 수정
	 * @param paramMap
	 * @throws Exception
	 */
	@SuppressWarnings("rawtypes")
	void updateAdm2000UseCd(Map paramMap) throws Exception;

	/**
	 * 사용자 삭제
	 * @param paramMap
	 * @throws Exception
	 */
	@SuppressWarnings("rawtypes")
	void deleteAdm2000UsrInfo(Map paramMap) throws Exception;
	
	/**
	 * 사용자 목록 엑셀 조회를 한다.
	 * @params adm2000VO
	 * @return List 배포 버전 요구사항 목록
	 * @throws Exception
	 */
	void selectAdm2000ExcelList(Adm2000VO adm2000vo,ExcelDataListResultHandler resultHandler) throws Exception;
	

	/**
	 * Adm2000 사용자 일괄 저장
	 * @param prjId 
	 * @param param - Map
	 * @return 
	 * @exception Exception
	 */
	void insertAdm2000AdmInfoListAjax(Map<String, String> paramMap, String prjId) throws Exception ;


	/**
	 * Adm2000 사용자 차단여부 수정 (단건)
	 * @param param - Map
	 * @return
	 * @exception Exception
	 */
	@SuppressWarnings("rawtypes")
	public int updateAdm2000Block(Map paramMap) throws Exception;


	/**
	 * Adm2000 사용자가 속해있는 조직목록 조회
	 * @param  param - Map
	 * @return
	 * @throws Exception
	 */
	@SuppressWarnings("rawtypes")
	public List selectAdm2000ExistUsrInDept(Map paramMap) throws Exception;

	void updateAdm2000AccountInit(Map<String, String> paramMap)  throws Exception;
}
