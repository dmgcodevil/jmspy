package com.github.dmgcodevil.jmspy.proxy.wrappers;

import com.github.dmgcodevil.jmspy.proxy.NotProxy;

import java.util.Collection;
import java.util.Iterator;

/**
 * Basic implementation of {@link Collection} interface.
 * Created by dmgcodevil on 11/14/2014.
 */
public abstract class AbstractCollectionWrapper implements Collection<Object>, Wrapper {

    @NotProxy
    private Collection<Object> target;

    public AbstractCollectionWrapper() {
    }

    public AbstractCollectionWrapper(Collection target) {
        this.target = target;
    }

    @Override
    public void setTarget(Object target) {
        this.target = (Collection) target;
    }

    @Override
    public Object getTarget() {
        return target;
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

    @Override
    public boolean add(Object o) {
        return target.add(o);
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
    public boolean addAll(Collection<?> c) {
        return target.addAll(c);
    }

    @Override
    public boolean removeAll(Collection<?> c) {
        return target.retainAll(c);
    }

    @Override
    public boolean retainAll(Collection<?> c) {
        return target.retainAll(c);
    }

    @Override
    public void clear() {
        target.clear();
    }

}