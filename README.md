# Mango - Server Installation Guide

Glassfish Server Application

- Install Glassfish server.
   - Download and extract to a folder. Preferably in `~home`
   - CD to install folder (glassfish4) and execute `$ ./glassfish/bin/asadmin start-domain domain1`
   - This will start the websever on `localhost:8080`
   - Access the admin panel by going to `localhost:4848`
  
- Download the repo.
   - Clone this repo into a new folder (The folder must be in: glassfish4/glassfish/). EG. `glassfish4/glassfish/myapp/`
   
- Manually add lib.
   - Due to a bug in Glassfish, it is required to replace a lib file. **Copy the `org.eclipse.persistence.moxy.jar` from the lib folder of the repository into the `glassfish4/glassfish/modules/` folder and replace the original file.**
  
- Add app to Glassfish.
   - Go to the glassfish admin panel, and click on applications, then click Deploy, and add the folder which contains the repo.
   - The context path option is the path that will be used as the application root.
     - Example context root of : `myapplication`
     - Result of the index file will be : `localhost:8080/myapplication/index.jsp`
    
- Source Editing.
   - Any java source files should be added to the src folder. Any other pages will be available at the URL at their respective directory.
   - Java files are in a directory structure that represents their package.

- Building.
   - CD to the app folder.
   - run `$ gradle classes`
      - **Do not run: `$ gradle` alone, it will create a directory structure that is not needed.**
  
- Running.
   - Whenever a change is made to the java classes, building will not be enough to see changes.
   - Go to the glassfish admin panel and click on server in the left panel, then click restart. Wait a few seconds and all will be updated.
  
___
  
# Mango - Application Guide

Overview flowchart: https://drive.google.com/file/d/0B1qxfIcmRWOsV1JleHlyb0dBQm8/view

Development trello: https://trello.com/b/YAlDCXr3/master-development

The flowchart is always kept up to date with progress and is considered to be the live view of how the application functions.

- Users and Authentication.
   - The users are divided into 3 catagories; normal, manager and admin, with level values of 0, 1 and 2 respectively. Normal members cannot access the editgroup.jsp, which can be seen in the flowchart. Admin currently has no benifit over manager, but is reserved for future use. Note, the green in the flowchart corresponds to 'NO ACCOUNT' and not to normal members. Normal memebrs are reflected with blue coloring in the flowchart.
      - User permissions can be given on user creation by typing 'manager' or 'admin' into the access code field upon account creation.
   - Authentication
      - Currently user passwords are hashed with the MD5 algorithm. This way the database does not know the password of the user, and is able to compare a signin password with a stored password without knowing the actual password, since the hashed versions are compaired.
      
- Accessing User data on JSP pages.
   - When a user is given a session upon signin, the user session is also given attributes which the client can access as needed. These can be accessed with `<%= session.getAttribute("username") %>` inside the JSP. The attribute `username` can be replaced with any of the following:
      - userid
      - username
      - firstname
      - lastname
      - email
      - level
   - Note: Int attributes will have to be cast to integers. `Integer.valueOf(String.valueOf(session.getAttribute("level")))`
      - userid is also an int and will require being cast.
      
- RESTful API.
   - The following is a list of urls which are accessable. (FormParam) **Not JSON!**
     - users/
     - users/username/{username}
     - users/userid/{userid}
     - users/signin (username, password)
     - users/signup (username, password, email, firstname, lastname, accesscode)
     - notes/
     - notes/addnote (username, title, note)
     
- Directory Structure.
   - pages/ **Incorrect!**
      - signin.jsp
      - signup.jsp
      - member/ (authfilter)
         - addnote.jsp
         - wall.jsp
   - rest/
      - See above.
