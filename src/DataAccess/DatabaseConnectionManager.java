package DataAccess;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class DatabaseConnectionManager {

    private static final String DB_URL = "jdbc:postgresql://localhost:5432/Assignment";
    private static final String DB_USER = "postgres";
    private static final String DB_PASSWORD = "123456";

    private static Connection connection = null;

    // Private constructor to prevent instantiation
    private DatabaseConnectionManager() { }

    public static Connection getConnection() {
        if (connection == null) {
            try {
                connection = DriverManager.getConnection(DB_URL, DB_USER, DB_PASSWORD);
               
            } catch (SQLException e) {
                System.out.println("\u001B[31mFailed to connect to database!\u001B[0m");
                e.printStackTrace();
            }
        }
        return connection;
    }

    public static void closeConnection() {
        if (connection != null) {
            try {
                connection.close();
                connection = null;
                System.out.println("Database connection closed.");
            } catch (SQLException e) {
                System.out.println("\u001B[31mFailed to close database connection!\u001B[0m");
                e.printStackTrace();
            }
        }
    }
}
