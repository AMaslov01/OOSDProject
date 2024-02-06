import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.Statement;

public class Connect {
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

            // Close resources

            statement.close();
            connection.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}