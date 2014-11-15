package com.github.dmgcodevil.jmspy.proxy.wrappers;

import com.github.dmgcodevil.jmspy.proxy.NotProxy;

import java.util.Collection;
import java.util.Iterator;

/**
 * Created by dmgcodevil on 11/14/2014.
 */
public abstract class AbstractCollectionWrapper implements Collection<Object>, Wrapper {

    @NotProxy
    private Collection source;

    public AbstractCollectionWrapper() {
    }

    public AbstractCollectionWrapper(Collection source) {
        this.source = source;
    }

    @Override
    public void setTarget(Object target) {
        source = (Collection)target;
    }

    @Override
    public Object getTarget() {
        return source;
    }

    @Override
    public int size() {
        return source.size();
    }

    @Override
    public boolean isEmpty() {
        return source.isEmpty();
    }

    @Override
    public boolean contains(Object o) {
        return source.contains(o);
    }

    @Override
    public Iterator<Object> iterator() {
        return source.iterator();
    }

    @Override
    public Object[] toArray() {
        return source.toArray();
    }

    @Override
    public Object[] toArray(Object[] a) {
        return source.toArray(a);
    }

    @Override
    public boolean add(Object o) {
        return source.add(o);
    }

    @Override
    public boolean remove(Object o) {
        return source.remove(o);
    }

    @Override
    public boolean containsAll(Collection<?> c) {
        return source.containsAll(c);
    }

    @Override
    public boolean addAll(Collection<?> c) {
        return source.addAll(c);
    }

    @Override
    public boolean removeAll(Collection<?> c) {
        return source.retainAll(c);
    }

    @Override
    public boolean retainAll(Collection<?> c) {
        return source.retainAll(c);
    }

    @Override
    public void clear() {
        source.clear();
    }

}