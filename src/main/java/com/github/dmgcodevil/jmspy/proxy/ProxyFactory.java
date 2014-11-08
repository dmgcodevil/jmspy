package com.github.dmgcodevil.jmspy.proxy;

import com.github.dmgcodevil.jmspy.graph.InvocationGraph;
import com.github.dmgcodevil.jmspy.proxy.wrappers.EntrySetWrapper;
import com.github.dmgcodevil.jmspy.proxy.wrappers.EntryWrapper;
import com.github.dmgcodevil.jmspy.proxy.wrappers.IteratorWrapper;
import com.github.dmgcodevil.jmspy.proxy.wrappers.Wrapper;
import com.google.common.base.Throwables;

import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

/**
 * Created by dmgcodevil on 11/7/2014.
 */
public class ProxyFactory {


    private InvocationGraph invocationGraph;

    private Map<Class<?>, Wrapper> wrappers = new HashMap<>();

    public ProxyFactory() {
        wrappers.put(Iterator.class, new IteratorWrapper());
        try {
            wrappers.put(Class.forName("java.util.HashMap$EntrySet"), new EntrySetWrapper());
            wrappers.put(Map.Entry.class, new EntryWrapper());
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }
    }

    public ProxyFactory(InvocationGraph invocationGraph) {
        this();
        this.invocationGraph = invocationGraph;
    }

    public Object create(Object target) {
        return create(target, new Type(target.getClass()));
    }

    public Object create(Object target, Type type) {
        try {
            return ProxyCreatorFactory.create(type.getTarget(), invocationGraph, wrappers).create(target, type);
        } catch (Throwable throwable) {
            throw Throwables.propagate(throwable);
        }
    }
}
