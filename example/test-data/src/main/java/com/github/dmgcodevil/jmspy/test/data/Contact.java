package com.github.dmgcodevil.jmspy.test.data;

/**
 * Class without default constructor.
 */
public class Contact {
    private String name;

//    public Contact() {
//    }

    public Contact(String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }


}
