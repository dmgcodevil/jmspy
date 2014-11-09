package com.github.dmgcodevil.jmspy.graph;

import org.apache.commons.lang3.StringUtils;

import java.util.HashSet;
import java.util.Set;

/**
 * Created by dmgcodevil on 11/8/2014.
 */
public class Node {

    private String id;

    private Class<?> type;

    private Set<Edge> outgoingEdges = new HashSet<>();

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public Class<?> getType() {
        return type;
    }

    public void setType(Class<?> type) {
        this.type = type;
    }

    public Set<Edge> getOutgoingEdges() {
        return outgoingEdges;
    }

    public void setOutgoingEdges(Set<Edge> outgoingEdges) {
        this.outgoingEdges = outgoingEdges;
    }

    public void addOutgoingEdge(Edge edge) {
        outgoingEdges.add(edge);
    }

    public synchronized Node findById(final String _id) {
        if (_id == null) {
            return null;
        }
        if (StringUtils.equals(this.id, _id)) {
            return this;
        }
        for (Edge edge : outgoingEdges) {
            if (edge.getTo() != null) {

                Node n = edge.getTo().findById(_id);
                if (n != null) {
                    return n;
                }
            }

        }
        return null;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        Node node = (Node) o;

        if (id != null ? !id.equals(node.id) : node.id != null) return false;
        if (type != null ? !type.equals(node.type) : node.type != null) return false;

        return true;
    }

    @Override
    public int hashCode() {
        int result = id != null ? id.hashCode() : 0;
        result = 31 * result + (type != null ? type.hashCode() : 0);
        return result;
    }
}
