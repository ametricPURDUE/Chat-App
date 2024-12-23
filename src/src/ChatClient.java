import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.*;
import java.net.*;
import javax.swing.*;
import java.awt.*;

public class ChatClient implements ClientInterface {
    private static Color backgroundColor = Color.LIGHT_GRAY;
    private ChatDatabase database;
    private String username;
    private String name;
    private int friendCount;
    private int blockedCount;

    private static void updatePanels(JPanel[] jPanels) {
        for(JPanel panel: jPanels) {
            panel.setBackground(backgroundColor);
            panel.revalidate();
            panel.repaint();
        }
    }

    public static void main(String[] args) {
        int serverPort;
        int serverIndex;
        boolean hasServer = true;
        ChatClient client = new ChatClient();
        client.database = new ChatDatabase(); // Initialize the database
        JFrame frame = new JFrame("Chat App");
        Container home = frame.getContentPane();
        frame.setSize(600, 400);
        frame.setLocationRelativeTo(null);
        frame.setBackground(backgroundColor);
        frame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        // create the side panel
        JPanel sidePanel = new JPanel();
        sidePanel.setLayout(new BoxLayout(sidePanel, BoxLayout.Y_AXIS));
        sidePanel.setBackground(Color.LIGHT_GRAY);

        /* For those creating the account screen and messages screen,
        please add the button action under your respective button */
        JButton accountButton = new JButton("Account");
        JButton messagesButton = new JButton("Messages");
        JButton settingsButton = new JButton("Settings");


        //adds the buttons on top of each other
        sidePanel.add(accountButton);
        sidePanel.add(Box.createVerticalStrut(140));
        sidePanel.add(messagesButton);
        sidePanel.add(Box.createVerticalStrut(140));
        sidePanel.add(settingsButton);

        frame.add(sidePanel, BorderLayout.WEST);

        //Initialize all JPanels
        JPanel loginScreen = new JPanel();
        JPanel createScreen = new JPanel();
        JPanel accountScreen = new JPanel();
        JPanel accountViewScreen = new JPanel();
        SpringLayout loginLayout = new SpringLayout();
        SpringLayout createScreenLayout = new SpringLayout();
        SpringLayout accountScreenLayout = new SpringLayout();
        SpringLayout accountViewScreenLayout = new SpringLayout();
        loginScreen.setLayout(loginLayout);
        createScreen.setLayout(createScreenLayout);
        accountScreen.setLayout(accountScreenLayout);
        accountViewScreen.setLayout(accountViewScreenLayout);
        //Create all JComponents for login screen
        JTextField usernameInput = new JTextField(15);
        JTextField passwordInput = new JTextField(15);
        JLabel usernameLabel = new JLabel("Username:");
        JLabel passwordLabel = new JLabel("Password:");
        JButton loginButton = new JButton("Login");
        JButton newUserButton = new JButton("New User");
        JLabel resultLabel = new JLabel("");

        loginScreen.add(usernameLabel);
        loginScreen.add(usernameInput);
        loginScreen.add(passwordLabel);
        loginScreen.add(passwordInput);
        loginScreen.add(loginButton);
        loginScreen.add(newUserButton);
        loginScreen.add(resultLabel);

        //Layout for username label text label
        loginLayout.putConstraint(SpringLayout.WEST, usernameLabel, 150, SpringLayout.WEST, loginScreen);
        loginLayout.putConstraint(SpringLayout.NORTH, usernameLabel, 100, SpringLayout.NORTH, loginScreen);
        //loginLayout for username input text field
        loginLayout.putConstraint(SpringLayout.WEST, usernameInput, 10, SpringLayout.EAST, usernameLabel);
        loginLayout.putConstraint(SpringLayout.NORTH, usernameInput, 100, SpringLayout.NORTH, loginScreen);
        //loginLayout for password label text label
        loginLayout.putConstraint(SpringLayout.WEST, passwordLabel, 150, SpringLayout.WEST, loginScreen);
        loginLayout.putConstraint(SpringLayout.NORTH, passwordLabel, 30, SpringLayout.SOUTH, usernameInput);
        //loginLayout for password input text field
        loginLayout.putConstraint(SpringLayout.WEST, passwordInput, 10, SpringLayout.EAST, passwordLabel);
        loginLayout.putConstraint(SpringLayout.NORTH, passwordInput, 30, SpringLayout.SOUTH, usernameInput);
        //loginLayout for login button
        loginLayout.putConstraint(SpringLayout.WEST, loginButton, 225, SpringLayout.WEST, loginScreen);
        loginLayout.putConstraint(SpringLayout.EAST, loginButton, 375, SpringLayout.WEST, loginScreen);
        loginLayout.putConstraint(SpringLayout.NORTH, loginButton, 30, SpringLayout.SOUTH, passwordInput);
        //loginLayout for new user button
        loginLayout.putConstraint(SpringLayout.WEST, newUserButton, 225, SpringLayout.WEST, loginScreen);
        loginLayout.putConstraint(SpringLayout.EAST, newUserButton, 375, SpringLayout.WEST, loginScreen);
        loginLayout.putConstraint(SpringLayout.NORTH, newUserButton, 20, SpringLayout.SOUTH, loginButton);
        //loginLayout for result label
        loginLayout.putConstraint(SpringLayout.WEST, resultLabel, 200, SpringLayout.WEST, loginScreen);
        loginLayout.putConstraint(SpringLayout.EAST, resultLabel, 600, SpringLayout.WEST, loginScreen);
        loginLayout.putConstraint(SpringLayout.NORTH, resultLabel, 20, SpringLayout.SOUTH, newUserButton);
        frame.add(loginScreen);
        frame.setVisible(true);

        //creates all Jcomponents for create new user screen
        JTextField createUsername = new JTextField(15);
        JTextField createPassword = new JTextField(15);
        JTextField nameInput = new JTextField(15);
        JTextField ageInput = new JTextField(15);
        JLabel createUsernameLabel = new JLabel("Username:");
        JLabel createPasswordLabel = new JLabel("Password:");
        JLabel nameLabel = new JLabel("Name:");
        JLabel ageLabel = new JLabel("Age:");
        JButton createButton = new JButton("Create");

        createScreen.add(nameLabel);
        createScreen.add(nameInput);
        createScreen.add(ageLabel);
        createScreen.add(ageInput);
        createScreen.add(createButton);
        createScreen.add(createPasswordLabel);
        createScreen.add(createUsernameLabel);
        createScreen.add(createUsername);
        createScreen.add(createPassword);

        //createLayout for createUsernameLabel
        createScreenLayout.putConstraint(SpringLayout.WEST, createUsernameLabel, 150, SpringLayout.WEST, createScreen);
        createScreenLayout.putConstraint(SpringLayout.NORTH, createUsernameLabel, 60, SpringLayout.NORTH, createScreen);
        //loginLayout for createUsername input text field
        createScreenLayout.putConstraint(SpringLayout.WEST, createUsername, 10, SpringLayout.EAST, createUsernameLabel);
        createScreenLayout.putConstraint(SpringLayout.NORTH, createUsername, 60, SpringLayout.NORTH, createScreen);
        //createLayout for createPasswordLabel
        createScreenLayout.putConstraint(SpringLayout.WEST, createPasswordLabel, 150, SpringLayout.WEST, createScreen);
        createScreenLayout.putConstraint(SpringLayout.NORTH, createPasswordLabel, 30, SpringLayout.SOUTH, createUsername);
        //loginLayout for createPassword input text field
        createScreenLayout.putConstraint(SpringLayout.WEST, createPassword, 10, SpringLayout.EAST, createPasswordLabel);
        createScreenLayout.putConstraint(SpringLayout.NORTH, createPassword, 30, SpringLayout.SOUTH, createUsername);
        //createLayout for nameLabel
        createScreenLayout.putConstraint(SpringLayout.WEST, nameLabel, 160, SpringLayout.WEST, createScreen);
        createScreenLayout.putConstraint(SpringLayout.NORTH, nameLabel, 30, SpringLayout.SOUTH, createPassword);
        //createLayout for name input text field
        createScreenLayout.putConstraint(SpringLayout.WEST, nameInput, 10, SpringLayout.EAST, createPasswordLabel);
        createScreenLayout.putConstraint(SpringLayout.NORTH, nameInput, 30, SpringLayout.SOUTH, createPassword);
        //createLayout for age label
        createScreenLayout.putConstraint(SpringLayout.WEST, ageLabel, 160, SpringLayout.WEST, createScreen);
        createScreenLayout.putConstraint(SpringLayout.NORTH, ageLabel, 30, SpringLayout.SOUTH, nameInput);
        //createLayout for age input
        createScreenLayout.putConstraint(SpringLayout.WEST, ageInput, 10, SpringLayout.EAST, createPasswordLabel);
        createScreenLayout.putConstraint(SpringLayout.NORTH, ageInput, 30, SpringLayout.SOUTH, nameInput);
        //createLayout for createButton
        createScreenLayout.putConstraint(SpringLayout.WEST, createButton, 275, SpringLayout.WEST, createScreen);
        //createScreenLayout.putConstraint(SpringLayout.EAST, createButton, 375, SpringLayout.WEST, createScreen);
        createScreenLayout.putConstraint(SpringLayout.NORTH, createButton, 30, SpringLayout.SOUTH, ageInput);

        //add the content to the settings page
        JLabel changeNameLabel = new JLabel("Change Name:");
        JButton changeNameButton = new JButton("Change Name");
        JLabel changePasswordLabel = new JLabel("Change Password:");
        JButton changePasswordButton = new JButton("Change Password");
        JLabel changeAgeLabel = new JLabel("Change Age:");
        JButton changeAgeButton = new JButton("Change Age");
        
        //sets the background colors
        JLabel setModeLabel = new JLabel("Set Background Mode:");
        JButton lightModeButton = new JButton("Light");
        JButton darkModeButton = new JButton("Dark");
        JButton defaultModeButton = new JButton("Default");

        //create all panels for settings
        JPanel settingsPanel = new JPanel();
        JPanel changeNamePanel = new JPanel();
        JPanel changeAgePanel = new JPanel();
        JPanel changePasswordPanel = new JPanel();

        //create all layouts for settings
        SpringLayout settingsLayout = new SpringLayout();
        SpringLayout changeNameLayout = new SpringLayout();
        SpringLayout changeAgeLayout = new SpringLayout();
        SpringLayout changePasswordLayout = new SpringLayout();

        //set layouts to respective panels for settings
        settingsPanel.setLayout(settingsLayout);
        changeNamePanel.setLayout(changeNameLayout);
        changeAgePanel.setLayout(changeAgeLayout);
        changePasswordPanel.setLayout(changePasswordLayout);

        //create change name JComponents
        JLabel confirmNameChangeLabel = new JLabel("Change Name");
        JLabel nameUsernameLabel = new JLabel("Enter Your Username");
        JTextField nameUsernameText = new JTextField(15);
        JLabel newNameLabel = new JLabel("Enter Your New Name");
        JTextField newNameText = new JTextField(15);
        JButton newNameButton = new JButton("Confirm");
        JLabel confirmationNameLabel = new JLabel("");

        //create change age JComponents
        JLabel confirmAgeChangeLabel = new JLabel("Change Age");
        JLabel ageUsernameLabel = new JLabel("Enter your username");
        JTextField ageUsernameText = new JTextField(15);
        JLabel newAgeLabel = new JLabel("Enter New Age");
        JTextField newAgeText = new JTextField(15);
        JButton newAgeButton = new JButton("Confirm");
        JLabel confirmationAgeLabel = new JLabel("");

        //create change password JComponents
        JLabel confirmPasswordChangeLabel = new JLabel("Change Password");
        JLabel passUsernameLabel = new JLabel("Enter current username");
        JTextField passUsernameText = new JTextField(15);
        JLabel newPasswordLabel = new JLabel("Enter New Password:");
        JTextField newPasswordText = new JTextField(15);
        JButton newPasswordButton = new JButton("Confirm");
        JLabel confirmationPasswordLabel = new JLabel("");

        //general back button for each screen
        JButton nameBackButton = new JButton("Back");
        JButton ageBackButton = new JButton("Back");
        JButton passwordBackButton = new JButton("Back");

        //set the general settings items to settings panel
        settingsPanel.add(changeNameLabel);
        settingsPanel.add(changeNameButton);
        settingsPanel.add(changeAgeLabel);
        settingsPanel.add(changeAgeButton);
        settingsPanel.add(changePasswordLabel);
        settingsPanel.add(changePasswordButton);
        settingsPanel.add(setModeLabel);
        settingsPanel.add(lightModeButton);
        settingsPanel.add(darkModeButton);
        settingsPanel.add(defaultModeButton);
        settingsPanel.setBackground(backgroundColor);

        //set the change name items to changeName panel
        changeNamePanel.add(confirmNameChangeLabel);
        changeNamePanel.add(nameUsernameLabel);
        changeNamePanel.add(nameUsernameText);
        changeNamePanel.add(newNameLabel);
        changeNamePanel.add(newNameText);
        changeNamePanel.add(newNameButton);
        changeNamePanel.add(confirmationNameLabel);
        changeNamePanel.setBackground(backgroundColor);

        //set the change age items to changeAgePanel
        changeAgePanel.add(confirmAgeChangeLabel);
        changeAgePanel.add(ageUsernameLabel);
        changeAgePanel.add(ageUsernameText);
        changeAgePanel.add(newAgeLabel);
        changeAgePanel.add(newAgeText);
        changeAgePanel.add(newAgeButton);
        changeAgePanel.add(ageBackButton);
        changeAgePanel.add(confirmationAgeLabel);
        changeAgePanel.setBackground(backgroundColor);

        //set the change password items to changePasswordPanel
        changePasswordPanel.add(confirmPasswordChangeLabel);
        changePasswordPanel.add(passUsernameLabel);
        changePasswordPanel.add(passUsernameText);
        changePasswordPanel.add(newPasswordLabel);
        changePasswordPanel.add(newPasswordText);
        changePasswordPanel.add(newPasswordButton);
        changePasswordPanel.add(confirmationPasswordLabel);
        changePasswordPanel.add(passwordBackButton);
        changePasswordPanel.setBackground(backgroundColor);

        //set the constraints for the name change label
        settingsLayout.putConstraint(SpringLayout.WEST, changeNameLabel, 100, SpringLayout.WEST, settingsPanel);
        settingsLayout.putConstraint(SpringLayout.NORTH, changeNameLabel, 70, SpringLayout.NORTH, settingsPanel);

        //set the constraints for the name change button
        settingsLayout.putConstraint(SpringLayout.WEST, changeNameButton, 120, SpringLayout.WEST, changeNameLabel);
        settingsLayout.putConstraint(SpringLayout.NORTH, changeNameButton, 65, SpringLayout.NORTH, settingsPanel);

        //set the constraints for the age change label
        settingsLayout.putConstraint(SpringLayout.WEST, changeAgeLabel, 100, SpringLayout.WEST, settingsPanel);
        settingsLayout.putConstraint(SpringLayout.NORTH, changeAgeLabel, 50, SpringLayout.NORTH, changeNameLabel);

        //set the constraints for the age change button
        settingsLayout.putConstraint(SpringLayout.WEST, changeAgeButton, 120, SpringLayout.WEST, changeAgeLabel);
        settingsLayout.putConstraint(SpringLayout.NORTH, changeAgeButton, 50, SpringLayout.NORTH, changeNameLabel);

        //set the constraints for the password change label
        settingsLayout.putConstraint(SpringLayout.WEST, changePasswordLabel, 100, SpringLayout.WEST, settingsPanel);
        settingsLayout.putConstraint(SpringLayout.NORTH, changePasswordLabel, 50, SpringLayout.NORTH, changeAgeLabel);

        //set the constraints for the password change button
        settingsLayout.putConstraint(SpringLayout.WEST, changePasswordButton, 120, SpringLayout.WEST, changeAgeLabel);
        settingsLayout.putConstraint(SpringLayout.NORTH, changePasswordButton, 50, SpringLayout.NORTH, changeAgeButton);

        //set the constraints for the color mode label
        settingsLayout.putConstraint(SpringLayout.WEST, setModeLabel, 100, SpringLayout.WEST, settingsPanel);
        settingsLayout.putConstraint(SpringLayout.NORTH, setModeLabel, 50, SpringLayout.NORTH, changePasswordLabel);

        //set the constraints for the light mode button
        settingsLayout.putConstraint(SpringLayout.WEST, lightModeButton, 140, SpringLayout.WEST, setModeLabel);
        settingsLayout.putConstraint(SpringLayout.NORTH, lightModeButton, 50, SpringLayout.NORTH, changePasswordButton);

        //set the constraints for the dark mode button
        settingsLayout.putConstraint(SpringLayout.WEST, darkModeButton, 140, SpringLayout.WEST, setModeLabel);
        settingsLayout.putConstraint(SpringLayout.NORTH, darkModeButton, 30, SpringLayout.NORTH, lightModeButton);

        //set the constraints for the default mode button
        settingsLayout.putConstraint(SpringLayout.WEST, defaultModeButton, 140, SpringLayout.WEST, setModeLabel);
        settingsLayout.putConstraint(SpringLayout.NORTH, defaultModeButton, 30, SpringLayout.NORTH, darkModeButton);

        //formating for each button on the screens
        //changeName
        changeNameLayout.putConstraint(SpringLayout.WEST, confirmNameChangeLabel, 100, SpringLayout.WEST, changeNamePanel);
        changeNameLayout.putConstraint(SpringLayout.NORTH, confirmNameChangeLabel, 50, SpringLayout.NORTH, changeNamePanel);
        changeNameLayout.putConstraint(SpringLayout.WEST, nameUsernameLabel, 100, SpringLayout.WEST, changeNamePanel);
        changeNameLayout.putConstraint(SpringLayout.NORTH, nameUsernameLabel, 100, SpringLayout.NORTH, changeNamePanel);
        changeNameLayout.putConstraint(SpringLayout.WEST, nameUsernameText, 100, SpringLayout.WEST, changeNamePanel);
        changeNameLayout.putConstraint(SpringLayout.NORTH, nameUsernameText, 20, SpringLayout.NORTH, nameUsernameLabel);
        changeNameLayout.putConstraint(SpringLayout.WEST, newNameLabel, 100, SpringLayout.WEST, changeNamePanel);
        changeNameLayout.putConstraint(SpringLayout.NORTH, newNameLabel, 20, SpringLayout.NORTH, nameUsernameText);
        changeNameLayout.putConstraint(SpringLayout.WEST, newNameText, 100, SpringLayout.WEST, changeNamePanel);
        changeNameLayout.putConstraint(SpringLayout.NORTH, newNameText, 20, SpringLayout.NORTH, newNameLabel);
        changeNameLayout.putConstraint(SpringLayout.WEST, newNameButton, 100, SpringLayout.WEST, changeNamePanel);
        changeNameLayout.putConstraint(SpringLayout.NORTH, newNameButton, 20, SpringLayout.NORTH, newNameText);
        changeNameLayout.putConstraint(SpringLayout.WEST, confirmationNameLabel, 100, SpringLayout.WEST, changeNamePanel);
        changeNameLayout.putConstraint(SpringLayout.NORTH, confirmationNameLabel, 30, SpringLayout.NORTH, newNameButton);

        //changeAge
        changeAgeLayout.putConstraint(SpringLayout.WEST, confirmAgeChangeLabel, 100, SpringLayout.WEST, changeAgePanel);
        changeAgeLayout.putConstraint(SpringLayout.NORTH, confirmAgeChangeLabel, 100, SpringLayout.NORTH, changeAgePanel);
        changeAgeLayout.putConstraint(SpringLayout.WEST, ageUsernameLabel, 100, SpringLayout.WEST, changeAgePanel);
        changeAgeLayout.putConstraint(SpringLayout.NORTH, ageUsernameLabel, 20, SpringLayout.NORTH, confirmAgeChangeLabel);
        changeAgeLayout.putConstraint(SpringLayout.WEST, ageUsernameText, 100, SpringLayout.WEST, changeAgePanel);
        changeAgeLayout.putConstraint(SpringLayout.NORTH, ageUsernameText, 20, SpringLayout.NORTH, ageUsernameLabel);
        changeAgeLayout.putConstraint(SpringLayout.WEST, newAgeLabel, 100, SpringLayout.WEST, changeAgePanel);
        changeAgeLayout.putConstraint(SpringLayout.NORTH, newAgeLabel, 20, SpringLayout.NORTH, ageUsernameText);
        changeAgeLayout.putConstraint(SpringLayout.WEST, newAgeText, 100, SpringLayout.WEST, changeAgePanel);
        changeAgeLayout.putConstraint(SpringLayout.NORTH, newAgeText, 20, SpringLayout.NORTH, newAgeLabel);
        changeAgeLayout.putConstraint(SpringLayout.WEST, newAgeButton, 170, SpringLayout.WEST, changeAgePanel);
        changeAgeLayout.putConstraint(SpringLayout.NORTH, newAgeButton, 30, SpringLayout.NORTH, newAgeText);
        changeAgeLayout.putConstraint(SpringLayout.WEST, confirmationAgeLabel, 100, SpringLayout.WEST, changeAgePanel);
        changeAgeLayout.putConstraint(SpringLayout.NORTH, confirmationAgeLabel, 30, SpringLayout.NORTH, newAgeButton);

        //changePassword
        changePasswordLayout.putConstraint(SpringLayout.WEST, confirmPasswordChangeLabel, 100, SpringLayout.WEST, changePasswordPanel);
        changePasswordLayout.putConstraint(SpringLayout.NORTH, confirmPasswordChangeLabel, 50, SpringLayout.NORTH, changePasswordPanel);
        changePasswordLayout.putConstraint(SpringLayout.WEST, passUsernameLabel, 100, SpringLayout.WEST, changePasswordPanel);
        changePasswordLayout.putConstraint(SpringLayout.NORTH, passUsernameLabel, 100, SpringLayout.NORTH, changePasswordPanel);
        changePasswordLayout.putConstraint(SpringLayout.WEST, passUsernameText, 100, SpringLayout.WEST, changePasswordPanel);
        changePasswordLayout.putConstraint(SpringLayout.NORTH, passUsernameText, 20, SpringLayout.NORTH, passUsernameLabel);
        changePasswordLayout.putConstraint(SpringLayout.WEST, newPasswordLabel, 100, SpringLayout.WEST, changePasswordPanel);
        changePasswordLayout.putConstraint(SpringLayout.NORTH, newPasswordLabel, 20, SpringLayout.NORTH, passUsernameText);
        changePasswordLayout.putConstraint(SpringLayout.WEST, newPasswordText, 100, SpringLayout.WEST, changePasswordPanel);
        changePasswordLayout.putConstraint(SpringLayout.NORTH, newPasswordText, 20, SpringLayout.NORTH, newPasswordLabel);
        changePasswordLayout.putConstraint(SpringLayout.WEST, newPasswordButton, 100, SpringLayout.WEST, changePasswordPanel);
        changePasswordLayout.putConstraint(SpringLayout.NORTH, newPasswordButton, 20, SpringLayout.NORTH, newPasswordText);
        changePasswordLayout.putConstraint(SpringLayout.WEST, confirmationPasswordLabel, 100, SpringLayout.WEST, changePasswordPanel);
        changePasswordLayout.putConstraint(SpringLayout.NORTH, confirmationPasswordLabel, 40, SpringLayout.NORTH, newPasswordButton);

        // Create JComponents for account screen
        JButton searchButton = new JButton("Search");
        JTextField searchField = new JTextField();
        JLabel accountTitleLabel = new JLabel("Account Details");
        JLabel currentUsernameLabel = new JLabel("Username:");
        JLabel currentNameLabel = new JLabel("Name:");
        JLabel currentUsername = new JLabel("");
        JLabel currentName = new JLabel("");
        JLabel friendsLabel = new JLabel("Number of Friends:");
        JLabel friends = new JLabel("");
        JButton viewFriendsButton = new JButton("View Friends");
        JLabel blockedLabel = new JLabel("Number of Blocked Users:");
        JLabel blocked = new JLabel("");
        JButton viewBlockedButton = new JButton("View Blocked");
        
        // Add components to the account screen
        accountScreen.add(searchButton);
        accountScreen.add(searchField);
        accountScreen.add(accountTitleLabel);
        accountScreen.add(currentUsernameLabel);
        accountScreen.add(currentNameLabel);
        accountScreen.add(currentUsername);
        accountScreen.add(currentName);
        accountScreen.add(friendsLabel);
        accountScreen.add(friends);
        accountScreen.add(viewFriendsButton);
        accountScreen.add(blockedLabel);
        accountScreen.add(blocked);
        accountScreen.add(viewBlockedButton);
        
        // Set layout constraints
        accountScreenLayout.putConstraint(SpringLayout.NORTH, searchField, 10, SpringLayout.NORTH, accountScreen);
        accountScreenLayout.putConstraint(SpringLayout.WEST, searchField, 10, SpringLayout.WEST, accountScreen);
        accountScreenLayout.putConstraint(SpringLayout.EAST, searchField, -200, SpringLayout.EAST, accountScreen);
        accountScreenLayout.putConstraint(SpringLayout.NORTH, searchButton, 10, SpringLayout.NORTH, accountScreen);
        accountScreenLayout.putConstraint(SpringLayout.WEST, searchButton, 10, SpringLayout.EAST, searchField);
        accountScreenLayout.putConstraint(SpringLayout.HORIZONTAL_CENTER, accountTitleLabel, 0, SpringLayout.HORIZONTAL_CENTER, accountScreen);
        accountScreenLayout.putConstraint(SpringLayout.NORTH, accountTitleLabel, 40, SpringLayout.NORTH, accountScreen);
        accountScreenLayout.putConstraint(SpringLayout.WEST, currentUsernameLabel, 150, SpringLayout.WEST, accountScreen);
        accountScreenLayout.putConstraint(SpringLayout.NORTH, currentUsernameLabel, 50, SpringLayout.SOUTH, accountTitleLabel);
        accountScreenLayout.putConstraint(SpringLayout.WEST, currentUsername, 30, SpringLayout.EAST, currentUsernameLabel);
        accountScreenLayout.putConstraint(SpringLayout.NORTH, currentUsername, 50, SpringLayout.SOUTH, accountTitleLabel);
        accountScreenLayout.putConstraint(SpringLayout.WEST, currentNameLabel, 150, SpringLayout.WEST, accountScreen);
        accountScreenLayout.putConstraint(SpringLayout.NORTH, currentNameLabel, 30, SpringLayout.SOUTH, currentUsernameLabel);
        accountScreenLayout.putConstraint(SpringLayout.WEST, currentName, 0, SpringLayout.WEST, currentUsername);
        accountScreenLayout.putConstraint(SpringLayout.NORTH, currentName, 30, SpringLayout.SOUTH, currentUsernameLabel);
        accountScreenLayout.putConstraint(SpringLayout.WEST, friendsLabel, 150, SpringLayout.WEST, accountScreen);
        accountScreenLayout.putConstraint(SpringLayout.NORTH, friendsLabel, 30, SpringLayout.SOUTH, currentNameLabel);
        accountScreenLayout.putConstraint(SpringLayout.WEST, friends, 30, SpringLayout.EAST, friendsLabel);
        accountScreenLayout.putConstraint(SpringLayout.NORTH, friends, 30, SpringLayout.SOUTH, currentNameLabel);
        accountScreenLayout.putConstraint(SpringLayout.WEST, viewFriendsButton, 10, SpringLayout.EAST, friends);
        accountScreenLayout.putConstraint(SpringLayout.SOUTH, viewFriendsButton, 10, SpringLayout.SOUTH, friends);
        accountScreenLayout.putConstraint(SpringLayout.WEST, blockedLabel, 150, SpringLayout.WEST, accountScreen);
        accountScreenLayout.putConstraint(SpringLayout.NORTH, blockedLabel, 30, SpringLayout.SOUTH, friendsLabel);
        accountScreenLayout.putConstraint(SpringLayout.WEST, blocked, 30, SpringLayout.EAST, blockedLabel);
        accountScreenLayout.putConstraint(SpringLayout.NORTH, blocked, 30, SpringLayout.SOUTH, friendsLabel);
        accountScreenLayout.putConstraint(SpringLayout.WEST, viewBlockedButton, 10, SpringLayout.EAST, blocked);
        accountScreenLayout.putConstraint(SpringLayout.SOUTH, viewBlockedButton, 10, SpringLayout.SOUTH, blocked);



        JLabel viewAccountTitleLabel = new JLabel("Account Details");
        JLabel viewCurrentUsernameLabel = new JLabel("Username:");
        JLabel viewCurrentNameLabel = new JLabel("Name:");
        JLabel viewCurrentAgeLabel = new JLabel("Age:");
        JLabel viewCurrentUsername = new JLabel("");
        JLabel viewCurrentName = new JLabel("");
        JLabel viewCurrentAge = new JLabel("");
        JButton addFriend = new JButton("Add Friend");
        JButton addBlocked = new JButton("Block User");
        JButton createMessage = new JButton("Send New Message");
        JLabel message = new JLabel("");

        accountViewScreen.add(viewAccountTitleLabel);
        accountViewScreen.add(viewCurrentUsernameLabel);
        accountViewScreen.add(viewCurrentUsername);
        accountViewScreen.add(viewCurrentNameLabel);
        accountViewScreen.add(viewCurrentName);
        accountViewScreen.add(viewCurrentAgeLabel);
        accountViewScreen.add(viewCurrentAge);
        accountViewScreen.add(addFriend);
        accountViewScreen.add(addBlocked);
        accountViewScreen.add(createMessage);
        accountViewScreen.add(message);

        accountViewScreenLayout.putConstraint(SpringLayout.HORIZONTAL_CENTER, viewAccountTitleLabel, 0, SpringLayout.HORIZONTAL_CENTER, accountViewScreen);
        accountViewScreenLayout.putConstraint(SpringLayout.NORTH, viewAccountTitleLabel, 40, SpringLayout.NORTH, accountViewScreen);
        accountViewScreenLayout.putConstraint(SpringLayout.WEST, viewCurrentUsernameLabel, 150, SpringLayout.WEST, accountViewScreen);
        accountViewScreenLayout.putConstraint(SpringLayout.NORTH, viewCurrentUsernameLabel, 50, SpringLayout.SOUTH, viewAccountTitleLabel);
        accountViewScreenLayout.putConstraint(SpringLayout.WEST, viewCurrentUsername, 30, SpringLayout.EAST, viewCurrentUsernameLabel);
        accountViewScreenLayout.putConstraint(SpringLayout.NORTH, viewCurrentUsername, 0, SpringLayout.NORTH, viewCurrentUsernameLabel);
        accountViewScreenLayout.putConstraint(SpringLayout.WEST, viewCurrentNameLabel, 150, SpringLayout.WEST, accountViewScreen);
        accountViewScreenLayout.putConstraint(SpringLayout.NORTH, viewCurrentNameLabel, 30, SpringLayout.SOUTH, viewCurrentUsernameLabel);
        accountViewScreenLayout.putConstraint(SpringLayout.WEST, viewCurrentName, 0, SpringLayout.WEST, viewCurrentUsername);
        accountViewScreenLayout.putConstraint(SpringLayout.NORTH, viewCurrentName, 30, SpringLayout.SOUTH, viewCurrentUsernameLabel);
        accountViewScreenLayout.putConstraint(SpringLayout.WEST, viewCurrentAgeLabel, 150, SpringLayout.WEST, accountViewScreen);
        accountViewScreenLayout.putConstraint(SpringLayout.NORTH, viewCurrentAgeLabel, 30, SpringLayout.SOUTH, viewCurrentNameLabel);
        accountViewScreenLayout.putConstraint(SpringLayout.WEST, viewCurrentAge, 0, SpringLayout.WEST, viewCurrentName);
        accountViewScreenLayout.putConstraint(SpringLayout.NORTH, viewCurrentAge, 30, SpringLayout.SOUTH, viewCurrentNameLabel);
        accountViewScreenLayout.putConstraint(SpringLayout.WEST, addFriend, 60, SpringLayout.WEST, accountViewScreen);
        accountViewScreenLayout.putConstraint(SpringLayout.NORTH, addFriend, 30, SpringLayout.SOUTH, viewCurrentAgeLabel);
        accountViewScreenLayout.putConstraint(SpringLayout.WEST, addBlocked, 20, SpringLayout.EAST, addFriend);
        accountViewScreenLayout.putConstraint(SpringLayout.NORTH, addBlocked, 30, SpringLayout.SOUTH, viewCurrentAgeLabel);
        accountViewScreenLayout.putConstraint(SpringLayout.WEST, createMessage, 20, SpringLayout.EAST, addBlocked);
        accountViewScreenLayout.putConstraint(SpringLayout.NORTH, createMessage, 30, SpringLayout.SOUTH, viewCurrentAgeLabel);
        accountViewScreenLayout.putConstraint(SpringLayout.WEST, message, 150, SpringLayout.WEST, accountViewScreen);
        accountViewScreenLayout.putConstraint(SpringLayout.NORTH, message, 30, SpringLayout.SOUTH, createMessage);


        JPanel friendsPanel = new JPanel();
        friendsPanel.setLayout(new BoxLayout(friendsPanel, BoxLayout.Y_AXIS));

        JPanel blockedPanel = new JPanel();
        blockedPanel.setLayout(new BoxLayout(blockedPanel, BoxLayout.Y_AXIS));

        JPanel[] jPanels = {sidePanel, loginScreen, createScreen, settingsPanel, changeAgePanel, changeNamePanel, changePasswordPanel, accountScreen, accountViewScreen};

        //buttons for general settings actions
        changeNameButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                frame.add(changeNamePanel);
                frame.remove(settingsPanel);
                frame.revalidate();
                frame.repaint();
            }
        });
        changeAgeButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                frame.add(changeAgePanel);
                frame.remove(settingsPanel);
                frame.revalidate();
                frame.repaint();
            }
        });
        defaultModeButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                backgroundColor = Color.LIGHT_GRAY;
                frame.getContentPane().getComponent(1).setBackground(backgroundColor);
                updatePanels(jPanels);
            }
        });
        darkModeButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                backgroundColor = new Color(100, 100, 100);
                frame.getContentPane().getComponent(1).setBackground(backgroundColor);
                updatePanels(jPanels);
            }
        });
        lightModeButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                backgroundColor = Color.WHITE;
                frame.getContentPane().getComponent(1).setBackground(backgroundColor);
                updatePanels(jPanels);
            }
        });

        try {
            Socket mainSocket = new Socket("localhost", 4242); // Connects to main server
            BufferedReader reader = new BufferedReader(new InputStreamReader(mainSocket.getInputStream()));
            PrintWriter out = new PrintWriter(mainSocket.getOutputStream());
            String subserverPort = reader.readLine(); // Recieves port number and index of an open subserver from the main server
            if (subserverPort.equals("No Open Servers")) {
                hasServer = false;
                out.write("NoServerFound");
                out.println();
                out.flush();
                mainSocket.close();
                System.out.println("No Open Servers found");
            } else {
                serverPort = Integer.parseInt(subserverPort.substring(0, subserverPort.indexOf(";")));
                serverIndex = Integer.parseInt(subserverPort.substring(subserverPort.indexOf(";") + 1));
                Socket subSocket = new Socket("localhost", serverPort);
                out.write("ReceivedServer");
                out.println();
                out.flush();
                mainSocket.close();
                reader.close();
                out.close();
                PrintWriter subOut = new PrintWriter(subSocket.getOutputStream());
                BufferedReader subIn = new BufferedReader(new InputStreamReader(subSocket.getInputStream()));
                addFriend.addActionListener(new ActionListener() {
                    public void actionPerformed(ActionEvent e) {
                        while(frame.getContentPane().getComponentCount() > 1) {
                            frame.getContentPane().remove(1);
                        }
                        writeServer("1", subOut);
                        writeServer("exit", subOut);
                        writeServer("1", subOut);
                        client.friendCount = Integer.parseInt(readServer(subIn));
                        friends.setText("" + client.friendCount);
                        frame.add(accountScreen);
                        frame.revalidate();
                        frame.repaint();
                    }
                });
                addBlocked.addActionListener(new ActionListener() {
                    public void actionPerformed(ActionEvent e) {
                        while(frame.getContentPane().getComponentCount() > 1) {
                            frame.getContentPane().remove(1);
                        }
                        writeServer("2", subOut);
                        writeServer("exit", subOut);
                        writeServer("2", subOut);
                        client.blockedCount = Integer.parseInt(readServer(subIn));
                        blocked.setText("" + client.blockedCount);
                        frame.add(accountScreen);
                        frame.revalidate();
                        frame.repaint();
                    }
                });
                createMessage.addActionListener(new ActionListener() {
                    public void actionPerformed(ActionEvent e) {
                        writeServer("5", subOut);
                        String s = readServer(subIn);
                        if (s.equals("exists")) {
                            message.setText("you have already messaged this user");
                        } else {
                            message.setText("Conversation created");
                        }
                    }
                });
                accountButton.addActionListener(new ActionListener() {
                    public void actionPerformed(ActionEvent e) {
                        writeServer("exit", subOut);
                        while(frame.getContentPane().getComponentCount() > 1) {
                            frame.getContentPane().remove(1);
                        }
                        writeServer("1", subOut);
                        client.friendCount = Integer.parseInt(readServer(subIn));
                        friends.setText("" + client.friendCount);
                        writeServer("2", subOut);
                        client.blockedCount = Integer.parseInt(readServer(subIn));
                        blocked.setText("" + client.blockedCount);
                        frame.add(accountScreen);
                        frame.revalidate();
                        frame.repaint();
                    }
                });
                searchButton.addActionListener(new ActionListener() {
                    public void actionPerformed(ActionEvent e) {
                        writeServer("exit", subOut);
                        writeServer("4", subOut);
                        writeServer(searchField.getText(), subOut);
                        String userInfo = readServer(subIn);
                        if (userInfo.equals("not exists") || userInfo.equals("User not found")) {
                                searchField.setText("User not found");
                        } else {
                            while (frame.getContentPane().getComponentCount() > 1) {
                                frame.getContentPane().remove(1);
                            }
                            System.out.println(userInfo);
                            viewCurrentName.setText(userInfo.substring(0, userInfo.indexOf(",")));
                            userInfo = userInfo.substring(userInfo.indexOf(",") + 1);
                            viewCurrentUsername.setText(userInfo.substring(0, userInfo.indexOf(",")));
                            userInfo = userInfo.substring(userInfo.indexOf(",") + 1);
                            viewCurrentAge.setText(userInfo);
                            searchField.setText("");
                            message.setText("");
                            frame.add(accountViewScreen);
                            frame.revalidate();
                            frame.repaint();
                        }
                    }
                });
                viewFriendsButton.addActionListener(new ActionListener() {
                    public void actionPerformed(ActionEvent e) {
                        while(frame.getContentPane().getComponentCount() > 1) {
                            frame.getContentPane().remove(1);
                        }
                        writeServer("8", subOut);
                        int friendCount = Integer.parseInt(readServer(subIn));
                        if (friendCount < 1) {
                            friendsPanel.removeAll();
                            JLabel noFriends = new JLabel("No friends");
                            friendsPanel.add(noFriends);
                            JScrollPane scrollPane = new JScrollPane(friendsPanel);
                            scrollPane.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_ALWAYS);
                            frame.add(scrollPane);
                            frame.revalidate();
                            frame.repaint();
                        } else {
                            friendsPanel.removeAll();
                            for (int i = 0; i < friendCount; i++) {
                                String friend = readServer(subIn);
                                writeServer("recieved", subOut);
                                JPanel oneFriend = new JPanel(new BorderLayout());
                                JLabel friendLabel = new JLabel(friend);
                                JButton removeButton = new JButton("Remove " + friend);
                                removeButton.addActionListener(new ActionListener() {
                                    public void actionPerformed(ActionEvent e) {
                                        writeServer("10", subOut);
                                        writeServer(friend, subOut);
                                        System.out.println(friend);
                                        writeServer("exit", subOut);
                                        while(frame.getContentPane().getComponentCount() > 1) {
                                            frame.getContentPane().remove(1);
                                        }
                                        writeServer("1", subOut);
                                        client.friendCount = Integer.parseInt(readServer(subIn));
                                        friends.setText("" + client.friendCount);
                                        writeServer("2", subOut);
                                        client.blockedCount = Integer.parseInt(readServer(subIn));
                                        blocked.setText("" + client.blockedCount);
                                        frame.add(accountScreen);
                                        frame.revalidate();
                                        frame.repaint();
                                    }
                                });
                                oneFriend.add(friendLabel, BorderLayout.CENTER);
                                oneFriend.add(removeButton, BorderLayout.EAST);
                                oneFriend.setMaximumSize(new Dimension(4500, 20));
                                friendsPanel.add(oneFriend);
                                friendsPanel.add(Box.createVerticalStrut(3));
                            }
                            JScrollPane scrollPane = new JScrollPane(friendsPanel);
                            scrollPane.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_ALWAYS);
                            frame.add(scrollPane);
                            frame.revalidate();
                            frame.repaint();
                        }
                    }
                });
                viewBlockedButton.addActionListener(new ActionListener() {
                    public void actionPerformed(ActionEvent e) {
                        while(frame.getContentPane().getComponentCount() > 1) {
                            frame.getContentPane().remove(1);
                        }
                        writeServer("9", subOut);
                        int blockedCount = Integer.parseInt(readServer(subIn));
                        if (blockedCount < 1) {
                            blockedPanel.removeAll();
                            JLabel noBlocked = new JLabel("No Blocked Users");
                            blockedPanel.add(noBlocked);
                            JScrollPane scrollPane = new JScrollPane(blockedPanel);
                            scrollPane.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_ALWAYS);
                            frame.add(scrollPane);
                            frame.revalidate();
                            frame.repaint();
                        } else {
                            blockedPanel.removeAll();
                            for (int i = 0; i < blockedCount; i++) {
                                String block = readServer(subIn);
                                writeServer("recieved", subOut);
                                JPanel oneBlock = new JPanel(new BorderLayout());
                                JLabel friendLabel = new JLabel(block);
                                JButton removeButton = new JButton("Unblock " + block);
                                removeButton.addActionListener(new ActionListener() {
                                    public void actionPerformed(ActionEvent e) {
                                        writeServer("11", subOut);
                                        writeServer(block, subOut);
                                        System.out.println(block);
                                        writeServer("exit", subOut);
                                        while(frame.getContentPane().getComponentCount() > 1) {
                                            frame.getContentPane().remove(1);
                                        }
                                        writeServer("1", subOut);
                                        client.blockedCount = Integer.parseInt(readServer(subIn));
                                        blocked.setText("" + client.friendCount);
                                        writeServer("2", subOut);
                                        client.blockedCount = Integer.parseInt(readServer(subIn));
                                        blocked.setText("" + client.blockedCount);
                                        frame.add(accountScreen);
                                        frame.revalidate();
                                        frame.repaint();
                                    }
                                });
                                oneBlock.add(friendLabel, BorderLayout.CENTER);
                                oneBlock.add(removeButton, BorderLayout.EAST);
                                oneBlock.setMaximumSize(new Dimension(4500, 20));
                                blockedPanel.add(oneBlock);
                                blockedPanel.add(Box.createVerticalStrut(3));
                            }
                            JScrollPane scrollPane = new JScrollPane(blockedPanel);
                            scrollPane.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_ALWAYS);
                            frame.add(scrollPane);
                            frame.revalidate();
                            frame.repaint();
                        }
                    }
                });
                messagesButton.addActionListener(new ActionListener() {
                    public void actionPerformed(ActionEvent e) {
                        if (client.username != null) {
                            while (frame.getContentPane().getComponentCount() > 1) {
                                frame.getContentPane().remove(1);
                            }
                            ChatMessagesScreen messagesScreen = new ChatMessagesScreen(client.username);
                            messagesScreen.createMessagesScreen(frame, backgroundColor, subOut, subIn);
                            frame.revalidate();
                            frame.repaint();
                        } else {
                            System.out.println("Error: Username not initialized");
                        }
                    }
                });
                //action listener for the login button
                loginButton.addActionListener(new ActionListener() {
                    public void actionPerformed(ActionEvent e) {
                        String enteredUsername = usernameInput.getText();
                        String password = passwordInput.getText();
                        writeServer("login", subOut);
                        writeServer(enteredUsername, subOut);
                        writeServer(password, subOut);
                        String loginCheck = readServer(subIn);
                        System.out.println(loginCheck);
                        if (loginCheck.equals("goodInfo")) {
                            client.username = enteredUsername; // Set username
                            while(frame.getContentPane().getComponentCount() > 1) {
                                frame.getContentPane().remove(1);
                            }
                            currentUsername.setText(client.username);
                            String name = readServer(subIn);
                            client.name = name;
                            currentName.setText(name);
                            writeServer("1", subOut);
                            client.friendCount = Integer.parseInt(readServer(subIn));
                            friends.setText("" + client.friendCount);
                            System.out.println(friends);
                            writeServer("2", subOut);
                            client.blockedCount = Integer.parseInt(readServer(subIn));
                            blocked.setText("" + client.blockedCount);
                            System.out.println(blocked);
                            frame.add(accountScreen);
                            frame.revalidate();
                            frame.repaint();
                            System.out.println("Login Successful, Welcome " + client.username);
                        } else {
                            System.out.println("Login Failed, please try again");
                        }
                    }
                });

                nameBackButton.addActionListener(new ActionListener() {
                    public void actionPerformed(ActionEvent e) {
                        if (client.username != null) {
                            while (frame.getContentPane().getComponentCount() > 1) {
                                frame.getContentPane().remove(1);
                            }
                            frame.add(settingsPanel);
                            frame.revalidate();
                            frame.repaint();
                        } else {
                            System.out.println("Username not initialized");
                        }
                    }
                });

                ageBackButton.addActionListener(new ActionListener() {
                    public void actionPerformed(ActionEvent e) {
                        if (client.username != null) {
                            while (frame.getContentPane().getComponentCount() > 1) {
                                frame.getContentPane().remove(1);
                            }
                            frame.add(settingsPanel);
                            frame.revalidate();
                            frame.repaint();
                        } else {
                            System.out.println("Username not initialized");
                        }
                    }
                });

                passwordBackButton.addActionListener(new ActionListener() {
                    public void actionPerformed(ActionEvent e) {
                        if (client.username != null) {
                            while (frame.getContentPane().getComponentCount() > 1) {
                                frame.getContentPane().remove(1);
                            }
                            frame.add(settingsPanel);
                            frame.revalidate();
                            frame.repaint();
                        } else {
                            System.out.println("Username not initialized");
                        }
                    }
                });

                settingsButton.addActionListener(new ActionListener() {
                    public void actionPerformed(ActionEvent e) {
                        if (client.username != null) {
                            while (frame.getContentPane().getComponentCount() > 1) {
                                frame.getContentPane().remove(1);
                            }
                            writeServer("exit", subOut);
                            frame.add(settingsPanel);
                            frame.revalidate();
                            frame.repaint();
                        } else {
                            System.out.println("Username not initialized");
                        }
                    }
                });

                changeNameButton.addActionListener(new ActionListener() {
                    public void actionPerformed(ActionEvent e) {
                        writeServer("12", subOut);
                        frame.add(changeNamePanel);
                        frame.remove(settingsPanel);
                        frame.revalidate();
                        frame.repaint();
                    }
                });

                newNameButton.addActionListener(new ActionListener() {
                    public void actionPerformed(ActionEvent e) {
                        writeServer(nameUsernameText.getText() + "," + newNameText.getText(), subOut);
                        boolean successful = Boolean.parseBoolean(readServer(subIn));
                        if (successful) {
                            confirmationNameLabel.setText("Changed Name Successfully");
                        } else {
                            confirmationNameLabel.setText("Error Occurred: Server Side");
                        }
                    }
                });

                changeAgeButton.addActionListener(new ActionListener() {
                    public void actionPerformed(ActionEvent e) {
                        writeServer("7", subOut);
                        frame.add(changeAgePanel);
                        frame.remove(settingsPanel);
                        frame.revalidate();
                        frame.repaint();
                    }
                });

                newAgeButton.addActionListener(new ActionListener() {
                    public void actionPerformed(ActionEvent e) {
                        try {
                            int age = Integer.parseInt(newAgeText.getText());
                            if (age <= 0) {
                                confirmationAgeLabel.setText("Please enter a positive number");
                                return;
                            }
                            writeServer(ageUsernameText.getText() + "," + String.valueOf(age), subOut);
                            boolean successful = Boolean.parseBoolean(readServer(subIn));
                            if (successful) {
                                confirmationAgeLabel.setText("Age Successfully Changed");
                            }
                        } catch (NumberFormatException f) {
                            confirmationAgeLabel.setText("Please Enter a Number");
                        }
                    }
                });

                changePasswordButton.addActionListener(new ActionListener() {
                    public void actionPerformed(ActionEvent e) {
                        writeServer("6", subOut);
                        frame.add(changePasswordPanel);
                        frame.remove(settingsPanel);
                        frame.revalidate();
                        frame.repaint();
                    }
                });

                newPasswordButton.addActionListener(new ActionListener() {
                    public void actionPerformed(ActionEvent e) {
                        if (!passUsernameText.getText().isEmpty() && !newPasswordText.getText().isEmpty()) {
                            String toSend = String.format("%s,%s", passUsernameText.getText(), newPasswordText.getText());
                            writeServer(toSend, subOut);
                            String valid = readServer(subIn);
                            System.out.println(valid);
                            boolean successful = Boolean.parseBoolean(readServer(subIn));
                            System.out.println(successful);
                        } else {
                            confirmationPasswordLabel.setText("Please fill out all the text fields");
                        }

                    }
                });

                newUserButton.addActionListener(new ActionListener() {
                    public void actionPerformed(ActionEvent e) {
                        writeServer("Create", subOut);
                        frame.add(createScreen);
                        frame.remove(loginScreen);
                        frame.revalidate();
                        frame.repaint();
                    }
                });

                createButton.addActionListener(new ActionListener() {
                    public void actionPerformed(ActionEvent e) {
                        writeServer(createUsername.getText(),subOut);
                        String good = readServer(subIn);
                        if (good.equals("GoodUsername")) {
                            writeServer(createPassword.getText(), subOut);
                            String goodPassword = readServer(subIn);
                            if (goodPassword.equals("Good Password")) {
                                writeServer(nameInput.getText(), subOut);
                                String goodName = readServer(subIn);
                                if (goodName.equals("GoodName")) {
                                    writeServer(ageInput.getText(), subOut);
                                    String result = readServer(subIn);
                                    System.out.println(result);
                                    resultLabel.setText(result);
                                    frame.add(loginScreen);
                                    frame.remove(createScreen);
                                    frame.revalidate();
                                    frame.repaint();
                                }
                            } else {
                                System.out.println(goodPassword);
                                resultLabel.setText(goodPassword);
                                frame.remove(createScreen);
                                frame.add(loginScreen);
                                frame.revalidate();
                                frame.repaint();
                            }
                        } else if (good.equals("Username cannot contain commas")) {
                            System.out.println(good);
                            resultLabel.setText(good);
                            frame.remove(createScreen);
                            frame.add(loginScreen);
                            frame.revalidate();
                            frame.repaint();
                        } else {
                            System.out.println("already exists");
                            resultLabel.setText("Username already exists");
                            frame.remove(createScreen);
                            frame.add(loginScreen);
                            frame.revalidate();
                            frame.repaint();
                        }
                    }
                });
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    /**
     * A method to read the message the server sends
     *
     * @param in the reader to use
     * @return returns the received message from the server
     */
    public static String readServer(BufferedReader in) {
        try {
            String s = in.readLine();
            return s;
        } catch (IOException e) {
            return "Socket is not connected";
        }
    }
    /**
     * A method that sends the inputted message to the server
     *
     * @param msg the message to be sent
     * @param out the print writer that will be used
     * @return return true if sent successfully and false otherwise
     */
    public static void writeServer(String msg, PrintWriter out) {
        out.write(msg);
        out.println();
        out.flush();
    }
}
