package orm.databse;

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

    private DatabaseConnection() {
    }

    private Connection connection;

    private static DatabaseConnection databaseConnection = null;

    public Connection getConnection() throws IOException {
        //Configuration database connection is stored in this file
        String src = "src\\main\\resources\\application.properties";
        File file = new File(src);
        Properties properties = new Properties();
        properties.load(new FileInputStream(file));
        this.dbDriver = properties.getProperty("com.setting.db_driver");
        this.dbPass = properties.getProperty("com.setting.db_pass");
        this.dbUser = properties.getProperty("com.settings.db_user");
        this.dbUrl = properties.getProperty("com.setting.db_url");
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
