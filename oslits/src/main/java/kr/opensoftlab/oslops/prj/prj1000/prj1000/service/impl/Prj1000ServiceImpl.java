package kr.opensoftlab.oslops.prj.prj1000.prj1000.service.impl;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import kr.opensoftlab.oslops.adm.adm1000.adm1000.service.impl.Adm1000DAO;
import kr.opensoftlab.oslops.com.fms.web.service.FileMngService;
import kr.opensoftlab.oslops.prj.prj1000.prj1000.service.Prj1000Service;
import kr.opensoftlab.oslops.prj.prj1000.prj1100.service.Prj1100Service;
import kr.opensoftlab.oslops.prj.prj1000.prj1100.service.impl.Prj1100DAO;
import kr.opensoftlab.oslops.prj.prj2000.prj2000.service.impl.Prj2000DAO;
import kr.opensoftlab.oslops.prj.prj3000.prj3000.service.impl.Prj3000DAO;
import kr.opensoftlab.oslops.req.req4000.req4000.service.impl.Req4000DAO;
import kr.opensoftlab.oslops.req.req4000.req4100.service.impl.Req4100DAO;
import kr.opensoftlab.oslops.req.req4000.req4100.vo.Req4100VO;

import org.codehaus.jettison.json.JSONArray;
import org.codehaus.jettison.json.JSONObject;
import org.springframework.stereotype.Service;

import com.google.gson.Gson;

import egovframework.com.cmm.service.EgovFileMngUtil;
import egovframework.com.cmm.service.FileVO;
import egovframework.com.cmm.service.impl.FileManageDAO;
import egovframework.rte.fdl.cmmn.EgovAbstractServiceImpl;
import egovframework.rte.fdl.idgnr.EgovIdGnrService;


/**
 * @Class Name : Prj1000ServiceImpl.java
 * @Description : Prj1000ServiceImpl Business Implement class
 * @Modification Information
 *
 * @author 강재민
 * @since 2016.01.23.
 * @version 1.0
 * @see
 *  
 *  Copyright (C) OpenSoftLab Corp. All right reserved.
 */
@Service("prj1000Service")
public class Prj1000ServiceImpl extends EgovAbstractServiceImpl implements Prj1000Service {
	/** Req4100DAO DI */
    @Resource(name="req4100DAO")
    private Req4100DAO req4100DAO;
    
    /** Req4000DAO DI */
    @Resource(name="req4000DAO")
    private Req4000DAO req4000DAO;

	/** Prj1000DAO DI */
    @Resource(name="prj1000DAO")
    private Prj1000DAO prj1000DAO;

    /** Prj1100DAO DI */
    @Resource(name="prj1100DAO")
    private Prj1100DAO prj1100DAO;
    
	/** Prj3000DAO DI */
    @Resource(name="prj3000DAO")
    private Prj3000DAO prj3000DAO;

	/** Prj2000DAO DI */
    @Resource(name="prj2000DAO")
    private Prj2000DAO prj2000DAO;
    
	/** Adm1000DAO DI */
    @Resource(name="adm1000DAO")
    private Adm1000DAO adm1000DAO;

	/** FileMngService */
   	@Resource(name="fileMngService")
   	private FileMngService fileMngService;
   	
	/** Prj1100Service DI */
    @Resource(name = "prj1100Service")
    private Prj1100Service prj1100Service;
    
	@Resource(name = "FileManageDAO")
	private FileManageDAO fileMngDAO;
	

   	@Resource(name = "egovFileIdGnrService")
	private EgovIdGnrService idgenService;
    /**
   	 * Prj1000 프로젝트 생성관리 목록 조회
   	 * @param param - Map
   	 * @return 
   	 * @exception Exception
   	 */
   	@SuppressWarnings("rawtypes")
   	public List selectPrj1000View(Map paramMap) throws Exception {
   		return prj1000DAO.selectPrj1000View(paramMap) ;
   	}
   	
   	
    /**
   	 * Prj1000 프로젝트 단건 조회
   	 * @param param - Map
   	 * @return 
   	 * @exception Exception
   	 */
   	@SuppressWarnings("rawtypes")
   	public Map selectPrj1000Info(Map paramMap) throws Exception {
   		return prj1000DAO.selectPrj1000Info(paramMap) ;
   	}
   	
   	/**
	 * Prj1000 프로젝트 그룹 존재하는지 체크
	 * @param param - Map
	 * @return 
	 * @exception Exception
	 */
	@SuppressWarnings("rawtypes")
	public List selectPrj1000PrjGrpExistCheck(Map paramMap) throws Exception {
		return prj1000DAO.selectPrj1000PrjGrpExistCheck(paramMap);
	}
   	
	/**
	 * Prj1000 프로젝트 그룹 ID에 해당하는 프로젝트가 존재하는지 체크
	 * @param param - Map
	 * @return 
	 * @exception Exception
	 */
	@SuppressWarnings("rawtypes")
	public List selectPrj1000PrjGrpIdExistCheck(Map paramMap) throws Exception {
		return prj1000DAO.selectPrj1000PrjGrpIdExistCheck(paramMap);
	}
	
   	/**
   	 * Prj1000 옵션정보 페이지에 접속 권한이 있는 프로젝트 목록 가져오기
   	 * @param param - Map
   	 * @return 
   	 * @exception Exception
   	 */
   	@SuppressWarnings("rawtypes")
   	public List selectPrj1000AuthGrpAndMenuIdPrjListAjax(Map paramMap) throws Exception {
   		return prj1000DAO.selectPrj1000AuthGrpAndMenuIdPrjListAjax(paramMap) ;
   	}
   	
   	/**
	 * Prj1000 프로젝트 그룹 생성(insert) AJAX
	 * @param Map
	 * @exception Exception
	 */
	@SuppressWarnings("rawtypes")
	public String insertPrj1000PrjGrpAjax(Map paramMap) throws Exception{
		return (String) prj1000DAO.insertPrj1000PrjGrpAjax(paramMap);
	}
   	
   	/**
	 * Prj1000 프로젝트 생성관리 등록(insert) AJAX
	 * @param Map
	 * @return 프로젝트 ID
	 * @exception Exception
	 */
	@SuppressWarnings({ "rawtypes" })
	public String insertPrj1001Ajax(Map paramMap) throws Exception{
		//프로젝트 생성관리 등록
		String prjId = "";
		
		Map rtnMap = (Map) prj1000DAO.insertPrj1001Ajax(paramMap);
		
		prjId = (String) rtnMap.get("newPrjId");
		return prjId;
	}
	
	
	
	/**
	 * Prj1000 프로젝트 생성관리 수정(update) AJAX
	 * @param Map
	 * @return update row
	 * @exception Exception
	 */
	@SuppressWarnings({ "rawtypes" })
	public int updatePrj1000Ajax(Map paramMap) throws Exception{
		//프로젝트 생성관리 수정
		return prj1000DAO.updatePrj1000Ajax(paramMap);
	}
	
	/**
	 * Prj1000 프로젝트 그룹 제거(delete) AJAX
	 * @param Map
	 * @return delete row
	 * @exception Exception
	 */
	@SuppressWarnings("rawtypes")
	public void deletePrj1000PrjGrpAjax(Map paramMap) throws Exception{
		prj1000DAO.deletePrj1000PrjGrpAjax(paramMap);
	}
	
	/**
	 * Prj1000 프로젝트 생성관리 삭제(delete) AJAX
	 * @param Map
	 * @return delete row
	 * @exception Exception
	 */
	@SuppressWarnings({ "rawtypes", "unchecked"})
	public void deletePrj1001Ajax(Map paramMap) throws Exception{
		//ATCH_FILE_ID 항목이있는 테이블 전체 조회		
		//요구사항 전체 목록 조회 VO
		Req4100VO req4100Vo = new Req4100VO();
		req4100Vo.setSelPrjId((String)paramMap.get("prjId"));

		//요구사항 목록 불러오기
		List<Map> prjReqList = req4100DAO.selectReq4100AllList(req4100Vo);
		
		//산출물 목록 불러오기
		List<Map> prjDocList = prj3000DAO.selectPrj3000BaseMenuList(paramMap);
		
		//추가항목 + 값 가져오기
		List<Map> prjFlwOptList = prj1100DAO.selectFlw1200FlwOptExistFileIdList(paramMap);
		
		prj1000DAO.updatePrj1000UserProjectId(paramMap);
		//프로젝트 생성관리 삭제
		prj1000DAO.deletePrj1001Ajax(paramMap);
		
		//fileAtchId 정리
		List<String> atchFileIdList = new ArrayList<String>();
		
		//요구사항 목록에서 FILE_ATCH_ID 가져오기
		if(prjReqList.size() > 0){
			for(Map prjReqInfo : prjReqList){
				String atchFileId = (String) prjReqInfo.get("atchFileId");
				
				//fileId있는경우 목록 추가
				if(atchFileId != null && !"".equals(atchFileId)){
					atchFileIdList.add(atchFileId);
				}
			}
		}
		
		//산출물 목록에서 DOC_FORM_FILE_ID, DOC_ATCH_FILE_ID 가져오기
		if(prjDocList.size() > 0){
			for(Map prjDocInfo : prjDocList){
				//양식 fileId
				String docFormFileId = (String) prjDocInfo.get("docFormFileId");
				
				//산출물 fileId
				String docAtchFileId = (String) prjDocInfo.get("docAtchFileId");
				
				//fileId있는경우 목록 추가
				if(docFormFileId != null && !"".equals(docFormFileId)){
					atchFileIdList.add(docFormFileId);
				}
				
				//fileId있는경우 목록 추가
				if(docAtchFileId != null && !"".equals(docAtchFileId)){
					atchFileIdList.add(docAtchFileId);
				}
			}
		}
		
		//추가항목 + 값 목록에서 ITEM_VALUE 가져오기
		if(prjFlwOptList.size() > 0){
			for(Map prjFlwOptInfo : prjFlwOptList){
				String itemValue = (String) prjFlwOptInfo.get("itemValue");
				
				//fileId있는경우 목록 추가
				if(itemValue != null && !"".equals(itemValue)){
					atchFileIdList.add(itemValue);
				}
			}
		}
		
		List<FileVO> delFileList = new ArrayList<FileVO>();
		
		//atchFileID로 생성된 파일 목록 조회
		if(atchFileIdList.size() > 0){
			for(String atchFileIdStr : atchFileIdList){
				FileVO fileVo = new FileVO();
				fileVo.setAtchFileId(atchFileIdStr);
				
				List<FileVO> selFileList = fileMngDAO.selectFileInfs(fileVo);
				delFileList.addAll(selFileList);
			}
		}
		
		//파일 목록
		if(delFileList.size() > 0){
			//파일 DB삭제
			for(FileVO delFileInfo : delFileList){
				//파일 DB 삭제
				fileMngDAO.deleteFileInf(delFileInfo);
			}
			//파일 물리적삭제
			for(FileVO delFileInfo : delFileList){
				try{
					//파일 물리적 삭제
					String fileDeletePath  = delFileInfo.getFileStreCours()+delFileInfo.getStreFileNm();
				    EgovFileMngUtil.deleteFile(fileDeletePath);
				}catch(Exception fileE){	//물리적 파일 삭제 오류시 skip
					continue;
				}
			}
		}
	}


	@SuppressWarnings("rawtypes")
	@Override
	public List<Map> selectPrj1000ProjectGroupListAjax(
			Map<String, String> paramMap) {

		return prj1000DAO.selectPrj1000ProjectGroupListAjax(paramMap) ;
	}
	
	@SuppressWarnings("rawtypes")
	@Override
	public int selectPrj1000ProjectAcronymCount(Map paramMap) throws Exception{
		return prj1000DAO.selectPrj1000ProjectAcronymCount(paramMap);
	}
	

	/**
	 * Prj1000 관리 권한 있는 프로젝트 목록 검색
	 * @param param - Map
	 * @return 
	 * @exception Exception
	 */
	@SuppressWarnings("rawtypes")
	public List selectPrj1000AdminPrjList(Map paramMap) throws Exception {
		return prj1000DAO.selectPrj1000AdminPrjList(paramMap);
	}
	
	/**
	 * Prj1000 프로젝트 생성 마법사
	 * @param param - Map
	 * @return 
	 * @exception Exception
	 */
	@SuppressWarnings({ "rawtypes", "unchecked" })
	public String insertPrj1000WizardProject(Map paramMap) throws Exception {
		/****************************
		******** 기본 정보 세팅
		****************************/
		String licGrpId = (String) paramMap.get("licGrpId");
		String regUsrId = (String) paramMap.get("regUsrId");
		String regUsrIp = (String) paramMap.get("regUsrIp");
		String modifyUsrId = (String) paramMap.get("modifyUsrId");
		String modifyUsrIp = (String) paramMap.get("modifyUsrIp");
		
		Map regModiMap = new HashMap();
		regModiMap.put("licGrpId", licGrpId);
		regModiMap.put("regUsrId", regUsrId);
		regModiMap.put("regUsrIp", regUsrIp);
		regModiMap.put("modifyUsrId", modifyUsrId);
		regModiMap.put("modifyUsrIp", modifyUsrIp);
		
		
		/****************************
		******** 마법사 데이터 세팅
		****************************/
		String wizardData = (String) paramMap.get("wizardData");
		
		JSONObject wizardJsonData = new JSONObject(wizardData);
		
		
		/****************************
		******** 프로젝트 생성
		****************************/
		JSONObject projectJson = wizardJsonData.getJSONObject("project");
		
		//프로젝트 데이터
		Map projectMapData = new Gson().fromJson(projectJson.toString(), HashMap.class);
		
		//정보 입력
		projectMapData.putAll(regModiMap);
		
		
		//프로젝트 생성 DAO
		String newPrjId = prj1000DAO.insertPrj1000PrjWizardAjax(projectMapData);
		
		/****************************
		******** 프로세스 생성 
		****************************/
		
		//프로세스 있는지 체크
		if(wizardJsonData.has("process")){
			JSONObject processJson = wizardJsonData.getJSONObject("process");
			
			//선택 프로세스 추가 (복사 service 사용)
			Iterator<String> keys = processJson.keys();
			while(keys.hasNext()) {
			    String key = keys.next();
			    
			    //json to map
			    Map processMapData = new Gson().fromJson(processJson.get(key).toString(), HashMap.class);
			    
			    //selPrjId넣기 - 생성된 prjId
			    processMapData.put("selPrjId", newPrjId);
			    processMapData.putAll(regModiMap);
				
			    //프로세스 복사 Service
			    prj1100Service.insertPrj1100ProcessCopyInfo(processMapData);
			}
		}
		
		/****************************
		******** 역할그룹 생성
		****************************/
		//역할그룹 생성
		
		JSONArray authGrpArr = (JSONArray) wizardJsonData.get("authGrp");
		
		for(int i=0;i<authGrpArr.length();i++){
			JSONObject authGrpInfo = authGrpArr.getJSONObject(i);
			
			//json to map
		    Map authGrpMapData = new Gson().fromJson(authGrpInfo.toString(), HashMap.class);
		    authGrpMapData.put("licGrpId", licGrpId);
		    
		    //권한그룹 조회 - adm1100
		    Map prjAuthGrpInfo = adm1000DAO.selectAdm1000AuthGrpInfoAjax(authGrpMapData);
		    
		    //프로젝트 Id 교체
		    prjAuthGrpInfo.remove("prjId");
		    prjAuthGrpInfo.put("prjId", newPrjId);
		    prjAuthGrpInfo.put("selPrjId", newPrjId);
		    prjAuthGrpInfo.put("authGrpUseCd", prjAuthGrpInfo.get("useCd"));
		    prjAuthGrpInfo.put("authGrpOrd", prjAuthGrpInfo.get("ord"));
		    prjAuthGrpInfo.putAll(regModiMap);
		    
		    //권한그룹 추가
		    String newAuthGrpId = prj2000DAO.insertPrj2000AuthGrpInfoAjax(prjAuthGrpInfo);
		    
		    //메뉴별 접근권한 가져오기 - adm1000
		    List<Map> prjAuthGrpSystemList = adm1000DAO.selectAdm1000AuthGrpSmallMenuList(authGrpMapData);
		    
		    
		    //권한그룹에 접근권한 추가
		    for(Map prjAuthGrpSystemInfo : prjAuthGrpSystemList){
		    	prjAuthGrpSystemInfo.put("prjId",newPrjId);
		    	prjAuthGrpSystemInfo.put("authGrpId",newAuthGrpId);
		    	prjAuthGrpSystemInfo.putAll(regModiMap);
		    	
		    	//추가
		    	adm1000DAO.saveAdm1000AuthGrpMenuAuthListAjax(prjAuthGrpSystemInfo);
		    }
		    //사용자 배정
		    Map newMap = new HashMap<>();
		    newMap.put("prjId",newPrjId);
		    newMap.put("authGrpId",newAuthGrpId);
		    newMap.putAll(regModiMap);
		    newMap.put("usrId", regUsrId);
		    
		    //사용자 배정 추가
		    prj2000DAO.insertPrj2000PrjUsrAuthListAjax(newMap);
		}
		
		/****************************
		******** 개발문서 양식
		****************************/
		String docPrjId = wizardJsonData.getString("document");

		//Map Setting
		Map newMap = new HashMap<>();
		newMap.put("prjId",docPrjId);
		newMap.putAll(regModiMap);
		
		//개발문서 양식 메뉴 정보 불러오기
		List<Map> prjDocList = null;
		
		//rootsystem인지 체크
		if("ROOTSYSTEM_PRJ".equals(docPrjId)){
			 prjDocList = prj3000DAO.selectPrj3000RootMenuList(newMap);
		}else{
			prjDocList = prj3000DAO.selectPrj3000BaseMenuList(newMap);
		}
		
		//산출물 정보가 있는경우
		if(prjDocList != null && prjDocList.size() > 0){
			for(Map prjDocInfo : prjDocList){
				//atch_file_id 강제 생성
				String docFormFileId = idgenService.getNextStringId();
				fileMngService.insertFileMasterInfo(docFormFileId);
				//atch_file_id 강제 생성
				String docAtchFileId = idgenService.getNextStringId();
				fileMngService.insertFileMasterInfo(docAtchFileId);
				
				//데이터 교체
				prjDocInfo.remove("prjId");
				prjDocInfo.remove("docFormFileId");
				prjDocInfo.remove("docAtchFileId");
				prjDocInfo.remove("regUsrId");
				prjDocInfo.remove("regUsrIp");
				prjDocInfo.remove("modifyUsrId");
				prjDocInfo.remove("modifyUsrIp");
				
				prjDocInfo.put("prjId",newPrjId);
				prjDocInfo.put("docFormFileId",docFormFileId);
				prjDocInfo.put("docAtchFileId",docAtchFileId);
				
				prjDocInfo.putAll(regModiMap);
				
				//산출물 추가
				prj3000DAO.insertPrj3000ParamMenuInfo(prjDocInfo);
			}
		}
		
		
		
		/****************************
		******** 요구사항 분류
		****************************/
		if(wizardJsonData.has("class")){
			String clsPrjId = wizardJsonData.getString("class");
			
			//Map Setting
			Map clsNewMap = new HashMap<>();
			clsNewMap.put("selPrjId",clsPrjId);
			
			//분류 목록
			List<Map> clsList = req4000DAO.selectReq4000ReqClsList(clsNewMap);
			
			//분류 루프
			if(clsList != null && clsList.size() > 0){
				for(Map clsInfo : clsList){
					//데이터 교체
					clsInfo.remove("regUsrId");
					clsInfo.remove("regUsrIp");
					clsInfo.remove("modifyUsrId");
					clsInfo.remove("modifyUsrIp");
					clsInfo.remove("prjId");
					
					clsInfo.put("prjId",newPrjId);
					clsInfo.putAll(regModiMap);
					
					req4000DAO.insertReq4000WizardReqClsInfo(clsInfo);
				}
			}
		}
		//class 없는경우 요구사항 분류 '없음'으로 프로젝트 이름으로 ROOT를 생성해준다.
		else{
			//프로젝트 명
			String prjNm = (String)projectJson.getString("wizard_prjNm");
			
			//Map Setting
			Map clsNewMap = new HashMap<>();
			clsNewMap.put("selPrjId",newPrjId);
			clsNewMap.put("newReqClsNm",prjNm);
			clsNewMap.put("lvl",-1);
			clsNewMap.put("ord",0);
			clsNewMap.put("licGrpId", licGrpId);
			clsNewMap.put("regUsrId", regUsrId);
			clsNewMap.put("regUsrIp", regUsrIp);
			clsNewMap.put("modifyUsrId", modifyUsrId);
			clsNewMap.put("modifyUsrIp", modifyUsrIp);
			
			req4000DAO.insertReq4000ReqClsInfo(clsNewMap);
			
		}
		
		return newPrjId;
	}
}








