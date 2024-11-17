import java.io.*;
import java.net.*;
import java.util.Scanner;

public class ChatClient implements ClientInterface{
    public static void main(String[] args) {
        int serverPort;
        int serverIndex;
        boolean hasServer = true;
        try {
            Socket mainSocket = new Socket("localhost", 4242); // Connects to main server
            BufferedReader reader = new BufferedReader(new InputStreamReader(mainSocket.getInputStream()));
            PrintWriter out = new PrintWriter(mainSocket.getOutputStream());
            String subserverPort = reader.readLine(); // Recieves port number and index of an open subserver from the main server
            if (subserverPort.equals("No Open Servers")) {
                hasServer = false;
                out.write("NoServerFound");
                out.println();
                out.flush();
                mainSocket.close();
                System.out.println("No Open Servers found");
            } else {
                serverPort = Integer.parseInt(subserverPort.substring(0, subserverPort.indexOf(";")));
                serverIndex = Integer.parseInt(subserverPort.substring(subserverPort.indexOf(";") + 1));
                Socket subSocket = new Socket("localhost", serverPort);
                out.write("ReceivedServer");
                out.println();
                out.flush();
                mainSocket.close();
                reader.close();
                out.close();
                PrintWriter subOut = new PrintWriter(subSocket.getOutputStream());
                BufferedReader subIn = new BufferedReader(new InputStreamReader(subSocket.getInputStream()));
                System.out.println("Server Port : " + serverPort);
                System.out.println("Server Index : " + serverIndex);
                System.out.println("Welcome!");
                Scanner scan = new Scanner(System.in);
                boolean continueLogin = true;
                boolean goodInfo = false;
                String username = "";
                boolean loggedIn = false;
                while (continueLogin) {
                    System.out.println("Please enter username to proceed, enter exit to exit");
                    username = scan.nextLine();
                    if (username.equals("exit")) {
                        continueLogin = false;
                        ClientInterface.writeServer(username, subOut);
                    } else {
                        ClientInterface.writeServer(username, subOut);
                        System.out.println("Please enter password");
                        ClientInterface.writeServer(scan.nextLine(), subOut);
                        String loginCheck = ClientInterface.readServer(subIn);
                        if (loginCheck.equals("goodInfo")) {
                            System.out.println("Login Successful, Welcome " + username);
                            continueLogin = false;
                            loggedIn = true;
                        } else {
                            System.out.println("Login Failed, please try again");
                        }
                        // Users own profile will be displayed here
                        while (loggedIn) {
                            System.out.println("press 1 to view friends, 2 to view blocked, 3 to view messages, 4 to search for users and 5 to logout");
                            String choice = scan.nextLine();
                            ClientInterface.writeServer(choice, subOut);
                            if (choice.equals("1")) {
                                int friendCount = Integer.parseInt(ClientInterface.readServer(subIn));
                                if (friendCount < 1) {
                                    System.out.println("No friends");
                                } else {
                                    System.out.println("Friends:");
                                    for (int i = 0; i < friendCount; i++) {
                                        String friend = ClientInterface.readServer(subIn);
                                        ClientInterface.writeServer("recieved", subOut);
                                        System.out.println(friend);
                                    }
                                }
                            } else if (choice.equals("2")) {
                                int blockedCount = Integer.parseInt(ClientInterface.readServer(subIn));
                                if (blockedCount < 1) {
                                    System.out.println("No blocked users");
                                } else {
                                    System.out.println("Blocked:");
                                    for (int i = 0; i < blockedCount; i++) {
                                        String blocked = ClientInterface.readServer(subIn);
                                        ClientInterface.writeServer("recieved", subOut);
                                        System.out.println(blocked);
                                    }
                                }
                            } else if (choice.equals("3")) {
                                int messageCount = Integer.parseInt(ClientInterface.readServer(subIn));
                                if (messageCount < 1) {
                                    System.out.println("No messages");
                                } else {
                                    System.out.println("Messages:");
                                    for (int i = 0; i < messageCount; i++) {
                                        String message = ClientInterface.readServer(subIn);
                                        ClientInterface.writeServer("recieved", subOut);
                                        System.out.println(message);
                                    }
                                }
                            } else if (choice.equals("4")) {
                                System.out.println("Please enter username");
                                String search = scan.nextLine();
                                ClientInterface.writeServer(search, subOut);
                                String found = ClientInterface.readServer(subIn);
                                System.out.println(found);
                                if (!found.equals("User not found")) {
                                    System.out.println("1 to friend, 2 to block, 3 to remove friend, 4 to unblock");
                                    String searchChoice = scan.nextLine();
                                    ClientInterface.writeServer(searchChoice, subOut);
                                }
                            } else if (choice.equals("5")) {
                                System.out.println("Goodbye " + username);
                                loggedIn = false;
                                continueLogin = true;
                            } else {
                                System.out.println("Invalid choice");
                            }
                        }
                    }
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
