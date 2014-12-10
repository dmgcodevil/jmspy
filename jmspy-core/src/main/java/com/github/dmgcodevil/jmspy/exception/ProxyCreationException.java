package com.github.dmgcodevil.jmspy.exception;

/**
 * // todo
 *
 * @author  dmgcodevil
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
