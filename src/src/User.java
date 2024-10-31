import java.util.ArrayList;
/**
 * The class that handles all functions relating to specific users
 * Purdue University -- Fall 2024 -- CS18000 -- Team Project
 *
 * @author AJ Metrick, Edward Ju, Nolan Shultz, Parshawn Haynes, Pranav Sangani
 * @version October 29, 2024
 */
public class User {
    private String name;
    private String username;
    private ArrayList <User> friends = new ArrayList<>();
    private User[] blocked = new User[0];
    private int age;

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

    public User[] getBlocked() {
        return blocked;
    }

    public void setBlocked(User[] blocked) {
        this.blocked = blocked;
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
