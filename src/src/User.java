import java.util.ArrayList;
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
    private ArrayList <User> friends = new ArrayList<>();
    private ArrayList <User> blocked = new ArrayList<>();
    private ArrayList <User> friendRequest = new ArrayList<>();
    private int age;
    private String password;

    public User(String name, String username, int age) {
        this.name = name;
        this.username = username;
        this.age = age;
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
        return blocked;
    }

    public void setBlocked(ArrayList<User> blocked) {
        this.blocked = blocked;
    }

    public void blockUser(User user) {
        blocked.add(user);
    }

    public void unblockUser(User user) {
        for(int i = 0; i < blocked.size(); i++) {
            if(blocked.get(i).equals(user)) {
                blocked.remove(i);
                i--;
            }
        }
    }

    public ArrayList<User> getFriends() {
        return friends;
    }

    public void setFriends(ArrayList<User> friends) {
        this.friends = friends;
    }

    public void addFriend(User user) {
        friends.add(user);
    }

    public void removeFriend(User user) {
        for (int i = 0; i < friends.size(); i++) {
            if (friends.get(i).equals(user)) {
                friends.remove(i);
                i--;
            }
        }
    }

    public void friendRequest(User user) {
        if (!friendRequest.contains(user) && !friends.contains(user)) {
            friendRequest.add(user);
        }
    }

    public void acceptFriendRequest(User user) {
        if (friendRequest.contains(user)) {
            friendRequest.remove(user); // Remove from friend requests
            friends.add(user);           // Add to friends list
        }
    }

    public void rejectFriendRequest(User user) {
        friendRequest.remove(user); // Simply remove from friend requests
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
}
