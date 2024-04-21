package newGUI;

import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.*;
import java.io.IOException;
import java.io.InputStream;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class Query {
    String url = "jdbc:mysql://37.27.34.21:3306/BeReal"; // JDBC URL for the MySQL database
    String user = "root"; // Username for database authentication
    String password = "xyrbib-1gitvY-ruvkok"; // Password for database authentication

    // Shared method to get a DB connection
    private Connection getConnection() throws SQLException {
        return DriverManager.getConnection(url, user, password);
    }

    // Execute non-return SQL commands
    public void execute(String sql) throws SQLException {
        try (Connection connection = getConnection();
             PreparedStatement pstat = connection.prepareStatement(sql)) {
            int result = pstat.executeUpdate();
            System.out.println(result + " record(s) successfully added/modified.");
        }
    }

    // Retrieve data from the database
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

    // Executing non-return blob update
    public void blobExecute(String sql, byte[] data) throws SQLException {
        try (Connection connection = getConnection();
             PreparedStatement pstat = connection.prepareStatement(sql)) {
            pstat.setBytes(1, data);
            int result = pstat.executeUpdate();
            System.out.println(result + " blob record successfully added to the table.");
        }
    }

    // Retrieve a blob from the database
    public Blob blobRetrieve(String sql) throws SQLException {
        try (Connection connection = getConnection();
             PreparedStatement pstmt = connection.prepareStatement(sql);
             ResultSet resultSet = pstmt.executeQuery()) {
            if (resultSet.next()) {
                return resultSet.getBlob(1);
            } else {
                System.out.println("No data found.");
                return null;
            }
        }
    }

    // Fetching a userID from given username from the DB
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

    // Fetching an array of friends of a specific given userID
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

    // Method for fetching friends images
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

    // Method for returning a name of a certain user/friend
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

    /*
     * Asynchronously authenticate a user.
     * username: The username of the user attempting to log in.
     * password: The password of the user.
     * callback: Callback that handles what happens after login attempt.
     */
    public void authenticateUserAsync(String username, String password, SwingWorker<Boolean, Void> callback) {
        SwingWorker<Boolean, Void> worker = new SwingWorker<>() {
            @Override
            protected Boolean doInBackground() throws Exception {
                return authenticateUser(username, password);
            }

            @Override
            protected void done() {
                callback.run();
            }
        };
        worker.execute();
    }

    /*
     * Synchronously authenticate a user.
     * username: The username.
     * password: The password.
     * return True if authentication is successful, false otherwise.
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

    public Query() {}
}