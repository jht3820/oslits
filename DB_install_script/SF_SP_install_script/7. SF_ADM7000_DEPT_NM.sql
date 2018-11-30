CREATE OR REPLACE FUNCTION SF_ADM7000_DEPT_NM
(
		I_LIC_GRP_ID	IN	VARCHAR2
	,	I_DEPT_ID	 	IN 	VARCHAR2
    ,	I_GB			IN	VARCHAR2
)

RETURN VARCHAR2 IS

 	RTN_DEPT_NM			VARCHAR2(1000);

BEGIN

	BEGIN

        CASE I_GB 	WHEN '1' THEN

        				SELECT	DEPT_NAME
                        INTO	RTN_DEPT_NM
                        FROM	ADM7000 B
                        WHERE	1=1
                        AND		B.LIC_GRP_ID = I_LIC_GRP_ID
                        AND 	B.DEPT_ID = I_DEPT_ID
                        ;

        			WHEN '2' THEN

                    	SELECT	SUBSTR(SYS_CONNECT_BY_PATH ( B.DEPT_NAME, ' > '),4) EE
                        INTO	RTN_DEPT_NM
                        FROM	( SELECT   *
                                    FROM   ADM7000 A
                                   WHERE   1 = 1
                                     AND   A.LIC_GRP_ID = I_LIC_GRP_ID
                                )  B
                        WHERE	1=1
                        AND		B.LIC_GRP_ID 	= I_LIC_GRP_ID
                        AND 	B.DEPT_ID 		= I_DEPT_ID
                        START WITH DEPT_ID = (SELECT C.DEPT_ID FROM ADM7000 C WHERE C.UPPER_DEPT_ID IS NULL AND C.LVL = '0' AND C.LIC_GRP_ID = I_LIC_GRP_ID)
                        CONNECT BY  B.UPPER_DEPT_ID =  PRIOR  B.DEPT_ID
                        ;


                	ELSE

                    	SELECT	DEPT_NAME
                        INTO	RTN_DEPT_NM
                        FROM	ADM7000 B
                        WHERE	1=1
                        AND		B.LIC_GRP_ID = I_LIC_GRP_ID
                        AND 	B.DEPT_ID = I_DEPT_ID
                        ;
        END CASE;

    EXCEPTION
    	WHEN NO_DATA_FOUND THEN
        	RTN_DEPT_NM := '';
            RETURN RTN_DEPT_NM;
    END;

 	RETURN RTN_DEPT_NM;
END;


