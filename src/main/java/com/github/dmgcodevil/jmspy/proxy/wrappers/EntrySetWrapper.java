package com.github.dmgcodevil.jmspy.proxy.wrappers;

import java.util.Set;

/**
 * Wrapper for {@link java.util.Map#entrySet()}.
 * <p/>
 * Created by dmgcodevil on 11/8/2014.
 */
public class EntrySetWrapper extends AbstractSetWrapper {

    public EntrySetWrapper() {
    }

    public EntrySetWrapper(Set target) {
        super(target);
    }

    @Override
    public Wrapper create(Object target) {
        return new EntrySetWrapper((Set) target);
    }

    @Override
    public Class<?> getType() {
        return EntrySetWrapper.class;
    }
}
