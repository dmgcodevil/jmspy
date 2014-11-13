package com.github.dmgcodevil.jmspy.example.jdkproxy;

/**
 * Class description.
 *
 * @author Raman_Pliashkou
 */
public class SomeObjectHolder {

   private ISomeObject someObject;

    public ISomeObject getSomeObject() {
        return someObject;
    }

    public void setSomeObject(ISomeObject someObject) {
        this.someObject = someObject;
    }
}
