package orm.utils;

import orm.annotations.Entity;

public class AnnotationUtil {
    public static boolean EntityAnnotationDeclared(Object object) {
        Entity entity = object.getClass().getDeclaredAnnotation(Entity.class);
        return entity != null;
    }
}
