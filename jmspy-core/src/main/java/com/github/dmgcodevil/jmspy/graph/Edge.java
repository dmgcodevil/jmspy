package com.github.dmgcodevil.jmspy.graph;

import com.github.dmgcodevil.jmspy.context.InvocationContextInfo;
import com.github.dmgcodevil.jmspy.proxy.JMethod;
import com.google.common.base.MoreObjects;
import com.google.common.base.Objects;

import java.io.Serializable;

/**
 * Edge is a connection between nodes and represented as java method.
 * Edge has two references for bidirectional navigation namely 'from' and 'to'.
 *
 * @author dmgcodevil
 */
public class Edge implements Serializable {

    private static final long serialVersionUID = -5509438349274267407L;
    private Node from;
    private Node to;
    private JMethod method;
    private InvocationContextInfo contextInfo;

    public Node getFrom() {
        return from;
    }

    public void setFrom(Node from) {
        this.from = from;
    }

    public Node getTo() {
        return to;
    }

    public void setTo(Node to) {
        this.to = to;
    }

    public JMethod getMethod() {
        return method;
    }

    public void setMethod(JMethod method) {
        this.method = method;
    }

    public InvocationContextInfo getContextInfo() {
        return contextInfo;
    }

    public void setContextInfo(InvocationContextInfo contextInfo) {
        this.contextInfo = contextInfo;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        Edge that = (Edge) o;

        return Objects.equal(this.from, that.from) &&
                Objects.equal(this.method, that.method) &&
                Objects.equal(this.to, that.to);
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(from, to, method);
    }

    @Override
    public String toString() {
        return MoreObjects.toStringHelper(this)
                .add("from", from)
                .add("to", to)
                .add("method", method)
                .toString();
    }
}
