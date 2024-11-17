import java.util.ArrayList;
import java.io.*;
/**
 * The class that handles all functions relating to specific users
 * Purdue University -- Fall 2024 -- CS18000 -- Team Project
 *
 * @author AJ Metrick, Edward Ju, Nolan Shultz, Parshawn Haynes, Pranav Sangani
 * @version October 29, 2024
 */
public class User implements userTemplate{
    private String name;
    private String username;
    private String userFriendsFilename;
    private String userBlockedFilename;
    private String userMessagesFilename;
    private ArrayList <User> friends = new ArrayList<>();
    private ArrayList <String> messages = new ArrayList<>();
    private ArrayList <User> blocked = new ArrayList<>();
    private ArrayList <User> friendRequests = new ArrayList<>();
    private int age;
    private String password;

    public User(String name, String username, int age) {
        this.name = name;
        this.username = username;
        this.age = age;
        this.userFriendsFilename = name + "_friends.txt";
        this.userMessagesFilename = username + "_messages.txt";
        this.userBlockedFilename = name + "_blocked.txt";
    }

    public User(String data) throws IncorrectInput {
        String[] parts = data.split(",");
        if (parts.length != 3) {
            throw new IncorrectInput("Wrong Data");
        }
        try {
            this.name = parts[0];
            this.username = parts[1];
            this.age = Integer.parseInt(parts[2]);
        } catch (NumberFormatException e) {
            throw new IncorrectInput("Wrong Data");
        }
        this.userFriendsFilename = name + "_friends.txt";
        this.userBlockedFilename = name + "_blocked.txt";
        this.userMessagesFilename = username + "_messages.txt";

    }
    
    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public void setPassword(String newPassword) {
        this.password = newPassword;
        ChatDatabase.updatePassword(this.username, newPassword);
    }

    public ArrayList<User> getBlocked() {
        readBlocked();
        return blocked;
    }

    public void setBlocked(ArrayList<User> blocked) {
        this.blocked = blocked;
    }

    /**
     * write the user's friends list into a text file for saving
     * @return - return true if successful and false if not
     */
    public boolean writeFriends() {
        try (BufferedWriter bfw = new BufferedWriter(new FileWriter(userFriendsFilename))) {
            for (User friend: friends) {
                bfw.write(friend.toString());
                bfw.newLine();
            }
            return true;
        } catch (IOException e) {
            return false;
        }
    }

    public boolean writeMessages() {
        try (BufferedWriter bfw = new BufferedWriter(new FileWriter(userMessagesFilename))) {
            for (String message: messages) {
                bfw.write(message);
                bfw.newLine();
            }
            return true;
        } catch (IOException e) {
            return false;
        }
    }

    public boolean newMessage(User user) {
        String filename;
        readMessages();
        if (this.getUsername().compareTo(user.getUsername()) < 0) {
            filename = "chat" + this.getUsername() + user.getUsername() + ".txt";
        } else {
            filename = "chat" + user.getUsername() + this.getUsername() + ".txt";
        }
        if (!messages.contains(filename)) {
            messages.add(filename);
            writeMessages();
            return true;
        } else {
            return false;
        }
    }

    public boolean readMessages() {
        try (BufferedReader bfr = new BufferedReader(new FileReader(userMessagesFilename))) {
            String line;
            messages = new ArrayList<String>();
            while ((line = bfr.readLine()) != null) {
                messages.add(line);
            }
            return true;
        } catch (IOException e) {
            return false;
        }
    }
    /**
     * write the user's blocked list into a text file for saving
     * @return - return true if successful and false if not
     */
    public boolean writeBlocked() {
        try (BufferedWriter bfw = new BufferedWriter(new FileWriter(userBlockedFilename))) {
            for (User block: blocked) {
                bfw.write(block.toString());
                bfw.newLine();
            }
            return true;
        } catch (IOException e) {
            return false;
        }
    }

    /**
     * Reads the [user]_friends.txt file and adds each line to a friends array as a new user
     * @return - returns true if successful and false if not
     */
    public boolean readFriends() {
        try (BufferedReader bfr = new BufferedReader(new FileReader(userFriendsFilename))) {
            String line;
            setFriends(new ArrayList<User>());
            while ((line = bfr.readLine()) != null) {
                friends.add(new User(line));
            }
            return true;
        } catch (IOException | IncorrectInput e) {
            return false;
        }
    }

    /**
     * Reads the [user]_blocked.txt file and adds each line to a blocked array as a new User
     * @return - returns true if successful and false if not
     */
    public boolean readBlocked() {
        try (BufferedReader bfr = new BufferedReader(new FileReader(userBlockedFilename))) {
            String line;
            setBlocked(new ArrayList<User>());
            while ((line = bfr.readLine()) != null) {
                blocked.add(new User(line));
            }
            return true;
        } catch (IOException | IncorrectInput e) {
            return false;
        }
    }

    /**
     * add the user to the blocked list and writes it to the user's specific blocked file
     * @param user - the user to be blocked
     */
    public void blockUser(User user) {
        readBlocked();
        for (User block: blocked) {
            if (block.equals(user)) {
                return;
            }
        }
        blocked.add(user);
        removeFriend(user);
        writeBlocked();
    }

    /**
     * removes the user from the blocked list and writes it to the user's specific blocked file
     * @param unblocked - the user to be unblocked
     */
    public void unblockUser(User unblocked) {
        readBlocked();
        for(User user: blocked) {
            if (user.equals(unblocked)) {
                blocked.remove(user);
                writeBlocked();
                break;
            }
        }
    }

    public ArrayList<User> getFriends() {
        readFriends();
        return friends;
    }

    public void setFriends(ArrayList<User> friends) {
        this.friends = friends;
    }

    /**
     * add the user to the friends list and writes it to the user's specific friend file
     * @param user - the user to be added
     */
    public void addFriend(User user) {
        readFriends();
        for (User friend: friends) {
            if (friend.equals(user)) {
                return;
            }
        }
        friends.add(user);
        writeFriends();
    }

    /**
     * removes the user from the friends list and writes it to the user's specific friend file
     * @param unfriend - the user to be unfriended
     */
    public void removeFriend(User unfriend) {
        readFriends();
        for (int i = 0; i < friends.size(); i++) {
            if (unfriend.equals(friends.get(i))) {
                friends.remove(i);
                break;
            }
        }
        writeFriends();
    }

    public void friendRequest(User user) {
        if (!friendRequests.contains(user) && !friends.contains(user)) {
            friendRequests.add(user);
        }
    }

    public void acceptFriendRequest(User user) {
        if (friendRequests.contains(user)) {
            friendRequests.remove(user); // Remove from friend requests
            friends.add(user);           // Add to friends list
        }
    }

    public void rejectFriendRequest(User user) {
        friendRequests.remove(user); // Simply remove from friend requests
    }

    public boolean equals(User user) {
        if (!name.equals(user.name)) {
            return false;
        }
        if (!username.equals(user.username)) {
            return false;
        }
        if (age != user.age) {
            return false;
        }
        return true;
    }

    public int getAge() {
        return age;
    }

    public void setAge(int age) {
        this.age = age;
    }

    public ArrayList<String> getMessages() {
        readMessages();
        return messages;
    }
    @Override
    public String toString() {
        return String.format("%s,%s,%d", name, username, age);
    }
}
