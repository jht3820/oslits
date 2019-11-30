package kr.opensoftlab.oslops.adm.adm7000.adm7000.service;

import java.util.List;
import java.util.Map;

import kr.opensoftlab.sdf.excel.ExcelDataListResultHandler;

/**
 * @Class Name : Adm7000Service.java
 * @Description : 조직 관리(Adm7000) 서비스 인터페이스
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
public interface Adm7000Service {
	
	/**
	 * Adm7000 등록된 조직 목록 조회 (List)
	 * <br> - 조직 목록을 조회한다.
	 * @param param - Map
	 * @return List - 조직 목록
	 * @exception Exception
	 */
	@SuppressWarnings("rawtypes")
	public List selectAdm7000DeptList(Map paramMap) throws Exception;

	
	/**
	 * Adm7000 조직 정보 조회 (단건)
	 * <br> - 1건의 조직 정보를 조회한다.
	 * @param param - Map
	 * @return 
	 * @exception Exception
	 */
	@SuppressWarnings("rawtypes")
	public Map selectAdm7000DeptInfo(Map paramMap) throws Exception;
	
	/**
	 * Adm7000 조직 루트 디렉토리 생성
	 * <br> - 등록된 조직이 없을경우 최상위 조직(ROOT)을 생성한다.
	 * @param param - Map
	 * @return 
	 * @exception Exception
	 */
	@SuppressWarnings("rawtypes")
	public String insertAdm7000RootDeptInfo(Map paramMap) throws Exception;
	
	
	/**
	 * Adm7000 조직 정보 등록(단건)
	 * <br> - 조직 정보를 등록한다.
	 * @param param - Map
	 * @return 
	 * @exception Exception
	 */
	@SuppressWarnings("rawtypes")
	public Map insertAdm7000DeptInfo(Map paramMap) throws Exception;
	
	
	/**
	 * Adm7000 조직 정보 수정(단건)
	 * <br> - 조직 정보를 수정한다.
	 * @param param - Map
	 * @return 
	 * @exception Exception
	 */
	@SuppressWarnings("rawtypes")
	public void updateAdm7000DpteInfo(Map paramMap) throws Exception;
	
	
	/**
	 * Adm7000 조직 정보 삭제(단건)
	 * <br> - 조직 정보를 삭제한다.
	 * @param param - Map
	 * @return 
	 * @exception Exception
	 */
	@SuppressWarnings("rawtypes")
	public void deleteAdm7000DeptInfo(Map paramMap) throws Exception;
	
	
	/**
	 * Adm7000 상위조직 조회 (list)
	 * <br> - 선택한 조직의 상위 조직명을 조회한다.
	 * @param param - Map
	 * @return List - 조직 목록
	 * @exception Exception
	 */
	@SuppressWarnings("rawtypes")
	public List selectAdm7000UpperDeptList(Map paramMap) throws Exception;
	
	
	/**
	 * Adm7000 해당 조직ID가 있는지 체크 
	 * @param param - Map
	 * @return 
	 * @exception Exception
	 */
	@SuppressWarnings("rawtypes")
	int selectAdm7000ExistDeptChk(Map paramMap) throws Exception;
	
	/**
	 * Adm7000 조직 목록 엑셀 다운로드
	 * @param param - Map
	 * @param resultHandler ExcelDataListResultHandler
	 * @return 
	 * @exception Exception
	 */
	@SuppressWarnings("rawtypes")
	void selectAdm7000ExcelList(Map paramMap, ExcelDataListResultHandler resultHandler) throws Exception;
}
