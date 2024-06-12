package it.mirea.kursovayabread;

public class Username {
    private String name, email, pass, phone;
    private boolean user = true, director = false, admin = false;

    public Username() {
        user = true;
        director = false;
        admin = false;
    }

    public Username(String name, String email, String pass, String phone) {
        this.name = name;
        this.email = email;
        this.pass = pass;
        this.phone = phone;
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

    public boolean isUser() {
        return user;
    }

    public void setUser() {
        user = true;
        director = false;
        admin = false;
    }

    public boolean isDirector() {
        return director;
    }

    public void setDirector() {
        director = true;
        user = false;
        admin = false;
    }

    public boolean isAdmin() {
        return admin;
    }

    public void setAdmin() {
        admin = true;
        director = false;
        user = false;
    }
}
