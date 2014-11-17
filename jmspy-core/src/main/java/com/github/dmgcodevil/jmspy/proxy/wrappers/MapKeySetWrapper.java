package com.github.dmgcodevil.jmspy.proxy.wrappers;

import java.util.Set;

/**
 * Wrapper for {@link java.util.Map.Entry#keySet()}.
 *
 * Created by dmgcodevil on 11/14/2014.
 */
public class MapKeySetWrapper extends AbstractSetWrapper{

    public MapKeySetWrapper() {
    }

    public MapKeySetWrapper(Set target) {
        super(target);
    }

    @Override
    public Wrapper create(Object target) {
        return new MapKeySetWrapper((Set) target);
    }

    @Override
    public Class<?> getType() {
        return MapKeySetWrapper.class;
    }
}

