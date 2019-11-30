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
    O_MSG := '�ʱ�ȭ';

    BEGIN

        DELETE
        FROM	ADM1100 A
        WHERE	1=1
        AND		A.PRJ_ID = I_PRJ_ID
        ;

    EXCEPTION
    	WHEN OTHERS THEN
          O_CODE := '-1';
          O_MSG := '���� �׷� ������ ������ �� �����ϴ�.';
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
          O_MSG := '����������Ʈ�� ����� ������ ������ �� �����ϴ�.';
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
          O_MSG := '�޴��� ���� ���� ������ ������ �� �����ϴ�.';
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
          O_MSG := '�䱸���� �з� ������ ������ �� �����ϴ�.';
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
          O_MSG := '�䱸���� ������ ������ �� �����ϴ�.';
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
          O_MSG := '�䱸���� �ڸ�Ʈ ������ ������ �� �����ϴ�.';
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
          O_MSG := '�䱸���� �˼� ������ ������ �� �����ϴ�.';
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
          O_MSG := '���� ������ ������ �� �����ϴ�.';
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
          O_MSG := '������Ʈ ǥ��/ǰ�� ���⹰ ������ ������ �� �����ϴ�.';
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
          O_MSG := '������Ʈ ������ ������ �� �����ϴ�.';
          ROLLBACK;
          RETURN;
    END;


    O_CODE := '1';
    O_MSG := '���� ����';

EXCEPTION
	WHEN OTHERS THEN
    	O_CODE 	:= '-1';
      	O_MSG 	:= SQLCODE || ' : ' || SQLERRM;
    	DBMS_OUTPUT.PUT_LINE('������Ʈ ������ ����ġ ���� ���� �߻�' || CHR(13) || SQLCODE || ' : ' || SQLERRM);
    	ROLLBACK;
      RETURN;
END;