package com.example.emailbackserver.EmailModel;

import org.springframework.stereotype.Repository;


@Repository
public class User implements Cloneable{
    String emailAddress;
    String name;
    String password;
    boolean loggedIn;

    public User() {
        this.emailAddress = "";
        this.name = "";
        this.password = "";
        this.loggedIn = false;
    }

    public User(String emailAddress) {
        this.emailAddress = emailAddress;
    }

    public User(String emailAddress, String password) {
        this.emailAddress = emailAddress;
        this.password = password;
        this.name = "";
    }

    public User(String emailAddress, String name, String password) {
        this.emailAddress = emailAddress;
        this.name = name;
        this.password = password;
        this.loggedIn = false;
    }

    public void setLoggedIn(boolean loggedIn) {
        this.loggedIn = loggedIn;
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
    public boolean isLoggedIn() {return loggedIn;}

    @Override
    public User clone() throws CloneNotSupportedException{
        return (User) super.clone();
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj)
            return true;

        if (!(obj instanceof User)) {
            return false;
        }

        // typecast o to Complex so that we can compare data members
        User u = (User) obj;

        // Compare the data members and return accordingly
        return this.name.equals(u.name) && this.emailAddress.equals(u.emailAddress);
    }
}
