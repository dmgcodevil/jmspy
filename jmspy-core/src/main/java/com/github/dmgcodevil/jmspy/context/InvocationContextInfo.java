package com.github.dmgcodevil.jmspy.context;

import java.io.Serializable;
import java.util.Map;

/**
 * Additional information about execution environment, such as app name, url and etc.
 *
 * @author dmgcodevil
 */
public class InvocationContextInfo implements Serializable {

    private static final long serialVersionUID = -2590916494625547978L;

    private String info;
    private Map<String, String> details;

    public String getInfo() {
        return info;
    }

    public void setInfo(String info) {
        this.info = info;
    }

    public Map<String, String> getDetails() {
        return details;
    }

    public void setDetails(Map<String, String> details) {
        this.details = details;
    }
}
