import java.io.*;
import java.util.Arrays;
/**
 * The database for our message app, handles the modification of users as well as other tasks
 * Purdue University -- Fall 2024 -- CS18000 -- Team Project
 *
 * @author AJ Metrick, Edward Ju, Nolan Shultz, Parshawn Haynes, Pranav Sangani
 * @version October 29, 2024
 */
public class ChatDatabase implements ChatDatabaseInterface {
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
    
    public boolean createUser(String data) {
        synchronized(MAIN_LOCK) {
            String[] userData = data.split(",");
            if (userData.length != 2|| userData[0].isEmpty() || userData[1].isEmpty()) {
                return false;  
            }
            String newUsername = userData[0];
            String newPassword = userData[1];

            for (String username : usernames) {
                if (username.equals(newUsername)) {
                    return false;  
                }
            }
            User newUser = new User(newUsername, newPassword);
            userList = Arrays.copyOf(userList, userList.length + 1);
            userList[userList.length - 1] = newUser;
            appendUsername(newUsername);
            appendPassword(newPassword);
            return true;
        }
    }
    public boolean removeUser(User user) {
        synchronized(MAIN_LOCK) {
            int index = -1;
            for (int i = 0; i < userList.length; i++) {
                if (userList[i].equals(user)) {
                    index = i;
                    break;
                }
            }
            if (index == -1) {
                return false;  
            }
            User[] newUserList = new User[userList.length - 1];
            System.arraycopy(userList, 0, newUserList, 0, index);
            System.arraycopy(userList, index + 1, newUserList, index, userList.length - index - 1);
            userList = newUserList;
            removeUsername(user.getUsername());
            removePassword(user.getPassword());
            return true;
    
        }   
    }
    private void removeUsername(String username) {
        String[] newUsernames = new String[usernames.length - 1];
        int index = 0;
        for (String existingUsername : usernames) {
            if (!existingUsername.equals(username)) {
                newUsernames[index++] = existingUsername;
            }
        }
        usernames = newUsernames;
    }

    private void removePassword(String password) {
        String[] newPasswords = new String[passwords.length - 1];
        int index = 0;
        for (String existingPassword : passwords) {
            if (!existingPassword.equals(password)) {
                newPasswords[index++] = existingPassword;
            }
        }
        passwords = newPasswords;
    }
}
