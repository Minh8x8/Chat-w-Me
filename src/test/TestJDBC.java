package test;

import db.DBConnection;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

public class TestJDBC {
    public static void main(String[] args) {
        Connection connection = null;
        try {
            // 1. Create connection
            connection = DBConnection.getConnection();
            // 2. Create statment
            Statement statement = connection.createStatement();
            // 3. SQL query
            String sql = "SELECT * FROM `detail` WHERE `chat_id` = 1;";
            // 4.
            ResultSet resultSet = statement.executeQuery(sql);
            List<String> messages = new ArrayList<>();
            while (resultSet.next()) {
                String message = resultSet.getString("message");
                messages.add(message);
            }
            for (String m : messages) {
                System.out.println(m);
            }
            // 5. Close connection
            DBConnection.closeConnection(connection);
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }
}
