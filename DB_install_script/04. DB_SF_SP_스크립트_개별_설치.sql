/*------------------------------------------------------------------------------
-- 개체 이름: OSLAGLDB.SF_ADM1000_GET_NEW_MENU_ID
-- 만든 날짜: 2016-12-20 오전 11:57:33
-- 마지막으로 수정한 날짜: 2016-12-20 오전 11:57:33
-- 상태: VALID
------------------------------------------------------------------------------*/
CREATE OR REPLACE FUNCTION OSLAGLDB.SF_ADM1000_GET_NEW_MENU_ID
(
		I_LIC_GRP_ID 	IN 	VARCHAR2
	,	I_MENU_ID		IN	VARCHAR2
)

/*******************************************************************************************************
 FUNCTION 명 	  :	SF_ADM1000_GET_NEW_MENU_ID
 FUNCTION 설명     : 상위 메뉴ID 를 이용해 새 메뉴ID를 발급한다.
 사용화면          : 공통, ADM1000
 INPUT             : I_UPPER_MENU_ID	: 상위 메뉴 ID

 RETURN			   : MENU_ID
 작성자/작성일     : 정형택 / 2016-01-13
 관련테이블        : ADM1000
 수정자/수정일     : 2016-01-13
 수정내용          : 최초생성
*******************************************************************************************************/

RETURN VARCHAR2 IS

	V_MENU_ID			VARCHAR2(12);
    V_TEMP_MENU_ID		VARCHAR2(12);
    V_LVL				VARCHAR2(2);
	V_CNT				NUMBER;
BEGIN
	BEGIN
        --상위 메뉴 정보 저장
        SELECT	LVL
        INTO	V_LVL
        FROM	ADM1000 A
        WHERE	1=1
        AND		A.LIC_GRP_ID = I_LIC_GRP_ID
        AND		A.MENU_ID = I_MENU_ID
        ;

        SELECT	COUNT(*)
        INTO	V_CNT
        FROM	ADM1000 A
        WHERE	1=1
        AND		A.LIC_GRP_ID = I_LIC_GRP_ID
        AND		A.UPPER_MENU_ID = I_MENU_ID
        ;

        --상위 메뉴가 루트(0) 이면 1뎁스 메뉴임(앞 4자리)
        --하위 메뉴가 존재하지 않으면 0001 강제로 세팅
        IF V_LVL = '0' THEN
            SELECT	LPAD(NVL(MAX(SUBSTR(MENU_ID,1,4)) + 1 , '0001'), 4, '0') || '00000000' AS MENU_ID
            INTO	V_MENU_ID
            FROM	ADM1000 A
            WHERE	1=1
            AND		A.LIC_GRP_ID = I_LIC_GRP_ID
            AND		A.UPPER_MENU_ID = I_MENU_ID
            ;

		--상위메뉴가 1뎁스 이면
        ELSIF V_LVL = '1' THEN
            V_TEMP_MENU_ID := SUBSTR(I_MENU_ID, 1, 4);

            SELECT	LPAD(NVL(MAX(SUBSTR(MENU_ID,1,8)) + 1 , V_TEMP_MENU_ID || '0001'), 8, '0') || '0000' AS MENU_ID
            INTO	V_MENU_ID
            FROM	ADM1000 A
            WHERE	1=1
            AND		A.LIC_GRP_ID = I_LIC_GRP_ID
            AND		A.UPPER_MENU_ID = I_MENU_ID
            ;

        --상위메뉴가 2뎁스이면
        ELSIF V_LVL = '2' THEN
            V_TEMP_MENU_ID := SUBSTR(I_MENU_ID, 1, 8);

            SELECT	LPAD(NVL(MAX(SUBSTR(MENU_ID,1,12)) + 1 , V_TEMP_MENU_ID || '0001'), 12, '0') AS MENU_ID
            INTO	V_MENU_ID
            FROM	ADM1000 A
            WHERE	1=1
            AND		A.LIC_GRP_ID = I_LIC_GRP_ID
            AND		A.UPPER_MENU_ID = I_MENU_ID
            ;

        ELSE
            V_MENU_ID := '';
        END IF;

    EXCEPTION
        WHEN NO_DATA_FOUND THEN
            V_MENU_ID := '';
            DBMS_OUTPUT.PUT_LINE('SQL_ERR : '  || SQLCODE || ' : ' || SQLERRM);
            RETURN V_MENU_ID;
        WHEN OTHERS THEN
            V_MENU_ID := '';
            DBMS_OUTPUT.PUT_LINE('SQL_ERR : '  || SQLCODE || ' : ' || SQLERRM);
            RETURN V_MENU_ID;
    END;

	RETURN V_MENU_ID;

END;


/*------------------------------------------------------------------------------
-- 개체 이름: OSLAGLDB.SF_ADM1000_MENU_NM
-- 만든 날짜: 2016-12-20 오전 11:57:35
-- 마지막으로 수정한 날짜: 2016-12-20 오전 11:57:35
-- 상태: VALID
------------------------------------------------------------------------------*/
CREATE OR REPLACE FUNCTION OSLAGLDB.SF_ADM1000_MENU_NM
(
		I_LIC_GRP_ID 	IN 	VARCHAR2
	,	I_MENU_ID		IN	VARCHAR2
    ,	I_RTN_GB		IN	VARCHAR2
)

/*******************************************************************************************************
 FUNCTION 명 	  :	SF_ADM1000_MENU_NM
 FUNCTION 설명     : 메뉴명 얻기 함수
 사용화면          : 공통, ADM1000
 INPUT             : I_LIC_GRP_ID 	: 라이선스 그룹 코드
 					 I_MENU_ID 		: 메뉴 ID
                     I_RTN_GB		: 1 (현재 메뉴코드의 메뉴명)
                      				  2 (현재 메뉴코드의 부모 메뉴명)
                                      3 (현재 메뉴코드의 부모의 부모 메뉴명)
 RETURN			   : 메뉴명
 작성자/작성일     : 정형택 / 2015-12-17
 관련테이블        : ADM1000
 수정자/수정일     : 2015-12-17
 수정내용          : 최초생성
*******************************************************************************************************/

RETURN VARCHAR2 IS

 	RTN_MENU_NM		VARCHAR2(1000);

BEGIN

	BEGIN

    	-- 구분이 1일경우 메뉴코드의 메뉴명
    	IF I_RTN_GB = '1' THEN
        	SELECT	MENU_NM
            INTO	RTN_MENU_NM
            FROM	ADM1000 A
            WHERE	1=1
            AND		A.LIC_GRP_ID = I_LIC_GRP_ID
            AND		A.MENU_ID = I_MENU_ID
            ;

        -- 구분이 2일 경우 메뉴코드의 부모 메뉴명
        ELSIF I_RTN_GB = '2' THEN
        	SELECT	MENU_NM
            INTO	RTN_MENU_NM
            FROM	ADM1000 A
            WHERE	1=1
            AND		A.LIC_GRP_ID = I_LIC_GRP_ID
            AND		A.MENU_ID = (
            						SELECT 	UPPER_MENU_ID
                                    FROM	ADM1000 A
                                    WHERE	1=1
                                    AND		A.LIC_GRP_ID = I_LIC_GRP_ID
                                    AND		A.MENU_ID = I_MENU_ID

            					)
            ;

        -- 구분이 3일 경우 메뉴코드의 부모의 부모 메뉴명
        ELSIF I_RTN_GB = '3' THEN
        	SELECT	MENU_NM
            INTO	RTN_MENU_NM
            FROM	ADM1000 A
            WHERE	1=1
			AND		A.LIC_GRP_ID = I_LIC_GRP_ID
            AND		A.MENU_ID = (
            						SELECT 	UPPER_MENU_ID
                                    FROM	ADM1000 A
                                    WHERE	1=1
                                    AND		A.LIC_GRP_ID = I_LIC_GRP_ID
                                    AND		A.MENU_ID = (
                                    						SELECT	UPPER_MENU_ID
                                                            FROM	ADM1000 A
                                                            WHERE	1=1
                                                            AND		A.LIC_GRP_ID = I_LIC_GRP_ID
                                                            AND 	A.MENU_ID = I_MENU_ID
                                    					)
            					)
            ;

        -- 그외 경우 값 없음.
        ELSE
        	RTN_MENU_NM := '';
        END IF;

    EXCEPTION
        WHEN NO_DATA_FOUND THEN
           RTN_MENU_NM :=  '';
        WHEN OTHERS THEN
           RTN_MENU_NM :=  '';
    END;

 	RETURN RTN_MENU_NM;
END;


/*------------------------------------------------------------------------------
-- 개체 이름: OSLAGLDB.SF_ADM1000_MST_CD_NM
-- 만든 날짜: 2016-12-20 오전 11:57:36
-- 마지막으로 수정한 날짜: 2016-12-20 오전 11:57:36
-- 상태: VALID
------------------------------------------------------------------------------*/
CREATE OR REPLACE FUNCTION OSLAGLDB.SF_ADM1000_MST_CD_NM
(
		I_LIC_GRP_ID 	IN 	VARCHAR2
	,	I_MST_CD		IN	VARCHAR2
)

/*******************************************************************************************************
 FUNCTION 명 	  :	SF_ADM1000_MST_CD_NM
 FUNCTION 설명     : 대분류명 얻기 함수
 사용화면          : 공통, ADM1000
 INPUT             : I_LIC_GRP_ID 	: 라이선스 그룹 코드
 					 I_MST_CD 		: 대분류 CD
 RETURN			   : 공통코드 대분류 명
 작성자/작성일     : 정형택 / 2016-01-14
 관련테이블        : ADM4000
 수정자/수정일     : 2016-01-14
 수정내용          : 최초생성
*******************************************************************************************************/

RETURN VARCHAR2 IS

 	RTN_MST_CD_NM		VARCHAR2(1000);

BEGIN

	BEGIN

    	SELECT	MST_CD_NM
        INTO	RTN_MST_CD_NM
        FROM	ADM4000 A
        WHERE	1=1
        AND		A.LIC_GRP_ID = I_LIC_GRP_ID
        AND		A.MST_CD = I_MST_CD
        ;

    EXCEPTION
        WHEN NO_DATA_FOUND THEN
           RTN_MST_CD_NM :=  '';
        WHEN OTHERS THEN
           RTN_MST_CD_NM :=  '';
    END;

 	RETURN RTN_MST_CD_NM;
END;


/*------------------------------------------------------------------------------
-- 개체 이름: OSLAGLDB.SF_ADM2000_USR_INFO
-- 만든 날짜: 2016-12-20 오전 11:57:37
-- 마지막으로 수정한 날짜: 2017-08-31 오후 2:01:20
-- 상태: VALID
------------------------------------------------------------------------------*/
CREATE OR REPLACE FUNCTION OSLAGLDB.SF_ADM2000_USR_INFO
(
		I_USR_ID		IN	VARCHAR2
	,	I_USR_INFO_CODE	IN 	VARCHAR2
)

/*******************************************************************************************************
 FUNCTION 명 	  :	SF_ADM2000_USR_INFO
 FUNCTION 설명     : 사용자ID로 사용자명 조회
 사용화면          : 공통
 INPUT             : I_USR_ID			: 사용자 ID
 					 I_USR_INFO_CODE 	: 사용자 정보 구분
                     						1 = 성명
                                            2 = 이메일
                                            3 = 전화번호
                                            4 = 근무시작시간
                                            5 = 근무종료시간
                                            6 = 사용자이미지ID
 RETURN			   : 작업흐름명
 작성자/작성일     : 정형택 / 2016-01-23
 관련테이블        : ADM2000
 수정자/수정일     : 진주영 / 2017-04-07
 수정내용          : 사용자이미지ID 추가
*******************************************************************************************************/

RETURN VARCHAR2 IS

 	V_USR_NM		VARCHAR(20);
    V_USR_EMAIL		VARCHAR(50);
	V_USR_TELNO		VARCHAR(20);
    V_WK_ST_TM		VARCHAR2(7);
    V_WK_ED_TM		VARCHAR2(7);
    V_USR_IMG_ID	VARCHAR2(20);

    O_RESULT		VARCHAR(50);

BEGIN

	BEGIN

    	SELECT	A.USR_NM
        	,	A.EMAIL
            ,	A.TELNO
            ,	A.WK_ST_TM
            ,	A.WK_ED_TM
            ,	A.USR_IMG_ID
        INTO	V_USR_NM
        	,	V_USR_EMAIL
            ,	V_USR_TELNO
            ,	V_WK_ST_TM
            ,	V_WK_ED_TM
            ,	V_USR_IMG_ID
        FROM	ADM2000	A
        WHERE	A.USR_ID = I_USR_ID
        ;

    EXCEPTION
        WHEN NO_DATA_FOUND THEN
           V_USR_NM :=  '';
           V_USR_EMAIL := '';
           V_USR_TELNO := '';
           V_WK_ST_TM := '';
           V_WK_ED_TM := '';
           V_USR_IMG_ID := '';
        WHEN OTHERS THEN
   			V_USR_NM :=  '';
           V_USR_EMAIL := '';
           V_USR_TELNO := '';
           V_WK_ST_TM := '';
           V_WK_ED_TM := '';
           V_USR_IMG_ID := '';
    END;

	IF I_USR_INFO_CODE = '1' THEN O_RESULT := V_USR_NM;
    ELSIF I_USR_INFO_CODE ='2' THEN O_RESULT := V_USR_EMAIL;
    ELSIF I_USR_INFO_CODE ='3' THEN O_RESULT := V_USR_TELNO;
    ELSIF I_USR_INFO_CODE ='4' THEN O_RESULT := V_WK_ST_TM;
    ELSIF I_USR_INFO_CODE ='5' THEN O_RESULT := V_WK_ED_TM;
    ELSIF I_USR_INFO_CODE ='6' THEN O_RESULT := V_USR_IMG_ID;
 	END IF;

    RETURN O_RESULT;
END;


