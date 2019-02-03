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
