package com.github.dmgcodevil.jmspy.proxy.wrappers;

import com.github.dmgcodevil.jmspy.proxy.NotProxy;

import java.util.ListIterator;

/**
 * Wrapper for {@link ListIterator}.
 *
 * @author dmgcodevil
 */
public class ListIteratorWrapper implements Wrapper<ListIterator>, ListIterator {

    @NotProxy
    private ListIterator target;

    public ListIteratorWrapper() {
    }

    public ListIteratorWrapper(ListIterator target) {
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
    public boolean hasPrevious() {
        return target.hasPrevious();
    }

    @Override
    public Object previous() {
        return target.previous();
    }

    @Override
    public int nextIndex() {
        return target.nextIndex();
    }

    @Override
    public int previousIndex() {
        return target.previousIndex();
    }

    @Override
    public void remove() {
        target.remove();
    }

    @Override
    public void set(Object o) {
        target.set(o);
    }

    @Override
    public void add(Object o) {
        target.add(o);
    }

    @Override
    public Wrapper create(ListIterator target) {
        return new ListIteratorWrapper(target);
    }

    @Override
    public void setTarget(ListIterator target) {
        this.target = target;
    }

    @Override
    public ListIterator getTarget() {
        return target;
    }

    @Override
    public Class<? extends Wrapper<ListIterator>> getType() {
        return ListIteratorWrapper.class;
    }
}
