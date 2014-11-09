package com.github.dmgcodevil.jmspy;

import com.github.dmgcodevil.jmspy.graph.InvocationGraph;
import com.github.dmgcodevil.jmspy.proxy.ProxyFactory;

import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.List;

import static com.github.dmgcodevil.jmspy.proxy.CommonUtils.isNotPrimitiveOrWrapper;

/**
 * Created by dmgcodevil on 11/8/2014.
 */
public class MethodInvocationRecorder {


    private List<InvocationRecord> invocationRecords = new ArrayList<>();

    private InvocationContext invocationContext;

    public List<InvocationRecord> getInvocationRecords() {
        return invocationRecords;
    }

    public InvocationContext getInvocationContext() {
        return invocationContext;
    }

    public Object record(Object target) {
        return record(null, target);
    }

    public Object record(Method method, Object target) {
        if (isNotPrimitiveOrWrapper(target)) {
            InvocationGraph invocationGraph = InvocationGraph.create(target);
            Object proxy = new ProxyFactory(invocationGraph).create(target);
            InvocationRecord invocationRecord = new InvocationRecord(method, invocationGraph);
            invocationRecords.add(invocationRecord);
            return proxy;
        } else {
            return target;
        }
    }
}
