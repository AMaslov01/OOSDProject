import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

public class test {
    public static void main(String[] args) {
        // Connection parameters
        String url = "jdbc:mysql://37.27.34.21:3306/BeReal";
        String user = "root";
        String password = "xyrbib-1gitvY-ruvkok";

        try {
            // Establish connection
            Connection connection = DriverManager.getConnection(url, user, password);

            // Create a statement
            Statement statement = connection.createStatement();

            // Select the database
            String databaseName = "BeReal";
            statement.execute("USE " + databaseName);

            // Execute a SELECT query
            String tableName = "User";
            ResultSet resultSet = statement.executeQuery("SELECT * FROM " + tableName);

            // Process the result set
            System.out.println("Contents of " + tableName + " table:");
            while (resultSet.next()) {
                // Access data from the result set
                int id = resultSet.getInt("id");
                String name = resultSet.getString("name");

                // Output the data
                System.out.println("ID: " + id + ", Name: " + name);
            }

            // Close resources
            resultSet.close();
            statement.close();
            connection.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}