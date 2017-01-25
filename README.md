# Mango - Server Installation Guide
Mango collaboration application.

Glassfish Server Application

- Install Glassfish server.
   - Download and extract to folder.
   - CD to install folder (glassfish4) and execute $ ./glassfish/bin/asadmin start-domain domain1
   - This will start the websever on the localhost.
   - Access the admin panel by going to localhost:4848
  
- Download the repo.
   - Clone this repo into a new folder (THE FOLDER MUST BE IN THE glassfish4/glassfish/ directory). EG. glassfish4/glassfish/myapp/
  
- Add Jar libraries to Glassfish.
   - Building will not work if the required Jars are not in the glassfish4/glassfish/lib folder.
   - Copy from local repo lib folder: mysql-connector-java-5.1.40-bin.jar and move it to the lib folder glassfish/lib.
   - Copy from local repo lib folder: jersey-bundle-1.19.1.jar and move it to the lib folder glassfish/lib.
  
- Add app to Glassfish.
   - Go to the glassfish admin panel, and click on applications, then click Deploy, and add the folder which contains the repo.
   - The context path option is the path that will be used as the application root.
     - Example context root of : myapplication
     - Result of the index file will be : localhost:8080/myapplication/index.jsp
    
- Source Editing.
   - Any java source files should be added to the src folder. Any other pages will be available at the URL at their respective directory.
   - Java files are in a directory structure that represents their package.

- Building.
   - CD to the app folder.
   - run $ gradle classes
      - DO NOT RUN JUST gradle !!! This will cause a mess of files!
  
- Running.
   - Whenever a change is made to the java classes, building will not be enough to see changes.
   - Go to the glassfish admin panel and click on server in the left panel, then click restart. Wait a few seconds and all will be updated.
  
