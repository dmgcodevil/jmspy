package com.github.dmgcodevil.jmspy.graph;

import static com.github.dmgcodevil.jmspy.proxy.CommonUtils.createIdentifier;

/**
 * Created by dmgcodevil on 11/8/2014.
 */
public class InvocationGraph {

    private Node root;

    public InvocationGraph(Node root) {
        this.root = root;
    }

    public static InvocationGraph create(Object target) {
       ///String id = createIdentifier();
        Node r = new Node();
        r.setType(target.getClass());
        ///r.setId(id);
        return new InvocationGraph(r);
    }

    public Node getRoot() {
        return root;
    }

    public synchronized Node findById(final String id) {
        if (id == null) {
            return null;
        }
       return root.findById(id);

    }

}
