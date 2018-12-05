CREATE OR REPLACE FUNCTION SF_ADM1000_MENU_NM
(
		I_LIC_GRP_ID 	IN 	VARCHAR2
	,	I_MENU_ID		IN	VARCHAR2
    ,	I_RTN_GB		IN	VARCHAR2
)


RETURN VARCHAR2 IS

 	RTN_MENU_NM		VARCHAR2(1000);

BEGIN

	BEGIN


    	IF I_RTN_GB = '1' THEN
        	SELECT	MENU_NM
            INTO	RTN_MENU_NM
            FROM	ADM1000 A
            WHERE	1=1
            AND		A.LIC_GRP_ID = I_LIC_GRP_ID
            AND		A.MENU_ID = I_MENU_ID
            ;


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


