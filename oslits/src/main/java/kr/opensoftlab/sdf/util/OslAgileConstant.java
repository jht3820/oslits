package kr.opensoftlab.sdf.util;

/**
 * OslAgileConstant.java 클래스
 * 
 * @author 안세웅
 * @since 2016. 01. 21.
 * @version 1.0
 * @see
 * @decription OSL AGILE 솔루션 static 상수 선언
 * 
 * <pre>
 * << 개정이력(Modification Information) >>
 *   
 *   수정일     		 		수정자            수정내용
 *  ---------------    -------------   ------------------------------
 *  2016. 01. 21.   		안세웅        	최초 생성
 *  2016. 01. 23.			정형택	초기화 디폴트 정보 추가
 * </pre>
 */

public interface OslAgileConstant {

	public static String EVENT_LOAD="onLoad";
	public static String EVENT_SELECT="onSelect";
	public static String EVENT_MODIFY="modify";
	public static String EVENT_UPDATE="update";
	public static String EVENT_DELETE="delete";
	public static String EVENT_INSERT="insert";
	
	public static int pageUnit = 30;
	public static int pageSize = 30;
	
	/* 초기화 디폴트 정보 추가 */
	public static String sortOrdr = "";
	public static String searchSelect = ""; 
	public static String searchTxt = ""; 
	public static int pageIndex = 1; 
	public static int firstIndex = 1; 
	public static int lastIndex = 1; 
	public static int recordCountPerPage = 20;
	public static int rowNo = 0; 
	
	public static final String YES = "Y";
	public static final String NO = "N";
	
	
}
