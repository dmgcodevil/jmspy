package com.github.dmgcodevil.jmspy.proxy;

import com.google.common.base.MoreObjects;
import com.google.common.base.Objects;

import java.io.Serializable;

/**
 * Simple representation of {@link java.lang.Class}.
 *
 * @author dmgcodevil
 */
public class JType implements Serializable {
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

        JType that = (JType) o;

        return Objects.equal(this.name, that.name);
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(name);
    }

    @Override
    public String toString() {
        return MoreObjects.toStringHelper(this)
                .add("name", name)
                .toString();
    }
}
