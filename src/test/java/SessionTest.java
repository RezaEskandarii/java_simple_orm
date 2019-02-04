import entites.User;
import org.junit.Before;
import org.junit.Test;
import orm.session.Session;

import static org.junit.Assert.*;

public class SessionTest {
    private Session session;


    @Before
    public void before() {
        this.session = new Session();
    }

    @Test
    public void sessionNotNull() {
        assertNotNull(session);
    }


    @Test
    public void sessionCanSave() {
        User user = new User();
        user.setId(3);
        user.setFirstName("Reza");
        user.setLastName("Eskandari");
        user.setEmail("example@example.com");
        user.setPhone(44888111);
        session.beginTransaction();
        session.save(user);
        assertNotNull(session.get(User.class, 3));
        session.commit();
    }

    @Test
    public void sessionCanDeleteObject() {
        session.beginTransaction();
        User user = (User) session.get(User.class, 3);
        session.delete(user);

        User u = (User) session.get(User.class, 3);
        assertNull(u);
        session.commit();
    }

    @Test
    public void sessionCanGetObjectsList() {
        session.beginTransaction();
        assertTrue(session.findAll(User.class).size() > 0);
        assertNotNull(session.findAll(User.class));
        session.findAll(User.class).stream().forEach(user -> {
            assertTrue(user instanceof User);
        });
        session.commit();
    }

    @Test
    public void sessionUpdateMethodWorks() {
        session.beginTransaction();
        User user = (User) session.get(User.class, 3);
        user.setFirstName("RZA");
        session.update(user);
        session.commit();
        User u = (User) session.get(User.class, 3);
        assertEquals("RZA", u.getFirstName());
    }

    @Test
    public void sessionGetMethodWorks() {
        session.beginTransaction();
        User user = (User) session.get(User.class, 3);
        assertNotNull(user);
        assertTrue(user instanceof User);
        assertEquals(user.getId(), 3);
    }

    @Test
    public void sessionCanClosed() {
        session.close();
        session.beginTransaction();
        assertNull(session.get(User.class,3));
    }
}
