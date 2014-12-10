package com.github.dmgcodevil.jmspy.exception;

/**
 * // todo
 *
 * @author  dmgcodevil
 */
public class ObjectInstantiationException extends RuntimeException {

    public ObjectInstantiationException() {
    }

    public ObjectInstantiationException(String message) {
        super(message);
    }

    public ObjectInstantiationException(String message, Throwable cause) {
        super(message, cause);
    }

    public ObjectInstantiationException(Throwable cause) {
        super(cause);
    }
}
