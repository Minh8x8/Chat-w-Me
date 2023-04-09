package db;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class DBConnection {
    public static Connection getConnection() throws SQLException {
        Connection conn = null;
        try {
            // Register the MySQL JDBC driver with DriverManager
            Class.forName("com.mysql.cj.jdbc.Driver");

            String url = "jdbc:mysql://localhost:3306/chatwme";
            String username = "root";
            String password = "";

            // Create connection
            conn = DriverManager.getConnection(url, username, password);
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return conn;
    }

    public static void closeConnection(Connection conn) throws SQLException {
        if (conn != null) {
            conn.close();
        }
    }
}
