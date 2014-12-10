package com.github.dmgcodevil.jmspy.exception;

/**
 * Exception is used to indicate any issues during proxy factory configuration.
 *
 * @author  dmgcodevil
 */
public class ConfigurationException extends RuntimeException {

    public ConfigurationException() {
    }

    public ConfigurationException(String message) {
        super(message);
    }

    public ConfigurationException(String message, Throwable cause) {
        super(message, cause);
    }

    public ConfigurationException(Throwable cause) {
        super(cause);
    }
}
