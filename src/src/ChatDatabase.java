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

    /**
     * reads the file "usernames.txt" and adds each username as a separate string in the usernames list
     * @return - returns true if successful and false if not
     */
    public boolean readUsernames() {
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

    /**
     * reads the file "passwords.txt" and adds each password as a separate string in the passwords list
     * @return - returns true if successful and false if not
     */
    public boolean readPasswords() {
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
    //set of write functions;
    /**
     * takes each item in usernames and appends it to "usernames.txt" as its own line
     * @return - returns true if successful and false if not
     */
    public boolean writeUsernames() {
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



    /**
     * takes each item in passwords and appends it to "passwords.txt" as its own line
     * @return - returns true if successful and false if not
     */

    public static boolean writePasswords() {
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

    // Updates the password in the passwords list for a given username.
    public static void updatePassword(String username, String newPassword) {
        int index = usernames.indexOf(username);
        if (index != -1) {
            passwords.set(index, newPassword);
            writePasswords();
        }
    }

    public boolean createUser(String data) throws IncorrectInput {
        String[] userData = data.split(",");
        if (userData.length < 3) {
            throw new IncorrectInput("Input string data formatted incorrectly");
        }
        int newAge = -1;
        try {
            newAge = Integer.parseInt(userData[2]);
        } catch (NumberFormatException e) {
            throw new IncorrectInput("Please input a number for age");
        }
        if (newAge == -1) {
            throw new IncorrectInput("Age cannot be below 0");
        }
        userList.add(new User(userData[0], userData[1], newAge));
        usernames.add(userData[1]);
        return true;
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
            return true;
    
        }   
    }
    /**
     * modifies given user based on input string,
     * input string data must be formatted "name,age,username,password"
     * @return - returns true user is found and modified and false if not
     */
    public boolean modifyUser(User user, String data) throws IncorrectInput{
        boolean changed = false;
        try {
            String name = data.substring(0, data.indexOf(","));
            data = data.substring(data.indexOf(",") + 1);
            int age = Integer.parseInt(data.substring(0, data.indexOf(",")));
            data = data.substring(data.indexOf(",") + 1);
            String username = data.substring(0, data.indexOf(","));
            data = data.substring(data.indexOf(",") + 1);
            String password = data.substring(0, data.indexOf(","));
            for (int i = 0; i < userList.size(); i++) {
                if (userList.get(i).equals(user)) {
                    changed = true;
                    user.setName(name);
                    user.setAge(age);
                    user.setUsername(username);
                    usernames.set(i, username);
                    passwords.set(i, password);
                }
            }
        } catch (IndexOutOfBoundsException | NumberFormatException e) {
            throw new IncorrectInput("Input string data formatted incorrectly");
        }
        return changed;
    }
}
