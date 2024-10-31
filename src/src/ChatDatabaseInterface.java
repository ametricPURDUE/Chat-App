public interface ChatDatabaseInterface {
    boolean readUsernames();
    boolean readPasswords();
    boolean writeUsernames();
    boolean writePasswords();
    boolean createUser(String data);
    boolean removeUser(User user);
}