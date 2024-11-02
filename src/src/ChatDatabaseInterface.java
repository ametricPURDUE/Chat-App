public interface ChatDatabaseInterface {
    boolean readUsernames();
    boolean readPasswords();
    boolean writeUsernames();
    boolean createUser(String data);
    boolean removeUser(User user);
    boolean modifyUser(User user, String data) throws IncorrectInput;
}