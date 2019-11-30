package kr.opensoftlab.oslops.prj.prj3000.prj3000.service.impl;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import kr.opensoftlab.oslops.prj.prj3000.prj3000.service.Prj3000Service;

import org.springframework.stereotype.Service;

import egovframework.com.cmm.EgovMessageSource;
import egovframework.com.cmm.service.EgovFileMngUtil;
import egovframework.com.cmm.service.FileVO;
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
	 * Prj3000 개발문서 삭제 AJAX
	 * 선택한 개발문서 및 하위 개발문서 삭제
	 * @param 
	 * @return 
	 * @exception Exception
	 */
	@SuppressWarnings({"rawtypes", "unchecked"})
	public void deletePrj3000MenuInfo(Map paramMap) throws Exception{
		
		// 삭제할 개발문서 List
		List<Map<String,String>> list = (List<Map<String,String>>) paramMap.get("list");
		// 라이선스 그룹 Id와 프로젝트 Id를 가져온다.
		String licGrpId = (String)paramMap.get("licGrpId");
		String prjId = (String)paramMap.get("prjId");
		
		// 첨부파일 Id List
		List<String> atchFileIdList = new ArrayList<String>();
		
		// 개발문서 List loop
		for(Map docMap : list) {

			// 라이선스 그룹 Id와 프로젝트 Id를 추가한다.
			docMap.put("licGrpId", licGrpId);
			docMap.put("prjId", prjId);
			
			// 산출물 삭제
			prj3000DAO.deletePrj3000MenuInfo(docMap);
			
			// 산출물 첨부파일 Id
			String docAtchFileId = (String)docMap.get("docAtchFileId");
			// 확정 산출물 첨부파일 Id
			String docFormFileId = (String)docMap.get("docFormFileId");
			
			// 산출물 첨부파일 Id 존재시 추가
			if(docAtchFileId != null && !"".equals(docAtchFileId)){
				atchFileIdList.add(docAtchFileId);
			}
			
			// 확정 산출물 첨부파일 Id 존재시 추가
			if(docFormFileId != null && !"".equals(docFormFileId)){
				atchFileIdList.add(docFormFileId);
			}
		}
		
		// 삭제할 첨부파일 list
		List<FileVO> delFileList = new ArrayList<FileVO>();
		
		// atchFileID 로 생성된 파일 목록 조회
		if(atchFileIdList.size() > 0){
			
			for(String atchFileIdStr : atchFileIdList){
				FileVO fileVo = new FileVO();
				fileVo.setAtchFileId(atchFileIdStr);
				
				// 파일목록 조회
				List<FileVO> selFileList = fileMngDAO.selectFileInfs(fileVo);
				delFileList.addAll(selFileList);
			}
		}
		
		// 파일 목록이 존재할때
		if(delFileList.size() > 0){
			// DB에서 파일정보 삭제
			for(FileVO delFileInfo : delFileList){
				// DB 파일정보 테이블에서 삭제
				fileMngDAO.deleteAllFileInf(delFileInfo);
				fileMngDAO.deleteFileInf(delFileInfo);
			}
			
			// 파일 물리적삭제
			for(FileVO delFileInfo : delFileList){
				try{
					//파일 물리적 삭제
					String fileDeletePath  = delFileInfo.getFileStreCours()+delFileInfo.getStreFileNm();
				    EgovFileMngUtil.deleteFile(fileDeletePath);
				}catch(Exception fileE){	
					//물리적 파일 삭제 오류시 skip
					continue;
				}
			}
		}
	}
	
	/**
	 * Prj3000 개발문서 수정(단건) AJAX
	 * 선택한 개발문서 단건 수정
	 * @param 
	 * @return 
	 * @exception Exception
	 */
	@SuppressWarnings("rawtypes")
	public void updatePrj3000MenuInfo(Map paramMap) throws Exception{
		//개발문서 수정
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

	/**
	 * Prj3000 ROOTSYSTEM_PRJ 산출물 메뉴 정보 가져오기
	 * @param param - Map
	 * @return 
	 * @exception Exception
	 */
	@SuppressWarnings("rawtypes")
	public List selectPrj3000RootMenuList(Map paramMap) throws Exception{
		return prj3000DAO.selectPrj3000RootMenuList(paramMap);
	}

	/**
	 * Prj3000 [프로젝트 마법사] 단순 산출물 정보 가져오기
	 * @param param - Map
	 * @return 
	 * @exception Exception
	 */
	@SuppressWarnings("rawtypes")
	public Map selectPrj3000WizardMenuInfo(Map paramMap) throws Exception{
		return prj3000DAO.selectPrj3000WizardMenuInfo(paramMap);
	}
}
