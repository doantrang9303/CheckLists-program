# CheckLists program
http://localhost:9292/swagger-ui/index.html#

## PREPARE

### **Install Docker**
`https://www.docker.com/products/docker-desktop`

### **Start Keycloak**
Open CMD : `docker run -p 8080:8080 -e KEYCLOAK_ADMIN=admin -e KEYCLOAK_ADMIN_PASSWORD=admin quay.io/keycloak/keycloak:23.0.7 start-dev`



#### Creating the realm

Open [Keycloak Admin Console](http://localhost:8080/admin/). Login with Username & Password = admin

Create a new realm called with YOUR_REALM_NAME you choice(find the `add realm` button in the drop-down
in the top-left corner). 


#### Create a client

Now create a client for the JS console by clicking on `clients` then `create`.

Fill in the following values:

* Client ID: YOUR_CLIENT_NAME 

On the next form fill in the following values:

<img width="488" alt="image" src="https://github.com/doantrang9303/keycloak-containers-demo/assets/133722717/3e96a841-2697-4ce7-8b76-0b8d543803e1">



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


## SETTING PARAMETER

### Setting for checklistprogram_server

Modify application.properties file in: ` your_folder\CheckLists-program\checklistproject_server\src\main\resources `
and choose `application.yml ` and change info match your system

<img width="679" alt="image" src="https://github.com/doantrang9303/keycloak-containers-demo/assets/133722717/e0b6b712-bf1d-4c67-8383-d824f8718317">


### Setting for Front-end

Open file .env in ` CheckLists-program\Front-end\website\my-react-app` folder and change config keycloak suitable for your local computer:
   
  <img width="551" alt="image" src="https://github.com/doantrang9303/keycloak-containers-demo/assets/133722717/d40e8fd5-41f2-4e75-86ef-e1d72d4749fb">



## RUN CHECKLIST PROGRAM 

### Install source: 
Open CMD: ` git clone https://github.com/doantrang9303/CheckLists-program `
 ####
 
### Run program by Docker:
Open docker 
#### Build image
Open cmd :  `docker build -t imageName `.
#### Run docker
On cmd :  `docker run -p setPort(5173:5173) imageName`

####


### Run program by CMD:

#### Start Back-end:
Navigate to the project directory:` cd CheckLists-program/checklistproject_server `

Build the project using Maven: ` mvn clean install `

Run the project using Maven: ` mvn spring-boot:run`

Access the project through your web browser at http://localhost:9292

#### Start Front-end:  

1. Open another terminal and navigate to ` CheckLists-program\Front-end\website\my-react-app` folder
  
2. Run the command below if you are running the application for the first time:
     `npm install`
   
3. Install library:  `npm install oidc-react axios`
      
4. Run the npm command below to start the application:
    `npm run dev`
    -> click  http://localhost:5173/ 
















