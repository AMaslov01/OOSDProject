package CRUD;

import java.sql.*;

public class Query {
    String url = "jdbc:mysql://37.27.34.21:3306/BeReal"; // JDBC URL for the MySQL database
    String user = "root"; // Username for database authentication
    String password = "xyrbib-1gitvY-ruvkok"; // Password for database authentication
    public Query(){}
    public void execute(String sql){
        Connection connection = null; // Connection object to establish a connection with the database
        PreparedStatement pstat = null; // PreparedStatement object for executing parameterized SQL queries
        int i = 0;
        try {
            // Establish connection to the database
            connection = DriverManager.getConnection(url, user, password);

            // Prepare the SQL statement with a placeholder for username
            pstat = connection.prepareStatement(sql);

            // Execute the SQL statement and get the number of rows affected
            i = pstat.executeUpdate();

            // Print the number of records successfully added to the table
            System.out.println(i + " record successfully added to the table.");
        } catch(SQLException | RuntimeException e) {
            // Handle SQLException by printing the stack trace
            e.printStackTrace();
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
    public Object[][] retrieve(String sql){


        try {
            // Establish connection
            Connection connection = DriverManager.getConnection(url, user, password);

            // Create a statement
            Statement statement = connection.createStatement(ResultSet.TYPE_SCROLL_INSENSITIVE, ResultSet.CONCUR_READ_ONLY);

            // Select the database
            String databaseName = "BeReal";
            statement.execute("USE " + databaseName);

            // Execute a SELECT query

            ResultSet resultSet = statement.executeQuery(sql);
            int columns = resultSet.getMetaData().getColumnCount();
            int strings = 0;
            while (resultSet.next()){
                strings++;
            }
            resultSet.beforeFirst();

            Object[][] res = new Object[strings][columns];
            int i = 0;
            // Process the result set

            while (resultSet.next()) {
                // Access data from the result set
                for(int j = 1; j <= columns; j++){
                    Object value = resultSet.getObject(j);
                    res[i][j-1] = value;
                }
                i++;
            }
            // Close resources
            resultSet.close();
            statement.close();
            connection.close();
            return res;
        } catch (SQLException | RuntimeException e) {
            e.printStackTrace();
            Object[][] res = null;
            return res;
        }

    }
    public Blob blobRetrieve(String sql){


        try {
            // Establish connection
            Connection connection = DriverManager.getConnection(url, user, password);

            // Create a statement
            Statement statement = connection.createStatement(ResultSet.TYPE_SCROLL_INSENSITIVE, ResultSet.CONCUR_READ_ONLY);

            // Select the database
            String databaseName = "BeReal";
            statement.execute("USE " + databaseName);
            Blob res = null;
            // Execute a SELECT query

            ResultSet resultSet = statement.executeQuery(sql);
            resultSet.beforeFirst();
            if(resultSet.next()){
                res = resultSet.getBlob(1);
            }
            else{
                System.out.println("blob is null");
            }


            // Close resources
            resultSet.close();
            statement.close();
            connection.close();
            return res;
        } catch (SQLException | RuntimeException e) {
            e.printStackTrace();
            Blob res = null;
            return res;
        }

    }
    public void blobExecute(String sql, byte[] arr) {
        Connection connection = null;
        PreparedStatement pstat = null;
        int i;
        try {
            connection = DriverManager.getConnection(url, user, password);

            // Prepare the SQL statement with a placeholder for username
            pstat = connection.prepareStatement(sql);
            pstat.setBytes(1, arr);

            // Execute the SQL statement and get the number of rows affected
            i = pstat.executeUpdate();

            // Print the number of records successfully added to the table
            System.out.println(i + " record successfully added to the table.");
        } catch (SQLException | RuntimeException e) {
            // Handle SQLException by printing the stack trace
            e.printStackTrace();
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
