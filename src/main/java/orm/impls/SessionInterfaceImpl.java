package orm.impls;

import orm.annotations.Column;
import orm.annotations.Entity;
import orm.annotations.Id;
import orm.annotations.Table;
import orm.configuration.DatabaseConnection;
import orm.configuration.ProjectConfig;
import orm.exception.EntityException;
import orm.interfaces.SessionInterface;
import orm.utils.AnnotationUtil;
import orm.utils.QueryUtil;

import java.io.IOException;
import java.lang.reflect.Field;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

/**
 * implements SessionInterface
 *
 * @author RezaEskandari
 */
public abstract class SessionInterfaceImpl implements SessionInterface {

    private DatabaseConnection databaseConnection = null;
    private Connection connection = null;
    private PreparedStatement statement;
    private ProjectConfig projectConfig;

    public SessionInterfaceImpl() {
        this.projectConfig = new ProjectConfig();
    }

    /**
     * check session transaction isolation level is set or not
     * transactionIsolationLevel = -1 meaning is transactionIsolationLevel is not set
     */
    private int transactionIsolationLevel = -1;

    public int getTransactionIsolationLevel() {
        return transactionIsolationLevel;
    }

    public void setTransactionIsolationLevel(int transactionIsolationLevel) {
        this.transactionIsolationLevel = transactionIsolationLevel;
    }

    /**
     * start transaction
     */
    public void beginTransaction() {
        databaseConnection = DatabaseConnection.getNewInstance();
        try {
            connection = databaseConnection.getConnection();
            try {
                connection.setAutoCommit(false);
                if (transactionIsolationLevel == -1) {
                    connection.setTransactionIsolation(Connection.TRANSACTION_READ_COMMITTED);
                } else {
                    connection.setTransactionIsolation(transactionIsolationLevel);
                }
            } catch (SQLException e) {
                e.printStackTrace();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }


    /**
     * save input object in database
     *
     * @param object
     */
    public void save(Object object) {
        if (AnnotationUtil.EntityAnnotationDeclared(object)) {
            String query = "INSERT INTO ";
            Table table = (Table) object.getClass().getDeclaredAnnotation(Table.class);
            query += table.name() + " (";
            Field[] fields = object.getClass().getDeclaredFields();
            for (Field field : fields) {
                field.setAccessible(true);
                Column column = field.getAnnotation(Column.class);
                if (column != null) {
                    query += column.name() + ",";
                }
            }
            query = QueryUtil.RemoveCommaFromQueryEnd(query);
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
            query = QueryUtil.RemoveCommaFromQueryEnd(query);
            query += ");";
            if (projectConfig.isSqlTraceEnable()) {
                System.out.println(query);
            }
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

    /**
     * update object in database
     *
     * @param object
     */
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
            query = QueryUtil.RemoveCommaFromQueryEnd(query);
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

    /**
     * @param clazz
     * @param id
     * @return object by id
     */
    public Object get(Class clazz, Object id) {
        Object object = null;
        int userCount = 0;
        try {
            object = clazz.newInstance();
        } catch (InstantiationException | IllegalAccessException e) {
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
                    userCount++;
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
                            } else if (field.getType().getSimpleName().endsWith("Date")) {
                                field.set(object, resultSet.getDate(column.name()));
                            } else if (field.getType().getSimpleName().endsWith("Boolean")) {
                                field.set(object, resultSet.getBoolean(column.name()));
                            } else if (field.getType().getSimpleName().endsWith("Float")) {
                                field.set(object, resultSet.getFloat(column.name()));
                            } else if (field.getType().getSimpleName().endsWith("Double")) {
                                field.set(object, resultSet.getDouble(column.name()));
                            } else if (field.getType().getSimpleName().endsWith("Timestamp")) {
                                field.set(object, resultSet.getTimestamp(column.name()));
                            }
                        }
                    }
                }
            } catch (SQLException | IllegalAccessException e) {
                e.printStackTrace();
            }
            if (userCount == 0) {
                throw new NullPointerException("user not found");
            }
            return object;
        } else {
            throw new NullPointerException("not found");
        }

    }


    /**
     * delete object in databsae
     *
     * @param object
     */
    public void delete(Object object) {
        if (AnnotationUtil.EntityAnnotationDeclared(object)) {
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
            QueryUtil.RemoveCommaFromQueryEnd(query);
            query += idColumn += " = " + oid;
            if (projectConfig.isSqlTraceEnable()) {
                System.out.println(query);
            }
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


    /**
     * get all data by object list
     *
     * @param clazz
     * @return
     */
    public List<Object> findAll(Class clazz) {
        Object object = null;
        try {
            object = clazz.newInstance();
        } catch (InstantiationException | IllegalAccessException e) {
            e.printStackTrace();
        }
        Entity entity = object.getClass().getDeclaredAnnotation(Entity.class);
        List<Object> objectList = new ArrayList<>();
        if (AnnotationUtil.EntityAnnotationDeclared(object)) {
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
                            } else if (field.getType().getSimpleName().endsWith("Date")) {
                                field.set(object, resultSet.getDate(column.name()));
                            } else if (field.getType().getSimpleName().endsWith("Boolean")) {
                                field.set(object, resultSet.getBoolean(column.name()));
                            } else if (field.getType().getSimpleName().endsWith("Float")) {
                                field.set(object, resultSet.getFloat(column.name()));
                            } else if (field.getType().getSimpleName().endsWith("Double")) {
                                field.set(object, resultSet.getDouble(column.name()));
                            } else if (field.getType().getSimpleName().endsWith("Timestamp")) {
                                field.set(object, resultSet.getTimestamp(column.name()));
                            }
                        }
                    }
                    objectList.add(object);
                }
            } catch (SQLException | IllegalAccessException | InstantiationException e) {
                e.printStackTrace();
            }
        } else {
            try {
                throw new EntityException(object);
            } catch (EntityException e) {
                e.printStackTrace();
            }
        }
        return objectList;
    }

    /**
     * commit transaction
     */
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


    /**
     * close all session connections and prepared statements
     */
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
