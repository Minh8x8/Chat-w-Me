package db;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

public class GetMessage {
    public List<String> getMessage(int chat_id) {
        Connection connection = null;
        try {
            // 1. Create connection
            connection = DBConnection.getConnection();
            // 2. Create statment
            Statement statement = connection.createStatement();
            // 3. SQL query
            String sql = "SELECT * FROM `detail` WHERE `chat_id` = " + chat_id + ";";
            // 4.
            ResultSet resultSet = statement.executeQuery(sql);
            List<String> messages = new ArrayList<>();
            while (resultSet.next()) {
                String message = resultSet.getString("message");
                messages.add(message);
            }
            // 5. Close connection
            DBConnection.closeConnection(connection);
            return messages;
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }
}
