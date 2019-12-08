

import java.sql.*;


public class SfDpl {
	
	private static final String DB_URL = "jdbc:[your DB IP or URL]:[your DB prot]:[your DB ID]:::?charset=UTF-8";
	private static final String DB_ID = [your DB ID];
	private static final String DB_PW = [your DB PW];
	
	public static String sfDpl1000DplInfo(String prjId, String dplId, String rtnGb) {
		
		if((prjId == null || "".equals(prjId))
				 || (dplId == null || "".equals(dplId))
				 	|| (rtnGb == null || "".equals(rtnGb))) {
			return " ";
		}
		
		String rtnValue = "";
		Connection conn = null;
		Statement stmt = null;
		ResultSet rs = null;
		
		String sql = 
				"  SELECT	Y.DPL_NM "
				+"  		,Y.DPL_VER "
				+"  		,Y.DPL_DT "
				+"  		,Y.DPL_USR_ID "
				+"  		,Y.DPL_DESC "
				+"  		,Y.DPL_STS_CD "
				+"  		,Y.DPL_TYPE_CD "
				+" FROM		DPL1000 Y "
				+" WHERE	1=1 "
				+" AND		Y.PRJ_ID = "+String.format("'%s'",prjId)
				+" AND		Y.DPL_ID = "+String.format("'%s'",dplId);
		
		try {
			
			Class.forName("cubrid.jdbc.driver.CUBRIDDriver");
			conn = DriverManager.getConnection(DB_URL,DB_ID,DB_PW);
			stmt = conn.createStatement();
			
			rs = stmt.executeQuery(sql);
			
			if(rs.next()) {
				String dplNm = rs.getString("DPL_NM");
				String dplVer = rs.getString("DPL_VER");
				String dplDt = rs.getString("DPL_DT");
				String dplUsrId = rs.getString("DPL_USR_ID");
				String dplDesc = rs.getString("DPL_DESC");
				String dplStsCd = rs.getString("DPL_STS_CD");
				String dplTypeCd = rs.getString("DPL_TYPE_CD");
				
				switch (rtnGb) {
				case "1":
					rtnValue = dplNm;
					break;
				case "2":
					rtnValue = dplVer;
					break;
				case "3":
					rtnValue = dplDt;
					break;
				case "4":
					rtnValue = dplUsrId;
					break;
				case "5":
					rtnValue = dplDesc;
					break;
				case "6":
					rtnValue = dplStsCd;
					break;
				case "7":
					rtnValue = dplTypeCd;
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

	
	public static String sfDpl1400LastInfo(String prjId, String dplId, String jenId, String jobId, String rtnGb) {
		
		if((prjId == null || "".equals(prjId))
				 || (dplId == null || "".equals(dplId))
				 	|| (jenId == null || "".equals(jenId))
				 		|| (jobId == null || "".equals(jobId))
				 			|| (rtnGb == null || "".equals(rtnGb))) {
			return " ";
		}
		
		String rtnValue = "";
		Connection conn = null;
		Statement stmt = null;
		ResultSet rs = null;
		
		 String sql = 
			"  SELECT	Y.BLD_NUM "
			+"  		,Y.BLD_RESULT "
			+"  		,Y.BLD_DURATION_TM "
			+"  		,Y.BLD_START_DTM "
			+"  		,Y.BLD_MAIN_NUM "
			+"  		,Y.BLD_CONSOLE_LOG "
			+"  		,Y.BLD_RESULT_MSG "
			+"  		,Y.BLD_SEQ "
			+" FROM		DPL1400 Y "
			+" WHERE	1=1 "
			+" AND		Y.PRJ_ID = "+String.format("'%s'",prjId)
			+" AND		Y.DPL_ID = "+String.format("'%s'",dplId)
			+" AND		Y.JEN_ID = "+String.format("'%s'",jenId)
			+" AND		Y.JOB_ID = "+String.format("'%s'",jobId)
			+" AND		Y.BLD_SEQ = "
			+"				( "
			+"					SELECT	BLD_SEQ "
			+"					FROM	DPL1400 Z "
			+"					WHERE 	1=1 "
			+"					AND 	Z.PRJ_ID = "+String.format("'%s'",prjId)
			+"					AND 	Z.DPL_ID = "+String.format("'%s'",dplId)
			+"					AND 	Z.JEN_ID = "+String.format("'%s'",jenId)
			+"					AND 	Z.JOB_ID = "+String.format("'%s'",jobId)
			+"					ORDER BY BLD_SEQ DESC "
			+"					LIMIT 1 "
			+"				) ";
		try {
			
			Class.forName("cubrid.jdbc.driver.CUBRIDDriver");
			conn = DriverManager.getConnection(DB_URL,DB_ID,DB_PW);
			stmt = conn.createStatement();
			
			rs = stmt.executeQuery(sql);
			
			if(rs.next()) {

				String bldNum = rs.getString("BLD_NUM");
				String bldResult = rs.getString("BLD_RESULT");
				String bldResultMsg = rs.getString("BLD_RESULT_MSG");
				String bldDurationTm = rs.getString("BLD_DURATION_TM");
				String bldStartDtm = rs.getString("BLD_START_DTM");
				String bldMainNum = rs.getString("BLD_MAIN_NUM");
				String bldConsoleLog = rs.getString("BLD_CONSOLE_LOG");
				String bldSeq = rs.getString("BLD_SEQ");

				switch (rtnGb) {
				case "1":
					rtnValue = bldNum;
					break;
				case "2":
					rtnValue = bldResult;
					break;
				case "3":
					rtnValue = bldResultMsg;
					break;
				case "4":
					rtnValue = bldDurationTm;
					break;
				case "5":
					rtnValue = bldStartDtm;
					break;
				case "6":
					rtnValue = bldMainNum;
					break;
				case "7":
					rtnValue = bldConsoleLog;
					break;
				case "8":
					rtnValue = bldSeq;
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
}
