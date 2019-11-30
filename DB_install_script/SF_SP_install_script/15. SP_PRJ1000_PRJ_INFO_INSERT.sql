
CREATE OR REPLACE PROCEDURE SP_PRJ1000_PRJ_INFO_INSERT
(
		I_PRJ_GRP_CHK	IN VARCHAR2
	,	I_PRJ_GRP_ID	IN VARCHAR2
   	,	I_LIC_GRP_ID	IN	VARCHAR2
	,	I_PRJ_NM		IN	VARCHAR2
    ,	I_START_DT		IN	VARCHAR2
	,	I_END_DT		IN	VARCHAR2

	,	I_ORD			IN	VARCHAR2
	,	I_PRJ_DESC		IN	VARCHAR2
	,	I_USE_CD		IN	VARCHAR2
    ,	I_PRJ_TYPE		IN  VARCHAR2
    , I_PRJ_ACRM		IN  VARCHAR2
	,	I_REG_USR_ID	IN	VARCHAR2
	,	I_REG_USR_IP	IN	VARCHAR2

	,	I_MODIFY_USR_ID	IN	VARCHAR2
	,	I_MODIFY_USR_IP	IN	VARCHAR2
    ,	O_PRJ_ID		OUT	VARCHAR2
    ,	O_CODE			OUT	VARCHAR2
    ,	O_MSG			OUT	VARCHAR2
)


IS

		V_NEW_PRJ_ID 		VARCHAR2(16);
		V_NEW_REQ_CLS_ID	VARCHAR2(16);

    V_BEGIN_ATCH_ID		NUMBER;
    V_END_ATCH_ID		NUMBER;

BEGIN


    O_CODE := '-1';
    O_MSG := '초기화';


    BEGIN


    	SELECT	NVL(
                        SUBSTR(NEW_PRJ_ID, 1, 11) || LPAD( (TO_NUMBER(SUBSTR(NEW_PRJ_ID, 12, 5)) + 1) , 5, '0')
                    ,	'PRJ' || TO_CHAR(SYSDATE, 'YYYYMMDD') || '00001'
                ) AS NEW_PRJ_ID
        INTO	V_NEW_PRJ_ID
        FROM	(
                    SELECT	MAX(PRJ_ID)  AS NEW_PRJ_ID
                    FROM	PRJ1000 A
                    WHERE	1=1
                    AND		A.PRJ_ID LIKE 'PRJ' || TO_CHAR(SYSDATE, 'YYYYMMDD') || '%'
                )
        ;

    EXCEPTION
    	WHEN NO_DATA_FOUND THEN
          O_CODE := '-1';
          O_MSG := '생성할 PRJ_ID를 만들수 없습니다.';
          ROLLBACK;
          RETURN;
    END;


	BEGIN

    	INSERT INTO PRJ1000 A
        (
                PRJ_ID		,	PRJ_GRP_ID	,	LIC_GRP_ID		,	PRJ_NM			,	START_DT		,	END_DT
            ,	ORD			,	PRJ_DESC	,	USE_CD			,	PRJ_TYPE	, PRJ_ACRM	,	PRJ_GRP_CD 		,	REG_DTM
            ,	REG_USR_ID  ,	REG_USR_IP	,	MODIFY_DTM		,	MODIFY_USR_ID	,	MODIFY_USR_IP
        )VALUES(
                V_NEW_PRJ_ID,	I_PRJ_GRP_ID,	I_LIC_GRP_ID	,	I_PRJ_NM    	,	REPLACE(I_START_DT, '-', '')		,	REPLACE(I_END_DT, '-', '')
            ,	I_ORD		,	I_PRJ_DESC	,	I_USE_CD		,	I_PRJ_TYPE	,I_PRJ_ACRM 		,	'02'			,	SYSDATE
            ,	I_REG_USR_ID,	I_REG_USR_IP,	SYSDATE			,	I_MODIFY_USR_ID ,	I_MODIFY_USR_ID
        );

        IF SQL%FOUND = FALSE THEN
        	O_CODE := '-1';
            O_MSG := '저장된 프로젝트 정보가 없습니다.';
            ROLLBACK;
        	RETURN;
        END IF;

    EXCEPTION
    	WHEN OTHERS THEN
        	O_CODE := '-1';
            O_MSG := SQLCODE || ' : ' || SQLERRM;
            DBMS_OUTPUT.PUT_LINE('예기치 못한 오류 발생' || CHR(13) || SQLCODE || ' : ' || SQLERRM);
            ROLLBACK;
        	RETURN;
    END;



	BEGIN

    	INSERT INTO ADM1100 A
        (
            LIC_GRP_ID,	PRJ_ID,		AUTH_GRP_ID,	AUTH_GRP_NM,	AUTH_GRP_DESC,		CREATE_DT,
            USE_CD,		ORD,	USR_TYP,		REG_DTM,		REG_USR_ID,			REG_USR_IP,
            MODIFY_DTM,	MODIFY_USR_ID,	MODIFY_USR_IP
        )
        SELECT	I_LIC_GRP_ID
        	,	V_NEW_PRJ_ID
            ,	B.AUTH_GRP_ID
            ,	B.AUTH_GRP_NM
            ,	B.AUTH_GRP_DESC
            ,	B.CREATE_DT
            ,	B.USE_CD
            ,	B.ORD
            , B.USR_TYP
            ,	SYSDATE
            ,	I_REG_USR_ID
            ,	I_REG_USR_IP
            ,	SYSDATE
            ,	I_MODIFY_USR_ID
            ,	I_MODIFY_USR_IP
        FROM	ADM1100 B
        WHERE	1=1
        AND  	B.LIC_GRP_ID = I_LIC_GRP_ID
        AND		B.PRJ_ID = 'ROOTSYSTEM_PRJ'
        ;

        IF SQL%FOUND = FALSE THEN
        	O_CODE := '-1';
            O_MSG := '저장된 권한그룹 정보가 없습니다.';
            ROLLBACK;
        	RETURN;
        END IF;

    EXCEPTION
    	WHEN OTHERS THEN
        	O_CODE := '-1';
            O_MSG := SQLCODE || ' : ' || SQLERRM;
            DBMS_OUTPUT.PUT_LINE('예기치 못한 오류 발생' || CHR(13) || SQLCODE || ' : ' || SQLERRM);
            ROLLBACK;
        	RETURN;
    END;


	BEGIN

    	INSERT INTO ADM1300 A
        (
            PRJ_ID, 		AUTH_GRP_ID,	USR_ID,
            REG_DTM,		REG_USR_ID,		REG_USR_IP,
            MODIFY_DTM,		MODIFY_USR_ID,	MODIFY_USR_IP
        )
        VALUES
        (
            V_NEW_PRJ_ID,	'AUT0000000000001',				I_REG_USR_ID,
            SYSDATE,		I_REG_USR_ID,		I_REG_USR_IP,
            SYSDATE,		I_MODIFY_USR_ID,	I_MODIFY_USR_IP
        );

        IF SQL%FOUND = FALSE THEN
        	O_CODE := '-1';
            O_MSG := '저장된 권한프로젝트별 사용자 정보가 없습니다.';
            ROLLBACK;
        	RETURN;
        END IF;

    EXCEPTION
    	WHEN OTHERS THEN
        	O_CODE := '-1';
            O_MSG := SQLCODE || ' : ' || SQLERRM;
            DBMS_OUTPUT.PUT_LINE('예기치 못한 오류 발생' || CHR(13) || SQLCODE || ' : ' || SQLERRM);
            ROLLBACK;
        	RETURN;
    END;


	BEGIN

    	INSERT INTO ADM1200 A
        (
            LIC_GRP_ID,		PRJ_ID,			AUTH_GRP_ID,	MENU_ID,	MAIN_YN,	ACCESS_YN,
            SELECT_YN,		REG_YN,			MODIFY_YN,		DEL_YN,			EXCEL_YN,
            PRINT_YN,		USE_CD,	REG_DTM,		REG_USR_ID,		REG_USR_IP,
            MODIFY_DTM,		MODIFY_USR_ID,	MODIFY_USR_IP
        )
        SELECT	I_LIC_GRP_ID,	V_NEW_PRJ_ID,	A.AUTH_GRP_ID,	A.MENU_ID, A.MAIN_YN,		A.ACCESS_YN,
                A.SELECT_YN,		A.REG_YN,			A.MODIFY_YN,		A.DEL_YN,			A.EXCEL_YN,
                A.PRINT_YN,		A.USE_CD,	SYSDATE,		I_REG_USR_ID,	I_REG_USR_IP,
                SYSDATE,		I_MODIFY_USR_ID,I_MODIFY_USR_IP
        FROM	ADM1200 A  , ADM1000 B
        WHERE	1=1
        AND		A.LIC_GRP_ID = I_LIC_GRP_ID
        AND		A.PRJ_ID = 'ROOTSYSTEM_PRJ'
        AND  A.LIC_GRP_ID = B.LIC_GRP_ID
        AND  A.MENU_ID = B.MENU_ID
				AND  ( B.PRJ_TYPE = I_PRJ_TYPE  OR    B.PRJ_TYPE = '03' ) -- 03 공통
        ;

        IF SQL%FOUND = FALSE THEN
        	O_CODE := '-1';
            O_MSG := '메뉴별 접근권한 정보가 없습니다.';
            ROLLBACK;
        	RETURN;
        END IF;

    EXCEPTION
    	WHEN OTHERS THEN
        	O_CODE := '-1';
            O_MSG := SQLCODE || ' : ' || SQLERRM;
            DBMS_OUTPUT.PUT_LINE('예기치 못한 오류 발생' || CHR(13) || SQLCODE || ' : ' || SQLERRM);
            ROLLBACK;
        	RETURN;
    END;


	BEGIN

        SELECT	NVL(
                        SUBSTR(NEW_REQ_CLS_ID, 1, 11) || LPAD( (TO_NUMBER(SUBSTR(NEW_REQ_CLS_ID, 12, 5)) + 1) , 5, '0')
                    ,	'CLS' || TO_CHAR(SYSDATE, 'YYYYMMDD') || '00001'
                ) AS NEW_REQ_CLS_ID
        INTO	V_NEW_REQ_CLS_ID
        FROM	(
                    SELECT	MAX(REQ_CLS_ID)  AS NEW_REQ_CLS_ID
                    FROM	REQ4000 A
                    WHERE	1=1
                    AND		A.PRJ_ID = V_NEW_PRJ_ID
                    AND		A.REQ_CLS_ID LIKE 'CLS' || TO_CHAR(SYSDATE, 'YYYYMMDD') || '%'
                )
        ;


        INSERT INTO REQ4000 A
        (
            PRJ_ID,			REQ_CLS_ID,			UPPER_REQ_CLS_ID,	REQ_CLS_NM,
            LVL,			ORD,				USE_CD,				REG_DTM,
            REG_USR_ID,		REG_USR_IP,			MODIFY_DTM,			MODIFY_USR_ID,
            MODIFY_USR_IP
        )
        VALUES
        (
        	V_NEW_PRJ_ID,	V_NEW_REQ_CLS_ID,	NULL,				I_PRJ_NM,
            0,				1,					'01',				SYSDATE,
            I_REG_USR_ID,	I_REG_USR_IP,		SYSDATE,			I_MODIFY_USR_ID,
            I_MODIFY_USR_IP
        )

        ;

        IF SQL%FOUND = FALSE THEN
        	O_CODE := '-1';
            O_MSG := '메뉴별 접근권한 정보가 없습니다.';
            ROLLBACK;
        	RETURN;
        END IF;

    EXCEPTION
    	WHEN OTHERS THEN
        	O_CODE := '-1';
            O_MSG := SQLCODE || ' : ' || SQLERRM;
            DBMS_OUTPUT.PUT_LINE('예기치 못한 오류 발생' || CHR(13) || SQLCODE || ' : ' || SQLERRM);
            ROLLBACK;
        	RETURN;
    END;



    BEGIN
    	SELECT NEXT_ID INTO V_BEGIN_ATCH_ID
        FROM COMTNCOPSEQ A
        WHERE A.TABLE_NAME = 'COMTNFILE';

        SELECT COUNT(0) INTO V_END_ATCH_ID
        FROM	PRJ3000 A
        WHERE	A.PRJ_ID = 'ROOTSYSTEM_PRJ'
        ;


        V_END_ATCH_ID := (V_BEGIN_ATCH_ID+(V_END_ATCH_ID*2));

    EXCEPTION
    WHEN OTHERS THEN
        O_CODE := '-1';
        O_MSG := SQLCODE || ' : ' || SQLERRM;
        DBMS_OUTPUT.PUT_LINE('예기치 못한 오류 발생' || CHR(13) || SQLCODE || ' : ' || SQLERRM);
        ROLLBACK;
        RETURN;
    END;


	BEGIN

        INSERT INTO PRJ3000 A
        (
            PRJ_ID,							  DOC_ID,						  UPPER_DOC_ID,		 		DOC_NM,				DOC_DESC,
            DOC_FORM_FILE_ID,			DOC_FORM_FILE_SN,		DOC_ATCH_FILE_ID,		DOC_FILE_SN,	LVL,
            ORD,		  		  		  DOC_ED_DTM,	  		  USE_CD,  			  		REG_DTM,			REG_USR_ID,
            REG_USR_IP,				 		MODIFY_DTM,			 		MODIFY_USR_ID,		  MODIFY_USR_IP
        )
        SELECT
        		V_NEW_PRJ_ID,			DOC_ID,			  	UPPER_DOC_ID,			DOC_NM,		DOC_DESC,
            'FILE_' || LPAD((V_BEGIN_ATCH_ID+(ROWNUM-1)+ROWNUM),15, '0') AS DOC_FORM_FILE_ID,
            DOC_FORM_FILE_SN,
            'FILE_' || LPAD((V_BEGIN_ATCH_ID+(ROWNUM*2)),15, '0') AS DOC_ATCH_FILE_ID,
            DOC_FILE_SN,	LVL,
            ORD,				  		DOC_ED_DTM, 		USE_CD,		  			SYSDATE,	I_REG_USR_ID,
            I_REG_USR_IP,			SYSDATE,			  I_MODIFY_USR_ID,	I_MODIFY_USR_IP
        FROM	PRJ3000 A
        WHERE	1=1
        AND		A.PRJ_ID = 'ROOTSYSTEM_PRJ'
	    	;

        IF SQL%FOUND = FALSE THEN
        	O_CODE := '-1';
            O_MSG := '생성된 프로젝트 표준/품질 산출물 정보가 없습니다.';
            ROLLBACK;
        	RETURN;
        END IF;

    EXCEPTION
    	WHEN OTHERS THEN
        	O_CODE := '-1';
            O_MSG := SQLCODE || ' : ' || SQLERRM;
            DBMS_OUTPUT.PUT_LINE('예기치 못한 오류 발생' || CHR(13) || SQLCODE || ' : ' || SQLERRM);
            ROLLBACK;
        	RETURN;
    END;

    BEGIN
    	UPDATE COMTNCOPSEQ
        SET NEXT_ID = (V_END_ATCH_ID+1)
        WHERE TABLE_NAME = 'COMTNFILE';

    EXCEPTION
        WHEN OTHERS THEN
            O_CODE := '-1';
              O_MSG := SQLCODE || ' : ' || SQLERRM;
              DBMS_OUTPUT.PUT_LINE('예기치 못한 오류 발생' || CHR(13) || SQLCODE || ' : ' || SQLERRM);
              ROLLBACK;
            RETURN;
      END;


    BEGIN
    FOR LOOP_INDEX IN V_BEGIN_ATCH_ID .. V_END_ATCH_ID
    	LOOP
    	INSERT INTO COMTNFILE(ATCH_FILE_ID,CREAT_DT,USE_AT) VALUES( 'FILE_' || LPAD(LOOP_INDEX,15, '0'), SYSDATE, 'Y');
    END LOOP;
	EXCEPTION
    	WHEN OTHERS THEN
        	O_CODE := '-1';
            O_MSG := SQLCODE || ' : ' || SQLERRM;
            DBMS_OUTPUT.PUT_LINE('예기치 못한 오류 발생' || CHR(13) || SQLCODE || ' : ' || SQLERRM);
            ROLLBACK;
        	RETURN;
    END;



	O_PRJ_ID := V_NEW_PRJ_ID;
    O_CODE := '1';
    O_MSG := '저장 성공';

EXCEPTION
	WHEN OTHERS THEN
    	O_CODE 	:= '-1';
      	O_MSG 	:= SQLCODE || ' : ' || SQLERRM;
    	DBMS_OUTPUT.PUT_LINE('예기치 못한 오류 발생' || CHR(13) || SQLCODE || ' : ' || SQLERRM);
    	ROLLBACK;
      RETURN;
END;


