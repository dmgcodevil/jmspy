package com.github.dmgcodevil.jmspy.proxy.wrappers;

/**
 * Created by dmgcodevil on 12/24/2014.
 */
public class Jdk8WrappersManager extends AbstractWrappersManager implements WrappersManager {

    @Override
    public void initWrappers() throws ClassNotFoundException {
        super.initWrappers();
        registerWrapper("java.util.LinkedHashMap$LinkedKeyIterator", new IteratorWrapper());
    }
}