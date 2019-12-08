

import java.sql.*;
import java.util.ArrayList;
import java.util.List;


public class SfAdm {
	
	private static final String DB_URL = "jdbc:[your DB IP or URL]:[your DB prot]:[your DB ID]:::?charset=UTF-8";
	private static final String DB_ID = [your DB ID];
	private static final String DB_PW = [your DB PW];
	
	public static String sfAdm1000GetNewMenuId(String licGrpId, String menuId) {
		
		if((licGrpId == null || "".equals(licGrpId)) || (menuId == null || "".equals(menuId))) {
			return " ";
		}
		
		String rtnValue = "";
		Connection conn = null;
		Statement stmt = null;
		ResultSet rs = null;
		
		sql
		String lvlSql = 
				" SELECT	LVL "
		        +" FROM		ADM1000 A "
				+" WHERE	1=1 "
		        +" AND		A.LIC_GRP_ID = "+String.format("'%s'",licGrpId)
		        +" AND		A.MENU_ID = "+String.format("'%s'",menuId);
		
		String menuIdSql =
		        " FROM		ADM1000 A "
				+" WHERE	1=1 "
		        +" AND		A.LIC_GRP_ID = "+String.format("'%s'",licGrpId)
		        +" AND		A.UPPER_MENU_ID = "+String.format("'%s'",menuId);
		
		try {
			
			Class.forName("cubrid.jdbc.driver.CUBRIDDriver");
			conn = DriverManager.getConnection(DB_URL,DB_ID,DB_PW);
			stmt = conn.createStatement();
			
			rs = stmt.executeQuery(lvlSql);
			
			int lvl = 01;
			
			if(rs.next()) {
				lvl = rs.getInt("lvl");
			
				sqlSelect
				String sqlSelect = "";
				
				if(lvl == 0) {
					sqlSelect = " SELECT	LPAD(NVL(MAX(SUBSTR(MENU_ID,1,4)) + 1 , '0001'), 4, '0') || '00000000' AS MENU_ID ";
				}
				else if(lvl == 1) {
					sqlSelect = " SELECT	LPAD(NVL(MAX(SUBSTR(MENU_ID,1,8)) + 1 , SUBSTR("+String.format("'%s'",menuId)+", 1, 4) || '0001'), 8, '0') || '0000' AS MENU_ID ";
				}
				else if(lvl == 2) {
					sqlSelect = " SELECT	LPAD(NVL(MAX(SUBSTR(MENU_ID,1,12)) + 1 , SUBSTR("+String.format("'%s'",menuId)+", 1, 8) || '0001'), 12, '0') AS MENU_ID ";
				}
				
				menuIdSql = sqlSelect+menuIdSql;
				
				rs.close();
				
				rs = stmt.executeQuery(menuIdSql);
				
				if(rs.next()) {
					rtnValue = rs.getString("MENU_ID");
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
	
	public static String sfAdm1000MenuNm(String licGrpId, String menuId, String rtnGb) {
		if((licGrpId == null || "".equals(licGrpId))
				 || (menuId == null || "".equals(menuId))
				 	|| (rtnGb == null || "".equals(rtnGb))) {
			return " ";
		}
		
		String rtnValue = "";
		Connection conn = null;
		Statement stmt = null;
		ResultSet rs = null;
		
		String gbSql[] = new String[3];
		
		gbSql[0] = 
				" SELECT	MENU_NM "
						+" FROM		ADM1000 A "
						+" WHERE	1=1 "
						+" AND		A.LIC_GRP_ID = "+String.format("'%s'",licGrpId)
						+" AND		A.MENU_ID = "+String.format("'%s'",menuId);
		
		gbSql[1] = 
				" SELECT	MENU_NM "
						+" FROM		ADM1000 A "
						+" WHERE	1=1 "
						+" AND		A.LIC_GRP_ID = "+String.format("'%s'",licGrpId)
						+" AND		A.MENU_ID = ("
						+"        					SELECT 	UPPER_MENU_ID"
						+"                          FROM	ADM1000 A"
						+"                          WHERE	1=1"
						+"                          AND		A.LIC_GRP_ID = "+String.format("'%s'",licGrpId)
						+"                          AND		A.MENU_ID = "+String.format("'%s'",menuId)
						+"						)";
		
		gbSql[2] = 
				" SELECT	MENU_NM "
						+" FROM		ADM1000 A "
						+" WHERE	1=1 "
						+" AND		A.LIC_GRP_ID = "+String.format("'%s'",licGrpId)
						+" AND		A.MENU_ID = ("
						+"        					SELECT 	UPPER_MENU_ID"
						+"                          FROM	ADM1000 A"
						+"                          WHERE	1=1"
						+"                          AND		A.LIC_GRP_ID = "+String.format("'%s'",licGrpId)
						+" 							AND		A.MENU_ID = ("
						+"        											SELECT 	UPPER_MENU_ID"
						+"                        							FROM	ADM1000 A"
						+"                         							WHERE	1=1"
						+"                          						AND		A.LIC_GRP_ID = "+String.format("'%s'",licGrpId)
						+"                          						AND		A.MENU_ID = "+String.format("'%s'",menuId)
						+"												)"
						+"						)";
		
		try {
			Class.forName("cubrid.jdbc.driver.CUBRIDDriver");
			conn = DriverManager.getConnection(DB_URL,DB_ID,DB_PW);
			stmt = conn.createStatement();
			
			String sql = "";
			int rtnGbNum = Integer.parseInt(rtnGb);
			
			if(rtnGbNum <= gbSql.length && rtnGbNum > 0) {
				sql = gbSql[rtnGbNum-1];
			}
				
			rs = stmt.executeQuery(sql);
			
			
			if(rs.next()) {
				rtnValue = rs.getString("MENU_NM");
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
	
	public static String sfAdm1000MenuAuthSave(String licGrpId, String prjId, String authGrpId, String menuId, String accessYn
			, String selectYn, String regYn, String modifyYn, String delYn, String excelYn, String printYn
			, String regUsrId, String regUsrIp, String modifyUsrId, String modifyUsrIp) {
		

		String rtnValue = "";
		Connection conn = null;
		Statement stmt = null;
		ResultSet rs = null;
		
		try {
			Class.forName("cubrid.jdbc.driver.CUBRIDDriver");
			conn = DriverManager.getConnection(DB_URL,DB_ID,DB_PW);
			conn.setAutoCommit(false);
			stmt = conn.createStatement();
			
			String sql = 
					" SELECT	A.UPPER_MENU_ID ,"
					+"("
					+"		SELECT	UPPER_MENU_ID"
					+"		FROM	ADM1000 Z"
					+"		WHERE	Z.LIC_GRP_ID = A.LIC_GRP_ID"
					+"			AND Z.MENU_ID = A.UPPER_MENU_ID"
					+") UPUP_MENU_ID"
			        +" FROM		ADM1000 A "
					+" WHERE	1=1 "
			        +" AND		A.LIC_GRP_ID = "+String.format("'%s'",licGrpId)
			        +" AND		A.MENU_ID = "+String.format("'%s'",menuId);
			
			String upupMenuId = "";
			String upMenuId = "";
			String upupAccessYn = "";
			String upAccessYn = "";
			
			rs = stmt.executeQuery(sql);
			if(rs.next()) {
				upMenuId = rs.getString("UPPER_MENU_ID");
				upupMenuId = rs.getString("UPUP_MENU_ID");
			}
			rs.close();
			
			sql = 
				" MERGE INTO 	ADM1200 A"
				+" USING		db_root B"
				+" ON (			A.LIC_GRP_ID = "+String.format("'%s'",licGrpId)
				+" AND 			A.PRJ_ID = "+String.format("'%s'",prjId)
				+" AND 			A.AUTH_GRP_ID = "+String.format("'%s'",authGrpId)
				+" AND 			A.MENU_ID = "+String.format("'%s'",menuId)
				+" )"
				+" WHEN MATCHED THEN"
				+" 		UPDATE"
				+"  	SET     ACCESS_YN = "+String.format("'%s'",accessYn)
				+" 		,		SELECT_YN = "+String.format("'%s'",selectYn)
				+" 		,		REG_YN = "+String.format("'%s'",regYn)
				+" 		,		MODIFY_YN = "+String.format("'%s'",modifyYn)
				+" 		,		DEL_YN = "+String.format("'%s'",delYn)
				+" 		,		EXCEL_YN = "+String.format("'%s'",excelYn)
				+" 		,		PRINT_YN = "+String.format("'%s'",printYn)
				+" 		,		MODIFY_DTM = CURRENT_TIMESTAMP"
				+" 		,		MODIFY_USR_ID = "+String.format("'%s'",modifyUsrId)
				+" 		,		MODIFY_USR_IP = "+String.format("'%s'",modifyUsrIp)
				+" WHEN NOT MATCHED THEN"
				+" 		INSERT"
				+" 		("
				+" 			LIC_GRP_ID,		PRJ_ID,			AUTH_GRP_ID,	MENU_ID,		ACCESS_YN,"
				+" 			SELECT_YN,		REG_YN,			MODIFY_YN,		DEL_YN,			EXCEL_YN,"
				+" 			PRINT_YN,		USE_CD,			REG_DTM,		REG_USR_ID,		REG_USR_IP,"
				+" 			MODIFY_DTM,		MODIFY_USR_ID,	MODIFY_USR_IP"
				+" 		)"
				+" 		VALUES"
				+" 		( "+String.format("'%s'",licGrpId)
				+" ,	"+String.format("'%s'",prjId)
				+" ,	"+String.format("'%s'",authGrpId)
				+" ,	"+String.format("'%s'",menuId)
				+" ,	"+String.format("'%s'",accessYn)
				+" ,	"+String.format("'%s'",selectYn)
				+" ,	"+String.format("'%s'",regYn)
				+" ,	"+String.format("'%s'",modifyYn)
				+" ,	"+String.format("'%s'",delYn)
				+" ,	"+String.format("'%s'",excelYn)
				+" ,	"+String.format("'%s'",printYn)
				+" ,	'01'"
				+" ,	CURRENT_TIMESTAMP"
				+" ,	"+String.format("'%s'",regUsrId)
				+" ,	"+String.format("'%s'",regUsrIp)
				+" ,	CURRENT_TIMESTAMP"
				+" ,	"+String.format("'%s'",modifyUsrId)
				+" ,	"+String.format("'%s'",modifyUsrIp)
				+" 		)";
			
			int executeResultCnt = stmt.executeUpdate(sql);
			error check
			if(executeResultCnt < 1) {
				throw new Exception("저장된 메뉴 정보가 없습니다.");
			}
			
			
			sql = 
				" SELECT	CASE WHEN COUNT(*) > 0 THEN 'Y' ELSE 'N' END AS ACCESS_YN"
		        +" FROM		ADM1200 A INNER JOIN "
				+" ("
				+" 		SELECT	*"
				+" 		FROM	ADM1000 Y"
				+" 		WHERE	1=1"
				+" 		AND		Y.UPPER_MENU_ID IN"
				+" 				("
				+" 					SELECT	MENU_ID"
				+" 					FROM	ADM1000 Z"
				+" 					WHERE	1=1"
				+" 					AND		Z.UPPER_MENU_ID = "+String.format("'%s'",upupMenuId)
				+" 					AND		Z.LIC_GRP_ID = "+String.format("'%s'",licGrpId)
				+" 				)"
				+" 		AND		Y.LIC_GRP_ID = "+String.format("'%s'",licGrpId)
				+" ) B ON"
				+" 		("
				+" 			A.LIC_GRP_ID = B.LIC_GRP_ID AND"
				+" 			A.MENU_ID = B.MENU_ID"
				+" 		)"
				+" WHERE 1=1"
				+" AND	A.PRJ_ID = "+String.format("'%s'",prjId)
				+" AND	A.AUTH_GRP_ID = "+String.format("'%s'",authGrpId)
				+" AND	A.ACCESS_YN = 'Y'";
			
			
			rs = stmt.executeQuery(sql);
			if(rs.next()) {
				upupAccessYn = rs.getString("ACCESS_YN");
			}
			rs.close();
			
			
			sql = 
				" MERGE INTO 	ADM1200 A"
				+" USING		db_root B"
				+" ON (			A.LIC_GRP_ID = "+String.format("'%s'",licGrpId)
				+" AND 			A.PRJ_ID = "+String.format("'%s'",prjId)
				+" AND 			A.AUTH_GRP_ID = "+String.format("'%s'",authGrpId)
				+" AND 			A.MENU_ID = "+String.format("'%s'",upupMenuId)
				+" )"
				+" WHEN MATCHED THEN"
				+" 		UPDATE"
				+"  	SET     ACCESS_YN = "+String.format("'%s'",upupAccessYn)
				+" 		,		MODIFY_DTM = CURRENT_TIMESTAMP"
				+" 		,		MODIFY_USR_ID = "+String.format("'%s'",modifyUsrId)
				+" 		,		MODIFY_USR_IP = "+String.format("'%s'",modifyUsrIp)
				+" WHEN NOT MATCHED THEN"
				+" 		INSERT"
				+" 		("
				+" 			LIC_GRP_ID,		PRJ_ID,			AUTH_GRP_ID,	MENU_ID,		ACCESS_YN,"
				+" 			USE_CD,			REG_DTM,		REG_USR_ID,		REG_USR_IP,"
				+" 			MODIFY_DTM,		MODIFY_USR_ID,	MODIFY_USR_IP"
				+" 		)"
				+" 		VALUES"
				+" 		( "+String.format("'%s'",licGrpId)
				+" ,	"+String.format("'%s'",prjId)
				+" ,	"+String.format("'%s'",authGrpId)
				+" ,	"+String.format("'%s'",upupMenuId)
				+" ,	"+String.format("'%s'",upupAccessYn)
				+" ,	'01'"
				+" ,	CURRENT_TIMESTAMP"
				+" ,	"+String.format("'%s'",regUsrId)
				+" ,	"+String.format("'%s'",regUsrIp)
				+" ,	CURRENT_TIMESTAMP"
				+" ,	"+String.format("'%s'",modifyUsrId)
				+" ,	"+String.format("'%s'",modifyUsrIp)
				+" 		)";
			
			
			executeResultCnt = stmt.executeUpdate(sql);
			
			error check
			if(executeResultCnt < 1) {
				throw new Exception("저장된 최상위 메뉴 정보가 없습니다.\n</br>"+sql);
			}

			
			sql = 
				" SELECT	CASE WHEN COUNT(*) > 0 THEN 'Y' ELSE 'N' END AS ACCESS_YN"
		        +" FROM		ADM1200 A INNER JOIN "
				+" ("
				+" 		SELECT	*"
				+" 		FROM	ADM1000 Y"
				+" 		WHERE	1=1"
				+" 		AND		Y.MENU_ID IN"
				+" 				("
				+" 					SELECT	MENU_ID"
				+" 					FROM	ADM1000 Z"
				+" 					WHERE	1=1"
				+" 					AND		Z.UPPER_MENU_ID = "+String.format("'%s'",upMenuId)
				+" 					AND		Z.LIC_GRP_ID = "+String.format("'%s'",licGrpId)
				+" 				)"
				+" 		AND		Y.LIC_GRP_ID = "+String.format("'%s'",licGrpId)
				+" ) B ON"
				+" 		("
				+" 			A.LIC_GRP_ID = B.LIC_GRP_ID AND"
				+" 			A.MENU_ID = B.MENU_ID"
				+" 		)"
				+" WHERE 1=1"
				+" AND	A.PRJ_ID = "+String.format("'%s'",prjId)
				+" AND	A.AUTH_GRP_ID = "+String.format("'%s'",authGrpId)
				+" AND	A.ACCESS_YN = 'Y'";
			
			
			rs = stmt.executeQuery(sql);
			if(rs.next()) {
				upAccessYn = rs.getString("ACCESS_YN");
			}
			rs.close();
			
			
			sql = 
				" MERGE INTO 	ADM1200 A"
				+" USING		db_root B"
				+" ON (			A.LIC_GRP_ID = "+String.format("'%s'",licGrpId)
				+" AND 			A.PRJ_ID = "+String.format("'%s'",prjId)
				+" AND 			A.AUTH_GRP_ID = "+String.format("'%s'",authGrpId)
				+" AND 			A.MENU_ID = "+String.format("'%s'",upMenuId)
				+" )"
				+" WHEN MATCHED THEN"
				+" 		UPDATE"
				+"  	SET     ACCESS_YN = "+String.format("'%s'",upAccessYn)
				+" 		,		MODIFY_DTM = CURRENT_TIMESTAMP"
				+" 		,		MODIFY_USR_ID = "+String.format("'%s'",modifyUsrId)
				+" 		,		MODIFY_USR_IP = "+String.format("'%s'",modifyUsrIp)
				+" WHEN NOT MATCHED THEN"
				+" 		INSERT"
				+" 		("
				+" 			LIC_GRP_ID,		PRJ_ID,			AUTH_GRP_ID,	MENU_ID,		ACCESS_YN,"
				+" 			USE_CD,			REG_DTM,		REG_USR_ID,		REG_USR_IP,"
				+" 			MODIFY_DTM,		MODIFY_USR_ID,	MODIFY_USR_IP"
				+" 		)"
				+" 		VALUES"
				+" 		( "+String.format("'%s'",licGrpId)
				+" ,	"+String.format("'%s'",prjId)
				+" ,	"+String.format("'%s'",authGrpId)
				+" ,	"+String.format("'%s'",upMenuId)
				+" ,	"+String.format("'%s'",upAccessYn)
				+" ,	'01'"
				+" ,	CURRENT_TIMESTAMP"
				+" ,	"+String.format("'%s'",regUsrId)
				+" ,	"+String.format("'%s'",regUsrIp)
				+" ,	CURRENT_TIMESTAMP"
				+" ,	"+String.format("'%s'",modifyUsrId)
				+" ,	"+String.format("'%s'",modifyUsrIp)
				+" 		)";
			
			
			executeResultCnt = stmt.executeUpdate(sql);
			
			error check
			if(executeResultCnt < 1) {
				throw new Exception("저장된 중메뉴 정보가 없습니다.\n</br>"+sql);
			}
			
			
			sql = 
				" MERGE INTO 	ADM1200 A"
				+" USING		db_root B"
				+" ON (			A.LIC_GRP_ID = "+String.format("'%s'",licGrpId)
				+" AND 			A.PRJ_ID = "+String.format("'%s'",prjId)
				+" AND 			A.AUTH_GRP_ID = "+String.format("'%s'",authGrpId)
				+" AND 			A.MENU_ID = '000'"
				+" )"
				+" WHEN MATCHED THEN"
				+" 		UPDATE"
				+"  	SET     ACCESS_YN = 'Y'"
				+" 		,		MODIFY_DTM = CURRENT_TIMESTAMP"
				+" 		,		MODIFY_USR_ID = "+String.format("'%s'",modifyUsrId)
				+" 		,		MODIFY_USR_IP = "+String.format("'%s'",modifyUsrIp)
				+" WHEN NOT MATCHED THEN"
				+" 		INSERT"
				+" 		("
				+" 			LIC_GRP_ID,		PRJ_ID,			AUTH_GRP_ID,	MENU_ID,		ACCESS_YN,"
				+" 			USE_CD,			REG_DTM,		REG_USR_ID,		REG_USR_IP,"
				+" 			MODIFY_DTM,		MODIFY_USR_ID,	MODIFY_USR_IP"
				+" 		)"
				+" 		VALUES"
				+" 		( "+String.format("'%s'",licGrpId)
				+" ,	"+String.format("'%s'",prjId)
				+" ,	"+String.format("'%s'",authGrpId)
				+" ,	'000'"
				+" ,	'Y'"
				+" ,	'01'"
				+" ,	CURRENT_TIMESTAMP"
				+" ,	"+String.format("'%s'",regUsrId)
				+" ,	"+String.format("'%s'",regUsrIp)
				+" ,	CURRENT_TIMESTAMP"
				+" ,	"+String.format("'%s'",modifyUsrId)
				+" ,	"+String.format("'%s'",modifyUsrIp)
				+" 		)";
			
			
			executeResultCnt = stmt.executeUpdate(sql);
			
			error check
			if(executeResultCnt < 1) {
				throw new Exception("최상위 솔루션 접근권한 저장에 실패했습니다.\n</br>"+sql);
			}
			
			rtnValue = "메뉴 저장에 성공하였습니다.";
			
			conn.commit();
		}catch(Exception e) {
			System.out.println(e.getMessage());
			rtnValue = e.getMessage();
			try {
				conn.rollback();
			}catch(Exception subE) {
				System.out.println(subE.getMessage());
				rtnValue = subE.getMessage();
			}
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
	
	public static String sfAdm4000MstCdNm(String licGrpId, String mstCd) {
		
		if((licGrpId == null || "".equals(licGrpId))
				|| (mstCd == null || "".equals(mstCd))) {
			return " ";
		}
		
		String rtnValue = "";
		Connection conn = null;
		Statement stmt = null;
		ResultSet rs = null;
		
		
		sql
		String sql = 
				"  SELECT	MST_CD_NM "
				+" FROM		ADM4000 "
				+" WHERE	1=1 "
				+" AND		LIC_GRP_ID = "+String.format("'%s'",licGrpId)
				+" AND		MST_CD = "+String.format("'%s'",mstCd);
		
		try {
			Class.forName("cubrid.jdbc.driver.CUBRIDDriver");
			conn = DriverManager.getConnection(DB_URL,DB_ID,DB_PW);
			stmt = conn.createStatement();
			
			rs = stmt.executeQuery(sql);
			
			if(rs.next()) {
				rtnValue = rs.getString("MST_CD_NM");
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
	
	
	public static String sfAdm2000UsrInfo(String usrId, String usrInfoCode) {
		
		if((usrId == null || "".equals(usrId))
				|| (usrInfoCode == null || "".equals(usrInfoCode))) {
			return " ";
		}
		
		
		String rtnValue = "";
		Connection conn = null;
		Statement stmt = null;
		ResultSet rs = null;
		
		
		sql
		String sql = 
				"  SELECT	USR_NM "
						+"			,EMAIL"
						+"			,TELNO"
						+"			,WK_ST_TM"
						+"			,WK_ED_TM"
						+"			,USR_IMG_ID"
						+" FROM		ADM2000 "
						+" WHERE	1=1 "
						+" AND		USR_ID = "+String.format("'%s'",usrId);
		
		try {
			
			Class.forName("cubrid.jdbc.driver.CUBRIDDriver");
			conn = DriverManager.getConnection(DB_URL,DB_ID,DB_PW);
			stmt = conn.createStatement();
			
			
			rs = stmt.executeQuery(sql);
			
			if(rs.next()) {
				String usrNm = rs.getString("USR_NM");
				String email = rs.getString("EMAIL");
				String telno = rs.getString("TELNO");
				String wkStTm = rs.getString("WK_ST_TM");
				String wkEdTm = rs.getString("WK_ED_TM");
				String usrImgId = rs.getString("USR_IMG_ID");
				
				switch (usrInfoCode) {
				case "1":
					rtnValue = usrNm;
					break;
				case "2":
					rtnValue = email;
					break;
				case "3":
					rtnValue = telno;
					break;
				case "4":
					rtnValue = wkStTm;
					break;
				case "5":
					rtnValue = wkEdTm;
					break;
				case "6":
					rtnValue = usrImgId;
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
	
	
	public static String sfAdm3000LicInfo(String licGrpId, String rtnGb) {
		
		if((licGrpId == null || "".equals(licGrpId))
				|| (rtnGb == null || "".equals(rtnGb))) {
			return " ";
		}
		
		String rtnValue = "";
		Connection conn = null;
		Statement stmt = null;
		ResultSet rs = null;
		
		
		sql
		String sql = 
				"  SELECT	LIC_GRP_ID "
						+"			,LIC_NO"
						+"			,LIC_NM"
						+"			,LIC_START_DT"
						+"			,LIC_END_DT"
						+"			,LIC_GB_CD"
						+"			,PERMIT_USR_CNT"
						+" FROM		ADM3000 "
						+" WHERE	1=1 "
						+" AND		LIC_GRP_ID = "+String.format("'%s'",licGrpId)
						+" AND		LIC_END_DT >= CURRENT_DATE"
						+" ORDER BY LIC_NO DESC";
		
		try {
			Class.forName("cubrid.jdbc.driver.CUBRIDDriver");
			conn = DriverManager.getConnection(DB_URL,DB_ID,DB_PW);
			stmt = conn.createStatement();
			
			rs = stmt.executeQuery(sql);
			
			if(rs.next()) {
				String licNo = rs.getString("LIC_NO");
				String licNm = rs.getString("LIC_NM");
				String licStartDt = rs.getString("LIC_START_DT");
				String licEndDt = rs.getString("LIC_END_DT");
				String permitUsrCnt = rs.getString("PERMIT_USR_CNT");
				
				switch (rtnGb) {
				case "1":
					rtnValue = licNo;
					break;
				case "2":
					rtnValue = licNm;
					break;
				case "3":
					rtnValue = licStartDt;
					break;
				case "4":
					rtnValue = licEndDt;
					break;
				case "5":
					rtnValue = permitUsrCnt;
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
	
	public static String sfAdm4100ComCdInfo(String licGrpId, String mstCd, String subCd, String rtnGb) {
		
		if((licGrpId == null || "".equals(licGrpId))
				|| (mstCd == null || "".equals(mstCd))
				|| (subCd == null || "".equals(subCd))
				|| (rtnGb == null || "".equals(rtnGb))
			) {
			return " ";
		}
		
		String rtnValue = "";
		Connection conn = null;
		Statement stmt = null;
		ResultSet rs = null;
		
		
		sql
		String sql = 
				"  SELECT	SUB_CD_NM "
						+"			,SUB_CD_REF1"
						+"			,SUB_CD_REF2"
						+"			,USE_YN"
						+"			,ORD"
						+" FROM		ADM4100 "
						+" WHERE	1=1 "
						+" AND		LIC_GRP_ID = "+String.format("'%s'",licGrpId)
						+" AND		MST_CD = "+String.format("'%s'",mstCd)
						+" AND		SUB_CD = "+String.format("'%s'",subCd);
		
		try {
			
			Class.forName("cubrid.jdbc.driver.CUBRIDDriver");
			conn = DriverManager.getConnection(DB_URL,DB_ID,DB_PW);
			stmt = conn.createStatement();
			
			rs = stmt.executeQuery(sql);
			
			if(rs.next()) {
				String subCdNm = rs.getString("SUB_CD_NM");
				String subCdRef1 = rs.getString("SUB_CD_REF1");
				String subCdRef2 = rs.getString("SUB_CD_REF2");
				String useYn = rs.getString("USE_YN");
				String ord = rs.getString("ORD");
				
				switch (rtnGb) {
				case "1":
					rtnValue = subCdNm;
					break;
				case "2":
					rtnValue = subCdRef1;
					break;
				case "3":
					rtnValue = subCdRef2;
					break;
				case "4":
					rtnValue = useYn;
					break;
				case "5":
					rtnValue = ord;
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
	
	
	public static String sfAdm7000DeptNm(String licGrpId, String deptId, String rtnGb) {
		
		if((licGrpId == null || "".equals(licGrpId))
				|| (deptId == null || "".equals(deptId))
				|| (rtnGb == null || "".equals(rtnGb))
				) {
			return " ";
		}
		
		String rtnValue = "";
		Connection conn = null;
		Statement stmt = null;
		ResultSet rs = null;
		
		
		sql
		String sql = 
				"  SELECT	DEPT_NAME "
				+" FROM		ADM7000 "
				+" WHERE	1=1 "
				+" AND		LIC_GRP_ID = "+String.format("'%s'",licGrpId)
				+" AND		DEPT_ID = "+String.format("'%s'",deptId);
		
		if("2".equals(rtnGb)) {
			sql = 
				"  SELECT	SUBSTR(SYS_CONNECT_BY_PATH ( DEPT_NAME, ' > '),4) AS DEPT_NAME"
				+" FROM		ADM7000 "
				+" WHERE	1=1 "
				+" AND		LIC_GRP_ID = "+String.format("'%s'",licGrpId)
				+" AND		DEPT_ID = "+String.format("'%s'",deptId)
				+" START WITH DEPT_ID = "
				+"				("
				+"					SELECT	C.DEPT_ID "
				+"					FROM	ADM7000 C "
				+"					WHERE	C.UPPER_DEPT_ID IS NULL"
				+"						AND C.LVL = '0'"
				+"						AND C.LIC_GRP_ID = "+String.format("'%s'",licGrpId)
				+"				)"
				+" CONNECT BY  UPPER_DEPT_ID =  PRIOR  DEPT_ID AND LIC_GRP_ID = "+String.format("'%s'",licGrpId);
		}
		
		try {
			
			Class.forName("cubrid.jdbc.driver.CUBRIDDriver");
			conn = DriverManager.getConnection(DB_URL,DB_ID,DB_PW);
			stmt = conn.createStatement();
			
			
			rs = stmt.executeQuery(sql);
			
			if(rs.next()) {
				rtnValue = rs.getString("DEPT_NAME");
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
	
	
	public static String sfAdm2000UsrAuthGrpNm(String licGrpId, String prjId, String usrId, String authIds, String acceptUseCd) {
		if((licGrpId == null || "".equals(licGrpId))
				|| (prjId == null || "".equals(prjId))
					|| (usrId == null || "".equals(usrId))
				) {
			return " ";
		}
		
		String rtnValue = "";
		Connection conn = null;
		Statement stmt = null;
		ResultSet rs = null;
		
		String sql = 
				"  SELECT		B.USR_ID "
				+"  		, 	B.USR_NM "
				+"  		,	C.AUTH_GRP_NM "
				+" FROM		ADM1300 A "
				+" INNER JOIN ADM2000 B "
				+" ON ( A.USR_ID = B.USR_ID ) "
				+" INNER JOIN ADM1100 C "
				+" ON ( B.LIC_GRP_ID = C.LIC_GRP_ID AND A.AUTH_GRP_ID = C.AUTH_GRP_ID AND A.PRJ_ID = C.PRJ_ID) "
				+" WHERE	1=1 "
				+" AND		B.LIC_GRP_ID = "+String.format("'%s'",licGrpId)
				+" AND		A.PRJ_ID = "+String.format("'%s'",prjId)
				+" AND		B.USE_CD = '01'"
				+" AND		C.USE_CD = '01' "
				+" AND		A.USR_ID = "+String.format("'%s'",usrId);
		
		
		if(authIds != null && !"".equals(authIds)){
			
			String[] authIdArr = authIds.split(",");
			String authIdStr = "";
			
			for(String authId : authIdArr) {
				authIdStr += ", '"+authId+"' ";
			}
			
			authIdStr = authIdStr.substring(1);
			
			sql += " AND	A.AUTH_GRP_ID IN ( " + String.format("%s",authIdStr) + " ) ";
		}
		
		
		if(acceptUseCd != null && !"".equals(acceptUseCd)) {
			sql += " AND	C.ACCEPT_USE_CD = " + String.format("'%s'",acceptUseCd);
		}
		
		sql += " GROUP BY  B.USR_ID, B.USR_NM, C.AUTH_GRP_NM ORDER BY C.ORD";

		try {
			
			Class.forName("cubrid.jdbc.driver.CUBRIDDriver");
			conn = DriverManager.getConnection(DB_URL,DB_ID,DB_PW);
			stmt = conn.createStatement();
			
			rs = stmt.executeQuery(sql);
			
			List<String> authGrpNmList = new ArrayList<String>();
			
			while(rs.next()) {
				authGrpNmList.add(rs.getString("AUTH_GRP_NM"));
			}
			
			String tmpAuthGrpNm = "";
			
			if(authGrpNmList.size() > 0) {
				for(String authGrpNm : authGrpNmList) {
					tmpAuthGrpNm += ", "+authGrpNm;
				}
			}
			
			if(tmpAuthGrpNm.length() > 0) {
				tmpAuthGrpNm = tmpAuthGrpNm.substring(2);
			}
			
			rtnValue = tmpAuthGrpNm;
			
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
