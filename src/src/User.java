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
    private User[] blocked = new User[0];
    private User[] friends = new User[0];
    private int age;

    public User(String name, String username, int age) {
        this.name = name;
        this.username = username;
        this.age = age;
        //this is a test?
    }
}