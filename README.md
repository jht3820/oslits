
[![HitCount](http://hits.dwyl.io/jht3820/jht3820/oslits.svg)](http://hits.dwyl.io/jht3820/jht3820/oslits)
[![GitHub issues](https://img.shields.io/github/issues/jht3820/oslits)](https://github.com/jht3820/oslits/issues)
[![GitHub forks](https://img.shields.io/github/forks/jht3820/oslits)](https://github.com/jht3820/oslits/network)
[![GitHub stars](https://img.shields.io/github/stars/jht3820/oslits)](https://github.com/jht3820/oslits/stargazers)
[![GitHub license](https://img.shields.io/github/license/jht3820/oslits)](https://github.com/jht3820/oslits/blob/master/LICENSE)
[![Build Status](https://travis-ci.org/jht3820/oslits.svg?branch=master)](https://travis-ci.org/jht3820/oslits)

# 1. OSL ™ ITS Introduction

- OSL ™ ITS is an open source based solution that can solve problems of existing inefficient business management (non-standard and budget duplication) and improve SW quality.
SW construction and operation integrated management solution. Implement DEV-OPS, which manages the life cycle of SW construction and operation in a unified system.


- OSL ™ ITS is built on the e-government standard framework that is widely used for building public institution information systems.


- It is composed of open source and can be updated continuously. It is compatible with various open sources.


- Compliant with HTML5 web standard to comply with multi-browser support and non-active environment.

# 2. Preparing to Install OSL ™ ITS

- OSL ™ ITS Installation Preparations
 
   - Server
   ```
     - JDK 1.7  or later
     - Tomcat 7.0  or later
     - Cubrid 9.3.3  or later
   ```
   - Client 
   ```
     - Google Chrome latest version
     - IE 11  or later
   ```
 - DB installation and property setting are required for OSL ™ ITS installation and execution.
 
 - After installing the DB, you need to set the environment file.
 
 -  It operates in accordance with the eGovFramework standard.
 
# 3. Install OSL ™ ITS

### 3.1 Install OSL ™ ITS DB on Cubrid 9.3.3.
 
 - Install the installation scripts in the DB_install_script directory in order from number 1.
 - Before creating the Cubrid function, compile the java file in the Cubrid Java Stored function folder and load it into Cubrid. After this, create a function.
  
### 3.2 OSL™ ITS DB Connection Address and Configuration
  ```
      /oslops/src/main/resources/egovframework/egovProps/globals.properties Change the settings as shown below.
      
    Globals.lunaops.cubrid.driver= Your cubrid DB  Driver
    Globals.lunaops.cubrid.url= Your cubrid DB URL
    Globals.lunaops.cubrid.username= Your DB username
    Globals.lunaops.cubrid.password= Your DB password
   ```   
### 3.3 OSL™ ITS Create the first system administrator
 ```
      /opsops/src/main/resources/egovframework/egovProps/globals.properties Change the settings as shown below
      
      Globals.opsops.userJoin= N → Change it to Y so that the first system administrator can join.
      
        ㄴ The ID created through membership is automatically created with the system administrator authority of the organization.
        ㄴ After creating through the system administrator ID membership, you need to remove the membership function by changing the   setting to 'N' again.
        ㄴ After that, the system administrator registers the user ID by registering the users who will use the system through the user  management function of the system.
            Issue it to use the system.
 ```
 
# 4. How to use OSL ™ ITS


### 4.1 Initial account creation and system setup

- Create your first account. Account created as a member has system administrator privileges.
   
   
- Create a project group and project on first login after account creation.


- After login, register the REST API, SVN, JENKINS information necessary for the system.


### 4.2 Usage by each permission


- The basic authority provided by the system is business manager, business manager, and general user.


#### 4.2.1 Project manager


1. The project manager manages all work settings such as projects and processes, and inspects and approves the work results performed by the person in charge.
    It also sets up information about systems, projects, processes, classifications, and deployment plans.
   
   
2. System settings manage common code, user management and organization information.


3. When you finish the system setup, you set up a business role. In addition to the three basic business roles provided, add the necessary business roles.


4. Assign users registered in the work role and set the development document form to be used by the system.


5. Next, set up the process you want to use in your project. Create a process and add workflow for that process. When the process is set up, the process is finalized. Confirmed processes will appear in the dashboard.
You can cancel the process but you cannot cancel the process if there is only one requirement.
   

6. Next, set up a requirement classification to classify the requirements, and create a deployment plan. The generated classification and distribution plan will be used by the staff in handling the requirements.
    Each can be assigned to that requirement.


7. The project manager will approve / refuse the payment from the person in charge.


8. You can check the requirements processing status of the project through the dashboard.

   
#### 4.2.2 Person in charge


1. The task manager classifies the requested requirements into the process and records the work for each activity set in the process.


2. Handles requirements, assigns them to preset classifications as needed, and adds a deployment plan if a process has a deployment plan.


3. If you made a payment while processing a requirement, you can check the status of your payment.


#### 4.2.3 End user


1. End users register their requirements with the system.


2. Check the processing status of the registered requirement.


# 5. Contributing

Join the project, ask questions about the project, discuss the  [Google Group](https://groups.google.com/forum/#!forum/opensoftlab-github-group)

  -Go to the group site and log in with your Google account and apply for membership.
  -After approval of membership, you can join the group.

# 6. License Information

- OSL ™ ITS uses the GPL3.0 license. ([GPL3.0 License Information](https://www.olis.or.kr/license/Detailselect.do?lId=1072))
