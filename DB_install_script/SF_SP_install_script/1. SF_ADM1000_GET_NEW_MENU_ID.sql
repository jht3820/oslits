CREATE OR REPLACE FUNCTION SF_ADM1000_GET_NEW_MENU_ID
(
		I_LIC_GRP_ID 	IN 	VARCHAR2
	,	I_MENU_ID		IN	VARCHAR2
)

RETURN VARCHAR2 IS

	V_MENU_ID			VARCHAR2(12);
    V_TEMP_MENU_ID		VARCHAR2(12);
    V_LVL				VARCHAR2(2);
	V_CNT				NUMBER;
BEGIN
	BEGIN
        SELECT	LVL
        INTO	V_LVL
        FROM	ADM1000 A
        WHERE	1=1
        AND		A.LIC_GRP_ID = I_LIC_GRP_ID
        AND		A.MENU_ID = I_MENU_ID
        ;

        SELECT	COUNT(*)
        INTO	V_CNT
        FROM	ADM1000 A
        WHERE	1=1
        AND		A.LIC_GRP_ID = I_LIC_GRP_ID
        AND		A.UPPER_MENU_ID = I_MENU_ID
        ;

        IF V_LVL = '0' THEN
            SELECT	LPAD(NVL(MAX(SUBSTR(MENU_ID,1,4)) + 1 , '0001'), 4, '0') || '00000000' AS MENU_ID
            INTO	V_MENU_ID
            FROM	ADM1000 A
            WHERE	1=1
            AND		A.LIC_GRP_ID = I_LIC_GRP_ID
            AND		A.UPPER_MENU_ID = I_MENU_ID
            ;

        ELSIF V_LVL = '1' THEN
            V_TEMP_MENU_ID := SUBSTR(I_MENU_ID, 1, 4);

            SELECT	LPAD(NVL(MAX(SUBSTR(MENU_ID,1,8)) + 1 , V_TEMP_MENU_ID || '0001'), 8, '0') || '0000' AS MENU_ID
            INTO	V_MENU_ID
            FROM	ADM1000 A
            WHERE	1=1
            AND		A.LIC_GRP_ID = I_LIC_GRP_ID
            AND		A.UPPER_MENU_ID = I_MENU_ID
            ;


        ELSIF V_LVL = '2' THEN
            V_TEMP_MENU_ID := SUBSTR(I_MENU_ID, 1, 8);

            SELECT	LPAD(NVL(MAX(SUBSTR(MENU_ID,1,12)) + 1 , V_TEMP_MENU_ID || '0001'), 12, '0') AS MENU_ID
            INTO	V_MENU_ID
            FROM	ADM1000 A
            WHERE	1=1
            AND		A.LIC_GRP_ID = I_LIC_GRP_ID
            AND		A.UPPER_MENU_ID = I_MENU_ID
            ;

        ELSE
            V_MENU_ID := '';
        END IF;

    EXCEPTION
        WHEN NO_DATA_FOUND THEN
            V_MENU_ID := '';
            DBMS_OUTPUT.PUT_LINE('SQL_ERR : '  || SQLCODE || ' : ' || SQLERRM);
            RETURN V_MENU_ID;
        WHEN OTHERS THEN
            V_MENU_ID := '';
            DBMS_OUTPUT.PUT_LINE('SQL_ERR : '  || SQLCODE || ' : ' || SQLERRM);
            RETURN V_MENU_ID;
    END;

	RETURN V_MENU_ID;

END;


