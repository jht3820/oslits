package kr.opensoftlab.oslops.cmm.cmm3000.cmm3200.service.impl;

import java.util.Map;

import javax.annotation.Resource;

import org.springframework.stereotype.Service;

import egovframework.com.cmm.EgovMessageSource;
import egovframework.com.utl.fcc.service.EgovStringUtil;
import egovframework.com.utl.sim.service.EgovFileScrty;
import egovframework.rte.fdl.cmmn.EgovAbstractServiceImpl;
import kr.opensoftlab.oslops.adm.adm5000.adm5200.service.impl.Adm5200DAO;
import kr.opensoftlab.oslops.cmm.cmm3000.cmm3200.service.Cmm3200Service;
import kr.opensoftlab.sdf.util.TableMakePrimaryKey;

/**
 * @Class Name : Cmm3200ServiceImpl.java
 * @Description : Cmm3200ServiceImpl Business Implement class
 * @Modification Information
 *
 * @author 김정환
 * @since 2016.01.16
 * @version 1.0
 * @see
 *  
 *  Copyright (C)  All right reserved.
 */

@Service("cmm3200Service")
public class Cmm3200ServiceImpl extends EgovAbstractServiceImpl implements Cmm3200Service {

	/** Cmm3200DAO DI */
    @Resource(name="cmm3200DAO")
    private Cmm3200DAO cmm3200DAO;

    /** Adm5200DAO DI */
    @Resource(name="adm5200DAO")
    private Adm5200DAO adm5200DAO;
    
    /** EgovMessageSource */
	@Resource(name = "egovMessageSource")
	EgovMessageSource egovMessageSource;
	
	/**
	 * 아이디 체크
	 * @param paramMap
	 * @return
	 * @throws Exception
	 */
	@SuppressWarnings({ "rawtypes" })
	public int selectCmm3200idChk(Map paramMap) throws Exception {
		return cmm3200DAO.selectCmm3200idChk( paramMap ) ;
	}
	
	/**
	 * 회원가입 처리
	 * @param paramMap
	 * @exception Exception
	 */
	@SuppressWarnings({ "rawtypes", "unchecked" })
	public String insertCmm3200JoinIng(Map paramMap) throws Exception{
		
		String licGrpId 	= (String) paramMap.get("usrId") + "_GRP";		// 라이선스 ID
		String usrPw 		= (String) paramMap.get("usrPw");
		String usrId 		= (String) paramMap.get("usrId");
		String enUsrPw 	= EgovFileScrty.encryptPassword(usrPw, usrId);	// 패스워드 암호화
		
		paramMap.put("licGrpId", licGrpId);
		paramMap.put("enUsrPw", enUsrPw);


		String menuBegin 		= cmm3200DAO.insertAdm1000MenuBegin( paramMap );		// 초기 메뉴 정보 세팅
		if( "".equals(EgovStringUtil.isNullToString(menuBegin)) ){
			throw new Exception(egovMessageSource.getMessage("cmm3200.fail.menu.insert"));
		}
		
		String userInfo 			= cmm3200DAO.insertAdm2000UserInfo( paramMap );			// 사용자 정보 등록
		if( "".equals(EgovStringUtil.isNullToString(userInfo)) ){
			throw new Exception(egovMessageSource.getMessage("cmm3200.fail.user.insert"));
		}
		
		String licenceGroup 	= cmm3200DAO.insertAdm3100LicenceGroup( paramMap );		// 라이선스 그룹 등록
		if( "".equals(EgovStringUtil.isNullToString(licenceGroup)) ){
			throw new Exception(egovMessageSource.getMessage("cmm3200.fail.licGrp.insert"));
		}
		
		String licenceInfo 		= cmm3200DAO.insertAdm3000LicenceInfo( paramMap );		// 라이선스 정보 등록
		if( "".equals(EgovStringUtil.isNullToString(licenceInfo)) ){
			throw new Exception(egovMessageSource.getMessage("cmm3200.fail.lic.insert"));
		}
		
		String codeM				= cmm3200DAO.insertAdm4000CodeM( paramMap );				// 공통코드 마스터 등록 
		if( "".equals(EgovStringUtil.isNullToString(codeM)) ){
			throw new Exception(egovMessageSource.getMessage("cmm3200.fail.mstCd.insert"));
		}

		String codeD				= cmm3200DAO.insertAdm4100CodeD( paramMap );				// 공통코드 디테일 등록	
		if( "".equals(EgovStringUtil.isNullToString(codeD)) ){
			throw new Exception(egovMessageSource.getMessage("cmm3200.fail.subCd.insert"));
		}
		
		String menuAuthInfo 		= cmm3200DAO.insertAdm4100MenuAuthInfo( paramMap );			//메뉴별 권한정보 저장
		if( "".equals(EgovStringUtil.isNullToString(menuAuthInfo)) ){
			throw new Exception(egovMessageSource.getMessage("cmm3200.fail.menuAuth.insert"));
		}

		String authGroupInfo 		= cmm3200DAO.insertAdm4100AuthGroupInfo( paramMap );		//권한그룹정보  저장
		if( "".equals(EgovStringUtil.isNullToString(authGroupInfo)) ){
			throw new Exception(egovMessageSource.getMessage("cmm3200.fail.authGroup.insert"));
		}
		
		//조직 insert Id 구하기
		String deptId = TableMakePrimaryKey.makeKeyId("DPT", 5, 1);
		paramMap.put("deptId",deptId);
		
		String rootDeptInfo 		= cmm3200DAO.insertAdm4100RootDeptInfo( paramMap );			// 조직 루트디렉토리 정보 저장
		if( "".equals(EgovStringUtil.isNullToString(rootDeptInfo)) ){
			throw new Exception(egovMessageSource.getMessage("cmm3200.fail.rootDept.insert"));
		}
		
		
		// 사용자 변경이력값 세팅
		paramMap.put("logState", "I");		// 사용자 변경이력 상태
		paramMap.put("pwChangeState", "N");	// 비밀번호 변경여부	
		
		// 회원가입 시 사용자 변경이력 등록
		adm5200DAO.insertAdm5200UserChangeLog(paramMap);
		
		String successYn = "Y";

		return successYn;
		
	}
    


}
