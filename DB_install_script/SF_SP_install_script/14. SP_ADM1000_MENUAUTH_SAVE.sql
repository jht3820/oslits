CREATE OR REPLACE PROCEDURE SP_ADM1000_MENUAUTH_SAVE
(
   		I_LIC_GRP_ID	IN	VARCHAR2
	,	I_PRJ_ID		IN	VARCHAR2
	,	I_AUTH_GRP_ID	IN	VARCHAR2
    ,	I_MENU_ID		IN	VARCHAR2
	,	I_ACCESS_YN		IN	VARCHAR2
	,	I_SELECT_YN		IN	VARCHAR2
	,	I_REG_YN		IN	VARCHAR2
	,	I_MODIFY_YN		IN	VARCHAR2
	,	I_DEL_YN		IN	VARCHAR2
	,	I_EXCEL_YN		IN	VARCHAR2
	,	I_PRINT_YN		IN	VARCHAR2
	,	I_REG_USR_ID	IN	VARCHAR2
	,	I_REG_USR_IP	IN	VARCHAR2
	,	I_MODIFY_USR_ID	IN	VARCHAR2
	,	I_MODIFY_USR_IP	IN	VARCHAR2

    ,	O_CODE			OUT	VARCHAR2
    ,	O_MSG			OUT	VARCHAR2
)


IS

	V_UPUP_MENU_ID		VARCHAR2(12);	--대메뉴 ID
    V_UP_MENU_ID		VARCHAR2(12);	--중메뉴 ID
	V_UPUP_ACCESS_YN 	VARCHAR2(1);	--대메뉴 ACCESS_YN 상태
    V_UP_ACCESS_YN 		VARCHAR2(1);	--중메뉴 ACCESS_YN 상태

BEGIN

    O_CODE := '-1';
    O_MSG := '초기화';


    BEGIN

    	SELECT	A.UPPER_MENU_ID
            ,	(SELECT	UPPER_MENU_ID FROM ADM1000 Z WHERE Z.LIC_GRP_ID = A.LIC_GRP_ID AND Z.MENU_ID = A.UPPER_MENU_ID) AS UPUP_MENU_ID
        INTO	V_UP_MENU_ID,	V_UPUP_MENU_ID
        FROM	ADM1000 A
        WHERE	1=1
        AND		A.LIC_GRP_ID = I_LIC_GRP_ID
        AND		A.MENU_ID = I_MENU_ID
        ;

    EXCEPTION
    	WHEN NO_DATA_FOUND THEN
          O_CODE := '-1';
          O_MSG := '상위메뉴정보를 찾을수 없습니다.';
          ROLLBACK;
          RETURN;
    END;

	BEGIN

		MERGE INTO 	ADM1200 A
        USING 		DUAL B
        ON        	( 	A.LIC_GRP_ID = I_LIC_GRP_ID
        AND 			A.PRJ_ID = I_PRJ_ID
        AND 			A.AUTH_GRP_ID = I_AUTH_GRP_ID
        AND 			A.MENU_ID = I_MENU_ID)
        WHEN MATCHED THEN
            UPDATE
            SET     ACCESS_YN		= I_ACCESS_YN
                ,	SELECT_YN		= I_SELECT_YN
                ,	REG_YN			= I_REG_YN
                ,	MODIFY_YN		= I_MODIFY_YN
                ,	DEL_YN			= I_DEL_YN
                ,	EXCEL_YN		= I_EXCEL_YN
                ,	PRINT_YN		= I_PRINT_YN
                ,	MODIFY_DTM		= SYSDATE
                ,	MODIFY_USR_ID	= I_MODIFY_USR_ID
                ,	MODIFY_USR_IP	= I_MODIFY_USR_IP
        WHEN NOT MATCHED THEN
            INSERT
            (
                LIC_GRP_ID,		PRJ_ID,			AUTH_GRP_ID,	MENU_ID,		ACCESS_YN,
                SELECT_YN,		REG_YN,			MODIFY_YN,		DEL_YN,			EXCEL_YN,
                PRINT_YN,		USE_CD,			REG_DTM,		REG_USR_ID,		REG_USR_IP,
                MODIFY_DTM,		MODIFY_USR_ID,	MODIFY_USR_IP
            )
            VALUES
            (
                I_LIC_GRP_ID,	I_PRJ_ID,		I_AUTH_GRP_ID,	I_MENU_ID,		I_ACCESS_YN,
                I_SELECT_YN,	I_REG_YN,		I_MODIFY_YN,	I_DEL_YN,		I_EXCEL_YN,
                I_PRINT_YN,		'01',			SYSDATE,		I_REG_USR_ID,	I_REG_USR_IP,
                SYSDATE,		I_MODIFY_USR_ID,I_MODIFY_USR_IP
            )
        ;


        IF SQL%FOUND = FALSE THEN
        	O_CODE := '-1';
            O_MSG := '저장된 메뉴권한 자료가 없습니다.';
            ROLLBACK;
        	RETURN;
        END IF;

    EXCEPTION
    	WHEN NO_DATA_FOUND THEN
    		O_CODE := '-1';
            O_MSG := '저장할 메뉴권한 자료가 없습니다.';
            ROLLBACK;
        	RETURN;
    END;


	BEGIN
    	SELECT	CASE WHEN COUNT(*) > 0 THEN 'Y' ELSE 'N' END AS ACCESS_YN
        INTO	V_UPUP_ACCESS_YN
        FROM	ADM1200 A
            ,	(SELECT	*
                FROM	ADM1000 A
                WHERE	1=1
                AND		A.UPPER_MENU_ID IN (
                                                SELECT	MENU_ID
                                                FROM	ADM1000 A
                                                WHERE	1=1
                                                AND		A.UPPER_MENU_ID = V_UPUP_MENU_ID
                                                AND		A.LIC_GRP_ID = I_LIC_GRP_ID
                                            )
                AND		A.LIC_GRP_ID = I_LIC_GRP_ID
                ) B
        WHERE	1=1
        AND		A.LIC_GRP_ID = B.LIC_GRP_ID
        AND		A.MENU_ID = B.MENU_ID
        AND		A.LIC_GRP_ID = I_LIC_GRP_ID
        AND		A.PRJ_ID = I_PRJ_ID
        AND		A.AUTH_GRP_ID = I_AUTH_GRP_ID
        AND		A.ACCESS_YN = 'Y'
    	;


        MERGE INTO 	ADM1200 A
        USING 		DUAL B
        ON        	( 	A.LIC_GRP_ID = I_LIC_GRP_ID
        AND 			A.PRJ_ID = I_PRJ_ID
        AND 			A.AUTH_GRP_ID = I_AUTH_GRP_ID
        AND 			A.MENU_ID = V_UPUP_MENU_ID )
        WHEN MATCHED THEN
            UPDATE
            SET     ACCESS_YN		= V_UPUP_ACCESS_YN
                ,	MODIFY_DTM		= SYSDATE
                ,	MODIFY_USR_ID	= I_MODIFY_USR_ID
                ,	MODIFY_USR_IP	= I_MODIFY_USR_IP
        WHEN NOT MATCHED THEN
            INSERT
            (
                LIC_GRP_ID,		PRJ_ID,			AUTH_GRP_ID,	MENU_ID,		ACCESS_YN,
                USE_CD,			REG_DTM,		REG_USR_ID,		REG_USR_IP,		MODIFY_DTM,
                MODIFY_USR_ID,  MODIFY_USR_IP
            )
            VALUES
            (
                I_LIC_GRP_ID,	I_PRJ_ID,		I_AUTH_GRP_ID,	V_UPUP_MENU_ID,	V_UPUP_ACCESS_YN,
                '01',			SYSDATE,		I_REG_USR_ID,	I_REG_USR_IP,	SYSDATE,
                I_MODIFY_USR_ID,I_MODIFY_USR_IP
            )
        ;


        IF SQL%FOUND = FALSE THEN
        	O_CODE := '-1';
            O_MSG := '저장된 최상위 메뉴 자료가 없습니다.';
            ROLLBACK;
        	RETURN;
        END IF;

    EXCEPTION
    	WHEN NO_DATA_FOUND THEN
    		O_CODE := '-1';
            O_MSG := '저장할 최상위 메뉴 자료가 없습니다.';
            ROLLBACK;
        	RETURN;
    END;

	BEGIN

    	SELECT	CASE WHEN COUNT(*) > 0 THEN 'Y' ELSE 'N' END AS ACCESS_YN
        INTO	V_UP_ACCESS_YN
        FROM	ADM1200 A
            ,	(SELECT	*
                FROM	ADM1000 A
                WHERE	1=1
                AND		A.MENU_ID IN (
                                                SELECT	MENU_ID
                                                FROM	ADM1000 A
                                                WHERE	1=1
                                                AND		A.UPPER_MENU_ID = V_UP_MENU_ID
                                                AND		A.LIC_GRP_ID = I_LIC_GRP_ID
                                            )
                AND		A.LIC_GRP_ID = I_LIC_GRP_ID
                ) B
        WHERE	1=1
        AND		A.LIC_GRP_ID = B.LIC_GRP_ID
        AND		A.MENU_ID = B.MENU_ID
        AND		A.LIC_GRP_ID = I_LIC_GRP_ID
        AND		A.PRJ_ID = I_PRJ_ID
        AND		A.AUTH_GRP_ID = I_AUTH_GRP_ID
        AND		A.ACCESS_YN = 'Y'
        ;

		MERGE INTO 	ADM1200 A
        USING 		DUAL B
        ON        	( 	A.LIC_GRP_ID = I_LIC_GRP_ID
        AND 			A.PRJ_ID = I_PRJ_ID
        AND 			A.AUTH_GRP_ID = I_AUTH_GRP_ID
        AND 			A.MENU_ID = V_UP_MENU_ID )
        WHEN MATCHED THEN
            UPDATE
            SET     ACCESS_YN		= V_UP_ACCESS_YN
                ,	MODIFY_DTM		= SYSDATE
                ,	MODIFY_USR_ID	= I_MODIFY_USR_ID
                ,	MODIFY_USR_IP	= I_MODIFY_USR_IP
        WHEN NOT MATCHED THEN
            INSERT
            (
                LIC_GRP_ID,		PRJ_ID,			AUTH_GRP_ID,	MENU_ID,		ACCESS_YN,
                USE_CD,			REG_DTM,		REG_USR_ID,		REG_USR_IP,		MODIFY_DTM,
                MODIFY_USR_ID,  MODIFY_USR_IP
            )
            VALUES
            (
                I_LIC_GRP_ID,	I_PRJ_ID,		I_AUTH_GRP_ID,	V_UP_MENU_ID,	V_UP_ACCESS_YN,
                '01',			SYSDATE,		I_REG_USR_ID,	I_REG_USR_IP,	SYSDATE,
                I_MODIFY_USR_ID,I_MODIFY_USR_IP
            )
        ;


        IF SQL%FOUND = FALSE THEN
        	O_CODE := '-1';
            O_MSG := '저장된 중메뉴 자료가 없습니다.';
            ROLLBACK;
        	RETURN;
        END IF;

    EXCEPTION
    	WHEN NO_DATA_FOUND THEN
    		O_CODE := '-1';
            O_MSG := '저장할 중메뉴 자료가 없습니다.';
            ROLLBACK;
        	RETURN;
    END;

	BEGIN

    	MERGE INTO 	ADM1200 A
        USING 		DUAL B
        ON        	( 	A.LIC_GRP_ID = I_LIC_GRP_ID
        AND 			A.PRJ_ID = I_PRJ_ID
        AND 			A.AUTH_GRP_ID = I_AUTH_GRP_ID
        AND 			A.MENU_ID = '000' )
        WHEN MATCHED THEN
            UPDATE
            SET     ACCESS_YN		= 'Y'
                ,	MODIFY_DTM		= SYSDATE
                ,	MODIFY_USR_ID	= I_MODIFY_USR_ID
                ,	MODIFY_USR_IP	= I_MODIFY_USR_IP
        WHEN NOT MATCHED THEN
            INSERT
            (
                LIC_GRP_ID,		PRJ_ID,			AUTH_GRP_ID,	MENU_ID,		ACCESS_YN,
                USE_CD,			REG_DTM,		REG_USR_ID,		REG_USR_IP,		MODIFY_DTM,
                MODIFY_USR_ID,  MODIFY_USR_IP
            )
            VALUES
            (
                I_LIC_GRP_ID,	I_PRJ_ID,		I_AUTH_GRP_ID,	'000',			'Y',
                '01',			SYSDATE,		I_REG_USR_ID,	I_REG_USR_IP,	SYSDATE,
                I_MODIFY_USR_ID,I_MODIFY_USR_IP
            )
        ;


        IF SQL%FOUND = FALSE THEN
        	O_CODE := '-1';
            O_MSG := '최상위(솔루션) 메뉴 자료가 없습니다.';
            ROLLBACK;
        	RETURN;
        END IF;

    EXCEPTION
    	WHEN NO_DATA_FOUND THEN
    		O_CODE := '-1';
            O_MSG := '저장할 최상위(솔루션) 메뉴 자료가 없습니다.';
            ROLLBACK;
        	RETURN;
    END;

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


