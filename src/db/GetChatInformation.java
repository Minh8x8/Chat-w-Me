package db;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

public class GetChatInformation {
    public List<String[]> getChatInformation() {
        Connection connection = null;
        try {
            // 1. Create connection
            connection = DBConnection.getConnection();
            // 2. Create statment
            Statement statement = connection.createStatement();
            // 3. SQL query
            String sql = "SELECT * FROM `chat`;";
            // 4.
            ResultSet resultSet = statement.executeQuery(sql);
            List<String[]> chatList = new ArrayList<>();
            while (resultSet.next()) {
                String[] chat = new String[2];
                chat[0] = resultSet.getString("id");
                chat[1] = resultSet.getString("time");
                chatList.add(chat);
            }
            // 5. Close connection
            resultSet.close();
            DBConnection.closeConnection(connection);
            statement.close();
            return chatList;
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }
}
