package orm.configuration;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.Properties;

public class ProjectConfig {

    private String databaseUrl;
    private String databaseUsername;
    private String databasePassword;
    private String databaseName;
    private String databaseDriver;
    private String ddl;
    private boolean sqlTraceEnable;

    public ProjectConfig() {
        String src = "src\\main\\resources\\application.properties";
        File file = new File(src);
        Properties properties = new Properties();
        try {
            properties.load(new FileInputStream(file));
            this.databaseUrl = String.valueOf(properties.get("com.setting.db_url"));
            this.databaseUsername = String.valueOf(properties.get("com.settings.db_user"));
            this.databasePassword = String.valueOf(properties.get("com.setting.db_pass"));
            this.databaseName = String.valueOf(properties.get("com.setting.db_url"));
            this.databaseDriver = String.valueOf(properties.get("com.setting.db_driver"));
            this.ddl = String.valueOf(properties.get("com.setting.ddl"));
            this.sqlTraceEnable = Boolean.parseBoolean((String) properties.get("com.setting.show_sql"));
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public String getDatabaseUrl() {
        return databaseUrl;
    }

    public String getDatabaseUsername() {
        return databaseUsername;
    }

    public String getDatabasePassword() {
        return databasePassword;
    }

    public String getDatabaseName() {
        return databaseName;
    }

    public String getDatabaseDriver() {
        return databaseDriver;
    }

    public boolean isSqlTraceEnable() {
        return sqlTraceEnable;
    }

    public String getDdl() {
        return ddl;
    }
}
