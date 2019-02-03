import entites.User;
import orm.annotations.Table;
import orm.session.Session;
import orm.session.TableCreator;

public class Main {
    public static void main(String[] args) {
        Session session = new Session();

        session.beginTransaction();
        User user = (User) session.get(User.class, 21);
        session.delete(user);
        session.findAll(User.class).stream().forEach(userObject ->{
            System.out.println(userObject);
        });
        session.commit();
    }
}
