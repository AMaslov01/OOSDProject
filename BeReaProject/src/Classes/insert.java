import java.sql.*;

public class insert {
    public static void main(String[] args) {
        // Connection parameters
        String url = "jdbc:mysql://37.27.34.21:3306/BeReal"; // JDBC URL for the MySQL database
        String user = "root"; // Username for database authentication
        String password = "xyrbib-1gitvY-ruvkok"; // Password for database authentication
        Connection connection = null; // Connection object to establish a connection with the database
        PreparedStatement pstat = null; // PreparedStatement object for executing parameterized SQL queries
        String username = "user"; // Username to be inserted into the database
        int i = 0; // Variable to store the result of the SQL operation

        try {
            // Establish connection to the database
            connection = DriverManager.getConnection(url, user, password);

            // Prepare the SQL statement with a placeholder for username
            pstat = connection.prepareStatement("INSERT INTO User(username) VALUES (?)");

            // Set the value for the placeholder in the SQL statement
            pstat.setString(1, username);

            // Execute the SQL statement and get the number of rows affected
            i = pstat.executeUpdate();

            // Print the number of records successfully added to the table
            System.out.println(i + " record successfully added to the table.");
        } catch(SQLException sqlException) {
            // Handle SQLException by printing the stack trace
            sqlException.printStackTrace();
        } finally {
            // Close the PreparedStatement and Connection objects in a finally block to ensure resources are released
            try {
                if (pstat != null) {
                    pstat.close();
                }
                if (connection != null) {
                    connection.close();
                }
            } catch (Exception exception) {
                // Handle any exceptions that occur while closing resources by printing the stack trace
                exception.printStackTrace();
            }
        }
    }
}

