import java.util.ArrayList;
/**
 * The class that handles all functions relating to specific users
 * Purdue University -- Fall 2024 -- CS18000 -- Team Project
 *
 * @author AJ Metrick, Edward Ju, Nolan Shultz, Parshawn Haynes, Pranav Sangani
 * @version October 29, 2024
 */
public interface userTemplate {
    public String getName();
    public void setName(String name);
    public String getUsername();
    public void setUsername(String username);
    public ArrayList<User> getBlocked();
    public void blockUser(User user);
    public void unblockUser(User user);
    public ArrayList<User> getFriends();
    public void addFriend(User user);
    public void removeFriend(User user);
    public int getAge();
    public void setAge(int age);
}
