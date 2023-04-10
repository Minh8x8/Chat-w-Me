package db;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class DB {
    Connection connection;
    private final String url = "jdbc:mysql://localhost:3306/chatwme";
    private final String username = "root";
    private final String password = "";
    private List<String[]> tableChat;
    private List<String[]> tableDetail = new ArrayList<>();

    public void connectDB() {
        try {
            connection = null;
            // Register the MySQL JDBC driver with DriverManager
            Class.forName("com.mysql.cj.jdbc.Driver");
            // Create connection
            connection = DriverManager.getConnection(url, username, password);
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public void closeConnection() throws SQLException {
        if (connection != null) {
            connection.close();
        }
    }
    public void setDB() {
        try {
            // connect to database
            connectDB();
            // Clear the table if it exists
            if (tableChat == null) {
                tableChat = new ArrayList<>();
            } else {
                tableChat.clear();
            }
            if (tableDetail == null) {
                tableDetail = new ArrayList<>();
            } else {
                tableDetail.clear();
            }
            // Create statement
            Statement statement = connection.createStatement();
            String tableChatSQL = "SELECT * FROM `chat`;";
            String tableDetailSQL = "SELECT * FROM `detail`;";
            // Get db
            ResultSet chatResultSet = statement.executeQuery(tableChatSQL);
            while (chatResultSet.next()) {
                String[] row = new String[2];
                row[0] = chatResultSet.getString("id");
                row[1] = chatResultSet.getString("time");
                tableChat.add(row);
            }
            ResultSet detailResultSet = statement.executeQuery(tableDetailSQL);
            while (detailResultSet.next()) {
                String[] row = new String[3];
                row[0] = detailResultSet.getString("id");
                row[1] = detailResultSet.getString("chat_id");
                row[2] = detailResultSet.getString("message");
                tableDetail.add(row);
            }
            // Close connection
            chatResultSet.close();
            detailResultSet.close();
            statement.close();
            closeConnection();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public List<String[]> getTableChat() {
        return tableChat;
    }

    public List<String[]> getTableDetail() {
        return tableDetail;
    }

    public void insertToTableChat(String time) {
        connectDB();
        String sql = "INSERT INTO `chat`(`time`) VALUES (?)";
        try {
            PreparedStatement statement = connection.prepareStatement(sql);
            statement.setString(1, time);
            statement.executeUpdate();
            statement.close();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        try {
            closeConnection();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        // update the table
        setDB();
    }
    public void insertMessage(String message) {
        connectDB();
        String sql = "INSERT INTO `detail`(`chat_id`, `message`) VALUES (?, ?)";
        int chat_id = tableChat.size();
        try {
            // Insert the new message into the detail table
            PreparedStatement statement = connection.prepareStatement(sql);
            statement.setInt(1, chat_id);
            statement.setString(2, message);
            statement.executeUpdate();
            System.out.println("Saved successfully!");
            statement.close();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        try {
            closeConnection();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        // update the table
        setDB();
    }

}
