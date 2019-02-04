import entites.User;

import orm.session.Session;

public class Main {
    public static void main(String[] args) {
        Session session = new Session();
        session.beginTransaction();
        //create
        for (int i = 0; i < 10; i++) {
            User user = new User();
            user.setFirstName(String.format("%s %d", "FirstName", i));
            user.setLastName(String.format("%s %d", "LastName", i));
            user.setPhone(4411);
            user.setEmail(String.format("%s %d", "example@mail.com", i));
            session.save(user);
            session.commit();
        }

        //find user
        User user = (User) session.get(User.class, 51);
        System.out.println(user.toString());

        //find and update
        User user1 = (User) session.get(User.class, 51);
        user1.setFirstName("Reza");
        user1.setLastName("Eskandari");
        session.update(user);
        System.out.println(user1.toString());

        //delete
        User userToDelete = (User) session.get(User.class, 80);
        session.delete(userToDelete);

        //get all entities in List
        session.findAll(User.class).stream().forEach(users -> {
            System.out.println(users.toString());
        });
        session.commit();
        session.close();
    }
}
