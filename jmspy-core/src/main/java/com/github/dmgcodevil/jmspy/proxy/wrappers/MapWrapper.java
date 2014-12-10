package com.github.dmgcodevil.jmspy.proxy.wrappers;

import java.util.Collection;
import java.util.Map;
import java.util.Set;

/**
 * Wrapper for {@link Map}.
 *
 * @author dmgcodevil
 */
public class MapWrapper implements Map, Wrapper<Map> {

    private Map target;

    public MapWrapper() {
    }

    public MapWrapper(Map target) {
        this.target = target;
    }

    @Override
    public int size() {
        return target.size();
    }

    @Override
    public boolean isEmpty() {
        return target.isEmpty();
    }

    @Override
    public boolean containsKey(Object key) {
        return target.containsKey(key);
    }

    @Override
    public boolean containsValue(Object value) {
        return target.containsValue(value);
    }

    @Override
    public Object get(Object key) {
        return target.get(key);
    }

    @Override
    public Object put(Object key, Object value) {
        return target.put(key, value);
    }

    @Override
    public Object remove(Object key) {
        return target.remove(key);
    }

    @Override
    public void putAll(Map m) {
        target.putAll(m);
    }

    @Override
    public void clear() {
        target.clear();
    }

    @Override
    public Set keySet() {
        return target.keySet();
    }

    @Override
    public Collection values() {
        return target.values();
    }

    @Override
    public Set<Entry> entrySet() {
        return target.entrySet();
    }

    @Override
    public Wrapper create(Map target) {
        return new MapWrapper(target);
    }

    @Override
    public void setTarget(Map target) {
        this.target = target;
    }

    @Override
    public Map getTarget() {
        return target;
    }

    @Override
    public Class<? extends Wrapper<Map>> getType() {
        return MapWrapper.class;
    }
}
