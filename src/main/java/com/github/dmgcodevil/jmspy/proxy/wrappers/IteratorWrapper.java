package com.github.dmgcodevil.jmspy.proxy.wrappers;

import com.github.dmgcodevil.jmspy.proxy.NotProxy;

import java.util.Iterator;

/**
 * Created by dmgcodevil on 11/8/2014.
 */
public class IteratorWrapper implements Wrapper, Iterator {

    @NotProxy
    private Iterator iterator;

    /* default constructor is required */
    public IteratorWrapper() {
    }

    public IteratorWrapper(Iterator iterator) {
        this.iterator = iterator;
    }

    public Iterator getIterator() {
        return iterator;
    }

    public void setIterator(Iterator iterator) {
        this.iterator = iterator;
    }

    @Override
    public boolean hasNext() {
        return iterator.hasNext();
    }

    @Override
    public Object next() {
        return iterator.next();
    }

    @Override
    public void remove() {
        iterator.remove();
    }

    @Override
    public Wrapper create(Object target) {
        return new IteratorWrapper((Iterator) target);
    }

    @Override
    public void setTarget(Object target) {
        this.iterator = (Iterator) target;
    }

    @Override
    public Object getTarget() {
        return iterator;
    }

    @Override
    public Class<?> getType() {
        return IteratorWrapper.class;
    }


}
