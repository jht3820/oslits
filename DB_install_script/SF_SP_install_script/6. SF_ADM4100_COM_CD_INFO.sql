CREATE OR REPLACE FUNCTION SF_ADM4100_COM_CD_INFO
(
		I_LIC_GRP_ID 	IN 	VARCHAR2
	,	I_MST_CD		IN	VARCHAR2
    ,	I_SUB_CD		IN	VARCHAR2
    ,	I_GB			IN	VARCHAR2
)


RETURN VARCHAR2 IS

 	RTN_NM			VARCHAR2(1000);

	V_SUB_CD_NM		VARCHAR2(1000);
    V_SUB_CD_REF1	VARCHAR2(1000);
    V_SUB_CD_REF2	VARCHAR2(1000);
    V_USE_YN		VARCHAR2(1);
    V_ORD			VARCHAR2(100);

BEGIN

	BEGIN

    	SELECT	SUB_CD_NM, 		SUB_CD_REF1, 	SUB_CD_REF2,
        		USE_YN, 		ORD
        INTO	V_SUB_CD_NM, 	V_SUB_CD_REF1, 	V_SUB_CD_REF2,
        		V_USE_YN, 		V_ORD
        FROM	ADM4100 A
        WHERE	1=1
        AND		A.LIC_GRP_ID 	= I_LIC_GRP_ID
        AND		A.MST_CD 		= I_MST_CD
        AND		A.SUB_CD 		= I_SUB_CD
        ;


        IF I_GB = '1' THEN
        	RTN_NM := V_SUB_CD_NM;
        ELSIF I_GB = '2' THEN
        	RTN_NM := V_SUB_CD_REF1;
        ELSIF I_GB = '3' THEN
        	RTN_NM := V_SUB_CD_REF2;
        ELSIF I_GB = '4' THEN
        	RTN_NM := V_USE_YN;
        ELSIF I_GB = '5' THEN
        	RTN_NM := V_ORD;
        ELSE
        	RTN_NM := '';
        END IF;

	EXCEPTION
    	WHEN NO_DATA_FOUND THEN
        	RTN_NM := '';
            RETURN RTN_NM;
        WHEN OTHERS THEN
        	RTN_NM := '';
            RETURN RTN_NM;
	END;

 	RETURN RTN_NM;
END;


