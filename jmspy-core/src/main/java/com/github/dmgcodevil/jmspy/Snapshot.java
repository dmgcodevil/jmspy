package com.github.dmgcodevil.jmspy;

import com.google.common.base.Throwables;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.Serializable;
import java.text.SimpleDateFormat;
import java.util.Collections;
import java.util.Date;
import java.util.List;

import static org.slf4j.helpers.MessageFormatter.format;

/**
 * Represents a snapshot of invocations.
 *
 * @author dmgcodevil
 */
public class Snapshot implements Serializable {


    private static final long serialVersionUID = -651468933350929861L;
    private List<InvocationRecord> invocationRecords = Collections.emptyList();
    private static final String PREFIX = "snapshot_{}.jmspy";

    public Snapshot() {
    }

    public Snapshot(List<InvocationRecord> invocationRecords) {
        this.invocationRecords = invocationRecords;
    }

    public List<InvocationRecord> getInvocationRecords() {
        return invocationRecords;
    }

    public static Snapshot save(Snapshot snapshot) {
        return save(snapshot, generateName());
    }

    public static Snapshot save(Snapshot snapshot, String fileName) {
        try (
                FileOutputStream fout = new FileOutputStream(fileName);
                ObjectOutputStream oos = new ObjectOutputStream(fout)
        ) {
            oos.writeObject(snapshot);
            return snapshot;
        } catch (IOException e) {
            throw Throwables.propagate(e);
        }

    }

    public static Snapshot load(File file) {
        try (
                FileInputStream fin = new FileInputStream(file);
                ObjectInputStream objectInputStream = new ObjectInputStream(fin)
        ) {
            return (Snapshot) objectInputStream.readObject();
        } catch (IOException | ClassNotFoundException e) {
            throw Throwables.propagate(e);
        }
    }

    private static String generateName() {
        Date date = new Date();
        SimpleDateFormat dt = new SimpleDateFormat("yyyy-MM-dd_hh-mm-ss");
        return format(PREFIX, dt.format(date)).getMessage();
    }

}
