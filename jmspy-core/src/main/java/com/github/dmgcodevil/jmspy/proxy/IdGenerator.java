package com.github.dmgcodevil.jmspy.proxy;

import java.util.concurrent.atomic.AtomicInteger;

/**
 * // todo
 *
 * @author  dmgcodevil
 */
public class IdGenerator {

    private static final IdGenerator ID_GENERATOR = new IdGenerator();

    private AtomicInteger atomicInteger = new AtomicInteger(0);

    public int generate(){
      return atomicInteger.incrementAndGet();
    }

    public static IdGenerator getInstance() {
        return ID_GENERATOR;
    }
}
