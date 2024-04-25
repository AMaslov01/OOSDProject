package newGUI;

import CRUD.Query;

import javax.swing.*;
import java.awt.*;
import java.sql.SQLException;

/**
 * Main application window class that manages different panels within a card layout.
 * This class serves as the container for various user interface panels such as login, registration, post, feed, loading, and friends.
 */
public class MainFrame extends JFrame {
    private CardLayout cardLayout;
    private JPanel cardPanel;
    private LogInPanel loginPanel;
    private RegisterPanel registerPanel;
    private PostPanel postPanel;
    private FeedPanel feedPanel;
    private LoadingPanel loadingPanel;
    private FriendsPanel friendsPanel;
    private Query query = new Query();

    /**
     * Constructor for MainFrame.
     * Sets up the application's main window with various panels using a CardLayout to switch between them.
     * Initializes and adds all panels to the card layout.
     */
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
        friendsPanel = new FriendsPanel(this);

        cardPanel.add(loginPanel, "Login");
        cardPanel.add(registerPanel, "Register");
        cardPanel.add(postPanel, "Post");
        cardPanel.add(feedPanel, "Feed");
        cardPanel.add(loadingPanel, "Loading");
        cardPanel.add(friendsPanel, "Friends");

        setResizable(false);
        setLocationRelativeTo(null);
        setVisible(true);
    }

    /**
     * Displays the login panel and clears any text fields.
     */
    public void showLoginPanel() {
        loginPanel.clearTextFields(); // Clear text fields when returning to the panel
        cardLayout.show(cardPanel, "Login");
    }

    /**
     * Displays the register panel.
     */
    public void showRegisterPanel() {
        cardLayout.show(cardPanel, "Register");
    }

    /**
     * Displays the post panel.
     */
    public void showPostPanel() {
        cardLayout.show(cardPanel, "Post");
    }

    /**
     * Displays the feed panel.
     */
    public void showFeedPanel() {
        cardLayout.show(cardPanel, "Feed");
    }

    /**
     * Displays the loading panel.
     */
    public void showLoadingPanel() {
        cardLayout.show(cardPanel, "Loading");
    }

    /**
     * Displays the friends panel.
     */
    public void showFriendsPanel() {
        cardLayout.show(cardPanel, "Friends");
    }

    /**
     * Updates the user session with data fetched from the database after successful login.
     * Updates all necessary data in the session manager and updates related UI components.
     * @param userName The username of the user who is logging in.
     * @throws SQLException If there is a problem with database access.
     */
    public void updateUserSession(String userName) throws SQLException {
        long userId = query.fetchUserIdFromDatabase(userName);
        SessionManager.getInstance().setCurrentUserName(userName);

        SessionManager.getInstance().setCurrentUserId(userId);
        long[] friendsIds = query.fetchFriendsIdsFromDatabase(userId);
        SessionManager.getInstance().setFriends(friendsIds);

        Image image = query.fetchUserImage(userId);
        SessionManager.getInstance().setCurrentImage(image);

        long berealID = query.fetchUserBeRealId(userId);
        SessionManager.getInstance().setCurrentBeRealId(berealID);

        // Update UI components
        postPanel.updateUserName();
        feedPanel.updateContent(); // TODO: Later to check for existing BeReal
        postPanel.setUserName(userName);
    }

    /**
     * Retrieves the FeedPanel instance.
     * @return the feed panel of the application.
     */
    public FeedPanel getFeedPanel() {
        return feedPanel;
    }

    /**
     * Retrieves the FriendsPanel instance.
     * @return the friends panel of the application.
     */
    public FriendsPanel getFriendsPanel() {
        return friendsPanel;
    }
}