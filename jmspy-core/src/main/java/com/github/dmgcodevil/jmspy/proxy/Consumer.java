package com.github.dmgcodevil.jmspy.proxy;

/**
 * Functional interface to be used in methods such as forEach.
 *
 * @author Raman_Pliashkou
 */
interface Consumer<T> {
    void consume(T input);
}