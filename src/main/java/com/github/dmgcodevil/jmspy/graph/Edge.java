package com.github.dmgcodevil.jmspy.graph;

import java.lang.reflect.Method;

/**
 * Created by dmgcodevil on 11/8/2014.
 */
public class Edge {


    Node from;
    Node to;
    Method method;

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

    public Method getMethod() {
        return method;
    }

    public void setMethod(Method method) {
        this.method = method;
    }


}
