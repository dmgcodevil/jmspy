package com.github.dmgcodevil.jmspy.graph;

import com.google.common.base.Predicate;
import com.google.common.collect.Iterables;
import org.apache.commons.lang3.StringUtils;

import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

/**
 * Created by dmgcodevil on 11/8/2014.
 */
public class Node {
    private String id;

    private Class<?> type;

    private List<Node> subNodes = new ArrayList<>();
    private Set<Method> methods = new HashSet<>();

    // private List<Edge> incomingEdges = new ArrayList<>();

    private List<Edge> outgoingEdges = new ArrayList<>();

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

    //    public List<Edge> getIncomingEdges() {
//        return incomingEdges;
//    }
//
//    public void setIncomingEdges(List<Edge> incomingEdges) {
//        this.incomingEdges = incomingEdges;
//    }
//
    public List<Edge> getOutgoingEdges() {
        return outgoingEdges;
    }

    public void setOutgoingEdges(List<Edge> outgoingEdges) {
        this.outgoingEdges = outgoingEdges;
    }

    public void addOutgoingEdge(Edge edge) {
        outgoingEdges.add(edge);
    }

    @Deprecated
    public List<Node> getSubNodes() {
        return subNodes;
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

    //    public Node findById(final String id) {
//        if (id == null) {
//            return null;
//        }
//        if (StringUtils.equals(this.id, id)) {
//            return this;
//        }
//        for (Node node : subNodes) {
//            if (id.equals(node.getId())) {
//                return node;
//            } else {
//                return node.findById(id);
//            }
//        }
//        return null;
//    }
    @Deprecated
    public void addMethod(Method method) {
        methods.add(method);
    }
}