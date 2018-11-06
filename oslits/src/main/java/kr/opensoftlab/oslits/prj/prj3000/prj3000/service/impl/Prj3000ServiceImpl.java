package kr.opensoftlab.oslits.prj.prj3000.prj3000.service.impl;

import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import kr.opensoftlab.oslits.prj.prj3000.prj3000.service.Prj3000Service;

import org.springframework.stereotype.Service;

import egovframework.com.cmm.EgovMessageSource;
import egovframework.com.cmm.service.impl.FileManageDAO;
import egovframework.rte.fdl.cmmn.EgovAbstractServiceImpl;

/**
 * @Class Name : Prj3000ServiceImpl.java
 * @Description : Prj3000ServiceImpl Business Implement class
 * @Modification Information
 *
 * @author 진주영
 * @since 2016.03.23.
 * @version 1.0
 * @see
 *  
 *  Copyright (C) OpenSoftLab Corp. All right reserved.
 */

@Service("prj3000Service")
public class Prj3000ServiceImpl extends EgovAbstractServiceImpl implements Prj3000Service {

	/** Prj3000DAO DI */
    @Resource(name="prj3000DAO")
    private Prj3000DAO prj3000DAO;
    
    @Resource(name = "FileManageDAO")
	private FileManageDAO fileMngDAO;
    
    /** EgovMessageSource */
	@Resource(name = "egovMessageSource")
	EgovMessageSource egovMessageSource;
	/**
	 * Prj3000 라이선스 그룹에 할당된 기본 메뉴정보 목록 가져오기
	 * @param param - Map
	 * @return 
	 * @exception Exception
	 */
	@SuppressWarnings("rawtypes")
	public List selectPrj3000BaseMenuList(Map paramMap) throws Exception{
		return prj3000DAO.selectPrj3000BaseMenuList(paramMap);
	}

	/**
	 * Prj3000 메뉴 정보 조회
	 * @param param - Map
	 * @return 
	 * @exception Exception
	 */
	@SuppressWarnings("rawtypes")
	public Map selectPrj3000MenuInfo(Map paramMap) throws Exception{
		return prj3000DAO.selectPrj3000MenuInfo(paramMap);
	}
	
	/**
	 * Prj3000 메뉴정보 등록(단건) AJAX
	 * 메뉴정보 등록후 등록 정보 결과 및 정보를 리턴한다.
	 * @param 
	 * @return 
	 * @exception Exception
	 */
	@SuppressWarnings({ "rawtypes", "unchecked" })
	public Map insertPrj3000MenuInfo(Map paramMap) throws Exception{
		Map map = prj3000DAO.selectPrj3000MenuInfo(paramMap);
		
		/* 입력자 정보 삽입 */
		map.put("regUsrId", paramMap.get("regUsrId"));
		map.put("modifyUsrId", paramMap.get("modifyUsrId"));
		map.put("regUsrIp", paramMap.get("regUsrIp"));
		map.put("modifyUsrIp", paramMap.get("modifyUsrIp"));

		// 신규로 메뉴가 등록될 경우 확정산출물 없으므로 0 세팅
		map.put("docFormFileSn", 0);
		
		//3depth 메뉴인경우 최초 파일 id 생성
		int lvl = Integer.parseInt(map.get("lvl").toString());

		map.put("docFormFileId", paramMap.get("fileFormId"));
		fileMngDAO.insertFileMasterInfo((String) paramMap.get("fileFormId"));
			
		map.put("docAtchFileId", paramMap.get("fileAtchId"));
		fileMngDAO.insertFileMasterInfo((String) paramMap.get("fileAtchId"));
		
		//상위메뉴정보를 이용해 하위 메뉴 기본정보 등록
		String insDocId = prj3000DAO.insertPrj3000MenuInfo(map);
		
		//생성된 키가 없으면 튕겨냄
		if(insDocId == null || "".equals(insDocId)){
			throw new Exception(egovMessageSource.getMessage("prj3000.fail.menu.insert"));
		}
		
		
		//생성된 menuId를 이용해 새로 등록한 메뉴 정보 조회
		map.put("docId", insDocId);
		
		Map newMap = prj3000DAO.selectPrj3000MenuInfo(map);
		
		return newMap;
	}
	
	/**
	 * 산출물 메뉴 루트 디렉토리 생성
	 * @param 
	 * @return 
	 * @exception Exception
	 */
	@SuppressWarnings("rawtypes")
	public String insertPrj3000RootMenuInfo(Map paramMap) throws Exception{
		return prj3000DAO.insertPrj3000RootMenuInfo(paramMap);
	}
	
	/**
	 * Prj3000 메뉴정보 삭제(단건) AJAX
	 * 메뉴정보 삭제
	 * @param 
	 * @return 
	 * @exception Exception
	 */
	@SuppressWarnings("rawtypes")
	public void deletePrj3000MenuInfo(Map paramMap) throws Exception{
		//메뉴정보 삭제
				int delCnt = prj3000DAO.deletePrj3000MenuInfo(paramMap);
				
				//삭제된 건이 없으면 튕겨냄
				if(delCnt == 0 ){
					throw new Exception(egovMessageSource.getMessage("fail.common.delete"));
				}
	}
	
	/**
	 * Prj3000 메뉴정보 수정(단건) AJAX
	 * 메뉴정보 수정
	 * @param 
	 * @return 
	 * @exception Exception
	 */
	@SuppressWarnings("rawtypes")
	public void updatePrj3000MenuInfo(Map paramMap) throws Exception{
		//메뉴정보 수정
				int upCnt = prj3000DAO.updatePrj3000MenuInfo(paramMap);
				
				//수정된 건이 없으면 튕겨냄
				if(upCnt == 0 ){
					throw new Exception(egovMessageSource.getMessage("fail.common.update"));
				}
	}
	
	/**
	 * Prj3000 선택한 산출물 확정 처리  AJAX
	 * 선택한 산출물 확정 처리 
	 * @param 
	 * @return 
	 * @exception Exception
	 */
	@SuppressWarnings("rawtypes")
	public void updatePrj3000FileSnSelect(Map paramMap) throws Exception{
		int upCnt = prj3000DAO.updatePrj3000FileSnSelect(paramMap);
		
		//수정된 건이 없으면 튕겨냄
		if(upCnt == 0 ){
			throw new Exception(egovMessageSource.getMessage("fail.common.update"));
		}
	}
	
	/**
	 * Prj3000 프로젝트에 할당된 선택 하위 메뉴 불러오기(첨부파일 압축 다운로드 사용)
	 * @param param - Map
	 * @return 
	 * @exception Exception
	 */
	@SuppressWarnings("rawtypes")
	public List selectPrj3000MenuTree(Map paramMap) throws Exception{
		return prj3000DAO.selectPrj3000MenuTree(paramMap);
	}
}
