package com.github.dmgcodevil.jmspy.proxy;

import java.util.concurrent.atomic.AtomicInteger;

/**
 * Created by dmgcodevil on 11/8/2014.
 */
public class IdGenerator {

    private static final IdGenerator ID_GENERATOR = new IdGenerator();

    private AtomicInteger atomicInteger = new AtomicInteger(0);

    public int generate(){
      return   atomicInteger.incrementAndGet();
    }

    public static IdGenerator getInstance() {
        return ID_GENERATOR;
    }
}
