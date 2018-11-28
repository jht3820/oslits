/*------------------------------------------------------------------------------
-- 개체 이름: SP_PRJ1000_PRJ_INFO_INSERT
-- 만든 날짜: 2018-07-02 오후 1:44:57
-- 마지막 수정 날짜: 2018-10-18 오후 6:53:57
-- 상태: VALID
------------------------------------------------------------------------------*/
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
/*******************************************************************************************************
 PROCEDURE 명      :	SP_PRJ1000_PRJ_INFO_INSERT
 PROCEDURE 설명    : PRJ1000의 프로젝트 생성시 프로젝트 생성과 동시에 필요한 관련 정보들을 자동 생성
 					- 프로젝트 생성자를 생성한 프로젝트의 AUT2018082100001 권한으로 자동 생성함.
 					- 1. ADM1100 - 권한그룹정보(ROOTSYSTEM_PRJ 복사해서 PRJ_ID를 새로만든 PRJ_ID로)
					- 2. ADM1300 - 권한프로젝트별 사용자 정보(프로젝트 생성자 ID를 AUT2018082100001로 만든다. 1 ROW)
					- 3. ADM1200 - 메뉴별 접근권한(자신의 LIC_GRP_ID, ROOTSYSTEM_PRJ 복사해서 새로만든 LIC_GRP_ID, PRJ_ID로 생성)
                    - 4. REQ4000 - 요구사항 분류정보의 루트 요구사항 분류 생성(프로젝트명으로 생성)
                    - 5. PRJ1100 - 프로젝트 작업흐름 정보 생성(ROOTSYSTEM_PRJ 복사해서 PRJ_ID를 새로만든 PRJ_ID로)
                    - 6. COMTNCOPSEQ - 파일 ID 생성을 위해 초기 값 불러오기
					- 7. PRJ3000 - 프로젝트 표준/품질 산출물 정보 생성(ROOTSYSTEM_PRJ 복사해서 PRJ_ID를 새로만든 PRJ_ID로, ATCH_FILE_ID = 6번 값 기준+1씩)
                    - 8. COMTNCOPSEQ - 추가된 값 만큼 재 설정
                    - 9. COMTNFILE - 6번 초기 값 부터 8번 마지막 값까지 ATCH_FILE_ID 추가
                    - 10. COMTNCOPSEQ - 옵션 ID 생성을 위해 초기 값 불러오기 ( 컬럼 값 : OPTINFO )
          - 11. PRJ6000 - 프로젝트 옵션 정보 생성 (ROOTSYSTEM_PRJ 복사해서 PRJ_ID를 새로만든 PRJ_ID로, OPT_ID = 11번값 기준 + 1씩)
          					- 12.  COMTNCOPSEQ - 추가된 값 만큼 재 설정
                    - 13. COMTNFILE - 10번 초기 값 부터 11번 마지막 값까지 OPT_ID 하나 추가

 사용화면          :
 작성자/작성일     : 정형택 / 2016-01-26
 INPUT             :	I_PRJ_GRP_CHK = 'Y' OR 'N' 프로젝트 최초 생성의 경우, 프로젝트 그룹정보도 같이 생성함.
 OUTPUT            : O_CODE		   	            	리턴코드		(1: 성공 , -1: 실패)
                     O_MSG		                	리턴 메세지
 관련테이블        :	ADM1200, ADM1100, ADM1300
 수정자/수정일/내용: 1.정형택 / 2016-07-11 / 프로젝트 표준-품질 산출물 정보 생성 추가
                     2.정형택 / 2016-12-20 / 프로젝트 작업흐름 정보 생성 수정
                     3.진주영 / 2017-04-16 / 산출물 생성 시 atch_file_id 강제 생성
                     4.김현종 / 2017-06-30 / 프로젝트 옵션 정보 생성 추가
                     5.진주영 / 2018-08-16 / 프로젝트 그룹 구분 추가
										 6.배용진 / 2018-09-13 / 산출물 DOC_ED_DTM 컬럼 추가
                     7.공대영 / 2018-09-27 / 프로젝트 타입의 의해 메뉴권한 생성 제
                     8.배용진 / 2018-10-18 / 프로젝트 약어 추가

*******************************************************************************************************/

IS

		V_NEW_PRJ_ID 		VARCHAR2(16);
		V_NEW_REQ_CLS_ID	VARCHAR2(16);

    V_BEGIN_ATCH_ID		NUMBER;
    V_END_ATCH_ID		NUMBER;

BEGIN

	--OUT 변수 초기화
    O_CODE := '-1';
    O_MSG := '초기화';

    /**
    *	새로 발급할 PRJ_ID 값 얻기 블럭
    */
    BEGIN

		--NEW_PRJ_ID 얻어오기
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


	--프로젝트 정보 생성 블럭
	BEGIN
    	-- 프로젝트 정보 생성
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


	--권한그룹 정보 생성 블럭
	BEGIN
    	-- 권한그룹 정보 생성
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

	-- 권한프로젝트별 사용자 정보 생성
	BEGIN
    	-- 권한프로젝트별 사용자 정보 생성
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

	-- 메뉴별 접근권한 정보 생성
	BEGIN
    	-- 메뉴별 접근권한 정보 생성
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

	-- 요구사항 분류 정보 루트 분류 생성
	BEGIN
    	-- 신규 요구사항 분류 ID 발급
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


	-- 초기값, 마지막 값 산정
    BEGIN
    	SELECT NEXT_ID INTO V_BEGIN_ATCH_ID
        FROM COMTNCOPSEQ A
        WHERE A.TABLE_NAME = 'COMTNFILE';

        SELECT COUNT(0) INTO V_END_ATCH_ID
        FROM	PRJ3000 A
        WHERE	A.PRJ_ID = 'ROOTSYSTEM_PRJ'
        ;

        --양식 아이디와 파일 아이디
        V_END_ATCH_ID := (V_BEGIN_ATCH_ID+(V_END_ATCH_ID*2));

    EXCEPTION
    WHEN OTHERS THEN
        O_CODE := '-1';
        O_MSG := SQLCODE || ' : ' || SQLERRM;
        DBMS_OUTPUT.PUT_LINE('예기치 못한 오류 발생' || CHR(13) || SQLCODE || ' : ' || SQLERRM);
        ROLLBACK;
        RETURN;
    END;

		-- 프로젝트 산출물 생성
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
	-- COMTNCOPSEQ 값 설정
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

    --COMTNFILE 테이블에 레코드 추가
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


