import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.Statement;


public class SfPrj {
	
	private static final String DB_URL = "jdbc:[your DB IP or URL]:[your DB prot]:[your DB ID]:::?charset=UTF-8";
	private static final String DB_ID = [your DB ID];
	private static final String DB_PW = [your DB PW];
	
	public static String sfPrj1000PrjInfoInsert(String prjGrpChk, String prjGrpId,String licGrpId, String prjNm, String startDt
			, String endDt, String ord, String prjDesc, String useCd, String prjType, String prjAcrm
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
			
			String newPrjId = "";
			String newReqClsId = "";
			int beginAtchId = 0;
			int endAtchId = 0;

			if(prjDesc == null || "".equals("prjDesc")){
				prjDesc = " ";
			}
			
			String sql = 
					" SELECT	COALESCE"
					+"			("
					+"				SUBSTR(NEW_PRJ_ID, 1, 11) || LPAD( (TO_NUMBER(SUBSTR(NEW_PRJ_ID, 12, 5)) + 1) , 5, '0')"
					+"				, 'PRJ' || TO_CHAR(SYSDATE, 'YYYYMMDD') || '00001'"
					+"			) NEW_PRJ_ID"
			        +" FROM"
					+" ("
			        +" 		SELECT		MAX(PRJ_ID)  AS NEW_PRJ_ID"
			        +" 		FROM		PRJ1000 A"
			        +" 		WHERE		1=1"
			        +" 		AND			A.PRJ_ID LIKE 'PRJ' || TO_CHAR(SYSDATE, 'YYYYMMDD') || '%'"
			        +" )";
			
			
			rs = stmt.executeQuery(sql);
			if(rs.next()) {
				newPrjId = rs.getString("NEW_PRJ_ID");
			}
			rs.close();
			
			
			sql =
				" INSERT INTO PRJ1000"
				+" ("	
				+" 		PRJ_ID		,	PRJ_GRP_ID	,	LIC_GRP_ID		,	PRJ_NM			,	START_DT		,	END_DT"	
				+" ,	ORD			,	PRJ_DESC	,	USE_CD			,	PRJ_TYPE		, PRJ_ACRM			,	PRJ_GRP_CD"	
				+" ,	REG_DTM		,	REG_USR_ID  ,	REG_USR_IP		,	MODIFY_DTM		,	MODIFY_USR_ID	,	MODIFY_USR_IP"	
				+" )"	
				+" VALUES"	
				+" ("	
				+" 	 "+String.format("'%s'",newPrjId)
				+" 	,"+String.format("'%s'",prjGrpId)
				+" 	,"+String.format("'%s'",licGrpId)
				+" 	,"+String.format("'%s'",prjNm)
				+" 	,DATE_FORMAT("+String.format("'%s'",startDt)+",'%Y%m%d')"
				+" 	,DATE_FORMAT("+String.format("'%s'",endDt)+",'%Y%m%d')"
				+" 	,"+String.format("'%s'",ord)
				+" 	,"+String.format("'%s'",prjDesc)
				+" 	,"+String.format("'%s'",useCd)
				+" 	,"+String.format("'%s'",prjType)
				+" 	,"+String.format("'%s'",prjAcrm)
				+" 	,'02'"
				+" 	,CURRENT_TIMESTAMP"
				+" 	,"+String.format("'%s'",regUsrId)
				+" 	,"+String.format("'%s'",regUsrIp)
				+" 	,CURRENT_TIMESTAMP"
				+" 	,"+String.format("'%s'",modifyUsrId)
				+" 	,"+String.format("'%s'",modifyUsrIp)
				+" )";	
			
		
			int executeResultCnt = stmt.executeUpdate(sql);
			
			if(executeResultCnt < 1) {
				throw new Exception("저장된 프로젝트 정보가 없습니다.");
			}
			
			sql =
				" INSERT INTO ADM1100"
				+" ("	
				+" 		LIC_GRP_ID, 	PRJ_ID,			AUTH_GRP_ID,		AUTH_GRP_NM,		AUTH_GRP_DESC,		CREATE_DT"	
				+" ,	USE_CD,			ORD,			USR_TYP,			ACCEPT_USE_CD ,		REG_DTM,			REG_USR_ID"	
				+" ,	REG_USR_IP,     MODIFY_DTM,		MODIFY_USR_ID,		MODIFY_USR_IP"	
				+" )"	
				+" SELECT "
				+"			 "+String.format("'%s'",licGrpId)
				+" 			,"+String.format("'%s'",newPrjId)
				+" 			, B.AUTH_GRP_ID"
				+" 			, B.AUTH_GRP_NM"
				+" 			, B.AUTH_GRP_DESC"
				+" 			, B.CREATE_DT"
				+" 			, B.USE_CD"
				+" 			, B.ORD"
				+" 			, B.USR_TYP"
				+" 			, B.ACCEPT_USE_CD"
				+" 			, CURRENT_TIMESTAMP"
				+" 			,"+String.format("'%s'",regUsrId)
				+" 			,"+String.format("'%s'",regUsrIp)
				+" 			,CURRENT_TIMESTAMP"
				+" 			,"+String.format("'%s'",modifyUsrId)
				+" 			,"+String.format("'%s'",modifyUsrIp)
				+" FROM		ADM1100 B"
				+" WHERE	1=1"
				+" AND		B.LIC_GRP_ID = "+String.format("'%s'",licGrpId)
				+" AND		B.PRJ_ID = 'ROOTSYSTEM_PRJ'";
			

			//쿼리 실행
			executeResultCnt = stmt.executeUpdate(sql);
			
			//error check
			if(executeResultCnt < 1) {
				throw new Exception("저장된 권한그룹 정보가 없습니다.\n</br>"+sql);
			}

			//프로젝트 정보 생성 블럭
			sql =
				" INSERT INTO ADM1300"
				+" ("	
				+" 		PRJ_ID, 		AUTH_GRP_ID,	USR_ID"	
				+" ,	REG_DTM,		REG_USR_ID,		REG_USR_IP"	
				+" ,	MODIFY_DTM,		MODIFY_USR_ID,	MODIFY_USR_IP"	
				+" )"	
				+" VALUES"	
				+" ("	
				+" 	 "+String.format("'%s'",newPrjId)
				+" 	,'AUT0000000000001'"
				+" 	,"+String.format("'%s'",regUsrId)
				+" 	,CURRENT_TIMESTAMP"
				+" 	,"+String.format("'%s'",regUsrId)
				+" 	,"+String.format("'%s'",regUsrIp)
				+" 	,CURRENT_TIMESTAMP"
				+" 	,"+String.format("'%s'",modifyUsrId)
				+" 	,"+String.format("'%s'",modifyUsrIp)
				+" )";	
			
			executeResultCnt = stmt.executeUpdate(sql);
			
			//error check
			if(executeResultCnt < 1) {
				throw new Exception("저장된 권한프로젝트별 사용자 정보가 없습니다.");
			}
			
			sql =
				" INSERT INTO ADM1200"
				+" ("	
				+" 		LIC_GRP_ID,		PRJ_ID,					AUTH_GRP_ID,		MENU_ID,		MAIN_YN,	ACCESS_YN"	
				+" ,	SELECT_YN,		REG_YN,					MODIFY_YN,			DEL_YN,			EXCEL_YN"	
				+" ,	PRINT_YN,		USE_CD,	REG_DTM,		REG_USR_ID,			REG_USR_IP"	
				+" ,	MODIFY_DTM,		MODIFY_USR_ID,			MODIFY_USR_IP"	
				+" )"	
				+" SELECT "
				+"			 "+String.format("'%s'",licGrpId)
				+" 			,"+String.format("'%s'",newPrjId)
				+" 			, A.AUTH_GRP_ID"
				+" 			, A.MENU_ID"
				+" 			, A.MAIN_YN"
				+" 			, A.ACCESS_YN"
				+" 			, A.SELECT_YN"
				+" 			, A.REG_YN"
				+" 			, A.MODIFY_YN"
				+" 			, A.DEL_YN"
				+" 			, A.EXCEL_YN"
				+" 			, A.PRINT_YN"
				+" 			, A.USE_CD"
				+" 			, CURRENT_TIMESTAMP"
				+" 			,"+String.format("'%s'",regUsrId)
				+" 			,"+String.format("'%s'",regUsrIp)
				+" 			,CURRENT_TIMESTAMP"
				+" 			,"+String.format("'%s'",modifyUsrId)
				+" 			,"+String.format("'%s'",modifyUsrIp)
				+" FROM		ADM1200 A LEFT JOIN ADM1000 B"
				+" ON		("
				+ "				A.LIC_GRP_ID = B.LIC_GRP_ID"
				+ "				AND A.MENU_ID = B.MENU_ID"
				+ "			)"
				+" WHERE	1=1"
				+" AND		A.LIC_GRP_ID = "+String.format("'%s'",licGrpId)
				+" AND		A.PRJ_ID = 'ROOTSYSTEM_PRJ'"
				+" AND		("
				+ "				B.PRJ_TYPE = "+String.format("'%s'",prjType)
				+ "				OR B.PRJ_TYPE = '03'"
				+ "			)";
			

			executeResultCnt = stmt.executeUpdate(sql);
			
			//error check
			if(executeResultCnt < 1) {
				throw new Exception("메뉴별 접근권한 정보가 없습니다.");
			}
			
			sql = 
				" SELECT	COALESCE"
				+"			("
				+"				SUBSTR(NEW_REQ_CLS_ID, 1, 11) || LPAD( (TO_NUMBER(SUBSTR(NEW_REQ_CLS_ID, 12, 5)) + 1) , 5, '0')"
				+"				, 'CLS' || TO_CHAR(SYSDATE, 'YYYYMMDD') || '00001'"
				+"			) NEW_REQ_CLS_ID"
		        +" FROM"
				+" ("
		        +" 		SELECT		MAX(REQ_CLS_ID)  AS NEW_REQ_CLS_ID"
		        +" 		FROM		REQ4000 A"
		        +" 		WHERE		1=1"
		        +" 		AND			A.PRJ_ID LIKE 'CLS' || TO_CHAR(SYSDATE, 'YYYYMMDD') || '%'"
		        +" )";
			
			rs = stmt.executeQuery(sql);
			if(rs.next()) {
				newReqClsId = rs.getString("NEW_REQ_CLS_ID");
			}
			rs.close();
			
			sql =
				" INSERT INTO REQ4000"
				+" ("	
				+" 		PRJ_ID,			REQ_CLS_ID,			UPPER_REQ_CLS_ID,	REQ_CLS_NM"	
				+" ,	LVL,			ORD,				USE_CD,				REG_DTM"	
				+" ,	REG_USR_ID,		REG_USR_IP,			MODIFY_DTM,			MODIFY_USR_ID"	
				+" ,	MODIFY_USR_IP"	
				+" )"	
				+" VALUES"	
				+" ("	
				+" 	 "+String.format("'%s'",newPrjId)
				+" 	,"+String.format("'%s'",newReqClsId)
				+" 	,NULL"
				+" 	,"+String.format("'%s'",prjNm)
				+" 	,0"
				+" 	,1"
				+" 	,'01'"
				+" 	,CURRENT_TIMESTAMP"
				+" 	,"+String.format("'%s'",regUsrId)
				+" 	,"+String.format("'%s'",regUsrIp)
				+" 	,CURRENT_TIMESTAMP"
				+" 	,"+String.format("'%s'",modifyUsrId)
				+" 	,"+String.format("'%s'",modifyUsrIp)
				+" )";	
			
			executeResultCnt = stmt.executeUpdate(sql);
			
			//error check
			if(executeResultCnt < 1) {
				throw new Exception("메뉴별 접근권한 정보가 없습니다.");
			}

			sql = 
				" SELECT	NEXT_ID"
		        +" FROM		COMTNCOPSEQ"
		        +" WHERE	TABLE_NAME = 'COMTNFILE'";

			
			rs = stmt.executeQuery(sql);
			if(rs.next()) {
				beginAtchId = rs.getInt("NEXT_ID");
			}
			rs.close();
			
			sql = 
				" SELECT	COUNT(0) ATCH_CNT"
		        +" FROM		PRJ3000"
		        +" WHERE	PRJ_ID = 'ROOTSYSTEM_PRJ'";

			
			rs = stmt.executeQuery(sql);
			if(rs.next()) {
				endAtchId = rs.getInt("ATCH_CNT");
			}
			rs.close();
			
			endAtchId = (beginAtchId+(endAtchId*2));
			
			sql =
				" INSERT INTO PRJ3000"
				+" ("	
				+" 		PRJ_ID,						DOC_ID,					UPPER_DOC_ID,		 		DOC_NM,				DOC_DESC"	
				+" ,	DOC_FORM_FILE_ID,			DOC_FORM_FILE_SN,		DOC_ATCH_FILE_ID,			DOC_FILE_SN,		LVL"	
				+" ,	ORD,		  		  		DOC_ED_DTM,	  		  	USE_CD,  			  		REG_DTM,			REG_USR_ID"	
				+" ,	REG_USR_IP,				 	MODIFY_DTM,			 	MODIFY_USR_ID,		  		MODIFY_USR_IP"	
				+" )"	
				+" SELECT "
				+" 			"+String.format("'%s'",newPrjId)
				+" 			, A.DOC_ID"
				+" 			, A.UPPER_DOC_ID"
				+" 			, A.DOC_NM"
				+" 			, A.DOC_DESC"
				+" 			, 'FILE_' || LPAD(("+beginAtchId+"+(ROWNUM-1)+ROWNUM),15, '0')"
				+" 			, A.DOC_FORM_FILE_SN"
				+" 			, 'FILE_' || LPAD(("+beginAtchId+"+(ROWNUM*2)),15, '0')"
				+" 			, A.DOC_FILE_SN"
				+" 			, A.LVL"
				+" 			, A.ORD"
				+" 			, A.DOC_ED_DTM"
				+" 			, A.USE_CD"
				+" 			, CURRENT_TIMESTAMP"
				+" 			,"+String.format("'%s'",regUsrId)
				+" 			,"+String.format("'%s'",regUsrIp)
				+" 			,CURRENT_TIMESTAMP"
				+" 			,"+String.format("'%s'",modifyUsrId)
				+" 			,"+String.format("'%s'",modifyUsrIp)
				+" FROM		PRJ3000 A"
				+" WHERE	1=1"
				+" AND		A.PRJ_ID = 'ROOTSYSTEM_PRJ'";
			
			executeResultCnt = stmt.executeUpdate(sql);
			
			//error check
			if(executeResultCnt < 1) {
				throw new Exception("생성된 프로젝트 표준/품질 산출물 정보가 없습니다.");
			}
			
			sql = 
				"  UPDATE	COMTNCOPSEQ"
		        +" SET"
				+" 			NEXT_ID = ("+endAtchId+"+1)"
		        +" WHERE	TABLE_NAME = 'COMTNFILE'";

			executeResultCnt = stmt.executeUpdate(sql);
			
			//error check
			if(executeResultCnt < 1) {
				throw new Exception("COMTNFILE SEQ 생성에 실패했습니다.");
			}
			
			for(int i=beginAtchId;i<=endAtchId;i++) {
				sql =
					" INSERT INTO COMTNFILE"
					+" ("
					+" 		ATCH_FILE_ID		,			CREAT_DT	,		USE_AT"
					+" )"
					+" VALUES"
					+" ("
					+"		'FILE_' || LPAD("+i+",15, '0')"
					+"	,	CURRENT_TIMESTAMP"
					+"	,	'Y'"
					+" )";
				
				stmt.executeUpdate(sql);
			}
			
			rtnValue = newPrjId;
			
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
	
	
	public static String sfPrj1000PrjInfoDelete(String licGrpId, String prjId) {
		String rtnValue = "";
		Connection conn = null;
		Statement stmt = null;
		ResultSet rs = null;
		String errorMessage = null;
		
		try {
			Class.forName("cubrid.jdbc.driver.CUBRIDDriver");
			conn = DriverManager.getConnection(DB_URL,DB_ID,DB_PW);
			conn.setAutoCommit(false);
			stmt = conn.createStatement();
			
			String sql = "";
			int executeResultCnt = 0;
			
			sql = 
				"  DELETE"
		        +" FROM		ADM1100"
		        +" WHERE	PRJ_ID = "+String.format("'%s'",prjId);

			errorMessage = "권한 그룹 정보를 삭제할 수 없습니다.";
			
			executeResultCnt = stmt.executeUpdate(sql);
			
			sql = 
				"  DELETE"
		        +" FROM		ADM1200"
		        +" WHERE	PRJ_ID = "+String.format("'%s'",prjId)
				+" AND		LIC_GRP_ID = "+String.format("'%s'",licGrpId);

			errorMessage = "메뉴별 접근 권한 정보를 삭제할 수 없습니다.";
			
			executeResultCnt = stmt.executeUpdate(sql);
			
			sql = 
					"  DELETE"
					+" FROM		ADM1300"
					+" WHERE	PRJ_ID = "+String.format("'%s'",prjId);

			errorMessage = "권한프로젝트별 사용자 정보를 삭제할 수 없습니다.";
			
			executeResultCnt = stmt.executeUpdate(sql);
			
			sql = 
					"  DELETE"
					+" FROM		API1100"
					+" WHERE	PRJ_ID = "+String.format("'%s'",prjId);

			errorMessage = "프로젝트 배정 API정보를 삭제할 수 없습니다.";
					
			executeResultCnt = stmt.executeUpdate(sql);
			
			sql = 
					"  DELETE"
					+" FROM		DPL1000"
					+" WHERE	PRJ_ID = "+String.format("'%s'",prjId);

			errorMessage = "배포 정보를 삭제할 수 없습니다.";
			
			executeResultCnt = stmt.executeUpdate(sql);
			
			sql = 
					"  DELETE"
					+" FROM		DPL1100"
					+" WHERE	PRJ_ID = "+String.format("'%s'",prjId);
			
			errorMessage = "배포 요구사항 배정 정보를 삭제할 수 없습니다.";
				
			executeResultCnt = stmt.executeUpdate(sql);
			
			sql = 
					"  DELETE"
					+" FROM		DPL1200"
					+" WHERE	PRJ_ID = "+String.format("'%s'",prjId);

			
			errorMessage = "배포 결재 이력 정보를 삭제할 수 없습니다.";
				
			executeResultCnt = stmt.executeUpdate(sql);
			
			sql = 
					"  DELETE"
					+" FROM		DPL1300"
					+" WHERE	PRJ_ID = "+String.format("'%s'",prjId);

			
			errorMessage = "배포 JOB 목록 정보를 삭제할 수 없습니다.";
				
			executeResultCnt = stmt.executeUpdate(sql);
			
			sql = 
					"  DELETE"
					+" FROM		DPL1400"
					+" WHERE	PRJ_ID = "+String.format("'%s'",prjId);

			errorMessage = "배포 실행이력 정보를 삭제할 수 없습니다.";
				
			executeResultCnt = stmt.executeUpdate(sql);

			sql = 
					"  DELETE"
					+" FROM		DPL1500"
					+" WHERE	PRJ_ID = "+String.format("'%s'",prjId);

			errorMessage = "배포 수정이력 정보를 삭제할 수 없습니다.";
				
			executeResultCnt = stmt.executeUpdate(sql);

			sql = 
					"  DELETE"
					+" FROM		FLW1000"
					+" WHERE	PRJ_ID = "+String.format("'%s'",prjId);

			errorMessage = "프로세스 정보를 삭제할 수 없습니다.";
				
			executeResultCnt = stmt.executeUpdate(sql);

			sql = 
					"  DELETE"
					+" FROM		FLW1100"
					+" WHERE	PRJ_ID = "+String.format("'%s'",prjId);

			errorMessage = "작업흐름 정보를 삭제할 수 없습니다.";
				
			executeResultCnt = stmt.executeUpdate(sql);

			sql = 
					"  DELETE"
					+" FROM		FLW1200"
					+" WHERE	PRJ_ID = "+String.format("'%s'",prjId);

			errorMessage = "작업흐름 항목 정보를 삭제할 수 없습니다.";
				
			executeResultCnt = stmt.executeUpdate(sql);

			sql = 
					"  DELETE"
					+" FROM		FLW1300"
					+" WHERE	PRJ_ID = "+String.format("'%s'",prjId);

			errorMessage = "작업흐름 항목 값 정보를 삭제할 수 없습니다.";
				
			executeResultCnt = stmt.executeUpdate(sql);

			sql = 
					"  DELETE"
					+" FROM		FLW1400"
					+" WHERE	PRJ_ID = "+String.format("'%s'",prjId);

			errorMessage = "작업흐름별 리비전 정보를 삭제할 수 없습니다.";
				
			executeResultCnt = stmt.executeUpdate(sql);

			sql = 
					"  DELETE"
					+" FROM		FLW1500"
					+" WHERE	PRJ_ID = "+String.format("'%s'",prjId);

			errorMessage = "작업흐름 항목 정보를 삭제할 수 없습니다.";
				
			executeResultCnt = stmt.executeUpdate(sql);

			sql = 
					"  DELETE"
					+" FROM		JEN1200"
					+" WHERE	PRJ_ID = "+String.format("'%s'",prjId);

			errorMessage = "JOB 권한정보를 삭제할 수 없습니다.";
				
			executeResultCnt = stmt.executeUpdate(sql);

			sql = 
					"  DELETE"
					+" FROM		JEN1300"
					+" WHERE	PRJ_ID = "+String.format("'%s'",prjId);


			errorMessage = "JOB 허용역할 삭제할 수 없습니다.";
				
			executeResultCnt = stmt.executeUpdate(sql);

			sql = 
					"  DELETE"
					+" FROM		MIL1000"
					+" WHERE	PRJ_ID = "+String.format("'%s'",prjId);

			errorMessage = "마일스톤 정보를 삭제할 수 없습니다.";
				
			executeResultCnt = stmt.executeUpdate(sql);

			sql = 
					"  DELETE"
					+" FROM		PRJ1000"
					+" WHERE	PRJ_ID = "+String.format("'%s'",prjId);

			executeResultCnt = stmt.executeUpdate(sql);

			if(executeResultCnt < 1) {
				errorMessage = "프로젝트 정보를 삭제할 수 없습니다.";
				throw new Exception(errorMessage);
			}
			
			sql = 
					"  DELETE"
					+" FROM		PRJ3000"
					+" WHERE	PRJ_ID = "+String.format("'%s'",prjId);


			errorMessage = "프로젝트 산출물 정보를 삭제할 수 없습니다.";
				
			//쿼리 실행
			executeResultCnt = stmt.executeUpdate(sql);


			sql = 
					"  DELETE"
					+" FROM		REQ4000"
					+" WHERE	PRJ_ID = "+String.format("'%s'",prjId);


			errorMessage = "요구사항 분류 정보를 삭제할 수 없습니다.";
				

			executeResultCnt = stmt.executeUpdate(sql);


			sql = 
					"  DELETE"
					+" FROM		REQ4100"
					+" WHERE	PRJ_ID = "+String.format("'%s'",prjId);


			errorMessage = "요구사항 정보를 삭제할 수 없습니다.";
				

			executeResultCnt = stmt.executeUpdate(sql);


			sql = 
					"  DELETE"
					+" FROM		REQ4300"
					+" WHERE	PRJ_ID = "+String.format("'%s'",prjId);


			errorMessage = "요구사항 코멘트 정보를 삭제할 수 없습니다.";
				

			executeResultCnt = stmt.executeUpdate(sql);


			sql = 
					"  DELETE"
					+" FROM		REQ4400"
					+" WHERE	PRJ_ID = "+String.format("'%s'",prjId);


			errorMessage = "요구사항 결재 정보를 삭제할 수 없습니다.";
				
			executeResultCnt = stmt.executeUpdate(sql);

			sql = 
					"  DELETE"
					+" FROM		REQ4600"
					+" WHERE	PRJ_ID = "+String.format("'%s'",prjId);

			errorMessage = "요구사항 클립보드 정보를 삭제할 수 없습니다.";
				
			executeResultCnt = stmt.executeUpdate(sql);

			sql = 
					"  DELETE"
					+" FROM		REQ4700"
					+" WHERE	PRJ_ID = "+String.format("'%s'",prjId);

			errorMessage = "요구사항 변경 이력 정보를 삭제할 수 없습니다.";
				
			executeResultCnt = stmt.executeUpdate(sql);

			sql = 
					"  DELETE"
					+" FROM		REQ4800"
					+" WHERE	PRJ_ID = "+String.format("'%s'",prjId);

			errorMessage = "요구사항 수정 이력 정보를 삭제할 수 없습니다.";
				
			executeResultCnt = stmt.executeUpdate(sql);

			sql = 
					"  DELETE"
					+" FROM		REQ4900"
					+" WHERE	PRJ_ID = "+String.format("'%s'",prjId);

			errorMessage = "요구사항 결재 정보를 삭제할 수 없습니다.";
				
			executeResultCnt = stmt.executeUpdate(sql);

			sql = 
					"  DELETE"
					+" FROM		RPT1000"
					+" WHERE	PRJ_ID = "+String.format("'%s'",prjId)
					+" AND		LIC_GRP_ID = "+String.format("'%s'",licGrpId);

			errorMessage = "보고서 설정 Master 정보를 삭제할 수 없습니다.";
				
			executeResultCnt = stmt.executeUpdate(sql);

			sql = 
					"  DELETE"
					+" FROM		RPT1100"
					+" WHERE	PRJ_ID = "+String.format("'%s'",prjId)
					+" AND		LIC_GRP_ID = "+String.format("'%s'",licGrpId);

			errorMessage = "보고서 설정 Detail 정보를 삭제할 수 없습니다.";
				
			executeResultCnt = stmt.executeUpdate(sql);

			sql = 
					"  DELETE"
					+" FROM		RPT1200"
					+" WHERE	PRJ_ID = "+String.format("'%s'",prjId)
					+" AND		LIC_GRP_ID = "+String.format("'%s'",licGrpId);

			errorMessage = "보고서 Master 정보를 삭제할 수 없습니다.";
				
			executeResultCnt = stmt.executeUpdate(sql);

			sql = 
					"  DELETE"
					+" FROM		RPT1300"
					+" WHERE	PRJ_ID = "+String.format("'%s'",prjId)
					+" AND		LIC_GRP_ID = "+String.format("'%s'",licGrpId);

			errorMessage = "보고서 Detail 정보를 삭제할 수 없습니다.";
				
			executeResultCnt = stmt.executeUpdate(sql);

			sql = 
					"  DELETE"
					+" FROM		SVN1200"
					+" WHERE	PRJ_ID = "+String.format("'%s'",prjId);

			errorMessage = "SVN 허용 역할 정보를 삭제할 수 없습니다.";
				
			executeResultCnt = stmt.executeUpdate(sql);
			
			rtnValue = "프로젝트 삭제 성공";
			
			conn.commit();
		}catch(Exception e) {
			System.out.println(e.getMessage());
			//오류 메시지 있는 경우
			if(errorMessage != null) {
				rtnValue = errorMessage;
			}
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
	
	

	public static String sfPrj1000GrpNm(String prjGrpId) {
		if((prjGrpId == null || "".equals(prjGrpId))) {
			return " ";
		}
		
		String rtnValue = "";
		Connection conn = null;
		Statement stmt = null;
		ResultSet rs = null;

		
		String sql = 
				"  SELECT	PRJ_NM "
						+" FROM		PRJ1000 "
						+" WHERE	1=1 "
						+" AND		PRJ_ID = "+String.format("'%s'",prjGrpId);
		
		try {
			
			Class.forName("cubrid.jdbc.driver.CUBRIDDriver");
			conn = DriverManager.getConnection(DB_URL,DB_ID,DB_PW);
			stmt = conn.createStatement();

			rs = stmt.executeQuery(sql);
			
			if(rs.next()) {
				rtnValue = rs.getString("PRJ_NM");
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
	

	public static String sfPrj2000MenuAuthSave(String licGrpId, String prjId, String authGrpId, String menuId
			, String mainMenuId, String accessYn, String selectYn, String regYn
			, String modifyYn, String delYn, String excelYn, String printYn
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
					" SELECT SF_ADM1000_MENU_AUTH_SAVE("
					+" 		"+String.format("'%s'",licGrpId)
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
					+" ,	"+String.format("'%s'",regUsrId)
					+" ,	"+String.format("'%s'",regUsrIp)
					+" ,	"+String.format("'%s'",modifyUsrId)
					+" ,	"+String.format("'%s'",modifyUsrIp)
					+" ) FROM db_root";
					
			rs = stmt.executeQuery(sql);
			rs.close();
			
			sql = 
				" UPDATE  ADM1200"
				+" SET"
				+" 		MAIN_YN = DECODE"
				+" ("
				+" "+String.format("'%s'",mainMenuId)
				+" ,"+String.format("'%s'",menuId)
				+" ,'Y', 'N')"
				+" WHERE	1=1"
				+" AND		LIC_GRP_ID = "+String.format("'%s'",licGrpId)
				+" AND		PRJ_ID = "+String.format("'%s'",prjId)
				+" AND		AUTH_GRP_ID = "+String.format("'%s'",authGrpId)
				+" AND		MENU_ID = "+String.format("'%s'",menuId);
			
			int executeResultCnt = stmt.executeUpdate(sql);
			
			if(executeResultCnt < 1) {
				throw new Exception("메인 메뉴 저장에 실패하였습니다.222");
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

	

	public static String sfPrj3000DocNm(String licGrpId, String prjId, String docId, String docInfoCode) {
		if((licGrpId == null || "".equals(licGrpId))
				 || (prjId == null || "".equals(prjId))
				 	|| (docId == null || "".equals(docId))
				 		|| (docInfoCode == null || "".equals(docInfoCode))) {
			return " ";
		}
		
		String rtnValue = "";
		Connection conn = null;
		Statement stmt = null;
		ResultSet rs = null;
		
		
		String docSql[] = new String[3];
		

		docSql[0] = 
				" SELECT	DOC_NM "
						+" FROM		PRJ3000 A INNER JOIN (SELECT PRJ_ID, LIC_GRP_ID FROM PRJ1000) B ON(A.PRJ_ID = B.PRJ_ID) "
						+" WHERE	1=1 "
						+" AND		B.LIC_GRP_ID = "+String.format("'%s'",licGrpId)
						+" AND		B.PRJ_ID = "+String.format("'%s'",prjId)
						+" AND		A.DOC_ID = "+String.format("'%s'",docId);

		docSql[1] = 
				" SELECT	DOC_NM "
						+" FROM		PRJ3000 A INNER JOIN (SELECT PRJ_ID, LIC_GRP_ID FROM PRJ1000) B ON(A.PRJ_ID = B.PRJ_ID) "
						+" WHERE	1=1 "
						+" AND		B.LIC_GRP_ID = "+String.format("'%s'",licGrpId)
						+" AND		B.PRJ_ID = "+String.format("'%s'",prjId)
						+" AND		A.DOC_ID = ("
						+"        					SELECT 	UPPER_DOC_ID"
						+"                          FROM	PRJ3000 A"
						+"                          WHERE	1=1"
						+"                          AND		A.PRJ_ID = B.PRJ_ID"
						+"                          AND		A.DOC_ID = "+String.format("'%s'",docId)
						+"						)";

		docSql[2] = 
				" SELECT	DOC_NM "
						+" FROM		PRJ3000 A INNER JOIN (SELECT PRJ_ID, LIC_GRP_ID FROM PRJ1000) B ON(A.PRJ_ID = B.PRJ_ID) "
						+" WHERE	1=1 "
						+" AND		B.LIC_GRP_ID = "+String.format("'%s'",licGrpId)
						+" AND		B.PRJ_ID = "+String.format("'%s'",prjId)
						+" AND		A.DOC_ID = ("
						+"        					SELECT 	UPPER_DOC_ID"
						+"                          FROM	PRJ3000 A"
						+"                          WHERE	1=1"
						+"                          AND		A.PRJ_ID = B.PRJ_ID"
						+" 							AND		A.DOC_ID = ("
						+"        											SELECT 	UPPER_DOC_ID"
						+"                        							FROM	PRJ3000 A"
						+"                         							WHERE	1=1"
						+"                          						AND		A.PRJ_ID = "+String.format("'%s'",prjId)
						+"                          						AND 	A.DOC_ID= "+String.format("'%s'",docId)
						+"												)"
						+"						)";
		
		try {
			
			Class.forName("cubrid.jdbc.driver.CUBRIDDriver");
			conn = DriverManager.getConnection(DB_URL,DB_ID,DB_PW);
			stmt = conn.createStatement();
			
			String sql = "";
			int rtnDocInfoNum = Integer.parseInt(docInfoCode);
			
			if(rtnDocInfoNum <= docSql.length && rtnDocInfoNum > 0) {
				sql = docSql[rtnDocInfoNum-1];
			}
				
			rs = stmt.executeQuery(sql);
			
			if(rs.next()) {
				rtnValue = rs.getString("DOC_NM");
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
