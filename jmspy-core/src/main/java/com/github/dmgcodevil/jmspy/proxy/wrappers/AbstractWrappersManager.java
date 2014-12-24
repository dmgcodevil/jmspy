package com.github.dmgcodevil.jmspy.proxy.wrappers;

import com.google.common.collect.Maps;

import java.util.Iterator;
import java.util.ListIterator;
import java.util.Map;

/**
 * Created by dmgcodevil on 12/24/2014.
 */
public abstract class AbstractWrappersManager implements WrappersManager {
    private Map<Class<?>, Wrapper> wrappers = Maps.newLinkedHashMap();

    @Override
    public Map<Class<?>, Wrapper> getWrappers() {
        return wrappers;
    }

    @Override
    public void initWrappers() throws ClassNotFoundException {

        wrappers.put(Iterator.class, new IteratorWrapper());
        wrappers.put(ListIterator.class, new ListIteratorWrapper());
        wrappers.put(Map.Entry.class, new EntryWrapper());
        wrappers.put(Class.forName("java.util.HashMap$EntrySet"), new EntrySetWrapper());
        wrappers.put(Class.forName("java.util.HashMap$Values"), new MapValuesWrapper());
        wrappers.put(Class.forName("java.util.HashMap$KeySet"), new MapKeySetWrapper());
        wrappers.put(Class.forName("java.util.HashMap$KeyIterator"), new IteratorWrapper());
        // wrappers.put(Class.forName("java.util.LinkedHashMap$KeyIterator"), new IteratorWrapper()); // java 7
        //wrappers.put(Class.forName("java.util.LinkedHashMap$LinkedKeyIterator"), new IteratorWrapper()); // java 8

        wrappers.put(Class.forName("java.util.ArrayList$Itr"), new IteratorWrapper());
        wrappers.put(Class.forName("java.util.ArrayList$ListItr"), new ListIteratorWrapper());

        wrappers.put(Class.forName("java.util.Collections$UnmodifiableMap"), new MapWrapper());
        wrappers.put(Class.forName("java.util.Collections$UnmodifiableSet"), new SetWrapper());
        wrappers.put(Class.forName("java.util.Collections$UnmodifiableRandomAccessList"), new ListWrapper());
        wrappers.put(Class.forName("java.util.Collections$UnmodifiableList"), new ListWrapper());
        wrappers.put(Class.forName("java.util.Collections$UnmodifiableCollection"), new CollectionWrapper());
    }

    @Override
    public void registerWrapper(Class<?> aClass, Wrapper wrapper) {
        wrappers.put(aClass, wrapper);
    }

    @Override
    public void registerWrapper(String className, Wrapper wrapper) throws ClassNotFoundException {
        wrappers.put(Class.forName(className), wrapper);
    }
}
