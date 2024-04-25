package newGUI;

import java.awt.*;

/**
 * Singleton class that manages session data across the application.
 * Stores user-specific information such as username, user ID, friends list, current image, and BeReal ID.
 */
public class SessionManager {
    private String currentUserName;
    private Image currentImage;
    private long beRealId;
    private long currentUserId;
    private long[] friends;
    private static final SessionManager instance = new SessionManager();

    /**
     * Retrieves the currently logged-in username.
     * @return The current user's username.
     */
    public String getCurrentUserName() {
        return currentUserName;
    }

    /**
     * Sets the currently logged-in username.
     * @param userName The username to be set for the current session.
     */
    public void setCurrentUserName(String userName) {
        currentUserName = userName;
    }

    /**
     * Retrieves the current user's image.
     * @return The current Image object associated with the user.
     */
    public Image getCurrentImage() {
        return currentImage;
    }

    /**
     * Sets the current user's image.
     * @param currentImage The Image to be set for the current session.
     */
    public void setCurrentImage(Image currentImage) {
        this.currentImage = currentImage;
    }

    /**
     * Retrieves the current user's unique ID.
     * @return The unique identifier for the current user.
     */
    public long getCurrentUserId() {
        return currentUserId;
    }

    /**
     * Sets the current user's unique ID.
     * @param userId The unique ID to be set for the current user.
     */
    public void setCurrentUserId(long userId) {
        currentUserId = userId;
    }

    /**
     * Retrieves the current user's friends list.
     * @return An array of user IDs representing the friends of the current user.
     */
    public long[] getFriends() {
        return friends;
    }

    /**
     * Sets the current user's friends list.
     * @param friendsList An array of user IDs to be set as the friends list for the current user.
     */
    public void setFriends(long[] friendsList) {
        friends = friendsList;
    }

    /**
     * Sets the current BeReal ID associated with the user's session.
     * @param beRealId The BeReal ID to be set for the current session.
     */
    public void setCurrentBeRealId(long beRealId){ this.beRealId = beRealId;}

    /**
     * Retrieves the current BeReal ID associated with the user's session.
     * @return The BeReal ID of the current user.
     */
    public long getCurrentBeRealId(){ return beRealId; }

    /**
     * Provides access to the singleton instance of the SessionManager.
     * @return The single instance of SessionManager.
     */
    public static SessionManager getInstance() {
        return instance;
    }
}