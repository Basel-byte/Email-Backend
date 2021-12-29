package com.example.emailbackserver.EmailModel;

import org.springframework.stereotype.Repository;


@Repository
public class User implements Cloneable{
    String emailAddress;
    String name;
    String password;
    boolean loggedIn;

    public User() {}

    public User(String emailAddress, String name, String password) {
        this.emailAddress = emailAddress;
        this.name = name;
        this.password = password;
        this.loggedIn = false;
    }

    public User(String emailAddress, String password) {
        this.emailAddress = emailAddress;
        this.password = password;
    }

    public String getEmailAddress() {
        return emailAddress;
    }
    public String getName() {
        return name;
    }
    public String getPassword() {
        return password;
    }
    //////hayet4al 4akloh//////
    public boolean isLoggedIn() {return loggedIn;}

    @Override
    public User clone() throws CloneNotSupportedException{
        return (User) super.clone();
    }
}
