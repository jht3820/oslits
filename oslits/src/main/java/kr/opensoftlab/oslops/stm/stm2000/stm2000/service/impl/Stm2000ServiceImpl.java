package kr.opensoftlab.oslops.stm.stm2000.stm2000.service.impl;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import kr.opensoftlab.oslops.stm.stm2000.stm2000.service.Stm2000Service;
import kr.opensoftlab.oslops.stm.stm2000.stm2000.vo.Stm2000VO;

import org.codehaus.jettison.json.JSONObject;
import org.springframework.stereotype.Service;

import egovframework.rte.fdl.cmmn.EgovAbstractServiceImpl;

/**
 * @Class Name : Stm2000ServiceImpl.java
 * @Description : Stm2000ServiceImpl Business Implement class
 * @Modification Information
 *
 * @author 공대영
 * @since 2018.07.02
 * @version 1.0
 * @see
 *  
 *  Copyright (C)  All right reserved.
 */
@Service("stm2000Service")
public class Stm2000ServiceImpl extends EgovAbstractServiceImpl implements Stm2000Service{
	
	/** Stm2000DAO DI */
	@Resource(name="stm2000DAO")
    private Stm2000DAO stm2000DAO;
	
	
	/**
	 * Stm2000 SVN Repository 목록을 조회한다.
	 * @param Stm2000VO
	 * @return List - SVN Repository 목록
	 * @exception Exception
	 */
	@Override
	public List<Stm2000VO> selectStm2000RepositoryList(Stm2000VO stm2000VO) throws Exception {
		return stm2000DAO.selectStm2000RepositoryList(stm2000VO);
	}

	/**
	 * Stm2000 SVN Repository 목록 총 수를 조회한다.
	 * @param Stm2000VO
	 * @return SVN Repository 목록 수
	 * @exception Exception
	 */
	@Override
	public int selectStm2000RepositoryListCnt(Stm2000VO stm2000VO) throws Exception {
		return stm2000DAO.selectStm2000RepositoryListCnt(stm2000VO);
	}

	/**
	 * Stm2000 SVN Repository 단건 조회한다.
	 * @param paramMap
	 * @return
	 * @exception Exception
	 */
	@SuppressWarnings("rawtypes")
	@Override
	public Map selectStm2000Info(Map<String, String> paramMap) throws Exception{
		return stm2000DAO.selectStm2000Info(paramMap);
	}
	
	/**
	 * Stm2000 SVN Repository를 등록/수정한다.
	 * @param paramMap
	 * @return
	 * @exception Exception
	 */
	@Override
	public Object saveStm2000Info(Map<String, String> paramMap) throws Exception{

		String insNewSvnRepId ="";
		int result = 0;
		String popupGb = (String)paramMap.get("popupGb");
		
		if("insert".equals(popupGb)){
			insNewSvnRepId = stm2000DAO.insertStm2000Info( paramMap);
			return insNewSvnRepId;
			//생성된 키가 없으면 튕겨냄
		}else if("update".equals(popupGb)){
			result = stm2000DAO.updateStm2000Info(paramMap);
			return result;
		}
		return null;
	}

	/**
	 * Stm2000 SVN Repository 상태를 확인한다.
	 * @param paramMap
	 * @return
	 * @exception Exception
	 */
	@Override
	public int selectStm2000UseCountInfo(Map<String, String> paramMap) throws Exception{
		return stm2000DAO.selectStm2000UseCountInfo(paramMap);
	}

	/**
	 * Stm2000 SVN Repository를 삭제한다.
	 * @param paramMap
	 * @return
	 * @exception Exception
	 */
	@Override
	public void deleteStm2000Info(Map<String, String> paramMap) throws Exception{
		stm2000DAO.deleteStm2000Info(paramMap);
	}

	/**
	 * Stm2000 SVN Repository 허용 역할 목록을 저장한다.
	 * @param paramMap
	 * @return
	 * @throws Exception
	 */
	@SuppressWarnings({ "rawtypes", "unchecked" })
	public int insertStm2000SvnAuthGrpList(Map paramMap) throws Exception {
		int selAuthCnt = Integer.parseInt((String)paramMap.get("selAuthCnt"));
		
		//추가 실패된 갯수
		int addFailAuthCnt = 0;
		
		if(selAuthCnt > 1){
			List<String> selAuthList = (List<String>) paramMap.get("selAuth");
			
			for(String selAuthInfo : selAuthList){
				Map newMap = new HashMap<String, String>();
				
				JSONObject jsonObj = null;
				jsonObj = new JSONObject(selAuthInfo);
				
				newMap.put("licGrpId", String.valueOf(paramMap.get("licGrpId")));
				newMap.put("prjId", String.valueOf(paramMap.get("prjId")));
				newMap.put("svnRepId", String.valueOf(paramMap.get("svnRepId")));
				newMap.put("authGrpId", String.valueOf(jsonObj.get("authGrpId")));
				
				//레코드 존재하는지 체크
				int flowAuthCnt = stm2000DAO.selectStm2000SvnAuthGrpCnt(newMap);
				
				//레코드 없는경우 insert
				if(flowAuthCnt == 0){
					stm2000DAO.insertStm2000SvnAuthGrpInfo(newMap);
				}else{
					addFailAuthCnt++;
				}
			}
		}else{
			String selAuthInfo = (String) paramMap.get("selAuth");
			
			Map newMap = new HashMap<String, String>();
			
			JSONObject jsonObj = null;
			jsonObj = new JSONObject(selAuthInfo);
			
			newMap.put("licGrpId", String.valueOf(paramMap.get("licGrpId")));
			newMap.put("prjId", String.valueOf(paramMap.get("prjId")));
			newMap.put("svnRepId", String.valueOf(paramMap.get("svnRepId")));
			newMap.put("authGrpId", String.valueOf(jsonObj.get("authGrpId")));
			
			//레코드 존재하는지 체크
			int reqRepCnt = stm2000DAO.selectStm2000SvnAuthGrpCnt(newMap);
			
			//레코드 없는경우 insert
			if(reqRepCnt == 0){
				stm2000DAO.insertStm2000SvnAuthGrpInfo(newMap);
			}else{
				addFailAuthCnt++;
			}
		}
		
		return addFailAuthCnt;
	}
	
	/**
	 * Stm2000 SVN Repository 허용 역할 목록을 제거한다.
	 * @param paramMap
	 * @return
	 * @throws Exception
	 */
	@SuppressWarnings({ "rawtypes", "unchecked" })
	public void deleteStm2000SvnAuthGrpList(Map paramMap) throws Exception {
		int selAuthCnt = Integer.parseInt((String)paramMap.get("selAuthCnt"));
		
		if(selAuthCnt > 1){
			List<String> selAuthList = (List<String>) paramMap.get("selAuth");
			
			for(String selAuthInfo : selAuthList){
				Map newMap = new HashMap<String, String>();
				
				JSONObject jsonObj = null;
				jsonObj = new JSONObject(selAuthInfo);
				
				newMap.put("prjId", String.valueOf(paramMap.get("prjId")));
				newMap.put("svnRepId", String.valueOf(paramMap.get("svnRepId")));
				newMap.put("authGrpId", String.valueOf(jsonObj.get("authGrpId")));
				
				//제거
				stm2000DAO.deleteStm2000SvnAuthGrpInfo(newMap);
			}
		}else{
			String selAuthInfo = (String) paramMap.get("selAuth");
			
			Map newMap = new HashMap<String, String>();
			
			JSONObject jsonObj = null;
			jsonObj = new JSONObject(selAuthInfo);
			
			newMap.put("prjId", String.valueOf(paramMap.get("prjId")));
			newMap.put("svnRepId", String.valueOf(paramMap.get("svnRepId")));
			newMap.put("authGrpId", String.valueOf(jsonObj.get("authGrpId")));
			
			//제거
			stm2000DAO.deleteStm2000SvnAuthGrpInfo(newMap);
		}
	}
	
	/**
	 * Stm2000 SVN Repository 허용 역할 정보를 저장한다. (단건)
	 * @param paramMap
	 * @return
	 * @throws Exception
	 */
	@SuppressWarnings("rawtypes")
	public void insertStm2000SvnAuthGrpInfo(Map paramMap) throws Exception {
		stm2000DAO.insertStm2000SvnAuthGrpInfo(paramMap);
	}
	
	/**
	 * Stm2000 SVN Repository 허용 역할 정보를 제거한다. (단건)
	 * @param paramMap
	 * @return
	 * @throws Exception
	 */
	@SuppressWarnings("rawtypes")
	public void deleteStm2000SvnAuthGrpInfo(Map paramMap) throws Exception {
		stm2000DAO.deleteStm2000SvnAuthGrpInfo(paramMap);
	}
	
	/**
	 * Stm2000 SVN Repository 허용 역할 정보 목록을 조회한다.
	 * @param paramMap
	 * @return List SVN Repository 허용 역할 목록
	 * @throws Exception
	 */
	@SuppressWarnings("rawtypes")
	public List selectStm2000SvnAuthGrpList(Map paramMap) throws Exception {
		return stm2000DAO.selectStm2000SvnAuthGrpList(paramMap);
	}
	
	/**
	 * Stm2000 그리드 페이징 처리를 위한 SVN Repository 허용 역할 목록 총 수를 조회한다.
	 * @param paramMap
	 * @return SVN Repository 허용 역할 목록 총 수
	 * @throws Exception
	 */
	@SuppressWarnings("rawtypes")
	public int selectStm2000SvnAuthGrpCnt(Map paramMap) throws Exception {
		return stm2000DAO.selectStm2000SvnAuthGrpCnt(paramMap);
	}
}
