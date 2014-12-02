package com.github.dmgcodevil.jmspy.functional;

/**
 * Functional interface is used to create an objects.
 *
 * @author Raman_Pliashkou
 */
public interface Producer<T> {

    /**
     * Creates new object with type <T>.
     *
     * @return new object with type <T>.
     */
    T produce();
}
