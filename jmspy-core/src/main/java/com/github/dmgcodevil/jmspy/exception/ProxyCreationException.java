package com.github.dmgcodevil.jmspy.exception;

/**
 * Created by dmgcodevil on 11/12/2014.
 */
public class ProxyCreationException extends RuntimeException {

    public ProxyCreationException(String message) {
        super(message);
    }

    public ProxyCreationException(Throwable cause) {
        super(cause);
    }

    public ProxyCreationException(String message, Throwable cause) {
        super(message, cause);
    }
}
