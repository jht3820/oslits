package kr.opensoftlab.oslops.adm.adm4000.adm4000.service;

import java.util.List;
import java.util.Map;

import kr.opensoftlab.oslops.com.vo.LoginVO;
import kr.opensoftlab.sdf.excel.ExcelDataListResultHandler;

/**
 * @Class Name : Adm4000Service.java
 * @Description : Adm4000Service Business class
 * @Modification Information
 *
 * @author 정형택
 * @since 2016.01.31
 * @version 1.0
 * @see
 *  
 *  Copyright (C)  All right reserved.
 */

public interface Adm4000Service {

	/**
	 * Adm4000 공통코드 마스터 조회
	 * @param param - Map
	 * @return 
	 * @exception Exception
	 */
	@SuppressWarnings("rawtypes")
	List selectAdm4000CommonCodeMasterList(Map inputMap) throws Exception;
	
	/**
	 * Adm4000 공통코드 디테일 조회
	 * @param param - Map
	 * @return 
	 * @exception Exception
	 */
	@SuppressWarnings("rawtypes")
	List selectAdm4000CommonCodeDetailList(Map inputMap) throws Exception;
	
	/**
	 * Adm4000 공통코드 마스터 정보 저장
	 * @param param - Map
	 * @return 
	 * @exception Exception
	 */
	@SuppressWarnings("rawtypes")
	public void saveAdm4000CommonCodeMaster(Map paramMap) throws Exception;
	
	/**
	 * Adm4000 배포 버전 등록
	 * @param param - Map
	 * @return 
	 * @exception Exception
	 */
	@SuppressWarnings("rawtypes")
	public void updateAdm4000CommonCodeInfo(Map paramMap) throws Exception;

	
	/**
	 * Adm4000 배포 버전 리스트 삭제
	 * @param param - Map
	 * @return 
	 * @exception Exception
	 */
	@SuppressWarnings("rawtypes")
	public void deleteAdm4000CommonCodeInfoList(Map paramMap) throws Exception;

	
	/**
	 * Adm4100 공통코드 디테일 정보 저장
	 * @param param - Map
	 * @return 
	 * @exception Exception
	 */
	@SuppressWarnings("rawtypes")
	void saveAdm4000CommonCodeDetail(Map paramMap) throws Exception;

	/**
	 * Adm4100 공통코드 디테일 정보 삭제
	 * @param param - Map
	 * @return 
	 * @exception Exception
	 */
	@SuppressWarnings("rawtypes")
	void deleteAdm4000CommonCodeDetail(Map paramMap) throws Exception;

	/**
	 * Adm4000 공통코드 마스터 , Adm4100 공통코드 디테일 정보 삭제
	 * @param param - Map
	 * @return 
	 * @exception Exception
	 */
	void deleteAdm4000CommonCodeMaster(Map paramMap) throws Exception;
	
	/**
	 * 공통코드 마스터 목록을 엑셀 조회를 한다.
	 * @params LoginVO
	 * @return List 배포 버전 요구사항 목록
	 * @throws Exception
	 */
	void selectAdm4000MasterExcelList(LoginVO loginVO,ExcelDataListResultHandler resultHandler) throws Exception;
	
	/**
	 * 공통코드 상세 목록을 엑셀 조회를 한다.
	 * @params LoginVO
	 * @return List 배포 버전 요구사항 목록
	 * @throws Exception
	 */
	void selectAdm4000DetailExcelList(Map paramMap,ExcelDataListResultHandler resultHandler) throws Exception;
	
	public int selectAdm4000CommonCodeCount(Map paramMap) throws Exception;

	int selectAdm4000CommonDetailCodeCount(Map paramMap) throws Exception;

}
