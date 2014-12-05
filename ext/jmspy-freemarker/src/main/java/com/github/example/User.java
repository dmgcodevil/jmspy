package com.github.example;

import java.util.ArrayList;
import java.util.List;

/**
 * Class description.
 *
 * @author raman_pliashkou
 */
public class User {

    private String firstname;
    private String lastname;
    private List<Role> roles = new ArrayList<Role>();
    private Profile profile = new Profile();

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

    public List<Role> getRoles() {
        return roles;
    }

    public void setRoles(List<Role> roles) {
        this.roles = roles;
    }

    public Profile getProfile() {
        return profile;
    }

    public void setProfile(Profile profile) {
        this.profile = profile;
    }

}
