CREATE OR REPLACE FUNCTION SF_REQ4000_REQ_CLS_NM
(
		I_PRJ_ID		IN	VARCHAR2
	,	I_REQ_CLS_ID 	IN 	VARCHAR2
    ,	I_GB			IN	VARCHAR2
)


RETURN VARCHAR2 IS

 	RTN_REQ_CLS_NM		VARCHAR2(1000);

BEGIN

	BEGIN


        CASE I_GB 	WHEN '1' THEN

        				SELECT	REQ_CLS_NM
                        INTO	RTN_REQ_CLS_NM
                        FROM	REQ4000 B
                        WHERE	1=1
                        AND		B.PRJ_ID 		= I_PRJ_ID
                        AND 	B.REQ_CLS_ID 	= I_REQ_CLS_ID
                        ;

        			WHEN '2' THEN

                    	SELECT	SUBSTR(SYS_CONNECT_BY_PATH ( B.REQ_CLS_NM, ' > '),4) EE
                        INTO	RTN_REQ_CLS_NM
                        FROM	( SELECT   *
                                     FROM   REQ4000 A
                                    WHERE   1 =1
                                      AND   A.PRJ_ID = I_PRJ_ID
                                  )  B
                        WHERE	1=1
                        AND		B.PRJ_ID 		= I_PRJ_ID
                        AND 	B.REQ_CLS_ID 	= I_REQ_CLS_ID
                        START WITH REQ_CLS_ID = (SELECT C.REQ_CLS_ID FROM REQ4000 C WHERE C.UPPER_REQ_CLS_ID IS NULL AND C.LVL = '0' AND C.PRJ_ID = I_PRJ_ID)
                        CONNECT BY  B.UPPER_REQ_CLS_ID =  PRIOR  B.REQ_CLS_ID
                        ;

                	ELSE

                    	SELECT	REQ_CLS_NM
                        INTO	RTN_REQ_CLS_NM
                        FROM	REQ4000 B
                        WHERE	1=1
                        AND		B.PRJ_ID 		= I_PRJ_ID
                        AND 	B.REQ_CLS_ID 	= I_REQ_CLS_ID
                        ;
        END CASE;

    EXCEPTION
    	WHEN NO_DATA_FOUND THEN
        	RTN_REQ_CLS_NM := '';
            RETURN RTN_REQ_CLS_NM;
    END;

 	RETURN RTN_REQ_CLS_NM;
END;


