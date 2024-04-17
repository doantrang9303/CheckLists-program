# CheckLists program

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

### KeyCloak
Default URL
 `http://localhost:8080`
User Name: `admin`
Password: `admin`
#### Creating the realm

Open [Keycloak Admin Console](http://localhost:8080/admin/). Login with Username & Password = admin

Create a new realm called with YOUR_REALM_NAME you choice(find the `add realm` button in the drop-down
in the top-left corner). 


#### Create a client

Now create a client for the JS console by clicking on `clients` then `create`.

Fill in the following values:

* Client ID: YOUR_CLIENT_NAME 

On the next form fill in the following values:

<img width="388" alt="image" src="https://github.com/doantrang9303/CheckLists-program/assets/133722717/e9583a1d-4af5-4a2a-a788-d9bc42707cc8">



## SETTING PARAMETER
## SETUP ENVIRONMENT VARIABLE
Open file `.env` in `CheckLists-program`


### DataBase
MYSQL
hostname: `localhost` or `mysql`
port: `3306`
user name : `root`
password: `123456789aA@`

#### Back-end:

Access the project through your web browser at `http://localhost:9292`
Health Check `http://localhost:9292/actuator/health`
### Mail

Access the project through your web browser at `http://localhost:9000`
Health Check `http://localhost:9000/actuator/health`

#### Front-end:  

Access the project through your web browser at `http://localhost:3000`


















