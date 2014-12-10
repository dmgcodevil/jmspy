package com.github.dmgcodevil.jmspy.agent;

/**
 * Created by dmgcodevil on 12/10/2014.
 */
public class AgentConfigurationException extends RuntimeException {
    public AgentConfigurationException() {
    }

    public AgentConfigurationException(String message) {
        super(message);
    }

    public AgentConfigurationException(String message, Throwable cause) {
        super(message, cause);
    }

    public AgentConfigurationException(Throwable cause) {
        super(cause);
    }
}
