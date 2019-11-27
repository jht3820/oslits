CREATE OR REPLACE FUNCTION SF_PRJ1000_GRP_NM
(
		I_PRJ_GRP_ID	IN	VARCHAR2
)


RETURN VARCHAR2 IS

 	RTN_PRJ_GRP_NM		VARCHAR2(1000);

BEGIN

	BEGIN
        	SELECT	PRJ_NM
            INTO	RTN_PRJ_GRP_NM
            FROM	PRJ1000 A
            WHERE	1=1
            AND		A.PRJ_ID = I_PRJ_GRP_ID
            ;

    EXCEPTION
        WHEN NO_DATA_FOUND THEN
           RTN_PRJ_GRP_NM :=  '';
        WHEN OTHERS THEN
           RTN_PRJ_GRP_NM :=  '';
    END;

 	RETURN RTN_PRJ_GRP_NM;
END;


