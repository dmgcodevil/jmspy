package com.github.dmgcodevil.jmspy.proxy.wrappers;

import com.github.dmgcodevil.jmspy.proxy.NotProxy;

import java.util.Iterator;

/**
 * Wrapper for {@link Iterator}.
 *
 * @author dmgcodevil
 */
public class IteratorWrapper implements Wrapper<Iterator>, Iterator {

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
    public Wrapper create(Iterator target) {
        return new IteratorWrapper(target);
    }

    @Override
    public void setTarget(Iterator target) {
        this.target = target;
    }

    @Override
    public Iterator getTarget() {
        return target;
    }

    @Override
    public Class<? extends Wrapper<Iterator>> getType() {
        return IteratorWrapper.class;
    }

}
