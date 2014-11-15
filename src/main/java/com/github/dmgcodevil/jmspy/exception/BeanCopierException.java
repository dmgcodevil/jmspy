package com.github.dmgcodevil.jmspy.exception;

/**
 * Created by dmgcodevil on 11/14/2014.
 */
public class BeanCopierException extends RuntimeException {

    public BeanCopierException() {
    }

    public BeanCopierException(String message) {
        super(message);
    }

    public BeanCopierException(Throwable cause) {
        super(cause);
    }

    public BeanCopierException(String message, Throwable cause) {
        super(message, cause);
    }
}
