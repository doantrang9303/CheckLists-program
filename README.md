# CheckLists program
https://qn24cpl202401.z23.web.core.windows.net/
## PREPARE

### **Install Docker**
`https://www.docker.com/products/docker-desktop`


### **Install GIT for WINDOWS**
 Download and install GIT : https://git-scm.com/download/win
 
 Check git version : `git –version`
 
### **Install Java JDK (JDK 17)**
Download:  https://download.oracle.com/java/17/latest/jdk-17_windows-x64_bin.exe 

Setup Java Home : RUN CMD With ADMIN
 `setx -m JAVA_HOME "C:\Program Files\Java\jdk-17" `

 `setx -m PATH "%JAVA_HOME%\bin;%PATH%" `
 
Check: java --version

 ### **Install Maven **
download: https://dlcdn.apache.org/maven/maven-3/3.9.6/binaries/apache-maven-3.9.6-bin.zip
unzip it to a specific folder of our choice

Setup MAVEN Home : open new CMD and run With ADMIN
 `setx -m MAVEN_HOME "folder_your_choice\apache-maven-3.8.6" `

 `setx -m PATH "%MAVEN_HOME%\bin;%PATH%"`
 
Check: `mvn –version `

### **Install Node.js**
-Node.js v20.11.1 :  ` https://nodejs.org/dist/v20.11.1/node-v20.11.1-x64.msi `

## CLONE CHECKLIST PROGRAM 
### Install source: 
Open CMD: ` git clone https://github.com/doantrang9303/CheckLists-program `

## RUN WITH DOCKER COMPOSE
Open `cmd` in `CheckLits-program` folder.
- enter : `docker-compose up`

## KeyCloak URL
Default URL
 `http://localhost:8080`
User Name: `admin`
Password: `admin`
#### Creating the realm

Open [Keycloak Admin Console](http://localhost:8080/admin/). Login with Username & Password = admin

Create a new realm called with YOUR_REALM_NAME (should be `checklistrealm`) and modify in .env file you choice(find the `add realm` button in the drop-down
in the top-left corner). 




#### Create a client
Now create a client for the JS console by clicking on `clients` then `create`.
![image](https://github.com/doantrang9303/CheckLists-program/assets/98958049/6b05dde4-c1ae-467c-b75b-49978b1c415d)

Chose import client and import client `checklistclient.json` from `CheckLists-program\client_keycloak` 

On the next form fill in the following values:

<img width="388" alt="image" src="https://github.com/doantrang9303/CheckLists-program/assets/133722717/e9583a1d-4af5-4a2a-a788-d9bc42707cc8">
#### Create new user
![image](https://github.com/doantrang9303/CheckLists-program/assets/98958049/1bdb0ffd-e4ae-48ff-b649-6d5b48f7b4fc)

And setup password for new user
![image](https://github.com/doantrang9303/CheckLists-program/assets/98958049/6ee2617b-032a-42a1-b3b0-d5da59a1fe3b)


####

## SETTING PARAMETER
## SETUP ENVIRONMENT VARIABLE
Open file `.env` in `CheckLists-program`

Change environment variable matches what's in your device.

## DataBase
MYSQL
hostname: `localhost` or `mysql`
port: `3306`
user name : `root`
password: `23012003aA@`

Create new database with name : `checklist`
Use sql script in `CheckLists-program\databases`

## Back-end:

Access the project through your web browser at `http://localhost:9292`

Health Check `http://localhost:9292/actuator/health`
## Mail

Access the project through your web browser at `http://localhost:9000`

Health Check `http://localhost:9000/actuator/health`

## Front-end:  

Access the project through your web browser at `http://localhost:3000`


















