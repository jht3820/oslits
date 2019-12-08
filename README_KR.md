
[![HitCount](http://hits.dwyl.io/jht3820/jht3820/oslits.svg)](http://hits.dwyl.io/jht3820/jht3820/oslits)
[![GitHub issues](https://img.shields.io/github/issues/jht3820/oslits)](https://github.com/jht3820/oslits/issues)
[![GitHub forks](https://img.shields.io/github/forks/jht3820/oslits)](https://github.com/jht3820/oslits/network)
[![GitHub stars](https://img.shields.io/github/stars/jht3820/oslits)](https://github.com/jht3820/oslits/stargazers)
[![GitHub license](https://img.shields.io/github/license/jht3820/oslits)](https://github.com/jht3820/oslits/blob/master/LICENSE)
[![Build Status](https://travis-ci.org/jht3820/oslits.svg?branch=master)](https://travis-ci.org/jht3820/oslits)

# 1. OSL™ ITS 소개

- OSL™ ITS는 기존 비효율적 사업 관리의 문제점(비표준 및 예산중복)을 해결하고 SW 품질을 향상시킬 수 있는 오픈소스 기반의 
SW구축 · 운영 통합관리 솔루션입니다. SW 구축 · 운영 전 생애주기를 단일화된 시스템에서 관리한 DEV-OPS를 구현합니다.


- OSL™ ITS는 공공기관 정보시스템 구축에 폭넓게 활용되는 전자정부 표준프레임워크 기반으로 구축되어 있습니다.


- 오픈소스로 구성되어 지속적으로 업데이트가 될 수 있으며, 다양한 오픈소스 간의 호환이 가능합니다.


- HTML5 웹표준 준수로 멀티 브라우저 지원 및 Non-ActiveX 환경을 준수합니다.

# 2. OSL™ ITS 설치 준비

- OSL™ ITS 설치 준비 사항
 
   - Server
   ```
     - JDK 1.7 이상
     - Tomcat 7.0 이상
     - Cubrid 9.3.3 이상
   ```
   - Client 
   ```
     - Chrome 최신버전
     - IE 11 이상
   ```
 - OSL™ ITS 설치와 실행을 위하여 DB 설치 및 property 설정이 필요합니다.
 
 - DB설치 이후 환경파일 설정 순으로 세팅이 필요합니다.
 
 - 이하 전자정부프레임워크(eGovFramework) 표준에 준하여 구동됩니다.
 
# 3. OSL™ ITS 설치

### 3.1 Cubrid에 OSL™ ITS DB 설치
 
 - DB_install_script 디렉토리에 설치 스크립트들을 1번부터 순서대로 설치 진행합니다.
 - Cubrid 함수 생성 전 Cubrid Java Stored function 폴더에 있는 java 파일을 컴파일하여 Cubrid에 load 합니다.
   이 후 함수를 생성합니다.
  
### 3.2 OSL™ ITS DB 접속 주소 및 환경설정
  ```
      /oslops/src/main/resources/egovframework/egovProps/globals.properties 아래와 같이 설정을 변경합니다.
      
      Globals.lunaops.cubrid.driver= Your cubrid DB  Driver
      Globals.lunaops.cubrid.url= Your cubrid DB URL
      Globals.lunaops.cubrid.username= Your DB username
      Globals.lunaops.cubrid.password= Your DB password
   ```   
### 3.3 OSL™ ITS 최초 시스템관리자 생성
 ```
      /olsops/src/main/resources/egovframework/egovProps/globals.properties 아래와 같이 설정을 변경합니다.
      
      Globals.oslits.userJoin= N → 해당 항목 Y 로 변경하여 최초 시스템관리자가 회원가입할 수 있도록 설정합니다.
      
        ㄴ 회원가입을 통해 생성한 ID가 해당 조직의 시스템관리자 권한으로 자동 생성됨니다.
        ㄴ 시스템관리자 ID 회원가입을 통해 생성한 후 다시 설정을 'N' 으로 변경하여 회원가입 기능 제거해야 합니다.
        ㄴ 이후 시스템관리자는 시스템의 사용자관리 기능을 통해 해당 시스템을 사용할 사용자들을 등록하여 ID를 
           발급하여 시스템을 사용하도록 합니다. 
 ```
# 4. OSL™ ITS 사용법


### 4.1 최초 계정 생성 및 시스템 설정

- 최초 계정을 생성합니다. 회원가입으로 생성된 계정은 시스템관리자 권한을 가집니다. 
   
   
- 계정 생성 후 최초 로그인 시 프로젝트 그룹과 프로젝트를 생성합니다.


- 로그인 후 시스템에 필요한 REST API, SVN, JENKINS 정보를 등록합니다.


### 4.2 각 권한별 사용법


- 시스템에서 기본으로 제공되는 권한은 사업담당자, 업무담당자, 일반사용자 입니다.


#### 4.2.1 사업담당자


1. 사업담당자는 프로젝트 및 프로세스 등 모든 업무 설정을 관리하고 업무 담당자가 수행한 업무 결과를 검수, 결재하는 역할을 합니다.
   또한 시스템, 프로젝트, 프로세스, 분류, 배포계획에 관한 정보들을 설정합니다.
   
   
2. 시스템 설정으로는 공통코드를 관리하고 사용자 관리 및 조직 정보를 관리합니다.


3. 시스템 설정을 끝내면 업무역할을 설정합니다. 기본으로 제공하는 3가지 업무역할 이외에 필요한 업무역할이 있다면 추가합니다.


4. 업무역할에 등록한 사용자들을 배정하고, 시스템에서 사용할 개발문서 양식을 설정합니다.


5. 다음으로 프로젝트에서 사용할 프로세스를 설정합니다. 프로세스를 생성하고 해당 프로세스의 작업흐름을 추가합니다. 프로세스 설정이 끝나면 프로세스를
   확정 처리합니다. 확정 처리된 프로세스는 대시보드에 나타나게 됩니다.  
   프로세스의 확정 취소는 가능하나 요구사항이 1건이라도 처리중이라면 확정 취소를 할 수 없습니다.
   

6. 다음으로 요구사항을 분류할 요구사항 분류를 설정하고, 배포계획을 생성합니다. 생성된 분류와 배포계획은 업무담당자가 요구사항을 처리하는 과정에서
   각각 해당 요구사항에 지정할 수 있습니다.


7. 사업담당자는 업무담당자로부터 올라온 결재에 대해서 승인/반려 처리를 합니다.


8. 프로젝트의 요구사항 처리현황을 대시보드를 통해서 확인할 수 있습니다.

   
#### 4.2.2 업무담당자   


1. 업무담당자는 요청 받은 요구사항을 프로세스에 맞게 분류하여 접수하고 프로세스에 설정된 활동별로 업무를 기록 관리합니다.


2. 요구사항을 처리하며, 필요에 따라 해당 요구사항을 미리 설정된 분류에 배정하며, 프로세스에 배포 계획이 지정되어 있다면 배포 계획을 추가합니다.


3. 요구사항 처리중 결재를 올렸다면, 업무담당자 본인이 올린 결재 현황을 확인할 수 있습니다.


#### 4.2.3 일반사용자


1. 일반 사용자는 요구사항을 시스템에 등록합니다.


2. 등록한 요구사항의 처리 현황을 확인합니다.


# 5. 사용자 그룹

프로젝트 참여 및 프로젝트에 대한 질문, 토론 [구글 그룹](https://groups.google.com/forum/#!forum/opensoftlab-github-group) 참여

 - 그룹 사이트로 이동하여 구글 계정으로 로그인 후 회원가입 신청을 합니다.
 - 회원가입 승인 후 그룹에 참여 가능합니다.


# 6. 라이센스 정보


- OSL™ ITS는 GPL3.0 라이센스를 사용합니다. ([GPL3.0 라이센스 정보](https://www.olis.or.kr/license/Detailselect.do?lId=1072))
