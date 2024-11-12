# Chat-App
TODO:
ChatDatabase:  
Also a "RunLocalTest.java" file  AJ

MainChatServer:
make an interface
Should be done besides that

ChatSubServer:
make an interface
Write all the code to interact with client and server

ChatClient:
- Search method/code
    - Doesn't need to be method but it will look nicer
- Messaging method/code\n
- View profile method\n
- ^
- above methods need to work with server as well

RunLocalTest
Read me documentation
1000 word report


Class Descriptions :
 - ChatDatabaseInterface.java:
     - Interface for ChatDatabase
     - Includes abstract versions of all methods required by ChatDatabase.java
     - Implemented by ChatDatabase.java
 - ChatDatabase.java
     - Main class for database
     - Stores a list of users, usernames, and passwords by writing them to a file in order
     - Allows for creation and modification of users
 - User.java
     - Object created for each user through ChatDatabase.java
     - Stores two lists of users, one for blocked users and one for friends
     - Also stores a users name, age, and username
 - UserTemplate.java
     - Interface for User.java
     - Includes abstract versions of all methods required by User.java
 - IncorrectInput.java
     - Extends Exception
     - is thrown when the input given to create or modify a user is not formatted correctly
   
