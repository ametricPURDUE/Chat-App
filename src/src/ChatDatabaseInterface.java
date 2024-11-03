public interface ChatDatabaseInterface {
    boolean readUsernames();
    boolean readPasswords();
    boolean writeUsernames();
    boolean createUser(String data) throws IncorrectInput;
    boolean removeUser(User user);
    boolean modifyUser(User user, String data) throws IncorrectInput;
}
