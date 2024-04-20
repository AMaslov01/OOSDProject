package newGUI;

import java.io.File;

public class SessionManager {
    private String currentUserName;
    private File currentFile;
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

    public File getCurrentFile() {
        return currentFile;
    }

    public void setCurrentFile(File file) {
        currentFile = file;
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

    public long getBeRealId(){ return beRealId; }

    public static SessionManager getInstance() {
        return instance;
    }
}