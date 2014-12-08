package com.github.example;

public class User {

    private String firstname;
    private String lastname;


    public User() {
    }

    public User(String firstname, String lastname) {
        this();
        this.firstname = firstname;
        this.lastname = lastname;

    }

    public String getFirstname() {
        return firstname;
    }

    public void setFirstname(String firstname) {
        this.firstname = firstname;
    }

    public String getLastname() {
        return lastname;
    }

    public void setLastname(String lastname) {
        this.lastname = lastname;
    }

}
