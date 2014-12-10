package com.github.dmgcodevil.jmspy.proxy;

import com.google.common.base.Objects;

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

    @Deprecated
    public void setName(String name) {
        this.name = name;
    }

    public String getReturnType() {
        return returnType;
    }

    @Deprecated
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

        JMethod that = (JMethod) o;

        return Objects.equal(this.name, that.name) &&
                Objects.equal(this.parameters, that.parameters) &&
                Objects.equal(this.returnType, that.returnType) &&
                Objects.equal(this.targetClass, that.targetClass);
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(name, parameters, returnType, targetClass);
    }

}
