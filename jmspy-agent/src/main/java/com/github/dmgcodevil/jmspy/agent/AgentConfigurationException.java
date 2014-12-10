package com.github.dmgcodevil.jmspy.agent;

/**
 * Exception indicates that any errors occurred during building {@link com.github.dmgcodevil.jmspy.agent.JmspyAgentConfig}.
 *
 * @author dmgcodevil
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
