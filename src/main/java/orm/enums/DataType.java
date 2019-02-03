package orm.enums;

/**
 * database datatypes to use in @Column annotation
 *
 * @author Reza Eskandari
 */
public enum DataType {
    /*String Data Types*/
    VARCHAR,
    INT,
    CHAR,
    BLOB,
    TEXT,
    TINYBLOB,
    MEDIUMBLOB,
    /* Boolean DataType */
    BOOLEAN,
    /*Numeric Data Types*/
    DOUBLE,
    TINYINT,
    SMALLINT,
    MEDIUMINT,
    BIGINT,
    FLOAT,
    DECIMAL,
    /*Date and Time Types*/
    DATETIME,
    TIMESTAMP,
    TIME,
    DATE,
    YEAR
}
