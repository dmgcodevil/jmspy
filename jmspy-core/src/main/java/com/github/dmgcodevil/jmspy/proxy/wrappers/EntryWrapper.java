package com.github.dmgcodevil.jmspy.proxy.wrappers;

import com.github.dmgcodevil.jmspy.proxy.NotProxy;

import java.util.Map;

/**
 * Wrapper for {@link java.util.Map.Entry}.
 * <p/>
 * Created by dmgcodevil.
 */
public class EntryWrapper implements Map.Entry<Object, Object>, Wrapper {

    @NotProxy
    private Map.Entry<Object, Object> target;

    public EntryWrapper() {
    }

    public EntryWrapper(Map.Entry<Object, Object> target) {
        this.target = target;
    }

    @Override
    public Object getKey() {
        return target.getKey();
    }

    @Override
    public Object getValue() {
        return target.getValue();
    }

    @Override
    public Object setValue(Object value) {
        return target.setValue(value);
    }

    @Override
    public boolean equals(Object o) {
        return target.equals(o);
    }

    @Override
    public int hashCode() {
        return target.hashCode();
    }

    @Override
    public Wrapper create(Object target) {
        return new EntryWrapper((Map.Entry) target);
    }

    @Override
    public void setTarget(Object target) {
        this.target = (Map.Entry) target;
    }

    @Override
    public Object getTarget() {
        return target;
    }

    @Override
    public Class<?> getType() {
        return EntryWrapper.class;
    }
}
