package kr.opensoftlab.oslits.stm.stm2000.stm2000.service.impl;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;


















import kr.opensoftlab.oslits.stm.stm2000.stm2000.service.Stm2000Service;
import kr.opensoftlab.oslits.stm.stm2000.stm2000.vo.Stm2000VO;

import org.codehaus.jettison.json.JSONObject;
import org.springframework.stereotype.Service;

import egovframework.com.cmm.EgovMessageSource;
import egovframework.com.utl.fcc.service.EgovStringUtil;
import egovframework.rte.fdl.cmmn.EgovAbstractServiceImpl;

/**
 * @Class Name : Svn1000ServiceImpl.java
 * @Description : Svn1000ServiceImpl Business Implement class
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
	/** Scpr5000DAO DI */

	@Resource(name="stm2000DAO")
    private Stm2000DAO stm2000DAO;
	
	
	
	@Override
	public List<Stm2000VO> selectStm2000RepositoryList(Stm2000VO stm2000VO)
			throws Exception {
		return stm2000DAO.selectStm2000RepositoryList(stm2000VO);
	}

	@Override
	public int selectStm2000RepositoryListCnt(Stm2000VO stm2000VO)
			throws Exception {
		return stm2000DAO.selectStm2000RepositoryListCnt(stm2000VO);
	}

	@Override
	public Map selectStm2000Info(Map<String, String> paramMap) {
		// TODO Auto-generated method stub
		return stm2000DAO.selectStm2000Info(paramMap);
	}
	

	@Override
	public Object saveStm2000Info(Map<String, String> paramMap) {
		// TODO Auto-generated method stub
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

	@Override
	public int selectStm2000UseCountInfo(Map<String, String> paramMap) {
		// TODO Auto-generated method stub
		return stm2000DAO.selectStm2000UseCountInfo(paramMap);
	}

	@Override
	public void deleteStm2000Info(Map<String, String> paramMap) {
		// TODO Auto-generated method stub
		stm2000DAO.deleteStm2000Info(paramMap);
	}

	/**
	 * SVN Repository 허용 역할 정보 목록 저장
	 * @param paramMap
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
	 * SVN Repository 허용 역할정보 제거
	 * @param paramMap
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
	 * SVN Repository 허용 역할 정보 저장
	 * @param paramMap
	 * @throws Exception
	 */
	@SuppressWarnings("rawtypes")
	public void insertStm2000SvnAuthGrpInfo(Map paramMap) throws Exception {
		stm2000DAO.insertStm2000SvnAuthGrpInfo(paramMap);
	}
	/**
	 * SVN Repository 허용 역할 정보 제거
	 * @param paramMap
	 * @throws Exception
	 */
	@SuppressWarnings("rawtypes")
	public void deleteStm2000SvnAuthGrpInfo(Map paramMap) throws Exception {
		stm2000DAO.deleteStm2000SvnAuthGrpInfo(paramMap);
	}
	
	/**
	 * SVN Repository 허용 역할 정보 목록 가져오기
	 * @param paramMap
	 * @throws Exception
	 */
	@SuppressWarnings("rawtypes")
	public List selectStm2000SvnAuthGrpList(Map paramMap) throws Exception {
		return stm2000DAO.selectStm2000SvnAuthGrpList(paramMap);
	}
	
	/**
	 * SVN Repository 허용 역할 정보 단건 갯수 가져오기
	 * @param paramMap
	 * @throws Exception
	 */
	@SuppressWarnings("rawtypes")
	public int selectStm2000SvnAuthGrpCnt(Map paramMap) throws Exception {
		return stm2000DAO.selectStm2000SvnAuthGrpCnt(paramMap);
	}
}
