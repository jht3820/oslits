package kr.opensoftlab.sdf.commons.dbcp;

import kr.opensoftlab.sdf.util.CommonScrty;

import org.apache.commons.dbcp.BasicDataSource;
import org.apache.log4j.Logger;


/**
 * 	BasicDataSource 설정시 properties 파일 암호화 저장하기 위해 클래스 재정의
 *  
 * 	최초작성 : 2016.01.08. 정형택
 *  수정작성 : 2017.02.16. 정형택
 *  수정내용 : BASE64 암호화에서 AES256 암복호화로 변경함.
 */
public class BasicDataSourceEnDe extends BasicDataSource {
	
	/** 로그4j 로거 로딩 */
	private static final Logger Log = Logger.getLogger(BasicDataSourceEnDe.class);
	
	
	
	@Override
	public void setPassword(String password) {
		String decodeStr = decode(password); //임시 복호화 함수 적용
		
		/* 패스워드 검증 코드 */
		/*try {
			System.out.println("원래 패스워드 ==> " + password);
			System.out.println("암호화된 패스워드 ==> " + CommonScrty.encryptedAria(password, username));
			System.out.println("복호화된 패스워드 ==> " + CommonScrty.decryptedAria(password, username));
		} catch (Exception e) {
			Log.error("\n=====> dataSource password decode 중 오류 발생 : " + e.getMessage());
		}*/
		
		super.setPassword(decodeStr) ;
	}

	private String decode(String password) {
		//복호화 암호 저장 변수
		String decodeStr = "";
		String username = super.getUsername();
		//암호화 
		try {
			
			decodeStr = CommonScrty.decryptedAria(password, username);
			//System.out.println("복호화된 패스워드 ==> " + decodeStr);
			
		} catch (Exception e) {
			Log.error("\n=====> dataSource password decode error : " + e.getMessage());
		}

		return decodeStr;
	}
}
