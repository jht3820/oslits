
/* 함수 생성 자바 파일 컴파일 */
javac java파일명 -encoding UTF-8


/* 커맨드 창에서 함수 class 파일이 존재하는 경로로 이동 (설치 경로\CUBRID\java 경로에 CLASS 파일 옮겨 놓을 것) */
/* 다음 명령어 실행 */
loadjava OSLITSDB 자바파일.class
(예) loadjava OSLITSDB SfAdm.class


/* DB에 java stored function 추가 */
CREATE FUNCTION SF_ADM1000_GET_NEW_MENU_ID (LIC_GRP_ID VARCHAR, MENU_ID VARCHAR) RETURN VARCHAR
AS LANGUAGE JAVA NAME 'SfAdm.sfAdm1000GetNewMenuId(java.lang.String, java.lang.String) return java.lang.String';

CREATE FUNCTION SF_ADM1000_MENU_NM (LIC_GRP_ID VARCHAR, MENU_ID VARCHAR, RTN_GB VARCHAR) RETURN VARCHAR
AS LANGUAGE JAVA NAME 'SfAdm.sfAdm1000MenuNm(java.lang.String, java.lang.String, java.lang.String) return java.lang.String';

CREATE FUNCTION SF_ADM2000_USR_INFO (USR_ID VARCHAR, USR_INFO_CODE VARCHAR) RETURN VARCHAR
AS LANGUAGE JAVA NAME 'SfAdm.sfAdm2000UsrInfo(java.lang.String, java.lang.String) return java.lang.String';

CREATE FUNCTION SF_ADM3000_LIC_INFO (LIC_GRP_ID VARCHAR, RTN_GB VARCHAR) RETURN VARCHAR
AS LANGUAGE JAVA NAME 'SfAdm.sfAdm3000LicInfo(java.lang.String, java.lang.String) return java.lang.String';

CREATE FUNCTION SF_ADM4000_MST_CD_NM (LIC_GRP_ID VARCHAR, MST_CD VARCHAR) RETURN VARCHAR
AS LANGUAGE JAVA NAME 'SfAdm.sfAdm4000MstCdNm(java.lang.String, java.lang.String) return java.lang.String';

CREATE FUNCTION SF_ADM4100_COM_CD_INFO (LIC_GRP_ID VARCHAR, MST_CD VARCHAR, SUB_CD VARCHAR, RTN_GB VARCHAR) RETURN VARCHAR
AS LANGUAGE JAVA NAME 'SfAdm.sfAdm4100ComCdInfo(java.lang.String, java.lang.String, java.lang.String, java.lang.String) return java.lang.String';

CREATE FUNCTION SF_ADM7000_DEPT_NM (LIC_GRP_ID VARCHAR, DEPT_ID VARCHAR, RTN_GB VARCHAR) RETURN VARCHAR
AS LANGUAGE JAVA NAME 'SfAdm.sfAdm7000DeptNm(java.lang.String, java.lang.String, java.lang.String) return java.lang.String';

CREATE FUNCTION SF_PRJ1000_GRP_NM (PRJ_GRP_ID VARCHAR) RETURN VARCHAR
AS LANGUAGE JAVA NAME 'SfPrj.sfPrj1000GrpNm(java.lang.String) return java.lang.String';

CREATE FUNCTION SF_PRJ3000_DOC_NM (LIC_GRP_ID VARCHAR,  PRJ_ID VARCHAR, DOC_ID VARCHAR, DOC_INFO_CODE VARCHAR) RETURN VARCHAR
AS LANGUAGE JAVA NAME 'SfPrj.sfPrj3000DocNm(java.lang.String, java.lang.String, java.lang.String, java.lang.String) return java.lang.String';

CREATE FUNCTION SF_DPL1000_DPL_INFO (PRJ_ID VARCHAR, DPL_ID VARCHAR, RTN_GB VARCHAR) RETURN VARCHAR
AS LANGUAGE JAVA NAME 'SfDpl.sfDpl1000DplInfo(java.lang.String, java.lang.String, java.lang.String) return java.lang.String';

CREATE FUNCTION SF_DPL1400_LAST_INFO (PRJ_ID VARCHAR, JDPL_ID VARCHAR, EN_ID VARCHAR, JOB_ID VARCHAR,  RTN_GB VARCHAR) RETURN VARCHAR
AS LANGUAGE JAVA NAME 'SfDpl.sfDpl1400LastInfo(java.lang.String, java.lang.String, java.lang.String, java.lang.String, java.lang.String) return java.lang.String';

CREATE FUNCTION SF_REQ4000_REQ_CLS_NM (PRJ_ID VARCHAR, REQ_CLS_ID VARCHAR, RTN_GB VARCHAR) RETURN VARCHAR
AS LANGUAGE JAVA NAME 'SfReq.sfReq4000ReqClsNm(java.lang.String, java.lang.String, java.lang.String) return java.lang.String';

CREATE FUNCTION SF_REQ4100_REQ_INFO (PRJ_ID VARCHAR, REQ_ID VARCHAR, RTN_GB VARCHAR) RETURN VARCHAR
AS LANGUAGE JAVA NAME 'SfReq.sfReq4100ReqInfo(java.lang.String, java.lang.String, java.lang.String) return java.lang.String';

CREATE FUNCTION SF_REQ4100_REQ_PROCESS_RATE (PRJ_ID VARCHAR, PROCESS_ID VARCHAR, ST_DTM VARCHAR, RTN_TYPE VARCHAR) RETURN VARCHAR
AS LANGUAGE JAVA NAME 'SfReq.sfReq4100ReqProcessRate(java.lang.String, java.lang.String, java.lang.String,  java.lang.String) return java.lang.String';

CREATE FUNCTION SF_REQ4100_REQ_UPGRADE_ACT (PRJ_ID VARCHAR, PROCESS_ID VARCHAR, ST_DTM VARCHAR, RTN_TYPE VARCHAR) RETURN VARCHAR
AS LANGUAGE JAVA NAME 'SfReq.sfReq4100ReqUpgradeAct(java.lang.String, java.lang.String, java.lang.String,  java.lang.String) return java.lang.String';

CREATE FUNCTION SF_REPLACE_ALL (P_DATA VARCHAR, P_REGEX VARCHAR, P_REPLACE VARCHAR) RETURN VARCHAR
AS LANGUAGE JAVA NAME 'SfComLuna.sfReplaceAll(java.lang.String, java.lang.String, java.lang.String) return java.lang.String';

CREATE FUNCTION SF_ADM2000_USR_AUTH_GRP_NM(LIC_GRP_ID VARCHAR, PRJ_ID VARCHAR, USR_ID VARCHAR, AUTH_IDS VARCHAR, ACCEPT_USE_CD VARCHAR) RETURN VARCHAR
AS LANGUAGE JAVA NAME 'SfAdm.sfAdm2000UsrAuthGrpNm(java.lang.String, java.lang.String, java.lang.String, java.lang.String, java.lang.String) return java.lang.String';

CREATE FUNCTION SF_ADM1000_MENU_AUTH_SAVE 
(
	LIC_GRP_ID	VARCHAR 
,	PRJ_ID		VARCHAR
,	AUTH_GRP_ID	VARCHAR
,	MENU_ID		VARCHAR
,	ACCESS_YN	VARCHAR
,	SELECT_YN	VARCHAR 
,	REG_YN		VARCHAR
,	MODIFY_YN	VARCHAR 
,	DEL_YN		VARCHAR
,	EXCEL_YN	VARCHAR
,	PRINT_YN	VARCHAR
,	REG_USR_ID	VARCHAR
,	REG_USR_IP	VARCHAR
,	MODIFY_USR_ID	VARCHAR 
,	MODIFY_USR_IP	VARCHAR
) RETURN VARCHAR
AS LANGUAGE JAVA NAME 'SfAdm.sfAdm1000MenuAuthSave(
	java.lang.String
,	java.lang.String
,	java.lang.String
,	java.lang.String
,	java.lang.String
,	java.lang.String
,	java.lang.String
,	java.lang.String
,	java.lang.String
,	java.lang.String
,	java.lang.String
,	java.lang.String
,	java.lang.String
,	java.lang.String 
,	java.lang.String
) return java.lang.String';


CREATE FUNCTION SF_PRJ2000_MENU_AUTH_SAVE 
(
	LIC_GRP_ID	VARCHAR 
,	PRJ_ID		VARCHAR
,	AUTH_GRP_ID	VARCHAR
,	MENU_ID		VARCHAR
,	MAIN_MENU_ID	VARCHAR
,	ACCESS_YN	VARCHAR
,	SELECT_YN	VARCHAR 
,	REG_YN		VARCHAR
,	MODIFY_YN	VARCHAR 
,	DEL_YN		VARCHAR
,	EXCEL_YN	VARCHAR
,	PRINT_YN	VARCHAR
,	REG_USR_ID	VARCHAR
,	REG_USR_IP	VARCHAR
,	MODIFY_USR_ID	VARCHAR 
,	MODIFY_USR_IP	VARCHAR
) RETURN VARCHAR
AS LANGUAGE JAVA NAME 'SfPrj.sfPrj2000MenuAuthSave(
	java.lang.String
,	java.lang.String
,	java.lang.String
,	java.lang.String
,	java.lang.String
,	java.lang.String
,	java.lang.String
,	java.lang.String
,	java.lang.String
,	java.lang.String
,	java.lang.String
,	java.lang.String
,	java.lang.String
,	java.lang.String
,	java.lang.String 
,	java.lang.String
) return java.lang.String';


CREATE FUNCTION SF_PRJ1000_PRJ_INFO_DELETE (LIC_GRP_ID VARCHAR, PRJ_ID VARCHAR) RETURN VARCHAR
AS LANGUAGE JAVA NAME 'SfPrj.sfPrj1000PrjInfoDelete(java.lang.String, java.lang.String) return java.lang.String';

CREATE FUNCTION SF_PRJ1000_PRJ_INFO_INSERT
(
I_PRJ_GRP_CHK	 VARCHAR
,	I_PRJ_GRP_ID	 VARCHAR
,	I_LIC_GRP_ID	VARCHAR
,	I_PRJ_NM		VARCHAR
,	I_START_DT		VARCHAR
,	I_END_DT		VARCHAR
,	I_ORD			VARCHAR
,	I_PRJ_DESC		VARCHAR
,	I_USE_CD		VARCHAR
,	I_PRJ_TYPE		VARCHAR
,	I_PRJ_ACRM		VARCHAR
,	I_REG_USR_ID		VARCHAR
,	I_REG_USR_IP		VARCHAR
,	I_MODIFY_USR_ID		VARCHAR
,	I_MODIFY_USR_IP		VARCHAR
) RETURN VARCHAR
AS LANGUAGE JAVA
NAME 'SfPrj.sfPrj1000PrjInfoInsert(
	java.lang.String
,	java.lang.String
,	java.lang.String
,	java.lang.String
,	java.lang.String
,	java.lang.String
,	java.lang.String
,	java.lang.String
,	java.lang.String
,	java.lang.String
,	java.lang.String
,	java.lang.String
,	java.lang.String
,	java.lang.String
,	java.lang.String
)';
