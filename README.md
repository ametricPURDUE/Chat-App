# Chat-App
TODO:
MainChatServer:
make an interface
Should be done besides that

Make a login function - Nolan

ChatSubServer:
make an interface
Write all the code to interact with client and server - nolan

ChatClient:
- Search method/code
    - Doesn't need to be method but it will look nicer
- Messaging method/code\n
- View profile method\n - nolan
- above methods need to work with server as well - nolan
- search - Edward


Server Stuff
- Receive Method - Nolan
- User messages txt file creation in the database - Pranav
- thread safe - Edward
- Test Cases - Parshawn

RunLocalTest, 
Read me documentation, 
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
 - ChatClient.java
     - Main method allows user to login, view friends/blocked, search users, and view conversations
 - ClientInterface.java
     - Interface for ChatClient.java
     - Has static read and write methods to interact with servers
 - MainChatServer.java
     - Main server, client connects and is sent the port of an open subserver
     - opens 10 subserver threads that wait for user connection
 - MainServerInterface.java
     - Interface for MainChatServer.java
 - ChatSubServer.java
     - Server that interacts with database and client
     - processes input from the client and acts accordingly, either modifying the database or returning the correct data
 - SubServerInterface.java
     - Interface for subServer
     - has static read and write methods to interact with client
  
Test Descriptions:
    - ChatClient.java:
        - main(Strin[] args)
            - Was tested a multitude of ways including sending all valid servers, some valid; some not, and all invalid servers to make sure it worked properly
    -ClientInterface.java:
        - readServer(Socket socket)
            - Was tested with both a valid and invalid server
            - Was sent many data types and all were returned correctly as strings
        - writeServer(String msg, Socket socket)
            - Was tested with an empty, null, and non-empty string
            - Both a valid and invalid server were passed through
    - ChatDatabase.java:
        - ChatDatabase()
            - multiple objects were created to test for static descrepencies
        - ChatDatabase(ArrayList<User> userList, ArrayList<String> usernames, ArrayList<String> passwords)
            - multiple objects were created to test for static descrepencies
            - Both empty, non-existence, and non-empty ArrayLists were passed to make sure they all worked
        - getUsers()
            - tested with empty and non-empty userList to make sure return values were correct
        - readUsernames()
            - created multple objects to make sure is was thread safe
            - tested with empty, non-empty, and non-existent files to make sure everything was initialized correctly
        - readPasswords();
            - created multple objects to make sure is was thread safe
            - tested with empty, non-empty, and non-existent files to make sure everything was initialized correctly
        - writeUsernames()
            - created multiple objects to test thread safety
            - tested with uninitialized, non-empty, and empty ArrayLists
        - writePasswords()
            - created multiple objects to test thread safety
            - tested with uninitialized, non-empty, and empty ArrayLists
        - updatePassword(String username, String newPassword)
            - was tested with invalid and valid username
            - was tested with empty and non-empty string
        - createUser(String data)
            - was tested with valid and invalid data
        - removeUser(User user)
            - tested with existent and non-existent users
        - modifyUser(User user, String data)
            - tested with existent and non-existent users
            - was tested with valid and invalid data
        - writeMessageToFile(String senderUsername, String receiverUsername, String message)
            - tested with valid and invalid senderUsername
            - tested with valid and invalid receiverUsername
            - tested with empty and non-empty username
        - readMessageFromFile(String senderUsername, String receiverUsername)
            - tested with valid and invalid senderUsername
            - tested with valid and invalid receiverUsername
        - searchFriends(String target)
            - tested with valid and invalid target strings
    - ChatSubServer.java:
        - run()
            - tested with multiple portNumbers to make sure that multiple servers are established
    - SubServerInterface.java
        - readClient(Socket socket)
            - tested invalid and valid socket ensure the correct connection
        - writeClient(String msg, Socket socket)
            - tested with empty, non-empty, and null msg
            - tested with valid and invalid socket
    - MainChatServer:
        - main(String[] args)
            - tested by running extensively to ensure all 10 sub servers were established correctly
    - User.java:
        - User(String name, String username, int age)
            - tested with multiple different values to ensure the User was created correctly
        - User(String data)
            - tested with valid and invalid data strings
        - getName(), getUsername(), getAge()
            - tested with 5 different users to ensure correct data was returned
        - setName(String name), setUsername(String username), setAge(int age)
            - tested with multiple users to ensure the date was initialized correctly
            - tested with empty and non-empty name/username strings
        - getBlocked(), getFriends()
            - tested with multiple users to ensure that the correct users were on the list
        - setBlocked(ArrayList<User> blocked), setFriends(ArrayList<User> friends)
            - tested with empty, non-empty, and null array lists to ensure the array list, blocked, was correctly initialized
        - writeFriend()
            - tested with valid, invalid, empty, and non-empty files
        - writeBlocked()
            - tested with valid, invalid, empty, and non-empty files
        - readFriends()
            - tested with valid, invalid, empty, and non-empty files
        - readBlocked()
            - tested with valid, invalid, empty, and non-empty files
        - blockUser(User user)
            - tested with an existing user, non-existent user, and a user already on the blocked list
        - unblockUser(User unblocked)
            - tested with an existing user, non-existent user, and a user not already on the blocked list
        - addFriend(User user)
            - tested with an existing user, non-existent user, and a user already on the friends list
        - removeFriend(User unfriend)
            - tested with an existing user, non-existent user, and a user not already on the friends list
        - friendRequest(User user)
            - tested with valid and invalid user as well as an empty and non-empty friendRequests list
        - acceptFriendRequest(User user)
            - tested with valid and invalid user as well as an empty and non-empty friendrequests list
        - rejectFreindRequest(User user)
            - tested with valid and invalid user as well as an empty and non-empty friendRequests list
        - equals(User user)
            - tested with valid and invalid user, and an identical and different user
        - toString()
            - tested with multiple users to ensure that the output is correct
