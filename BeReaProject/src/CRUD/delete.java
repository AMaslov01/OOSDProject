package CRUD;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.SQLException;

// This is an example of an update class
public class delete {
    public static void main(String[] args) {
        String url = "jdbc:mysql://37.27.34.21:3306/BeReal";
        String user = "root";
        String password = "xyrbib-1gitvY-ruvkok";

        Connection connection = null;
        PreparedStatement pstat = null;
        int i = 0;
        int userID = 6;

        try {
            // Establish connection
            connection = DriverManager.getConnection(url, user, password);

            // Create Prepared Statement for updating data in the table
            pstat = connection.prepareStatement("DELETE from User WHERE userID=?");
            pstat.setInt(1, userID);

            // Delete data from the table
            i = pstat.executeUpdate();
            System.out. println(i + " record successfully removed from the table");

        } catch (SQLException sqlException) {
            sqlException.printStackTrace();
        } finally {
            try {
                assert pstat != null;
                pstat.close();
                connection.close();
            } catch (Exception exception) {
                exception.printStackTrace();
            }
        }
    }
}