CREATE OR REPLACE FUNCTION SF_PRJ3000_DOC_NM
(
		I_LIC_GRP_ID 	IN 	VARCHAR2
	,	I_DOC_ID		IN	VARCHAR2
    ,	I_RTN_GB		IN	VARCHAR2
)

RETURN VARCHAR2 IS

 	RTN_DOC_NM		VARCHAR2(1000);

BEGIN

	BEGIN


    	IF I_RTN_GB = '1' THEN
        	SELECT	DOC_NM
            INTO	RTN_DOC_NM
            FROM	PRJ3000 A JOIN (SELECT PRJ_ID,LIC_GRP_ID FROM PRJ1000) B ON(A.PRJ_ID = B.PRJ_ID)
            WHERE	1=1
            AND		B.LIC_GRP_ID = I_LIC_GRP_ID
            AND		A.DOC_ID = I_DOC_ID
            ;


        ELSIF I_RTN_GB = '2' THEN
        	SELECT	DOC_NM
            INTO	RTN_DOC_NM
            FROM	PRJ3000 A JOIN (SELECT PRJ_ID,LIC_GRP_ID FROM PRJ1000) B ON(A.PRJ_ID = B.PRJ_ID)
            WHERE	1=1
            AND		B.LIC_GRP_ID = I_LIC_GRP_ID
            AND		A.DOC_ID = (
            						SELECT 	UPPER_DOC_ID
                                    FROM	PRJ3000 A
                                    WHERE	1=1
                                    AND		B.LIC_GRP_ID = I_LIC_GRP_ID
                                    AND		A.DOC_ID = I_DOC_ID

            					)
            ;


        ELSIF I_RTN_GB = '3' THEN
        	SELECT	DOC_NM
            INTO	RTN_DOC_NM
            FROM	PRJ3000 A JOIN (SELECT PRJ_ID,LIC_GRP_ID FROM PRJ1000) B ON(A.PRJ_ID = B.PRJ_ID)
            WHERE	1=1
			AND		B.LIC_GRP_ID = I_LIC_GRP_ID
            AND		A.DOC_ID = (
            						SELECT 	UPPER_DOC_ID
                                    FROM	PRJ3000 A
                                    WHERE	1=1
                                    AND		B.LIC_GRP_ID = I_LIC_GRP_ID
                                    AND		A.DOC_ID = (
                                    						SELECT	UPPER_DOC_ID
                                                            FROM	PRJ3000 A
                                                            WHERE	1=1
                                                            AND		B.LIC_GRP_ID = I_LIC_GRP_ID
                                                            AND 	A.DOC_ID = I_DOC_ID
                                    					)
            					)
            ;


        ELSE
        	RTN_DOC_NM := '';
        END IF;

    EXCEPTION
        WHEN NO_DATA_FOUND THEN
           RTN_DOC_NM :=  '';
        WHEN OTHERS THEN
           RTN_DOC_NM :=  '';
    END;

 	RETURN RTN_DOC_NM;
END;


