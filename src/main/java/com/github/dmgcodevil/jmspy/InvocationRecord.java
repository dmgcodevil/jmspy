package com.github.dmgcodevil.jmspy;

import com.github.dmgcodevil.jmspy.graph.InvocationGraph;

import java.lang.reflect.Method;

/**
 * Created by dmgcodevil on 11/8/2014.
 */
public class InvocationRecord {
    private Method root;
    private InvocationGraph invocationGraph;

    public InvocationRecord(Method root, InvocationGraph invocationGraph) {
        this.root = root;
        this.invocationGraph = invocationGraph;
    }

    public InvocationRecord(InvocationGraph invocationGraph) {
        this.invocationGraph = invocationGraph;
    }
}
