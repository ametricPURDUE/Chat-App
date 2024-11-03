# Chat-App
TODO:
User class:
- addFriend(User friend);  Parshawn
- removeFriend(User Friend);  Parshawn
- blockUser(User user);  Parshawn
- unblockUser(User use);  Parshawn

ChatDatabase:  
Also a "RunLocalTest.java" file  AJ


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
   
