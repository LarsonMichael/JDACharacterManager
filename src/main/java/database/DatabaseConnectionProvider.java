package database;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class DatabaseConnectionProvider {
    private Connection databaseConnection;

    public Connection get() {
            try {
                databaseConnection = null;
                databaseConnection = DriverManager.getConnection("jdbc:postgresql://localhost/postgres", System.getenv("DATABASE_USER"), System.getenv("DATABASE_PASSWORD"));
                System.out.println("Database connection successful");
            } catch (SQLException e) {
                System.out.println(e.getMessage());
            }
        return databaseConnection;
    }
}
