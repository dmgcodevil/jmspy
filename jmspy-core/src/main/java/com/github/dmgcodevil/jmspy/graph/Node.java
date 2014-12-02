package com.github.dmgcodevil.jmspy.graph;

import com.github.dmgcodevil.jmspy.proxy.JMethod;
import com.github.dmgcodevil.jmspy.proxy.JType;
import com.google.common.base.Predicate;
import com.google.common.base.Verify;
import com.google.common.collect.Iterables;
import org.apache.commons.lang3.StringUtils;

import java.io.Serializable;
import java.util.HashSet;
import java.util.Set;

/**
 * Created by dmgcodevil on 11/8/2014.
 */
public class Node implements Serializable {


    private static final long serialVersionUID = 5483570738425483697L;

    private String id;

    private JType type;

    private Set<Edge> outgoingEdges = new HashSet<>();

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public JType getType() {
        return type;
    }

    public void setType(JType type) {
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

    public synchronized Edge getEdge(Node parent, final JMethod jMethod) {
        Verify.verifyNotNull(jMethod, "jMethod cannot be null");
        return Iterables.tryFind(parent.getOutgoingEdges(), new Predicate<Edge>() {
            @Override
            public boolean apply(Edge input) {
                return input != null && input.getMethod().equals(jMethod);
            }
        }).orNull();
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
