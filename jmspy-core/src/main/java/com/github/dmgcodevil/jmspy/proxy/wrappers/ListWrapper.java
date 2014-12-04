package com.github.dmgcodevil.jmspy.proxy.wrappers;

import java.util.Collection;
import java.util.List;
import java.util.ListIterator;

/**
 * Concrete implementation of {@link List} interface.
 *
 * @author dmgcodevil
 */
public class ListWrapper extends AbstractCollectionWrapper<List> implements List {

    public ListWrapper() {
    }

    public ListWrapper(List target) {
        super(target);
    }

    @Override
    public Wrapper create(List target) {
        return new ListWrapper(target);
    }

    @Override
    public Class<? extends Wrapper<List>> getType() {
        return ListWrapper.class;
    }

    @Override
    public boolean addAll(int index, Collection c) {
        return target.addAll(index, c);
    }

    @Override
    public Object get(int index) {
        return target.get(index);
    }

    @Override
    public Object set(int index, Object element) {
        return target.set(index, element);
    }

    @Override
    public void add(int index, Object element) {
        target.add(index, element);
    }

    @Override
    public Object remove(int index) {
        return target.remove(index);
    }

    @Override
    public int indexOf(Object o) {
        return target.indexOf(o);
    }

    @Override
    public int lastIndexOf(Object o) {
        return target.lastIndexOf(o);
    }

    @Override
    public ListIterator listIterator() {
        return target.listIterator();
    }

    @Override
    public ListIterator listIterator(int index) {
        return target.listIterator(index);
    }

    @Override
    public List subList(int fromIndex, int toIndex) {
        return target.subList(fromIndex, toIndex);
    }
}
