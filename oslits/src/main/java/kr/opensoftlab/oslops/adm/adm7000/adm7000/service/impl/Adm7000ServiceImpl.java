package kr.opensoftlab.oslops.adm.adm7000.adm7000.service.impl;

import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import kr.opensoftlab.oslops.adm.adm7000.adm7000.service.Adm7000Service;
import kr.opensoftlab.sdf.excel.ExcelDataListResultHandler;

import org.springframework.stereotype.Service;

import egovframework.com.cmm.EgovMessageSource;
import egovframework.rte.fdl.cmmn.EgovAbstractServiceImpl;

/**
 * @Class Name : Adm7000ServiceImpl.java
 * @Description : 조직 관리(Adm7000) 서비스 클래스
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
@Service("adm7000Service")
public class Adm7000ServiceImpl extends EgovAbstractServiceImpl implements Adm7000Service {

	/** Adm7000DAO DI */
    @Resource(name="adm7000DAO")
    private Adm7000DAO adm7000DAO;
    
    /** EgovMessageSource */
	@Resource(name = "egovMessageSource")
	EgovMessageSource egovMessageSource;

	
	/**
	 * Adm7000 등록된 조직 목록 조회 (List)
	 * <br> - 조직 목록을 조회한다.
	 * @param param - Map
	 * @return List - 조직 목록
	 * @exception Exception
	 */
	@SuppressWarnings("rawtypes")
	@Override
	public List selectAdm7000DeptList(Map paramMap) throws Exception {
		return adm7000DAO.selectAdm7000DeptList(paramMap);
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
		return adm7000DAO.selectAdm7000DeptInfo(paramMap);
	}

	
	/**
	 * Adm7000 조직 루트 디렉토리 생성
	 * <br> - 등록된 조직이 없을경우 최상위 조직(ROOT)을 생성한다.
	 * @param param - Map
	 * @return 
	 * @exception Exception
	 */
	@SuppressWarnings("rawtypes")
	@Override
	public String insertAdm7000RootDeptInfo(Map paramMap) throws Exception {
		return adm7000DAO.insertAdm7000RootDeptInfo(paramMap);
	}

	
	/**
	 * Adm7000 조직 정보 등록(단건)
	 * <br> - 조직 정보를 등록한다.
	 * @param param - Map
	 * @return 
	 * @exception Exception
	 */
	@SuppressWarnings({ "rawtypes", "unchecked" })
	@Override
	public Map insertAdm7000DeptInfo(Map paramMap) throws Exception {
	
		//상위 조직 정보를 이용해 하위 조직기본정보 등록
		String insDeptId = adm7000DAO.insertAdm7000DeptInfo(paramMap);
		
		// 생성된 ID가 없으면 튕겨낸다.
		if(insDeptId == null || "".equals(insDeptId)){
			throw new Exception(egovMessageSource.getMessage("req4000.notFoundUpperMenu.fail"));
		}
		
		//생성된 deptId를 이용해 새로 등록한 조직 정보 조회
		paramMap.put("deptId", insDeptId);
		
		Map newMap = adm7000DAO.selectAdm7000DeptInfo(paramMap);
		
		return newMap;
	}


	/**
	 * Adm7000 조직 정보 수정(단건)
	 * <br> - 조직 정보를 수정한다.
	 * @param param - Map
	 * @return 
	 * @exception Exception
	 */
	@SuppressWarnings("rawtypes")
	@Override
	public void updateAdm7000DpteInfo(Map paramMap) throws Exception {
		
		int updateCnt = adm7000DAO.updateAdm7000DpteInfo(paramMap);
		
		// 수정된 건이 없으면 튕겨낸다.
		if(updateCnt == 0){
			throw new Exception(egovMessageSource.getMessage("fail.common.update"));
		}
	}


	/**
	 * Adm7000 조직 정보 삭제(단건)
	 * <br> - 조직 정보를 삭제한다.
	 * @param param - Map
	 * @return 
	 * @exception Exception
	 */
	@SuppressWarnings("rawtypes")
	@Override
	public void deleteAdm7000DeptInfo(Map paramMap) throws Exception {
		
		int deleteCnt = adm7000DAO.deleteAdm7000DeptInfo(paramMap);
		
		//삭제된 건이 없으면 튕겨냄
		if(deleteCnt == 0 ){
			throw new Exception(egovMessageSource.getMessage("fail.common.delete"));
		}
	}

	
	/**
	 * Adm7000 상위조직 조회 (list)
	 * <br> - 선택한 조직의 상위 조직명을 조회한다.
	 * @param param - Map
	 * @return List - 조직 목록
	 * @exception Exception
	 */
	@SuppressWarnings("rawtypes")
	@Override
	public List selectAdm7000UpperDeptList(Map paramMap) throws Exception {
		return adm7000DAO.selectAdm7000UpperDeptList(paramMap);
	}

	/**
	 * Adm7000 해당 조직ID가 있는지 체크 
	 * @param param - Map
	 * @return 
	 * @exception Exception
	 */
	@SuppressWarnings("rawtypes")
	@Override
	public int selectAdm7000ExistDeptChk(Map paramMap) throws Exception {
		return adm7000DAO.selectAdm7000ExistDeptChk(paramMap);
	}

	/**
	 * Adm7000 조직 목록 엑셀 다운로드
	 * @param param - Map
	 * @param resultHandler ExcelDataListResultHandler
	 * @return 
	 * @exception Exception
	 */
	@SuppressWarnings("rawtypes")
	@Override
	public void selectAdm7000ExcelList(Map paramMap, ExcelDataListResultHandler resultHandler) throws Exception {
		adm7000DAO.selectAdm7000ExcelList(paramMap,resultHandler);
	}

}
