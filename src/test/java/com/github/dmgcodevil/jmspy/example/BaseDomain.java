package com.github.dmgcodevil.jmspy.example;

/**
 * Created by dmgcodevil on 11/7/2014.
 */
public class BaseDomain implements IDomain<String> {

    private String id;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }


}
