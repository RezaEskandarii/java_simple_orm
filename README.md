### this project is simple java orm for create and manage java objects in database by reflection and jdbc tecnology

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
