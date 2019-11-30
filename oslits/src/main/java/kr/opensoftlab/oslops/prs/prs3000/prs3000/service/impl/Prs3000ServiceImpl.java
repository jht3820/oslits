package kr.opensoftlab.oslops.prs.prs3000.prs3000.service.impl;

import java.util.Map;

import javax.annotation.Resource;

import kr.opensoftlab.oslops.adm.adm5000.adm5200.service.impl.Adm5200DAO;
import kr.opensoftlab.oslops.com.vo.LoginVO;
import kr.opensoftlab.oslops.prs.prs3000.prs3000.service.Prs3000Service;

import org.springframework.stereotype.Service;

import egovframework.com.cmm.EgovMessageSource;
import egovframework.com.utl.fcc.service.EgovStringUtil;
import egovframework.com.utl.sim.service.EgovFileScrty;
import egovframework.rte.fdl.cmmn.EgovAbstractServiceImpl;

/**
 * @Class Name : Prs3000ServiceImpl.java
 * @Description : Prs3000ServiceImpl Business Implement class
 * @Modification Information
 *
 * @author 강재민
 * @since 2016.01.31
 * @version 1.0
 * @see
 *  
 *  Copyright (C)  All right reserved.
 */
@Service("prs3000Service")
public class Prs3000ServiceImpl extends EgovAbstractServiceImpl implements Prs3000Service{
	
	/** Prs3000DAO DI */
    @Resource(name="prs3000DAO")
    private Prs3000DAO prs3000DAO;
    
    /** Adm5200DAO DI */
    @Resource(name="adm5200DAO")
    private Adm5200DAO adm5200DAO;
    
    /** EgovMessageSource */
	@Resource(name = "egovMessageSource")
	EgovMessageSource egovMessageSource;
    
    /**
	 * selectPrs3000 개인정보를 조회한다.(단건)
	 * @param Map
	 * @return int
	 * @exception Exception
	 */
    @SuppressWarnings("rawtypes")
   	public Map selectPrs3000(Map paramMap) throws Exception {
   		return (Map) prs3000DAO.selectPrs3000(paramMap) ;
   	}
	
	
	
    /**
	 * Prs3000 개인정보 수정 화면(이메일 체크) AJAX
	 * @param Map
	 * @return int
	 * @exception Exception
	 */
    @SuppressWarnings("rawtypes")
   	public int selectPrs3000emailChRepAjax(Map paramMap) throws Exception {
   		return prs3000DAO.selectPrs3000emailChRepAjax(paramMap) ;
   	}
    
    
    
    /**
	 * Prs3000 개인정보 수정(UPDATE) AJAX
	 * @param Map
	 * @return int
	 * @exception Exception
	 */
    @SuppressWarnings({ "rawtypes", "unchecked" })
   	public int updatePrs3000(Map paramMap) throws Exception {
    	
    	int uCnt = prs3000DAO.updatePrs3000(paramMap);
    	
		// 사용자 변경이력값 세팅
		paramMap.put("logState", "U");	// 사용자 변경이력 상태
		paramMap.put("pwChangeState", "N");	
		// 개인정보 수정되면 변경이력 등록
		adm5200DAO.insertAdm5200UserChangeLog(paramMap);
    	
   		return uCnt;
   	}

    
    /**
   	 * Prs3001 비밀번호 수정(UPDATE) AJAX
   	 * @param Map
   	 * @return int
   	 * @exception Exception
   	 */
	@Override
	public int updatePrs3001(Map<String, String> paramMap) throws Exception {
		
		String usrPw 		= (String) paramMap.get("usrPw");
		String usrId 		= (String) paramMap.get("usrId");
		String newUsrPw 	= (String) paramMap.get("newUsrPw");
		
		// 이전 패스워드 조회
    	String bePw = prs3000DAO.selectPrs3001PwCheck( paramMap );
    	
		// 이전 패스워드가 입력한 현재 비밀번호와 일지 하는지 확인하기 위한 용
		String enUsrPw 	= EgovFileScrty.encryptPassword(usrPw, usrId);	// 패스워드 암호화
		
		// 새비밀번호 암호화
		newUsrPw = EgovFileScrty.encryptPassword(newUsrPw, usrId);
		paramMap.put("newUsrPw", newUsrPw);
		
		// 비밀번호 수정확인용
		int pCnt = 0;
		
		// 현재비밀번호와 이전 패스워드 일치
		if(bePw.equals(EgovStringUtil.isNullToString(enUsrPw))) {
			
			// 1년간 사용한 비밀번호 체크
			String isUsedPw = (String) prs3000DAO.selectPrs3001BeforeUsedPwCheck(paramMap);
			
			// 새비밀번호가 1년안에 사용한 비밀번호가 아닐 시 수정
			if(!isUsedPw.equals("Y")) {
				
				// 비밀번호 수정
				pCnt = prs3000DAO.updatePrs3001(paramMap) ;
				
				// 사용자 변경이력값 세팅
				paramMap.put("logState", "U");	// 사용자 변경이력 상태
				paramMap.put("pwChangeState", "Y");	// 비밀번호 변경이력 
				
				// 개인정보 수정되면 변경이력 등록
				adm5200DAO.insertAdm5200UserChangeLog(paramMap);
				
			} else {
				pCnt = 2;
			}
			
		} else {
			pCnt = 3;
		}
		
		return pCnt;
	}

	/**
   	 * Prs3002 대시 보드 표시 구분 , 메인 프로젝트  조회 (SELECT) AJAX
   	 * @param Map
   	 * @return Map
   	 * @exception Exception
   	 */
	@SuppressWarnings("rawtypes")
	@Override
	public Map selectPrs3002(Map<String, String> paramMap) throws Exception {
		return prs3000DAO.selectPrs3002(paramMap);
	}


	/**
   	 * Prs3002 기타정보 수정(UPDATE)  AJAX
   	 * @param Map
   	 * @return void
   	 * @exception Exception
   	 */
	@Override
	public int updatePrs3002(Map<String, String> paramMap) throws Exception {
		
		int dCnt = prs3000DAO.updatePrs3002(paramMap);
		
		// 기타정보 상태 업데이트 로그
		paramMap.put("logState", "U");
		paramMap.put("pwChangeState", "N");	
		
		// 기타정보 변경이력 등록
		adm5200DAO.insertAdm5200UserChangeLog(paramMap);
		
		return dCnt;
	}


	/**
   	 * Prs3000LoginVO 세션 갱신
   	 * @param Map
   	 * @return Map
   	 * @exception Exception
   	 */
	@Override
	public LoginVO selectPrs3000LoginVO(Map<String, String> paramMap) throws Exception {
		return prs3000DAO.selectPrs3000LoginVO(paramMap);
	}
    
}
