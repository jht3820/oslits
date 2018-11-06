package kr.opensoftlab.sdf.util;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;



/**
 * @Class Name : CalendarUtil.java
 * @Description : 날짜 계산을 위한 유틸 클래스
 * @Modification Information
 * <pre>
 *   수정일      수정자          수정내용
 *  -------    --------    ---------------------------
 *  2018.08.24  배용진          최초 생성
 *  
 * </pre>
 * @author 배용진
 * @since 2018.08.24.
 * @version 1.0
 * @see
 *  
 *  Copyright (C) Open Soft Lab Corp. All Rights Reserved
 */
public class CalendarUtil {

	
	/**
	 * 현재날짜와 입력된 날짜 차이를 계산하고 그 결과값을 리턴한다.
	 *   
	 * @param inputDate 입력날짜
	 * @param option 계산옵션 (D : 일 차이계산 , M : 월차이 계산)
	 * @return calDate 날짜계산 결과
	 */
	public static int calDifferentOfDate(String inputDate, String option) throws Exception{
		  
		SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss");

		// 현재날짜
	    Date currentDate = new Date();
		// 입력된 날짜
		Date inDate = formatter.parse(inputDate);
	
		long result = currentDate.getTime() - inDate.getTime(); 

		long cDay = 24 * 60 * 60 * 1000;	// 일 만듬(시 * 분 * 초 * 밀리세컨)
		long cMonth = cDay * 30;			// 월 만듬
		
		// 계산결과
		long calDate = 0; 
		
		if("D".equals(option)){			// 일 차이 계산
			calDate = (result + 1)/cDay;
		}else if("M".equals(option)){	// 월 차이 계산
			calDate = result/cMonth;
		}
		
		calDate = Math.abs(calDate);
		
		return (int)calDate;
	}

	
	
	/**
	 * 입력된 날짜의 이전 또는 이후를 계산한다.
	 *   
	 * @param inputDate 입력날짜
	 * @param option 계산옵션 (D : 일  , M : 월)
	 * @param flow 흐른 날짜, (예)옵션이 D이고 2를 입력했다면 2일 후, -2를 입력하면 2일 전
	 * @return calDate 날짜계산 결과
	 */
	public static String calAfterOrBeforeOfDate(String inputDate, String option, int flow) throws Exception{
		  
		SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss");

		Date inDate = formatter.parse(inputDate);
		
		Calendar cal = Calendar.getInstance();
        cal.setTime(inDate);
        
        if("D".equals(option)){
        	cal.add(Calendar.DATE, flow);
		}else if("M".equals(option)){			
			cal.add(Calendar.MONTH, flow);
		}
        // 계산한 날짜를 문자열로 
        String resultDate = formatter.format(cal.getTime());

		return resultDate;
	}

	
}
