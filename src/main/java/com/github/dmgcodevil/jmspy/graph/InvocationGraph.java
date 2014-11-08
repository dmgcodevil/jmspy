package com.github.dmgcodevil.jmspy.graph;

import com.google.common.base.Predicate;
import com.google.common.collect.Iterables;
import org.apache.commons.lang3.StringUtils;

import java.util.ArrayList;
import java.util.List;

import static com.github.dmgcodevil.jmspy.proxy.CommonUtils.createIdentifier;

/**
 * Created by dmgcodevil on 11/8/2014.
 */
public class InvocationGraph {

    private Node root;
   // private List<Node> nodes = new ArrayList<>();

    public static InvocationGraph create(Object target) {
        String id = createIdentifier();
        Node r = new Node();
        r.setType(target.getClass());
        r.setId(id);
        return new InvocationGraph(r);
    }

    public InvocationGraph(Node root) {
        this.root = root;
    }

    public Node getRoot() {
        return root;
    }

    public Node findById(final String id) {
        if (id == null) {
            return null;
        }
       return root.findById(id);

    }

}
