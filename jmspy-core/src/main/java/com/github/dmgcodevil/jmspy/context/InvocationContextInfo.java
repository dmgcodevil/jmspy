package com.github.dmgcodevil.jmspy.context;

import java.io.Serializable;

/**
 * Additional information about execution environment, such as app name, url and etc.
 *
 * @author dmgcodevil
 */
public class InvocationContextInfo implements Serializable {

    private static final long serialVersionUID = -2590916494625547978L;

    private String description;

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }
}
