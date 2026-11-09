package config;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public final class DBConnection {
    // Update these values to match your local MySQL setup.
    private static final String URL =
            "jdbc:mysql://localhost:3306/securebank?useSSL=false&allowPublicKeyRetrieval=true&serverTimezone=UTC";
    private static final String USER = "root";
    // Put your MySQL root password here (or set the SECUREBANK_DB_PASSWORD environment variable).
    private static final String PASSWORD = "root";

    private DBConnection() {}

    public static Connection getConnection() throws SQLException {
        try {
            // Makes a missing driver JAR obvious instead of "No suitable driver found".
            Class.forName("com.mysql.cj.jdbc.Driver");
        } catch (ClassNotFoundException e) {
            throw new SQLException("MySQL Connector/J is not on the classpath. "
                    + "Add mysql-connector-j to the project libraries (see README).", e);
        }
        String env = System.getenv("SECUREBANK_DB_PASSWORD");
        String password = (env == null || env.isBlank()) ? PASSWORD : env;
        return DriverManager.getConnection(URL, USER, password);
    }
}
