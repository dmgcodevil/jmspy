package com.github.dmgcodevil.jmspy.proxy.wrapper;

/**
 * Created by dmgcodevil on 12/29/2014.
 */
public abstract class AbstractWrapper<T> implements Wrapper<T> {

    private T target;

    public AbstractWrapper() {
        // default constructor is required
    }

    @Override
    public void setTarget(T target) {
        this.target = target;
    }

    @Override
    public T getTarget() {
        return target;
    }

}

