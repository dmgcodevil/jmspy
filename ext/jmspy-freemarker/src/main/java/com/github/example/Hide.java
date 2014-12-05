package com.github.example;

/**
 * Created by dmgcodevil on 11/3/2014.
 */
public class Hide {
    private String test;
    private String prop;

   public Hide(){

    }

    public Hide(String test) {
        this.test = test;
    }

    public String getVal(){
        return test;
    }

    public String getProp() {
        return prop;
    }

    public void setProp(String prop) {
        this.prop = prop;
    }
}
