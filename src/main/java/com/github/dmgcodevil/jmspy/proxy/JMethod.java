package com.github.dmgcodevil.jmspy.proxy;

import java.io.Serializable;
import java.lang.reflect.Method;
import java.util.Arrays;

/**
 * Created by dmgcodevil on 11/16/2014.
 */
public class JMethod implements Serializable {
    private String name;
    private String parameters;
    private String returnType;

    public JMethod() {
    }

    public JMethod(Method method) {
        name = method.getName();
        parameters = Arrays.toString(method.getParameterTypes());
        returnType = method.getReturnType().getName();

    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getReturnType() {
        return returnType;
    }

    public void setReturnType(String returnType) {
        this.returnType = returnType;
    }

    @Override
    public String toString() {
        return name + "(" + parameters + "):" + returnType;
    }
}
