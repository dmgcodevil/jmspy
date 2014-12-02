package com.github.dmgcodevil.jmspy.context;

import java.io.Serializable;

/**
 * Class description.
 *
 * @author Raman_Pliashkou
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
