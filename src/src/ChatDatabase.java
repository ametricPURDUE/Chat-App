import java.io.*;
import java.util.Arrays;
/**
 * The database for our message app, handles the modification of users as well as other tasks
 * Purdue University -- Fall 2024 -- CS18000 -- Team Project
 *
 * @author AJ Metrick, Edward Ju, Nolan Shultz, Parshawn Haynes, Pranav Sangani
 * @version October 29, 2024
 */
public class ChatDatabase {
    private static User[] userList;
    private static String[] usernames;
    private static String[] passwords;
    public static final Object MAIN_LOCK = new Object();


    public ChatDatabase() {
        synchronized(MAIN_LOCK) {
            userList = new User[0];
            usernames = new String[0];
            passwords = new String[0];
        }
    }

    //set of read functions

    /**
     * adds a new username to the usernames list
     * @param newUsername - the new username to be added
     */
    public void appendUsername(String newUsername) {
        usernames = Arrays.copyOf(usernames, usernames.length + 1);
        usernames[usernames.length - 1] = newUsername;
    }

    /**
     * reads the file "usernames.txt" and adds each username as a separate string in the usernames list
     * @return - returns true if successful and false if not
     */
    public boolean readUsernames() {
        try(BufferedReader bfr = new BufferedReader(new FileReader("usernames.txt"))) {
            String line;
            while ((line = bfr.readLine()) != null) {
                appendUsername(line);
            }
            return true;
        } catch (IOException e) {
            return false;
        }
    }

    /**
     * adds a new password to the passwords list
     * @param newPassword - the new password to be added
     */
    public void appendPassword(String newPassword) {
        passwords = Arrays.copyOf(passwords, passwords.length + 1);
        passwords[passwords.length - 1] = newPassword;
    }

    /**
     * reads the file "passwords.txt" and adds each password as a separate string in the passwords list
     * @return - returns true if successful and false if not
     */
    public boolean readPasswords() {
        try (BufferedReader bfr = new BufferedReader(new FileReader("passwords.txt"))) {
            String line;
            while ((line = bfr.readLine()) != null) {
                appendPassword(line);
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
    public boolean writePasswords() {
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

    public void printUsernames() {
        for (String user: usernames) {
            System.out.println(user);
        }
    }

    public void printPasswords() {
        for (String passwd: passwords) {
            System.out.println(passwd);
        }
    }
}