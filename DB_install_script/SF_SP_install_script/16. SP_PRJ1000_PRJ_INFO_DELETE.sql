CREATE OR REPLACE PROCEDURE OSLOPSDB.SP_PRJ1000_PRJ_INFO_DELETE
(
   		I_LIC_GRP_ID	IN	VARCHAR2
	,	I_PRJ_ID		IN	VARCHAR2
    ,	O_CODE			OUT	VARCHAR2
    ,	O_MSG			OUT	VARCHAR2
)

IS

BEGIN


    O_CODE := '-1';
    O_MSG := '초기화';

    BEGIN

        DELETE
        FROM	ADM1100 A
        WHERE	1=1
        AND		A.PRJ_ID = I_PRJ_ID
        ;

    EXCEPTION
    	WHEN OTHERS THEN
          O_CODE := '-1';
          O_MSG := '권한 그룹 정보를 삭제할 수 없습니다.';
          ROLLBACK;
          RETURN;
    END;


    BEGIN

        DELETE
        FROM	ADM1300 A
        WHERE	1=1
        AND		A.PRJ_ID = I_PRJ_ID
        ;

    EXCEPTION
    	WHEN OTHERS THEN
          O_CODE := '-1';
          O_MSG := '권한프로젝트별 사용자 정보를 삭제할 수 없습니다.';
          ROLLBACK;
          RETURN;
    END;


    BEGIN


        DELETE
        FROM	ADM1200 A
        WHERE	1=1
        AND		A.LIC_GRP_ID = I_LIC_GRP_ID
        AND		A.PRJ_ID = I_PRJ_ID
        ;

    EXCEPTION
    	WHEN OTHERS THEN
          O_CODE := '-1';
          O_MSG := '메뉴별 접근 권한 정보를 삭제할 수 없습니다.';
          ROLLBACK;
          RETURN;
    END;


    BEGIN

        DELETE
        FROM	REQ4000 A
        WHERE	1=1
        AND		A.PRJ_ID = I_PRJ_ID
        ;

    EXCEPTION
    	WHEN OTHERS THEN
          O_CODE := '-1';
          O_MSG := '요구사항 분류 정보를 삭제할 수 없습니다.';
          ROLLBACK;
          RETURN;
    END;


    BEGIN

        DELETE
        FROM	REQ4100 A
        WHERE	1=1
        AND		A.PRJ_ID = I_PRJ_ID
        ;

    EXCEPTION
    	WHEN OTHERS THEN
          O_CODE := '-1';
          O_MSG := '요구사항 정보를 삭제할 수 없습니다.';
          ROLLBACK;
          RETURN;
    END;


    BEGIN

        DELETE
        FROM	REQ4300 A
        WHERE	1=1
        AND		A.PRJ_ID = I_PRJ_ID
        ;

    EXCEPTION
    	WHEN OTHERS THEN
          O_CODE := '-1';
          O_MSG := '요구사항 코멘트 정보를 삭제할 수 없습니다.';
          ROLLBACK;
          RETURN;
    END;


    BEGIN

        DELETE
        FROM	REQ4400 A
        WHERE	1=1
        AND		A.PRJ_ID = I_PRJ_ID
        ;

    EXCEPTION
    	WHEN OTHERS THEN
          O_CODE := '-1';
          O_MSG := '요구사항 검수 정보를 삭제할 수 없습니다.';
          ROLLBACK;
          RETURN;
    END;


    BEGIN
        DELETE
        FROM	DPL1000 A
        WHERE	1=1
        AND		A.PRJ_ID = I_PRJ_ID
        ;

    EXCEPTION
    	WHEN OTHERS THEN
          O_CODE := '-1';
          O_MSG := '배포 정보를 삭제할 수 없습니다.';
          ROLLBACK;
          RETURN;
    END;


    BEGIN

	DELETE
        FROM	PRJ3000 A
        WHERE	1=1
        AND		A.PRJ_ID = I_PRJ_ID
        ;

    EXCEPTION
    	WHEN OTHERS THEN
          O_CODE := '-1';
          O_MSG := '프로젝트 표준/품질 산출물 정보를 삭제할 수 없습니다.';
          ROLLBACK;
          RETURN;
    END;


    BEGIN

        DELETE
        FROM	PRJ1000 A
        WHERE	1=1
        AND		A.PRJ_ID = I_PRJ_ID
        ;

    EXCEPTION
    	WHEN OTHERS THEN
          O_CODE := '-1';
          O_MSG := '프로젝트 정보를 삭제할 수 없습니다.';
          ROLLBACK;
          RETURN;
    END;


    O_CODE := '1';
    O_MSG := '저장 성공';

EXCEPTION
	WHEN OTHERS THEN
    	O_CODE 	:= '-1';
      	O_MSG 	:= SQLCODE || ' : ' || SQLERRM;
    	DBMS_OUTPUT.PUT_LINE('프로젝트 삭제시 예기치 못한 오류 발생' || CHR(13) || SQLCODE || ' : ' || SQLERRM);
    	ROLLBACK;
      RETURN;
END;