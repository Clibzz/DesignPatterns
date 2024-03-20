package nhlstenden.bookandsales.util;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class DatabaseUtil {

    private static Connection connection;

    public DatabaseUtil() {
        connection = null;
    }

    public static Connection getConnection() throws SQLException {
        if (connection != null) {
            return connection;
        } else {
            String driver = "com.mysql.cj.jdbc.Driver";
            String url = "jdbc:mysql://localhost:3306/book-sales";
            String user = "root";
            String password = "root";

            try {
                Class.forName(driver);
                connection = DriverManager.getConnection(url, user, password);
            } catch (ClassNotFoundException e) {
                e.getMessage();
            }
        }

        return connection;
    }
}