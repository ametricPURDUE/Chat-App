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
                    String type = SubServerInterface.readClient(in);
                    if (type.equals("exit")) {
                        loggedIn = false;
                        loginCorrect = true;
                        running = false;
                    } else if (type.equals("Create")) {
                        System.out.println("Creating");
                        createUser(out, in);
                    } else if (type.equals("login")) {
                        username = SubServerInterface.readClient(in);
                        System.out.println(username);
                        String password = SubServerInterface.readClient(in);
                        System.out.println(password);
                        //code to check if username and password are correct store in variable goodInfo
                        boolean goodInfo = database.login(username, password);
                        System.out.println(goodInfo);
                        if (goodInfo) {
                            SubServerInterface.writeClient("goodInfo", out);
                            String name = database.getUsers(username).getName();
                            SubServerInterface.writeClient(name, out);
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
                            viewFriendsCount(out, in, username);
                            break;
                        case "2" : //sends all blocked users to client to display
                            viewBlockedCount(out, in, username);
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
                        case "6":
                            newPassword(out, in);
                            break;
                        case "7":
                            newAge(out, in);
                            break;
                        case "8":
                            viewFriends(out, in, username);
                            break;
                        case "9":
                            viewBlocked(out, in, username);
                            break;
                        case "10":
                            removeFriend(out, in, username);
                            break;
                        case "11":
                            removeBlocked(out, in, username);
                            break;
                        case "12":
                            newName(out, in);
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
    public static void viewFriendsCount(PrintWriter out, BufferedReader in, String username) {
        database.readUsernames();
        ArrayList<User> friends = database.getUsers(username).getFriends();
        SubServerInterface.writeClient("" + friends.size(), out);
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

    public static void viewBlockedCount(PrintWriter out, BufferedReader in, String username) {
        database.readUsernames();
        ArrayList<User> blocked = database.getUsers(username).getBlocked();
        SubServerInterface.writeClient("" + blocked.size(), out);
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
        System.out.println("mesage");
        String choice = SubServerInterface.readClient(in);
        choice = choice.substring(4);
            if (choice.indexOf(username) == 0) {
                choice = choice.substring(username.length(), choice.indexOf("."));
            } else {
                choice = choice.substring(0, username.length() - 1);
            }
            System.out.println("CHOIDCE : " + choice);
            ArrayList<String> message = database.readMessagesFromFile(username, choice);
            SubServerInterface.writeClient("" + message.size(), out);
            for (int j = 0; j < message.size(); j++) {
                SubServerInterface.writeClient(message.get(j), out);
            }
            while (true) {
                String newMessage = SubServerInterface.readClient(in);
                if (newMessage.equals("exit")) {
                    break;
                } else {
                    database.writeMessageToFile(username, choice, newMessage);
                }
            }

    }

    public static void findUser(PrintWriter out, BufferedReader in, String username) {
        String search = SubServerInterface.readClient(in);
        System.out.println(search);
        if (database.getUsers(search) != null) {
            SubServerInterface.writeClient(database.getUsers(search).toString(), out);
            String searchChoice = "";
            System.out.println(searchChoice);
            do {
                searchChoice = SubServerInterface.readClient(in);
                if (searchChoice.equals("1")) {
                    System.out.println("running");
                    database.getUsers(search).addFriend(database.getUsers(username));
                    database.getUsers(username).addFriend(database.getUsers(search));
                } else if (searchChoice.equals("2")) {
                    database.getUsers(username).blockUser(database.getUsers(search));
                } else if (searchChoice.equals("5")) {
                    String filename;
                    if (username.compareTo(search) < 0) {
                        filename = "chat" + username + search + ".txt";
                    } else {
                        filename = "chat" + search + username + ".txt";
                    }
                    File file = new File(filename);
                    if (file.isFile()) {
                        SubServerInterface.writeClient("exists", out);
                        System.out.println("exists");
                    } else {
                        SubServerInterface.writeClient("not exists", out);
                        database.writeMessageToFile(username, search, "Hello!");
//                        database.getUsers(username).newMessage(database.getUsers(search));
//                        database.getUsers(search).newMessage(database.getUsers(username));
                        System.out.println("not exists");
                    }
                }
            } while (searchChoice.equals("5"));
        } else {
            System.out.println("bad");
            SubServerInterface.writeClient("User not found", out);
        }
    }
    public static void removeFriend(PrintWriter out, BufferedReader in, String username) {
        String search = SubServerInterface.readClient(in);
        System.out.println("removing " + search);
        database.getUsers(search).removeFriend(database.getUsers(search));
        database.getUsers(username).removeFriend(database.getUsers(search));
    }
    public static void removeBlocked(PrintWriter out, BufferedReader in, String username) {
        String search = SubServerInterface.readClient(in);
        System.out.println("unblocking " + search);
        database.getUsers(username).unblockUser(database.getUsers(search));
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
        }
    }

    public static void newPassword(PrintWriter out, BufferedReader in) {
        String data = SubServerInterface.readClient(in);
        System.out.println(data);
        String[] parts = data.split(",");
        if (parts.length == 2) {
            SubServerInterface.writeClient("valid", out);
        } else {
            SubServerInterface.writeClient("invalid", out);
            return;
        }
        boolean successful = database.updatePassword(parts[0], parts[1]);
        SubServerInterface.writeClient(String.valueOf(successful), out);
    }

    public static void newAge(PrintWriter out, BufferedReader in) {
        String data = SubServerInterface.readClient(in);
        String[] parts = data.split(",");
        System.out.println("Data received");
        boolean successful = database.updateAge(parts[0], Integer.parseInt(parts[1]));
        System.out.println(successful);
        SubServerInterface.writeClient(String.valueOf(successful), out);

    }

    public static void newName(PrintWriter out, BufferedReader in) {
        String data = SubServerInterface.readClient(in);
        String[] parts = data.split(",");
        if (parts.length != 2) {
            SubServerInterface.writeClient("false", out);
        } else {
            boolean success = database.updateName(parts[0], parts[1]);
            SubServerInterface.writeClient(String.valueOf(success), out);
        }
    }
}
