CREATE OR REPLACE FUNCTION OSLOPSDB.SF_REQ4100_REQ_PROCESS_RATE
                                               (
                                                       I_PRJ_ID     IN VARCHAR2 ,
                                                       I_PROCESS_ID IN VARCHAR2 ,
                                                       I_ST_DTM     IN VARCHAR2 ,
                                                       I_TYPE       IN VARCHAR2
                                               )

        RETURN NUMBER
IS
        V_RATE   NUMBER;
        V_ST_DTM DATE;
        V_ED_DTM DATE;
BEGIN
        BEGIN
                IF I_TYPE = '1' THEN
                        SELECT START_DT,
                               END_DT
                        INTO   V_ST_DTM ,
                               V_ED_DTM
                        FROM   ( SELECT LEVEL ,
                                       ADD_MONTHS(TRUNC( TO_DATE( I_ST_DTM ,'YYYYMM')  ,'Y'),(LEVEL-1)*6) START_DT,
                                       ADD_MONTHS(TRUNC(TO_DATE( I_ST_DTM ,'YYYYMM')  ,'Y'),LEVEL *6) -1 END_DT
                               FROM    DUAL CONNECT BY LEVEL <=2
                               )
                        WHERE  TO_DATE( I_ST_DTM ,'YYYYMM') BETWEEN START_DT AND    END_DT;

                ELSIF I_TYPE = '2' THEN
                        SELECT START_DT,
                               END_DT
                        INTO   V_ST_DTM ,
                               V_ED_DTM
                        FROM   ( SELECT LEVEL ,
                                       ADD_MONTHS(TRUNC(TO_DATE( I_ST_DTM ,'YYYYMM') ,'Y'),(LEVEL-1)*3) START_DT,
                                       ADD_MONTHS(TRUNC( TO_DATE( I_ST_DTM ,'YYYYMM')  ,'Y'),LEVEL *3) -1 END_DT
                               FROM    DUAL CONNECT BY LEVEL <=4
                               )
                        WHERE  TO_DATE( I_ST_DTM ,'YYYYMM') BETWEEN START_DT AND    END_DT;

                END IF;
                SELECT TRUNC(
                		 (SUM(
                       CASE
                              WHEN A.REQ_ED_DU_DTM >= A.REQ_ED_DTM
                              THEN 1
                              ELSE 0
                       END ) / COUNT(A.REQ_ED_DU_DTM) * 100),2) AS RATE
                INTO   V_RATE
                FROM   REQ4100 A
                WHERE  PRJ_ID     = I_PRJ_ID
                AND    PROCESS_ID =I_PROCESS_ID
                AND    REQ_ED_DU_DTM BETWEEN V_ST_DTM AND    V_ED_DTM
                AND    REQ_ED_DU_DTM IS NOT NULL;

        EXCEPTION
        WHEN NO_DATA_FOUND THEN
                V_RATE := 0;
                RETURN V_RATE;
        WHEN OTHERS THEN
                V_RATE := 0;
                RETURN V_RATE;
        END;
        RETURN V_RATE;
END;