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
                    String type = readClient(in);
                    if (type.equals("exit")) {
                        loggedIn = false;
                        loginCorrect = true;
                        running = false;
                    } else if (type.equals("Create")) {
                        System.out.println("Creating");
                        createUser(out, in);
                    } else if (type.equals("login")) {
                        username = readClient(in);
                        System.out.println(username);
                        String password = readClient(in);
                        System.out.println(password);
                        //code to check if username and password are correct store in variable goodInfo
                        boolean goodInfo = database.login(username, password);
                        System.out.println(goodInfo);
                        if (goodInfo) {
                            writeClient("goodInfo", out);
                            String name = database.getUsers(username).getName();
                            writeClient(name, out);
                            loginCorrect = true;
                        } else {
                            writeClient("badInfo", out);
                        }
                    }
                }
                while (loggedIn) {
                    String choice = readClient(in);
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
        writeClient("" + friends.size(), out);
    }
    public static void viewFriends(PrintWriter out, BufferedReader in, String username) {
        database.readUsernames();
        ArrayList<User> friends = database.getUsers(username).getFriends();
        writeClient("" + friends.size(), out);
        for (User user : friends) {
            System.out.println(user.getUsername());
            writeClient(user.getUsername(), out);
            String s = readClient(in);
            //System.out.println(s);
        }
    }

    public static void viewBlockedCount(PrintWriter out, BufferedReader in, String username) {
        database.readUsernames();
        ArrayList<User> blocked = database.getUsers(username).getBlocked();
        writeClient("" + blocked.size(), out);
    }
    public static void viewBlocked(PrintWriter out, BufferedReader in, String username) {
        database.readUsernames();
        ArrayList<User> blocked = database.getUsers(username).getBlocked();
        writeClient("" + blocked.size(), out);
        for (User user : blocked) {
            System.out.println(user.getUsername());
            writeClient(user.getUsername(), out);
            String s = readClient(in);
        }
    }

    public static void getMessages(PrintWriter out, BufferedReader in, String username) {
        System.out.println("mesage");
        String choice = readClient(in);
        choice = choice.substring(4);
            if (choice.indexOf(username) == 0) {
                choice = choice.substring(username.length(), choice.indexOf("."));
            } else {
                choice = choice.substring(0, username.length() - 1);
            }
            System.out.println("CHOIDCE : " + choice);
            ArrayList<String> message = database.readMessagesFromFile(username, choice);
            writeClient("" + message.size(), out);
            for (int j = 0; j < message.size(); j++) {
                writeClient(message.get(j), out);
            }
            while (true) {
                String newMessage = readClient(in);
                if (newMessage.equals("exit")) {
                    break;
                } else {
                    database.writeMessageToFile(username, choice, newMessage);
                }
            }

    }

    public static void findUser(PrintWriter out, BufferedReader in, String username) {
        String search = readClient(in);
        System.out.println(search);
        if (database.getUsers(search) != null) {
            writeClient(database.getUsers(search).toString(), out);
            String searchChoice = "";
            System.out.println(searchChoice);
            do {
                searchChoice = readClient(in);
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
                        writeClient("exists", out);
                        System.out.println("exists");
                    } else {
                        writeClient("not exists", out);
                        database.writeMessageToFile(username, search, "Hello!");
//                        database.getUsers(username).newMessage(database.getUsers(search));
//                        database.getUsers(search).newMessage(database.getUsers(username));
                        System.out.println("not exists");
                    }
                }
            } while (searchChoice.equals("5"));
        } else {
            System.out.println("bad");
            writeClient("User not found", out);
        }
    }
    public static void removeFriend(PrintWriter out, BufferedReader in, String username) {
        String search = readClient(in);
        System.out.println("removing " + search);
        database.getUsers(search).removeFriend(database.getUsers(search));
        database.getUsers(username).removeFriend(database.getUsers(search));
    }
    public static void removeBlocked(PrintWriter out, BufferedReader in, String username) {
        String search = readClient(in);
        System.out.println("unblocking " + search);
        database.getUsers(username).unblockUser(database.getUsers(search));
    }
    public static void createUser(PrintWriter out, BufferedReader in) {
        String newUsername = readClient(in);
        ArrayList<String> usernames = database.getUsernames();
        if (usernames.contains(newUsername)) {
            writeClient("Exists", out);
        } else if (newUsername.contains(",")){
            writeClient("Username cannot contain commas", out);
        } else {
            writeClient("GoodUsername", out);
            String newPassword = readClient(in);
            if (newPassword.contains(",")) {
                writeClient("Password cannot contain commas", out);
            } else {
                writeClient("Good Password", out);
                String newName = readClient(in);
                if (newName.contains(",")) {
                    writeClient("Name cannot contain commas", out);
                } else {
                    writeClient("GoodName", out);
                    String newAge = readClient(in);
                    if (newAge.contains(",")) {
                        writeClient("Age cannot contain commas", out);
                    }
                    try {
                        int age = Integer.parseInt(newAge);
                        if (age < 0) {
                            writeClient("Age cannot less than zero", out);
                        } else {
                            try {
                                database.createUser(newName + "," + newUsername + "," + age + "," + newPassword);
                                writeClient("User created", out);
                            } catch (IncorrectInput e) {
                                writeClient(e.getMessage(), out);
                            }
                        }
                    } catch (NumberFormatException e) {
                        writeClient("Age must be a number", out);
                    }
                }
            }
        }
    }

    public static void newPassword(PrintWriter out, BufferedReader in) {
        String data = readClient(in);
        System.out.println(data);
        String[] parts = data.split(",");
        if (parts.length == 2) {
            writeClient("valid", out);
        } else {
            writeClient("invalid", out);
            return;
        }
        boolean successful = database.updatePassword(parts[0], parts[1]);
        writeClient(String.valueOf(successful), out);
    }

    public static void newAge(PrintWriter out, BufferedReader in) {
        String data = readClient(in);
        String[] parts = data.split(",");
        System.out.println("Data received");
        boolean successful = database.updateAge(parts[0], Integer.parseInt(parts[1]));
        System.out.println(successful);
        writeClient(String.valueOf(successful), out);

    }

    public static void newName(PrintWriter out, BufferedReader in) {
        String data = readClient(in);
        String[] parts = data.split(",");
        if (parts.length != 2) {
            writeClient("false", out);
        } else {
            boolean success = database.updateName(parts[0], parts[1]);
            writeClient(String.valueOf(success), out);
        }
    }
    /**
     * A method to read the message the client sends
     *
     * @param in the reader to use
     * @return returns the received message from the server
     */
    public static String readClient(BufferedReader in) {
        try {
            String s = in.readLine();
            return s;
        } catch (IOException e) {
            e.printStackTrace();
            return "Socket is not connected1";
        }
    }

    /**
     * a method to send data to the client
     * @param msg the message to be sent
     * @param out the printwriter to use
     * @return returns true if sent successfully and false otherwise
     */
    public static void writeClient(String msg, PrintWriter out) {
        // writes and sends the msg to the client, then flushes the writer
        out.write(msg);
        out.println();
        out.flush();
    }
}
