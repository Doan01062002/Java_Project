package ra.edu.business.config;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class DatabaseConnection {

    private static final String DB_URL = "jdbc:mysql://localhost:3306/course_management_system"; // Replace 'your_database_name' with the actual database name
    private static final String DB_USER = "root";
    private static final String DB_PASSWORD = "01062002";

    public static Connection getConnection() {
        Connection connection = null;
        try {
            connection = DriverManager.getConnection(DB_URL, DB_USER, DB_PASSWORD);
            System.out.println("Kết nối database thành công!");
        } catch (SQLException e) {
            System.err.println("kết nối thất bại: " + e.getMessage());
        }
        return connection;
    }

    public static void closeConnection(Connection connection) {
        if (connection != null) {
            try {
                connection.close();
                System.out.println("Đóng kêt nối database thành công!");
            } catch (SQLException e) {
                System.err.println("Đóng kết nối thất bại: " + e.getMessage());
            }
        }
    }
}