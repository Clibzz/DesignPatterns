package nhlstenden.bookandsales.Model;

import jakarta.persistence.Id;
import org.springframework.lang.NonNull;

import java.time.LocalDate;

public class User {

    @Id
    private int id;
    @NonNull
    private String firstName;
    @NonNull
    private String lastName;
    private LocalDate dateOfBirth;
    @NonNull
    private String address;
    @NonNull
    private String password;

    public User(int id, String firstName, String lastName, LocalDate dateOfBirth, String address, String password) {
        this.setId(id);
        this.setFirstName(firstName);
        this.setLastName(lastName);
        this.setDateOfBirth(dateOfBirth);
        this.setAddress(address);
        this.setPassword(password);
    }

    public int getId() {
        return this.id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getFirstName() {
        return this.firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getLastName() {
        return this.lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public LocalDate getDateOfBirth() {
        return this.dateOfBirth;
    }

    public void setDateOfBirth(LocalDate dateOfBirth) {
        this.dateOfBirth = dateOfBirth;
    }

    public String getAddress() {
        return this.address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getPassword() {
        return this.password;
    }

    public void setPassword(String password) {
        this.password = password;
    }
}
