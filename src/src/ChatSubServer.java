import java.io.*;
import java.net.*;
import java.util.ArrayList;

class ChatSubServer implements Runnable {
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
                    username = readClient(in);
                    if (username.equals("exit")) {
                        loggedIn = false;
                        loginCorrect = true;
                        running = false;
                    } else {
                        System.out.println(username);
                        String password = readClient(in);
                        System.out.println(password);
                        //code to check if username and password are correct store in variable goodInfo
                        boolean goodInfo = database.login(username, password);
                        System.out.println(goodInfo);
                        if (goodInfo) {
                            writeClient("goodInfo", out);
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
                            database.readUsernames();
                            ArrayList<User> friends = database.getUsers(username).getFriends();
                            writeClient("" + friends.size(), out);
                            for (User user : friends) {
                                System.out.println(user.getUsername());
                                writeClient(user.getUsername(), out);
                                String s = readClient(in);
                                //System.out.println(s);
                            }
                            break;
                        case "2" : //sends all blocked users to client to display
                            database.readUsernames();
                            ArrayList<User> blocked = database.getUsers(username).getBlocked();
                            writeClient("" + blocked.size(), out);
                            for (User user : blocked) {
                                System.out.println(user.getUsername());
                                writeClient(user.getUsername(), out);
                                String s = readClient(in);
                            }
                            break;
                        case "3":
                            ArrayList<String> messages = database.getUsers(username).getMessages();
                            writeClient("" + messages.size(), out);
                            for (String message : messages) {
                                message = message.substring(4);
                                if (message.indexOf(username) != 0) {
                                    message = message.substring(0, message.indexOf(username));
                                } else {
                                    System.out.println(message);
                                    message = message.substring(username.length(), message.length() - 4);
                                    System.out.println(message);;
                                }
                                writeClient(message, out);
                                readClient(in);
                            }
                            break;
                        case "4":
                            String search = readClient(in);
                            if (database.getUsers(search) != null) {
                                writeClient(database.getUsers(search).toString(), out);
                                String searchChoice = readClient(in);
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
                                writeClient("User not found", out);
                            }
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
        //System.out.println(msg + " sent");
    }
}
