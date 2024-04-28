package CRUD;

import javax.imageio.ImageIO;
import java.awt.*;
import java.io.IOException;
import java.io.InputStream;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

/**
 * Handles all database interaction for the whole application, including user authentication, registration, and data retrieval.
 * Provides methods for executing SQL commands and managing user data, such as images and comments.
 */
public class Query {
    // Fields
    String url = "jdbc:mysql://37.27.34.21:3306/BeReal"; // JDBC URL for the MySQL database
    String user = "root"; // Username for database authentication
    String password = "xyrbib-1gitvY-ruvkok"; // Password for database authentication

    /**
     * Establishes a database connection using configured credentials.
     * @return A new Connection object to the database.
     * @throws SQLException if a database access error occurs or the url is null.
     */
    private Connection getConnection() throws SQLException {
        return DriverManager.getConnection(url, user, password);
    }

    /**
     * Executes a SQL command that does not return a result set.
     * @param sql The SQL statement to execute.
     * @throws SQLException if a database access error occurs.
     */
    public void execute(String sql) throws SQLException {
        try (Connection connection = getConnection();
             PreparedStatement pstat = connection.prepareStatement(sql)) {
            int result = pstat.executeUpdate();
            System.out.println(result + " record(s) successfully added/modified.");
        }
    }

    /**
     * Retrieves data from the database.
     * @param sql The SQL query to execute.
     * @return A 2D Object array containing the data retrieved from the database.
     * @throws SQLException if a database access error occurs.
     */
    public Object[][] retrieve(String sql) throws SQLException {
        try (Connection connection = getConnection();
             Statement statement = connection.createStatement(ResultSet.TYPE_SCROLL_INSENSITIVE, ResultSet.CONCUR_READ_ONLY);
             ResultSet resultSet = statement.executeQuery(sql)) {
            int columns = resultSet.getMetaData().getColumnCount();
            resultSet.last();
            int rows = resultSet.getRow();
            resultSet.beforeFirst();
            Object[][] res = new Object[rows][columns];
            int i = 0;
            while (resultSet.next()) {
                for (int j = 1; j <= columns; j++) {
                    res[i][j - 1] = resultSet.getObject(j);
                }
                i++;
            }
            return res;
        }
    }

    /**
     * Inserts a blob (binary large object) into the database.
     * @param data The byte array containing the blob data to be inserted.
     * @throws SQLException if a database access error occurs.
     */
    public void blobExecute(byte[] data) throws SQLException {
        String sql = "INSERT INTO `Image`(`image`) VALUES (?)";
        try (Connection connection = getConnection();
             PreparedStatement pstat = connection.prepareStatement(sql)) {
            pstat.setBytes(1, data);
            int result = pstat.executeUpdate();
            System.out.println(result + " blob record successfully added to the table.");
        }
    }

    /**
     * Retrieves the latest image ID from the database.
     * @return The maximum image ID found in the database.
     */
    public long fetchUserImageId() {
        String sql = "SELECT MAX(`imageID`) FROM `Image`";
        long imageId = 0;
        try {
            Connection connection = getConnection();
            Statement statement = connection.createStatement();
            ResultSet resultSet = statement.executeQuery(sql);
            if(resultSet.next()){
                imageId = resultSet.getLong(1);
            }
            return imageId;
        } catch (SQLException e) {
            e.printStackTrace();
            System.out.println("Error retrieving user's imageID: " + e.getMessage());
        }
        return imageId;
    }

    /**
     * Retrieves an image from the database for a specific user.
     * @param userID The ID of the user whose image is to be fetched.
     * @return The Image object retrieved from the database.
     * @throws SQLException if a database access error occurs.
     */
    public Image fetchUserImage(long userID) throws SQLException {
        try {
            Connection connection = getConnection();
             String sql = "SELECT i.image FROM Image i JOIN BeReal b ON i.imageID = b.imageID WHERE b.userID = " + userID
                     + " AND b.berealID = (SELECT MAX(b.berealID) FROM BeReal b WHERE b.userID = " + userID + ")";
             PreparedStatement pstmt = connection.prepareStatement(sql);
             ResultSet resultSet = pstmt.executeQuery();
            if (resultSet.next()) {
                Blob blob = resultSet.getBlob(1);
                if (blob != null) {
                    try (InputStream inputStream = blob.getBinaryStream()) {
                        return ImageIO.read(inputStream);
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
            } else {
                System.out.println("No data found.");
                return null;
            }
        } catch (SQLException e){
            e.printStackTrace();
        }
        return null;
    }

    /**
     * Fetches a user ID from the database based on a provided username.
     * @param userName The username for which the user ID needs to be fetched.
     * @return The user ID associated with the given username.
     */
    public long fetchUserIdFromDatabase(String userName) {
        String sql = "SELECT `userID` FROM `User` WHERE `username` = ?";
        try (Connection connection = DriverManager.getConnection(url, user, password);
             PreparedStatement pstmt = connection.prepareStatement(sql)) {
            pstmt.setString(1, userName);
            ResultSet rs = pstmt.executeQuery();
            if (rs.next()) {
                return rs.getLong("userID");
            }
            rs.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return -1; // Return -1 or throw an exception if user not found or error occurs
    }

    /**
     * Retrieves a list of friend IDs from the database for a given user.
     * @param userId The user ID for whom to fetch the friend IDs.
     * @return An array of long integers representing the IDs of friends.
     */
    public long[] fetchFriendsIdsFromDatabase(long userId) {
        String sql = "SELECT `UserID2` FROM `User_to_User` WHERE `UserID1` = ?";
        List<Long> friendsList = new ArrayList<>();
        try (Connection connection = DriverManager.getConnection(url, user, password);
             PreparedStatement pstmt = connection.prepareStatement(sql)) {
            pstmt.setLong(1, userId);
            ResultSet rs = pstmt.executeQuery();
            while (rs.next()) {
                friendsList.add(rs.getLong("userID2"));
            }
            rs.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return friendsList.stream().mapToLong(l -> l).toArray();
    }

    /**
     * Fetches the latest image for a friend from the database.
     * @param userId The ID of the user whose friend's image is to be fetched.
     * @return The Image object retrieved from the database.
     */
    public Image fetchFriendImage(long userId) {
        String sql = "SELECT i.image FROM Image i " +
                "JOIN BeReal b ON i.imageID = b.imageID " +
                "WHERE b.userID = ? AND " +
                "b.beRealID = (SELECT MAX(beRealID) FROM BeReal WHERE userID = ?);";
        try (Connection connection = getConnection();
             PreparedStatement pstmt = connection.prepareStatement(sql)) {
            pstmt.setLong(1, userId);
            pstmt.setLong(2, userId);
            ResultSet resultSet = pstmt.executeQuery();
            if (resultSet.next()) {
                Blob blob = resultSet.getBlob(1);
                if (blob != null) {
                    try (InputStream inputStream = blob.getBinaryStream()) {
                        return ImageIO.read(inputStream);
                    }
                }
            }
        } catch (SQLException | IOException e) {
            e.printStackTrace();
            System.out.println("Error retrieving friend's image: " + e.getMessage());
        }
        return null;
    }

    /**
     * Fetches comments associated with a specific BeReal post.
     * @param beRealId The BeReal ID for which comments are fetched.
     * @return A 2D String array containing usernames and their respective comments.
     */
    public String[][] fetchUserComments(long beRealId) {
        String sql = "SELECT u.username, c.text FROM Comment c JOIN User u ON c.userID = u.userID WHERE c.berealID = " + beRealId;
        try {
            Connection connection = getConnection();
            Statement statement = connection.createStatement(ResultSet.TYPE_SCROLL_INSENSITIVE, ResultSet.CONCUR_READ_ONLY);
            ResultSet resultSet = statement.executeQuery(sql);
            resultSet.last();
            int rows = resultSet.getRow();
            System.out.println("rows: " + rows);
            resultSet.beforeFirst();
            String[][] comments = new String[rows][2];
            int i = 0;
            while (resultSet.next()) {
                for (int j = 1; j <= 2; j++) {
                    comments[i][j - 1] = resultSet.getString(j);
                }
                i++;
            }
            return comments;

        } catch (SQLException e) {
            e.printStackTrace();
            System.out.println("Error retrieving friend's comments: " + e.getMessage());
        }
        return null;
    }

    /**
     * Inserts a comment into the database.
     * @param commentText The text of the comment to insert.
     * @param userID The ID of the user posting the comment.
     * @param beRealId The ID of the BeReal post to which the comment is associated.
     */
    public void executeComment(String commentText, long userID, long beRealId) {
        String sql = "INSERT INTO `Comment`( `text`, `userID`, `berealID`) VALUES ('" + commentText + "','" + userID + "', '" + beRealId + "')";
        try (Connection connection = getConnection();
             PreparedStatement pstat = connection.prepareStatement(sql)) {
            int result = pstat.executeUpdate();
            System.out.println(result + " record(s) successfully added.");
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    /**
     * Inserts a new BeReal post into the database.
     * @param imageID The ID of the image associated with the BeReal post.
     * @param userID The ID of the user creating the BeReal post.
     */
    public void executeBeReal(long imageID, long userID) {
        String sql = "INSERT INTO `BeReal`(`imageID`, `userID`) VALUES (" + imageID + "," + userID + ")";
        try (Connection connection = getConnection();
             PreparedStatement pstat = connection.prepareStatement(sql)) {
            int result = pstat.executeUpdate();
            System.out.println(result + " record(s) successfully added.");
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    /**
     * Fetches the latest BeReal ID for a user.
     * @param userId The user ID for whom the latest BeReal ID is fetched.
     * @return The latest BeReal ID associated with the user.
     */
    public long fetchUserBeRealId(long userId) {
        String sql = "SELECT MAX(`berealID`) FROM `BeReal` WHERE userID = " + userId;
        long beRealId = 0;
        try {
            Connection connection = getConnection();
            Statement statement = connection.createStatement();
            ResultSet resultSet = statement.executeQuery(sql);
            if(resultSet.next()){
                beRealId = resultSet.getLong(1);
            }
            return beRealId;
        } catch (SQLException e) {
            e.printStackTrace();
            System.out.println("Error retrieving user's berealID: " + e.getMessage());
        }
        return beRealId;
    }

    /**
     * Fetches the username for a given user ID.
     * @param userId The user ID for which the username is fetched.
     * @return The username associated with the given user ID.
     */
    public String fetchUserNameFromDatabase(long userId) {
        String sql = "SELECT `username` FROM `User` WHERE `userID` = ?";
        try (Connection connection = getConnection();
             PreparedStatement pstmt = connection.prepareStatement(sql)) {
            pstmt.setLong(1, userId);
            ResultSet rs = pstmt.executeQuery();
            if (rs.next()) {
                return rs.getString("username");
            }
            rs.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null; // Return null or throw an exception if user not found or error occurs
    }

    /**
     * Authenticates a user based on the provided username and password.
     * @param username The username to authenticate.
     * @param password The password to authenticate against.
     * @return true if the authentication is successful, false otherwise.
     * @throws SQLException if a database access error occurs.
     */
    public boolean authenticateUser(String username, String password) throws SQLException {
        String sql = "SELECT `userID` FROM `User` WHERE `username` = ? AND `password` = ?";
        try (Connection conn = getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setString(1, username);
            pstmt.setString(2, password);
            ResultSet rs = pstmt.executeQuery();
            return rs.next(); // Return true if a row exists (i.e., user found)
        }
    }

    /**
     * Registers a new user in the database.
     * @param username The username for the new user.
     * @param password The password for the new user.
     * @return true if the registration is successful, false if the username already exists.
     * @throws SQLException if a database access error occurs.
     */
    public boolean registerUser(String username, String password) throws SQLException {
        String sql1 = "SELECT * FROM `User` WHERE `username` = ?";
        try (Connection conn = getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql1)) {
            pstmt.setString(1, username);
            ResultSet rs = pstmt.executeQuery();
            if (rs.next()) {
                return false; // Username already exists
            }
        }

        String sql2 = "INSERT INTO User(username, password) VALUES(?, ?)";
        try (Connection conn = getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql2)) {
            pstmt.setString(1, username);
            pstmt.setString(2, password);
            int rowsAffected = pstmt.executeUpdate();
            return rowsAffected > 0;
        }
    }

    /**
     * Adds a friend for a given user.
     * @param userId The user ID who is adding a friend.
     * @param friendUsername The username of the friend to add.
     * @return true if the friend is added successfully, false if the friend does not exist or they are already friends.
     * @throws SQLException if a database access error occurs.
     */
    public boolean addFriend(long userId, String friendUsername) throws SQLException {
        long friendId = fetchUserIdFromDatabase(friendUsername);
        if (friendId == -1) {
            return false; // Friend username does not exist
        }

        if (alreadyFriends(userId, friendId)) {
            return false; // They are already friends
        }

        String sql = "INSERT INTO User_to_User (UserID1, UserID2) VALUES (?, ?)";
        try (Connection conn = getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setLong(1, userId);
            pstmt.setLong(2, friendId);
            int rowsAffected = pstmt.executeUpdate();
            pstmt.setLong(2, userId);
            pstmt.setLong(1, friendId);
            rowsAffected = pstmt.executeUpdate();
            return rowsAffected > 0;
        }
    }

    /**
     * Checks if already friends.
     * @param userId The user ID who is deleting a friend.
     * @param friendId The id of the friend to delete.
     * @return true if the friend is deleted successfully, false if the friend does not exist or they are not friends.
     * @throws SQLException if a database access error occurs.
     */
    private boolean alreadyFriends(long userId, long friendId) throws SQLException {
        String sql = "SELECT * FROM User_to_User WHERE (UserID1 = ? AND UserID2 = ?) OR (UserID1 = ? AND UserID2 = ?)";
        try (Connection conn = getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setLong(1, userId);
            pstmt.setLong(2, friendId);
            pstmt.setLong(3, friendId);
            pstmt.setLong(4, userId);
            ResultSet rs = pstmt.executeQuery();
            return rs.next();
        }
    }

    /**
     * Deletes a friend for a given user.
     * @param userId The user ID who is deleting a friend.
     * @param friendUsername The username of the friend to delete.
     * @return true if the friend is deleted successfully, false if the friend does not exist or they are not friends.
     * @throws SQLException if a database access error occurs.
     */
    public boolean deleteFriend(long userId, String friendUsername) throws SQLException {
        long friendId = fetchUserIdFromDatabase(friendUsername);
        if (friendId == -1 || !alreadyFriends(userId, friendId)) {
            return false; // Friend username does not exist or they are not friends
        }

        String sql = "DELETE FROM User_to_User WHERE (UserID1 = ? AND UserID2 = ?) OR (UserID1 = ? AND UserID2 = ?)";
        try (Connection conn = getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setLong(1, userId);
            pstmt.setLong(2, friendId);
            pstmt.setLong(3, friendId);
            pstmt.setLong(4, userId);
            int rowsAffected = pstmt.executeUpdate();
            return rowsAffected > 0;
        }
    }

    /**
     * Constructor for the Query class.
     */
    public Query() {}
}