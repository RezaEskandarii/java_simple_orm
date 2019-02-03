package orm.session;

import orm.annotations.Column;
import orm.annotations.Entity;
import orm.annotations.Id;
import orm.annotations.Table;
import orm.databse.DatabaseConnection;
import orm.exception.EntityException;
import orm.utils.AnnotationUtil;

import java.io.IOException;
import java.lang.reflect.Field;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class Session {
    private DatabaseConnection databaseConnection = null;
    private Connection connection = null;
    private PreparedStatement statement;

    private int transactionIsolationLevel = -1;

    public int getTransactionIsolationLevel() {
        return transactionIsolationLevel;
    }

    public void setTransactionIsolationLevel(int transactionIsolationLevel) {
        this.transactionIsolationLevel = transactionIsolationLevel;
    }

    public void beginTransaction() {
        databaseConnection = DatabaseConnection.getNewInstance();
        try {
            connection = databaseConnection.getConnection();
            try {
                connection.setAutoCommit(false);
                if (transactionIsolationLevel == -1) {
                    connection.setTransactionIsolation(Connection.TRANSACTION_READ_COMMITTED);
                }else {
                    connection.setTransactionIsolation(transactionIsolationLevel);
                }
            } catch (SQLException e) {
                e.printStackTrace();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void save(Object object) {
        if (AnnotationUtil.EntityAnnotationDeclared(object)) {
            String query = "INSERT INTO ";
            Table table = object.getClass().getDeclaredAnnotation(Table.class);
            query += table.name() + " (";
            Field[] fields = object.getClass().getDeclaredFields();
            for (Field field : fields) {
                field.setAccessible(true);
                Column column = field.getAnnotation(Column.class);
                if (column != null) {
                    query += column.name() + ",";
                }
            }
            if (query.trim().endsWith(",")) {
                query = query.substring(0, query.length() - 1);
            }
            query += ") VALUES (";
            for (Field field : fields) {
                field.setAccessible(true);
                try {
                    if (field.getType().getSimpleName().endsWith("String")) {
                        query += "'" + field.get(object) + "'" + ",";
                    } else {
                        query += field.get(object) + ",";
                    }
                } catch (IllegalAccessException e) {
                    e.printStackTrace();
                }
            }
            if (query.trim().endsWith(",")) {
                query = query.substring(0, query.length() - 1);
            }
            query += ");";
            System.out.println(query);
            try {
                statement = connection.prepareStatement(query);
                statement.execute();
            } catch (SQLException e) {
                e.printStackTrace();
            }
        } else {
            try {
                throw new EntityException(object);
            } catch (EntityException e) {
                e.printStackTrace();
            }
        }
    }

    public void update(Object object) {
        if (AnnotationUtil.EntityAnnotationDeclared(object)) {
            Table table = object.getClass().getDeclaredAnnotation(Table.class);
            Entity entity = object.getClass().getDeclaredAnnotation(Entity.class);
            String query = "UPDATE " + table.name() + " SET ";
            Field[] fields = object.getClass().getDeclaredFields();
            Object oid = null;
            String idColumn = "";
            for (Field field : fields) {
                Column column = field.getDeclaredAnnotation(Column.class);
                field.setAccessible(true);
                Id id = field.getDeclaredAnnotation(Id.class);
                if (id != null) {
                    try {
                        oid = field.get(object);
                    } catch (IllegalAccessException e) {
                        e.printStackTrace();
                    }
                    idColumn = column.name();
                }
                if (column != null) {
                    try {
                        if (id == null) {
                            if (field.getType().getSimpleName().endsWith("String")) {
                                query += column.name() + "=" + "'" + field.get(object) + "',";
                            } else {
                                query += column.name() + "=" + field.get(object) + ",";
                            }
                        }
                    } catch (IllegalAccessException e) {
                        e.printStackTrace();
                    }
                }
            }
            if (query.trim().endsWith(",")) {
                query = query.substring(0, query.length() - 1);
            }
            query += " WHERE " + idColumn + " = " + oid;
            try {
                statement = connection.prepareStatement(query);
                statement.executeUpdate();
            } catch (SQLException e) {
                e.printStackTrace();
            }
        } else {
            try {
                throw new EntityException(object);
            } catch (EntityException e) {
                e.printStackTrace();
            }
        }
    }

    public Object get(Class clazz, Object id) {
        Object object = null;
        try {
            object = clazz.newInstance();
        } catch (InstantiationException e) {
            e.printStackTrace();
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        }
        if (AnnotationUtil.EntityAnnotationDeclared(object)) {
            Table table = object.getClass().getDeclaredAnnotation(Table.class);
            Entity entity = object.getClass().getDeclaredAnnotation(Entity.class);
            Field[] fields = object.getClass().getDeclaredFields();
            String tableName = table.name();
            String idColumnName = "";
            for (Field field : fields) {
                Id idAnnotation = field.getDeclaredAnnotation(Id.class);
                if (idAnnotation != null) {
                    Column column = field.getDeclaredAnnotation(Column.class);
                    idColumnName = column.name();
                }
            }
            String query = "SELECT * FROM " + tableName + "  WHERE " + idColumnName + " = " + id;
            ResultSet resultSet = null;
            try {
                statement = connection.prepareStatement(query);
                resultSet = statement.executeQuery();
                while (resultSet.next()) {
                    for (Field field : fields) {
                        field.setAccessible(true);
                        Column column = field.getDeclaredAnnotation(Column.class);
                        if (column != null) {
                            if (field.getType().getSimpleName().endsWith("int")) {
                                field.set(object, resultSet.getInt(column.name()));
                            } else if (field.getType().getSimpleName().endsWith("Integer")) {
                                field.set(object, resultSet.getInt(column.name()));
                            } else if (field.getType().getSimpleName().endsWith("String")) {
                                field.set(object, resultSet.getString(column.name()));
                            }
                        }
                    }
                }
            } catch (SQLException e) {
                e.printStackTrace();
            } catch (IllegalAccessException e) {
                e.printStackTrace();
            }
            return object;
        } else {
            try {
                throw new EntityException(object);
            } catch (EntityException e) {
                e.printStackTrace();
            }
        }
        return object;
    }


    public void delete(Object object) {
        if (AnnotationUtil.EntityAnnotationDeclared(object)){
            Table table = object.getClass().getDeclaredAnnotation(Table.class);
            Entity entity = object.getClass().getDeclaredAnnotation(Entity.class);
            String query = "DELETE FROM " + table.name() + " WHERE ";
            Field[] fields = object.getClass().getDeclaredFields();
            Object oid = null;
            String idColumn = "";
            for (Field field : fields) {
                field.setAccessible(true);
                Id id = field.getDeclaredAnnotation(Id.class);
                if (id != null) {
                    Column column = field.getDeclaredAnnotation(Column.class);
                    try {
                        oid = field.get(object);
                    } catch (IllegalAccessException e) {
                        e.printStackTrace();
                    }
                    idColumn = column.name();
                }
            }
            if (query.trim().endsWith(",")) {
                query = query.substring(0, query.length() - 1);
            }
            query += idColumn += " = " + oid;
            System.out.println(query);
            try {
                statement = connection.prepareStatement(query);
                statement.executeUpdate();
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }else {
            try {
                throw new EntityException(object);
            } catch (EntityException e) {
                e.printStackTrace();
            }
        }
    }


    public List<Object> findAll(Class clazz) {
        Object object = null;
        try {
            object = clazz.newInstance();
        } catch (InstantiationException e) {
            e.printStackTrace();
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        }
        Entity entity = object.getClass().getDeclaredAnnotation(Entity.class);
        List<Object> objectList = new ArrayList<>();
        if (AnnotationUtil.EntityAnnotationDeclared(object)){
            Table table = object.getClass().getDeclaredAnnotation(Table.class);
            Field[] fields = object.getClass().getDeclaredFields();
            String tableName = table.name();
            String idColumnName = "";
            String query = "SELECT * FROM " + tableName;
            ResultSet resultSet = null;
            try {
                statement = connection.prepareStatement(query);
                resultSet = statement.executeQuery();
                while (resultSet.next()) {
                    object = object.getClass().newInstance();
                    for (Field field : fields) {
                        field.setAccessible(true);
                        Column column = field.getDeclaredAnnotation(Column.class);
                        if (column != null) {
                            if (field.getType().getSimpleName().endsWith("int")) {
                                field.set(object, resultSet.getInt(column.name()));
                            } else if (field.getType().getSimpleName().endsWith("Integer")) {
                                field.set(object, resultSet.getInt(column.name()));
                            } else if (field.getType().getSimpleName().endsWith("String")) {
                                field.set(object, resultSet.getString(column.name()));
                            }
                        }
                    }
                    objectList.add(object);
                }
            } catch (SQLException e) {
                e.printStackTrace();
            } catch (IllegalAccessException e) {
                e.printStackTrace();
            } catch (InstantiationException e) {
                e.printStackTrace();
            }
        }else {
            try {
                throw new EntityException(object);
            } catch (EntityException e) {
                e.printStackTrace();
            }
        }
        return objectList;
    }

    public void commit() {
        try {
            try {
                connection.commit();
            } catch (SQLException e) {
                e.printStackTrace();
            }
        } catch (Exception e) {
            try {
                connection.rollback();
            } catch (SQLException e1) {
                e1.printStackTrace();
            }
        }
    }

    public void close() {
        if (statement != null) {
            try {
                statement.close();
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
        if (connection != null) {
            try {
                connection.close();
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
    }
}
