package com.github.dmgcodevil.jmspy.proxy.wrappers;

import com.github.dmgcodevil.jmspy.proxy.NotProxy;

import java.util.Collection;
import java.util.Iterator;
import java.util.Set;

/**
 * Basic implementation of {@link Set} interface.
 *
 * Created by dmgcodevil on 11/16/2014.
 */
public abstract class AbstractSetWrapper implements Set<Object>, Wrapper {

    @NotProxy
    private Set<Object> target;

    /* default constructor is required */
    public AbstractSetWrapper() {
    }

    public AbstractSetWrapper(Set target) {
        this.target = target;
    }

    public Object getTarget() {
        return target;
    }

    public void setTarget(Object target) {
        this.target = (Set) target;
    }

    @Override
    public int size() {
        return target.size();
    }

    @Override
    public boolean isEmpty() {
        return target.isEmpty();
    }

    @Override
    public boolean contains(Object o) {
        return target.contains(o);
    }

    @Override
    public Iterator<Object> iterator() {
        return target.iterator();
    }

    @Override
    public Object[] toArray() {
        return target.toArray();
    }

    @Override
    public Object[] toArray(Object[] a) {
        return target.toArray(a);
    }

    public boolean add(Object t) {
        return target.add(t);
    }

    @Override
    public boolean remove(Object o) {
        return target.remove(o);
    }

    @Override
    public boolean containsAll(Collection<?> c) {
        return target.containsAll(c);
    }

    @Override
    public boolean addAll(Collection<? extends Object> c) {
        return target.addAll(c);
    }

    @Override
    public boolean retainAll(Collection<?> c) {
        return target.retainAll(c);
    }

    @Override
    public boolean removeAll(Collection<?> c) {
        return target.removeAll(c);
    }

    @Override
    public void clear() {
        target.clear();
    }

    @Override
    public boolean equals(Object o) {
        return target.equals(o);
    }

    @Override
    public int hashCode() {
        return target.hashCode();
    }

}
