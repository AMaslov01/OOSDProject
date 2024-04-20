package newGUI;

import javax.swing.*;
import java.awt.*;
import java.io.File;


public class MainFrame extends JFrame {
    private CardLayout cardLayout;
    private JPanel cardPanel;
    private LogInPanel loginPanel;
    private RegisterPanel registerPanel;
    private PostPanel postPanel;
    private FeedPanel feedPanel;
    private LoadingPanel loadingPanel;
    private Query query = new Query();

    public MainFrame() {
        super("BeReal");
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setSize(500, 888);

        cardLayout = new CardLayout();
        cardPanel = new JPanel(cardLayout);
        add(cardPanel);

        // Initialize panels
        loginPanel = new LogInPanel(this);
        registerPanel = new RegisterPanel(this);
        postPanel = new PostPanel(this);
        feedPanel = new FeedPanel(this);  // Initialize without user data
        loadingPanel = new LoadingPanel(this);

        cardPanel.add(loginPanel, "Login");
        cardPanel.add(registerPanel, "Register");
        cardPanel.add(postPanel, "Post");
        cardPanel.add(feedPanel, "Feed");
        cardPanel.add(loadingPanel, "Loading");

        setResizable(false);
        setLocationRelativeTo(null);
        setVisible(true);
    }

    public void showLoginPanel() {
        loginPanel.clearTextFields(); // Clear text fields when returning to the panel
        cardLayout.show(cardPanel, "Login");
    }

    public void showRegisterPanel() {
        cardLayout.show(cardPanel, "Register");
    }

    public void showPostPanel() {
        cardLayout.show(cardPanel, "Post");
    }

    public void showFeedPanel(File file) {
        cardLayout.show(cardPanel, "Feed");
    }

    public void showLoadingPanel() {
        cardLayout.show(cardPanel, "Loading");
    }

    // Update session when user logs in
    public void updateUserSession(String userName) {
        long userId = query.fetchUserIdFromDatabase(userName);
        SessionManager.getInstance().setCurrentUserName(userName);

        SessionManager.getInstance().setCurrentUserId(userId);
        long[] friendsIds = query.fetchFriendsIdsFromDatabase(userId);
        SessionManager.getInstance().setFriends(friendsIds);

        // Now update UI components
        postPanel.updateUserName();
        feedPanel.updateContent(); // TODO: Later to check for existing BeReal
        postPanel.setUserName(userName);
    }

    public FeedPanel getFeedPanel() {
        return feedPanel;
    }
}