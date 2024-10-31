public interface ChatDatabaseInterface {
    void appendUsername(String newUsername);
    boolean readUsernames();
    void appendPassword(String newPassword);
    boolean readPasswords();
    boolean createUser(String data);
    boolean removeUser(User user);
}
