package it.mirea.kursovayabread.models;

public class User {
    private String name, email, pass, phone;
    private UserRole role;

    public User() {
        role = UserRole.USER;
    }

    public User(String name, String email, String pass, String phone, UserRole role) {
        this.name = name;
        this.email = email;
        this.pass = pass;
        this.phone = phone;
        this.role = role;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPass() {
        return pass;
    }

    public void setPass(String pass) {
        this.pass = pass;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public UserRole getRole() {
        return role;
    }
    public void setRole(UserRole role) {
        this.role = role;
    }
}
