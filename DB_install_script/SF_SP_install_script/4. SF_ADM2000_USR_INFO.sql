CREATE OR REPLACE FUNCTION SF_ADM2000_USR_INFO
(
		I_USR_ID		IN	VARCHAR2
	,	I_USR_INFO_CODE	IN 	VARCHAR2
)


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


