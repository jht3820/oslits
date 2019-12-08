

import java.sql.*;
import java.text.DecimalFormat;


public class SfReq {
	
	private static final String DB_URL = "jdbc:[your DB IP or URL]:[your DB prot]:[your DB ID]:::?charset=UTF-8";
	private static final String DB_ID = [your DB ID];
	private static final String DB_PW = [your DB PW];
	

	public static String sfReq4000ReqClsNm(String prjId, String reqClsId, String rtnGb) {
		if((prjId == null || "".equals(prjId))
				 || (reqClsId == null || "".equals(reqClsId))
				 	|| (rtnGb == null || "".equals(rtnGb))) {
			return " ";
		}
		
		String rtnValue = "";
		Connection conn = null;
		Statement stmt = null;
		ResultSet rs = null;

		String sql = 
				"  SELECT	REQ_CLS_NM "
				+" FROM		REQ4000 "
				+" WHERE	1=1 "
				+" AND		PRJ_ID = "+String.format("'%s'",prjId)
				+" AND		REQ_CLS_ID = "+String.format("'%s'",reqClsId);
		
		if("2".equals(rtnGb)) {
			sql = 
				"  SELECT	SUBSTR(SYS_CONNECT_BY_PATH ( B.REQ_CLS_NM, ' > '),4) AS REQ_CLS_NM "
				+" FROM		( "
				+" 			SELECT	* "
				+" 			FROM   REQ4000 A "
				+" 			WHERE   1 =1 "
				+" 			AND   A.PRJ_ID = "+String.format("'%s'",prjId)
				+" 		) B "
				+" WHERE	1=1 "
				+" AND 		B.REQ_CLS_ID = "+String.format("'%s'",reqClsId)
				+" START WITH REQ_CLS_ID = "
				+"				("
				+"					SELECT	C.REQ_CLS_ID "
				+"					FROM	REQ4000 C "
				+"					WHERE	C.UPPER_REQ_CLS_ID IS NULL"
				+"					AND 	C.LVL = '0'"
				+"					AND 	C.PRJ_ID = "+String.format("'%s'",prjId)
				+"				)"
				+" CONNECT BY  B.UPPER_REQ_CLS_ID =  PRIOR  B.REQ_CLS_ID";
		}
		
		try {
			
			Class.forName("cubrid.jdbc.driver.CUBRIDDriver");
			conn = DriverManager.getConnection(DB_URL,DB_ID,DB_PW);
			stmt = conn.createStatement();

			rs = stmt.executeQuery(sql);
			
			if(rs.next()) {
				rtnValue = rs.getString("REQ_CLS_NM");
			}
			
		}catch(Exception e) {
			System.out.println(e.getMessage());
			rtnValue = e.getMessage();
		}finally {
			try {
				if(rs != null) {
					rs.close();
				}
				if(stmt != null) {
					stmt.close();
				}
				if(conn != null) {
					conn.close();
				}
				
			}catch(Exception e) {
				System.out.println(e.getMessage());
				rtnValue = e.getMessage();
			}
		}
		return rtnValue;
	}


	public static String sfReq4100ReqInfo(String prjId, String reqId, String rtnGb) {
		if((prjId == null || "".equals(prjId))
				 || (reqId == null || "".equals(reqId))
				 	|| (rtnGb == null || "".equals(rtnGb))) {
			return " ";
		}
		
		String rtnValue = "";
		Connection conn = null;
		Statement stmt = null;
		ResultSet rs = null;

		//sql
		String sql = 
				"  SELECT	REQ_NM "
				+" FROM		REQ4100 "
				+" WHERE	1=1 "
				+" AND		PRJ_ID = "+String.format("'%s'",prjId)
				+" AND		REQ_ID = "+String.format("'%s'",reqId);
		
		if("2".equals(rtnGb)) {
			sql = 
				"  SELECT	REQ_NO "
				+" FROM		REQ4100 "
				+" WHERE	1=1 "
				+" AND		PRJ_ID = "+String.format("'%s'",prjId)
				+" AND		REQ_ID = "+String.format("'%s'",reqId);
		}
		
		try {
			
			Class.forName("cubrid.jdbc.driver.CUBRIDDriver");
			conn = DriverManager.getConnection(DB_URL,DB_ID,DB_PW);
			stmt = conn.createStatement();

			rs = stmt.executeQuery(sql);
			
			if(rs.next()) {

				switch (rtnGb) {
				case "1":
					rtnValue = rs.getString("REQ_NM");
					break;
				case "2":
					rtnValue = rs.getString("REQ_NO");
					break;
				}
			}
			
		}catch(Exception e) {
			System.out.println(e.getMessage());
			rtnValue = e.getMessage();
		}finally {
			try {
				if(rs != null) {
					rs.close();
				}
				if(stmt != null) {
					stmt.close();
				}
				if(conn != null) {
					conn.close();
				}
				
			}catch(Exception e) {
				System.out.println(e.getMessage());
				rtnValue = e.getMessage();
			}
		}
		return rtnValue;
	}
	
	
	
	public static String sfReq4100ReqProcessRate(String prjId, String processId, String stDtm, String rtnType) {
		if((prjId == null || "".equals(prjId))
				 || (processId == null || "".equals(processId))
				 	|| (stDtm == null || "".equals(stDtm))
				 		|| (rtnType == null || "".equals(rtnType))) {
			return " ";
		}
		
		String rtnValue = "";
		Connection conn = null;
		Statement stmt = null;
		ResultSet rs = null;

		//sql
		String sql = 
				"  SELECT	START_DT "
				+"  		,END_DT "
				+" FROM		( "
				+" 				SELECT 	LEVEL , "
				+" 						ADD_MONTHS(TRUNC( TO_DATE( "+String.format("'%s'",stDtm)
				+" 							,'YYYYMM')  ,'YYYY'),(LEVEL-1)*6) START_DT "
				+" 						,ADD_MONTHS(TRUNC(TO_DATE( "+String.format("'%s'",stDtm)
				+"							,'YYYYMM')  ,'YYYY'),LEVEL *6) -1 END_DT "
				+" 				FROM    DB_ROOT "
				+"				CONNECT BY LEVEL <=2 "
				+" 			) "
				+" WHERE	1=1 "
				+" AND		TO_DATE( " + String.format("'%s'",stDtm)
				+" 			,'YYYYMM') "
				+" BETWEEN START_DT AND END_DT ";

		
		if("2".equals(rtnType)) {
			sql = 
				"  SELECT	START_DT "
				+"  		,END_DT "
				+" FROM		( "
				+" 				SELECT 	LEVEL , "
				+" 						ADD_MONTHS(TRUNC( TO_DATE( "+String.format("'%s'",stDtm)
				+" 							,'YYYYMM')  ,'YYYY'),(LEVEL-1)*3) START_DT "
				+" 						,ADD_MONTHS(TRUNC(TO_DATE( "+String.format("'%s'",stDtm)
				+"							,'YYYYMM')  ,'YYYY'),LEVEL *3) -1 END_DT "
				+" 				FROM    DB_ROOT "
				+"				CONNECT BY LEVEL <=4 "
				+" 			) "
				+" WHERE	1=1 "
				+" AND		TO_DATE( " + String.format("'%s'",stDtm)
				+" 			,'YYYYMM') "
				+" BETWEEN START_DT AND END_DT ";
		}
		
		try {
			
			Class.forName("cubrid.jdbc.driver.CUBRIDDriver");
			conn = DriverManager.getConnection(DB_URL,DB_ID,DB_PW);
			stmt = conn.createStatement();

			String startDt = "";
			String ednDt = "";
			
			rs = stmt.executeQuery(sql);
			
			if(rs.next()) {
				startDt = rs.getString("START_DT");
				ednDt = rs.getString("END_DT");
			}
			rs.close();
			
			
			sql = 
				"  SELECT	SUM(CASE  WHEN A.REQ_ED_DU_DTM >= A.REQ_ED_DTM THEN 1 ELSE 0 END) AS REQ_SUM "
				+"  		,COUNT(A.REQ_ED_DU_DTM) AS REQ_CNT "
				+" FROM req4100 A "
				+" WHERE	1=1 "
				+" AND		A.PRJ_ID = " + String.format("'%s'",prjId)
				+" AND		A.PROCESS_ID = " + String.format("'%s'",processId)
				+" AND		TO_DATE(TO_CHAR(REQ_ED_DU_DTM, 'yyyy-mm-dd')) "
				+" 			BETWEEN TO_DATE(" + String.format("'%s'",startDt) 
				+"				, 'YYYY-MM-DD')"
				+" 			AND TO_DATE(" + String.format("'%s'",ednDt)
				+"				, 'YYYY-MM-DD')"
				+" AND    REQ_ED_DU_DTM IS NOT NULL ";
			
			String strReqSum = "";
			String strReqCnt = "";

			rs = stmt.executeQuery(sql);
			if(rs.next()) {
				strReqSum = rs.getString("REQ_SUM");
				strReqCnt = rs.getString("REQ_CNT");
			}
			rs.close();
			
			double reqSum = Double.parseDouble(strReqSum);
			double reqCnt = Double.parseDouble(strReqCnt);
			double reqRate = Math.floor((reqSum/reqCnt)*100);

			DecimalFormat deFromat = new DecimalFormat("#.##");

			rtnValue = deFromat.format(reqRate);

		}catch(Exception e) {
			System.out.println(e.getMessage());
			rtnValue = e.getMessage();
		}finally {
			try {
				if(rs != null) {
					rs.close();
				}
				if(stmt != null) {
					stmt.close();
				}
				if(conn != null) {
					conn.close();
				}
				
			}catch(Exception e) {
				System.out.println(e.getMessage());
				rtnValue = e.getMessage();
			}
		}
		return rtnValue;
	}
	
	
	public static String sfReq4100ReqUpgradeAct(String prjId, String processId, String stDtm, String rtnType) {
		if((prjId == null || "".equals(prjId))
				 || (processId == null || "".equals(processId))
				 	|| (stDtm == null || "".equals(stDtm))
				 		|| (rtnType == null || "".equals(rtnType))) {
			return " ";
		}
		
		String rtnValue = "";
		Connection conn = null;
		Statement stmt = null;
		ResultSet rs = null;

		//sql
		String sql = 
				"  SELECT	START_DT "
				+"  		,END_DT "
				+" FROM		( "
				+" 				SELECT 	LEVEL , "
				+" 						ADD_MONTHS(TRUNC( TO_DATE( "+String.format("'%s'",stDtm)
				+" 							,'YYYYMM')  ,'YYYY'),(LEVEL-1)*6) START_DT "
				+" 						,ADD_MONTHS(TRUNC(TO_DATE( "+String.format("'%s'",stDtm)
				+"							,'YYYYMM')  ,'YYYY'),LEVEL *6) -1 END_DT "
				+" 				FROM    DB_ROOT "
				+"				CONNECT BY LEVEL <=2 "
				+" 			) "
				+" WHERE	1=1 "
				+" AND		TO_DATE( " + String.format("'%s'",stDtm)
				+" 			,'YYYYMM') "
				+" BETWEEN START_DT AND END_DT ";

		
		if("2".equals(rtnType)) {
			sql = 
				"  SELECT	START_DT "
				+"  		,END_DT "
				+" FROM		( "
				+" 				SELECT 	LEVEL , "
				+" 						ADD_MONTHS(TRUNC( TO_DATE( "+String.format("'%s'",stDtm)
				+" 							,'YYYYMM')  ,'YYYY'),(LEVEL-1)*3) START_DT "
				+" 						,ADD_MONTHS(TRUNC(TO_DATE( "+String.format("'%s'",stDtm)
				+"							,'YYYYMM')  ,'YYYY'),LEVEL *3) -1 END_DT "
				+" 				FROM    DB_ROOT "
				+"				CONNECT BY LEVEL <=4 "
				+" 			) "
				+" WHERE	1=1 "
				+" AND		TO_DATE( " + String.format("'%s'",stDtm)
				+" 			,'YYYYMM') "
				+" BETWEEN START_DT AND END_DT ";
		}
		
		try {
			
			Class.forName("cubrid.jdbc.driver.CUBRIDDriver");
			conn = DriverManager.getConnection(DB_URL,DB_ID,DB_PW);
			stmt = conn.createStatement();

			String startDt = "";
			String ednDt = "";
			
			rs = stmt.executeQuery(sql);
			
			if(rs.next()) {
				startDt = rs.getString("START_DT");
				ednDt = rs.getString("END_DT");
			}
			rs.close();
			

			sql = 
				"  SELECT	COUNT(1) AS REQ_PIA_CNT "
				+" FROM 	REQ4100 A "
				+" WHERE	1=1 "
				+" AND		A.PRJ_ID = " + String.format("'%s'",prjId)
				+" AND		TO_DATE(TO_CHAR(REQ_ED_DU_DTM, 'yyyy-mm-dd')) "
				+" 			BETWEEN TO_DATE(" + String.format("'%s'",startDt) 
				+"				, 'YYYY-MM-DD')"
				+" 			AND TO_DATE(" + String.format("'%s'",ednDt)
				+"				, 'YYYY-MM-DD')"
				+" AND    	REQ_ED_DU_DTM IS NOT NULL "
				+" AND 		PIA_CD = '01' ";
			
			String strReqPiaCnt = "";

			rs = stmt.executeQuery(sql);
			if(rs.next()) {
				strReqPiaCnt = rs.getString("REQ_PIA_CNT");
			}
			rs.close();
			
			
			sql = 
				"  SELECT	SUM(CASE  WHEN A.REQ_ED_DU_DTM >= A.REQ_ED_DTM THEN 1 ELSE 0 END) AS REQ_SUM "
				+" FROM 	REQ4100 A "
				+" WHERE	1=1 "
				+" AND		A.PRJ_ID = " + String.format("'%s'",prjId)
				+" AND		TO_DATE(TO_CHAR(REQ_ED_DU_DTM, 'yyyy-mm-dd')) "
				+" 			BETWEEN TO_DATE(" + String.format("'%s'",startDt) 
				+"				, 'YYYY-MM-DD')"
				+" 			AND TO_DATE(" + String.format("'%s'",ednDt)
				+"				, 'YYYY-MM-DD')"
				+" AND    REQ_ED_DU_DTM IS NOT NULL ";
			
			
			String strReqSum = "";

			rs = stmt.executeQuery(sql);
			if(rs.next()) {
				strReqSum = rs.getString("REQ_SUM");
			}
			rs.close();			
			
			
			sql = 
				"  SELECT	SUM( LAB_INP ) AS REQ_LAB_INP "
				+" FROM 	REQ4100 A "
				+" WHERE	1=1 "
				+" AND		A.PRJ_ID = " + String.format("'%s'",prjId)
				+" AND		TO_DATE(TO_CHAR(REQ_ED_DU_DTM, 'yyyy-mm-dd')) "
				+" 			BETWEEN TO_DATE(" + String.format("'%s'",startDt) 
				+"				, 'YYYY-MM-DD')"
				+" 			AND TO_DATE(" + String.format("'%s'",ednDt)
				+"				, 'YYYY-MM-DD')"
				+" AND    REQ_ED_DU_DTM IS NOT NULL ";			
			
			String strReqLabInp = "";

			rs = stmt.executeQuery(sql);
			if(rs.next()) {
				strReqLabInp = rs.getString("REQ_LAB_INP");
			}
			rs.close();	
			
			double reqPiaCnt = Double.parseDouble(strReqPiaCnt);
			double reqSum = Double.parseDouble(strReqSum);
			double reqLabInp = Double.parseDouble(strReqLabInp);
			double temVal =   ((reqPiaCnt/reqLabInp) * 0.7 + (reqSum/reqPiaCnt )*0.3) * 10;
			
			DecimalFormat deFromat = new DecimalFormat("#.##");
			rtnValue = deFromat.format(temVal);

		}catch(Exception e) {
			System.out.println(e.getMessage());
			rtnValue = e.getMessage();
		}finally {
			try {
				if(rs != null) {
					rs.close();
				}
				if(stmt != null) {
					stmt.close();
				}
				if(conn != null) {
					conn.close();
				}
				
			}catch(Exception e) {
				System.out.println(e.getMessage());
				rtnValue = e.getMessage();
			}
		}
		return rtnValue;
	}
	
}
