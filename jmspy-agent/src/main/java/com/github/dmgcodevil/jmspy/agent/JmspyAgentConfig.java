package com.github.dmgcodevil.jmspy.agent;

import java.io.IOException;
import java.io.InputStream;
import java.util.Collections;
import java.util.HashSet;
import java.util.Properties;
import java.util.Set;

import static org.apache.commons.lang3.StringUtils.isNotBlank;

/**
 * Config for {@link com.github.dmgcodevil.jmspy.agent.Agent}.
 *
 * @author dmgcodevil
 */
public class JmspyAgentConfig {

    private static final String INSTRUMENTED_RESOURCES_PROP = "instrumentedResources";
    private static final String EMPTY = "";
    private static final String CLASS_SUFFIX = "/class";
    private static final String JMSPY_AGENT_PROPERTIES = "jmspy_agent.properties";
    private Set<String> instrumentedPackages = Collections.emptySet();
    private Set<String> instrumentedClasses = Collections.emptySet();

    public JmspyAgentConfig(String agentArgs) {
        if (isNotBlank(agentArgs)) {
            init(agentArgs);
        } else if (isNotBlank(agentArgs = loadFromPropertyFile())) {
            init(agentArgs);
        }
    }

    public Set<String> getInstrumentedPackages() {
        return Collections.unmodifiableSet(instrumentedPackages);
    }

    public Set<String> getInstrumentedClasses() {
        return Collections.unmodifiableSet(instrumentedClasses);
    }

    private String loadFromPropertyFile() {
        String result = EMPTY;
        Properties prop = new Properties();
        String propFileName = JMSPY_AGENT_PROPERTIES;

        InputStream inputStream = getClass().getClassLoader().getResourceAsStream(propFileName);

        if (inputStream != null) {
            try {
                prop.load(inputStream);
                result = prop.getProperty(INSTRUMENTED_RESOURCES_PROP);
            } catch (IOException e) {
                throw new AgentConfigurationException(e);
            }
        }
        return result;
    }

    private void init(String data) {
        if (isNotBlank(data)) {
            data = data.replace(".", "/");
            data = data.replaceAll("\\s", EMPTY); // remove whitespaces
            String[] elements = data.split(",");
            if (elements.length > 0) {
                instrumentedPackages = new HashSet<>();
                instrumentedClasses = new HashSet<>();
                for (String el : elements) {
                    if (el.endsWith(CLASS_SUFFIX)) {
                        instrumentedClasses.add(el.replace(CLASS_SUFFIX, EMPTY));
                    } else {
                        instrumentedPackages.add(el);
                    }
                }
            }
        }
    }

}
