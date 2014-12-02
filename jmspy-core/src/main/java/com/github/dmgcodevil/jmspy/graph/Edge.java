package com.github.dmgcodevil.jmspy.graph;

import com.github.dmgcodevil.jmspy.proxy.JMethod;

import java.io.Serializable;

/**
 * Created by dmgcodevil on 11/8/2014.
 */
public class Edge implements Serializable {

    private static final long serialVersionUID = -5509438349274267407L;
    private Node from;
    private Node to; // todo change it to collection, because method can return same objects but with different used fields
    private JMethod method;

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


    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        Edge edge = (Edge) o;

        if (from != null ? !from.equals(edge.from) : edge.from != null) return false;
        if (method != null ? !method.equals(edge.method) : edge.method != null) return false;
        if (to != null ? !to.equals(edge.to) : edge.to != null) return false;

        return true;
    }

    @Override
    public int hashCode() {
        int result = from != null ? from.hashCode() : 0;
        result = 31 * result + (to != null ? to.hashCode() : 0);
        result = 31 * result + (method != null ? method.hashCode() : 0);
        return result;
    }

}
