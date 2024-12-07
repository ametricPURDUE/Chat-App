import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;

public class ChatMessagesScreen {

    private ChatDatabase database; // Access to backend database
    private String username; // Currently logged-in user

    public ChatMessagesScreen(ChatDatabase database, String username) {
        this.database = database;
        this.username = username;
    }

    public void createMessagesScreen(JPanel sidePanel) {
        // Set up the frame
        JFrame frame = new JFrame("Messages");
        Container messages = frame.getContentPane();
        frame.setSize(600, 400);
        frame.setLocationRelativeTo(null);
        frame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        frame.setBackground(Color.LIGHT_GRAY);
        messages.add(sidePanel, BorderLayout.WEST);
        frame.setVisible(true);

        // Set up the main layout
        SpringLayout layout = new SpringLayout();
        JPanel mainPanel = new JPanel();
        mainPanel.setLayout(layout);

        // Add search bar components
        JLabel searchLabel = new JLabel("Search:");
        JTextField searchField = new JTextField(20);
        JButton searchButton = new JButton("Search");
        mainPanel.add(searchLabel);
        mainPanel.add(searchField);
        mainPanel.add(searchButton);

        // Add scrollable message list
        JPanel messageListPanel = new JPanel();
        messageListPanel.setLayout(new BoxLayout(messageListPanel, BoxLayout.Y_AXIS));
        JScrollPane scrollPane = new JScrollPane(messageListPanel);
        scrollPane.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_ALWAYS);
        mainPanel.add(scrollPane);

        // Set layout constraints
        layout.putConstraint(SpringLayout.WEST, searchLabel, 10, SpringLayout.WEST, mainPanel);
        layout.putConstraint(SpringLayout.NORTH, searchLabel, 10, SpringLayout.NORTH, mainPanel);

        layout.putConstraint(SpringLayout.WEST, searchField, 10, SpringLayout.EAST, searchLabel);
        layout.putConstraint(SpringLayout.NORTH, searchField, 10, SpringLayout.NORTH, mainPanel);

        layout.putConstraint(SpringLayout.WEST, searchButton, 10, SpringLayout.EAST, searchField);
        layout.putConstraint(SpringLayout.NORTH, searchButton, 10, SpringLayout.NORTH, mainPanel);

        layout.putConstraint(SpringLayout.WEST, scrollPane, 10, SpringLayout.WEST, mainPanel);
        layout.putConstraint(SpringLayout.NORTH, scrollPane, 20, SpringLayout.SOUTH, searchLabel);
        layout.putConstraint(SpringLayout.EAST, scrollPane, -10, SpringLayout.EAST, mainPanel);
        layout.putConstraint(SpringLayout.SOUTH, scrollPane, -10, SpringLayout.SOUTH, mainPanel);

        // Add friends to the message list
        ArrayList<User> friends = database.getUsers(username).getFriends();
        populateMessageList(messageListPanel, friends);

        // Search button functionality (updates list only when clicked)
        searchButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                String searchQuery = searchField.getText().toLowerCase();
                ArrayList<User> filteredFriends = new ArrayList<>();
                for (User friend : friends) {
                    if (friend.getUsername().toLowerCase().contains(searchQuery)) {
                        filteredFriends.add(friend);
                    }
                }
                populateMessageList(messageListPanel, filteredFriends);
                messageListPanel.revalidate();
                messageListPanel.repaint();
            }
        });

        // Add main panel to frame
        frame.add(mainPanel, BorderLayout.CENTER);
    }

    private void populateMessageList(JPanel panel, ArrayList<User> friends) {
        panel.removeAll(); // Clear the panel
        for (User friend : friends) {
            // Create a panel for each user
            JPanel userPanel = new JPanel();
            userPanel.setLayout(new FlowLayout(FlowLayout.LEFT));
            userPanel.setBorder(BorderFactory.createLineBorder(Color.BLACK));
            userPanel.setPreferredSize(new Dimension(400, 50));

            JLabel userNameLabel = new JLabel(friend.getUsername());
            JButton openChatButton = new JButton("Open Chat");

            openChatButton.addActionListener(new ActionListener() {
                public void actionPerformed(ActionEvent e) {
                    System.out.println("Opening conversation with " + friend.getUsername());
                }
            });

            userPanel.add(userNameLabel);
            userPanel.add(openChatButton);
            panel.add(userPanel);
        }
    }
}
