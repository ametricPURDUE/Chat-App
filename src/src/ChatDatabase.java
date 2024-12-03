import java.io.*;
import java.util.Arrays;
import java.util.ArrayList;
/**
 * The database for our message app, handles the modification of users as well as other tasks
 * Purdue University -- Fall 2024 -- CS18000 -- Team Project
 *
 * @author AJ Metrick, Edward Ju, Nolan Shultz, Parshawn Haynes, Pranav Sangani
 * @version October 29, 2024
 */
public class ChatDatabase implements ChatDatabaseInterface {
    private static ArrayList<User> userList;
    private static ArrayList<String> usernames;
    private static ArrayList<String> passwords;
    public static final Object MAIN_LOCK = new Object();

    public ChatDatabase() {
        synchronized(MAIN_LOCK) {
            userList = new ArrayList<User>();
            usernames = new ArrayList<String>();
            passwords = new ArrayList<String>();
        }
    }

    public ChatDatabase(ArrayList<User> userList, ArrayList<String> usernames, ArrayList<String> passwords) {
        synchronized (MAIN_LOCK) {
            ChatDatabase.userList = userList;
            ChatDatabase.usernames = usernames;
            ChatDatabase.passwords = passwords;
            writePasswords();
            writeUsers();
            writeUsernames();
        }
    }

    public User getUsers(String username) {
        readUsers();
        synchronized(MAIN_LOCK) {
            for(User user : userList) {
                if(user.getUsername().equals(username)) {
                    return user;
                }
            }
        }
        return null;
    }
    /**
     * reads the file "usernames.txt" and adds each username as a separate string in the usernames list
     * @return - returns true if successful and false if not
     */
    public boolean readUsernames() {
        synchronized(MAIN_LOCK) {
            usernames.clear();
            try(BufferedReader bfr = new BufferedReader(new FileReader("usernames.txt"))) {
                String line;
                while ((line = bfr.readLine()) != null) {
                    usernames.add(line);
                }
                return true;
            } catch (IOException e) {
                return false;
            }
        }
    }

    public boolean readUsers() {
        synchronized(MAIN_LOCK) {
            userList.clear();
            try(BufferedReader bfr = new BufferedReader(new FileReader("users.txt"))) {
                String line;
                while ((line = bfr.readLine()) != null) {
                    userList.add(new User(line));
                }
                return true;
            } catch (IOException e) {
                return false;
            } catch (IncorrectInput e) {
                return false;
            }
        }
    }
    /**
     * reads the file "passwords.txt" and adds each password as a separate string in the passwords list
     * @return - returns true if successful and false if not
     */
    public boolean readPasswords() {
        synchronized(MAIN_LOCK) {
            passwords.clear();
            try (BufferedReader bfr = new BufferedReader(new FileReader("passwords.txt"))) {
                String line;
                while ((line = bfr.readLine()) != null) {
                    passwords.add(line);
                }
                return true;
            } catch (IOException e) {
                return false;
            }
        }
    }
    //set of write functions;
    /**
     * takes each item in usernames and appends it to "usernames.txt" as its own line
     * @return - returns true if successful and false if not
     */
    public boolean writeUsernames() {
        synchronized(MAIN_LOCK) {
            try (BufferedWriter bfw = new BufferedWriter(new FileWriter("usernames.txt"))) {
                for(String username: usernames) {
                    bfw.write(username);
                    bfw.newLine();
                }
                return true;
            } catch (IOException e) {
                return false;
            }
        }
    }
    public boolean writeUsers() {
        synchronized(MAIN_LOCK) {
            try (BufferedWriter bfw = new BufferedWriter(new FileWriter("users.txt"))) {
                for(User user: userList) {
                    bfw.write(user.toString());
                    bfw.newLine();
                }
                return true;
            } catch (IOException e) {
                return false;
            }
        }
    }


    /**
     * takes each item in passwords and appends it to "passwords.txt" as its own line
     * @return - returns true if successful and false if not
     */

    public static boolean writePasswords() {
        synchronized(MAIN_LOCK) {
            try (BufferedWriter bfw = new BufferedWriter(new FileWriter("passwords.txt"))) {
                for(String password: passwords) {
                    bfw.write(password);
                    bfw.newLine();
                }
                return true;
            } catch (IOException e) {
                return false;
            }
        }
    }

    // Updates the password in the passwords list for a given username.
    public boolean updatePassword(String username, String newPassword) {
        synchronized(MAIN_LOCK) {
            readPasswords();
            int index = usernames.indexOf(username);
            if (index != -1) {
                getUsers(username).setPassword(newPassword);
                passwords.set(index, newPassword);
                writePasswords();
                return true;
            }
            return false;
        }
    }

    public boolean createUser(String data) throws IncorrectInput {
        synchronized(MAIN_LOCK) {
            String[] userData = data.split(",");
            if (userData.length != 4) {
                throw new IncorrectInput("Input string data formatted incorrectly");
            }
            int newAge;
            try {
                newAge = Integer.parseInt(userData[2]);
            } catch (NumberFormatException e) {
                throw new IncorrectInput("Please input a number for age");
            }
            if (newAge < 0) {
                throw new IncorrectInput("Age cannot be below 0");
            }
            readUsernames();
            readUsers();
            readPasswords();
            if (usernames.contains(userData[1])) {
                throw new IncorrectInput("Username already exists");
            }
            userList.add(new User(userData[0], userData[1], newAge));
            usernames.add(userData[1]);
            passwords.add(userData[3]);
            writeUsers();
            writeUsernames();
            writePasswords();
            return true;
        }
    }

    public boolean removeUser(User user) {
        synchronized(MAIN_LOCK) {
            int index = -1;
            for (int i = 0; i < userList.size(); i++) {
                if (userList.get(i).equals(user)) {
                    index = i;
                    break;
                }
            }
            if (index == -1) {
                return false;  
            }
            userList.remove(index);
            usernames.remove(index);
            passwords.remove(index);
            writeUsers();
            writeUsernames();
            writePasswords();
            return true;
    
        }   
    }
    /**
     * modifies given user based on input string,
     * input string data must be formatted "name,age,username,password"
     * @return - returns true user is found and modified and false if not
     */
    public boolean modifyUser(User user, String data) {
        synchronized (MAIN_LOCK) {
            String[] parts = data.split(",");
            if (parts.length != 3) {
                return false;
            }
            String name = parts[0];
            int age = -1;
            try {
                age = Integer.parseInt(parts[1]);
            } catch (NumberFormatException e) {
                return false;
            }
            String password = parts[2];
            User newUser = new User(name, user.getUsername(), age);
            for (int i = 0; i < userList.size(); i++) {
                if (userList.get(i).equals(user)) {
                    readUsers();
                    readPasswords();
                    userList.set(i, newUser);
                    passwords.set(i, password);
                    writeUsers();
                    writePasswords();
                    return true;
                }
            }
            return false;
        }
    }
    public boolean writeMessageToFile(String senderUsername, String receiverUsername, String message) {
        synchronized(MAIN_LOCK) {
            String filename;
            if (senderUsername.compareTo(receiverUsername) < 0) {
                filename = "chat" + senderUsername + receiverUsername + ".txt";
            } else {
                filename = "chat" + receiverUsername + senderUsername + ".txt";
            }
            try (PrintWriter writer = new PrintWriter(new FileWriter(filename, true))) {
                writer.println(senderUsername + ": " + message);
                return true;
            } catch (IOException e) {
                e.printStackTrace();
                return false;
            }
        }
    }


        public ArrayList<String> readMessagesFromFile(String senderUsername, String receiverUsername) {
            synchronized(MAIN_LOCK) {
                String filename;
                if (senderUsername.compareTo(receiverUsername) < 0) {
                    filename = "chat" + senderUsername + receiverUsername + ".txt";
                } else {
                    filename = "chat" + receiverUsername + senderUsername + ".txt";
                }
    
                ArrayList<String> messages = new ArrayList<>();
    
                try (BufferedReader reader = new BufferedReader(new FileReader(filename))) {
                    String line;
                    while ((line = reader.readLine()) != null) {
                        messages.add(line);
                    }
                } catch (FileNotFoundException e) {
                    System.out.println("No previous conversation found between " + senderUsername + " and " + receiverUsername);
                } catch (IOException e) {
                    e.printStackTrace();
                }
                return messages;
            }
        }
        public boolean login(String username, String password) {
            synchronized (MAIN_LOCK) {
                boolean found = false;
                int index;
                readPasswords();
                readUsernames();
                index = usernames.indexOf(username);
                if (index != -1) {
                    found = true;
                } else {
                    return false;
                }
                if (found) {
                    if (password.equals(passwords.get(index))) {
                        return true;
                    }
                }
                return false;
            }
        }
        public ArrayList<String> getUsernames() {
            readUsernames();
            return usernames;
        }
    }


