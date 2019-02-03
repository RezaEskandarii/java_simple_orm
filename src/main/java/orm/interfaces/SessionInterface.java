package orm.interfaces;

import java.util.List;

public interface SessionInterface {

    public void save(Object object);

    public void delete(Object object);

    public Object get(Class clazz, Object id);

    public List<Object> findAll(Class clazz);

    public void beginTransaction();

    public void commit();

    public void close();
}
