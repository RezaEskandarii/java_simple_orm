package orm;

import annotations.Column;
import annotations.Id;
import annotations.Table;
import databse.DatabaseConnection;

import java.io.IOException;
import java.lang.annotation.Annotation;
import java.lang.reflect.Field;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;

public class TableCreator {
    public void create(Object object) {
        DatabaseConnection databaseConnection = DatabaseConnection.getNewInstance();
        Connection connection = null;
        try {
            connection = databaseConnection.getConnection();
        } catch (IOException e) {
            e.printStackTrace();
        }
        Table table = object.getClass().getDeclaredAnnotation(Table.class);
        String query = "CREATE TABLE IF NOT EXISTS " + table.name() + " (";
        Field[] fields = object.getClass().getDeclaredFields();
        for (Field field : fields) {
            Annotation[] annotations = field.getDeclaredAnnotations();
            for (Annotation annotation : annotations) {
                if (annotation instanceof Column) {
                    Column column = field.getAnnotation(Column.class);
                    query += column.name() +" "+ column.dataType() + "(" + column.size() + ")";
                    Id id = field.getDeclaredAnnotation(Id.class);
                    if (id!=null){
                        if (id.autoIncrement()){
                            query+="NOT NULL PRIMARY KEY AUTO_INCREMENT";
                        }else {
                            query+="NOT NULL PRIMARY KEY";
                        }
                    }
                    query+=",";
                }
            }
        }
        if (query.trim().endsWith(",")) {
            query = query.substring(0, query.length() - 1);
        }
        query += ");";
        try {
            PreparedStatement statement = connection.prepareStatement(query);
            System.out.println(query);
            statement.execute();
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
    }
}
