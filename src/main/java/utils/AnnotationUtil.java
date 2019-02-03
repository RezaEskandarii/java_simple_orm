package utils;

import annotations.Entity;
import exception.EntityException;

public class AnnotationUtil {
    public static boolean EntityAnnotationDeclared(Object object) {
        Entity entity = object.getClass().getDeclaredAnnotation(Entity.class);
        if (entity == null) {
            return false;
        }
        return true;
    }
}
