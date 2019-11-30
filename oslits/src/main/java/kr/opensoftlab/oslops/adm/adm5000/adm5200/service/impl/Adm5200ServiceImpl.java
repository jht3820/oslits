package kr.opensoftlab.oslops.adm.adm5000.adm5200.service.impl;



import java.util.Map;

import javax.annotation.Resource;

import kr.opensoftlab.oslops.adm.adm2000.adm2000.service.impl.Adm2000DAO;
import kr.opensoftlab.oslops.adm.adm5000.adm5200.service.Adm5200Service;

import org.springframework.stereotype.Service;

import egovframework.com.utl.fcc.service.EgovStringUtil;
import egovframework.com.utl.sim.service.EgovFileScrty;
import egovframework.rte.fdl.cmmn.EgovAbstractServiceImpl;


/**
 * @Class Name : Adm5200ServiceImpl.java
 * @Description : 사용자 변경이력 관리(Adm5200) 서비스 클래스
 * @Modification Information
 * <pre>
 *   수정일      수정자          수정내용
 *  -------    --------    ---------------------------
 *  2018.08.25  배용진          최초 생성
 *  
 * </pre>
 *  @author 배용진
 *  @version 1.0
 *  @see 
 *  
 *  Copyright (C) Open Soft Lab Corp. All Rights Reserved
 */
@Service("adm5200Service")
public class Adm5200ServiceImpl extends EgovAbstractServiceImpl implements Adm5200Service {
	
	/** Adm2000DAO DI */
    @Resource(name="adm2000DAO")
    private Adm2000DAO adm2000DAO;
	
	/** DAO Bean Injection */
    @Resource(name="adm5200DAO")
    private Adm5200DAO adm5200DAO;

	/**
	 * Adm5200 사용자 변경이력 등록
	 * @param param - Map
	 * @return 
	 * @exception Exception
	 */
	@SuppressWarnings("rawtypes")
    @Override
	public void insertAdm5200UserChangeLog(Map paramMap) throws Exception {
		adm5200DAO.insertAdm5200UserChangeLog(paramMap);
	}


	/**
	 * Adm5200  사용자 비밀번호 변경일 체크
	 * @param param - Map
	 * @return 
	 * @exception Exception
	 */
	@SuppressWarnings("rawtypes")
	@Override
	public int selectAdm5200PwChangeDateCheck(Map paramMap) throws Exception{
		return adm5200DAO.selectAdm5200PwChangeDateCheck(paramMap);
	}
	
	
	/**
	 * Adm5200 사용자가 1년이내 사용했던 비밀번호 체크
	 * @param param - Map
	 * @return 사용유무 - Y , N
	 * @exception Exception
	 */
	@SuppressWarnings({ "rawtypes", "unchecked"})
	@Override
	public String selectAdm5200BeforeUsedPwCheck(Map paramMap) throws Exception {
	
		String usrPw 	= (String) paramMap.get("usrPw");
		String usrId 	= (String) paramMap.get("usrId");
		String isUsedPw = "";
		
		// 이전 패스워드 조회
		String bePw = adm2000DAO.selectAdm2000PwCheck(paramMap);
		
		// 이전 패스워드와 , 넘어온 패스워드가 같다면 비밀번호 수정을 하지않은 상태
		// 이때는 1년간 비밀번호 사용여부 체크하지 않음
		if( bePw.equals(EgovStringUtil.isNullToString(usrPw)) ){
			isUsedPw = "N";
		}else{
			String enUsrPw 	= EgovFileScrty.encryptPassword(usrPw, usrId);	// 패스워드 암호화
			paramMap.put("usrPw", enUsrPw);
			
			// 비밀번호를 변경했을 경우, 1년간 사용한 비밀번호 체크
			isUsedPw = (String) adm5200DAO.selectAdm5200BeforeUsedPwCheck(paramMap);
		}
		
		return isUsedPw;
	}  

    
}
