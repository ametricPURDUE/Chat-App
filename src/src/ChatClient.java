import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.*;
import java.net.*;
import javax.swing.*;
import java.awt.*;

public class ChatClient implements ClientInterface {
    public static void main(String[] args) {
        int serverPort;
        int serverIndex;
        boolean hasServer = true;
        JFrame frame = new JFrame("Chat App");
        Container home = frame.getContentPane();
        frame.setSize(600, 400);
        frame.setLocationRelativeTo(null);
        frame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        //Initialize all JPanels
        JPanel loginScreen = new JPanel();
        JPanel createScreen = new JPanel();
        SpringLayout loginLayout = new SpringLayout();
        SpringLayout createScreenLayout = new SpringLayout();
        loginScreen.setLayout(loginLayout);
        createScreen.setLayout(createScreenLayout);
        //Create all JComponents for login screen
        JTextField usernameInput = new JTextField(15);
        JTextField passwordInput = new JTextField(15);
        JLabel usernameLabel = new JLabel("Username:");
        JLabel passwordLabel = new JLabel("Password:");
        JButton loginButton = new JButton("Login");
        JButton newUserButton = new JButton("New User");
        JLabel resultLabel = new JLabel("");
        loginScreen.add(usernameLabel);
        loginScreen.add(usernameInput);
        loginScreen.add(passwordLabel);
        loginScreen.add(passwordInput);
        loginScreen.add(loginButton);
        loginScreen.add(newUserButton);
        loginScreen.add(resultLabel);
        //Layout for username label text label
        loginLayout.putConstraint(SpringLayout.WEST, usernameLabel, 150, SpringLayout.WEST, loginScreen);
        loginLayout.putConstraint(SpringLayout.NORTH, usernameLabel, 100, SpringLayout.NORTH, loginScreen);
        //loginLayout for username input text field
        loginLayout.putConstraint(SpringLayout.WEST, usernameInput, 10, SpringLayout.EAST, usernameLabel);
        loginLayout.putConstraint(SpringLayout.NORTH, usernameInput, 100, SpringLayout.NORTH, loginScreen);
        //loginLayout for password label text label
        loginLayout.putConstraint(SpringLayout.WEST, passwordLabel, 150, SpringLayout.WEST, loginScreen);
        loginLayout.putConstraint(SpringLayout.NORTH, passwordLabel, 30, SpringLayout.SOUTH, usernameInput);
        //loginLayout for password input text field
        loginLayout.putConstraint(SpringLayout.WEST, passwordInput, 10, SpringLayout.EAST, passwordLabel);
        loginLayout.putConstraint(SpringLayout.NORTH, passwordInput, 30, SpringLayout.SOUTH, usernameInput);
        //loginLayout for login button
        loginLayout.putConstraint(SpringLayout.WEST, loginButton, 225, SpringLayout.WEST, loginScreen);
        loginLayout.putConstraint(SpringLayout.EAST, loginButton, 375, SpringLayout.WEST, loginScreen);
        loginLayout.putConstraint(SpringLayout.NORTH, loginButton, 30, SpringLayout.SOUTH, passwordInput);
        //loginLayout for new user button
        loginLayout.putConstraint(SpringLayout.WEST, newUserButton, 225, SpringLayout.WEST, loginScreen);
        loginLayout.putConstraint(SpringLayout.EAST, newUserButton, 375, SpringLayout.WEST, loginScreen);
        loginLayout.putConstraint(SpringLayout.NORTH, newUserButton, 20, SpringLayout.SOUTH, loginButton);
        //loginLayout for result label
        loginLayout.putConstraint(SpringLayout.WEST, resultLabel, 200, SpringLayout.WEST, loginScreen);
        loginLayout.putConstraint(SpringLayout.EAST, resultLabel, 600, SpringLayout.WEST, loginScreen);
        loginLayout.putConstraint(SpringLayout.NORTH, resultLabel, 20, SpringLayout.SOUTH, newUserButton);
        frame.add(loginScreen);
        frame.setVisible(true);
        //creates all Jcomponents for create new user screen
        JTextField createUsername = new JTextField(15);
        JTextField createPassword = new JTextField(15);
        JTextField nameInput = new JTextField(15);
        JTextField ageInput = new JTextField(15);
        JLabel createUsernameLabel = new JLabel("Username:");
        JLabel createPasswordLabel = new JLabel("Password:");
        JLabel nameLabel = new JLabel("Name:");
        JLabel ageLabel = new JLabel("Age:");
        JButton createButton = new JButton("Create");
        createScreen.add(nameLabel);
        createScreen.add(nameInput);
        createScreen.add(ageLabel);
        createScreen.add(ageInput);
        createScreen.add(createButton);
        createScreen.add(createPasswordLabel);
        createScreen.add(createUsernameLabel);
        createScreen.add(createUsername);
        createScreen.add(createPassword);
        //createLayout for createUsernameLabel
        createScreenLayout.putConstraint(SpringLayout.WEST, createUsernameLabel, 150, SpringLayout.WEST, createScreen);
        createScreenLayout.putConstraint(SpringLayout.NORTH, createUsernameLabel, 60, SpringLayout.NORTH, createScreen);
        //loginLayout for createUsername input text field
        createScreenLayout.putConstraint(SpringLayout.WEST, createUsername, 10, SpringLayout.EAST, createUsernameLabel);
        createScreenLayout.putConstraint(SpringLayout.NORTH, createUsername, 60, SpringLayout.NORTH, createScreen);
        //createLayout for createPasswordLabel
        createScreenLayout.putConstraint(SpringLayout.WEST, createPasswordLabel, 150, SpringLayout.WEST, createScreen);
        createScreenLayout.putConstraint(SpringLayout.NORTH, createPasswordLabel, 30, SpringLayout.SOUTH, createUsername);
        //loginLayout for createPassword input text field
        createScreenLayout.putConstraint(SpringLayout.WEST, createPassword, 10, SpringLayout.EAST, createPasswordLabel);
        createScreenLayout.putConstraint(SpringLayout.NORTH, createPassword, 30, SpringLayout.SOUTH, createUsername);
        //createLayout for nameLabel
        createScreenLayout.putConstraint(SpringLayout.WEST, nameLabel, 160, SpringLayout.WEST, createScreen);
        createScreenLayout.putConstraint(SpringLayout.NORTH, nameLabel, 30, SpringLayout.SOUTH, createPassword);
        //createLayout for name input text field
        createScreenLayout.putConstraint(SpringLayout.WEST, nameInput, 10, SpringLayout.EAST, createPasswordLabel);
        createScreenLayout.putConstraint(SpringLayout.NORTH, nameInput, 30, SpringLayout.SOUTH, createPassword);
        //createLayout for age label
        createScreenLayout.putConstraint(SpringLayout.WEST, ageLabel, 160, SpringLayout.WEST, createScreen);
        createScreenLayout.putConstraint(SpringLayout.NORTH, ageLabel, 30, SpringLayout.SOUTH, nameInput);
        //createLayout for age input
        createScreenLayout.putConstraint(SpringLayout.WEST, ageInput, 10, SpringLayout.EAST, createPasswordLabel);
        createScreenLayout.putConstraint(SpringLayout.NORTH, ageInput, 30, SpringLayout.SOUTH, nameInput);
        //createLayout for createButton
        createScreenLayout.putConstraint(SpringLayout.WEST, createButton, 275, SpringLayout.WEST, createScreen);
        //createScreenLayout.putConstraint(SpringLayout.EAST, createButton, 375, SpringLayout.WEST, createScreen);
        createScreenLayout.putConstraint(SpringLayout.NORTH, createButton, 30, SpringLayout.SOUTH, ageInput);


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
                //action listener for the login button
                loginButton.addActionListener(new ActionListener() {
                    public void actionPerformed(ActionEvent e) {
                        String username = usernameInput.getText();
                        String password = passwordInput.getText();
                        ClientInterface.writeServer("login", subOut);
                        ClientInterface.writeServer(username, subOut);
                        ClientInterface.writeServer(password, subOut);
                        String loginCheck = ClientInterface.readServer(subIn);
                        if (loginCheck.equals("goodInfo")) {
                            System.out.println("Login Successful, Welcome " + username);
                        } else {
                            System.out.println("Login Failed, please try again");
                        }
                    }
                });
                newUserButton.addActionListener(new ActionListener() {
                    public void actionPerformed(ActionEvent e) {
                        ClientInterface.writeServer("Create", subOut);
                        frame.add(createScreen);
                        frame.remove(loginScreen);
                        frame.revalidate();
                        frame.repaint();
                    }
                });
                createButton.addActionListener(new ActionListener() {
                    public void actionPerformed(ActionEvent e) {
                        ClientInterface.writeServer(createUsername.getText(),subOut);
                        String good = ClientInterface.readServer(subIn);
                        if (good.equals("GoodUsername")) {
                            ClientInterface.writeServer(createPassword.getText(), subOut);
                            String goodPassword = ClientInterface.readServer(subIn);
                            if (goodPassword.equals("Good Password")) {
                                ClientInterface.writeServer(nameInput.getText(), subOut);
                                String goodName = ClientInterface.readServer(subIn);
                                if (goodName.equals("GoodName")) {
                                    ClientInterface.writeServer(ageInput.getText(), subOut);
                                    String result = ClientInterface.readServer(subIn);
                                    System.out.println(result);
                                    resultLabel.setText(result);
                                    frame.add(loginScreen);
                                    frame.remove(createScreen);
                                    frame.revalidate();
                                    frame.repaint();
                                }
                            } else {
                                System.out.println(goodPassword);
                                resultLabel.setText(goodPassword);
                                frame.remove(createScreen);
                                frame.add(loginScreen);
                                frame.revalidate();
                                frame.repaint();
                            }
                        } else if (good.equals("Username cannot contain commas")) {
                            System.out.println(good);
                            resultLabel.setText(good);
                            frame.remove(createScreen);
                            frame.add(loginScreen);
                            frame.revalidate();
                            frame.repaint();
                        } else {
                            System.out.println("already exists");
                            resultLabel.setText("Username already exists");
                            frame.remove(createScreen);
                            frame.add(loginScreen);
                            frame.revalidate();
                            frame.repaint();
                        }
                    }
                });
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

}
//                System.out.println("Server Port : " + serverPort);
//                System.out.println("Server Index : " + serverIndex);
//                System.out.println("Welcome!");
//                Scanner scan = new Scanner(System.in);
//                boolean continueLogin = true;
//                boolean goodInfo = false;
//                String username = "";
//                boolean loggedIn = false;
//                frame.setVisible(true);
//                while (continueLogin) {
//                    System.out.println("Please enter username to proceed, enter exit to exit, or enter Create to create new user");
//                    username = scan.nextLine();
//                    if (username.equals("exit")) {
//                        continueLogin = false;
//                        ClientInterface.writeServer(username, subOut);
//                    } else if (username.equals("Create")) {
//                        ClientInterface.writeServer(username, subOut);
//                        System.out.println("Please enter username");
//                        String newUsername = scan.nextLine();
//                        ClientInterface.writeServer(newUsername, subOut);
//                        String good = ClientInterface.readServer(subIn);
//                        if (good.equals("GoodUsername")) {
//                            System.out.println("Please enter password");
//                            String newPassword = scan.nextLine();
//                            ClientInterface.writeServer(newPassword, subOut);
//                            String goodPassword = ClientInterface.readServer(subIn);
//                            if (goodPassword.equals("Good Password")) {
//                                System.out.println("Please enter your name");
//                                String newName = scan.nextLine();
//                                ClientInterface.writeServer(newName, subOut);
//                                String goodName = ClientInterface.readServer(subIn);
//                                if (goodName.equals("GoodName")) {
//                                    System.out.println("Please enter your age");
//                                    String newAge = scan.nextLine();
//                                    ClientInterface.writeServer(newAge, subOut);
//                                    String result = ClientInterface.readServer(subIn);
//                                    System.out.println(result);
//                                }
//                            } else {
//                                System.out.println(goodPassword);
//                            }
//                        } else if (good.equals("Username cannot contain commas")) {
//                            System.out.println(good);
//                        } else {
//                            System.out.println("Username already exists");
//                        }
//                    } else {
//                        ClientInterface.writeServer(username, subOut);
//                        System.out.println("Please enter password");
//                        ClientInterface.writeServer(scan.nextLine(), subOut);
//                        String loginCheck = ClientInterface.readServer(subIn);
//                        if (loginCheck.equals("goodInfo")) {
//                            System.out.println("Login Successful, Welcome " + username);
//                            continueLogin = false;
//                            loggedIn = true;
//                        } else {
//                            System.out.println("Login Failed, please try again");
//                        }
//                        // Users own profile will be displayed here
//                        while (loggedIn) {
//                            System.out.println("press 1 to view friends, 2 to view blocked, 3 to view messages, 4 to search for users and 5 to logout");
//                            String choice = scan.nextLine();
//                            ClientInterface.writeServer(choice, subOut);
//                            if (choice.equals("1")) {
//                                viewFriends(subOut, subIn);
//                            } else if (choice.equals("2")) {
//                                viewBlocked(subOut, subIn);
//                            } else if (choice.equals("3")) {
//                                viewMessage(subOut, subIn);
//                            } else if (choice.equals("4")) {
//                                viewUser(subOut, subIn);
//                            } else if (choice.equals("5")) {
//                                System.out.println("Goodbye " + username);
//                                loggedIn = false;
//                                continueLogin = true;
//                            } else {
//                                System.out.println("Invalid choice");
//                            }
//                        }
//                    }
//                }
//            }
//        } catch (IOException e) {
//            e.printStackTrace();
//        }
//    }
//    public static void viewFriends(PrintWriter out, BufferedReader in) {
//        int friendCount = Integer.parseInt(ClientInterface.readServer(in));
//        if (friendCount < 1) {
//            System.out.println("No friends");
//        } else {
//            System.out.println("Friends:");
//            for (int i = 0; i < friendCount; i++) {
//                String friend = ClientInterface.readServer(in);
//                ClientInterface.writeServer("recieved", out);
//                System.out.println(friend);
//            }
//        }
//    }
//    public static void viewBlocked(PrintWriter out, BufferedReader in) {
//        int blockedCount = Integer.parseInt(ClientInterface.readServer(in));
//        if (blockedCount < 1) {
//            System.out.println("No blocked users");
//        } else {
//            System.out.println("Blocked:");
//            for (int i = 0; i < blockedCount; i++) {
//                String blocked = ClientInterface.readServer(in);
//                ClientInterface.writeServer("recieved", out);
//                System.out.println(blocked);
//            }
//        }
//    }
//    public static void viewMessage(PrintWriter out, BufferedReader in) {
//        int messageCount = Integer.parseInt(ClientInterface.readServer(in));
//        System.out.println("Message count: " + messageCount);
//        if (messageCount < 1) {
//            System.out.println("No messages");
//        } else {
//            System.out.println("Messages:");
//            for (int i = 0; i < messageCount; i++) {
//                String message = ClientInterface.readServer(in);
//                ClientInterface.writeServer("recieved", out);
//                System.out.println(message);
//            }
//        }
//        System.out.println("Type the number of the message you would like to access or type exit to exit");
//        Scanner scan = new Scanner(System.in);
//        String choice = scan.nextLine();
//        ClientInterface.writeServer(choice, out);
//        String result = ClientInterface.readServer(in);
//        if (result.equals("goodInfo")) {
//            System.out.println("enter exit to exit");
//            int size = Integer.parseInt(ClientInterface.readServer(in));
//            for (int i = 0; i < size; i++) {
//                System.out.println(ClientInterface.readServer(in));
//            }
//            while (true) {
//                String s = scan.nextLine();
//                ClientInterface.writeServer(s, out);
//                if (s.equals("exit")) {
//                    break;
//                }
//            }
//        }
//    }
//    public static void viewUser(PrintWriter out, BufferedReader in) {
//        System.out.println("Please enter username");
//        Scanner scan = new Scanner(System.in);
//        String search = scan.nextLine();
//        ClientInterface.writeServer(search, out);
//        String found = ClientInterface.readServer(in);
//        System.out.println(found);
//        if (!found.equals("User not found")) {
//            System.out.println("1 to friend, 2 to block, 3 to remove friend, 4 to unblock, 5 to add new message (you can only message friends)");
//            String searchChoice = scan.nextLine();
//            ClientInterface.writeServer(searchChoice, out);
//            if (searchChoice.equals("5")) {
//                String exists = ClientInterface.readServer(in);
//                System.out.println(exists);
//                if (exists.equals("not exists")) {
//                    System.out.println("Conversation Created");
//                } else {
//                    System.out.println("You have already messaged this user");
//                }
//            }
//        }
//    }
//}
