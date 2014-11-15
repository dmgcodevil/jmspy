package com.github.dmgcodevil.jmspy.proxy.wrappers;

import java.util.Collection;

/**
 * Created by dmgcodevil on 11/14/2014.
 */
public class MapValuesWrapper extends AbstractCollectionWrapper implements Collection<Object>, Wrapper {
    public MapValuesWrapper() {
    }

    public MapValuesWrapper(Collection source) {
        super(source);
    }

    @Override
    public Wrapper create(Object target) {
        return new MapValuesWrapper((Collection) target);
    }

    @Override
    public Class<?> getType() {
        return MapValuesWrapper.class;
    }
}
