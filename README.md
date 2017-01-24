# mango-collaboration
Mango collaboration application.

Glassfish Server Application

1. Install Glassfish server.
  Download and extract to folder.
  CD to install folder and execute $ ./glassfish/bin/asadmin start-domain domain1
  This will start the websever on the localhost.
  Access the admin panel by going to localhost:4848
  
2. Download the repo.
  Clone this repo into any folder, make sure the folder is specifically for the application and does not contain any other files.
  
3. Add app to Glassfish
  Go to the glassfish admin panel, and click on applications, then click add, and add the folder which contains the repo.
  The context path option is the path that will be used as the application root.
    Example context root of : myapplication
    Result of the index file will be : localhost:8080/myapplication/index.jsp
    
4. Source Editing
  Any java source files should be added to the src folder. Any other pages will be available at the URL at their respective directory.
  Java files are in a directory structure that represents their package.

5. Building
  CD to the app folder.
  run $ gradle classes
  DO NOT RUN JUST gradle !!! This will cause a mess of files!
  
6. Running
  Whenever a change is made to the java classes, building will not be enought to see changes.
  Go to the glassfish admin panel and click on server in the left panel, then click restart. Wait a few seconds and all will be updated.
  
