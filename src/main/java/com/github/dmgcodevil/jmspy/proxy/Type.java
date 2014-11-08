package com.github.dmgcodevil.jmspy.proxy;

/**
 * Created by dmgcodevil on 11/8/2014.
 */
public class Type {
    private Class<?> target;
    private java.lang.reflect.Type[] parameterizedTypes;

    public Type() {
    }

    public Type(Class<?> target) {
        this.target = target;
    }

    public Class<?> getTarget() {
        return target;
    }

    public void setTarget(Class<?> target) {
        this.target = target;
    }

    public java.lang.reflect.Type[] getParameterizedTypes() {
        return parameterizedTypes;
    }

    public void setParameterizedTypes(java.lang.reflect.Type[] parameterizedTypes) {
        this.parameterizedTypes = parameterizedTypes;
    }
}
