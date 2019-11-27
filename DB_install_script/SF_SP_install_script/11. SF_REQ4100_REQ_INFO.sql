CREATE OR REPLACE FUNCTION SF_REQ4100_REQ_INFO
(
		I_PRJ_ID		IN	VARCHAR2
	,	I_REQ_ID 		IN 	VARCHAR2
    ,	I_RTN_GB		IN 	VARCHAR2
)


RETURN VARCHAR2 IS

 	RTN_REQ_INFO		VARCHAR2(4000);

BEGIN

	BEGIN

    	IF I_RTN_GB = '1' THEN
        	SELECT	A.REQ_NM
            INTO	RTN_REQ_INFO
            FROM	REQ4100 A
            WHERE	1=1
            AND		A.PRJ_ID = I_PRJ_ID
            AND		A.REQ_ID = I_REQ_ID
            ;
        ELSIF I_RTN_GB = '2' THEN
        	SELECT	A.REQ_NO
            INTO	RTN_REQ_INFO
            FROM	REQ4100 A
            WHERE	1=1
            AND		A.PRJ_ID = I_PRJ_ID
            AND		A.REQ_ID = I_REQ_ID
            ;
        ELSE
        	RTN_REQ_INFO := '';
        END IF;

    EXCEPTION
    	WHEN NO_DATA_FOUND THEN
        	RTN_REQ_INFO := '';
            RETURN RTN_REQ_INFO;
    END;

 	RETURN RTN_REQ_INFO;
END;


