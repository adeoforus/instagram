# instagram

**Note: the Project is not yet complete**

**The Project Concept**

The folder java-instagram-web is where the Web Based front end is stored. From there a user would initiate a Request to the Java Server and then the java server would start the map reduce tasks, and when all tasks are complete, a response would be sent back to the PHP-Client specifying the location of the results. 


**To Test the Project, There are a few steps that are required to be done:**

 - Through Command Prompt, you must navigate to the folder java-instagram-web and execute the following command :  **php composer.phar install**. This will install the required dependencies. **Note:** the web front end is not yet hooked up to the Java Back End
 - In the Java Project in folder global_server, you must do the following:
		 - Run the Class GeneralServer in Package server (Communication to PHP-Client)
		 - Run the Class MasterServer in Package master
		 - Run the Class SlaveMachine1 in Pachage slave
		 - Then finally to simulate a request from a client run the class Client in the package server

	   
I have added a screen shot of the front end incase you wish to see what the front end looks like. It is in the root folder under the name screenshot.PNG 