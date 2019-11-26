package kr.opensoftlab.oslits.prs.prs3000.prs3000.service.impl;

import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import kr.opensoftlab.oslits.adm.adm5000.adm5200.service.impl.Adm5200DAO;
import kr.opensoftlab.oslits.com.vo.LoginVO;
import kr.opensoftlab.oslits.prs.prs3000.prs3000.service.Prs3000Service;

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
    	
		String usrPw 		= (String) paramMap.get("usrPw");
		String usrId 		= (String) paramMap.get("usrId");
		
    	// 이전 패스워드 조회
    	String bePw = prs3000DAO.selectPrs3000PwCheck( paramMap );
    	
		// 이전 패스워드와 , 넘어온 패스워드가 같다면 이전 패스워드 적용
		if( bePw.equals(EgovStringUtil.isNullToString(usrPw)) ){
			paramMap.put("enUsrPw", usrPw);
		}else{
			String enUsrPw 	= EgovFileScrty.encryptPassword(usrPw, usrId);	// 패스워드 암호화
			paramMap.put("enUsrPw", enUsrPw);
			
			// 비밀번호 변경여부 세팅
			paramMap.put("pwChangeState", "Y");
		}
		
    	int uCnt = prs3000DAO.updatePrs3000(paramMap) ;
    	if(uCnt == 0){
			throw new Exception(egovMessageSource.getMessage("prs3000.fail.user.update"));
		}
    	
		// 사용자 변경이력값 세팅
		paramMap.put("logState", "U");	// 사용자 변경이력 상태
		
		// 개인정보 수정되면 변경이력 등록
		adm5200DAO.insertAdm5200UserChangeLog(paramMap);
    	
   		return uCnt;
   	}



    /**
	 * Prs3000 개인정보 수정(수정 전 비밀번호 조회) AJAX
	 * @param Map
	 * @return int
	 * @exception Exception
	 */
    @SuppressWarnings("rawtypes")
	@Override
	public String selectPrs3000PwCheck(Map paramMap) throws Exception {
		return prs3000DAO.selectPrs3000PwCheck( paramMap );
	}
    
}
