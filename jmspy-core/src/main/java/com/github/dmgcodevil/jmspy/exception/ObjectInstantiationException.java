package com.github.dmgcodevil.jmspy.exception;

/**
 * Created by dmgcodevil on 12/8/2014.
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
