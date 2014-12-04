package com.github.dmgcodevil.jmspy.proxy;

import java.io.Serializable;
import java.lang.reflect.Method;
import java.util.Arrays;

/**
 * Simple representation of {@link java.lang.reflect.Method}.
 *
 * @author dmgcodevil
 */
public class JMethod implements Serializable {

    private static final long serialVersionUID = -404618293637362098L;

    private String name;
    private String parameters;
    private String returnType;
    private String targetClass;

    public JMethod() {
    }

    public JMethod(Method method) {
        name = method.getName();
        parameters = Arrays.toString(method.getParameterTypes());
        returnType = method.getReturnType().getName();
        targetClass = method.getDeclaringClass().getName();
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

    public String getTargetClass() {
        return targetClass;
    }

    @Override
    public String toString() {
        return name + "(" + parameters + "):" + returnType;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        JMethod jMethod = (JMethod) o;

        if (name != null ? !name.equals(jMethod.name) : jMethod.name != null) return false;
        if (parameters != null ? !parameters.equals(jMethod.parameters) : jMethod.parameters != null) return false;
        if (returnType != null ? !returnType.equals(jMethod.returnType) : jMethod.returnType != null) return false;
        if (targetClass != null ? !targetClass.equals(jMethod.targetClass) : jMethod.targetClass != null) return false;

        return true;
    }

    @Override
    public int hashCode() {
        int result = name != null ? name.hashCode() : 0;
        result = 31 * result + (parameters != null ? parameters.hashCode() : 0);
        result = 31 * result + (returnType != null ? returnType.hashCode() : 0);
        result = 31 * result + (targetClass != null ? targetClass.hashCode() : 0);
        return result;
    }
}
