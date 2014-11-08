package com.github.dmgcodevil.jmspy.proxy.wrappers;

import com.github.dmgcodevil.jmspy.proxy.NotProxy;

import java.util.Map;

/**
 * Created by dmgcodevil on 11/8/2014.
 */
public class EntryWrapper implements Map.Entry<Object, Object>, Wrapper {

    @NotProxy
    private Map.Entry<Object, Object> entry;

    public EntryWrapper() {
    }

    public EntryWrapper(Map.Entry<Object, Object> entry) {
        this.entry = entry;
    }

    public Map.Entry<Object, Object> getEntry() {
        return entry;
    }

    public void setEntry(Map.Entry<Object, Object> entry) {
        this.entry = entry;
    }

    @Override
    public Object getKey() {
        return entry.getKey();
    }

    @Override
    public Object getValue() {
        return entry.getValue();
    }

    @Override
    public Object setValue(Object value) {
        return entry.setValue(value);
    }

    @Override
    public boolean equals(Object o) {
        return entry.equals(o);
    }

    @Override
    public int hashCode() {
        return entry.hashCode();
    }

    @Override
    public Wrapper create(Object target) {
        return new EntryWrapper((Map.Entry) target);
    }

    @Override
    public Class<?> getType() {
        return EntryWrapper.class;
    }
}
