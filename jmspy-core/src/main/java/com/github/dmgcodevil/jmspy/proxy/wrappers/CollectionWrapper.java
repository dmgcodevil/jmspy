package com.github.dmgcodevil.jmspy.proxy.wrappers;

import java.util.Collection;

/**
 * Concrete implementation of {@link Collection} interface.
 *
 * @author dmgcodevil
 */
public class CollectionWrapper extends AbstractCollectionWrapper<Collection> {

    public CollectionWrapper() {
    }

    public CollectionWrapper(Collection target) {
        super(target);
    }

    @Override
    public Wrapper create(Collection target) {
        return new CollectionWrapper(target);
    }

    @Override
    public Class<? extends Wrapper<Collection>> getType() {
        return CollectionWrapper.class;
    }

}
