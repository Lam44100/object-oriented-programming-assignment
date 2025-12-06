package core;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class DatabaseConnection {
    // UPDATE THESE WITH YOUR DATABASE CONFIGURATION
    private static final String URL = "jdbc:mysql://localhost:3306/library_db";
    private static final String USER = "root"; // Your MySQL username
    private static final String PASS = "Lwc4932!"; // Your MySQL password

    public static Connection getConnection() {
        try {
            return DriverManager.getConnection(URL, USER, PASS);
        } catch (SQLException e) {
            System.out.println("Error: Unable to connect to database.");
            e.printStackTrace();
            return null;
        }
    }
}