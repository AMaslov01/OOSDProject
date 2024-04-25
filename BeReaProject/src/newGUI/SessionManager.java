package newGUI;

import java.awt.*;

public class SessionManager {
    private String currentUserName;
    private Image currentImage;
    private long beRealId;
    private long currentUserId;
    private long[] friends;
    private static final SessionManager instance = new SessionManager();

    public String getCurrentUserName() {
        return currentUserName;
    }

    public void setCurrentUserName(String userName) {
        currentUserName = userName;
    }

    public Image getCurrentImage() {
        return currentImage;
    }

    public void setCurrentImage(Image currentImage) {
        this.currentImage = currentImage;
    }

    public long getCurrentUserId() {
        return currentUserId;
    }

    public void setCurrentUserId(long userId) {
        currentUserId = userId;
    }

    public long[] getFriends() {
        return friends;
    }

    public void setFriends(long[] friendsList) {
        friends = friendsList;
    }

    public void setCurrentBeRealId(long beRealId){ this.beRealId = beRealId;}

    public long getCurrentBeRealId(){ return beRealId; }

    public static SessionManager getInstance() {
        return instance;
    }
}