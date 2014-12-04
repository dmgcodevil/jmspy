package com.github.dmgcodevil.jmspy.functional;

/**
 * Functional interface to be used in methods such as forEach.
 *
 * @author dmgcodevil
 */
public interface Consumer<T> {
    /**
     * Consumes an object of <T> type to apply any actions.
     *
     * @param input the object of <T> type
     */
    void consume(T input);
}