package newGUI;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.util.concurrent.ExecutionException;

public class FriendsPanel extends JPanel {
    private MainFrame mainFrame;
    private JButton backButton;
    private JTextField usernameField;
    private JButton addButton, deleteButton;
    private JPanel friendsListPanel;  // Panel to display friends
    private JScrollPane scrollPane;  // Scrollable view for the friends list

    public FriendsPanel(MainFrame mainFrame) {
        this.mainFrame = mainFrame;
        initializeUI();
    }

    // TODO: fix the loading pause when adding/deleting a friend

    private void initializeUI() {
        setLayout(new BorderLayout());
        JPanel topPanel = new JPanel(new BorderLayout());
        topPanel.setBackground(Color.black);
        backButton = new JButton("Back");
        backButton.addActionListener(e -> mainFrame.showFeedPanel(SessionManager.getInstance().getCurrentFile()));
        styleButtonBack(backButton);
        topPanel.add(backButton, BorderLayout.WEST);
        topPanel.setOpaque(true);
        add(topPanel, BorderLayout.NORTH);

        JPanel centerPanel = new JPanel(new GridBagLayout());
        stylePanel(centerPanel);
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(10, 10, 10, 10);
        gbc.anchor = GridBagConstraints.WEST;

        JLabel usernameLabel = new JLabel("Username:");
        styleLabel(usernameLabel);
        gbc.gridx = 0;
        gbc.gridy = 0;
        centerPanel.add(usernameLabel, gbc);

        usernameField = new JTextField(20);
        styleText(usernameField);
        gbc.gridx = 1;
        centerPanel.add(usernameField, gbc);

        addButton = new JButton("Add Friend");
        styleButton(addButton);
        addButton.addActionListener(this::addFriend);
        gbc.gridy = 1;
        gbc.gridx = 0;
        gbc.gridwidth = 2;
        gbc.anchor = GridBagConstraints.CENTER;
        centerPanel.add(addButton, gbc);

        deleteButton = new JButton("Delete Friend");
        styleButton(deleteButton);
        deleteButton.addActionListener(this::deleteFriend);
        gbc.gridy = 2;
        centerPanel.add(deleteButton, gbc);

        // Friends list panel
        friendsListPanel = new JPanel();
        friendsListPanel.setBackground(Color.black);
        friendsListPanel.setLayout(new BoxLayout(friendsListPanel, BoxLayout.Y_AXIS));
        scrollPane = new JScrollPane(friendsListPanel);
        scrollPane.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_ALWAYS);

        add(scrollPane, BorderLayout.SOUTH);
        add(centerPanel, BorderLayout.CENTER);
    }

    private void addFriend(ActionEvent e) {
        String username = usernameField.getText().trim();
        if (username.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Username field cannot be empty.", "Error", JOptionPane.ERROR_MESSAGE);
            return;
        }

        // Check if the user is trying to add themselves as a friend
        if (username.equalsIgnoreCase(SessionManager.getInstance().getCurrentUserName())) {
            JOptionPane.showMessageDialog(this, "You cannot add yourself as a friend.", "Error", JOptionPane.ERROR_MESSAGE);
            return;
        }

        mainFrame.showLoadingPanel(); // Show loading panel while processing the addition

        SwingWorker<Boolean, Void> worker = new SwingWorker<Boolean, Void>() {
            @Override
            protected Boolean doInBackground() throws Exception {
                Query query = new Query();
                boolean success = query.addFriend(SessionManager.getInstance().getCurrentUserId(), username);
                if (success) {
                    // Fetch the updated data and set it in the session manager
                    long userId = SessionManager.getInstance().getCurrentUserId();
                    String currentUserName = SessionManager.getInstance().getCurrentUserName();
                    long[] friendsIds = query.fetchFriendsIdsFromDatabase(userId);
                    SessionManager.getInstance().setFriends(friendsIds);
                    SessionManager.getInstance().setCurrentUserName(currentUserName);
                    SessionManager.getInstance().setCurrentUserId(userId);
                    mainFrame.getFeedPanel().updateContent();

                    updateFriendsList();  // Update the friend list display

                    return true;
                }
                return false;
            }

            @Override
            protected void done() {
                try {
                    boolean success = get();
                    if (success) {
                        //JOptionPane.showMessageDialog(FriendsPanel.this, "Friend added successfully!", "Success", JOptionPane.INFORMATION_MESSAGE);
                        mainFrame.showFriendsPanel(); // Refresh friends panel
                    } else {
                        JOptionPane.showMessageDialog(FriendsPanel.this, "Could not add user as friend. They may already be your friend or the username does not exist.", "Error", JOptionPane.ERROR_MESSAGE);
                    }
                } catch (InterruptedException | ExecutionException ex) {
                    ex.printStackTrace();
                    JOptionPane.showMessageDialog(FriendsPanel.this, "Failed to add friend.", "Error", JOptionPane.ERROR_MESSAGE);
                }
            }
        };
        worker.execute();
    }

    private void deleteFriend(ActionEvent e) {
        String username = usernameField.getText().trim();
        if (username.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Username field cannot be empty.", "Error", JOptionPane.ERROR_MESSAGE);
            return;
        }

        mainFrame.showLoadingPanel(); // Show loading panel while processing the deletion

        SwingWorker<Boolean, Void> worker = new SwingWorker<Boolean, Void>() {
            @Override
            protected Boolean doInBackground() throws Exception {
                Query query = new Query();
                boolean success = query.deleteFriend(SessionManager.getInstance().getCurrentUserId(), username);
                if (success) {
                    // Fetch the updated data and set it in the session manager
                    long userId = SessionManager.getInstance().getCurrentUserId();
                    String currentUserName = SessionManager.getInstance().getCurrentUserName();
                    long[] friendsIds = query.fetchFriendsIdsFromDatabase(userId);
                    SessionManager.getInstance().setFriends(friendsIds);
                    SessionManager.getInstance().setCurrentUserName(currentUserName);
                    SessionManager.getInstance().setCurrentUserId(userId);
                    mainFrame.getFeedPanel().updateContent();

                    updateFriendsList();  // Update the friend list display

                    return true;
                }
                return false;
            }

            @Override
            protected void done() {
                try {
                    boolean success = get();
                    if (success) {
                        //JOptionPane.showMessageDialog(FriendsPanel.this, "Friend deleted successfully!", "Success", JOptionPane.INFORMATION_MESSAGE);
                        mainFrame.showFriendsPanel(); // Refresh friends panel
                    } else {
                        JOptionPane.showMessageDialog(FriendsPanel.this, "Could not delete friend. They may not be your friend, or the username does not exist.", "Error", JOptionPane.ERROR_MESSAGE);
                    }
                } catch (InterruptedException | ExecutionException ex) {
                    ex.printStackTrace();
                    JOptionPane.showMessageDialog(FriendsPanel.this, "Failed to delete friend.", "Error", JOptionPane.ERROR_MESSAGE);
                }
            }
        };
        worker.execute();
    }

    public void updateFriendsList() {
        friendsListPanel.removeAll();  // Clear existing labels
        long[] friendIds = SessionManager.getInstance().getFriends();  // Get friend IDs from SessionManager

        if (friendIds != null && friendIds.length > 0) {
            Query query = new Query();

            JLabel titleLabel = new JLabel("Current Friends:");
            styleLabel(titleLabel);
            friendsListPanel.add(titleLabel);

            for (long friendId : friendIds) {
                String friendName = query.fetchUserNameFromDatabase(friendId);  // Fetch each friend's name using their ID
                if (friendName != null && !friendName.isEmpty()) {
                    System.out.println(friendName);
                    JLabel friendLabel = new JLabel(friendName);  // Create a label for each friend's name
                    styleFriendNameLabel(friendLabel);  // Apply styling
                    friendsListPanel.add(friendLabel);  // Add the label to the panel
                } else {
                    System.out.println("No name found for friend ID: " + friendId);
                }
            }
        } else {
            System.out.println("Friend list is empty or not found");
        }

        friendsListPanel.revalidate();  // Refresh panel to display updated list
        friendsListPanel.repaint();
    }

    private void styleButtonBack(JButton button){
        button.setOpaque(true);
        button.setPreferredSize(new Dimension(120, 45));
        button.setForeground(Color.WHITE);
        button.setBackground(Color.BLACK);
        button.setFont(new Font("JetBrains Mono", Font.BOLD, 20));
    }

    private void stylePanel(JPanel panel){
        panel.setBackground(Color.black);
    }

    private void styleLabel(JLabel label){
        label.setForeground(Color.white);
        label.setFont(new Font("JetBrains Mono", Font.BOLD, 30));
    }

    private void styleFriendNameLabel(JLabel label){
        label.setForeground(Color.white);
        label.setFont(new Font("JetBrains Mono", Font.BOLD, 20));
    }

    private void styleText(JTextField text){
        text.setPreferredSize(new Dimension(100, 35));
        text.setFont(new Font("JetBrains Mono", Font.PLAIN, 22));
    }

    private void styleButton(JButton button){
        button.setPreferredSize(new Dimension(200, 50));
        button.setFont(new Font("JetBrains Mono", Font.PLAIN, 22));
    }
}