public interface ChatDatabaseInterface {
    boolean readUsernames();
    boolean readPasswords();
    boolean writeUsernames();
    boolean createUser(String data);
    boolean removeUser(User user);
}