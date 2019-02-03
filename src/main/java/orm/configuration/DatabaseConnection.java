package orm.configuration;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.Properties;

/**
 * singleton class to connect to database
 *
 * @author Reza Eskandari
 */
public class DatabaseConnection {
    private String dbDriver;

    private String dbUser;

    private String dbPass;

    private String dbUrl;

    private ProjectConfig projectConfig;

    private DatabaseConnection() {
        this.projectConfig = new ProjectConfig();
    }

    private Connection connection;

    private static DatabaseConnection databaseConnection = null;

    public Connection getConnection() throws IOException {
        this.dbDriver = projectConfig.getDatabaseDriver();
        this.dbPass = projectConfig.getDatabasePassword();
        this.dbUser = projectConfig.getDatabaseUsername();
        this.dbUrl = projectConfig.getDatabaseUrl();
        try {
            connection = DriverManager.getConnection(this.dbUrl, this.dbUser, this.dbPass);
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return connection;
    }

    /**
     * @return DatabaseConnection singleton instance
     * @singleton
     */
    public static DatabaseConnection getNewInstance() {
        if (databaseConnection == null) {
            synchronized (DatabaseConnection.class) {
                databaseConnection = new DatabaseConnection();
            }
        }
        return databaseConnection;
    }
}
