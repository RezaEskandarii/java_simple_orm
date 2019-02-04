### this project is simple java orm for create and manage java objects in database by reflection and jdbc tecnology
+ simple orm can. 
    - Create tables from your Java classes.
    - Create columns from fields of your Java classes by @Column annotation.
    - Work with MySql .
   
--The project configuration file is located in the directory src / main / resources / application.properties
+ In this file you can perform the following configurations
	- com.settings.db_user=your database username
	- com.setting.db_pass= your database password
	- com.setting.db_url=jdbc:mysql://localhost:3306/your_database_name_in_mysql
	- com.setting.db_driver=com.mysql.jdbc.Driver
	- com.setting.show_sql=true
	- com.setting.ddl=update 
### If com.setting.dll is set to create, each time the object is saved in session, the entire table will be erased and rebuilt.
If we put it equal to the update, the table will not be deleted and only new objects will be saved.
Set the com.setting.dll equal to create the first instance of the Session class save method so that the entity equivalent will create a table in the database.

##### To get started, you can create a user entity.
#####  To save an user entity in the database, the user entity must use the @Entity and @Table annotation
for example :
```
package entites;
import orm.annotations.Column;
import orm.annotations.Entity;
import orm.annotations.Id;
import orm.annotations.Table;
import orm.enums.DataType;

@Table(name = "user_tbl")
@Entity
public class User {

@Column(name = "u_id", dataType = DataType.INT, size = 255)
@Id(autoIncrement = true)
private int id;

@Column(name = "first_name", dataType = DataType.VARCHAR)
private String firstName;

@Column(name = "last_name", dataType = DataType.VARCHAR)
private String lastName;

@Column(name = "email", dataType = DataType.VARCHAR)
private String email;

@Column(name = "phone", dataType = DataType.VARCHAR)
private int phone;

public int getId() {
return id;
}

public void setId(int id) {

this.id = id;

}

public String getFirstName() {
return firstName;
}

public void setFirstName(String firstName) {
this.firstName = firstName;
}

public String getLastName() {
return lastName;
}

public void setLastName(String lastName) {
this.lastName = lastName;
}

public String getEmail() {
return email;
}

public void setEmail(String email) {
this.email = email;
}

public int getPhone() {
return phone;
}

public void setPhone(int phone) {
this.phone = phone;
}

@Override
public String toString() {
return "User{" +

"id=" + id +

", firstName='" + firstName + '\'' +

", lastName='" + lastName + '\'' +

", email='" + email + '\'' +

", phone=" + phone +

'}';

}

}
```
#### for CRUD operations, the following code should be used.
##### To start, we need to create a new instance from the session class
```
import entites.User;  
  
import orm.session.Session;  
  
public class Main {  
    public static void main(String[] args) {  
       Session session = new Session();  
	   session.beginTransaction();  
	  //create  or insert into database
	  for (int i = 0; i < 10; i++) {  
            User user = new User();  
		  user.setFirstName(String.format("%s %d", "FirstName", i));  
		  user.setLastName(String.format("%s %d", "LastName", i));  
		  user.setPhone(4411);  
		  user.setEmail(String.format("%s %d", "example@mail.com", i));  
		  session.save(user);  
		  session.commit();  
  }  
  
        //fetch user data 
	  User user = (User) session.get(User.class, 51);  
	  System.out.println(user.toString());  
  
	  //fetcha and update  
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
```
