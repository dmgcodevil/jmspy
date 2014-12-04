package com.github.dmgcodevil.jmspy.proxy.wrappers;

import java.util.Set;

/**
 * Concrete implementation of {@link Set} interface.
 *
 * @author dmgcodevil
 */
public class SetWrapper extends AbstractCollectionWrapper<Set> implements Set {

    public SetWrapper() {
    }

    public SetWrapper(Set target) {
        super(target);
    }

    @Override
    public Wrapper create(Set target) {
        return new SetWrapper(target);
    }

    @Override
    public Class<? extends Wrapper<Set>> getType() {
        return SetWrapper.class;
    }
}
