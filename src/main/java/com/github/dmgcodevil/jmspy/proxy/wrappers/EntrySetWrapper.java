package com.github.dmgcodevil.jmspy.proxy.wrappers;

import com.github.dmgcodevil.jmspy.proxy.NotProxy;

import java.util.Collection;
import java.util.Iterator;
import java.util.Set;

/**
 * Created by dmgcodevil on 11/8/2014.
 */
public class EntrySetWrapper implements Set<Object>, Wrapper {
    @NotProxy
    private Set<Object> set;

    public EntrySetWrapper() {
    }

    public EntrySetWrapper(Set set) {
        this.set = set;
    }

    public Set getSet() {
        return set;
    }

    public void setSet(Set set) {
        this.set = set;
    }

    @Override
    public int size() {
        return set.size();
    }

    @Override
    public boolean isEmpty() {
        return set.isEmpty();
    }

    @Override
    public boolean contains(Object o) {
        return set.contains(o);
    }

    @Override
    public Iterator<Object> iterator() {
        return set.iterator();
    }

    @Override
    public Object[] toArray() {
        return set.toArray();
    }

    @Override
    public  Object[] toArray(Object[] a) {
        return set.toArray(a);
    }

    public boolean add(Object t) {
        return set.add(t);
    }

    @Override
    public boolean remove(Object o) {
        return set.remove(o);
    }

    @Override
    public boolean containsAll(Collection<?> c) {
        return set.containsAll(c);
    }

    @Override
    public boolean addAll(Collection<? extends Object> c) {
        return set.addAll(c);
    }

    @Override
    public boolean retainAll(Collection<?> c) {
        return set.retainAll(c);
    }

    @Override
    public boolean removeAll(Collection<?> c) {
        return set.removeAll(c);
    }

    @Override
    public void clear() {
        set.clear();
    }

    @Override
    public boolean equals(Object o) {
        return set.equals(o);
    }

    @Override
    public int hashCode() {
        return set.hashCode();
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
