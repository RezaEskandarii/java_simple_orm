package orm.session;

import orm.annotations.Column;
import orm.annotations.Id;
import orm.annotations.Table;
import orm.configuration.DatabaseConnection;
import orm.configuration.ProjectConfig;
import orm.interfaces.TableCreatorInterface;
import orm.utils.QueryUtil;

import java.io.IOException;
import java.lang.annotation.Annotation;
import java.lang.reflect.Field;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;

/**
 * create database table per input object in create method
 *
 * @author Reza Eskandari
 */
public class TableCreator implements TableCreatorInterface {
    private ProjectConfig projectConfig;

    public TableCreator() {
        projectConfig = new ProjectConfig();
    }

    public void create(Class object) {

        //initial database connection
        DatabaseConnection databaseConnection = DatabaseConnection.getNewInstance();
        Connection connection = null;
        try {
            connection = databaseConnection.getConnection();
        } catch (IOException e) {
            e.printStackTrace();
        }
        //read table name from object in table annotation
        Table table = (Table) object.getDeclaredAnnotation(Table.class);
        String dropQuery = String.format("%s  %s ", "DROP TABLE IF EXISTS ", table.name());
        String query = "CREATE TABLE IF NOT EXISTS " + table.name() + " (";
        Field[] fields = object.getDeclaredFields();
        for (Field field : fields) {
            Annotation[] annotations = field.getDeclaredAnnotations();
            for (Annotation annotation : annotations) {
                if (annotation instanceof Column) {
                    Column column = field.getAnnotation(Column.class);
                    query += column.name() + " " + column.dataType() + "(" + column.size() + ")";
                    Id id = field.getDeclaredAnnotation(Id.class);
                    if (id != null) {
                        if (id.autoIncrement()) {
                            query += "NOT NULL PRIMARY KEY AUTO_INCREMENT";
                        } else {
                            query += "NOT NULL PRIMARY KEY";
                        }
                    }
                    query += ",";
                }
            }
        }
        query = QueryUtil.RemoveCommaFromQueryEnd(query);
        query += ");";
        try {
            assert connection != null;
            PreparedStatement statement = connection.prepareStatement(query);
            PreparedStatement drpQueryStatement = connection.prepareStatement(dropQuery);
            if (projectConfig.isSqlTraceEnable()) {
                System.out.println(dropQuery);
                System.out.println(query);
            }
            drpQueryStatement.execute();
            statement.execute();
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
    }
}
