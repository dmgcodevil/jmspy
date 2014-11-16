package com.github.dmgcodevil.jmspy.proxy.wrappers;

import com.github.dmgcodevil.jmspy.proxy.NotProxy;

import java.util.Iterator;

/**
 * Wrapper for {@link Iterator}.
 *
 * Created by dmgcodevil on 11/8/2014.
 */
public class IteratorWrapper implements Wrapper, Iterator {

    @NotProxy
    private Iterator target;

    /* default constructor is required */
    public IteratorWrapper() {
    }

    public IteratorWrapper(Iterator target) {
        this.target = target;
    }

    @Override
    public boolean hasNext() {
        return target.hasNext();
    }

    @Override
    public Object next() {
        return target.next();
    }

    @Override
    public void remove() {
        target.remove();
    }

    @Override
    public Wrapper create(Object target) {
        return new IteratorWrapper((Iterator) target);
    }

    @Override
    public void setTarget(Object target) {
        this.target = (Iterator) target;
    }

    @Override
    public Object getTarget() {
        return target;
    }

    @Override
    public Class<?> getType() {
        return IteratorWrapper.class;
    }

}
