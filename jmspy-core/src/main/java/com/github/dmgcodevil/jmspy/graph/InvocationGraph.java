package com.github.dmgcodevil.jmspy.graph;

import com.github.dmgcodevil.jmspy.proxy.JType;
import com.google.common.base.Throwables;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.Serializable;
import java.util.UUID;

/**
 * Created by dmgcodevil on 11/8/2014.
 */
public class InvocationGraph implements Serializable {


    private static final long serialVersionUID = -1634474286639697525L;
    private Node root;
    private final transient String id;

    public InvocationGraph(Node root) {
        this.root = root;
        id = UUID.randomUUID().toString();
    }

    public String getId() {
        return id;
    }

    public static InvocationGraph create(Object target) {
        ///String id = createIdentifier();
        Node r = new Node();
        r.setType(new JType(target.getClass()));
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

    /**
     * Saves invocationGraph to a file with the given fileName.
     *
     * @param invocationGraph the current invocation graph
     * @param fileName        the name of file
     * @deprecated see {@link com.github.dmgcodevil.jmspy.Snapshot#save(com.github.dmgcodevil.jmspy.Snapshot)}
     */
    @Deprecated
    public static void save(InvocationGraph invocationGraph, String fileName) {
        FileOutputStream fout = null;
        try {
            fout = new FileOutputStream(fileName);
            ObjectOutputStream oos = new ObjectOutputStream(fout);
            oos.writeObject(invocationGraph);
        } catch (IOException e) {
            throw Throwables.propagate(e);
        }

    }

    /**
     * Loads invocationGraph from the given file.
     *
     * @param file the file to load invocationGraph
     * @return {@link InvocationGraph}
     * @deprecated see {@link com.github.dmgcodevil.jmspy.Snapshot#load(java.io.File)}
     */
    @Deprecated
    public static InvocationGraph load(File file) {
        FileInputStream fin = null;
        try {
            fin = new FileInputStream(file);
            ObjectInputStream objectInputStream = new ObjectInputStream(fin);
            return (InvocationGraph) objectInputStream.readObject();
        } catch (IOException | ClassNotFoundException e) {
            throw Throwables.propagate(e);
        }
    }

}
