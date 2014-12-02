package com.github.dmgcodevil.jmspy.proxy;

import java.io.Serializable;

/**
 * Simple representation of {@link java.lang.Class}.
 *
 * @author dmgcodevil
 */
public class JType implements Serializable{
    private String name;

    public JType() {
    }

    public JType(String name) {
        this.name = name;
    }

    public JType(Class<?> type) {
        name = type.getName();
    }


    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        JType jType = (JType) o;

        if (name != null ? !name.equals(jType.name) : jType.name != null) return false;

        return true;
    }

    @Override
    public int hashCode() {
        return name != null ? name.hashCode() : 0;
    }
}
