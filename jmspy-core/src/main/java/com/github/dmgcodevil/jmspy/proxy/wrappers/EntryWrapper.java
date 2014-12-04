package com.github.dmgcodevil.jmspy.proxy.wrappers;

import com.github.dmgcodevil.jmspy.proxy.NotProxy;

import java.util.Map;

/**
 * Wrapper for {@link java.util.Map.Entry}.
 *
 * @author dmgcodevil
 */
public class EntryWrapper implements Map.Entry, Wrapper<Map.Entry> {

    @NotProxy
    private Map.Entry target;

    public EntryWrapper() {
    }

    public EntryWrapper(Map.Entry target) {
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
    public Wrapper create(Map.Entry target) {
        return new EntryWrapper(target);
    }

    @Override
    public void setTarget(Map.Entry target) {
        this.target = target;
    }

    @Override
    public Map.Entry getTarget() {
        return target;
    }

    @Override
    public Class<? extends Wrapper<Map.Entry>> getType() {
        return EntryWrapper.class;
    }
}
