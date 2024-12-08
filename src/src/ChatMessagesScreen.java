import java.io.*;
import java.util.ArrayList;
import javax.swing.*;
import java.awt.*;
import java.awt.event.*;

public class ChatMessagesScreen {
    private String username;
    private Color background;
    private PrintWriter out;
    private BufferedReader in;

    public ChatMessagesScreen(String username) {
        this.username = username;
    }

    public void createMessagesScreen(JFrame frame, Color background, PrintWriter out, BufferedReader in) {
        this.out = out;
        this.in = in;
        JPanel mainPanel = new JPanel(new BorderLayout());
        this.background = background;
        JPanel searchPanel = new JPanel(new BorderLayout());
        JTextField searchField = new JTextField();
        if (background.equals(new Color(50, 50, 50))) {
            searchField.setBackground(new Color(75, 75, 75));
        } else if (background.equals(new Color(255, 255, 255))) {
            searchField.setBackground(new Color(225, 225, 225));
        } else {
            searchField.setBackground(new Color (170, 170, 170));
        }
        JLabel searchLabel = new JLabel("Search Conversations:");
        JButton searchButton = new JButton("Search");

        searchPanel.add(searchLabel, BorderLayout.WEST);
        searchPanel.add(searchField, BorderLayout.CENTER);
        searchPanel.add(searchButton, BorderLayout.EAST);

        JPanel conversationPanel = new JPanel();
        conversationPanel.setLayout(new BoxLayout(conversationPanel, BoxLayout.Y_AXIS));

        ArrayList<String> conversations = getConversationsForUser();
        populateConversationList(conversationPanel, conversations, frame);

        JScrollPane scrollPane = new JScrollPane(conversationPanel);
        scrollPane.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_ALWAYS);

        mainPanel.add(searchPanel, BorderLayout.NORTH);
        mainPanel.add(scrollPane, BorderLayout.CENTER);
        searchPanel.setBackground(background);
        scrollPane.setBackground(background);
        mainPanel.setBackground(background);

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
                populateConversationList(conversationPanel, filteredConversations, frame);
                conversationPanel.revalidate();
                conversationPanel.repaint();
            }
        });
    }

    private void populateConversationList(JPanel conversationPanel, ArrayList<String> conversations, JFrame frame) {
        conversationPanel.removeAll();
        for (String conversation : conversations) {
            String displayName = getFriendlyName(conversation);
            conversationPanel.add(createConversationPanel(conversation, displayName, frame));
            conversationPanel.add(Box.createVerticalStrut(10));
        }
    }
    private JPanel createConversationPanel(String conversation, String displayName, JFrame frame) {
        JPanel conversationPanel = new JPanel(new BorderLayout());
        conversationPanel.setBorder(BorderFactory.createLineBorder(Color.BLACK));
        conversationPanel.setBackground(background);
        conversationPanel.setPreferredSize(new Dimension(450, 50));

        JLabel conversationLabel = new JLabel(displayName);
        conversationLabel.setFont(new Font("Georgia", Font.BOLD, 14));

        JButton openChatButton = new JButton("Open Chat");
        openChatButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                openConversation(conversation, frame);
            }
        });

        conversationPanel.add(conversationLabel, BorderLayout.CENTER);
        conversationPanel.add(openChatButton, BorderLayout.EAST);

        return conversationPanel;
    }

    private void openConversation(String conversation, JFrame frame) {
        JPanel bigPanel = new JPanel(new BorderLayout());
        JPanel conversationPanel = new JPanel();
//        conversationPanel.setBackground(background);
        conversationPanel.setLayout(new BoxLayout(conversationPanel, BoxLayout.Y_AXIS));
        JPanel messagePanel = new JPanel(new BorderLayout());
        messagePanel.setMaximumSize(new Dimension(4500, 20));
        JButton send = new JButton("Send");
        JTextField messageField = new JTextField();
        messagePanel.add(messageField, BorderLayout.CENTER);
        messagePanel.setBackground(background);
        messagePanel.add(send, BorderLayout.EAST);
        ArrayList<String> messages = new ArrayList<>();
        System.out.println(conversation);
        System.out.println(conversation);
        out.println("exit");
        out.flush();
        out.println("3");
        out.flush();
        out.println(conversation);
        out.flush();
        int size = Integer.parseInt(ClientInterface.readServer(in));
        System.out.println(size);
        for (int i = 0; i < size; i++) {
            messages.add(ClientInterface.readServer(in));
            conversationPanel.add(new JLabel(messages.get(i)));
            conversationPanel.add(Box.createVerticalStrut(3));
        }
        while (frame.getContentPane().getComponentCount() > 1) {
            System.out.println("removed");
            frame.remove(frame.getContentPane().getComponent(1));
        }
        JScrollPane scrollPane = new JScrollPane(conversationPanel);
        scrollPane.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_ALWAYS);
        bigPanel.add(scrollPane, BorderLayout.CENTER);
        bigPanel.add(messagePanel, BorderLayout.SOUTH);
        frame.add(bigPanel, BorderLayout.CENTER);
        frame.revalidate();
        frame.repaint();
        System.out.println("added");
        send.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                String message = messageField.getText();
                ClientInterface.writeServer(message, out);
                messageField.setText("");
                if (!message.equals("exit")){
                    conversationPanel.add(new JLabel(username + ": " + message));
                    frame.revalidate();
                    frame.repaint();
                }
            }
        });
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
