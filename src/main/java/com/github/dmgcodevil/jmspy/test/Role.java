package com.github.dmgcodevil.jmspy.test;

/**
 * Created by dmgcodevil on 11/8/2014.
 */
public class Role extends BaseDomain {
    private String name;

    public Role() {
    }

    public Role(String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}
