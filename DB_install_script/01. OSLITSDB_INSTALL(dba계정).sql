/* 데이터베이스 생성  - command*/
cubrid createdb --db-volume-size=2G --db-volume-size=1G --db-volume-size=200M OSLITSDB ko_KR.utf8

/* 데이터베이스 시작  - command*/
cubrid server start OSLITSDB

/* 데이터베이스 cubrid.conf 수정*/
설치 드라이브\CUBRID\conf\cubrid.conf

/* 주석 풀고 OSLITSDB 입력 */
#server=foo,bar
-> 
server=OSLITSDB

/* 자바 함수 사용하기위해 추가 */
java_stored_procedure=yes 

/* 외부 접속을 위해 추가 */
access_ip_control=yes
access_ip_control_file=C:/CUBRID/conf/db_acl.access

/* 빈문자열 null처리 위해 추가 */
oracle_style_empty_string=yes

/* ip 허용 적용 (cubrid server acl status OSLITSDB - ip허용 확인) */
cubrid server acl reload OSLITSDB


[@OSLITSDB]
*
-> 설치 드라이브/CUBRID/conf/db_acl.access  파일 생성 ( 추후 * 부분에 IP 등록)

/*  command에서 큐브리드 재시작 요청 */
cubrid service restart

/* 데이터베이스 접속  - command*/
csql -C -u dba OSLITSDB

/* 데이터베이스 사용자 계정 생성 - command*/
create user OSLITSDB GROUPS dba;

alter user OSLITSDB password 'OSLITSDB';
