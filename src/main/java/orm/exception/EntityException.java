package orm.exception;

/*
 *this orm.exception helps to handle annotation is not declared Exception in objects
 *that uses in orm.session for store in database
 */
public class EntityException extends Exception {
    public EntityException(Object o) {
        System.out.println("@Entity annotation is not declared on class " + o.getClass().getName());
    }
}
