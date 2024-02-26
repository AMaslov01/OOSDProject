package CRUD;

import java.sql.*;

// This is an example of an update class
public class update {
    public static void main(String[] args) {
        String url = "jdbc:mysql://37.27.34.21:3306/BeReal";
        String user = "root";
        String password = "xyrbib-1gitvY-ruvkok";

        String firstname = "Evgenii";
        String lastname = "Salnikov";
        Connection connection = null;
        PreparedStatement pstat = null;
        int i = 0;

        try {
            // Establish connection
            connection = DriverManager.getConnection(url, user, password);

            // Create Prepared Statement for updating data in the table
            pstat = connection.prepareStatement("Update Authors SET LastName=? Where FirstName=?");
            pstat.setString(1, lastname);
            pstat.setString(2, firstname);

            // Update data in the table
            i = pstat.executeUpdate();
            System.out.println(i + " record successfully updated in the table");

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