CREATE OR REPLACE FUNCTION SF_ADM3000_LIC_INFO
(
		I_LIC_GRP_ID 	IN 	VARCHAR2
    ,	I_RTN_GB		IN	VARCHAR2
)

RETURN VARCHAR2 IS

 	RTN_LIC_INFO		VARCHAR2(1000);
    V_LIC_NO			VARCHAR2(20);
    V_LIC_NM			VARCHAR2(500);
    V_LIC_START_DT		VARCHAR2(8);
    V_LIC_END_DT		VARCHAR2(8);
    V_PERMIT_USR_CNT	NUMBER;
    V_IS_ACT_LIC_YN		VARCHAR2(1);

BEGIN

	BEGIN

		WITH W_TBL AS
        (
            SELECT	LIC_GRP_ID
                ,	LIC_NO
                ,	LIC_NM
                ,	LIC_START_DT
                ,	LIC_END_DT
                ,	LIC_GB_CD
                ,	PERMIT_USR_CNT
            FROM	ADM3000
            WHERE	1=1
            AND		LIC_GRP_ID = I_LIC_GRP_ID
            AND		LIC_END_DT >= TO_CHAR(SYSDATE, 'YYYYMMDD')
            ORDER BY LIC_NO DESC
        )
        SELECT	LIC_NO
            ,	LIC_NM
            ,	LIC_START_DT
            ,	LIC_END_DT
            ,	PERMIT_USR_CNT
            ,	'Y'
        INTO	V_LIC_NO
            ,	V_LIC_NM
            ,	V_LIC_START_DT
            ,	V_LIC_END_DT
            ,	V_PERMIT_USR_CNT
            ,	V_IS_ACT_LIC_YN
        FROM	W_TBL A
        WHERE	1=1
        AND		ROWNUM = 1
        ;

		-- 구분에 따라 결과값 세팅
        IF I_RTN_GB = '1' THEN
        	RTN_LIC_INFO := V_LIC_NO;
        ELSIF I_RTN_GB = '2' THEN
        	RTN_LIC_INFO := V_LIC_NM;
        ELSIF I_RTN_GB = '3' THEN
        	RTN_LIC_INFO := V_LIC_START_DT;
        ELSIF I_RTN_GB = '4' THEN
        	RTN_LIC_INFO := V_LIC_END_DT;
        ELSIF I_RTN_GB = '5' THEN
        	RTN_LIC_INFO := V_PERMIT_USR_CNT;
        ELSIF I_RTN_GB = '6' THEN
        	RTN_LIC_INFO := V_IS_ACT_LIC_YN;
        END IF;

    EXCEPTION
        WHEN NO_DATA_FOUND THEN
    		RTN_LIC_INFO :=  '';
           	IF I_RTN_GB = '6' THEN
           		RTN_LIC_INFO := 'N';
           	END IF;
        WHEN OTHERS THEN
           	RTN_LIC_INFO :=  '';
    END;

 	RETURN RTN_LIC_INFO;
END;


