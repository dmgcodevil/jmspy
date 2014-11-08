package com.github.dmgcodevil.jmspy.test;

/**
 * Created by dmgcodevil on 11/8/2014.
 */
public class Profile  extends BaseDomain{

    private String email;

    public Profile() {
    }

    public Profile(String email) {
        this.email = email;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }
}
