package exception;

public class EntityException extends Exception {
    public EntityException(Object o) {
        System.out.println("@Entity annotation is not declared on class " + o.getClass().getName());
    }
}
