package kr.opensoftlab.oslops.com.exception;

/**
 * UserDefineException.java 클래스
 * 
 * @author 정형택
 * @since 2013. 11. 06.
 * @version 1.0
 * @see
 *
 * <pre>
 * << 개정이력(Modification Information) >>
 *   
 *   수정일     		 		수정자            수정내용
 *  ---------------    -------------   ------------------------------
 *   2013. 11. 06.   		정형택        	최초 생성
 * </pre>
 */

public class UserDefineException extends Exception{
	static final long serialVersionUID = -3387516993124229948L;
	public UserDefineException(){
		super();
	}
	public UserDefineException(String msg){
		super(msg);
	}
}