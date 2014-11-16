package com.github.dmgcodevil.jmspy.proxy.wrappers;

import java.util.Collection;

/**
 * Wrapper for {@link java.util.Map.Entry#values()}.
 *
 * Created by dmgcodevil.
 */
public class MapValuesWrapper extends AbstractCollectionWrapper {
    public MapValuesWrapper() {
    }

    public MapValuesWrapper(Collection target) {
        super(target);
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
