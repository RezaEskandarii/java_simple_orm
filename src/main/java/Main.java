import entites.User;
import orm.annotations.Table;
import orm.session.Session;
import orm.session.TableCreator;

public class Main {
    public static void main(String[] args) {
        Session session = new Session();

        session.beginTransaction();

        User user = (User) session.get(User.class,3);
        session.update(user);
        session.commit();
    }
}
