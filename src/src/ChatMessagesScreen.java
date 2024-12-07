import java.io.*;
import java.util.ArrayList;
import javax.swing.*;
import java.awt.*;
import java.awt.event.*;

public class ChatMessagesScreen {
    private String username;

    public ChatMessagesScreen(String username) {
        this.username = username;
    }

    public void createMessagesScreen(JFrame frame) {
        JPanel mainPanel = new JPanel(new BorderLayout());

        JPanel searchPanel = new JPanel(new BorderLayout());
        JTextField searchField = new JTextField();
        JLabel searchLabel = new JLabel("Search Conversations:");
        JButton searchButton = new JButton("Search");

        searchPanel.add(searchLabel, BorderLayout.WEST);
        searchPanel.add(searchField, BorderLayout.CENTER);
        searchPanel.add(searchButton, BorderLayout.EAST);

        JPanel conversationPanel = new JPanel();
        conversationPanel.setLayout(new BoxLayout(conversationPanel, BoxLayout.Y_AXIS));

        ArrayList<String> conversations = getConversationsForUser();
        populateConversationList(conversationPanel, conversations);

        JScrollPane scrollPane = new JScrollPane(conversationPanel);
        scrollPane.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_ALWAYS);

        mainPanel.add(searchPanel, BorderLayout.NORTH);
        mainPanel.add(scrollPane, BorderLayout.CENTER);

        frame.add(mainPanel, BorderLayout.CENTER);

        searchButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                String searchTerm = searchField.getText().toLowerCase();
                ArrayList<String> filteredConversations = new ArrayList<>();
                for (String conversation : conversations) {
                    String displayName = getFriendlyName(conversation);
                    if (displayName.toLowerCase().contains(searchTerm)) {
                        filteredConversations.add(conversation);
                    }
                }
                populateConversationList(conversationPanel, filteredConversations);
                conversationPanel.revalidate();
                conversationPanel.repaint();
            }
        });
    }

    private void populateConversationList(JPanel conversationPanel, ArrayList<String> conversations) {
        conversationPanel.removeAll();
        for (String conversation : conversations) {
            String displayName = getFriendlyName(conversation);
            conversationPanel.add(createConversationPanel(conversation, displayName));
            conversationPanel.add(Box.createVerticalStrut(10));
        }
    }
    private JPanel createConversationPanel(String conversation, String displayName) {
        JPanel conversationPanel = new JPanel(new BorderLayout());
        conversationPanel.setBorder(BorderFactory.createLineBorder(Color.BLACK));
        conversationPanel.setPreferredSize(new Dimension(450, 50));

        JLabel conversationLabel = new JLabel(displayName);
        conversationLabel.setFont(new Font("Georgia", Font.BOLD, 14));

        JButton openChatButton = new JButton("Open Chat");
        openChatButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                openConversation(conversation);
            }
        });

        conversationPanel.add(conversationLabel, BorderLayout.CENTER);
        conversationPanel.add(openChatButton, BorderLayout.EAST);

        return conversationPanel;
    }

    private void openConversation(String conversation) {
        System.out.println("Opening conversation: " + conversation);
    }

    private ArrayList<String> getConversationsForUser() {
        ArrayList<String> conversations = new ArrayList<>();
        File directory = new File(System.getProperty("user.dir"));
        if (directory.exists() && directory.isDirectory()) {
            for (File file : directory.listFiles()) {
                if (file.getName().startsWith("chat") && file.getName().contains(username)) {
                    conversations.add(file.getName());
                }
            }
        }
        return conversations;
    }

    private String getFriendlyName(String conversation) {
        String strippedName = conversation.replace("chat", "").replace(".txt", "");
        strippedName = strippedName.replace(username, "");
        if (!strippedName.isEmpty()) {
            strippedName = strippedName.substring(0, 1).toUpperCase() + strippedName.substring(1);
        }
        return strippedName.trim();
    }
}
