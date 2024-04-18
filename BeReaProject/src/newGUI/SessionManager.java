package newGUI;

import java.io.File;

public class SessionManager {
    private static String currentUserName;
    private static File currentFile;
    private static long currentUserId;
    private static long[] friends;

    public static String getCurrentUserName() {
        return currentUserName;
    }

    public static void setCurrentUserName(String userName) {
        currentUserName = userName;
    }

    public static File getCurrentFile() {
        return currentFile;
    }

    public static void setCurrentFile(File file) {
        currentFile = file;
    }

    public static long getCurrentUserId() {
        return currentUserId;
    }

    public static void setCurrentUserId(long userId) {
        currentUserId = userId;
    }

    public static long[] getFriends() {
        return friends;
    }

    public static void setFriends(long[] friendsList) {
        friends = friendsList;
    }
}