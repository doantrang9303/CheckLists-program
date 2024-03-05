# CheckLists program


## KeyCloak


### Start Keycloak
Install Keycloak and unzip 
Open folder keycloak unzip-> Open folder bin
 Right click and choose Open in Terminal -> Enter './kc.bat start-dev' and wait for it to run


### Creating the realm

Open [Keycloak Admin Console](http://localhost:8080/auth/admin/). Create username and password for Admin and Login.

Create a new realm called with YOUR_REALM_NAME you choice(find the `add realm` button in the drop-down
in the top-left corner). 


### Create a client

Now create a client for the JS console by clicking on `clients` then `create`.

Fill in the following values:

* Client ID: YOUR_CLIENT_NAME 

On the next form fill in the following values:

* Valid Redirect URIs:  `http://localhost:5173/*`
* Valid post logout redirect URIs: `http://localhost:5173/*`
* Web Origins: `http://localhost:5173`
* Client authentication : ON
* Click `Save`


## Backend
### Prepare
#### INSTALL GIT for WINDOWS
 Download and install GIT : https://git-scm.com/download/win
 
 Check git version : `git –version`
#### Install Java JDK (JDK 17)
Download:  https://download.oracle.com/java/17/latest/jdk-17_windows-x64_bin.exe 

Setup Java Home : RUN CMD With ADMIN
 `setx -m JAVA_HOME "C:\Program Files\Java\jdk-17" `

 `setx -m PATH "%JAVA_HOME%\bin;%PATH%" `
 
Check: java --version
 #### Install Maven 
download: https://dlcdn.apache.org/maven/maven-3/3.9.6/binaries/apache-maven-3.9.6-bin.zip
unzip it to a specific folder of our choice

Setup MAVEN Home : open new CMD and run With ADMIN
 `setx -m MAVEN_HOME "folder_your_choice\apache-maven-3.8.6" `

 `setx -m PATH "%MAVEN_HOME%\bin;%PATH%"`
 
Check: `mvn –version `


### Start Backend
Open CMD: ` git clone https://github.com/doantrang9303/CheckLists-program `

Navigate to the project directory:` cd CheckLists-program/checklistproject_server `

Build the project using Maven: ` mvn clean install `

Modify application.properties file in: ` your_folder\CheckLists-program\checklistproject_server\src\main\resources `
and choose `application.yml ` and change info match your system

Run the project using Maven: ` mvn spring-boot:run`

Access the project through your web browser at http://localhost:9292


## Frontend
### Prepare
Install :
-Node.js v20.11.1 :  ` https://nodejs.org/dist/v20.11.1/node-v20.11.1-x64.msi `

-VISUAL STUDIO CODE

### Start Frontend:
1. Open VISUAL STUDIO CODE
2. Open folder Front-end  : File -> Open Folder -> select folder `Front-end` -> select `website` -> select `my-react-app`
   
3. Change config keycloak suitable for your local computer at file .evn
   
 `VITE_KEYCLOAK_URL =http://localhost:8080`
 
`VITE_KEYCLOAK_REALM ='YOUR_REALM' `

`VITE_KEYCLOAK_CLIENT ='YOUR_CLIENT_NAME' `

`API_SSO_VERIFY_ACCESS_TOKEN ="http://localhost:9292"`

`VITE_KEYCLOAK_SECRET_CLIENT ='YOUR_SECRET_CLIENT' `

4. **Ctrl + Shift + `**  -> Enter **npm install**
   
                    -> Enter  `npm install oidc-react axios`
   
5. Check package.json and look at dependencies check if some library not exist to install by **Ctrl + Shift + `**  -> Enter **npm i LIBRARY_NAME**
    
   ` "@react-keycloak/web": "^3.4.0",
    "bootstrap": "^5.3.3",
    "bootstrap-icons": "^1.11.3",
    "keycloak-js": "^23.0.7",
    "react": "^18.2.0",
    "react-bootstrap": "^2.10.1",
    "react-dom": "^18.2.0" `
   
6. Run project : Ctrl + Shift + ` -> Enter npm run dev -> click  https://localhost5173/ 
















