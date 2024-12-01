import java.io.*;
import java.net.*;
import java.util.ArrayList;

public class ChatSubServer implements Runnable, SubServerInterface {
    private int portNumber;
    ServerSocket serverSocket;
    private boolean running = false;
    private static ChatDatabase database = new ChatDatabase();
    public ChatSubServer(int portNumber) {
        this.portNumber = portNumber;
    }
    public void run() {
        try {
            serverSocket = new ServerSocket(portNumber);
            Socket socket = serverSocket.accept();
            System.out.println("client connected");
            running = true;
            PrintWriter out = new PrintWriter(socket.getOutputStream());
            BufferedReader in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
            while(running) {
                boolean loggedIn = true;
                boolean loginCorrect = false;
                String username = "";
                while (!loginCorrect) {
                    username = SubServerInterface.readClient(in);
                    if (username.equals("exit")) {
                        loggedIn = false;
                        loginCorrect = true;
                        running = false;
                    } else if (username.equals("Create")) {
                        createUser(out, in);
                    } else {
                        System.out.println(username);
                        String password = SubServerInterface.readClient(in);
                        System.out.println(password);
                        //code to check if username and password are correct store in variable goodInfo
                        boolean goodInfo = database.login(username, password);
                        System.out.println(goodInfo);
                        if (goodInfo) {
                            SubServerInterface.writeClient("goodInfo", out);
                            loginCorrect = true;
                        } else {
                            SubServerInterface.writeClient("badInfo", out);
                        }
                    }
                }
                while (loggedIn) {
                    String choice = SubServerInterface.readClient(in);
                    System.out.println(choice);
                    switch (choice) {
                        case "1" : //sends all friends to client to display
                            viewFriends(out, in, username);
                            break;
                        case "2" : //sends all blocked users to client to display
                            viewBlocked(out, in, username);
                            break;
                        case "3":
                            getMessages(out, in, username);
                            break;
                        case "4":
                            findUser(out, in, username);
                            break;
                        case "5":
                            loggedIn = false;
                            loginCorrect = false;
                            break;
                    }
                }

            }
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
    public boolean portOpen() {
        return !running;
    }
    public static void viewFriends(PrintWriter out, BufferedReader in, String username) {
        database.readUsernames();
        ArrayList<User> friends = database.getUsers(username).getFriends();
        SubServerInterface.writeClient("" + friends.size(), out);
        for (User user : friends) {
            System.out.println(user.getUsername());
            SubServerInterface.writeClient(user.getUsername(), out);
            String s = SubServerInterface.readClient(in);
            //System.out.println(s);
        }
    }
    public static void viewBlocked(PrintWriter out, BufferedReader in, String username) {
        database.readUsernames();
        ArrayList<User> blocked = database.getUsers(username).getBlocked();
        SubServerInterface.writeClient("" + blocked.size(), out);
        for (User user : blocked) {
            System.out.println(user.getUsername());
            SubServerInterface.writeClient(user.getUsername(), out);
            String s = SubServerInterface.readClient(in);
        }
    }
    public static void getMessages(PrintWriter out, BufferedReader in, String username) {
        ArrayList<String> messages = database.getUsers(username).getMessages();
        SubServerInterface.writeClient("" + messages.size(), out);
        for (String message : messages) {
            message = message.substring(4);
            if (message.indexOf(username) != 0) {
                message = message.substring(0, message.indexOf(username));
            } else {
                System.out.println(message);
                message = message.substring(username.length(), message.length() - 4);
                System.out.println(message);;
            }
            SubServerInterface.writeClient(message, out);
            SubServerInterface.readClient(in);
        }
    }
    public static void findUser(PrintWriter out, BufferedReader in, String username) {
        String search = SubServerInterface.readClient(in);
        if (database.getUsers(search) != null) {
            SubServerInterface.writeClient(database.getUsers(search).toString(), out);
            String searchChoice = SubServerInterface.readClient(in);
            System.out.println(searchChoice);
            if (searchChoice.equals("1")) {
                System.out.println("running");
                database.getUsers(search).addFriend(database.getUsers(username));
                database.getUsers(username).addFriend(database.getUsers(search));
            } else if (searchChoice.equals("2")) {
                database.getUsers(username).blockUser(database.getUsers(search));
            } else if (searchChoice.equals("3")) {
                database.getUsers(search).removeFriend(database.getUsers(search));
                database.getUsers(username).removeFriend(database.getUsers(search));
            } else if (searchChoice.equals("4")) {
                database.getUsers(username).unblockUser(database.getUsers(search));
            }
        } else {
            System.out.println("bad");
            SubServerInterface.writeClient("User not found", out);
        }
    }
    public static void createUser(PrintWriter out, BufferedReader in) {
        String newUsername = SubServerInterface.readClient(in);
        ArrayList<String> usernames = database.getUsernames();
        if (usernames.contains(newUsername)) {
            SubServerInterface.writeClient("Exists", out);
        } else if (newUsername.contains(",")){
            SubServerInterface.writeClient("Username cannot contain commas", out);
        } else {
            SubServerInterface.writeClient("GoodUsername", out);
            String newPassword = SubServerInterface.readClient(in);
            if (newPassword.contains(",")) {
                SubServerInterface.writeClient("Password cannot contain commas", out);
            } else {
                SubServerInterface.writeClient("Good Password", out);
                String newName = SubServerInterface.readClient(in);
                if (newName.contains(",")) {
                    SubServerInterface.writeClient("Name cannot contain commas", out);
                } else {
                    SubServerInterface.writeClient("GoodName", out);
                    String newAge = SubServerInterface.readClient(in);
                    if (newAge.contains(",")) {
                        SubServerInterface.writeClient("Age cannot contain commas", out);
                    }
                    try {
                        int age = Integer.parseInt(newAge);
                        if (age < 0) {
                            SubServerInterface.writeClient("Age cannot less than zero", out);
                        } else {
                            try {
                                database.createUser(newName + "," + newUsername + "," + age + "," + newPassword);
                                SubServerInterface.writeClient("User created", out);
                            } catch (IncorrectInput e) {
                                SubServerInterface.writeClient(e.getMessage(), out);
                            }
                        }
                    } catch (NumberFormatException e) {
                        SubServerInterface.writeClient("Age must be a number", out);
                    }
                }
            }
        }}
}
