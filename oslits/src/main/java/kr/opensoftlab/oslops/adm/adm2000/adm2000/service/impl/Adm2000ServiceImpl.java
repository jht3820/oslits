package kr.opensoftlab.oslops.adm.adm2000.adm2000.service.impl;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import kr.opensoftlab.oslops.adm.adm2000.adm2000.service.Adm2000Service;
import kr.opensoftlab.oslops.adm.adm2000.adm2000.vo.Adm2000VO;
import kr.opensoftlab.oslops.adm.adm5000.adm5200.service.impl.Adm5200DAO;
import kr.opensoftlab.oslops.arm.arm1000.arm1000.service.impl.Arm1000DAO;
import kr.opensoftlab.sdf.excel.ExcelDataListResultHandler;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.springframework.stereotype.Service;

import com.fasterxml.jackson.databind.ObjectMapper;

import egovframework.com.cmm.EgovMessageSource;
import egovframework.com.utl.fcc.service.EgovStringUtil;
import egovframework.com.utl.sim.service.EgovFileScrty;
import egovframework.rte.fdl.cmmn.EgovAbstractServiceImpl;


/**
 * @Class Name : Adm2000ServiceImpl.java
 * @Description : Adm2000ServiceImpl Business Implement class
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
@Service("adm2000Service")
public class Adm2000ServiceImpl extends EgovAbstractServiceImpl implements Adm2000Service {

	/** Adm2000DAO DI */
    @Resource(name="adm2000DAO")
    private Adm2000DAO adm2000DAO;
    
    /** Arm1000DAO DI */
    @Resource(name="arm1000DAO")
    private Arm1000DAO arm1000DAO;
    
    /** Adm5200DAO DI */
    @Resource(name="adm5200DAO")
    private Adm5200DAO adm5200DAO;
    
    /** EgovMessageSource */
	@Resource(name = "egovMessageSource")
	EgovMessageSource egovMessageSource;

    /**
	 * Adm2000  사용자 일괄저장
	 * @param param - Map
	 * @param param - prjId 프로젝트 id
	 * @return 
	 * @exception Exception
	 */
	
	@SuppressWarnings({"rawtypes", "unchecked" })
	@Override
	public void insertAdm2000AdmInfoListAjax(Map<String, String> paramMap, String prjId) throws Exception{
		//service에 넘길 List Data
		List<Map> userData = new ArrayList();
		
		//넘겨 받은 JSON 데이터
		String jsonData = paramMap.get("jsonData");
		
		//JSON파서 선언
		JSONParser jsonParser = new JSONParser();
		
		//넘겨 받은 데이터 JSON Array로 파싱
		JSONArray jsonArray = (JSONArray) jsonParser.parse(jsonData);
		for(int i=0; i<jsonArray.size(); i++){
			JSONObject token = (JSONObject) jsonArray.get(i);
			
			//JSON to Object
			HashMap<String, Object> tokenObj = new ObjectMapper().readValue(token.toString(), HashMap.class) ;
			
			userData.add(tokenObj);
		}
		
		for(Map data : userData){
			Map<String,String> admMap = data;
			String enUsrPw 	= EgovFileScrty.encryptPassword(admMap.get("usrId").toString(), admMap.get("usrId").toString());	// 패스워드 암호화   
			
			String srtuUseCd = admMap.get("useCd").toString();
			
			admMap.put("enUsrPw", enUsrPw);                    
			admMap.put("useCd", srtuUseCd.toUpperCase().equals("Y")?"01":"02");
			admMap.put("usrEmail", admMap.get("email").toString());                                                                                    
			admMap.put("usrTelNo", admMap.get("telno").toString());                                                                                    
			admMap.put("usrEtc", admMap.get("etc").toString());
			
			admMap.put("regUsrId", paramMap.get("regUsrId"));
			admMap.put("regUsrIp", paramMap.get("regUsrIp"));
			admMap.put("licGrpId", paramMap.get("licGrpId"));
			adm2000DAO.insertAdm2000AdmInfoAjax(admMap);   
			
			// 사용자 변경이력값 세팅
			admMap.put("logState", "I");		// 사용자 변경이력 상태
			admMap.put("pwChangeState", "N");	// 비밀번호 변경여부	
			
			// 일괄 저장 시 사용자 변경이록에 추가
			adm5200DAO.insertAdm5200UserChangeLog(admMap);
		}
	}
    
	public int selectAdm2000UsrListCnt(Adm2000VO adm2000VO) throws Exception {
		 return adm2000DAO.selectAdm2000UsrListCnt(adm2000VO);
	}
	
	public List<Adm2000VO> selectAdm2000UsrList(Adm2000VO adm2000VO) throws Exception {
		return adm2000DAO.selectAdm2000UsrList(adm2000VO);
	}
	
    @SuppressWarnings("rawtypes")
	public Map selectAdm2000UsrInfo(Map param) throws Exception {
    	return adm2000DAO.selectAdm2000UsrInfo(param); 
    }
    
	@SuppressWarnings({ "rawtypes", "unchecked"})
	public void saveAdm2000UsrInfo(Map paramMap) throws Exception{

		String proStatus = (String) paramMap.get("proStatus");
		String usrPw 		= (String) paramMap.get("usrPw");
		String usrId 		= (String) paramMap.get("usrId");
		
		if( "I".equals(proStatus)){
			String enUsrPw 	= EgovFileScrty.encryptPassword(usrPw, usrId);	// 패스워드 암호화
			paramMap.put("enUsrPw", enUsrPw);
			
			String newUsrId = adm2000DAO.insertAdm2000UsrInfo( paramMap );
			
			if( "".equals(EgovStringUtil.isNullToString(newUsrId)) ){
				throw new Exception(egovMessageSource.getMessage("adm2000.fail.user.insert"));
			}
			
			// 사용자 변경이력값 세팅
			paramMap.put("logState", proStatus);	// 사용자 변경이력 상태
			paramMap.put("pwChangeState", "N");		// 비밀번호 변경여부
			
			// 사용자 등록되면 변경이력 등록
			adm5200DAO.insertAdm5200UserChangeLog(paramMap);
		}

		if("U".equals(proStatus)){
			
			// 이전 패스워드 조회
			String bePw = adm2000DAO.selectAdm2000PwCheck( paramMap );
			
			// 이전 패스워드와 , 넘어온 패스워드가 같다면 이전 패스워드 적용
			if( bePw.equals(EgovStringUtil.isNullToString(usrPw)) ){
				paramMap.put("enUsrPw", usrPw);
			}else{
				String enUsrPw 	= EgovFileScrty.encryptPassword(usrPw, usrId);	// 패스워드 암호화
				paramMap.put("enUsrPw", enUsrPw);
				
				// 비밀번호 변경여부 세팅
				paramMap.put("pwChangeState", "Y");
			}
			
			int uCnt = adm2000DAO.updateAdm2000UsrInfo( paramMap );
			if(uCnt == 0){
				throw new Exception(egovMessageSource.getMessage("adm2000.fail.user.update"));
			}
			
			// 사용자 수정되면 변경이력 등록
			paramMap.put("logState", proStatus);
			adm5200DAO.insertAdm5200UserChangeLog(paramMap);
		}
	}

	@SuppressWarnings({ "rawtypes" })
	public int selectCmm2000IdCheck(Map paramMap) throws Exception {
		return adm2000DAO.selectCmm2000IdCheck( paramMap ) ;
	}
	
	@SuppressWarnings({ "rawtypes" })
	public int selectCmm2000EmailCheck(Map paramMap) throws Exception {
		return adm2000DAO.selectCmm2000EmailCheck( paramMap ) ;
	}

	@SuppressWarnings({ "rawtypes", "unchecked" })
	public void updateAdm2000UseCd(Map paramMap) throws Exception {

		String usrIdAttr = (String) paramMap.get("usrId");
		String useCdAttr = (String) paramMap.get("useCd");
		
		String[] usrIdList = usrIdAttr.split(",");
		String[] useCdList = useCdAttr.split(",");
		
		for( int i=0; i < usrIdList.length; i++ ){
			
			paramMap.put("usrId", usrIdList[i]);
			paramMap.put("useCd", useCdList[i]);
			
			adm2000DAO.updateAdm2000UseCd( paramMap ) ;
			
		}
	}

	@SuppressWarnings( { "rawtypes", "unchecked" } )
	public void deleteAdm2000UsrInfo(Map paramMap) throws Exception {
		
		String usrIdAttr = (String) paramMap.get("usrId");
		String[] usrIdList = usrIdAttr.split(",");
		
		paramMap.put("usrIdList", usrIdList);
		
		int count = adm2000DAO.selectAdm1300ExistUsrInProject(paramMap);
		if(count > 0 ){
			//"프로젝트에 배정된 사용자가 포함되어있어 삭제할 수 없습니다. "
			throw new Exception(egovMessageSource.getMessage("adm2000.fail.prjUser.delete"));
		}
		
		// 변경이력정보 담을 Map
		Map changeInfoMap = new HashMap<String, String>();
		
		for(String userId : usrIdList){
			
			String uId = userId.replaceAll("'", "");
			changeInfoMap.put("logState", "D");
			changeInfoMap.put("licGrpId", paramMap.get("licGrpId"));
			changeInfoMap.put("usrId", uId);
			
			// 삭제 시 사용자 변경이력에 추가
			adm5200DAO.insertAdm5200UserChangeLog(changeInfoMap);
		}
		
		//사용자 제거
		int delCnt = adm2000DAO.deleteAdm2000UsrInfo( paramMap ) ;
	
		// 삭제된 건이 없으면 튕겨냄
		if(delCnt == 0 ){
			throw new Exception(egovMessageSource.getMessage("fail.common.delete"));
		}
		
		//사용자 쪽지 제거
		arm1000DAO.updateArm1000AlarmList(paramMap);
	}
	
	@Override
	public void selectAdm2000ExcelList(Adm2000VO adm2000vo, ExcelDataListResultHandler resultHandler) throws Exception {
		adm2000DAO.selectAdm2000ExcelList(adm2000vo, resultHandler);
	}
	
	
	
	/**
	 * Adm2000 사용자 차단여부 수정 (단건)
	 * @param param - Map
	 * @return
	 * @exception Exception
	 */
	@SuppressWarnings("rawtypes")
	public int updateAdm2000Block(Map paramMap) throws Exception{
		if( "02".equals(paramMap.get("block"))   ){
			adm2000DAO.insertAdm2100BlockLog(paramMap);
		}
		return adm2000DAO.updateAdm2000Block(paramMap);
	}

	/**
	 * Adm2000 사용자가 속해있는 조직목록 조회
	 * @param  param - Map
	 * @return
	 * @throws Exception
	 */
	@SuppressWarnings("rawtypes")
	@Override
	public List selectAdm2000ExistUsrInDept(Map paramMap) throws Exception {
		return adm2000DAO.selectAdm2000ExistUsrInDept(paramMap);
	}

	@Override
	public void updateAdm2000AccountInit(Map<String, String> paramMap) throws Exception {
		// TODO Auto-generated method stub
		
		String usrId 		= (String) paramMap.get("usrId");
		
		String enUsrPw 	= EgovFileScrty.encryptPassword(usrId, usrId);	// 패스워드 암호화
		
		paramMap.put("enUsrPw", enUsrPw);
		paramMap.put("iniYn", "Y");
		adm2000DAO.updateAdm2000AccountInit(paramMap);
		
		// 비밀번호 초기화시 변경이력 등록
		paramMap.put("logState", "U");
		paramMap.put("pwChangeState", "Y");
		adm5200DAO.insertAdm5200UserChangeLog(paramMap);
	}
	
}
