package com.github.dmgcodevil.jmspy;

import com.github.dmgcodevil.jmspy.context.InvocationContext;
import com.github.dmgcodevil.jmspy.graph.InvocationGraph;

import java.io.Serializable;
import java.util.Date;
import java.util.UUID;

/**
 * Represent invocation record including invocation graph.
 *
 * @author dmgcodevil
 */
public class InvocationRecord implements Serializable {


    private static final long serialVersionUID = 17935977835053980L;
    private String id = UUID.randomUUID().toString();
    private InvocationContext invocationContext;
    private InvocationGraph invocationGraph;
    private Date created = new Date();

    public InvocationRecord() {
    }

    public InvocationRecord(InvocationContext invocationContext, InvocationGraph invocationGraph) {
        this.invocationContext = invocationContext;
        this.invocationGraph = invocationGraph;
    }

    public InvocationRecord(InvocationGraph invocationGraph) {
        this.invocationGraph = invocationGraph;
    }

    public InvocationContext getInvocationContext() {
        return invocationContext;
    }

    public Date getCreated() {
        return created;
    }

    public InvocationGraph getInvocationGraph() {
        return invocationGraph;
    }

    public String getId() {
        return id;
    }
}
